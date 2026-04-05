package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * Repository for chat embeddings
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\u0010\u0014\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000f0\u000eH\u00a6@\u00a2\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010\u0010J\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\"\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ.\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\u0018J6\u0010\u0019\u001a\u00020\u00032\u001e\u0010\u001a\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000f0\u001c0\u001b2\u0006\u0010\u0017\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\u001d\u00a8\u0006\u001e"}, d2 = {"Lcom/dairum/domain/repository/ChatEmbeddingRepository;", "", "deleteEmbeddingForMessage", "", "messageId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEmbeddingsForSession", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteObsoleteEmbeddings", "currentModel", "getAllEmbeddings", "", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEmbeddingCount", "", "getEmbeddingForMessage", "getEmbeddingsForSession", "saveEmbedding", "embedding", "model", "(JLjava/lang/String;[FLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveEmbeddings", "embeddings", "", "Lkotlin/Triple;", "(Ljava/util/List;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ChatEmbeddingRepository {
    
    /**
     * Get embedding for a message
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEmbeddingForMessage(long messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super float[]> $completion);
    
    /**
     * Get all embeddings for a session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEmbeddingsForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.Long, float[]>> $completion);
    
    /**
     * Get all embeddings
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.Long, float[]>> $completion);
    
    /**
     * Save embedding for a message
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveEmbedding(long messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    float[] embedding, @org.jetbrains.annotations.NotNull()
    java.lang.String model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Save multiple embeddings
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveEmbeddings(@org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Triple<java.lang.Long, java.lang.String, float[]>> embeddings, @org.jetbrains.annotations.NotNull()
    java.lang.String model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete embedding for a message
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEmbeddingForMessage(long messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete all embeddings for a session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEmbeddingsForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete obsolete embeddings (from different model)
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteObsoleteEmbeddings(@org.jetbrains.annotations.NotNull()
    java.lang.String currentModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Get embedding count
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEmbeddingCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}