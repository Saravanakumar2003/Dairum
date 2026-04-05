package com.dairum.di;

import android.content.Context;
import com.dairum.data.ai.llm.LocalLLM;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AIModule_ProvideLocalLLMFactory implements Factory<LocalLLM> {
  private final Provider<Context> contextProvider;

  public AIModule_ProvideLocalLLMFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LocalLLM get() {
    return provideLocalLLM(contextProvider.get());
  }

  public static AIModule_ProvideLocalLLMFactory create(Provider<Context> contextProvider) {
    return new AIModule_ProvideLocalLLMFactory(contextProvider);
  }

  public static LocalLLM provideLocalLLM(Context context) {
    return Preconditions.checkNotNullFromProvides(AIModule.INSTANCE.provideLocalLLM(context));
  }
}
