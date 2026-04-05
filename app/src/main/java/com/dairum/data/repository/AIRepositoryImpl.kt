package com.dairum.data.repository

import com.dairum.data.ai.model.ModelManager
import com.dairum.data.local.dao.*
import com.dairum.data.local.entity.*
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of AIModelRepository
 */
@Singleton
class AIModelRepositoryImpl @Inject constructor(
    private val aiModelDao: AIModelDao
) : AIModelRepository {
    
    override fun getAllModels(): Flow<List<AIModel>> {
        return aiModelDao.getAllModels().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getModelsByType(modelType: ModelType): Flow<List<AIModel>> {
        return aiModelDao.getModelsByType(modelType.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getDownloadedModels(): Flow<List<AIModel>> {
        return aiModelDao.getDownloadedModels().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getModelById(id: String): AIModel? {
        return aiModelDao.getModelById(id)?.toDomain()
    }
    
    override suspend fun getDefaultModel(modelType: ModelType): AIModel? {
        return aiModelDao.getDefaultModel(modelType.name)?.toDomain()
    }
    
    override suspend fun saveModel(model: AIModel) {
        aiModelDao.insertModel(AIModelEntity.fromDomain(model))
    }
    
    override suspend fun setAsDefault(modelId: String, modelType: ModelType) {
        aiModelDao.clearDefaultForType(modelType.name)
        aiModelDao.setAsDefault(modelId)
    }
    
    override suspend fun updateDownloadStatus(modelId: String, isDownloaded: Boolean, filePath: String) {
        aiModelDao.updateDownloadStatus(modelId, isDownloaded, filePath)
    }
    
    override suspend fun updateLastUsed(modelId: String) {
        aiModelDao.updateLastUsed(modelId, LocalDateTime.now())
    }
    
    override suspend fun deleteModel(modelId: String, deleteFile: Boolean) {
        aiModelDao.deleteModelById(modelId)
    }
    
    override suspend fun initializeDefaultModels() {
        val existingModels = aiModelDao.getAllModelsList()
        val existingIds = existingModels.map { it.id }.toSet()
        
        // Insert default models that don't already exist
        var inserted = 0
        ModelManager.DEFAULT_MODELS.forEach { model ->
            if (model.id !in existingIds) {
                android.util.Log.d("AIModelRepository", "Inserting default model: ${model.name} (${model.modelType})")
                aiModelDao.insertModel(AIModelEntity.fromDomain(model))
                inserted++
            }
        }
        android.util.Log.d("AIModelRepository", "Initialized default models: $inserted new, ${existingModels.size} existing")
    }
}

/**
 * Implementation of EmbeddingRepository
 */
@Singleton
class EmbeddingRepositoryImpl @Inject constructor(
    private val embeddingDao: JournalEmbeddingDao
) : EmbeddingRepository {
    
    override suspend fun getEmbeddingForEntry(entryId: Long): JournalEmbedding? {
        return embeddingDao.getEmbeddingForEntry(entryId)?.toDomain()
    }
    
    override suspend fun getAllEmbeddings(): List<JournalEmbedding> {
        return embeddingDao.getAllEmbeddings().map { it.toDomain() }
    }
    
    override suspend fun getAllEmbeddedEntryIds(): List<Long> {
        return embeddingDao.getAllEmbeddedEntryIds()
    }
    
    override suspend fun getEmbeddingCount(): Int {
        return embeddingDao.getEmbeddingCount()
    }
    
    override suspend fun saveEmbedding(embedding: JournalEmbedding) {
        embeddingDao.insertEmbedding(JournalEmbeddingEntity.fromDomain(embedding))
    }
    
    override suspend fun saveEmbeddings(embeddings: List<JournalEmbedding>) {
        embeddingDao.insertEmbeddings(embeddings.map { JournalEmbeddingEntity.fromDomain(it) })
    }
    
    override suspend fun deleteEmbeddingForEntry(entryId: Long) {
        embeddingDao.deleteEmbeddingForEntry(entryId)
    }
    
    override suspend fun deleteObsoleteEmbeddings(currentModel: String) {
        embeddingDao.deleteObsoleteEmbeddings(currentModel)
    }
    
    override suspend fun deleteAllEmbeddings() {
        embeddingDao.deleteAllEmbeddings()
    }
    
    override suspend fun getEmbeddingsInDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<JournalEmbedding> {
        return embeddingDao.getEmbeddingsInDateRange(startDate, endDate).map { it.toDomain() }
    }
}

/**
 * Implementation of AIContentRepository
 */
@Singleton
class AIContentRepositoryImpl @Inject constructor(
    private val reflectionDao: WeeklyReflectionDao,
    private val patternDao: PatternResultDao,
    private val summaryDao: SmartSummaryDao,
    private val moshi: Moshi
) : AIContentRepository {
    
    private val stringListAdapter = moshi.adapter<List<String>>(
        Types.newParameterizedType(List::class.java, String::class.java)
    )
    private val stringIntMapAdapter = moshi.adapter<Map<String, Int>>(
        Types.newParameterizedType(Map::class.java, String::class.java, Integer::class.java)
    )
    
    // Weekly Reflections
    
    override fun getAllReflections(): Flow<List<WeeklyReflection>> {
        return reflectionDao.getAllReflections().map { entities ->
            entities.map { entityToReflection(it) }
        }
    }
    
    override fun getRecentReflections(limit: Int): Flow<List<WeeklyReflection>> {
        return reflectionDao.getRecentReflections(limit).map { entities ->
            entities.map { entityToReflection(it) }
        }
    }
    
    override suspend fun getReflectionById(id: Long): WeeklyReflection? {
        return reflectionDao.getReflectionById(id)?.let { entityToReflection(it) }
    }
    
    override suspend fun getReflectionForWeek(weekStart: LocalDateTime): WeeklyReflection? {
        return reflectionDao.getReflectionForWeek(weekStart)?.let { entityToReflection(it) }
    }
    
    override suspend fun saveReflection(reflection: WeeklyReflection): Long {
        return reflectionDao.insertReflection(reflectionToEntity(reflection))
    }
    
    override suspend fun deleteReflection(id: Long) {
        reflectionDao.deleteReflectionById(id)
    }
    
    private fun entityToReflection(entity: WeeklyReflectionEntity): WeeklyReflection {
        return WeeklyReflection(
            id = entity.id,
            weekStartDate = entity.weekStartDate,
            weekEndDate = entity.weekEndDate,
            summary = entity.summary,
            moodTrend = MoodTrend(
                averageMood = entity.averageMood,
                moodRange = entity.moodRangeMin to entity.moodRangeMax,
                dominantMood = entity.dominantMood,
                moodDistribution = stringIntMapAdapter.fromJson(entity.moodDistributionJson) ?: emptyMap(),
                trend = TrendDirection.valueOf(entity.moodTrend)
            ),
            keyThemes = stringListAdapter.fromJson(entity.keyThemesJson) ?: emptyList(),
            highlights = stringListAdapter.fromJson(entity.highlightsJson) ?: emptyList(),
            suggestions = stringListAdapter.fromJson(entity.suggestionsJson) ?: emptyList(),
            entryCount = entity.entryCount,
            totalWordCount = entity.totalWordCount,
            createdAt = entity.createdAt
        )
    }
    
    private fun reflectionToEntity(reflection: WeeklyReflection): WeeklyReflectionEntity {
        return WeeklyReflectionEntity(
            id = reflection.id,
            weekStartDate = reflection.weekStartDate,
            weekEndDate = reflection.weekEndDate,
            summary = reflection.summary,
            averageMood = reflection.moodTrend.averageMood,
            moodRangeMin = reflection.moodTrend.moodRange.first,
            moodRangeMax = reflection.moodTrend.moodRange.second,
            dominantMood = reflection.moodTrend.dominantMood,
            moodDistributionJson = stringIntMapAdapter.toJson(reflection.moodTrend.moodDistribution),
            moodTrend = reflection.moodTrend.trend.name,
            keyThemesJson = stringListAdapter.toJson(reflection.keyThemes),
            highlightsJson = stringListAdapter.toJson(reflection.highlights),
            suggestionsJson = stringListAdapter.toJson(reflection.suggestions),
            entryCount = reflection.entryCount,
            totalWordCount = reflection.totalWordCount,
            createdAt = reflection.createdAt
        )
    }
    
    // Pattern Results
    
    override fun getAllPatterns(): Flow<List<PatternResult>> {
        return patternDao.getAllPatterns().map { entities ->
            entities.map { entityToPattern(it) }
        }
    }
    
    override fun getPatternsByType(patternType: PatternType): Flow<List<PatternResult>> {
        return patternDao.getPatternsByType(patternType.name).map { entities ->
            entities.map { entityToPattern(it) }
        }
    }
    
    override fun getRecentPatterns(limit: Int): Flow<List<PatternResult>> {
        return patternDao.getRecentPatterns(limit).map { entities ->
            entities.map { entityToPattern(it) }
        }
    }
    
    override suspend fun getPatternById(id: Long): PatternResult? {
        return patternDao.getPatternById(id)?.let { entityToPattern(it) }
    }
    
    override suspend fun savePattern(pattern: PatternResult): Long {
        return patternDao.insertPattern(patternToEntity(pattern))
    }
    
    override suspend fun deletePattern(id: Long) {
        patternDao.deletePattern(PatternResultEntity(id = id, "", "", 0f, "", "", LocalDateTime.now()))
    }
    
    override suspend fun deleteOldPatterns(olderThan: LocalDateTime) {
        patternDao.deleteOldPatterns(olderThan)
    }
    
    private fun entityToPattern(entity: PatternResultEntity): PatternResult {
        return PatternResult(
            id = entity.id,
            patternType = PatternType.valueOf(entity.patternType),
            description = entity.description,
            confidence = entity.confidence,
            evidence = emptyList(), // Would need to parse JSON
            insights = stringListAdapter.fromJson(entity.insightsJson) ?: emptyList(),
            detectedAt = entity.detectedAt
        )
    }
    
    private fun patternToEntity(pattern: PatternResult): PatternResultEntity {
        return PatternResultEntity(
            id = pattern.id,
            patternType = pattern.patternType.name,
            description = pattern.description,
            confidence = pattern.confidence,
            evidenceJson = "[]",
            insightsJson = stringListAdapter.toJson(pattern.insights),
            detectedAt = pattern.detectedAt
        )
    }
    
    // Smart Summaries
    
    override fun getAllSummaries(): Flow<List<SmartSummary>> {
        return summaryDao.getAllSummaries().map { entities ->
            entities.map { entityToSummary(it) }
        }
    }
    
    override fun getSummariesByType(summaryType: SummaryType): Flow<List<SmartSummary>> {
        return summaryDao.getSummariesByType(summaryType.name).map { entities ->
            entities.map { entityToSummary(it) }
        }
    }
    
    override fun getRecentSummaries(limit: Int): Flow<List<SmartSummary>> {
        return summaryDao.getRecentSummaries(limit).map { entities ->
            entities.map { entityToSummary(it) }
        }
    }
    
    override suspend fun getSummaryById(id: Long): SmartSummary? {
        return summaryDao.getSummaryById(id)?.let { entityToSummary(it) }
    }
    
    override suspend fun getSummaryForPeriod(type: SummaryType, periodStart: LocalDateTime): SmartSummary? {
        return summaryDao.getSummaryForPeriod(type.name, periodStart)?.let { entityToSummary(it) }
    }
    
    override suspend fun saveSummary(summary: SmartSummary): Long {
        return summaryDao.insertSummary(summaryToEntity(summary))
    }
    
    override suspend fun deleteSummary(id: Long) {
        summaryDao.deleteSummaryById(id)
    }
    
    private fun entityToSummary(entity: SmartSummaryEntity): SmartSummary {
        return SmartSummary(
            id = entity.id,
            summaryType = SummaryType.valueOf(entity.summaryType),
            period = DateRange(entity.periodStart, entity.periodEnd),
            title = entity.title,
            summary = entity.summary,
            keyPoints = stringListAdapter.fromJson(entity.keyPointsJson) ?: emptyList(),
            moodOverview = entity.moodOverview,
            topTags = stringListAdapter.fromJson(entity.topTagsJson) ?: emptyList(),
            topPeople = stringListAdapter.fromJson(entity.topPeopleJson) ?: emptyList(),
            wordCount = entity.wordCount,
            entryCount = entity.entryCount,
            createdAt = entity.createdAt
        )
    }
    
    private fun summaryToEntity(summary: SmartSummary): SmartSummaryEntity {
        return SmartSummaryEntity(
            id = summary.id,
            summaryType = summary.summaryType.name,
            periodStart = summary.period.start,
            periodEnd = summary.period.end,
            title = summary.title,
            summary = summary.summary,
            keyPointsJson = stringListAdapter.toJson(summary.keyPoints),
            moodOverview = summary.moodOverview,
            topTagsJson = stringListAdapter.toJson(summary.topTags),
            topPeopleJson = stringListAdapter.toJson(summary.topPeople),
            wordCount = summary.wordCount,
            entryCount = summary.entryCount,
            createdAt = summary.createdAt
        )
    }
}

/**
 * Implementation of AIConversationRepository
 */
@Singleton
class AIConversationRepositoryImpl @Inject constructor(
    private val conversationDao: AIConversationDao,
    private val moshi: Moshi
) : AIConversationRepository {
    
    private val longListAdapter = moshi.adapter<List<Long>>(
        Types.newParameterizedType(List::class.java, Long::class.javaObjectType)
    )
    
    override fun getConversation(sessionId: String): Flow<List<AIConversationMessage>> {
        return conversationDao.getConversation(sessionId).map { entities ->
            entities.map { entityToMessage(it) }
        }
    }
    
    override fun getAllSessionIds(): Flow<List<String>> {
        return conversationDao.getAllSessionIds()
    }
    
    override suspend fun addMessage(
        sessionId: String,
        role: String,
        content: String,
        sourceEntryIds: List<Long>?,
        tokensUsed: Int,
        generationTimeMs: Long
    ): Long {
        val entity = AIConversationEntity(
            sessionId = sessionId,
            role = role,
            content = content,
            sourceEntryIdsJson = sourceEntryIds?.let { longListAdapter.toJson(it) },
            tokensUsed = tokensUsed,
            generationTimeMs = generationTimeMs,
            createdAt = LocalDateTime.now()
        )
        return conversationDao.insertMessage(entity)
    }
    
    override suspend fun deleteConversation(sessionId: String) {
        conversationDao.deleteConversation(sessionId)
    }
    
    override suspend fun deleteOldConversations(olderThan: LocalDateTime) {
        conversationDao.deleteOldConversations(olderThan)
    }
    
    override suspend fun deleteAllConversations() {
        conversationDao.deleteAllConversations()
    }
    
    private fun entityToMessage(entity: AIConversationEntity): AIConversationMessage {
        return AIConversationMessage(
            id = entity.id,
            sessionId = entity.sessionId,
            role = entity.role,
            content = entity.content,
            sourceEntryIds = entity.sourceEntryIdsJson?.let { longListAdapter.fromJson(it) },
            tokensUsed = entity.tokensUsed,
            generationTimeMs = entity.generationTimeMs,
            createdAt = entity.createdAt
        )
    }
}
