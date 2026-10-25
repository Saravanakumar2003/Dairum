package com.dairum.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

private val Context.gratiaDataStore: DataStore<Preferences> by preferencesDataStore(name = "gratia_settings")

enum class BackgroundMusicOption(val label: String, val description: String) {
    NONE("None", "No background music"),
    GENTLE_WAVES("Gentle Waves", "Soft sine waves at healing frequencies"),
    DEEP_CALM("Deep Calm", "Low-frequency ambient drone"),
    NATURE_BREEZE("Nature Breeze", "Gentle wind-like white noise"),
    MEDITATION_BELLS("Meditation Bells", "Soft periodic bell tones"),
    OCEAN_DRIFT("Ocean Drift", "Rhythmic wave-like oscillation"),
    CUSTOM("Custom", "Pick your own audio file")
}

enum class VoiceGender(val label: String, val description: String) {
    AUTO("Auto", "Use system default voice"),
    MALE("Male", "Prefer deeper male voice"),
    FEMALE("Female", "Prefer higher female voice")
}

enum class VisualEffect(val label: String, val emoji: String) {
    NONE("None", ""),
    SNOW("Snowfall", "❄️"),
    RAIN("Rain", "🌧️"),
    FIREFLIES("Fireflies", "✨"),
    AURORA("Aurora", "🌌"),
    SAKURA("Cherry Blossoms", "🌸"),
    STARS("Starfield", "⭐"),
    BUBBLES("Bubbles", "🫧")
}

data class CustomMusicEntry(
    val name: String,
    val uri: String
)

data class GratiaSettings(
    val ttsSpeed: Float = 0.9f,
    val ttsPitch: Float = 1.0f,
    val ttsVoice: String = "",  // empty = system default
    val voiceGender: VoiceGender = VoiceGender.AUTO,
    val backgroundMusic: BackgroundMusicOption = BackgroundMusicOption.GENTLE_WAVES,
    val backgroundMusicVolume: Float = 0.15f,
    val customMusicUri: String = "",
    val customMusicList: List<CustomMusicEntry> = emptyList(),
    val selectedCustomMusicIndex: Int = 0,
    val pauseBetweenAffirmations: Int = 3, // seconds
    val autoPlayOnOpen: Boolean = false,
    val repeatSession: Boolean = false,
    val showBreathingAnimation: Boolean = true,
    val visualEffect: VisualEffect = VisualEffect.NONE,
    val visualEffectIntensity: Float = 0.5f
) {
    val activeCustomMusicUri: String get() {
        if (customMusicList.isNotEmpty() && selectedCustomMusicIndex in customMusicList.indices) {
            return customMusicList[selectedCustomMusicIndex].uri
        }
        return customMusicUri
    }
}

