package com.dairum.domain.repository

import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * Repository for backup and restore functionality
 */
interface BackupRepository {
    
    /**
     * Creates a full backup of the app data
     * @param destinationPath Optional custom path for backup
     * @param includeMedia Whether to include media files in backup
     * @return Result with backup file path on success
     */
    suspend fun createBackup(
        destinationPath: String? = null,
        includeMedia: Boolean = true
    ): Result<BackupResult>
    
    /**
     * Restores app data from a backup file
     * @param backupFile The backup file to restore from
     * @param mergeData Whether to merge with existing data (true) or replace (false)
     * @return Result with restore summary on success
     */
    suspend fun restoreBackup(
        backupFile: File,
        mergeData: Boolean = false
    ): Result<RestoreResult>
    
    /**
     * Validates a backup file
     * @return BackupInfo if valid, null otherwise
     */
    suspend fun validateBackup(backupFile: File): BackupInfo?
    
    /**
     * Gets list of available backups
     */
    fun getAvailableBackups(): Flow<List<BackupInfo>>
    
    /**
     * Deletes a backup file
     */
    suspend fun deleteBackup(backupPath: String): Boolean
    
    /**
     * Gets the backup directory
     */
    fun getBackupDirectory(): File
}

data class BackupResult(
    val filePath: String,
    val fileName: String,
    val fileSize: Long,
    val entriesCount: Int,
    val mediaCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class RestoreResult(
    val entriesRestored: Int,
    val mediaRestored: Int,
    val categoriesRestored: Int,
    val tagsRestored: Int,
    val peopleRestored: Int,
    val trackersRestored: Int,
    val memoriesRestored: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class BackupInfo(
    val filePath: String,
    val fileName: String,
    val fileSize: Long,
    val createdAt: Long,
    val appVersion: String,
    val entriesCount: Int,
    val mediaCount: Int,
    val includesMedia: Boolean
)
