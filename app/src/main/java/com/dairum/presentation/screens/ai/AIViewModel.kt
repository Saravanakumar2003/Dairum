package com.dairum.presentation.screens.ai

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.ai.model.ModelManager
import com.dairum.data.ai.model.StorageUsage
import com.dairum.data.ai.retrieval.RetrievalService
import com.dairum.data.ai.usecase.AIOrchestrator
import com.dairum.domain.model.*
import com.dairum.domain.repository.AIContentRepository
import com.dairum.domain.repository.AIModelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.util.UUID
import javax.inject.Inject

/**
 * UI State for the AI Screen
 */
data class AIUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    
    // Model state
    val llmModels: List<AIModel> = emptyList(),
    val embeddingModels: List<AIModel> = emptyList(),
    val selectedLLMId: String? = null,
    val selectedEmbeddingId: String? = null,
    val isLLMLoaded: Boolean = false,
    val isEmbeddingLoaded: Boolean = false,
    val isNativeLLMAvailable: Boolean = true, // False when using FallbackLLM
    val storageUsage: StorageUsage? = null,
    
    // Embedding stats
    val embeddingStats: EmbeddingStats = EmbeddingStats(),
    
    // Conversation state
    val currentSessionId: String = UUID.randomUUID().toString(),
    val conversationMessages: List<ChatMessage> = emptyList(),
    val currentQuery: String = "",
    val isGenerating: Boolean = false,
    val streamingResponse: String = "",
    
    // Reflections and summaries
    val weeklyReflections: List<WeeklyReflection> = emptyList(),
    val patterns: List<PatternResult> = emptyList(),
    val summaries: List<SmartSummary> = emptyList(),
    
    // Embedding progress
    val embeddingProgress: EmbeddingProgress? = null,
    
    // Selected tab
    val selectedTab: AITab = AITab.CHAT
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: String, // "user" or "assistant"
    val content: String,
    val sources: List<Long> = emptyList(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isStreaming: Boolean = false
)

data class EmbeddingProgress(
    val current: Int,
    val total: Int,
    val isComplete: Boolean = false,
    val isGenerating: Boolean = false
)

data class EmbeddingStats(
    val embeddingCount: Int = 0,
    val totalEntries: Int = 0,
    val lastGenerated: LocalDateTime? = null
) {
    val allGenerated: Boolean get() = embeddingCount >= totalEntries && totalEntries > 0
    val pendingCount: Int get() = (totalEntries - embeddingCount).coerceAtLeast(0)
}

enum class AITab {
    CHAT,
    REFLECTIONS,
    PATTERNS,
    MODELS
}

