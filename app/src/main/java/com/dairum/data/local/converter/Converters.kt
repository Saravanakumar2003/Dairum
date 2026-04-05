package com.dairum.data.local.converter

import androidx.room.TypeConverter
import com.dairum.data.local.entity.ExportFormat
import com.dairum.data.local.entity.MediaType
import com.dairum.data.local.entity.TrackerType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    
    // LocalDateTime converters
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? = 
        value?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    
    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? = 
        value?.let { 
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime() 
        }
    
    // MediaType converters
    @TypeConverter
    fun fromMediaType(value: MediaType): String = value.name
    
    @TypeConverter
    fun toMediaType(value: String): MediaType = MediaType.valueOf(value)
    
    // TrackerType converters
    @TypeConverter
    fun fromTrackerType(value: TrackerType): String = value.name
    
    @TypeConverter
    fun toTrackerType(value: String): TrackerType = TrackerType.valueOf(value)
    
    // ExportFormat converters
    @TypeConverter
    fun fromExportFormat(value: ExportFormat): String = value.name
    
    @TypeConverter
    fun toExportFormat(value: String): ExportFormat = ExportFormat.valueOf(value)
    
    // List<Long> converters (for storing arrays of IDs)
    @TypeConverter
    fun fromLongList(value: List<Long>?): String? = value?.joinToString(",")
    
    @TypeConverter
    fun toLongList(value: String?): List<Long>? = value?.split(",")?.mapNotNull { it.toLongOrNull() }
    
    // List<String> converters
    @TypeConverter
    fun fromStringList(value: List<String>?): String? = value?.joinToString("|||")
    
    @TypeConverter
    fun toStringList(value: String?): List<String>? = value?.split("|||")?.filter { it.isNotEmpty() }
}
