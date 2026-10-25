package com.dairum.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dairum.data.local.entity.LocationNicknameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationNicknameDao {

    @Query("SELECT * FROM location_nicknames ORDER BY updatedAt DESC")
    fun getAllFlow(): Flow<List<LocationNicknameEntity>>

    @Query("SELECT * FROM location_nicknames ORDER BY updatedAt DESC")
    suspend fun getAll(): List<LocationNicknameEntity>

    @Query("SELECT * FROM location_nicknames WHERE locationKey = :key LIMIT 1")
    suspend fun getByKey(key: String): LocationNicknameEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: LocationNicknameEntity)

    @Query("DELETE FROM location_nicknames WHERE locationKey = :key")
    suspend fun deleteByKey(key: String)
}
