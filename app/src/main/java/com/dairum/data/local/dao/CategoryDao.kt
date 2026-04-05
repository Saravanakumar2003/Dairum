package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.CategoryEntity
import com.dairum.data.local.entity.CategoryWithCount
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long
    
    @Update
    suspend fun update(category: CategoryEntity)
    
    @Delete
    suspend fun delete(category: CategoryEntity)
    
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: Long): CategoryEntity?
    
    @Query("SELECT * FROM categories ORDER BY sortOrder ASC, name ASC")
    fun getAllFlow(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories ORDER BY sortOrder ASC, name ASC")
    suspend fun getAll(): List<CategoryEntity>
    
    @Query("SELECT * FROM categories WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefault(): CategoryEntity?
    
    @Query("UPDATE categories SET isDefault = 0")
    suspend fun clearDefaultFlag()
    
    @Query("UPDATE categories SET isDefault = 1 WHERE id = :id")
    suspend fun setAsDefault(id: Long)
    
    @Query("""
        SELECT c.*, COUNT(e.id) as entryCount 
        FROM categories c
        LEFT JOIN journal_entries e ON c.id = e.categoryId AND e.isArchived = 0 AND e.isDraft = 0
        GROUP BY c.id
        ORDER BY c.sortOrder ASC, c.name ASC
    """)
    fun getAllWithCountFlow(): Flow<List<CategoryWithCount>>
    
    @Query("""
        SELECT c.*, COUNT(e.id) as entryCount 
        FROM categories c
        LEFT JOIN journal_entries e ON c.id = e.categoryId AND e.isArchived = 0 AND e.isDraft = 0
        GROUP BY c.id
        ORDER BY entryCount DESC
        LIMIT :limit
    """)
    suspend fun getTopCategories(limit: Int): List<CategoryWithCount>
    
    @Query("SELECT MAX(sortOrder) FROM categories")
    suspend fun getMaxSortOrder(): Int?
    
    @Query("UPDATE categories SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: Long, sortOrder: Int)
}
