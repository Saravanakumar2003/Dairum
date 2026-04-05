package com.dairum.di

import android.content.Context
import com.dairum.data.ai.embedding.EmbeddingService
import com.dairum.data.ai.embedding.ONNXEmbeddingService
import com.dairum.data.ai.llm.FallbackLLM
import com.dairum.data.ai.llm.LlamaCppLLM
import com.dairum.data.ai.llm.LocalLLM
import com.dairum.data.ai.model.ModelManager
import com.dairum.data.ai.retrieval.LocalRetrievalService
import com.dairum.data.ai.retrieval.RetrievalService
import com.dairum.data.repository.*
import com.dairum.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for AI-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AIModule {
    
    @Provides
    @Singleton
    fun provideEmbeddingService(
        @ApplicationContext context: Context
    ): EmbeddingService {
        return ONNXEmbeddingService(context)
    }
    
    @Provides
    @Singleton
    fun provideLocalLLM(
        @ApplicationContext context: Context
    ): LocalLLM {
        // Try to use LlamaCpp, fallback if native library not available
        return try {
            LlamaCppLLM(context)
        } catch (e: UnsatisfiedLinkError) {
            FallbackLLM()
        }
    }
    
    @Provides
    @Singleton
    fun provideRetrievalService(
        embeddingService: EmbeddingService,
        embeddingRepository: EmbeddingRepository,
        journalRepository: JournalRepository,
        modelManager: ModelManager,
        modelRepository: AIModelRepository
    ): RetrievalService {
        return LocalRetrievalService(
            embeddingService = embeddingService,
            embeddingRepository = embeddingRepository,
            journalRepository = journalRepository,
            modelManager = modelManager,
            modelRepository = modelRepository
        )
    }
}

/**
 * Hilt module for AI repository bindings
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AIRepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAIModelRepository(
        impl: AIModelRepositoryImpl
    ): AIModelRepository
    
    @Binds
    @Singleton
    abstract fun bindEmbeddingRepository(
        impl: EmbeddingRepositoryImpl
    ): EmbeddingRepository
    
    @Binds
    @Singleton
    abstract fun bindAIContentRepository(
        impl: AIContentRepositoryImpl
    ): AIContentRepository
    
    @Binds
    @Singleton
    abstract fun bindAIConversationRepository(
        impl: AIConversationRepositoryImpl
    ): AIConversationRepository
}
