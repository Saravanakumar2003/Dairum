package com.dairum.presentation.screens.chat;

import androidx.compose.animation.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.ImeAction;
import androidx.compose.ui.text.style.TextOverflow;
import com.dairum.domain.model.*;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\\\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\u001af\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\u001a\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\nH\u0003\u001a8\u0010\u0012\u001a\u00020\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00100\u00142\u0006\u0010\u0015\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\b\u0010\u0016\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0017\u001a\u00020\u0018H\u0003\u001a.\u0010\u0019\u001a\u00020\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\u001b\u001a\u00020\u001cH\u0007\u001a^\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\n2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a0\u0010$\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\n2\b\u0010%\u001a\u0004\u0018\u00010\u00032\u0006\u0010&\u001a\u00020\'2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a:\u0010(\u001a\u00020\u00012\f\u0010)\u001a\b\u0012\u0004\u0012\u00020*0\u00142\u0006\u0010&\u001a\u00020\'2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\u0010\u0010-\u001a\u00020\u00012\u0006\u0010.\u001a\u00020*H\u0003\u001a4\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u00020\n2\f\u00103\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u00104\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001aV\u00105\u001a\u00020\u00012\f\u00106\u001a\b\u0012\u0004\u0012\u0002010\u00142\b\u00107\u001a\u0004\u0018\u00010\u00032\u0012\u00108\u001a\u000e\u0012\u0004\u0012\u000201\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u00109\u001a\u000e\u0012\u0004\u0012\u000201\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\b\u0010:\u001a\u00020\u0001H\u0003\u00a8\u0006;"}, d2 = {"ChatInputBar", "", "input", "", "onInputChange", "Lkotlin/Function1;", "onSend", "Lkotlin/Function0;", "onCancel", "isGenerating", "", "canSend", "isAIReady", "onNavigateToModels", "ChatMessageBubble", "message", "Lcom/dairum/domain/model/ChatMessage;", "isStreaming", "ChatMessagesList", "messages", "", "streamingContent", "processingStatus", "listState", "Landroidx/compose/foundation/lazy/LazyListState;", "ChatScreen", "onNavigateBack", "viewModel", "Lcom/dairum/presentation/screens/chat/ChatViewModel;", "ChatTopBar", "title", "isSaved", "onToggleSave", "onNewConversation", "onShowSessions", "onShowMemoryInsights", "EmptyChatContent", "modelName", "reflectionCount", "", "MemoryInsightsSheet", "reflections", "Lcom/dairum/domain/model/ReflectionMemory;", "onGenerateReflections", "onDismiss", "ReflectionCard", "reflection", "SessionListItem", "session", "Lcom/dairum/domain/model/ChatSession;", "isSelected", "onClick", "onDelete", "SessionsDrawer", "sessions", "currentSessionId", "onSelectSession", "onDeleteSession", "TypingIndicator", "app_debug"})
public final class ChatScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ChatScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToModels, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.chat.ChatViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void ChatTopBar(java.lang.String title, boolean isSaved, kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleSave, kotlin.jvm.functions.Function0<kotlin.Unit> onNewConversation, kotlin.jvm.functions.Function0<kotlin.Unit> onShowSessions, kotlin.jvm.functions.Function0<kotlin.Unit> onShowMemoryInsights) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ChatInputBar(java.lang.String input, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onInputChange, kotlin.jvm.functions.Function0<kotlin.Unit> onSend, kotlin.jvm.functions.Function0<kotlin.Unit> onCancel, boolean isGenerating, boolean canSend, boolean isAIReady, kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToModels) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ChatMessagesList(java.util.List<com.dairum.domain.model.ChatMessage> messages, java.lang.String streamingContent, boolean isGenerating, java.lang.String processingStatus, androidx.compose.foundation.lazy.LazyListState listState) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ChatMessageBubble(com.dairum.domain.model.ChatMessage message, boolean isStreaming) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TypingIndicator() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EmptyChatContent(boolean isAIReady, java.lang.String modelName, int reflectionCount, kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToModels) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SessionsDrawer(java.util.List<com.dairum.domain.model.ChatSession> sessions, java.lang.String currentSessionId, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.ChatSession, kotlin.Unit> onSelectSession, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.ChatSession, kotlin.Unit> onDeleteSession, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SessionListItem(com.dairum.domain.model.ChatSession session, boolean isSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MemoryInsightsSheet(java.util.List<com.dairum.domain.model.ReflectionMemory> reflections, int reflectionCount, kotlin.jvm.functions.Function0<kotlin.Unit> onGenerateReflections, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReflectionCard(com.dairum.domain.model.ReflectionMemory reflection) {
    }
}