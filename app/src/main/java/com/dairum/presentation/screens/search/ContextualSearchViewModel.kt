package com.dairum.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.search.ContextualMatch
import com.dairum.data.search.ContextualSearchService
import com.dairum.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContextualSearchUiState(
    val query: String = "",
    val results: List<ContextualMatch> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false
)

/**
 * Backs the "Contextual Search" screen: finds diary entries by memory/keyword/event, matching
 * on content, tags, people, locations, and dates - not just plain full-text search.
 */
@HiltViewModel
class ContextualSearchViewModel @Inject constructor(
    private val contextualSearchService: ContextualSearchService,
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContextualSearchUiState())
    val uiState: StateFlow<ContextualSearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update { it.copy(results = emptyList(), isSearching = false, hasSearched = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // Debounce as the user types
            _uiState.update { it.copy(isSearching = true) }
            val results = contextualSearchService.search(query, limit = 30)
            _uiState.update { it.copy(results = results, isSearching = false, hasSearched = true) }
        }
    }

    fun toggleFavorite(entryId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            journalRepository.toggleFavorite(entryId, isFavorite)
        }
    }
}
