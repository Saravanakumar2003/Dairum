package com.dairum.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

/**
 * Database migrations for Dairum.
 * Each migration should be carefully tested before release.
 */
object Migrations {
    
    /**
     * Migration from version 1 to 2
     * Added export and notification tables
     */
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            Timber.d("Running migration 1 -> 2")
            
            // Create export_config table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS export_config (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    format TEXT NOT NULL,
                    schedule TEXT,
                    includeMedia INTEGER NOT NULL DEFAULT 0,
                    includeLocation INTEGER NOT NULL DEFAULT 0,
                    includeWeather INTEGER NOT NULL DEFAULT 0,
                    dateRangeStart TEXT,
                    dateRangeEnd TEXT,
                    categoryIds TEXT,
                    tagIds TEXT,
                    lastExportedAt TEXT,
                    createdAt TEXT NOT NULL,
                    updatedAt TEXT NOT NULL
                )
            """.trimIndent())
            
            // Create export_history table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS export_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    configId INTEGER,
                    format TEXT NOT NULL,
                    filePath TEXT NOT NULL,
                    fileSize INTEGER NOT NULL,
                    entryCount INTEGER NOT NULL,
                    isSuccess INTEGER NOT NULL DEFAULT 1,
                    errorMessage TEXT,
                    exportedAt TEXT NOT NULL,
                    FOREIGN KEY(configId) REFERENCES export_config(id) ON DELETE SET NULL
                )
            """.trimIndent())
            
            // Create reminder_config table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS reminder_config (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    type TEXT NOT NULL,
                    name TEXT NOT NULL,
                    scheduleTime TEXT NOT NULL,
                    scheduleDays TEXT NOT NULL,
                    priority TEXT NOT NULL DEFAULT 'NORMAL',
                    isEnabled INTEGER NOT NULL DEFAULT 1,
                    createdAt TEXT NOT NULL,
                    updatedAt TEXT NOT NULL
                )
            """.trimIndent())
        }
    }
    
    /**
     * Migration from version 2 to 3
     * Added smart reminders and notification history
     */
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            Timber.d("Running migration 2 -> 3")
            
            // Create smart_reminder table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS smart_reminder (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    reminderType TEXT NOT NULL,
                    reminderDate TEXT NOT NULL,
                    relatedPersonId INTEGER,
                    relatedMemoryId INTEGER,
                    customMessage TEXT,
                    isProcessed INTEGER NOT NULL DEFAULT 0,
                    createdAt TEXT NOT NULL
                )
            """.trimIndent())
            
            // Create notification_history table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS notification_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    type TEXT NOT NULL,
                    sourceId INTEGER,
                    title TEXT NOT NULL,
                    message TEXT NOT NULL,
                    wasTapped INTEGER NOT NULL DEFAULT 0,
                    sentAt TEXT NOT NULL
                )
            """.trimIndent())
            
            // Create index on smart_reminder
            db.execSQL("CREATE INDEX IF NOT EXISTS index_smart_reminder_reminderDate ON smart_reminder(reminderDate)")
        }
    }
    
    /**
     * Migration from version 3 to 4
     * Added memory collections feature
     */
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            Timber.d("Running migration 3 -> 4")
            
            // Create memory table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS memory (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    title TEXT NOT NULL,
                    description TEXT,
                    coverImageUri TEXT,
                    startDate TEXT NOT NULL,
                    endDate TEXT,
                    location TEXT,
                    isFavorite INTEGER NOT NULL DEFAULT 0,
                    createdAt TEXT NOT NULL,
                    updatedAt TEXT NOT NULL
                )
            """.trimIndent())
            
            // Create memory_entry_cross_ref table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS memory_entry_cross_ref (
                    memoryId INTEGER NOT NULL,
                    entryId INTEGER NOT NULL,
                    sortOrder INTEGER NOT NULL DEFAULT 0,
                    addedAt TEXT NOT NULL,
                    PRIMARY KEY(memoryId, entryId),
                    FOREIGN KEY(memoryId) REFERENCES memory(id) ON DELETE CASCADE,
                    FOREIGN KEY(entryId) REFERENCES journal_entry(id) ON DELETE CASCADE
                )
            """.trimIndent())
            
            // Create memory_milestone table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS memory_milestone (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    memoryId INTEGER NOT NULL,
                    title TEXT NOT NULL,
                    description TEXT,
                    milestoneDate TEXT NOT NULL,
                    icon TEXT,
                    createdAt TEXT NOT NULL,
                    FOREIGN KEY(memoryId) REFERENCES memory(id) ON DELETE CASCADE
                )
            """.trimIndent())
            
            // Create memory_tracker table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS memory_tracker (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    memoryId INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    type TEXT NOT NULL,
                    goalValue TEXT,
                    currentValue TEXT,
                    unit TEXT,
                    icon TEXT,
                    createdAt TEXT NOT NULL,
                    updatedAt TEXT NOT NULL,
                    FOREIGN KEY(memoryId) REFERENCES memory(id) ON DELETE CASCADE
                )
            """.trimIndent())
            
            // Create indices
            db.execSQL("CREATE INDEX IF NOT EXISTS index_memory_entry_cross_ref_entryId ON memory_entry_cross_ref(entryId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_memory_milestone_memoryId ON memory_milestone(memoryId)")
        }
    }
    
    /**
     * Migration from version 4 to 5
     * Added mood check-in configuration
     */
    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            Timber.d("Running migration 4 -> 5")
            
            // Create mood_checkin_config table
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS mood_checkin_config (
                    id INTEGER PRIMARY KEY NOT NULL DEFAULT 1,
                    isEnabled INTEGER NOT NULL DEFAULT 0,
                    scheduleTime TEXT NOT NULL DEFAULT '20:00',
                    scheduleDays TEXT NOT NULL DEFAULT '["MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"]',
                    enableQuickMood INTEGER NOT NULL DEFAULT 1,
                    enableNote INTEGER NOT NULL DEFAULT 1,
                    createdAt TEXT NOT NULL,
                    updatedAt TEXT NOT NULL
                )
            """.trimIndent())
        }
    }
    
    /**
     * Migration from version 6 to 7
     * Added expanded Person fields: email, phone, birthday, importantDates
     */
    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            Timber.d("Running migration 6 -> 7")
            
            // Add new columns to people table
            db.execSQL("ALTER TABLE people ADD COLUMN email TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE people ADD COLUMN phone TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE people ADD COLUMN birthday INTEGER DEFAULT NULL")
            db.execSQL("ALTER TABLE people ADD COLUMN importantDates TEXT DEFAULT NULL")
        }
    }
    
    /**
     * Get all migrations
     */
    val ALL_MIGRATIONS = arrayOf(
        MIGRATION_1_2,
        MIGRATION_2_3,
        MIGRATION_3_4,
        MIGRATION_4_5,
        MIGRATION_6_7
    )
}
