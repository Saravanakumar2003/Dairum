package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * DAO for AI model management
 */
@Dao
interface AIModelDao {
    
    @Query("SELECT * FROM ai_models ORDER BY name ASC")
    fun getAllModels(): Flow<List<AIModelEntity>>
    
    @Query("SELECT * FROM ai_models ORDER BY name ASC")
    suspend fun getAllModelsList(): List<AIModelEntity>
    
    @Query("SELECT * FROM ai_models WHERE modelType = :modelType ORDER BY name ASC")
    fun getModelsByType(modelType: String): Flow<List<AIModelEntity>>
    
    @Query("SELECT * FROM ai_models WHERE isDownloaded = 1 ORDER BY name ASC")
    fun getDownloadedModels(): Flow<List<AIModelEntity>>
    
    @Query("SELECT * FROM ai_models WHERE id = :id")
    suspend fun getModelById(id: String): AIModelEntity?
    
    @Query("SELECT * FROM ai_models WHERE isDefault = 1 AND modelType = :modelType LIMIT 1")
    suspend fun getDefaultModel(modelType: String): AIModelEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: AIModelEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModels(models: List<AIModelEntity>)
    
    @Update
    suspend fun updateModel(model: AIModelEntity)
    
    @Query("UPDATE ai_models SET isDefault = 0 WHERE modelType = :modelType")
    suspend fun clearDefaultForType(modelType: String)
    
    @Query("UPDATE ai_models SET isDefault = 1 WHERE id = :modelId")
    suspend fun setAsDefault(modelId: String)
    
    @Query("UPDATE ai_models SET isDownloaded = :isDownloaded, filePath = :filePath WHERE id = :modelId")
    suspend fun updateDownloadStatus(modelId: String, isDownloaded: Boolean, filePath: String)
    
    @Query("UPDATE ai_models SET lastUsedAt = :lastUsedAt WHERE id = :modelId")
    suspend fun updateLastUsed(modelId: String, lastUsedAt: LocalDateTime)
    
    @Delete
    suspend fun deleteModel(model: AIModelEntity)
    
    @Query("DELETE FROM ai_models WHERE id = :modelId")
    suspend fun deleteModelById(modelId: String)
}

/**
 * DAO for journal embeddings
 */
@Dao
interface JournalEmbeddingDao {
    
    @Query("SELECT * FROM journal_embeddings WHERE entryId = :entryId")
    suspend fun getEmbeddingForEntry(entryId: Long): JournalEmbeddingEntity?
    
    @Query("SELECT * FROM journal_embeddings")
    suspend fun getAllEmbeddings(): List<JournalEmbeddingEntity>
    
    @Query("SELECT entryId FROM journal_embeddings")
    suspend fun getAllEmbeddedEntryIds(): List<Long>
    
    @Query("SELECT COUNT(*) FROM journal_embeddings")
    suspend fun getEmbeddingCount(): Int
    
