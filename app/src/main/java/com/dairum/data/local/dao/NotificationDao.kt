package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.*
import com.dairum.domain.model.ReminderType
import com.dairum.domain.model.SmartReminderType
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    
    // ============ REMINDER CONFIGS ============
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminderConfig(config: ReminderConfigEntity): Long
    
    @Update
    suspend fun updateReminderConfig(config: ReminderConfigEntity)
    
    @Query("DELETE FROM reminder_configs WHERE id = :id")
    suspend fun deleteReminderConfig(id: Long)
    
    @Query("SELECT * FROM reminder_configs WHERE id = :id")
    suspend fun getReminderConfigById(id: Long): ReminderConfigEntity?
    
    @Query("SELECT * FROM reminder_configs ORDER BY createdAt DESC")
    fun getAllReminderConfigsFlow(): Flow<List<ReminderConfigEntity>>
    
    @Query("SELECT * FROM reminder_configs WHERE isEnabled = 1")
    suspend fun getEnabledReminderConfigs(): List<ReminderConfigEntity>
    
    @Query("SELECT * FROM reminder_configs WHERE type = :type")
    suspend fun getReminderConfigsByType(type: ReminderType): List<ReminderConfigEntity>
    
    @Query("UPDATE reminder_configs SET isEnabled = :isEnabled, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setReminderEnabled(id: Long, isEnabled: Boolean, updatedAt: Long = System.currentTimeMillis())
    
    // ============ SMART REMINDERS ============
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSmartReminder(reminder: SmartReminderEntity): Long
    
    @Update
    suspend fun updateSmartReminder(reminder: SmartReminderEntity)
    
    @Query("DELETE FROM smart_reminders WHERE id = :id")
    suspend fun deleteSmartReminder(id: Long)
    
    @Query("SELECT * FROM smart_reminders WHERE id = :id")
    suspend fun getSmartReminderById(id: Long): SmartReminderEntity?
    
    @Query("SELECT * FROM smart_reminders ORDER BY reminderDate ASC")
    fun getAllSmartRemindersFlow(): Flow<List<SmartReminderEntity>>
    
    @Query("SELECT * FROM smart_reminders WHERE isEnabled = 1 ORDER BY reminderDate ASC")
    fun getEnabledSmartRemindersFlow(): Flow<List<SmartReminderEntity>>
    
    @Query("SELECT * FROM smart_reminders WHERE reminderType = :type ORDER BY reminderDate ASC")
    fun getSmartRemindersByTypeFlow(type: SmartReminderType): Flow<List<SmartReminderEntity>>
    
    @Query("SELECT * FROM smart_reminders WHERE relatedPersonId = :personId")
    suspend fun getSmartRemindersForPerson(personId: Long): List<SmartReminderEntity>
    
    @Query("""
        SELECT * FROM smart_reminders 
        WHERE isEnabled = 1 
        AND reminderDate BETWEEN :startTime AND :endTime
    """)
    suspend fun getUpcomingSmartReminders(startTime: Long, endTime: Long): List<SmartReminderEntity>
    
    @Query("UPDATE smart_reminders SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun setSmartReminderEnabled(id: Long, isEnabled: Boolean)
    
    @Query("UPDATE smart_reminders SET lastNotifiedAt = :timestamp WHERE id = :id")
    suspend fun updateLastNotified(id: Long, timestamp: Long)
    
    // Birthday and Anniversary queries
    @Query("""
        SELECT * FROM smart_reminders 
        WHERE reminderType IN ('BIRTHDAY', 'ANNIVERSARY') 
        AND isEnabled = 1 
        AND repeatYearly = 1
        ORDER BY reminderDate ASC
    """)
    fun getAnniversaryRemindersFlow(): Flow<List<SmartReminderEntity>>
    
    /**
     * Get all enabled smart reminders (for recurrence-aware processing).
     * Recurring reminders are fetched unfiltered by date so the worker can
     * adjust to the current year before deciding whether to fire.
     */
    @Query("""
        SELECT * FROM smart_reminders 
        WHERE isEnabled = 1
        ORDER BY reminderDate ASC
    """)
    suspend fun getAllEnabledSmartReminders(): List<SmartReminderEntity>
    
    // ============ NOTIFICATION HISTORY ============
    
    @Insert
    suspend fun insertNotificationHistory(history: NotificationHistoryEntity): Long
    
    @Query("SELECT * FROM notification_history ORDER BY sentAt DESC LIMIT :limit")
    suspend fun getRecentNotificationHistory(limit: Int): List<NotificationHistoryEntity>
    
    @Query("SELECT * FROM notification_history ORDER BY sentAt DESC")
    fun getNotificationHistoryFlow(): Flow<List<NotificationHistoryEntity>>
    
    @Query("UPDATE notification_history SET wasTapped = 1 WHERE id = :id")
    suspend fun markNotificationTapped(id: Long)
    
    @Query("DELETE FROM notification_history WHERE sentAt < :beforeTimestamp")
    suspend fun clearOldNotificationHistory(beforeTimestamp: Long)
    
    @Query("SELECT COUNT(*) FROM notification_history WHERE sentAt >= :sinceTimestamp")
    suspend fun getNotificationCountSince(sinceTimestamp: Long): Int
    
    // ============ MOOD CHECK-IN CONFIG ============
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodCheckInConfig(config: MoodCheckInConfigEntity): Long
    
    @Update
    suspend fun updateMoodCheckInConfig(config: MoodCheckInConfigEntity)
    
    @Query("SELECT * FROM mood_checkin_config LIMIT 1")
    suspend fun getMoodCheckInConfig(): MoodCheckInConfigEntity?
    
    @Query("SELECT * FROM mood_checkin_config LIMIT 1")
    fun getMoodCheckInConfigFlow(): Flow<MoodCheckInConfigEntity?>
    
    @Query("UPDATE mood_checkin_config SET lastPromptAt = :timestamp, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateMoodCheckInLastPrompt(id: Long, timestamp: Long, updatedAt: Long = System.currentTimeMillis())
}
