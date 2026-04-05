package com.dairum.domain.model

import java.time.LocalDateTime

/**
 * Domain model for a journal entry
 */
data class JournalEntry(
    val id: Long = 0,
    val title: String? = null,
    val content: RichContent = RichContent(),
    val plainContent: String = "",
    val category: Category? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val mood: Mood? = null,
    val weather: Weather? = null,
    val location: Location? = null,
    val media: List<Media> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val people: List<Person> = emptyList(),
    val trackerValues: Map<Long, String> = emptyMap(), // trackerId to value
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val isDraft: Boolean = false,
    val isPinned: Boolean = false,
    val wordCount: Int = 0
)

/**
 * Rich text content with formatting
 */
data class RichContent(
    val blocks: List<ContentBlock> = emptyList()
)

sealed class ContentBlock {
    data class Text(
        val text: String,
        val styles: Set<TextStyle> = emptySet()
    ) : ContentBlock()
    
    data class Heading(
        val text: String,
        val level: Int = 1 // 1-3
    ) : ContentBlock()
    
    data class BulletList(
        val items: List<String>
    ) : ContentBlock()
    
    data class NumberedList(
        val items: List<String>
    ) : ContentBlock()
    
    data class Quote(
        val text: String
    ) : ContentBlock()
    
    data class Link(
        val text: String,
        val url: String
    ) : ContentBlock()
    
    data class Timestamp(
        val timestamp: LocalDateTime,
        val format: TimestampFormat = TimestampFormat.TIME_ONLY
    ) : ContentBlock()
}

enum class TextStyle {
    BOLD, ITALIC, UNDERLINE, STRIKETHROUGH
}

enum class TimestampFormat {
    TIME_ONLY,      // "10:30 AM"
    DATE_TIME,      // "March 25, 2026 10:30 AM"
    RELATIVE        // "2 hours ago"
}

/**
 * Category domain model
 */
data class Category(
    val id: Long = 0,
    val name: String,
    val color: Int,
    val icon: String = "folder",
    val sortOrder: Int = 0,
    val isDefault: Boolean = false,
    val entryCount: Int = 0
)

/**
 * Tag domain model
 */
data class Tag(
    val id: Long = 0,
    val name: String,
    val color: Int? = null,
    val usageCount: Int = 0
)

/**
 * Mood domain model with extended emotions
 */
data class Mood(
    val level: Int, // 1-5 for basic intensity or mood type ID for extended moods
    val icon: String? = null,
    val moodType: MoodType = MoodType.BASIC
) {
    val label: String
        get() = when (moodType) {
            MoodType.BASIC -> when (level) {
                1 -> "Terrible"
                2 -> "Bad"
                3 -> "Okay"
                4 -> "Good"
                5 -> "Amazing"
                else -> "Unknown"
            }
            MoodType.EXTENDED -> MoodOption.fromId(level)?.label ?: "Unknown"
        }
    
    val emoji: String
        get() = when (moodType) {
            MoodType.BASIC -> when (level) {
                1 -> "😢"
                2 -> "😔"
                3 -> "😐"
                4 -> "🙂"
                5 -> "😄"
                else -> "❓"
            }
            MoodType.EXTENDED -> MoodOption.fromId(level)?.emoji ?: "❓"
        }
    
    companion object {
        val ALL = listOf(
            Mood(1), Mood(2), Mood(3), Mood(4), Mood(5)
        )
        
        val ALL_EXTENDED = MoodOption.values().map { 
            Mood(level = it.id, moodType = MoodType.EXTENDED) 
        }
        
        /**
         * Creates a Mood from database values, auto-detecting the moodType.
         * If the icon matches a MoodOption emoji or the level is >= 1 and has a valid MoodOption,
         * it's treated as EXTENDED. Otherwise, it defaults to BASIC (1-5 scale).
         */
        fun fromDb(level: Int, icon: String? = null): Mood {
            // Check if icon matches any MoodOption emoji
            val moodOption = icon?.let { emoji ->
                MoodOption.values().find { it.emoji == emoji }
            }
            
            return if (moodOption != null) {
                // Icon matched a MoodOption, use EXTENDED
                Mood(level = moodOption.id, icon = icon, moodType = MoodType.EXTENDED)
            } else {
                // Also check by level if it has a valid MoodOption
                val byLevel = MoodOption.fromId(level)
                if (byLevel != null) {
                    Mood(level = level, icon = icon ?: byLevel.emoji, moodType = MoodType.EXTENDED)
                } else {
                    // Default to BASIC (1-5 scale)
                    Mood(level = level, icon = icon, moodType = MoodType.BASIC)
                }
            }
        }
    }
}

