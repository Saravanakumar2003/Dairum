package com.dairum.presentation.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.local.dao.JournalEntryDao
import com.dairum.domain.model.*
import com.dairum.domain.repository.MediaRepository
import com.dairum.domain.repository.PersonRepository
import com.dairum.domain.repository.TagRepository
import com.dairum.domain.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val tagRepository: TagRepository,
    private val personRepository: PersonRepository,
    private val mediaRepository: MediaRepository,
    private val trackerRepository: TrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            loadJournalStats()
            loadMoodStats()
            loadActivityStats()
            loadTagStats()
            loadPeopleStats()
            loadTrackerStats()
        }
    }

    private suspend fun loadJournalStats() {
        val now = System.currentTimeMillis()
        val weekAgo = LocalDate.now().minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val monthAgo = LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val yearAgo = LocalDate.now().minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val totalEntries = entryDao.getTotalCount()
        val entriesThisWeek = entryDao.getCountSince(weekAgo)
        val entriesThisMonth = entryDao.getCountSince(monthAgo)
        val entriesThisYear = entryDao.getCountSince(yearAgo)
        val avgWordCount = entryDao.getAverageWordCount() ?: 0.0
        val totalWordCount = entryDao.getTotalWordCount() ?: 0

        // Calculate streaks
        val dates = entryDao.getDistinctEntryDates()
        val (currentStreak, longestStreak) = calculateStreaks(dates)

        // Media counts
        val totalPhotos = mediaRepository.getCountByType(MediaType.PHOTO)
        val totalVideos = mediaRepository.getCountByType(MediaType.VIDEO)
        val totalVoiceNotes = mediaRepository.getCountByType(MediaType.VOICE)
        val totalAttachments = mediaRepository.getCountByType(MediaType.ATTACHMENT)
        val totalStorage = mediaRepository.getTotalSize()

        _uiState.update {
            it.copy(
                journalStats = JournalStats(
                    totalEntries = totalEntries,
                    entriesThisWeek = entriesThisWeek,
                    entriesThisMonth = entriesThisMonth,
                    entriesThisYear = entriesThisYear,
                    currentStreak = currentStreak,
                    longestStreak = longestStreak,
                    averageWordCount = avgWordCount,
                    totalWordCount = totalWordCount,
                    totalPhotos = totalPhotos,
                    totalVideos = totalVideos,
                    totalVoiceNotes = totalVoiceNotes,
                    totalAttachments = totalAttachments,
                    storageUsed = totalStorage
                ),
                isLoading = false
            )
        }
    }

    private suspend fun loadMoodStats() {
        val moodCounts = entryDao.getMoodDistribution()
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

        val average = moodCounts.sumOf { it.moodLevel * it.count }.toDouble() / totalWithMood
        val mostCommon = moodCounts.maxByOrNull { it.count }?.moodLevel

        _uiState.update {
            it.copy(
                moodStats = MoodStats(
                    distribution = distribution,
                    averageMood = average,
                    moodTrend = emptyList(), // TODO: Calculate trend
                    mostCommonMood = mostCommon,
                    moodByDayOfWeek = emptyMap() // TODO: Calculate
                )
            )
        }
    }

    private suspend fun loadActivityStats() {
        val hourlyDist = entryDao.getHourlyDistribution().associate { it.hour to it.count }
        val weekdayDist = entryDao.getWeekdayDistribution().associate { it.dayOfWeek to it.count }

        val mostActiveHour = hourlyDist.maxByOrNull { it.value }?.key
        val mostActiveDay = weekdayDist.maxByOrNull { it.value }?.key

        _uiState.update {
            it.copy(
                activityStats = EntryActivityStats(
                    dailyCounts = emptyList(), // TODO: Load daily counts
                    hourlyDistribution = hourlyDist,
                    weekdayDistribution = weekdayDist,
                    mostActiveHour = mostActiveHour,
                    mostActiveDay = mostActiveDay
                )
            )
        }
    }

    private suspend fun loadTagStats() {
        val topTags = tagRepository.getTopTags(20)
        val totalEntries = entryDao.getTotalCount()

        val tagUsage = topTags.map { tag ->
            TagUsage(
                tag = tag,
                count = tag.usageCount,
                percentage = if (totalEntries > 0) tag.usageCount.toFloat() / totalEntries else 0f
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

    private suspend fun loadPeopleStats() {
        val topPeople = personRepository.getTopPeople(20)
        val allPeople = personRepository.getAllFlow().firstOrNull() ?: emptyList()
        val totalEntries = entryDao.getTotalCount()

        val peopleMentions = topPeople.map { person ->
            PersonMention(
                person = person,
                count = person.mentionCount,
                percentage = if (totalEntries > 0) person.mentionCount.toFloat() / totalEntries else 0f
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
        trackerRepository.getActiveTrackersFlow().collect { trackers ->
            val stats = trackers.mapNotNull { tracker ->
                if (tracker.type == TrackerType.NUMBER || tracker.type == TrackerType.WEIGHT) {
                    trackerRepository.getTrackerStats(tracker.id)
                } else null
            }
            
            _uiState.update { it.copy(trackerStats = stats) }
        }
    }
    
    /**
     * Load stats for a specific time range
     */
    fun loadStatsForRange(range: TimeRange) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Calculate time bounds based on range
            val now = System.currentTimeMillis()
            val startTime = when (range) {
                TimeRange.WEEK -> LocalDate.now().minusWeeks(1)
                TimeRange.MONTH -> LocalDate.now().minusMonths(1)
                TimeRange.YEAR -> LocalDate.now().minusYears(1)
                TimeRange.ALL_TIME -> LocalDate.of(2000, 1, 1)
            }.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            
            // Reload all stats with the time filter
            // For now, simply reload all stats (time filtering to be enhanced)
            loadStats()
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
}

data class StatsUiState(
    val journalStats: JournalStats = JournalStats(),
    val moodStats: MoodStats? = null,
    val activityStats: EntryActivityStats? = null,
    val tagStats: TagStats? = null,
    val peopleStats: PeopleStats? = null,
    val trackerStats: List<TrackerStats> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
