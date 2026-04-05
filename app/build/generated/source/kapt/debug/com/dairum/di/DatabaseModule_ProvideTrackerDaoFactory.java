package com.dairum.di;

import com.dairum.data.local.dao.TrackerDao;
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
public final class DatabaseModule_ProvideTrackerDaoFactory implements Factory<TrackerDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideTrackerDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TrackerDao get() {
    return provideTrackerDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTrackerDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTrackerDaoFactory(databaseProvider);
  }

  public static TrackerDao provideTrackerDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTrackerDao(database));
  }
}
