package com.dairum.presentation.screens.memories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.*
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.MemoryRepository
import com.dairum.domain.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class MemoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val memoryRepository: MemoryRepository,
    private val journalRepository: JournalRepository,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val memoryId: Long = savedStateHandle.get<Long>("memoryId") ?: 0L

    private val _uiState = MutableStateFlow(MemoryDetailUiState())
    val uiState: StateFlow<MemoryDetailUiState> = _uiState.asStateFlow()

    init {
        loadMemory()
    }

    private fun loadMemory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            memoryRepository.getMemoryByIdFlow(memoryId).collect { memory ->
                if (memory != null) {
                    // Load entries with full details
                    val entries = memory.entries
                    
                    // Calculate insights
                    val insights = calculateInsights(memory, entries)
                    
                    // Group entries by date
                    val entriesByDate = entries.groupBy { entry ->
                        entry.createdAt.toLocalDate()
                    }.toSortedMap(reverseOrder())
                    
                    // Get all photos from entries
                    val allPhotos = entries.flatMap { it.media.filter { m -> m.type == MediaType.PHOTO } }
                    
                    // Get unique locations
                    val locations = entries.mapNotNull { it.location }.distinctBy { 
                        "${it.latitude},${it.longitude}" 
                    }
                    
                    // Get people mentioned
                    val people = entries.flatMap { it.people }.distinctBy { it.id }
                    
                    // Get all tags used
                    val allTags = entries.flatMap { it.tags }.distinctBy { it.id }
                    
                    // Mood journey
                    val moodJourney = entries
                        .filter { it.mood != null }
                        .sortedBy { it.createdAt }
                        .map { MoodPoint(it.createdAt, it.mood!!.level) }
                    
                    _uiState.update {
                        it.copy(
                            memory = memory,
                            entries = entries,
                            entriesByDate = entriesByDate,
                            photos = allPhotos,
                            locations = locations,
                            people = people,
                            tags = allTags,
                            moodJourney = moodJourney,
                            insights = insights,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Memory not found") }
                }
            }
        }
    }

    private fun calculateInsights(memory: Memory, entries: List<JournalEntry>): MemoryInsights {
        val totalDays = ChronoUnit.DAYS.between(memory.startDate, memory.endDate) + 1
        
        // Writing frequency
        val writingDays = entries.map { it.createdAt.toLocalDate() }.distinct().size
        val writingFrequency = if (totalDays > 0) (writingDays.toFloat() / totalDays * 100) else 0f
        
        // Average entry length
        val avgWordCount = if (entries.isNotEmpty()) entries.sumOf { it.wordCount } / entries.size else 0
        
        // Most active hour
        val hourCounts = entries.groupBy { it.createdAt.hour }.mapValues { it.value.size }
        val mostActiveHour = hourCounts.maxByOrNull { it.value }?.key
        
        // Mood stats
        val moods = entries.mapNotNull { it.mood?.level }
        val avgMood = if (moods.isNotEmpty()) moods.average().toFloat() else null
        val moodVariability = if (moods.size > 1) {
            val mean = moods.average()
            val variance = moods.map { (it - mean) * (it - mean) }.average()
            kotlin.math.sqrt(variance).toFloat()
        } else null
        
        // Best day
        val bestMoodEntry = entries.maxByOrNull { it.mood?.level ?: 0 }
        
        // Most productive day
        val entriesByDay = entries.groupBy { it.createdAt.dayOfWeek.name }
        val mostProductiveDay = entriesByDay.maxByOrNull { it.value.size }?.key
        
        // Photo stats
        val photoCount = entries.sumOf { it.media.count { m -> m.type == MediaType.PHOTO } }
        val entriesWithPhotos = entries.count { it.media.any { m -> m.type == MediaType.PHOTO } }
        
        // Longest entry
        val longestEntry = entries.maxByOrNull { it.wordCount }
        
        // Streak info
        val sortedDates = entries.map { it.createdAt.toLocalDate() }.distinct().sorted()
        var longestStreak = 1
        var currentStreak = 1
        for (i in 1 until sortedDates.size) {
            if (ChronoUnit.DAYS.between(sortedDates[i-1], sortedDates[i]) == 1L) {
                currentStreak++
                if (currentStreak > longestStreak) longestStreak = currentStreak
            } else {
                currentStreak = 1
            }
        }
        
        return MemoryInsights(
            totalDays = totalDays.toInt(),
            writingDays = writingDays,
            writingFrequency = writingFrequency,
            avgWordCount = avgWordCount,
            mostActiveHour = mostActiveHour,
            avgMood = avgMood,
            moodVariability = moodVariability,
            bestMoodEntry = bestMoodEntry,
            mostProductiveDay = mostProductiveDay,
            photoCount = photoCount,
            entriesWithPhotos = entriesWithPhotos,
            longestEntry = longestEntry,
            longestStreak = longestStreak,
            totalWords = entries.sumOf { it.wordCount },
            uniqueLocations = entries.mapNotNull { it.location }.distinctBy { 
                "${it.latitude},${it.longitude}" 
            }.size
        )
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _uiState.value.memory?.let { memory ->
                memoryRepository.toggleFavorite(memory.id, !memory.isFavorite)
            }
        }
    }

    fun updateMemory(title: String, description: String?) {
        viewModelScope.launch {
            _uiState.value.memory?.let { memory ->
                val updated = memory.copy(title = title, description = description)
                memoryRepository.updateMemory(updated)
            }
        }
    }

    fun deleteMemory() {
        viewModelScope.launch {
            memoryRepository.deleteMemory(memoryId)
        }
    }

    fun removeEntry(entryId: Long) {
        viewModelScope.launch {
            memoryRepository.removeEntryFromMemory(memoryId, entryId)
        }
    }

    fun addMilestone(title: String, description: String?, date: LocalDateTime) {
        viewModelScope.launch {
            memoryRepository.addMilestone(
                MemoryMilestone(
                    memoryId = memoryId,
                    title = title,
                    description = description,
                    milestoneDate = date
                )
            )
        }
    }

    fun deleteMilestone(milestoneId: Long) {
        viewModelScope.launch {
            memoryRepository.deleteMilestone(milestoneId)
        }
    }
    
    /**
     * Load all entries that can be added to this memory (no date restriction)
     */
    fun loadAvailableEntries() {
        viewModelScope.launch {
            // Get ALL entries that aren't already in the memory
            journalRepository.getAllEntries().collect { allEntries ->
                val existingEntryIds = _uiState.value.entries.map { it.id }.toSet()
                val availableEntries = allEntries
                    .filter { it.id !in existingEntryIds }
                    .sortedByDescending { it.createdAt }
                
                _uiState.update { it.copy(availableEntries = availableEntries) }
            }
        }
    }
    
    /**
     * Add entries to this memory
     */
    fun addEntries(entryIds: List<Long>) {
        viewModelScope.launch {
            memoryRepository.addEntriesToMemory(memoryId, entryIds)
            // Reload memory to get updated entries
            loadMemory()
        }
    }

    fun generateHighlights() {
        viewModelScope.launch {
            val highlights = memoryRepository.generateMemoryHighlights(memoryId)
            _uiState.value.memory?.let { memory ->
                memoryRepository.updateMemory(memory.copy(highlights = highlights))
            }
        }
    }

    fun setActiveTab(tab: MemoryTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }
}

