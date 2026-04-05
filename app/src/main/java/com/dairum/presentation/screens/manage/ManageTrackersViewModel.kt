package com.dairum.presentation.screens.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.CustomTracker
import com.dairum.domain.model.TrackerType
import com.dairum.domain.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageTrackersViewModel @Inject constructor(
    private val trackerRepository: TrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageTrackersUiState())
    val uiState: StateFlow<ManageTrackersUiState> = _uiState.asStateFlow()

    init {
        loadTrackers()
    }

    private fun loadTrackers() {
        viewModelScope.launch {
            trackerRepository.getAllTrackersFlow()
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { trackers ->
                    _uiState.update { it.copy(trackers = trackers, isLoading = false) }
                }
        }
    }

    fun createTracker(name: String, type: TrackerType, unit: String?) {
        viewModelScope.launch {
            trackerRepository.createTracker(
                CustomTracker(
                    name = name,
                    type = type,
                    unit = unit,
                    isActive = true
                )
            )
        }
    }

    fun updateTracker(tracker: CustomTracker) {
        viewModelScope.launch {
            trackerRepository.updateTracker(tracker)
        }
    }

    fun deleteTracker(id: Long) {
        viewModelScope.launch {
            trackerRepository.deleteTracker(id)
        }
    }

    fun toggleActive(id: Long, isActive: Boolean) {
        viewModelScope.launch {
            trackerRepository.toggleTrackerActive(id, isActive)
        }
    }
}

data class ManageTrackersUiState(
    val trackers: List<CustomTracker> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
