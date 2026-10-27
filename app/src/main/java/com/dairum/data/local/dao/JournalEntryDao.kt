package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalEntryDao {
    
    // Insert/Update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntryEntity): Long
    
    @Update
    suspend fun update(entry: JournalEntryEntity)
    
    @Delete
    suspend fun delete(entry: JournalEntryEntity)
    
    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    // Basic queries
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getById(id: Long): JournalEntryEntity?
    
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<JournalEntryEntity?>
    
    @Transaction
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getWithDetails(id: Long): EntryWithDetails?
    
    @Transaction
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    fun getWithDetailsFlow(id: Long): Flow<EntryWithDetails?>
    
    // Timeline view - all entries sorted by date
    @Query("""
        SELECT * FROM journal_entries 
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    fun getAllEntriesFlow(): Flow<List<JournalEntryEntity>>
    
    @Transaction
    @Query("""
        SELECT * FROM journal_entries 
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    fun getAllEntriesWithDetailsFlow(): Flow<List<EntryWithDetails>>
    
    @Query("""
        SELECT * FROM journal_entries 
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getEntriesPaginated(limit: Int, offset: Int): List<JournalEntryEntity>
    
    // Category view
    @Query("""
        SELECT * FROM journal_entries 
        WHERE categoryId = :categoryId AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    fun getEntriesByCategoryFlow(categoryId: Long): Flow<List<JournalEntryEntity>>
    
    // Mood view
    @Query("""
        SELECT * FROM journal_entries 
        WHERE moodLevel = :moodLevel AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    fun getEntriesByMoodFlow(moodLevel: Int): Flow<List<JournalEntryEntity>>
    
    // Favorites
    @Query("""
        SELECT * FROM journal_entries 
        WHERE isFavorite = 1 AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    fun getFavoriteEntriesFlow(): Flow<List<JournalEntryEntity>>
    
    @Query("UPDATE journal_entries SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean, updatedAt: Long)
    
    // Drafts
    @Query("SELECT * FROM journal_entries WHERE isDraft = 1 AND deletedAt IS NULL ORDER BY updatedAt DESC")
    fun getDraftsFlow(): Flow<List<JournalEntryEntity>>
    
    // Archived
    @Query("SELECT * FROM journal_entries WHERE isArchived = 1 AND deletedAt IS NULL ORDER BY updatedAt DESC")
    fun getArchivedEntriesFlow(): Flow<List<JournalEntryEntity>>
    
    @Query("UPDATE journal_entries SET isArchived = :isArchived, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateArchived(id: Long, isArchived: Boolean, updatedAt: Long)
    
    @Query("UPDATE journal_entries SET isPinned = :isPinned, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updatePinned(id: Long, isPinned: Boolean, updatedAt: Long)
    
    // Search
    @Query("""
        SELECT * FROM journal_entries 
        WHERE (title LIKE '%' || :query || '%' OR plainContent LIKE '%' || :query || '%')
        AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    fun searchEntries(query: String): Flow<List<JournalEntryEntity>>
    
    // Date range queries
    @Query("""
        SELECT * FROM journal_entries 
        WHERE createdAt >= :startTime AND createdAt < :endTime
        AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    fun getEntriesInDateRange(startTime: Long, endTime: Long): Flow<List<JournalEntryEntity>>
    
    @Query("""
        SELECT * FROM journal_entries 
        WHERE createdAt >= :startTime AND createdAt < :endTime
        AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    suspend fun getEntriesInDateRangeSync(startTime: Long, endTime: Long): List<JournalEntryEntity>

    /**
     * Rows for the "Metadata for Kith" export: the same selection as the "All" export (no archived,
     * draft or trashed entries), oldest first, and only the columns that export may use.
     */
    @Transaction
    @Query("""
        SELECT id, createdAt, title, categoryId, moodLevel FROM journal_entries
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt ASC, id ASC
    """)
    suspend fun getRowsForMetadataExport(): List<EntryMetadataRow>

    /** Same selection as getEntriesInDateRange, with category, tags, people, media and tracker values. */
    @Transaction
    @Query("""
        SELECT * FROM journal_entries
        WHERE createdAt >= :startTime AND createdAt < :endTime
        AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    suspend fun getEntriesInDateRangeWithDetails(startTime: Long, endTime: Long): List<EntryWithDetails>
    
    // Story view - "On This Day" queries
    @Query("""
        SELECT * FROM journal_entries 
        WHERE strftime('%m-%d', createdAt/1000, 'unixepoch', 'localtime') = :monthDay
        AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY createdAt DESC
    """)
    fun getEntriesOnThisDay(monthDay: String): Flow<List<JournalEntryEntity>>
    
    // Statistics
    @Query("SELECT COUNT(*) FROM journal_entries WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL")
    suspend fun getTotalCount(): Int
    
    @Query("SELECT COUNT(*) FROM journal_entries")
    suspend fun getEntryCount(): Int
    
    @Query("""
        SELECT COUNT(*) FROM journal_entries 
        WHERE createdAt >= :startTime AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
    """)
    suspend fun getCountSince(startTime: Long): Int
    
    @Query("""
        SELECT AVG(wordCount) FROM journal_entries 
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
    """)
    suspend fun getAverageWordCount(): Double?
    
    @Query("""
        SELECT SUM(wordCount) FROM journal_entries 
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
    """)
    suspend fun getTotalWordCount(): Int?
    
    // Mood distribution
    @Query("""
        SELECT moodLevel, COUNT(*) as count 
        FROM journal_entries 
        WHERE moodLevel IS NOT NULL AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        GROUP BY moodLevel
        ORDER BY moodLevel
    """)
    suspend fun getMoodDistribution(): List<MoodCount>
    
    @Query("""
        SELECT moodLevel, COUNT(*) as count 
        FROM journal_entries 
        WHERE moodLevel IS NOT NULL AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        AND createdAt >= :startTime
        GROUP BY moodLevel
        ORDER BY moodLevel
    """)
    suspend fun getMoodDistributionSince(startTime: Long): List<MoodCount>
    
    // Daily counts for charts
    @Query("""
        SELECT date(createdAt/1000, 'unixepoch', 'localtime') as date, COUNT(*) as count
        FROM journal_entries
        WHERE createdAt >= :startTime AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        GROUP BY date
        ORDER BY date
    """)
    suspend fun getDailyCounts(startTime: Long): List<DateCount>
    
    // Hourly distribution
    @Query("""
        SELECT CAST(strftime('%H', createdAt/1000, 'unixepoch', 'localtime') AS INTEGER) as hour, 
               COUNT(*) as count
        FROM journal_entries
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        GROUP BY hour
        ORDER BY hour
    """)
    suspend fun getHourlyDistribution(): List<HourCount>
    
    @Query("""
        SELECT CAST(strftime('%H', createdAt/1000, 'unixepoch', 'localtime') AS INTEGER) as hour, 
               COUNT(*) as count
        FROM journal_entries
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL AND createdAt >= :startTime
        GROUP BY hour
        ORDER BY hour
    """)
    suspend fun getHourlyDistributionSince(startTime: Long): List<HourCount>
    
    // Day of week distribution
    @Query("""
        SELECT CAST(strftime('%w', createdAt/1000, 'unixepoch', 'localtime') AS INTEGER) as dayOfWeek,
               COUNT(*) as count
        FROM journal_entries
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        GROUP BY dayOfWeek
        ORDER BY dayOfWeek
    """)
    suspend fun getWeekdayDistribution(): List<WeekdayCount>
    
    @Query("""
        SELECT CAST(strftime('%w', createdAt/1000, 'unixepoch', 'localtime') AS INTEGER) as dayOfWeek,
               COUNT(*) as count
        FROM journal_entries
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL AND createdAt >= :startTime
        GROUP BY dayOfWeek
        ORDER BY dayOfWeek
    """)
    suspend fun getWeekdayDistributionSince(startTime: Long): List<WeekdayCount>
    
    // Streak calculation helper - get distinct dates with entries
    @Query("""
        SELECT DISTINCT date(createdAt/1000, 'unixepoch', 'localtime') as date
        FROM journal_entries
        WHERE isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
        ORDER BY date DESC
    """)
    suspend fun getDistinctEntryDates(): List<String>
    
    // Entry count between dates (for notification checks)
    @Query("""
        SELECT COUNT(*) FROM journal_entries 
        WHERE createdAt >= :startTime AND createdAt < :endTime
        AND isArchived = 0 AND isDraft = 0 AND deletedAt IS NULL
    """)
    suspend fun getEntryCountBetween(startTime: Long, endTime: Long): Int
    
    // Soft delete
    @Query("UPDATE journal_entries SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE id = :id")
    suspend fun softDelete(id: Long, deletedAt: Long)
    
    @Query("UPDATE journal_entries SET deletedAt = NULL, updatedAt = :updatedAt WHERE id = :id")
    suspend fun restoreEntry(id: Long, updatedAt: Long)

    /** Marks the entry as changed without touching its content (used when links or trackers change). */
    @Query("UPDATE journal_entries SET updatedAt = :updatedAt WHERE id = :id")
    suspend fun touch(id: Long, updatedAt: Long)
    
    // Trash - get all soft-deleted entries
    @Query("SELECT * FROM journal_entries WHERE deletedAt IS NOT NULL ORDER BY deletedAt DESC")
    fun getDeletedEntriesFlow(): Flow<List<JournalEntryEntity>>
    
    // Permanently delete entries older than 30 days in trash
    @Query("DELETE FROM journal_entries WHERE deletedAt IS NOT NULL AND deletedAt < :cutoff")
    suspend fun emptyOldTrash(cutoff: Long)
    
    // Permanently delete all trash
    @Query("DELETE FROM journal_entries WHERE deletedAt IS NOT NULL")
    suspend fun emptyTrash()
}

data class MoodCount(val moodLevel: Int, val count: Int)
data class DateCount(val date: String, val count: Int)
data class HourCount(val hour: Int, val count: Int)
data class WeekdayCount(val dayOfWeek: Int, val count: Int)
