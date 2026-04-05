package com.dairum.data.repository

import com.dairum.data.local.dao.TrackerDao
import com.dairum.data.local.entity.CustomTrackerEntity
import com.dairum.data.local.entity.TrackerValueEntity
import com.dairum.domain.model.CustomTracker
import com.dairum.domain.model.TrackerDataPoint
import com.dairum.domain.model.TrackerStats
import com.dairum.domain.model.TrackerType
import com.dairum.domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrackerRepositoryImpl @Inject constructor(
    private val trackerDao: TrackerDao
) : TrackerRepository {

    override suspend fun createTracker(tracker: CustomTracker): Long {
        val maxOrder = trackerDao.getMaxTrackerSortOrder() ?: 0
        return trackerDao.insertTracker(tracker.toEntity().copy(sortOrder = maxOrder + 1))
    }

    override suspend fun updateTracker(tracker: CustomTracker) {
        trackerDao.updateTracker(tracker.toEntity())
    }

    override suspend fun deleteTracker(id: Long) {
        trackerDao.deleteTrackerById(id)
    }

    override suspend fun getTrackerById(id: Long): CustomTracker? {
        return trackerDao.getTrackerById(id)?.toDomain()
    }

    override suspend fun getAllTrackers(): List<CustomTracker> {
        return trackerDao.getAllTrackers().map { it.toDomain() }
    }

    override fun getActiveTrackersFlow(): Flow<List<CustomTracker>> {
        return trackerDao.getActiveTrackersFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllTrackersFlow(): Flow<List<CustomTracker>> {
        return trackerDao.getAllTrackersFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun toggleTrackerActive(id: Long, isActive: Boolean) {
        trackerDao.updateTrackerActive(id, isActive)
    }

    override suspend fun updateTrackerSortOrder(id: Long, sortOrder: Int) {
        trackerDao.updateTrackerSortOrder(id, sortOrder)
    }

    override suspend fun setTrackerValue(entryId: Long, trackerId: Long, value: String) {
        // Delete existing value if any
        trackerDao.deleteValueForEntryAndTracker(entryId, trackerId)
        // Insert new value
        trackerDao.insertValue(
            TrackerValueEntity(
                entryId = entryId,
                trackerId = trackerId,
                value = value
            )
        )
    }

    override suspend fun deleteTrackerValue(entryId: Long, trackerId: Long) {
        trackerDao.deleteValueForEntryAndTracker(entryId, trackerId)
    }

    override suspend fun getTrackerValuesForEntry(entryId: Long): Map<Long, String> {
        return trackerDao.getValuesForEntry(entryId).associate { it.trackerId to it.value }
    }

    override fun getTrackerValuesForEntryFlow(entryId: Long): Flow<Map<Long, String>> {
        return trackerDao.getValuesForEntryFlow(entryId).map { list ->
            list.associate { it.trackerId to it.value }
        }
    }

    override suspend fun getTrackerStats(trackerId: Long): TrackerStats {
        val tracker = trackerDao.getTrackerById(trackerId)?.toDomain()
            ?: throw IllegalArgumentException("Tracker not found")
        
        val values = trackerDao.getTrackerValuesWithEntryDate(trackerId)
        val dataPoints = values.mapNotNull { value ->
            val numericValue = value.value.toDoubleOrNull()
            if (numericValue != null) {
                TrackerDataPoint(
                    date = value.createdAt,
                    value = numericValue
                )
            } else null
        }
        
        return TrackerStats(
            tracker = tracker,
            values = dataPoints,
            average = trackerDao.getAverageValue(trackerId),
            min = trackerDao.getMinValue(trackerId),
            max = trackerDao.getMaxValue(trackerId),
            count = trackerDao.getValueCount(trackerId)
        )
    }

    private fun CustomTrackerEntity.toDomain(): CustomTracker {
        return CustomTracker(
            id = id,
            name = name,
            type = type.toDomain(),
            icon = icon,
            unit = unit,
            minValue = minValue,
            maxValue = maxValue,
            options = options?.split(",")?.filter { it.isNotEmpty() },
            isBuiltIn = isBuiltIn,
            isActive = isActive,
            sortOrder = sortOrder
        )
    }

    private fun CustomTracker.toEntity(): CustomTrackerEntity {
        return CustomTrackerEntity(
            id = id,
            name = name,
            type = type.toEntity(),
            icon = icon,
            unit = unit,
            minValue = minValue,
            maxValue = maxValue,
            options = options?.joinToString(","),
            isBuiltIn = isBuiltIn,
            isActive = isActive,
            sortOrder = sortOrder
        )
    }

    private fun com.dairum.data.local.entity.TrackerType.toDomain(): TrackerType {
        return when (this) {
            com.dairum.data.local.entity.TrackerType.TEXT -> TrackerType.TEXT
            com.dairum.data.local.entity.TrackerType.NUMBER -> TrackerType.NUMBER
            com.dairum.data.local.entity.TrackerType.RATING -> TrackerType.RATING
            com.dairum.data.local.entity.TrackerType.BOOLEAN -> TrackerType.BOOLEAN
            com.dairum.data.local.entity.TrackerType.SELECT -> TrackerType.SELECT
            com.dairum.data.local.entity.TrackerType.PEOPLE -> TrackerType.PEOPLE
            com.dairum.data.local.entity.TrackerType.WEIGHT -> TrackerType.WEIGHT
        }
    }

    private fun TrackerType.toEntity(): com.dairum.data.local.entity.TrackerType {
        return when (this) {
            TrackerType.TEXT -> com.dairum.data.local.entity.TrackerType.TEXT
            TrackerType.NUMBER -> com.dairum.data.local.entity.TrackerType.NUMBER
            TrackerType.RATING -> com.dairum.data.local.entity.TrackerType.RATING
            TrackerType.BOOLEAN -> com.dairum.data.local.entity.TrackerType.BOOLEAN
            TrackerType.SELECT -> com.dairum.data.local.entity.TrackerType.SELECT
            TrackerType.PEOPLE -> com.dairum.data.local.entity.TrackerType.PEOPLE
            TrackerType.WEIGHT -> com.dairum.data.local.entity.TrackerType.WEIGHT
        }
    }
}
