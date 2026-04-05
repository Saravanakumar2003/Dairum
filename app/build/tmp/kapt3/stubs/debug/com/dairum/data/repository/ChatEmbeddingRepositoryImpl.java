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
 * Implementation of ChatEmbeddingRepository
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\u0010\u0014\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\rJ\u001a\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00120\u0011H\u0096@\u00a2\u0006\u0002\u0010\u0013J\u000e\u0010\u0014\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\"\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u000b\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\rJ.\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ6\u0010\u001c\u001a\u00020\u00062\u001e\u0010\u001d\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00120\u001f0\u001e2\u0006\u0010\u001a\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010 R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/dairum/data/repository/ChatEmbeddingRepositoryImpl;", "Lcom/dairum/domain/repository/ChatEmbeddingRepository;", "chatEmbeddingDao", "Lcom/dairum/data/local/dao/ChatEmbeddingDao;", "(Lcom/dairum/data/local/dao/ChatEmbeddingDao;)V", "deleteEmbeddingForMessage", "", "messageId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEmbeddingsForSession", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteObsoleteEmbeddings", "currentModel", "getAllEmbeddings", "", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEmbeddingCount", "", "getEmbeddingForMessage", "getEmbeddingsForSession", "saveEmbedding", "embedding", "model", "(JLjava/lang/String;[FLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveEmbeddings", "embeddings", "", "Lkotlin/Triple;", "(Ljava/util/List;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ChatEmbeddingRepositoryImpl implements com.dairum.domain.repository.ChatEmbeddingRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.ChatEmbeddingDao chatEmbeddingDao = null;
    
    @javax.inject.Inject()
    public ChatEmbeddingRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.ChatEmbeddingDao chatEmbeddingDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEmbeddingForMessage(long messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super float[]> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEmbeddingsForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.Long, float[]>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.Long, float[]>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveEmbedding(long messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    float[] embedding, @org.jetbrains.annotations.NotNull()
    java.lang.String model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveEmbeddings(@org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Triple<java.lang.Long, java.lang.String, float[]>> embeddings, @org.jetbrains.annotations.NotNull()
    java.lang.String model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteEmbeddingForMessage(long messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteEmbeddingsForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
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
    public java.lang.Object getEmbeddingCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
}