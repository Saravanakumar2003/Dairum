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
class AffirmationFolderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: GratiaRepository
) : ViewModel() {

    private val folderId: Long = savedStateHandle.get<Long>("folderId") ?: 0L

    private val _folder = MutableStateFlow<AffirmationFolder?>(null)
    val folder: StateFlow<AffirmationFolder?> = _folder.asStateFlow()

    val affirmations: StateFlow<List<Affirmation>> = repository.getAffirmationsByFolder(folderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            _folder.value = repository.getAffirmationFolder(folderId)
        }
    }

    fun addAffirmation(text: String) {
        viewModelScope.launch {
            val currentCount = affirmations.value.size
            repository.insertAffirmation(
                Affirmation(folderId = folderId, text = text, sortOrder = currentCount)
            )
        }
    }

    fun deleteAffirmation(affirmation: Affirmation) {
        viewModelScope.launch { repository.deleteAffirmation(affirmation) }
    }

    fun updateAffirmation(affirmation: Affirmation) {
        viewModelScope.launch { repository.updateAffirmation(affirmation) }
    }

    fun updateBackgroundAudio(uri: String?) {
        viewModelScope.launch {
            _folder.value?.let { folder ->
                val updated = folder.copy(backgroundAudioUri = uri)
                repository.updateAffirmationFolder(updated)
                _folder.value = updated
            }
        }
    }
}
