package com.dairum.presentation.screens.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.*
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoryUiState())
    val uiState: StateFlow<StoryUiState> = _uiState.asStateFlow()

    init {
        loadMemories()
    }

    private fun loadMemories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val today = LocalDate.now()
                val memories = mutableMapOf<Int, List<JournalEntry>>()
                
                // Go back up to 10 years
                for (year in (today.year - 1) downTo (today.year - 10)) {
                    val pastDate = today.withYear(year)
                    
                    // Get entries from the same week (3 days before and after)
                    val startDate = pastDate.minusDays(3)
                    val endDate = pastDate.plusDays(3)
                    
                    val entries = journalRepository.getEntriesInDateRange(
                        startDate = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        endDate = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    )
                    
                    if (entries.isNotEmpty()) {
                        memories[year] = entries.sortedByDescending { it.createdAt }
                    }
                }
                
                _uiState.update { 
                    it.copy(
                        memories = memories,
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

    fun toggleFavorite(entryId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            journalRepository.toggleFavorite(entryId, isFavorite)
            loadMemories() // Refresh to update the UI
        }
    }
}

data class StoryUiState(
    val memories: Map<Int, List<JournalEntry>> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null
)