data class MemoryDetailUiState(
    val memory: Memory? = null,
    val entries: List<JournalEntry> = emptyList(),
    val entriesByDate: Map<java.time.LocalDate, List<JournalEntry>> = emptyMap(),
    val availableEntries: List<JournalEntry> = emptyList(),
    val photos: List<Media> = emptyList(),
    val locations: List<Location> = emptyList(),
    val people: List<Person> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val moodJourney: List<MoodPoint> = emptyList(),
    val insights: MemoryInsights? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val activeTab: MemoryTab = MemoryTab.OVERVIEW
)

enum class MemoryTab {
    OVERVIEW, ENTRIES, GALLERY, INSIGHTS, TIMELINE
}

data class MoodPoint(
    val date: LocalDateTime,
    val moodLevel: Int
)

data class MemoryInsights(
    val totalDays: Int,
    val writingDays: Int,
    val writingFrequency: Float,
    val avgWordCount: Int,
    val mostActiveHour: Int?,
    val avgMood: Float?,
    val moodVariability: Float?,
    val bestMoodEntry: JournalEntry?,
    val mostProductiveDay: String?,
    val photoCount: Int,
    val entriesWithPhotos: Int,
    val longestEntry: JournalEntry?,
    val longestStreak: Int,
    val totalWords: Int,
    val uniqueLocations: Int
)
