package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\t\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\t\u001a\u00020\nH\u00a6@\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\rH\u00a6@\u00a2\u0006\u0002\u0010\u000eJ*\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00052\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u0014J&\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0018J.\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\rH\u00a6@\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u00052\u0006\u0010 \u001a\u00020!H\u00a6@\u00a2\u0006\u0002\u0010\"J\u0016\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020%H\u00a6@\u00a2\u0006\u0002\u0010&J\u0016\u0010\'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010)\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010*\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u0014\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020!0-0,H&J\u0014\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0-0,H&J\u0014\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0-0,H&J\u0014\u00100\u001a\b\u0012\u0004\u0012\u00020!0-H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u0014\u00101\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0-0,H&J\u0010\u00102\u001a\u0004\u0018\u000103H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u0014\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002050-0,H&J\u001e\u00106\u001a\b\u0012\u0004\u0012\u0002050-2\b\b\u0002\u00107\u001a\u00020\rH\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0018\u00108\u001a\u0004\u0018\u00010!2\u0006\u0010(\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0018\u00109\u001a\u0004\u0018\u00010%2\u0006\u0010(\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010:\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0-0,2\u0006\u0010;\u001a\u00020<H&J\u001e\u0010=\u001a\b\u0012\u0004\u0012\u00020%0-2\b\b\u0002\u0010>\u001a\u00020\rH\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\b\u0010?\u001a\u00020\nH&J.\u0010@\u001a\u00020\u00032\u0006\u0010;\u001a\u00020\u00112\u0006\u0010A\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010B\u001a\u00020\u0011H\u00a6@\u00a2\u0006\u0002\u0010CJ\u0016\u0010D\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010E\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u000e\u0010F\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u000e\u0010G\u001a\u00020HH\u00a6@\u00a2\u0006\u0002\u0010\bJ\u001e\u0010I\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u00052\u0006\u0010J\u001a\u00020\nH\u00a6@\u00a2\u0006\u0002\u0010KJ\u001e\u0010L\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u00052\u0006\u0010J\u001a\u00020\nH\u00a6@\u00a2\u0006\u0002\u0010KJ\u0016\u0010M\u001a\u00020\u00032\u0006\u0010 \u001a\u000203H\u00a6@\u00a2\u0006\u0002\u0010NJ\u0016\u0010O\u001a\u00020\u00032\u0006\u0010 \u001a\u00020!H\u00a6@\u00a2\u0006\u0002\u0010\"J\u0016\u0010P\u001a\u00020\u00032\u0006\u0010$\u001a\u00020%H\u00a6@\u00a2\u0006\u0002\u0010&\u00a8\u0006Q"}, d2 = {"Lcom/dairum/domain/repository/NotificationRepository;", "", "cancelReminderNotification", "", "reminderId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkPersonMentionGaps", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkStreakRisk", "", "clearOldHistory", "daysToKeep", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createAnniversaryReminder", "title", "", "date", "description", "(Ljava/lang/String;JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createBirthdayReminder", "personId", "name", "(JLjava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createMoodCheckInReminder", "enabled", "hour", "minute", "intervalHours", "(ZIIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createReminderConfig", "config", "Lcom/dairum/domain/model/ReminderConfig;", "(Lcom/dairum/domain/model/ReminderConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createSmartReminder", "reminder", "Lcom/dairum/domain/model/SmartReminder;", "(Lcom/dairum/domain/model/SmartReminder;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteReminderConfig", "id", "deleteSmartReminder", "generateMemoryAnniversaryReminders", "getAllReminderConfigsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getAllSmartRemindersFlow", "getAnniversaryRemindersFlow", "getEnabledReminderConfigs", "getEnabledSmartRemindersFlow", "getMoodCheckInConfig", "Lcom/dairum/domain/repository/MoodCheckInConfig;", "getNotificationHistoryFlow", "Lcom/dairum/domain/model/NotificationHistory;", "getRecentNotificationHistory", "limit", "getReminderConfigById", "getSmartReminderById", "getSmartRemindersByTypeFlow", "type", "Lcom/dairum/domain/model/SmartReminderType;", "getUpcomingSmartReminders", "daysAhead", "hasNotificationPermission", "logNotification", "sourceId", "message", "(Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markNotificationTapped", "scheduleReminderNotifications", "scheduleSmartReminders", "sendTestNotification", "Lcom/dairum/domain/repository/NotificationResult;", "setReminderEnabled", "isEnabled", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setSmartReminderEnabled", "updateMoodCheckInConfig", "(Lcom/dairum/domain/repository/MoodCheckInConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateReminderConfig", "updateSmartReminder", "app_debug"})
public abstract interface NotificationRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createReminderConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReminderConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateReminderConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReminderConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteReminderConfig(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReminderConfigById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ReminderConfig> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ReminderConfig>> getAllReminderConfigsFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEnabledReminderConfigs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReminderConfig>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setReminderEnabled(long id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createSmartReminder(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartReminder reminder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSmartReminder(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartReminder reminder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSmartReminder(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSmartReminderById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.SmartReminder> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartReminder>> getAllSmartRemindersFlow();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartReminder>> getEnabledSmartRemindersFlow();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartReminder>> getSmartRemindersByTypeFlow(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartReminderType type);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUpcomingSmartReminders(int daysAhead, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.SmartReminder>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setSmartReminderEnabled(long id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createBirthdayReminder(long personId, @org.jetbrains.annotations.NotNull()
    java.lang.String name, long date, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createAnniversaryReminder(@org.jetbrains.annotations.NotNull()
    java.lang.String title, long date, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartReminder>> getAnniversaryRemindersFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object generateMemoryAnniversaryReminders(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object checkPersonMentionGaps(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object checkStreakRisk(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object logNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String type, long sourceId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.NotificationHistory>> getNotificationHistoryFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentNotificationHistory(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.NotificationHistory>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markNotificationTapped(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearOldHistory(int daysToKeep, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object scheduleReminderNotifications(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object cancelReminderNotification(long reminderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object scheduleSmartReminders(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    public abstract boolean hasNotificationPermission();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sendTestNotification(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.repository.NotificationResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createMoodCheckInReminder(boolean enabled, int hour, int minute, int intervalHours, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMoodCheckInConfig(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.repository.MoodCheckInConfig> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMoodCheckInConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MoodCheckInConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}