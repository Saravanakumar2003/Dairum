package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * Repository for managing chat sessions
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00120\u0011H&J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00120\u0011H&J\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00120\u0011H&J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0016\u001a\u00020\u0017H\u00a6@\u00a2\u0006\u0002\u0010\u0018J\u0018\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u001b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u00112\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\u001c\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0\u00112\u0006\u0010\u0004\u001a\u00020\u0005H&J\u001e\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001fH\u00a6@\u00a2\u0006\u0002\u0010 J\u0016\u0010!\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u001e\u0010\"\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010$\u00a8\u0006%"}, d2 = {"Lcom/dairum/domain/repository/ChatSessionRepository;", "", "archiveSession", "", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createSession", "session", "Lcom/dairum/domain/model/ChatSession;", "(Lcom/dairum/domain/model/ChatSession;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteOldArchivedSessions", "olderThan", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteSession", "getActiveSessions", "Lkotlinx/coroutines/flow/Flow;", "", "getAllSessions", "getSavedSessions", "getSessionById", "getSessionCount", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSessionWithMessages", "Lcom/dairum/domain/model/ChatSessionWithMessages;", "observeSession", "observeSessionWithMessages", "toggleSaveStatus", "isSaved", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSession", "updateSessionTitle", "title", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ChatSessionRepository {
    
    /**
     * Get all chat sessions
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ChatSession>> getAllSessions();
    
    /**
     * Get only saved sessions
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ChatSession>> getSavedSessions();
    
    /**
     * Get active (non-archived) sessions
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ChatSession>> getActiveSessions();
    
    /**
     * Get a session by ID
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionById(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ChatSession> $completion);
    
    /**
     * Observe a session by ID
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.model.ChatSession> observeSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId);
    
    /**
     * Get session with all messages
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionWithMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ChatSessionWithMessages> $completion);
    
    /**
     * Observe session with messages
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.model.ChatSessionWithMessages> observeSessionWithMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId);
    
    /**
     * Create a new session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createSession(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ChatSession session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSession(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ChatSession session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update session title
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSessionTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Toggle save status
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object toggleSaveStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, boolean isSaved, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Archive a session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object archiveSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete a session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete old archived sessions
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteOldArchivedSessions(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime olderThan, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Get session count
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}