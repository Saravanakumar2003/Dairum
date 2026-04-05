package com.dairum.presentation.screens.ai;

import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.data.ai.retrieval.RetrievalService;
import com.dairum.data.ai.usecase.AIOrchestrator;
import com.dairum.domain.repository.AIContentRepository;
import com.dairum.domain.repository.AIModelRepository;
import com.dairum.domain.repository.EmbeddingRepository;
import com.dairum.domain.repository.JournalRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AIViewModel_Factory implements Factory<AIViewModel> {
  private final Provider<AIOrchestrator> aiOrchestratorProvider;

  private final Provider<ModelManager> modelManagerProvider;

  private final Provider<AIModelRepository> modelRepositoryProvider;

  private final Provider<AIContentRepository> contentRepositoryProvider;

  private final Provider<RetrievalService> retrievalServiceProvider;

  private final Provider<LocalLLM> localLLMProvider;

  private final Provider<EmbeddingRepository> embeddingRepositoryProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  public AIViewModel_Factory(Provider<AIOrchestrator> aiOrchestratorProvider,
      Provider<ModelManager> modelManagerProvider,
      Provider<AIModelRepository> modelRepositoryProvider,
      Provider<AIContentRepository> contentRepositoryProvider,
      Provider<RetrievalService> retrievalServiceProvider, Provider<LocalLLM> localLLMProvider,
      Provider<EmbeddingRepository> embeddingRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider) {
    this.aiOrchestratorProvider = aiOrchestratorProvider;
    this.modelManagerProvider = modelManagerProvider;
    this.modelRepositoryProvider = modelRepositoryProvider;
    this.contentRepositoryProvider = contentRepositoryProvider;
    this.retrievalServiceProvider = retrievalServiceProvider;
    this.localLLMProvider = localLLMProvider;
    this.embeddingRepositoryProvider = embeddingRepositoryProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
  }

  @Override
  public AIViewModel get() {
    return newInstance(aiOrchestratorProvider.get(), modelManagerProvider.get(), modelRepositoryProvider.get(), contentRepositoryProvider.get(), retrievalServiceProvider.get(), localLLMProvider.get(), embeddingRepositoryProvider.get(), journalRepositoryProvider.get());
  }

  public static AIViewModel_Factory create(Provider<AIOrchestrator> aiOrchestratorProvider,
      Provider<ModelManager> modelManagerProvider,
      Provider<AIModelRepository> modelRepositoryProvider,
      Provider<AIContentRepository> contentRepositoryProvider,
      Provider<RetrievalService> retrievalServiceProvider, Provider<LocalLLM> localLLMProvider,
      Provider<EmbeddingRepository> embeddingRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider) {
    return new AIViewModel_Factory(aiOrchestratorProvider, modelManagerProvider, modelRepositoryProvider, contentRepositoryProvider, retrievalServiceProvider, localLLMProvider, embeddingRepositoryProvider, journalRepositoryProvider);
  }

  public static AIViewModel newInstance(AIOrchestrator aiOrchestrator, ModelManager modelManager,
      AIModelRepository modelRepository, AIContentRepository contentRepository,
      RetrievalService retrievalService, LocalLLM localLLM, EmbeddingRepository embeddingRepository,
      JournalRepository journalRepository) {
    return new AIViewModel(aiOrchestrator, modelManager, modelRepository, contentRepository, retrievalService, localLLM, embeddingRepository, journalRepository);
  }
}
