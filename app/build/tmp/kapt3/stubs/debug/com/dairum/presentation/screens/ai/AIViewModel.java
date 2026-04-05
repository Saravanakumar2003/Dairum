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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ac\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001BG\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\u0002\u0010\u0012J\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bJ\u0006\u0010\u001d\u001a\u00020\u001eJ\u0006\u0010\u001f\u001a\u00020\u001eJ\u0006\u0010 \u001a\u00020\u001eJ\u000e\u0010!\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020#J\u000e\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&J\u000e\u0010\'\u001a\u00020\u001e2\u0006\u0010(\u001a\u00020)J\u000e\u0010*\u001a\u00020\u001e2\u0006\u0010+\u001a\u00020,J\u0006\u0010-\u001a\u00020\u001eJ\u000e\u0010.\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020#J\u0006\u0010/\u001a\u00020\u001eJ\u0006\u00100\u001a\u00020\u001eJ\u0006\u00101\u001a\u00020\u001eJ\u0006\u00102\u001a\u00020#J\u000e\u00103\u001a\u00020#2\u0006\u00104\u001a\u000205J\u001e\u00106\u001a\u00020\u001e2\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u00020#2\u0006\u0010:\u001a\u00020;J\u0006\u0010<\u001a\u00020\u001eJ\b\u0010=\u001a\u00020\u001eH\u0002J\b\u0010>\u001a\u00020\u001eH\u0002J\b\u0010?\u001a\u00020\u001eH\u0002J\b\u0010@\u001a\u00020\u001eH\u0002J\u000e\u0010A\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020#J\u000e\u0010B\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020#J\u000e\u0010C\u001a\u00020\u001e2\u0006\u0010D\u001a\u00020EJ\u0006\u0010F\u001a\u00020\u001eJ\u0006\u0010G\u001a\u00020\u001eJ\u000e\u0010H\u001a\u00020\u001e2\u0006\u0010I\u001a\u00020#J\"\u0010J\u001a\b\u0012\u0004\u0012\u00020L0K2\f\u0010M\u001a\b\u0012\u0004\u0012\u00020L0KH\u0082@\u00a2\u0006\u0002\u0010NR\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006O"}, d2 = {"Lcom/dairum/presentation/screens/ai/AIViewModel;", "Landroidx/lifecycle/ViewModel;", "aiOrchestrator", "Lcom/dairum/data/ai/usecase/AIOrchestrator;", "modelManager", "Lcom/dairum/data/ai/model/ModelManager;", "modelRepository", "Lcom/dairum/domain/repository/AIModelRepository;", "contentRepository", "Lcom/dairum/domain/repository/AIContentRepository;", "retrievalService", "Lcom/dairum/data/ai/retrieval/RetrievalService;", "localLLM", "Lcom/dairum/data/ai/llm/LocalLLM;", "embeddingRepository", "Lcom/dairum/domain/repository/EmbeddingRepository;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "(Lcom/dairum/data/ai/usecase/AIOrchestrator;Lcom/dairum/data/ai/model/ModelManager;Lcom/dairum/domain/repository/AIModelRepository;Lcom/dairum/domain/repository/AIContentRepository;Lcom/dairum/data/ai/retrieval/RetrievalService;Lcom/dairum/data/ai/llm/LocalLLM;Lcom/dairum/domain/repository/EmbeddingRepository;Lcom/dairum/domain/repository/JournalRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/ai/AIUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "analyzeMood", "Lkotlinx/coroutines/flow/Flow;", "Lcom/dairum/domain/model/AIResponse;", "clearConversation", "", "clearError", "clearSuccessMessage", "deleteModel", "modelId", "", "deletePattern", "pattern", "Lcom/dairum/domain/model/PatternResult;", "deleteReflection", "reflection", "Lcom/dairum/domain/model/WeeklyReflection;", "deleteSummary", "summary", "Lcom/dairum/domain/model/SmartSummary;", "detectPatterns", "downloadModel", "generateAllEmbeddings", "generateMonthlySummary", "generateWeeklyReflection", "getShareableConversation", "getShareableMessage", "message", "Lcom/dairum/presentation/screens/ai/ChatMessage;", "importModel", "uri", "Landroid/net/Uri;", "name", "modelType", "Lcom/dairum/domain/model/ModelType;", "initializeIfNeeded", "loadContent", "loadEmbeddingStats", "loadModels", "observeSelectedModels", "selectEmbeddingModel", "selectLLMModel", "selectTab", "tab", "Lcom/dairum/presentation/screens/ai/AITab;", "sendMessage", "sendMessageWithStreaming", "updateQuery", "query", "verifyModelFiles", "", "Lcom/dairum/domain/model/AIModel;", "models", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AIViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.usecase.AIOrchestrator aiOrchestrator = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.model.ModelManager modelManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.AIModelRepository modelRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.AIContentRepository contentRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.retrieval.RetrievalService retrievalService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.llm.LocalLLM localLLM = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.EmbeddingRepository embeddingRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.ai.AIUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.ai.AIUiState> uiState = null;
    
    @javax.inject.Inject()
    public AIViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.usecase.AIOrchestrator aiOrchestrator, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.model.ModelManager modelManager, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.AIModelRepository modelRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.AIContentRepository contentRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.retrieval.RetrievalService retrievalService, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.llm.LocalLLM localLLM, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.EmbeddingRepository embeddingRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.ai.AIUiState> getUiState() {
        return null;
    }
    
    /**
     * Load embedding statistics
     */
    private final void loadEmbeddingStats() {
    }
    
    private final void loadModels() {
    }
    
    /**
     * Verify that downloaded models still have their files and update records if files are missing
     */
    private final java.lang.Object verifyModelFiles(java.util.List<com.dairum.domain.model.AIModel> models, kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.AIModel>> $completion) {
        return null;
    }
    
    private final void loadContent() {
    }
    
    private final void observeSelectedModels() {
    }
    
    public final void selectTab(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.AITab tab) {
    }
    
    public final void updateQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    /**
     * Send a message to the AI
     */
    public final void sendMessage() {
    }
    
    /**
     * Send a message with streaming response
     */
    public final void sendMessageWithStreaming() {
    }
    
    /**
     * Generate weekly reflection
     */
    public final void generateWeeklyReflection() {
    }
    
    /**
     * Detect patterns
     */
    public final void detectPatterns() {
    }
    
    /**
     * Delete a reflection
     */
    public final void deleteReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.WeeklyReflection reflection) {
    }
    
    /**
     * Delete a pattern
     */
    public final void deletePattern(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.PatternResult pattern) {
    }
    
    /**
     * Delete a summary
     */
    public final void deleteSummary(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartSummary summary) {
    }
    
    /**
     * Generate monthly summary
     */
    public final void generateMonthlySummary() {
    }
    
    /**
     * Analyze mood
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.dairum.domain.model.AIResponse> analyzeMood() {
        return null;
    }
    
    /**
     * Download a model
     */
    public final void downloadModel(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId) {
    }
    
    /**
     * Select LLM model
     */
    public final void selectLLMModel(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId) {
    }
    
    /**
     * Select embedding model
     */
    public final void selectEmbeddingModel(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId) {
    }
    
    /**
     * Delete a model
     */
    public final void deleteModel(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId) {
    }
    
    /**
     * Generate embeddings for all entries
     */
    public final void generateAllEmbeddings() {
    }
    
    /**
     * Clear success message
     */
    public final void clearSuccessMessage() {
    }
    
    /**
     * Clear conversation
     */
    public final void clearConversation() {
    }
    
    /**
     * Get shareable text for the conversation
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getShareableConversation() {
        return null;
    }
    
    /**
     * Get shareable text for a single message
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getShareableMessage(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.ai.ChatMessage message) {
        return null;
    }
    
    /**
     * Clear error
     */
    public final void clearError() {
    }
    
    /**
     * Import a model from a file URI
     */
    public final void importModel(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ModelType modelType) {
    }
    
    /**
     * Initialize models on first launch
     */
    public final void initializeIfNeeded() {
    }
}