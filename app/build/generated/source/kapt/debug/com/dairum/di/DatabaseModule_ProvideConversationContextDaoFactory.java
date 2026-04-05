package com.dairum.di;

import com.dairum.data.local.dao.ConversationContextDao;
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
public final class DatabaseModule_ProvideConversationContextDaoFactory implements Factory<ConversationContextDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideConversationContextDaoFactory(
      Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ConversationContextDao get() {
    return provideConversationContextDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideConversationContextDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideConversationContextDaoFactory(databaseProvider);
  }

  public static ConversationContextDao provideConversationContextDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideConversationContextDao(database));
  }
}
