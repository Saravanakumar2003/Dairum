package com.dairum.data.ai.retrieval

import com.dairum.data.ai.embedding.EmbeddingService
import com.dairum.data.ai.model.ModelManager
import com.dairum.domain.model.JournalEmbedding
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.RetrievalResult
import com.dairum.domain.repository.AIModelRepository
import com.dairum.domain.repository.EmbeddingRepository
import com.dairum.domain.repository.JournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

/**
 * Interface for semantic search and retrieval
 */
interface RetrievalService {
    
    /**
     * Search for relevant entries based on a query
     */
    suspend fun searchRelevantEntries(
        query: String,
        topK: Int = 5,
        minSimilarity: Float = 0.3f
    ): Result<List<RetrievalResult>>
    
    /**
     * Search within a date range
     */
    suspend fun searchRelevantEntriesInRange(
        query: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        topK: Int = 5,
        minSimilarity: Float = 0.3f
    ): Result<List<RetrievalResult>>
    
    /**
     * Find similar entries to a given entry
     */
    suspend fun findSimilarEntries(
        entryId: Long,
        topK: Int = 5,
        excludeSelf: Boolean = true
    ): Result<List<RetrievalResult>>
    
    /**
     * Compute similarity between two texts
     */
    suspend fun computeSimilarity(text1: String, text2: String): Result<Float>
    
    /**
     * Update embedding for a single entry
     */
    suspend fun updateEntryEmbedding(entry: JournalEntry): Result<Unit>
    
    /**
     * Update embeddings for multiple entries (batch)
     */
    suspend fun updateEntryEmbeddings(entries: List<JournalEntry>): Result<Int>
    
    /**
     * Generate embeddings for all entries that don't have one
     */
    suspend fun generateMissingEmbeddings(
        onProgress: ((current: Int, total: Int) -> Unit)? = null
    ): Result<Int>
    
    /**
     * Check if embeddings need to be regenerated (e.g., model changed)
     */
    suspend fun needsRegeneration(): Boolean
}

/**
 * Implementation of retrieval service using cosine similarity
 */
