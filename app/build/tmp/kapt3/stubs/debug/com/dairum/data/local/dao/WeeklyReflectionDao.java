package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * DAO for weekly reflections
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH\'J\u001c\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\u000f\u001a\u00020\u0010H\'J\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u0012\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0013\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0017"}, d2 = {"Lcom/dairum/data/local/dao/WeeklyReflectionDao;", "", "deleteReflection", "", "reflection", "Lcom/dairum/data/local/entity/WeeklyReflectionEntity;", "(Lcom/dairum/data/local/entity/WeeklyReflectionEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteReflectionById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllReflections", "Lkotlinx/coroutines/flow/Flow;", "", "getRecentReflections", "limit", "", "getReflectionById", "getReflectionForWeek", "weekStart", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertReflection", "app_debug"})
@androidx.room.Dao()
public abstract interface WeeklyReflectionDao {
    
    @androidx.room.Query(value = "SELECT * FROM weekly_reflections ORDER BY weekStartDate DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.WeeklyReflectionEntity>> getAllReflections();
    
    @androidx.room.Query(value = "SELECT * FROM weekly_reflections ORDER BY weekStartDate DESC LIMIT :limit")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.WeeklyReflectionEntity>> getRecentReflections(int limit);
    
    @androidx.room.Query(value = "SELECT * FROM weekly_reflections WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.WeeklyReflectionEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM weekly_reflections WHERE weekStartDate = :weekStart")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionForWeek(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime weekStart, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.WeeklyReflectionEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.WeeklyReflectionEntity reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.WeeklyReflectionEntity reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM weekly_reflections WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteReflectionById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}