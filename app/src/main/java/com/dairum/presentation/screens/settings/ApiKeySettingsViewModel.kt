package com.dairum.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.repository.ApiKeyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiKeySettingsViewModel @Inject constructor(
    private val apiKeyRepository: ApiKeyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiKeySettingsUiState())
    val uiState: StateFlow<ApiKeySettingsUiState> = _uiState.asStateFlow()

    init {
        loadApiKeys()
    }

    private fun loadApiKeys() {
        viewModelScope.launch {
            combine(
                apiKeyRepository.getGoogleMapsApiKey(),
                apiKeyRepository.getWeatherApiKey(),
                apiKeyRepository.getGoogleCalendarApiKey()
            ) { mapsKey, weatherKey, calendarKey ->
                ApiKeySettingsUiState(
                    googleMapsApiKey = mapsKey ?: "",
                    weatherApiKey = weatherKey ?: "",
                    googleCalendarApiKey = calendarKey ?: "",
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun setGoogleMapsApiKey(key: String) {
        _uiState.update { it.copy(googleMapsApiKey = key, isGoogleMapsKeyValid = null, testResult = null) }
        viewModelScope.launch {
            apiKeyRepository.setGoogleMapsApiKey(key.takeIf { it.isNotBlank() })
        }
    }

    fun setWeatherApiKey(key: String) {
        _uiState.update { it.copy(weatherApiKey = key, isWeatherKeyValid = null, testResult = null) }
        viewModelScope.launch {
            apiKeyRepository.setWeatherApiKey(key.takeIf { it.isNotBlank() })
        }
    }

    fun setGoogleCalendarApiKey(key: String) {
        _uiState.update { it.copy(googleCalendarApiKey = key, isGoogleCalendarKeyValid = null, testResult = null) }
        viewModelScope.launch {
            apiKeyRepository.setGoogleCalendarApiKey(key.takeIf { it.isNotBlank() })
        }
    }

    fun testConnections() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTesting = true, testResult = null) }
            
            var allValid = true
            var message = StringBuilder()
            
            // Test Google Maps
            if (_uiState.value.googleMapsApiKey.isNotBlank()) {
                val mapsValid = apiKeyRepository.validateGoogleMapsKey(_uiState.value.googleMapsApiKey)
                _uiState.update { it.copy(isGoogleMapsKeyValid = mapsValid) }
                if (!mapsValid) {
                    allValid = false
                    message.append("Google Maps: Invalid\n")
                } else {
                    message.append("Google Maps: Valid ✓\n")
                }
            }
            
            // Test Weather API
            if (_uiState.value.weatherApiKey.isNotBlank()) {
                val weatherValid = apiKeyRepository.validateWeatherKey(_uiState.value.weatherApiKey)
                _uiState.update { it.copy(isWeatherKeyValid = weatherValid) }
                if (!weatherValid) {
                    allValid = false
                    message.append("Weather API: Invalid\n")
                } else {
                    message.append("Weather API: Valid ✓\n")
                }
            }
            
            // Test Google Calendar
            if (_uiState.value.googleCalendarApiKey.isNotBlank()) {
                val calendarValid = apiKeyRepository.validateGoogleCalendarKey(_uiState.value.googleCalendarApiKey)
                _uiState.update { it.copy(isGoogleCalendarKeyValid = calendarValid) }
                if (!calendarValid) {
                    allValid = false
                    message.append("Google Calendar: Invalid\n")
                } else {
                    message.append("Google Calendar: Valid ✓\n")
                }
            }
            
            val finalMessage = if (message.isEmpty()) {
                "No API keys configured to test"
            } else {
                message.toString().trim()
            }
            
            _uiState.update { 
                it.copy(
                    isTesting = false,
                    testResult = TestResult(
                        success = allValid && message.isNotEmpty(),
                        message = finalMessage
                    )
                )
            }
        }
    }
}

data class ApiKeySettingsUiState(
    val googleMapsApiKey: String = "",
    val weatherApiKey: String = "",
    val googleCalendarApiKey: String = "",
    val isGoogleMapsKeyValid: Boolean? = null,
    val isWeatherKeyValid: Boolean? = null,
    val isGoogleCalendarKeyValid: Boolean? = null,
    val isLoading: Boolean = true,
    val isTesting: Boolean = false,
    val testResult: TestResult? = null
)

data class TestResult(
    val success: Boolean,
    val message: String
)
