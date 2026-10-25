package com.dairum.presentation.screens.gratia

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.preferences.BackgroundMusicOption
import com.dairum.data.preferences.GratiaPreferences
import com.dairum.data.preferences.GratiaSettings
import com.dairum.data.preferences.VisualEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**
 * Predefined breathing techniques
 */
enum class BreathingTechnique(
    val label: String,
    val description: String,
    val emoji: String,
    val inhaleSeconds: Int,
    val holdAfterInhaleSeconds: Int,
    val exhaleSeconds: Int,
    val holdAfterExhaleSeconds: Int,
    val cycles: Int
) {
    BOX_BREATHING(
        "Box Breathing", "Navy SEAL technique for calm focus", "📦",
        inhaleSeconds = 4, holdAfterInhaleSeconds = 4, exhaleSeconds = 4, holdAfterExhaleSeconds = 4, cycles = 4
    ),
    FOUR_SEVEN_EIGHT(
        "4-7-8 Relaxing", "Dr. Weil's natural tranquilizer", "😌",
        inhaleSeconds = 4, holdAfterInhaleSeconds = 7, exhaleSeconds = 8, holdAfterExhaleSeconds = 0, cycles = 4
    ),
    DEEP_CALM(
        "Deep Calm", "Simple deep breathing for relaxation", "🧘",
        inhaleSeconds = 5, holdAfterInhaleSeconds = 2, exhaleSeconds = 5, holdAfterExhaleSeconds = 2, cycles = 6
    ),
    ENERGIZING(
        "Energizing Breath", "Quick breaths to boost energy", "⚡",
        inhaleSeconds = 2, holdAfterInhaleSeconds = 0, exhaleSeconds = 2, holdAfterExhaleSeconds = 0, cycles = 10
    ),
    COHERENT(
        "Coherent Breathing", "5.5 breaths/min for heart coherence", "💚",
        inhaleSeconds = 5, holdAfterInhaleSeconds = 0, exhaleSeconds = 5, holdAfterExhaleSeconds = 0, cycles = 6
    ),
    CUSTOM(
        "Custom", "Design your own pattern", "🎨",
        inhaleSeconds = 4, holdAfterInhaleSeconds = 4, exhaleSeconds = 4, holdAfterExhaleSeconds = 4, cycles = 4
    )
}

enum class BreathPhase { IDLE, BREATHE_IN, HOLD_IN, BREATHE_OUT, HOLD_OUT, FINISHED }

data class BreathingState(
    val technique: BreathingTechnique = BreathingTechnique.BOX_BREATHING,
    val isRunning: Boolean = false,
    val phase: BreathPhase = BreathPhase.IDLE,
    val phaseTimeLeft: Int = 0, // seconds remaining in current phase
    val currentCycle: Int = 0,
    val totalCycles: Int = 4,
    // Custom overrides
    val customInhale: Int = 4,
    val customHoldIn: Int = 4,
    val customExhale: Int = 4,
    val customHoldOut: Int = 4,
    val customCycles: Int = 4,
    // Voice guidance
    val voiceGuidance: Boolean = true,
    val backgroundMusicEnabled: Boolean = true
) {
    val phaseLabel: String get() = when (phase) {
        BreathPhase.IDLE -> "Ready"
        BreathPhase.BREATHE_IN -> "Breathe In"
        BreathPhase.HOLD_IN -> "Hold"
        BreathPhase.BREATHE_OUT -> "Breathe Out"
        BreathPhase.HOLD_OUT -> "Hold"
        BreathPhase.FINISHED -> "Complete"
    }

    val progress: Float get() {
        if (totalCycles == 0) return 0f
        val cycleDuration = getInhale() + getHoldIn() + getExhale() + getHoldOut()
        val totalDuration = cycleDuration * totalCycles
        if (totalDuration == 0) return 0f
        val elapsed = (currentCycle * cycleDuration) + (cycleDuration - getRemainingInCycle())
        return elapsed.toFloat() / totalDuration
    }

    fun getInhale(): Int = if (technique == BreathingTechnique.CUSTOM) customInhale else technique.inhaleSeconds
    fun getHoldIn(): Int = if (technique == BreathingTechnique.CUSTOM) customHoldIn else technique.holdAfterInhaleSeconds
    fun getExhale(): Int = if (technique == BreathingTechnique.CUSTOM) customExhale else technique.exhaleSeconds
    fun getHoldOut(): Int = if (technique == BreathingTechnique.CUSTOM) customHoldOut else technique.holdAfterExhaleSeconds
    fun getCycles(): Int = if (technique == BreathingTechnique.CUSTOM) customCycles else technique.cycles

    private fun getRemainingInCycle(): Int {
        val phaseMax = when (phase) {
            BreathPhase.BREATHE_IN -> getInhale()
            BreathPhase.HOLD_IN -> getHoldIn()
            BreathPhase.BREATHE_OUT -> getExhale()
            BreathPhase.HOLD_OUT -> getHoldOut()
            else -> 0
        }
        return phaseTimeLeft + when (phase) {
            BreathPhase.BREATHE_IN -> getHoldIn() + getExhale() + getHoldOut()
            BreathPhase.HOLD_IN -> getExhale() + getHoldOut()
            BreathPhase.BREATHE_OUT -> getHoldOut()
            else -> 0
        }
    }
}

