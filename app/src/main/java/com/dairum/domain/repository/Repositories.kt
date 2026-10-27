package com.dairum.domain.repository

import com.dairum.domain.model.*
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    
    // Entry CRUD
    suspend fun createEntry(entry: JournalEntry): Long
    suspend fun updateEntry(entry: JournalEntry)
    suspend fun deleteEntry(id: Long)
    suspend fun getEntry(id: Long): JournalEntry?
    fun getEntryFlow(id: Long): Flow<JournalEntry?>
    
    // Count
    suspend fun getEntryCount(): Int
    
    // Listing
    fun getAllEntries(): Flow<List<JournalEntry>>
    fun getEntriesByCategory(categoryId: Long): Flow<List<JournalEntry>>
    fun getEntriesByMood(moodLevel: Int): Flow<List<JournalEntry>>
    fun getEntriesByTag(tagId: Long): Flow<List<JournalEntry>>
    fun getFavoriteEntries(): Flow<List<JournalEntry>>
    fun getDraftEntries(): Flow<List<JournalEntry>>
    fun getArchivedEntries(): Flow<List<JournalEntry>>
    
    // Search
    fun searchEntries(query: String): Flow<List<JournalEntry>>
    
    // Date range
    fun getEntriesInDateRangeFlow(startTime: Long, endTime: Long): Flow<List<JournalEntry>>
    suspend fun getEntriesInDateRange(startDate: Long, endDate: Long): List<JournalEntry>

    /**
     * Entries in the range with category, tags, people, media and tracker values (the plain range
     * queries return basic entries without them). Use this for anything that exports entries.
     */
    suspend fun getEntriesInDateRangeWithDetails(startTime: Long, endTime: Long): List<JournalEntry>
    
    // Story view ("On This Day")
    fun getEntriesOnThisDay(monthDay: String): Flow<List<JournalEntry>>
    
    // Actions
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)
    suspend fun togglePin(id: Long, isPinned: Boolean)
    suspend fun archiveEntry(id: Long, isArchived: Boolean)
    
    // Soft delete / Trash
    suspend fun softDelete(id: Long)
    suspend fun restoreEntry(id: Long)
    fun getDeletedEntries(): Flow<List<JournalEntry>>
    suspend fun emptyTrash()
    
    // Tags for entry
    suspend fun setTagsForEntry(entryId: Long, tagIds: List<Long>)
    
    // People for entry
    suspend fun setPeopleForEntry(entryId: Long, personIds: List<Long>)
    
    // Tracker values for entry
    suspend fun setTrackerValues(entryId: Long, values: Map<Long, String>)
}

interface CategoryRepository {
    suspend fun create(category: Category): Long
    suspend fun update(category: Category)
    suspend fun delete(id: Long)
    suspend fun getCategoryById(id: Long): Category?
    suspend fun getDefault(): Category?
    suspend fun setDefault(id: Long)
    fun getAllFlow(): Flow<List<Category>>
    fun getAllWithCountFlow(): Flow<List<Category>>
    suspend fun getTopCategories(limit: Int): List<Category>
    suspend fun updateSortOrder(id: Long, sortOrder: Int)
}

interface TagRepository {
    suspend fun create(tag: Tag): Long
    suspend fun createOrGet(name: String): Tag
    suspend fun update(tag: Tag)
    suspend fun delete(id: Long)
    suspend fun getById(id: Long): Tag?
    suspend fun getByName(name: String): Tag?
    fun getAllFlow(): Flow<List<Tag>>
    fun getAllWithCountFlow(): Flow<List<Tag>>
    suspend fun search(query: String, limit: Int = 10): List<Tag>
    suspend fun getTopTags(limit: Int): List<Tag>
    suspend fun getTagsForEntry(entryId: Long): List<Tag>
}

interface MediaRepository {
    suspend fun addMedia(media: Media): Long
    suspend fun addMediaList(mediaList: List<Media>): List<Long>
    suspend fun updateMedia(media: Media)
    suspend fun deleteMedia(id: Long)
    suspend fun deleteAllForEntry(entryId: Long)
    suspend fun getMediaById(id: Long): Media?
    suspend fun getMediaForEntry(entryId: Long): List<Media>
    fun getMediaForEntryFlow(entryId: Long): Flow<List<Media>>
    
    // By type
    fun getPhotosByEntryFlow(): Flow<List<MediaWithEntryInfo>>
    fun getVideosByEntryFlow(): Flow<List<MediaWithEntryInfo>>
    fun getVoiceNotesByEntryFlow(): Flow<List<MediaWithEntryInfo>>
    fun getAttachmentsByEntryFlow(): Flow<List<MediaWithEntryInfo>>
    
