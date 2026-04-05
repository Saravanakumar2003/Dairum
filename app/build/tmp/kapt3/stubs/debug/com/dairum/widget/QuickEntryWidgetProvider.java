package com.dairum.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.dairum.R;
import com.dairum.domain.model.WritingPrompts;
import com.dairum.presentation.MainActivity;
import timber.log.Timber;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Quick Entry Widget Provider.
 * Displays a daily writing prompt and allows quick entry creation.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0018\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rH\u0016J \u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J \u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0015H\u0002\u00a8\u0006\u0017"}, d2 = {"Lcom/dairum/widget/QuickEntryWidgetProvider;", "Landroid/appwidget/AppWidgetProvider;", "()V", "launchNewEntry", "", "context", "Landroid/content/Context;", "promptText", "", "onDisabled", "onEnabled", "onReceive", "intent", "Landroid/content/Intent;", "onUpdate", "appWidgetManager", "Landroid/appwidget/AppWidgetManager;", "appWidgetIds", "", "updateAppWidget", "appWidgetId", "", "Companion", "app_debug"})
public final class QuickEntryWidgetProvider extends android.appwidget.AppWidgetProvider {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_REFRESH_PROMPT = "com.dairum.widget.REFRESH_PROMPT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_NEW_ENTRY = "com.dairum.widget.NEW_ENTRY";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_NEW_ENTRY_WITH_PROMPT = "com.dairum.widget.NEW_ENTRY_WITH_PROMPT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PROMPT_TEXT = "prompt_text";
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.widget.QuickEntryWidgetProvider.Companion Companion = null;
    
    public QuickEntryWidgetProvider() {
        super();
    }
    
    @java.lang.Override()
    public void onUpdate(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.appwidget.AppWidgetManager appWidgetManager, @org.jetbrains.annotations.NotNull()
    int[] appWidgetIds) {
    }
    
    @java.lang.Override()
    public void onReceive(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
    
    private final void launchNewEntry(android.content.Context context, java.lang.String promptText) {
    }
    
    private final void updateAppWidget(android.content.Context context, android.appwidget.AppWidgetManager appWidgetManager, int appWidgetId) {
    }
    
    @java.lang.Override()
    public void onEnabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @java.lang.Override()
    public void onDisabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/dairum/widget/QuickEntryWidgetProvider$Companion;", "", "()V", "ACTION_NEW_ENTRY", "", "ACTION_NEW_ENTRY_WITH_PROMPT", "ACTION_REFRESH_PROMPT", "EXTRA_PROMPT_TEXT", "updateWidget", "", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Manually trigger widget update from app
         */
        public final void updateWidget(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
    }
}