package com.dairum.domain.repository

import com.dairum.domain.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Repository for managing AI models
 */
interface AIModelRepository {
    
    /**
     * Get all available AI models
     */
    fun getAllModels(): Flow<List<AIModel>>
    
    /**
     * Get models by type (LLM or EMBEDDING)
     */
    fun getModelsByType(modelType: ModelType): Flow<List<AIModel>>
    
    /**
     * Get all downloaded models
     */
    fun getDownloadedModels(): Flow<List<AIModel>>
    
    /**
     * Get a specific model by ID
     */
    suspend fun getModelById(id: String): AIModel?
    
    /**
     * Get the default model for a type
     */
    suspend fun getDefaultModel(modelType: ModelType): AIModel?
    
    /**
     * Save a model (insert or update)
     */
    suspend fun saveModel(model: AIModel)
    
    /**
     * Set a model as the default for its type
     */
    suspend fun setAsDefault(modelId: String, modelType: ModelType)
    
    /**
     * Update download status
     */
    suspend fun updateDownloadStatus(modelId: String, isDownloaded: Boolean, filePath: String)
    
    /**
     * Update last used timestamp
     */
    suspend fun updateLastUsed(modelId: String)
    
    /**
     * Delete a model (and optionally its file)
     */
    suspend fun deleteModel(modelId: String, deleteFile: Boolean = true)
    
    /**
     * Initialize default models if none exist
     */
    suspend fun initializeDefaultModels()
}

/**
 * Repository for managing journal embeddings
 */
interface EmbeddingRepository {
    
    /**
     * Get embedding for a specific entry
     */
    suspend fun getEmbeddingForEntry(entryId: Long): JournalEmbedding?
    
    /**
     * Get all embeddings
     */
    suspend fun getAllEmbeddings(): List<JournalEmbedding>
    
    /**
     * Get IDs of all entries that have embeddings
     */
    suspend fun getAllEmbeddedEntryIds(): List<Long>
    
    /**
     * Get embedding count
     */
    suspend fun getEmbeddingCount(): Int
    
    /**
     * Save an embedding
     */
    suspend fun saveEmbedding(embedding: JournalEmbedding)
    
    /**
     * Save multiple embeddings
     */
    suspend fun saveEmbeddings(embeddings: List<JournalEmbedding>)
    
    /**
     * Delete embedding for an entry
     */
    suspend fun deleteEmbeddingForEntry(entryId: Long)
    
    /**
     * Delete all embeddings for obsolete model
     */
    suspend fun deleteObsoleteEmbeddings(currentModel: String)
    
    /**
     * Delete all embeddings
     */
    suspend fun deleteAllEmbeddings()
    
    /**
     * Get embeddings within date range
     */
    suspend fun getEmbeddingsInDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<JournalEmbedding>
}

/**
 * Repository for AI-generated content (reflections, patterns, summaries)
 */
interface AIContentRepository {
    
    // Weekly Reflections
    fun getAllReflections(): Flow<List<WeeklyReflection>>
    fun getRecentReflections(limit: Int): Flow<List<WeeklyReflection>>
    suspend fun getReflectionById(id: Long): WeeklyReflection?
    suspend fun getReflectionForWeek(weekStart: LocalDateTime): WeeklyReflection?
    suspend fun saveReflection(reflection: WeeklyReflection): Long
    suspend fun deleteReflection(id: Long)
    
    // Pattern Results
    fun getAllPatterns(): Flow<List<PatternResult>>
    fun getPatternsByType(patternType: PatternType): Flow<List<PatternResult>>
    fun getRecentPatterns(limit: Int): Flow<List<PatternResult>>
    suspend fun getPatternById(id: Long): PatternResult?
    suspend fun savePattern(pattern: PatternResult): Long
    suspend fun deletePattern(id: Long)
    suspend fun deleteOldPatterns(olderThan: LocalDateTime)
    
    // Smart Summaries
    fun getAllSummaries(): Flow<List<SmartSummary>>
    fun getSummariesByType(summaryType: SummaryType): Flow<List<SmartSummary>>
    fun getRecentSummaries(limit: Int): Flow<List<SmartSummary>>
    suspend fun getSummaryById(id: Long): SmartSummary?
    suspend fun getSummaryForPeriod(type: SummaryType, periodStart: LocalDateTime): SmartSummary?
    suspend fun saveSummary(summary: SmartSummary): Long
    suspend fun deleteSummary(id: Long)
}

/**
 * Repository for AI conversation history
 */
interface AIConversationRepository {
    
    /**
     * Get conversation by session ID
     */
    fun getConversation(sessionId: String): Flow<List<AIConversationMessage>>
    
    /**
     * Get all session IDs
     */
    fun getAllSessionIds(): Flow<List<String>>
    
    /**
     * Add a message to a conversation
     */
    suspend fun addMessage(
        sessionId: String,
        role: String,
        content: String,
        sourceEntryIds: List<Long>? = null,
        tokensUsed: Int = 0,
        generationTimeMs: Long = 0
    ): Long
    
    /**
     * Delete a conversation
     */
    suspend fun deleteConversation(sessionId: String)
    
    /**
     * Delete old conversations
     */
    suspend fun deleteOldConversations(olderThan: LocalDateTime)
    
    /**
     * Delete all conversations
     */
    suspend fun deleteAllConversations()
}

/**
 * AI conversation message
 */
data class AIConversationMessage(
    val id: Long,
    val sessionId: String,
    val role: String,
    val content: String,
    val sourceEntryIds: List<Long>?,
    val tokensUsed: Int,
    val generationTimeMs: Long,
    val createdAt: LocalDateTime
)