@HiltViewModel
class BreathingExerciseViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: GratiaPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(BreathingState())
    val state: StateFlow<BreathingState> = _state.asStateFlow()

    private val _visualEffect = MutableStateFlow(VisualEffect.NONE)
    val visualEffect: StateFlow<VisualEffect> = _visualEffect.asStateFlow()

    private val _visualEffectIntensity = MutableStateFlow(0.5f)
    val visualEffectIntensity: StateFlow<Float> = _visualEffectIntensity.asStateFlow()

    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private var breathingJob: Job? = null
    private var ambientTrack: AudioTrack? = null
    private var ambientJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null
    private var currentSettings: GratiaSettings = GratiaSettings()

    init {
        initTts()
        collectSettings()
    }

    private fun collectSettings() {
        viewModelScope.launch {
            preferences.settings.collect { settings ->
                currentSettings = settings
                _visualEffect.value = settings.visualEffect
                _visualEffectIntensity.value = settings.visualEffectIntensity
                // Apply TTS settings dynamically
                applyTtsSettings(settings)
            }
        }
    }

    private fun initTts() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                applyTtsSettings(currentSettings)
                ttsReady = true
            }
        }
    }

    private fun applyTtsSettings(settings: GratiaSettings) {
        tts?.setSpeechRate(settings.ttsSpeed.coerceIn(0.25f, 2.0f))
        tts?.setPitch(settings.ttsPitch.coerceIn(0.5f, 2.0f))
        if (settings.ttsVoice.isNotEmpty()) {
            tts?.voices?.find { it.name == settings.ttsVoice }?.let { voice ->
                tts?.voice = voice
            }
        }
    }

    fun selectTechnique(technique: BreathingTechnique) {
        _state.update { it.copy(technique = technique, phase = BreathPhase.IDLE, isRunning = false) }
    }

    fun updateCustomInhale(seconds: Int) { _state.update { it.copy(customInhale = seconds) } }
    fun updateCustomHoldIn(seconds: Int) { _state.update { it.copy(customHoldIn = seconds) } }
    fun updateCustomExhale(seconds: Int) { _state.update { it.copy(customExhale = seconds) } }
    fun updateCustomHoldOut(seconds: Int) { _state.update { it.copy(customHoldOut = seconds) } }
    fun updateCustomCycles(cycles: Int) { _state.update { it.copy(customCycles = cycles) } }
    fun toggleVoiceGuidance(enabled: Boolean) { _state.update { it.copy(voiceGuidance = enabled) } }
    fun toggleBackgroundMusic(enabled: Boolean) {
        _state.update { it.copy(backgroundMusicEnabled = enabled) }
        if (!enabled) stopAmbient() else if (_state.value.isRunning) startAmbient()
    }

    fun start() {
        breathingJob?.cancel()
        val s = _state.value
        _state.update {
            it.copy(
                isRunning = true,
                phase = BreathPhase.BREATHE_IN,
                currentCycle = 0,
                totalCycles = s.getCycles(),
                phaseTimeLeft = s.getInhale()
            )
        }
        if (s.backgroundMusicEnabled) startAmbient()
        breathingJob = viewModelScope.launch {
            // Countdown before start
            speak("Let's begin. Get comfortable.")
            delay(2500)

            val cycles = s.getCycles()
            for (cycle in 0 until cycles) {
                _state.update { it.copy(currentCycle = cycle) }

                // BREATHE IN
                val inhale = s.getInhale()
                if (inhale > 0) {
                    _state.update { it.copy(phase = BreathPhase.BREATHE_IN, phaseTimeLeft = inhale) }
                    speak("Breathe in")
                    for (t in inhale downTo 1) {
                        _state.update { it.copy(phaseTimeLeft = t) }
                        delay(1000)
                        if (!_state.value.isRunning) return@launch
                    }
                }

                // HOLD AFTER INHALE
                val holdIn = s.getHoldIn()
                if (holdIn > 0) {
                    _state.update { it.copy(phase = BreathPhase.HOLD_IN, phaseTimeLeft = holdIn) }
                    speak("Hold")
                    for (t in holdIn downTo 1) {
                        _state.update { it.copy(phaseTimeLeft = t) }
                        delay(1000)
                        if (!_state.value.isRunning) return@launch
                    }
                }

                // BREATHE OUT
                val exhale = s.getExhale()
                if (exhale > 0) {
                    _state.update { it.copy(phase = BreathPhase.BREATHE_OUT, phaseTimeLeft = exhale) }
                    speak("Breathe out")
                    for (t in exhale downTo 1) {
                        _state.update { it.copy(phaseTimeLeft = t) }
                        delay(1000)
                        if (!_state.value.isRunning) return@launch
                    }
                }

                // HOLD AFTER EXHALE
                val holdOut = s.getHoldOut()
                if (holdOut > 0) {
                    _state.update { it.copy(phase = BreathPhase.HOLD_OUT, phaseTimeLeft = holdOut) }
                    speak("Hold")
                    for (t in holdOut downTo 1) {
                        _state.update { it.copy(phaseTimeLeft = t) }
                        delay(1000)
                        if (!_state.value.isRunning) return@launch
                    }
                }
            }

            // Finished
            speak("Well done. Take a moment to notice how you feel.")
            _state.update { it.copy(phase = BreathPhase.FINISHED, isRunning = false, phaseTimeLeft = 0) }
            stopAmbient()
        }
    }

    fun stop() {
        breathingJob?.cancel()
        tts?.stop()
        stopAmbient()
        _state.update { it.copy(isRunning = false, phase = BreathPhase.IDLE, phaseTimeLeft = 0, currentCycle = 0) }
    }

    private fun speak(text: String) {
        if (!ttsReady || !_state.value.voiceGuidance) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "breathing_${System.currentTimeMillis()}")
    }

    private fun startAmbient() {
        stopAmbient()
        if (currentSettings.backgroundMusic == BackgroundMusicOption.NONE) return
        
        // Custom music via MediaPlayer
        if (currentSettings.backgroundMusic == BackgroundMusicOption.CUSTOM) {
            val uri = currentSettings.activeCustomMusicUri.ifEmpty { null } ?: return
            try {
                val vol = currentSettings.backgroundMusicVolume
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(context, android.net.Uri.parse(uri))
                    isLooping = true
                    setVolume(vol, vol)
                    prepare()
                    start()
                }
                return
            } catch (_: Exception) {
                // Fall through to generated ambient
            }
        }
        
        ambientJob = viewModelScope.launch(Dispatchers.IO) {
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

                ambientTrack = track
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

                while (_state.value.isRunning && _state.value.backgroundMusicEnabled) {
                    for (i in buffer.indices) {
                        var sample = 0.0
                        for (freq in frequencies) {
                            val mod = 1.0 + 0.05 * Math.sin(phase * 0.00005 * freq)
                            sample += Math.sin(2.0 * Math.PI * freq * mod * phase / sampleRate) / frequencies.size
                        }
                        val envelope = if (currentSettings.backgroundMusic == BackgroundMusicOption.OCEAN_DRIFT) {
                            0.5 + 0.5 * Math.sin(2.0 * Math.PI * 0.1 * phase / sampleRate)
                        } else 1.0
                        buffer[i] = (sample * volume * envelope * Short.MAX_VALUE).toInt()
                            .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                        phase++
                    }
                    track.write(buffer, 0, buffer.size)
                }
                track.stop()
                track.release()
                ambientTrack = null
            } catch (_: Exception) {}
        }
    }

    private fun stopAmbient() {
        ambientJob?.cancel()
        ambientTrack?.apply {
            try { stop(); release() } catch (_: Exception) {}
        }
        ambientTrack = null
        mediaPlayer?.apply {
            try { stop(); release() } catch (_: Exception) {}
        }
        mediaPlayer = null
    }

    override fun onCleared() {
        stop()
        tts?.shutdown()
        super.onCleared()
    }
}
