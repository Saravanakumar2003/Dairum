package com.dairum.di;

import com.dairum.data.local.dao.WeeklyReflectionDao;
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
public final class DatabaseModule_ProvideWeeklyReflectionDaoFactory implements Factory<WeeklyReflectionDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideWeeklyReflectionDaoFactory(
      Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public WeeklyReflectionDao get() {
    return provideWeeklyReflectionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideWeeklyReflectionDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideWeeklyReflectionDaoFactory(databaseProvider);
  }

  public static WeeklyReflectionDao provideWeeklyReflectionDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideWeeklyReflectionDao(database));
  }
}
