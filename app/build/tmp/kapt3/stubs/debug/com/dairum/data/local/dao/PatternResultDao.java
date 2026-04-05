package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * DAO for pattern results
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\r0\fH\'J\u0018\u0010\u000e\u001a\u0004\u0018\u00010\t2\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\r0\f2\u0006\u0010\u0013\u001a\u00020\u0014H\'J\u001c\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\r0\f2\u0006\u0010\u0016\u001a\u00020\u0017H\'J\u0016\u0010\u0018\u001a\u00020\u00102\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u0019"}, d2 = {"Lcom/dairum/data/local/dao/PatternResultDao;", "", "deleteOldPatterns", "", "beforeDate", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deletePattern", "pattern", "Lcom/dairum/data/local/entity/PatternResultEntity;", "(Lcom/dairum/data/local/entity/PatternResultEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllPatterns", "Lkotlinx/coroutines/flow/Flow;", "", "getPatternById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPatternsByType", "patternType", "", "getRecentPatterns", "limit", "", "insertPattern", "app_debug"})
@androidx.room.Dao()
public abstract interface PatternResultDao {
    
    @androidx.room.Query(value = "SELECT * FROM pattern_results ORDER BY detectedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.PatternResultEntity>> getAllPatterns();
    
    @androidx.room.Query(value = "SELECT * FROM pattern_results WHERE patternType = :patternType ORDER BY detectedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.PatternResultEntity>> getPatternsByType(@org.jetbrains.annotations.NotNull()
    java.lang.String patternType);
    
    @androidx.room.Query(value = "SELECT * FROM pattern_results ORDER BY detectedAt DESC LIMIT :limit")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.PatternResultEntity>> getRecentPatterns(int limit);
    
    @androidx.room.Query(value = "SELECT * FROM pattern_results WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPatternById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.PatternResultEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertPattern(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.PatternResultEntity pattern, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deletePattern(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.PatternResultEntity pattern, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM pattern_results WHERE detectedAt < :beforeDate")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteOldPatterns(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime beforeDate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}