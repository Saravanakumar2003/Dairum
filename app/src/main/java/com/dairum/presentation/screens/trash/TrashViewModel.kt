package com.dairum.presentation.screens.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrashUiState())
    val uiState: StateFlow<TrashUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            journalRepository.getDeletedEntries()
                .collect { entries ->
                    _uiState.update { it.copy(entries = entries) }
                }
        }
    }

    fun restoreEntry(id: Long) {
        viewModelScope.launch {
            journalRepository.restoreEntry(id)
        }
    }

    fun permanentlyDelete(id: Long) {
        viewModelScope.launch {
            journalRepository.deleteEntry(id)
        }
    }

    fun emptyTrash() {
        viewModelScope.launch {
            journalRepository.emptyTrash()
        }
    }
}

data class TrashUiState(
    val entries: List<JournalEntry> = emptyList()
)
