package com.dairum.domain.repository

import com.dairum.domain.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Repository for managing chat sessions
 */
interface ChatSessionRepository {
    
    /**
     * Get all chat sessions
     */
    fun getAllSessions(): Flow<List<ChatSession>>
    
    /**
     * Get only saved sessions
     */
    fun getSavedSessions(): Flow<List<ChatSession>>
    
    /**
     * Get active (non-archived) sessions
     */
    fun getActiveSessions(): Flow<List<ChatSession>>
    
    /**
     * Get a session by ID
     */
    suspend fun getSessionById(sessionId: String): ChatSession?
    
    /**
     * Observe a session by ID
     */
    fun observeSession(sessionId: String): Flow<ChatSession?>
    
    /**
     * Get session with all messages
     */
    suspend fun getSessionWithMessages(sessionId: String): ChatSessionWithMessages?
    
    /**
     * Observe session with messages
     */
    fun observeSessionWithMessages(sessionId: String): Flow<ChatSessionWithMessages?>
    
    /**
     * Create a new session
     */
    suspend fun createSession(session: ChatSession)
    
    /**
     * Update session
     */
    suspend fun updateSession(session: ChatSession)
    
    /**
     * Update session title
     */
    suspend fun updateSessionTitle(sessionId: String, title: String)
    
    /**
     * Toggle save status
     */
    suspend fun toggleSaveStatus(sessionId: String, isSaved: Boolean)
    
    /**
     * Archive a session
     */
    suspend fun archiveSession(sessionId: String)
    
    /**
     * Delete a session
     */
    suspend fun deleteSession(sessionId: String)
    
    /**
     * Delete old archived sessions
     */
    suspend fun deleteOldArchivedSessions(olderThan: LocalDateTime)
    
    /**
     * Get session count
     */
    suspend fun getSessionCount(): Int
}

/**
 * Repository for chat messages
 */
interface ChatMessageRepository {
    
    /**
     * Get messages for a session
     */
    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessage>>
    
    /**
     * Get messages list (non-Flow)
     */
    suspend fun getMessagesForSessionList(sessionId: String): List<ChatMessage>
    
    /**
     * Get recent messages (for short-term memory)
     */
    suspend fun getRecentMessages(sessionId: String, limit: Int): List<ChatMessage>
    
    /**
     * Get last N messages in reverse chronological order
     */
    suspend fun getLastNMessages(sessionId: String, limit: Int): List<ChatMessage>
    
    /**
     * Get a message by ID
     */
    suspend fun getMessageById(messageId: Long): ChatMessage?
    
    /**
     * Add a message
     */
    suspend fun addMessage(message: ChatMessage): Long
    
    /**
     * Add multiple messages
     */
    suspend fun addMessages(messages: List<ChatMessage>)
    
    /**
     * Update message content
     */
    suspend fun updateMessageContent(messageId: Long, content: String)
    
    /**
     * Delete a message
     */
    suspend fun deleteMessage(messageId: Long)
    
    /**
     * Delete all messages in a session
     */
    suspend fun deleteMessagesForSession(sessionId: String)
    
    /**
     * Get message count for session
     */
    suspend fun getMessageCountForSession(sessionId: String): Int
    
    /**
     * Get total token usage for session
     */
    suspend fun getTotalTokensForSession(sessionId: String): Int
}

/**
 * Repository for chat embeddings
 */
interface ChatEmbeddingRepository {
    
    /**
     * Get embedding for a message
     */
    suspend fun getEmbeddingForMessage(messageId: Long): FloatArray?
    
    /**
     * Get all embeddings for a session
     */
    suspend fun getEmbeddingsForSession(sessionId: String): Map<Long, FloatArray>
    
    /**
     * Get all embeddings
     */
    suspend fun getAllEmbeddings(): Map<Long, FloatArray>
    
    /**
     * Save embedding for a message
     */
    suspend fun saveEmbedding(messageId: Long, sessionId: String, embedding: FloatArray, model: String)
    
    /**
     * Save multiple embeddings
     */
    suspend fun saveEmbeddings(embeddings: List<Triple<Long, String, FloatArray>>, model: String)
    
    /**
     * Delete embedding for a message
     */
    suspend fun deleteEmbeddingForMessage(messageId: Long)
    
    /**
     * Delete all embeddings for a session
     */
    suspend fun deleteEmbeddingsForSession(sessionId: String)
    
    /**
     * Delete obsolete embeddings (from different model)
     */
    suspend fun deleteObsoleteEmbeddings(currentModel: String)
    
    /**
     * Get embedding count
     */
    suspend fun getEmbeddingCount(): Int
}

/**
 * Repository for reflection memories
 */
interface ReflectionMemoryRepository {
    
