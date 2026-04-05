package com.dairum.di;

import com.dairum.data.local.dao.JournalEntryDao;
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
public final class DatabaseModule_ProvideJournalEntryDaoFactory implements Factory<JournalEntryDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideJournalEntryDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public JournalEntryDao get() {
    return provideJournalEntryDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideJournalEntryDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideJournalEntryDaoFactory(databaseProvider);
  }

  public static JournalEntryDao provideJournalEntryDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideJournalEntryDao(database));
  }
}
