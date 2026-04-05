package com.dairum.di;

import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.data.ai.reflection.ReflectionGenerationService;
import com.dairum.domain.repository.ChatMessageRepository;
import com.dairum.domain.repository.ConversationContextRepository;
import com.dairum.domain.repository.ReflectionMemoryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class ConversationModule_ProvideReflectionGenerationServiceFactory implements Factory<ReflectionGenerationService> {
  private final Provider<LocalLLM> localLLMProvider;

  private final Provider<EmbeddingService> embeddingServiceProvider;

  private final Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider;

  private final Provider<ChatMessageRepository> chatMessageRepositoryProvider;

  private final Provider<ConversationContextRepository> conversationContextRepositoryProvider;

  public ConversationModule_ProvideReflectionGenerationServiceFactory(
      Provider<LocalLLM> localLLMProvider, Provider<EmbeddingService> embeddingServiceProvider,
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
    return provideReflectionGenerationService(localLLMProvider.get(), embeddingServiceProvider.get(), reflectionMemoryRepositoryProvider.get(), chatMessageRepositoryProvider.get(), conversationContextRepositoryProvider.get());
  }

  public static ConversationModule_ProvideReflectionGenerationServiceFactory create(
      Provider<LocalLLM> localLLMProvider, Provider<EmbeddingService> embeddingServiceProvider,
      Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider,
      Provider<ConversationContextRepository> conversationContextRepositoryProvider) {
    return new ConversationModule_ProvideReflectionGenerationServiceFactory(localLLMProvider, embeddingServiceProvider, reflectionMemoryRepositoryProvider, chatMessageRepositoryProvider, conversationContextRepositoryProvider);
  }

  public static ReflectionGenerationService provideReflectionGenerationService(LocalLLM localLLM,
      EmbeddingService embeddingService, ReflectionMemoryRepository reflectionMemoryRepository,
      ChatMessageRepository chatMessageRepository,
      ConversationContextRepository conversationContextRepository) {
    return Preconditions.checkNotNullFromProvides(ConversationModule.INSTANCE.provideReflectionGenerationService(localLLM, embeddingService, reflectionMemoryRepository, chatMessageRepository, conversationContextRepository));
  }
}
