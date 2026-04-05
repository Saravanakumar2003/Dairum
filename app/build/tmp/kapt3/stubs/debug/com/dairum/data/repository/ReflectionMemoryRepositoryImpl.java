package com.dairum.data.repository;

import com.dairum.data.local.dao.*;
import com.dairum.data.local.entity.*;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.*;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of ReflectionMemoryRepository
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0014\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0017J\u000e\u0010\u0018\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0019J\u0014\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\t0\u001bH\u0016J\u0014\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001c0\tH\u0096@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001c0\t2\u0006\u0010\u0013\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010\u001fJ\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001c0\t2\u0006\u0010!\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\"J\u0018\u0010#\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0010\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u0011J*\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001c0\t2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020&0\t2\u0006\u0010!\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\'J\u001c\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\t0\u001b2\u0006\u0010)\u001a\u00020&H\u0016J\u001c\u0010*\u001a\b\u0012\u0004\u0012\u00020\u001c0\t2\u0006\u0010)\u001a\u00020&H\u0096@\u00a2\u0006\u0002\u0010+J\u0014\u0010,\u001a\b\u0012\u0004\u0012\u00020\u001c0\tH\u0096@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010-\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010.\u001a\u00020\n2\u0006\u0010/\u001a\u00020\u001cH\u0096@\u00a2\u0006\u0002\u00100J\u001c\u00101\u001a\u00020\u000f2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u001c0\tH\u0096@\u00a2\u0006\u0002\u00103J\u001e\u00104\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\n2\u0006\u00105\u001a\u000206H\u0096@\u00a2\u0006\u0002\u00107J&\u00108\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\n2\u0006\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020\rH\u0096@\u00a2\u0006\u0002\u0010<J\u0016\u0010=\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010>\u001a\u00020\u000f2\u0006\u0010/\u001a\u00020\u001cH\u0096@\u00a2\u0006\u0002\u00100J\f\u0010?\u001a\u00020\u001c*\u00020@H\u0002J\f\u0010A\u001a\u00020@*\u00020\u001cH\u0002RJ\u0010\u0007\u001a>\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\n \u000b*\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t0\t \u000b*\u001e\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\n \u000b*\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t0\t\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000RJ\u0010\f\u001a>\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\r \u000b*\n\u0012\u0004\u0012\u00020\r\u0018\u00010\t0\t \u000b*\u001e\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\r \u000b*\n\u0012\u0004\u0012\u00020\r\u0018\u00010\t0\t\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006B"}, d2 = {"Lcom/dairum/data/repository/ReflectionMemoryRepositoryImpl;", "Lcom/dairum/domain/repository/ReflectionMemoryRepository;", "reflectionMemoryDao", "Lcom/dairum/data/local/dao/ReflectionMemoryDao;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Lcom/dairum/data/local/dao/ReflectionMemoryDao;Lcom/squareup/moshi/Moshi;)V", "longListAdapter", "Lcom/squareup/moshi/JsonAdapter;", "", "", "kotlin.jvm.PlatformType", "stringListAdapter", "", "deleteReflection", "", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteWeakReflections", "minConfidence", "", "minOccurrences", "", "(FILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveReflectionCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveReflections", "Lkotlinx/coroutines/flow/Flow;", "Lcom/dairum/domain/model/ReflectionMemory;", "getActiveReflectionsList", "getHighConfidenceReflections", "(FLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentReflections", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReflectionById", "getReflectionsByCategories", "categories", "Lcom/dairum/domain/model/ReflectionCategory;", "(Ljava/util/List;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReflectionsByCategory", "category", "getReflectionsByCategoryList", "(Lcom/dairum/domain/model/ReflectionCategory;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReflectionsWithEmbeddings", "incrementOccurrenceCount", "saveReflection", "reflection", "(Lcom/dairum/domain/model/ReflectionMemory;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveReflections", "reflections", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateActiveStatus", "isActive", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEmbedding", "embedding", "", "model", "(J[FLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastReferenced", "updateReflection", "toDomain", "Lcom/dairum/data/local/entity/ReflectionMemoryEntity;", "toEntity", "app_debug"})
public final class ReflectionMemoryRepositoryImpl implements com.dairum.domain.repository.ReflectionMemoryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.ReflectionMemoryDao reflectionMemoryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.squareup.moshi.Moshi moshi = null;
    private final com.squareup.moshi.JsonAdapter<java.util.List<java.lang.Long>> longListAdapter = null;
    private final com.squareup.moshi.JsonAdapter<java.util.List<java.lang.String>> stringListAdapter = null;
    
    @javax.inject.Inject()
    public ReflectionMemoryRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.ReflectionMemoryDao reflectionMemoryDao, @org.jetbrains.annotations.NotNull()
    com.squareup.moshi.Moshi moshi) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ReflectionMemory>> getActiveReflections() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getActiveReflectionsList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getReflectionsWithEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ReflectionMemory>> getReflectionsByCategory(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionCategory category) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getReflectionsByCategoryList(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionCategory category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getReflectionById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ReflectionMemory> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getRecentReflections(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getHighConfidenceReflections(float minConfidence, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionMemory reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveReflections(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ReflectionMemory> reflections, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionMemory reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateLastReferenced(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object incrementOccurrenceCount(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateActiveStatus(long id, boolean isActive, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateEmbedding(long id, @org.jetbrains.annotations.NotNull()
    float[] embedding, @org.jetbrains.annotations.NotNull()
    java.lang.String model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteReflection(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteWeakReflections(float minConfidence, int minOccurrences, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getActiveReflectionCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getReflectionsByCategories(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.dairum.domain.model.ReflectionCategory> categories, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.ReflectionMemory toDomain(com.dairum.data.local.entity.ReflectionMemoryEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.ReflectionMemoryEntity toEntity(com.dairum.domain.model.ReflectionMemory $this$toEntity) {
        return null;
    }
}