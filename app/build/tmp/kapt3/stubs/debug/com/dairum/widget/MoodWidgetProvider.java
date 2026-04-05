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
 * Mood Widget Provider.
 * Provides quick mood logging from home screen.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0018\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rH\u0016J \u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J(\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\bH\u0002J \u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\bH\u0002\u00a8\u0006\u001a"}, d2 = {"Lcom/dairum/widget/MoodWidgetProvider;", "Landroid/appwidget/AppWidgetProvider;", "()V", "launchWithMood", "", "context", "Landroid/content/Context;", "moodLevel", "", "onDisabled", "onEnabled", "onReceive", "intent", "Landroid/content/Intent;", "onUpdate", "appWidgetManager", "Landroid/appwidget/AppWidgetManager;", "appWidgetIds", "", "setupMoodButton", "views", "Landroid/widget/RemoteViews;", "viewId", "updateAppWidget", "appWidgetId", "Companion", "app_debug"})
public final class MoodWidgetProvider extends android.appwidget.AppWidgetProvider {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_LOG_MOOD = "com.dairum.widget.LOG_MOOD";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_MOOD_LEVEL = "mood_level";
    public static final int MOOD_VERY_SAD = 1;
    public static final int MOOD_SAD = 2;
    public static final int MOOD_NEUTRAL = 3;
    public static final int MOOD_HAPPY = 4;
    public static final int MOOD_VERY_HAPPY = 5;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.widget.MoodWidgetProvider.Companion Companion = null;
    
    public MoodWidgetProvider() {
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
    
    private final void launchWithMood(android.content.Context context, int moodLevel) {
    }
    
    private final void updateAppWidget(android.content.Context context, android.appwidget.AppWidgetManager appWidgetManager, int appWidgetId) {
    }
    
    private final void setupMoodButton(android.content.Context context, android.widget.RemoteViews views, int viewId, int moodLevel) {
    }
    
    @java.lang.Override()
    public void onEnabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @java.lang.Override()
    public void onDisabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/dairum/widget/MoodWidgetProvider$Companion;", "", "()V", "ACTION_LOG_MOOD", "", "EXTRA_MOOD_LEVEL", "MOOD_HAPPY", "", "MOOD_NEUTRAL", "MOOD_SAD", "MOOD_VERY_HAPPY", "MOOD_VERY_SAD", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}