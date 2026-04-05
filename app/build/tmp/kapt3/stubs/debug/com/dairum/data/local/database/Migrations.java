package com.dairum.data.local.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import timber.log.Timber;

/**
 * Database migrations for Dairum.
 * Each migration should be carefully tested before release.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u000f\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\t\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000bR\u0011\u0010\u0010\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000bR\u0011\u0010\u0012\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000b\u00a8\u0006\u0014"}, d2 = {"Lcom/dairum/data/local/database/Migrations;", "", "()V", "ALL_MIGRATIONS", "", "Landroidx/room/migration/Migration;", "getALL_MIGRATIONS", "()[Landroidx/room/migration/Migration;", "[Landroidx/room/migration/Migration;", "MIGRATION_1_2", "getMIGRATION_1_2", "()Landroidx/room/migration/Migration;", "MIGRATION_2_3", "getMIGRATION_2_3", "MIGRATION_3_4", "getMIGRATION_3_4", "MIGRATION_4_5", "getMIGRATION_4_5", "MIGRATION_6_7", "getMIGRATION_6_7", "app_debug"})
public final class Migrations {
    
    /**
     * Migration from version 1 to 2
     * Added export and notification tables
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_1_2 = null;
    
    /**
     * Migration from version 2 to 3
     * Added smart reminders and notification history
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_2_3 = null;
    
    /**
     * Migration from version 3 to 4
     * Added memory collections feature
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_3_4 = null;
    
    /**
     * Migration from version 4 to 5
     * Added mood check-in configuration
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_4_5 = null;
    
    /**
     * Migration from version 6 to 7
     * Added expanded Person fields: email, phone, birthday, importantDates
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_6_7 = null;
    
    /**
     * Get all migrations
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration[] ALL_MIGRATIONS = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.local.database.Migrations INSTANCE = null;
    
    private Migrations() {
        super();
    }
    
    /**
     * Migration from version 1 to 2
     * Added export and notification tables
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.room.migration.Migration getMIGRATION_1_2() {
        return null;
    }
    
    /**
     * Migration from version 2 to 3
     * Added smart reminders and notification history
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.room.migration.Migration getMIGRATION_2_3() {
        return null;
    }
    
    /**
     * Migration from version 3 to 4
     * Added memory collections feature
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.room.migration.Migration getMIGRATION_3_4() {
        return null;
    }
    
    /**
     * Migration from version 4 to 5
     * Added mood check-in configuration
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.room.migration.Migration getMIGRATION_4_5() {
        return null;
    }
    
    /**
     * Migration from version 6 to 7
     * Added expanded Person fields: email, phone, birthday, importantDates
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.room.migration.Migration getMIGRATION_6_7() {
        return null;
    }
    
    /**
     * Get all migrations
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.room.migration.Migration[] getALL_MIGRATIONS() {
        return null;
    }
}