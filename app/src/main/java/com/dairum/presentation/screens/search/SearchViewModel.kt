package com.dairum.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val categoryRepository: CategoryRepository,
    private val tagRepository: TagRepository,
    private val personRepository: PersonRepository,
    private val trackerRepository: TrackerRepository,
    private val exportRepository: ExportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    private var searchJob: Job? = null
    private var currentQuery = ""

    init {
        viewModelScope.launch {
            launch {
                categoryRepository.getAllFlow().collect { categories ->
                    _uiState.update { it.copy(categories = categories) }
                }
            }
            launch {
                tagRepository.getAllFlow().collect { tags ->
                    _uiState.update { it.copy(allTags = tags) }
                }
            }
            launch {
                personRepository.getAllFlow().collect { people ->
                    _uiState.update { it.copy(allPeople = people) }
                }
            }
            launch {
                trackerRepository.getAllTrackersFlow().collect { trackers ->
                    _uiState.update { it.copy(allTrackers = trackers) }
                }
            }
        }
    }

    fun search(query: String) {
        currentQuery = query
        searchJob?.cancel()
        
        if (query.isBlank() && !_uiState.value.hasActiveFilters) {
            _uiState.update { it.copy(results = emptyList(), resultCount = 0) }
            return
        }
        
        searchJob = viewModelScope.launch {
            delay(300)
            applySearch()
        }
    }

    fun toggleFavoritesFilter() {
        _uiState.update { it.copy(filterFavorites = !it.filterFavorites) }
        triggerSearch()
    }

    fun toggleHasMediaFilter() {
        _uiState.update { it.copy(filterHasMedia = !it.filterHasMedia) }
        triggerSearch()
    }

    fun toggleHasLocationFilter() {
        _uiState.update { it.copy(filterHasLocation = !it.filterHasLocation) }
        triggerSearch()
    }

    fun toggleHasWeatherFilter() {
        _uiState.update { it.copy(filterHasWeather = !it.filterHasWeather) }
        triggerSearch()
    }

    fun setCategoryFilter(categoryId: Long) {
        _uiState.update { 
            it.copy(selectedCategoryId = if (it.selectedCategoryId == categoryId) null else categoryId) 
        }
        triggerSearch()
    }

    fun setMoodFilter(level: Int) {
        _uiState.update { 
            it.copy(selectedMoodLevel = if (it.selectedMoodLevel == level) null else level) 
        }
        triggerSearch()
    }

    fun toggleTagFilter(tagId: Long) {
        _uiState.update { state ->
            val newTags = if (tagId in state.selectedTagIds) state.selectedTagIds - tagId else state.selectedTagIds + tagId
            state.copy(selectedTagIds = newTags)
        }
        triggerSearch()
    }

    fun togglePersonFilter(personId: Long) {
        _uiState.update { state ->
            val newPeople = if (personId in state.selectedPersonIds) state.selectedPersonIds - personId else state.selectedPersonIds + personId
            state.copy(selectedPersonIds = newPeople)
        }
        triggerSearch()
    }

    fun toggleTrackerFilter(trackerId: Long) {
        _uiState.update { state ->
            val newTrackers = if (trackerId in state.selectedTrackerIds) state.selectedTrackerIds - trackerId else state.selectedTrackerIds + trackerId
            state.copy(selectedTrackerIds = newTrackers)
        }
        triggerSearch()
    }

    fun setTrackerValueQuery(query: String) {
        _uiState.update { it.copy(trackerValueQuery = query) }
        triggerSearch()
    }

    fun exportSearchResults(format: ExportFormat, includeMedia: Boolean = true) {
        viewModelScope.launch {
            val results = _uiState.value.results
            if (results.isEmpty()) {
                _uiState.update { it.copy(exportError = "No results to export") }
                return@launch
            }
            _uiState.update { it.copy(isExporting = true, exportSuccess = false, exportError = null) }
            try {
                val result = exportRepository.exportEntries(
                    entries = results,
                    format = format,
                    includeMedia = includeMedia
                )
                result.fold(
                    onSuccess = {
                        _uiState.update { it.copy(isExporting = false, exportSuccess = true) }
                    },
                    onFailure = { e ->
                        _uiState.update { it.copy(isExporting = false, exportError = e.message ?: "Export failed") }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(isExporting = false, exportError = e.message ?: "Export failed") }
            }
        }
    }

    fun clearExportState() {
        _uiState.update { it.copy(exportSuccess = false, exportError = null) }
    }

    fun setDateFrom(date: LocalDate?) {
        _uiState.update { it.copy(dateFrom = date) }
        triggerSearch()
    }

    fun setDateTo(date: LocalDate?) {
        _uiState.update { it.copy(dateTo = date) }
        triggerSearch()
    }

    fun setMinWordCount(count: Int?) {
        _uiState.update { it.copy(minWordCount = count) }
        triggerSearch()
    }

    fun setMaxWordCount(count: Int?) {
        _uiState.update { it.copy(maxWordCount = count) }
        triggerSearch()
    }

    fun setSortOrder(order: SearchSortOrder) {
        _uiState.update { it.copy(sortOrder = order) }
        triggerSearch()
    }

    fun clearAllFilters() {
        _uiState.update {
            SearchUiState(
                categories = it.categories,
                allTags = it.allTags,
                allPeople = it.allPeople,
                allTrackers = it.allTrackers
            )
        }
        currentQuery = ""
        triggerSearch()
    }

    fun toggleAdvancedFilters() {
        _uiState.update { it.copy(showAdvancedFilters = !it.showAdvancedFilters) }
    }

    private fun triggerSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch { applySearch() }
    }

    private suspend fun applySearch() {
        _uiState.update { it.copy(isSearching = true) }
        val state = _uiState.value
        
        val baseEntries = when {
            state.dateFrom != null || state.dateTo != null -> {
                val startMillis = (state.dateFrom ?: LocalDate.of(2000, 1, 1))
                    .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val endMillis = (state.dateTo ?: LocalDate.of(2100, 12, 31))
                    .atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                if (currentQuery.isNotBlank()) {
                    val searchResults = journalRepository.searchEntries(currentQuery).firstOrNull() ?: emptyList()
                    val dateResults = journalRepository.getEntriesInDateRange(startMillis, endMillis)
                    val dateIds = dateResults.map { it.id }.toSet()
                    searchResults.filter { it.id in dateIds }
                } else {
                    journalRepository.getEntriesInDateRange(startMillis, endMillis)
                }
            }
            currentQuery.isNotBlank() -> journalRepository.searchEntries(currentQuery).firstOrNull() ?: emptyList()
            else -> journalRepository.getAllEntries().firstOrNull() ?: emptyList()
        }
        
        val filtered = baseEntries.filter { entry ->
            val matchCategory = state.selectedCategoryId == null || entry.category?.id == state.selectedCategoryId
            val matchFavorite = !state.filterFavorites || entry.isFavorite
            val matchMood = state.selectedMoodLevel == null || entry.mood?.level == state.selectedMoodLevel
            val matchTags = state.selectedTagIds.isEmpty() || entry.tags.any { it.id in state.selectedTagIds }
            val matchPeople = state.selectedPersonIds.isEmpty() || entry.people.any { it.id in state.selectedPersonIds }
            val matchTrackers = state.selectedTrackerIds.isEmpty() || entry.trackerValues.keys.any { it in state.selectedTrackerIds }
            val matchTrackerValue = state.trackerValueQuery.isBlank() || entry.trackerValues.values.any { 
                it.contains(state.trackerValueQuery, ignoreCase = true)
            }
            val matchMedia = !state.filterHasMedia || entry.media.isNotEmpty()
            val matchLocation = !state.filterHasLocation || entry.location != null
            val matchWeather = !state.filterHasWeather || entry.weather != null
            val matchMinWords = state.minWordCount == null || entry.wordCount >= state.minWordCount
            val matchMaxWords = state.maxWordCount == null || entry.wordCount <= state.maxWordCount
            matchCategory && matchFavorite && matchMood && matchTags && matchPeople &&
                matchTrackers && matchTrackerValue && matchMedia && matchLocation && matchWeather &&
                matchMinWords && matchMaxWords
        }
        
        val sorted = when (state.sortOrder) {
            SearchSortOrder.NEWEST_FIRST -> filtered.sortedByDescending { it.createdAt }
            SearchSortOrder.OLDEST_FIRST -> filtered.sortedBy { it.createdAt }
            SearchSortOrder.TITLE_AZ -> filtered.sortedBy { it.title?.lowercase() ?: "" }
            SearchSortOrder.TITLE_ZA -> filtered.sortedByDescending { it.title?.lowercase() ?: "" }
            SearchSortOrder.LONGEST -> filtered.sortedByDescending { it.wordCount }
            SearchSortOrder.SHORTEST -> filtered.sortedBy { it.wordCount }
        }
        
        _uiState.update { it.copy(results = sorted, resultCount = sorted.size, isSearching = false) }
    }
}

