package com.dairum.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.Category
import com.dairum.domain.repository.CategoryRepository
import com.dairum.domain.repository.SettingsRepository
import com.dairum.domain.repository.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                settingsRepository.getThemeModeFlow(),
                settingsRepository.getDynamicColorsFlow(),
                settingsRepository.getWeatherAutoFetchFlow(),
                settingsRepository.getLocationAutoFetchFlow(),
                settingsRepository.getAppLockEnabledFlow()
            ) { themeMode, dynamicColors, weatherAuto, locationAuto, appLock ->
                SettingsUiState(
                    themeMode = themeMode,
                    useDynamicColors = dynamicColors,
                    weatherAutoFetch = weatherAuto,
                    locationAutoFetch = locationAuto,
                    appLockEnabled = appLock,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }

        // Load default category
        viewModelScope.launch {
            settingsRepository.getDefaultCategoryIdFlow().collect { categoryId ->
                if (categoryId != null) {
                    categoryRepository.getCategoryById(categoryId)?.let { category ->
                        _uiState.update { it.copy(defaultCategory = category) }
                    }
                }
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }

    fun setDynamicColors(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDynamicColors(enabled)
        }
    }

    fun setWeatherAutoFetch(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setWeatherAutoFetch(enabled)
        }
    }

    fun setLocationAutoFetch(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setLocationAutoFetch(enabled)
        }
    }

    fun setAppLock(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAppLockEnabled(enabled)
        }
    }

    fun setDefaultCategory(category: Category) {
        viewModelScope.launch {
            settingsRepository.setDefaultCategoryId(category.id)
            _uiState.update { it.copy(defaultCategory = category) }
        }
    }
}

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val useDynamicColors: Boolean = true,
    val weatherAutoFetch: Boolean = true,
    val locationAutoFetch: Boolean = false,
    val appLockEnabled: Boolean = false,
    val defaultCategory: Category? = null,
    val isLoading: Boolean = true
)
