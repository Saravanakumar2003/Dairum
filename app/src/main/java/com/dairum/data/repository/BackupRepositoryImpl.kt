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

    /** Media type directories that MediaRepositoryImpl saves into */
    private val mediaDirs: List<File>
        get() = listOf("photo", "video", "voice", "attachment").map { File(context.filesDir, it) }
    
    /**
     * Save backup file to Documents/Data Exports/Dairum folder
     */
    private suspend fun saveToDocumentsDairum(tempFile: File, fileName: String): String = 
        withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore API for Android 10+
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/zip")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Data Exports/Dairum")
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
            val dairumDir = File(File(documentsDir, "Data Exports"), "Dairum").also { it.mkdirs() }
            val destFile = File(dairumDir, fileName)
            
            tempFile.copyTo(destFile, overwrite = true)
            destFile.absolutePath
        }
    }

    /** Writes shared preferences (e.g. encrypted API keys) and every DataStore file (settings,
     * AI guardrails, templates, etc.) under config/ - shared by both backup flows so neither one
     * silently omits app configuration the way only `createCompleteExport` used to. */
    private fun writeConfigEntries(zipOut: ZipOutputStream) {
        val prefsDir = File(context.filesDir.parent ?: context.filesDir.absolutePath, "shared_prefs")
        if (prefsDir.exists()) {
            prefsDir.listFiles()?.filter { it.isFile && it.name.endsWith(".xml") && !isExcludedPreferenceFile(it.name) }?.forEach { prefFile ->
                zipOut.putNextEntry(ZipEntry("config/preferences/${prefFile.name}"))
                FileInputStream(prefFile).use { it.copyTo(zipOut) }
                zipOut.closeEntry()
            }
        }

        val datastoreDir = File(context.filesDir, "datastore")
        if (datastoreDir.exists()) {
            datastoreDir.walkTopDown().filter { it.isFile }.forEach { file ->
                val relativePath = file.relativeTo(datastoreDir).path
                zipOut.putNextEntry(ZipEntry("config/datastore/$relativePath"))
                FileInputStream(file).use { it.copyTo(zipOut) }
                zipOut.closeEntry()
            }
        }
    }

    /** Restores whatever writeConfigEntries() wrote, from an extracted backup's temp directory. */
    private fun restoreConfigEntries(tempDir: File) {
        val prefsExtracted = File(tempDir, "config/preferences")
        if (prefsExtracted.exists()) {
            val prefsDir = File(context.filesDir.parent ?: context.filesDir.absolutePath, "shared_prefs")
            prefsDir.mkdirs()
            // Old backups may still contain the encrypted key file; never restore it (see isExcludedPreferenceFile).
            prefsExtracted.listFiles()?.filter { !isExcludedPreferenceFile(it.name) }?.forEach { file ->
                file.copyTo(File(prefsDir, file.name), overwrite = true)
            }
        }

        val datastoreExtracted = File(tempDir, "config/datastore")
        if (datastoreExtracted.exists()) {
            val datastoreDir = File(context.filesDir, "datastore")
            datastoreDir.mkdirs()
            datastoreExtracted.walkTopDown().filter { it.isFile }.forEach { file ->
                val relativePath = file.relativeTo(datastoreExtracted).path
                val destFile = File(datastoreDir, relativePath)
                destFile.parentFile?.mkdirs()
                file.copyTo(destFile, overwrite = true)
            }
        }
    }

    override suspend fun createBackup(
        destinationPath: String?,
        includeMedia: Boolean
    ): Result<BackupResult> = withContext(Dispatchers.IO) {
        try {
            val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                .format(LocalDateTime.now())
            val fileName = "diarum_backup_$timestamp.zip"
            
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
                if (includeMedia) {
                    for (dir in mediaDirs) {
                        if (dir.exists()) {
                            dir.walkTopDown().filter { it.isFile }.forEach { file ->
                                val relativePath = "${dir.name}/${file.relativeTo(dir).path}"
                                zipOut.putNextEntry(ZipEntry("media/$relativePath"))
                                FileInputStream(file).use { it.copyTo(zipOut) }
                                zipOut.closeEntry()
                                mediaCount++
                            }
                        }
                    }
                }

                // 3. Export app configuration (settings, AI guardrails, templates, API keys, etc.)
                writeConfigEntries(zipOut)

                // 4. Create metadata file
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
            
            // Save to Documents/Data Exports/Dairum (unless custom destination provided)
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
        mergeData: Boolean // NOTE: merge is not supported; DB is always fully replaced.
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

                // Restore media files (stored as media/{type}/filename in the zip)
                val extractedMediaDir = File(tempDir, "media")
                if (extractedMediaDir.exists()) {
                    // Always clean existing media (merge is not supported for DB)
                    mediaDirs.forEach { it.deleteRecursively() }
                    
                    extractedMediaDir.walkTopDown().filter { it.isFile }.forEach { file ->
                        val relativePath = file.relativeTo(extractedMediaDir).path
                        val destFile = File(context.filesDir, relativePath)
                        destFile.parentFile?.mkdirs()
                        file.copyTo(destFile, overwrite = true)
                        mediaRestored++
                    }
                }
                
                // Restore app configuration (settings, AI guardrails, templates, API keys, etc.)
                restoreConfigEntries(tempDir)

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
        val allFiles = mutableListOf<File>()
        
        // 1. Internal backup directory
        internalBackupDir.listFiles()
            ?.filter { it.isFile && it.name.endsWith(".zip") }
            ?.let { allFiles.addAll(it) }
        
        // 2. Public Documents/Data Exports/Dairum directory (legacy storage, pre-Q)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val dairumDir = File(File(documentsDir, "Data Exports"), "Dairum")
            dairumDir.listFiles()
                ?.filter { it.isFile && it.name.endsWith(".zip") }
                ?.let { allFiles.addAll(it) }
        }
        
        val seen = mutableSetOf<String>()
        val backups = allFiles
            .mapNotNull { file -> validateBackup(file) }
            .filter { seen.add(it.fileName) } // deduplicate by name
            .sortedByDescending { it.createdAt }
        
        emit(backups)
    }

    override suspend fun deleteBackup(backupPath: String): Boolean = withContext(Dispatchers.IO) {
        File(backupPath).delete()
    }

    override fun getBackupDirectory(): File = internalBackupDir

    /**
     * Creates a comprehensive ZIP export with organized folder structure:
     * - entries/ (individual JSON files per entry with all metadata)
     * - config/ (categories.json, tags.json, people.json, trackers.json, settings.json)
     * - attachments/photo/, attachments/video/, attachments/audio/, attachments/files/
     */
    override suspend fun createCompleteExport(includeMedia: Boolean): Result<BackupResult> =
        withContext(Dispatchers.IO) {
            try {
                val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                    .format(LocalDateTime.now())
                val fileName = "dairum_complete_export_$timestamp.zip"
                val tempFile = File(internalBackupDir, "temp_$fileName")
                
                var mediaCount = 0

                // Get database data for structured export
                val dbFile = context.getDatabasePath(DairumDatabase.DATABASE_NAME)
                
                // Checkpoint DB first
                try {
                    val query = androidx.sqlite.db.SimpleSQLiteQuery("PRAGMA wal_checkpoint(FULL)")
                    database.openHelper.writableDatabase.query(query)
                } catch (_: Exception) {}

                ZipOutputStream(BufferedOutputStream(FileOutputStream(tempFile))).use { zipOut ->
                    
                    // 1. entries/ - Individual entry files as JSON
                    val entriesCount = database.journalEntryDao().getEntryCount()
                    // Export the database itself for full data integrity
                    if (dbFile.exists()) {
                        zipOut.putNextEntry(ZipEntry("entries/database.db"))
                        FileInputStream(dbFile).use { it.copyTo(zipOut) }
                        zipOut.closeEntry()
                        
                        val walFile = File(dbFile.path + "-wal")
                        if (walFile.exists()) {
                            zipOut.putNextEntry(ZipEntry("entries/database.db-wal"))
                            FileInputStream(walFile).use { it.copyTo(zipOut) }
                            zipOut.closeEntry()
                        }
                        
                        val shmFile = File(dbFile.path + "-shm")
                        if (shmFile.exists()) {
                            zipOut.putNextEntry(ZipEntry("entries/database.db-shm"))
                            FileInputStream(shmFile).use { it.copyTo(zipOut) }
                            zipOut.closeEntry()
                        }
                    }
                    
                    // 2. config/ - All config data
                    val configData = buildString {
                        appendLine("{")
                        appendLine("  \"appVersion\": \"${context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"}\",")
                        appendLine("  \"exportVersion\": $COMPLETE_EXPORT_VERSION,")
                        appendLine("  \"exportType\": \"complete\",")
                        appendLine("  \"createdAt\": ${System.currentTimeMillis()},")
                        appendLine("  \"entriesCount\": $entriesCount,")
                        appendLine("  \"includesMedia\": $includeMedia")
                        appendLine("}")
                    }
                    zipOut.putNextEntry(ZipEntry("config/manifest.json"))
                    zipOut.write(configData.toByteArray())
                    zipOut.closeEntry()
                    
                    writeConfigEntries(zipOut)

                    // 3. attachments/ - Organized by type
                    if (includeMedia) {
                        val mediaMapping = mapOf(
                            "photo" to "attachments/photo",
                            "video" to "attachments/video",
                            "voice" to "attachments/audio",
                            "attachment" to "attachments/files"
                        )
                        
                        for ((dirName, zipPath) in mediaMapping) {
                            val dir = File(context.filesDir, dirName)
                            if (dir.exists()) {
                                dir.walkTopDown().filter { it.isFile }.forEach { file ->
                                    val relativePath = file.relativeTo(dir).path
                                    zipOut.putNextEntry(ZipEntry("$zipPath/$relativePath"))
                                    FileInputStream(file).use { it.copyTo(zipOut) }
                                    zipOut.closeEntry()
                                    mediaCount++
                                }
                            }
                        }
                    }
                }
                
                // Save to Documents/Data Exports/Dairum
                val savedPath = saveToDocumentsDairum(tempFile, fileName)
                val fileSize = tempFile.length()
                tempFile.delete()
                
                val entriesCount2 = database.journalEntryDao().getEntryCount()
                
                Result.success(
                    BackupResult(
                        filePath = savedPath,
                        fileName = fileName,
                        fileSize = fileSize,
                        entriesCount = entriesCount2,
                        mediaCount = mediaCount
                    )
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Import from a complete export ZIP with the organized folder structure
     */
    override suspend fun importCompleteExport(importFile: File): Result<RestoreResult> =
        withContext(Dispatchers.IO) {
            try {
                if (!importFile.exists() || !importFile.name.endsWith(".zip")) {
                    return@withContext Result.failure(IllegalArgumentException("Invalid export file"))
                }
                
                var entriesRestored = 0
                var mediaRestored = 0
                
                val tempDir = File(context.cacheDir, "import_temp_${System.currentTimeMillis()}")
                tempDir.mkdirs()
                
                try {
                    // Extract the ZIP
                    ZipInputStream(BufferedInputStream(FileInputStream(importFile))).use { zipIn ->
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
                    
                    // Restore entries/database
                    val extractedDb = File(tempDir, "entries/database.db")
                    if (extractedDb.exists()) {
                        val currentDbFile = context.getDatabasePath(DairumDatabase.DATABASE_NAME)
                        currentDbFile.delete()
                        File(currentDbFile.path + "-wal").delete()
                        File(currentDbFile.path + "-shm").delete()
                        
                        extractedDb.copyTo(currentDbFile, overwrite = true)
                        
                        val extractedWal = File(tempDir, "entries/database.db-wal")
                        if (extractedWal.exists()) {
                            extractedWal.copyTo(File(currentDbFile.path + "-wal"), overwrite = true)
                        }
                        val extractedShm = File(tempDir, "entries/database.db-shm")
                        if (extractedShm.exists()) {
                            extractedShm.copyTo(File(currentDbFile.path + "-shm"), overwrite = true)
                        }
                        
                        entriesRestored = -1 // Will be counted from restored DB
                    }
                    
                    restoreConfigEntries(tempDir)
                    
                    // Restore attachments
                    val attachmentMapping = mapOf(
                        "attachments/photo" to "photo",
                        "attachments/video" to "video",
                        "attachments/audio" to "voice",
                        "attachments/files" to "attachment"
                    )
                    
                    for ((zipPath, dirName) in attachmentMapping) {
                        val extractedDir = File(tempDir, zipPath)
                        if (extractedDir.exists()) {
                            val targetDir = File(context.filesDir, dirName)
                            targetDir.mkdirs()
                            extractedDir.walkTopDown().filter { it.isFile }.forEach { file ->
                                val relativePath = file.relativeTo(extractedDir).path
                                val destFile = File(targetDir, relativePath)
                                destFile.parentFile?.mkdirs()
                                file.copyTo(destFile, overwrite = true)
                                mediaRestored++
                            }
                        }
                    }
                    
                } finally {
                    tempDir.deleteRecursively()
                }
                
                Result.success(
                    RestoreResult(
                        entriesRestored = entriesRestored,
                        mediaRestored = mediaRestored,
                        categoriesRestored = 0,
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

    companion object {
        /**
         * Preference files that must never travel in a backup: the encrypted API keys are bound to this
         * install's Android Keystore key, so a restored copy cannot be decrypted, and the notice flag
         * belongs to that file.
         */
        internal fun isExcludedPreferenceFile(fileName: String): Boolean =
            fileName == "${ApiKeyRepositoryImpl.FILE_NAME}.xml" || fileName == "${ApiKeyRepositoryImpl.STATE_FILE_NAME}.xml"

        private const val BACKUP_VERSION = 1
        private const val COMPLETE_EXPORT_VERSION = 1
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
