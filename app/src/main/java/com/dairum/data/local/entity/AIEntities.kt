package com.dairum.data.local.entity

import androidx.room.*
import com.dairum.domain.model.*
import java.time.LocalDateTime

/**
 * Entity for storing AI model metadata
 */
@Entity(
    tableName = "ai_models",
    indices = [
        Index(value = ["modelType"]),
        Index(value = ["isDefault"])
    ]
)
data class AIModelEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val filePath: String,
    val sizeBytes: Long,
    val modelType: String, // LLM or EMBEDDING
    val quantization: String?,
    val contextLength: Int,
    val isDefault: Boolean,
    val isDownloaded: Boolean,
    val downloadUrl: String?,
    val createdAt: LocalDateTime,
    val lastUsedAt: LocalDateTime?
) {
    fun toDomain() = AIModel(
        id = id,
        name = name,
        description = description,
        filePath = filePath,
        sizeBytes = sizeBytes,
        modelType = ModelType.valueOf(modelType),
        quantization = quantization,
        contextLength = contextLength,
        isDefault = isDefault,
        isDownloaded = isDownloaded,
        downloadUrl = downloadUrl,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt
    )

    companion object {
        fun fromDomain(model: AIModel) = AIModelEntity(
            id = model.id,
            name = model.name,
            description = model.description,
            filePath = model.filePath,
            sizeBytes = model.sizeBytes,
            modelType = model.modelType.name,
            quantization = model.quantization,
            contextLength = model.contextLength,
            isDefault = model.isDefault,
            isDownloaded = model.isDownloaded,
            downloadUrl = model.downloadUrl,
            createdAt = model.createdAt,
            lastUsedAt = model.lastUsedAt
        )
    }
}

/**
 * Entity for storing journal entry embeddings
 */
@Entity(
    tableName = "journal_embeddings",
    foreignKeys = [
        ForeignKey(
            entity = JournalEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["entryId"], unique = true),
        Index(value = ["model"])
    ]
)
data class JournalEmbeddingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entryId: Long,
    val embedding: ByteArray, // Stored as byte array for efficiency
    val embeddingDimension: Int,
    val model: String,
    val version: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    fun toDomain(): JournalEmbedding {
        return JournalEmbedding(
            id = id,
            entryId = entryId,
            embedding = byteArrayToFloatArray(embedding),
            model = model,
            version = version,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as JournalEmbeddingEntity
        return id == other.id && entryId == other.entryId
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + entryId.hashCode()
        return result
    }

    companion object {
        fun fromDomain(embedding: JournalEmbedding) = JournalEmbeddingEntity(
            id = embedding.id,
            entryId = embedding.entryId,
            embedding = floatArrayToByteArray(embedding.embedding),
            embeddingDimension = embedding.embedding.size,
            model = embedding.model,
            version = embedding.version,
            createdAt = embedding.createdAt,
            updatedAt = embedding.updatedAt
        )

        private fun floatArrayToByteArray(floats: FloatArray): ByteArray {
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

        private fun byteArrayToFloatArray(bytes: ByteArray): FloatArray {
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
 * Entity for weekly reflections
 */
@Entity(
    tableName = "weekly_reflections",
    indices = [
        Index(value = ["weekStartDate"], unique = true)
    ]
)
data class WeeklyReflectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val weekStartDate: LocalDateTime,
    val weekEndDate: LocalDateTime,
    val summary: String,
    val averageMood: Float,
    val moodRangeMin: Int,
    val moodRangeMax: Int,
    val dominantMood: String,
    val moodDistributionJson: String, // JSON map
    val moodTrend: String, // TrendDirection name
    val keyThemesJson: String, // JSON list
    val highlightsJson: String, // JSON list
    val suggestionsJson: String, // JSON list
    val entryCount: Int,
    val totalWordCount: Int,
    val createdAt: LocalDateTime
)

/**
 * Entity for pattern results
 */
@Entity(
    tableName = "pattern_results",
    indices = [
        Index(value = ["patternType"]),
        Index(value = ["detectedAt"])
    ]
)
data class PatternResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val patternType: String, // PatternType name
    val description: String,
    val confidence: Float,
    val evidenceJson: String, // JSON list of PatternEvidence
    val insightsJson: String, // JSON list
    val detectedAt: LocalDateTime
)

/**
 * Entity for smart summaries
 */
@Entity(
    tableName = "smart_summaries",
    indices = [
        Index(value = ["summaryType"]),
        Index(value = ["periodStart", "periodEnd"])
    ]
)
data class SmartSummaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val summaryType: String, // SummaryType name
    val periodStart: LocalDateTime,
    val periodEnd: LocalDateTime,
    val title: String,
    val summary: String,
    val keyPointsJson: String, // JSON list
    val moodOverview: String?,
    val topTagsJson: String, // JSON list
    val topPeopleJson: String, // JSON list
    val wordCount: Int,
    val entryCount: Int,
    val createdAt: LocalDateTime
)

/**
 * Entity for AI chat history/conversation
 */
@Entity(
    tableName = "ai_conversations",
    indices = [
        Index(value = ["sessionId"]),
        Index(value = ["createdAt"])
    ]
)
data class AIConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: String,
    val role: String, // "user" or "assistant"
    val content: String,
    val sourceEntryIdsJson: String?, // JSON list of entry IDs used as context
    val tokensUsed: Int,
    val generationTimeMs: Long,
    val createdAt: LocalDateTime
)
