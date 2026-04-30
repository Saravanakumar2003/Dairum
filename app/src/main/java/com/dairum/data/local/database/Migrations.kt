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
    val ALL_MIGRATIONS = arrayOf<Migration>(
    )
}
