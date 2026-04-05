package com.dairum.data.repository;

import com.dairum.data.local.dao.ReflectionMemoryDao;
import com.squareup.moshi.Moshi;
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
public final class ReflectionMemoryRepositoryImpl_Factory implements Factory<ReflectionMemoryRepositoryImpl> {
  private final Provider<ReflectionMemoryDao> reflectionMemoryDaoProvider;

  private final Provider<Moshi> moshiProvider;

  public ReflectionMemoryRepositoryImpl_Factory(
      Provider<ReflectionMemoryDao> reflectionMemoryDaoProvider, Provider<Moshi> moshiProvider) {
    this.reflectionMemoryDaoProvider = reflectionMemoryDaoProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public ReflectionMemoryRepositoryImpl get() {
    return newInstance(reflectionMemoryDaoProvider.get(), moshiProvider.get());
  }

  public static ReflectionMemoryRepositoryImpl_Factory create(
      Provider<ReflectionMemoryDao> reflectionMemoryDaoProvider, Provider<Moshi> moshiProvider) {
    return new ReflectionMemoryRepositoryImpl_Factory(reflectionMemoryDaoProvider, moshiProvider);
  }

  public static ReflectionMemoryRepositoryImpl newInstance(ReflectionMemoryDao reflectionMemoryDao,
      Moshi moshi) {
    return new ReflectionMemoryRepositoryImpl(reflectionMemoryDao, moshi);
  }
}