    // Stats
    suspend fun getCountByType(type: MediaType): Int
    suspend fun getTotalSize(): Long
    suspend fun getSizeByType(type: MediaType): Long
    
    // File operations
    suspend fun saveMediaFile(uri: String, entryId: Long, type: MediaType): Media
    suspend fun createThumbnail(mediaId: Long): String?
    suspend fun deleteMediaFile(uri: String)
}

data class MediaWithEntryInfo(
    val media: Media,
    val entryTitle: String?,
    val entryCreatedAt: Long
)

interface PersonRepository {
    suspend fun create(person: Person): Long
    suspend fun update(person: Person)
    suspend fun delete(id: Long)
    suspend fun getById(id: Long): Person?
    fun getAllFlow(): Flow<List<Person>>
    fun getAllWithCountFlow(): Flow<List<Person>>
    suspend fun search(query: String, limit: Int = 10): List<Person>
    suspend fun getTopPeople(limit: Int): List<Person>
    suspend fun getPeopleForEntry(entryId: Long): List<Person>
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)
}

/** Persistent, entry-independent nicknames for locations (e.g. "Grandma's House"). */
interface LocationNicknameRepository {
    fun getAllFlow(): Flow<List<LocationNickname>>
    suspend fun getAll(): List<LocationNickname>
    suspend fun setNickname(location: Location, nickname: String)
    suspend fun removeNickname(location: Location)
}

interface TrackerRepository {
    // Tracker definitions
    suspend fun createTracker(tracker: CustomTracker): Long
    suspend fun updateTracker(tracker: CustomTracker)
    suspend fun deleteTracker(id: Long)
    suspend fun getTrackerById(id: Long): CustomTracker?
    suspend fun getAllTrackers(): List<CustomTracker>
    fun getActiveTrackersFlow(): Flow<List<CustomTracker>>
    fun getAllTrackersFlow(): Flow<List<CustomTracker>>
    suspend fun toggleTrackerActive(id: Long, isActive: Boolean)
    suspend fun updateTrackerSortOrder(id: Long, sortOrder: Int)
    
    // Tracker values
    suspend fun setTrackerValue(entryId: Long, trackerId: Long, value: String)
    suspend fun deleteTrackerValue(entryId: Long, trackerId: Long)
    suspend fun getTrackerValuesForEntry(entryId: Long): Map<Long, String>
    fun getTrackerValuesForEntryFlow(entryId: Long): Flow<Map<Long, String>>
    
    // Stats
    suspend fun getTrackerStats(trackerId: Long): TrackerStats
}

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Result<Weather>
    suspend fun getCurrentWeatherByCity(city: String): Result<Weather>
}

interface ExportRepository {
    // Config
    suspend fun createConfig(config: ExportConfig): Long
    suspend fun updateConfig(config: ExportConfig)
    suspend fun deleteConfig(id: Long)
    suspend fun getExportConfig(): ExportConfig?
    fun getAllConfigsFlow(): Flow<List<ExportConfig>>
    suspend fun getEnabledConfigs(): List<ExportConfig>
    suspend fun toggleConfigEnabled(id: Long, isEnabled: Boolean)
    
    // Export execution
    suspend fun exportEntries(
        entries: List<JournalEntry>,
        format: ExportFormat,
        includeMedia: Boolean
    ): Result<ExportHistory>
    
    suspend fun exportToTxt(entries: List<JournalEntry>): Result<String>
    suspend fun exportToPdf(entries: List<JournalEntry>, includeMedia: Boolean): Result<String>
    suspend fun exportToJson(entries: List<JournalEntry>): Result<String>
    suspend fun exportToCsv(entries: List<JournalEntry>): Result<String>
    suspend fun triggerImmediateExport()
    
    // Export to external storage (user-selected folder via SAF)
    suspend fun exportToExternalUri(
        entries: List<JournalEntry>,
        format: ExportFormat,
        includeMedia: Boolean,
        destinationUri: android.net.Uri
    ): Result<ExportHistory>
    
    // Copy existing export to external location
    suspend fun copyExportToUri(exportFilePath: String, destinationUri: android.net.Uri): Result<Boolean>
    
    // History
    fun getExportHistoryFlow(): Flow<List<ExportHistory>>
    suspend fun getRecentHistory(limit: Int): List<ExportHistory>
    suspend fun deleteHistory(id: Long)
    suspend fun clearOldHistory(beforeTimestamp: Long)
}

interface SettingsRepository {
    // Theme
    fun getThemeModeFlow(): Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
    
