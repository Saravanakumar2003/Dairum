package com.dairum.presentation.screens.entry

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.preferences.GratiaPreferences
import com.dairum.data.preferences.GratiaSettings
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EntryViewViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val journalRepository: JournalRepository,
    private val mediaRepository: MediaRepository,
    private val trackerRepository: TrackerRepository,
    private val gratiaPreferences: GratiaPreferences,
    private val locationNicknameRepository: LocationNicknameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EntryViewUiState())
    val uiState: StateFlow<EntryViewUiState> = _uiState.asStateFlow()

    private val _isReading = MutableStateFlow(false)
    val isReading: StateFlow<Boolean> = _isReading.asStateFlow()

    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private var currentSettings: GratiaSettings = GratiaSettings()

    init {
        viewModelScope.launch {
            gratiaPreferences.settings.collect { currentSettings = it }
        }
        viewModelScope.launch {
            locationNicknameRepository.getAllFlow().collect { nicknames ->
                _uiState.update {
                    it.copy(locationNicknames = nicknames.associate { n -> n.locationKey to n.nickname })
                }
            }
        }
    }

    private fun initTtsIfNeeded() {
        if (tts != null) return
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(currentSettings.ttsSpeed)
                tts?.setPitch(currentSettings.ttsPitch)
                if (currentSettings.ttsVoice.isNotEmpty()) {
                    tts?.voices?.find { it.name == currentSettings.ttsVoice }?.let { tts?.voice = it }
                }
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) { _isReading.value = false }
                    @Deprecated("Deprecated")
                    override fun onError(utteranceId: String?) { _isReading.value = false }
                })
                ttsReady = true
                if (_isReading.value) startReading() // was pending
            }
        }
    }

    fun loadEntry(entryId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val entry = journalRepository.getEntry(entryId)
                val media = mediaRepository.getMediaForEntry(entryId)
                val trackers = trackerRepository.getAllTrackers()
                _uiState.update { it.copy(entry = entry, media = media, trackers = trackers, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun toggleReadAloud() {
        if (_isReading.value) {
            tts?.stop()
            _isReading.value = false
        } else {
            _isReading.value = true
            initTtsIfNeeded()
            if (ttsReady) startReading()
        }
    }

    private fun startReading() {
        val entry = _uiState.value.entry ?: return
        val text = buildString {
            entry.title?.let { append(it); append(". ") }
            append(entry.plainContent)
        }
        if (text.isBlank()) { _isReading.value = false; return }
        tts?.setSpeechRate(currentSettings.ttsSpeed)
        tts?.setPitch(currentSettings.ttsPitch)
        if (currentSettings.ttsVoice.isNotEmpty()) {
            tts?.voices?.find { it.name == currentSettings.ttsVoice }?.let { tts?.voice = it }
        }
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle(), "journal_read")
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val entry = _uiState.value.entry ?: return@launch
            val newFavorite = !entry.isFavorite
            journalRepository.toggleFavorite(entry.id, newFavorite)
            _uiState.update { it.copy(entry = entry.copy(isFavorite = newFavorite)) }
        }
    }

    fun deleteEntry() {
        viewModelScope.launch {
            val entry = _uiState.value.entry ?: return@launch
            journalRepository.softDelete(entry.id)
        }
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        super.onCleared()
    }
}

data class EntryViewUiState(
    val entry: JournalEntry? = null,
    val media: List<Media> = emptyList(),
    val trackers: List<CustomTracker> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val locationNicknames: Map<String, String> = emptyMap()
) {
    fun displayNameFor(location: Location): String {
        return locationNicknames[location.nicknameKey()] ?: location.placeName ?: location.address ?: "Location"
    }
}
