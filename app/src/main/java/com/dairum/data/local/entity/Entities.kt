package com.dairum.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dairum.domain.model.NotificationPriority
import com.dairum.domain.model.ReminderType
import com.dairum.domain.model.SmartReminderType

/**
 * Core journal entry entity
 */
@Entity(
    tableName = "journal_entries",
    indices = [
        Index(value = ["categoryId"]),
        Index(value = ["createdAt"]),
        Index(value = ["moodLevel"]),
        Index(value = ["isFavorite"]),
        Index(value = ["isArchived"])
    ]
)
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String? = null,
    val content: String = "", // Rich text stored as JSON
    val plainContent: String = "", // Plain text for search
    val categoryId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val moodLevel: Int? = null, // 1-5 scale (1=terrible, 5=amazing)
    val moodIcon: String? = null, // Custom emoji/icon
    val weatherData: String? = null, // JSON: {temp, condition, icon, humidity, location}
    val locationData: String? = null, // JSON: {lat, lng, address, placeId, placeName}
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val isDraft: Boolean = false,
    val isPinned: Boolean = false,
    val wordCount: Int = 0,
    val backgroundColor: String? = null // Background color name
)

/**
 * Category for organizing entries
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: Int, // Color as ARGB int
    val icon: String = "folder", // Material icon name
    val sortOrder: Int = 0,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Tag for labeling entries
 */
@Entity(
    tableName = "tags",
    indices = [Index(value = ["name"], unique = true)]
)
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: Int? = null,
    val usageCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Junction table for entry-tag many-to-many relationship
 */
@Entity(
    tableName = "entry_tags",
    primaryKeys = ["entryId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = JournalEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["entryId"]),
        Index(value = ["tagId"])
    ]
)
data class EntryTagCrossRef(
    val entryId: Long,
    val tagId: Long
)

/**
 * Media attachments (photos, videos, voice notes, files)
 */
@Entity(
    tableName = "media",
    foreignKeys = [
        ForeignKey(
            entity = JournalEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["entryId"]),
        Index(value = ["type"]),
        Index(value = ["createdAt"])
    ]
)
data class MediaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entryId: Long,
    val type: MediaType,
    val uri: String,
    val thumbnailUri: String? = null,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val duration: Long? = null, // For video/voice in milliseconds
    val width: Int? = null, // For images/videos
    val height: Int? = null,
    val sortOrder: Int = 0,
    val caption: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class MediaType {
    PHOTO,
    VIDEO,
    VOICE,
    ATTACHMENT
}

/**
 * Custom tracker definition
 */
@Entity(tableName = "custom_trackers")
data class CustomTrackerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: TrackerType,
    val icon: String = "analytics",
    val unit: String? = null, // e.g., "kg", "hours", etc.
    val minValue: Double? = null,
    val maxValue: Double? = null,
    val options: String? = null, // JSON array for SELECT type
    val isBuiltIn: Boolean = false, // true for People, Weight, etc.
    val isActive: Boolean = true,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class TrackerType {
    TEXT,      // Free text input
    NUMBER,    // Numeric input
    RATING,    // 1-5 star rating
    BOOLEAN,   // Yes/No toggle
    SELECT,    // Single select from options
    PEOPLE,    // People selector (special handling)
    WEIGHT     // Weight with unit conversion
}

/**
 * Tracker value for an entry
 */
@Entity(
    tableName = "tracker_values",
    foreignKeys = [
        ForeignKey(
            entity = JournalEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CustomTrackerEntity::class,
            parentColumns = ["id"],
            childColumns = ["trackerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["entryId"]),
        Index(value = ["trackerId"])
    ]
)
data class TrackerValueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entryId: Long,
    val trackerId: Long,
    val value: String, // Stored as string, parsed based on tracker type
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Person for People tracker
 */
@Entity(
    tableName = "people",
    indices = [Index(value = ["name"])]
)
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val relationship: String? = null,
    val photoUri: String? = null,
    val notes: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val birthday: Long? = null, // Timestamp for birthday
    val importantDates: String? = null, // JSON: [{"label": "Anniversary", "date": 1234567890}, ...]
    val isFavorite: Boolean = false,
    val mentionCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Junction table for entry-person relationship
 */
@Entity(
    tableName = "entry_people",
    primaryKeys = ["entryId", "personId"],
    foreignKeys = [
        ForeignKey(
            entity = JournalEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["entryId"]),
        Index(value = ["personId"])
    ]
)
data class EntryPersonCrossRef(
    val entryId: Long,
    val personId: Long
)

/**
 * Export configuration
 */
@Entity(tableName = "export_configs")
data class ExportConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "Default",
    val format: ExportFormat,
    val scheduleHour: Int = 0, // 0-23
    val scheduleMinute: Int = 0, // 0-59
    val scheduleDays: String = "1,2,3,4,5,6,7", // Comma-separated day numbers (1=Monday)
    val isEnabled: Boolean = false,
    val exportPath: String? = null,
    val includeMedia: Boolean = true,
    val lastExportAt: Long? = null,
    val lastExportStatus: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ExportFormat {
    TXT,  // Plain text without images
    PDF,  // Formatted with images
    JSON, // Structured JSON export
    CSV   // Spreadsheet-compatible CSV
}

/**
 * Export history log
 */
@Entity(
    tableName = "export_history",
    indices = [Index(value = ["exportedAt"])]
)
data class ExportHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val configId: Long? = null,
    val format: ExportFormat,
    val filePath: String,
    val fileName: String,
    val entriesCount: Int,
    val fileSize: Long,
    val isSuccess: Boolean,
    val errorMessage: String? = null,
    val exportedAt: Long = System.currentTimeMillis()
)

// ============ NOTIFICATION ENTITIES ============

/**
 * Reminder notification configuration
 */
@Entity(tableName = "reminder_configs")
data class ReminderConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: ReminderType,
    val name: String,
    val description: String? = null,
    val isEnabled: Boolean = true,
    val scheduleHour: Int = 20, // 8 PM default
    val scheduleMinute: Int = 0,
    val scheduleDays: String = "1,2,3,4,5,6,7", // All days
    val notificationTitle: String,
    val notificationMessage: String,
    val priority: NotificationPriority = NotificationPriority.DEFAULT,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Smart reminder for special dates and stories
 */
@Entity(
    tableName = "smart_reminders",
    indices = [
        Index(value = ["reminderDate"]),
        Index(value = ["reminderType"])
    ]
)
data class SmartReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reminderType: SmartReminderType,
    val title: String,
    val description: String? = null,
    val reminderDate: Long, // Date to remind
    val repeatYearly: Boolean = false, // For birthdays, anniversaries
    val relatedPersonId: Long? = null, // Link to person
    val relatedEntryId: Long? = null, // Link to entry (for memories)
    val isEnabled: Boolean = true,
    val notifyDaysBefore: Int = 0, // 0 = on the day, 1 = day before, etc.
    val lastNotifiedAt: Long? = null,
    val metadata: String? = null, // JSON for extra data
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Notification history for tracking sent notifications
 */
@Entity(
    tableName = "notification_history",
    indices = [Index(value = ["sentAt"])]
)
data class NotificationHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "reminder" or "smart"
    val sourceId: Long, // ReminderConfig or SmartReminder ID
    val title: String,
    val message: String,
    val wasTapped: Boolean = false,
    val sentAt: Long = System.currentTimeMillis()
)

