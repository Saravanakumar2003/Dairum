package com.dairum.di;

import com.dairum.data.local.dao.SmartSummaryDao;
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
public final class DatabaseModule_ProvideSmartSummaryDaoFactory implements Factory<SmartSummaryDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideSmartSummaryDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SmartSummaryDao get() {
    return provideSmartSummaryDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSmartSummaryDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSmartSummaryDaoFactory(databaseProvider);
  }

  public static SmartSummaryDao provideSmartSummaryDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSmartSummaryDao(database));
  }
}
