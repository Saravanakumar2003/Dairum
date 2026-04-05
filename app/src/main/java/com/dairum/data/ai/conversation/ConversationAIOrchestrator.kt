package com.dairum.data.ai.conversation

import com.dairum.data.ai.embedding.EmbeddingService
import com.dairum.data.ai.llm.LocalLLM
import com.dairum.data.ai.reflection.ReflectionGenerationService
import com.dairum.data.ai.retrieval.ConversationMemoryService
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orchestrates deep conversational AI interactions with memory-aware context.
 * 
 * This orchestrator:
 * 1. Manages conversation sessions and messages
 * 2. Retrieves relevant memories from 3-layer memory system
 * 3. Builds optimized prompts within token limits
 * 4. Generates thoughtful, context-aware responses
 * 5. Triggers reflection generation periodically
 */
@Singleton
class ConversationAIOrchestrator @Inject constructor(
    private val localLLM: LocalLLM,
    private val embeddingService: EmbeddingService,
    private val memoryService: ConversationMemoryService,
    private val reflectionService: ReflectionGenerationService,
    private val chatSessionRepository: ChatSessionRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val conversationContextRepository: ConversationContextRepository
) {
    
    companion object {
        private const val TAG = "ConversationAIOrchestrator"
        
        // System instruction for the AI companion
        private const val SYSTEM_INSTRUCTION = """You are a thoughtful, reflective AI companion. Engage deeply with the user's ideas, especially around ethics, philosophy, emotions, life experiences, and meaningful topics.

Your approach:
- Be genuinely curious and ask thoughtful follow-up questions
- Explore ideas together rather than just answering questions
- Recognize patterns in the user's thinking and gently reflect them back
- Be clear, honest, and nuanced in your responses
- Remember context from the conversation and reference it naturally
- Avoid generic responses - be specific and personal
- When appropriate, share relevant perspectives or gentle challenges

You have access to:
- Recent conversation context
- Relevant past discussions and journal reflections
- Insights about the user's values, concerns, and thinking patterns

Use this context to provide continuity and depth, but don't explicitly mention "memory" or "context" - integrate it naturally."""
        
        // Maximum tokens for context and generation
        private const val MAX_CONTEXT_TOKENS = 1800
        private const val MAX_GENERATION_TOKENS = 400
        private const val MEMORY_SNIPPET_MAX_CHARS = 120
    }
    
    /**
     * Create a new conversation session
     */
    suspend fun createSession(title: String = "New Conversation"): ChatSession {
        val session = ChatSession(
            id = UUID.randomUUID().toString(),
            title = title,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        chatSessionRepository.createSession(session)
        
        // Initialize conversation context
        conversationContextRepository.saveContext(ConversationContext(
            sessionId = session.id
        ))
        
        return session
    }
    
    /**
     * Send a message and get AI response
     */
    suspend fun sendMessage(
        sessionId: String,
        userMessage: String
    ): Result<ChatMessage> = withContext(Dispatchers.Default) {
        try {
            val startTime = System.currentTimeMillis()
            
            // Check if LLM is loaded
            if (!localLLM.isModelLoaded()) {
                return@withContext Result.failure(
                    Exception("AI model not loaded. Please download and select a model.")
                )
            }
            
            // Save user message
            val userMsg = ChatMessage(
                sessionId = sessionId,
                role = MessageRole.USER,
                content = userMessage,
                createdAt = LocalDateTime.now()
            )
            val userMsgId = chatMessageRepository.addMessage(userMsg)
            val savedUserMsg = userMsg.copy(id = userMsgId)
            
            // Generate embedding for user message (async)
            if (embeddingService.isModelLoaded()) {
                try {
                    memoryService.generateMessageEmbedding(savedUserMsg)
                } catch (e: Exception) {
                    Timber.w(e, "Failed to generate user message embedding")
                }
            }
            
            // Retrieve memories
            val memoryConfig = MemoryRetrievalConfig()
            val memories = memoryService.retrieveMemories(userMessage, sessionId, memoryConfig)
                .getOrNull() ?: MemoryRetrievalResult(
                    shortTermMessages = emptyList(),
                    longTermChatMessages = emptyList(),
                    journalEntries = emptyList(),
                    reflectionMemories = emptyList(),
                    totalTokenEstimate = 0
                )
            
            // Build prompt
            val prompt = buildConversationPrompt(userMessage, memories, memoryConfig)
            
            Timber.d("Prompt built, estimated tokens: ${prompt.totalTokens}")
            
            // Generate response
            val response = localLLM.generate(
                prompt = prompt.build(),
                maxTokens = MAX_GENERATION_TOKENS,
                temperature = 0.75f,
                topP = 0.9f,
                repeatPenalty = 1.15f,
                stopTokens = listOf("User:", "Human:", "---")
            ).getOrNull()
            
            if (response == null) {
                return@withContext Result.failure(Exception("Failed to generate response"))
            }
            
            val elapsedTime = System.currentTimeMillis() - startTime
            
            // Create assistant message
            val assistantMsg = ChatMessage(
                sessionId = sessionId,
                role = MessageRole.ASSISTANT,
                content = response.trim(),
                tokensUsed = estimateTokens(response),
                generationTimeMs = elapsedTime,
                metadata = MessageMetadata(
                    sourceEntryIds = memories.journalEntries.map { it.entry.id },
                    sourceReflectionIds = memories.reflectionMemories.map { it.reflection.id }
                ),
                createdAt = LocalDateTime.now()
            )
            val assistantMsgId = chatMessageRepository.addMessage(assistantMsg)
            val savedAssistantMsg = assistantMsg.copy(id = assistantMsgId)
            
            // Generate embedding for assistant message
            if (embeddingService.isModelLoaded()) {
                try {
                    memoryService.generateMessageEmbedding(savedAssistantMsg)
                } catch (e: Exception) {
                    Timber.w(e, "Failed to generate assistant message embedding")
                }
            }
            
            // Check if we should generate reflections
            val messageCount = chatMessageRepository.getMessageCountForSession(sessionId)
            if (messageCount % ReflectionGenerationService.REFLECTION_MESSAGE_INTERVAL == 0) {
                try {
                    reflectionService.generateReflections(sessionId)
                } catch (e: Exception) {
                    Timber.w(e, "Failed to generate reflections")
                }
            }
            
            Result.success(savedAssistantMsg)
        } catch (e: Exception) {
            Timber.e(e, "Error sending message")
            Result.failure(e)
        }
    }
    
    /**
     * Send a message with streaming response
     */
    fun sendMessageStream(
        sessionId: String,
        userMessage: String
    ): Flow<ConversationStreamEvent> = flow {
        try {
            val startTime = System.currentTimeMillis()
            
            // Check if LLM is loaded
            if (!localLLM.isModelLoaded()) {
                emit(ConversationStreamEvent.Error("AI model not loaded"))
                return@flow
            }
            
            emit(ConversationStreamEvent.Processing("Preparing context..."))
            
            // Save user message
            val userMsg = ChatMessage(
                sessionId = sessionId,
                role = MessageRole.USER,
                content = userMessage,
                createdAt = LocalDateTime.now()
            )
            val userMsgId = chatMessageRepository.addMessage(userMsg)
            emit(ConversationStreamEvent.UserMessageSaved(userMsg.copy(id = userMsgId)))
            
            // Retrieve memories
            emit(ConversationStreamEvent.Processing("Gathering memories..."))
            val memories = memoryService.retrieveMemories(userMessage, sessionId)
                .getOrNull() ?: MemoryRetrievalResult(
                    shortTermMessages = emptyList(),
                    longTermChatMessages = emptyList(),
                    journalEntries = emptyList(),
                    reflectionMemories = emptyList(),
                    totalTokenEstimate = 0
                )
            
            // Build prompt
            val prompt = buildConversationPrompt(userMessage, memories, MemoryRetrievalConfig())
            
            emit(ConversationStreamEvent.GenerationStarted)
            
            // Stream generation
            val responseBuilder = StringBuilder()
            localLLM.generateStream(
                prompt = prompt.build(),
                maxTokens = MAX_GENERATION_TOKENS,
                temperature = 0.75f,
                topP = 0.9f,
                repeatPenalty = 1.15f,
                stopTokens = listOf("User:", "Human:", "---")
            ).collect { token ->
                responseBuilder.append(token)
                emit(ConversationStreamEvent.Token(token))
            }
            
            val elapsedTime = System.currentTimeMillis() - startTime
            val fullResponse = responseBuilder.toString().trim()
            
            // Save assistant message
            val assistantMsg = ChatMessage(
                sessionId = sessionId,
                role = MessageRole.ASSISTANT,
                content = fullResponse,
                tokensUsed = estimateTokens(fullResponse),
                generationTimeMs = elapsedTime,
                metadata = MessageMetadata(
                    sourceEntryIds = memories.journalEntries.map { it.entry.id },
                    sourceReflectionIds = memories.reflectionMemories.map { it.reflection.id }
                ),
                createdAt = LocalDateTime.now()
            )
            val assistantMsgId = chatMessageRepository.addMessage(assistantMsg)
            
            emit(ConversationStreamEvent.Complete(assistantMsg.copy(id = assistantMsgId)))
            
        } catch (e: Exception) {
            Timber.e(e, "Error in streaming message")
            emit(ConversationStreamEvent.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.Default)
    
    /**
     * Build the conversation prompt with memory context
     */
    private fun buildConversationPrompt(
        userQuestion: String,
        memories: MemoryRetrievalResult,
        config: MemoryRetrievalConfig
    ): ConversationPrompt {
        var tokensUsed = estimateTokens(SYSTEM_INSTRUCTION) + estimateTokens(userQuestion)
        
        // Build recent conversation section
        val recentConversation = buildString {
            memories.shortTermMessages.forEach { message ->
                val role = if (message.role == MessageRole.USER) "User" else "Assistant"
                val content = message.content.take(MEMORY_SNIPPET_MAX_CHARS * 2)
                appendLine("$role: $content")
                tokensUsed += estimateTokens(content) + 5
            }
        }
        
        // Build relevant memories section
        val relevantMemories = buildString {
            // Add reflections first (most persistent)
            if (memories.reflectionMemories.isNotEmpty()) {
                memories.reflectionMemories.forEach { scored ->
                    val snippet = "• ${scored.reflection.content.take(MEMORY_SNIPPET_MAX_CHARS)}"
                    if (tokensUsed + estimateTokens(snippet) < config.availableContextTokens) {
                        appendLine(snippet)
                        tokensUsed += estimateTokens(snippet)
                    }
                }
            }
            
            // Add journal entries
            if (memories.journalEntries.isNotEmpty()) {
                appendLine()
                appendLine("From journal:")
                memories.journalEntries.forEach { result ->
                    val entry = result.entry
                    val snippet = "• [${entry.createdAt.toLocalDate()}] ${entry.plainContent.take(MEMORY_SNIPPET_MAX_CHARS)}"
                    if (tokensUsed + estimateTokens(snippet) < config.availableContextTokens) {
                        appendLine(snippet)
                        tokensUsed += estimateTokens(snippet)
                    }
                }
            }
            
            // Add long-term chat memories
            if (memories.longTermChatMessages.isNotEmpty()) {
                appendLine()
                appendLine("From past discussions:")
                memories.longTermChatMessages.forEach { scored ->
                    val msg = scored.message
                    val prefix = if (msg.role == MessageRole.USER) "User mentioned" else "We discussed"
                    val snippet = "• $prefix: ${msg.content.take(MEMORY_SNIPPET_MAX_CHARS)}"
                    if (tokensUsed + estimateTokens(snippet) < config.availableContextTokens) {
                        appendLine(snippet)
                        tokensUsed += estimateTokens(snippet)
                    }
                }
            }
        }.trim()
        
        return ConversationPrompt(
            systemInstruction = SYSTEM_INSTRUCTION,
            recentConversation = recentConversation,
            relevantMemories = relevantMemories,
            userQuestion = userQuestion,
            totalTokens = tokensUsed
        )
    }
    
    /**
     * Estimate token count (~4 characters per token)
     */
    private fun estimateTokens(text: String): Int {
        return (text.length / 4).coerceAtLeast(1)
    }
    
    /**
     * Cancel current generation
     */
    fun cancelGeneration() {
        localLLM.cancelGeneration()
    }
    
    /**
     * Check if AI is ready for conversation
     */
    fun isReady(): Boolean {
        return localLLM.isModelLoaded() && localLLM.isNativeImplementation()
    }
    
    /**
     * Get conversation statistics
     */
    suspend fun getConversationStats(): ConversationStats {
        val sessionCount = chatSessionRepository.getSessionCount()
        // Additional stats could be gathered here
        return ConversationStats(
            totalSessions = sessionCount
        )
    }
    
    /**
     * Update session title based on conversation
     */
    suspend fun generateSessionTitle(sessionId: String): Result<String> = withContext(Dispatchers.Default) {
        try {
            if (!localLLM.isModelLoaded()) {
                return@withContext Result.failure(Exception("LLM not loaded"))
            }
            
            val messages = chatMessageRepository.getMessagesForSessionList(sessionId)
                .filter { it.role != MessageRole.SYSTEM }
                .take(6)
            
            if (messages.size < 2) {
                return@withContext Result.success("New Conversation")
            }
            
            val conversationSummary = messages.joinToString("\n") { msg ->
                val role = if (msg.role == MessageRole.USER) "User" else "Assistant"
                "$role: ${msg.content.take(100)}"
            }
            
            val prompt = """Generate a brief, engaging title (3-6 words) for this conversation. Just output the title, nothing else.

Conversation:
$conversationSummary

Title:"""
            
            val title = localLLM.generate(
                prompt = prompt,
                maxTokens = 20,
                temperature = 0.7f
            ).getOrNull()?.trim()?.take(50) ?: "New Conversation"
            
            chatSessionRepository.updateSessionTitle(sessionId, title)
            
            Result.success(title)
        } catch (e: Exception) {
            Timber.e(e, "Error generating session title")
            Result.failure(e)
        }
    }
    
    /**
     * Force reflection generation for a session
     */
    suspend fun generateReflections(sessionId: String): Result<List<ReflectionMemory>> {
        return reflectionService.generateReflections(sessionId, forceGeneration = true)
    }
}

/**
 * Events emitted during streaming conversation
 */
sealed class ConversationStreamEvent {
    data class Processing(val status: String) : ConversationStreamEvent()
    data class UserMessageSaved(val message: ChatMessage) : ConversationStreamEvent()
    data object GenerationStarted : ConversationStreamEvent()
    data class Token(val token: String) : ConversationStreamEvent()
    data class Complete(val message: ChatMessage) : ConversationStreamEvent()
    data class Error(val message: String) : ConversationStreamEvent()
}
