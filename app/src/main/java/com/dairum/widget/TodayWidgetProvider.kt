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

/**
 * Today Widget Provider.
 * Shows today's journal entry status - whether entries have been added or not.
 */
class TodayWidgetProvider : AppWidgetProvider() {
    
    companion object {
        const val ACTION_NEW_ENTRY = "com.dairum.widget.TODAY_NEW_ENTRY"
        const val ACTION_OPEN_APP = "com.dairum.widget.TODAY_OPEN_APP"
        
        // Keys for storing stats in SharedPreferences
        const val PREFS_NAME = "widget_today_prefs"
        const val KEY_TODAY_ENTRIES = "today_entries"
        const val KEY_LAST_UPDATE_DATE = "last_update_date"
        
        /**
         * Update widget with today's entry count from app
         */
        fun updateTodayEntries(context: Context, todayEntries: Int) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit()
                .putInt(KEY_TODAY_ENTRIES, todayEntries)
                .putString(KEY_LAST_UPDATE_DATE, LocalDate.now().toString())
                .apply()
            
            // Trigger widget update
            val intent = Intent(context, TodayWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
            val widgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = widgetManager.getAppWidgetIds(
                ComponentName(context, TodayWidgetProvider::class.java)
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
            context.sendBroadcast(intent)
        }
    }
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Timber.d("TodayWidget onUpdate: ${appWidgetIds.size} widgets")
        
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        when (intent.action) {
            ACTION_NEW_ENTRY -> {
                launchNewEntry(context)
            }
            ACTION_OPEN_APP -> {
                launchApp(context)
            }
        }
    }
    
    private fun launchApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(intent)
    }
    
    private fun launchNewEntry(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("destination", "new_entry")
        }
        context.startActivity(intent)
    }
    
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_today)
        
        // Get today's entries from SharedPreferences
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastUpdateDate = prefs.getString(KEY_LAST_UPDATE_DATE, null)
        val todayDate = LocalDate.now().toString()
        
        // Reset count if it's a new day
        val todayEntries = if (lastUpdateDate == todayDate) {
            prefs.getInt(KEY_TODAY_ENTRIES, 0)
        } else {
            0
        }
        
        // Update UI based on entry count
        if (todayEntries > 0) {
            // Entries exist - show success state
            views.setTextViewText(R.id.widget_status_icon, "✓")
            views.setTextViewText(R.id.widget_status_text, "All Good!")
            views.setTextViewText(
                R.id.widget_entry_count,
                if (todayEntries == 1) "1 entry today" else "$todayEntries entries today"
            )
        } else {
            // No entries - show reminder state
            views.setTextViewText(R.id.widget_status_icon, "📝")
            views.setTextViewText(R.id.widget_status_text, "No entry yet")
            views.setTextViewText(R.id.widget_entry_count, "Tap to start writing")
        }
        
        // Set click action for add button
        val newEntryIntent = Intent(context, TodayWidgetProvider::class.java).apply {
            action = ACTION_NEW_ENTRY
        }
        val newEntryPendingIntent = PendingIntent.getBroadcast(
            context, 0, newEntryIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_add_button, newEntryPendingIntent)
        
        // Set click action for entire widget to open app
        val openAppIntent = Intent(context, TodayWidgetProvider::class.java).apply {
            action = ACTION_OPEN_APP
        }
        val openAppPendingIntent = PendingIntent.getBroadcast(
            context, 1, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_container, openAppPendingIntent)
        
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    
    override fun onEnabled(context: Context) {
        Timber.d("TodayWidget enabled")
    }
    
    override fun onDisabled(context: Context) {
        Timber.d("TodayWidget disabled")
    }
}