    fun getDynamicColorsFlow(): Flow<Boolean>
    suspend fun setDynamicColors(enabled: Boolean)
    
    // Defaults
    fun getDefaultCategoryIdFlow(): Flow<Long?>
    suspend fun setDefaultCategoryId(id: Long?)
    
    fun getWeatherAutoFetchFlow(): Flow<Boolean>
    suspend fun setWeatherAutoFetch(enabled: Boolean)
    
    fun getLocationAutoFetchFlow(): Flow<Boolean>
    suspend fun setLocationAutoFetch(enabled: Boolean)
    
    fun getTimeFormat(): Flow<TimeFormat>
    suspend fun setTimeFormat(format: TimeFormat)
    
    fun getDateFormat(): Flow<DateFormat>
    suspend fun setDateFormat(format: DateFormat)
    
    fun getWeightUnit(): Flow<WeightUnit>
    suspend fun setWeightUnit(unit: WeightUnit)
    
    // Security
    fun getAppLockEnabledFlow(): Flow<Boolean>
    suspend fun setAppLockEnabled(enabled: Boolean)
    
    fun getBiometricEnabled(): Flow<Boolean>
    suspend fun setBiometricEnabled(enabled: Boolean)
    
    fun getPinHash(): Flow<String?>
    suspend fun setPinHash(hash: String?)
    
    // Temperature
    fun getTemperatureUnit(): Flow<TemperatureUnit>
    suspend fun setTemperatureUnit(unit: TemperatureUnit)
    
    // First launch
    fun isFirstLaunch(): Flow<Boolean>
    suspend fun setFirstLaunchComplete()
    
    // Tasker/Automation Integration
    fun getTaskerEnabledFlow(): Flow<Boolean>
    suspend fun setTaskerEnabled(enabled: Boolean)
    
    fun getTaskerDefaultFormatFlow(): Flow<ExportFormat>
    suspend fun setTaskerDefaultFormat(format: ExportFormat)
    
    fun getTaskerDefaultPeriodFlow(): Flow<TaskerPeriod>
    suspend fun setTaskerDefaultPeriod(period: TaskerPeriod)
    
    fun getTaskerDefaultIncludeMediaFlow(): Flow<Boolean>
    suspend fun setTaskerDefaultIncludeMedia(include: Boolean)
}

// Tasker period enum for automation
enum class TaskerPeriod(val label: String, val value: String) {
    ALL("All", "all"),
    DAY("Day", "day"),
    WEEK("Week", "week"),
    MONTH("Month", "month")
}

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

enum class TimeFormat {
    TWELVE_HOUR, TWENTY_FOUR_HOUR
}

enum class DateFormat {
    MDY, DMY, YMD
}

enum class WeightUnit {
    KG, LBS
}

enum class TemperatureUnit {
    CELSIUS, FAHRENHEIT
}

// ============ NOTIFICATION REPOSITORY ============

interface NotificationRepository {
    // Reminder Configs
    suspend fun createReminderConfig(config: ReminderConfig): Long
    suspend fun updateReminderConfig(config: ReminderConfig)
    suspend fun deleteReminderConfig(id: Long)
    suspend fun getReminderConfigById(id: Long): ReminderConfig?
    fun getAllReminderConfigsFlow(): Flow<List<ReminderConfig>>
    suspend fun getEnabledReminderConfigs(): List<ReminderConfig>
    suspend fun setReminderEnabled(id: Long, isEnabled: Boolean)
    
    // Smart Reminders
    suspend fun createSmartReminder(reminder: SmartReminder): Long
    suspend fun updateSmartReminder(reminder: SmartReminder)
    suspend fun deleteSmartReminder(id: Long)
    suspend fun getSmartReminderById(id: Long): SmartReminder?
    fun getAllSmartRemindersFlow(): Flow<List<SmartReminder>>
    fun getEnabledSmartRemindersFlow(): Flow<List<SmartReminder>>
    fun getSmartRemindersByTypeFlow(type: SmartReminderType): Flow<List<SmartReminder>>
    suspend fun getUpcomingSmartReminders(daysAhead: Int = 7): List<SmartReminder>
    suspend fun setSmartReminderEnabled(id: Long, isEnabled: Boolean)
    
    // Birthday/Anniversary helpers
    suspend fun createBirthdayReminder(personId: Long, name: String, date: Long): Long
    suspend fun createAnniversaryReminder(title: String, date: Long, description: String? = null): Long
    fun getAnniversaryRemindersFlow(): Flow<List<SmartReminder>>
    
    // Intelligent reminder generation
    suspend fun generateMemoryAnniversaryReminders()
    suspend fun checkPersonMentionGaps()
    suspend fun checkStreakRisk(): Boolean
    
