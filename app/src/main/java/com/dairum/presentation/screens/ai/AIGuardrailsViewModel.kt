package com.dairum.presentation.screens.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.AIGuardrailSettings
import com.dairum.domain.model.GuardrailRule
import com.dairum.domain.repository.AIGuardrailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AIGuardrailsUiState(
    val settings: AIGuardrailSettings = AIGuardrailSettings(),
    val editedSystemPrompt: String = "",
    val newRuleText: String = "",
    val successMessage: String? = null
)

@HiltViewModel
class AIGuardrailsViewModel @Inject constructor(
    private val guardrailsRepository: AIGuardrailsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AIGuardrailsUiState())
    val uiState: StateFlow<AIGuardrailsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            guardrailsRepository.getSettingsFlow().collect { settings ->
                _uiState.update {
                    it.copy(
                        settings = settings,
                        // Don't clobber in-progress edits with every DataStore emission
                        editedSystemPrompt = if (it.editedSystemPrompt.isBlank()) settings.systemPrompt else it.editedSystemPrompt
                    )
                }
            }
        }
    }

    fun updateEditedSystemPrompt(text: String) {
        _uiState.update { it.copy(editedSystemPrompt = text) }
    }

    fun saveSystemPrompt() {
        viewModelScope.launch {
            guardrailsRepository.updateSystemPrompt(_uiState.value.editedSystemPrompt)
            _uiState.update { it.copy(successMessage = "System prompt updated") }
        }
    }

    fun resetSystemPrompt() {
        viewModelScope.launch {
            guardrailsRepository.resetSystemPrompt()
        }
    }

    fun updateNewRuleText(text: String) {
        _uiState.update { it.copy(newRuleText = text) }
    }

    fun addRule() {
        val text = _uiState.value.newRuleText
        if (text.isBlank()) return
        viewModelScope.launch {
            guardrailsRepository.addRule(text)
            _uiState.update { it.copy(newRuleText = "") }
        }
    }

    fun toggleRule(rule: GuardrailRule) {
        viewModelScope.launch {
            guardrailsRepository.setRuleEnabled(rule.id, !rule.isEnabled)
        }
    }

    fun removeRule(rule: GuardrailRule) {
        viewModelScope.launch {
            guardrailsRepository.removeRule(rule.id)
        }
    }

    fun resetToDefaults() {
        viewModelScope.launch {
            guardrailsRepository.resetToDefaults()
            _uiState.update { it.copy(editedSystemPrompt = "") } // Re-sync from the flow on next emission
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}
