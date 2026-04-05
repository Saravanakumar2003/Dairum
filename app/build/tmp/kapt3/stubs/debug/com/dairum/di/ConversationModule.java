package com.dairum.di;

import com.dairum.data.ai.conversation.ConversationAIOrchestrator;
import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.data.ai.reflection.ReflectionGenerationService;
import com.dairum.data.ai.retrieval.ConversationMemoryService;
import com.dairum.data.repository.*;
import com.dairum.domain.repository.*;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

/**
 * Hilt module for conversation-related dependencies
 */
@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J@\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J8\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0007J0\u0010\u001c\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007\u00a8\u0006\u001d"}, d2 = {"Lcom/dairum/di/ConversationModule;", "", "()V", "provideConversationAIOrchestrator", "Lcom/dairum/data/ai/conversation/ConversationAIOrchestrator;", "localLLM", "Lcom/dairum/data/ai/llm/LocalLLM;", "embeddingService", "Lcom/dairum/data/ai/embedding/EmbeddingService;", "memoryService", "Lcom/dairum/data/ai/retrieval/ConversationMemoryService;", "reflectionService", "Lcom/dairum/data/ai/reflection/ReflectionGenerationService;", "chatSessionRepository", "Lcom/dairum/domain/repository/ChatSessionRepository;", "chatMessageRepository", "Lcom/dairum/domain/repository/ChatMessageRepository;", "conversationContextRepository", "Lcom/dairum/domain/repository/ConversationContextRepository;", "provideConversationMemoryService", "chatEmbeddingRepository", "Lcom/dairum/domain/repository/ChatEmbeddingRepository;", "reflectionMemoryRepository", "Lcom/dairum/domain/repository/ReflectionMemoryRepository;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "embeddingRepository", "Lcom/dairum/domain/repository/EmbeddingRepository;", "provideReflectionGenerationService", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class ConversationModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.di.ConversationModule INSTANCE = null;
    
    private ConversationModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.ai.retrieval.ConversationMemoryService provideConversationMemoryService(@org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.embedding.EmbeddingService embeddingService, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ChatMessageRepository chatMessageRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ChatEmbeddingRepository chatEmbeddingRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ReflectionMemoryRepository reflectionMemoryRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.EmbeddingRepository embeddingRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.ai.reflection.ReflectionGenerationService provideReflectionGenerationService(@org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.llm.LocalLLM localLLM, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.embedding.EmbeddingService embeddingService, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ReflectionMemoryRepository reflectionMemoryRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ChatMessageRepository chatMessageRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ConversationContextRepository conversationContextRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.ai.conversation.ConversationAIOrchestrator provideConversationAIOrchestrator(@org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.llm.LocalLLM localLLM, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.embedding.EmbeddingService embeddingService, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.retrieval.ConversationMemoryService memoryService, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.reflection.ReflectionGenerationService reflectionService, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ChatSessionRepository chatSessionRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ChatMessageRepository chatMessageRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ConversationContextRepository conversationContextRepository) {
        return null;
    }
}