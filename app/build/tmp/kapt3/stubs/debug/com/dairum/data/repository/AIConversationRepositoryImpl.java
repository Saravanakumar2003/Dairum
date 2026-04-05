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
 * Implementation of AIConversationRepository
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006JF\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\u000e\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u0015J\u000e\u0010\u0016\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u00172\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u001dH\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0002J\u0014\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\t0$H\u0016J\u001c\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\t0$2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000RJ\u0010\u0007\u001a>\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\n \u000b*\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t0\t \u000b*\u001e\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\n \u000b*\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t0\t\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/dairum/data/repository/AIConversationRepositoryImpl;", "Lcom/dairum/domain/repository/AIConversationRepository;", "conversationDao", "Lcom/dairum/data/local/dao/AIConversationDao;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Lcom/dairum/data/local/dao/AIConversationDao;Lcom/squareup/moshi/Moshi;)V", "longListAdapter", "Lcom/squareup/moshi/JsonAdapter;", "", "", "kotlin.jvm.PlatformType", "addMessage", "sessionId", "", "role", "content", "sourceEntryIds", "tokensUsed", "", "generationTimeMs", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;IJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllConversations", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteConversation", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteOldConversations", "olderThan", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "entityToMessage", "Lcom/dairum/domain/repository/AIConversationMessage;", "entity", "Lcom/dairum/data/local/entity/AIConversationEntity;", "getAllSessionIds", "Lkotlinx/coroutines/flow/Flow;", "getConversation", "app_debug"})
public final class AIConversationRepositoryImpl implements com.dairum.domain.repository.AIConversationRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.AIConversationDao conversationDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.squareup.moshi.Moshi moshi = null;
    private final com.squareup.moshi.JsonAdapter<java.util.List<java.lang.Long>> longListAdapter = null;
    
    @javax.inject.Inject()
    public AIConversationRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.AIConversationDao conversationDao, @org.jetbrains.annotations.NotNull()
    com.squareup.moshi.Moshi moshi) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.AIConversationMessage>> getConversation(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<java.lang.String>> getAllSessionIds() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object addMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String role, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.Long> sourceEntryIds, int tokensUsed, long generationTimeMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteConversation(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteOldConversations(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime olderThan, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteAllConversations(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.repository.AIConversationMessage entityToMessage(com.dairum.data.local.entity.AIConversationEntity entity) {
        return null;
    }
}