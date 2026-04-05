package com.dairum.presentation.screens.chat;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.dairum.data.ai.conversation.ConversationAIOrchestrator;
import com.dairum.data.ai.conversation.ConversationStreamEvent;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.*;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import timber.log.Timber;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\b\u0007\u0018\u00002\u00020\u0001B?\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J&\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001f2\u0006\u0010!\u001a\u00020\"H\u0002J\u0006\u0010#\u001a\u00020$J\u0006\u0010%\u001a\u00020$J\u0006\u0010&\u001a\u00020$J\u000e\u0010\'\u001a\u00020$H\u0082@\u00a2\u0006\u0002\u0010(J\u000e\u0010)\u001a\u00020$2\u0006\u0010\u001c\u001a\u00020\u001dJ\u001a\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0,0+2\u0006\u0010!\u001a\u00020\"J\u0006\u0010-\u001a\u00020$J\b\u0010.\u001a\u00020$H\u0002J\b\u0010/\u001a\u00020$H\u0002J\b\u00100\u001a\u00020$H\u0002J\b\u00101\u001a\u00020$H\u0002J\u0016\u00102\u001a\u00020$2\u0006\u0010\u001c\u001a\u00020\u001dH\u0082@\u00a2\u0006\u0002\u00103J\b\u00104\u001a\u00020$H\u0002J\b\u00105\u001a\u00020$H\u0014J\u0006\u00106\u001a\u00020$J\u0006\u00107\u001a\u00020$J\u0006\u00108\u001a\u00020$J\u000e\u00109\u001a\u00020$2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010:\u001a\u00020$J\u0006\u0010;\u001a\u00020$J\u0006\u0010<\u001a\u00020$J\u000e\u0010=\u001a\u00020$2\u0006\u0010>\u001a\u00020\u001bJ\u000e\u0010?\u001a\u00020$2\u0006\u0010@\u001a\u00020\u001bR\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00130\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006A"}, d2 = {"Lcom/dairum/presentation/screens/chat/ChatViewModel;", "Landroidx/lifecycle/ViewModel;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "conversationOrchestrator", "Lcom/dairum/data/ai/conversation/ConversationAIOrchestrator;", "chatSessionRepository", "Lcom/dairum/domain/repository/ChatSessionRepository;", "chatMessageRepository", "Lcom/dairum/domain/repository/ChatMessageRepository;", "reflectionMemoryRepository", "Lcom/dairum/domain/repository/ReflectionMemoryRepository;", "modelManager", "Lcom/dairum/data/ai/model/ModelManager;", "localLLM", "Lcom/dairum/data/ai/llm/LocalLLM;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/dairum/data/ai/conversation/ConversationAIOrchestrator;Lcom/dairum/domain/repository/ChatSessionRepository;Lcom/dairum/domain/repository/ChatMessageRepository;Lcom/dairum/domain/repository/ReflectionMemoryRepository;Lcom/dairum/data/ai/model/ModelManager;Lcom/dairum/data/ai/llm/LocalLLM;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/chat/ChatUiState;", "generationJob", "Lkotlinx/coroutines/Job;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "buildExportContent", "", "session", "Lcom/dairum/domain/model/ChatSession;", "messages", "", "Lcom/dairum/domain/model/ChatMessage;", "format", "Lcom/dairum/domain/model/ChatExportFormat;", "cancelGeneration", "", "clearError", "clearSuccessMessage", "createNewSession", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteSession", "exportConversation", "Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Result;", "generateReflections", "generateTitle", "initializeChat", "loadReflections", "loadSavedSessions", "loadSession", "(Lcom/dairum/domain/model/ChatSession;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeModelState", "onCleared", "sendMessage", "sendMessageStreaming", "startNewConversation", "switchToSession", "toggleMemoryInsights", "toggleSave", "toggleSessionsList", "updateInput", "text", "updateTitle", "title", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ChatViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.SavedStateHandle savedStateHandle = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.conversation.ConversationAIOrchestrator conversationOrchestrator = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ChatSessionRepository chatSessionRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ChatMessageRepository chatMessageRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ReflectionMemoryRepository reflectionMemoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.model.ModelManager modelManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.llm.LocalLLM localLLM = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.chat.ChatUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.chat.ChatUiState> uiState = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job generationJob;
    
    @javax.inject.Inject()
    public ChatViewModel(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.conversation.ConversationAIOrchestrator conversationOrchestrator, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ChatSessionRepository chatSessionRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ChatMessageRepository chatMessageRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ReflectionMemoryRepository reflectionMemoryRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.model.ModelManager modelManager, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.llm.LocalLLM localLLM) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.chat.ChatUiState> getUiState() {
        return null;
    }
    
    private final void initializeChat() {
    }
    
    private final void observeModelState() {
    }
    
    private final void loadSavedSessions() {
    }
    
    private final void loadReflections() {
    }
    
    private final java.lang.Object createNewSession(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadSession(com.dairum.domain.model.ChatSession session, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Update the current input text
     */
    public final void updateInput(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    /**
     * Send a message to the AI
     */
    public final void sendMessage() {
    }
    
    /**
     * Send message with streaming response
     */
    public final void sendMessageStreaming() {
    }
    
    /**
     * Cancel ongoing generation
     */
    public final void cancelGeneration() {
    }
    
    /**
     * Toggle save status of current session
     */
    public final void toggleSave() {
    }
    
    /**
     * Update session title
     */
    public final void updateTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String title) {
    }
    
    /**
     * Generate title automatically
     */
    private final void generateTitle() {
    }
    
    /**
     * Start a new conversation
     */
    public final void startNewConversation() {
    }
    
    /**
     * Switch to a different session
     */
    public final void switchToSession(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ChatSession session) {
    }
    
    /**
     * Delete a session
     */
    public final void deleteSession(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ChatSession session) {
    }
    
    /**
     * Toggle sessions list visibility
     */
    public final void toggleSessionsList() {
    }
    
    /**
     * Toggle memory insights visibility
     */
    public final void toggleMemoryInsights() {
    }
    
    /**
     * Force reflection generation
     */
    public final void generateReflections() {
    }
    
    /**
     * Clear error message
     */
    public final void clearError() {
    }
    
    /**
     * Clear success message
     */
    public final void clearSuccessMessage() {
    }
    
    /**
     * Export current conversation
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<kotlin.Result<java.lang.String>> exportConversation(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ChatExportFormat format) {
        return null;
    }
    
    private final java.lang.String buildExportContent(com.dairum.domain.model.ChatSession session, java.util.List<com.dairum.domain.model.ChatMessage> messages, com.dairum.domain.model.ChatExportFormat format) {
        return null;
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}