package com.dairum.presentation.screens.lock

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.dairum.data.security.AppLockManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LockScreen(
    appLockManager: AppLockManager,
    biometricEnabled: Boolean,
    onUnlocked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isVerifying by remember { mutableStateOf(false) }
    var biometricTriggered by remember { mutableStateOf(false) }
    
    // Auto-trigger biometric on launch if enabled - watch biometricEnabled parameter
    LaunchedEffect(biometricEnabled) {
        if (biometricEnabled && 
            !biometricTriggered && 
            activity != null && 
            appLockManager.isBiometricAvailable()
        ) {
            biometricTriggered = true
            delay(300) // Small delay for smooth transition
            triggerBiometric(
                appLockManager = appLockManager,
                activity = activity,
                onSuccess = onUnlocked,
                onError = { errorMessage ->
                    error = errorMessage
                },
                onFallbackToPin = {
                    // User chose to use PIN instead
                }
            )
        }
    }
    
    // Verify PIN when 4 digits entered
    LaunchedEffect(pin) {
        if (pin.length == 4 && !isVerifying) {
            isVerifying = true
            val verified = appLockManager.verifyPin(pin)
            if (verified) {
                onUnlocked()
            } else {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                error = "Incorrect PIN"
                pin = ""
            }
            isVerifying = false
        }
    }
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            
            // App icon/logo area
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "D",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Enter PIN",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Enter your 4-digit PIN to unlock",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // PIN dots
            LockScreenPinDots(length = pin.length, hasError = error != null)
            
            // Error message
            AnimatedVisibility(
                visible = error != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Text(
                    text = error ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            // Loading indicator
            if (isVerifying) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(top = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // PIN Keypad
            LockScreenKeypad(
                onDigit = { digit ->
                    error = null
                    if (pin.length < 4) {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        pin += digit
                    }
                },
                onBackspace = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (pin.isNotEmpty()) {
                        pin = pin.dropLast(1)
                    }
                },
                onBiometric = if (biometricEnabled && appLockManager.isBiometricAvailable() && activity != null) {
                    {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        triggerBiometric(
                            appLockManager = appLockManager,
                            activity = activity,
                            onSuccess = onUnlocked,
                            onError = { errorMessage ->
                                error = errorMessage
                            },
                            onFallbackToPin = {}
                        )
                    }
                } else null
            )
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun LockScreenPinDots(
    length: Int,
    hasError: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        repeat(4) { index ->
            val isFilled = index < length
            val dotColor = when {
                hasError -> MaterialTheme.colorScheme.error
                isFilled -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline
            }
            
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .then(
                        if (isFilled) {
                            Modifier.background(dotColor)
                        } else {
                            Modifier.background(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        }
                    )
            )
        }
    }
}

@Composable
private fun LockScreenKeypad(
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
    onBiometric: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        // Row 1: 1, 2, 3
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            listOf("1", "2", "3").forEach { digit ->
                LockScreenKey(text = digit, onClick = { onDigit(digit) })
            }
        }
        
        // Row 2: 4, 5, 6
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            listOf("4", "5", "6").forEach { digit ->
                LockScreenKey(text = digit, onClick = { onDigit(digit) })
            }
        }
        
        // Row 3: 7, 8, 9
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            listOf("7", "8", "9").forEach { digit ->
                LockScreenKey(text = digit, onClick = { onDigit(digit) })
            }
        }
        
        // Row 4: biometric/empty, 0, backspace
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            // Biometric or empty
            if (onBiometric != null) {
                LockScreenKey(onClick = onBiometric) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Use fingerprint",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(72.dp))
            }
            
            // 0
            LockScreenKey(text = "0", onClick = { onDigit("0") })
            
            // Backspace
            LockScreenKey(onClick = onBackspace) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun LockScreenKey(
    text: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(72.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (content != null) {
                content()
            } else {
                text?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

private fun triggerBiometric(
    appLockManager: AppLockManager,
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
    onFallbackToPin: () -> Unit
) {
    val biometricPrompt = appLockManager.createBiometricPrompt(
        activity = activity,
        onSuccess = onSuccess,
        onError = onError,
        onFailed = onFallbackToPin
    )
    
    val promptInfo = appLockManager.createPromptInfo()
    
    biometricPrompt.authenticate(promptInfo)
}
