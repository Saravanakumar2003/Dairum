package com.dairum.presentation.screens.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.Tag
import com.dairum.domain.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageTagsViewModel @Inject constructor(
    private val tagRepository: TagRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageTagsUiState())
    val uiState: StateFlow<ManageTagsUiState> = _uiState.asStateFlow()

    init {
        loadTags()
    }

    private fun loadTags() {
        viewModelScope.launch {
            tagRepository.getAllWithCountFlow()
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { tags ->
                    _uiState.update { it.copy(tags = tags, isLoading = false) }
                }
        }
    }

    fun createTag(name: String) {
        viewModelScope.launch {
            tagRepository.create(Tag(name = name))
        }
    }

    fun updateTag(tag: Tag) {
        viewModelScope.launch {
            tagRepository.update(tag)
        }
    }

    fun deleteTag(id: Long) {
        viewModelScope.launch {
            tagRepository.delete(id)
        }
    }
}

data class ManageTagsUiState(
    val tags: List<Tag> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
