package com.dairum.di;

import android.content.Context;
import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.embedding.ONNXEmbeddingService;
import com.dairum.data.ai.llm.FallbackLLM;
import com.dairum.data.ai.llm.LlamaCppLLM;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.data.ai.retrieval.LocalRetrievalService;
import com.dairum.data.ai.retrieval.RetrievalService;
import com.dairum.data.repository.*;
import com.dairum.domain.repository.*;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

/**
 * Hilt module for AI repository bindings
 */
@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\tH\'J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\fH\'J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u000fH\'\u00a8\u0006\u0010"}, d2 = {"Lcom/dairum/di/AIRepositoryModule;", "", "()V", "bindAIContentRepository", "Lcom/dairum/domain/repository/AIContentRepository;", "impl", "Lcom/dairum/data/repository/AIContentRepositoryImpl;", "bindAIConversationRepository", "Lcom/dairum/domain/repository/AIConversationRepository;", "Lcom/dairum/data/repository/AIConversationRepositoryImpl;", "bindAIModelRepository", "Lcom/dairum/domain/repository/AIModelRepository;", "Lcom/dairum/data/repository/AIModelRepositoryImpl;", "bindEmbeddingRepository", "Lcom/dairum/domain/repository/EmbeddingRepository;", "Lcom/dairum/data/repository/EmbeddingRepositoryImpl;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class AIRepositoryModule {
    
    public AIRepositoryModule() {
        super();
    }
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.AIModelRepository bindAIModelRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.AIModelRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.EmbeddingRepository bindEmbeddingRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.EmbeddingRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.AIContentRepository bindAIContentRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.AIContentRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.AIConversationRepository bindAIConversationRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.AIConversationRepositoryImpl impl);
}