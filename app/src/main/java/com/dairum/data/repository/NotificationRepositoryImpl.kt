package com.dairum.data.repository

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import com.dairum.R
import com.dairum.data.local.dao.NotificationDao
import com.dairum.data.local.dao.JournalEntryDao
import com.dairum.data.local.dao.PersonDao
import com.dairum.data.local.entity.*
import com.dairum.domain.model.*
import com.dairum.domain.repository.MoodCheckInConfig
import com.dairum.domain.repository.NotificationRepository
import com.dairum.domain.repository.NotificationResult
import com.dairum.data.service.MoodCheckInWorker
import com.dairum.data.service.ReminderWorker
import com.dairum.data.service.SmartReminderWorker
import com.dairum.presentation.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao,
    private val journalEntryDao: JournalEntryDao,
    private val personDao: PersonDao,
    @ApplicationContext private val context: Context
) : NotificationRepository {

    private val workManager = WorkManager.getInstance(context)

    // ============ REMINDER CONFIGS ============

    override suspend fun createReminderConfig(config: ReminderConfig): Long {
        val entity = config.toEntity()
        val id = notificationDao.insertReminderConfig(entity)
        if (config.isEnabled) {
            scheduleReminderNotifications()
        }
        return id
    }

    override suspend fun updateReminderConfig(config: ReminderConfig) {
        notificationDao.updateReminderConfig(config.toEntity())
        scheduleReminderNotifications()
    }

    override suspend fun deleteReminderConfig(id: Long) {
        notificationDao.deleteReminderConfig(id)
        cancelReminderNotification(id)
    }

    override suspend fun getReminderConfigById(id: Long): ReminderConfig? {
        return notificationDao.getReminderConfigById(id)?.toDomain()
    }

    override fun getAllReminderConfigsFlow(): Flow<List<ReminderConfig>> {
        return notificationDao.getAllReminderConfigsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getEnabledReminderConfigs(): List<ReminderConfig> {
        return notificationDao.getEnabledReminderConfigs().map { it.toDomain() }
    }

    override suspend fun setReminderEnabled(id: Long, isEnabled: Boolean) {
        notificationDao.setReminderEnabled(id, isEnabled)
        scheduleReminderNotifications()
    }

    // ============ SMART REMINDERS ============

    override suspend fun createSmartReminder(reminder: SmartReminder): Long {
        val id = notificationDao.insertSmartReminder(reminder.toEntity())
        scheduleSmartReminders()
        return id
    }

    override suspend fun updateSmartReminder(reminder: SmartReminder) {
        notificationDao.updateSmartReminder(reminder.toEntity())
        scheduleSmartReminders()
    }

    override suspend fun deleteSmartReminder(id: Long) {
        notificationDao.deleteSmartReminder(id)
    }

    override suspend fun getSmartReminderById(id: Long): SmartReminder? {
        return notificationDao.getSmartReminderById(id)?.toDomain()
    }

    override fun getAllSmartRemindersFlow(): Flow<List<SmartReminder>> {
        return notificationDao.getAllSmartRemindersFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getEnabledSmartRemindersFlow(): Flow<List<SmartReminder>> {
        return notificationDao.getEnabledSmartRemindersFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getSmartRemindersByTypeFlow(type: SmartReminderType): Flow<List<SmartReminder>> {
        return notificationDao.getSmartRemindersByTypeFlow(type).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getUpcomingSmartReminders(daysAhead: Int): List<SmartReminder> {
        val now = System.currentTimeMillis()
        val endTime = now + TimeUnit.DAYS.toMillis(daysAhead.toLong())
        return notificationDao.getUpcomingSmartReminders(now, endTime).map { it.toDomain() }
    }

    override suspend fun setSmartReminderEnabled(id: Long, isEnabled: Boolean) {
        notificationDao.setSmartReminderEnabled(id, isEnabled)
        scheduleSmartReminders()
    }

    // ============ BIRTHDAY/ANNIVERSARY HELPERS ============

    override suspend fun createBirthdayReminder(personId: Long, name: String, date: Long): Long {
        val reminder = SmartReminderEntity(
            reminderType = SmartReminderType.BIRTHDAY,
            title = "$name's Birthday",
            description = "Don't forget to wish $name a happy birthday!",
            reminderDate = date,
            repeatYearly = true,
            relatedPersonId = personId,
            isEnabled = true,
            notifyDaysBefore = 1 // Remind day before
        )
        return notificationDao.insertSmartReminder(reminder)
    }

    override suspend fun createAnniversaryReminder(title: String, date: Long, description: String?): Long {
        val reminder = SmartReminderEntity(
            reminderType = SmartReminderType.ANNIVERSARY,
            title = title,
            description = description,
            reminderDate = date,
            repeatYearly = true,
            isEnabled = true,
            notifyDaysBefore = 1
        )
        return notificationDao.insertSmartReminder(reminder)
    }

    override fun getAnniversaryRemindersFlow(): Flow<List<SmartReminder>> {
        return notificationDao.getAnniversaryRemindersFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    // ============ INTELLIGENT REMINDER GENERATION ============

    override suspend fun generateMemoryAnniversaryReminders() {
        // Find entries from this date in previous years
        val now = LocalDateTime.now()
        val monthDay = String.format("%02d-%02d", now.monthValue, now.dayOfMonth)
        
        // This would be called by a worker to generate "On This Day" reminders
        // Implementation depends on how entries are queried
    }

    override suspend fun checkPersonMentionGaps() {
        // Check if favorite people haven't been mentioned in a while
        // Alert user to reconnect
        val favoritePeople = personDao.getFavoritePeople()
        val thirtyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
        
        // Implementation would check entry_people table for last mention
    }

    override suspend fun checkStreakRisk(): Boolean {
        // Check if user hasn't written today and it's late
        val todayStart = LocalDateTime.now().toLocalDate().atStartOfDay()
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val todayEnd = todayStart + TimeUnit.DAYS.toMillis(1)
        
        val todayEntryCount = journalEntryDao.getEntryCountBetween(todayStart, todayEnd)
        return todayEntryCount == 0
    }

    // ============ NOTIFICATION HISTORY ============

    override suspend fun logNotification(type: String, sourceId: Long, title: String, message: String) {
        notificationDao.insertNotificationHistory(
            NotificationHistoryEntity(
                type = type,
                sourceId = sourceId,
                title = title,
                message = message
            )
        )
    }

    override fun getNotificationHistoryFlow(): Flow<List<NotificationHistory>> {
        return notificationDao.getNotificationHistoryFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getRecentNotificationHistory(limit: Int): List<NotificationHistory> {
        return notificationDao.getRecentNotificationHistory(limit).map { it.toDomain() }
    }

    override suspend fun markNotificationTapped(id: Long) {
        notificationDao.markNotificationTapped(id)
    }

    override suspend fun clearOldHistory(daysToKeep: Int) {
        val cutoff = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysToKeep.toLong())
        notificationDao.clearOldNotificationHistory(cutoff)
    }

    // ============ SCHEDULING ============

    override suspend fun scheduleReminderNotifications() {
        // Cancel existing periodic work
        workManager.cancelUniqueWork(REMINDER_WORK_NAME)
        
        val enabledReminders = getEnabledReminderConfigs()
        if (enabledReminders.isEmpty()) return
        
        // Schedule periodic work for reminder checking
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            15, TimeUnit.MINUTES // Check every 15 minutes
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    override suspend fun cancelReminderNotification(reminderId: Long) {
        // Individual reminder cancellation handled by checking enabled status
        scheduleReminderNotifications()
    }

    override suspend fun scheduleSmartReminders() {
        // Cancel existing smart reminder work
        workManager.cancelUniqueWork(SMART_REMINDER_WORK_NAME)
        
        // Schedule daily work for smart reminder checking
        val workRequest = PeriodicWorkRequestBuilder<SmartReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            SMART_REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private fun calculateInitialDelay(): Long {
        // Schedule to run at 8 AM
        val now = LocalDateTime.now()
        var nextRun = now.toLocalDate().atTime(8, 0)
        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusDays(1)
        }
        return ChronoUnit.MILLIS.between(now, nextRun)
    }

    // ============ ENTITY MAPPERS ============

    private fun ReminderConfig.toEntity() = ReminderConfigEntity(
        id = id,
        type = type,
        name = name,
        description = description,
        isEnabled = isEnabled,
        scheduleHour = scheduleHour,
        scheduleMinute = scheduleMinute,
        scheduleDays = scheduleDays.joinToString(","),
        notificationTitle = notificationTitle,
        notificationMessage = notificationMessage,
        priority = priority
    )

    private fun ReminderConfigEntity.toDomain() = ReminderConfig(
        id = id,
        type = type,
        name = name,
        description = description,
        isEnabled = isEnabled,
        scheduleHour = scheduleHour,
        scheduleMinute = scheduleMinute,
        scheduleDays = scheduleDays.split(",").mapNotNull { it.toIntOrNull() },
        notificationTitle = notificationTitle,
        notificationMessage = notificationMessage,
        priority = priority
    )

    private fun SmartReminder.toEntity() = SmartReminderEntity(
        id = id,
        reminderType = reminderType,
        title = title,
        description = description,
        reminderDate = reminderDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        repeatYearly = repeatYearly,
        relatedPersonId = relatedPersonId,
        relatedEntryId = relatedEntryId,
        isEnabled = isEnabled,
        notifyDaysBefore = notifyDaysBefore,
        lastNotifiedAt = lastNotifiedAt?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
        metadata = metadata?.entries?.joinToString(";") { "${it.key}=${it.value}" }
    )

    private fun SmartReminderEntity.toDomain() = SmartReminder(
        id = id,
        reminderType = reminderType,
        title = title,
        description = description,
        reminderDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(reminderDate), ZoneId.systemDefault()),
        repeatYearly = repeatYearly,
        relatedPersonId = relatedPersonId,
        relatedEntryId = relatedEntryId,
        isEnabled = isEnabled,
        notifyDaysBefore = notifyDaysBefore,
        lastNotifiedAt = lastNotifiedAt?.let { 
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()) 
        },
        metadata = metadata?.split(";")?.mapNotNull { 
            val parts = it.split("=")
            if (parts.size == 2) parts[0] to parts[1] else null
        }?.toMap()
    )

    private fun NotificationHistoryEntity.toDomain() = NotificationHistory(
        id = id,
        type = type,
        sourceId = sourceId,
        title = title,
        message = message,
        wasTapped = wasTapped,
        sentAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(sentAt), ZoneId.systemDefault())
    )

    override suspend fun sendTestNotification(): NotificationResult {
        createNotificationChannel()
        
        // Check permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return NotificationResult(
                    success = false,
                    message = "Notification permission not granted",
                    requiresPermission = true
                )
            }
        }
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, TEST_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Test Notification ✅")
            .setContentText("Great! Notifications are working perfectly.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        try {
            NotificationManagerCompat.from(context).notify(TEST_NOTIFICATION_ID, notification)
            
            // Log the test notification
            logNotification("TEST", 0, "Test Notification", "Notifications are working")
            
            return NotificationResult(
                success = true,
                message = "Test notification sent successfully"
            )
        } catch (e: SecurityException) {
            return NotificationResult(
                success = false,
                message = "Permission denied: ${e.message}",
                requiresPermission = true
            )
        }
    }
    
    override fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
    
    override suspend fun createMoodCheckInReminder(enabled: Boolean, hour: Int, minute: Int, intervalHours: Int) {
        val config = MoodCheckInConfigEntity(
            isEnabled = enabled,
            intervalHours = intervalHours,
            startHour = hour,
            endHour = 22
        )
        notificationDao.insertMoodCheckInConfig(config)
        if (enabled) {
            scheduleMoodCheckInReminders()
        }
    }
    
    override suspend fun getMoodCheckInConfig(): MoodCheckInConfig? {
        return notificationDao.getMoodCheckInConfig()?.toDomain()
    }
    
    override suspend fun updateMoodCheckInConfig(config: MoodCheckInConfig) {
        notificationDao.updateMoodCheckInConfig(config.toEntity())
        if (config.isEnabled) {
            scheduleMoodCheckInReminders()
        } else {
            cancelMoodCheckInReminders()
        }
    }
    
    private fun scheduleMoodCheckInReminders() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()
        
        val workRequest = PeriodicWorkRequestBuilder<MoodCheckInWorker>(
            1, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag(MOOD_CHECKIN_WORK_TAG)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            MOOD_CHECKIN_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
    
    private fun cancelMoodCheckInReminders() {
        workManager.cancelUniqueWork(MOOD_CHECKIN_WORK_NAME)
    }
    
    private fun MoodCheckInConfigEntity.toDomain() = MoodCheckInConfig(
        id = id,
        isEnabled = isEnabled,
        intervalHours = intervalHours,
        startHour = startHour,
        endHour = endHour,
        lastPromptAt = lastPromptAt
    )
    
    private fun MoodCheckInConfig.toEntity() = MoodCheckInConfigEntity(
        id = id,
        isEnabled = isEnabled,
        intervalHours = intervalHours,
        startHour = startHour,
        endHour = endHour,
        lastPromptAt = lastPromptAt
    )
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TEST_CHANNEL_ID,
                "Test Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for testing notifications"
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val REMINDER_WORK_NAME = "dairum_reminder_work"
        private const val SMART_REMINDER_WORK_NAME = "dairum_smart_reminder_work"
        private const val MOOD_CHECKIN_WORK_NAME = "dairum_mood_checkin_work"
        private const val MOOD_CHECKIN_WORK_TAG = "mood_checkin_tag"
        private const val TEST_CHANNEL_ID = "dairum_test_channel"
        private const val TEST_NOTIFICATION_ID = 99999
    }
}
