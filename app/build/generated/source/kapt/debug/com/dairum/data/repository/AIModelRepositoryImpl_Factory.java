package com.dairum.data.repository;

import com.dairum.data.local.dao.AIModelDao;
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
public final class AIModelRepositoryImpl_Factory implements Factory<AIModelRepositoryImpl> {
  private final Provider<AIModelDao> aiModelDaoProvider;

  public AIModelRepositoryImpl_Factory(Provider<AIModelDao> aiModelDaoProvider) {
    this.aiModelDaoProvider = aiModelDaoProvider;
  }

  @Override
  public AIModelRepositoryImpl get() {
    return newInstance(aiModelDaoProvider.get());
  }

  public static AIModelRepositoryImpl_Factory create(Provider<AIModelDao> aiModelDaoProvider) {
    return new AIModelRepositoryImpl_Factory(aiModelDaoProvider);
  }

  public static AIModelRepositoryImpl newInstance(AIModelDao aiModelDao) {
    return new AIModelRepositoryImpl(aiModelDao);
  }
}
