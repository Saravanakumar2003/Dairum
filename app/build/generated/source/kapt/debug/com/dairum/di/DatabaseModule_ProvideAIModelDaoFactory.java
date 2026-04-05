package com.dairum.di;

import com.dairum.data.local.dao.AIModelDao;
import com.dairum.data.local.database.DairumDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideAIModelDaoFactory implements Factory<AIModelDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideAIModelDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AIModelDao get() {
    return provideAIModelDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAIModelDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAIModelDaoFactory(databaseProvider);
  }

  public static AIModelDao provideAIModelDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAIModelDao(database));
  }
}
