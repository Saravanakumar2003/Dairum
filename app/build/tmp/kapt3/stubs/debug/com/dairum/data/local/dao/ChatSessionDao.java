package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * DAO for chat sessions
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\n\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00110\u0010H\'J\u0014\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00110\u0010H\'J\u000e\u0010\u0013\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u0014\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00110\u0010H\'J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u0018\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u0018\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ&\u0010\u001b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u00102\u0006\u0010\f\u001a\u00020\rH\'J\u0018\u0010!\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0\u00102\u0006\u0010\f\u001a\u00020\rH\'J&\u0010\"\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010#\u001a\u00020$2\u0006\u0010\u001d\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010%J&\u0010&\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\'\u001a\u00020$2\u0006\u0010\u001d\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010%J\u0016\u0010(\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ&\u0010)\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010*\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010+J&\u0010,\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010-\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010+\u00a8\u0006."}, d2 = {"Lcom/dairum/data/local/dao/ChatSessionDao;", "", "deleteOldArchivedSessions", "", "beforeDate", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteSession", "session", "Lcom/dairum/data/local/entity/ChatSessionEntity;", "(Lcom/dairum/data/local/entity/ChatSessionEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteSessionById", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveSessions", "Lkotlinx/coroutines/flow/Flow;", "", "getAllSessions", "getSavedSessionCount", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSavedSessions", "getSessionById", "getSessionCount", "getSessionWithMessages", "Lcom/dairum/data/local/entity/ChatSessionWithMessagesRelation;", "incrementMessageCount", "lastMessageAt", "updatedAt", "(Ljava/lang/String;Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertSession", "observeSession", "observeSessionWithMessages", "updateArchivedStatus", "isArchived", "", "(Ljava/lang/String;ZLjava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSavedStatus", "isSaved", "updateSession", "updateSessionSummary", "summary", "(Ljava/lang/String;Ljava/lang/String;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSessionTitle", "title", "app_debug"})
@androidx.room.Dao()
public abstract interface ChatSessionDao {
    
    @androidx.room.Query(value = "SELECT * FROM chat_sessions ORDER BY lastMessageAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.ChatSessionEntity>> getAllSessions();
    
    @androidx.room.Query(value = "SELECT * FROM chat_sessions WHERE isSaved = 1 ORDER BY lastMessageAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.ChatSessionEntity>> getSavedSessions();
    
    @androidx.room.Query(value = "SELECT * FROM chat_sessions WHERE isArchived = 0 ORDER BY lastMessageAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.ChatSessionEntity>> getActiveSessions();
    
    @androidx.room.Query(value = "SELECT * FROM chat_sessions WHERE id = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionById(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.ChatSessionEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM chat_sessions WHERE id = :sessionId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.data.local.entity.ChatSessionEntity> observeSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM chat_sessions WHERE id = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionWithMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.ChatSessionWithMessagesRelation> $completion);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM chat_sessions WHERE id = :sessionId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.data.local.entity.ChatSessionWithMessagesRelation> observeSessionWithMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertSession(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ChatSessionEntity session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSession(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ChatSessionEntity session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE chat_sessions SET title = :title, updatedAt = :updatedAt WHERE id = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSessionTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE chat_sessions SET isSaved = :isSaved, updatedAt = :updatedAt WHERE id = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSavedStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, boolean isSaved, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE chat_sessions SET isArchived = :isArchived, updatedAt = :updatedAt WHERE id = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateArchivedStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, boolean isArchived, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE chat_sessions SET messageCount = messageCount + 1, lastMessageAt = :lastMessageAt, updatedAt = :updatedAt WHERE id = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementMessageCount(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime lastMessageAt, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE chat_sessions SET summary = :summary, updatedAt = :updatedAt WHERE id = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSessionSummary(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String summary, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSession(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ChatSessionEntity session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM chat_sessions WHERE id = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSessionById(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM chat_sessions WHERE isArchived = 1 AND updatedAt < :beforeDate")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteOldArchivedSessions(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime beforeDate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM chat_sessions")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM chat_sessions WHERE isSaved = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSavedSessionCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}