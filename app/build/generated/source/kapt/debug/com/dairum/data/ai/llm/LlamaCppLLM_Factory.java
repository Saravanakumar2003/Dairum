package com.dairum.data.ai.llm;

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
public final class LlamaCppLLM_Factory implements Factory<LlamaCppLLM> {
  private final Provider<Context> contextProvider;

  public LlamaCppLLM_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LlamaCppLLM get() {
    return newInstance(contextProvider.get());
  }

  public static LlamaCppLLM_Factory create(Provider<Context> contextProvider) {
    return new LlamaCppLLM_Factory(contextProvider);
  }

  public static LlamaCppLLM newInstance(Context context) {
    return new LlamaCppLLM(context);
  }
}
