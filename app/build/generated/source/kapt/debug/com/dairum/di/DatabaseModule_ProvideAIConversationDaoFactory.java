package com.dairum.di;

import com.dairum.data.local.dao.AIConversationDao;
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
public final class DatabaseModule_ProvideAIConversationDaoFactory implements Factory<AIConversationDao> {
  private final Provider<DairumDatabase> databaseProvider;

  public DatabaseModule_ProvideAIConversationDaoFactory(Provider<DairumDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AIConversationDao get() {
    return provideAIConversationDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAIConversationDaoFactory create(
      Provider<DairumDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAIConversationDaoFactory(databaseProvider);
  }

  public static AIConversationDao provideAIConversationDao(DairumDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAIConversationDao(database));
  }
}
