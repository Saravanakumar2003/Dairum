package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * Repository for chat messages
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\nH\u00a6@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u0012J$\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015H\u00a6@\u00a2\u0006\u0002\u0010\u0016J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\u00052\u0006\u0010\r\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u0012J\u001c\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\n0\u001a2\u0006\u0010\u0010\u001a\u00020\u0011H&J\u001c\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00050\n2\u0006\u0010\u0010\u001a\u00020\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u0012J$\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00050\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015H\u00a6@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u0012J\u001e\u0010\u001e\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u0011H\u00a6@\u00a2\u0006\u0002\u0010 \u00a8\u0006!"}, d2 = {"Lcom/dairum/domain/repository/ChatMessageRepository;", "", "addMessage", "", "message", "Lcom/dairum/domain/model/ChatMessage;", "(Lcom/dairum/domain/model/ChatMessage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addMessages", "", "messages", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMessage", "messageId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMessagesForSession", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLastNMessages", "limit", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMessageById", "getMessageCountForSession", "getMessagesForSession", "Lkotlinx/coroutines/flow/Flow;", "getMessagesForSessionList", "getRecentMessages", "getTotalTokensForSession", "updateMessageContent", "content", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ChatMessageRepository {
    
    /**
     * Get messages for a session
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ChatMessage>> getMessagesForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId);
    
    /**
     * Get messages list (non-Flow)
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMessagesForSessionList(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ChatMessage>> $completion);
    
    /**
     * Get recent messages (for short-term memory)
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ChatMessage>> $completion);
    
    /**
     * Get last N messages in reverse chronological order
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLastNMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ChatMessage>> $completion);
    
    /**
     * Get a message by ID
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMessageById(long messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ChatMessage> $completion);
    
    /**
     * Add a message
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addMessage(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ChatMessage message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Add multiple messages
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addMessages(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ChatMessage> messages, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update message content
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMessageContent(long messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete a message
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMessage(long messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete all messages in a session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMessagesForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Get message count for session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMessageCountForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    /**
     * Get total token usage for session
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalTokensForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}