package com.dairum.presentation.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.ExportFormat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualExportScreen(
    onBackClick: () -> Unit,
    viewModel: ManualExportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle share
    LaunchedEffect(uiState.shareFilePath) {
        uiState.shareFilePath?.let { path ->
            try {
                val uri: Uri
                val mimeType: String
                
                // Check if path is a content:// URI or a file path
                if (path.startsWith("content://")) {
                    // Already a content URI (from MediaStore)
                    uri = Uri.parse(path)
                    mimeType = context.contentResolver.getType(uri) ?: "*/*"
                } else {
                    // It's a file path - use FileProvider
                    val file = File(path)
                    if (!file.exists()) {
                        snackbarHostState.showSnackbar("Export file not found")
                        viewModel.clearSharePath()
                        return@let
                    }
                    uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    mimeType = when {
                        path.endsWith(".pdf") -> "application/pdf"
                        path.endsWith(".txt") -> "text/plain"
                        else -> "*/*"
                    }
                }
                
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = mimeType
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Share Export"))
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("Share failed: ${e.message}")
            }
            viewModel.clearSharePath()
        }
    }
    
    LaunchedEffect(uiState.exportSuccess) {
        if (uiState.exportSuccess) {
            showShareDialog = true
        }
    }
    
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar("Export failed: $error")
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export Entries") },
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
            // Date Period Selection
            item {
                Text(
                    text = "Date Period",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Quick period options
                        Text(
                            text = "Quick Select",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DatePeriod.entries.take(4).forEach { period ->
                                FilterChip(
                                    selected = uiState.selectedPeriod == period,
                                    onClick = { viewModel.setDatePeriod(period) },
                                    label = { Text(period.label) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Custom date range
                        Text(
                            text = "Or Custom Range",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedCard(
                                onClick = { 
                                    viewModel.setDatePeriod(DatePeriod.CUSTOM)
                                    showStartDatePicker = true 
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "From",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = uiState.startDate?.let { dateFormat.format(Date(it)) } ?: "Any",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                            
                            OutlinedCard(
                                onClick = { 
                                    viewModel.setDatePeriod(DatePeriod.CUSTOM)
                                    showEndDatePicker = true 
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "To",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = uiState.endDate?.let { dateFormat.format(Date(it)) } ?: "Any",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Export Format
            item {
                Text(
                    text = "Export Format",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ExportFormat.entries.forEach { format ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.setExportFormat(format) }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = uiState.exportFormat == format,
                                    onClick = { viewModel.setExportFormat(format) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = when (format) {
                                            ExportFormat.TXT -> "Plain Text (.txt)"
                                            ExportFormat.PDF -> "PDF Document (.pdf)"
                                            ExportFormat.JSON -> "JSON (.json)"
                                            ExportFormat.CSV -> "CSV Spreadsheet (.csv)"
                                        },
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = when (format) {
                                            ExportFormat.TXT -> "Clean text with all metadata"
                                            ExportFormat.PDF -> "Beautiful formatted document with photos, weather, mood, location, and all details"
                                            ExportFormat.JSON -> "Structured data, ideal for import/export"
                                            ExportFormat.CSV -> "Spreadsheet compatible, tabular data"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            
                            if (format != ExportFormat.entries.last()) {
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }

            // Include Media (for PDF)
            if (uiState.exportFormat == ExportFormat.PDF) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        ListItem(
                            headlineContent = { Text("Include Photos") },
                            supportingContent = { Text("Embed photos in the PDF export") },
                            trailingContent = {
                                Switch(
                                    checked = uiState.includeMedia,
                                    onCheckedChange = viewModel::setIncludeMedia
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.setIncludeMedia(!uiState.includeMedia)
                            }
                        )
                    }
                }
            }

            // Export Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { viewModel.exportEntries() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isExporting
                ) {
                    if (uiState.isExporting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Icon(Icons.Default.FileDownload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Now")
                }
            }
            
            // Export History
            if (uiState.exportHistory.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Recent Exports",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                items(uiState.exportHistory.take(5)) { history ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        ListItem(
                            headlineContent = { Text(history.fileName) },
                            supportingContent = {
                                Text(
                                    "${history.entriesCount} entries • ${formatFileSize(history.fileSize)}"
                                )
                            },
                            trailingContent = {
                                IconButton(
                                    onClick = { viewModel.shareExport(history.filePath) }
                                ) {
                                    Icon(Icons.Outlined.Share, contentDescription = "Share")
                                }
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = when (history.format) {
                                        ExportFormat.PDF -> Icons.Default.PictureAsPdf
                                        ExportFormat.TXT -> Icons.Default.Description
                                        ExportFormat.JSON -> Icons.Default.DataObject
                                        ExportFormat.CSV -> Icons.Default.TableChart
                                    },
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Date Pickers
    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.startDate ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setStartDate(datePickerState.selectedDateMillis)
                    showStartDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.endDate ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setEndDate(datePickerState.selectedDateMillis)
                    showEndDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    // Export Success Dialog
    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { 
                showShareDialog = false
                viewModel.clearSuccess()
            },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Export Complete!") },
            text = {
                Column {
                    Text("Successfully exported ${uiState.lastExportEntryCount} journal entries.")
                    Spacer(modifier = Modifier.height(8.dp))
                    if (uiState.exportDestination == null) {
                        Text(
                            text = "The file has been saved. You can share it to save it externally.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "The file has been saved to your chosen location.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.shareLastExport()
                    showShareDialog = false
                    viewModel.clearSuccess()
                }) {
                    Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showShareDialog = false
                    viewModel.clearSuccess()
                }) {
                    Text("Done")
                }
            }
        )
    }
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> "${bytes / (1024 * 1024)} MB"
    }
}

enum class DatePeriod(val label: String) {
    ALL("All"),
    LAST_DAY("Day"),
    LAST_WEEK("Week"),
    LAST_MONTH("Month"),
    CUSTOM("Custom")
}
