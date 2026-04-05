package com.dairum.presentation.screens.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.Tag
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagEntriesViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val tagRepository: TagRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TagEntriesUiState())
    val uiState: StateFlow<TagEntriesUiState> = _uiState.asStateFlow()

    fun loadEntriesForTag(tagId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Load tag details
            tagRepository.getById(tagId)?.let { tag ->
                _uiState.update { it.copy(tag = tag) }
            }
            
            // Load entries with this tag
            journalRepository.getEntriesByTag(tagId)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { entries ->
                    _uiState.update { 
                        it.copy(
                            entries = entries,
                            isLoading = false
                        )
                    }
                }
        }
    }
}

data class TagEntriesUiState(
    val tag: Tag? = null,
    val entries: List<JournalEntry> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