enum class MoodType {
    BASIC,
    EXTENDED
}

/**
 * Extended mood options with emojis
 */
enum class MoodOption(
    val id: Int,
    val label: String,
    val emoji: String,
    val category: MoodCategory
) {
    // Positive emotions
    HAPPY(1, "Happy", "😊", MoodCategory.POSITIVE),
    AMAZING(2, "Amazing", "🤩", MoodCategory.POSITIVE),
    EXCITED(3, "Excited", "😆", MoodCategory.POSITIVE),
    GRATEFUL(4, "Grateful", "🙏", MoodCategory.POSITIVE),
    LOVE(5, "Love", "❤️", MoodCategory.POSITIVE),
    PEACEFUL(6, "Peaceful", "😌", MoodCategory.POSITIVE),
    HOPEFUL(7, "Hopeful", "🌟", MoodCategory.POSITIVE),
    PROUD(8, "Proud", "😤", MoodCategory.POSITIVE),
    CONTENT(9, "Content", "☺️", MoodCategory.POSITIVE),
    INSPIRED(10, "Inspired", "💡", MoodCategory.POSITIVE),
    
    // Neutral emotions
    NEUTRAL(11, "Neutral", "😐", MoodCategory.NEUTRAL),
    CALM(12, "Calm", "😶", MoodCategory.NEUTRAL),
    THOUGHTFUL(13, "Thoughtful", "🤔", MoodCategory.NEUTRAL),
    TIRED(14, "Tired", "😴", MoodCategory.NEUTRAL),
    BORED(15, "Bored", "🥱", MoodCategory.NEUTRAL),
    
    // Negative emotions
    SAD(16, "Sad", "😢", MoodCategory.NEGATIVE),
    ANXIOUS(17, "Anxious", "😰", MoodCategory.NEGATIVE),
    STRESSED(18, "Stressed", "😫", MoodCategory.NEGATIVE),
    ANGRY(19, "Angry", "😠", MoodCategory.NEGATIVE),
    FRUSTRATED(20, "Frustrated", "😤", MoodCategory.NEGATIVE),
    FEARFUL(21, "Fearful", "😨", MoodCategory.NEGATIVE),
    LONELY(22, "Lonely", "😔", MoodCategory.NEGATIVE),
    DISAPPOINTED(23, "Disappointed", "😞", MoodCategory.NEGATIVE),
    OVERWHELMED(24, "Overwhelmed", "🤯", MoodCategory.NEGATIVE),
    DEPRESSED(25, "Depressed", "😞", MoodCategory.NEGATIVE),
    
    // Mixed/Special emotions
    CONFUSED(26, "Confused", "😕", MoodCategory.MIXED),
    NERVOUS(27, "Nervous", "😬", MoodCategory.MIXED),
    SURPRISED(28, "Surprised", "😲", MoodCategory.MIXED),
    NOSTALGIC(29, "Nostalgic", "🥹", MoodCategory.MIXED),
    MOTIVATED(30, "Motivated", "💪", MoodCategory.POSITIVE);
    
    companion object {
        fun fromId(id: Int): MoodOption? = values().find { it.id == id }
        
        fun getByCategory(category: MoodCategory): List<MoodOption> = 
            values().filter { it.category == category }
    }
}

enum class MoodCategory {
    POSITIVE,
    NEUTRAL,
    NEGATIVE,
    MIXED
}

/**
 * Weather domain model
 */
data class Weather(
    val temperature: Double, // In Celsius
    val temperatureFahrenheit: Double,
    val condition: String, // e.g., "Sunny", "Cloudy"
    val icon: String, // URL or icon code
    val humidity: Int,
    val feelsLike: Double,
    val locationName: String? = null
)

/**
 * Location domain model
 */
data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val placeName: String? = null,
    val placeId: String? = null
)

/**
 * Media domain model
 */
data class Media(
    val id: Long = 0,
    val entryId: Long = 0,
    val type: MediaType,
    val uri: String,
    val thumbnailUri: String? = null,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val duration: Long? = null,
    val width: Int? = null,
    val height: Int? = null,
    val caption: String? = null,
    val sortOrder: Int = 0
)

enum class MediaType {
    PHOTO, VIDEO, VOICE, ATTACHMENT
}

/**
 * Person domain model (for People tracker)
 */
data class Person(
    val id: Long = 0,
    val name: String,
    val relationship: String? = null,
    val photoUri: String? = null,
    val notes: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val birthday: Long? = null, // Timestamp for birthday
    val importantDates: List<ImportantDate> = emptyList(),
    val isFavorite: Boolean = false,
    val mentionCount: Int = 0
)

/**
 * Important date for a person
 */
