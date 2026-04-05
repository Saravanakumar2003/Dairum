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
 * Implementation of ConversationContextRepository
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0010\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u00152\u0006\u0010\u0010\u001a\u00020\nH\u0016J\u0016\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0013H\u0096@\u00a2\u0006\u0002\u0010\u0018J\u001e\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\u001bH\u0096@\u00a2\u0006\u0002\u0010\u001cJ\u001e\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u001fJ\u001e\u0010 \u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\n2\u0006\u0010!\u001a\u00020\"H\u0096@\u00a2\u0006\u0002\u0010#J$\u0010$\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\n2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0096@\u00a2\u0006\u0002\u0010&J\f\u0010\'\u001a\u00020\u0013*\u00020(H\u0002J\f\u0010)\u001a\u00020(*\u00020\u0013H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000RJ\u0010\u0007\u001a>\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\n \u000b*\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t0\t \u000b*\u001e\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\n \u000b*\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t0\t\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000Rb\u0010\f\u001aV\u0012$\u0012\"\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n \u000b*\u0010\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n\u0018\u00010\r0\r \u000b**\u0012$\u0012\"\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n \u000b*\u0010\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n\u0018\u00010\r0\r\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/dairum/data/repository/ConversationContextRepositoryImpl;", "Lcom/dairum/domain/repository/ConversationContextRepository;", "conversationContextDao", "Lcom/dairum/data/local/dao/ConversationContextDao;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Lcom/dairum/data/local/dao/ConversationContextDao;Lcom/squareup/moshi/Moshi;)V", "stringListAdapter", "Lcom/squareup/moshi/JsonAdapter;", "", "", "kotlin.jvm.PlatformType", "stringMapAdapter", "", "deleteContextForSession", "", "sessionId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getContextForSession", "Lcom/dairum/domain/model/ConversationContext;", "observeContextForSession", "Lkotlinx/coroutines/flow/Flow;", "saveContext", "context", "(Lcom/dairum/domain/model/ConversationContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateDiscussionDepth", "depth", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEmotionalTone", "tone", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastSummaryTime", "time", "Ljava/time/LocalDateTime;", "(Ljava/lang/String;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTopics", "topics", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toDomain", "Lcom/dairum/data/local/entity/ConversationContextEntity;", "toEntity", "app_debug"})
public final class ConversationContextRepositoryImpl implements com.dairum.domain.repository.ConversationContextRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.ConversationContextDao conversationContextDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.squareup.moshi.Moshi moshi = null;
    private final com.squareup.moshi.JsonAdapter<java.util.List<java.lang.String>> stringListAdapter = null;
    private final com.squareup.moshi.JsonAdapter<java.util.Map<java.lang.String, java.lang.String>> stringMapAdapter = null;
    
    @javax.inject.Inject()
    public ConversationContextRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.ConversationContextDao conversationContextDao, @org.jetbrains.annotations.NotNull()
    com.squareup.moshi.Moshi moshi) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getContextForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ConversationContext> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.model.ConversationContext> observeContextForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveContext(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ConversationContext context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateTopics(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> topics, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateEmotionalTone(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String tone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateDiscussionDepth(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, int depth, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateLastSummaryTime(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime time, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteContextForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.ConversationContext toDomain(com.dairum.data.local.entity.ConversationContextEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.ConversationContextEntity toEntity(com.dairum.domain.model.ConversationContext $this$toEntity) {
        return null;
    }
}