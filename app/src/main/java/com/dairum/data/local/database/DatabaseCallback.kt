package com.dairum.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dairum.data.local.entity.CategoryEntity
import com.dairum.data.local.entity.CustomTrackerEntity
import com.dairum.data.local.entity.TrackerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Database callback to prepopulate initial data
 */
class DatabaseCallback(
    private val context: Context
) : RoomDatabase.Callback() {
    
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        
        CoroutineScope(Dispatchers.IO).launch {
            val database = Room.databaseBuilder(
                context.applicationContext,
                DairumDatabase::class.java,
                DairumDatabase.DATABASE_NAME
            ).build()
            
            // Insert default categories
            val categoryDao = database.categoryDao()
            val defaultCategories = getDefaultCategories()
            defaultCategories.forEachIndexed { index, category ->
                categoryDao.insert(category.copy(sortOrder = index))
            }
            
            // Insert built-in trackers
            val trackerDao = database.trackerDao()
            val builtInTrackers = getBuiltInTrackers()
            builtInTrackers.forEachIndexed { index, tracker ->
                trackerDao.insertTracker(tracker.copy(sortOrder = index))
            }
        }
    }
    
    private fun getDefaultCategories(): List<CategoryEntity> = listOf(
        CategoryEntity(
            name = "General",
            color = 0xFF6750A4.toInt(), // Purple
            icon = "edit_note",
            isDefault = true
        ),
        CategoryEntity(
            name = "Personal",
            color = 0xFF7D5260.toInt(), // Pink
            icon = "person"
        ),
        CategoryEntity(
            name = "Work",
            color = 0xFF625B71.toInt(), // Purple Grey
            icon = "work"
        ),
        CategoryEntity(
            name = "Travel",
            color = 0xFF006D77.toInt(), // Teal
            icon = "flight"
        ),
        CategoryEntity(
            name = "Health",
            color = 0xFF83C5BE.toInt(), // Light Teal
            icon = "favorite"
        ),
        CategoryEntity(
            name = "Goals",
            color = 0xFFE29578.toInt(), // Peach
            icon = "flag"
        )
    )
    
    private fun getBuiltInTrackers(): List<CustomTrackerEntity> = listOf(
        CustomTrackerEntity(
            name = "People",
            type = TrackerType.PEOPLE,
            icon = "group",
            isBuiltIn = true
        ),
        CustomTrackerEntity(
            name = "Weight",
            type = TrackerType.WEIGHT,
            icon = "monitor_weight",
            unit = "kg",
            minValue = 20.0,
            maxValue = 300.0,
            isBuiltIn = true
        )
    )
}
