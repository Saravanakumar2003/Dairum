package com.dairum.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onManageTemplates: () -> Unit = {},
    onExportSettings: () -> Unit,
    onBackupRestore: () -> Unit,
    onTaskerIntegration: () -> Unit,
    onSecuritySettings: () -> Unit,
    onNotificationSettings: () -> Unit,
    onApiKeySettings: () -> Unit,
    onAbout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_settings)) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // ============ TEMPLATES ============
            item {
                SettingsSectionHeader(title = "Templates")
            }
            
            item {
                SettingsItem(
                    icon = Icons.Outlined.Description,
                    title = "Custom Templates",
                    subtitle = "Create your own journal templates",
                    onClick = onManageTemplates
                )
            }
            
            // ============ EXPORT & BACKUP ============
            item {
                SettingsSectionHeader(title = "Export & Backup")
            }
            
            item {
                SettingsItem(
                    icon = Icons.Outlined.FileDownload,
                    title = "Export Entries",
                    subtitle = "Export to PDF or Text file",
                    onClick = onExportSettings
                )
            }
            
            item {
                SettingsItem(
                    icon = Icons.Outlined.Backup,
                    title = "Backup & Restore",
                    subtitle = "Full app data backup",
                    onClick = onBackupRestore
                )
            }
            
            item {
                SettingsItem(
                    icon = Icons.Outlined.AutoAwesome,
                    title = "Automation",
                    subtitle = "Tasker, MacroDroid integration",
                    onClick = onTaskerIntegration
                )
            }
            
            // ============ SECURITY ============
            item {
                SettingsSectionHeader(title = stringResource(R.string.settings_security))
            }
            
            item {
                SettingsItem(
                    icon = Icons.Outlined.Lock,
                    title = stringResource(R.string.settings_app_lock),
                    subtitle = if (uiState.appLockEnabled) "Enabled" else "Disabled",
                    onClick = onSecuritySettings
                )
            }
            
            // ============ NOTIFICATIONS ============
            item {
                SettingsSectionHeader(title = "Notifications")
            }
            
            item {
                SettingsItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Notification Settings",
                    subtitle = "Reminders, birthdays, anniversaries",
                    onClick = onNotificationSettings
                )
            }
            
            // ============ API CONFIGURATION ============
            item {
                SettingsSectionHeader(title = "API Configuration")
            }
            
            item {
                SettingsItem(
                    icon = Icons.Outlined.Key,
                    title = "API Keys",
                    subtitle = "Configure Maps and Calendar API keys",
                    onClick = onApiKeySettings
                )
            }
            
            // ============ ABOUT ============
            item {
                SettingsSectionHeader(title = stringResource(R.string.settings_about))
            }
            
            item {
                SettingsItem(
                    icon = Icons.Outlined.Info,
                    title = stringResource(R.string.settings_about),
                    subtitle = "Version 1.0.0",
                    onClick = onAbout
                )
            }
        }
    }
}

@Composable
internal fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    )
}


