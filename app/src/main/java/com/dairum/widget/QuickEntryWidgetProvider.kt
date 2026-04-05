package com.dairum.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.dairum.R
import com.dairum.domain.model.WritingPrompts
import com.dairum.presentation.MainActivity
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Quick Entry Widget Provider.
 * Displays a daily writing prompt and allows quick entry creation.
 */
class QuickEntryWidgetProvider : AppWidgetProvider() {
    
    companion object {
        const val ACTION_REFRESH_PROMPT = "com.dairum.widget.REFRESH_PROMPT"
        const val ACTION_NEW_ENTRY = "com.dairum.widget.NEW_ENTRY"
        const val ACTION_NEW_ENTRY_WITH_PROMPT = "com.dairum.widget.NEW_ENTRY_WITH_PROMPT"
        const val EXTRA_PROMPT_TEXT = "prompt_text"
        
        /**
         * Manually trigger widget update from app
         */
        fun updateWidget(context: Context) {
            val intent = Intent(context, QuickEntryWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
            val widgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = widgetManager.getAppWidgetIds(
                ComponentName(context, QuickEntryWidgetProvider::class.java)
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
        Timber.d("QuickEntryWidget onUpdate: ${appWidgetIds.size} widgets")
        
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        when (intent.action) {
            ACTION_REFRESH_PROMPT -> {
                Timber.d("Widget: Refreshing prompt")
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, QuickEntryWidgetProvider::class.java)
                )
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
            ACTION_NEW_ENTRY -> {
                Timber.d("Widget: New entry clicked")
                launchNewEntry(context, null)
            }
            ACTION_NEW_ENTRY_WITH_PROMPT -> {
                val promptText = intent.getStringExtra(EXTRA_PROMPT_TEXT)
                Timber.d("Widget: New entry with prompt: $promptText")
                launchNewEntry(context, promptText)
            }
        }
    }
    
    private fun launchNewEntry(context: Context, promptText: String?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("destination", "new_entry")
            promptText?.let { putExtra("prompt", it) }
        }
        context.startActivity(intent)
    }
    
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_quick_entry)
        
        // Set date
        val today = LocalDate.now()
        val dateText = today.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))
        views.setTextViewText(R.id.widget_date, dateText)
        
        // Set daily prompt
        val prompt = WritingPrompts.getPromptOfTheDay()
        views.setTextViewText(R.id.widget_prompt, "\"${prompt.text}\"")
        
        // Set up click for the whole widget to create new entry
        val newEntryIntent = Intent(context, QuickEntryWidgetProvider::class.java).apply {
            action = ACTION_NEW_ENTRY
        }
        val newEntryPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            newEntryIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_new_entry_button, newEntryPendingIntent)
        
        // Set up click for using the prompt
        val promptIntent = Intent(context, QuickEntryWidgetProvider::class.java).apply {
            action = ACTION_NEW_ENTRY_WITH_PROMPT
            putExtra(EXTRA_PROMPT_TEXT, prompt.text)
        }
        val promptPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            promptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_prompt, promptPendingIntent)
        views.setOnClickPendingIntent(R.id.widget_use_prompt_button, promptPendingIntent)
        
        // Set up refresh button
        val refreshIntent = Intent(context, QuickEntryWidgetProvider::class.java).apply {
            action = ACTION_REFRESH_PROMPT
        }
        val refreshPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            refreshIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_refresh_button, refreshPendingIntent)
        
        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    
    override fun onEnabled(context: Context) {
        Timber.d("QuickEntryWidget enabled")
    }
    
    override fun onDisabled(context: Context) {
        Timber.d("QuickEntryWidget disabled")
    }
}
