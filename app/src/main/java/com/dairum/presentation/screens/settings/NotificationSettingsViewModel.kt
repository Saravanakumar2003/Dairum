package com.dairum.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.*
import com.dairum.domain.repository.MoodCheckInConfig
import com.dairum.domain.repository.NotificationRepository
import com.dairum.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val personRepository: PersonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationSettingsUiState())
    val uiState: StateFlow<NotificationSettingsUiState> = _uiState.asStateFlow()

    init {
        loadReminders()
        loadSmartReminders()
        loadMoodCheckInConfig()
        loadMultipleDailyReminders()
        checkNotificationPermission()
    }
    
    private fun checkNotificationPermission() {
        val hasPermission = notificationRepository.hasNotificationPermission()
        _uiState.update { it.copy(hasNotificationPermission = hasPermission) }
    }
    
    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(hasNotificationPermission = granted) }
    }

    private fun loadReminders() {
        viewModelScope.launch {
            notificationRepository.getAllReminderConfigsFlow().collect { reminders ->
                _uiState.update { it.copy(reminderConfigs = reminders, isLoading = false) }
            }
        }
    }

    private fun loadSmartReminders() {
        viewModelScope.launch {
            notificationRepository.getAllSmartRemindersFlow().collect { reminders ->
                _uiState.update { it.copy(smartReminders = reminders) }
            }
        }
    }
    
    private fun loadMoodCheckInConfig() {
        viewModelScope.launch {
            val config = notificationRepository.getMoodCheckInConfig()
            _uiState.update { 
                it.copy(
                    moodCheckInEnabled = config?.isEnabled ?: false,
                    moodCheckInIntervalHours = config?.intervalHours ?: 4,
                    moodCheckInStartHour = config?.startHour ?: 9,
                    moodCheckInEndHour = config?.endHour ?: 22
                ) 
            }
        }
    }

    fun createDefaultReminders() {
        viewModelScope.launch {
            // Create daily journal reminder
            notificationRepository.createReminderConfig(
                ReminderConfig(
                    type = ReminderType.DAILY_JOURNAL,
                    name = "Daily Journal Reminder",
                    description = "A gentle reminder to write in your journal",
                    isEnabled = true,
                    scheduleHour = 20,
                    scheduleMinute = 0,
                    scheduleDays = listOf(1, 2, 3, 4, 5, 6, 7),
                    notificationTitle = "Time to Journal ✍️",
                    notificationMessage = "Take a moment to reflect on your day"
                )
            )
            
            // Create weekly reflection reminder
            notificationRepository.createReminderConfig(
                ReminderConfig(
                    type = ReminderType.WEEKLY_REFLECTION,
                    name = "Weekly Reflection",
                    description = "Weekly reflection reminder",
                    isEnabled = false,
                    scheduleHour = 10,
                    scheduleMinute = 0,
                    scheduleDays = listOf(7), // Sunday
                    notificationTitle = "Weekly Reflection 📝",
                    notificationMessage = "Look back on your week and celebrate your wins"
                )
            )
        }
    }

    fun toggleReminder(reminderId: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            notificationRepository.setReminderEnabled(reminderId, isEnabled)
        }
    }

    fun updateReminderTime(reminderId: Long, hour: Int, minute: Int) {
        viewModelScope.launch {
            val config = notificationRepository.getReminderConfigById(reminderId) ?: return@launch
            notificationRepository.updateReminderConfig(
                config.copy(scheduleHour = hour, scheduleMinute = minute)
            )
        }
    }

    fun updateReminderDays(reminderId: Long, days: List<Int>) {
        viewModelScope.launch {
            val config = notificationRepository.getReminderConfigById(reminderId) ?: return@launch
            notificationRepository.updateReminderConfig(config.copy(scheduleDays = days))
        }
    }

    fun deleteReminder(reminderId: Long) {
        viewModelScope.launch {
            notificationRepository.deleteReminderConfig(reminderId)
        }
    }

    fun createCustomReminder(
        name: String,
        title: String,
        message: String,
        hour: Int,
        minute: Int,
        days: List<Int>
    ) {
        viewModelScope.launch {
            notificationRepository.createReminderConfig(
                ReminderConfig(
                    type = ReminderType.CUSTOM,
                    name = name,
                    isEnabled = true,
                    scheduleHour = hour,
                    scheduleMinute = minute,
                    scheduleDays = days,
                    notificationTitle = title,
                    notificationMessage = message
                )
            )
        }
    }

    // Smart reminders
    fun toggleSmartReminder(reminderId: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            notificationRepository.setSmartReminderEnabled(reminderId, isEnabled)
        }
    }

    fun createBirthdayReminder(personId: Long, name: String, date: Long) {
        viewModelScope.launch {
            notificationRepository.createBirthdayReminder(personId, name, date)
        }
    }

    fun createAnniversaryReminder(title: String, date: Long, description: String?) {
        viewModelScope.launch {
            notificationRepository.createAnniversaryReminder(title, date, description)
        }
    }

    fun deleteSmartReminder(reminderId: Long) {
        viewModelScope.launch {
            notificationRepository.deleteSmartReminder(reminderId)
        }
    }

    fun scheduleNotifications() {
        viewModelScope.launch {
            notificationRepository.scheduleReminderNotifications()
            notificationRepository.scheduleSmartReminders()
        }
    }
    
    fun sendTestNotification() {
        viewModelScope.launch {
            val result = notificationRepository.sendTestNotification()
            if (result.success) {
                _uiState.update { it.copy(testNotificationSent = true, testNotificationMessage = result.message) }
            } else if (result.requiresPermission) {
                _uiState.update { it.copy(showPermissionRequest = true, testNotificationMessage = result.message) }
            } else {
                _uiState.update { it.copy(error = result.message) }
            }
        }
    }
    
    fun clearTestNotificationSent() {
        _uiState.update { it.copy(testNotificationSent = false, testNotificationMessage = null) }
    }
    
    fun clearPermissionRequest() {
        _uiState.update { it.copy(showPermissionRequest = false) }
    }
    
    // Mood Check-in methods
    fun setMoodCheckInEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val currentConfig = notificationRepository.getMoodCheckInConfig()
            if (currentConfig != null) {
                notificationRepository.updateMoodCheckInConfig(
                    currentConfig.copy(isEnabled = enabled)
                )
            } else {
                notificationRepository.createMoodCheckInReminder(
                    enabled = enabled,
                    hour = _uiState.value.moodCheckInStartHour,
                    minute = 0,
                    intervalHours = _uiState.value.moodCheckInIntervalHours
                )
            }
            _uiState.update { it.copy(moodCheckInEnabled = enabled) }
        }
    }
    
    fun setMoodCheckInInterval(hours: Int) {
        viewModelScope.launch {
            val currentConfig = notificationRepository.getMoodCheckInConfig()
            if (currentConfig != null) {
                notificationRepository.updateMoodCheckInConfig(
                    currentConfig.copy(intervalHours = hours)
                )
            }
            _uiState.update { it.copy(moodCheckInIntervalHours = hours) }
        }
    }
    
    fun setMoodCheckInTimeRange(startHour: Int, endHour: Int) {
        viewModelScope.launch {
            val currentConfig = notificationRepository.getMoodCheckInConfig()
            if (currentConfig != null) {
                notificationRepository.updateMoodCheckInConfig(
                    currentConfig.copy(startHour = startHour, endHour = endHour)
                )
            }
            _uiState.update { 
                it.copy(
                    moodCheckInStartHour = startHour,
                    moodCheckInEndHour = endHour
                ) 
            }
        }
    }
    
    // Multiple Daily Reminders
    fun setMultipleDailyReminders(
        enabled: Boolean,
        times: List<Pair<Int, Int>>,
        title: String,
        message: String
    ) {
        viewModelScope.launch {
            // Delete existing multiple daily reminder configs
            _uiState.value.reminderConfigs
                .filter { it.type == ReminderType.CUSTOM && it.name.startsWith("Daily Reminder #") }
                .forEach { notificationRepository.deleteReminderConfig(it.id) }
            
            if (enabled && times.isNotEmpty()) {
                // Create new reminder configs for each time
                times.forEachIndexed { index, (hour, minute) ->
                    notificationRepository.createReminderConfig(
                        ReminderConfig(
                            type = ReminderType.CUSTOM,
                            name = "Daily Reminder #${index + 1}",
                            description = "Multiple daily reminder at ${String.format("%02d:%02d", hour, minute)}",
                            isEnabled = true,
                            scheduleHour = hour,
                            scheduleMinute = minute,
                            scheduleDays = listOf(1, 2, 3, 4, 5, 6, 7),
                            notificationTitle = title,
                            notificationMessage = message
                        )
                    )
                }
            }
            
            _uiState.update { 
                it.copy(
                    multipleDailyRemindersEnabled = enabled,
                    dailyReminderTimes = times,
                    dailyReminderTitle = title,
                    dailyReminderMessage = message
                ) 
            }
        }
    }
    
    private fun loadMultipleDailyReminders() {
        viewModelScope.launch {
            notificationRepository.getAllReminderConfigsFlow().collect { reminders ->
                val dailyReminders = reminders
                    .filter { it.type == ReminderType.CUSTOM && it.name.startsWith("Daily Reminder #") }
                    .sortedBy { it.name }
                
                if (dailyReminders.isNotEmpty()) {
                    val times = dailyReminders.map { Pair(it.scheduleHour, it.scheduleMinute) }
                    val firstReminder = dailyReminders.first()
                    _uiState.update { 
                        it.copy(
                            multipleDailyRemindersEnabled = dailyReminders.any { r -> r.isEnabled },
                            dailyReminderTimes = times,
                            dailyReminderTitle = firstReminder.notificationTitle ?: "Journal Reminder",
                            dailyReminderMessage = firstReminder.notificationMessage ?: "Take a moment to write in your journal"
                        ) 
                    }
                }
            }
        }
    }
    
    fun createGenericReminder(
        title: String,
        message: String,
        dateTime: LocalDateTime
    ) {
        viewModelScope.launch {
            notificationRepository.createSmartReminder(
                SmartReminder(
                    reminderType = SmartReminderType.SPECIAL_DATE,
                    title = title,
                    description = message,
                    reminderDate = dateTime,
                    repeatYearly = false,
                    isEnabled = true
                )
            )
        }
    }
}

data class NotificationSettingsUiState(
    val reminderConfigs: List<ReminderConfig> = emptyList(),
    val smartReminders: List<SmartReminder> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val testNotificationSent: Boolean = false,
    val testNotificationMessage: String? = null,
    val hasNotificationPermission: Boolean = true,
    val showPermissionRequest: Boolean = false,
    // Mood Check-in
    val moodCheckInEnabled: Boolean = false,
    val moodCheckInIntervalHours: Int = 4,
    val moodCheckInStartHour: Int = 9,
    val moodCheckInEndHour: Int = 22,
    // Multiple Daily Reminders
    val multipleDailyRemindersEnabled: Boolean = false,
    val dailyReminderTimes: List<Pair<Int, Int>> = emptyList(),
    val dailyReminderTitle: String = "Journal Reminder",
    val dailyReminderMessage: String = "Take a moment to write in your journal"
)
