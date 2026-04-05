package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * DAO for reflection memories
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001e\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00150\u0014H\'J\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u001c\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00050\u00152\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u0018J\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u00152\u0006\u0010\u001a\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u0018\u0010\u001c\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ*\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00050\u00152\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00152\u0006\u0010\u001a\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010 J\u001c\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00150\u00142\u0006\u0010\"\u001a\u00020\u001fH\'J\u001c\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u00152\u0006\u0010\"\u001a\u00020\u001fH\u00a7@\u00a2\u0006\u0002\u0010$J\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00050\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010&\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u001e\u0010\'\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010(\u001a\u00020)H\u00a7@\u00a2\u0006\u0002\u0010*J\u0016\u0010+\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010,\u001a\u00020\u00032\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00050\u0015H\u00a7@\u00a2\u0006\u0002\u0010.J&\u0010/\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u00100\u001a\u0002012\u0006\u0010(\u001a\u00020)H\u00a7@\u00a2\u0006\u0002\u00102J&\u00103\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u00104\u001a\u00020\r2\u0006\u0010(\u001a\u00020)H\u00a7@\u00a2\u0006\u0002\u00105J.\u00106\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020)H\u00a7@\u00a2\u0006\u0002\u0010:J\u001e\u0010;\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010<\u001a\u00020)H\u00a7@\u00a2\u0006\u0002\u0010*J\u0016\u0010=\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006>"}, d2 = {"Lcom/dairum/data/local/dao/ReflectionMemoryDao;", "", "deleteReflection", "", "reflection", "Lcom/dairum/data/local/entity/ReflectionMemoryEntity;", "(Lcom/dairum/data/local/entity/ReflectionMemoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteReflectionById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteWeakReflections", "minConfidence", "", "minOccurrences", "", "(FILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveReflectionCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveReflections", "Lkotlinx/coroutines/flow/Flow;", "", "getActiveReflectionsList", "getHighConfidenceReflections", "(FLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentReflections", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReflectionById", "getReflectionsByCategories", "categories", "", "(Ljava/util/List;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReflectionsByCategory", "category", "getReflectionsByCategoryList", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReflectionsWithEmbeddings", "getTotalReflectionCount", "incrementOccurrenceCount", "updatedAt", "Ljava/time/LocalDateTime;", "(JLjava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertReflection", "insertReflections", "reflections", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateActiveStatus", "isActive", "", "(JZLjava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateConfidence", "confidence", "(JFLjava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEmbedding", "embedding", "", "model", "(J[BLjava/lang/String;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastReferenced", "lastReferenced", "updateReflection", "app_debug"})
@androidx.room.Dao()
public abstract interface ReflectionMemoryDao {
    
    @androidx.room.Query(value = "SELECT * FROM reflection_memories WHERE isActive = 1 ORDER BY updatedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.ReflectionMemoryEntity>> getActiveReflections();
    
    @androidx.room.Query(value = "SELECT * FROM reflection_memories WHERE isActive = 1 ORDER BY updatedAt DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveReflectionsList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ReflectionMemoryEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM reflection_memories WHERE isActive = 1 AND embedding IS NOT NULL ORDER BY updatedAt DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionsWithEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ReflectionMemoryEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM reflection_memories WHERE category = :category AND isActive = 1 ORDER BY confidence DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.ReflectionMemoryEntity>> getReflectionsByCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String category);
    
    @androidx.room.Query(value = "SELECT * FROM reflection_memories WHERE category = :category AND isActive = 1 ORDER BY confidence DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionsByCategoryList(@org.jetbrains.annotations.NotNull()
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ReflectionMemoryEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM reflection_memories WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.ReflectionMemoryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM reflection_memories ORDER BY updatedAt DESC LIMIT :limit")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentReflections(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ReflectionMemoryEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM reflection_memories WHERE confidence >= :minConfidence AND isActive = 1 ORDER BY confidence DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHighConfidenceReflections(float minConfidence, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ReflectionMemoryEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ReflectionMemoryEntity reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertReflections(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.ReflectionMemoryEntity> reflections, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ReflectionMemoryEntity reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE reflection_memories SET lastReferenced = :lastReferenced WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastReferenced(long id, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime lastReferenced, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE reflection_memories SET occurrenceCount = occurrenceCount + 1, updatedAt = :updatedAt WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementOccurrenceCount(long id, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE reflection_memories SET isActive = :isActive, updatedAt = :updatedAt WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateActiveStatus(long id, boolean isActive, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE reflection_memories SET confidence = :confidence, updatedAt = :updatedAt WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateConfidence(long id, float confidence, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        UPDATE reflection_memories \n        SET embedding = :embedding, embeddingModel = :model, updatedAt = :updatedAt \n        WHERE id = :id\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEmbedding(long id, @org.jetbrains.annotations.NotNull()
    byte[] embedding, @org.jetbrains.annotations.NotNull()
    java.lang.String model, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ReflectionMemoryEntity reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM reflection_memories WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteReflectionById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM reflection_memories WHERE confidence < :minConfidence AND occurrenceCount < :minOccurrences")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteWeakReflections(float minConfidence, int minOccurrences, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM reflection_memories WHERE isActive = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveReflectionCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM reflection_memories")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalReflectionCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM reflection_memories \n        WHERE category IN (:categories) AND isActive = 1 \n        ORDER BY confidence DESC \n        LIMIT :limit\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionsByCategories(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> categories, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ReflectionMemoryEntity>> $completion);
}