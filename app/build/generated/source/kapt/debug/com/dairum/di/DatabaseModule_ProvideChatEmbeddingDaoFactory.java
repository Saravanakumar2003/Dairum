package com.dairum.di;

import com.dairum.data.local.dao.ChatEmbeddingDao;
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
public final class DatabaseModule_ProvideChatEmbeddingDaoFactory implements Factory<ChatEmbeddingDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideChatEmbeddingDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ChatEmbeddingDao get() {
    return provideChatEmbeddingDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideChatEmbeddingDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideChatEmbeddingDaoFactory(databaseProvider);
  }

  public static ChatEmbeddingDao provideChatEmbeddingDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideChatEmbeddingDao(database));
  }
}
