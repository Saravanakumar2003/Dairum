package com.dairum.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.repository.TaskerPeriod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskerIntegrationScreen(
    onBackClick: () -> Unit,
    viewModel: TaskerIntegrationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(uiState.copySuccess) {
        if (uiState.copySuccess) {
            snackbarHostState.showSnackbar("Copied to clipboard!")
            viewModel.clearCopySuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Automation Integration") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Introduction
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Automation Support",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Dairum integrates with automation apps like Tasker, MacroDroid, and Locale. You can configure actions through a user-friendly interface or use intents for advanced control.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Enable Integration
            item {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text("Enable Automation") },
                        supportingContent = { Text("Allow external apps to trigger exports") },
                        trailingContent = {
                            Switch(
                                checked = uiState.isEnabled,
                                onCheckedChange = viewModel::setEnabled
                            )
                        },
                        modifier = Modifier.clickable {
                            viewModel.setEnabled(!uiState.isEnabled)
                        }
                    )
                }
            }
            
            if (uiState.isEnabled) {
                // Default Export Settings
                item {
                    Text(
                        text = "Default Export Settings",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Format selection
                            Text(
                                text = "Default Format",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ExportFormat.entries.forEach { format ->
                                    FilterChip(
                                        selected = uiState.defaultFormat == format,
                                        onClick = { viewModel.setDefaultFormat(format) },
                                        label = { 
                                            Text(
                                                when (format) {
                                                    ExportFormat.TXT -> "Text"
                                                    ExportFormat.PDF -> "PDF"
                                                }
                                            )
                                        }
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Default period
                            Text(
                                text = "Default Period",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TaskerPeriod.entries.forEach { period ->
                                    FilterChip(
                                        selected = uiState.defaultPeriod == period,
                                        onClick = { viewModel.setDefaultPeriod(period) },
                                        label = { Text(period.label) }
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Include media
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Include Photos (PDF only)")
                                Switch(
                                    checked = uiState.defaultIncludeMedia,
                                    onCheckedChange = viewModel::setDefaultIncludeMedia
                                )
                            }
                        }
                    }
                }
                
                // UI-Based Plugin Setup (Recommended)
                item {
                    Text(
                        text = "Easy Setup (Recommended)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.TouchApp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Plugin Action",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Dairum appears as a plugin action in your automation app. Configure exports through a simple UI without needing to set up intents manually.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "How to add in MacroDroid:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            val macrodroidSteps = """
1. Create or edit a macro
2. Add Action → Applications → Locale/Tasker Plugin
3. Select "Dairum Action" from the list
4. Configure your export settings in the UI
5. Save the action
                            """.trimIndent()
                            
                            Text(
                                text = macrodroidSteps,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Default,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "How to add in Tasker:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            val taskerSteps = """
1. Create or edit a task
2. Add Action → Plugin → Dairum Action
3. Tap the pencil icon to configure
4. Set your export preferences
5. Save the action
                            """.trimIndent()
                            
                            Text(
                                text = taskerSteps,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Default,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                
                // Intent Actions (Advanced)
                item {
                    Text(
                        text = "Advanced: Intent Actions",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                item {
                    Text(
                        text = "For advanced users who prefer manual intent configuration:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                item {
                    IntentActionCard(
                        title = "Export Entries",
                        action = "com.dairum.action.EXPORT",
                        description = "Triggers an export with specified parameters",
                        extras = listOf(
                            IntentExtra("format", "String", "\"txt\" or \"pdf\" (default: your setting)"),
                            IntentExtra("period", "String", "\"all\", \"day\", \"week\", \"month\" (default: your setting)"),
                            IntentExtra("include_media", "Boolean", "true/false (default: your setting)"),
                            IntentExtra("start_date", "Long", "Start timestamp in milliseconds (optional)"),
                            IntentExtra("end_date", "Long", "End timestamp in milliseconds (optional)")
                        ),
                        onCopy = { action ->
                            clipboardManager.setText(AnnotatedString(action))
                            viewModel.setCopySuccess()
                        }
                    )
                }
                
                item {
                    IntentActionCard(
                        title = "Create Backup",
                        action = "com.dairum.action.BACKUP",
                        description = "Creates a full backup of all app data",
                        extras = listOf(
                            IntentExtra("include_media", "Boolean", "true/false (default: true)")
                        ),
                        onCopy = { action ->
                            clipboardManager.setText(AnnotatedString(action))
                            viewModel.setCopySuccess()
                        }
                    )
                }
                
                // Example for Tasker
                item {
                    Text(
                        text = "Tasker Example",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "In Tasker, create a new task:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            val taskerExample = """
1. Add action: "Send Intent"
2. Action: com.dairum.action.EXPORT
3. Cat: None
4. Extra: format:pdf
5. Extra: period:week
6. Extra: include_media:true
7. Package: com.dairum
8. Target: Broadcast Receiver
                            """.trimIndent()
                            
                            SelectionContainer {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = taskerExample,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            TextButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(taskerExample))
                                    viewModel.setCopySuccess()
                                }
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy")
                            }
                        }
                    }
                }
                
                // MacroDroid Example
                item {
                    Text(
                        text = "MacroDroid Example",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "In MacroDroid, create a new macro:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            val macrodroidExample = """
1. Add action: "Intent" > "Send Broadcast"
2. Intent Action: com.dairum.action.EXPORT
3. Add extras as key-value pairs:
   - format = pdf
   - period = day
   - include_media = true
                            """.trimIndent()
                            
                            SelectionContainer {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = macrodroidExample,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Security Note
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Security Note",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = "Only apps with explicit permission can send these intents. Exports are saved to app-private storage and can be shared via the app.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IntentActionCard(
    title: String,
    action: String,
    description: String,
    extras: List<IntentExtra>,
    onCopy: (String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { onCopy(action) }) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = "Copy action",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            SelectionContainer {
                Text(
                    text = action,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (extras.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Extras:",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                extras.forEach { extra ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "${extra.name} ",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "(${extra.type}): ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = extra.description,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

private data class IntentExtra(
    val name: String,
    val type: String,
    val description: String
)
