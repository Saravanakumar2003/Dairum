package com.dairum.presentation.screens.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.Category
import com.dairum.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageCategoriesUiState())
    val uiState: StateFlow<ManageCategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllWithCountFlow()
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { categories ->
                    _uiState.update { it.copy(categories = categories, isLoading = false) }
                }
        }
    }

    fun createCategory(name: String, color: Int, icon: String) {
        viewModelScope.launch {
            categoryRepository.create(
                Category(
                    name = name,
                    color = color,
                    icon = icon
                )
            )
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.update(category)
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            categoryRepository.delete(id)
        }
    }

    fun setDefaultCategory(id: Long) {
        viewModelScope.launch {
            categoryRepository.setDefault(id)
        }
    }
}

data class ManageCategoriesUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
