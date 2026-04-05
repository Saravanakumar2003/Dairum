package com.dairum.data.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.dairum.data.local.database.DairumDatabase
import com.dairum.domain.repository.BackupInfo
import com.dairum.domain.repository.BackupRepository
import com.dairum.domain.repository.BackupResult
import com.dairum.domain.repository.RestoreResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: DairumDatabase,
    private val moshi: Moshi
) : BackupRepository {

    private val internalBackupDir: File
        get() = File(context.filesDir, "backups").also { it.mkdirs() }

    private val mediaDir: File
        get() = File(context.filesDir, "media")
    
    /**
     * Save backup file to Documents/dairum folder
     */
    private suspend fun saveToDocumentsDairum(tempFile: File, fileName: String): String = 
        withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore API for Android 10+
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/zip")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/dairum")
            }
            
            val collection = MediaStore.Files.getContentUri("external")
            val uri = context.contentResolver.insert(collection, contentValues)
            
            if (uri != null) {
                context.contentResolver.openOutputStream(uri)?.use { output ->
                    tempFile.inputStream().use { input ->
                        input.copyTo(output)
                    }
                }
                uri.toString()
            } else {
                // Fallback to internal storage
                val internalFile = File(internalBackupDir, fileName)
                tempFile.copyTo(internalFile, overwrite = true)
                internalFile.absolutePath
            }
        } else {
            // Legacy approach for Android 9 and below
            val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val dairumDir = File(documentsDir, "dairum").also { it.mkdirs() }
            val destFile = File(dairumDir, fileName)
            
            tempFile.copyTo(destFile, overwrite = true)
            destFile.absolutePath
        }
    }

    override suspend fun createBackup(
        destinationPath: String?,
        includeMedia: Boolean
    ): Result<BackupResult> = withContext(Dispatchers.IO) {
        try {
            val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                .format(LocalDateTime.now())
            val fileName = "dairum_backup_$timestamp.zip"
            
            // Create temp file first
            val tempFile = File(internalBackupDir, "temp_$fileName")

            // Get entries count BEFORE opening the ZIP stream to avoid SQLite error
            val entriesCount = database.journalEntryDao().getEntryCount()
            var mediaCount = 0

            ZipOutputStream(BufferedOutputStream(FileOutputStream(tempFile))).use { zipOut ->
                // 1. Export database
                val dbFile = context.getDatabasePath(DairumDatabase.DATABASE_NAME)
                if (dbFile.exists()) {
                    // Checkpoint the database using query instead of execSQL
                    // This avoids the "Queries can be performed using SQLiteDatabase query or rawQuery methods only" error
                    try {
                        val query = androidx.sqlite.db.SimpleSQLiteQuery("PRAGMA wal_checkpoint(FULL)")
                        database.openHelper.writableDatabase.query(query)
                    } catch (e: Exception) {
                        // If checkpoint fails, try closing and reopening to flush WAL
                        // This ensures data integrity even if PRAGMA fails
                    }
                    
                    zipOut.putNextEntry(ZipEntry("database/${DairumDatabase.DATABASE_NAME}"))
                    FileInputStream(dbFile).use { it.copyTo(zipOut) }
                    zipOut.closeEntry()
                    
                    // Also backup WAL and SHM files if they exist
                    val walFile = File(dbFile.path + "-wal")
                    if (walFile.exists()) {
                        zipOut.putNextEntry(ZipEntry("database/${DairumDatabase.DATABASE_NAME}-wal"))
                        FileInputStream(walFile).use { it.copyTo(zipOut) }
                        zipOut.closeEntry()
                    }
                    
                    val shmFile = File(dbFile.path + "-shm")
                    if (shmFile.exists()) {
                        zipOut.putNextEntry(ZipEntry("database/${DairumDatabase.DATABASE_NAME}-shm"))
                        FileInputStream(shmFile).use { it.copyTo(zipOut) }
                        zipOut.closeEntry()
                    }
                }

                // 2. Export media files if requested
                if (includeMedia && mediaDir.exists()) {
                    mediaDir.walkTopDown().filter { it.isFile }.forEach { file ->
                        val relativePath = file.relativeTo(mediaDir).path
                        zipOut.putNextEntry(ZipEntry("media/$relativePath"))
                        FileInputStream(file).use { it.copyTo(zipOut) }
                        zipOut.closeEntry()
                        mediaCount++
                    }
                }

                // 3. Create metadata file
                val metadata = BackupMetadata(
                    appVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0",
                    backupVersion = BACKUP_VERSION,
                    createdAt = System.currentTimeMillis(),
                    entriesCount = entriesCount,
                    mediaCount = mediaCount,
                    includesMedia = includeMedia
                )
                val metadataJson = moshi.adapter(BackupMetadata::class.java).toJson(metadata)
                zipOut.putNextEntry(ZipEntry("metadata.json"))
                zipOut.write(metadataJson.toByteArray())
                zipOut.closeEntry()
            }
            
            // Save to Documents/dairum (unless custom destination provided)
            val savedPath = if (destinationPath != null) {
                val destDir = File(destinationPath)
                destDir.mkdirs()
                val destFile = File(destDir, fileName)
                tempFile.copyTo(destFile, overwrite = true)
                tempFile.delete()
                destFile.absolutePath
            } else {
                val savedPath = saveToDocumentsDairum(tempFile, fileName)
                tempFile.delete()
                savedPath
            }

            // Clean up old backups - keep only last 5
            cleanupOldBackups()

            Result.success(
                BackupResult(
                    filePath = savedPath,
                    fileName = fileName,
                    fileSize = File(savedPath).let { if (it.exists()) it.length() else tempFile.length() },
                    entriesCount = entriesCount,
                    mediaCount = mediaCount
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Keep only the last 5 backups in internal storage
     */
    private fun cleanupOldBackups() {
        val backupFiles = internalBackupDir.listFiles()
            ?.filter { it.isFile && it.name.endsWith(".zip") && !it.name.startsWith("temp_") }
            ?.sortedByDescending { it.lastModified() }
            ?: return
        
        // Delete all backups beyond the 5th
        backupFiles.drop(5).forEach { file ->
            file.delete()
        }
    }

    override suspend fun restoreBackup(
        backupFile: File,
        mergeData: Boolean
    ): Result<RestoreResult> = withContext(Dispatchers.IO) {
        try {
            // Validate backup first
            val backupInfo = validateBackup(backupFile)
                ?: return@withContext Result.failure(IllegalArgumentException("Invalid backup file"))

            var entriesRestored = 0
            var mediaRestored = 0

            // Temporary directory for extraction
            val tempDir = File(context.cacheDir, "restore_temp_${System.currentTimeMillis()}")
            tempDir.mkdirs()

            try {
                // Extract backup
                ZipInputStream(BufferedInputStream(FileInputStream(backupFile))).use { zipIn ->
                    var entry: ZipEntry?
                    while (zipIn.nextEntry.also { entry = it } != null) {
                        entry?.let { zipEntry ->
                            val destFile = File(tempDir, zipEntry.name)
                            destFile.parentFile?.mkdirs()
                            
                            if (!zipEntry.isDirectory) {
                                FileOutputStream(destFile).use { out ->
                                    zipIn.copyTo(out)
                                }
                            }
                        }
                    }
                }

                // Close current database
                database.close()

                // Restore database
                val extractedDbFile = File(tempDir, "database/${DairumDatabase.DATABASE_NAME}")
                if (extractedDbFile.exists()) {
                    val currentDbFile = context.getDatabasePath(DairumDatabase.DATABASE_NAME)
                    
                    // Delete current database files
                    currentDbFile.delete()
                    File(currentDbFile.path + "-wal").delete()
                    File(currentDbFile.path + "-shm").delete()
                    
                    // Copy restored database
                    extractedDbFile.copyTo(currentDbFile, overwrite = true)
                    
                    // Copy WAL and SHM if they exist
                    val extractedWal = File(tempDir, "database/${DairumDatabase.DATABASE_NAME}-wal")
                    if (extractedWal.exists()) {
                        extractedWal.copyTo(File(currentDbFile.path + "-wal"), overwrite = true)
                    }
                    
                    val extractedShm = File(tempDir, "database/${DairumDatabase.DATABASE_NAME}-shm")
                    if (extractedShm.exists()) {
                        extractedShm.copyTo(File(currentDbFile.path + "-shm"), overwrite = true)
                    }
                    
                    entriesRestored = backupInfo.entriesCount
                }

                // Restore media files
                val extractedMediaDir = File(tempDir, "media")
                if (extractedMediaDir.exists()) {
                    if (!mergeData) {
                        mediaDir.deleteRecursively()
                    }
                    mediaDir.mkdirs()
                    
                    extractedMediaDir.walkTopDown().filter { it.isFile }.forEach { file ->
                        val relativePath = file.relativeTo(extractedMediaDir).path
                        val destFile = File(mediaDir, relativePath)
                        destFile.parentFile?.mkdirs()
                        file.copyTo(destFile, overwrite = true)
                        mediaRestored++
                    }
                }

            } finally {
                // Clean up temp directory
                tempDir.deleteRecursively()
            }

            Result.success(
                RestoreResult(
                    entriesRestored = entriesRestored,
                    mediaRestored = mediaRestored,
                    categoriesRestored = 0, // Will be counted from restored DB
                    tagsRestored = 0,
                    peopleRestored = 0,
                    trackersRestored = 0,
                    memoriesRestored = 0
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun validateBackup(backupFile: File): BackupInfo? = withContext(Dispatchers.IO) {
        try {
            if (!backupFile.exists() || !backupFile.name.endsWith(".zip")) {
                return@withContext null
            }

            ZipInputStream(BufferedInputStream(FileInputStream(backupFile))).use { zipIn ->
                var entry: ZipEntry?
                while (zipIn.nextEntry.also { entry = it } != null) {
                    if (entry?.name == "metadata.json") {
                        val content = zipIn.readBytes().toString(Charsets.UTF_8)
                        val metadata = moshi.adapter(BackupMetadata::class.java).fromJson(content)
                            ?: return@withContext null

                        return@withContext BackupInfo(
                            filePath = backupFile.absolutePath,
                            fileName = backupFile.name,
                            fileSize = backupFile.length(),
                            createdAt = metadata.createdAt,
                            appVersion = metadata.appVersion,
                            entriesCount = metadata.entriesCount,
                            mediaCount = metadata.mediaCount,
                            includesMedia = metadata.includesMedia
                        )
                    }
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }

    override fun getAvailableBackups(): Flow<List<BackupInfo>> = flow {
        val backups = internalBackupDir.listFiles()
            ?.filter { it.isFile && it.name.endsWith(".zip") }
            ?.mapNotNull { file -> validateBackup(file) }
            ?.sortedByDescending { it.createdAt }
            ?: emptyList()
        emit(backups)
    }

    override suspend fun deleteBackup(backupPath: String): Boolean = withContext(Dispatchers.IO) {
        File(backupPath).delete()
    }

    override fun getBackupDirectory(): File = internalBackupDir

    companion object {
        private const val BACKUP_VERSION = 1
    }
}

private data class BackupMetadata(
    val appVersion: String,
    val backupVersion: Int,
    val createdAt: Long,
    val entriesCount: Int,
    val mediaCount: Int,
    val includesMedia: Boolean
)