@Singleton
class LocalRetrievalService @Inject constructor(
    private val embeddingService: EmbeddingService,
    private val embeddingRepository: EmbeddingRepository,
    private val journalRepository: JournalRepository,
    private val modelManager: ModelManager,
    private val modelRepository: AIModelRepository
) : RetrievalService {
    
    companion object {
        private const val TAG = "LocalRetrievalService"
        private const val BATCH_SIZE = 32
    }
    
    // In-memory cache of embeddings for fast search
    private var embeddingCache: Map<Long, FloatArray> = emptyMap()
    private var cacheVersion = 0
    
    /**
     * Ensures an embedding model is loaded. Tries selected model first, then any available model.
     * Returns failure if no model is available.
     */
    private suspend fun ensureEmbeddingModelLoaded(): Result<Unit> {
        if (embeddingService.isModelLoaded()) {
            return Result.success(Unit)
        }
        
        // Try to load the selected embedding model first
        val selectedModelId = modelManager.getSelectedEmbeddingModelId().first()
        Timber.d("Loading embedding model, selected ID: $selectedModelId")
        
        if (selectedModelId != null) {
            val model = modelRepository.getModelById(selectedModelId)
            if (model != null && model.isDownloaded && model.filePath.isNotBlank()) {
                val modelFile = java.io.File(model.filePath)
                if (modelFile.exists() && modelFile.length() > 0) {
                    Timber.d("Loading selected embedding model: ${model.name}")
                    return embeddingService.loadModel(model.filePath)
                }
            }
        }
        
        // Try to find any available embedding model
        val embeddingModels = modelRepository.getModelsByType(com.dairum.domain.model.ModelType.EMBEDDING).first()
        val availableModel = embeddingModels.firstOrNull { m -> 
            m.isDownloaded && m.filePath.isNotBlank() && java.io.File(m.filePath).exists()
        }
        
        return if (availableModel != null) {
            Timber.d("Using available embedding model: ${availableModel.name}")
            val result = embeddingService.loadModel(availableModel.filePath)
            if (result.isSuccess) {
                modelManager.selectEmbeddingModel(availableModel.id)
            }
            result
        } else {
            Result.failure(Exception("No embedding model available. Please download or import an embedding model from the Models tab."))
        }
    }
    
    override suspend fun searchRelevantEntries(
        query: String,
        topK: Int,
        minSimilarity: Float
    ): Result<List<RetrievalResult>> = withContext(Dispatchers.Default) {
        try {
            ensureEmbeddingModelLoaded().getOrThrow()
            
            // Generate query embedding
            val queryEmbedding = embeddingService.generateEmbedding(query).getOrThrow()
            
            // Load all embeddings (cached for performance)
            val embeddings = loadEmbeddingsToCache()
            
            if (embeddings.isEmpty()) {
                return@withContext Result.success(emptyList())
            }
            
            // Compute similarities and rank
            val similarities = embeddings.map { (entryId, embedding) ->
                entryId to cosineSimilarity(queryEmbedding, embedding)
            }
            
            // Filter by minimum similarity and sort
            val topResults = similarities
                .filter { it.second >= minSimilarity }
                .sortedByDescending { it.second }
                .take(topK)
            
            // Load actual entries
            val results = topResults.mapIndexedNotNull { index, (entryId, score) ->
                val entry = journalRepository.getEntry(entryId)
                entry?.let {
                    RetrievalResult(
                        entry = it,
                        score = score,
                        relevanceRank = index + 1
                    )
                }
            }
            
            Timber.d("Search found ${results.size} relevant entries for query: ${query.take(50)}...")
            Result.success(results)
        } catch (e: Exception) {
            Timber.e(e, "Search failed")
            Result.failure(e)
        }
    }
    
    override suspend fun searchRelevantEntriesInRange(
        query: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        topK: Int,
        minSimilarity: Float
    ): Result<List<RetrievalResult>> = withContext(Dispatchers.Default) {
        try {
            ensureEmbeddingModelLoaded().getOrThrow()
            
            // Generate query embedding
            val queryEmbedding = embeddingService.generateEmbedding(query).getOrThrow()
            
            // Get embeddings in date range
            val embeddings = embeddingRepository.getEmbeddingsInDateRange(startDate, endDate)
            
            if (embeddings.isEmpty()) {
                return@withContext Result.success(emptyList())
            }
            
            // Compute similarities
            val similarities = embeddings.map { embedding ->
                embedding.entryId to cosineSimilarity(queryEmbedding, embedding.embedding)
            }
            
            // Filter and sort
            val topResults = similarities
                .filter { it.second >= minSimilarity }
                .sortedByDescending { it.second }
                .take(topK)
            
            // Load entries
            val results = topResults.mapIndexedNotNull { index, (entryId, score) ->
                val entry = journalRepository.getEntry(entryId)
                entry?.let {
                    RetrievalResult(
                        entry = it,
                        score = score,
                        relevanceRank = index + 1
                    )
                }
            }
            
            Result.success(results)
        } catch (e: Exception) {
            Timber.e(e, "Search in range failed")
            Result.failure(e)
        }
    }
    
    override suspend fun findSimilarEntries(
        entryId: Long,
        topK: Int,
        excludeSelf: Boolean
    ): Result<List<RetrievalResult>> = withContext(Dispatchers.Default) {
        try {
            // Get embedding for the source entry
            val sourceEmbedding = embeddingRepository.getEmbeddingForEntry(entryId)
                ?: return@withContext Result.failure(Exception("No embedding found for entry $entryId"))
            
            // Load all embeddings
            val embeddings = loadEmbeddingsToCache()
            
            // Compute similarities
            val similarities = embeddings
                .filter { !excludeSelf || it.key != entryId }
                .map { (id, embedding) ->
                    id to cosineSimilarity(sourceEmbedding.embedding, embedding)
                }
            
            // Sort and take top K
            val topResults = similarities
                .sortedByDescending { it.second }
                .take(topK)
            
            // Load entries
            val results = topResults.mapIndexedNotNull { index, (id, score) ->
                val entry = journalRepository.getEntry(id)
                entry?.let {
                    RetrievalResult(
                        entry = it,
                        score = score,
                        relevanceRank = index + 1
                    )
                }
            }
            
            Result.success(results)
        } catch (e: Exception) {
            Timber.e(e, "Find similar entries failed")
            Result.failure(e)
        }
    }
    
    override suspend fun computeSimilarity(text1: String, text2: String): Result<Float> = withContext(Dispatchers.Default) {
        try {
            ensureEmbeddingModelLoaded().getOrThrow()
            
            val embeddings = embeddingService.generateEmbeddings(listOf(text1, text2)).getOrThrow()
            val similarity = cosineSimilarity(embeddings[0], embeddings[1])
            Result.success(similarity)
        } catch (e: Exception) {
            Timber.e(e, "Compute similarity failed")
            Result.failure(e)
        }
    }
    
    override suspend fun updateEntryEmbedding(entry: JournalEntry): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            ensureEmbeddingModelLoaded().getOrThrow()
            
            val text = buildEntryText(entry)
            if (text.isBlank()) {
                Timber.d("Entry ${entry.id} has no content, skipping embedding")
                return@withContext Result.success(Unit)
            }
            
            val embeddingArray = embeddingService.generateEmbedding(text).getOrThrow()
            
            val embedding = JournalEmbedding(
                entryId = entry.id,
                embedding = embeddingArray,
                model = embeddingService.getModelName(),
                version = 1,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
            
            embeddingRepository.saveEmbedding(embedding)
            invalidateCache()
            
            Timber.d("Updated embedding for entry ${entry.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to update embedding for entry ${entry.id}")
            Result.failure(e)
        }
    }
    
    override suspend fun updateEntryEmbeddings(entries: List<JournalEntry>): Result<Int> = withContext(Dispatchers.IO) {
        try {
            ensureEmbeddingModelLoaded().getOrThrow()
            
            var updatedCount = 0
            
            // Process in batches
            entries.chunked(BATCH_SIZE).forEach { batch ->
                val texts = batch.map { buildEntryText(it) }
                val validIndices = texts.indices.filter { texts[it].isNotBlank() }
                
                if (validIndices.isNotEmpty()) {
                    val validTexts = validIndices.map { texts[it] }
                    val embeddingsResult = embeddingService.generateEmbeddings(validTexts)
                    
                    if (embeddingsResult.isSuccess) {
                        val embeddingArrays = embeddingsResult.getOrThrow()
                        val embeddings = validIndices.mapIndexed { idx, originalIdx ->
                            JournalEmbedding(
                                entryId = batch[originalIdx].id,
                                embedding = embeddingArrays[idx],
                                model = embeddingService.getModelName(),
                                version = 1,
                                createdAt = LocalDateTime.now(),
                                updatedAt = LocalDateTime.now()
                            )
                        }
                        
                        embeddingRepository.saveEmbeddings(embeddings)
                        updatedCount += embeddings.size
                    }
                }
            }
            
            invalidateCache()
            Timber.d("Updated $updatedCount embeddings")
            Result.success(updatedCount)
        } catch (e: Exception) {
            Timber.e(e, "Failed to update embeddings")
            Result.failure(e)
        }
    }
    
    override suspend fun generateMissingEmbeddings(
        onProgress: ((current: Int, total: Int) -> Unit)?
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            ensureEmbeddingModelLoaded().getOrThrow()
            
            // Get all entry IDs
            val allEntries = journalRepository.getAllEntries().first()
            val embeddedIds = embeddingRepository.getAllEmbeddedEntryIds().toSet()
            
            // Find entries without embeddings
            val missingEntries = allEntries.filter { it.id !in embeddedIds }
            
            if (missingEntries.isEmpty()) {
                Timber.d("No missing embeddings")
                return@withContext Result.success(0)
            }
            
            Timber.d("Generating embeddings for ${missingEntries.size} entries")
            
            var generatedCount = 0
            val total = missingEntries.size
            
            // Process in batches
            missingEntries.chunked(BATCH_SIZE).forEach { batch ->
                val texts = batch.map { buildEntryText(it) }
                val validIndices = texts.indices.filter { texts[it].isNotBlank() }
                
                if (validIndices.isNotEmpty()) {
                    val validTexts = validIndices.map { texts[it] }
                    val embeddingsResult = embeddingService.generateEmbeddings(validTexts)
                    
                    if (embeddingsResult.isSuccess) {
                        val embeddingArrays = embeddingsResult.getOrThrow()
                        val embeddings = validIndices.mapIndexed { idx, originalIdx ->
                            JournalEmbedding(
                                entryId = batch[originalIdx].id,
                                embedding = embeddingArrays[idx],
                                model = embeddingService.getModelName(),
                                version = 1,
                                createdAt = LocalDateTime.now(),
                                updatedAt = LocalDateTime.now()
                            )
                        }
                        
                        embeddingRepository.saveEmbeddings(embeddings)
                        generatedCount += embeddings.size
                    }
                }
                
                onProgress?.invoke(generatedCount, total)
            }
            
            invalidateCache()
            Timber.d("Generated $generatedCount embeddings")
            Result.success(generatedCount)
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate missing embeddings")
            Result.failure(e)
        }
    }
    
    override suspend fun needsRegeneration(): Boolean {
        val currentModel = embeddingService.getModelName()
        val existingCount = embeddingRepository.getEmbeddingCount()
        
        if (existingCount == 0) {
            return journalRepository.getAllEntries().first().isNotEmpty()
        }
        
        // Check if model changed - would need to track model used for embeddings
        // For now, assume regeneration not needed if embeddings exist
        return false
    }
    
    /**
     * Build searchable text from entry
     */
    private fun buildEntryText(entry: JournalEntry): String {
        val parts = mutableListOf<String>()
        
        entry.title?.let { parts.add(it) }
        
        if (entry.plainContent.isNotBlank()) {
            parts.add(entry.plainContent)
        }
        
        // Add metadata for better context
        entry.mood?.let { parts.add("Mood: ${it.label}") }
        if (entry.tags.isNotEmpty()) {
            parts.add("Tags: ${entry.tags.joinToString(", ") { it.name }}")
        }
        if (entry.people.isNotEmpty()) {
            parts.add("People: ${entry.people.joinToString(", ") { it.name }}")
        }
        entry.location?.placeName?.let { parts.add("Location: $it") }
        
        return parts.joinToString("\n")
    }
    
    /**
     * Load embeddings into memory cache
     */
    private suspend fun loadEmbeddingsToCache(): Map<Long, FloatArray> {
        if (embeddingCache.isEmpty()) {
            val embeddings = embeddingRepository.getAllEmbeddings()
            embeddingCache = embeddings.associate { it.entryId to it.embedding }
        }
        return embeddingCache
    }
    
    private fun invalidateCache() {
        embeddingCache = emptyMap()
        cacheVersion++
    }
    
    /**
     * Compute cosine similarity between two vectors
     */
    private fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
        if (a.size != b.size) return 0f
        
        var dotProduct = 0f
        var normA = 0f
        var normB = 0f
        
        for (i in a.indices) {
            dotProduct += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }
        
        val denominator = sqrt(normA) * sqrt(normB)
        return if (denominator > 0) dotProduct / denominator else 0f
    }
}
