package com.dairum.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.Memory
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val entries: List<JournalEntry> = emptyList(),
    val favoriteMemories: List<Memory> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val memoryRepository: MemoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    init {
        loadFavorites()
        loadFavoriteMemories()
    }
    
    private fun loadFavorites() {
        viewModelScope.launch {
            journalRepository.getFavoriteEntries()
                .collect { entries ->
                    _uiState.update { 
                        it.copy(
                            entries = entries.sortedByDescending { entry -> entry.createdAt },
                            isLoading = false
                        )
                    }
                }
        }
    }
    
    private fun loadFavoriteMemories() {
        viewModelScope.launch {
            memoryRepository.getFavoriteMemoriesFlow()
                .collect { memories ->
                    _uiState.update { 
                        it.copy(
                            favoriteMemories = memories.sortedByDescending { memory -> memory.startDate },
                            isLoading = false
                        )
                    }
                }
        }
    }
    
    fun toggleFavorite(entryId: Long) {
        viewModelScope.launch {
            // Remove from favorites (toggle to false)
            journalRepository.toggleFavorite(entryId, false)
        }
    }
    
    fun toggleMemoryFavorite(memoryId: Long) {
        viewModelScope.launch {
            // Remove from favorites (toggle to false)
            memoryRepository.toggleFavorite(memoryId, false)
        }
    }
}
