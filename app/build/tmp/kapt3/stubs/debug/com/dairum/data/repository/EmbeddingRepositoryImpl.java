package com.dairum.data.repository;

import com.dairum.data.ai.model.ModelManager;
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
 * Implementation of EmbeddingRepository
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\u0007J\u0016\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\u0011H\u0096@\u00a2\u0006\u0002\u0010\u0007J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u0011H\u0096@\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\u0014\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00132\u0006\u0010\t\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u000bJ$\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00130\u00112\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u0013H\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u001c\u0010\u001f\u001a\u00020\u00062\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00130\u0011H\u0096@\u00a2\u0006\u0002\u0010!R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/dairum/data/repository/EmbeddingRepositoryImpl;", "Lcom/dairum/domain/repository/EmbeddingRepository;", "embeddingDao", "Lcom/dairum/data/local/dao/JournalEmbeddingDao;", "(Lcom/dairum/data/local/dao/JournalEmbeddingDao;)V", "deleteAllEmbeddings", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEmbeddingForEntry", "entryId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteObsoleteEmbeddings", "currentModel", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEmbeddedEntryIds", "", "getAllEmbeddings", "Lcom/dairum/domain/model/JournalEmbedding;", "getEmbeddingCount", "", "getEmbeddingForEntry", "getEmbeddingsInDateRange", "startDate", "Ljava/time/LocalDateTime;", "endDate", "(Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveEmbedding", "embedding", "(Lcom/dairum/domain/model/JournalEmbedding;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveEmbeddings", "embeddings", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class EmbeddingRepositoryImpl implements com.dairum.domain.repository.EmbeddingRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.JournalEmbeddingDao embeddingDao = null;
    
    @javax.inject.Inject()
    public EmbeddingRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.JournalEmbeddingDao embeddingDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEmbeddingForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.JournalEmbedding> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.JournalEmbedding>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllEmbeddedEntryIds(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEmbeddingCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveEmbedding(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.JournalEmbedding embedding, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveEmbeddings(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEmbedding> embeddings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteEmbeddingForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteObsoleteEmbeddings(@org.jetbrains.annotations.NotNull()
    java.lang.String currentModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteAllEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEmbeddingsInDateRange(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startDate, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endDate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.JournalEmbedding>> $completion) {
        return null;
    }
}