package com.dairum.data.ai.reflection;

import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.domain.repository.ChatMessageRepository;
import com.dairum.domain.repository.ConversationContextRepository;
import com.dairum.domain.repository.ReflectionMemoryRepository;
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
public final class ReflectionGenerationService_Factory implements Factory<ReflectionGenerationService> {
  private final Provider<LocalLLM> localLLMProvider;

  private final Provider<EmbeddingService> embeddingServiceProvider;

  private final Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider;

  private final Provider<ChatMessageRepository> chatMessageRepositoryProvider;

  private final Provider<ConversationContextRepository> conversationContextRepositoryProvider;

  public ReflectionGenerationService_Factory(Provider<LocalLLM> localLLMProvider,
      Provider<EmbeddingService> embeddingServiceProvider,
      Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider,
      Provider<ConversationContextRepository> conversationContextRepositoryProvider) {
    this.localLLMProvider = localLLMProvider;
    this.embeddingServiceProvider = embeddingServiceProvider;
    this.reflectionMemoryRepositoryProvider = reflectionMemoryRepositoryProvider;
    this.chatMessageRepositoryProvider = chatMessageRepositoryProvider;
    this.conversationContextRepositoryProvider = conversationContextRepositoryProvider;
  }

  @Override
  public ReflectionGenerationService get() {
    return newInstance(localLLMProvider.get(), embeddingServiceProvider.get(), reflectionMemoryRepositoryProvider.get(), chatMessageRepositoryProvider.get(), conversationContextRepositoryProvider.get());
  }

  public static ReflectionGenerationService_Factory create(Provider<LocalLLM> localLLMProvider,
      Provider<EmbeddingService> embeddingServiceProvider,
      Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider,
      Provider<ConversationContextRepository> conversationContextRepositoryProvider) {
    return new ReflectionGenerationService_Factory(localLLMProvider, embeddingServiceProvider, reflectionMemoryRepositoryProvider, chatMessageRepositoryProvider, conversationContextRepositoryProvider);
  }

  public static ReflectionGenerationService newInstance(LocalLLM localLLM,
      EmbeddingService embeddingService, ReflectionMemoryRepository reflectionMemoryRepository,
      ChatMessageRepository chatMessageRepository,
      ConversationContextRepository conversationContextRepository) {
    return new ReflectionGenerationService(localLLM, embeddingService, reflectionMemoryRepository, chatMessageRepository, conversationContextRepository);
  }
}
