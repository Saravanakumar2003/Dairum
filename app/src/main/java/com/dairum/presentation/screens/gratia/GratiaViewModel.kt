package com.dairum.presentation.screens.gratia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.*
import com.dairum.domain.repository.GratiaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GratiaViewModel @Inject constructor(
    private val repository: GratiaRepository
) : ViewModel() {

    val gratitudeFolders: StateFlow<List<GratitudeFolder>> = repository.getAllGratitudeFolders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val affirmationFolders: StateFlow<List<AffirmationFolder>> = repository.getAllAffirmationFolders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Inline detail state
    private val _currentPrompts = MutableStateFlow<List<GratitudePrompt>>(emptyList())
    val currentPrompts: StateFlow<List<GratitudePrompt>> = _currentPrompts.asStateFlow()

    private val _currentAffirmations = MutableStateFlow<List<Affirmation>>(emptyList())
    val currentAffirmations: StateFlow<List<Affirmation>> = _currentAffirmations.asStateFlow()

    private val _randomPrompt = MutableStateFlow<GratitudePrompt?>(null)
    val randomPrompt: StateFlow<GratitudePrompt?> = _randomPrompt.asStateFlow()

    private var currentFolderId: Long = 0L
    private var promptsJob: kotlinx.coroutines.Job? = null
    private var affirmationsJob: kotlinx.coroutines.Job? = null

    fun loadPromptsForFolder(folderId: Long) {
        currentFolderId = folderId
        promptsJob?.cancel()
        promptsJob = viewModelScope.launch {
            repository.getPromptsByFolder(folderId).collect { _currentPrompts.value = it }
        }
    }

    fun loadAffirmationsForFolder(folderId: Long) {
        currentFolderId = folderId
        affirmationsJob?.cancel()
        affirmationsJob = viewModelScope.launch {
            repository.getAffirmationsByFolder(folderId).collect { _currentAffirmations.value = it }
        }
    }

    fun addPrompt(folderId: Long, text: String) {
        viewModelScope.launch {
            repository.insertPrompt(GratitudePrompt(folderId = folderId, prompt = text))
        }
    }

    fun deletePrompt(prompt: GratitudePrompt) {
        viewModelScope.launch { repository.deletePrompt(prompt) }
    }

    fun shufflePrompt(folderId: Long) {
        viewModelScope.launch {
            _randomPrompt.value = repository.getRandomPrompt(folderId)
        }
    }

    fun clearRandomPrompt() {
        _randomPrompt.value = null
    }

    fun addAffirmation(folderId: Long, text: String) {
        viewModelScope.launch {
            repository.insertAffirmation(Affirmation(folderId = folderId, text = text))
        }
    }

    fun deleteAffirmation(affirmation: Affirmation) {
        viewModelScope.launch { repository.deleteAffirmation(affirmation) }
    }

    fun createGratitudeFolder(name: String, emoji: String, description: String?) {
        viewModelScope.launch {
            repository.insertGratitudeFolder(
                GratitudeFolder(name = name, emoji = emoji, description = description)
            )
        }
    }

    fun deleteGratitudeFolder(folder: GratitudeFolder) {
        viewModelScope.launch { repository.deleteGratitudeFolder(folder) }
    }

    fun createAffirmationFolder(name: String, emoji: String, description: String?) {
        viewModelScope.launch {
            repository.insertAffirmationFolder(
                AffirmationFolder(name = name, emoji = emoji, description = description)
            )
        }
    }

    fun deleteAffirmationFolder(folder: AffirmationFolder) {
        viewModelScope.launch { repository.deleteAffirmationFolder(folder) }
    }
}
