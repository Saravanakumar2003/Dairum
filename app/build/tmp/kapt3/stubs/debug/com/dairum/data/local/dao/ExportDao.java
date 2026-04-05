package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.ExportConfigEntity;
import com.dairum.data.local.entity.ExportHistoryEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00120\u0015H\'J\u0014\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00120\u0015H\'J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0010\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u000e\u0010\u0019\u001a\u00020\u001aH\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u001c\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00120\u00152\u0006\u0010\u001c\u001a\u00020\rH\'J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\tH\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u001c\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001f\u001a\u00020\u001aH\u00a7@\u00a2\u0006\u0002\u0010 J\u0010\u0010!\u001a\u0004\u0018\u00010\rH\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\"\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010#\u001a\u00020\r2\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010$\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010%\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010&\u001a\u00020\'H\u00a7@\u00a2\u0006\u0002\u0010(J&\u0010)\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010*\u001a\u00020\r2\u0006\u0010+\u001a\u00020,H\u00a7@\u00a2\u0006\u0002\u0010-\u00a8\u0006."}, d2 = {"Lcom/dairum/data/local/dao/ExportDao;", "", "deleteConfig", "", "config", "Lcom/dairum/data/local/entity/ExportConfigEntity;", "(Lcom/dairum/data/local/entity/ExportConfigEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteHistory", "history", "Lcom/dairum/data/local/entity/ExportHistoryEntity;", "(Lcom/dairum/data/local/entity/ExportHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteHistoryBefore", "beforeTimestamp", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteHistoryById", "id", "getAllConfigs", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllConfigsFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllHistoryFlow", "getConfigById", "getEnabledConfigs", "getHistoryCount", "", "getHistoryForConfigFlow", "configId", "getLastSuccessfulExport", "getRecentHistory", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTotalExportSize", "insertConfig", "insertHistory", "updateConfig", "updateConfigEnabled", "isEnabled", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastExport", "timestamp", "status", "", "(JJLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface ExportDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ExportConfigEntity config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ExportConfigEntity config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ExportConfigEntity config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM export_configs WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getConfigById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.ExportConfigEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM export_configs ORDER BY createdAt ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.ExportConfigEntity>> getAllConfigsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM export_configs ORDER BY createdAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllConfigs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ExportConfigEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM export_configs WHERE isEnabled = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEnabledConfigs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ExportConfigEntity>> $completion);
    
    @androidx.room.Query(value = "UPDATE export_configs SET isEnabled = :isEnabled WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateConfigEnabled(long id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE export_configs SET lastExportAt = :timestamp, lastExportStatus = :status WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastExport(long id, long timestamp, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertHistory(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ExportHistoryEntity history, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteHistory(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ExportHistoryEntity history, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM export_history WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteHistoryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM export_history WHERE exportedAt < :beforeTimestamp")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteHistoryBefore(long beforeTimestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM export_history ORDER BY exportedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.ExportHistoryEntity>> getAllHistoryFlow();
    
    @androidx.room.Query(value = "SELECT * FROM export_history ORDER BY exportedAt DESC LIMIT :limit")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentHistory(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ExportHistoryEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM export_history WHERE configId = :configId ORDER BY exportedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.ExportHistoryEntity>> getHistoryForConfigFlow(long configId);
    
    @androidx.room.Query(value = "SELECT * FROM export_history WHERE isSuccess = 1 ORDER BY exportedAt DESC LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLastSuccessfulExport(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.ExportHistoryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM export_history")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHistoryCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT SUM(fileSize) FROM export_history WHERE isSuccess = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalExportSize(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
}