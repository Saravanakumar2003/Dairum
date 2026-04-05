package com.dairum.presentation.screens.ai;

import android.content.Intent;
import android.net.Uri;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.ExperimentalFoundationApi;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.ImeAction;
import androidx.compose.ui.text.style.TextOverflow;
import timber.log.Timber;
import com.dairum.domain.model.*;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\\\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a \u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a \u0010\u0006\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u0012\u0010\u0007\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a \u0010\b\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a \u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a:\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0006\u0010\u0013\u001a\u00020\u0014H\u0003\u001aF\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000f0\u0011H\u0003\u001a\b\u0010\u001a\u001a\u00020\u0001H\u0003\u001a\u0010\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u000fH\u0003\u001aJ\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00142\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0006\u0010$\u001a\u00020\u0014H\u0003\u001a\u0016\u0010%\u001a\u00020\u00012\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a\u008a\u0001\u0010\'\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010)\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010*\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a\u001e\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u0002012\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a2\u00102\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\f\u00103\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0012\u00104\u001a\u000e\u0012\u0004\u0012\u000201\u0012\u0004\u0012\u00020\u00010\u0011H\u0003\u001a\u001e\u00105\u001a\u00020\u00012\u0006\u00106\u001a\u0002072\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001aT\u00108\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\f\u00109\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0012\u0010;\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010<\u001a\u000e\u0012\u0004\u0012\u00020=\u0012\u0004\u0012\u00020\u00010\u0011H\u0003\u001a\u001e\u0010>\u001a\u00020\u00012\u0006\u0010?\u001a\u00020=2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u00a8\u0006@"}, d2 = {"AIPatternsScreen", "", "onNavigateBack", "Lkotlin/Function0;", "viewModel", "Lcom/dairum/presentation/screens/ai/AIViewModel;", "AIReflectionsScreen", "AIScreen", "AISettingsScreen", "ChatBubble", "message", "Lcom/dairum/presentation/screens/ai/ChatMessage;", "onLongPress", "ChatInput", "query", "", "onQueryChange", "Lkotlin/Function1;", "onSend", "isEnabled", "", "ChatTab", "uiState", "Lcom/dairum/presentation/screens/ai/AIUiState;", "onSendMessage", "onCopyMessage", "EmptyConversationPrompt", "ExamplePrompt", "text", "ModelCard", "model", "Lcom/dairum/domain/model/AIModel;", "isSelected", "onDownload", "onSelect", "onDelete", "isLoading", "ModelHelpDialog", "onDismiss", "ModelsTab", "onDownloadModel", "onSelectLLM", "onSelectEmbedding", "onDeleteModel", "onGenerateEmbeddings", "onImportLLM", "onImportEmbedding", "PatternCard", "pattern", "Lcom/dairum/domain/model/PatternResult;", "PatternsTab", "onDetectPatterns", "onDeletePattern", "ReflectionCard", "reflection", "Lcom/dairum/domain/model/WeeklyReflection;", "ReflectionsTab", "onGenerateReflection", "onGenerateSummary", "onDeleteReflection", "onDeleteSummary", "Lcom/dairum/domain/model/SmartSummary;", "SummaryCard", "summary", "app_debug"})
public final class AIScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AIScreen(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.AIViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void ChatTab(com.dairum.presentation.screens.ai.AIUiState uiState, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQueryChange, kotlin.jvm.functions.Function0<kotlin.Unit> onSendMessage, kotlin.jvm.functions.Function1<? super com.dairum.presentation.screens.ai.ChatMessage, java.lang.String> onCopyMessage) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EmptyConversationPrompt() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ExamplePrompt(java.lang.String text) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void ChatBubble(com.dairum.presentation.screens.ai.ChatMessage message, kotlin.jvm.functions.Function0<kotlin.Unit> onLongPress) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ChatInput(java.lang.String query, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQueryChange, kotlin.jvm.functions.Function0<kotlin.Unit> onSend, boolean isEnabled) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReflectionsTab(com.dairum.presentation.screens.ai.AIUiState uiState, kotlin.jvm.functions.Function0<kotlin.Unit> onGenerateReflection, kotlin.jvm.functions.Function0<kotlin.Unit> onGenerateSummary, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.WeeklyReflection, kotlin.Unit> onDeleteReflection, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.SmartSummary, kotlin.Unit> onDeleteSummary) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReflectionCard(com.dairum.domain.model.WeeklyReflection reflection, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SummaryCard(com.dairum.domain.model.SmartSummary summary, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PatternsTab(com.dairum.presentation.screens.ai.AIUiState uiState, kotlin.jvm.functions.Function0<kotlin.Unit> onDetectPatterns, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.PatternResult, kotlin.Unit> onDeletePattern) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PatternCard(com.dairum.domain.model.PatternResult pattern, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModelsTab(com.dairum.presentation.screens.ai.AIUiState uiState, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDownloadModel, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelectLLM, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelectEmbedding, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDeleteModel, kotlin.jvm.functions.Function0<kotlin.Unit> onGenerateEmbeddings, kotlin.jvm.functions.Function0<kotlin.Unit> onImportLLM, kotlin.jvm.functions.Function0<kotlin.Unit> onImportEmbedding) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModelHelpDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModelCard(com.dairum.domain.model.AIModel model, boolean isSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onDownload, kotlin.jvm.functions.Function0<kotlin.Unit> onSelect, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, boolean isLoading) {
    }
    
    /**
     * Standalone Reflections Screen
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AIReflectionsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.AIViewModel viewModel) {
    }
    
    /**
     * Standalone Patterns Screen
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AIPatternsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.AIViewModel viewModel) {
    }
    
    /**
     * Standalone AI Settings Screen (Models)
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AISettingsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.AIViewModel viewModel) {
    }
}