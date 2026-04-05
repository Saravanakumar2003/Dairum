package com.dairum.data.service

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
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dairum.R
import com.dairum.data.local.dao.JournalEntryDao
import com.dairum.data.local.dao.NotificationDao
import com.dairum.data.local.entity.NotificationHistoryEntity
import com.dairum.domain.model.SmartReminderType
import com.dairum.presentation.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@HiltWorker
class SmartReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationDao: NotificationDao,
    private val journalEntryDao: JournalEntryDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        createNotificationChannels()
        
        // Check various smart reminder types
        checkBirthdaysAndAnniversaries()
        checkMemoryAnniversaries()
        checkStreakRisk()
        
        return Result.success()
    }

    private suspend fun checkBirthdaysAndAnniversaries() {
        val now = System.currentTimeMillis()
        val weekFromNow = now + TimeUnit.DAYS.toMillis(7)
        
        val upcomingReminders = notificationDao.getUpcomingSmartReminders(now, weekFromNow)
        
        for (reminder in upcomingReminders) {
            val reminderDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(reminder.reminderDate),
                ZoneId.systemDefault()
            )
            
            val today = LocalDate.now()
            val reminderLocalDate = reminderDate.toLocalDate()
            
            // Adjust for yearly repeating reminders
            val effectiveDate = if (reminder.repeatYearly) {
                reminderLocalDate.withYear(today.year).let { 
                    if (it.isBefore(today)) it.plusYears(1) else it
                }
            } else {
                reminderLocalDate
            }
            
            val daysUntil = ChronoUnit.DAYS.between(today, effectiveDate)
            
            // Check if we should notify based on notifyDaysBefore
            if (daysUntil == reminder.notifyDaysBefore.toLong()) {
                val lastNotified = reminder.lastNotifiedAt
                val alreadyNotifiedToday = lastNotified != null && 
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(lastNotified), ZoneId.systemDefault())
                        .toLocalDate() == today
                
                if (!alreadyNotifiedToday) {
                    val message = when {
                        daysUntil == 0L -> "Today!"
                        daysUntil == 1L -> "Tomorrow"
                        else -> "In $daysUntil days"
                    }
                    
                    sendSmartReminderNotification(
                        id = (SMART_NOTIFICATION_BASE_ID + reminder.id).toInt(),
                        title = reminder.title,
                        message = "${reminder.description ?: ""} $message".trim(),
                        type = reminder.reminderType
                    )
                    
                    notificationDao.updateLastNotified(reminder.id, System.currentTimeMillis())
                    
                    notificationDao.insertNotificationHistory(
                        NotificationHistoryEntity(
                            type = "smart",
                            sourceId = reminder.id,
                            title = reminder.title,
                            message = message
                        )
                    )
                }
            }
        }
    }

    private suspend fun checkMemoryAnniversaries() {
        val today = LocalDate.now()
        val monthDay = String.format("%02d-%02d", today.monthValue, today.dayOfMonth)
        
        // Get entries from this date in previous years
        // This queries using the custom query in JournalEntryDao
        val entriesOnThisDay = journalEntryDao.getEntriesInDateRangeSync(0, System.currentTimeMillis())
            .filter { entry ->
                val entryDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(entry.createdAt),
                    ZoneId.systemDefault()
                ).toLocalDate()
                
                entryDate.monthValue == today.monthValue && 
                entryDate.dayOfMonth == today.dayOfMonth &&
                entryDate.year < today.year
            }
        
        if (entriesOnThisDay.isNotEmpty()) {
            val yearsAgo = entriesOnThisDay.map { entry ->
                today.year - LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(entry.createdAt),
                    ZoneId.systemDefault()
                ).year
            }.distinct()
            
            val message = when {
                yearsAgo.size == 1 -> "You have a memory from ${yearsAgo.first()} year${if (yearsAgo.first() > 1) "s" else ""} ago"
                else -> "You have memories from ${yearsAgo.size} different years on this day"
            }
            
            sendSmartReminderNotification(
                id = MEMORY_NOTIFICATION_ID,
                title = "📅 On This Day",
                message = message,
                type = SmartReminderType.MEMORY_ANNIVERSARY
            )
        }
    }

    private suspend fun checkStreakRisk() {
        val now = LocalDateTime.now()
        
        // Only check in the evening (after 7 PM)
        if (now.hour < 19) return
        
        val todayStart = now.toLocalDate().atStartOfDay()
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val todayEnd = todayStart + TimeUnit.DAYS.toMillis(1)
        
        val todayEntryCount = journalEntryDao.getEntryCountBetween(todayStart, todayEnd)
        
        if (todayEntryCount == 0) {
            // Check if user has a streak going
            val recentDates = journalEntryDao.getDistinctEntryDates().take(30)
            val streakLength = calculateCurrentStreak(recentDates)
            
            if (streakLength >= 3) {
                sendSmartReminderNotification(
                    id = STREAK_NOTIFICATION_ID,
                    title = "🔥 Don't break your streak!",
                    message = "You have a $streakLength day streak. Write something today to keep it going!",
                    type = SmartReminderType.STREAK_RISK
                )
            }
        }
    }

    private fun calculateCurrentStreak(dates: List<String>): Int {
        if (dates.isEmpty()) return 0
        
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var streak = 0
        var expectedDate = LocalDate.now().minusDays(1) // Start from yesterday
        
        for (dateStr in dates) {
            val date = LocalDate.parse(dateStr, formatter)
            if (date == expectedDate) {
                streak++
                expectedDate = date.minusDays(1)
            } else if (date.isBefore(expectedDate)) {
                break
            }
        }
        
        return streak
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Smart reminders channel
            val smartChannel = NotificationChannel(
                SMART_REMINDER_CHANNEL_ID,
                "Smart Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Intelligent reminders for birthdays, anniversaries, and memories"
            }
            notificationManager.createNotificationChannel(smartChannel)
            
            // Memory channel
            val memoryChannel = NotificationChannel(
                MEMORY_CHANNEL_ID,
                "Memory Reminders",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Reminders about past journal entries"
            }
            notificationManager.createNotificationChannel(memoryChannel)
        }
    }

    private fun sendSmartReminderNotification(id: Int, title: String, message: String, type: SmartReminderType) {
        if (!hasNotificationPermission()) return
        
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("action", when (type) {
                SmartReminderType.MEMORY_ANNIVERSARY -> "view_memories"
                SmartReminderType.STREAK_RISK -> "new_entry"
                else -> "view_reminders"
            })
        }
        
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val channelId = when (type) {
            SmartReminderType.MEMORY_ANNIVERSARY -> MEMORY_CHANNEL_ID
            else -> SMART_REMINDER_CHANNEL_ID
        }
        
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(applicationContext).notify(id, notification)
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object {
        const val SMART_REMINDER_CHANNEL_ID = "dairum_smart_reminders"
        const val MEMORY_CHANNEL_ID = "dairum_memories"
        const val SMART_NOTIFICATION_BASE_ID = 10000
        const val MEMORY_NOTIFICATION_ID = 20000
        const val STREAK_NOTIFICATION_ID = 20001
    }
}
