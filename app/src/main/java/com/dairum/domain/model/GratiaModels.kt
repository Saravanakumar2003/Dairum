package com.dairum.domain.model

data class GratitudeFolder(
    val id: Long = 0,
    val name: String,
    val emoji: String = "🙏",
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class GratitudePrompt(
    val id: Long = 0,
    val folderId: Long,
    val prompt: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class AffirmationFolder(
    val id: Long = 0,
    val name: String,
    val emoji: String = "✨",
    val description: String? = null,
    val backgroundAudioUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class Affirmation(
    val id: Long = 0,
    val folderId: Long,
    val text: String,
    val audioUri: String? = null,
    val useTts: Boolean = true,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