@Singleton
class GratiaPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.gratiaDataStore

    private object Keys {
        val TTS_SPEED = floatPreferencesKey("tts_speed")
        val TTS_PITCH = floatPreferencesKey("tts_pitch")
        val TTS_VOICE = stringPreferencesKey("tts_voice")
        val VOICE_GENDER = stringPreferencesKey("voice_gender")
        val BACKGROUND_MUSIC = stringPreferencesKey("background_music")
        val BACKGROUND_MUSIC_VOLUME = floatPreferencesKey("background_music_volume")
        val CUSTOM_MUSIC_URI = stringPreferencesKey("custom_music_uri")
        val CUSTOM_MUSIC_LIST = stringPreferencesKey("custom_music_list")
        val SELECTED_CUSTOM_MUSIC = intPreferencesKey("selected_custom_music")
        val PAUSE_BETWEEN = intPreferencesKey("pause_between_affirmations")
        val AUTO_PLAY = booleanPreferencesKey("auto_play_on_open")
        val REPEAT_SESSION = booleanPreferencesKey("repeat_session")
        val SHOW_BREATHING = booleanPreferencesKey("show_breathing_animation")
        val VISUAL_EFFECT = stringPreferencesKey("visual_effect")
        val VISUAL_EFFECT_INTENSITY = floatPreferencesKey("visual_effect_intensity")
    }

    val settings: Flow<GratiaSettings> = dataStore.data.map { prefs ->
        GratiaSettings(
            ttsSpeed = prefs[Keys.TTS_SPEED] ?: 0.9f,
            ttsPitch = prefs[Keys.TTS_PITCH] ?: 1.0f,
            ttsVoice = prefs[Keys.TTS_VOICE] ?: "",
            voiceGender = try {
                VoiceGender.valueOf(prefs[Keys.VOICE_GENDER] ?: "AUTO")
            } catch (e: Exception) {
                VoiceGender.AUTO
            },
            backgroundMusic = try {
                BackgroundMusicOption.valueOf(prefs[Keys.BACKGROUND_MUSIC] ?: "GENTLE_WAVES")
            } catch (e: Exception) {
                BackgroundMusicOption.GENTLE_WAVES
            },
            backgroundMusicVolume = prefs[Keys.BACKGROUND_MUSIC_VOLUME] ?: 0.15f,
            customMusicUri = prefs[Keys.CUSTOM_MUSIC_URI] ?: "",
            customMusicList = parseCustomMusicList(prefs[Keys.CUSTOM_MUSIC_LIST] ?: ""),
            selectedCustomMusicIndex = prefs[Keys.SELECTED_CUSTOM_MUSIC] ?: 0,
            pauseBetweenAffirmations = prefs[Keys.PAUSE_BETWEEN] ?: 3,
            autoPlayOnOpen = prefs[Keys.AUTO_PLAY] ?: false,
            repeatSession = prefs[Keys.REPEAT_SESSION] ?: false,
            showBreathingAnimation = prefs[Keys.SHOW_BREATHING] ?: true,
            visualEffect = try {
                VisualEffect.valueOf(prefs[Keys.VISUAL_EFFECT] ?: "NONE")
            } catch (e: Exception) {
                VisualEffect.NONE
            },
            visualEffectIntensity = prefs[Keys.VISUAL_EFFECT_INTENSITY] ?: 0.5f
        )
    }

    suspend fun updateTtsSpeed(speed: Float) {
        dataStore.edit { it[Keys.TTS_SPEED] = speed }
    }

    suspend fun updateTtsPitch(pitch: Float) {
        dataStore.edit { it[Keys.TTS_PITCH] = pitch }
    }

    suspend fun updateTtsVoice(voice: String) {
        dataStore.edit { it[Keys.TTS_VOICE] = voice }
    }

    suspend fun updateBackgroundMusic(option: BackgroundMusicOption) {
        dataStore.edit { it[Keys.BACKGROUND_MUSIC] = option.name }
    }

    suspend fun updateBackgroundMusicVolume(volume: Float) {
        dataStore.edit { it[Keys.BACKGROUND_MUSIC_VOLUME] = volume }
    }

    suspend fun updatePauseBetween(seconds: Int) {
        dataStore.edit { it[Keys.PAUSE_BETWEEN] = seconds }
    }

    suspend fun updateAutoPlay(enabled: Boolean) {
        dataStore.edit { it[Keys.AUTO_PLAY] = enabled }
    }

    suspend fun updateRepeatSession(enabled: Boolean) {
        dataStore.edit { it[Keys.REPEAT_SESSION] = enabled }
    }

    suspend fun updateShowBreathing(enabled: Boolean) {
        dataStore.edit { it[Keys.SHOW_BREATHING] = enabled }
    }

    suspend fun updateVoiceGender(gender: VoiceGender) {
        dataStore.edit { it[Keys.VOICE_GENDER] = gender.name }
    }

    suspend fun updateCustomMusicUri(uri: String) {
        dataStore.edit { it[Keys.CUSTOM_MUSIC_URI] = uri }
    }

    suspend fun updateCustomMusicList(list: List<CustomMusicEntry>) {
        dataStore.edit { it[Keys.CUSTOM_MUSIC_LIST] = serializeCustomMusicList(list) }
    }

    suspend fun updateSelectedCustomMusic(index: Int) {
        dataStore.edit { it[Keys.SELECTED_CUSTOM_MUSIC] = index }
    }

    suspend fun updateVisualEffect(effect: VisualEffect) {
        dataStore.edit { it[Keys.VISUAL_EFFECT] = effect.name }
    }

    suspend fun updateVisualEffectIntensity(intensity: Float) {
        dataStore.edit { it[Keys.VISUAL_EFFECT_INTENSITY] = intensity }
    }

    private fun parseCustomMusicList(json: String): List<CustomMusicEntry> {
        if (json.isBlank()) return emptyList()
        return try {
            val arr = JSONArray(json)
            (0 until arr.length()).map { i ->
                val obj = arr.getJSONObject(i)
                CustomMusicEntry(
                    name = obj.getString("name"),
                    uri = obj.getString("uri")
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun serializeCustomMusicList(list: List<CustomMusicEntry>): String {
        val arr = JSONArray()
        list.forEach { entry ->
            arr.put(JSONObject().apply {
                put("name", entry.name)
                put("uri", entry.uri)
            })
        }
        return arr.toString()
    }
}
