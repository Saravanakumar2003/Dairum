package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.EntryTagCrossRef
import com.dairum.data.local.entity.TagEntity
import com.dairum.data.local.entity.TagWithCount
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: TagEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(tag: TagEntity): Long
    
    @Update
    suspend fun update(tag: TagEntity)
    
    @Delete
    suspend fun delete(tag: TagEntity)
    
    @Query("DELETE FROM tags WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getById(id: Long): TagEntity?
    
    @Query("SELECT * FROM tags WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): TagEntity?
    
    @Query("SELECT * FROM tags ORDER BY usageCount DESC, name ASC")
    fun getAllFlow(): Flow<List<TagEntity>>
    
    @Query("SELECT * FROM tags ORDER BY usageCount DESC, name ASC")
    suspend fun getAll(): List<TagEntity>
    
    @Query("SELECT * FROM tags WHERE name LIKE '%' || :query || '%' ORDER BY usageCount DESC LIMIT :limit")
    suspend fun search(query: String, limit: Int = 10): List<TagEntity>
    
    @Query("""
        SELECT t.*, COUNT(et.entryId) as entryCount 
        FROM tags t
        LEFT JOIN entry_tags et ON t.id = et.tagId
        LEFT JOIN journal_entries e ON et.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0
        GROUP BY t.id
        ORDER BY entryCount DESC, t.name ASC
    """)
    fun getAllWithCountFlow(): Flow<List<TagWithCount>>
    
    @Query("""
        SELECT t.*, COUNT(et.entryId) as entryCount 
        FROM tags t
        LEFT JOIN entry_tags et ON t.id = et.tagId
        LEFT JOIN journal_entries e ON et.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0
        GROUP BY t.id
        ORDER BY entryCount DESC
        LIMIT :limit
    """)
    suspend fun getTopTags(limit: Int): List<TagWithCount>
    
    @Query("""
        SELECT t.*, COUNT(et.entryId) as entryCount 
        FROM tags t
        LEFT JOIN entry_tags et ON t.id = et.tagId
        LEFT JOIN journal_entries e ON et.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0
            AND e.createdAt >= :startTime
        GROUP BY t.id
        ORDER BY entryCount DESC
        LIMIT :limit
    """)
    suspend fun getTopTagsSince(limit: Int, startTime: Long): List<TagWithCount>
    
    @Query("UPDATE tags SET usageCount = usageCount + 1 WHERE id = :id")
    suspend fun incrementUsageCount(id: Long)
    
    @Query("UPDATE tags SET usageCount = usageCount - 1 WHERE id = :id AND usageCount > 0")
    suspend fun decrementUsageCount(id: Long)
    
    // Entry-Tag relationship
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntryTag(crossRef: EntryTagCrossRef)
    
    @Delete
    suspend fun deleteEntryTag(crossRef: EntryTagCrossRef)
    
    @Query("DELETE FROM entry_tags WHERE entryId = :entryId")
    suspend fun deleteAllTagsForEntry(entryId: Long)
    
    @Query("SELECT tagId FROM entry_tags WHERE entryId = :entryId")
    suspend fun getTagIdsForEntry(entryId: Long): List<Long>
    
    @Query("""
        SELECT t.* FROM tags t
        INNER JOIN entry_tags et ON t.id = et.tagId
        WHERE et.entryId = :entryId
    """)
    suspend fun getTagsForEntry(entryId: Long): List<TagEntity>
    
    @Query("""
        SELECT t.* FROM tags t
        INNER JOIN entry_tags et ON t.id = et.tagId
        WHERE et.entryId = :entryId
    """)
    fun getTagsForEntryFlow(entryId: Long): Flow<List<TagEntity>>
    
    // Get entries by tag
    @Query("""
        SELECT e.id FROM journal_entries e
        INNER JOIN entry_tags et ON e.id = et.entryId
        WHERE et.tagId = :tagId AND e.isArchived = 0 AND e.isDraft = 0
    """)
    suspend fun getEntryIdsForTag(tagId: Long): List<Long>
}
