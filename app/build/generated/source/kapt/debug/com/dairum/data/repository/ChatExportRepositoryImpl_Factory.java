package com.dairum.data.repository;

import android.content.Context;
import com.dairum.domain.repository.ChatMessageRepository;
import com.dairum.domain.repository.ChatSessionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ChatExportRepositoryImpl_Factory implements Factory<ChatExportRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<ChatSessionRepository> chatSessionRepositoryProvider;

  private final Provider<ChatMessageRepository> chatMessageRepositoryProvider;

  public ChatExportRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<ChatSessionRepository> chatSessionRepositoryProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.chatSessionRepositoryProvider = chatSessionRepositoryProvider;
    this.chatMessageRepositoryProvider = chatMessageRepositoryProvider;
  }

  @Override
  public ChatExportRepositoryImpl get() {
    return newInstance(contextProvider.get(), chatSessionRepositoryProvider.get(), chatMessageRepositoryProvider.get());
  }

  public static ChatExportRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<ChatSessionRepository> chatSessionRepositoryProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider) {
    return new ChatExportRepositoryImpl_Factory(contextProvider, chatSessionRepositoryProvider, chatMessageRepositoryProvider);
  }

  public static ChatExportRepositoryImpl newInstance(Context context,
      ChatSessionRepository chatSessionRepository, ChatMessageRepository chatMessageRepository) {
    return new ChatExportRepositoryImpl(context, chatSessionRepository, chatMessageRepository);
  }
}
