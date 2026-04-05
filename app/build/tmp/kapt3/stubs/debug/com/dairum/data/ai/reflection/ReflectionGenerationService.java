package com.dairum.data.ai.reflection;

import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.*;
import kotlinx.coroutines.Dispatchers;
import timber.log.Timber;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Service for generating reflection memories from conversations.
 *
 * Periodically analyzes conversations to extract lasting insights about the user,
 * such as personality traits, values, concerns, goals, and behavioral patterns.
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0004\b\u0007\u0018\u0000 32\u00020\u0001:\u00013B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0002J\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u001c\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0018\u0010\u0016J\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001cH\u0002J4\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\u00100\u00132\u0006\u0010 \u001a\u00020\u000e2\b\b\u0002\u0010!\u001a\u00020\"H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b#\u0010$J\u001c\u0010%\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00102\u0006\u0010&\u001a\u00020\'H\u0086@\u00a2\u0006\u0002\u0010(J&\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\u0006\u0010 \u001a\u00020\u000e2\b\u0010*\u001a\u0004\u0018\u00010+H\u0082@\u00a2\u0006\u0002\u0010,J,\u0010-\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00102\u0006\u0010.\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020\u000e2\f\u0010/\u001a\b\u0012\u0004\u0012\u0002000\u0010H\u0002J\u0016\u00101\u001a\u00020\"2\u0006\u0010 \u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u00102R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00064"}, d2 = {"Lcom/dairum/data/ai/reflection/ReflectionGenerationService;", "", "localLLM", "Lcom/dairum/data/ai/llm/LocalLLM;", "embeddingService", "Lcom/dairum/data/ai/embedding/EmbeddingService;", "reflectionMemoryRepository", "Lcom/dairum/domain/repository/ReflectionMemoryRepository;", "chatMessageRepository", "Lcom/dairum/domain/repository/ChatMessageRepository;", "conversationContextRepository", "Lcom/dairum/domain/repository/ConversationContextRepository;", "(Lcom/dairum/data/ai/llm/LocalLLM;Lcom/dairum/data/ai/embedding/EmbeddingService;Lcom/dairum/domain/repository/ReflectionMemoryRepository;Lcom/dairum/domain/repository/ChatMessageRepository;Lcom/dairum/domain/repository/ConversationContextRepository;)V", "buildReflectionPrompt", "", "messages", "", "Lcom/dairum/domain/model/ChatMessage;", "cleanupReflections", "Lkotlin/Result;", "", "cleanupReflections-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "consolidateReflections", "consolidateReflections-IoAF18A", "cosineSimilarity", "", "a", "", "b", "generateReflections", "Lcom/dairum/domain/model/ReflectionMemory;", "sessionId", "forceGeneration", "", "generateReflections-0E7RQCE", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCategoryInsights", "category", "Lcom/dairum/domain/model/ReflectionCategory;", "(Lcom/dairum/domain/model/ReflectionCategory;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMessagesForReflectionGeneration", "lastSummaryAt", "Ljava/time/LocalDateTime;", "(Ljava/lang/String;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseReflections", "response", "sourceMessageIds", "", "shouldGenerateReflection", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class ReflectionGenerationService {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.llm.LocalLLM localLLM = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.embedding.EmbeddingService embeddingService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ReflectionMemoryRepository reflectionMemoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ChatMessageRepository chatMessageRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ConversationContextRepository conversationContextRepository = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ReflectionGenerationService";
    public static final int REFLECTION_MESSAGE_INTERVAL = 12;
    public static final int MIN_MESSAGES_FOR_REFLECTION = 8;
    public static final int MAX_REFLECTIONS_PER_BATCH = 5;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.ai.reflection.ReflectionGenerationService.Companion Companion = null;
    
    @javax.inject.Inject()
    public ReflectionGenerationService(@org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.llm.LocalLLM localLLM, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.embedding.EmbeddingService embeddingService, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ReflectionMemoryRepository reflectionMemoryRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ChatMessageRepository chatMessageRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ConversationContextRepository conversationContextRepository) {
        super();
    }
    
    /**
     * Check if reflection generation is needed based on message count
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object shouldGenerateReflection(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Get messages to use for reflection generation
     */
    private final java.lang.Object getMessagesForReflectionGeneration(java.lang.String sessionId, java.time.LocalDateTime lastSummaryAt, kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ChatMessage>> $completion) {
        return null;
    }
    
    /**
     * Build the prompt for reflection generation
     */
    private final java.lang.String buildReflectionPrompt(java.util.List<com.dairum.domain.model.ChatMessage> messages) {
        return null;
    }
    
    /**
     * Parse reflections from LLM response
     */
    private final java.util.List<com.dairum.domain.model.ReflectionMemory> parseReflections(java.lang.String response, java.lang.String sessionId, java.util.List<java.lang.Long> sourceMessageIds) {
        return null;
    }
    
    /**
     * Get a summary of reflection insights for a category
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCategoryInsights(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionCategory category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion) {
        return null;
    }
    
    private final float cosineSimilarity(float[] a, float[] b) {
        return 0.0F;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/dairum/data/ai/reflection/ReflectionGenerationService$Companion;", "", "()V", "MAX_REFLECTIONS_PER_BATCH", "", "MIN_MESSAGES_FOR_REFLECTION", "REFLECTION_MESSAGE_INTERVAL", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}