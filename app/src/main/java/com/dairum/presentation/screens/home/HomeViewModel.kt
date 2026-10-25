package com.dairum.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.Category
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.Memory
import com.dairum.domain.model.Person
import com.dairum.domain.model.Tag
import com.dairum.domain.repository.CategoryRepository
import com.dairum.domain.repository.ExportRepository
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.MemoryRepository
import com.dairum.domain.repository.PersonRepository
import com.dairum.domain.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val categoryRepository: CategoryRepository,
    private val tagRepository: TagRepository,
    private val personRepository: PersonRepository,
    private val memoryRepository: MemoryRepository,
    private val exportRepository: ExportRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    private val _viewMode = MutableStateFlow(ViewMode.TIMELINE)
    val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()
    
    // Keep a reference to all entries for selection
    private var allEntries: List<JournalEntry> = emptyList()
    
    init {
        loadEntries()
        loadCategories()
        loadTags()
        loadPeople()
        loadMemories()
    }
    
    private fun loadEntries() {
        viewModelScope.launch {
            journalRepository.getAllEntries()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { entries ->
                    allEntries = entries
                    _uiState.update { it.copy(entries = entries, isLoading = false) }
                }
        }
    }
    
    private fun loadMemories() {
        viewModelScope.launch {
            memoryRepository.getAllMemoriesFlow()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { memories ->
                    _uiState.update { it.copy(memories = memories) }
                }
        }
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllWithCountFlow()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { categories ->
                    _uiState.update { it.copy(categories = categories) }
                }
        }
    }
    
    private fun loadTags() {
        viewModelScope.launch {
            tagRepository.getAllWithCountFlow()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { tags ->
                    _uiState.update { it.copy(tags = tags) }
                }
        }
    }
    
    private fun loadPeople() {
        viewModelScope.launch {
            personRepository.getAllWithCountFlow()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { people ->
                    _uiState.update { it.copy(people = people) }
                }
        }
    }
    
    fun setViewMode(mode: ViewMode) {
        _viewMode.value = mode
    }
    
    fun toggleFavorite(entryId: Long) {
        viewModelScope.launch {
            val entry = _uiState.value.entries.find { it.id == entryId } ?: return@launch
            journalRepository.toggleFavorite(entryId, !entry.isFavorite)
        }
    }
    
    fun togglePin(entryId: Long) {
        viewModelScope.launch {
            val entry = _uiState.value.entries.find { it.id == entryId } ?: return@launch
            journalRepository.togglePin(entryId, !entry.isPinned)
        }
    }
    
    fun archiveEntry(entryId: Long) {
        viewModelScope.launch {
            val entry = _uiState.value.entries.find { it.id == entryId } ?: return@launch
            journalRepository.archiveEntry(entryId, !entry.isArchived)
        }
    }
    
    fun duplicateEntry(entryId: Long) {
        viewModelScope.launch {
            val entry = _uiState.value.entries.find { it.id == entryId } ?: return@launch
            // Create a copy with new id
            val duplicate = entry.copy(
                id = 0,
                title = "Copy of ${entry.title ?: "Untitled"}",
                createdAt = java.time.LocalDateTime.now(),
                updatedAt = java.time.LocalDateTime.now()
            )
            journalRepository.createEntry(duplicate)
        }
    }
    
    fun clearFilter() {
        loadEntries()
    }

    fun searchEntries(query: String) {
        if (query.isBlank()) {
            loadEntries()
            return
        }
        
        viewModelScope.launch {
            journalRepository.searchEntries(query)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { entries ->
                    _uiState.update { it.copy(entries = entries, searchQuery = query) }
                }
        }
    }
    
    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        loadEntries()
    }
    
    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
    
    // Selection mode functions
    fun toggleEntrySelection(entryId: Long) {
        val currentSelected = _uiState.value.selectedEntryIds.toMutableSet()
        if (currentSelected.contains(entryId)) {
            currentSelected.remove(entryId)
        } else {
            currentSelected.add(entryId)
        }
        
        _uiState.update { 
            it.copy(
                selectedEntryIds = currentSelected,
                isSelectionMode = currentSelected.isNotEmpty()
            ) 
        }
    }
    
    fun selectAllEntries() {
        val allIds = _uiState.value.entries.map { it.id }.toSet()
        _uiState.update { 
            it.copy(
                selectedEntryIds = allIds,
                isSelectionMode = true
            ) 
        }
    }
    
    fun clearSelection() {
        _uiState.update { 
            it.copy(
                selectedEntryIds = emptySet(),
                isSelectionMode = false
            ) 
        }
    }
    
    fun getSelectedEntries(): List<JournalEntry> {
        return _uiState.value.entries.filter { it.id in _uiState.value.selectedEntryIds }
    }
    
    /**
     * Add selected entries to an existing memory
     */
    fun addEntriesToMemory(memoryId: Long) {
        viewModelScope.launch {
            val entryIds = _uiState.value.selectedEntryIds.toList()
            if (entryIds.isNotEmpty()) {
                memoryRepository.addEntriesToMemory(memoryId, entryIds)
                clearSelection()
            }
        }
    }
    
    fun exportSelectedToPdf() {
        viewModelScope.launch {
            val entries = getSelectedEntries()
            if (entries.isEmpty()) return@launch
            _uiState.update { it.copy(isExporting = true) }
            val result = exportRepository.exportToPdf(entries, includeMedia = true)
            _uiState.update {
                it.copy(
                    isExporting = false,
                    exportResult = result.fold(
                        onSuccess = { pathWithSize -> "PDF exported successfully" },
                        onFailure = { e -> "Export failed: ${e.message}" }
                    ),
                    exportFilePath = result.getOrNull()?.split("|")?.firstOrNull()
                )
            }
            clearSelection()
        }
    }

    fun clearExportResult() {
        _uiState.update { it.copy(exportResult = null, exportFilePath = null) }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            // Small delay for visual feedback
            kotlinx.coroutines.delay(300)
            loadEntries()
            loadCategories()
            loadTags()
            loadPeople()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }
}

data class HomeUiState(
    val entries: List<JournalEntry> = emptyList(),
    val categories: List<Category> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val people: List<Person> = emptyList(),
    val memories: List<Memory> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null,
    // Selection mode
    val isSelectionMode: Boolean = false,
    val selectedEntryIds: Set<Long> = emptySet(),
    // Export
    val isExporting: Boolean = false,
    val exportResult: String? = null,
    val exportFilePath: String? = null
)

enum class ViewMode {
    TIMELINE,
    CALENDAR,
    CATEGORY,
    TAG,
    MOOD,
    PEOPLE,
    LOCATION,
    STORY
}
