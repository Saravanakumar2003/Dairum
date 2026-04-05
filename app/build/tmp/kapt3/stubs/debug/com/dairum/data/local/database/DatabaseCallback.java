package com.dairum.data.local.database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.dairum.data.local.entity.CategoryEntity;
import com.dairum.data.local.entity.CustomTrackerEntity;
import com.dairum.data.local.entity.TrackerType;
import kotlinx.coroutines.Dispatchers;

/**
 * Database callback to prepopulate initial data
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0002J\u000e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006H\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/dairum/data/local/database/DatabaseCallback;", "Landroidx/room/RoomDatabase$Callback;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getBuiltInTrackers", "", "Lcom/dairum/data/local/entity/CustomTrackerEntity;", "getDefaultCategories", "Lcom/dairum/data/local/entity/CategoryEntity;", "onCreate", "", "db", "Landroidx/sqlite/db/SupportSQLiteDatabase;", "app_debug"})
public final class DatabaseCallback extends androidx.room.RoomDatabase.Callback {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    public DatabaseCallback(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.NotNull()
    androidx.sqlite.db.SupportSQLiteDatabase db) {
    }
    
    private final java.util.List<com.dairum.data.local.entity.CategoryEntity> getDefaultCategories() {
        return null;
    }
    
    private final java.util.List<com.dairum.data.local.entity.CustomTrackerEntity> getBuiltInTrackers() {
        return null;
    }
}