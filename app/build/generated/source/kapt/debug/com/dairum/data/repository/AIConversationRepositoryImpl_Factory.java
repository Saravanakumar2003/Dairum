package com.dairum.data.repository;

import com.dairum.data.local.dao.AIConversationDao;
import com.squareup.moshi.Moshi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AIConversationRepositoryImpl_Factory implements Factory<AIConversationRepositoryImpl> {
  private final Provider<AIConversationDao> conversationDaoProvider;

  private final Provider<Moshi> moshiProvider;

  public AIConversationRepositoryImpl_Factory(Provider<AIConversationDao> conversationDaoProvider,
      Provider<Moshi> moshiProvider) {
    this.conversationDaoProvider = conversationDaoProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public AIConversationRepositoryImpl get() {
    return newInstance(conversationDaoProvider.get(), moshiProvider.get());
  }

  public static AIConversationRepositoryImpl_Factory create(
      Provider<AIConversationDao> conversationDaoProvider, Provider<Moshi> moshiProvider) {
    return new AIConversationRepositoryImpl_Factory(conversationDaoProvider, moshiProvider);
  }

  public static AIConversationRepositoryImpl newInstance(AIConversationDao conversationDao,
      Moshi moshi) {
    return new AIConversationRepositoryImpl(conversationDao, moshi);
  }
}
