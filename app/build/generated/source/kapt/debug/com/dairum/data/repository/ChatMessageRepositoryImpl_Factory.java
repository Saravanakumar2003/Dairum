package com.dairum.data.repository;

import com.dairum.data.local.dao.ChatMessageDao;
import com.dairum.data.local.dao.ChatSessionDao;
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
public final class ChatMessageRepositoryImpl_Factory implements Factory<ChatMessageRepositoryImpl> {
  private final Provider<ChatMessageDao> chatMessageDaoProvider;

  private final Provider<ChatSessionDao> chatSessionDaoProvider;

  private final Provider<Moshi> moshiProvider;

  public ChatMessageRepositoryImpl_Factory(Provider<ChatMessageDao> chatMessageDaoProvider,
      Provider<ChatSessionDao> chatSessionDaoProvider, Provider<Moshi> moshiProvider) {
    this.chatMessageDaoProvider = chatMessageDaoProvider;
    this.chatSessionDaoProvider = chatSessionDaoProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public ChatMessageRepositoryImpl get() {
    return newInstance(chatMessageDaoProvider.get(), chatSessionDaoProvider.get(), moshiProvider.get());
  }

  public static ChatMessageRepositoryImpl_Factory create(
      Provider<ChatMessageDao> chatMessageDaoProvider,
      Provider<ChatSessionDao> chatSessionDaoProvider, Provider<Moshi> moshiProvider) {
    return new ChatMessageRepositoryImpl_Factory(chatMessageDaoProvider, chatSessionDaoProvider, moshiProvider);
  }

  public static ChatMessageRepositoryImpl newInstance(ChatMessageDao chatMessageDao,
      ChatSessionDao chatSessionDao, Moshi moshi) {
    return new ChatMessageRepositoryImpl(chatMessageDao, chatSessionDao, moshi);
  }
}
