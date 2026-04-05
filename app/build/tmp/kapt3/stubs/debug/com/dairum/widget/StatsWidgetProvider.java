package com.dairum.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.dairum.R;
import com.dairum.presentation.MainActivity;
import timber.log.Timber;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Stats Widget Provider.
 * Displays quick journal statistics on home screen.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0004\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u001a\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0004H\u0002J\u0010\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000e\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J \u0010\u0011\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J \u0010\u0016\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0006H\u0002\u00a8\u0006\u0019"}, d2 = {"Lcom/dairum/widget/StatsWidgetProvider;", "Landroid/appwidget/AppWidgetProvider;", "()V", "formatNumber", "", "num", "", "launchApp", "", "context", "Landroid/content/Context;", "destination", "onDisabled", "onEnabled", "onReceive", "intent", "Landroid/content/Intent;", "onUpdate", "appWidgetManager", "Landroid/appwidget/AppWidgetManager;", "appWidgetIds", "", "updateAppWidget", "appWidgetId", "Companion", "app_debug"})
public final class StatsWidgetProvider extends android.appwidget.AppWidgetProvider {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_OPEN_STATS = "com.dairum.widget.OPEN_STATS";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_OPEN_APP = "com.dairum.widget.OPEN_APP";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PREFS_NAME = "widget_stats_prefs";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_TOTAL_ENTRIES = "total_entries";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_CURRENT_STREAK = "current_streak";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_THIS_MONTH = "this_month";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_TOTAL_WORDS = "total_words";
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.widget.StatsWidgetProvider.Companion Companion = null;
    
    public StatsWidgetProvider() {
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
    
    private final void launchApp(android.content.Context context, java.lang.String destination) {
    }
    
    private final void updateAppWidget(android.content.Context context, android.appwidget.AppWidgetManager appWidgetManager, int appWidgetId) {
    }
    
    private final java.lang.String formatNumber(int num) {
        return null;
    }
    
    @java.lang.Override()
    public void onEnabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @java.lang.Override()
    public void onDisabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J.\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0010R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/dairum/widget/StatsWidgetProvider$Companion;", "", "()V", "ACTION_OPEN_APP", "", "ACTION_OPEN_STATS", "KEY_CURRENT_STREAK", "KEY_THIS_MONTH", "KEY_TOTAL_ENTRIES", "KEY_TOTAL_WORDS", "PREFS_NAME", "updateStats", "", "context", "Landroid/content/Context;", "totalEntries", "", "currentStreak", "thisMonth", "totalWords", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Update widget stats from app
         */
        public final void updateStats(@org.jetbrains.annotations.NotNull()
        android.content.Context context, int totalEntries, int currentStreak, int thisMonth, int totalWords) {
        }
    }
}