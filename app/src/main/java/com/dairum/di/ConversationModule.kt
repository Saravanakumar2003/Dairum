package com.dairum.di

import com.dairum.data.ai.conversation.ConversationAIOrchestrator
import com.dairum.data.ai.embedding.EmbeddingService
import com.dairum.data.ai.llm.LocalLLM
import com.dairum.data.ai.reflection.ReflectionGenerationService
import com.dairum.data.ai.retrieval.ConversationMemoryService
import com.dairum.data.repository.*
import com.dairum.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for conversation-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object ConversationModule {
    
    @Provides
    @Singleton
    fun provideConversationMemoryService(
        embeddingService: EmbeddingService,
        chatMessageRepository: ChatMessageRepository,
        chatEmbeddingRepository: ChatEmbeddingRepository,
        reflectionMemoryRepository: ReflectionMemoryRepository,
        journalRepository: JournalRepository,
        embeddingRepository: EmbeddingRepository,
        contextualSearchService: com.dairum.data.search.ContextualSearchService
    ): ConversationMemoryService {
        return ConversationMemoryService(
            embeddingService = embeddingService,
            chatMessageRepository = chatMessageRepository,
            chatEmbeddingRepository = chatEmbeddingRepository,
            reflectionMemoryRepository = reflectionMemoryRepository,
            journalRepository = journalRepository,
            embeddingRepository = embeddingRepository,
            contextualSearchService = contextualSearchService
        )
    }
    
    @Provides
    @Singleton
    fun provideReflectionGenerationService(
        localLLM: LocalLLM,
        embeddingService: EmbeddingService,
        reflectionMemoryRepository: ReflectionMemoryRepository,
        chatMessageRepository: ChatMessageRepository,
        conversationContextRepository: ConversationContextRepository
    ): ReflectionGenerationService {
        return ReflectionGenerationService(
            localLLM = localLLM,
            embeddingService = embeddingService,
            reflectionMemoryRepository = reflectionMemoryRepository,
            chatMessageRepository = chatMessageRepository,
            conversationContextRepository = conversationContextRepository
        )
    }
    
    @Provides
    @Singleton
    fun provideConversationAIOrchestrator(
        localLLM: LocalLLM,
        embeddingService: EmbeddingService,
        memoryService: ConversationMemoryService,
        reflectionService: ReflectionGenerationService,
        chatSessionRepository: ChatSessionRepository,
        chatMessageRepository: ChatMessageRepository,
        conversationContextRepository: ConversationContextRepository,
        guardrailsRepository: AIGuardrailsRepository
    ): ConversationAIOrchestrator {
        return ConversationAIOrchestrator(
            localLLM = localLLM,
            embeddingService = embeddingService,
            memoryService = memoryService,
            reflectionService = reflectionService,
            chatSessionRepository = chatSessionRepository,
            chatMessageRepository = chatMessageRepository,
            conversationContextRepository = conversationContextRepository,
            guardrailsRepository = guardrailsRepository
        )
    }
}

/**
 * Hilt module for conversation repository bindings
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ConversationRepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindChatSessionRepository(
        impl: ChatSessionRepositoryImpl
    ): ChatSessionRepository
    
    @Binds
    @Singleton
    abstract fun bindChatMessageRepository(
        impl: ChatMessageRepositoryImpl
    ): ChatMessageRepository
    
    @Binds
    @Singleton
    abstract fun bindChatEmbeddingRepository(
        impl: ChatEmbeddingRepositoryImpl
    ): ChatEmbeddingRepository
    
    @Binds
    @Singleton
    abstract fun bindReflectionMemoryRepository(
        impl: ReflectionMemoryRepositoryImpl
    ): ReflectionMemoryRepository
    
    @Binds
    @Singleton
    abstract fun bindAIGuardrailsRepository(
        impl: AIGuardrailsRepositoryImpl
    ): AIGuardrailsRepository
    
    @Binds
    @Singleton
    abstract fun bindConversationContextRepository(
        impl: ConversationContextRepositoryImpl
    ): ConversationContextRepository
    
    @Binds
    @Singleton
    abstract fun bindChatExportRepository(
        impl: ChatExportRepositoryImpl
    ): ChatExportRepository
}
