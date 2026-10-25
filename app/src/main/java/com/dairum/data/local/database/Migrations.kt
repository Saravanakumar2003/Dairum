package com.dairum.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

/**
 * Database migrations for Dairum.
 *
 * Version 1 = clean-slate schema matching all current Room entities.
 * Add future migrations here as MIGRATION_1_2, MIGRATION_2_3, etc.
 */
object Migrations {

    /**
     * All migrations to register with Room.
     * Currently empty because version 1 is the first shipped schema.
     */
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            Timber.d("Migrating database from version 1 to 2")
            db.execSQL("ALTER TABLE journal_entries ADD COLUMN deletedAt INTEGER DEFAULT NULL")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            Timber.d("Migrating database from version 2 to 3")
            // Gratitude folders
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS gratitude_folders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    emoji TEXT NOT NULL DEFAULT '🙏',
                    description TEXT,
                    createdAt INTEGER NOT NULL DEFAULT 0
                )
            """)
            // Gratitude prompts
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS gratitude_prompts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    folderId INTEGER NOT NULL,
                    prompt TEXT NOT NULL,
                    createdAt INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY (folderId) REFERENCES gratitude_folders(id) ON DELETE CASCADE
                )
            """)
            db.execSQL("CREATE INDEX IF NOT EXISTS index_gratitude_prompts_folderId ON gratitude_prompts(folderId)")
            // Affirmation folders
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS affirmation_folders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    emoji TEXT NOT NULL DEFAULT '✨',
                    description TEXT,
                    backgroundAudioUri TEXT,
                    createdAt INTEGER NOT NULL DEFAULT 0
                )
            """)
            // Affirmations
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS affirmations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    folderId INTEGER NOT NULL,
                    text TEXT NOT NULL,
                    audioUri TEXT,
                    useTts INTEGER NOT NULL DEFAULT 1,
                    sortOrder INTEGER NOT NULL DEFAULT 0,
                    createdAt INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY (folderId) REFERENCES affirmation_folders(id) ON DELETE CASCADE
                )
            """)
            db.execSQL("CREATE INDEX IF NOT EXISTS index_affirmations_folderId ON affirmations(folderId)")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            Timber.d("Migrating database from version 3 to 4")
            // Multiple locations per entry
            db.execSQL("ALTER TABLE journal_entries ADD COLUMN additionalLocationsData TEXT DEFAULT NULL")
            // Inline images embedded within entry text
            db.execSQL("ALTER TABLE media ADD COLUMN inlineKey TEXT DEFAULT NULL")
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            Timber.d("Migrating database from version 4 to 5")
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS location_nicknames (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    locationKey TEXT NOT NULL,
                    nickname TEXT NOT NULL,
                    latitude REAL NOT NULL,
                    longitude REAL NOT NULL,
                    placeName TEXT,
                    address TEXT,
                    updatedAt INTEGER NOT NULL DEFAULT 0
                )
            """)
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_location_nicknames_locationKey ON location_nicknames(locationKey)")
        }
    }

    val ALL_MIGRATIONS = arrayOf<Migration>(
        MIGRATION_1_2,
        MIGRATION_2_3,
        MIGRATION_3_4,
        MIGRATION_4_5
    )
}
