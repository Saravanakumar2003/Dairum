package com.dairum.domain.repository

import com.dairum.domain.model.*
import kotlinx.coroutines.flow.Flow

interface GratiaRepository {
    // Gratitude
    fun getAllGratitudeFolders(): Flow<List<GratitudeFolder>>
    suspend fun insertGratitudeFolder(folder: GratitudeFolder): Long
    suspend fun deleteGratitudeFolder(folder: GratitudeFolder)
    fun getPromptsByFolder(folderId: Long): Flow<List<GratitudePrompt>>
    suspend fun getRandomPrompt(folderId: Long): GratitudePrompt?
    suspend fun insertPrompt(prompt: GratitudePrompt): Long
    suspend fun deletePrompt(prompt: GratitudePrompt)
    suspend fun getGratitudeFolder(folderId: Long): GratitudeFolder?

    // Affirmation
    fun getAllAffirmationFolders(): Flow<List<AffirmationFolder>>
    suspend fun insertAffirmationFolder(folder: AffirmationFolder): Long
    suspend fun updateAffirmationFolder(folder: AffirmationFolder)
    suspend fun deleteAffirmationFolder(folder: AffirmationFolder)
    fun getAffirmationsByFolder(folderId: Long): Flow<List<Affirmation>>
    suspend fun getAffirmationsByFolderSync(folderId: Long): List<Affirmation>
    suspend fun insertAffirmation(affirmation: Affirmation): Long
    suspend fun updateAffirmation(affirmation: Affirmation)
    suspend fun deleteAffirmation(affirmation: Affirmation)
    suspend fun getAffirmationFolder(folderId: Long): AffirmationFolder?
}