    // Notification History
    suspend fun logNotification(type: String, sourceId: Long, title: String, message: String)
    fun getNotificationHistoryFlow(): Flow<List<NotificationHistory>>
    suspend fun getRecentNotificationHistory(limit: Int = 50): List<NotificationHistory>
    suspend fun markNotificationTapped(id: Long)
    suspend fun clearOldHistory(daysToKeep: Int = 30)
    
    // Scheduling
    suspend fun scheduleReminderNotifications()
    suspend fun cancelReminderNotification(reminderId: Long)
    suspend fun scheduleSmartReminders()
    
    // Permission & Test notification
    fun hasNotificationPermission(): Boolean
    suspend fun sendTestNotification(): NotificationResult
    
    // Mood Check-in
    suspend fun createMoodCheckInReminder(enabled: Boolean, hour: Int, minute: Int, intervalHours: Int)
    suspend fun getMoodCheckInConfig(): MoodCheckInConfig?
    suspend fun updateMoodCheckInConfig(config: MoodCheckInConfig)
}

data class NotificationResult(
    val success: Boolean,
    val message: String,
    val requiresPermission: Boolean = false
)

data class MoodCheckInConfig(
    val id: Long = 0,
    val isEnabled: Boolean = false,
    val intervalHours: Int = 4, // Every 4 hours by default
    val startHour: Int = 9, // Start checking at 9 AM
    val endHour: Int = 22, // Stop checking at 10 PM
    val lastPromptAt: Long? = null
)

// ============ API KEY REPOSITORY ============

interface ApiKeyRepository {
    // Google Maps
    fun getGoogleMapsApiKey(): Flow<String?>
    suspend fun setGoogleMapsApiKey(key: String?)
    suspend fun validateGoogleMapsKey(key: String): Boolean
    
    // Weather API
    fun getWeatherApiKey(): Flow<String?>
    suspend fun setWeatherApiKey(key: String?)
    suspend fun validateWeatherKey(key: String): Boolean
    
    // Google Calendar
    fun getGoogleCalendarApiKey(): Flow<String?>
    suspend fun setGoogleCalendarApiKey(key: String?)
    suspend fun validateGoogleCalendarKey(key: String): Boolean

    /**
     * True exactly once after the stored keys turned out to be unreadable and were reset, so the UI can
     * ask the user to enter them again. Opening the store also performs the reset if it is needed.
     */
    suspend fun consumeKeysResetNotice(): Boolean
}

// ============ MEMORY REPOSITORY ============

interface MemoryRepository {
    // Memory CRUD
    suspend fun createMemory(memory: Memory): Long
    suspend fun updateMemory(memory: Memory)
    suspend fun deleteMemory(id: Long)
    suspend fun getMemoryById(id: Long): Memory?
    fun getMemoryByIdFlow(id: Long): Flow<Memory?>
    
    // Listing
    fun getAllMemoriesFlow(): Flow<List<Memory>>
    fun getFavoriteMemoriesFlow(): Flow<List<Memory>>
    fun getMemoriesInDateRange(startDate: Long, endDate: Long): Flow<List<Memory>>
    fun searchMemories(query: String): Flow<List<Memory>>
    
    // Entry management
    suspend fun addEntriesToMemory(memoryId: Long, entryIds: List<Long>)
    suspend fun removeEntryFromMemory(memoryId: Long, entryId: Long)
    fun getEntriesForMemory(memoryId: Long): Flow<List<JournalEntry>>
    fun getMemoriesForEntry(entryId: Long): Flow<List<Memory>>
    
    // Milestones
    suspend fun addMilestone(milestone: MemoryMilestone): Long
    suspend fun updateMilestone(milestone: MemoryMilestone)
    suspend fun deleteMilestone(id: Long)
    fun getMilestonesForMemory(memoryId: Long): Flow<List<MemoryMilestone>>
    
    // Trackers
    suspend fun addMemoryTracker(tracker: MemoryTracker): Long
    suspend fun updateMemoryTracker(tracker: MemoryTracker)
    suspend fun deleteMemoryTracker(id: Long)
    fun getTrackersForMemory(memoryId: Long): Flow<List<MemoryTracker>>
    
    // Actions
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)
    suspend fun recalculateMemoryStats(memoryId: Long)
    
    // Statistics
    suspend fun getTotalMemoryCount(): Int
    suspend fun getTotalTimeSpentInMemories(): Long
    
    // Smart suggestions
    suspend fun suggestEntriesForMemory(memoryId: Long): List<JournalEntry>
    suspend fun generateMemoryHighlights(memoryId: Long): List<String>
}
