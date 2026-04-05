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
 * Stats Widget Provider.
 * Displays quick journal statistics on home screen.
 */
class StatsWidgetProvider : AppWidgetProvider() {
    
    companion object {
        const val ACTION_OPEN_STATS = "com.dairum.widget.OPEN_STATS"
        const val ACTION_OPEN_APP = "com.dairum.widget.OPEN_APP"
        
        // Keys for storing stats in SharedPreferences
        const val PREFS_NAME = "widget_stats_prefs"
        const val KEY_TOTAL_ENTRIES = "total_entries"
        const val KEY_CURRENT_STREAK = "current_streak"
        const val KEY_THIS_MONTH = "this_month"
        const val KEY_TOTAL_WORDS = "total_words"
        
        /**
         * Update widget stats from app
         */
        fun updateStats(
            context: Context,
            totalEntries: Int,
            currentStreak: Int,
            thisMonth: Int,
            totalWords: Int
        ) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit()
                .putInt(KEY_TOTAL_ENTRIES, totalEntries)
                .putInt(KEY_CURRENT_STREAK, currentStreak)
                .putInt(KEY_THIS_MONTH, thisMonth)
                .putInt(KEY_TOTAL_WORDS, totalWords)
                .apply()
            
            // Trigger widget update
            val intent = Intent(context, StatsWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
            val widgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = widgetManager.getAppWidgetIds(
                ComponentName(context, StatsWidgetProvider::class.java)
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
        Timber.d("StatsWidget onUpdate: ${appWidgetIds.size} widgets")
        
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        when (intent.action) {
            ACTION_OPEN_STATS -> {
                launchApp(context, "stats")
            }
            ACTION_OPEN_APP -> {
                launchApp(context, null)
            }
        }
    }
    
    private fun launchApp(context: Context, destination: String?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            destination?.let { putExtra("destination", it) }
        }
        context.startActivity(intent)
    }
    
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_stats)
        
        // Get stats from SharedPreferences
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val totalEntries = prefs.getInt(KEY_TOTAL_ENTRIES, 0)
        val currentStreak = prefs.getInt(KEY_CURRENT_STREAK, 0)
        val thisMonth = prefs.getInt(KEY_THIS_MONTH, 0)
        
        // Set values - compact format (3 stats: entries, streak, this month)
        views.setTextViewText(R.id.widget_total_entries, totalEntries.toString())
        views.setTextViewText(R.id.widget_streak, currentStreak.toString())
        views.setTextViewText(R.id.widget_this_month, thisMonth.toString())
        
        // Set click action to open stats
        val openStatsIntent = Intent(context, StatsWidgetProvider::class.java).apply {
            action = ACTION_OPEN_STATS
        }
        val openStatsPendingIntent = PendingIntent.getBroadcast(
            context, 0, openStatsIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_container, openStatsPendingIntent)
        
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    
    private fun formatNumber(num: Int): String {
        return when {
            num >= 1000000 -> String.format("%.1fM", num / 1000000.0)
            num >= 1000 -> String.format("%.1fK", num / 1000.0)
            else -> num.toString()
        }
    }
    
    override fun onEnabled(context: Context) {
        Timber.d("StatsWidget enabled")
    }
    
    override fun onDisabled(context: Context) {
        Timber.d("StatsWidget disabled")
    }
}
