package com.dairum.data.local.entity

import androidx.room.*
import java.time.LocalDateTime

/**
 * Entity for chat sessions with memory-aware conversations
 */
@Entity(
    tableName = "chat_sessions",
    indices = [
        Index(value = ["createdAt"]),
        Index(value = ["isSaved"]),
        Index(value = ["isArchived"])
    ]
)
data class ChatSessionEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val summary: String? = null,
    val isSaved: Boolean = false,
    val isArchived: Boolean = false,
    val messageCount: Int = 0,
    val lastMessageAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * Entity for individual chat messages within a session
 */
@Entity(
    tableName = "chat_messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["sessionId"]),
        Index(value = ["createdAt"]),
        Index(value = ["role"])
    ]
)
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: String,
    val role: String, // "user", "assistant", "system"
    val content: String,
    val tokensUsed: Int = 0,
    val generationTimeMs: Long = 0,
    val isEdited: Boolean = false,
    val metadata: String? = null, // JSON for additional context like sources
    val createdAt: LocalDateTime = LocalDateTime.now()
)

/**
 * Entity for storing embeddings of chat messages for semantic search
 */
@Entity(
    tableName = "chat_embeddings",
    foreignKeys = [
        ForeignKey(
            entity = ChatMessageEntity::class,
            parentColumns = ["id"],
            childColumns = ["messageId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["messageId"], unique = true),
        Index(value = ["sessionId"]),
        Index(value = ["model"])
    ]
)
data class ChatEmbeddingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val messageId: Long,
    val sessionId: String,
    val embedding: ByteArray, // Stored as byte array for efficiency
    val embeddingDimension: Int,
    val model: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ChatEmbeddingEntity
        return id == other.id && messageId == other.messageId
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + messageId.hashCode()
        return result
    }
    
    companion object {
        fun floatArrayToByteArray(floats: FloatArray): ByteArray {
            val bytes = ByteArray(floats.size * 4)
            for (i in floats.indices) {
                val bits = java.lang.Float.floatToIntBits(floats[i])
                bytes[i * 4] = (bits shr 24).toByte()
                bytes[i * 4 + 1] = (bits shr 16).toByte()
                bytes[i * 4 + 2] = (bits shr 8).toByte()
                bytes[i * 4 + 3] = bits.toByte()
            }
            return bytes
        }

        fun byteArrayToFloatArray(bytes: ByteArray): FloatArray {
            val floats = FloatArray(bytes.size / 4)
            for (i in floats.indices) {
                val bits = (bytes[i * 4].toInt() and 0xFF shl 24) or
                        (bytes[i * 4 + 1].toInt() and 0xFF shl 16) or
                        (bytes[i * 4 + 2].toInt() and 0xFF shl 8) or
                        (bytes[i * 4 + 3].toInt() and 0xFF)
                floats[i] = java.lang.Float.intBitsToFloat(bits)
            }
            return floats
        }
    }
}

/**
 * Entity for reflection memories - summarized insights about the user
 * Generated periodically based on conversation patterns
 */
@Entity(
    tableName = "reflection_memories",
    indices = [
        Index(value = ["category"]),
        Index(value = ["createdAt"]),
        Index(value = ["confidence"]),
        Index(value = ["isActive"])
    ]
)
data class ReflectionMemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String, // The reflection summary (e.g., "User values honesty and struggles with moral decisions")
    val category: String, // Category of reflection: PERSONALITY, INTEREST, CONCERN, VALUE, GOAL, HABIT, RELATIONSHIP
    val confidence: Float = 1.0f, // Confidence score 0-1
    val sourceMessageIds: String? = null, // JSON array of message IDs that contributed to this reflection
    val sourceSessionIds: String? = null, // JSON array of session IDs
    val occurrenceCount: Int = 1, // How many times this theme has appeared
    val lastReferenced: LocalDateTime? = null, // Last time this was retrieved for context
    val isActive: Boolean = true, // Whether to include in retrieval
    val embedding: ByteArray? = null, // Embedding for semantic search
    val embeddingModel: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ReflectionMemoryEntity
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

/**
 * Entity for tracking conversation context and continuity
 */
@Entity(
    tableName = "conversation_context",
    foreignKeys = [
        ForeignKey(
            entity = ChatSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["sessionId"], unique = true)
    ]
)
data class ConversationContextEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: String,
    val currentTopics: String? = null, // JSON array of current discussion topics
    val emotionalTone: String? = null, // Current emotional context
    val pendingQuestions: String? = null, // Questions AI wants to follow up on
    val userPreferences: String? = null, // Discovered user preferences in this conversation
    val discussionDepth: Int = 0, // How deep into a topic the conversation is
    val lastSummaryAt: LocalDateTime? = null, // Last time conversation was summarized
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * Relation class for session with all its messages
 * Named with "Relation" suffix to avoid conflict with domain model
 */
data class ChatSessionWithMessagesRelation(
    @Embedded val session: ChatSessionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val messages: List<ChatMessageEntity>
)
