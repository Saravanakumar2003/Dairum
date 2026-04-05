package com.dairum.di;

import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.retrieval.ConversationMemoryService;
import com.dairum.domain.repository.ChatEmbeddingRepository;
import com.dairum.domain.repository.ChatMessageRepository;
import com.dairum.domain.repository.EmbeddingRepository;
import com.dairum.domain.repository.JournalRepository;
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
public final class ConversationModule_ProvideConversationMemoryServiceFactory implements Factory<ConversationMemoryService> {
  private final Provider<EmbeddingService> embeddingServiceProvider;

  private final Provider<ChatMessageRepository> chatMessageRepositoryProvider;

  private final Provider<ChatEmbeddingRepository> chatEmbeddingRepositoryProvider;

  private final Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<EmbeddingRepository> embeddingRepositoryProvider;

  public ConversationModule_ProvideConversationMemoryServiceFactory(
      Provider<EmbeddingService> embeddingServiceProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider,
      Provider<ChatEmbeddingRepository> chatEmbeddingRepositoryProvider,
      Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<EmbeddingRepository> embeddingRepositoryProvider) {
    this.embeddingServiceProvider = embeddingServiceProvider;
    this.chatMessageRepositoryProvider = chatMessageRepositoryProvider;
    this.chatEmbeddingRepositoryProvider = chatEmbeddingRepositoryProvider;
    this.reflectionMemoryRepositoryProvider = reflectionMemoryRepositoryProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.embeddingRepositoryProvider = embeddingRepositoryProvider;
  }

  @Override
  public ConversationMemoryService get() {
    return provideConversationMemoryService(embeddingServiceProvider.get(), chatMessageRepositoryProvider.get(), chatEmbeddingRepositoryProvider.get(), reflectionMemoryRepositoryProvider.get(), journalRepositoryProvider.get(), embeddingRepositoryProvider.get());
  }

  public static ConversationModule_ProvideConversationMemoryServiceFactory create(
      Provider<EmbeddingService> embeddingServiceProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider,
      Provider<ChatEmbeddingRepository> chatEmbeddingRepositoryProvider,
      Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<EmbeddingRepository> embeddingRepositoryProvider) {
    return new ConversationModule_ProvideConversationMemoryServiceFactory(embeddingServiceProvider, chatMessageRepositoryProvider, chatEmbeddingRepositoryProvider, reflectionMemoryRepositoryProvider, journalRepositoryProvider, embeddingRepositoryProvider);
  }

  public static ConversationMemoryService provideConversationMemoryService(
      EmbeddingService embeddingService, ChatMessageRepository chatMessageRepository,
      ChatEmbeddingRepository chatEmbeddingRepository,
      ReflectionMemoryRepository reflectionMemoryRepository, JournalRepository journalRepository,
      EmbeddingRepository embeddingRepository) {
    return Preconditions.checkNotNullFromProvides(ConversationModule.INSTANCE.provideConversationMemoryService(embeddingService, chatMessageRepository, chatEmbeddingRepository, reflectionMemoryRepository, journalRepository, embeddingRepository));
  }
}
