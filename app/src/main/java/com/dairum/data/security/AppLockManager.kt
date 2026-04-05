package com.dairum.data.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.dairum.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLockManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository
) {
    
    /**
     * Check if biometric authentication is available on this device
     * Checks for both BIOMETRIC_STRONG and BIOMETRIC_WEAK for wider device compatibility
     */
    fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        // First try BIOMETRIC_STRONG, then fall back to BIOMETRIC_WEAK
        val strongResult = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        val weakResult = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        
        return strongResult == BiometricManager.BIOMETRIC_SUCCESS ||
               weakResult == BiometricManager.BIOMETRIC_SUCCESS
    }
    
    /**
     * Check if strong biometric is available (fingerprint, face ID with proper depth sensor)
     */
    fun isStrongBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == 
               BiometricManager.BIOMETRIC_SUCCESS
    }
    
    /**
     * Check if biometric hardware exists (even if not enrolled)
     */
    fun hasBiometricHardware(): Boolean {
        val biometricManager = BiometricManager.from(context)
        val strongResult = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        val weakResult = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        
        return strongResult == BiometricManager.BIOMETRIC_SUCCESS ||
               strongResult == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ||
               weakResult == BiometricManager.BIOMETRIC_SUCCESS ||
               weakResult == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
    }
    
    /**
     * Flow to observe if app lock is enabled
     */
    fun isAppLockEnabled(): Flow<Boolean> = settingsRepository.getAppLockEnabledFlow()
    
    /**
     * Flow to observe if biometric is enabled for app lock
     */
    fun isBiometricEnabled(): Flow<Boolean> = settingsRepository.getBiometricEnabled()
    
    /**
     * Check if a PIN has been set up
     */
    fun hasPinSetup(): Flow<Boolean> = settingsRepository.getPinHash().map { it != null }
    
    /**
     * Check if app lock is configured (enabled and PIN/biometric is set up)
     */
    fun isLockConfigured(): Flow<Boolean> = combine(
        settingsRepository.getAppLockEnabledFlow(),
        settingsRepository.getPinHash()
    ) { enabled, pinHash ->
        enabled && pinHash != null
    }
    
    /**
     * Set up or change the PIN
     */
    suspend fun setPin(pin: String) {
        val hashedPin = hashPin(pin)
        settingsRepository.setPinHash(hashedPin)
    }
    
    /**
     * Verify if the provided PIN matches the stored hash
     */
    suspend fun verifyPin(pin: String): Boolean {
        val storedHash = settingsRepository.getPinHash().first() ?: return false
        val inputHash = hashPin(pin)
        return storedHash == inputHash
    }
    
    /**
     * Remove the PIN
     */
    suspend fun removePin() {
        settingsRepository.setPinHash(null)
    }
    
    /**
     * Enable or disable app lock
     */
    suspend fun setAppLockEnabled(enabled: Boolean) {
        settingsRepository.setAppLockEnabled(enabled)
    }
    
    /**
     * Enable or disable biometric authentication
     */
    suspend fun setBiometricEnabled(enabled: Boolean) {
        settingsRepository.setBiometricEnabled(enabled)
    }
    
    /**
     * Hash a PIN using SHA-256
     */
    private fun hashPin(pin: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(pin.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
    
    /**
     * Create a BiometricPrompt for authentication
     */
    fun createBiometricPrompt(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(context)
        return BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Don't treat user cancellation as an error that should show a PIN option
                    if (errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                        errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        onFailed()
                    } else {
                        onError(errString.toString())
                    }
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }
        )
    }
    
    /**
     * Create prompt info for biometric dialog
     */
    fun createPromptInfo(
        title: String = "Unlock Dairum",
        subtitle: String = "Use your fingerprint to unlock",
        negativeButtonText: String = "Use PIN"
    ): BiometricPrompt.PromptInfo {
        // Use BIOMETRIC_STRONG if available, otherwise fall back to BIOMETRIC_WEAK
        val authenticators = if (isStrongBiometricAvailable()) {
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        } else {
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        }
        
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
            .setAllowedAuthenticators(authenticators)
            .build()
    }
}
