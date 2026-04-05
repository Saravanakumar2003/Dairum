package com.dairum.data.ai.usecase;

import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.data.ai.retrieval.RetrievalService;
import com.dairum.domain.repository.AIContentRepository;
import com.dairum.domain.repository.EmbeddingRepository;
import com.dairum.domain.repository.JournalRepository;
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
public final class AIOrchestrator_Factory implements Factory<AIOrchestrator> {
  private final Provider<RetrievalService> retrievalServiceProvider;

  private final Provider<LocalLLM> localLLMProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<AIContentRepository> aiContentRepositoryProvider;

  private final Provider<EmbeddingRepository> embeddingRepositoryProvider;

  public AIOrchestrator_Factory(Provider<RetrievalService> retrievalServiceProvider,
      Provider<LocalLLM> localLLMProvider, Provider<JournalRepository> journalRepositoryProvider,
      Provider<AIContentRepository> aiContentRepositoryProvider,
      Provider<EmbeddingRepository> embeddingRepositoryProvider) {
    this.retrievalServiceProvider = retrievalServiceProvider;
    this.localLLMProvider = localLLMProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.aiContentRepositoryProvider = aiContentRepositoryProvider;
    this.embeddingRepositoryProvider = embeddingRepositoryProvider;
  }

  @Override
  public AIOrchestrator get() {
    return newInstance(retrievalServiceProvider.get(), localLLMProvider.get(), journalRepositoryProvider.get(), aiContentRepositoryProvider.get(), embeddingRepositoryProvider.get());
  }

  public static AIOrchestrator_Factory create(Provider<RetrievalService> retrievalServiceProvider,
      Provider<LocalLLM> localLLMProvider, Provider<JournalRepository> journalRepositoryProvider,
      Provider<AIContentRepository> aiContentRepositoryProvider,
      Provider<EmbeddingRepository> embeddingRepositoryProvider) {
    return new AIOrchestrator_Factory(retrievalServiceProvider, localLLMProvider, journalRepositoryProvider, aiContentRepositoryProvider, embeddingRepositoryProvider);
  }

  public static AIOrchestrator newInstance(RetrievalService retrievalService, LocalLLM localLLM,
      JournalRepository journalRepository, AIContentRepository aiContentRepository,
      EmbeddingRepository embeddingRepository) {
    return new AIOrchestrator(retrievalService, localLLM, journalRepository, aiContentRepository, embeddingRepository);
  }
}
