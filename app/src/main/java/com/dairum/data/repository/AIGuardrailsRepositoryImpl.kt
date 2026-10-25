package com.dairum.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dairum.domain.model.AIGuardrailDefaults
import com.dairum.domain.model.AIGuardrailSettings
import com.dairum.domain.model.GuardrailRule
import com.dairum.domain.repository.AIGuardrailsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.aiGuardrailsDataStore by preferencesDataStore(name = "ai_guardrails")

@Singleton
class AIGuardrailsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AIGuardrailsRepository {

    companion object {
        private val SYSTEM_PROMPT_KEY = stringPreferencesKey("system_prompt")
        private val RULES_KEY = stringPreferencesKey("rules_json")
    }

    private val dataStore = context.aiGuardrailsDataStore

    override fun getSettingsFlow(): Flow<AIGuardrailSettings> {
        return dataStore.data.map { it.toSettings() }
    }

    override suspend fun getSettings(): AIGuardrailSettings {
        return dataStore.data.first().toSettings()
    }

    override suspend fun updateSystemPrompt(prompt: String) {
        dataStore.edit { it[SYSTEM_PROMPT_KEY] = prompt }
    }

    override suspend fun resetSystemPrompt() {
        dataStore.edit { it[SYSTEM_PROMPT_KEY] = AIGuardrailDefaults.DEFAULT_SYSTEM_PROMPT }
    }

    override suspend fun addRule(text: String) {
        if (text.isBlank()) return
        val current = getSettings().rules
        val newRule = GuardrailRule(id = UUID.randomUUID().toString(), text = text.trim(), isEnabled = true)
        saveRules(current + newRule)
    }

    override suspend fun removeRule(id: String) {
        saveRules(getSettings().rules.filterNot { it.id == id })
    }

    override suspend fun setRuleEnabled(id: String, isEnabled: Boolean) {
        saveRules(getSettings().rules.map { if (it.id == id) it.copy(isEnabled = isEnabled) else it })
    }

    override suspend fun updateRuleText(id: String, text: String) {
        saveRules(getSettings().rules.map { if (it.id == id) it.copy(text = text) else it })
    }

    override suspend fun resetToDefaults() {
        dataStore.edit {
            it[SYSTEM_PROMPT_KEY] = AIGuardrailDefaults.DEFAULT_SYSTEM_PROMPT
            it[RULES_KEY] = rulesToJson(AIGuardrailDefaults.DEFAULT_RULES)
        }
    }

    private suspend fun saveRules(rules: List<GuardrailRule>) {
        dataStore.edit { it[RULES_KEY] = rulesToJson(rules) }
    }

    private fun Preferences.toSettings(): AIGuardrailSettings {
        val prompt = this[SYSTEM_PROMPT_KEY] ?: AIGuardrailDefaults.DEFAULT_SYSTEM_PROMPT
        val rulesJson = this[RULES_KEY]
        val rules = if (rulesJson.isNullOrBlank()) {
            AIGuardrailDefaults.DEFAULT_RULES
        } else {
            parseRulesJson(rulesJson)
        }
        return AIGuardrailSettings(systemPrompt = prompt, rules = rules)
    }

    private fun rulesToJson(rules: List<GuardrailRule>): String {
        val array = JSONArray()
        rules.forEach { rule ->
            array.put(JSONObject().apply {
                put("id", rule.id)
                put("text", rule.text)
                put("isEnabled", rule.isEnabled)
                put("isBuiltIn", rule.isBuiltIn)
            })
        }
        return array.toString()
    }

    private fun parseRulesJson(json: String): List<GuardrailRule> {
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { index ->
                val obj = array.getJSONObject(index)
                GuardrailRule(
                    id = obj.getString("id"),
                    text = obj.getString("text"),
                    isEnabled = obj.optBoolean("isEnabled", true),
                    isBuiltIn = obj.optBoolean("isBuiltIn", false)
                )
            }
        } catch (e: Exception) {
            AIGuardrailDefaults.DEFAULT_RULES
        }
    }
}