    @Query("SELECT COUNT(*) FROM journal_embeddings WHERE model = :model")
    suspend fun getEmbeddingCountByModel(model: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmbedding(embedding: JournalEmbeddingEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmbeddings(embeddings: List<JournalEmbeddingEntity>)
    
    @Update
    suspend fun updateEmbedding(embedding: JournalEmbeddingEntity)
    
    @Query("DELETE FROM journal_embeddings WHERE entryId = :entryId")
    suspend fun deleteEmbeddingForEntry(entryId: Long)
    
    @Query("DELETE FROM journal_embeddings WHERE model != :currentModel")
    suspend fun deleteObsoleteEmbeddings(currentModel: String)
    
    @Query("DELETE FROM journal_embeddings")
    suspend fun deleteAllEmbeddings()
    
    @Query("""
        SELECT je.* FROM journal_embeddings je
        INNER JOIN journal_entries e ON je.entryId = e.id
        WHERE e.createdAt BETWEEN :startDate AND :endDate
    """)
    suspend fun getEmbeddingsInDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<JournalEmbeddingEntity>
}

/**
 * DAO for weekly reflections
 */
@Dao
interface WeeklyReflectionDao {
    
    @Query("SELECT * FROM weekly_reflections ORDER BY weekStartDate DESC")
    fun getAllReflections(): Flow<List<WeeklyReflectionEntity>>
    
    @Query("SELECT * FROM weekly_reflections ORDER BY weekStartDate DESC LIMIT :limit")
    fun getRecentReflections(limit: Int): Flow<List<WeeklyReflectionEntity>>
    
    @Query("SELECT * FROM weekly_reflections WHERE id = :id")
    suspend fun getReflectionById(id: Long): WeeklyReflectionEntity?
    
    @Query("SELECT * FROM weekly_reflections WHERE weekStartDate = :weekStart")
    suspend fun getReflectionForWeek(weekStart: LocalDateTime): WeeklyReflectionEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(reflection: WeeklyReflectionEntity): Long
    
    @Delete
    suspend fun deleteReflection(reflection: WeeklyReflectionEntity)
    
    @Query("DELETE FROM weekly_reflections WHERE id = :id")
    suspend fun deleteReflectionById(id: Long)
}

/**
 * DAO for pattern results
 */
@Dao
interface PatternResultDao {
    
    @Query("SELECT * FROM pattern_results ORDER BY detectedAt DESC")
    fun getAllPatterns(): Flow<List<PatternResultEntity>>
    
    @Query("SELECT * FROM pattern_results WHERE patternType = :patternType ORDER BY detectedAt DESC")
    fun getPatternsByType(patternType: String): Flow<List<PatternResultEntity>>
    
    @Query("SELECT * FROM pattern_results ORDER BY detectedAt DESC LIMIT :limit")
    fun getRecentPatterns(limit: Int): Flow<List<PatternResultEntity>>
    
    @Query("SELECT * FROM pattern_results WHERE id = :id")
    suspend fun getPatternById(id: Long): PatternResultEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPattern(pattern: PatternResultEntity): Long
    
    @Delete
    suspend fun deletePattern(pattern: PatternResultEntity)
    
    @Query("DELETE FROM pattern_results WHERE detectedAt < :beforeDate")
    suspend fun deleteOldPatterns(beforeDate: LocalDateTime)
}

/**
 * DAO for smart summaries
 */
@Dao
interface SmartSummaryDao {
    
    @Query("SELECT * FROM smart_summaries ORDER BY createdAt DESC")
    fun getAllSummaries(): Flow<List<SmartSummaryEntity>>
    
    @Query("SELECT * FROM smart_summaries WHERE summaryType = :summaryType ORDER BY createdAt DESC")
    fun getSummariesByType(summaryType: String): Flow<List<SmartSummaryEntity>>
    
    @Query("SELECT * FROM smart_summaries ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentSummaries(limit: Int): Flow<List<SmartSummaryEntity>>
    
    @Query("SELECT * FROM smart_summaries WHERE id = :id")
    suspend fun getSummaryById(id: Long): SmartSummaryEntity?
    
    @Query("SELECT * FROM smart_summaries WHERE summaryType = :type AND periodStart = :periodStart")
    suspend fun getSummaryForPeriod(type: String, periodStart: LocalDateTime): SmartSummaryEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: SmartSummaryEntity): Long
    
    @Delete
    suspend fun deleteSummary(summary: SmartSummaryEntity)
    
    @Query("DELETE FROM smart_summaries WHERE id = :id")
    suspend fun deleteSummaryById(id: Long)
}

/**
 * DAO for AI conversations
 */
@Dao
interface AIConversationDao {
    
    @Query("SELECT * FROM ai_conversations WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    fun getConversation(sessionId: String): Flow<List<AIConversationEntity>>
    
    @Query("SELECT DISTINCT sessionId FROM ai_conversations ORDER BY createdAt DESC")
    fun getAllSessionIds(): Flow<List<String>>
    
    @Query("SELECT * FROM ai_conversations WHERE sessionId = :sessionId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastMessageInSession(sessionId: String): AIConversationEntity?
    
    @Insert
    suspend fun insertMessage(message: AIConversationEntity): Long
    
    @Query("DELETE FROM ai_conversations WHERE sessionId = :sessionId")
    suspend fun deleteConversation(sessionId: String)
    
    @Query("DELETE FROM ai_conversations WHERE createdAt < :beforeDate")
    suspend fun deleteOldConversations(beforeDate: LocalDateTime)
    
    @Query("DELETE FROM ai_conversations")
    suspend fun deleteAllConversations()
}
