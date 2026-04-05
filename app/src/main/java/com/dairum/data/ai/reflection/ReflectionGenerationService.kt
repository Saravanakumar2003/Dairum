package com.dairum.data.ai.reflection

import com.dairum.data.ai.embedding.EmbeddingService
import com.dairum.data.ai.llm.LocalLLM
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for generating reflection memories from conversations.
 * 
 * Periodically analyzes conversations to extract lasting insights about the user,
 * such as personality traits, values, concerns, goals, and behavioral patterns.
 */
@Singleton
class ReflectionGenerationService @Inject constructor(
    private val localLLM: LocalLLM,
    private val embeddingService: EmbeddingService,
    private val reflectionMemoryRepository: ReflectionMemoryRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val conversationContextRepository: ConversationContextRepository
) {
    
    companion object {
        private const val TAG = "ReflectionGenerationService"
        
        // Number of messages between reflection generations
        const val REFLECTION_MESSAGE_INTERVAL = 12
        
        // Minimum messages needed for reflection
        const val MIN_MESSAGES_FOR_REFLECTION = 8
        
        // Maximum reflections to generate per session
        const val MAX_REFLECTIONS_PER_BATCH = 5
    }
    
    /**
     * Check if reflection generation is needed based on message count
     */
    suspend fun shouldGenerateReflection(sessionId: String): Boolean {
        val context = conversationContextRepository.getContextForSession(sessionId)
        val messageCount = chatMessageRepository.getMessageCountForSession(sessionId)
        
        if (messageCount < MIN_MESSAGES_FOR_REFLECTION) {
            return false
        }
        
        // Check if we've generated a reflection recently
        val lastSummaryAt = context?.lastSummaryAt
        if (lastSummaryAt != null) {
            val messagesSinceLastSummary = chatMessageRepository.getMessagesForSessionList(sessionId)
                .filter { it.createdAt.isAfter(lastSummaryAt) }
                .size
            return messagesSinceLastSummary >= REFLECTION_MESSAGE_INTERVAL
        }
        
        return true
    }
    
    /**
     * Generate reflections from recent conversation
     */
    suspend fun generateReflections(
        sessionId: String,
        forceGeneration: Boolean = false
    ): Result<List<ReflectionMemory>> = withContext(Dispatchers.Default) {
        try {
            if (!forceGeneration && !shouldGenerateReflection(sessionId)) {
                return@withContext Result.success(emptyList())
            }
            
            if (!localLLM.isModelLoaded()) {
                return@withContext Result.failure(Exception("LLM model not loaded"))
            }
            
            Timber.d("Generating reflections for session: $sessionId")
            
            // Get recent messages
            val context = conversationContextRepository.getContextForSession(sessionId)
            val messages = getMessagesForReflectionGeneration(sessionId, context?.lastSummaryAt)
            
            if (messages.size < MIN_MESSAGES_FOR_REFLECTION) {
                Timber.d("Not enough messages for reflection generation: ${messages.size}")
                return@withContext Result.success(emptyList())
            }
            
            // Build the reflection generation prompt
            val prompt = buildReflectionPrompt(messages)
            
            // Generate reflections
            val response = localLLM.generate(
                prompt = prompt,
                maxTokens = 400,
                temperature = 0.6f,
                topP = 0.9f,
                repeatPenalty = 1.1f
            ).getOrNull() ?: return@withContext Result.failure(Exception("LLM generation failed"))
            
            // Parse reflections from response
            val reflections = parseReflections(response, sessionId, messages.map { it.id })
            
            if (reflections.isNotEmpty()) {
                // Save reflections
                reflections.forEach { reflection ->
                    val id = reflectionMemoryRepository.saveReflection(reflection)
                    
                    // Generate embedding for the reflection
                    if (embeddingService.isModelLoaded()) {
                        try {
                            val embedding = embeddingService.generateEmbedding(reflection.content).getOrNull()
                            if (embedding != null) {
                                reflectionMemoryRepository.updateEmbedding(id, embedding, embeddingService.getModelName())
                            }
                        } catch (e: Exception) {
                            Timber.w(e, "Failed to generate reflection embedding")
                        }
                    }
                }
                
                // Update last summary time
                conversationContextRepository.updateLastSummaryTime(sessionId, LocalDateTime.now())
                
                Timber.d("Generated ${reflections.size} reflections")
            }
            
            Result.success(reflections)
        } catch (e: Exception) {
            Timber.e(e, "Error generating reflections")
            Result.failure(e)
        }
    }
    
    /**
     * Get messages to use for reflection generation
     */
    private suspend fun getMessagesForReflectionGeneration(
        sessionId: String,
        lastSummaryAt: LocalDateTime?
    ): List<ChatMessage> {
        val allMessages = chatMessageRepository.getMessagesForSessionList(sessionId)
            .filter { it.role != MessageRole.SYSTEM }
        
        return if (lastSummaryAt != null) {
            allMessages.filter { it.createdAt.isAfter(lastSummaryAt) }
        } else {
            allMessages.takeLast(REFLECTION_MESSAGE_INTERVAL * 2)
        }
    }
    
    /**
     * Build the prompt for reflection generation
     */
    private fun buildReflectionPrompt(messages: List<ChatMessage>): String {
        val conversationText = messages.joinToString("\n") { message ->
            val role = if (message.role == MessageRole.USER) "User" else "Assistant"
            "$role: ${message.content.take(300)}"
        }
        
        return """You are an insightful observer analyzing a conversation to identify lasting insights about the user.

Analyze this conversation and extract 1-3 brief, meaningful observations about the user. Focus on:
- Personality traits or characteristics
- Values and beliefs they hold
- Concerns or anxieties they express
- Goals or aspirations they mention
- Emotional patterns or triggers
- Intellectual interests
- Philosophical reflections on life

For each observation, provide:
1. CATEGORY: One of [PERSONALITY, VALUE, CONCERN, GOAL, EMOTIONAL, INTELLECTUAL, PHILOSOPHICAL, INTEREST, HABIT, RELATIONSHIP]
2. INSIGHT: A concise observation (maximum 100 characters)
3. CONFIDENCE: A number from 0.5 to 1.0 indicating how confident you are

Format each as:
[CATEGORY] (confidence) insight_text

Example outputs:
[VALUE] (0.85) User highly values honesty and transparency in relationships
[CONCERN] (0.7) User experiences stress related to work-life balance
[PHILOSOPHICAL] (0.9) User contemplates the meaning of success beyond material wealth

Recent Conversation:
$conversationText

Now extract meaningful insights (1-3):"""
    }
    
    /**
     * Parse reflections from LLM response
     */
    private fun parseReflections(
        response: String,
        sessionId: String,
        sourceMessageIds: List<Long>
    ): List<ReflectionMemory> {
        val reflections = mutableListOf<ReflectionMemory>()
        
        // Pattern to match: [CATEGORY] (confidence) insight_text
        val pattern = Regex("""\[(\w+)\]\s*\((\d+\.?\d*)\)\s*(.+?)(?=\[|\z)""", RegexOption.DOT_MATCHES_ALL)
        
        pattern.findAll(response).forEach { match ->
            try {
                val categoryStr = match.groupValues[1].uppercase().trim()
                val confidence = match.groupValues[2].toFloatOrNull()?.coerceIn(0.5f, 1.0f) ?: 0.7f
                val content = match.groupValues[3].trim()
                    .replace("\n", " ")
                    .take(150) // Limit to 150 chars
                
                if (content.length >= 20) { // Minimum meaningful content
                    val category = try {
                        ReflectionCategory.valueOf(categoryStr)
                    } catch (e: Exception) {
                        ReflectionCategory.PERSONALITY // Default
                    }
                    
                    reflections.add(ReflectionMemory(
                        content = content,
                        category = category,
                        confidence = confidence,
                        sourceMessageIds = sourceMessageIds,
                        sourceSessionIds = listOf(sessionId),
                        occurrenceCount = 1,
                        isActive = true,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    ))
                }
            } catch (e: Exception) {
                Timber.w(e, "Failed to parse reflection: ${match.value}")
            }
        }
        
        return reflections.take(MAX_REFLECTIONS_PER_BATCH)
    }
    
    /**
     * Merge similar reflections to avoid duplication
     */
    suspend fun consolidateReflections(): Result<Int> = withContext(Dispatchers.Default) {
        try {
            if (!embeddingService.isModelLoaded()) {
                return@withContext Result.success(0)
            }
            
            val reflections = reflectionMemoryRepository.getReflectionsWithEmbeddings()
            if (reflections.size < 2) {
                return@withContext Result.success(0)
            }
            
            var mergedCount = 0
            val processedIds = mutableSetOf<Long>()
            
            for (i in reflections.indices) {
                if (reflections[i].id in processedIds) continue
                
                val embedding1 = reflections[i].embedding ?: continue
                
                for (j in (i + 1) until reflections.size) {
                    if (reflections[j].id in processedIds) continue
                    
                    val embedding2 = reflections[j].embedding ?: continue
                    
                    val similarity = cosineSimilarity(embedding1, embedding2)
                    
                    // If very similar, merge them
                    if (similarity > 0.85f) {
                        // Keep the one with higher confidence/occurrence
                        val (keep, discard) = if (
                            reflections[i].confidence * reflections[i].occurrenceCount >
                            reflections[j].confidence * reflections[j].occurrenceCount
                        ) {
                            reflections[i] to reflections[j]
                        } else {
                            reflections[j] to reflections[i]
                        }
                        
                        // Update the kept reflection
                        reflectionMemoryRepository.incrementOccurrenceCount(keep.id)
                        
                        // Deactivate the discarded one
                        reflectionMemoryRepository.updateActiveStatus(discard.id, false)
                        processedIds.add(discard.id)
                        mergedCount++
                    }
                }
            }
            
            Timber.d("Consolidated $mergedCount similar reflections")
            Result.success(mergedCount)
        } catch (e: Exception) {
            Timber.e(e, "Error consolidating reflections")
            Result.failure(e)
        }
    }
    
    /**
     * Clean up weak or old reflections
     */
    suspend fun cleanupReflections(): Result<Int> = withContext(Dispatchers.Default) {
        try {
            // Delete reflections with low confidence that haven't been referenced much
            reflectionMemoryRepository.deleteWeakReflections(
                minConfidence = 0.4f,
                minOccurrences = 2
            )
            
            val remaining = reflectionMemoryRepository.getActiveReflectionCount()
            Timber.d("Cleanup complete, $remaining active reflections remaining")
            Result.success(remaining)
        } catch (e: Exception) {
            Timber.e(e, "Error cleaning up reflections")
            Result.failure(e)
        }
    }
    
    /**
     * Get a summary of reflection insights for a category
     */
    suspend fun getCategoryInsights(
        category: ReflectionCategory
    ): List<String> {
        return reflectionMemoryRepository.getReflectionsByCategoryList(category)
            .sortedByDescending { it.confidence * it.occurrenceCount }
            .take(5)
            .map { it.content }
    }
    
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
            dotProduct / (kotlin.math.sqrt(normA) * kotlin.math.sqrt(normB))
        } else 0f
    }
}
