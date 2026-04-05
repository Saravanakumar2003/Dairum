package com.dairum.data.ai.llm;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class FallbackLLM_Factory implements Factory<FallbackLLM> {
  @Override
  public FallbackLLM get() {
    return newInstance();
  }

  public static FallbackLLM_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FallbackLLM newInstance() {
    return new FallbackLLM();
  }

  private static final class InstanceHolder {
    private static final FallbackLLM_Factory INSTANCE = new FallbackLLM_Factory();
  }
}
