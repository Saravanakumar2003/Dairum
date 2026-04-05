package com.dairum.data.repository;

import com.dairum.data.local.dao.ChatMessageDao;
import com.dairum.data.local.dao.ChatSessionDao;
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
public final class ChatSessionRepositoryImpl_Factory implements Factory<ChatSessionRepositoryImpl> {
  private final Provider<ChatSessionDao> chatSessionDaoProvider;

  private final Provider<ChatMessageDao> chatMessageDaoProvider;

  public ChatSessionRepositoryImpl_Factory(Provider<ChatSessionDao> chatSessionDaoProvider,
      Provider<ChatMessageDao> chatMessageDaoProvider) {
    this.chatSessionDaoProvider = chatSessionDaoProvider;
    this.chatMessageDaoProvider = chatMessageDaoProvider;
  }

  @Override
  public ChatSessionRepositoryImpl get() {
    return newInstance(chatSessionDaoProvider.get(), chatMessageDaoProvider.get());
  }

  public static ChatSessionRepositoryImpl_Factory create(
      Provider<ChatSessionDao> chatSessionDaoProvider,
      Provider<ChatMessageDao> chatMessageDaoProvider) {
    return new ChatSessionRepositoryImpl_Factory(chatSessionDaoProvider, chatMessageDaoProvider);
  }

  public static ChatSessionRepositoryImpl newInstance(ChatSessionDao chatSessionDao,
      ChatMessageDao chatMessageDao) {
    return new ChatSessionRepositoryImpl(chatSessionDao, chatMessageDao);
  }
}
