package com.dairum.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.security.AppLockManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecuritySettingsViewModel @Inject constructor(
    private val appLockManager: AppLockManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SecuritySettingsUiState())
    val uiState: StateFlow<SecuritySettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                appLockManager.isAppLockEnabled(),
                appLockManager.isBiometricEnabled(),
                appLockManager.hasPinSetup()
            ) { appLockEnabled, biometricEnabled, hasPinSetup ->
                SecuritySettingsUiState(
                    appLockEnabled = appLockEnabled,
                    biometricEnabled = biometricEnabled,
                    hasPinSetup = hasPinSetup,
                    isBiometricAvailable = appLockManager.isBiometricAvailable(),
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    fun setAppLockEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appLockManager.setAppLockEnabled(enabled)
        }
    }
    
    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appLockManager.setBiometricEnabled(enabled)
        }
    }
    
    fun setupPin(pin: String) {
        viewModelScope.launch {
            appLockManager.setPin(pin)
            // Also enable app lock when PIN is set
            appLockManager.setAppLockEnabled(true)
        }
    }
    
    suspend fun verifyPin(pin: String): Boolean {
        return appLockManager.verifyPin(pin)
    }
    
    fun removePin() {
        viewModelScope.launch {
            appLockManager.removePin()
            appLockManager.setAppLockEnabled(false)
            appLockManager.setBiometricEnabled(false)
        }
    }
}

data class SecuritySettingsUiState(
    val appLockEnabled: Boolean = false,
    val biometricEnabled: Boolean = false,
    val hasPinSetup: Boolean = false,
    val isBiometricAvailable: Boolean = false,
    val isLoading: Boolean = true
)