data class ImportantDate(
    val label: String,
    val date: Long // Timestamp
)

/**
 * Custom tracker domain model
 */
data class CustomTracker(
    val id: Long = 0,
    val name: String,
    val type: TrackerType,
    val icon: String = "analytics",
    val unit: String? = null,
    val minValue: Double? = null,
    val maxValue: Double? = null,
    val options: List<String>? = null, // For SELECT type
    val isBuiltIn: Boolean = false,
    val isActive: Boolean = true,
    val sortOrder: Int = 0
)

enum class TrackerType {
    TEXT, NUMBER, RATING, BOOLEAN, SELECT, PEOPLE, WEIGHT
}

/**
 * Export configuration
 */
data class ExportConfig(
    val id: Long = 0,
    val name: String = "Default",
    val format: ExportFormat,
    val scheduleHour: Int = 0,
    val scheduleMinute: Int = 0,
    val scheduleDays: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7),
    val isEnabled: Boolean = false,
    val exportPath: String? = null,
    val includeMedia: Boolean = true,
    val lastExportAt: LocalDateTime? = null,
    val lastExportStatus: String? = null
)

enum class ExportFormat {
    TXT, PDF
}

/**
 * Export history entry
 */
data class ExportHistory(
    val id: Long = 0,
    val configId: Long? = null,
    val format: ExportFormat,
    val filePath: String,
    val fileName: String,
    val entriesCount: Int,
    val fileSize: Long,
    val isSuccess: Boolean,
    val errorMessage: String? = null,
    val exportedAt: LocalDateTime
)

// ============ NOTIFICATION MODELS ============

/**
 * Reminder configuration domain model
 */
data class ReminderConfig(
    val id: Long = 0,
    val type: ReminderType,
    val name: String,
    val description: String? = null,
    val isEnabled: Boolean = true,
    val scheduleHour: Int = 20,
    val scheduleMinute: Int = 0,
    val scheduleDays: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7),
    val notificationTitle: String,
    val notificationMessage: String,
    val priority: NotificationPriority = NotificationPriority.DEFAULT
)

enum class ReminderType {
    DAILY_JOURNAL,
    WEEKLY_REFLECTION,
    CUSTOM
}

enum class NotificationPriority {
    LOW, DEFAULT, HIGH
}

/**
 * Smart reminder domain model
 */
data class SmartReminder(
    val id: Long = 0,
    val reminderType: SmartReminderType,
    val title: String,
    val description: String? = null,
    val reminderDate: LocalDateTime,
    val repeatYearly: Boolean = false,
    val relatedPersonId: Long? = null,
    val relatedEntryId: Long? = null,
    val isEnabled: Boolean = true,
    val notifyDaysBefore: Int = 0,
    val lastNotifiedAt: LocalDateTime? = null,
    val metadata: Map<String, String>? = null
)

enum class SmartReminderType {
    BIRTHDAY,
    ANNIVERSARY,
    MEMORY_ANNIVERSARY,
    SPECIAL_DATE,
    PERSON_MENTION,
    STREAK_RISK,
    MOOD_CHECK_IN
}

/**
 * Notification history domain model
 */
data class NotificationHistory(
    val id: Long = 0,
    val type: String,
    val sourceId: Long,
    val title: String,
    val message: String,
    val wasTapped: Boolean = false,
    val sentAt: LocalDateTime
)

// ============ MEMORY MODELS ============

/**
 * Memory - a collection of related journal entries
 */
data class Memory(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val coverImageUri: String? = null,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val location: String? = null,
    val locationData: Location? = null,
    val isFavorite: Boolean = false,
    val color: Int? = null,
    val icon: String? = null,
    val tags: List<String> = emptyList(),
    val moodSummary: MoodSummary? = null,
    val totalTimeSpent: Long = 0,
    val entryCount: Int = 0,
    val wordCount: Int = 0,
    val photoCount: Int = 0,
    val videoCount: Int = 0,
    val peopleInvolved: List<Long> = emptyList(),
    val highlights: List<String> = emptyList(),
    val trackerSummary: Map<String, String>? = null,
    val entries: List<JournalEntry> = emptyList(),
    val milestones: List<MemoryMilestone> = emptyList(),
    val trackers: List<MemoryTracker> = emptyList()
) {
    val duration: String
        get() {
            val days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1
            return when {
                days == 1L -> "1 day"
                days < 7 -> "$days days"
                days < 30 -> "${days / 7} weeks"
                days < 365 -> "${days / 30} months"
                else -> "${days / 365} years"
            }
        }
    
    val averageMood: Float?
        get() = moodSummary?.averageMood
}

/**
 * Mood summary for a memory
 */
