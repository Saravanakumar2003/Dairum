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
        WHERE isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
    """)
    fun getAllEntriesFlow(): Flow<List<JournalEntryEntity>>
    
    @Transaction
    @Query("""
        SELECT * FROM journal_entries 
        WHERE isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
    """)
    fun getAllEntriesWithDetailsFlow(): Flow<List<EntryWithDetails>>
    
    @Query("""
        SELECT * FROM journal_entries 
        WHERE isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getEntriesPaginated(limit: Int, offset: Int): List<JournalEntryEntity>
    
    // Category view
    @Query("""
        SELECT * FROM journal_entries 
        WHERE categoryId = :categoryId AND isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
    """)
    fun getEntriesByCategoryFlow(categoryId: Long): Flow<List<JournalEntryEntity>>
    
    // Mood view
    @Query("""
        SELECT * FROM journal_entries 
        WHERE moodLevel = :moodLevel AND isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
    """)
    fun getEntriesByMoodFlow(moodLevel: Int): Flow<List<JournalEntryEntity>>
    
    // Favorites
    @Query("""
        SELECT * FROM journal_entries 
        WHERE isFavorite = 1 AND isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
    """)
    fun getFavoriteEntriesFlow(): Flow<List<JournalEntryEntity>>
    
    @Query("UPDATE journal_entries SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)
    
    // Drafts
    @Query("SELECT * FROM journal_entries WHERE isDraft = 1 ORDER BY updatedAt DESC")
    fun getDraftsFlow(): Flow<List<JournalEntryEntity>>
    
    // Archived
    @Query("SELECT * FROM journal_entries WHERE isArchived = 1 ORDER BY updatedAt DESC")
    fun getArchivedEntriesFlow(): Flow<List<JournalEntryEntity>>
    
    @Query("UPDATE journal_entries SET isArchived = :isArchived WHERE id = :id")
    suspend fun updateArchived(id: Long, isArchived: Boolean)
    
    @Query("UPDATE journal_entries SET isPinned = :isPinned WHERE id = :id")
    suspend fun updatePinned(id: Long, isPinned: Boolean)
    
    // Search
    @Query("""
        SELECT * FROM journal_entries 
        WHERE (title LIKE '%' || :query || '%' OR plainContent LIKE '%' || :query || '%')
        AND isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
    """)
    fun searchEntries(query: String): Flow<List<JournalEntryEntity>>
    
    // Date range queries
    @Query("""
        SELECT * FROM journal_entries 
        WHERE createdAt >= :startTime AND createdAt < :endTime
        AND isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
    """)
    fun getEntriesInDateRange(startTime: Long, endTime: Long): Flow<List<JournalEntryEntity>>
    
    @Query("""
        SELECT * FROM journal_entries 
        WHERE createdAt >= :startTime AND createdAt < :endTime
        AND isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
    """)
    suspend fun getEntriesInDateRangeSync(startTime: Long, endTime: Long): List<JournalEntryEntity>
    
    // Story view - "On This Day" queries
    @Query("""
        SELECT * FROM journal_entries 
        WHERE strftime('%m-%d', createdAt/1000, 'unixepoch', 'localtime') = :monthDay
        AND isArchived = 0 AND isDraft = 0
        ORDER BY createdAt DESC
    """)
    fun getEntriesOnThisDay(monthDay: String): Flow<List<JournalEntryEntity>>
    
    // Statistics
    @Query("SELECT COUNT(*) FROM journal_entries WHERE isArchived = 0 AND isDraft = 0")
    suspend fun getTotalCount(): Int
    
    @Query("SELECT COUNT(*) FROM journal_entries")
    suspend fun getEntryCount(): Int
    
    @Query("""
        SELECT COUNT(*) FROM journal_entries 
        WHERE createdAt >= :startTime AND isArchived = 0 AND isDraft = 0
    """)
    suspend fun getCountSince(startTime: Long): Int
    
    @Query("""
        SELECT AVG(wordCount) FROM journal_entries 
        WHERE isArchived = 0 AND isDraft = 0
    """)
    suspend fun getAverageWordCount(): Double?
    
    @Query("""
        SELECT SUM(wordCount) FROM journal_entries 
        WHERE isArchived = 0 AND isDraft = 0
    """)
    suspend fun getTotalWordCount(): Int?
    
    // Mood distribution
    @Query("""
        SELECT moodLevel, COUNT(*) as count 
        FROM journal_entries 
        WHERE moodLevel IS NOT NULL AND isArchived = 0 AND isDraft = 0
        GROUP BY moodLevel
        ORDER BY moodLevel
    """)
    suspend fun getMoodDistribution(): List<MoodCount>
    
    // Daily counts for charts
    @Query("""
        SELECT date(createdAt/1000, 'unixepoch', 'localtime') as date, COUNT(*) as count
        FROM journal_entries
        WHERE createdAt >= :startTime AND isArchived = 0 AND isDraft = 0
        GROUP BY date
        ORDER BY date
    """)
    suspend fun getDailyCounts(startTime: Long): List<DateCount>
    
    // Hourly distribution
    @Query("""
        SELECT CAST(strftime('%H', createdAt/1000, 'unixepoch', 'localtime') AS INTEGER) as hour, 
               COUNT(*) as count
        FROM journal_entries
        WHERE isArchived = 0 AND isDraft = 0
        GROUP BY hour
        ORDER BY hour
    """)
    suspend fun getHourlyDistribution(): List<HourCount>
    
    // Day of week distribution
    @Query("""
        SELECT CAST(strftime('%w', createdAt/1000, 'unixepoch', 'localtime') AS INTEGER) as dayOfWeek,
               COUNT(*) as count
        FROM journal_entries
        WHERE isArchived = 0 AND isDraft = 0
        GROUP BY dayOfWeek
        ORDER BY dayOfWeek
    """)
    suspend fun getWeekdayDistribution(): List<WeekdayCount>
    
    // Streak calculation helper - get distinct dates with entries
    @Query("""
        SELECT DISTINCT date(createdAt/1000, 'unixepoch', 'localtime') as date
        FROM journal_entries
        WHERE isArchived = 0 AND isDraft = 0
        ORDER BY date DESC
    """)
    suspend fun getDistinctEntryDates(): List<String>
    
    // Entry count between dates (for notification checks)
    @Query("""
        SELECT COUNT(*) FROM journal_entries 
        WHERE createdAt >= :startTime AND createdAt < :endTime
        AND isArchived = 0 AND isDraft = 0
    """)
    suspend fun getEntryCountBetween(startTime: Long, endTime: Long): Int
}

data class MoodCount(val moodLevel: Int, val count: Int)
data class DateCount(val date: String, val count: Int)
data class HourCount(val hour: Int, val count: Int)
data class WeekdayCount(val dayOfWeek: Int, val count: Int)
