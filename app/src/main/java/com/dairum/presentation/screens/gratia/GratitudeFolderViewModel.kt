package com.dairum.presentation.screens.gratia

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.*
import com.dairum.domain.repository.GratiaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GratitudeFolderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: GratiaRepository
) : ViewModel() {

    private val folderId: Long = savedStateHandle.get<Long>("folderId") ?: 0L

    private val _folder = MutableStateFlow<GratitudeFolder?>(null)
    val folder: StateFlow<GratitudeFolder?> = _folder.asStateFlow()

    val prompts: StateFlow<List<GratitudePrompt>> = repository.getPromptsByFolder(folderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _randomPrompt = MutableStateFlow<GratitudePrompt?>(null)
    val randomPrompt: StateFlow<GratitudePrompt?> = _randomPrompt.asStateFlow()

    init {
        viewModelScope.launch {
            _folder.value = repository.getGratitudeFolder(folderId)
        }
    }

    fun addPrompt(text: String) {
        viewModelScope.launch {
            repository.insertPrompt(GratitudePrompt(folderId = folderId, prompt = text))
        }
    }

    fun deletePrompt(prompt: GratitudePrompt) {
        viewModelScope.launch { repository.deletePrompt(prompt) }
    }

    fun shufflePrompt() {
        viewModelScope.launch {
            _randomPrompt.value = repository.getRandomPrompt(folderId)
        }
    }

    fun clearRandomPrompt() {
        _randomPrompt.value = null
    }
}
