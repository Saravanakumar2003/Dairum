package com.dairum.data.ai.conversation;

import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.data.ai.reflection.ReflectionGenerationService;
import com.dairum.data.ai.retrieval.ConversationMemoryService;
import com.dairum.domain.repository.ChatMessageRepository;
import com.dairum.domain.repository.ChatSessionRepository;
import com.dairum.domain.repository.ConversationContextRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ConversationAIOrchestrator_Factory implements Factory<ConversationAIOrchestrator> {
  private final Provider<LocalLLM> localLLMProvider;

  private final Provider<EmbeddingService> embeddingServiceProvider;

  private final Provider<ConversationMemoryService> memoryServiceProvider;

  private final Provider<ReflectionGenerationService> reflectionServiceProvider;

  private final Provider<ChatSessionRepository> chatSessionRepositoryProvider;

  private final Provider<ChatMessageRepository> chatMessageRepositoryProvider;

  private final Provider<ConversationContextRepository> conversationContextRepositoryProvider;

  public ConversationAIOrchestrator_Factory(Provider<LocalLLM> localLLMProvider,
      Provider<EmbeddingService> embeddingServiceProvider,
      Provider<ConversationMemoryService> memoryServiceProvider,
      Provider<ReflectionGenerationService> reflectionServiceProvider,
      Provider<ChatSessionRepository> chatSessionRepositoryProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider,
      Provider<ConversationContextRepository> conversationContextRepositoryProvider) {
    this.localLLMProvider = localLLMProvider;
    this.embeddingServiceProvider = embeddingServiceProvider;
    this.memoryServiceProvider = memoryServiceProvider;
    this.reflectionServiceProvider = reflectionServiceProvider;
    this.chatSessionRepositoryProvider = chatSessionRepositoryProvider;
    this.chatMessageRepositoryProvider = chatMessageRepositoryProvider;
    this.conversationContextRepositoryProvider = conversationContextRepositoryProvider;
  }

  @Override
  public ConversationAIOrchestrator get() {
    return newInstance(localLLMProvider.get(), embeddingServiceProvider.get(), memoryServiceProvider.get(), reflectionServiceProvider.get(), chatSessionRepositoryProvider.get(), chatMessageRepositoryProvider.get(), conversationContextRepositoryProvider.get());
  }

  public static ConversationAIOrchestrator_Factory create(Provider<LocalLLM> localLLMProvider,
      Provider<EmbeddingService> embeddingServiceProvider,
      Provider<ConversationMemoryService> memoryServiceProvider,
      Provider<ReflectionGenerationService> reflectionServiceProvider,
      Provider<ChatSessionRepository> chatSessionRepositoryProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider,
      Provider<ConversationContextRepository> conversationContextRepositoryProvider) {
    return new ConversationAIOrchestrator_Factory(localLLMProvider, embeddingServiceProvider, memoryServiceProvider, reflectionServiceProvider, chatSessionRepositoryProvider, chatMessageRepositoryProvider, conversationContextRepositoryProvider);
  }

  public static ConversationAIOrchestrator newInstance(LocalLLM localLLM,
      EmbeddingService embeddingService, ConversationMemoryService memoryService,
      ReflectionGenerationService reflectionService, ChatSessionRepository chatSessionRepository,
      ChatMessageRepository chatMessageRepository,
      ConversationContextRepository conversationContextRepository) {
    return new ConversationAIOrchestrator(localLLM, embeddingService, memoryService, reflectionService, chatSessionRepository, chatMessageRepository, conversationContextRepository);
  }
}
