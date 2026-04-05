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
import com.dairum.presentation.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@HiltWorker
class MoodCheckInWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationDao: NotificationDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val config = notificationDao.getMoodCheckInConfig() ?: return Result.success()
        
        if (!config.isEnabled) return Result.success()
        
        val now = LocalDateTime.now()
        val currentHour = now.hour
        val currentDayOfWeek = now.dayOfWeek.value // 1 = Monday, 7 = Sunday
        
        // Check if within allowed time window
        if (currentHour < config.startHour || currentHour > config.endHour) {
            return Result.success()
        }
        
        // Check if today is an enabled day
        val enabledDays = config.enabledDays.split(",").mapNotNull { it.toIntOrNull() }
        if (currentDayOfWeek !in enabledDays) {
            return Result.success()
        }
        
        // Check if enough time has passed since last prompt
        val lastPrompt = config.lastPromptAt
        if (lastPrompt != null) {
            val hoursSinceLastPrompt = TimeUnit.MILLISECONDS.toHours(
                System.currentTimeMillis() - lastPrompt
            )
            if (hoursSinceLastPrompt < config.intervalHours) {
                return Result.success()
            }
        }
        
        // Send the mood check-in notification
        createNotificationChannel()
        sendMoodCheckInNotification()
        
        // Update the last prompt time
        notificationDao.updateMoodCheckInLastPrompt(config.id, System.currentTimeMillis())
        
        // Log the notification
        notificationDao.insertNotificationHistory(
            NotificationHistoryEntity(
                type = "mood_checkin",
                sourceId = config.id,
                title = "How are you feeling?",
                message = "Take a moment to check in with yourself"
            )
        )
        
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MOOD_CHECKIN_CHANNEL_ID,
                "Mood Check-ins",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Periodic reminders to log your mood"
            }
            
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendMoodCheckInNotification() {
        if (!hasNotificationPermission()) return
        
        val moodMessages = listOf(
            "How are you feeling right now? 🌟",
            "Take a moment to check in with yourself 💭",
            "What's your mood like? ✨",
            "Time for a quick mood check 🎯",
            "How's your day going? 📝"
        )
        
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("action", "mood_checkin")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            MOOD_CHECKIN_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(applicationContext, MOOD_CHECKIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Mood Check-in")
            .setContentText(moodMessages.random())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "😊 Good",
                createMoodActionIntent("good")
            )
            .addAction(
                R.drawable.ic_launcher_foreground,
                "😐 Okay",
                createMoodActionIntent("neutral")
            )
            .addAction(
                R.drawable.ic_launcher_foreground,
                "😔 Low",
                createMoodActionIntent("low")
            )
            .build()
        
        NotificationManagerCompat.from(applicationContext).notify(MOOD_CHECKIN_NOTIFICATION_ID, notification)
    }
    
    private fun createMoodActionIntent(mood: String): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("action", "quick_mood")
            putExtra("mood_value", mood)
        }
        
        return PendingIntent.getActivity(
            applicationContext,
            mood.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
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
        const val MOOD_CHECKIN_CHANNEL_ID = "dairum_mood_checkins"
        const val MOOD_CHECKIN_NOTIFICATION_ID = 88888
    }
}
