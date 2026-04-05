package com.dairum.data.repository;

import com.dairum.data.local.dao.ConversationContextDao;
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
public final class ConversationContextRepositoryImpl_Factory implements Factory<ConversationContextRepositoryImpl> {
  private final Provider<ConversationContextDao> conversationContextDaoProvider;

  private final Provider<Moshi> moshiProvider;

  public ConversationContextRepositoryImpl_Factory(
      Provider<ConversationContextDao> conversationContextDaoProvider,
      Provider<Moshi> moshiProvider) {
    this.conversationContextDaoProvider = conversationContextDaoProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public ConversationContextRepositoryImpl get() {
    return newInstance(conversationContextDaoProvider.get(), moshiProvider.get());
  }

  public static ConversationContextRepositoryImpl_Factory create(
      Provider<ConversationContextDao> conversationContextDaoProvider,
      Provider<Moshi> moshiProvider) {
    return new ConversationContextRepositoryImpl_Factory(conversationContextDaoProvider, moshiProvider);
  }

  public static ConversationContextRepositoryImpl newInstance(
      ConversationContextDao conversationContextDao, Moshi moshi) {
    return new ConversationContextRepositoryImpl(conversationContextDao, moshi);
  }
}
