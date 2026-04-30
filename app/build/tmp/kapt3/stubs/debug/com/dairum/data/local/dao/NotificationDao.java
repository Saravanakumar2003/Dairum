package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import com.dairum.domain.model.ReminderType;
import com.dairum.domain.model.SmartReminderType;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\f\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\t\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000b0\u000fH\'J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000fH\'J\u0014\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000fH\'J\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00100\u000bH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000fH\'J\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u000fH\'J\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u000b0\u000fH\'J\u001c\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001c0\u000b2\u0006\u0010\u001e\u001a\u00020\u0019H\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u0018\u0010 \u001a\u0004\u0018\u00010\u00102\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00100\u000b2\u0006\u0010\"\u001a\u00020#H\u00a7@\u00a2\u0006\u0002\u0010$J\u0018\u0010%\u001a\u0004\u0018\u00010\f2\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000f2\u0006\u0010\"\u001a\u00020\'H\'J\u001c\u0010(\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010)\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J$\u0010*\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010+\u001a\u00020\u00052\u0006\u0010,\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010-J\u0016\u0010.\u001a\u00020\u00052\u0006\u0010/\u001a\u00020\u0016H\u00a7@\u00a2\u0006\u0002\u00100J\u0016\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u001cH\u00a7@\u00a2\u0006\u0002\u00103J\u0016\u00104\u001a\u00020\u00052\u0006\u0010/\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u00105J\u0016\u00106\u001a\u00020\u00052\u0006\u00107\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u00108J\u0016\u00109\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J(\u0010:\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010;\u001a\u00020<2\b\b\u0002\u0010=\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010>J\u001e\u0010?\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010;\u001a\u00020<H\u00a7@\u00a2\u0006\u0002\u0010@J\u001e\u0010A\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010B\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010-J\u0016\u0010C\u001a\u00020\u00032\u0006\u0010/\u001a\u00020\u0016H\u00a7@\u00a2\u0006\u0002\u00100J(\u0010D\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010B\u001a\u00020\u00052\b\b\u0002\u0010=\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010EJ\u0016\u0010F\u001a\u00020\u00032\u0006\u0010/\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u00105J\u0016\u0010G\u001a\u00020\u00032\u0006\u00107\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u00108\u00a8\u0006H"}, d2 = {"Lcom/dairum/data/local/dao/NotificationDao;", "", "clearOldNotificationHistory", "", "beforeTimestamp", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteReminderConfig", "id", "deleteSmartReminder", "getAllEnabledSmartReminders", "", "Lcom/dairum/data/local/entity/SmartReminderEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllReminderConfigsFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/dairum/data/local/entity/ReminderConfigEntity;", "getAllSmartRemindersFlow", "getAnniversaryRemindersFlow", "getEnabledReminderConfigs", "getEnabledSmartRemindersFlow", "getMoodCheckInConfig", "Lcom/dairum/data/local/entity/MoodCheckInConfigEntity;", "getMoodCheckInConfigFlow", "getNotificationCountSince", "", "sinceTimestamp", "getNotificationHistoryFlow", "Lcom/dairum/data/local/entity/NotificationHistoryEntity;", "getRecentNotificationHistory", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReminderConfigById", "getReminderConfigsByType", "type", "Lcom/dairum/domain/model/ReminderType;", "(Lcom/dairum/domain/model/ReminderType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSmartReminderById", "getSmartRemindersByTypeFlow", "Lcom/dairum/domain/model/SmartReminderType;", "getSmartRemindersForPerson", "personId", "getUpcomingSmartReminders", "startTime", "endTime", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMoodCheckInConfig", "config", "(Lcom/dairum/data/local/entity/MoodCheckInConfigEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertNotificationHistory", "history", "(Lcom/dairum/data/local/entity/NotificationHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertReminderConfig", "(Lcom/dairum/data/local/entity/ReminderConfigEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertSmartReminder", "reminder", "(Lcom/dairum/data/local/entity/SmartReminderEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markNotificationTapped", "setReminderEnabled", "isEnabled", "", "updatedAt", "(JZJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setSmartReminderEnabled", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastNotified", "timestamp", "updateMoodCheckInConfig", "updateMoodCheckInLastPrompt", "(JJJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateReminderConfig", "updateSmartReminder", "app_debug"})
@androidx.room.Dao()
public abstract interface NotificationDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertReminderConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ReminderConfigEntity config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateReminderConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ReminderConfigEntity config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM reminder_configs WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteReminderConfig(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM reminder_configs WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReminderConfigById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.ReminderConfigEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM reminder_configs ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.ReminderConfigEntity>> getAllReminderConfigsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM reminder_configs WHERE isEnabled = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEnabledReminderConfigs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ReminderConfigEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM reminder_configs WHERE type = :type")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReminderConfigsByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReminderType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ReminderConfigEntity>> $completion);
    
    @androidx.room.Query(value = "UPDATE reminder_configs SET isEnabled = :isEnabled, updatedAt = :updatedAt WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setReminderEnabled(long id, boolean isEnabled, long updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertSmartReminder(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.SmartReminderEntity reminder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSmartReminder(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.SmartReminderEntity reminder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM smart_reminders WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSmartReminder(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM smart_reminders WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSmartReminderById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.SmartReminderEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM smart_reminders ORDER BY reminderDate ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.SmartReminderEntity>> getAllSmartRemindersFlow();
    
    @androidx.room.Query(value = "SELECT * FROM smart_reminders WHERE isEnabled = 1 ORDER BY reminderDate ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.SmartReminderEntity>> getEnabledSmartRemindersFlow();
    
    @androidx.room.Query(value = "SELECT * FROM smart_reminders WHERE reminderType = :type ORDER BY reminderDate ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.SmartReminderEntity>> getSmartRemindersByTypeFlow(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartReminderType type);
    
    @androidx.room.Query(value = "SELECT * FROM smart_reminders WHERE relatedPersonId = :personId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSmartRemindersForPerson(long personId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.SmartReminderEntity>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM smart_reminders \n        WHERE isEnabled = 1 \n        AND reminderDate BETWEEN :startTime AND :endTime\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUpcomingSmartReminders(long startTime, long endTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.SmartReminderEntity>> $completion);
    
    @androidx.room.Query(value = "UPDATE smart_reminders SET isEnabled = :isEnabled WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setSmartReminderEnabled(long id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE smart_reminders SET lastNotifiedAt = :timestamp WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastNotified(long id, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM smart_reminders \n        WHERE reminderType IN (\'BIRTHDAY\', \'ANNIVERSARY\') \n        AND isEnabled = 1 \n        AND repeatYearly = 1\n        ORDER BY reminderDate ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.SmartReminderEntity>> getAnniversaryRemindersFlow();
    
    /**
     * Get all enabled smart reminders (for recurrence-aware processing).
     * Recurring reminders are fetched unfiltered by date so the worker can
     * adjust to the current year before deciding whether to fire.
     */
    @androidx.room.Query(value = "\n        SELECT * FROM smart_reminders \n        WHERE isEnabled = 1\n        ORDER BY reminderDate ASC\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllEnabledSmartReminders(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.SmartReminderEntity>> $completion);
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertNotificationHistory(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.NotificationHistoryEntity history, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM notification_history ORDER BY sentAt DESC LIMIT :limit")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentNotificationHistory(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.NotificationHistoryEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM notification_history ORDER BY sentAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.NotificationHistoryEntity>> getNotificationHistoryFlow();
    
    @androidx.room.Query(value = "UPDATE notification_history SET wasTapped = 1 WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markNotificationTapped(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM notification_history WHERE sentAt < :beforeTimestamp")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearOldNotificationHistory(long beforeTimestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM notification_history WHERE sentAt >= :sinceTimestamp")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getNotificationCountSince(long sinceTimestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMoodCheckInConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MoodCheckInConfigEntity config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMoodCheckInConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MoodCheckInConfigEntity config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM mood_checkin_config LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMoodCheckInConfig(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.MoodCheckInConfigEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM mood_checkin_config LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.data.local.entity.MoodCheckInConfigEntity> getMoodCheckInConfigFlow();
    
    @androidx.room.Query(value = "UPDATE mood_checkin_config SET lastPromptAt = :timestamp, updatedAt = :updatedAt WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMoodCheckInLastPrompt(long id, long timestamp, long updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}