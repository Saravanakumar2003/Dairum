package com.dairum.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dairum.data.local.converter.Converters
import com.dairum.data.local.dao.*
import com.dairum.data.local.entity.*

@Database(
    entities = [
        JournalEntryEntity::class,
        CategoryEntity::class,
        TagEntity::class,
        EntryTagCrossRef::class,
        MediaEntity::class,
        CustomTrackerEntity::class,
        TrackerValueEntity::class,
        PersonEntity::class,
        EntryPersonCrossRef::class,
        ExportConfigEntity::class,
        ExportHistoryEntity::class,
        ReminderConfigEntity::class,
        SmartReminderEntity::class,
        NotificationHistoryEntity::class,
        MemoryEntity::class,
        MemoryEntryCrossRef::class,
        MemoryMilestoneEntity::class,
        MemoryTrackerEntity::class,
        MoodCheckInConfigEntity::class,
        // AI entities
        AIModelEntity::class,
        JournalEmbeddingEntity::class,
        WeeklyReflectionEntity::class,
        PatternResultEntity::class,
        SmartSummaryEntity::class,
        AIConversationEntity::class,
        // Conversation entities
        ChatSessionEntity::class,
        ChatMessageEntity::class,
        ChatEmbeddingEntity::class,
        ReflectionMemoryEntity::class,
        ConversationContextEntity::class,
        // Gratia entities
        GratitudeFolderEntity::class,
        GratitudePromptEntity::class,
        AffirmationFolderEntity::class,
        AffirmationEntity::class,
        // Location nicknames
        LocationNicknameEntity::class
    ],
    version = 5,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class DairumDatabase : RoomDatabase() {
    
    abstract fun journalEntryDao(): JournalEntryDao
    abstract fun categoryDao(): CategoryDao
    abstract fun tagDao(): TagDao
    abstract fun mediaDao(): MediaDao
    abstract fun personDao(): PersonDao
    abstract fun trackerDao(): TrackerDao
    abstract fun exportDao(): ExportDao
    abstract fun notificationDao(): NotificationDao
    abstract fun memoryDao(): MemoryDao
    
    // AI DAOs
    abstract fun aiModelDao(): AIModelDao
    abstract fun journalEmbeddingDao(): JournalEmbeddingDao
    abstract fun weeklyReflectionDao(): WeeklyReflectionDao
    abstract fun patternResultDao(): PatternResultDao
    abstract fun smartSummaryDao(): SmartSummaryDao
    abstract fun aiConversationDao(): AIConversationDao
    
    // Conversation DAOs
    abstract fun chatSessionDao(): ChatSessionDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun chatEmbeddingDao(): ChatEmbeddingDao
    abstract fun reflectionMemoryDao(): ReflectionMemoryDao
    abstract fun conversationContextDao(): ConversationContextDao
    
    // Gratia DAOs
    abstract fun gratiaDao(): GratiaDao
    
    abstract fun locationNicknameDao(): LocationNicknameDao
    
    companion object {
        const val DATABASE_NAME = "dairum.db"
    }
}
