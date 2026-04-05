package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {
    
    // ============ MEMORY CRUD ============
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity): Long
    
    @Update
    suspend fun updateMemory(memory: MemoryEntity)
    
    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemory(id: Long)
    
    @Query("SELECT * FROM memories WHERE id = :id")
    suspend fun getMemoryById(id: Long): MemoryEntity?
    
    @Query("SELECT * FROM memories WHERE id = :id")
    fun getMemoryByIdFlow(id: Long): Flow<MemoryEntity?>
    
    @Query("SELECT * FROM memories ORDER BY startDate DESC")
    fun getAllMemoriesFlow(): Flow<List<MemoryEntity>>
    
    @Query("SELECT * FROM memories WHERE isFavorite = 1 ORDER BY startDate DESC")
    fun getFavoriteMemoriesFlow(): Flow<List<MemoryEntity>>
    
    @Query("""
        SELECT * FROM memories 
        WHERE startDate >= :startTime AND endDate <= :endTime
        ORDER BY startDate DESC
    """)
    fun getMemoriesInDateRange(startTime: Long, endTime: Long): Flow<List<MemoryEntity>>
    
    @Query("UPDATE memories SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean, updatedAt: Long = System.currentTimeMillis())
    
    // ============ MEMORY-ENTRY RELATIONSHIPS ============
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoryEntry(crossRef: MemoryEntryCrossRef)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoryEntries(crossRefs: List<MemoryEntryCrossRef>)
    
    @Delete
    suspend fun deleteMemoryEntry(crossRef: MemoryEntryCrossRef)
    
    @Query("DELETE FROM memory_entries WHERE memoryId = :memoryId")
    suspend fun deleteAllEntriesFromMemory(memoryId: Long)
    
    @Query("DELETE FROM memory_entries WHERE memoryId = :memoryId AND entryId = :entryId")
    suspend fun removeEntryFromMemory(memoryId: Long, entryId: Long)
    
    @Query("""
        SELECT e.* FROM journal_entries e
        INNER JOIN memory_entries me ON e.id = me.entryId
        WHERE me.memoryId = :memoryId
        ORDER BY me.sortOrder ASC, e.createdAt ASC
    """)
    fun getEntriesForMemory(memoryId: Long): Flow<List<JournalEntryEntity>>
    
    @Query("""
        SELECT m.* FROM memories m
        INNER JOIN memory_entries me ON m.id = me.memoryId
        WHERE me.entryId = :entryId
    """)
    fun getMemoriesForEntry(entryId: Long): Flow<List<MemoryEntity>>
    
    @Query("SELECT COUNT(*) FROM memory_entries WHERE memoryId = :memoryId")
    suspend fun getEntryCountForMemory(memoryId: Long): Int
    
    // ============ MILESTONES ============
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: MemoryMilestoneEntity): Long
    
    @Update
    suspend fun updateMilestone(milestone: MemoryMilestoneEntity)
    
    @Query("DELETE FROM memory_milestones WHERE id = :id")
    suspend fun deleteMilestone(id: Long)
    
    @Query("SELECT * FROM memory_milestones WHERE memoryId = :memoryId ORDER BY milestoneDate ASC")
    fun getMilestonesForMemory(memoryId: Long): Flow<List<MemoryMilestoneEntity>>
    
    // ============ MEMORY TRACKERS ============
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoryTracker(tracker: MemoryTrackerEntity): Long
    
    @Update
    suspend fun updateMemoryTracker(tracker: MemoryTrackerEntity)
    
    @Query("DELETE FROM memory_trackers WHERE id = :id")
    suspend fun deleteMemoryTracker(id: Long)
    
    @Query("SELECT * FROM memory_trackers WHERE memoryId = :memoryId")
    fun getTrackersForMemory(memoryId: Long): Flow<List<MemoryTrackerEntity>>
    
    // ============ STATISTICS & AGGREGATIONS ============
    
    @Query("SELECT COUNT(*) FROM memories")
    suspend fun getTotalMemoryCount(): Int
    
    @Query("""
        SELECT SUM(entryCount) FROM memories
    """)
    suspend fun getTotalEntriesInMemories(): Int?
    
    @Query("""
        SELECT SUM(totalTimeSpent) FROM memories
    """)
    suspend fun getTotalTimeSpentInMemories(): Long?
    
    @Query("""
        SELECT * FROM memories 
        WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'
        ORDER BY startDate DESC
    """)
    fun searchMemories(query: String): Flow<List<MemoryEntity>>
    
    // Update memory statistics
    @Query("""
        UPDATE memories SET 
            entryCount = :entryCount,
            wordCount = :wordCount,
            photoCount = :photoCount,
            videoCount = :videoCount,
            updatedAt = :updatedAt
        WHERE id = :memoryId
    """)
    suspend fun updateMemoryStats(
        memoryId: Long,
        entryCount: Int,
        wordCount: Int,
        photoCount: Int,
        videoCount: Int,
        updatedAt: Long = System.currentTimeMillis()
    )
}
