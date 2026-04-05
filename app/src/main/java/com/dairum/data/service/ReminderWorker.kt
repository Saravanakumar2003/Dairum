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
import com.dairum.data.local.dao.NotificationDao
import com.dairum.data.local.entity.NotificationHistoryEntity
import com.dairum.domain.model.ReminderType
import com.dairum.presentation.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationDao: NotificationDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        createNotificationChannel()
        
        val enabledReminders = notificationDao.getEnabledReminderConfigs()
        val now = LocalDateTime.now()
        val currentHour = now.hour
        val currentMinute = now.minute
        val currentDayOfWeek = now.dayOfWeek.value // 1 = Monday, 7 = Sunday
        
        for (reminder in enabledReminders) {
            val scheduledDays = reminder.scheduleDays.split(",").mapNotNull { it.toIntOrNull() }
            
            // Check if current time matches scheduled time (within 15 min window)
            val isTimeMatch = currentHour == reminder.scheduleHour && 
                currentMinute >= reminder.scheduleMinute && 
                currentMinute < reminder.scheduleMinute + 15
            
            val isDayMatch = currentDayOfWeek in scheduledDays
            
            if (isTimeMatch && isDayMatch) {
                sendReminderNotification(
                    id = reminder.id.toInt(),
                    title = reminder.notificationTitle,
                    message = reminder.notificationMessage,
                    type = reminder.type
                )
                
                // Log notification
                notificationDao.insertNotificationHistory(
                    NotificationHistoryEntity(
                        type = "reminder",
                        sourceId = reminder.id,
                        title = reminder.notificationTitle,
                        message = reminder.notificationMessage
                    )
                )
            }
        }
        
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                "Journal Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders to write in your journal"
            }
            
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendReminderNotification(id: Int, title: String, message: String, type: ReminderType) {
        if (!hasNotificationPermission()) return
        
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("action", "new_entry")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val icon = when (type) {
            ReminderType.DAILY_JOURNAL -> R.drawable.ic_launcher_foreground
            ReminderType.WEEKLY_REFLECTION -> R.drawable.ic_launcher_foreground
            ReminderType.CUSTOM -> R.drawable.ic_launcher_foreground
        }
        
        val notification = NotificationCompat.Builder(applicationContext, REMINDER_CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
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
        const val REMINDER_CHANNEL_ID = "dairum_reminders"
    }
}
