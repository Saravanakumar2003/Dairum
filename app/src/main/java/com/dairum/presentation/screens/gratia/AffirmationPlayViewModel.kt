package com.dairum.presentation.screens.gratia

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.preferences.BackgroundMusicOption
import com.dairum.data.preferences.GratiaPreferences
import com.dairum.data.preferences.GratiaSettings
import com.dairum.data.preferences.VisualEffect
import com.dairum.domain.model.Affirmation
import com.dairum.domain.model.AffirmationFolder
import com.dairum.domain.repository.GratiaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class PlaybackState(
    val folder: AffirmationFolder? = null,
    val affirmations: List<Affirmation> = emptyList(),
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false,
    val isFinished: Boolean = false,
    val useNaturalVoice: Boolean = false,
    val backgroundMusicEnabled: Boolean = true,
    val showBreathingAnimation: Boolean = true,
    val visualEffect: VisualEffect = VisualEffect.NONE,
    val visualEffectIntensity: Float = 0.5f
) {
    val currentAffirmation: Affirmation? get() = affirmations.getOrNull(currentIndex)
    val progress: Float get() = if (affirmations.isEmpty()) 0f 
        else (currentIndex + 1).toFloat() / affirmations.size
}

@HiltViewModel
class AffirmationPlayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val repository: GratiaRepository,
    private val gratiaPreferences: GratiaPreferences
) : ViewModel() {

    private val folderId: Long = savedStateHandle.get<Long>("folderId") ?: 0L

    private val _state = MutableStateFlow(PlaybackState())
    val state: StateFlow<PlaybackState> = _state.asStateFlow()

    private var currentSettings: GratiaSettings = GratiaSettings()
    private var tts: TextToSpeech? = null
    private var mediaPlayer: MediaPlayer? = null
    private var ambientPlayer: android.media.AudioTrack? = null
    private var ttsReady = false

    init {
        initTts()
        loadAffirmations()
        collectSettings()
    }

    private fun collectSettings() {
        viewModelScope.launch {
            gratiaPreferences.settings.collect { settings ->
                currentSettings = settings
                _state.update { 
                    it.copy(
                        backgroundMusicEnabled = settings.backgroundMusic != BackgroundMusicOption.NONE,
                        showBreathingAnimation = settings.showBreathingAnimation,
                        visualEffect = settings.visualEffect,
                        visualEffectIntensity = settings.visualEffectIntensity
                    ) 
                }
                // Apply TTS settings
                tts?.setSpeechRate(settings.ttsSpeed)
                tts?.setPitch(settings.ttsPitch)
                if (settings.ttsVoice.isNotEmpty()) {
                    tts?.voices?.find { it.name == settings.ttsVoice }?.let { voice ->
                        tts?.voice = voice
                    }
                }
            }
        }
    }

    private fun initTts() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(currentSettings.ttsSpeed)
                tts?.setPitch(currentSettings.ttsPitch)
                if (currentSettings.ttsVoice.isNotEmpty()) {
                    tts?.voices?.find { it.name == currentSettings.ttsVoice }?.let { voice ->
                        tts?.voice = voice
                    }
                }
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        viewModelScope.launch {
                            delay(currentSettings.pauseBetweenAffirmations * 1000L)
                            nextAffirmation()
                        }
                    }
                    @Deprecated("Deprecated")
                    override fun onError(utteranceId: String?) {}
                })
                ttsReady = true
            }
        }
    }

    private fun loadAffirmations() {
        viewModelScope.launch {
            val folder = repository.getAffirmationFolder(folderId)
            val affirmations = repository.getAffirmationsByFolderSync(folderId)
            _state.value = PlaybackState(folder = folder, affirmations = affirmations)
            // Auto-play if setting enabled
            if (currentSettings.autoPlayOnOpen && affirmations.isNotEmpty()) {
                delay(500) // Brief delay for UI to settle
                play()
            }
        }
    }

    fun toggleNaturalVoice(enabled: Boolean) {
        _state.update { it.copy(useNaturalVoice = enabled) }
        // Set network TTS engine if available
        if (enabled) {
            tts?.let { engine ->
                val engines = engine.engines
                val networkEngine = engines.firstOrNull { 
                    it.name.contains("google", ignoreCase = true) && it.name != engine.defaultEngine 
                }
                networkEngine?.let { /* Engine switching requires TTS reinit */ }
            }
        }
    }

    fun play() {
        _state.update { it.copy(isPlaying = true, isFinished = false) }
        startBackgroundAudio()
        speakCurrent()
    }

    fun toggleBackgroundMusic(enabled: Boolean) {
        _state.update { it.copy(backgroundMusicEnabled = enabled) }
        if (enabled && _state.value.isPlaying) {
            startBackgroundAudio()
        } else if (!enabled) {
            stopBackgroundAudio()
        }
    }

    fun pause() {
        _state.update { it.copy(isPlaying = false) }
        tts?.stop()
        mediaPlayer?.pause()
    }

    fun resume() {
        _state.update { it.copy(isPlaying = true) }
        mediaPlayer?.start()
        speakCurrent()
    }

    fun skipNext() {
        tts?.stop()
        val current = _state.value
        if (current.currentIndex < current.affirmations.size - 1) {
            _state.update { it.copy(currentIndex = it.currentIndex + 1) }
            if (current.isPlaying) speakCurrent()
        } else {
            _state.update { it.copy(isPlaying = false, isFinished = true) }
            stopBackgroundAudio()
        }
    }

    fun skipPrevious() {
        tts?.stop()
        val current = _state.value
        if (current.currentIndex > 0) {
            _state.update { it.copy(currentIndex = it.currentIndex - 1) }
            if (current.isPlaying) speakCurrent()
        }
    }

    private fun nextAffirmation() {
        val current = _state.value
        if (current.currentIndex < current.affirmations.size - 1) {
            _state.update { it.copy(currentIndex = it.currentIndex + 1) }
            if (current.isPlaying) speakCurrent()
        } else if (currentSettings.repeatSession) {
            _state.update { it.copy(currentIndex = 0) }
            if (current.isPlaying) speakCurrent()
        } else {
            _state.update { it.copy(isPlaying = false, isFinished = true) }
            stopBackgroundAudio()
        }
    }

    private fun speakCurrent() {
        val affirmation = _state.value.currentAffirmation ?: return
        if (!ttsReady) return

        if (affirmation.audioUri != null) {
            // Play recorded audio
            try {
                mediaPlayer?.let { if (it.isPlaying) it.stop() }
                val audioPlayer = MediaPlayer().apply {
                    setDataSource(context, android.net.Uri.parse(affirmation.audioUri))
                    prepare()
                    setOnCompletionListener {
                        viewModelScope.launch {
                            delay(currentSettings.pauseBetweenAffirmations * 1000L)
                            nextAffirmation()
                        }
                        it.release()
                    }
                    start()
                }
            } catch (e: Exception) {
                // Fallback to TTS
                speakWithTts(affirmation.text)
            }
        } else {
            speakWithTts(affirmation.text)
        }
    }

    private fun speakWithTts(text: String) {
        val params = android.os.Bundle()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "affirmation_${_state.value.currentIndex}")
    }

    private fun startBackgroundAudio() {
        if (!_state.value.backgroundMusicEnabled) return
        if (currentSettings.backgroundMusic == BackgroundMusicOption.NONE) return
        
        // If folder has custom audio and settings allow, use it
        if (currentSettings.backgroundMusic == BackgroundMusicOption.CUSTOM) {
            val uri = currentSettings.activeCustomMusicUri.ifEmpty { 
                _state.value.folder?.backgroundAudioUri 
            } ?: return
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
            } catch (e: Exception) {
                // Fall through to default ambient
            }
        }
        
        // Generated ambient background music
        startDefaultAmbient()
    }

    private fun startDefaultAmbient() {
        stopAmbientPlayer()
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val sampleRate = 44100
                val bufferSize = android.media.AudioTrack.getMinBufferSize(
                    sampleRate,
                    android.media.AudioFormat.CHANNEL_OUT_MONO,
                    android.media.AudioFormat.ENCODING_PCM_16BIT
                )
                val track = android.media.AudioTrack.Builder()
                    .setAudioAttributes(
                        android.media.AudioAttributes.Builder()
                            .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        android.media.AudioFormat.Builder()
                            .setEncoding(android.media.AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(android.media.AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize * 2)
                    .setTransferMode(android.media.AudioTrack.MODE_STREAM)
                    .build()
                
                ambientPlayer = track
                track.play()
                
                val buffer = ShortArray(bufferSize / 2)
                val volume = currentSettings.backgroundMusicVolume.toDouble()
                var phase = 0.0
                
                // Different frequency sets for different music options
                val frequencies = when (currentSettings.backgroundMusic) {
                    BackgroundMusicOption.GENTLE_WAVES -> doubleArrayOf(174.0, 261.6, 329.6)
                    BackgroundMusicOption.DEEP_CALM -> doubleArrayOf(63.0, 126.0, 189.0)
                    BackgroundMusicOption.NATURE_BREEZE -> doubleArrayOf(200.0, 400.0, 600.0, 800.0)
                    BackgroundMusicOption.MEDITATION_BELLS -> doubleArrayOf(528.0, 639.0, 741.0)
                    BackgroundMusicOption.OCEAN_DRIFT -> doubleArrayOf(110.0, 220.0, 165.0)
                    else -> doubleArrayOf(174.0, 261.6, 329.6)
                }
                
                while (_state.value.isPlaying && _state.value.backgroundMusicEnabled) {
                    for (i in buffer.indices) {
                        var sample = 0.0
                        for (freq in frequencies) {
                            // Add slight modulation for organic feel
                            val modulation = 1.0 + 0.05 * Math.sin(phase * 0.00005 * freq)
                            sample += Math.sin(2.0 * Math.PI * freq * modulation * phase / sampleRate) / frequencies.size
                        }
                        // Ocean drift gets rhythmic envelope
                        val envelope = if (currentSettings.backgroundMusic == BackgroundMusicOption.OCEAN_DRIFT) {
                            0.5 + 0.5 * Math.sin(2.0 * Math.PI * 0.1 * phase / sampleRate)
                        } else 1.0
                        buffer[i] = (sample * volume * envelope * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                        phase++
                    }
                    track.write(buffer, 0, buffer.size)
                }
                
                track.stop()
                track.release()
                ambientPlayer = null
            } catch (e: Exception) {
                // Ignore audio errors
            }
        }
    }

    private fun stopAmbientPlayer() {
        ambientPlayer?.let {
            try {
                it.stop()
                it.release()
            } catch (e: Exception) { /* ignore */ }
        }
        ambientPlayer = null
    }

    private fun stopBackgroundAudio() {
        mediaPlayer?.apply {
            try { if (isPlaying) stop() } catch (e: Exception) {}
            release()
        }
        mediaPlayer = null
        stopAmbientPlayer()
    }

    fun getJournalPrompt(): String {
        val folder = _state.value.folder
        val affirmations = _state.value.affirmations
        return buildString {
            appendLine("🧘 Affirmation Session: ${folder?.name ?: "Untitled"}")
            appendLine()
            affirmations.forEach { append("• ${it.text}\n") }
            appendLine()
            appendLine("How did this session make you feel?")
        }
    }

    override fun onCleared() {
        tts?.shutdown()
        stopBackgroundAudio()
        super.onCleared()
    }
}