    /**
     * Get all active reflections as Flow
     */
    fun getActiveReflections(): Flow<List<ReflectionMemory>>
    
    /**
     * Get all active reflections as list
     */
    suspend fun getActiveReflectionsList(): List<ReflectionMemory>
    
    /**
     * Get reflections with embeddings for semantic search
     */
    suspend fun getReflectionsWithEmbeddings(): List<ReflectionMemory>
    
    /**
     * Get reflections by category
     */
    fun getReflectionsByCategory(category: ReflectionCategory): Flow<List<ReflectionMemory>>
    
    /**
     * Get reflections by category as list
     */
    suspend fun getReflectionsByCategoryList(category: ReflectionCategory): List<ReflectionMemory>
    
    /**
     * Get reflection by ID
     */
    suspend fun getReflectionById(id: Long): ReflectionMemory?
    
    /**
     * Get recent reflections
     */
    suspend fun getRecentReflections(limit: Int): List<ReflectionMemory>
    
    /**
     * Get high confidence reflections
     */
    suspend fun getHighConfidenceReflections(minConfidence: Float = 0.7f): List<ReflectionMemory>
    
    /**
     * Save a reflection
     */
    suspend fun saveReflection(reflection: ReflectionMemory): Long
    
    /**
     * Save multiple reflections
     */
    suspend fun saveReflections(reflections: List<ReflectionMemory>)
    
    /**
     * Update reflection
     */
    suspend fun updateReflection(reflection: ReflectionMemory)
    
    /**
     * Update last referenced time (when used in retrieval)
     */
    suspend fun updateLastReferenced(id: Long)
    
    /**
     * Increment occurrence count
     */
    suspend fun incrementOccurrenceCount(id: Long)
    
    /**
     * Update active status
     */
    suspend fun updateActiveStatus(id: Long, isActive: Boolean)
    
    /**
     * Update embedding
     */
    suspend fun updateEmbedding(id: Long, embedding: FloatArray, model: String)
    
    /**
     * Delete a reflection
     */
    suspend fun deleteReflection(id: Long)
    
    /**
     * Delete weak reflections (low confidence, low occurrence)
     */
    suspend fun deleteWeakReflections(minConfidence: Float = 0.3f, minOccurrences: Int = 2)
    
    /**
     * Get reflection count
     */
    suspend fun getActiveReflectionCount(): Int
    
    /**
     * Get reflections by multiple categories
     */
    suspend fun getReflectionsByCategories(categories: List<ReflectionCategory>, limit: Int): List<ReflectionMemory>
}

/**
 * Repository for the user-editable AI system prompt and guardrail rules.
 */
interface AIGuardrailsRepository {
    fun getSettingsFlow(): Flow<AIGuardrailSettings>
    suspend fun getSettings(): AIGuardrailSettings
    suspend fun updateSystemPrompt(prompt: String)
    suspend fun resetSystemPrompt()
    suspend fun addRule(text: String)
    suspend fun removeRule(id: String)
    suspend fun setRuleEnabled(id: String, isEnabled: Boolean)
    suspend fun updateRuleText(id: String, text: String)
    suspend fun resetToDefaults()
}

/**
 * Repository for conversation context
 */
interface ConversationContextRepository {
    
    /**
     * Get context for a session
     */
    suspend fun getContextForSession(sessionId: String): ConversationContext?
    
    /**
     * Observe context for a session
     */
    fun observeContextForSession(sessionId: String): Flow<ConversationContext?>
    
    /**
     * Save or update context
     */
    suspend fun saveContext(context: ConversationContext)
    
    /**
     * Update current topics
     */
    suspend fun updateTopics(sessionId: String, topics: List<String>)
    
    /**
     * Update emotional tone
     */
    suspend fun updateEmotionalTone(sessionId: String, tone: String)
    
    /**
     * Update discussion depth
     */
    suspend fun updateDiscussionDepth(sessionId: String, depth: Int)
    
    /**
     * Update last summary time
     */
    suspend fun updateLastSummaryTime(sessionId: String, time: LocalDateTime)
    
    /**
     * Delete context for session
     */
    suspend fun deleteContextForSession(sessionId: String)
}

/**
 * Repository for exporting chat sessions
 */
interface ChatExportRepository {
    
    /**
     * Export session to text
     */
    suspend fun exportToText(sessionId: String, options: ChatExportOptions): Result<String>
    
    /**
     * Export session to PDF
     */
    suspend fun exportToPdf(sessionId: String, options: ChatExportOptions): Result<java.io.File>
    
    /**
     * Export session to JSON
     */
    suspend fun exportToJson(sessionId: String, options: ChatExportOptions): Result<String>
    
    /**
     * Save export to file
     */
    suspend fun saveExportToFile(content: String, filename: String, format: ChatExportFormat): Result<java.io.File>
}
