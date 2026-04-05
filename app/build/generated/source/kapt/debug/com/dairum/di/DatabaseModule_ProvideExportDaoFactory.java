package com.dairum.di;

import com.dairum.data.local.dao.ExportDao;
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
public final class DatabaseModule_ProvideExportDaoFactory implements Factory<ExportDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideExportDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ExportDao get() {
    return provideExportDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideExportDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideExportDaoFactory(databaseProvider);
  }

  public static ExportDao provideExportDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideExportDao(database));
  }
}
