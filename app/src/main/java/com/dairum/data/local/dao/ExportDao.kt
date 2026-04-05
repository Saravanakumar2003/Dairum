package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.ExportConfigEntity
import com.dairum.data.local.entity.ExportHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExportDao {
    
    // Export Config CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ExportConfigEntity): Long
    
    @Update
    suspend fun updateConfig(config: ExportConfigEntity)
    
    @Delete
    suspend fun deleteConfig(config: ExportConfigEntity)
    
    @Query("SELECT * FROM export_configs WHERE id = :id")
    suspend fun getConfigById(id: Long): ExportConfigEntity?
    
    @Query("SELECT * FROM export_configs ORDER BY createdAt ASC")
    fun getAllConfigsFlow(): Flow<List<ExportConfigEntity>>
    
    @Query("SELECT * FROM export_configs ORDER BY createdAt ASC")
    suspend fun getAllConfigs(): List<ExportConfigEntity>
    
    @Query("SELECT * FROM export_configs WHERE isEnabled = 1")
    suspend fun getEnabledConfigs(): List<ExportConfigEntity>
    
    @Query("UPDATE export_configs SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun updateConfigEnabled(id: Long, isEnabled: Boolean)
    
    @Query("UPDATE export_configs SET lastExportAt = :timestamp, lastExportStatus = :status WHERE id = :id")
    suspend fun updateLastExport(id: Long, timestamp: Long, status: String)
    
    // Export History CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: ExportHistoryEntity): Long
    
    @Delete
    suspend fun deleteHistory(history: ExportHistoryEntity)
    
    @Query("DELETE FROM export_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Long)
    
    @Query("DELETE FROM export_history WHERE exportedAt < :beforeTimestamp")
    suspend fun deleteHistoryBefore(beforeTimestamp: Long)
    
    @Query("SELECT * FROM export_history ORDER BY exportedAt DESC")
    fun getAllHistoryFlow(): Flow<List<ExportHistoryEntity>>
    
    @Query("SELECT * FROM export_history ORDER BY exportedAt DESC LIMIT :limit")
    suspend fun getRecentHistory(limit: Int): List<ExportHistoryEntity>
    
    @Query("SELECT * FROM export_history WHERE configId = :configId ORDER BY exportedAt DESC")
    fun getHistoryForConfigFlow(configId: Long): Flow<List<ExportHistoryEntity>>
    
    @Query("SELECT * FROM export_history WHERE isSuccess = 1 ORDER BY exportedAt DESC LIMIT 1")
    suspend fun getLastSuccessfulExport(): ExportHistoryEntity?
    
    @Query("SELECT COUNT(*) FROM export_history")
    suspend fun getHistoryCount(): Int
    
    @Query("SELECT SUM(fileSize) FROM export_history WHERE isSuccess = 1")
    suspend fun getTotalExportSize(): Long?
}
