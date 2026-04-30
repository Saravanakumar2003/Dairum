package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.EntryPersonCrossRef
import com.dairum.data.local.entity.PersonEntity
import com.dairum.data.local.entity.PersonWithCount
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: PersonEntity): Long
    
    @Update
    suspend fun update(person: PersonEntity)
    
    @Delete
    suspend fun delete(person: PersonEntity)
    
    @Query("DELETE FROM people WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT * FROM people WHERE id = :id")
    suspend fun getById(id: Long): PersonEntity?
    
    @Query("SELECT * FROM people ORDER BY isFavorite DESC, mentionCount DESC, name ASC")
    fun getAllFlow(): Flow<List<PersonEntity>>
    
    @Query("SELECT * FROM people ORDER BY mentionCount DESC, name ASC")
    suspend fun getAll(): List<PersonEntity>
    
    @Query("SELECT * FROM people WHERE name LIKE '%' || :query || '%' ORDER BY mentionCount DESC LIMIT :limit")
    suspend fun search(query: String, limit: Int = 10): List<PersonEntity>
    
    @Query("""
        SELECT p.*, COUNT(ep.entryId) as entryCount 
        FROM people p
        LEFT JOIN entry_people ep ON p.id = ep.personId
        LEFT JOIN journal_entries e ON ep.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0
        GROUP BY p.id
        ORDER BY entryCount DESC, p.name ASC
    """)
    fun getAllWithCountFlow(): Flow<List<PersonWithCount>>
    
    @Query("""
        SELECT p.*, COUNT(ep.entryId) as entryCount 
        FROM people p
        LEFT JOIN entry_people ep ON p.id = ep.personId
        LEFT JOIN journal_entries e ON ep.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0
        GROUP BY p.id
        ORDER BY entryCount DESC
        LIMIT :limit
    """)
    suspend fun getTopPeople(limit: Int): List<PersonWithCount>
    
    @Query("""
        SELECT p.*, COUNT(ep.entryId) as entryCount 
        FROM people p
        LEFT JOIN entry_people ep ON p.id = ep.personId
        LEFT JOIN journal_entries e ON ep.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0
            AND e.createdAt >= :startTime
        GROUP BY p.id
        ORDER BY entryCount DESC
        LIMIT :limit
    """)
    suspend fun getTopPeopleSince(limit: Int, startTime: Long): List<PersonWithCount>
    
    @Query("UPDATE people SET mentionCount = mentionCount + 1 WHERE id = :id")
    suspend fun incrementMentionCount(id: Long)
    
    @Query("UPDATE people SET mentionCount = mentionCount - 1 WHERE id = :id AND mentionCount > 0")
    suspend fun decrementMentionCount(id: Long)
    
    @Query("UPDATE people SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)
    
    // Entry-Person relationship
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntryPerson(crossRef: EntryPersonCrossRef)
    
    @Delete
    suspend fun deleteEntryPerson(crossRef: EntryPersonCrossRef)
    
    @Query("DELETE FROM entry_people WHERE entryId = :entryId")
    suspend fun deleteAllPeopleForEntry(entryId: Long)
    
    @Query("""
        SELECT p.* FROM people p
        INNER JOIN entry_people ep ON p.id = ep.personId
        WHERE ep.entryId = :entryId
    """)
    suspend fun getPeopleForEntry(entryId: Long): List<PersonEntity>
    
    @Query("""
        SELECT p.* FROM people p
        INNER JOIN entry_people ep ON p.id = ep.personId
        WHERE ep.entryId = :entryId
    """)
    fun getPeopleForEntryFlow(entryId: Long): Flow<List<PersonEntity>>
    
    // Favorite people (for notification reminder checks)
    @Query("SELECT * FROM people WHERE isFavorite = 1 ORDER BY name ASC")
    suspend fun getFavoritePeople(): List<PersonEntity>
}
