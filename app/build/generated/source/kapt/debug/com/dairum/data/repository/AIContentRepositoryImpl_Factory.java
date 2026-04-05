package com.dairum.data.repository;

import com.dairum.data.local.dao.PatternResultDao;
import com.dairum.data.local.dao.SmartSummaryDao;
import com.dairum.data.local.dao.WeeklyReflectionDao;
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
public final class AIContentRepositoryImpl_Factory implements Factory<AIContentRepositoryImpl> {
  private final Provider<WeeklyReflectionDao> reflectionDaoProvider;

  private final Provider<PatternResultDao> patternDaoProvider;

  private final Provider<SmartSummaryDao> summaryDaoProvider;

  private final Provider<Moshi> moshiProvider;

  public AIContentRepositoryImpl_Factory(Provider<WeeklyReflectionDao> reflectionDaoProvider,
      Provider<PatternResultDao> patternDaoProvider, Provider<SmartSummaryDao> summaryDaoProvider,
      Provider<Moshi> moshiProvider) {
    this.reflectionDaoProvider = reflectionDaoProvider;
    this.patternDaoProvider = patternDaoProvider;
    this.summaryDaoProvider = summaryDaoProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public AIContentRepositoryImpl get() {
    return newInstance(reflectionDaoProvider.get(), patternDaoProvider.get(), summaryDaoProvider.get(), moshiProvider.get());
  }

  public static AIContentRepositoryImpl_Factory create(
      Provider<WeeklyReflectionDao> reflectionDaoProvider,
      Provider<PatternResultDao> patternDaoProvider, Provider<SmartSummaryDao> summaryDaoProvider,
      Provider<Moshi> moshiProvider) {
    return new AIContentRepositoryImpl_Factory(reflectionDaoProvider, patternDaoProvider, summaryDaoProvider, moshiProvider);
  }

  public static AIContentRepositoryImpl newInstance(WeeklyReflectionDao reflectionDao,
      PatternResultDao patternDao, SmartSummaryDao summaryDao, Moshi moshi) {
    return new AIContentRepositoryImpl(reflectionDao, patternDao, summaryDao, moshi);
  }
}
