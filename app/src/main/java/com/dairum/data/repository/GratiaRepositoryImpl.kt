package com.dairum.data.repository

import com.dairum.data.local.dao.GratiaDao
import com.dairum.data.local.entity.*
import com.dairum.domain.model.*
import com.dairum.domain.repository.GratiaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GratiaRepositoryImpl @Inject constructor(
    private val gratiaDao: GratiaDao
) : GratiaRepository {

    // Gratitude Folders
    override fun getAllGratitudeFolders(): Flow<List<GratitudeFolder>> =
        gratiaDao.getAllGratitudeFolders().map { list -> list.map { it.toDomain() } }

    override suspend fun insertGratitudeFolder(folder: GratitudeFolder): Long =
        gratiaDao.insertGratitudeFolder(folder.toEntity())

    override suspend fun deleteGratitudeFolder(folder: GratitudeFolder) =
        gratiaDao.deleteGratitudeFolder(folder.toEntity())

    override suspend fun getGratitudeFolder(folderId: Long): GratitudeFolder? =
        gratiaDao.getGratitudeFolder(folderId)?.toDomain()

    // Gratitude Prompts
    override fun getPromptsByFolder(folderId: Long): Flow<List<GratitudePrompt>> =
        gratiaDao.getPromptsByFolder(folderId).map { list -> list.map { it.toDomain() } }

    override suspend fun getRandomPrompt(folderId: Long): GratitudePrompt? =
        gratiaDao.getRandomPrompt(folderId)?.toDomain()

    override suspend fun insertPrompt(prompt: GratitudePrompt): Long =
        gratiaDao.insertPrompt(prompt.toEntity())

    override suspend fun deletePrompt(prompt: GratitudePrompt) =
        gratiaDao.deletePrompt(prompt.toEntity())

    // Affirmation Folders
    override fun getAllAffirmationFolders(): Flow<List<AffirmationFolder>> =
        gratiaDao.getAllAffirmationFolders().map { list -> list.map { it.toDomain() } }

    override suspend fun insertAffirmationFolder(folder: AffirmationFolder): Long =
        gratiaDao.insertAffirmationFolder(folder.toEntity())

    override suspend fun updateAffirmationFolder(folder: AffirmationFolder) =
        gratiaDao.updateAffirmationFolder(folder.toEntity())

    override suspend fun deleteAffirmationFolder(folder: AffirmationFolder) =
        gratiaDao.deleteAffirmationFolder(folder.toEntity())

    override suspend fun getAffirmationFolder(folderId: Long): AffirmationFolder? =
        gratiaDao.getAffirmationFolder(folderId)?.toDomain()

    // Affirmations
    override fun getAffirmationsByFolder(folderId: Long): Flow<List<Affirmation>> =
        gratiaDao.getAffirmationsByFolder(folderId).map { list -> list.map { it.toDomain() } }

    override suspend fun getAffirmationsByFolderSync(folderId: Long): List<Affirmation> =
        gratiaDao.getAffirmationsByFolderSync(folderId).map { it.toDomain() }

    override suspend fun insertAffirmation(affirmation: Affirmation): Long =
        gratiaDao.insertAffirmation(affirmation.toEntity())

    override suspend fun updateAffirmation(affirmation: Affirmation) =
        gratiaDao.updateAffirmation(affirmation.toEntity())

    override suspend fun deleteAffirmation(affirmation: Affirmation) =
        gratiaDao.deleteAffirmation(affirmation.toEntity())
}

// Extension mappers
private fun GratitudeFolderEntity.toDomain() = GratitudeFolder(id, name, emoji, description, createdAt)
private fun GratitudeFolder.toEntity() = GratitudeFolderEntity(id, name, emoji, description, createdAt)
private fun GratitudePromptEntity.toDomain() = GratitudePrompt(id, folderId, prompt, createdAt)
private fun GratitudePrompt.toEntity() = GratitudePromptEntity(id, folderId, prompt, createdAt)
private fun AffirmationFolderEntity.toDomain() = AffirmationFolder(id, name, emoji, description, backgroundAudioUri, createdAt)
private fun AffirmationFolder.toEntity() = AffirmationFolderEntity(id, name, emoji, description, backgroundAudioUri, createdAt)
private fun AffirmationEntity.toDomain() = Affirmation(id, folderId, text, audioUri, useTts, sortOrder, createdAt)
private fun Affirmation.toEntity() = AffirmationEntity(id, folderId, text, audioUri, useTts, sortOrder, createdAt)
