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
 * Hilt module for AI-related dependencies
 */
@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0007\u00a8\u0006\u0014"}, d2 = {"Lcom/dairum/di/AIModule;", "", "()V", "provideEmbeddingService", "Lcom/dairum/data/ai/embedding/EmbeddingService;", "context", "Landroid/content/Context;", "provideLocalLLM", "Lcom/dairum/data/ai/llm/LocalLLM;", "provideRetrievalService", "Lcom/dairum/data/ai/retrieval/RetrievalService;", "embeddingService", "embeddingRepository", "Lcom/dairum/domain/repository/EmbeddingRepository;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "modelManager", "Lcom/dairum/data/ai/model/ModelManager;", "modelRepository", "Lcom/dairum/domain/repository/AIModelRepository;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AIModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.di.AIModule INSTANCE = null;
    
    private AIModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.ai.embedding.EmbeddingService provideEmbeddingService(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.ai.llm.LocalLLM provideLocalLLM(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.ai.retrieval.RetrievalService provideRetrievalService(@org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.embedding.EmbeddingService embeddingService, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.EmbeddingRepository embeddingRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.model.ModelManager modelManager, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.AIModelRepository modelRepository) {
        return null;
    }
}