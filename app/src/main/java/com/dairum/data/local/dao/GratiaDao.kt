package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GratiaDao {
    // Gratitude Folders
    @Query("SELECT * FROM gratitude_folders ORDER BY createdAt DESC")
    fun getAllGratitudeFolders(): Flow<List<GratitudeFolderEntity>>

    @Query("SELECT * FROM gratitude_folders ORDER BY createdAt DESC")
    suspend fun getAllGratitudeFoldersSync(): List<GratitudeFolderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGratitudeFolder(folder: GratitudeFolderEntity): Long

    @Delete
    suspend fun deleteGratitudeFolder(folder: GratitudeFolderEntity)

    // Gratitude Prompts
    @Query("SELECT * FROM gratitude_prompts WHERE folderId = :folderId ORDER BY createdAt DESC")
    fun getPromptsByFolder(folderId: Long): Flow<List<GratitudePromptEntity>>

    @Query("SELECT * FROM gratitude_prompts WHERE folderId = :folderId ORDER BY createdAt DESC")
    suspend fun getPromptsByFolderSync(folderId: Long): List<GratitudePromptEntity>

    @Query("SELECT * FROM gratitude_prompts WHERE folderId = :folderId ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomPrompt(folderId: Long): GratitudePromptEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrompt(prompt: GratitudePromptEntity): Long

    @Delete
    suspend fun deletePrompt(prompt: GratitudePromptEntity)

    // Affirmation Folders
    @Query("SELECT * FROM affirmation_folders ORDER BY createdAt DESC")
    fun getAllAffirmationFolders(): Flow<List<AffirmationFolderEntity>>

    @Query("SELECT * FROM affirmation_folders ORDER BY createdAt DESC")
    suspend fun getAllAffirmationFoldersSync(): List<AffirmationFolderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAffirmationFolder(folder: AffirmationFolderEntity): Long

    @Update
    suspend fun updateAffirmationFolder(folder: AffirmationFolderEntity)

    @Delete
    suspend fun deleteAffirmationFolder(folder: AffirmationFolderEntity)

    // Affirmations
    @Query("SELECT * FROM affirmations WHERE folderId = :folderId ORDER BY sortOrder ASC")
    fun getAffirmationsByFolder(folderId: Long): Flow<List<AffirmationEntity>>

    @Query("SELECT * FROM affirmations WHERE folderId = :folderId ORDER BY sortOrder ASC")
    suspend fun getAffirmationsByFolderSync(folderId: Long): List<AffirmationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAffirmation(affirmation: AffirmationEntity): Long

    @Update
    suspend fun updateAffirmation(affirmation: AffirmationEntity)

    @Delete
    suspend fun deleteAffirmation(affirmation: AffirmationEntity)

    @Query("SELECT * FROM affirmation_folders WHERE id = :folderId")
    suspend fun getAffirmationFolder(folderId: Long): AffirmationFolderEntity?

    @Query("SELECT * FROM gratitude_folders WHERE id = :folderId")
    suspend fun getGratitudeFolder(folderId: Long): GratitudeFolderEntity?

    // Stats queries
    @Query("SELECT COUNT(*) FROM gratitude_folders")
    suspend fun getGratitudeFolderCount(): Int

    @Query("SELECT COUNT(*) FROM gratitude_prompts")
    suspend fun getGratitudePromptCount(): Int

    @Query("SELECT COUNT(*) FROM affirmation_folders")
    suspend fun getAffirmationFolderCount(): Int

    @Query("SELECT COUNT(*) FROM affirmations")
    suspend fun getAffirmationCount(): Int
}
