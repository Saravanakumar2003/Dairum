package com.dairum.di;

import com.dairum.data.local.dao.MemoryDao;
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
public final class DatabaseModule_ProvideMemoryDaoFactory implements Factory<MemoryDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideMemoryDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MemoryDao get() {
    return provideMemoryDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMemoryDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMemoryDaoFactory(databaseProvider);
  }

  public static MemoryDao provideMemoryDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMemoryDao(database));
  }
}
