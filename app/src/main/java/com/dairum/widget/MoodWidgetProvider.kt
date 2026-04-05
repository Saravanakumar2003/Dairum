package com.dairum.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.dairum.R
import com.dairum.presentation.MainActivity
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Mood Widget Provider.
 * Provides quick mood logging from home screen.
 */
class MoodWidgetProvider : AppWidgetProvider() {
    
    companion object {
        const val ACTION_LOG_MOOD = "com.dairum.widget.LOG_MOOD"
        const val EXTRA_MOOD_LEVEL = "mood_level"
        
        // Mood levels 1-5
        const val MOOD_VERY_SAD = 1
        const val MOOD_SAD = 2
        const val MOOD_NEUTRAL = 3
        const val MOOD_HAPPY = 4
        const val MOOD_VERY_HAPPY = 5
    }
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Timber.d("MoodWidget onUpdate: ${appWidgetIds.size} widgets")
        
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        if (intent.action == ACTION_LOG_MOOD) {
            val moodLevel = intent.getIntExtra(EXTRA_MOOD_LEVEL, MOOD_NEUTRAL)
            Timber.d("MoodWidget: Logging mood level $moodLevel")
            
            // Launch app with pre-selected mood
            launchWithMood(context, moodLevel)
        }
    }
    
    private fun launchWithMood(context: Context, moodLevel: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("destination", "new_entry")
            putExtra("preset_mood", moodLevel)
        }
        context.startActivity(intent)
    }
    
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_mood)
        
        // Set date
        val today = LocalDate.now()
        views.setTextViewText(
            R.id.widget_date,
            today.format(DateTimeFormatter.ofPattern("EEEE, MMM d"))
        )
        
        // Set up mood button click handlers
        setupMoodButton(context, views, R.id.mood_very_sad, MOOD_VERY_SAD)
        setupMoodButton(context, views, R.id.mood_sad, MOOD_SAD)
        setupMoodButton(context, views, R.id.mood_neutral, MOOD_NEUTRAL)
        setupMoodButton(context, views, R.id.mood_happy, MOOD_HAPPY)
        setupMoodButton(context, views, R.id.mood_very_happy, MOOD_VERY_HAPPY)
        
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    
    private fun setupMoodButton(
        context: Context,
        views: RemoteViews,
        viewId: Int,
        moodLevel: Int
    ) {
        val intent = Intent(context, MoodWidgetProvider::class.java).apply {
            action = ACTION_LOG_MOOD
            putExtra(EXTRA_MOOD_LEVEL, moodLevel)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, moodLevel, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(viewId, pendingIntent)
    }
    
    override fun onEnabled(context: Context) {
        Timber.d("MoodWidget enabled")
    }
    
    override fun onDisabled(context: Context) {
        Timber.d("MoodWidget disabled")
    }
}
