package com.dairum.data.local.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import timber.log.Timber;

/**
 * Database migrations for Dairum.
 *
 * Version 1 = clean-slate schema matching all current Room entities.
 * Add future migrations here as MIGRATION_1_2, MIGRATION_2_3, etc.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\t"}, d2 = {"Lcom/dairum/data/local/database/Migrations;", "", "()V", "ALL_MIGRATIONS", "", "Landroidx/room/migration/Migration;", "getALL_MIGRATIONS", "()[Landroidx/room/migration/Migration;", "[Landroidx/room/migration/Migration;", "app_debug"})
public final class Migrations {
    
    /**
     * All migrations to register with Room.
     * Currently empty because version 1 is the first shipped schema.
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration[] ALL_MIGRATIONS = {};
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.local.database.Migrations INSTANCE = null;
    
    private Migrations() {
        super();
    }
    
    /**
     * All migrations to register with Room.
     * Currently empty because version 1 is the first shipped schema.
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.room.migration.Migration[] getALL_MIGRATIONS() {
        return null;
    }
}