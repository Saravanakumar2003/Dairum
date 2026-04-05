package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * Repository for managing journal embeddings
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eH\u00a6@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u000eH\u00a6@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0006\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\bJ$\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00100\u000e2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016H\u00a6@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u0010H\u00a6@\u00a2\u0006\u0002\u0010\u001bJ\u001c\u0010\u001c\u001a\u00020\u00032\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00100\u000eH\u00a6@\u00a2\u0006\u0002\u0010\u001e\u00a8\u0006\u001f"}, d2 = {"Lcom/dairum/domain/repository/EmbeddingRepository;", "", "deleteAllEmbeddings", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEmbeddingForEntry", "entryId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteObsoleteEmbeddings", "currentModel", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEmbeddedEntryIds", "", "getAllEmbeddings", "Lcom/dairum/domain/model/JournalEmbedding;", "getEmbeddingCount", "", "getEmbeddingForEntry", "getEmbeddingsInDateRange", "startDate", "Ljava/time/LocalDateTime;", "endDate", "(Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveEmbedding", "embedding", "(Lcom/dairum/domain/model/JournalEmbedding;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveEmbeddings", "embeddings", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface EmbeddingRepository {
    
    /**
     * Get embedding for a specific entry
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEmbeddingForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.JournalEmbedding> $completion);
    
    /**
     * Get all embeddings
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.JournalEmbedding>> $completion);
    
    /**
     * Get IDs of all entries that have embeddings
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllEmbeddedEntryIds(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion);
    
    /**
     * Get embedding count
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEmbeddingCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    /**
     * Save an embedding
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveEmbedding(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.JournalEmbedding embedding, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Save multiple embeddings
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveEmbeddings(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEmbedding> embeddings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete embedding for an entry
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEmbeddingForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete all embeddings for obsolete model
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteObsoleteEmbeddings(@org.jetbrains.annotations.NotNull()
    java.lang.String currentModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete all embeddings
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Get embeddings within date range
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEmbeddingsInDateRange(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startDate, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endDate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.JournalEmbedding>> $completion);
}