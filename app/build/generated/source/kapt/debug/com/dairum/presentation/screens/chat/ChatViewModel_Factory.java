package com.dairum.presentation.screens.chat;

import androidx.lifecycle.SavedStateHandle;
import com.dairum.data.ai.conversation.ConversationAIOrchestrator;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.domain.repository.ChatMessageRepository;
import com.dairum.domain.repository.ChatSessionRepository;
import com.dairum.domain.repository.ReflectionMemoryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<ConversationAIOrchestrator> conversationOrchestratorProvider;

  private final Provider<ChatSessionRepository> chatSessionRepositoryProvider;

  private final Provider<ChatMessageRepository> chatMessageRepositoryProvider;

  private final Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider;

  private final Provider<ModelManager> modelManagerProvider;

  private final Provider<LocalLLM> localLLMProvider;

  public ChatViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ConversationAIOrchestrator> conversationOrchestratorProvider,
      Provider<ChatSessionRepository> chatSessionRepositoryProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider,
      Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider,
      Provider<ModelManager> modelManagerProvider, Provider<LocalLLM> localLLMProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.conversationOrchestratorProvider = conversationOrchestratorProvider;
    this.chatSessionRepositoryProvider = chatSessionRepositoryProvider;
    this.chatMessageRepositoryProvider = chatMessageRepositoryProvider;
    this.reflectionMemoryRepositoryProvider = reflectionMemoryRepositoryProvider;
    this.modelManagerProvider = modelManagerProvider;
    this.localLLMProvider = localLLMProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(savedStateHandleProvider.get(), conversationOrchestratorProvider.get(), chatSessionRepositoryProvider.get(), chatMessageRepositoryProvider.get(), reflectionMemoryRepositoryProvider.get(), modelManagerProvider.get(), localLLMProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ConversationAIOrchestrator> conversationOrchestratorProvider,
      Provider<ChatSessionRepository> chatSessionRepositoryProvider,
      Provider<ChatMessageRepository> chatMessageRepositoryProvider,
      Provider<ReflectionMemoryRepository> reflectionMemoryRepositoryProvider,
      Provider<ModelManager> modelManagerProvider, Provider<LocalLLM> localLLMProvider) {
    return new ChatViewModel_Factory(savedStateHandleProvider, conversationOrchestratorProvider, chatSessionRepositoryProvider, chatMessageRepositoryProvider, reflectionMemoryRepositoryProvider, modelManagerProvider, localLLMProvider);
  }

  public static ChatViewModel newInstance(SavedStateHandle savedStateHandle,
      ConversationAIOrchestrator conversationOrchestrator,
      ChatSessionRepository chatSessionRepository, ChatMessageRepository chatMessageRepository,
      ReflectionMemoryRepository reflectionMemoryRepository, ModelManager modelManager,
      LocalLLM localLLM) {
    return new ChatViewModel(savedStateHandle, conversationOrchestrator, chatSessionRepository, chatMessageRepository, reflectionMemoryRepository, modelManager, localLLM);
  }
}
