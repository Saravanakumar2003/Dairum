package com.dairum.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "gratitude_folders")
data class GratitudeFolderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val emoji: String = "🙏",
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "gratitude_prompts",
    foreignKeys = [
        ForeignKey(
            entity = GratitudeFolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["folderId"])]
)
data class GratitudePromptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val folderId: Long,
    val prompt: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "affirmation_folders")
data class AffirmationFolderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val emoji: String = "✨",
    val description: String? = null,
    val backgroundAudioUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "affirmations",
    foreignKeys = [
        ForeignKey(
            entity = AffirmationFolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["folderId"])]
)
data class AffirmationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val folderId: Long,
    val text: String,
    val audioUri: String? = null,
    val useTts: Boolean = true,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
