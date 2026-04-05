package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * Repository for conversation context
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\n2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\bH\u00a6@\u00a2\u0006\u0002\u0010\rJ\u001e\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0010H\u00a6@\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0017H\u00a6@\u00a2\u0006\u0002\u0010\u0018J$\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\u001bH\u00a6@\u00a2\u0006\u0002\u0010\u001c\u00a8\u0006\u001d"}, d2 = {"Lcom/dairum/domain/repository/ConversationContextRepository;", "", "deleteContextForSession", "", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getContextForSession", "Lcom/dairum/domain/model/ConversationContext;", "observeContextForSession", "Lkotlinx/coroutines/flow/Flow;", "saveContext", "context", "(Lcom/dairum/domain/model/ConversationContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateDiscussionDepth", "depth", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEmotionalTone", "tone", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastSummaryTime", "time", "Ljava/time/LocalDateTime;", "(Ljava/lang/String;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTopics", "topics", "", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ConversationContextRepository {
    
    /**
     * Get context for a session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getContextForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ConversationContext> $completion);
    
    /**
     * Observe context for a session
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.model.ConversationContext> observeContextForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId);
    
    /**
     * Save or update context
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveContext(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ConversationContext context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update current topics
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTopics(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> topics, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update emotional tone
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEmotionalTone(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String tone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update discussion depth
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateDiscussionDepth(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, int depth, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update last summary time
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastSummaryTime(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime time, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete context for session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteContextForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}