data class MoodSummary(
    val averageMood: Float,
    val moodDistribution: Map<Int, Int>, // moodLevel to count
    val dominantMood: Int?
)

/**
 * Memory milestone
 */
data class MemoryMilestone(
    val id: Long = 0,
    val memoryId: Long,
    val title: String,
    val description: String? = null,
    val milestoneDate: LocalDateTime,
    val relatedEntryId: Long? = null,
    val icon: String? = null,
    val sortOrder: Int = 0
)

/**
 * Memory tracker
 */
data class MemoryTracker(
    val id: Long = 0,
    val memoryId: Long,
    val name: String,
    val type: MemoryTrackerType,
    val icon: String? = null,
    val unit: String? = null,
    val goalValue: String? = null,
    val currentValue: String? = null,
    val progress: Float = 0f
)

enum class MemoryTrackerType {
    COUNT,
    DURATION,
    DISTANCE,
    EXPENSE,
    RATING,
    CUSTOM
}

// ============ ENTRY TEMPLATES ============

/**
 * Entry template for quick entry creation
 */
data class EntryTemplate(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val icon: String = "📝",
    val category: Category? = null,
    val defaultTitle: String? = null,
    val promptText: String? = null,
    val tags: List<Tag> = emptyList(),
    val trackers: List<Long> = emptyList(),
    val isBuiltIn: Boolean = false,
    val isCustom: Boolean = false,
    val sortOrder: Int = 0
) {
    companion object {
        val BUILT_IN_TEMPLATES = listOf(
            EntryTemplate(
                id = -1,
                name = "Daily Gratitude",
                description = "Focus on what you're thankful for",
                icon = "🙏",
                defaultTitle = "Gratitude Journal",
                promptText = "3 things I'm grateful for today:\n\n1. \n2. \n3. \n\nWhy these matter to me:\n",
                isBuiltIn = true,
                sortOrder = 0
            ),
            EntryTemplate(
                id = -2,
                name = "Morning Pages",
                description = "Start your day with stream of consciousness writing",
                icon = "☀️",
                defaultTitle = "Morning Thoughts",
                promptText = "What's on my mind this morning?\n\nHow am I feeling?\n\nMy intentions for today:\n",
                isBuiltIn = true,
                sortOrder = 1
            ),
            EntryTemplate(
                id = -3,
                name = "Evening Reflection",
                description = "Review and reflect on your day",
                icon = "🌙",
                defaultTitle = "Evening Reflection",
                promptText = "Today's highlights:\n\nChallenges faced:\n\nLessons learned:\n\nTomorrow I will:\n",
                isBuiltIn = true,
                sortOrder = 2
            ),
            EntryTemplate(
                id = -4,
                name = "Weekly Review",
                description = "Summarize your week and plan ahead",
                icon = "📊",
                defaultTitle = "Weekly Review",
                promptText = "Week in Review\n\n✅ Accomplishments:\n\n⚠️ Challenges:\n\n📈 Goals for next week:\n\n💡 Key insights:\n",
                isBuiltIn = true,
                sortOrder = 3
            ),
            EntryTemplate(
                id = -5,
                name = "Mood Check-in",
                description = "Track and understand your emotional state",
                icon = "😊",
                defaultTitle = "Mood Check-in",
                promptText = "How am I feeling right now?\n\nWhat's contributing to this mood?\n\nWhat would make today better?\n",
                isBuiltIn = true,
                sortOrder = 4
            ),
            EntryTemplate(
                id = -6,
                name = "Meeting Notes",
                description = "Capture key points from meetings",
                icon = "📋",
                defaultTitle = "Meeting Notes",
                promptText = "Meeting: \n\nAttendees:\n\nAgenda:\n\nDiscussion:\n\nAction Items:\n- [ ] \n- [ ] \n",
                isBuiltIn = true,
                sortOrder = 5
            ),
            EntryTemplate(
                id = -7,
                name = "Travel Journal",
                description = "Document your travel experiences",
                icon = "✈️",
                defaultTitle = "Travel Journal",
                promptText = "📍 Location:\n\n🗓 Date:\n\n✨ Highlights:\n\n🍽 Food & Drinks:\n\n📸 Memories:\n",
                isBuiltIn = true,
                sortOrder = 6
            ),
            EntryTemplate(
                id = -8,
                name = "Dream Log",
                description = "Record and analyze your dreams",
                icon = "💭",
                defaultTitle = "Dream Log",
                promptText = "Dream Description:\n\nSymbols/Themes:\n\nEmotions felt:\n\nPossible meanings:\n",
                isBuiltIn = true,
                sortOrder = 7
            )
        )
    }
}
