package com.dairum.presentation.screens.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.Location
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.MediaRepository
import com.dairum.domain.repository.MediaWithEntryInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilesUiState())
    val uiState: StateFlow<FilesUiState> = _uiState.asStateFlow()

    init {
        loadPhotos()
        loadVideos()
        loadVoiceNotes()
        loadAttachments()
        loadLocations()
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            mediaRepository.getPhotosByEntryFlow()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { photos ->
                    _uiState.update { it.copy(photos = photos, isLoading = false) }
                }
        }
    }

    private fun loadVideos() {
        viewModelScope.launch {
            mediaRepository.getVideosByEntryFlow()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { videos ->
                    _uiState.update { it.copy(videos = videos) }
                }
        }
    }

    private fun loadVoiceNotes() {
        viewModelScope.launch {
            mediaRepository.getVoiceNotesByEntryFlow()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { voiceNotes ->
                    _uiState.update { it.copy(voiceNotes = voiceNotes) }
                }
        }
    }

    private fun loadAttachments() {
        viewModelScope.launch {
            mediaRepository.getAttachmentsByEntryFlow()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { attachments ->
                    _uiState.update { it.copy(attachments = attachments) }
                }
        }
    }

    private fun loadLocations() {
        viewModelScope.launch {
            journalRepository.getAllEntries()
                .catch { e ->
                    android.util.Log.e("FilesViewModel", "Error loading entries for locations: ${e.message}")
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { entries ->
                    android.util.Log.d("FilesViewModel", "loadLocations: ${entries.size} total entries")
                    entries.forEachIndexed { index, entry ->
                        android.util.Log.d("FilesViewModel", "  Entry[$index] id=${entry.id}: location=${entry.location?.let { "lat=${it.latitude}, lng=${it.longitude}" } ?: "null"}")
                    }
                    
                    // Filter entries with locations and group by location
                    val entriesWithLocation = entries.filter { it.location != null }
                    android.util.Log.d("FilesViewModel", "Entries with location: ${entriesWithLocation.size}")
                    
                    // Group entries by location (using lat/lng as key)
                    val locationGroups = entriesWithLocation
                        .groupBy { entry ->
                            val loc = entry.location!!
                            // Round to 4 decimal places to group nearby locations
                            "${(loc.latitude * 10000).toInt()}_${(loc.longitude * 10000).toInt()}"
                        }
                        .map { (_, groupEntries) ->
                            val representativeLocation = groupEntries.first().location!!
                            LocationWithEntries(
                                location = representativeLocation,
                                entries = groupEntries.sortedByDescending { it.createdAt }
                            )
                        }
                        .sortedByDescending { it.entries.size }
                    
                    android.util.Log.d("FilesViewModel", "Location groups created: ${locationGroups.size}")
                    _uiState.update { it.copy(locations = locationGroups) }
                }
        }
    }
}

data class LocationWithEntries(
    val location: Location,
    val entries: List<JournalEntry>
)

data class FilesUiState(
    val photos: List<MediaWithEntryInfo> = emptyList(),
    val videos: List<MediaWithEntryInfo> = emptyList(),
    val voiceNotes: List<MediaWithEntryInfo> = emptyList(),
    val attachments: List<MediaWithEntryInfo> = emptyList(),
    val locations: List<LocationWithEntries> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
