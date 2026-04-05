package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * Repository for reflection memories
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\"\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\r\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u0010H&J\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u001e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\b\b\u0002\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\u0015J\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0017\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\u0018J\u0018\u0010\u0019\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J*\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00112\u0006\u0010\u0017\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\u001dJ\u001c\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u00102\u0006\u0010\u001f\u001a\u00020\u001cH&J\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u001f\u001a\u00020\u001cH\u00a6@\u00a2\u0006\u0002\u0010!J\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010#\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010$\u001a\u00020\u00052\u0006\u0010%\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010&J\u001c\u0010\'\u001a\u00020\u00032\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u00a6@\u00a2\u0006\u0002\u0010)J\u001e\u0010*\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010+\u001a\u00020,H\u00a6@\u00a2\u0006\u0002\u0010-J&\u0010.\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010/\u001a\u0002002\u0006\u00101\u001a\u000202H\u00a6@\u00a2\u0006\u0002\u00103J\u0016\u00104\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u00105\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010&\u00a8\u00066"}, d2 = {"Lcom/dairum/domain/repository/ReflectionMemoryRepository;", "", "deleteReflection", "", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteWeakReflections", "minConfidence", "", "minOccurrences", "", "(FILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveReflectionCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveReflections", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/dairum/domain/model/ReflectionMemory;", "getActiveReflectionsList", "getHighConfidenceReflections", "(FLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentReflections", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReflectionById", "getReflectionsByCategories", "categories", "Lcom/dairum/domain/model/ReflectionCategory;", "(Ljava/util/List;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReflectionsByCategory", "category", "getReflectionsByCategoryList", "(Lcom/dairum/domain/model/ReflectionCategory;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReflectionsWithEmbeddings", "incrementOccurrenceCount", "saveReflection", "reflection", "(Lcom/dairum/domain/model/ReflectionMemory;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveReflections", "reflections", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateActiveStatus", "isActive", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEmbedding", "embedding", "", "model", "", "(J[FLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastReferenced", "updateReflection", "app_debug"})
public abstract interface ReflectionMemoryRepository {
    
    /**
     * Get all active reflections as Flow
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ReflectionMemory>> getActiveReflections();
    
    /**
     * Get all active reflections as list
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveReflectionsList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion);
    
    /**
     * Get reflections with embeddings for semantic search
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionsWithEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion);
    
    /**
     * Get reflections by category
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ReflectionMemory>> getReflectionsByCategory(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionCategory category);
    
    /**
     * Get reflections by category as list
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionsByCategoryList(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionCategory category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion);
    
    /**
     * Get reflection by ID
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ReflectionMemory> $completion);
    
    /**
     * Get recent reflections
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentReflections(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion);
    
    /**
     * Get high confidence reflections
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHighConfidenceReflections(float minConfidence, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion);
    
    /**
     * Save a reflection
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionMemory reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Save multiple reflections
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveReflections(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ReflectionMemory> reflections, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update reflection
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionMemory reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update last referenced time (when used in retrieval)
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastReferenced(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Increment occurrence count
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementOccurrenceCount(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update active status
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateActiveStatus(long id, boolean isActive, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update embedding
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEmbedding(long id, @org.jetbrains.annotations.NotNull()
    float[] embedding, @org.jetbrains.annotations.NotNull()
    java.lang.String model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete a reflection
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteReflection(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete weak reflections (low confidence, low occurrence)
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteWeakReflections(float minConfidence, int minOccurrences, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Get reflection count
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveReflectionCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    /**
     * Get reflections by multiple categories
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionsByCategories(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.dairum.domain.model.ReflectionCategory> categories, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ReflectionMemory>> $completion);
    
    /**
     * Repository for reflection memories
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}