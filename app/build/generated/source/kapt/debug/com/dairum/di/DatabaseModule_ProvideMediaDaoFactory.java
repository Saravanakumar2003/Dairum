package com.dairum.di;

import com.dairum.data.local.dao.MediaDao;
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
public final class DatabaseModule_ProvideMediaDaoFactory implements Factory<MediaDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideMediaDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MediaDao get() {
    return provideMediaDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMediaDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMediaDaoFactory(databaseProvider);
  }

  public static MediaDao provideMediaDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMediaDao(database));
  }
}
