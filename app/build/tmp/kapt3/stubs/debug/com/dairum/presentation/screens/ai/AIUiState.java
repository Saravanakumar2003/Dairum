package com.dairum.presentation.screens.ai;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.data.ai.model.StorageUsage;
import com.dairum.data.ai.retrieval.RetrievalService;
import com.dairum.data.ai.usecase.AIOrchestrator;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.AIContentRepository;
import com.dairum.domain.repository.AIModelRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import timber.log.Timber;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;
import javax.inject.Inject;

/**
 * UI State for the AI Screen
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b3\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0091\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\b\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\b\u0012\u000e\b\u0002\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\b\u0012\u000e\b\u0002\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\b\u0012\n\b\u0002\u0010 \u001a\u0004\u0018\u00010!\u0012\b\b\u0002\u0010\"\u001a\u00020#\u00a2\u0006\u0002\u0010$J\t\u0010=\u001a\u00020\u0003H\u00c6\u0003J\t\u0010>\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010?\u001a\u0004\u0018\u00010\u0011H\u00c6\u0003J\t\u0010@\u001a\u00020\u0013H\u00c6\u0003J\t\u0010A\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00160\bH\u00c6\u0003J\t\u0010C\u001a\u00020\u0005H\u00c6\u0003J\t\u0010D\u001a\u00020\u0003H\u00c6\u0003J\t\u0010E\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010F\u001a\b\u0012\u0004\u0012\u00020\u001b0\bH\u00c6\u0003J\u000f\u0010G\u001a\b\u0012\u0004\u0012\u00020\u001d0\bH\u00c6\u0003J\u000b\u0010H\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u001f0\bH\u00c6\u0003J\u000b\u0010J\u001a\u0004\u0018\u00010!H\u00c6\u0003J\t\u0010K\u001a\u00020#H\u00c6\u0003J\u000b\u0010L\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000f\u0010M\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00c6\u0003J\u000f\u0010N\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00c6\u0003J\u000b\u0010O\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010P\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010Q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010R\u001a\u00020\u0003H\u00c6\u0003J\u0095\u0002\u0010S\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\b2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00052\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\b2\b\b\u0002\u0010\u0017\u001a\u00020\u00052\b\b\u0002\u0010\u0018\u001a\u00020\u00032\b\b\u0002\u0010\u0019\u001a\u00020\u00052\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\b2\u000e\b\u0002\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\b2\u000e\b\u0002\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\b2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010!2\b\b\u0002\u0010\"\u001a\u00020#H\u00c6\u0001J\u0013\u0010T\u001a\u00020\u00032\b\u0010U\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010V\u001a\u00020WH\u00d6\u0001J\t\u0010X\u001a\u00020\u0005H\u00d6\u0001R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\b\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0011\u0010\u0017\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0011\u0010\u0014\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010(R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010&R\u0013\u0010 \u001a\u0004\u0018\u00010!\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010(R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u00100R\u0011\u0010\u0018\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u00100R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u00100R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u00100R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u00100R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010&R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010&R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010(R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010(R\u0011\u0010\"\u001a\u00020#\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u00106R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u0011\u0010\u0019\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010(R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010(R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010&R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010&\u00a8\u0006Y"}, d2 = {"Lcom/dairum/presentation/screens/ai/AIUiState;", "", "isLoading", "", "error", "", "successMessage", "llmModels", "", "Lcom/dairum/domain/model/AIModel;", "embeddingModels", "selectedLLMId", "selectedEmbeddingId", "isLLMLoaded", "isEmbeddingLoaded", "isNativeLLMAvailable", "storageUsage", "Lcom/dairum/data/ai/model/StorageUsage;", "embeddingStats", "Lcom/dairum/presentation/screens/ai/EmbeddingStats;", "currentSessionId", "conversationMessages", "Lcom/dairum/presentation/screens/ai/ChatMessage;", "currentQuery", "isGenerating", "streamingResponse", "weeklyReflections", "Lcom/dairum/domain/model/WeeklyReflection;", "patterns", "Lcom/dairum/domain/model/PatternResult;", "summaries", "Lcom/dairum/domain/model/SmartSummary;", "embeddingProgress", "Lcom/dairum/presentation/screens/ai/EmbeddingProgress;", "selectedTab", "Lcom/dairum/presentation/screens/ai/AITab;", "(ZLjava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;ZZZLcom/dairum/data/ai/model/StorageUsage;Lcom/dairum/presentation/screens/ai/EmbeddingStats;Ljava/lang/String;Ljava/util/List;Ljava/lang/String;ZLjava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lcom/dairum/presentation/screens/ai/EmbeddingProgress;Lcom/dairum/presentation/screens/ai/AITab;)V", "getConversationMessages", "()Ljava/util/List;", "getCurrentQuery", "()Ljava/lang/String;", "getCurrentSessionId", "getEmbeddingModels", "getEmbeddingProgress", "()Lcom/dairum/presentation/screens/ai/EmbeddingProgress;", "getEmbeddingStats", "()Lcom/dairum/presentation/screens/ai/EmbeddingStats;", "getError", "()Z", "getLlmModels", "getPatterns", "getSelectedEmbeddingId", "getSelectedLLMId", "getSelectedTab", "()Lcom/dairum/presentation/screens/ai/AITab;", "getStorageUsage", "()Lcom/dairum/data/ai/model/StorageUsage;", "getStreamingResponse", "getSuccessMessage", "getSummaries", "getWeeklyReflections", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class AIUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String successMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.AIModel> llmModels = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.AIModel> embeddingModels = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String selectedLLMId = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String selectedEmbeddingId = null;
    private final boolean isLLMLoaded = false;
    private final boolean isEmbeddingLoaded = false;
    private final boolean isNativeLLMAvailable = false;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.data.ai.model.StorageUsage storageUsage = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.presentation.screens.ai.EmbeddingStats embeddingStats = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentSessionId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.presentation.screens.ai.ChatMessage> conversationMessages = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentQuery = null;
    private final boolean isGenerating = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String streamingResponse = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.WeeklyReflection> weeklyReflections = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.PatternResult> patterns = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.SmartSummary> summaries = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.presentation.screens.ai.EmbeddingProgress embeddingProgress = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.presentation.screens.ai.AITab selectedTab = null;
    
    public AIUiState(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String successMessage, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.AIModel> llmModels, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.AIModel> embeddingModels, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedLLMId, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedEmbeddingId, boolean isLLMLoaded, boolean isEmbeddingLoaded, boolean isNativeLLMAvailable, @org.jetbrains.annotations.Nullable()
    com.dairum.data.ai.model.StorageUsage storageUsage, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.EmbeddingStats embeddingStats, @org.jetbrains.annotations.NotNull()
    java.lang.String currentSessionId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.presentation.screens.ai.ChatMessage> conversationMessages, @org.jetbrains.annotations.NotNull()
    java.lang.String currentQuery, boolean isGenerating, @org.jetbrains.annotations.NotNull()
    java.lang.String streamingResponse, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.WeeklyReflection> weeklyReflections, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.PatternResult> patterns, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.SmartSummary> summaries, @org.jetbrains.annotations.Nullable()
    com.dairum.presentation.screens.ai.EmbeddingProgress embeddingProgress, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.AITab selectedTab) {
        super();
    }
    
    public final boolean isLoading() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.AIModel> getLlmModels() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.AIModel> getEmbeddingModels() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSelectedLLMId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSelectedEmbeddingId() {
        return null;
    }
    
    public final boolean isLLMLoaded() {
        return false;
    }
    
    public final boolean isEmbeddingLoaded() {
        return false;
    }
    
    public final boolean isNativeLLMAvailable() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.data.ai.model.StorageUsage getStorageUsage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.ai.EmbeddingStats getEmbeddingStats() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentSessionId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.presentation.screens.ai.ChatMessage> getConversationMessages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentQuery() {
        return null;
    }
    
    public final boolean isGenerating() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStreamingResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.WeeklyReflection> getWeeklyReflections() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.PatternResult> getPatterns() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.SmartSummary> getSummaries() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.presentation.screens.ai.EmbeddingProgress getEmbeddingProgress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.ai.AITab getSelectedTab() {
        return null;
    }
    
    public AIUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean component10() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.data.ai.model.StorageUsage component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.ai.EmbeddingStats component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.presentation.screens.ai.ChatMessage> component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    public final boolean component16() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.WeeklyReflection> component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.PatternResult> component19() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.SmartSummary> component20() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.presentation.screens.ai.EmbeddingProgress component21() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.ai.AITab component22() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.AIModel> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.AIModel> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.ai.AIUiState copy(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String successMessage, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.AIModel> llmModels, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.AIModel> embeddingModels, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedLLMId, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedEmbeddingId, boolean isLLMLoaded, boolean isEmbeddingLoaded, boolean isNativeLLMAvailable, @org.jetbrains.annotations.Nullable()
    com.dairum.data.ai.model.StorageUsage storageUsage, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.EmbeddingStats embeddingStats, @org.jetbrains.annotations.NotNull()
    java.lang.String currentSessionId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.presentation.screens.ai.ChatMessage> conversationMessages, @org.jetbrains.annotations.NotNull()
    java.lang.String currentQuery, boolean isGenerating, @org.jetbrains.annotations.NotNull()
    java.lang.String streamingResponse, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.WeeklyReflection> weeklyReflections, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.PatternResult> patterns, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.SmartSummary> summaries, @org.jetbrains.annotations.Nullable()
    com.dairum.presentation.screens.ai.EmbeddingProgress embeddingProgress, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.AITab selectedTab) {
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