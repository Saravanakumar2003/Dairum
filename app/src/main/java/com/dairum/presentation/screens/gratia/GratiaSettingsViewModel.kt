package com.dairum.presentation.screens.gratia

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.preferences.BackgroundMusicOption
import com.dairum.data.preferences.CustomMusicEntry
import com.dairum.data.preferences.GratiaPreferences
import com.dairum.data.preferences.GratiaSettings
import com.dairum.data.preferences.VisualEffect
import com.dairum.data.preferences.VoiceGender
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GratiaSettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: GratiaPreferences
) : ViewModel() {

    val settings: StateFlow<GratiaSettings> = preferences.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GratiaSettings())

    private val _availableVoices = MutableStateFlow<List<VoiceInfo>>(emptyList())
    val availableVoices: StateFlow<List<VoiceInfo>> = _availableVoices.asStateFlow()

    private val _testingVoice = MutableStateFlow(false)
    val testingVoice: StateFlow<Boolean> = _testingVoice.asStateFlow()

    private val _testingMusic = MutableStateFlow(false)
    val testingMusic: StateFlow<Boolean> = _testingMusic.asStateFlow()

    private val _testingVisual = MutableStateFlow(false)
    val testingVisual: StateFlow<Boolean> = _testingVisual.asStateFlow()

    private val _testingAll = MutableStateFlow(false)
    val testingAll: StateFlow<Boolean> = _testingAll.asStateFlow()

    private var tts: TextToSpeech? = null
    private var testMusicPlayer: MediaPlayer? = null
    private var testAmbientTrack: AudioTrack? = null
    private var testMusicJob: Job? = null

    init {
        loadVoices()
    }

    private fun loadVoices() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val voices = tts?.voices?.filter { 
                    it.locale.language == Locale.getDefault().language || 
                    it.locale.language == "en"
                }?.map { voice ->
                    val nameLower = voice.name.lowercase()
                    val features = voice.features ?: emptySet()
                    val gender = when {
                        // Check Android voice features first (most reliable)
                        features.any { it.contains("female", ignoreCase = true) } -> VoiceGender.FEMALE
                        features.any { it.contains("male", ignoreCase = true) && !it.contains("female", ignoreCase = true) } -> VoiceGender.MALE
                        // Check voice name patterns (common across TTS engines)
                        nameLower.contains("female") || nameLower.contains("woman") || 
                        nameLower.contains("-f-") || nameLower.endsWith("-f") ||
                        nameLower.contains("_f_") || nameLower.endsWith("_f") -> VoiceGender.FEMALE
                        nameLower.contains("male") || nameLower.contains("man") ||
                        nameLower.contains("-m-") || nameLower.endsWith("-m") ||
                        nameLower.contains("_m_") || nameLower.endsWith("_m") -> VoiceGender.MALE
                        else -> VoiceGender.AUTO
                    }
                    VoiceInfo(
                        name = voice.name,
                        displayName = buildString {
                            append(voice.locale.displayName)
                            val genderTag = when (gender) {
                                VoiceGender.FEMALE -> " ♀"
                                VoiceGender.MALE -> " ♂"
                                VoiceGender.AUTO -> ""
                            }
                            append(genderTag)
                            if (voice.isNetworkConnectionRequired) append(" (AI/Online)")
                            val quality = when {
                                voice.quality >= 400 -> " • High"
                                voice.quality >= 300 -> " • Medium"
                                else -> ""
                            }
                            append(quality)
                        },
                        isNetworkRequired = voice.isNetworkConnectionRequired,
                        gender = gender,
                        quality = voice.quality
                    )
                }?.sortedWith(compareByDescending<VoiceInfo> { it.isNetworkRequired }
                    .thenByDescending { it.quality }) ?: emptyList()
                
                _availableVoices.value = voices
            }
        }
    }

    fun getFilteredVoices(gender: VoiceGender): List<VoiceInfo> {
        return if (gender == VoiceGender.AUTO) {
            _availableVoices.value
        } else {
            _availableVoices.value.filter { it.gender == gender || it.gender == VoiceGender.AUTO }
        }
    }

    fun updateTtsSpeed(speed: Float) {
        viewModelScope.launch { preferences.updateTtsSpeed(speed) }
    }

    fun updateTtsPitch(pitch: Float) {
        viewModelScope.launch { preferences.updateTtsPitch(pitch) }
    }

    fun updateTtsVoice(voice: String) {
        viewModelScope.launch { preferences.updateTtsVoice(voice) }
    }

    fun updateVoiceGender(gender: VoiceGender) {
        viewModelScope.launch { preferences.updateVoiceGender(gender) }
    }

    fun updateBackgroundMusic(option: BackgroundMusicOption) {
        viewModelScope.launch { preferences.updateBackgroundMusic(option) }
    }

    fun updateBackgroundMusicVolume(volume: Float) {
        viewModelScope.launch { preferences.updateBackgroundMusicVolume(volume) }
    }

    fun updateCustomMusicUri(uri: String) {
        viewModelScope.launch { preferences.updateCustomMusicUri(uri) }
    }

    fun addCustomMusic(name: String, uri: String) {
        viewModelScope.launch {
            val current = settings.value.customMusicList.toMutableList()
            current.add(CustomMusicEntry(name = name, uri = uri))
            preferences.updateCustomMusicList(current)
        }
    }

    fun removeCustomMusic(index: Int) {
        viewModelScope.launch {
            val current = settings.value.customMusicList.toMutableList()
            if (index in current.indices) {
                current.removeAt(index)
                preferences.updateCustomMusicList(current)
                if (settings.value.selectedCustomMusicIndex >= current.size) {
                    preferences.updateSelectedCustomMusic((current.size - 1).coerceAtLeast(0))
                }
            }
        }
    }

    fun selectCustomMusic(index: Int) {
        viewModelScope.launch {
            preferences.updateSelectedCustomMusic(index)
            preferences.updateBackgroundMusic(BackgroundMusicOption.CUSTOM)
        }
    }

    fun updatePauseBetween(seconds: Int) {
        viewModelScope.launch { preferences.updatePauseBetween(seconds) }
    }

    fun updateAutoPlay(enabled: Boolean) {
        viewModelScope.launch { preferences.updateAutoPlay(enabled) }
    }

    fun updateRepeatSession(enabled: Boolean) {
        viewModelScope.launch { preferences.updateRepeatSession(enabled) }
    }

    fun updateShowBreathing(enabled: Boolean) {
        viewModelScope.launch { preferences.updateShowBreathing(enabled) }
    }

    fun updateVisualEffect(effect: VisualEffect) {
        viewModelScope.launch { preferences.updateVisualEffect(effect) }
    }

    fun updateVisualEffectIntensity(intensity: Float) {
        viewModelScope.launch { preferences.updateVisualEffectIntensity(intensity) }
    }

    // ========== TEST FUNCTIONS ==========

    fun testVoice() {
        if (_testingVoice.value) {
            stopVoiceTest()
            return
        }
        _testingVoice.value = true
        val currentSettings = settings.value
        tts?.setSpeechRate(currentSettings.ttsSpeed)
        tts?.setPitch(currentSettings.ttsPitch)
        if (currentSettings.ttsVoice.isNotEmpty()) {
            tts?.voices?.find { it.name == currentSettings.ttsVoice }?.let { voice ->
                tts?.voice = voice
            }
        }
        val testPhrases = listOf(
            "You are worthy of love and happiness.",
            "Today is a beautiful day to be alive.",
            "I am grateful for this moment of peace."
        )
        val phrase = testPhrases.random()
        tts?.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, "test_voice")
        tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) { _testingVoice.value = false }
            @Deprecated("Deprecated")
            override fun onError(utteranceId: String?) { _testingVoice.value = false }
        })
    }

    private fun stopVoiceTest() {
        tts?.stop()
        _testingVoice.value = false
    }

    fun testMusic() {
        if (_testingMusic.value) {
            stopMusicTest()
            return
        }
        _testingMusic.value = true
        val currentSettings = settings.value

        if (currentSettings.backgroundMusic == BackgroundMusicOption.CUSTOM && currentSettings.customMusicUri.isNotEmpty()) {
            try {
                testMusicPlayer = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(currentSettings.customMusicUri))
                    val vol = currentSettings.backgroundMusicVolume
                    setVolume(vol, vol)
                    prepare()
                    start()
                    setOnCompletionListener {
                        _testingMusic.value = false
                        it.release()
                        testMusicPlayer = null
                    }
                }
                // Auto-stop after 10 seconds
                testMusicJob = viewModelScope.launch {
                    delay(10_000)
                    stopMusicTest()
                }
                return
            } catch (e: Exception) {
                // Fall through to ambient
            }
        }

        // Generate ambient for 8 seconds
        testMusicJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val sampleRate = 44100
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT
                )
                val track = AudioTrack.Builder()
                    .setAudioAttributes(AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                    .setAudioFormat(AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                    .setBufferSizeInBytes(bufferSize * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM).build()

                testAmbientTrack = track
                track.play()
                val buffer = ShortArray(bufferSize / 2)
                val volume = currentSettings.backgroundMusicVolume.toDouble()
                var phase = 0.0
                val frequencies = when (currentSettings.backgroundMusic) {
                    BackgroundMusicOption.GENTLE_WAVES -> doubleArrayOf(174.0, 261.6, 329.6)
                    BackgroundMusicOption.DEEP_CALM -> doubleArrayOf(63.0, 126.0, 189.0)
                    BackgroundMusicOption.NATURE_BREEZE -> doubleArrayOf(200.0, 400.0, 600.0, 800.0)
                    BackgroundMusicOption.MEDITATION_BELLS -> doubleArrayOf(528.0, 639.0, 741.0)
                    BackgroundMusicOption.OCEAN_DRIFT -> doubleArrayOf(110.0, 220.0, 165.0)
                    else -> doubleArrayOf(174.0, 261.6, 329.6)
                }
                val totalSamples = sampleRate * 8 // 8 seconds
                var written = 0
                while (written < totalSamples && _testingMusic.value) {
                    for (i in buffer.indices) {
                        var sample = 0.0
                        for (freq in frequencies) {
                            val mod = 1.0 + 0.05 * Math.sin(phase * 0.00005 * freq)
                            sample += Math.sin(2.0 * Math.PI * freq * mod * phase / sampleRate) / frequencies.size
                        }
                        buffer[i] = (sample * volume * Short.MAX_VALUE).toInt()
                            .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                        phase++
                        written++
                    }
                    track.write(buffer, 0, buffer.size)
                }
                track.stop()
                track.release()
                testAmbientTrack = null
                _testingMusic.value = false
            } catch (e: Exception) {
                _testingMusic.value = false
            }
        }
    }

    private fun stopMusicTest() {
        testMusicJob?.cancel()
        testMusicPlayer?.apply {
            try { if (isPlaying) stop() } catch (_: Exception) {}
            release()
        }
        testMusicPlayer = null
        testAmbientTrack?.apply {
            try { stop(); release() } catch (_: Exception) {}
        }
        testAmbientTrack = null
        _testingMusic.value = false
    }

    fun testVisual() {
        if (_testingVisual.value) {
            _testingVisual.value = false
            return
        }
        _testingVisual.value = true
        viewModelScope.launch {
            delay(6_000)
            _testingVisual.value = false
        }
    }

    fun testAll() {
        if (_testingAll.value) {
            stopAllTests()
            return
        }
        _testingAll.value = true
        testVoice()
        testMusic()
        testVisual()
        viewModelScope.launch {
            delay(10_000)
            _testingAll.value = false
        }
    }

    private fun stopAllTests() {
        stopVoiceTest()
        stopMusicTest()
        _testingVisual.value = false
        _testingAll.value = false
    }

    override fun onCleared() {
        stopAllTests()
        tts?.shutdown()
        super.onCleared()
    }
}

data class VoiceInfo(
    val name: String,
    val displayName: String,
    val isNetworkRequired: Boolean,
    val gender: VoiceGender = VoiceGender.AUTO,
    val quality: Int = 0
)
