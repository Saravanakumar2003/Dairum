package com.dairum.di;

import com.dairum.data.local.dao.ChatSessionDao;
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
public final class DatabaseModule_ProvideChatSessionDaoFactory implements Factory<ChatSessionDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideChatSessionDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ChatSessionDao get() {
    return provideChatSessionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideChatSessionDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideChatSessionDaoFactory(databaseProvider);
  }

  public static ChatSessionDao provideChatSessionDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideChatSessionDao(database));
  }
}
