package com.dairum.presentation.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.ai.conversation.ConversationAIOrchestrator
import com.dairum.data.ai.conversation.ConversationStreamEvent
import com.dairum.data.ai.llm.LocalLLM
import com.dairum.data.ai.model.ModelManager
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

/**
 * UI State for the Chat Screen
 */
data class ChatUiState(
    val isLoading: Boolean = false,
    val isInitializing: Boolean = true,
    val error: String? = null,
    val successMessage: String? = null,
    
    // Session state
    val currentSession: ChatSession? = null,
    val messages: List<ChatMessage> = emptyList(),
    
    // Input state
    val currentInput: String = "",
    val isGenerating: Boolean = false,
    val streamingContent: String = "",
    val processingStatus: String? = null,
    
    // Sessions list
    val savedSessions: List<ChatSession> = emptyList(),
    val showSessionsList: Boolean = false,
    
    // AI readiness
    val isAIReady: Boolean = false,
    val isLLMLoaded: Boolean = false,
    val isEmbeddingLoaded: Boolean = false,
    val selectedLLMName: String? = null,
    
    // Memory insights (optional display)
    val reflectionCount: Int = 0,
    val showMemoryInsights: Boolean = false,
    val recentReflections: List<ReflectionMemory> = emptyList()
) {
    val canSend: Boolean
        get() = currentInput.isNotBlank() && !isGenerating && isAIReady
    
    val isSaved: Boolean
        get() = currentSession?.isSaved == true
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val conversationOrchestrator: ConversationAIOrchestrator,
    private val chatSessionRepository: ChatSessionRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val reflectionMemoryRepository: ReflectionMemoryRepository,
    private val modelManager: ModelManager,
    private val localLLM: LocalLLM
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    private var generationJob: Job? = null
    
    init {
        initializeChat()
        observeModelState()
        loadSavedSessions()
        loadReflections()
    }
    
    private fun initializeChat() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isInitializing = true) }
                
                // Check if we're resuming an existing session
                val sessionId = savedStateHandle.get<String>("sessionId")
                
                if (sessionId != null) {
                    // Load existing session
                    val session = chatSessionRepository.getSessionById(sessionId)
                    if (session != null) {
                        loadSession(session)
                    } else {
                        createNewSession()
                    }
                } else {
                    // Create new session
                    createNewSession()
                }
                
                _uiState.update { 
                    it.copy(
                        isInitializing = false,
                        isAIReady = conversationOrchestrator.isReady()
                    ) 
                }
            } catch (e: Exception) {
                Timber.e(e, "Error initializing chat")
                _uiState.update { 
                    it.copy(
                        isInitializing = false,
                        error = "Failed to initialize chat: ${e.message}"
                    )
                }
            }
        }
    }
    
    private fun observeModelState() {
        viewModelScope.launch {
            modelManager.getSelectedLLMModelId().collect { modelId ->
                val isLoaded = localLLM.isModelLoaded()
                val modelName = localLLM.getModelName()
                _uiState.update { 
                    it.copy(
                        isLLMLoaded = isLoaded,
                        selectedLLMName = modelName,
                        isAIReady = isLoaded && localLLM.isNativeImplementation()
                    )
                }
            }
        }
    }
    
    private fun loadSavedSessions() {
        viewModelScope.launch {
            chatSessionRepository.getSavedSessions().collect { sessions ->
                _uiState.update { it.copy(savedSessions = sessions) }
            }
        }
    }
    
    private fun loadReflections() {
        viewModelScope.launch {
            reflectionMemoryRepository.getActiveReflections().collect { reflections ->
                _uiState.update { 
                    it.copy(
                        reflectionCount = reflections.size,
                        recentReflections = reflections.take(5)
                    )
                }
            }
        }
    }
    
    private suspend fun createNewSession() {
        val session = conversationOrchestrator.createSession()
        _uiState.update { 
            it.copy(
                currentSession = session,
                messages = emptyList()
            )
        }
        savedStateHandle["sessionId"] = session.id
    }
    
    private suspend fun loadSession(session: ChatSession) {
        chatMessageRepository.getMessagesForSession(session.id).collect { messages ->
            _uiState.update { 
                it.copy(
                    currentSession = session,
                    messages = messages
                )
            }
        }
    }
    
    /**
     * Update the current input text
     */
    fun updateInput(text: String) {
        _uiState.update { it.copy(currentInput = text) }
    }
    
    /**
     * Send a message to the AI
     */
    fun sendMessage() {
        val input = _uiState.value.currentInput.trim()
        val sessionId = _uiState.value.currentSession?.id ?: return
        
        if (input.isBlank() || _uiState.value.isGenerating) return
        
        _uiState.update { 
            it.copy(
                currentInput = "",
                isGenerating = true,
                error = null
            )
        }
        
        generationJob = viewModelScope.launch {
            // Add user message optimistically
            val tempUserMessage = ChatMessage(
                id = -1, // Temporary ID
                sessionId = sessionId,
                role = MessageRole.USER,
                content = input,
                createdAt = LocalDateTime.now()
            )
            _uiState.update { it.copy(messages = it.messages + tempUserMessage) }
            
            val result = conversationOrchestrator.sendMessage(sessionId, input)
            
            result.onSuccess { assistantMessage ->
                // Refresh messages from repository
                val messages = chatMessageRepository.getMessagesForSessionList(sessionId)
                _uiState.update { 
                    it.copy(
                        messages = messages,
                        isGenerating = false
                    )
                }
                
                // Generate title after first exchange
                if (messages.size == 2) {
                    generateTitle()
                }
            }.onFailure { error ->
                // Remove temporary message on error
                _uiState.update { state ->
                    state.copy(
                        messages = state.messages.filter { it.id != -1L },
                        isGenerating = false,
                        error = error.message ?: "Failed to send message"
                    )
                }
            }
        }
    }
    
    /**
     * Send message with streaming response
     */
    fun sendMessageStreaming() {
        val input = _uiState.value.currentInput.trim()
        val sessionId = _uiState.value.currentSession?.id ?: return
        
        if (input.isBlank() || _uiState.value.isGenerating) return
        
        _uiState.update { 
            it.copy(
                currentInput = "",
                isGenerating = true,
                streamingContent = "",
                error = null
            )
        }
        
        generationJob = viewModelScope.launch {
            conversationOrchestrator.sendMessageStream(sessionId, input).collect { event ->
                when (event) {
                    is ConversationStreamEvent.Processing -> {
                        _uiState.update { it.copy(processingStatus = event.status) }
                    }
                    is ConversationStreamEvent.UserMessageSaved -> {
                        _uiState.update { state ->
                            state.copy(
                                messages = state.messages + event.message,
                                processingStatus = null
                            )
                        }
                    }
                    is ConversationStreamEvent.GenerationStarted -> {
                        _uiState.update { it.copy(streamingContent = "") }
                    }
                    is ConversationStreamEvent.Token -> {
                        _uiState.update { state ->
                            state.copy(streamingContent = state.streamingContent + event.token)
                        }
                    }
                    is ConversationStreamEvent.Complete -> {
                        _uiState.update { state ->
                            state.copy(
                                messages = state.messages + event.message,
                                isGenerating = false,
                                streamingContent = ""
                            )
                        }
                        
                        // Generate title after first exchange
                        if (_uiState.value.messages.size == 2) {
                            generateTitle()
                        }
                    }
                    is ConversationStreamEvent.Error -> {
                        _uiState.update { 
                            it.copy(
                                isGenerating = false,
                                streamingContent = "",
                                error = event.message
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Cancel ongoing generation
     */
    fun cancelGeneration() {
        generationJob?.cancel()
        conversationOrchestrator.cancelGeneration()
        _uiState.update { 
            it.copy(
                isGenerating = false,
                streamingContent = "",
                processingStatus = null
            )
        }
    }
    
    /**
     * Toggle save status of current session
     */
    fun toggleSave() {
        val session = _uiState.value.currentSession ?: return
        
        viewModelScope.launch {
            val newSaveStatus = !session.isSaved
            chatSessionRepository.toggleSaveStatus(session.id, newSaveStatus)
            
            _uiState.update { state ->
                state.copy(
                    currentSession = session.copy(isSaved = newSaveStatus),
                    successMessage = if (newSaveStatus) "Conversation saved" else "Conversation unsaved"
                )
            }
        }
    }
    
    /**
     * Update session title
     */
    fun updateTitle(title: String) {
        val sessionId = _uiState.value.currentSession?.id ?: return
        
        viewModelScope.launch {
            chatSessionRepository.updateSessionTitle(sessionId, title)
            _uiState.update { state ->
                state.copy(
                    currentSession = state.currentSession?.copy(title = title)
                )
            }
        }
    }
    
    /**
     * Generate title automatically
     */
    private fun generateTitle() {
        val sessionId = _uiState.value.currentSession?.id ?: return
        
        viewModelScope.launch {
            conversationOrchestrator.generateSessionTitle(sessionId).onSuccess { title ->
                _uiState.update { state ->
                    state.copy(
                        currentSession = state.currentSession?.copy(title = title)
                    )
                }
            }
        }
    }
    
    /**
     * Start a new conversation
     */
    fun startNewConversation() {
        viewModelScope.launch {
            createNewSession()
        }
    }
    
    /**
     * Switch to a different session
     */
    fun switchToSession(session: ChatSession) {
        viewModelScope.launch {
            savedStateHandle["sessionId"] = session.id
            loadSession(session)
            _uiState.update { it.copy(showSessionsList = false) }
        }
    }
    
    /**
     * Delete a session
     */
    fun deleteSession(session: ChatSession) {
        viewModelScope.launch {
            chatSessionRepository.deleteSession(session.id)
            
            // If we deleted the current session, create a new one
            if (session.id == _uiState.value.currentSession?.id) {
                createNewSession()
            }
        }
    }
    
    /**
     * Toggle sessions list visibility
     */
    fun toggleSessionsList() {
        _uiState.update { it.copy(showSessionsList = !it.showSessionsList) }
    }
    
    /**
     * Toggle memory insights visibility
     */
    fun toggleMemoryInsights() {
        _uiState.update { it.copy(showMemoryInsights = !it.showMemoryInsights) }
    }
    
    /**
     * Force reflection generation
     */
    fun generateReflections() {
        val sessionId = _uiState.value.currentSession?.id ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            conversationOrchestrator.generateReflections(sessionId).onSuccess { reflections ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        successMessage = "Generated ${reflections.size} reflections"
                    )
                }
            }.onFailure { error ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Failed to generate reflections: ${error.message}"
                    )
                }
            }
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    /**
     * Clear success message
     */
    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
    
    /**
     * Export current conversation
     */
    fun exportConversation(format: ChatExportFormat): Flow<Result<String>> = flow {
        val session = _uiState.value.currentSession
        val messages = _uiState.value.messages
        
        if (session == null || messages.isEmpty()) {
            emit(Result.failure(Exception("No conversation to export")))
            return@flow
        }
        
        val content = buildExportContent(session, messages, format)
        emit(Result.success(content))
    }
    
    private fun buildExportContent(
        session: ChatSession,
        messages: List<ChatMessage>,
        format: ChatExportFormat
    ): String {
        return when (format) {
            ChatExportFormat.PLAIN_TEXT -> {
                buildString {
                    appendLine("Conversation: ${session.displayTitle}")
                    appendLine("Date: ${session.createdAt}")
                    appendLine("=" .repeat(50))
                    appendLine()
                    
                    messages.forEach { message ->
                        val role = if (message.role == MessageRole.USER) "You" else "Assistant"
                        appendLine("[$role]")
                        appendLine(message.content)
                        appendLine()
                    }
                }
            }
            ChatExportFormat.MARKDOWN -> {
                buildString {
                    appendLine("# ${session.displayTitle}")
                    appendLine()
                    appendLine("*${session.createdAt}*")
                    appendLine()
                    appendLine("---")
                    appendLine()
                    
                    messages.forEach { message ->
                        val role = if (message.role == MessageRole.USER) "**You**" else "**Assistant**"
                        appendLine("### $role")
                        appendLine()
                        appendLine(message.content)
                        appendLine()
                    }
                }
            }
            ChatExportFormat.JSON -> {
                // Simple JSON format
                """
                {
                    "title": "${session.displayTitle}",
                    "created": "${session.createdAt}",
                    "messages": [
                        ${messages.joinToString(",\n") { msg ->
                            """{"role": "${msg.role.name.lowercase()}", "content": "${msg.content.replace("\"", "\\\"").replace("\n", "\\n")}"}"""
                        }}
                    ]
                }
                """.trimIndent()
            }
            ChatExportFormat.PDF -> {
                // For PDF, we'd need a separate export service
                buildExportContent(session, messages, ChatExportFormat.PLAIN_TEXT)
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        generationJob?.cancel()
    }
}
