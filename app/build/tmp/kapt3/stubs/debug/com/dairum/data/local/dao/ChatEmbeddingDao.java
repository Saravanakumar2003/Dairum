package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * DAO for chat message embeddings
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0013\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00120\u00102\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\u00020\u00032\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00120\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u001c\u00a8\u0006\u001d"}, d2 = {"Lcom/dairum/data/local/dao/ChatEmbeddingDao;", "", "deleteAllEmbeddings", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEmbeddingForMessage", "messageId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEmbeddingsForSession", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteObsoleteEmbeddings", "currentModel", "getAllEmbeddedMessageIds", "", "getAllEmbeddings", "Lcom/dairum/data/local/entity/ChatEmbeddingEntity;", "getEmbeddingCount", "", "getEmbeddingForMessage", "getEmbeddingsForSession", "insertEmbedding", "embedding", "(Lcom/dairum/data/local/entity/ChatEmbeddingEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertEmbeddings", "embeddings", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface ChatEmbeddingDao {
    
    @androidx.room.Query(value = "SELECT * FROM chat_embeddings WHERE messageId = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEmbeddingForMessage(long messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.ChatEmbeddingEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM chat_embeddings WHERE sessionId = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEmbeddingsForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ChatEmbeddingEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM chat_embeddings")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.ChatEmbeddingEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT messageId FROM chat_embeddings")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllEmbeddedMessageIds(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM chat_embeddings")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEmbeddingCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEmbedding(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ChatEmbeddingEntity embedding, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEmbeddings(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.ChatEmbeddingEntity> embeddings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM chat_embeddings WHERE messageId = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEmbeddingForMessage(long messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM chat_embeddings WHERE sessionId = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEmbeddingsForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM chat_embeddings WHERE model != :currentModel")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteObsoleteEmbeddings(@org.jetbrains.annotations.NotNull()
    java.lang.String currentModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM chat_embeddings")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllEmbeddings(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}