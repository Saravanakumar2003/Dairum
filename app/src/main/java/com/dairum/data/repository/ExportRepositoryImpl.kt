package com.dairum.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.print.PrintAttributes
import android.print.pdf.PrintedPdfDocument
import android.provider.MediaStore
import android.text.Html
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Base64
import com.dairum.data.local.dao.CategoryDao
import com.dairum.data.local.dao.ExportDao
import com.dairum.data.local.dao.GratiaDao
import com.dairum.data.local.dao.JournalEntryDao
import com.dairum.data.local.dao.MediaDao
import com.dairum.data.local.dao.MemoryDao
import com.dairum.data.local.dao.NotificationDao
import com.dairum.data.local.dao.PersonDao
import com.dairum.data.local.dao.TagDao
import com.dairum.data.local.dao.TrackerDao
import com.dairum.data.local.entity.ExportConfigEntity
import com.dairum.data.local.entity.ExportHistoryEntity
import com.dairum.domain.model.ExportConfig
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.model.ExportHistory
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.MediaType
import com.dairum.BuildConfig
import com.dairum.data.export.KithMetadataJson
import com.dairum.domain.repository.ExportRepository
import com.dairum.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ExportRepositoryImpl @Inject constructor(
    private val exportDao: ExportDao,
    private val categoryDao: CategoryDao,
    private val tagDao: TagDao,
    private val personDao: PersonDao,
    private val trackerDao: TrackerDao,
    private val mediaDao: MediaDao,
    private val memoryDao: MemoryDao,
    private val notificationDao: NotificationDao,
    private val gratiaDao: GratiaDao,
    private val journalEntryDao: JournalEntryDao,
    private val settingsRepository: SettingsRepository,
    private val clock: Clock,
    @ApplicationContext private val context: Context
) : ExportRepository {

    private val internalExportDir: File
        get() = File(context.filesDir, "exports").also { it.mkdirs() }
    
    /** Media type directories that MediaRepositoryImpl saves into */
    private val mediaDirs: List<File>
        get() = listOf("photo", "video", "voice", "attachment").map { File(context.filesDir, it) }
    
    /**
     * Save a file to Documents/Data Exports/Dairum folder using MediaStore for Android 10+
     * or legacy file system approach for older versions.
     * Returns the file path or URI string.
     */
    private suspend fun saveToDocumentsDiarum(tempFile: File, fileName: String, mimeType: String): String = 
        withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore API for Android 10+
            try {
                // First, delete any existing file with the same name to avoid conflicts
                val existingUri = findExistingFile(fileName)
                if (existingUri != null) {
                    try {
                        context.contentResolver.delete(existingUri, null, null)
                    } catch (e: Exception) {
                        // Ignore deletion errors, file may not exist
                    }
                }
                
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Data Exports/Dairum")
                    // Set IS_PENDING to write atomically
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.MediaColumns.IS_PENDING, 1)
                    }
                }
                
                val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                val uri = context.contentResolver.insert(collection, contentValues)
                
                if (uri != null) {
                    context.contentResolver.openOutputStream(uri)?.use { output ->
                        tempFile.inputStream().use { input ->
                            input.copyTo(output)
                        }
                    }
                    
                    // Clear IS_PENDING flag to make file visible
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val updateValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.IS_PENDING, 0)
                        }
                        context.contentResolver.update(uri, updateValues, null, null)
                    }
                    
                    // Return the URI string for the file
                    return@withContext uri.toString()
                } else {
                    // Fallback to internal storage
                    val internalFile = File(internalExportDir, fileName)
                    tempFile.copyTo(internalFile, overwrite = true)
                    return@withContext internalFile.absolutePath
                }
            } catch (e: Exception) {
                // Log the error and fallback to internal storage
                android.util.Log.e("ExportRepositoryImpl", "Failed to save to Documents/Data Exports/Dairum: ${e.message}", e)
                val internalFile = File(internalExportDir, fileName)
                tempFile.copyTo(internalFile, overwrite = true)
                return@withContext internalFile.absolutePath
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
    
    /**
     * Find an existing file in Documents/Data Exports/Dairum by name
     */
    private fun findExistingFile(fileName: String): Uri? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return null
        
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(fileName, Environment.DIRECTORY_DOCUMENTS + "/Data Exports/Dairum/")
        
        context.contentResolver.query(
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                return MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY, id)
            }
        }
        return null
    }

    override suspend fun createConfig(config: ExportConfig): Long {
        return exportDao.insertConfig(config.toEntity())
    }

    override suspend fun updateConfig(config: ExportConfig) {
        exportDao.updateConfig(config.toEntity())
    }

    override suspend fun deleteConfig(id: Long) {
        exportDao.deleteConfig(ExportConfigEntity(id = id, format = com.dairum.data.local.entity.ExportFormat.TXT))
    }

    override suspend fun getExportConfig(): ExportConfig? {
        return exportDao.getAllConfigs().firstOrNull()?.toDomain()
    }

    override fun getAllConfigsFlow(): Flow<List<ExportConfig>> {
        return exportDao.getAllConfigsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getEnabledConfigs(): List<ExportConfig> {
        return exportDao.getEnabledConfigs().map { it.toDomain() }
    }

    override suspend fun toggleConfigEnabled(id: Long, isEnabled: Boolean) {
        exportDao.updateConfigEnabled(id, isEnabled)
    }

    override suspend fun exportEntries(
        entries: List<JournalEntry>,
        format: ExportFormat,
        includeMedia: Boolean
    ): Result<ExportHistory> {
        return when (format) {
            ExportFormat.TXT -> exportToTxt(entries).map { filePath ->
                createHistoryEntry(filePath, entries.size, format)
            }
            ExportFormat.PDF -> exportToPdf(entries, includeMedia).map { filePath ->
                createHistoryEntry(filePath, entries.size, format)
            }
            ExportFormat.JSON -> exportToJson(entries).map { filePath ->
                createHistoryEntry(filePath, entries.size, format)
            }
            ExportFormat.CSV -> exportToCsv(entries).map { filePath ->
                createHistoryEntry(filePath, entries.size, format)
            }
            // A full snapshot of its own: it ignores `entries` and uses the saved "Include titles" option.
            ExportFormat.KITH_METADATA -> exportKithMetadata(settingsRepository.getKithIncludeTitlesFlow().first())
        }
    }

    override suspend fun exportKithMetadata(includeTitles: Boolean): Result<ExportHistory> =
        writeKithMetadata(includeTitles).map { (pathWithSize, count) ->
            createHistoryEntry(pathWithSize, count, ExportFormat.KITH_METADATA)
        }

    /** Writes the metadata file through MediaStore like the other exports. Returns ("path|size", entry count). */
    private suspend fun writeKithMetadata(includeTitles: Boolean): Result<Pair<String, Int>> =
        withContext(Dispatchers.IO) {
            try {
                val rows = journalEntryDao.getRowsForMetadataExport()
                val json = KithMetadataJson.render(
                    rows = rows,
                    includeTitles = includeTitles,
                    exportedAtEpochMs = clock.millis(),
                    zone = ZoneId.systemDefault(),
                    appVersion = BuildConfig.VERSION_NAME
                )
                val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now())
                val fileName = "dairum_metadata_$timestamp.json"
                val tempFile = File(internalExportDir, "temp_$fileName")
                tempFile.writeText(json, Charsets.UTF_8)
                val fileSize = tempFile.length()
                val savedPath = saveToDocumentsDiarum(tempFile, fileName, "application/json")
                tempFile.delete()
                Result.success("$savedPath|$fileSize" to rows.size)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun exportToTxt(entries: List<JournalEntry>): Result<String> = 
        withContext(Dispatchers.IO) {
        try {
            val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                .format(LocalDateTime.now())
            val fileName = "diarum_export_$timestamp.txt"
            
            // Write to temp file first
            val tempFile = File(internalExportDir, "temp_$fileName")
            
            FileOutputStream(tempFile).bufferedWriter().use { writer ->
                entries.forEach { entry ->
                    writer.appendLine("=" .repeat(60))
                    writer.appendLine()
                    
                    // Date
                    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")
                    writer.appendLine(entry.createdAt.format(dateFormatter))
                    writer.appendLine()
                    
                    // Title
                    entry.title?.let {
                        writer.appendLine(it)
                        writer.appendLine("-".repeat(it.length))
                        writer.appendLine()
                    }
                    
                    // Content
                    if (entry.plainContent.isNotBlank()) {
                        writer.appendLine(entry.plainContent)
                        writer.appendLine()
                    }
                    
                    // Metadata
                    val metadata = mutableListOf<String>()
                    
                    entry.mood?.let { metadata.add("Mood: ${it.label} ${it.emoji}") }
                    entry.category?.let { metadata.add("Category: ${it.name}") }
                    if (entry.tags.isNotEmpty()) {
                        metadata.add("Tags: ${entry.tags.joinToString(", ") { it.name }}")
                    }
                    if (entry.people.isNotEmpty()) {
                        metadata.add("People: ${entry.people.joinToString(", ") { it.name }}")
                    }
                    entry.weather?.let { 
                        metadata.add("Weather: ${it.condition}, ${it.temperature}°C")
                    }
                    entry.location?.let { loc ->
                        val locationStr = if (!loc.placeName.isNullOrBlank()) {
                            "Location: ${loc.placeName}"
                        } else if (!loc.address.isNullOrBlank()) {
                            "Location: ${loc.address}"
                        } else {
                            "Location: ${loc.latitude}, ${loc.longitude}"
                        }
                        metadata.add(locationStr)
                        // Also add GPS coordinates if we have a place name or address
                        if (!loc.placeName.isNullOrBlank() || !loc.address.isNullOrBlank()) {
                            metadata.add("GPS: ${loc.latitude}, ${loc.longitude}")
                        }
                    }
                    
                    if (metadata.isNotEmpty()) {
                        writer.appendLine()
                        metadata.forEach { writer.appendLine("• $it") }
                    }
                    
                    writer.appendLine()
                }
            }
            
            // Get file size before moving
            val fileSize = tempFile.length()
            
            // Save to Documents/Data Exports/Dairum
            val savedPath = saveToDocumentsDiarum(tempFile, fileName, "text/plain")
            
            // Store the file size in a temporary way (append to path with separator)
            // We'll parse this in createHistoryEntry
            val pathWithSize = "$savedPath|$fileSize"
            
            // Cleanup temp file
            tempFile.delete()
            
            Result.success(pathWithSize)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun exportToJson(entries: List<JournalEntry>): Result<String> =
        withContext(Dispatchers.IO) {
        try {
            val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                .format(LocalDateTime.now())
            val fileName = "dairum_export_$timestamp.json"
            val tempFile = File(internalExportDir, "temp_$fileName")

            val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

            // Fetch all DB data for complete export
            val allCategories = categoryDao.getAll()
            val allTags = tagDao.getAll()
            val allPeople = personDao.getAll()
            val allTrackers = trackerDao.getAllTrackers()
            val allMemories = memoryDao.getAllMemoriesSync()
            val allMemoryEntryLinks = memoryDao.getAllMemoryEntryLinks()
            val allReminderConfigs = notificationDao.getAllReminderConfigs()
            val allSmartReminders = notificationDao.getAllSmartReminders()
            val allGratitudeFolders = gratiaDao.getAllGratitudeFoldersSync()
            val allAffirmationFolders = gratiaDao.getAllAffirmationFoldersSync()

            FileOutputStream(tempFile).bufferedWriter().use { writer ->
                writer.appendLine("{")
                writer.appendLine("  \"exportedAt\": \"${LocalDateTime.now().format(dateFormatter)}\",")
                writer.appendLine("  \"version\": 1,")

                // ===== ENTRIES =====
                writer.appendLine("  \"entries\": [")
                entries.forEachIndexed { index, entry ->
                    // Get media and tracker values for this entry
                    val entryMedia = mediaDao.getMediaForEntry(entry.id)
                    val entryTrackerValues = trackerDao.getValuesForEntry(entry.id)

                    val json = buildString {
                        append("    {\n")
                        append("      \"id\": ${entry.id},\n")
                        append("      \"title\": ${escapeJson(entry.title)},\n")
                        append("      \"content\": ${escapeJson(entry.plainContent)},\n")
                        append("      \"formattingJson\": ${escapeJson(entry.formattingJson)},\n")
                        append("      \"createdAt\": \"${entry.createdAt.format(dateFormatter)}\",\n")
                        append("      \"updatedAt\": \"${entry.updatedAt.format(dateFormatter)}\",\n")
                        append("      \"wordCount\": ${entry.wordCount},\n")
                        append("      \"isFavorite\": ${entry.isFavorite},\n")
                        append("      \"isArchived\": ${entry.isArchived},\n")
                        append("      \"isDraft\": ${entry.isDraft},\n")
                        append("      \"isPinned\": ${entry.isPinned},\n")
                        append("      \"backgroundColor\": ${escapeJson(entry.backgroundColor)},\n")

                        // Mood
                        if (entry.mood != null) {
                            append("      \"mood\": { \"label\": ${escapeJson(entry.mood.label)}, \"emoji\": ${escapeJson(entry.mood.emoji)}, \"level\": ${entry.mood.level} },\n")
                        } else {
                            append("      \"mood\": null,\n")
                        }

                        // Category
                        if (entry.category != null) {
                            append("      \"category\": { \"id\": ${entry.category.id}, \"name\": ${escapeJson(entry.category.name)} },\n")
                        } else {
                            append("      \"category\": null,\n")
                        }

                        // Tags
                        val tagsStr = entry.tags.joinToString(", ") { "{ \"id\": ${it.id}, \"name\": ${escapeJson(it.name)} }" }
                        append("      \"tags\": [$tagsStr],\n")

                        // People
                        val peopleStr = entry.people.joinToString(", ") { "{ \"id\": ${it.id}, \"name\": ${escapeJson(it.name)} }" }
                        append("      \"people\": [$peopleStr],\n")

                        // Weather
                        if (entry.weather != null) {
                            append("      \"weather\": { \"condition\": ${escapeJson(entry.weather.condition)}, \"temperature\": ${entry.weather.temperature}, \"temperatureFahrenheit\": ${entry.weather.temperatureFahrenheit}, \"humidity\": ${entry.weather.humidity}, \"feelsLike\": ${entry.weather.feelsLike}, \"icon\": ${escapeJson(entry.weather.icon)}, \"locationName\": ${escapeJson(entry.weather.locationName)} },\n")
                        } else {
                            append("      \"weather\": null,\n")
                        }

                        // Location
                        if (entry.location != null) {
                            append("      \"location\": { \"latitude\": ${entry.location.latitude}, \"longitude\": ${entry.location.longitude}, \"address\": ${escapeJson(entry.location.address)}, \"placeName\": ${escapeJson(entry.location.placeName)}, \"placeId\": ${escapeJson(entry.location.placeId)} },\n")
                        } else {
                            append("      \"location\": null,\n")
                        }

                        // Tracker Values
                        val trackerValuesStr = entryTrackerValues.joinToString(", ") {
                            "{ \"trackerId\": ${it.trackerId}, \"value\": ${escapeJson(it.value)} }"
                        }
                        append("      \"trackerValues\": [$trackerValuesStr],\n")

                        // Media
                        val mediaStr = entryMedia.joinToString(", ") { m ->
                            buildString {
                                append("{ \"id\": ${m.id}")
                                append(", \"type\": ${escapeJson(m.type.name)}")
                                append(", \"uri\": ${escapeJson(m.uri)}")
                                append(", \"fileName\": ${escapeJson(m.fileName)}")
                                append(", \"mimeType\": ${escapeJson(m.mimeType)}")
                                append(", \"sizeBytes\": ${m.sizeBytes}")
                                m.duration?.let { append(", \"duration\": $it") }
                                m.width?.let { append(", \"width\": $it") }
                                m.height?.let { append(", \"height\": $it") }
                                m.caption?.let { append(", \"caption\": ${escapeJson(it)}") }
                                append(", \"sortOrder\": ${m.sortOrder}")
                                append(" }")
                            }
                        }
                        append("      \"media\": [$mediaStr]\n")
                        append("    }")
                    }
                    writer.append(json)
                    if (index < entries.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ],")

                // ===== CATEGORIES =====
                writer.appendLine("  \"categories\": [")
                allCategories.forEachIndexed { index, cat ->
                    writer.append("    { \"id\": ${cat.id}, \"name\": ${escapeJson(cat.name)}, \"color\": ${cat.color}, \"icon\": ${escapeJson(cat.icon)}, \"sortOrder\": ${cat.sortOrder}, \"isDefault\": ${cat.isDefault} }")
                    if (index < allCategories.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ],")

                // ===== TAGS =====
                writer.appendLine("  \"tags\": [")
                allTags.forEachIndexed { index, tag ->
                    writer.append("    { \"id\": ${tag.id}, \"name\": ${escapeJson(tag.name)}, \"color\": ${tag.color}, \"usageCount\": ${tag.usageCount} }")
                    if (index < allTags.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ],")

                // ===== PEOPLE =====
                writer.appendLine("  \"people\": [")
                allPeople.forEachIndexed { index, person ->
                    val personJson = buildString {
                        append("    { \"id\": ${person.id}")
                        append(", \"name\": ${escapeJson(person.name)}")
                        append(", \"relationship\": ${escapeJson(person.relationship)}")
                        append(", \"photoUri\": ${escapeJson(person.photoUri)}")
                        append(", \"notes\": ${escapeJson(person.notes)}")
                        append(", \"email\": ${escapeJson(person.email)}")
                        append(", \"phone\": ${escapeJson(person.phone)}")
                        person.birthday?.let { append(", \"birthday\": $it") }
                        append(", \"importantDates\": ${person.importantDates ?: "null"}")
                        append(", \"isFavorite\": ${person.isFavorite}")
                        append(", \"mentionCount\": ${person.mentionCount}")
                        append(" }")
                    }
                    writer.append(personJson)
                    if (index < allPeople.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ],")

                // ===== CUSTOM TRACKERS =====
                writer.appendLine("  \"customTrackers\": [")
                allTrackers.forEachIndexed { index, tracker ->
                    val trackerJson = buildString {
                        append("    { \"id\": ${tracker.id}")
                        append(", \"name\": ${escapeJson(tracker.name)}")
                        append(", \"type\": ${escapeJson(tracker.type.name)}")
                        append(", \"icon\": ${escapeJson(tracker.icon)}")
                        append(", \"unit\": ${escapeJson(tracker.unit)}")
                        tracker.minValue?.let { append(", \"minValue\": $it") }
                        tracker.maxValue?.let { append(", \"maxValue\": $it") }
                        append(", \"options\": ${tracker.options ?: "null"}")
                        append(", \"isBuiltIn\": ${tracker.isBuiltIn}")
                        append(", \"isActive\": ${tracker.isActive}")
                        append(", \"sortOrder\": ${tracker.sortOrder}")
                        append(" }")
                    }
                    writer.append(trackerJson)
                    if (index < allTrackers.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ],")

                // ===== MEMORIES =====
                writer.appendLine("  \"memories\": [")
                allMemories.forEachIndexed { index, memory ->
                    val milestones = memoryDao.getMilestonesForMemorySync(memory.id)
                    val memTrackers = memoryDao.getTrackersForMemorySync(memory.id)
                    val entryLinks = allMemoryEntryLinks.filter { it.memoryId == memory.id }

                    val memoryJson = buildString {
                        append("    {\n")
                        append("      \"id\": ${memory.id},\n")
                        append("      \"title\": ${escapeJson(memory.title)},\n")
                        append("      \"description\": ${escapeJson(memory.description)},\n")
                        append("      \"coverImageUri\": ${escapeJson(memory.coverImageUri)},\n")
                        append("      \"startDate\": ${memory.startDate},\n")
                        append("      \"endDate\": ${memory.endDate},\n")
                        append("      \"location\": ${escapeJson(memory.location)},\n")
                        append("      \"locationData\": ${memory.locationData ?: "null"},\n")
                        append("      \"isFavorite\": ${memory.isFavorite},\n")
                        append("      \"color\": ${memory.color},\n")
                        append("      \"icon\": ${escapeJson(memory.icon)},\n")
                        append("      \"tags\": ${escapeJson(memory.tags)},\n")
                        append("      \"moodSummary\": ${memory.moodSummary ?: "null"},\n")
                        append("      \"totalTimeSpent\": ${memory.totalTimeSpent},\n")
                        append("      \"entryCount\": ${memory.entryCount},\n")
                        append("      \"wordCount\": ${memory.wordCount},\n")
                        append("      \"photoCount\": ${memory.photoCount},\n")
                        append("      \"videoCount\": ${memory.videoCount},\n")
                        append("      \"peopleInvolved\": ${peopleIdsJson(memory.peopleInvolved)},\n")
                        append("      \"highlights\": ${memory.highlights ?: "null"},\n")
                        append("      \"trackerSummary\": ${memory.trackerSummary ?: "null"},\n")

                        // Entry links
                        val linksStr = entryLinks.joinToString(", ") { "{ \"entryId\": ${it.entryId}, \"sortOrder\": ${it.sortOrder} }" }
                        append("      \"entryLinks\": [$linksStr],\n")

                        // Milestones
                        val milestonesStr = milestones.joinToString(", ") { ms ->
                            "{ \"id\": ${ms.id}, \"title\": ${escapeJson(ms.title)}, \"description\": ${escapeJson(ms.description)}, \"milestoneDate\": ${ms.milestoneDate}, \"relatedEntryId\": ${ms.relatedEntryId}, \"icon\": ${escapeJson(ms.icon)}, \"sortOrder\": ${ms.sortOrder} }"
                        }
                        append("      \"milestones\": [$milestonesStr],\n")

                        // Memory Trackers
                        val memTrackersStr = memTrackers.joinToString(", ") { mt ->
                            "{ \"id\": ${mt.id}, \"name\": ${escapeJson(mt.name)}, \"type\": ${escapeJson(mt.type.name)}, \"icon\": ${escapeJson(mt.icon)}, \"unit\": ${escapeJson(mt.unit)}, \"goalValue\": ${escapeJson(mt.goalValue)}, \"currentValue\": ${escapeJson(mt.currentValue)}, \"progress\": ${mt.progress} }"
                        }
                        append("      \"trackers\": [$memTrackersStr]\n")
                        append("    }")
                    }
                    writer.append(memoryJson)
                    if (index < allMemories.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ],")

                // ===== REMINDER CONFIGS =====
                writer.appendLine("  \"reminderConfigs\": [")
                allReminderConfigs.forEachIndexed { index, rc ->
                    val rcJson = buildString {
                        append("    { \"id\": ${rc.id}")
                        append(", \"type\": ${escapeJson(rc.type.name)}")
                        append(", \"name\": ${escapeJson(rc.name)}")
                        append(", \"description\": ${escapeJson(rc.description)}")
                        append(", \"isEnabled\": ${rc.isEnabled}")
                        append(", \"scheduleHour\": ${rc.scheduleHour}")
                        append(", \"scheduleMinute\": ${rc.scheduleMinute}")
                        append(", \"scheduleDays\": ${escapeJson(rc.scheduleDays)}")
                        append(", \"notificationTitle\": ${escapeJson(rc.notificationTitle)}")
                        append(", \"notificationMessage\": ${escapeJson(rc.notificationMessage)}")
                        append(", \"priority\": ${escapeJson(rc.priority.name)}")
                        append(" }")
                    }
                    writer.append(rcJson)
                    if (index < allReminderConfigs.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ],")

                // ===== SMART REMINDERS =====
                writer.appendLine("  \"smartReminders\": [")
                allSmartReminders.forEachIndexed { index, sr ->
                    val srJson = buildString {
                        append("    { \"id\": ${sr.id}")
                        append(", \"reminderType\": ${escapeJson(sr.reminderType.name)}")
                        append(", \"title\": ${escapeJson(sr.title)}")
                        append(", \"description\": ${escapeJson(sr.description)}")
                        append(", \"reminderDate\": ${sr.reminderDate}")
                        append(", \"repeatYearly\": ${sr.repeatYearly}")
                        append(", \"relatedPersonId\": ${sr.relatedPersonId}")
                        append(", \"relatedEntryId\": ${sr.relatedEntryId}")
                        append(", \"isEnabled\": ${sr.isEnabled}")
                        append(", \"notifyDaysBefore\": ${sr.notifyDaysBefore}")
                        sr.lastNotifiedAt?.let { append(", \"lastNotifiedAt\": $it") }
                        append(", \"metadata\": ${sr.metadata ?: "null"}")
                        append(" }")
                    }
                    writer.append(srJson)
                    if (index < allSmartReminders.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ],")

                // ===== GRATITUDE FOLDERS (with prompts) =====
                writer.appendLine("  \"gratitudeFolders\": [")
                allGratitudeFolders.forEachIndexed { index, folder ->
                    val prompts = gratiaDao.getPromptsByFolderSync(folder.id)
                    val promptsStr = prompts.joinToString(", ") { p ->
                        "{ \"id\": ${p.id}, \"prompt\": ${escapeJson(p.prompt)} }"
                    }
                    writer.append("    { \"id\": ${folder.id}, \"name\": ${escapeJson(folder.name)}, \"emoji\": ${escapeJson(folder.emoji)}, \"description\": ${escapeJson(folder.description)}, \"prompts\": [$promptsStr] }")
                    if (index < allGratitudeFolders.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ],")

                // ===== AFFIRMATION FOLDERS (with affirmations) =====
                writer.appendLine("  \"affirmationFolders\": [")
                allAffirmationFolders.forEachIndexed { index, folder ->
                    val affirmations = gratiaDao.getAffirmationsByFolderSync(folder.id)
                    val affirmationsStr = affirmations.joinToString(", ") { a ->
                        "{ \"id\": ${a.id}, \"text\": ${escapeJson(a.text)}, \"audioUri\": ${escapeJson(a.audioUri)}, \"useTts\": ${a.useTts}, \"sortOrder\": ${a.sortOrder} }"
                    }
                    writer.append("    { \"id\": ${folder.id}, \"name\": ${escapeJson(folder.name)}, \"emoji\": ${escapeJson(folder.emoji)}, \"description\": ${escapeJson(folder.description)}, \"backgroundAudioUri\": ${escapeJson(folder.backgroundAudioUri)}, \"affirmations\": [$affirmationsStr] }")
                    if (index < allAffirmationFolders.size - 1) writer.append(",")
                    writer.appendLine()
                }
                writer.appendLine("  ]")

                writer.appendLine("}")
            }

            val fileSize = tempFile.length()
            val savedPath = saveToDocumentsDiarum(tempFile, fileName, "application/json")
            val pathWithSize = "$savedPath|$fileSize"
            tempFile.delete()

            Result.success(pathWithSize)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun exportToCsv(entries: List<JournalEntry>): Result<String> =
        withContext(Dispatchers.IO) {
        try {
            val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                .format(LocalDateTime.now())
            val fileName = "diarum_export_$timestamp.csv"
            val tempFile = File(internalExportDir, "temp_$fileName")

            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

            FileOutputStream(tempFile).bufferedWriter().use { writer ->
                // CSV Header
                writer.appendLine("\"ID\",\"Title\",\"Content\",\"Created At\",\"Updated At\",\"Category\",\"Mood\",\"Mood Emoji\",\"Tags\",\"People\",\"Weather\",\"Temperature (°C)\",\"Location\",\"Latitude\",\"Longitude\",\"Word Count\",\"Favorite\",\"Archived\",\"Draft\",\"Pinned\",\"Background Color\"")

                entries.forEach { entry ->
                    val row = buildString {
                        append(entry.id)
                        append(",")
                        append(escapeCsv(entry.title ?: ""))
                        append(",")
                        append(escapeCsv(entry.plainContent))
                        append(",")
                        append(escapeCsv(entry.createdAt.format(dateFormatter)))
                        append(",")
                        append(escapeCsv(entry.updatedAt.format(dateFormatter)))
                        append(",")
                        append(escapeCsv(entry.category?.name ?: ""))
                        append(",")
                        append(escapeCsv(entry.mood?.label ?: ""))
                        append(",")
                        append(escapeCsv(entry.mood?.emoji ?: ""))
                        append(",")
                        append(escapeCsv(entry.tags.joinToString("; ") { it.name }))
                        append(",")
                        append(escapeCsv(entry.people.joinToString("; ") { it.name }))
                        append(",")
                        append(escapeCsv(entry.weather?.condition ?: ""))
                        append(",")
                        append(entry.weather?.temperature ?: "")
                        append(",")
                        append(escapeCsv(entry.location?.placeName ?: entry.location?.address ?: ""))
                        append(",")
                        append(entry.location?.latitude ?: "")
                        append(",")
                        append(entry.location?.longitude ?: "")
                        append(",")
                        append(entry.wordCount)
                        append(",")
                        append(entry.isFavorite)
                        append(",")
                        append(entry.isArchived)
                        append(",")
                        append(entry.isDraft)
                        append(",")
                        append(entry.isPinned)
                        append(",")
                        append(escapeCsv(entry.backgroundColor ?: ""))
                    }
                    writer.appendLine(row)
                }
            }

            val fileSize = tempFile.length()
            val savedPath = saveToDocumentsDiarum(tempFile, fileName, "text/csv")
            val pathWithSize = "$savedPath|$fileSize"
            tempFile.delete()

            Result.success(pathWithSize)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * memories.peopleInvolved is stored as a comma-separated id list ("1,4"). It used to be written
     * into the JSON verbatim, which is not valid JSON for two or more people. Always a JSON array now
     * ("[]" when empty); a "[1,4]" stored form is accepted too.
     */
    private fun peopleIdsJson(raw: String?): String =
        Regex("-?\\d+").findAll(raw.orEmpty()).map { it.value }.joinToString(prefix = "[", postfix = "]", separator = ",")

    private fun escapeJson(value: String?): String {
        if (value == null) return "null"
        val escaped = value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
        return "\"$escaped\""
    }

    private fun escapeCsv(value: String): String {
        return if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            "\"$value\""
        }
    }

    override suspend fun exportToPdf(entries: List<JournalEntry>, includeMedia: Boolean): Result<String> = 
        withContext(Dispatchers.IO) {
        try {
            val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                .format(LocalDateTime.now())
            val fileName = "diarum_export_$timestamp.pdf"
            val tempFile = File(internalExportDir, "temp_$fileName")
            
            // Create PDF document
            val pageWidth = 595 // A4 width in points
            val pageHeight = 842 // A4 height in points
            val margin = 40f
            val contentWidth = pageWidth - (margin * 2)
            
            val document = PdfDocument()
            var pageNumber = 1
            var currentY = margin
            var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            var page = document.startPage(pageInfo)
            var canvas = page.canvas
            
            val titlePaint = TextPaint().apply {
                textSize = 24f
                isFakeBoldText = true
                color = android.graphics.Color.parseColor("#1a1a1a")
            }
            
            val datePaint = TextPaint().apply {
                textSize = 12f
                color = android.graphics.Color.parseColor("#666666")
            }
            
            val headingPaint = TextPaint().apply {
                textSize = 16f
                isFakeBoldText = true
                color = android.graphics.Color.parseColor("#333333")
            }
            
            val bodyPaint = TextPaint().apply {
                textSize = 11f
                color = android.graphics.Color.parseColor("#333333")
            }
            
            val valuePaint = TextPaint().apply {
                textSize = 10f
                color = android.graphics.Color.parseColor("#333333")
            }
            
            fun startNewPage(): Canvas {
                document.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                page = document.startPage(pageInfo)
                currentY = margin
                return page.canvas
            }
            
            fun checkPageSpace(neededHeight: Float): Canvas {
                if (currentY + neededHeight > pageHeight - margin) {
                    return startNewPage()
                }
                return canvas
            }
            
            // Header
            val headerText = "Dairum Journal Export"
            canvas.drawText(headerText, margin, currentY + 24f, titlePaint)
            currentY += 40f
            
            val exportDateStr = "Exported on ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"))}"
            canvas.drawText(exportDateStr, margin, currentY, datePaint)
            currentY += 8f
            
            canvas.drawText("${entries.size} journal entries", margin, currentY + 12f, datePaint)
            currentY += 40f
            
            // Draw separator line
            val linePaint = android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#e0e0e0")
                strokeWidth = 1f
            }
            canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
            currentY += 20f
            
            // Process each entry
            entries.sortedByDescending { it.createdAt }.forEach { entry ->
                // Estimate space needed for this entry (minimum)
                canvas = checkPageSpace(150f)
                
                // Entry date header
                val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
                val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
                val dateStr = entry.createdAt.format(dateFormatter)
                val timeStr = entry.createdAt.format(timeFormatter)
                
                canvas.drawText("$dateStr at $timeStr", margin, currentY + 12f, datePaint)
                currentY += 20f
                
                // Title
                if (!entry.title.isNullOrBlank()) {
                    val titleLayout = StaticLayout.Builder.obtain(
                        entry.title, 0, entry.title.length, headingPaint,
                        contentWidth.toInt()
                    ).build()
                    
                    canvas.save()
                    canvas.translate(margin, currentY)
                    titleLayout.draw(canvas)
                    canvas.restore()
                    currentY += titleLayout.height + 8f
                }
                
                // Mood (if present)
                entry.mood?.let { mood ->
                    canvas = checkPageSpace(30f)
                    canvas.drawText("Mood: ${mood.emoji} ${mood.label}", margin, currentY + 14f, valuePaint)
                    currentY += 24f
                }
                
                // Weather (if present)
                entry.weather?.let { weather ->
                    canvas = checkPageSpace(30f)
                    val weatherStr = "Weather: ${weather.condition}, ${weather.temperature.toInt()}°C (feels like ${weather.feelsLike.toInt()}°C), ${weather.humidity}% humidity"
                    canvas.drawText(weatherStr, margin, currentY + 10f, valuePaint)
                    currentY += 20f
                }
                
                // Location (if present)
                entry.location?.let { location ->
                    canvas = checkPageSpace(30f)
                    val locationStr = "Location: ${location.placeName ?: location.address ?: "${location.latitude}, ${location.longitude}"}"
                    canvas.drawText(locationStr, margin, currentY + 10f, valuePaint)
                    currentY += 20f
                }
                
                // Content
                if (entry.plainContent.isNotBlank()) {
                    canvas = checkPageSpace(50f)
                    currentY += 8f
                    
                    val contentLayout = StaticLayout.Builder.obtain(
                        entry.plainContent, 0, entry.plainContent.length, bodyPaint,
                        contentWidth.toInt()
                    ).setLineSpacing(4f, 1f).build()
                    
                    // Handle multi-page content
                    var contentDrawn = 0
                    val lines = contentLayout.lineCount
                    
                    while (contentDrawn < lines) {
                        val availableHeight = pageHeight - margin - currentY
                        var linesToDraw = 0
                        var heightUsed = 0f
                        
                        for (i in contentDrawn until lines) {
                            val lineBottom = contentLayout.getLineBottom(i).toFloat() - 
                                (if (contentDrawn > 0) contentLayout.getLineTop(contentDrawn).toFloat() else 0f)
                            if (lineBottom <= availableHeight) {
                                linesToDraw++
                                heightUsed = lineBottom
                            } else {
                                break
                            }
                        }
                        
                        if (linesToDraw > 0) {
                            canvas.save()
                            canvas.clipRect(margin, currentY, pageWidth - margin, currentY + heightUsed)
                            canvas.translate(margin, currentY - (if (contentDrawn > 0) contentLayout.getLineTop(contentDrawn).toFloat() else 0f))
                            contentLayout.draw(canvas)
                            canvas.restore()
                            
                            contentDrawn += linesToDraw
                            currentY += heightUsed
                        }
                        
                        if (contentDrawn < lines) {
                            canvas = startNewPage()
                        }
                    }
                    
                    currentY += 8f
                }
                
                // Photos (if include media and PDF format)
                if (includeMedia && entry.media.isNotEmpty()) {
                    val photos = entry.media.filter { it.type == MediaType.PHOTO }
                    if (photos.isNotEmpty()) {
                        canvas = checkPageSpace(150f)
                        currentY += 8f
                        
                        photos.take(4).forEachIndexed { index, media ->
                            try {
                                // Handle both absolute paths and file:// URIs
                                val imagePath = if (media.uri.startsWith("/")) {
                                    media.uri  // Already an absolute path
                                } else if (media.uri.startsWith("file://")) {
                                    Uri.parse(media.uri).path ?: return@forEachIndexed
                                } else if (media.uri.startsWith("content://")) {
                                    // For content URIs, try to open via ContentResolver
                                    val bitmap = context.contentResolver.openInputStream(Uri.parse(media.uri))?.use { inputStream ->
                                        val options = BitmapFactory.Options().apply {
                                            inSampleSize = 4
                                        }
                                        BitmapFactory.decodeStream(inputStream, null, options)
                                    }
                                    if (bitmap != null) {
                                        canvas = checkPageSpace(150f)
                                        
                                        val maxWidth = contentWidth / 2 - 5
                                        val maxHeight = 120f
                                        val scale = minOf(maxWidth / bitmap.width, maxHeight / bitmap.height)
                                        val scaledWidth = bitmap.width * scale
                                        val scaledHeight = bitmap.height * scale
                                        
                                        val xOffset = if (index % 2 == 1) contentWidth / 2 + 5 else 0f
                                        
                                        val scaled = Bitmap.createScaledBitmap(
                                            bitmap,
                                            scaledWidth.toInt(),
                                            scaledHeight.toInt(),
                                            true
                                        )
                                        canvas.drawBitmap(scaled, margin + xOffset, currentY, null)
                                        
                                        if (index % 2 == 1 || index == photos.lastIndex) {
                                            currentY += scaledHeight + 8f
                                        }
                                        
                                        bitmap.recycle()
                                        scaled.recycle()
                                    }
                                    return@forEachIndexed // Already handled
                                } else {
                                    media.uri // Try as-is
                                }
                                
                                val imageFile = File(imagePath)
                                if (imageFile.exists()) {
                                    val options = BitmapFactory.Options().apply {
                                        inSampleSize = 4 // Scale down for PDF
                                    }
                                    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
                                    if (bitmap != null) {
                                        canvas = checkPageSpace(150f)
                                        
                                        val maxWidth = contentWidth / 2 - 5
                                        val maxHeight = 120f
                                        val scale = minOf(maxWidth / bitmap.width, maxHeight / bitmap.height)
                                        val scaledWidth = bitmap.width * scale
                                        val scaledHeight = bitmap.height * scale
                                        
                                        val xOffset = if (index % 2 == 1) contentWidth / 2 + 5 else 0f
                                        
                                        val scaled = Bitmap.createScaledBitmap(
                                            bitmap,
                                            scaledWidth.toInt(),
                                            scaledHeight.toInt(),
                                            true
                                        )
                                        canvas.drawBitmap(scaled, margin + xOffset, currentY, null)
                                        
                                        if (index % 2 == 1 || index == photos.lastIndex) {
                                            currentY += scaledHeight + 8f
                                        }
                                        
                                        bitmap.recycle()
                                        scaled.recycle()
                                    }
                                }
                            } catch (e: Exception) {
                                // Log error but continue
                                e.printStackTrace()
                            }
                        }
                    }
                }
                
                // Tags (with text wrapping)
                if (entry.tags.isNotEmpty()) {
                    canvas = checkPageSpace(40f)
                    val tagsStr = "Tags: ${entry.tags.joinToString(", ") { "#${it.name}" }}"
                    val tagsLayout = StaticLayout.Builder.obtain(
                        tagsStr, 0, tagsStr.length, valuePaint, contentWidth.toInt()
                    ).build()
                    
                    canvas.save()
                    canvas.translate(margin, currentY)
                    tagsLayout.draw(canvas)
                    canvas.restore()
                    currentY += tagsLayout.height + 8f
                }
                
                // People (with text wrapping)
                if (entry.people.isNotEmpty()) {
                    canvas = checkPageSpace(40f)
                    val peopleStr = "With: ${entry.people.joinToString(", ") { it.name }}"
                    val peopleLayout = StaticLayout.Builder.obtain(
                        peopleStr, 0, peopleStr.length, valuePaint, contentWidth.toInt()
                    ).build()
                    
                    canvas.save()
                    canvas.translate(margin, currentY)
                    peopleLayout.draw(canvas)
                    canvas.restore()
                    currentY += peopleLayout.height + 8f
                }
                
                // Category
                entry.category?.let { category ->
                    canvas = checkPageSpace(30f)
                    canvas.drawText("Category: ${category.name}", margin, currentY + 10f, valuePaint)
                    currentY += 20f
                }
                
                // Trackers (with proper formatting and text wrapping)
                if (entry.trackerValues.isNotEmpty()) {
                    canvas = checkPageSpace(50f)
                    val trackerItems = entry.trackerValues.entries.mapNotNull { (key, value) ->
                        if (value.isNotBlank()) "$key: $value" else null
                    }
                    if (trackerItems.isNotEmpty()) {
                        val trackersStr = "Trackers: ${trackerItems.joinToString(" | ")}"
                        val trackersLayout = StaticLayout.Builder.obtain(
                            trackersStr, 0, trackersStr.length, valuePaint, contentWidth.toInt()
                        ).build()
                        
                        canvas.save()
                        canvas.translate(margin, currentY)
                        trackersLayout.draw(canvas)
                        canvas.restore()
                        currentY += trackersLayout.height + 8f
                    }
                }
                
                // Entry separator
                currentY += 10f
                canvas = checkPageSpace(20f)
                canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
                currentY += 25f
            }
            
            // Finish last page
            document.finishPage(page)
            
            // Write to temp file
            FileOutputStream(tempFile).use { out ->
                document.writeTo(out)
            }
            document.close()
            
            // Get file size before moving
            val fileSize = tempFile.length()
            
            // Save to Documents/Data Exports/Dairum
            val savedPath = saveToDocumentsDiarum(tempFile, fileName, "application/pdf")
            
            // Store the file size in a temporary way (append to path with separator)
            val pathWithSize = "$savedPath|$fileSize"
            
            // Cleanup temp file
            tempFile.delete()
            
            Result.success(pathWithSize)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun triggerImmediateExport() {
        // TODO: Trigger WorkManager for immediate export
        // This would use WorkManager to schedule an immediate one-time export
    }

    override fun getExportHistoryFlow(): Flow<List<ExportHistory>> {
        return exportDao.getAllHistoryFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getRecentHistory(limit: Int): List<ExportHistory> {
        return exportDao.getRecentHistory(limit).map { it.toDomain() }
    }

    override suspend fun deleteHistory(id: Long) {
        exportDao.deleteHistoryById(id)
    }

    override suspend fun clearOldHistory(beforeTimestamp: Long) {
        exportDao.deleteHistoryBefore(beforeTimestamp)
    }

    private suspend fun createHistoryEntry(
        pathWithSize: String,
        entriesCount: Int,
        format: ExportFormat
    ): ExportHistory {
        // Parse path and file size (format: "path|size")
        val parts = pathWithSize.split("|")
        val filePath = parts[0]
        val fileSize = parts.getOrNull(1)?.toLongOrNull() ?: 0L
        
        // Extract filename from path or URI
        val fileName = if (filePath.startsWith("content://")) {
            // For content URIs, extract the last path segment
            Uri.parse(filePath).lastPathSegment ?: "export"
        } else {
            File(filePath).name
        }
        
        val entity = ExportHistoryEntity(
            format = format.toEntity(),
            filePath = filePath,
            fileName = fileName,
            entriesCount = entriesCount,
            fileSize = fileSize,
            isSuccess = true
        )
        val id = exportDao.insertHistory(entity)
        
        // Clean up old exports - keep only last 5
        cleanupOldExports()
        
        return entity.copy(id = id).toDomain()
    }
    
    /**
     * Keep only the last 5 exports in internal storage and history
     */
    private suspend fun cleanupOldExports() {
        // Clean up old export files in internal storage
        val exportFiles = internalExportDir.listFiles()
            ?.filter { it.isFile && !it.name.startsWith("temp_") }
            ?.sortedByDescending { it.lastModified() }
            ?: return
        
        // Delete all exports beyond the 5th
        exportFiles.drop(5).forEach { file ->
            file.delete()
        }
        
        // Also clean up database history beyond 5 entries
        val allHistory = exportDao.getRecentHistory(Int.MAX_VALUE)
        allHistory.drop(5).forEach { history ->
            exportDao.deleteHistoryById(history.id)
        }
    }

    // Mapping functions
    private fun ExportConfigEntity.toDomain(): ExportConfig {
        return ExportConfig(
            id = id,
            name = name,
            format = format.toDomain(),
            scheduleHour = scheduleHour,
            scheduleMinute = scheduleMinute,
            scheduleDays = scheduleDays.split(",").mapNotNull { it.toIntOrNull() },
            isEnabled = isEnabled,
            exportPath = exportPath,
            includeMedia = includeMedia,
            lastExportAt = lastExportAt?.let {
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
            },
            lastExportStatus = lastExportStatus
        )
    }

    private fun ExportConfig.toEntity(): ExportConfigEntity {
        return ExportConfigEntity(
            id = id,
            name = name,
            format = format.toEntity(),
            scheduleHour = scheduleHour,
            scheduleMinute = scheduleMinute,
            scheduleDays = scheduleDays.joinToString(","),
            isEnabled = isEnabled,
            exportPath = exportPath,
            includeMedia = includeMedia,
            lastExportAt = lastExportAt?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
            lastExportStatus = lastExportStatus
        )
    }

    private fun ExportHistoryEntity.toDomain(): ExportHistory {
        return ExportHistory(
            id = id,
            configId = configId,
            format = format.toDomain(),
            filePath = filePath,
            fileName = fileName,
            entriesCount = entriesCount,
            fileSize = fileSize,
            isSuccess = isSuccess,
            errorMessage = errorMessage,
            exportedAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(exportedAt),
                ZoneId.systemDefault()
            )
        )
    }

    private fun com.dairum.data.local.entity.ExportFormat.toDomain(): ExportFormat {
        return when (this) {
            com.dairum.data.local.entity.ExportFormat.TXT -> ExportFormat.TXT
            com.dairum.data.local.entity.ExportFormat.PDF -> ExportFormat.PDF
            com.dairum.data.local.entity.ExportFormat.JSON -> ExportFormat.JSON
            com.dairum.data.local.entity.ExportFormat.CSV -> ExportFormat.CSV
            com.dairum.data.local.entity.ExportFormat.KITH_METADATA -> ExportFormat.KITH_METADATA
        }
    }

    private fun ExportFormat.toEntity(): com.dairum.data.local.entity.ExportFormat {
        return when (this) {
            ExportFormat.TXT -> com.dairum.data.local.entity.ExportFormat.TXT
            ExportFormat.PDF -> com.dairum.data.local.entity.ExportFormat.PDF
            ExportFormat.JSON -> com.dairum.data.local.entity.ExportFormat.JSON
            ExportFormat.CSV -> com.dairum.data.local.entity.ExportFormat.CSV
            ExportFormat.KITH_METADATA -> com.dairum.data.local.entity.ExportFormat.KITH_METADATA
        }
    }
    
    override suspend fun exportToExternalUri(
        entries: List<JournalEntry>,
        format: ExportFormat,
        includeMedia: Boolean,
        destinationUri: Uri
    ): Result<ExportHistory> = withContext(Dispatchers.IO) {
        try {
            // First export to internal storage
            val internalResult = when (format) {
                ExportFormat.TXT -> exportToTxt(entries)
                ExportFormat.PDF -> exportToPdf(entries, includeMedia)
                ExportFormat.JSON -> exportToJson(entries)
                ExportFormat.CSV -> exportToCsv(entries)
                ExportFormat.KITH_METADATA ->
                    writeKithMetadata(settingsRepository.getKithIncludeTitlesFlow().first()).map { it.first }
            }
            
            internalResult.fold(
                onSuccess = { internalPathWithSize ->
                    // Parse path and size from the internal result
                    val parts = internalPathWithSize.split("|")
                    val internalPath = parts[0]
                    val fileSize = parts.getOrNull(1)?.toLongOrNull() ?: 0L
                    
                    // Now copy to the external location
                    val sourceFile = File(internalPath)
                    
                    context.contentResolver.openOutputStream(destinationUri)?.use { outputStream ->
                        sourceFile.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    } ?: return@withContext Result.failure(Exception("Failed to open destination"))
                    
                    // Create history entry with external path info (include file size)
                    val history = createHistoryEntry(
                        pathWithSize = "${destinationUri}|$fileSize",
                        entriesCount = entries.size,
                        format = format
                    )
                    
                    // Optionally delete the internal copy
                    sourceFile.delete()
                    
                    Result.success(history)
                },
                onFailure = { e ->
                    Result.failure(e)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun copyExportToUri(exportFilePath: String, destinationUri: Uri): Result<Boolean> = 
        withContext(Dispatchers.IO) {
        try {
            val sourceFile = File(exportFilePath)
            if (!sourceFile.exists()) {
                return@withContext Result.failure(Exception("Source file not found"))
            }
            
            context.contentResolver.openOutputStream(destinationUri)?.use { outputStream ->
                sourceFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return@withContext Result.failure(Exception("Failed to open destination"))
            
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
