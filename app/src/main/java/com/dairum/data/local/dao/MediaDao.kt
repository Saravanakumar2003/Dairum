package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.MediaEntity
import com.dairum.data.local.entity.MediaType
import com.dairum.data.local.entity.MediaWithEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(media: MediaEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(media: List<MediaEntity>): List<Long>
    
    @Update
    suspend fun update(media: MediaEntity)
    
    @Delete
    suspend fun delete(media: MediaEntity)
    
    @Query("DELETE FROM media WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM media WHERE entryId = :entryId")
    suspend fun deleteAllForEntry(entryId: Long)
    
    @Query("SELECT * FROM media WHERE id = :id")
    suspend fun getById(id: Long): MediaEntity?
    
    @Query("SELECT * FROM media WHERE entryId = :entryId ORDER BY sortOrder ASC")
    suspend fun getMediaForEntry(entryId: Long): List<MediaEntity>
    
    @Query("SELECT * FROM media WHERE entryId = :entryId ORDER BY sortOrder ASC")
    fun getMediaForEntryFlow(entryId: Long): Flow<List<MediaEntity>>
    
    // By type
    @Query("SELECT * FROM media WHERE type = :type ORDER BY createdAt DESC")
    fun getMediaByTypeFlow(type: MediaType): Flow<List<MediaEntity>>
    
    @Query("""
        SELECT m.*, e.title as entryTitle, e.createdAt as entryCreatedAt
        FROM media m
        INNER JOIN journal_entries e ON m.entryId = e.id
        WHERE m.type = :type
        ORDER BY m.createdAt DESC
    """)
    fun getMediaWithEntryByTypeFlow(type: MediaType): Flow<List<MediaWithEntry>>
    
    @Query("""
        SELECT m.*, e.title as entryTitle, e.createdAt as entryCreatedAt
        FROM media m
        INNER JOIN journal_entries e ON m.entryId = e.id
        WHERE m.type = :type
        ORDER BY m.createdAt DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getMediaWithEntryByTypePaginated(type: MediaType, limit: Int, offset: Int): List<MediaWithEntry>
    
    // All media with entry info
    @Query("""
        SELECT m.*, e.title as entryTitle, e.createdAt as entryCreatedAt
        FROM media m
        INNER JOIN journal_entries e ON m.entryId = e.id
        ORDER BY m.createdAt DESC
    """)
    fun getAllMediaWithEntryFlow(): Flow<List<MediaWithEntry>>
    
    // Counts
    @Query("SELECT COUNT(*) FROM media WHERE type = :type")
    suspend fun getCountByType(type: MediaType): Int
    
    @Query("SELECT COUNT(*) FROM media")
    suspend fun getTotalCount(): Int
    
    @Query("SELECT SUM(sizeBytes) FROM media")
    suspend fun getTotalSize(): Long?
    
    @Query("SELECT SUM(sizeBytes) FROM media WHERE type = :type")
    suspend fun getSizeByType(type: MediaType): Long?
    
    // First media for entry preview
    @Query("""
        SELECT * FROM media 
        WHERE entryId = :entryId 
        ORDER BY sortOrder ASC 
        LIMIT 1
    """)
    suspend fun getFirstMediaForEntry(entryId: Long): MediaEntity?
    
    // Update sort order
    @Query("UPDATE media SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: Long, sortOrder: Int)
}
