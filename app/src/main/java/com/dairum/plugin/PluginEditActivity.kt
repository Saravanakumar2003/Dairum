package com.dairum.plugin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.repository.TaskerPeriod
import com.dairum.presentation.theme.DairumTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Tasker/Locale/MacroDroid plugin configuration activity.
 * This activity is shown when the user selects Diarum as an action in their automation app.
 * The user can configure export settings which are then saved and passed back to the automation app.
 */
@AndroidEntryPoint
class PluginEditActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get existing configuration from the automation app (if editing)
        val existingBundle = intent.getBundleExtra(EXTRA_BUNDLE)
        
        val initialAction = existingBundle?.getString(BUNDLE_ACTION) ?: ACTION_EXPORT
        val initialFormat = existingBundle?.getString(BUNDLE_FORMAT) ?: "pdf"
        val initialPeriod = existingBundle?.getString(BUNDLE_PERIOD) ?: "all"
        val initialIncludeMedia = existingBundle?.getBoolean(BUNDLE_INCLUDE_MEDIA, true) ?: true
        val initialExportPath = existingBundle?.getString(BUNDLE_EXPORT_PATH) ?: ""
        val initialToken = existingBundle?.getString(BUNDLE_TOKEN) ?: ""
        val initialTitles = existingBundle?.getBoolean(BUNDLE_TITLES, true) ?: true
        
        setContent {
            DairumTheme {
                PluginEditScreen(
                    initialAction = initialAction,
                    initialFormat = initialFormat,
                    initialPeriod = initialPeriod,
                    initialIncludeMedia = initialIncludeMedia,
                    initialExportPath = initialExportPath,
                    initialToken = initialToken,
                    initialTitles = initialTitles,
                    onSave = { action, format, period, includeMedia, exportPath, token, titles ->
                        saveConfiguration(action, format, period, includeMedia, exportPath, token, titles)
                    },
                    onCancel = {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                )
            }
        }
    }
    
    private fun saveConfiguration(
        action: String,
        format: String,
        period: String,
        includeMedia: Boolean,
        exportPath: String,
        token: String,
        titles: Boolean
    ) {
        // Create the bundle that will be passed back to the automation app
        val resultBundle = Bundle().apply {
            putString(BUNDLE_ACTION, action)
            putString(BUNDLE_TOKEN, token.trim())
            if (action == ACTION_METADATA) putBoolean(BUNDLE_TITLES, titles)
            putString(BUNDLE_FORMAT, format)
            putString(BUNDLE_PERIOD, period)
            putBoolean(BUNDLE_INCLUDE_MEDIA, includeMedia)
            if (exportPath.isNotBlank()) {
                putString(BUNDLE_EXPORT_PATH, exportPath)
            }
        }
        
        // Generate the blurb (description shown in the automation app)
        val blurb = generateBlurb(action, format, period, includeMedia, exportPath, titles)
        
        // Create the result intent
        val resultIntent = Intent().apply {
            putExtra(EXTRA_BUNDLE, resultBundle)
            putExtra(EXTRA_STRING_BLURB, blurb)
        }
        
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
    
    private fun generateBlurb(
        action: String,
        format: String,
        period: String,
        includeMedia: Boolean,
        exportPath: String,
        titles: Boolean
    ): String {
        return when (action) {
            ACTION_EXPORT -> {
                val formatStr = format.uppercase()
                val periodStr = when (period) {
                    "day" -> "last day"
                    "week" -> "last week"
                    "month" -> "last month"
                    else -> "all entries"
                }
                val mediaStr = if (includeMedia && format == "pdf") " with photos" else ""
                val pathStr = if (exportPath.isNotBlank()) " → $exportPath" else ""
                "Export $periodStr to $formatStr$mediaStr$pathStr"
            }
            ACTION_BACKUP -> {
                val mediaStr = if (includeMedia) " with media" else ""
                val pathStr = if (exportPath.isNotBlank()) " → $exportPath" else ""
                "Create backup$mediaStr$pathStr"
            }
            ACTION_METADATA -> "Export metadata for Kith" + if (titles) "" else " (no titles)"
            else -> "Diarum Action"
        }
    }

    companion object {
        // Standard Locale/Tasker extras
        const val EXTRA_BUNDLE = "com.twofortyfouram.locale.intent.extra.BUNDLE"
        const val EXTRA_STRING_BLURB = "com.twofortyfouram.locale.intent.extra.BLURB"
        
        // Our custom bundle keys
        const val BUNDLE_ACTION = "action"
        const val BUNDLE_FORMAT = "format"
        const val BUNDLE_PERIOD = "period"
        const val BUNDLE_INCLUDE_MEDIA = "include_media"
        const val BUNDLE_EXPORT_PATH = "export_path"
        const val BUNDLE_TOKEN = "token"
        const val BUNDLE_TITLES = "titles"
        
        // Actions
        const val ACTION_EXPORT = "export"
        const val ACTION_BACKUP = "backup"
        const val ACTION_METADATA = "metadata"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PluginEditScreen(
    initialAction: String,
    initialFormat: String,
    initialPeriod: String,
    initialIncludeMedia: Boolean,
    initialExportPath: String,
    initialToken: String,
    initialTitles: Boolean,
    onSave: (action: String, format: String, period: String, includeMedia: Boolean, exportPath: String, token: String, titles: Boolean) -> Unit,
    onCancel: () -> Unit
) {
    var selectedAction by remember { mutableStateOf(initialAction) }
    var selectedFormat by remember { mutableStateOf(initialFormat) }
    var selectedPeriod by remember { mutableStateOf(initialPeriod) }
    var includeMedia by remember { mutableStateOf(initialIncludeMedia) }
    var exportPath by remember { mutableStateOf(initialExportPath) }
    var token by remember { mutableStateOf(initialToken) }
    var titles by remember { mutableStateOf(initialTitles) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configure Diarum Action") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { onSave(selectedAction, selectedFormat, selectedPeriod, includeMedia, exportPath, token, titles) }
                    ) {
                        Text("Save", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Action Selection
            Text(
                text = "Action Type",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column {
                    ActionOption(
                        title = "Export Journal",
                        description = "Export entries to TXT, PDF, JSON, or CSV",
                        icon = Icons.Default.FileDownload,
                        selected = selectedAction == PluginEditActivity.ACTION_EXPORT,
                        onClick = { selectedAction = PluginEditActivity.ACTION_EXPORT }
                    )
                    
                    HorizontalDivider()
                    
                    ActionOption(
                        title = "Create Backup",
                        description = "Create a full backup of all data",
                        icon = Icons.Default.Backup,
                        selected = selectedAction == PluginEditActivity.ACTION_BACKUP,
                        onClick = { selectedAction = PluginEditActivity.ACTION_BACKUP }
                    )

                    HorizontalDivider()

                    ActionOption(
                        title = "Metadata for Kith",
                        description = "Titles, mood, category, tags and people only (no entry text)",
                        icon = Icons.Default.FileDownload,
                        selected = selectedAction == PluginEditActivity.ACTION_METADATA,
                        onClick = { selectedAction = PluginEditActivity.ACTION_METADATA }
                    )
                }
            }
            
            // Export-specific options
            if (selectedAction == PluginEditActivity.ACTION_EXPORT) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Export Format",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFormat == "txt",
                        onClick = { selectedFormat = "txt" },
                        label = { Text("Plain Text") },
                        leadingIcon = if (selectedFormat == "txt") {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                    FilterChip(
                        selected = selectedFormat == "pdf",
                        onClick = { selectedFormat = "pdf" },
                        label = { Text("PDF") },
                        leadingIcon = if (selectedFormat == "pdf") {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFormat == "json",
                        onClick = { selectedFormat = "json" },
                        label = { Text("JSON") },
                        leadingIcon = if (selectedFormat == "json") {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                    FilterChip(
                        selected = selectedFormat == "csv",
                        onClick = { selectedFormat = "csv" },
                        label = { Text("CSV") },
                        leadingIcon = if (selectedFormat == "csv") {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Date Range",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        PeriodOption(
                            title = "All Entries",
                            description = "Export entire journal",
                            selected = selectedPeriod == "all",
                            onClick = { selectedPeriod = "all" }
                        )
                        HorizontalDivider()
                        PeriodOption(
                            title = "Last Day",
                            description = "Entries from the past 24 hours",
                            selected = selectedPeriod == "day",
                            onClick = { selectedPeriod = "day" }
                        )
                        HorizontalDivider()
                        PeriodOption(
                            title = "Last Week",
                            description = "Entries from the past 7 days",
                            selected = selectedPeriod == "week",
                            onClick = { selectedPeriod = "week" }
                        )
                        HorizontalDivider()
                        PeriodOption(
                            title = "Last Month",
                            description = "Entries from the past 30 days",
                            selected = selectedPeriod == "month",
                            onClick = { selectedPeriod = "month" }
                        )
                    }
                }
            }
            
            // Include Media option (for both export PDF and backup)
            if ((selectedAction == PluginEditActivity.ACTION_EXPORT && selectedFormat == "pdf") ||
                selectedAction == PluginEditActivity.ACTION_BACKUP) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text("Include Photos") },
                        supportingContent = { 
                            Text(
                                if (selectedAction == PluginEditActivity.ACTION_BACKUP)
                                    "Include media files in backup"
                                else
                                    "Embed photos in PDF export"
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = includeMedia,
                                onCheckedChange = { includeMedia = it }
                            )
                        }
                    )
                }
            }
            
            // Custom Export Path
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Automation token",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = token,
                onValueChange = { token = it },
                label = { Text("Token (required)") },
                supportingText = { Text("The token set in Diarum Settings > Automation. Requests without the right token are rejected.") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (selectedAction == PluginEditActivity.ACTION_METADATA) {
                ListItem(
                    headlineContent = { Text("Include titles") },
                    supportingContent = { Text("Entry titles are the only free text in this export") },
                    trailingContent = { Switch(checked = titles, onCheckedChange = { titles = it }) }
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Export Location",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                OutlinedTextField(
                    value = exportPath,
                    onValueChange = { exportPath = it },
                    label = { Text("Custom path (optional)") },
                    placeholder = { Text("e.g. /storage/emulated/0/Documents/MyExports") },
                    supportingText = { Text("Leave empty to use default (Documents/Data Exports/Dairum)") },
                    leadingIcon = { Icon(Icons.Default.Folder, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Preview Card
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Preview,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Action Preview",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = generatePreviewText(selectedAction, selectedFormat, selectedPeriod, includeMedia, exportPath, titles),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "How it works",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "When this action runs, Diarum will automatically perform the configured operation in the background. Exports are saved to the specified location or the app's default export folder (Documents/Data Exports/Dairum).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionOption(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal) },
        supportingContent = { Text(description) },
        leadingContent = {
            Icon(
                icon,
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PeriodOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal) },
        supportingContent = { Text(description) },
        trailingContent = {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

private fun generatePreviewText(
    action: String,
    format: String,
    period: String,
    includeMedia: Boolean,
    exportPath: String,
    titles: Boolean
): String {
    return when (action) {
        PluginEditActivity.ACTION_METADATA ->
            "Write a metadata-only JSON file for Kith (no entry text)" +
                (if (titles) ", with titles." else ", without titles.") +
                "\nSaved to: Documents/Data Exports/Dairum"
        PluginEditActivity.ACTION_EXPORT -> {
            val formatStr = when (format) {
                "pdf" -> "PDF"
                "json" -> "JSON"
                "csv" -> "CSV"
                else -> "plain text"
            }
            val periodStr = when (period) {
                "day" -> "from the last day"
                "week" -> "from the last week"
                "month" -> "from the last month"
                else -> "(all entries)"
            }
            val mediaStr = if (includeMedia && format == "pdf") "\nPhotos will be embedded in the PDF." else ""
            val pathStr = if (exportPath.isNotBlank()) "\nSaved to: $exportPath" else "\nSaved to: Documents/Data Exports/Dairum (default)"
            "Export journal entries $periodStr to $formatStr format.$mediaStr$pathStr"
        }
        PluginEditActivity.ACTION_BACKUP -> {
            val mediaStr = if (includeMedia) "including all photos and media files" else "without media files"
            val pathStr = if (exportPath.isNotBlank()) "\nSaved to: $exportPath" else ""
            "Create a complete backup of all journal data, $mediaStr.$pathStr"
        }
        else -> "Unknown action"
    }
}
