package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * DAO for smart summaries
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH\'J\u001c\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\u000f\u001a\u00020\u0010H\'J\u001c\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\u0012\u001a\u00020\u0013H\'J\u0018\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ \u0010\u0015\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0018H\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u001b"}, d2 = {"Lcom/dairum/data/local/dao/SmartSummaryDao;", "", "deleteSummary", "", "summary", "Lcom/dairum/data/local/entity/SmartSummaryEntity;", "(Lcom/dairum/data/local/entity/SmartSummaryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteSummaryById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllSummaries", "Lkotlinx/coroutines/flow/Flow;", "", "getRecentSummaries", "limit", "", "getSummariesByType", "summaryType", "", "getSummaryById", "getSummaryForPeriod", "type", "periodStart", "Ljava/time/LocalDateTime;", "(Ljava/lang/String;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertSummary", "app_debug"})
@androidx.room.Dao()
public abstract interface SmartSummaryDao {
    
    @androidx.room.Query(value = "SELECT * FROM smart_summaries ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.SmartSummaryEntity>> getAllSummaries();
    
    @androidx.room.Query(value = "SELECT * FROM smart_summaries WHERE summaryType = :summaryType ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.SmartSummaryEntity>> getSummariesByType(@org.jetbrains.annotations.NotNull()
    java.lang.String summaryType);
    
    @androidx.room.Query(value = "SELECT * FROM smart_summaries ORDER BY createdAt DESC LIMIT :limit")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.SmartSummaryEntity>> getRecentSummaries(int limit);
    
    @androidx.room.Query(value = "SELECT * FROM smart_summaries WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSummaryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.SmartSummaryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM smart_summaries WHERE summaryType = :type AND periodStart = :periodStart")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSummaryForPeriod(@org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime periodStart, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.SmartSummaryEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertSummary(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.SmartSummaryEntity summary, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSummary(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.SmartSummaryEntity summary, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM smart_summaries WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSummaryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}