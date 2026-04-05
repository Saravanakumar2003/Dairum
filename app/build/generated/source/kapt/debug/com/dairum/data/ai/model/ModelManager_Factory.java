package com.dairum.data.ai.model;

import android.content.Context;
import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.domain.repository.AIModelRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ModelManager_Factory implements Factory<ModelManager> {
  private final Provider<Context> contextProvider;

  private final Provider<AIModelRepository> modelRepositoryProvider;

  private final Provider<EmbeddingService> embeddingServiceProvider;

  private final Provider<LocalLLM> localLLMProvider;

  private final Provider<OkHttpClient> okHttpClientProvider;

  public ModelManager_Factory(Provider<Context> contextProvider,
      Provider<AIModelRepository> modelRepositoryProvider,
      Provider<EmbeddingService> embeddingServiceProvider, Provider<LocalLLM> localLLMProvider,
      Provider<OkHttpClient> okHttpClientProvider) {
    this.contextProvider = contextProvider;
    this.modelRepositoryProvider = modelRepositoryProvider;
    this.embeddingServiceProvider = embeddingServiceProvider;
    this.localLLMProvider = localLLMProvider;
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public ModelManager get() {
    return newInstance(contextProvider.get(), modelRepositoryProvider.get(), embeddingServiceProvider.get(), localLLMProvider.get(), okHttpClientProvider.get());
  }

  public static ModelManager_Factory create(Provider<Context> contextProvider,
      Provider<AIModelRepository> modelRepositoryProvider,
      Provider<EmbeddingService> embeddingServiceProvider, Provider<LocalLLM> localLLMProvider,
      Provider<OkHttpClient> okHttpClientProvider) {
    return new ModelManager_Factory(contextProvider, modelRepositoryProvider, embeddingServiceProvider, localLLMProvider, okHttpClientProvider);
  }

  public static ModelManager newInstance(Context context, AIModelRepository modelRepository,
      EmbeddingService embeddingService, LocalLLM localLLM, OkHttpClient okHttpClient) {
    return new ModelManager(context, modelRepository, embeddingService, localLLM, okHttpClient);
  }
}
