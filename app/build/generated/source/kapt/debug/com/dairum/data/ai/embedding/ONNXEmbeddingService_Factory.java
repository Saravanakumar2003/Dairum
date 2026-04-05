package com.dairum.data.ai.embedding;

import android.content.Context;
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
public final class ONNXEmbeddingService_Factory implements Factory<ONNXEmbeddingService> {
  private final Provider<Context> contextProvider;

  public ONNXEmbeddingService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ONNXEmbeddingService get() {
    return newInstance(contextProvider.get());
  }

  public static ONNXEmbeddingService_Factory create(Provider<Context> contextProvider) {
    return new ONNXEmbeddingService_Factory(contextProvider);
  }

  public static ONNXEmbeddingService newInstance(Context context) {
    return new ONNXEmbeddingService(context);
  }
}
