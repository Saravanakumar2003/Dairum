package com.dairum.data.ai.retrieval

import com.dairum.data.ai.embedding.EmbeddingService
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

/**
 * Service that implements the 3-layer memory system for conversational AI
 * 
 * Layer 1: Short-term memory (last 3-5 messages)
 * Layer 2: Long-term memory (semantic retrieval from chat history + journal entries)
 * Layer 3: Reflection memory (summarized insights about the user)
 */
@Singleton
class ConversationMemoryService @Inject constructor(
    private val embeddingService: EmbeddingService,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatEmbeddingRepository: ChatEmbeddingRepository,
    private val reflectionMemoryRepository: ReflectionMemoryRepository,
    private val journalRepository: JournalRepository,
    private val embeddingRepository: EmbeddingRepository
) {
    
    companion object {
        private const val TAG = "ConversationMemoryService"
        
        // Approximate token counts for memory management
        private const val TOKENS_PER_MESSAGE_ESTIMATE = 50
        private const val TOKENS_PER_JOURNAL_ENTRY_ESTIMATE = 100
        private const val TOKENS_PER_REFLECTION_ESTIMATE = 30
        private const val SYSTEM_PROMPT_TOKEN_ESTIMATE = 200
    }
    
    /**
     * Retrieve relevant memories for building the conversation prompt
     */
    suspend fun retrieveMemories(
        query: String,
        sessionId: String,
        config: MemoryRetrievalConfig = MemoryRetrievalConfig()
    ): Result<MemoryRetrievalResult> = withContext(Dispatchers.Default) {
        try {
            Timber.d("Retrieving memories for query: ${query.take(50)}...")
            
            // Generate query embedding
            val queryEmbedding = embeddingService.generateEmbedding(query).getOrNull()
            if (queryEmbedding == null) {
                Timber.w("Failed to generate query embedding, using fallback retrieval")
                return@withContext fallbackRetrieval(sessionId, config)
            }
            
            // Run retrievals in parallel
            val shortTermDeferred = async { getShortTermMemory(sessionId, config.shortTermLimit) }
            val longTermChatDeferred = async { getLongTermChatMemory(queryEmbedding, sessionId, config) }
            val journalDeferred = async { getJournalMemory(queryEmbedding, config) }
            val reflectionDeferred = async { getReflectionMemory(queryEmbedding, config) }
            
            val shortTermMessages = shortTermDeferred.await()
            val longTermChatMessages = longTermChatDeferred.await()
            val journalEntries = journalDeferred.await()
            val reflectionMemories = reflectionDeferred.await()
            
            // Calculate token estimate
            val tokenEstimate = calculateTokenEstimate(
                shortTermMessages,
                longTermChatMessages,
                journalEntries,
                reflectionMemories
            )
            
            // Update last referenced for used reflections
            reflectionMemories.forEach { scored ->
                reflectionMemoryRepository.updateLastReferenced(scored.reflection.id)
            }
            
            Timber.d("Retrieved: ${shortTermMessages.size} short-term, " +
                    "${longTermChatMessages.size} long-term chat, " +
                    "${journalEntries.size} journal, " +
                    "${reflectionMemories.size} reflections. " +
                    "Est. tokens: $tokenEstimate")
            
            Result.success(MemoryRetrievalResult(
                shortTermMessages = shortTermMessages,
                longTermChatMessages = longTermChatMessages,
                journalEntries = journalEntries,
                reflectionMemories = reflectionMemories,
                totalTokenEstimate = tokenEstimate
            ))
        } catch (e: Exception) {
            Timber.e(e, "Error retrieving memories")
            Result.failure(e)
        }
    }
    
    /**
     * Layer 1: Get short-term memory (last N messages in current session)
     */
    private suspend fun getShortTermMemory(
        sessionId: String,
        limit: Int
    ): List<ChatMessage> {
        return chatMessageRepository.getRecentMessages(sessionId, limit)
            .filter { it.role != MessageRole.SYSTEM }
    }
    
    /**
     * Layer 2a: Get long-term memory from chat history via semantic search
     */
    private suspend fun getLongTermChatMemory(
        queryEmbedding: FloatArray,
        currentSessionId: String,
        config: MemoryRetrievalConfig
    ): List<ScoredChatMessage> {
        // Get all chat embeddings
        val allEmbeddings = chatEmbeddingRepository.getAllEmbeddings()
        
        if (allEmbeddings.isEmpty()) {
            return emptyList()
        }
        
        // Calculate similarities
        val similarities = allEmbeddings.mapNotNull { (messageId, embedding) ->
            val score = cosineSimilarity(queryEmbedding, embedding)
            if (score >= config.minSimilarity) {
                messageId to score
            } else null
        }
        
        // Get top-K messages (excluding current session's recent messages)
        val recentMessageIds = chatMessageRepository.getRecentMessages(
            currentSessionId, 
            config.shortTermLimit
        ).map { it.id }.toSet()
        
        val topMessages = similarities
            .filter { (messageId, _) -> messageId !in recentMessageIds }
            .sortedByDescending { it.second }
            .take(config.longTermChatLimit)
        
        // Fetch actual messages
        return topMessages.mapIndexedNotNull { index, (messageId, score) ->
            chatMessageRepository.getMessageById(messageId)?.let { message ->
                ScoredChatMessage(
                    message = message,
                    score = score,
                    rank = index + 1
                )
            }
        }
    }
    
    /**
     * Layer 2b: Get relevant journal entries via semantic search
     */
    private suspend fun getJournalMemory(
        queryEmbedding: FloatArray,
        config: MemoryRetrievalConfig
    ): List<RetrievalResult> {
        // Load journal embeddings
        val journalEmbeddings = embeddingRepository.getAllEmbeddings()
        
        if (journalEmbeddings.isEmpty()) {
            return emptyList()
        }
        
        // Create embedding map
        val embeddingMap = journalEmbeddings.associate { it.entryId to it.embedding }
        
        // Calculate similarities
        val similarities = embeddingMap.mapNotNull { (entryId, embedding) ->
            val score = cosineSimilarity(queryEmbedding, embedding)
            if (score >= config.minSimilarity) {
                entryId to score
            } else null
        }
        
        // Get top entries
        val topEntries = similarities
            .sortedByDescending { it.second }
            .take(config.journalLimit)
        
        // Fetch actual entries
        return topEntries.mapIndexedNotNull { index, (entryId, score) ->
            journalRepository.getEntry(entryId)?.let { entry ->
                RetrievalResult(
                    entry = entry,
                    score = score,
                    relevanceRank = index + 1
                )
            }
        }
    }
    
    /**
     * Layer 3: Get reflection memories via semantic search
     */
    private suspend fun getReflectionMemory(
        queryEmbedding: FloatArray,
        config: MemoryRetrievalConfig
    ): List<ScoredReflection> {
        val reflections = reflectionMemoryRepository.getReflectionsWithEmbeddings()
        
        if (reflections.isEmpty()) {
            return emptyList()
        }
        
        // Calculate similarities
        val scored = reflections.mapNotNull { reflection ->
            reflection.embedding?.let { embedding ->
                val score = cosineSimilarity(queryEmbedding, embedding)
                if (score >= config.minSimilarity) {
                    reflection to score
                } else null
            }
        }
        
        // Get top reflections
        return scored
            .sortedByDescending { it.second }
            .take(config.reflectionLimit)
            .mapIndexed { index, (reflection, score) ->
                ScoredReflection(
                    reflection = reflection,
                    score = score,
                    rank = index + 1
                )
            }
    }
    
    /**
     * Fallback retrieval when embedding service is unavailable
     */
    private suspend fun fallbackRetrieval(
        sessionId: String,
        config: MemoryRetrievalConfig
    ): Result<MemoryRetrievalResult> {
        val shortTermMessages = getShortTermMemory(sessionId, config.shortTermLimit)
        val recentReflections = reflectionMemoryRepository.getHighConfidenceReflections(0.7f)
            .take(config.reflectionLimit)
            .mapIndexed { index, reflection ->
                ScoredReflection(reflection, 0.8f, index + 1)
            }
        
        return Result.success(MemoryRetrievalResult(
            shortTermMessages = shortTermMessages,
            longTermChatMessages = emptyList(),
            journalEntries = emptyList(),
            reflectionMemories = recentReflections,
            totalTokenEstimate = calculateTokenEstimate(
                shortTermMessages, emptyList(), emptyList(), recentReflections
            )
        ))
    }
    
    /**
     * Calculate estimated token count for memory
     */
    private fun calculateTokenEstimate(
        shortTermMessages: List<ChatMessage>,
        longTermChatMessages: List<ScoredChatMessage>,
        journalEntries: List<RetrievalResult>,
        reflectionMemories: List<ScoredReflection>
    ): Int {
        return SYSTEM_PROMPT_TOKEN_ESTIMATE +
                shortTermMessages.sumOf { estimateTokens(it.content) } +
                longTermChatMessages.sumOf { estimateTokens(it.message.content) } +
                journalEntries.sumOf { TOKENS_PER_JOURNAL_ENTRY_ESTIMATE } +
                reflectionMemories.sumOf { TOKENS_PER_REFLECTION_ESTIMATE }
    }
    
    /**
     * Simple token estimation (~4 chars per token)
     */
    private fun estimateTokens(text: String): Int {
        return (text.length / 4).coerceAtLeast(1)
    }
    
    /**
     * Generate and save embedding for a chat message
     */
    suspend fun generateMessageEmbedding(message: ChatMessage): Result<Unit> = withContext(Dispatchers.Default) {
        try {
            val embedding = embeddingService.generateEmbedding(message.content).getOrThrow()
            chatEmbeddingRepository.saveEmbedding(
                message.id,
                message.sessionId,
                embedding,
                embeddingService.getModelName()
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate message embedding")
            Result.failure(e)
        }
    }
    
    /**
     * Generate embeddings for multiple messages (batch)
     */
    suspend fun generateMessageEmbeddings(messages: List<ChatMessage>): Result<Int> = withContext(Dispatchers.Default) {
        try {
            val texts = messages.map { it.content }
            val embeddings = embeddingService.generateEmbeddings(texts).getOrThrow()
            
            val toSave = messages.zip(embeddings).map { (message, embedding) ->
                Triple(message.id, message.sessionId, embedding)
            }
            
            chatEmbeddingRepository.saveEmbeddings(toSave, embeddingService.getModelName())
            Result.success(embeddings.size)
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate message embeddings")
            Result.failure(e)
        }
    }
    
    /**
     * Generate and save embedding for a reflection memory
     */
    suspend fun generateReflectionEmbedding(reflection: ReflectionMemory): Result<Unit> = withContext(Dispatchers.Default) {
        try {
            val embedding = embeddingService.generateEmbedding(reflection.content).getOrThrow()
            reflectionMemoryRepository.updateEmbedding(
                reflection.id,
                embedding,
                embeddingService.getModelName()
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate reflection embedding")
            Result.failure(e)
        }
    }
    
    /**
     * Search for similar messages across all chat history
     */
    suspend fun searchSimilarMessages(
        query: String,
        limit: Int = 5,
        minSimilarity: Float = 0.35f
    ): Result<List<ScoredChatMessage>> = withContext(Dispatchers.Default) {
        try {
            val queryEmbedding = embeddingService.generateEmbedding(query).getOrThrow()
            val allEmbeddings = chatEmbeddingRepository.getAllEmbeddings()
            
            val scored = allEmbeddings.mapNotNull { (messageId, embedding) ->
                val score = cosineSimilarity(queryEmbedding, embedding)
                if (score >= minSimilarity) messageId to score else null
            }
            
            val results = scored
                .sortedByDescending { it.second }
                .take(limit)
                .mapIndexedNotNull { index, (messageId, score) ->
                    chatMessageRepository.getMessageById(messageId)?.let { message ->
                        ScoredChatMessage(message, score, index + 1)
                    }
                }
            
            Result.success(results)
        } catch (e: Exception) {
            Timber.e(e, "Error searching similar messages")
            Result.failure(e)
        }
    }
    
    /**
     * Find messages that might need reflection generation
     */
    suspend fun getMessagesForReflection(
        sessionId: String,
        lastReflectionAt: java.time.LocalDateTime?,
        minMessages: Int = 10
    ): List<ChatMessage> {
        val messages = chatMessageRepository.getMessagesForSessionList(sessionId)
        
        return if (lastReflectionAt != null) {
            messages.filter { it.createdAt.isAfter(lastReflectionAt) }
        } else {
            messages
        }.filter { it.role != MessageRole.SYSTEM }
            .takeLast(minMessages * 2) // Take enough context
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
        
        return if (normA > 0 && normB > 0) {
            dotProduct / (sqrt(normA) * sqrt(normB))
        } else 0f
    }
}