// ============ MEMORY ENTITIES ============

/**
 * Memory - a collection of related journal entries representing a significant event or period
 */
@Entity(
    tableName = "memories",
    indices = [
        Index(value = ["startDate"]),
        Index(value = ["endDate"]),
        Index(value = ["isFavorite"])
    ]
)
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val coverImageUri: String? = null, // Optional cover image
    val startDate: Long, // Memory start date
    val endDate: Long, // Memory end date
    val location: String? = null, // Primary location
    val locationData: String? = null, // JSON: {lat, lng, address}
    val isFavorite: Boolean = false,
    val color: Int? = null, // Theme color for the memory
    val icon: String? = null, // Icon identifier
    val tags: String? = null, // Comma-separated tags
    val moodSummary: String? = null, // JSON: average mood, mood distribution
    val totalTimeSpent: Long = 0, // Calculated total time (in millis)
    val entryCount: Int = 0,
    val wordCount: Int = 0,
    val photoCount: Int = 0,
    val videoCount: Int = 0,
    val peopleInvolved: String? = null, // JSON array of person IDs
    val highlights: String? = null, // JSON array of highlight texts
    val trackerSummary: String? = null, // JSON: aggregated tracker data
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Junction table for Memory-Entry relationship
 */
@Entity(
    tableName = "memory_entries",
    primaryKeys = ["memoryId", "entryId"],
    foreignKeys = [
        ForeignKey(
            entity = MemoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["memoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = JournalEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["memoryId"]),
        Index(value = ["entryId"])
    ]
)
data class MemoryEntryCrossRef(
    val memoryId: Long,
    val entryId: Long,
    val sortOrder: Int = 0, // Order within the memory
    val addedAt: Long = System.currentTimeMillis()
)

/**
 * Memory milestone - significant moments within a memory
 */
@Entity(
    tableName = "memory_milestones",
    foreignKeys = [
        ForeignKey(
            entity = MemoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["memoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["memoryId"])]
)
data class MemoryMilestoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val memoryId: Long,
    val title: String,
    val description: String? = null,
    val milestoneDate: Long,
    val relatedEntryId: Long? = null,
    val icon: String? = null,
    val sortOrder: Int = 0
)

/**
 * Memory tracker configuration - custom trackers for memories
 */
@Entity(
    tableName = "memory_trackers",
    foreignKeys = [
        ForeignKey(
            entity = MemoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["memoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["memoryId"])]
)
data class MemoryTrackerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val memoryId: Long,
    val name: String,
    val type: MemoryTrackerType,
    val icon: String? = null,
    val unit: String? = null,
    val goalValue: String? = null, // Target value (if applicable)
    val currentValue: String? = null, // Aggregated current value
    val progress: Float = 0f // 0-1 progress towards goal
)

enum class MemoryTrackerType {
    COUNT,          // Count occurrences (e.g., number of hikes)
    DURATION,       // Total time spent
    DISTANCE,       // Total distance traveled
    EXPENSE,        // Money spent
    RATING,         // Average rating
    CUSTOM          // Custom metric
}

// ============ MOOD CHECK-IN CONFIG ============

/**
 * Configuration for periodic mood check-in reminders
 */
@Entity(tableName = "mood_checkin_config")
data class MoodCheckInConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isEnabled: Boolean = false,
    val intervalHours: Int = 4, // How often to prompt (in hours)
    val startHour: Int = 9, // Don't prompt before this hour (24h format)
    val endHour: Int = 22, // Don't prompt after this hour (24h format)
    val lastPromptAt: Long? = null, // Last time a mood check-in was prompted
    val enabledDays: String = "1,2,3,4,5,6,7", // Days of week to prompt (1=Mon, 7=Sun)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
