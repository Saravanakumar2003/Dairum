package com.dairum.data.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.hilt.work.HiltWorker;
import androidx.work.CoroutineWorker;
import androidx.work.WorkerParameters;
import com.dairum.R;
import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.NotificationDao;
import com.dairum.data.local.entity.NotificationHistoryEntity;
import com.dairum.domain.model.SmartReminderType;
import com.dairum.presentation.MainActivity;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000  2\u00020\u0001:\u0001 B+\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0002J\u000e\u0010\u0010\u001a\u00020\u0011H\u0082@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\u00020\u0011H\u0082@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0014\u001a\u00020\u0011H\u0082@\u00a2\u0006\u0002\u0010\u0012J\b\u0010\u0015\u001a\u00020\u0011H\u0002J\u000e\u0010\u0016\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010\u0012J\b\u0010\u0018\u001a\u00020\u0019H\u0002J(\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/dairum/data/service/SmartReminderWorker;", "Landroidx/work/CoroutineWorker;", "context", "Landroid/content/Context;", "workerParams", "Landroidx/work/WorkerParameters;", "notificationDao", "Lcom/dairum/data/local/dao/NotificationDao;", "journalEntryDao", "Lcom/dairum/data/local/dao/JournalEntryDao;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;Lcom/dairum/data/local/dao/NotificationDao;Lcom/dairum/data/local/dao/JournalEntryDao;)V", "calculateCurrentStreak", "", "dates", "", "", "checkBirthdaysAndAnniversaries", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkMemoryAnniversaries", "checkStreakRisk", "createNotificationChannels", "doWork", "Landroidx/work/ListenableWorker$Result;", "hasNotificationPermission", "", "sendSmartReminderNotification", "id", "title", "message", "type", "Lcom/dairum/domain/model/SmartReminderType;", "Companion", "app_debug"})
@androidx.hilt.work.HiltWorker()
public final class SmartReminderWorker extends androidx.work.CoroutineWorker {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.NotificationDao notificationDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.JournalEntryDao journalEntryDao = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SMART_REMINDER_CHANNEL_ID = "dairum_smart_reminders";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MEMORY_CHANNEL_ID = "dairum_memories";
    public static final int SMART_NOTIFICATION_BASE_ID = 10000;
    public static final int MEMORY_NOTIFICATION_ID = 20000;
    public static final int STREAK_NOTIFICATION_ID = 20001;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.service.SmartReminderWorker.Companion Companion = null;
    
    @dagger.assisted.AssistedInject()
    public SmartReminderWorker(@dagger.assisted.Assisted()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @dagger.assisted.Assisted()
    @org.jetbrains.annotations.NotNull()
    androidx.work.WorkerParameters workerParams, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.NotificationDao notificationDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.JournalEntryDao journalEntryDao) {
        super(null, null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object doWork(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> $completion) {
        return null;
    }
    
    private final java.lang.Object checkBirthdaysAndAnniversaries(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object checkMemoryAnniversaries(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object checkStreakRisk(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final int calculateCurrentStreak(java.util.List<java.lang.String> dates) {
        return 0;
    }
    
    private final void createNotificationChannels() {
    }
    
    private final void sendSmartReminderNotification(int id, java.lang.String title, java.lang.String message, com.dairum.domain.model.SmartReminderType type) {
    }
    
    private final boolean hasNotificationPermission() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/dairum/data/service/SmartReminderWorker$Companion;", "", "()V", "MEMORY_CHANNEL_ID", "", "MEMORY_NOTIFICATION_ID", "", "SMART_NOTIFICATION_BASE_ID", "SMART_REMINDER_CHANNEL_ID", "STREAK_NOTIFICATION_ID", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}