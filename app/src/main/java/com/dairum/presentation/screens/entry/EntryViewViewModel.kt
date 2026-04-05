package com.dairum.presentation.screens.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryViewViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val mediaRepository: MediaRepository,
    private val trackerRepository: TrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EntryViewUiState())
    val uiState: StateFlow<EntryViewUiState> = _uiState.asStateFlow()

    fun loadEntry(entryId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val entry = journalRepository.getEntry(entryId)
                val media = mediaRepository.getMediaForEntry(entryId)
                val trackers = trackerRepository.getAllTrackers()
                
                _uiState.update {
                    it.copy(
                        entry = entry,
                        media = media,
                        trackers = trackers,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val entry = _uiState.value.entry ?: return@launch
            val newFavorite = !entry.isFavorite
            journalRepository.toggleFavorite(entry.id, newFavorite)
            _uiState.update {
                it.copy(entry = entry.copy(isFavorite = newFavorite))
            }
        }
    }

    fun deleteEntry() {
        viewModelScope.launch {
            val entry = _uiState.value.entry ?: return@launch
            journalRepository.deleteEntry(entry.id)
        }
    }
}

data class EntryViewUiState(
    val entry: JournalEntry? = null,
    val media: List<Media> = emptyList(),
    val trackers: List<CustomTracker> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
