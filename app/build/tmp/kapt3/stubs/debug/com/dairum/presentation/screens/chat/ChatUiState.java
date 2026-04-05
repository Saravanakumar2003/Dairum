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

/**
 * UI State for the Chat Screen
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b/\b\u0086\b\u0018\u00002\u00020\u0001B\u00df\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0006\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u000b\u00a2\u0006\u0002\u0010\u001cJ\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00103\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000f\u00104\u001a\b\u0012\u0004\u0012\u00020\t0\u000bH\u00c6\u0003J\t\u00105\u001a\u00020\u0003H\u00c6\u0003J\t\u00106\u001a\u00020\u0003H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u0010:\u001a\u00020\u0018H\u00c6\u0003J\t\u0010;\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010<\u001a\b\u0012\u0004\u0012\u00020\u001b0\u000bH\u00c6\u0003J\t\u0010=\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010?\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010@\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000f\u0010A\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00c6\u0003J\t\u0010B\u001a\u00020\u0006H\u00c6\u0003J\t\u0010C\u001a\u00020\u0003H\u00c6\u0003J\t\u0010D\u001a\u00020\u0006H\u00c6\u0003J\u00e3\u0001\u0010E\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\b\b\u0002\u0010\r\u001a\u00020\u00062\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00062\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00062\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u000b2\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u00032\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u000bH\u00c6\u0001J\u0013\u0010F\u001a\u00020\u00032\b\u0010G\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010H\u001a\u00020\u0018H\u00d6\u0001J\t\u0010I\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u001d\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\r\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010!R\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u001fR\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u001fR\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u001fR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u001fR\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u001fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u001fR\u0011\u0010%\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b%\u0010\u001fR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010!R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\'R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\'R\u0013\u0010\u0016\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010!R\u0011\u0010\u0019\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001fR\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001fR\u0011\u0010\u000f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010!R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010!\u00a8\u0006J"}, d2 = {"Lcom/dairum/presentation/screens/chat/ChatUiState;", "", "isLoading", "", "isInitializing", "error", "", "successMessage", "currentSession", "Lcom/dairum/domain/model/ChatSession;", "messages", "", "Lcom/dairum/domain/model/ChatMessage;", "currentInput", "isGenerating", "streamingContent", "processingStatus", "savedSessions", "showSessionsList", "isAIReady", "isLLMLoaded", "isEmbeddingLoaded", "selectedLLMName", "reflectionCount", "", "showMemoryInsights", "recentReflections", "Lcom/dairum/domain/model/ReflectionMemory;", "(ZZLjava/lang/String;Ljava/lang/String;Lcom/dairum/domain/model/ChatSession;Ljava/util/List;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/util/List;ZZZZLjava/lang/String;IZLjava/util/List;)V", "canSend", "getCanSend", "()Z", "getCurrentInput", "()Ljava/lang/String;", "getCurrentSession", "()Lcom/dairum/domain/model/ChatSession;", "getError", "isSaved", "getMessages", "()Ljava/util/List;", "getProcessingStatus", "getRecentReflections", "getReflectionCount", "()I", "getSavedSessions", "getSelectedLLMName", "getShowMemoryInsights", "getShowSessionsList", "getStreamingContent", "getSuccessMessage", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class ChatUiState {
    private final boolean isLoading = false;
    private final boolean isInitializing = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String successMessage = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.ChatSession currentSession = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.ChatMessage> messages = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentInput = null;
    private final boolean isGenerating = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String streamingContent = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String processingStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.ChatSession> savedSessions = null;
    private final boolean showSessionsList = false;
    private final boolean isAIReady = false;
    private final boolean isLLMLoaded = false;
    private final boolean isEmbeddingLoaded = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String selectedLLMName = null;
    private final int reflectionCount = 0;
    private final boolean showMemoryInsights = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.ReflectionMemory> recentReflections = null;
    
    public ChatUiState(boolean isLoading, boolean isInitializing, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String successMessage, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.ChatSession currentSession, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ChatMessage> messages, @org.jetbrains.annotations.NotNull()
    java.lang.String currentInput, boolean isGenerating, @org.jetbrains.annotations.NotNull()
    java.lang.String streamingContent, @org.jetbrains.annotations.Nullable()
    java.lang.String processingStatus, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ChatSession> savedSessions, boolean showSessionsList, boolean isAIReady, boolean isLLMLoaded, boolean isEmbeddingLoaded, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedLLMName, int reflectionCount, boolean showMemoryInsights, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ReflectionMemory> recentReflections) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    public final boolean isInitializing() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSuccessMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.ChatSession getCurrentSession() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ChatMessage> getMessages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentInput() {
        return null;
    }
    
    public final boolean isGenerating() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStreamingContent() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getProcessingStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ChatSession> getSavedSessions() {
        return null;
    }
    
    public final boolean getShowSessionsList() {
        return false;
    }
    
    public final boolean isAIReady() {
        return false;
    }
    
    public final boolean isLLMLoaded() {
        return false;
    }
    
    public final boolean isEmbeddingLoaded() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSelectedLLMName() {
        return null;
    }
    
    public final int getReflectionCount() {
        return 0;
    }
    
    public final boolean getShowMemoryInsights() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ReflectionMemory> getRecentReflections() {
        return null;
    }
    
    public final boolean getCanSend() {
        return false;
    }
    
    public final boolean isSaved() {
        return false;
    }
    
    public ChatUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ChatSession> component11() {
        return null;
    }
    
    public final boolean component12() {
        return false;
    }
    
    public final boolean component13() {
        return false;
    }
    
    public final boolean component14() {
        return false;
    }
    
    public final boolean component15() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component16() {
        return null;
    }
    
    public final int component17() {
        return 0;
    }
    
    public final boolean component18() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ReflectionMemory> component19() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.ChatSession component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ChatMessage> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.chat.ChatUiState copy(boolean isLoading, boolean isInitializing, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String successMessage, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.ChatSession currentSession, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ChatMessage> messages, @org.jetbrains.annotations.NotNull()
    java.lang.String currentInput, boolean isGenerating, @org.jetbrains.annotations.NotNull()
    java.lang.String streamingContent, @org.jetbrains.annotations.Nullable()
    java.lang.String processingStatus, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ChatSession> savedSessions, boolean showSessionsList, boolean isAIReady, boolean isLLMLoaded, boolean isEmbeddingLoaded, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedLLMName, int reflectionCount, boolean showMemoryInsights, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ReflectionMemory> recentReflections) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}