@HiltViewModel
class AIViewModel @Inject constructor(
    private val aiOrchestrator: AIOrchestrator,
    private val modelManager: ModelManager,
    private val modelRepository: AIModelRepository,
    private val contentRepository: AIContentRepository,
    private val retrievalService: RetrievalService,
    private val localLLM: com.dairum.data.ai.llm.LocalLLM,
    private val embeddingRepository: com.dairum.domain.repository.EmbeddingRepository,
    private val journalRepository: com.dairum.domain.repository.JournalRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AIUiState())
    val uiState: StateFlow<AIUiState> = _uiState.asStateFlow()
    
    init {
        // Check if native LLM is available
        _uiState.update { it.copy(isNativeLLMAvailable = localLLM.isNativeImplementation()) }
        
        loadModels()
        loadContent()
        loadEmbeddingStats()
        observeSelectedModels()
    }
    
    /**
     * Load embedding statistics
     */
    private fun loadEmbeddingStats() {
        viewModelScope.launch {
            try {
                Timber.d("Loading embedding stats...")
                val embeddingCount = embeddingRepository.getEmbeddingCount()
                val totalEntries = journalRepository.getEntryCount()
                
                Timber.d("Embedding stats loaded: embeddingCount=$embeddingCount, totalEntries=$totalEntries")
                
                _uiState.update { state ->
                    state.copy(
                        embeddingStats = EmbeddingStats(
                            embeddingCount = embeddingCount,
                            totalEntries = totalEntries,
                            lastGenerated = if (embeddingCount > 0) LocalDateTime.now() else null
                        )
                    )
                }
                
                Timber.d("UI state updated with embedding stats: ${_uiState.value.embeddingStats}")
                
                // Auto-generate embeddings if we have entries but no embeddings
                if (embeddingCount == 0 && totalEntries > 0) {
                    Timber.d("Auto-generating missing embeddings...")
                    generateAllEmbeddings()
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading embedding stats")
                _uiState.update { it.copy(error = "Failed to load embedding stats: ${e.message}") }
            }
        }
    }
    
    private fun loadModels() {
        viewModelScope.launch {
            // Load LLM models and verify downloaded files exist
            modelRepository.getModelsByType(ModelType.LLM).collect { models ->
                val verifiedModels = verifyModelFiles(models)
                _uiState.update { it.copy(llmModels = verifiedModels) }
            }
        }
        
        viewModelScope.launch {
            // Load embedding models and verify downloaded files exist
            modelRepository.getModelsByType(ModelType.EMBEDDING).collect { models ->
                val verifiedModels = verifyModelFiles(models)
                _uiState.update { it.copy(embeddingModels = verifiedModels) }
            }
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(storageUsage = modelManager.getStorageUsage()) }
        }
    }
    
    /**
     * Verify that downloaded models still have their files and update records if files are missing
     */
    private suspend fun verifyModelFiles(models: List<AIModel>): List<AIModel> {
        return models.map { model ->
            if (model.isDownloaded && model.filePath.isNotBlank()) {
                val file = java.io.File(model.filePath)
                if (!file.exists()) {
                    // File is missing, update the model record
                    Timber.w("Model file missing: ${model.name}, path: ${model.filePath}")
                    modelRepository.updateDownloadStatus(model.id, false, "")
                    model.copy(isDownloaded = false, filePath = "")
                } else {
                    model
                }
            } else {
                model
            }
        }
    }
    
    private fun loadContent() {
        viewModelScope.launch {
            contentRepository.getRecentReflections(10).collect { reflections ->
                _uiState.update { it.copy(weeklyReflections = reflections) }
            }
        }
        
        viewModelScope.launch {
            contentRepository.getRecentPatterns(10).collect { patterns ->
                _uiState.update { it.copy(patterns = patterns) }
            }
        }
        
        viewModelScope.launch {
            contentRepository.getRecentSummaries(10).collect { summaries ->
                _uiState.update { it.copy(summaries = summaries) }
            }
        }
    }
    
    private fun observeSelectedModels() {
        viewModelScope.launch {
            modelManager.getSelectedLLMModelId().collect { modelId ->
                _uiState.update { it.copy(selectedLLMId = modelId) }
            }
        }
        
        viewModelScope.launch {
            modelManager.getSelectedEmbeddingModelId().collect { modelId ->
                _uiState.update { it.copy(selectedEmbeddingId = modelId) }
            }
        }
    }
    
    fun selectTab(tab: AITab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
    
    fun updateQuery(query: String) {
        _uiState.update { it.copy(currentQuery = query) }
    }
    
    /**
     * Send a message to the AI
     */
    fun sendMessage() {
        val query = _uiState.value.currentQuery.trim()
        if (query.isBlank()) return
        
        viewModelScope.launch {
            // Add user message
            val userMessage = ChatMessage(
                role = "user",
                content = query
            )
            _uiState.update { state ->
                state.copy(
                    conversationMessages = state.conversationMessages + userMessage,
                    currentQuery = "",
                    isGenerating = true,
                    error = null
                )
            }
            
            // Generate response
            val response = aiOrchestrator.askJournal(query)
            
            if (response.isSuccess) {
                val assistantMessage = ChatMessage(
                    role = "assistant",
                    content = response.content,
                    sources = response.sources.map { it.id }
                )
                _uiState.update { state ->
                    state.copy(
                        conversationMessages = state.conversationMessages + assistantMessage,
                        isGenerating = false
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        isGenerating = false,
                        error = response.error ?: "Failed to generate response"
                    )
                }
            }
        }
    }
    
    /**
     * Send a message with streaming response
     */
    fun sendMessageWithStreaming() {
        val query = _uiState.value.currentQuery.trim()
        if (query.isBlank()) return
        
        viewModelScope.launch {
            // Add user message
            val userMessage = ChatMessage(
                role = "user",
                content = query
            )
            _uiState.update { state ->
                state.copy(
                    conversationMessages = state.conversationMessages + userMessage,
                    currentQuery = "",
                    isGenerating = true,
                    streamingResponse = "",
                    error = null
                )
            }
            
            // Stream response
            aiOrchestrator.askJournalStream(query).collect { (token, finalResponse) ->
                if (finalResponse != null) {
                    // Generation complete
                    val assistantMessage = ChatMessage(
                        role = "assistant",
                        content = finalResponse.content,
                        sources = finalResponse.sources.map { it.id }
                    )
                    _uiState.update { state ->
                        state.copy(
                            conversationMessages = state.conversationMessages + assistantMessage,
                            isGenerating = false,
                            streamingResponse = ""
                        )
                    }
                } else {
                    // Streaming token
                    _uiState.update { state ->
                        state.copy(streamingResponse = state.streamingResponse + token)
                    }
                }
            }
        }
    }
    
    /**
     * Generate weekly reflection
     */
    fun generateWeeklyReflection() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val weekStart = LocalDateTime.now()
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                
                val result = aiOrchestrator.generateWeeklyReflection(weekStart)
                
                if (result.isSuccess) {
                    _uiState.update { it.copy(isLoading = false) }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Generation failed"
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error generating reflection")
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    /**
     * Detect patterns
     */
    fun detectPatterns() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = aiOrchestrator.detectPatterns(
                    startDate = LocalDateTime.now().minusMonths(1),
                    endDate = LocalDateTime.now()
                )
                
                if (result.isSuccess) {
                    _uiState.update { it.copy(isLoading = false) }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Pattern detection failed"
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error detecting patterns")
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    /**
     * Delete a reflection
     */
    fun deleteReflection(reflection: WeeklyReflection) {
        viewModelScope.launch {
            try {
                contentRepository.deleteReflection(reflection.id)
            } catch (e: Exception) {
                Timber.e(e, "Error deleting reflection")
            }
        }
    }
    
    /**
     * Delete a pattern
     */
    fun deletePattern(pattern: PatternResult) {
        viewModelScope.launch {
            try {
                contentRepository.deletePattern(pattern.id)
            } catch (e: Exception) {
                Timber.e(e, "Error deleting pattern")
            }
        }
    }
    
    /**
     * Delete a summary
     */
    fun deleteSummary(summary: SmartSummary) {
        viewModelScope.launch {
            try {
                contentRepository.deleteSummary(summary.id)
            } catch (e: Exception) {
                Timber.e(e, "Error deleting summary")
            }
        }
    }
    
    /**
     * Generate monthly summary
     */
    fun generateMonthlySummary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val now = LocalDateTime.now()
            val monthStart = now.withDayOfMonth(1)
            
            val result = aiOrchestrator.generateSummary(
                type = SummaryType.MONTHLY,
                startDate = monthStart,
                endDate = now
            )
            
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
            } else {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }
    
    /**
     * Analyze mood
     */
    fun analyzeMood(): Flow<AIResponse> = flow {
        val response = aiOrchestrator.analyzeMood()
        emit(response)
    }
    
    /**
     * Download a model
     */
    fun downloadModel(modelId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = modelManager.downloadModel(modelId) { downloaded, total ->
                Timber.d("Download progress: $downloaded / $total")
            }
            
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        storageUsage = modelManager.getStorageUsage()
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }
    
    /**
     * Select LLM model
     */
    fun selectLLMModel(modelId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            modelManager.selectLLMModel(modelId)
            _uiState.update { it.copy(isLoading = false, isLLMLoaded = true) }
        }
    }
    
    /**
     * Select embedding model
     */
    fun selectEmbeddingModel(modelId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Verify model file exists before selecting
                val model = modelRepository.getModelById(modelId)
                if (model != null && model.isDownloaded) {
                    val file = java.io.File(model.filePath)
                    if (!file.exists()) {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Model file not found. Please re-download or re-import the model."
                        )}
                        return@launch
                    }
                }
                modelManager.selectEmbeddingModel(modelId)
                _uiState.update { it.copy(isLoading = false, isEmbeddingLoaded = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = "Failed to select model: ${e.message}"
                )}
            }
        }
    }
    
    /**
     * Delete a model
     */
    fun deleteModel(modelId: String) {
        viewModelScope.launch {
            val result = modelManager.deleteModel(modelId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(storageUsage = modelManager.getStorageUsage())
                }
            }
        }
    }
    
    /**
     * Generate embeddings for all entries
     */
    fun generateAllEmbeddings() {
        viewModelScope.launch {
            try {
                // Get total entries first
                val totalEntries = journalRepository.getEntryCount()
                
                if (totalEntries == 0) {
                    _uiState.update { it.copy(
                        error = "No journal entries to generate embeddings for",
                        embeddingProgress = null
                    )}
                    return@launch
                }
                
                _uiState.update { it.copy(
                    embeddingProgress = EmbeddingProgress(0, totalEntries, false, true),
                    error = null,
                    successMessage = null
                )}
                
                val result = retrievalService.generateMissingEmbeddings { current, total ->
                    _uiState.update { state ->
                        state.copy(
                            embeddingProgress = EmbeddingProgress(current, total, false, true)
                        )
                    }
                }
                
                // Refresh embedding stats
                val embeddingCount = embeddingRepository.getEmbeddingCount()
                
                if (result.isSuccess) {
                    val generatedCount = result.getOrDefault(0)
                    _uiState.update { state ->
                        state.copy(
                            embeddingProgress = EmbeddingProgress(embeddingCount, totalEntries, true, false),
                            embeddingStats = state.embeddingStats.copy(
                                embeddingCount = embeddingCount,
                                totalEntries = totalEntries,
                                lastGenerated = LocalDateTime.now()
                            ),
                            successMessage = if (generatedCount > 0) 
                                "Generated $generatedCount embeddings successfully!" 
                            else 
                                "All entries already have embeddings",
                            error = null
                        )
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            embeddingProgress = null,
                            error = "Failed to generate embeddings: ${result.exceptionOrNull()?.message}",
                            successMessage = null  // Clear any previous success message
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error generating embeddings")
                _uiState.update { it.copy(
                    embeddingProgress = null,
                    error = "Error: ${e.message}",
                    successMessage = null  // Clear any previous success message
                )}
            }
        }
    }
    
    /**
     * Clear success message
     */
    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
    
    /**
     * Clear conversation
     */
    fun clearConversation() {
        _uiState.update { state ->
            state.copy(
                currentSessionId = UUID.randomUUID().toString(),
                conversationMessages = emptyList()
            )
        }
    }
    
    /**
     * Get shareable text for the conversation
     */
    fun getShareableConversation(): String {
        val messages = _uiState.value.conversationMessages
        if (messages.isEmpty()) return ""
        
        return buildString {
            appendLine("=".repeat(40))
            appendLine("AI Journal Conversation")
            appendLine(java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a")
                .format(LocalDateTime.now()))
            appendLine("=".repeat(40))
            appendLine()
            
            messages.forEach { message ->
                val role = if (message.role == "user") "You" else "AI"
                appendLine("[$role]")
                appendLine(message.content)
                appendLine()
            }
        }
    }
    
    /**
     * Get shareable text for a single message
     */
    fun getShareableMessage(message: ChatMessage): String {
        val role = if (message.role == "user") "You" else "AI"
        return "[$role]\n${message.content}"
    }
    
    /**
     * Clear error
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    /**
     * Import a model from a file URI
     */
    fun importModel(uri: Uri, name: String, modelType: ModelType) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = modelManager.importModelFromUri(uri, name, modelType)
            
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        storageUsage = modelManager.getStorageUsage()
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to import model"
                    )
                }
            }
        }
    }
    
    /**
     * Initialize models on first launch
     */
    fun initializeIfNeeded() {
        viewModelScope.launch {
            modelManager.initializeDefaultModels()
            modelManager.loadSelectedModels()
        }
    }
}
