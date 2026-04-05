package com.dairum.presentation.screens.memories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.*
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MemoriesViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository,
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemoriesUiState())
    val uiState: StateFlow<MemoriesUiState> = _uiState.asStateFlow()

    private val _selectedMemory = MutableStateFlow<Memory?>(null)
    val selectedMemory: StateFlow<Memory?> = _selectedMemory.asStateFlow()

    init {
        loadMemories()
    }

    private fun loadMemories() {
        viewModelScope.launch {
            memoryRepository.getAllMemoriesFlow().collect { memories ->
                // Group memories by year
                val groupedByYear = memories.groupBy { it.startDate.year }
                    .toSortedMap(reverseOrder())
                
                _uiState.update { 
                    it.copy(
                        memories = memories,
                        memoriesByYear = groupedByYear,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectMemory(memoryId: Long) {
        viewModelScope.launch {
            memoryRepository.getMemoryByIdFlow(memoryId).collect { memory ->
                _selectedMemory.value = memory
            }
        }
    }

    fun clearSelectedMemory() {
        _selectedMemory.value = null
    }

    fun createMemory(
        title: String,
        description: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        entryIds: List<Long> = emptyList()
    ) {
        viewModelScope.launch {
            val memory = Memory(
                title = title,
                description = description,
                startDate = startDate,
                endDate = endDate
            )
            val memoryId = memoryRepository.createMemory(memory)
            
            if (entryIds.isNotEmpty()) {
                memoryRepository.addEntriesToMemory(memoryId, entryIds)
            }
            
            _uiState.update { it.copy(showCreateDialog = false) }
        }
    }

    fun updateMemory(memory: Memory) {
        viewModelScope.launch {
            memoryRepository.updateMemory(memory)
        }
    }

    fun deleteMemory(memoryId: Long) {
        viewModelScope.launch {
            memoryRepository.deleteMemory(memoryId)
            _selectedMemory.value = null
        }
    }

    fun toggleFavorite(memoryId: Long) {
        viewModelScope.launch {
            val memory = _uiState.value.memories.find { it.id == memoryId } ?: return@launch
            memoryRepository.toggleFavorite(memoryId, !memory.isFavorite)
        }
    }

    fun addEntriesToMemory(memoryId: Long, entryIds: List<Long>) {
        viewModelScope.launch {
            memoryRepository.addEntriesToMemory(memoryId, entryIds)
        }
    }

    fun removeEntryFromMemory(memoryId: Long, entryId: Long) {
        viewModelScope.launch {
            memoryRepository.removeEntryFromMemory(memoryId, entryId)
        }
    }

    // Milestones
    fun addMilestone(memoryId: Long, title: String, description: String?, date: LocalDateTime) {
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

    // Trackers
    fun addTracker(memoryId: Long, name: String, type: MemoryTrackerType, unit: String?, goalValue: String?) {
        viewModelScope.launch {
            memoryRepository.addMemoryTracker(
                MemoryTracker(
                    memoryId = memoryId,
                    name = name,
                    type = type,
                    unit = unit,
                    goalValue = goalValue
                )
            )
        }
    }

    fun updateTracker(tracker: MemoryTracker) {
        viewModelScope.launch {
            memoryRepository.updateMemoryTracker(tracker)
        }
    }

    fun deleteTracker(trackerId: Long) {
        viewModelScope.launch {
            memoryRepository.deleteMemoryTracker(trackerId)
        }
    }

    // Suggestions
    fun getSuggestedEntries(memoryId: Long) {
        viewModelScope.launch {
            val suggestions = memoryRepository.suggestEntriesForMemory(memoryId)
            _uiState.update { it.copy(suggestedEntries = suggestions) }
        }
    }

    fun generateHighlights(memoryId: Long) {
        viewModelScope.launch {
            val highlights = memoryRepository.generateMemoryHighlights(memoryId)
            // Update the memory with new highlights
            _selectedMemory.value?.let { memory ->
                memoryRepository.updateMemory(memory.copy(highlights = highlights))
            }
        }
    }

    // UI State
    fun showCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    fun hideCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = false) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.isBlank()) {
            loadMemories()
        } else {
            viewModelScope.launch {
                memoryRepository.searchMemories(query).collect { memories ->
                    _uiState.update { it.copy(memories = memories) }
                }
            }
        }
    }
}

data class MemoriesUiState(
    val memories: List<Memory> = emptyList(),
    val memoriesByYear: Map<Int, List<Memory>> = emptyMap(),
    val suggestedEntries: List<JournalEntry> = emptyList(),
    val isLoading: Boolean = true,
    val showCreateDialog: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null
)
