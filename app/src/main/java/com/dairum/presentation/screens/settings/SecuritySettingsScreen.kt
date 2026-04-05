package com.dairum.presentation.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecuritySettingsScreen(
    onNavigateUp: () -> Unit,
    viewModel: SecuritySettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showSetupPinDialog by remember { mutableStateOf(false) }
    var showChangePinDialog by remember { mutableStateOf(false) }
    var showRemovePinDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Security") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // App Lock Toggle
            ListItem(
                headlineContent = { Text("App Lock") },
                supportingContent = { 
                    Text(
                        if (uiState.appLockEnabled) 
                            "Require authentication to open app" 
                        else 
                            "App lock is disabled"
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingContent = {
                    Switch(
                        checked = uiState.appLockEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled && !uiState.hasPinSetup) {
                                showSetupPinDialog = true
                            } else {
                                viewModel.setAppLockEnabled(enabled)
                            }
                        }
                    )
                },
                modifier = Modifier.clickable {
                    val newState = !uiState.appLockEnabled
                    if (newState && !uiState.hasPinSetup) {
                        showSetupPinDialog = true
                    } else {
                        viewModel.setAppLockEnabled(newState)
                    }
                }
            )
            
            // PIN Settings (only show when app lock is enabled)
            AnimatedVisibility(visible = uiState.appLockEnabled || uiState.hasPinSetup) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Text(
                        text = "PIN Settings",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    
                    if (uiState.hasPinSetup) {
                        // Change PIN option
                        ListItem(
                            headlineContent = { Text("Change PIN") },
                            supportingContent = { Text("Update your current PIN") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Password,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.clickable { showChangePinDialog = true }
                        )
                        
                        // Remove PIN option
                        ListItem(
                            headlineContent = { 
                                Text(
                                    "Remove PIN",
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            supportingContent = { Text("This will also disable app lock") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            modifier = Modifier.clickable { showRemovePinDialog = true }
                        )
                    } else {
                        // Setup PIN option
                        ListItem(
                            headlineContent = { Text("Set up PIN") },
                            supportingContent = { Text("Create a PIN to lock your app") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.AddCircleOutline,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.clickable { showSetupPinDialog = true }
                        )
                    }
                }
            }
            
            // Biometric Settings (only show when app lock is enabled and biometric is available)
            AnimatedVisibility(visible = uiState.appLockEnabled && uiState.isBiometricAvailable) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Text(
                        text = "Biometric",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    
                    ListItem(
                        headlineContent = { Text("Use Fingerprint") },
                        supportingContent = { Text("Unlock with fingerprint in addition to PIN") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.Fingerprint,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = uiState.biometricEnabled,
                                onCheckedChange = viewModel::setBiometricEnabled,
                                enabled = uiState.hasPinSetup
                            )
                        },
                        modifier = Modifier.clickable(enabled = uiState.hasPinSetup) {
                            viewModel.setBiometricEnabled(!uiState.biometricEnabled)
                        }
                    )
                    
                    if (!uiState.hasPinSetup) {
                        Text(
                            text = "Set up a PIN first to enable biometric unlock",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
            
            // Info section
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "When app lock is enabled, you'll need to authenticate every time you open the app. Your entries and files will be protected from unauthorized access.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
    
    // Setup PIN Dialog
    if (showSetupPinDialog) {
        PinSetupDialog(
            onDismiss = { showSetupPinDialog = false },
            onPinSet = { pin ->
                viewModel.setupPin(pin)
                showSetupPinDialog = false
            }
        )
    }
    
    // Change PIN Dialog
    if (showChangePinDialog) {
        ChangePinDialog(
            onDismiss = { showChangePinDialog = false },
            onVerifyCurrentPin = viewModel::verifyPin,
            onPinChanged = { newPin ->
                viewModel.setupPin(newPin)
                showChangePinDialog = false
            }
        )
    }
    
    // Remove PIN Confirmation
    if (showRemovePinDialog) {
        VerifyPinDialog(
            title = "Remove PIN",
            subtitle = "Enter your current PIN to remove it",
            onDismiss = { showRemovePinDialog = false },
            onVerify = viewModel::verifyPin,
            onSuccess = {
                viewModel.removePin()
                showRemovePinDialog = false
            }
        )
    }
}

@Composable
private fun PinSetupDialog(
    onDismiss: () -> Unit,
    onPinSet: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(PinSetupStep.ENTER) }
    var error by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = when (step) {
                    PinSetupStep.ENTER -> "Create PIN"
                    PinSetupStep.CONFIRM -> "Confirm PIN"
                }
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (step) {
                        PinSetupStep.ENTER -> "Enter a 4-digit PIN"
                        PinSetupStep.CONFIRM -> "Re-enter your PIN to confirm"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                PinDots(
                    length = if (step == PinSetupStep.ENTER) pin.length else confirmPin.length,
                    maxLength = 4
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                error?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PinKeypad(
                    onDigit = { digit ->
                        error = null
                        when (step) {
                            PinSetupStep.ENTER -> {
                                if (pin.length < 4) {
                                    pin += digit
                                    if (pin.length == 4) {
                                        step = PinSetupStep.CONFIRM
                                    }
                                }
                            }
                            PinSetupStep.CONFIRM -> {
                                if (confirmPin.length < 4) {
                                    confirmPin += digit
                                    if (confirmPin.length == 4) {
                                        if (confirmPin == pin) {
                                            onPinSet(pin)
                                        } else {
                                            error = "PINs don't match"
                                            confirmPin = ""
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onBackspace = {
                        when (step) {
                            PinSetupStep.ENTER -> {
                                if (pin.isNotEmpty()) {
                                    pin = pin.dropLast(1)
                                }
                            }
                            PinSetupStep.CONFIRM -> {
                                if (confirmPin.isNotEmpty()) {
                                    confirmPin = confirmPin.dropLast(1)
                                }
                            }
                        }
                    }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ChangePinDialog(
    onDismiss: () -> Unit,
    onVerifyCurrentPin: suspend (String) -> Boolean,
    onPinChanged: (String) -> Unit
) {
    var currentPin by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(ChangePinStep.VERIFY_CURRENT) }
    var error by remember { mutableStateOf<String?>(null) }
    var isVerifying by remember { mutableStateOf(false) }
    
    LaunchedEffect(currentPin) {
        if (currentPin.length == 4 && !isVerifying) {
            isVerifying = true
            val verified = onVerifyCurrentPin(currentPin)
            if (verified) {
                step = ChangePinStep.ENTER_NEW
            } else {
                error = "Incorrect PIN"
                currentPin = ""
            }
            isVerifying = false
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = when (step) {
                    ChangePinStep.VERIFY_CURRENT -> "Enter Current PIN"
                    ChangePinStep.ENTER_NEW -> "Enter New PIN"
                    ChangePinStep.CONFIRM_NEW -> "Confirm New PIN"
                }
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (step) {
                        ChangePinStep.VERIFY_CURRENT -> "Enter your current PIN to continue"
                        ChangePinStep.ENTER_NEW -> "Enter a new 4-digit PIN"
                        ChangePinStep.CONFIRM_NEW -> "Re-enter your new PIN"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                PinDots(
                    length = when (step) {
                        ChangePinStep.VERIFY_CURRENT -> currentPin.length
                        ChangePinStep.ENTER_NEW -> newPin.length
                        ChangePinStep.CONFIRM_NEW -> confirmPin.length
                    },
                    maxLength = 4
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                error?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                
                if (isVerifying) {
                    Spacer(modifier = Modifier.height(8.dp))
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PinKeypad(
                    onDigit = { digit ->
                        error = null
                        when (step) {
                            ChangePinStep.VERIFY_CURRENT -> {
                                if (currentPin.length < 4) {
                                    currentPin += digit
                                }
                            }
                            ChangePinStep.ENTER_NEW -> {
                                if (newPin.length < 4) {
                                    newPin += digit
                                    if (newPin.length == 4) {
                                        step = ChangePinStep.CONFIRM_NEW
                                    }
                                }
                            }
                            ChangePinStep.CONFIRM_NEW -> {
                                if (confirmPin.length < 4) {
                                    confirmPin += digit
                                    if (confirmPin.length == 4) {
                                        if (confirmPin == newPin) {
                                            onPinChanged(newPin)
                                        } else {
                                            error = "PINs don't match"
                                            confirmPin = ""
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onBackspace = {
                        when (step) {
                            ChangePinStep.VERIFY_CURRENT -> {
                                if (currentPin.isNotEmpty()) {
                                    currentPin = currentPin.dropLast(1)
                                }
                            }
                            ChangePinStep.ENTER_NEW -> {
                                if (newPin.isNotEmpty()) {
                                    newPin = newPin.dropLast(1)
                                }
                            }
                            ChangePinStep.CONFIRM_NEW -> {
                                if (confirmPin.isNotEmpty()) {
                                    confirmPin = confirmPin.dropLast(1)
                                }
                            }
                        }
                    }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun VerifyPinDialog(
    title: String,
    subtitle: String,
    onDismiss: () -> Unit,
    onVerify: suspend (String) -> Boolean,
    onSuccess: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isVerifying by remember { mutableStateOf(false) }
    
    LaunchedEffect(pin) {
        if (pin.length == 4 && !isVerifying) {
            isVerifying = true
            val verified = onVerify(pin)
            if (verified) {
                onSuccess()
            } else {
                error = "Incorrect PIN"
                pin = ""
            }
            isVerifying = false
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                PinDots(length = pin.length, maxLength = 4)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                error?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                
                if (isVerifying) {
                    Spacer(modifier = Modifier.height(8.dp))
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PinKeypad(
                    onDigit = { digit ->
                        error = null
                        if (pin.length < 4) {
                            pin += digit
                        }
                    },
                    onBackspace = {
                        if (pin.isNotEmpty()) {
                            pin = pin.dropLast(1)
                        }
                    }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PinDots(
    length: Int,
    maxLength: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        repeat(maxLength) { index ->
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .then(
                        if (index < length) {
                            Modifier.background(MaterialTheme.colorScheme.primary)
                        } else {
                            Modifier.border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                        }
                    )
            )
        }
    }
}

@Composable
fun PinKeypad(
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = LocalHapticFeedback.current
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        // Row 1: 1, 2, 3
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf("1", "2", "3").forEach { digit ->
                PinKey(
                    text = digit,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDigit(digit)
                    }
                )
            }
        }
        
        // Row 2: 4, 5, 6
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf("4", "5", "6").forEach { digit ->
                PinKey(
                    text = digit,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDigit(digit)
                    }
                )
            }
        }
        
        // Row 3: 7, 8, 9
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf("7", "8", "9").forEach { digit ->
                PinKey(
                    text = digit,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDigit(digit)
                    }
                )
            }
        }
        
        // Row 4: empty, 0, backspace
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Empty space
            Spacer(modifier = Modifier.size(64.dp))
            
            // 0
            PinKey(
                text = "0",
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onDigit("0")
                }
            )
            
            // Backspace
            PinKey(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onBackspace()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun PinKey(
    text: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(64.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant
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

private enum class PinSetupStep {
    ENTER, CONFIRM
}

private enum class ChangePinStep {
    VERIFY_CURRENT, ENTER_NEW, CONFIRM_NEW
}
