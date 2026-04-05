package com.dairum.domain.model

import java.time.LocalDateTime

/**
 * Domain model for a chat session
 */
data class ChatSession(
    val id: String,
    val title: String,
    val summary: String? = null,
    val isSaved: Boolean = false,
    val isArchived: Boolean = false,
    val messageCount: Int = 0,
    val lastMessageAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    val displayTitle: String
        get() = title.ifBlank { "New Conversation" }
    
    val isActive: Boolean
        get() = !isArchived
}

/**
 * Domain model for a chat message
 */
data class ChatMessage(
    val id: Long = 0,
    val sessionId: String,
    val role: MessageRole,
    val content: String,
    val tokensUsed: Int = 0,
    val generationTimeMs: Long = 0,
    val isEdited: Boolean = false,
    val metadata: MessageMetadata? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

/**
 * Message role enum
 */
enum class MessageRole {
    USER,
    ASSISTANT,
    SYSTEM
}

/**
 * Metadata attached to messages
 */
data class MessageMetadata(
    val sourceEntryIds: List<Long> = emptyList(),
    val sourceReflectionIds: List<Long> = emptyList(),
    val relevanceScores: Map<Long, Float> = emptyMap(),
    val emotionalTone: String? = null,
    val conversationTopics: List<String> = emptyList()
)

/**
 * Domain model for reflection memory - insights about the user
 */
data class ReflectionMemory(
    val id: Long = 0,
    val content: String,
    val category: ReflectionCategory,
    val confidence: Float = 1.0f,
    val sourceMessageIds: List<Long> = emptyList(),
    val sourceSessionIds: List<String> = emptyList(),
    val occurrenceCount: Int = 1,
    val lastReferenced: LocalDateTime? = null,
    val isActive: Boolean = true,
    val embedding: FloatArray? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ReflectionMemory
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

/**
 * Categories for reflection memories
 */
enum class ReflectionCategory {
    PERSONALITY,   // Traits and characteristics
    INTEREST,      // Hobbies, topics of interest  
    CONCERN,       // Worries, anxieties, issues
    VALUE,         // Core values, beliefs
    GOAL,          // Aspirations, objectives
    HABIT,         // Behavioral patterns
    RELATIONSHIP,  // Social connections
    EMOTIONAL,     // Emotional patterns and triggers
    INTELLECTUAL,  // Thinking patterns, intellectual interests
    PHILOSOPHICAL  // Deep reflections on life, ethics, meaning
}

/**
 * Domain model for conversation context
 */
data class ConversationContext(
    val id: Long = 0,
    val sessionId: String,
    val currentTopics: List<String> = emptyList(),
    val emotionalTone: String? = null,
    val pendingQuestions: List<String> = emptyList(),
    val userPreferences: Map<String, String> = emptyMap(),
    val discussionDepth: Int = 0,
    val lastSummaryAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * Memory retrieval result combining different memory types
 */
data class MemoryRetrievalResult(
    val shortTermMessages: List<ChatMessage>,
    val longTermChatMessages: List<ScoredChatMessage>,
    val journalEntries: List<RetrievalResult>,
    val reflectionMemories: List<ScoredReflection>,
    val totalTokenEstimate: Int
)

/**
 * Scored chat message from semantic search
 */
data class ScoredChatMessage(
    val message: ChatMessage,
    val score: Float,
    val rank: Int
)

/**
 * Scored reflection memory from semantic search
 */
data class ScoredReflection(
    val reflection: ReflectionMemory,
    val score: Float,
    val rank: Int
)

/**
 * Prompt components built from memory retrieval
 */
data class ConversationPrompt(
    val systemInstruction: String,
    val recentConversation: String,
    val relevantMemories: String,
    val userQuestion: String,
    val totalTokens: Int
) {
    fun build(): String = buildString {
        appendLine(systemInstruction)
        appendLine()
        if (recentConversation.isNotBlank()) {
            appendLine("---")
            appendLine()
            appendLine("Recent Conversation:")
            appendLine(recentConversation)
            appendLine()
        }
        if (relevantMemories.isNotBlank()) {
            appendLine("---")
            appendLine()
            appendLine("Relevant Past Thoughts:")
            appendLine(relevantMemories)
            appendLine()
        }
        appendLine("---")
        appendLine()
        appendLine("User Question:")
        appendLine(userQuestion)
        appendLine()
        appendLine("Assistant:")
    }
}

/**
 * Configuration for memory retrieval
 */
data class MemoryRetrievalConfig(
    val shortTermLimit: Int = 5,
    val longTermChatLimit: Int = 3,
    val journalLimit: Int = 3,
    val reflectionLimit: Int = 2,
    val minSimilarity: Float = 0.35f,
    val maxTotalTokens: Int = 2048,
    val tokenReserveForGeneration: Int = 512
) {
    val availableContextTokens: Int
        get() = maxTotalTokens - tokenReserveForGeneration
}

/**
 * Reflection generation request
 */
data class ReflectionGenerationRequest(
    val sessionId: String? = null,
    val messageIds: List<Long> = emptyList(),
    val forceGeneration: Boolean = false
)

/**
 * Chat session with messages
 */
data class ChatSessionWithMessages(
    val session: ChatSession,
    val messages: List<ChatMessage>
)

/**
 * Export format options
 */
enum class ChatExportFormat {
    PLAIN_TEXT,
    MARKDOWN,
    PDF,
    JSON
}

/**
 * Export options for chat sessions
 */
data class ChatExportOptions(
    val format: ChatExportFormat = ChatExportFormat.PLAIN_TEXT,
    val includeMetadata: Boolean = false,
    val includeTimestamps: Boolean = true,
    val includeSystemMessages: Boolean = false
)

/**
 * Conversation statistics
 */
data class ConversationStats(
    val totalSessions: Int = 0,
    val savedSessions: Int = 0,
    val totalMessages: Int = 0,
    val totalReflections: Int = 0,
    val averageMessagesPerSession: Float = 0f,
    val mostDiscussedTopics: List<String> = emptyList()
)
