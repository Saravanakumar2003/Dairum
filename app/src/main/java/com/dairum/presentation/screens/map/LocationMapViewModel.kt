package com.dairum.presentation.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.Location
import com.dairum.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationMapViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationMapUiState())
    val uiState: StateFlow<LocationMapUiState> = _uiState.asStateFlow()

    init {
        loadEntriesWithLocations()
    }

    private fun loadEntriesWithLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            journalRepository.getAllEntries()
                .catch { e ->
                    android.util.Log.e("LocationMapVM", "Error loading entries: ${e.message}")
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { entries ->
                    android.util.Log.d("LocationMapVM", "Total entries loaded: ${entries.size}")
                    
                    // Debug each entry's location status
                    entries.forEachIndexed { index, entry ->
                        android.util.Log.d("LocationMapVM", "Entry $index (id=${entry.id}): location=${entry.location?.let { "lat=${it.latitude}, lng=${it.longitude}, place=${it.placeName}" } ?: "null"}")
                    }
                    
                    // Filter entries with locations and group by location
                    val entriesWithLocation = entries.filter { it.location != null }
                    android.util.Log.d("LocationMapVM", "Entries with location: ${entriesWithLocation.size}")
                    
                    // Group entries by location (using lat/lng as key)
                    val locationGroups = entriesWithLocation
                        .groupBy { entry ->
                            val loc = entry.location!!
                            // Round to 4 decimal places to group nearby locations
                            val key = "${(loc.latitude * 10000).toInt()}_${(loc.longitude * 10000).toInt()}"
                            key
                        }
                        .map { (_, groupEntries) ->
                            // Use the first entry's location as the representative location
                            val representativeLocation = groupEntries.first().location!!
                            LocationWithEntries(
                                location = representativeLocation,
                                entries = groupEntries.sortedByDescending { it.createdAt }
                            )
                        }
                        .sortedByDescending { it.entries.size }
                    
                    android.util.Log.d("LocationMapVM", "Location groups: ${locationGroups.size}, total entries with location: ${entriesWithLocation.size}")
                    
                    _uiState.update { 
                        it.copy(
                            locations = locationGroups,
                            totalEntries = entriesWithLocation.size,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun selectLocation(location: LocationWithEntries) {
        _uiState.update { it.copy(selectedLocation = location) }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedLocation = null) }
    }
}

data class LocationMapUiState(
    val locations: List<LocationWithEntries> = emptyList(),
    val selectedLocation: LocationWithEntries? = null,
    val totalEntries: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)
