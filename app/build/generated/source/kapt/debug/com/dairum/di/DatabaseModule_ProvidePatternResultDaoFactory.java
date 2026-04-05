package com.dairum.di;

import com.dairum.data.local.dao.PatternResultDao;
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
public final class DatabaseModule_ProvidePatternResultDaoFactory implements Factory<PatternResultDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvidePatternResultDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PatternResultDao get() {
    return providePatternResultDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePatternResultDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePatternResultDaoFactory(databaseProvider);
  }

  public static PatternResultDao providePatternResultDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePatternResultDao(database));
  }
}
