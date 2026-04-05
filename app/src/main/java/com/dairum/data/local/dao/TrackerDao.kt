package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.CustomTrackerEntity
import com.dairum.data.local.entity.TrackerType
import com.dairum.data.local.entity.TrackerValueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackerDao {
    
    // Custom Tracker CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracker(tracker: CustomTrackerEntity): Long
    
    @Update
    suspend fun updateTracker(tracker: CustomTrackerEntity)
    
    @Delete
    suspend fun deleteTracker(tracker: CustomTrackerEntity)
    
    @Query("DELETE FROM custom_trackers WHERE id = :id")
    suspend fun deleteTrackerById(id: Long)
    
    @Query("SELECT * FROM custom_trackers WHERE id = :id")
    suspend fun getTrackerById(id: Long): CustomTrackerEntity?
    
    @Query("SELECT * FROM custom_trackers WHERE isActive = 1 ORDER BY sortOrder ASC, name ASC")
    fun getActiveTrackersFlow(): Flow<List<CustomTrackerEntity>>
    
    @Query("SELECT * FROM custom_trackers ORDER BY sortOrder ASC, name ASC")
    fun getAllTrackersFlow(): Flow<List<CustomTrackerEntity>>
    
    @Query("SELECT * FROM custom_trackers ORDER BY sortOrder ASC, name ASC")
    suspend fun getAllTrackers(): List<CustomTrackerEntity>
    
    @Query("SELECT * FROM custom_trackers WHERE isActive = 1 ORDER BY sortOrder ASC")
    suspend fun getActiveTrackers(): List<CustomTrackerEntity>
    
    @Query("SELECT * FROM custom_trackers WHERE type = :type AND isActive = 1")
    suspend fun getTrackersByType(type: TrackerType): List<CustomTrackerEntity>
    
    @Query("UPDATE custom_trackers SET isActive = :isActive WHERE id = :id")
    suspend fun updateTrackerActive(id: Long, isActive: Boolean)
    
    @Query("UPDATE custom_trackers SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateTrackerSortOrder(id: Long, sortOrder: Int)
    
    @Query("SELECT MAX(sortOrder) FROM custom_trackers")
    suspend fun getMaxTrackerSortOrder(): Int?
    
    // Tracker Values CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValue(value: TrackerValueEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValues(values: List<TrackerValueEntity>): List<Long>
    
    @Update
    suspend fun updateValue(value: TrackerValueEntity)
    
    @Delete
    suspend fun deleteValue(value: TrackerValueEntity)
    
    @Query("DELETE FROM tracker_values WHERE id = :id")
    suspend fun deleteValueById(id: Long)
    
    @Query("DELETE FROM tracker_values WHERE entryId = :entryId")
    suspend fun deleteAllValuesForEntry(entryId: Long)
    
    @Query("DELETE FROM tracker_values WHERE entryId = :entryId AND trackerId = :trackerId")
    suspend fun deleteValueForEntryAndTracker(entryId: Long, trackerId: Long)
    
    @Query("SELECT * FROM tracker_values WHERE entryId = :entryId")
    suspend fun getValuesForEntry(entryId: Long): List<TrackerValueEntity>
    
    @Query("SELECT * FROM tracker_values WHERE entryId = :entryId")
    fun getValuesForEntryFlow(entryId: Long): Flow<List<TrackerValueEntity>>
    
    @Query("SELECT * FROM tracker_values WHERE trackerId = :trackerId ORDER BY createdAt DESC")
    suspend fun getValuesForTracker(trackerId: Long): List<TrackerValueEntity>
    
    @Query("SELECT * FROM tracker_values WHERE trackerId = :trackerId ORDER BY createdAt DESC")
    fun getValuesForTrackerFlow(trackerId: Long): Flow<List<TrackerValueEntity>>
    
    @Query("""
        SELECT tv.* FROM tracker_values tv
        INNER JOIN journal_entries e ON tv.entryId = e.id
        WHERE tv.trackerId = :trackerId AND e.isArchived = 0 AND e.isDraft = 0
        ORDER BY e.createdAt DESC
    """)
    suspend fun getTrackerValuesWithEntryDate(trackerId: Long): List<TrackerValueEntity>
    
    // Get value for specific entry and tracker
    @Query("SELECT * FROM tracker_values WHERE entryId = :entryId AND trackerId = :trackerId LIMIT 1")
    suspend fun getValueForEntryAndTracker(entryId: Long, trackerId: Long): TrackerValueEntity?
    
    // Statistics
    @Query("""
        SELECT AVG(CAST(value AS REAL)) FROM tracker_values 
        WHERE trackerId = :trackerId
    """)
    suspend fun getAverageValue(trackerId: Long): Double?
    
    @Query("""
        SELECT MIN(CAST(value AS REAL)) FROM tracker_values 
        WHERE trackerId = :trackerId
    """)
    suspend fun getMinValue(trackerId: Long): Double?
    
    @Query("""
        SELECT MAX(CAST(value AS REAL)) FROM tracker_values 
        WHERE trackerId = :trackerId
    """)
    suspend fun getMaxValue(trackerId: Long): Double?
    
    @Query("SELECT COUNT(*) FROM tracker_values WHERE trackerId = :trackerId")
    suspend fun getValueCount(trackerId: Long): Int
}
