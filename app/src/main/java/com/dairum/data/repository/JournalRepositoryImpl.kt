package com.dairum.data.repository

import com.dairum.data.local.dao.*
import com.dairum.data.local.entity.*
import com.dairum.domain.model.*
import com.dairum.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(
    private val entryDao: JournalEntryDao,
    private val tagDao: TagDao,
    private val personDao: PersonDao,
    private val trackerDao: TrackerDao,
    private val mediaDao: MediaDao,
    private val categoryDao: CategoryDao
) : JournalRepository {

    override suspend fun createEntry(entry: JournalEntry): Long {
        android.util.Log.d("JournalRepo", "createEntry called - location: ${entry.location?.let { "lat=${it.latitude}, lng=${it.longitude}" } ?: "null"}")
        val entity = entry.toEntity()
        android.util.Log.d("JournalRepo", "Entity locationData: ${entity.locationData ?: "null"}")
        val id = entryDao.insert(entity)
        android.util.Log.d("JournalRepo", "Entry created with id: $id")
        return id
    }

    override suspend fun updateEntry(entry: JournalEntry) {
        android.util.Log.d("JournalRepo", "updateEntry called for id=${entry.id} - location: ${entry.location?.let { "lat=${it.latitude}, lng=${it.longitude}" } ?: "null"}")
        val entity = entry.toEntity()
        android.util.Log.d("JournalRepo", "Entity locationData: ${entity.locationData ?: "null"}")
        entryDao.update(entity)
        android.util.Log.d("JournalRepo", "Entry updated")
    }

    override suspend fun deleteEntry(id: Long) {
        entryDao.deleteById(id)
    }

    override suspend fun getEntry(id: Long): JournalEntry? {
        val details = entryDao.getWithDetails(id) ?: return null
        return details.toDomain()
    }

    override fun getEntryFlow(id: Long): Flow<JournalEntry?> {
        return entryDao.getWithDetailsFlow(id).map { it?.toDomain() }
    }

    override suspend fun getEntryCount(): Int {
        return entryDao.getEntryCount()
    }

    override fun getAllEntries(): Flow<List<JournalEntry>> {
        return entryDao.getAllEntriesWithDetailsFlow().map { list ->
            android.util.Log.d("JournalRepo", "getAllEntries: ${list.size} entries from database")
            list.forEachIndexed { index, details ->
                android.util.Log.d("JournalRepo", "  Entry[$index] id=${details.entry.id}: locationData='${details.entry.locationData ?: "null"}'")
            }
            list.map { it.toDomain() }
        }
    }

    override fun getEntriesByCategory(categoryId: Long): Flow<List<JournalEntry>> {
        return entryDao.getEntriesByCategoryFlow(categoryId).map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override fun getEntriesByMood(moodLevel: Int): Flow<List<JournalEntry>> {
        return entryDao.getEntriesByMoodFlow(moodLevel).map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override fun getEntriesByTag(tagId: Long): Flow<List<JournalEntry>> {
        return entryDao.getAllEntriesWithDetailsFlow().map { list ->
            list.filter { entry -> entry.tags.any { it.id == tagId } }
                .map { it.toDomain() }
        }
    }

    override fun getFavoriteEntries(): Flow<List<JournalEntry>> {
        return entryDao.getFavoriteEntriesFlow().map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override fun getDraftEntries(): Flow<List<JournalEntry>> {
        return entryDao.getDraftsFlow().map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override fun getArchivedEntries(): Flow<List<JournalEntry>> {
        return entryDao.getArchivedEntriesFlow().map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override fun searchEntries(query: String): Flow<List<JournalEntry>> {
        return entryDao.searchEntries(query).map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override fun getEntriesInDateRangeFlow(startTime: Long, endTime: Long): Flow<List<JournalEntry>> {
        return entryDao.getEntriesInDateRange(startTime, endTime).map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override suspend fun getEntriesInDateRange(startDate: Long, endDate: Long): List<JournalEntry> {
        return entryDao.getEntriesInDateRangeSync(startDate, endDate).map { it.toDomainBasic() }
    }

    override fun getEntriesOnThisDay(monthDay: String): Flow<List<JournalEntry>> {
        return entryDao.getEntriesOnThisDay(monthDay).map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        entryDao.updateFavorite(id, isFavorite)
    }

    override suspend fun archiveEntry(id: Long, isArchived: Boolean) {
        entryDao.updateArchived(id, isArchived)
    }

    override suspend fun togglePin(id: Long, isPinned: Boolean) {
        entryDao.updatePinned(id, isPinned)
    }

    override suspend fun softDelete(id: Long) {
        entryDao.softDelete(id)
    }

    override suspend fun restoreEntry(id: Long) {
        entryDao.restoreEntry(id)
    }

    override fun getDeletedEntries(): Flow<List<JournalEntry>> {
        return entryDao.getDeletedEntriesFlow().map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override suspend fun emptyTrash() {
        entryDao.emptyTrash()
    }

    override suspend fun setTagsForEntry(entryId: Long, tagIds: List<Long>) {
        // Diff old vs new to correctly adjust usage counts
        val oldTagIds = tagDao.getTagIdsForEntry(entryId).toSet()
        val newTagIds = tagIds.toSet()
        
        val removed = oldTagIds - newTagIds
        val added = newTagIds - oldTagIds
        
        // Remove old relationships
        tagDao.deleteAllTagsForEntry(entryId)
        // Insert new relationships
        tagIds.forEach { tagId ->
            tagDao.insertEntryTag(EntryTagCrossRef(entryId, tagId))
        }
        // Adjust counts
        removed.forEach { tagDao.decrementUsageCount(it) }
        added.forEach { tagDao.incrementUsageCount(it) }
    }

    override suspend fun setPeopleForEntry(entryId: Long, personIds: List<Long>) {
        // Diff old vs new to correctly adjust mention counts
        val oldPersonIds = personDao.getPeopleForEntry(entryId).map { it.id }.toSet()
        val newPersonIds = personIds.toSet()
        
        val removed = oldPersonIds - newPersonIds
        val added = newPersonIds - oldPersonIds
        
        // Remove old relationships
        personDao.deleteAllPeopleForEntry(entryId)
        // Insert new relationships
        personIds.forEach { personId ->
            personDao.insertEntryPerson(EntryPersonCrossRef(entryId, personId))
        }
        // Adjust counts
        removed.forEach { personDao.decrementMentionCount(it) }
        added.forEach { personDao.incrementMentionCount(it) }
    }

    override suspend fun setTrackerValues(entryId: Long, values: Map<Long, String>) {
        // Delete existing values
        trackerDao.deleteAllValuesForEntry(entryId)
        // Insert new values
        values.forEach { (trackerId, value) ->
            trackerDao.insertValue(
                TrackerValueEntity(
                    entryId = entryId,
                    trackerId = trackerId,
                    value = value
                )
            )
        }
    }

    // Entity to Domain mappings
    private fun EntryWithDetails.toDomain(): JournalEntry {
        val categoryDomain = category?.toDomain()
        return JournalEntry(
            id = entry.id,
            title = entry.title,
            content = RichContent(),
            plainContent = entry.plainContent,
            formattingJson = entry.content, // content field stores formatting JSON
            category = categoryDomain,
            createdAt = entry.createdAt.toLocalDateTime(),
            updatedAt = entry.updatedAt.toLocalDateTime(),
            mood = entry.moodLevel?.let { Mood.fromDb(it, entry.moodIcon) },
            weather = entry.weatherData?.let { parseWeatherJson(it) },
            location = entry.locationData?.let { parseLocationJson(it) },
            additionalLocations = entry.additionalLocationsData?.let { parseLocationsListJson(it) } ?: emptyList(),
            media = media.map { it.toDomain() },
            tags = tags.map { it.toDomain() },
            people = people.map { it.toDomain() },
            trackerValues = trackerValues.associate { it.trackerId to it.value },
            isFavorite = entry.isFavorite,
            isArchived = entry.isArchived,
            isDraft = entry.isDraft,
            isPinned = entry.isPinned,
            wordCount = entry.wordCount,
            backgroundColor = entry.backgroundColor
        )
    }

    private fun JournalEntryEntity.toDomainBasic(): JournalEntry {
        return JournalEntry(
            id = id,
            title = title,
            plainContent = plainContent,
            formattingJson = content, // Load formatting JSON
            createdAt = createdAt.toLocalDateTime(),
            updatedAt = updatedAt.toLocalDateTime(),
            mood = moodLevel?.let { Mood.fromDb(it, moodIcon) },
            weather = weatherData?.let { parseWeatherJson(it) },
            location = locationData?.let { parseLocationJson(it) },
            additionalLocations = additionalLocationsData?.let { parseLocationsListJson(it) } ?: emptyList(),
            isFavorite = isFavorite,
            isArchived = isArchived,
            isDraft = isDraft,
            isPinned = isPinned,
            wordCount = wordCount,
            backgroundColor = backgroundColor
        )
    }

    private fun JournalEntry.toEntity(): JournalEntryEntity {
        return JournalEntryEntity(
            id = id,
            title = title,
            content = formattingJson, // Store formatting JSON in content field
            plainContent = plainContent,
            categoryId = category?.id,
            createdAt = createdAt.toEpochMillis(),
            updatedAt = updatedAt.toEpochMillis(),
            moodLevel = mood?.level,
            moodIcon = mood?.icon,
            weatherData = weather?.let { serializeWeatherJson(it) },
            locationData = location?.let { serializeLocationJson(it) },
            additionalLocationsData = additionalLocations.takeIf { it.isNotEmpty() }?.let { serializeLocationsListJson(it) },
            isFavorite = isFavorite,
            isArchived = isArchived,
            isDraft = isDraft,
            isPinned = isPinned,
            wordCount = wordCount,
            backgroundColor = backgroundColor
        )
    }

    private fun CategoryEntity.toDomain(): Category {
        return Category(
            id = id,
            name = name,
            color = color,
            icon = icon,
            sortOrder = sortOrder,
            isDefault = isDefault
        )
    }

    private fun TagEntity.toDomain(): Tag {
        return Tag(
            id = id,
            name = name,
            color = color,
            usageCount = usageCount
        )
    }

    private fun MediaEntity.toDomain(): Media {
        return Media(
            id = id,
            entryId = entryId,
            type = type.toDomain(),
            uri = uri,
            thumbnailUri = thumbnailUri,
            fileName = fileName,
            mimeType = mimeType,
            sizeBytes = sizeBytes,
            duration = duration,
            width = width,
            height = height,
            caption = caption,
            sortOrder = sortOrder,
            inlineKey = inlineKey
        )
    }

    private fun PersonEntity.toDomain(): Person {
        return Person(
            id = id,
            name = name,
            relationship = relationship,
            photoUri = photoUri,
            notes = notes,
            isFavorite = isFavorite,
            mentionCount = mentionCount
        )
    }

    private fun com.dairum.data.local.entity.MediaType.toDomain(): com.dairum.domain.model.MediaType {
        return when (this) {
            com.dairum.data.local.entity.MediaType.PHOTO -> com.dairum.domain.model.MediaType.PHOTO
            com.dairum.data.local.entity.MediaType.VIDEO -> com.dairum.domain.model.MediaType.VIDEO
            com.dairum.data.local.entity.MediaType.VOICE -> com.dairum.domain.model.MediaType.VOICE
            com.dairum.data.local.entity.MediaType.ATTACHMENT -> com.dairum.domain.model.MediaType.ATTACHMENT
        }
    }

    // JSON parsing helpers using org.json
    private fun parseWeatherJson(json: String): Weather? {
        return try {
            if (json.isBlank() || json == "{}") return null
            val obj = org.json.JSONObject(json)
            Weather(
                temperature = obj.optDouble("temperature", 0.0),
                temperatureFahrenheit = obj.optDouble("temperatureFahrenheit", 0.0),
                condition = obj.optString("condition", "Unknown"),
                icon = obj.optString("icon", ""),
                humidity = obj.optInt("humidity", 0),
                feelsLike = obj.optDouble("feelsLike", 0.0),
                locationName = if (obj.has("locationName")) obj.optString("locationName", "").takeIf { it.isNotBlank() } else null
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun parseLocationJson(json: String): Location? {
        return try {
            android.util.Log.d("JournalRepo", "Parsing location JSON: $json")
            if (json.isBlank() || json == "{}") {
                android.util.Log.d("JournalRepo", "Location JSON is blank/empty")
                return null
            }
            val obj = org.json.JSONObject(json)
            
            // Try both "latitude"/"longitude" and "lat"/"lng" formats for backwards compatibility
            val latitude = when {
                obj.has("latitude") -> obj.optDouble("latitude", 0.0)
                obj.has("lat") -> obj.optDouble("lat", 0.0)
                else -> 0.0
            }
            val longitude = when {
                obj.has("longitude") -> obj.optDouble("longitude", 0.0)
                obj.has("lng") -> obj.optDouble("lng", 0.0)
                else -> 0.0
            }
            
            android.util.Log.d("JournalRepo", "Parsed coordinates: lat=$latitude, lng=$longitude")
            
            // Only return Location if we have valid coordinates
            if (latitude == 0.0 && longitude == 0.0) {
                android.util.Log.d("JournalRepo", "Invalid coordinates (0,0), returning null")
                return null
            }
            
            val location = Location(
                latitude = latitude,
                longitude = longitude,
                address = if (obj.has("address")) obj.optString("address", "").takeIf { it.isNotBlank() } else null,
                placeName = if (obj.has("placeName")) obj.optString("placeName", "").takeIf { it.isNotBlank() } else null,
                placeId = if (obj.has("placeId")) obj.optString("placeId", "").takeIf { it.isNotBlank() } else null
            )
            android.util.Log.d("JournalRepo", "Parsed location: ${location.latitude},${location.longitude}, place=${location.placeName}")
            location
        } catch (e: Exception) {
            android.util.Log.e("JournalRepo", "Error parsing location JSON: ${e.message}")
            null
        }
    }

    private fun serializeWeatherJson(weather: Weather): String {
        return org.json.JSONObject().apply {
            put("temperature", weather.temperature)
            put("temperatureFahrenheit", weather.temperatureFahrenheit)
            put("condition", weather.condition)
            put("icon", weather.icon)
            put("humidity", weather.humidity)
            put("feelsLike", weather.feelsLike)
            weather.locationName?.let { put("locationName", it) }
        }.toString()
    }

    private fun serializeLocationJson(location: Location): String {
        val json = org.json.JSONObject().apply {
            put("latitude", location.latitude)
            put("longitude", location.longitude)
            location.address?.let { put("address", it) }
            location.placeName?.let { put("placeName", it) }
            location.placeId?.let { put("placeId", it) }
        }.toString()
        android.util.Log.d("JournalRepo", "Serializing location: ${location.latitude},${location.longitude} -> $json")
        return json
    }

    private fun parseLocationsListJson(json: String): List<Location> {
        return try {
            if (json.isBlank()) return emptyList()
            val array = org.json.JSONArray(json)
            (0 until array.length()).mapNotNull { index ->
                parseLocationJson(array.getJSONObject(index).toString())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun serializeLocationsListJson(locations: List<Location>): String {
        val array = org.json.JSONArray()
        locations.forEach { location ->
            array.put(org.json.JSONObject(serializeLocationJson(location)))
        }
        return array.toString()
    }

    // Time conversion helpers
    private fun Long.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(this),
            ZoneId.systemDefault()
        )
    }

    private fun LocalDateTime.toEpochMillis(): Long {
        return atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
