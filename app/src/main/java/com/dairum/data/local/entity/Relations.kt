package com.dairum.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Entry with all related data
 */
data class EntryWithDetails(
    @Embedded
    val entry: JournalEntryEntity,
    
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "entryId"
    )
    val media: List<MediaEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = EntryTagCrossRef::class,
            parentColumn = "entryId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = EntryPersonCrossRef::class,
            parentColumn = "entryId",
            entityColumn = "personId"
        )
    )
    val people: List<PersonEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "entryId"
    )
    val trackerValues: List<TrackerValueEntity>
)

/**
 * Category with entry count
 */
data class CategoryWithCount(
    @Embedded
    val category: CategoryEntity,
    val entryCount: Int
)

/**
 * Tag with entry count
 */
data class TagWithCount(
    @Embedded
    val tag: TagEntity,
    val entryCount: Int
)

/**
 * Person with entry count
 */
data class PersonWithCount(
    @Embedded
    val person: PersonEntity,
    val entryCount: Int
)

/**
 * Media with entry info
 */
data class MediaWithEntry(
    @Embedded
    val media: MediaEntity,
    val entryTitle: String?,
    val entryCreatedAt: Long
)

/**
 * Entry summary for list views
 */
data class EntrySummary(
    val id: Long,
    val title: String?,
    val plainContent: String,
    val categoryId: Long?,
    val categoryName: String?,
    val categoryColor: Int?,
    val createdAt: Long,
    val moodLevel: Int?,
    val moodIcon: String?,
    val hasMedia: Boolean,
    val mediaCount: Int,
    val firstMediaUri: String?,
    val firstMediaType: MediaType?,
    val tagCount: Int,
    val isFavorite: Boolean,
    val weatherCondition: String?,
    val weatherIcon: String?,
    val locationName: String?
)

/**
 * Stats aggregation
 */
data class EntryStats(
    val totalEntries: Int,
    val entriesThisWeek: Int,
    val entriesThisMonth: Int,
    val entriesThisYear: Int,
    val averageWordCount: Double,
    val totalWordCount: Int,
    val totalPhotos: Int,
    val totalVideos: Int,
    val totalVoiceNotes: Int,
    val totalAttachments: Int
)

/**
 * Mood distribution
 */
data class MoodDistribution(
    val moodLevel: Int,
    val count: Int,
    val percentage: Float
)

/**
 * Entries per day for chart
 */
data class DailyEntryCount(
    val date: Long, // Start of day timestamp
    val count: Int
)

/**
 * Hourly activity distribution
 */
data class HourlyActivity(
    val hour: Int, // 0-23
    val count: Int
)

/**
 * Day of week activity
 */
data class WeekdayActivity(
    val dayOfWeek: Int, // 1-7 (Monday-Sunday)
    val count: Int
)
