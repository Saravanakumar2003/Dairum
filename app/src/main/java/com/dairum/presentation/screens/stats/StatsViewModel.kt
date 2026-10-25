package com.dairum.presentation.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.local.dao.JournalEntryDao
import com.dairum.data.local.dao.GratiaDao
import com.dairum.data.local.dao.PersonDao
import com.dairum.data.local.dao.TagDao
import com.dairum.domain.model.*
import com.dairum.domain.repository.MediaRepository
import com.dairum.domain.repository.PersonRepository
import com.dairum.domain.repository.TagRepository
import com.dairum.domain.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val entryDao: JournalEntryDao,
    private val tagDao: TagDao,
    private val personDao: PersonDao,
    private val gratiaDao: GratiaDao,
    private val tagRepository: TagRepository,
    private val personRepository: PersonRepository,
    private val mediaRepository: MediaRepository,
    private val trackerRepository: TrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()
    
    // Current selected time range
    private var currentTimeRange: TimeRange = TimeRange.ALL_TIME
    private var trackerCollectorJob: Job? = null

    init {
        loadStats()
    }
    
    /**
     * Get the start timestamp for the given time range
     */
    private fun getStartTimeForRange(range: TimeRange): Long {
        val today = LocalDate.now()
        return when (range) {
            TimeRange.WEEK -> today.minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            TimeRange.MONTH -> today.minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            TimeRange.YEAR -> today.minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            TimeRange.ALL_TIME -> 0L
            TimeRange.CUSTOM -> customRangeStart
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            loadJournalStats(currentTimeRange)
            loadMoodStats(currentTimeRange)
            loadActivityStats(currentTimeRange)
            loadTagStats(currentTimeRange)
            loadPeopleStats(currentTimeRange)
            loadTrackerStats()
            loadGratiaStats()
        }
    }

    private suspend fun loadJournalStats(range: TimeRange) {
        val now = System.currentTimeMillis()
        val today = LocalDate.now()
        val rangeStartTime = getStartTimeForRange(range)
        val weekAgo = today.minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val monthAgo = today.minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val twoMonthsAgo = today.minusMonths(2).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val yearAgo = today.minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        
        // Debug logging
        android.util.Log.d("StatsViewModel", "Today: $today, WeekAgo millis: $weekAgo, MonthAgo millis: $monthAgo")
        
        // Get entries within the selected time range
        val entriesInRange = if (range == TimeRange.ALL_TIME) {
            entryDao.getTotalCount()
        } else {
            entryDao.getCountSince(rangeStartTime)
        }

        // Calculate other metrics relative to the selected range
        val totalEntries = entryDao.getTotalCount()
        val entriesThisWeek = entryDao.getCountSince(weekAgo)
        val entriesThisMonth = entryDao.getCountSince(monthAgo)
        val entriesThisYear = entryDao.getCountSince(yearAgo)
        val entriesLastMonth = entryDao.getEntryCountBetween(twoMonthsAgo, monthAgo)
        val avgWordCount = entryDao.getAverageWordCount() ?: 0.0
        val totalWordCount = entryDao.getTotalWordCount() ?: 0
        
        // Debug logging - entry counts and dates
        android.util.Log.d("StatsViewModel", "Total: $totalEntries, ThisWeek: $entriesThisWeek, ThisMonth: $entriesThisMonth")

        // Calculate streaks
        val dates = entryDao.getDistinctEntryDates()
        android.util.Log.d("StatsViewModel", "Distinct entry dates: $dates")
        val (currentStreak, longestStreak) = calculateStreaks(dates)

        // Media counts
        val totalPhotos = mediaRepository.getCountByType(MediaType.PHOTO)
        val totalVideos = mediaRepository.getCountByType(MediaType.VIDEO)
        val totalVoiceNotes = mediaRepository.getCountByType(MediaType.VOICE)
        val totalAttachments = mediaRepository.getCountByType(MediaType.ATTACHMENT)
        val totalStorage = mediaRepository.getTotalSize()
        
        // Calculate new insights
        val totalDaysJournaling = dates.size
        
        // Average entries per week (based on all-time data)
        val firstEntryDate = dates.lastOrNull()?.let { LocalDate.parse(it) }
        val weeksJournaling = if (firstEntryDate != null) {
            ChronoUnit.WEEKS.between(firstEntryDate, today).coerceAtLeast(1)
        } else 1
        val averageEntriesPerWeek = totalEntries.toDouble() / weeksJournaling
        
        // Consistency score: percentage of last 30 days with entries
        val last30Days = (0 until 30).map { today.minusDays(it.toLong()).toString() }
        val daysWithEntries = dates.count { it in last30Days }
        val consistencyScore = daysWithEntries.toFloat() / 30f
        
        // Days since last entry
        val lastEntryDate = dates.firstOrNull()?.let { LocalDate.parse(it) }
        val daysSinceLastEntry = if (lastEntryDate != null) {
            ChronoUnit.DAYS.between(lastEntryDate, today).toInt()
        } else 0
        
        // Monthly growth
        val monthlyGrowth = if (entriesLastMonth > 0) {
            ((entriesThisMonth - entriesLastMonth).toFloat() / entriesLastMonth) * 100f
        } else if (entriesThisMonth > 0) {
            100f
        } else 0f
        
        // Best writing day and writing time preference
        val hourlyDist = entryDao.getHourlyDistribution().associate { it.hour to it.count }
        val weekdayDist = entryDao.getWeekdayDistribution().associate { it.dayOfWeek to it.count }
        val bestWritingDay = weekdayDist.maxByOrNull { it.value }?.key
        
        // Calculate writing time preference
        val morningEntries = (5 until 11).sumOf { hourlyDist.getOrDefault(it, 0) }
        val afternoonEntries = (11 until 17).sumOf { hourlyDist.getOrDefault(it, 0) }
        val eveningEntries = (17 until 23).sumOf { hourlyDist.getOrDefault(it, 0) }
        val lateNightEntries = (listOf(23, 0, 1, 2, 3, 4)).sumOf { hourlyDist.getOrDefault(it, 0) }
        
        val writingTimePreference = when {
            morningEntries >= afternoonEntries && morningEntries >= eveningEntries && morningEntries >= lateNightEntries -> 
                com.dairum.domain.model.WritingTimePreference.EARLY_BIRD
            afternoonEntries >= morningEntries && afternoonEntries >= eveningEntries && afternoonEntries >= lateNightEntries -> 
                com.dairum.domain.model.WritingTimePreference.AFTERNOON
            eveningEntries >= morningEntries && eveningEntries >= afternoonEntries && eveningEntries >= lateNightEntries -> 
                com.dairum.domain.model.WritingTimePreference.NIGHT_OWL
            lateNightEntries > 0 -> 
                com.dairum.domain.model.WritingTimePreference.LATE_NIGHT
            else -> 
                com.dairum.domain.model.WritingTimePreference.UNKNOWN
        }

        _uiState.update {
            it.copy(
                journalStats = JournalStats(
                    totalEntries = totalEntries,
                    entriesThisWeek = entriesThisWeek,
                    entriesThisMonth = entriesThisMonth,
                    entriesThisYear = entriesThisYear,
                    entriesInCustomRange = 0, // Not applicable for preset ranges
                    currentStreak = currentStreak,
                    longestStreak = longestStreak,
                    averageWordCount = avgWordCount,
                    totalWordCount = totalWordCount,
                    totalPhotos = totalPhotos,
                    totalVideos = totalVideos,
                    totalVoiceNotes = totalVoiceNotes,
                    totalAttachments = totalAttachments,
                    storageUsed = totalStorage,
                    totalStorageUsed = totalStorage,
                    comparisonToLastMonth = monthlyGrowth,
                    // New insights
                    averageEntriesPerWeek = averageEntriesPerWeek,
                    consistencyScore = consistencyScore,
                    daysSinceLastEntry = daysSinceLastEntry,
                    bestWritingDayOfWeek = bestWritingDay,
                    writingTimePreference = writingTimePreference,
                    totalDaysJournaling = totalDaysJournaling,
                    entriesLastMonth = entriesLastMonth,
                    monthlyGrowth = monthlyGrowth
                ),
                isLoading = false
            )
        }
    }

    private suspend fun loadMoodStats(range: TimeRange) {
        val rangeStartTime = getStartTimeForRange(range)
        val moodCounts = if (range == TimeRange.ALL_TIME) {
            entryDao.getMoodDistribution()
        } else {
            entryDao.getMoodDistributionSince(rangeStartTime)
        }
        val totalWithMood = moodCounts.sumOf { it.count }
        
        if (totalWithMood == 0) {
            _uiState.update { it.copy(moodStats = null) }
            return
        }

        val distribution = moodCounts.map { 
            MoodDistributionItem(
                level = it.moodLevel,
                count = it.count,
                percentage = it.count.toFloat() / totalWithMood
            )
        }

        val average = moodCounts.sumOf { 
            (MoodOption.normalizedScore(it.moodLevel) * it.count).toDouble() 
        } / totalWithMood
        val mostCommon = moodCounts.maxByOrNull { it.count }?.moodLevel

        _uiState.update {
            it.copy(
                moodStats = MoodStats(
                    distribution = distribution,
                    averageMood = average,
                    moodTrend = calculateMoodTrend(rangeStartTime),
                    mostCommonMood = mostCommon,
                    moodByDayOfWeek = calculateMoodByDayOfWeek(rangeStartTime)
                )
            )
        }
    }

    private suspend fun loadActivityStats(range: TimeRange) {
        val rangeStartTime = getStartTimeForRange(range)
        val hourlyDist = if (range == TimeRange.ALL_TIME) {
            entryDao.getHourlyDistribution()
        } else {
            entryDao.getHourlyDistributionSince(rangeStartTime)
        }.associate { it.hour to it.count }
        val weekdayDist = if (range == TimeRange.ALL_TIME) {
            entryDao.getWeekdayDistribution()
        } else {
            entryDao.getWeekdayDistributionSince(rangeStartTime)
        }.associate { it.dayOfWeek to it.count }

        val mostActiveHour = hourlyDist.maxByOrNull { it.value }?.key
        val mostActiveDay = weekdayDist.maxByOrNull { it.value }?.key

        _uiState.update {
            it.copy(
                activityStats = EntryActivityStats(
                    dailyCounts = calculateDailyCounts(rangeStartTime),
                    hourlyDistribution = hourlyDist,
                    weekdayDistribution = weekdayDist,
                    mostActiveHour = mostActiveHour,
                    mostActiveDay = mostActiveDay
                )
            )
        }
    }

    private suspend fun loadTagStats(range: TimeRange) {
        val rangeStartTime = getStartTimeForRange(range)
        val topTags = if (range == TimeRange.ALL_TIME) {
            tagDao.getTopTags(20)
        } else {
            tagDao.getTopTagsSince(20, rangeStartTime)
        }
        val totalEntries = if (range == TimeRange.ALL_TIME) {
            entryDao.getTotalCount()
        } else {
            entryDao.getCountSince(rangeStartTime)
        }

        val tagUsage = topTags.map { tagWithCount ->
            TagUsage(
                tag = Tag(
                    id = tagWithCount.tag.id,
                    name = tagWithCount.tag.name,
                    color = tagWithCount.tag.color,
                    usageCount = tagWithCount.tag.usageCount
                ),
                count = tagWithCount.entryCount,
                percentage = if (totalEntries > 0) tagWithCount.entryCount.toFloat() / totalEntries else 0f
            )
        }

        _uiState.update {
            it.copy(
                tagStats = TagStats(
                    topTags = tagUsage,
                    totalTags = topTags.size,
                    coOccurringTags = emptyList() // TODO: Calculate
                )
            )
        }
    }

    private suspend fun loadPeopleStats(range: TimeRange) {
        val rangeStartTime = getStartTimeForRange(range)
        val topPeople = if (range == TimeRange.ALL_TIME) {
            personDao.getTopPeople(20)
        } else {
            personDao.getTopPeopleSince(20, rangeStartTime)
        }
        val allPeople = personRepository.getAllFlow().firstOrNull() ?: emptyList()
        val totalEntries = if (range == TimeRange.ALL_TIME) {
            entryDao.getTotalCount()
        } else {
            entryDao.getCountSince(rangeStartTime)
        }

        val peopleMentions = topPeople.map { personWithCount ->
            PersonMention(
                person = Person(
                    id = personWithCount.person.id,
                    name = personWithCount.person.name,
                    relationship = personWithCount.person.relationship,
                    photoUri = personWithCount.person.photoUri,
                    notes = personWithCount.person.notes,
                    isFavorite = personWithCount.person.isFavorite,
                    mentionCount = personWithCount.person.mentionCount
                ),
                count = personWithCount.entryCount,
                percentage = if (totalEntries > 0) personWithCount.entryCount.toFloat() / totalEntries else 0f
            )
        }

        _uiState.update {
            it.copy(
                peopleStats = PeopleStats(
                    topPeople = peopleMentions,
                    totalPeople = allPeople.size
                )
            )
        }
    }

    private suspend fun loadTrackerStats() {
        // Cancel any prior collector to avoid accumulating duplicate collectors (Issue #11)
        trackerCollectorJob?.cancel()
        trackerCollectorJob = viewModelScope.launch {
            trackerRepository.getActiveTrackersFlow().collect { trackers ->
                val stats = trackers.mapNotNull { tracker ->
                    if (tracker.type == TrackerType.NUMBER || tracker.type == TrackerType.WEIGHT) {
                        trackerRepository.getTrackerStats(tracker.id)
                    } else null
                }
                
                _uiState.update { it.copy(trackerStats = stats) }
            }
        }
    }

    private suspend fun loadGratiaStats() {
        val stats = GratiaStats(
            gratitudeFolderCount = gratiaDao.getGratitudeFolderCount(),
            gratitudePromptCount = gratiaDao.getGratitudePromptCount(),
            affirmationFolderCount = gratiaDao.getAffirmationFolderCount(),
            affirmationCount = gratiaDao.getAffirmationCount()
        )
        _uiState.update { it.copy(gratiaStats = stats) }
    }
    
    /**
     * Load stats for a specific time range
     */
    fun loadStatsForRange(range: TimeRange) {
        currentTimeRange = range
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, selectedTimeRange = range) }
            
            // Reload all stats with the new time range filter
            loadJournalStats(range)
            loadMoodStats(range)
            loadActivityStats(range)
            loadTagStats(range)
            loadPeopleStats(range)
            loadTrackerStats()
            
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    
    /**
     * Load stats for a custom date range
     */
    fun loadStatsForCustomRange(startMillis: Long, endMillis: Long) {
        currentTimeRange = TimeRange.CUSTOM
        customRangeStart = startMillis
        customRangeEnd = endMillis
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, selectedTimeRange = TimeRange.CUSTOM) }
                
                // Load journal stats for custom range
                loadJournalStatsCustom(startMillis, endMillis)
                loadMoodStats(TimeRange.CUSTOM)
                loadActivityStats(TimeRange.CUSTOM)
                loadTagStats(TimeRange.CUSTOM)
                loadPeopleStats(TimeRange.CUSTOM)
                loadTrackerStats()
            } catch (e: Exception) {
                android.util.Log.e("StatsViewModel", "Error loading custom stats: ${e.message}", e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    
    private var customRangeStart: Long = 0
    private var customRangeEnd: Long = System.currentTimeMillis()
    
    private suspend fun loadJournalStatsCustom(startMillis: Long, endMillis: Long) {
        try {
            val today = LocalDate.now()
            val weekAgo = today.minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val monthAgo = today.minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val twoMonthsAgo = today.minusMonths(2).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val yearAgo = today.minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            
            // Get entries within custom range
            val entriesInRange = entryDao.getEntryCountBetween(startMillis, endMillis)

            val totalEntries = entryDao.getTotalCount()
            val entriesThisWeek = entryDao.getCountSince(weekAgo)
            val entriesThisMonth = entryDao.getCountSince(monthAgo)
            val entriesThisYear = entryDao.getCountSince(yearAgo)
            val entriesLastMonth = entryDao.getEntryCountBetween(twoMonthsAgo, monthAgo)
            val avgWordCount = entryDao.getAverageWordCount() ?: 0.0
            val totalWordCount = entryDao.getTotalWordCount() ?: 0

            val dates = entryDao.getDistinctEntryDates()
            val (currentStreak, longestStreak) = calculateStreaks(dates)

            val totalPhotos = mediaRepository.getCountByType(MediaType.PHOTO)
            val totalVideos = mediaRepository.getCountByType(MediaType.VIDEO)
            val totalVoiceNotes = mediaRepository.getCountByType(MediaType.VOICE)
            val totalAttachments = mediaRepository.getCountByType(MediaType.ATTACHMENT)
            val totalStorage = mediaRepository.getTotalSize()
            
            val totalDaysJournaling = dates.size
            val firstEntryDate = dates.lastOrNull()?.let { LocalDate.parse(it) }
            val weeksJournaling = if (firstEntryDate != null) {
                ChronoUnit.WEEKS.between(firstEntryDate, today).coerceAtLeast(1)
            } else 1
            val averageEntriesPerWeek = totalEntries.toDouble() / weeksJournaling
            
            val last30Days = (0 until 30).map { today.minusDays(it.toLong()).toString() }
            val daysWithEntries = dates.count { it in last30Days }
            val consistencyScore = daysWithEntries.toFloat() / 30f
            
            val lastEntryDate = dates.firstOrNull()?.let { LocalDate.parse(it) }
            val daysSinceLastEntry = if (lastEntryDate != null) {
                ChronoUnit.DAYS.between(lastEntryDate, today).toInt()
            } else 0

            val comparisonPercentage = if (entriesLastMonth > 0) {
                ((entriesThisMonth - entriesLastMonth).toFloat() / entriesLastMonth) * 100f
            } else if (entriesThisMonth > 0) {
                100f
            } else 0f

            _uiState.update {
                it.copy(
                    journalStats = JournalStats(
                        totalEntries = totalEntries,
                        entriesThisWeek = entriesThisWeek,
                        entriesThisMonth = entriesThisMonth,
                        entriesThisYear = entriesThisYear,
                        entriesInCustomRange = entriesInRange,
                        averageWordCount = avgWordCount,
                        totalWordCount = totalWordCount,
                        currentStreak = currentStreak,
                        longestStreak = longestStreak,
                        totalPhotos = totalPhotos,
                        totalVideos = totalVideos,
                        totalVoiceNotes = totalVoiceNotes,
                        totalAttachments = totalAttachments,
                        storageUsed = totalStorage,
                        totalStorageUsed = totalStorage,
                        comparisonToLastMonth = comparisonPercentage,
                        totalDaysJournaling = totalDaysJournaling,
                        averageEntriesPerWeek = averageEntriesPerWeek,
                        consistencyScore = consistencyScore,
                        daysSinceLastEntry = daysSinceLastEntry
                    )
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("StatsViewModel", "Error in loadJournalStatsCustom: ${e.message}", e)
        }
    }

    private fun calculateStreaks(dates: List<String>): Pair<Int, Int> {
        if (dates.isEmpty()) return 0 to 0

        val today = LocalDate.now().toString()
        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 1

        // Check if today has an entry
        if (dates.firstOrNull() == today || dates.firstOrNull() == LocalDate.now().minusDays(1).toString()) {
            currentStreak = 1
        }

        for (i in 0 until dates.size - 1) {
            val current = LocalDate.parse(dates[i])
            val next = LocalDate.parse(dates[i + 1])
            
            if (ChronoUnit.DAYS.between(next, current) == 1L) {
                tempStreak++
                if (i == 0 || (i == 1 && currentStreak > 0)) {
                    currentStreak = tempStreak
                }
            } else {
                longestStreak = maxOf(longestStreak, tempStreak)
                tempStreak = 1
            }
        }

        longestStreak = maxOf(longestStreak, tempStreak)
        
        return currentStreak to longestStreak
    }
    
    private suspend fun calculateMoodTrend(rangeStartTime: Long): List<MoodTrendPoint> {
        val entries = if (rangeStartTime == 0L) {
            entryDao.getEntriesInDateRangeSync(0, System.currentTimeMillis())
        } else {
            entryDao.getEntriesInDateRangeSync(rangeStartTime, System.currentTimeMillis())
        }
        
        // Group by date, calculate average mood per day
        return entries
            .filter { it.moodLevel != null }
            .groupBy { java.time.Instant.ofEpochMilli(it.createdAt).atZone(ZoneId.systemDefault()).toLocalDate() }
            .map { (date, dayEntries) ->
                MoodTrendPoint(
                    date = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    averageMood = dayEntries.mapNotNull { it.moodLevel }.map { MoodOption.normalizedScore(it).toDouble() }.average()
                )
            }
            .sortedBy { it.date }
    }
    
    private suspend fun calculateMoodByDayOfWeek(rangeStartTime: Long): Map<Int, Double> {
        val entries = if (rangeStartTime == 0L) {
            entryDao.getEntriesInDateRangeSync(0, System.currentTimeMillis())
        } else {
            entryDao.getEntriesInDateRangeSync(rangeStartTime, System.currentTimeMillis())
        }
        
        return entries
            .filter { it.moodLevel != null }
            .groupBy { java.time.Instant.ofEpochMilli(it.createdAt).atZone(ZoneId.systemDefault()).dayOfWeek.value }
            .mapValues { (_, dayEntries) -> dayEntries.mapNotNull { it.moodLevel }.map { MoodOption.normalizedScore(it).toDouble() }.average() }
    }
    
    private suspend fun calculateDailyCounts(rangeStartTime: Long): List<DailyCount> {
        val startTime = if (rangeStartTime == 0L) {
            LocalDate.now().minusMonths(3).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        } else {
            rangeStartTime
        }
        
        return entryDao.getDailyCounts(startTime).map { dateCount ->
            DailyCount(
                date = LocalDate.parse(dateCount.date).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                count = dateCount.count
            )
        }
    }
}

data class StatsUiState(
    val journalStats: JournalStats = JournalStats(),
    val moodStats: MoodStats? = null,
    val activityStats: EntryActivityStats? = null,
    val tagStats: TagStats? = null,
    val peopleStats: PeopleStats? = null,
    val trackerStats: List<TrackerStats> = emptyList(),
    val gratiaStats: GratiaStats? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedTimeRange: TimeRange = TimeRange.ALL_TIME
)
