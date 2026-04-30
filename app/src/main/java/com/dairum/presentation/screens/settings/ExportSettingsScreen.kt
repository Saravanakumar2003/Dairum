package com.dairum.presentation.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.ExportFormat
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: ExportSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }
    
    // Snackbar for success/error messages
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Create document launcher for saving to user-selected location
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(
            when (uiState.exportFormat) {
                ExportFormat.PDF -> "application/pdf"
                ExportFormat.TXT -> "text/plain"
                ExportFormat.JSON -> "application/json"
                ExportFormat.CSV -> "text/csv"
            }
        )
    ) { uri: Uri? ->
        uri?.let {
            viewModel.exportToExternalLocation(uiState.startDate, uiState.endDate, it)
        }
    }
    
    // Handle share
    LaunchedEffect(uiState.shareFilePath) {
        uiState.shareFilePath?.let { path ->
            try {
                val file = File(path)
                if (file.exists()) {
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = when (uiState.exportFormat) {
                            ExportFormat.PDF -> "application/pdf"
                            ExportFormat.TXT -> "text/plain"
                            ExportFormat.JSON -> "application/json"
                            ExportFormat.CSV -> "text/csv"
                        }
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Export"))
                }
            } catch (e: Exception) {
                // File provider not configured or file doesn't exist
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
    
    // Function to generate export filename
    fun generateExportFilename(): String {
        val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now())
        val extension = when (uiState.exportFormat) {
            ExportFormat.PDF -> "pdf"
            ExportFormat.TXT -> "txt"
            ExportFormat.JSON -> "json"
            ExportFormat.CSV -> "csv"
        }
        return "dairum_export_$timestamp.$extension"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export Settings") },
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
            // Auto Export Toggle
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text("Automatic Export") },
                        supportingContent = { Text("Automatically export journal entries on schedule") },
                        trailingContent = {
                            Switch(
                                checked = uiState.autoExportEnabled,
                                onCheckedChange = viewModel::setAutoExportEnabled
                            )
                        },
                        modifier = Modifier.clickable {
                            viewModel.setAutoExportEnabled(!uiState.autoExportEnabled)
                        }
                    )
                }
            }

            // Schedule Section
            if (uiState.autoExportEnabled) {
                item {
                    Text(
                        text = "Schedule",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Export Time", style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = uiState.scheduleHour.toString().padStart(2, '0'),
                                    onValueChange = { 
                                        it.toIntOrNull()?.let { h -> 
                                            if (h in 0..23) viewModel.setScheduleHour(h)
                                        }
                                    },
                                    label = { Text("Hour") },
                                    modifier = Modifier.width(80.dp),
                                    singleLine = true
                                )
                                Text(":", style = MaterialTheme.typography.headlineMedium)
                                OutlinedTextField(
                                    value = uiState.scheduleMinute.toString().padStart(2, '0'),
                                    onValueChange = { 
                                        it.toIntOrNull()?.let { m -> 
                                            if (m in 0..59) viewModel.setScheduleMinute(m)
                                        }
                                    },
                                    label = { Text("Minute") },
                                    modifier = Modifier.width(80.dp),
                                    singleLine = true
                                )
                            }
                        }
                    }
                }

                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Days", style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(8.dp))

                            val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                days.forEachIndexed { index, day ->
                                    FilterChip(
                                        selected = (index + 1) in uiState.scheduleDays,
                                        onClick = { viewModel.toggleScheduleDay(index + 1) },
                                        label = { Text(day) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Format Section
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
                        ExportFormat.values().forEach { format ->
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
                                Column {
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
                                            ExportFormat.TXT -> "Text only, no media"
                                            ExportFormat.PDF -> "Includes photos and formatting"
                                            ExportFormat.JSON -> "Structured data, ideal for import/export"
                                            ExportFormat.CSV -> "Spreadsheet compatible, tabular data"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
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

            // Export Now Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "One-Time Export",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Date Range Selection
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Date Range (Optional)",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Leave empty to export all entries",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Start Date
                            OutlinedCard(
                                onClick = { showStartDatePicker = true },
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
                            
                            // End Date
                            OutlinedCard(
                                onClick = { showEndDatePicker = true },
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
                        
                        // Clear dates button
                        if (uiState.startDate != null || uiState.endDate != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = {
                                    viewModel.setStartDate(null)
                                    viewModel.setEndDate(null)
                                }
                            ) {
                                Text("Clear dates")
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Export to Internal Storage
                Button(
                    onClick = { viewModel.exportWithDateRange(uiState.startDate, uiState.endDate) },
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
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Export to Custom Location
                OutlinedButton(
                    onClick = { createDocumentLauncher.launch(generateExportFilename()) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isExporting
                ) {
                    Icon(Icons.Outlined.FolderOpen, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save to Custom Location...")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "\"Export Now\" saves to app storage. Use \"Save to Custom Location\" to choose a folder like Downloads.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
    
    // Export Success Dialog with Share option
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
                    Text(
                        text = "The file has been saved to your device. You can share it to save it externally or send it to another app.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
