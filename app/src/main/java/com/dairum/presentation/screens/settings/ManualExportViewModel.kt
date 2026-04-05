package com.dairum.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.model.ExportHistory
import com.dairum.domain.repository.ExportRepository
import com.dairum.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ManualExportViewModel @Inject constructor(
    private val exportRepository: ExportRepository,
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManualExportUiState())
    val uiState: StateFlow<ManualExportUiState> = _uiState.asStateFlow()

    init {
        loadExportHistory()
    }

    private fun loadExportHistory() {
        viewModelScope.launch {
            exportRepository.getExportHistoryFlow().collect { history ->
                _uiState.update { it.copy(exportHistory = history) }
            }
        }
    }

    fun setDatePeriod(period: DatePeriod) {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        
        val (startDate, endDate) = when (period) {
            DatePeriod.ALL -> null to null
            DatePeriod.LAST_DAY -> {
                calendar.timeInMillis = now
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis to now
            }
            DatePeriod.LAST_WEEK -> {
                calendar.timeInMillis = now
                calendar.add(Calendar.WEEK_OF_YEAR, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis to now
            }
            DatePeriod.LAST_MONTH -> {
                calendar.timeInMillis = now
                calendar.add(Calendar.MONTH, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis to now
            }
            DatePeriod.CUSTOM -> _uiState.value.startDate to _uiState.value.endDate
        }
        
        _uiState.update { 
            it.copy(
                selectedPeriod = period,
                startDate = startDate,
                endDate = endDate
            ) 
        }
    }

    fun setStartDate(timestamp: Long?) {
        _uiState.update { 
            it.copy(
                startDate = timestamp,
                selectedPeriod = DatePeriod.CUSTOM
            ) 
        }
    }

    fun setEndDate(timestamp: Long?) {
        _uiState.update { 
            it.copy(
                endDate = timestamp,
                selectedPeriod = DatePeriod.CUSTOM
            ) 
        }
    }

    fun setExportFormat(format: ExportFormat) {
        _uiState.update { it.copy(exportFormat = format) }
    }

    fun setIncludeMedia(include: Boolean) {
        _uiState.update { it.copy(includeMedia = include) }
    }

    fun setExportDestination(destination: String?) {
        _uiState.update { it.copy(exportDestination = destination) }
    }

    fun exportEntries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true) }
            try {
                val state = _uiState.value
                
                // Get entries based on date range
                val entriesFlow = if (state.startDate != null && state.endDate != null) {
                    journalRepository.getEntriesInDateRangeFlow(state.startDate, state.endDate)
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
                    format = state.exportFormat,
                    includeMedia = state.includeMedia
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
                _uiState.update { 
                    it.copy(
                        isExporting = false, 
                        error = e.message ?: "Unknown error"
                    ) 
                }
            }
        }
    }

    fun shareLastExport() {
        val path = _uiState.value.lastExportPath
        if (path != null) {
            _uiState.update { it.copy(shareFilePath = path) }
        }
    }

    fun shareExport(filePath: String) {
        _uiState.update { it.copy(shareFilePath = filePath) }
    }

    fun clearSharePath() {
        _uiState.update { it.copy(shareFilePath = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(exportSuccess = false) }
    }
}

data class ManualExportUiState(
    val selectedPeriod: DatePeriod = DatePeriod.ALL,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val exportFormat: ExportFormat = ExportFormat.PDF,
    val includeMedia: Boolean = true,
    val exportDestination: String? = null,
    val isExporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val error: String? = null,
    val lastExportPath: String? = null,
    val lastExportEntryCount: Int = 0,
    val shareFilePath: String? = null,
    val exportHistory: List<ExportHistory> = emptyList()
)
