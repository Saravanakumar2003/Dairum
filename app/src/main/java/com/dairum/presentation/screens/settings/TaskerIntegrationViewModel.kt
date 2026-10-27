package com.dairum.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.repository.MIN_AUTOMATION_TOKEN_LENGTH
import com.dairum.domain.repository.SettingsRepository
import com.dairum.domain.repository.TaskerPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskerIntegrationViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskerIntegrationUiState())
    val uiState: StateFlow<TaskerIntegrationUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getTaskerEnabledFlow().collect { enabled ->
                _uiState.update { it.copy(isEnabled = enabled) }
            }
        }
        
        viewModelScope.launch {
            settingsRepository.getAutomationTokenSetFlow().collect { set ->
                _uiState.update { it.copy(tokenSet = set) }
            }
        }

        viewModelScope.launch {
            settingsRepository.getTaskerDefaultFormatFlow().collect { format ->
                _uiState.update { it.copy(defaultFormat = format) }
            }
        }
        
        viewModelScope.launch {
            settingsRepository.getTaskerDefaultPeriodFlow().collect { period ->
                _uiState.update { it.copy(defaultPeriod = period) }
            }
        }
        
        viewModelScope.launch {
            settingsRepository.getTaskerDefaultIncludeMediaFlow().collect { include ->
                _uiState.update { it.copy(defaultIncludeMedia = include) }
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setTaskerEnabled(enabled)
            _uiState.update { it.copy(isEnabled = enabled) }
        }
    }

    fun setDefaultFormat(format: ExportFormat) {
        viewModelScope.launch {
            settingsRepository.setTaskerDefaultFormat(format)
            _uiState.update { it.copy(defaultFormat = format) }
        }
    }

    fun setDefaultPeriod(period: TaskerPeriod) {
        viewModelScope.launch {
            settingsRepository.setTaskerDefaultPeriod(period)
            _uiState.update { it.copy(defaultPeriod = period) }
        }
    }

    fun setDefaultIncludeMedia(include: Boolean) {
        viewModelScope.launch {
            settingsRepository.setTaskerDefaultIncludeMedia(include)
            _uiState.update { it.copy(defaultIncludeMedia = include) }
        }
    }

    /** Returns an error message, or null when saved. */
    fun saveToken(token: String, onResult: (String?) -> Unit) {
        val trimmed = token.trim()
        if (trimmed.length < MIN_AUTOMATION_TOKEN_LENGTH) {
            onResult("At least $MIN_AUTOMATION_TOKEN_LENGTH characters")
            return
        }
        viewModelScope.launch {
            settingsRepository.setAutomationToken(trimmed)
            onResult(null)
        }
    }

    fun clearToken() {
        viewModelScope.launch { settingsRepository.clearAutomationToken() }
    }

    fun setCopySuccess() {
        _uiState.update { it.copy(copySuccess = true) }
    }

    fun clearCopySuccess() {
        _uiState.update { it.copy(copySuccess = false) }
    }
}

data class TaskerIntegrationUiState(
    val isEnabled: Boolean = false,
    val defaultFormat: ExportFormat = ExportFormat.PDF,
    val defaultPeriod: TaskerPeriod = TaskerPeriod.ALL,
    val defaultIncludeMedia: Boolean = true,
    val tokenSet: Boolean = false,
    val copySuccess: Boolean = false
)
