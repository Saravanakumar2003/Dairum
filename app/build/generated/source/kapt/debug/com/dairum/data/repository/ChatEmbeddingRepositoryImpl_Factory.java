package com.dairum.data.repository;

import com.dairum.data.local.dao.ChatEmbeddingDao;
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
public final class ChatEmbeddingRepositoryImpl_Factory implements Factory<ChatEmbeddingRepositoryImpl> {
  private final Provider<ChatEmbeddingDao> chatEmbeddingDaoProvider;

  public ChatEmbeddingRepositoryImpl_Factory(Provider<ChatEmbeddingDao> chatEmbeddingDaoProvider) {
    this.chatEmbeddingDaoProvider = chatEmbeddingDaoProvider;
  }

  @Override
  public ChatEmbeddingRepositoryImpl get() {
    return newInstance(chatEmbeddingDaoProvider.get());
  }

  public static ChatEmbeddingRepositoryImpl_Factory create(
      Provider<ChatEmbeddingDao> chatEmbeddingDaoProvider) {
    return new ChatEmbeddingRepositoryImpl_Factory(chatEmbeddingDaoProvider);
  }

  public static ChatEmbeddingRepositoryImpl newInstance(ChatEmbeddingDao chatEmbeddingDao) {
    return new ChatEmbeddingRepositoryImpl(chatEmbeddingDao);
  }
}
