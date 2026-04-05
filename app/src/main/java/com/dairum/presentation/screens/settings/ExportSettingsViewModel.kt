package com.dairum.presentation.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.ExportConfig
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.repository.ExportRepository
import com.dairum.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportSettingsViewModel @Inject constructor(
    private val exportRepository: ExportRepository,
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportSettingsUiState())
    val uiState: StateFlow<ExportSettingsUiState> = _uiState.asStateFlow()

    init {
        loadConfig()
    }

    private fun loadConfig() {
        viewModelScope.launch {
            val config = exportRepository.getExportConfig()
            if (config != null) {
                _uiState.update {
                    it.copy(
                        configId = config.id,
                        autoExportEnabled = config.isEnabled,
                        scheduleHour = config.scheduleHour,
                        scheduleMinute = config.scheduleMinute,
                        scheduleDays = config.scheduleDays,
                        exportFormat = config.format,
                        includeMedia = config.includeMedia,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun setAutoExportEnabled(enabled: Boolean) {
        _uiState.update { it.copy(autoExportEnabled = enabled) }
        saveConfig()
    }

    fun setScheduleHour(hour: Int) {
        _uiState.update { it.copy(scheduleHour = hour) }
        saveConfig()
    }

    fun setScheduleMinute(minute: Int) {
        _uiState.update { it.copy(scheduleMinute = minute) }
        saveConfig()
    }

    fun toggleScheduleDay(dayOfWeek: Int) {
        val currentDays = _uiState.value.scheduleDays
        val newDays = if (dayOfWeek in currentDays) {
            currentDays - dayOfWeek
        } else {
            currentDays + dayOfWeek
        }
        _uiState.update { it.copy(scheduleDays = newDays) }
        saveConfig()
    }

    fun setExportFormat(format: ExportFormat) {
        _uiState.update { it.copy(exportFormat = format) }
        saveConfig()
    }

    fun setIncludeMedia(include: Boolean) {
        _uiState.update { it.copy(includeMedia = include) }
        saveConfig()
    }

    private fun saveConfig() {
        viewModelScope.launch {
            val state = _uiState.value
            val config = ExportConfig(
                id = state.configId,
                name = "Auto Export",
                format = state.exportFormat,
                scheduleHour = state.scheduleHour,
                scheduleMinute = state.scheduleMinute,
                scheduleDays = state.scheduleDays,
                isEnabled = state.autoExportEnabled,
                includeMedia = state.includeMedia
            )
            
            if (state.configId == 0L) {
                val newId = exportRepository.createConfig(config)
                _uiState.update { it.copy(configId = newId) }
            } else {
                exportRepository.updateConfig(config)
            }
        }
    }

    fun exportNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true) }
            try {
                journalRepository.getAllEntries().first().let { entries ->
                    exportRepository.exportEntries(
                        entries = entries,
                        format = _uiState.value.exportFormat,
                        includeMedia = _uiState.value.includeMedia
                    )
                }
                _uiState.update { it.copy(isExporting = false, exportSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isExporting = false, error = e.message) }
            }
        }
    }

    fun exportWithDateRange(startDate: Long?, endDate: Long?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true) }
            try {
                val entriesFlow = if (startDate != null && endDate != null) {
                    journalRepository.getEntriesInDateRangeFlow(startDate, endDate)
                } else {
                    journalRepository.getAllEntries()
                }
                
                val entries = entriesFlow.first()
                
                if (entries.isEmpty()) {
                    _uiState.update { 
                        it.copy(
                            isExporting = false, 
                            error = "No entries found in the selected date range"
                        ) 
                    }
                    return@launch
                }
                
                val result = exportRepository.exportEntries(
                    entries = entries,
                    format = _uiState.value.exportFormat,
                    includeMedia = _uiState.value.includeMedia
                )
                
                result.fold(
                    onSuccess = { exportHistory ->
                        _uiState.update { 
                            it.copy(
                                isExporting = false, 
                                exportSuccess = true,
                                lastExportPath = exportHistory.filePath,
                                lastExportEntryCount = entries.size
                            ) 
                        }
                    },
                    onFailure = { e ->
                        _uiState.update { 
                            it.copy(
                                isExporting = false, 
                                error = e.message ?: "Export failed"
                            ) 
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(isExporting = false, error = e.message ?: "Unknown error") }
            }
        }
    }
    
    fun shareLastExport() {
        val path = _uiState.value.lastExportPath
        if (path != null) {
            _uiState.update { it.copy(shareFilePath = path) }
        }
    }
    
    fun clearSharePath() {
        _uiState.update { it.copy(shareFilePath = null) }
    }
    
    fun exportToExternalLocation(startDate: Long?, endDate: Long?, destinationUri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true) }
            try {
                val entriesFlow = if (startDate != null && endDate != null) {
                    journalRepository.getEntriesInDateRangeFlow(startDate, endDate)
                } else {
                    journalRepository.getAllEntries()
                }
                
                val entries = entriesFlow.first()
                
                if (entries.isEmpty()) {
                    _uiState.update { 
                        it.copy(
                            isExporting = false, 
                            error = "No entries found in the selected date range"
                        ) 
                    }
                    return@launch
                }
                
                val result = exportRepository.exportToExternalUri(
                    entries = entries,
                    format = _uiState.value.exportFormat,
                    includeMedia = _uiState.value.includeMedia,
                    destinationUri = destinationUri
                )
                
                result.fold(
                    onSuccess = { exportHistory ->
                        _uiState.update { 
                            it.copy(
                                isExporting = false, 
                                exportSuccess = true,
                                lastExportPath = null, // External export - no internal path
                                lastExportEntryCount = entries.size
                            ) 
                        }
                    },
                    onFailure = { e ->
                        _uiState.update { 
                            it.copy(
                                isExporting = false, 
                                error = e.message ?: "Export failed"
                            ) 
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(isExporting = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    fun setStartDate(timestamp: Long?) {
        _uiState.update { it.copy(startDate = timestamp) }
    }

    fun setEndDate(timestamp: Long?) {
        _uiState.update { it.copy(endDate = timestamp) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(exportSuccess = false) }
    }
}

data class ExportSettingsUiState(
    val configId: Long = 0,
    val autoExportEnabled: Boolean = false,
    val scheduleHour: Int = 23,
    val scheduleMinute: Int = 0,
    val scheduleDays: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7), // All days
    val exportFormat: ExportFormat = ExportFormat.TXT,
    val includeMedia: Boolean = true,
    val startDate: Long? = null, // For one-time export
    val endDate: Long? = null, // For one-time export
    val isLoading: Boolean = true,
    val isExporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val error: String? = null,
    val lastExportPath: String? = null,
    val lastExportEntryCount: Int = 0,
    val shareFilePath: String? = null
)
