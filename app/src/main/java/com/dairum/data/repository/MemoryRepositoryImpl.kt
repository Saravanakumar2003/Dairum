package com.dairum.data.repository

import com.dairum.data.local.dao.JournalEntryDao
import com.dairum.data.local.dao.MediaDao
import com.dairum.data.local.dao.MemoryDao
import com.dairum.data.local.entity.*
import com.dairum.domain.model.*
import com.dairum.domain.repository.MemoryRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryRepositoryImpl @Inject constructor(
    private val memoryDao: MemoryDao,
    private val journalEntryDao: JournalEntryDao,
    private val mediaDao: MediaDao
) : MemoryRepository {

    // ============ MEMORY CRUD ============

    override suspend fun createMemory(memory: Memory): Long {
        val entity = memory.toEntity()
        return memoryDao.insertMemory(entity)
    }

    override suspend fun updateMemory(memory: Memory) {
        memoryDao.updateMemory(memory.toEntity())
    }

    override suspend fun deleteMemory(id: Long) {
        memoryDao.deleteMemory(id)
    }

    override suspend fun getMemoryById(id: Long): Memory? {
        val entity = memoryDao.getMemoryById(id) ?: return null
        val entries = memoryDao.getEntriesForMemory(id).first()
        val milestones = memoryDao.getMilestonesForMemory(id).first()
        val trackers = memoryDao.getTrackersForMemory(id).first()
        
        // Load media for all entries concurrently
        val entriesWithMedia = coroutineScope {
            entries.map { entry ->
                async { entry.toEntryWithMedia() }
            }.awaitAll()
        }
        
        return entity.toDomain(
            entries = entriesWithMedia,
            milestones = milestones.map { it.toDomain() },
            trackers = trackers.map { it.toDomain() }
        )
    }

    override fun getMemoryByIdFlow(id: Long): Flow<Memory?> {
        return combine(
            memoryDao.getMemoryByIdFlow(id),
            memoryDao.getEntriesForMemory(id),
            memoryDao.getMilestonesForMemory(id),
            memoryDao.getTrackersForMemory(id)
        ) { entity, entries, milestones, trackers ->
            if (entity == null) {
                null
            } else {
                // Load media for all entries concurrently
                coroutineScope {
                    val entriesWithMedia = entries.map { entry ->
                        async { entry.toEntryWithMedia() }
                    }.awaitAll()
                    
                    entity.toDomain(
                        entries = entriesWithMedia,
                        milestones = milestones.map { it.toDomain() },
                        trackers = trackers.map { it.toDomain() }
                    )
                }
            }
        }
    }

    // ============ LISTING ============

    override fun getAllMemoriesFlow(): Flow<List<Memory>> {
        return memoryDao.getAllMemoriesFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getFavoriteMemoriesFlow(): Flow<List<Memory>> {
        return memoryDao.getFavoriteMemoriesFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getMemoriesInDateRange(startDate: Long, endDate: Long): Flow<List<Memory>> {
        return memoryDao.getMemoriesInDateRange(startDate, endDate).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun searchMemories(query: String): Flow<List<Memory>> {
        return memoryDao.searchMemories(query).map { list ->
            list.map { it.toDomain() }
        }
    }

    // ============ ENTRY MANAGEMENT ============

    override suspend fun addEntriesToMemory(memoryId: Long, entryIds: List<Long>) {
        val crossRefs = entryIds.mapIndexed { index, entryId ->
            MemoryEntryCrossRef(
                memoryId = memoryId,
                entryId = entryId,
                sortOrder = index
            )
        }
        memoryDao.insertMemoryEntries(crossRefs)
        recalculateMemoryStats(memoryId)
    }

    override suspend fun removeEntryFromMemory(memoryId: Long, entryId: Long) {
        memoryDao.removeEntryFromMemory(memoryId, entryId)
        recalculateMemoryStats(memoryId)
    }

    override fun getEntriesForMemory(memoryId: Long): Flow<List<JournalEntry>> {
        return memoryDao.getEntriesForMemory(memoryId).map { list ->
            coroutineScope {
                list.map { entry ->
                    async { entry.toEntryWithMedia() }
                }.awaitAll()
            }
        }
    }

    override fun getMemoriesForEntry(entryId: Long): Flow<List<Memory>> {
        return memoryDao.getMemoriesForEntry(entryId).map { list ->
            list.map { it.toDomain() }
        }
    }

    // ============ MILESTONES ============

    override suspend fun addMilestone(milestone: MemoryMilestone): Long {
        return memoryDao.insertMilestone(milestone.toEntity())
    }

    override suspend fun updateMilestone(milestone: MemoryMilestone) {
        memoryDao.updateMilestone(milestone.toEntity())
    }

    override suspend fun deleteMilestone(id: Long) {
        memoryDao.deleteMilestone(id)
    }

    override fun getMilestonesForMemory(memoryId: Long): Flow<List<MemoryMilestone>> {
        return memoryDao.getMilestonesForMemory(memoryId).map { list ->
            list.map { it.toDomain() }
        }
    }

    // ============ TRACKERS ============

    override suspend fun addMemoryTracker(tracker: MemoryTracker): Long {
        return memoryDao.insertMemoryTracker(tracker.toEntity())
    }

    override suspend fun updateMemoryTracker(tracker: MemoryTracker) {
        memoryDao.updateMemoryTracker(tracker.toEntity())
    }

    override suspend fun deleteMemoryTracker(id: Long) {
        memoryDao.deleteMemoryTracker(id)
    }

    override fun getTrackersForMemory(memoryId: Long): Flow<List<MemoryTracker>> {
        return memoryDao.getTrackersForMemory(memoryId).map { list ->
            list.map { it.toDomain() }
        }
    }

    // ============ ACTIONS ============

    override suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        memoryDao.updateFavorite(id, isFavorite)
    }

    override suspend fun recalculateMemoryStats(memoryId: Long) {
        val entries = memoryDao.getEntriesForMemory(memoryId).first()
        
        var totalWordCount = 0
        var photoCount = 0
        var videoCount = 0
        
        for (entry in entries) {
            totalWordCount += entry.wordCount
            // Count media for this entry
            // This would need the full entry with media relations
        }
        
        memoryDao.updateMemoryStats(
            memoryId = memoryId,
            entryCount = entries.size,
            wordCount = totalWordCount,
            photoCount = photoCount,
            videoCount = videoCount
        )
    }

    // ============ STATISTICS ============

    override suspend fun getTotalMemoryCount(): Int {
        return memoryDao.getTotalMemoryCount()
    }

    override suspend fun getTotalTimeSpentInMemories(): Long {
        return memoryDao.getTotalTimeSpentInMemories() ?: 0L
    }

    // ============ SMART SUGGESTIONS ============

    override suspend fun suggestEntriesForMemory(memoryId: Long): List<JournalEntry> {
        val memory = memoryDao.getMemoryById(memoryId) ?: return emptyList()
        
        // Get entries in the memory's date range that aren't already in the memory
        val entriesInRange = journalEntryDao.getEntriesInDateRangeSync(
            memory.startDate,
            memory.endDate
        )
        
        val existingEntryIds = memoryDao.getEntriesForMemory(memoryId).first().map { it.id }.toSet()
        
        return entriesInRange
            .filter { it.id !in existingEntryIds }
            .map { it.toBasicDomain() }
    }

    override suspend fun generateMemoryHighlights(memoryId: Long): List<String> {
        val entries = memoryDao.getEntriesForMemory(memoryId).first()
        
        // Simple highlight generation based on entry content
        val highlights = mutableListOf<String>()
        
        // Find entries with high mood
        val happyEntries = entries.filter { (it.moodLevel ?: 3) >= 4 }
        if (happyEntries.isNotEmpty()) {
            highlights.add("${happyEntries.size} happy moments recorded")
        }
        
        // Check for entries with media
        // This would need full entry relations
        
        // Check word count
        val totalWords = entries.sumOf { it.wordCount }
        if (totalWords > 1000) {
            highlights.add("Over ${totalWords / 1000}k words written")
        }
        
        return highlights
    }

    // ============ MAPPERS ============

    private fun Memory.toEntity() = MemoryEntity(
        id = id,
        title = title,
        description = description,
        coverImageUri = coverImageUri,
        startDate = startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        endDate = endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        location = location,
        locationData = locationData?.let { 
            """{"lat":${it.latitude},"lng":${it.longitude},"address":"${it.address ?: ""}"}""" 
        },
        isFavorite = isFavorite,
        color = color,
        icon = icon,
        tags = tags.joinToString(","),
        moodSummary = moodSummary?.let {
            """{"avg":${it.averageMood},"dominant":${it.dominantMood}}"""
        },
        totalTimeSpent = totalTimeSpent,
        entryCount = entryCount,
        wordCount = wordCount,
        photoCount = photoCount,
        videoCount = videoCount,
        peopleInvolved = peopleInvolved.joinToString(","),
        highlights = highlights.joinToString("|")
    )

    private fun MemoryEntity.toDomain(
        entries: List<JournalEntry> = emptyList(),
        milestones: List<MemoryMilestone> = emptyList(),
        trackers: List<MemoryTracker> = emptyList()
    ) = Memory(
        id = id,
        title = title,
        description = description,
        coverImageUri = coverImageUri,
        startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneId.systemDefault()),
        endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneId.systemDefault()),
        location = location,
        isFavorite = isFavorite,
        color = color,
        icon = icon,
        tags = tags?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
        totalTimeSpent = totalTimeSpent,
        entryCount = entryCount,
        wordCount = wordCount,
        photoCount = photoCount,
        videoCount = videoCount,
        peopleInvolved = peopleInvolved?.split(",")?.mapNotNull { it.toLongOrNull() } ?: emptyList(),
        highlights = highlights?.split("|")?.filter { it.isNotBlank() } ?: emptyList(),
        entries = entries,
        milestones = milestones,
        trackers = trackers
    )

    private fun MemoryMilestone.toEntity() = MemoryMilestoneEntity(
        id = id,
        memoryId = memoryId,
        title = title,
        description = description,
        milestoneDate = milestoneDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        relatedEntryId = relatedEntryId,
        icon = icon,
        sortOrder = sortOrder
    )

    private fun MemoryMilestoneEntity.toDomain() = MemoryMilestone(
        id = id,
        memoryId = memoryId,
        title = title,
        description = description,
        milestoneDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(milestoneDate), ZoneId.systemDefault()),
        relatedEntryId = relatedEntryId,
        icon = icon,
        sortOrder = sortOrder
    )

    private fun MemoryTracker.toEntity() = MemoryTrackerEntity(
        id = id,
        memoryId = memoryId,
        name = name,
        type = com.dairum.data.local.entity.MemoryTrackerType.valueOf(type.name),
        icon = icon,
        unit = unit,
        goalValue = goalValue,
        currentValue = currentValue,
        progress = progress
    )

    private fun MemoryTrackerEntity.toDomain() = MemoryTracker(
        id = id,
        memoryId = memoryId,
        name = name,
        type = com.dairum.domain.model.MemoryTrackerType.valueOf(type.name),
        icon = icon,
        unit = unit,
        goalValue = goalValue,
        currentValue = currentValue,
        progress = progress
    )

    private fun JournalEntryEntity.toBasicDomain() = JournalEntry(
        id = id,
        title = title,
        plainContent = plainContent,
        createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()),
        updatedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(updatedAt), ZoneId.systemDefault()),
        mood = moodLevel?.let { Mood.fromDb(it, moodIcon) },
        isFavorite = isFavorite,
        isArchived = isArchived,
        isDraft = isDraft,
        wordCount = wordCount
    )
    
    /**
     * Convert JournalEntryEntity to JournalEntry with media included
     */
    private suspend fun JournalEntryEntity.toEntryWithMedia(): JournalEntry {
        // Fetch media for this entry
        val mediaList = mediaDao.getMediaForEntry(id)
        
        return JournalEntry(
            id = id,
            title = title,
            plainContent = plainContent,
            createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()),
            updatedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(updatedAt), ZoneId.systemDefault()),
            mood = moodLevel?.let { Mood.fromDb(it, moodIcon) },
            media = mediaList.map { it.toDomain() },
            isFavorite = isFavorite,
            isArchived = isArchived,
            isDraft = isDraft,
            wordCount = wordCount
        )
    }
    
    private fun MediaEntity.toDomain() = Media(
        id = id,
        entryId = entryId,
        type = com.dairum.domain.model.MediaType.valueOf(type.name),
        uri = uri,
        thumbnailUri = thumbnailUri,
        fileName = fileName,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        duration = duration,
        width = width,
        height = height,
        caption = caption,
        sortOrder = sortOrder
    )
}
