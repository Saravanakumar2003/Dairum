package com.dairum.presentation.screens.settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.ReminderConfig
import com.dairum.domain.model.ReminderType
import com.dairum.domain.model.SmartReminder
import com.dairum.domain.model.SmartReminderType
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onNavigateUp: () -> Unit,
    viewModel: NotificationSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    var showAddReminderDialog by remember { mutableStateOf(false) }
    var showAddSmartReminderDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf<Long?>(null) }
    var showMoodCheckInDialog by remember { mutableStateOf(false) }
    var showMultipleDailyRemindersDialog by remember { mutableStateOf(false) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }
    
    // Permission launcher for Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionResult(isGranted)
        if (isGranted) {
            // Permission granted, try test notification again
            viewModel.sendTestNotification()
        } else {
            showPermissionDeniedDialog = true
        }
    }
    
    LaunchedEffect(uiState.showPermissionRequest) {
        if (uiState.showPermissionRequest) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            viewModel.clearPermissionRequest()
        }
    }
    
    LaunchedEffect(Unit) {
        if (uiState.reminderConfigs.isEmpty() && !uiState.isLoading) {
            viewModel.createDefaultReminders()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddReminderDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Reminder")
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                // Permission Warning Banner (if no permission)
                if (!uiState.hasNotificationPermission) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Notifications Disabled",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Text(
                                        text = "Grant permission to receive reminders",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                                    )
                                }
                                Button(
                                    onClick = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Enable")
                                }
                            }
                        }
                    }
                }
                
                // Test Notification Section
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Test Notifications",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = if (uiState.hasNotificationPermission) 
                                        "Send a test notification to verify everything is working"
                                    else 
                                        "Enable notifications first to test",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                                )
                            }
                            Button(
                                onClick = { viewModel.sendTestNotification() },
                                enabled = uiState.hasNotificationPermission,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.NotificationsActive,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Test")
                            }
                        }
                    }
                }
                
                // Journal Reminders Section
                item {
                    SettingsSectionHeader(title = "Journal Reminders")
                }
                
                item {
                    Text(
                        text = "Get gentle reminders to write in your journal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                
                items(uiState.reminderConfigs) { config ->
                    ReminderConfigItem(
                        config = config,
                        onToggle = { viewModel.toggleReminder(config.id, it) },
                        onTimeClick = { showTimePickerDialog = config.id },
                        onDelete = { viewModel.deleteReminder(config.id) }
                    )
                }
                
                // Auto Reminders Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SettingsSectionHeader(title = "Auto Reminders")
                }
                
                item {
                    Text(
                        text = "Automatic reminders based on your journal activity",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                
                item {
                    SmartReminderTypeCard(
                        icon = Icons.Outlined.AutoAwesome,
                        title = "On This Day",
                        description = "Memories from this day in previous years",
                        enabled = true,
                        onClick = { }
                    )
                }
                
                item {
                    SmartReminderTypeCard(
                        icon = Icons.Outlined.LocalFireDepartment,
                        title = "Streak Protection",
                        description = "Get reminded if you might break your streak",
                        enabled = true,
                        onClick = { }
                    )
                }
                
                item {
                    SmartReminderTypeCard(
                        icon = Icons.Outlined.Mood,
                        title = "Mood Check-in",
                        description = if (uiState.moodCheckInEnabled) 
                            "Every ${uiState.moodCheckInIntervalHours}h between ${uiState.moodCheckInStartHour}:00-${uiState.moodCheckInEndHour}:00"
                        else 
                            "Periodic reminders to log your mood",
                        enabled = uiState.moodCheckInEnabled,
                        onClick = { showMoodCheckInDialog = true }
                    )
                }
                
                // Multiple Daily Reminders Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SettingsSectionHeader(title = "Multiple Daily Reminders")
                }
                
                item {
                    Text(
                        text = "Set up generic reminders at multiple times throughout the day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                
                item {
                    SmartReminderTypeCard(
                        icon = Icons.Outlined.NotificationsActive,
                        title = "Daily Notification Schedule",
                        description = if (uiState.multipleDailyRemindersEnabled && uiState.dailyReminderTimes.isNotEmpty())
                            "${uiState.dailyReminderTimes.size} notifications daily at ${uiState.dailyReminderTimes.take(3).joinToString(", ") { String.format("%02d:%02d", it.first, it.second) }}${if (uiState.dailyReminderTimes.size > 3) "..." else ""}"
                        else 
                            "Tap to setup multiple daily notifications",
                        enabled = uiState.multipleDailyRemindersEnabled && uiState.dailyReminderTimes.isNotEmpty(),
                        onClick = { showMultipleDailyRemindersDialog = true }
                    )
                }
                
                // Custom Reminders Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Custom Reminders",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(onClick = { showAddSmartReminderDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add")
                        }
                    }
                }
                
                if (uiState.smartReminders.isNotEmpty()) {
                    items(uiState.smartReminders) { reminder ->
                        SmartReminderItem(
                            reminder = reminder,
                            onToggle = { viewModel.toggleSmartReminder(reminder.id, it) },
                            onDelete = { viewModel.deleteSmartReminder(reminder.id) }
                        )
                    }
                } else {
                    item {
                        Text(
                            text = "No custom reminders yet. Add one to get notified on specific dates.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
                
                // Instructions Section
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "About Notifications",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Journal reminders help you maintain a consistent writing habit\n" +
                                      "• Smart reminders automatically detect birthdays and anniversaries from your entries\n" +
                                      "• 'On This Day' shows memories from previous years\n" +
                                      "• Streak protection alerts you before you break your writing streak",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
    
    // Add Reminder Dialog
    if (showAddReminderDialog) {
        AddReminderDialog(
            onDismiss = { showAddReminderDialog = false },
            onCreate = { name, title, message, hour, minute, days ->
                viewModel.createCustomReminder(name, title, message, hour, minute, days)
                showAddReminderDialog = false
            }
        )
    }
    
    // Add Smart Reminder Dialog
    if (showAddSmartReminderDialog) {
        AddSmartReminderDialog(
            onDismiss = { showAddSmartReminderDialog = false },
            onCreateBirthday = { name, date ->
                viewModel.createBirthdayReminder(0, name, date)
                showAddSmartReminderDialog = false
            },
            onCreateAnniversary = { title, date, description ->
                viewModel.createAnniversaryReminder(title, date, description)
                showAddSmartReminderDialog = false
            }
        )
    }
    
    // Mood Check-in Dialog
    if (showMoodCheckInDialog) {
        MoodCheckInConfigDialog(
            isEnabled = uiState.moodCheckInEnabled,
            intervalHours = uiState.moodCheckInIntervalHours,
            startHour = uiState.moodCheckInStartHour,
            endHour = uiState.moodCheckInEndHour,
            onDismiss = { showMoodCheckInDialog = false },
            onSave = { enabled, interval, start, end ->
                viewModel.setMoodCheckInEnabled(enabled)
                viewModel.setMoodCheckInInterval(interval)
                viewModel.setMoodCheckInTimeRange(start, end)
                showMoodCheckInDialog = false
            }
        )
    }
    
    // Multiple Daily Reminders Dialog
    if (showMultipleDailyRemindersDialog) {
        MultipleDailyRemindersDialog(
            isEnabled = uiState.multipleDailyRemindersEnabled,
            times = uiState.dailyReminderTimes,
            title = uiState.dailyReminderTitle,
            message = uiState.dailyReminderMessage,
            onDismiss = { showMultipleDailyRemindersDialog = false },
            onSave = { enabled, times, title, message ->
                viewModel.setMultipleDailyReminders(enabled, times, title, message)
                showMultipleDailyRemindersDialog = false
            }
        )
    }
    
    // Permission Denied Dialog
    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            icon = { Icon(Icons.Default.NotificationsOff, contentDescription = null) },
            title = { Text("Notifications Disabled") },
            text = { 
                Text("To receive reminders, please enable notifications in your device settings.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Open app settings
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                        showPermissionDeniedDialog = false
                    }
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDeniedDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Test notification success snackbar
    LaunchedEffect(uiState.testNotificationSent) {
        if (uiState.testNotificationSent) {
            viewModel.clearTestNotificationSent()
        }
    }
}

@Composable
private fun ReminderConfigItem(
    config: ReminderConfig,
    onToggle: (Boolean) -> Unit,
    onTimeClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = config.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = config.description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = config.isEnabled,
                    onCheckedChange = onToggle
                )
            }
            
            if (config.isEnabled) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Time
                    OutlinedButton(onClick = onTimeClick) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%02d:%02d", config.scheduleHour, config.scheduleMinute)
                        )
                    }
                    
                    // Days
                    Text(
                        text = formatDays(config.scheduleDays),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (config.type == ReminderType.CUSTOM) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Reminder?") },
            text = { Text("This reminder will be permanently deleted.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SmartReminderTypeCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    enabled: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (enabled) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Enabled",
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "Configure",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SmartReminderItem(
    reminder: SmartReminder,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                when (reminder.reminderType) {
                    SmartReminderType.BIRTHDAY -> Icons.Outlined.Cake
                    SmartReminderType.ANNIVERSARY -> Icons.Outlined.Favorite
                    SmartReminderType.MEMORY_ANNIVERSARY -> Icons.Outlined.PhotoLibrary
                    SmartReminderType.SPECIAL_DATE -> Icons.Outlined.Event
                    SmartReminderType.PERSON_MENTION -> Icons.Outlined.Person
                    SmartReminderType.STREAK_RISK -> Icons.Outlined.LocalFireDepartment
                    SmartReminderType.MOOD_CHECK_IN -> Icons.Outlined.Mood
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = reminder.reminderDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = reminder.isEnabled,
                onCheckedChange = onToggle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddReminderDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, title: String, message: String, hour: Int, minute: Int, days: List<Int>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf(20) }
    var minute by remember { mutableStateOf(0) }
    var selectedDays by remember { mutableStateOf(listOf(1, 2, 3, 4, 5, 6, 7)) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Custom Reminder") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Reminder Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Notification Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Notification Message") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Time: ${String.format("%02d:%02d", hour, minute)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Days: ${formatDays(selectedDays)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && title.isNotBlank()) {
                        onCreate(name, title, message, hour, minute, selectedDays)
                    }
                },
                enabled = name.isNotBlank() && title.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSmartReminderDialog(
    onDismiss: () -> Unit,
    onCreateBirthday: (name: String, date: Long) -> Unit,
    onCreateAnniversary: (title: String, date: Long, description: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var repeatYearly by remember { mutableStateOf(false) }
    
    // Date and time state
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedHour by remember { mutableStateOf(9) }
    var selectedMinute by remember { mutableStateOf(0) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Custom Reminder") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    placeholder = { Text("e.g., Doctor's appointment") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message (optional)") },
                    placeholder = { Text("Additional details...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Date selector
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Date",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = datePickerState.selectedDateMillis?.let {
                                    java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                                        .format(java.util.Date(it))
                                } ?: "Select date",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Icon(
                            Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Time selector
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Time",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = String.format("%02d:%02d %s", 
                                    if (selectedHour == 0) 12 else if (selectedHour > 12) selectedHour - 12 else selectedHour,
                                    selectedMinute,
                                    if (selectedHour < 12) "AM" else "PM"
                                ),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = repeatYearly,
                        onCheckedChange = { repeatYearly = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Repeat every year",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        val selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        // Combine date with time
                        val calendar = java.util.Calendar.getInstance().apply {
                            timeInMillis = selectedDate
                            set(java.util.Calendar.HOUR_OF_DAY, selectedHour)
                            set(java.util.Calendar.MINUTE, selectedMinute)
                        }
                        onCreateAnniversary(title, calendar.timeInMillis, message.takeIf { it.isNotBlank() })
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
    
    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    // Time picker dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Time") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(onClick = { 
                    selectedHour = timePickerState.hour
                    selectedMinute = timePickerState.minute
                    showTimePicker = false 
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun formatDays(days: List<Int>): String {
    if (days.size == 7) return "Every day"
    if (days == listOf(1, 2, 3, 4, 5)) return "Weekdays"
    if (days == listOf(6, 7)) return "Weekends"
    
    val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    return days.map { dayNames[it - 1] }.joinToString(", ")
}

@Composable
private fun MoodCheckInConfigDialog(
    isEnabled: Boolean,
    intervalHours: Int,
    startHour: Int,
    endHour: Int,
    onDismiss: () -> Unit,
    onSave: (enabled: Boolean, interval: Int, start: Int, end: Int) -> Unit
) {
    var enabled by remember { mutableStateOf(isEnabled) }
    var interval by remember { mutableStateOf(intervalHours) }
    var start by remember { mutableStateOf(startHour) }
    var end by remember { mutableStateOf(endHour) }
    
    val intervalOptions = listOf(2, 3, 4, 6, 8)
    
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Outlined.Mood, contentDescription = null) },
        title = { Text("Mood Check-in") },
        text = {
            Column {
                Text(
                    text = "Get periodic reminders to log your mood throughout the day.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Enable toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Enable Mood Check-ins",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = enabled,
                        onCheckedChange = { enabled = it }
                    )
                }
                
                if (enabled) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Interval selection
                    Text(
                        text = "Check-in Frequency",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        intervalOptions.forEach { hours ->
                            FilterChip(
                                selected = interval == hours,
                                onClick = { interval = hours },
                                label = { Text("${hours}h") }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Time range
                    Text(
                        text = "Active Hours",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("From", style = MaterialTheme.typography.labelSmall)
                            OutlinedCard {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { if (start > 5) start-- },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                    }
                                    Text(
                                        text = String.format("%02d:00", start),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    IconButton(
                                        onClick = { if (start < end - 2) start++ },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase")
                                    }
                                }
                            }
                        }
                        
                        Text("to", style = MaterialTheme.typography.bodyMedium)
                        
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Until", style = MaterialTheme.typography.labelSmall)
                            OutlinedCard {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { if (end > start + 2) end-- },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                    }
                                    Text(
                                        text = String.format("%02d:00", end),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    IconButton(
                                        onClick = { if (end < 23) end++ },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase")
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "You'll get up to ${(end - start) / interval} check-ins per day",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(enabled, interval, start, end) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun MultipleDailyRemindersDialog(
    isEnabled: Boolean,
    times: List<Pair<Int, Int>>,
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onSave: (enabled: Boolean, times: List<Pair<Int, Int>>, title: String, message: String) -> Unit
) {
    var enabled by remember { mutableStateOf(isEnabled) }
    var reminderTimes by remember { mutableStateOf(times.toMutableList().ifEmpty { mutableListOf(Pair(9, 0)) }) }
    var reminderTitle by remember { mutableStateOf(title.ifBlank { "Journal Reminder" }) }
    var reminderMessage by remember { mutableStateOf(message.ifBlank { "Take a moment to write in your journal" }) }
    var showTimePickerForIndex by remember { mutableStateOf<Int?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Outlined.NotificationsActive, contentDescription = null) },
        title = { Text("Multiple Daily Reminders") },
        text = {
            Column {
                Text(
                    text = "Set up multiple notifications throughout the day to remind you to journal.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Enable toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Enable Daily Reminders",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = enabled,
                        onCheckedChange = { enabled = it }
                    )
                }
                
                if (enabled) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Notification title
                    OutlinedTextField(
                        value = reminderTitle,
                        onValueChange = { reminderTitle = it },
                        label = { Text("Notification Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Notification message
                    OutlinedTextField(
                        value = reminderMessage,
                        onValueChange = { reminderMessage = it },
                        label = { Text("Notification Message") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Times section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Notification Times (${reminderTimes.size})",
                            style = MaterialTheme.typography.labelLarge
                        )
                        IconButton(
                            onClick = { 
                                // Add a new time - default to next hour after last time
                                val lastTime = reminderTimes.lastOrNull() ?: Pair(8, 0)
                                val newHour = (lastTime.first + 3).coerceAtMost(23)
                                reminderTimes = (reminderTimes + Pair(newHour, 0)).toMutableList()
                            },
                            enabled = reminderTimes.size < 10
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add time")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Time chips
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        reminderTimes.forEachIndexed { index, time ->
                            InputChip(
                                selected = false,
                                onClick = { showTimePickerForIndex = index },
                                label = { 
                                    Text(String.format("%02d:%02d", time.first, time.second))
                                },
                                trailingIcon = if (reminderTimes.size > 1) {
                                    {
                                        IconButton(
                                            onClick = { 
                                                reminderTimes = reminderTimes.toMutableList().apply { removeAt(index) }
                                            },
                                            modifier = Modifier.size(18.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Remove",
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                } else null
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Tap on a time to change it. You can add up to 10 reminders.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    onSave(
                        enabled, 
                        reminderTimes.sortedBy { it.first * 60 + it.second },
                        reminderTitle,
                        reminderMessage
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
    
    // Time picker for editing individual times
    showTimePickerForIndex?.let { index ->
        val currentTime = reminderTimes.getOrNull(index) ?: Pair(9, 0)
        val timePickerState = rememberTimePickerState(
            initialHour = currentTime.first,
            initialMinute = currentTime.second
        )
        AlertDialog(
            onDismissRequest = { showTimePickerForIndex = null },
            title = { Text("Select Time") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(onClick = { 
                    reminderTimes = reminderTimes.toMutableList().apply {
                        set(index, Pair(timePickerState.hour, timePickerState.minute))
                    }
                    showTimePickerForIndex = null 
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerForIndex = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
