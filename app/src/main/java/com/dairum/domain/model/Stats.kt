package com.dairum.domain.model

/**
 * Statistics models for the Stats screen
 */

data class JournalStats(
    val totalEntries: Int = 0,
    val entriesThisWeek: Int = 0,
    val entriesThisMonth: Int = 0,
    val entriesThisYear: Int = 0,
    val entriesInCustomRange: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val averageWordCount: Double = 0.0,
    val totalWordCount: Int = 0,
    val totalPhotos: Int = 0,
    val totalVideos: Int = 0,
    val totalVoiceNotes: Int = 0,
    val totalAttachments: Int = 0,
    val storageUsed: Long = 0, // bytes
    val totalStorageUsed: Long = 0, // bytes (for ViewModel compatibility)
    val comparisonToLastMonth: Float = 0f, // percentage change from last month
    // New insights
    val averageEntriesPerWeek: Double = 0.0,
    val consistencyScore: Float = 0f, // percentage of days with entries in last 30 days
    val daysSinceLastEntry: Int = 0,
    val bestWritingDayOfWeek: Int? = null, // 1-7 (Monday-Sunday)
    val writingTimePreference: WritingTimePreference = WritingTimePreference.UNKNOWN,
    val totalDaysJournaling: Int = 0,
    val entriesLastMonth: Int = 0, // for comparison with this month
    val monthlyGrowth: Float = 0f // percentage change from last month
)

enum class WritingTimePreference {
    EARLY_BIRD,    // Most entries between 5-11
    AFTERNOON,     // Most entries between 11-17
    NIGHT_OWL,     // Most entries between 17-23
    LATE_NIGHT,    // Most entries between 23-5
    UNKNOWN
}

data class MoodStats(
    val distribution: List<MoodDistributionItem>,
    val averageMood: Double,
    val moodTrend: List<MoodTrendPoint>,
    val mostCommonMood: Int?,
    val moodByDayOfWeek: Map<Int, Double> // dayOfWeek (1-7) to average mood
)

data class MoodDistributionItem(
    val level: Int,
    val count: Int,
    val percentage: Float
)

data class MoodTrendPoint(
    val date: Long, // timestamp
    val averageMood: Double
)

data class EntryActivityStats(
    val dailyCounts: List<DailyCount>,
    val hourlyDistribution: Map<Int, Int>, // hour (0-23) to count
    val weekdayDistribution: Map<Int, Int>, // dayOfWeek (1-7) to count
    val mostActiveHour: Int?,
    val mostActiveDay: Int?
)

data class DailyCount(
    val date: Long, // timestamp (start of day)
    val count: Int
)

data class TagStats(
    val topTags: List<TagUsage>,
    val totalTags: Int,
    val coOccurringTags: List<TagPair>
)

data class TagUsage(
    val tag: Tag,
    val count: Int,
    val percentage: Float
)

data class TagPair(
    val tag1: Tag,
    val tag2: Tag,
    val count: Int
)

data class PeopleStats(
    val topPeople: List<PersonMention>,
    val totalPeople: Int
)

data class PersonMention(
    val person: Person,
    val count: Int,
    val percentage: Float
)

data class TrackerStats(
    val tracker: CustomTracker,
    val values: List<TrackerDataPoint>,
    val average: Double?,
    val min: Double?,
    val max: Double?,
    val count: Int
)

data class TrackerDataPoint(
    val date: Long,
    val value: Double
)

data class CategoryStats(
    val distribution: List<CategoryUsage>,
    val totalCategories: Int
)

data class CategoryUsage(
    val category: Category,
    val count: Int,
    val percentage: Float
)
