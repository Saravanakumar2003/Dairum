package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * DAO for conversation context
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u000e2\u0006\u0010\b\u001a\u00020\tH\'J\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J&\u0010\u0010\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u0015J&\u0010\u0016\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u0018J&\u0010\u0019\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u001bJ&\u0010\u001c\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u0018\u00a8\u0006\u001e"}, d2 = {"Lcom/dairum/data/local/dao/ConversationContextDao;", "", "deleteContext", "", "context", "Lcom/dairum/data/local/entity/ConversationContextEntity;", "(Lcom/dairum/data/local/entity/ConversationContextEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteContextForSession", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getContextForSession", "insertContext", "observeContextForSession", "Lkotlinx/coroutines/flow/Flow;", "updateContext", "updateDiscussionDepth", "depth", "", "updatedAt", "Ljava/time/LocalDateTime;", "(Ljava/lang/String;ILjava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEmotionalTone", "tone", "(Ljava/lang/String;Ljava/lang/String;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastSummaryTime", "lastSummaryAt", "(Ljava/lang/String;Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTopics", "topics", "app_debug"})
@androidx.room.Dao()
public abstract interface ConversationContextDao {
    
    @androidx.room.Query(value = "SELECT * FROM conversation_context WHERE sessionId = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getContextForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.ConversationContextEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM conversation_context WHERE sessionId = :sessionId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.data.local.entity.ConversationContextEntity> observeContextForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertContext(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ConversationContextEntity context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateContext(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ConversationContextEntity context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        UPDATE conversation_context \n        SET currentTopics = :topics, updatedAt = :updatedAt \n        WHERE sessionId = :sessionId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTopics(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String topics, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        UPDATE conversation_context \n        SET emotionalTone = :tone, updatedAt = :updatedAt \n        WHERE sessionId = :sessionId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEmotionalTone(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String tone, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        UPDATE conversation_context \n        SET discussionDepth = :depth, updatedAt = :updatedAt \n        WHERE sessionId = :sessionId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateDiscussionDepth(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, int depth, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        UPDATE conversation_context \n        SET lastSummaryAt = :lastSummaryAt, updatedAt = :updatedAt \n        WHERE sessionId = :sessionId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastSummaryTime(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime lastSummaryAt, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteContext(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ConversationContextEntity context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM conversation_context WHERE sessionId = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteContextForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}