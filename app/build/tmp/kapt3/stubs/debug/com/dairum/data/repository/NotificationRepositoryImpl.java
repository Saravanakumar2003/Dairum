package com.dairum.data.repository;

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
import androidx.work.*;
import com.dairum.R;
import com.dairum.data.local.dao.NotificationDao;
import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.PersonDao;
import com.dairum.data.local.entity.*;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.MoodCheckInConfig;
import com.dairum.domain.repository.NotificationRepository;
import com.dairum.domain.repository.NotificationResult;
import com.dairum.data.service.MoodCheckInWorker;
import com.dairum.data.service.ReminderWorker;
import com.dairum.data.service.SmartReminderWorker;
import com.dairum.presentation.MainActivity;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.Flow;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u009e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 f2\u00020\u0001:\u0001fB)\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\u0016\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u000e\u0010\u0014\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u000e\u0010\u0016\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ(\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000e2\b\u0010 \u001a\u0004\u0018\u00010\u001eH\u0096@\u00a2\u0006\u0002\u0010!J&\u0010\"\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020\u000e2\u0006\u0010$\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010%J.\u0010&\u001a\u00020\u00102\u0006\u0010\'\u001a\u00020\u00172\u0006\u0010(\u001a\u00020\u001a2\u0006\u0010)\u001a\u00020\u001a2\u0006\u0010*\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010+J\b\u0010,\u001a\u00020\u0010H\u0002J\u0016\u0010-\u001a\u00020\u000e2\u0006\u0010.\u001a\u00020/H\u0096@\u00a2\u0006\u0002\u00100J\u0016\u00101\u001a\u00020\u000e2\u0006\u00102\u001a\u000203H\u0096@\u00a2\u0006\u0002\u00104J\u0016\u00105\u001a\u00020\u00102\u0006\u00106\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0016\u00107\u001a\u00020\u00102\u0006\u00106\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u000e\u00108\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u0014\u00109\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020/0;0:H\u0016J\u0014\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030;0:H\u0016J\u0014\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030;0:H\u0016J\u0014\u0010>\u001a\b\u0012\u0004\u0012\u00020/0;H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u0014\u0010?\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030;0:H\u0016J\u0010\u0010@\u001a\u0004\u0018\u00010AH\u0096@\u00a2\u0006\u0002\u0010\u0015J\u0014\u0010B\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020C0;0:H\u0016J\u001c\u0010D\u001a\b\u0012\u0004\u0012\u00020C0;2\u0006\u0010E\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0018\u0010F\u001a\u0004\u0018\u00010/2\u0006\u00106\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0018\u0010G\u001a\u0004\u0018\u0001032\u0006\u00106\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u001c\u0010H\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030;0:2\u0006\u0010I\u001a\u00020JH\u0016J\u001c\u0010K\u001a\b\u0012\u0004\u0012\u0002030;2\u0006\u0010L\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\b\u0010M\u001a\u00020\u0017H\u0016J.\u0010N\u001a\u00020\u00102\u0006\u0010I\u001a\u00020\u001e2\u0006\u0010O\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010P\u001a\u00020\u001eH\u0096@\u00a2\u0006\u0002\u0010QJ\u0016\u0010R\u001a\u00020\u00102\u0006\u00106\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0013J\b\u0010S\u001a\u00020\u0010H\u0002J\u000e\u0010T\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u000e\u0010U\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u000e\u0010V\u001a\u00020WH\u0096@\u00a2\u0006\u0002\u0010\u0015J\u001e\u0010X\u001a\u00020\u00102\u0006\u00106\u001a\u00020\u000e2\u0006\u0010Y\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010ZJ\u001e\u0010[\u001a\u00020\u00102\u0006\u00106\u001a\u00020\u000e2\u0006\u0010Y\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010ZJ\u0016\u0010\\\u001a\u00020\u00102\u0006\u0010.\u001a\u00020AH\u0096@\u00a2\u0006\u0002\u0010]J\u0016\u0010^\u001a\u00020\u00102\u0006\u0010.\u001a\u00020/H\u0096@\u00a2\u0006\u0002\u00100J\u0016\u0010_\u001a\u00020\u00102\u0006\u00102\u001a\u000203H\u0096@\u00a2\u0006\u0002\u00104J\f\u0010`\u001a\u00020A*\u00020aH\u0002J\f\u0010`\u001a\u00020C*\u00020bH\u0002J\f\u0010`\u001a\u00020/*\u00020cH\u0002J\f\u0010`\u001a\u000203*\u00020dH\u0002J\f\u0010e\u001a\u00020c*\u00020/H\u0002J\f\u0010e\u001a\u00020d*\u000203H\u0002J\f\u0010e\u001a\u00020a*\u00020AH\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006g"}, d2 = {"Lcom/dairum/data/repository/NotificationRepositoryImpl;", "Lcom/dairum/domain/repository/NotificationRepository;", "notificationDao", "Lcom/dairum/data/local/dao/NotificationDao;", "journalEntryDao", "Lcom/dairum/data/local/dao/JournalEntryDao;", "personDao", "Lcom/dairum/data/local/dao/PersonDao;", "context", "Landroid/content/Context;", "(Lcom/dairum/data/local/dao/NotificationDao;Lcom/dairum/data/local/dao/JournalEntryDao;Lcom/dairum/data/local/dao/PersonDao;Landroid/content/Context;)V", "workManager", "Landroidx/work/WorkManager;", "calculateInitialDelay", "", "cancelMoodCheckInReminders", "", "cancelReminderNotification", "reminderId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkPersonMentionGaps", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkStreakRisk", "", "clearOldHistory", "daysToKeep", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createAnniversaryReminder", "title", "", "date", "description", "(Ljava/lang/String;JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createBirthdayReminder", "personId", "name", "(JLjava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createMoodCheckInReminder", "enabled", "hour", "minute", "intervalHours", "(ZIIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createNotificationChannel", "createReminderConfig", "config", "Lcom/dairum/domain/model/ReminderConfig;", "(Lcom/dairum/domain/model/ReminderConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createSmartReminder", "reminder", "Lcom/dairum/domain/model/SmartReminder;", "(Lcom/dairum/domain/model/SmartReminder;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteReminderConfig", "id", "deleteSmartReminder", "generateMemoryAnniversaryReminders", "getAllReminderConfigsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getAllSmartRemindersFlow", "getAnniversaryRemindersFlow", "getEnabledReminderConfigs", "getEnabledSmartRemindersFlow", "getMoodCheckInConfig", "Lcom/dairum/domain/repository/MoodCheckInConfig;", "getNotificationHistoryFlow", "Lcom/dairum/domain/model/NotificationHistory;", "getRecentNotificationHistory", "limit", "getReminderConfigById", "getSmartReminderById", "getSmartRemindersByTypeFlow", "type", "Lcom/dairum/domain/model/SmartReminderType;", "getUpcomingSmartReminders", "daysAhead", "hasNotificationPermission", "logNotification", "sourceId", "message", "(Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markNotificationTapped", "scheduleMoodCheckInReminders", "scheduleReminderNotifications", "scheduleSmartReminders", "sendTestNotification", "Lcom/dairum/domain/repository/NotificationResult;", "setReminderEnabled", "isEnabled", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setSmartReminderEnabled", "updateMoodCheckInConfig", "(Lcom/dairum/domain/repository/MoodCheckInConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateReminderConfig", "updateSmartReminder", "toDomain", "Lcom/dairum/data/local/entity/MoodCheckInConfigEntity;", "Lcom/dairum/data/local/entity/NotificationHistoryEntity;", "Lcom/dairum/data/local/entity/ReminderConfigEntity;", "Lcom/dairum/data/local/entity/SmartReminderEntity;", "toEntity", "Companion", "app_debug"})
public final class NotificationRepositoryImpl implements com.dairum.domain.repository.NotificationRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.NotificationDao notificationDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.JournalEntryDao journalEntryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.PersonDao personDao = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.work.WorkManager workManager = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String REMINDER_WORK_NAME = "dairum_reminder_work";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String SMART_REMINDER_WORK_NAME = "dairum_smart_reminder_work";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MOOD_CHECKIN_WORK_NAME = "dairum_mood_checkin_work";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MOOD_CHECKIN_WORK_TAG = "mood_checkin_tag";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TEST_CHANNEL_ID = "dairum_test_channel";
    private static final int TEST_NOTIFICATION_ID = 99999;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.repository.NotificationRepositoryImpl.Companion Companion = null;
    
    @javax.inject.Inject()
    public NotificationRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.NotificationDao notificationDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.JournalEntryDao journalEntryDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.PersonDao personDao, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createReminderConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReminderConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateReminderConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReminderConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteReminderConfig(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getReminderConfigById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ReminderConfig> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ReminderConfig>> getAllReminderConfigsFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEnabledReminderConfigs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReminderConfig>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setReminderEnabled(long id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createSmartReminder(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartReminder reminder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateSmartReminder(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartReminder reminder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteSmartReminder(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getSmartReminderById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.SmartReminder> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartReminder>> getAllSmartRemindersFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartReminder>> getEnabledSmartRemindersFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartReminder>> getSmartRemindersByTypeFlow(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartReminderType type) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getUpcomingSmartReminders(int daysAhead, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.SmartReminder>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setSmartReminderEnabled(long id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createBirthdayReminder(long personId, @org.jetbrains.annotations.NotNull()
    java.lang.String name, long date, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createAnniversaryReminder(@org.jetbrains.annotations.NotNull()
    java.lang.String title, long date, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartReminder>> getAnniversaryRemindersFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object generateMemoryAnniversaryReminders(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object checkPersonMentionGaps(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object checkStreakRisk(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object logNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String type, long sourceId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.NotificationHistory>> getNotificationHistoryFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getRecentNotificationHistory(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.NotificationHistory>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object markNotificationTapped(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearOldHistory(int daysToKeep, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object scheduleReminderNotifications(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object cancelReminderNotification(long reminderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object scheduleSmartReminders(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final long calculateInitialDelay() {
        return 0L;
    }
    
    private final com.dairum.data.local.entity.ReminderConfigEntity toEntity(com.dairum.domain.model.ReminderConfig $this$toEntity) {
        return null;
    }
    
    private final com.dairum.domain.model.ReminderConfig toDomain(com.dairum.data.local.entity.ReminderConfigEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.SmartReminderEntity toEntity(com.dairum.domain.model.SmartReminder $this$toEntity) {
        return null;
    }
    
    private final com.dairum.domain.model.SmartReminder toDomain(com.dairum.data.local.entity.SmartReminderEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.NotificationHistory toDomain(com.dairum.data.local.entity.NotificationHistoryEntity $this$toDomain) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object sendTestNotification(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.repository.NotificationResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public boolean hasNotificationPermission() {
        return false;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createMoodCheckInReminder(boolean enabled, int hour, int minute, int intervalHours, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getMoodCheckInConfig(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.repository.MoodCheckInConfig> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateMoodCheckInConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MoodCheckInConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void scheduleMoodCheckInReminders() {
    }
    
    private final void cancelMoodCheckInReminders() {
    }
    
    private final com.dairum.domain.repository.MoodCheckInConfig toDomain(com.dairum.data.local.entity.MoodCheckInConfigEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.MoodCheckInConfigEntity toEntity(com.dairum.domain.repository.MoodCheckInConfig $this$toEntity) {
        return null;
    }
    
    private final void createNotificationChannel() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/dairum/data/repository/NotificationRepositoryImpl$Companion;", "", "()V", "MOOD_CHECKIN_WORK_NAME", "", "MOOD_CHECKIN_WORK_TAG", "REMINDER_WORK_NAME", "SMART_REMINDER_WORK_NAME", "TEST_CHANNEL_ID", "TEST_NOTIFICATION_ID", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}