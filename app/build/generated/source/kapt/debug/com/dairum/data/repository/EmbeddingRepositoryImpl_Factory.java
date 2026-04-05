package com.dairum.data.repository;

import com.dairum.data.local.dao.JournalEmbeddingDao;
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
public final class EmbeddingRepositoryImpl_Factory implements Factory<EmbeddingRepositoryImpl> {
  private final Provider<JournalEmbeddingDao> embeddingDaoProvider;

  public EmbeddingRepositoryImpl_Factory(Provider<JournalEmbeddingDao> embeddingDaoProvider) {
    this.embeddingDaoProvider = embeddingDaoProvider;
  }

  @Override
  public EmbeddingRepositoryImpl get() {
    return newInstance(embeddingDaoProvider.get());
  }

  public static EmbeddingRepositoryImpl_Factory create(
      Provider<JournalEmbeddingDao> embeddingDaoProvider) {
    return new EmbeddingRepositoryImpl_Factory(embeddingDaoProvider);
  }

  public static EmbeddingRepositoryImpl newInstance(JournalEmbeddingDao embeddingDao) {
    return new EmbeddingRepositoryImpl(embeddingDao);
  }
}
