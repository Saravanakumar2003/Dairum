package com.dairum.di;

import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.data.ai.retrieval.RetrievalService;
import com.dairum.domain.repository.AIModelRepository;
import com.dairum.domain.repository.EmbeddingRepository;
import com.dairum.domain.repository.JournalRepository;
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
public final class AIModule_ProvideRetrievalServiceFactory implements Factory<RetrievalService> {
  private final Provider<EmbeddingService> embeddingServiceProvider;

  private final Provider<EmbeddingRepository> embeddingRepositoryProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<ModelManager> modelManagerProvider;

  private final Provider<AIModelRepository> modelRepositoryProvider;

  public AIModule_ProvideRetrievalServiceFactory(
      Provider<EmbeddingService> embeddingServiceProvider,
      Provider<EmbeddingRepository> embeddingRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<ModelManager> modelManagerProvider,
      Provider<AIModelRepository> modelRepositoryProvider) {
    this.embeddingServiceProvider = embeddingServiceProvider;
    this.embeddingRepositoryProvider = embeddingRepositoryProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.modelManagerProvider = modelManagerProvider;
    this.modelRepositoryProvider = modelRepositoryProvider;
  }

  @Override
  public RetrievalService get() {
    return provideRetrievalService(embeddingServiceProvider.get(), embeddingRepositoryProvider.get(), journalRepositoryProvider.get(), modelManagerProvider.get(), modelRepositoryProvider.get());
  }

  public static AIModule_ProvideRetrievalServiceFactory create(
      Provider<EmbeddingService> embeddingServiceProvider,
      Provider<EmbeddingRepository> embeddingRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<ModelManager> modelManagerProvider,
      Provider<AIModelRepository> modelRepositoryProvider) {
    return new AIModule_ProvideRetrievalServiceFactory(embeddingServiceProvider, embeddingRepositoryProvider, journalRepositoryProvider, modelManagerProvider, modelRepositoryProvider);
  }

  public static RetrievalService provideRetrievalService(EmbeddingService embeddingService,
      EmbeddingRepository embeddingRepository, JournalRepository journalRepository,
      ModelManager modelManager, AIModelRepository modelRepository) {
    return Preconditions.checkNotNullFromProvides(AIModule.INSTANCE.provideRetrievalService(embeddingService, embeddingRepository, journalRepository, modelManager, modelRepository));
  }
}
