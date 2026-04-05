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
 * Hilt module for conversation repository bindings
 */
@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\tH\'J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\fH\'J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u000fH\'J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u0012H\'J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0015H\'\u00a8\u0006\u0016"}, d2 = {"Lcom/dairum/di/ConversationRepositoryModule;", "", "()V", "bindChatEmbeddingRepository", "Lcom/dairum/domain/repository/ChatEmbeddingRepository;", "impl", "Lcom/dairum/data/repository/ChatEmbeddingRepositoryImpl;", "bindChatExportRepository", "Lcom/dairum/domain/repository/ChatExportRepository;", "Lcom/dairum/data/repository/ChatExportRepositoryImpl;", "bindChatMessageRepository", "Lcom/dairum/domain/repository/ChatMessageRepository;", "Lcom/dairum/data/repository/ChatMessageRepositoryImpl;", "bindChatSessionRepository", "Lcom/dairum/domain/repository/ChatSessionRepository;", "Lcom/dairum/data/repository/ChatSessionRepositoryImpl;", "bindConversationContextRepository", "Lcom/dairum/domain/repository/ConversationContextRepository;", "Lcom/dairum/data/repository/ConversationContextRepositoryImpl;", "bindReflectionMemoryRepository", "Lcom/dairum/domain/repository/ReflectionMemoryRepository;", "Lcom/dairum/data/repository/ReflectionMemoryRepositoryImpl;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class ConversationRepositoryModule {
    
    public ConversationRepositoryModule() {
        super();
    }
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.ChatSessionRepository bindChatSessionRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.ChatSessionRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.ChatMessageRepository bindChatMessageRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.ChatMessageRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.ChatEmbeddingRepository bindChatEmbeddingRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.ChatEmbeddingRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.ReflectionMemoryRepository bindReflectionMemoryRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.ReflectionMemoryRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.ConversationContextRepository bindConversationContextRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.ConversationContextRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.ChatExportRepository bindChatExportRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.ChatExportRepositoryImpl impl);
}