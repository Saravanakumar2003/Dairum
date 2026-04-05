package com.dairum.presentation.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.Category
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.repository.CategoryRepository
import com.dairum.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryEntriesUiState(
    val category: Category? = null,
    val entries: List<JournalEntry> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class CategoryEntriesViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryEntriesUiState())
    val uiState: StateFlow<CategoryEntriesUiState> = _uiState.asStateFlow()

    fun loadCategory(categoryId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // Load category details
                val category = categoryRepository.getCategoryById(categoryId)
                _uiState.update { it.copy(category = category) }
                
                // Load entries for this category
                journalRepository.getEntriesByCategory(categoryId)
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
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun toggleFavorite(entryId: Long) {
        viewModelScope.launch {
            val entry = _uiState.value.entries.find { it.id == entryId } ?: return@launch
            journalRepository.toggleFavorite(entryId, !entry.isFavorite)
        }
    }
}
