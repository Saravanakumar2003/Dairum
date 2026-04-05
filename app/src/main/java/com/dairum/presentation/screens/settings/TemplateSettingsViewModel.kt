package com.dairum.presentation.screens.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.EntryTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

private val Context.templateDataStore: DataStore<Preferences> by preferencesDataStore(name = "templates")

@HiltViewModel
class TemplateSettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val dataStore = context.templateDataStore
    
    private val _uiState = MutableStateFlow(TemplateSettingsUiState())
    val uiState: StateFlow<TemplateSettingsUiState> = _uiState.asStateFlow()
    
    companion object {
        private val CUSTOM_TEMPLATES_KEY = stringPreferencesKey("custom_templates")
    }
    
    init {
        loadCustomTemplates()
    }
    
    private fun loadCustomTemplates() {
        viewModelScope.launch {
            dataStore.data.collect { preferences ->
                val templatesJson = preferences[CUSTOM_TEMPLATES_KEY] ?: "[]"
                val templates = parseTemplatesFromJson(templatesJson)
                _uiState.update { it.copy(customTemplates = templates) }
            }
        }
    }
    
    fun createTemplate(name: String, icon: String, defaultTitle: String, promptText: String) {
        viewModelScope.launch {
            val newTemplate = EntryTemplate(
                id = System.currentTimeMillis(),
                name = name,
                icon = icon,
                defaultTitle = defaultTitle.takeIf { it.isNotBlank() },
                promptText = promptText.takeIf { it.isNotBlank() },
                isCustom = true,
                sortOrder = _uiState.value.customTemplates.size
            )
            
            val updatedList = _uiState.value.customTemplates + newTemplate
            saveTemplates(updatedList)
        }
    }
    
    fun updateTemplate(template: EntryTemplate) {
        viewModelScope.launch {
            val updatedList = _uiState.value.customTemplates.map {
                if (it.id == template.id) template else it
            }
            saveTemplates(updatedList)
        }
    }
    
    fun deleteTemplate(id: Long) {
        viewModelScope.launch {
            val updatedList = _uiState.value.customTemplates.filter { it.id != id }
            saveTemplates(updatedList)
        }
    }
    
    private suspend fun saveTemplates(templates: List<EntryTemplate>) {
        val json = templatesToJson(templates)
        dataStore.edit { preferences ->
            preferences[CUSTOM_TEMPLATES_KEY] = json
        }
    }
    
    private fun templatesToJson(templates: List<EntryTemplate>): String {
        val jsonArray = JSONArray()
        templates.forEach { template ->
            val jsonObject = JSONObject().apply {
                put("id", template.id)
                put("name", template.name)
                put("icon", template.icon)
                put("defaultTitle", template.defaultTitle ?: "")
                put("promptText", template.promptText ?: "")
                put("sortOrder", template.sortOrder)
            }
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }
    
    private fun parseTemplatesFromJson(json: String): List<EntryTemplate> {
        return try {
            val jsonArray = JSONArray(json)
            (0 until jsonArray.length()).map { index ->
                val obj = jsonArray.getJSONObject(index)
                EntryTemplate(
                    id = obj.getLong("id"),
                    name = obj.getString("name"),
                    icon = obj.optString("icon", "📝"),
                    defaultTitle = obj.optString("defaultTitle").takeIf { it.isNotBlank() },
                    promptText = obj.optString("promptText").takeIf { it.isNotBlank() },
                    isCustom = true,
                    sortOrder = obj.optInt("sortOrder", 0)
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

data class TemplateSettingsUiState(
    val customTemplates: List<EntryTemplate> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