enum class SearchSortOrder(val label: String) {
    NEWEST_FIRST("Newest first"),
    OLDEST_FIRST("Oldest first"),
    TITLE_AZ("Title A→Z"),
    TITLE_ZA("Title Z→A"),
    LONGEST("Longest"),
    SHORTEST("Shortest")
}

data class SearchUiState(
    val results: List<JournalEntry> = emptyList(),
    val resultCount: Int = 0,
    val isSearching: Boolean = false,
    val categories: List<Category> = emptyList(),
    val allTags: List<Tag> = emptyList(),
    val allPeople: List<Person> = emptyList(),
    val allTrackers: List<CustomTracker> = emptyList(),
    val selectedCategoryId: Long? = null,
    val filterFavorites: Boolean = false,
    val selectedMoodLevel: Int? = null,
    val selectedTagIds: Set<Long> = emptySet(),
    val selectedPersonIds: Set<Long> = emptySet(),
    val selectedTrackerIds: Set<Long> = emptySet(),
    val trackerValueQuery: String = "", // Sub-tracker value search (e.g., search for specific person in People tracker)
    val dateFrom: LocalDate? = null,
    val dateTo: LocalDate? = null,
    val minWordCount: Int? = null,
    val maxWordCount: Int? = null,
    val filterHasMedia: Boolean = false,
    val filterHasLocation: Boolean = false,
    val filterHasWeather: Boolean = false,
    val sortOrder: SearchSortOrder = SearchSortOrder.NEWEST_FIRST,
    val showAdvancedFilters: Boolean = false,
    val isExporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val exportError: String? = null
) {
    val hasActiveFilters: Boolean
        get() = selectedCategoryId != null || filterFavorites || selectedMoodLevel != null ||
                selectedTagIds.isNotEmpty() || selectedPersonIds.isNotEmpty() ||
                selectedTrackerIds.isNotEmpty() || trackerValueQuery.isNotBlank() ||
                dateFrom != null || dateTo != null ||
                minWordCount != null || maxWordCount != null || filterHasMedia ||
                filterHasLocation || filterHasWeather

    val activeFilterCount: Int
        get() {
            var count = 0
            if (selectedCategoryId != null) count++
            if (filterFavorites) count++
            if (selectedMoodLevel != null) count++
            count += selectedTagIds.size
            count += selectedPersonIds.size
            count += selectedTrackerIds.size
            if (trackerValueQuery.isNotBlank()) count++
            if (dateFrom != null || dateTo != null) count++
            if (minWordCount != null || maxWordCount != null) count++
            if (filterHasMedia) count++
            if (filterHasLocation) count++
            if (filterHasWeather) count++
            return count
        }
}
