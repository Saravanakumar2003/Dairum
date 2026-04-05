package com.dairum.presentation.screens.editor

import android.content.Context
import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.service.LocationService
import com.dairum.data.service.VoiceRecorderService
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import com.dairum.widget.TodayWidgetProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private val Context.templateDataStore by preferencesDataStore(name = "templates")

@HiltViewModel
class EditorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val journalRepository: JournalRepository,
    private val categoryRepository: CategoryRepository,
    private val tagRepository: TagRepository,
    private val personRepository: PersonRepository,
    private val trackerRepository: TrackerRepository,
    private val mediaRepository: MediaRepository,
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository,
    private val locationService: LocationService,
    private val voiceRecorderService: VoiceRecorderService
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private var originalEntry: JournalEntry? = null
    private var isNewEntry: Boolean = true
    
    companion object {
        private val CUSTOM_TEMPLATES_KEY = stringPreferencesKey("custom_templates")
    }
    
    init {
        loadCategories()
        loadTags()
        loadPeople()
        loadTrackers()
        loadTemplates()
        
        // Observe voice recording state
        viewModelScope.launch {
            voiceRecorderService.isRecording.collect { isRecording ->
                _uiState.update { it.copy(isRecording = isRecording) }
            }
        }
        
        viewModelScope.launch {
            voiceRecorderService.recordingDuration.collect { duration ->
                _uiState.update { it.copy(recordingDuration = duration) }
            }
        }
        
        // Note: Auto-fetch for location is handled by EditorScreen's LaunchedEffect
        // to avoid race condition when editing existing entries
    }

    private fun autoFetchLocationAndWeather() {
        viewModelScope.launch {
            // Only auto-fetch for new entries (when originalEntry is null)
            // AND when location hasn't already been set
            if (originalEntry != null || _uiState.value.location != null) {
                android.util.Log.d("EditorViewModel", "Not auto-fetching - editing existing entry or location already set")
                return@launch
            }
            
            android.util.Log.d("EditorViewModel", "Auto-fetching location for new entry")
            _uiState.update { it.copy(isLoadingLocation = true) }
            
            val locationResult = locationService.getCurrentLocation()
            
            locationResult.fold(
                onSuccess = { location ->
                    android.util.Log.d("EditorViewModel", "Auto-fetch success: lat=${location.latitude}, lng=${location.longitude}, place=${location.placeName}")
                    _uiState.update { it.copy(location = location, isLoadingLocation = false) }
                    
                    // After getting location, fetch weather based on it
                    fetchWeatherForLocation(location.latitude, location.longitude)
                },
                onFailure = { error ->
                    android.util.Log.e("EditorViewModel", "Auto-fetch failed: ${error.message}")
                    _uiState.update { it.copy(isLoadingLocation = false) }
                }
            )
        }
    }

    private fun fetchWeatherForLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingWeather = true) }
            
            val result = weatherRepository.getCurrentWeather(latitude, longitude)
            
            result.fold(
                onSuccess = { weather ->
                    _uiState.update { it.copy(weather = weather, isLoadingWeather = false) }
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoadingWeather = false,
                            weatherError = error.message
                        ) 
                    }
                }
            )
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllFlow().collect { categories ->
                _uiState.update { it.copy(availableCategories = categories) }
                
                // Set default category if none selected
                if (_uiState.value.category == null) {
                    val defaultCategory = categories.find { it.isDefault }
                        ?: categories.firstOrNull()
                    _uiState.update { it.copy(category = defaultCategory) }
                }
            }
        }
    }

    private fun loadTags() {
        viewModelScope.launch {
            tagRepository.getAllFlow().collect { tags ->
                _uiState.update { it.copy(availableTags = tags) }
            }
        }
    }

    private fun loadPeople() {
        viewModelScope.launch {
            personRepository.getAllFlow().collect { people ->
                _uiState.update { it.copy(availablePeople = people) }
            }
        }
    }

    private fun loadTrackers() {
        viewModelScope.launch {
            trackerRepository.getActiveTrackersFlow().collect { trackers ->
                _uiState.update { it.copy(trackers = trackers) }
            }
        }
    }
    
    private fun loadTemplates() {
        viewModelScope.launch {
            context.templateDataStore.data.collect { preferences ->
                val templatesJson = preferences[CUSTOM_TEMPLATES_KEY] ?: "[]"
                val customTemplates = parseTemplatesFromJson(templatesJson)
                val allTemplates = EntryTemplate.BUILT_IN_TEMPLATES + customTemplates
                _uiState.update { it.copy(templates = allTemplates) }
            }
        }
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

    fun loadEntry(entryId: Long) {
        isNewEntry = false
        viewModelScope.launch {
            val entry = journalRepository.getEntry(entryId)
            entry?.let {
                originalEntry = it
                // Get the trackers that have values for this entry
                val selectedTrackerIds = it.trackerValues.keys
                val selectedTrackers = _uiState.value.trackers.filter { tracker -> 
                    selectedTrackerIds.contains(tracker.id) 
                }
                
                _uiState.update { state ->
                    state.copy(
                        title = it.title ?: "",
                        content = TextFieldValue(it.plainContent),
                        category = it.category,
                        mood = it.mood,
                        weather = it.weather,
                        location = it.location,
                        tags = it.tags,
                        people = it.people,
                        trackerValues = it.trackerValues,
                        selectedTrackers = selectedTrackers,
                        createdAt = it.createdAt,
                        isDraft = it.isDraft
                    )
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title, hasChanges = true) }
    }

    fun updateContent(content: TextFieldValue) {
        _uiState.update { it.copy(content = content, hasChanges = true) }
    }

    fun updateCategory(category: Category) {
        _uiState.update { it.copy(category = category, hasChanges = true) }
    }

    fun updateMood(mood: Mood?) {
        _uiState.update { it.copy(mood = mood, hasChanges = true) }
    }

    fun addTag(tag: Tag) {
        val currentTags = _uiState.value.tags.toMutableList()
        if (!currentTags.any { it.id == tag.id }) {
            currentTags.add(tag)
            _uiState.update { it.copy(tags = currentTags, hasChanges = true) }
        }
    }

    fun removeTag(tag: Tag) {
        val currentTags = _uiState.value.tags.toMutableList()
        currentTags.removeIf { it.id == tag.id }
        _uiState.update { it.copy(tags = currentTags, hasChanges = true) }
    }

    fun createAndAddTag(name: String) {
        viewModelScope.launch {
            val tag = tagRepository.createOrGet(name)
            addTag(tag)
        }
    }

    fun addPerson(person: Person) {
        val currentPeople = _uiState.value.people.toMutableList()
        if (!currentPeople.any { it.id == person.id }) {
            currentPeople.add(person)
            _uiState.update { it.copy(people = currentPeople, hasChanges = true) }
        }
    }

    fun createAndAddPerson(name: String) {
        viewModelScope.launch {
            // Create new person
            val personId = personRepository.create(
                Person(name = name)
            )
            // Get the created person and add it
            val person = personRepository.getById(personId)
            person?.let { addPerson(it) }
        }
    }

    fun removePerson(person: Person) {
        val currentPeople = _uiState.value.people.toMutableList()
        currentPeople.removeIf { it.id == person.id }
        _uiState.update { it.copy(people = currentPeople, hasChanges = true) }
    }

    fun addMedia(uri: Uri, type: MediaType) {
        val currentMedia = _uiState.value.media.toMutableList()
        currentMedia.add(MediaItem(uri = uri.toString(), type = type))
        _uiState.update { it.copy(media = currentMedia, hasChanges = true) }
    }

    fun removeMedia(media: MediaItem) {
        val currentMedia = _uiState.value.media.toMutableList()
        currentMedia.remove(media)
        _uiState.update { it.copy(media = currentMedia, hasChanges = true) }
    }

    fun updateTrackerValue(trackerId: Long, value: String) {
        val currentValues = _uiState.value.trackerValues.toMutableMap()
        currentValues[trackerId] = value
        _uiState.update { it.copy(trackerValues = currentValues, hasChanges = true) }
    }

    fun addTracker(tracker: CustomTracker) {
        val currentTrackers = _uiState.value.selectedTrackers.toMutableList()
        if (!currentTrackers.any { it.id == tracker.id }) {
            currentTrackers.add(tracker)
            _uiState.update { it.copy(selectedTrackers = currentTrackers, hasChanges = true) }
        }
    }

    fun removeTracker(tracker: CustomTracker) {
        val currentTrackers = _uiState.value.selectedTrackers.toMutableList()
        currentTrackers.removeIf { it.id == tracker.id }
        val currentValues = _uiState.value.trackerValues.toMutableMap()
        currentValues.remove(tracker.id)
        _uiState.update { 
            it.copy(
                selectedTrackers = currentTrackers, 
                trackerValues = currentValues,
                hasChanges = true
            ) 
        }
    }

    fun insertTimestamp() {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("h:mm a")
        val timestamp = "[${now.format(formatter)}] "
        
        val currentContent = _uiState.value.content
        val newContent = TextFieldValue(
            text = currentContent.text + timestamp,
            selection = currentContent.selection
        )
        _uiState.update { it.copy(content = newContent, hasChanges = true) }
    }

    fun fetchWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingWeather = true) }
            
            // Try to get current location first
            val locationResult = locationService.getCurrentLocation()
            
            val result = locationResult.fold(
                onSuccess = { location ->
                    weatherRepository.getCurrentWeather(location.latitude, location.longitude)
                },
                onFailure = {
                    // Fallback to a default location if GPS fails
                    weatherRepository.getCurrentWeatherByCity("San Francisco")
                }
            )
            
            result.fold(
                onSuccess = { weather ->
                    _uiState.update { it.copy(weather = weather, isLoadingWeather = false) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoadingWeather = false) }
                }
            )
        }
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingLocation = true) }
            
            val result = locationService.getCurrentLocation()
            
            result.fold(
                onSuccess = { location ->
                    _uiState.update { 
                        it.copy(
                            location = location, 
                            isLoadingLocation = false,
                            hasChanges = true
                        ) 
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoadingLocation = false) }
                }
            )
        }
    }

    fun setLocation(location: Location) {
        _uiState.update { it.copy(location = location, hasChanges = true) }
    }

    fun setLocationFromCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            android.util.Log.d("EditorViewModel", "setLocationFromCoordinates: lat=$latitude, lng=$longitude")
            _uiState.update { it.copy(isLoadingLocation = true) }
            
            val address = locationService.getAddressFromCoordinates(latitude, longitude)
            val placeName = address?.split(",")?.firstOrNull()?.trim()
            
            val location = Location(
                latitude = latitude,
                longitude = longitude,
                address = address,
                placeName = placeName
            )
            
            android.util.Log.d("EditorViewModel", "Location created: lat=${location.latitude}, lng=${location.longitude}, place=$placeName, address=$address")
            
            _uiState.update { 
                it.copy(
                    location = location, 
                    hasChanges = true, 
                    isLoadingLocation = false
                ) 
            }
        }
    }

    fun searchLocations(query: String) {
        viewModelScope.launch {
            if (query.length < 3) {
                _uiState.update { it.copy(locationSearchResults = emptyList()) }
                return@launch
            }
            
            val results = locationService.searchLocation(query)
            _uiState.update { it.copy(locationSearchResults = results) }
        }
    }

    fun clearLocationSearch() {
        _uiState.update { it.copy(locationSearchResults = emptyList()) }
    }

    fun toggleLocationPicker() {
        _uiState.update { it.copy(showLocationPicker = !it.showLocationPicker) }
    }

    fun removeLocation() {
        _uiState.update { it.copy(location = null, hasChanges = true) }
    }

    fun startVoiceRecording() {
        viewModelScope.launch {
            val result = voiceRecorderService.startRecording()
            result.onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun stopVoiceRecording() {
        viewModelScope.launch {
            val result = voiceRecorderService.stopRecording()
            result.fold(
                onSuccess = { recording ->
                    // Add voice note to media list
                    val currentMedia = _uiState.value.media.toMutableList()
                    currentMedia.add(MediaItem(uri = recording.filePath, type = MediaType.VOICE))
                    _uiState.update { it.copy(media = currentMedia, hasChanges = true) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
            )
        }
    }

    fun cancelVoiceRecording() {
        voiceRecorderService.cancelRecording()
    }

    fun updateRecordingAmplitude() {
        val amplitude = voiceRecorderService.getMaxAmplitude()
        voiceRecorderService.updateDuration()
    }

    // Text formatting
    fun toggleBold() {
        // TODO: Implement rich text formatting
    }

    fun toggleItalic() {
        // TODO: Implement rich text formatting
    }

    fun toggleUnderline() {
        // TODO: Implement rich text formatting
    }

    fun addBulletList() {
        val currentContent = _uiState.value.content
        val newContent = TextFieldValue(
            text = currentContent.text + "\n• ",
            selection = currentContent.selection
        )
        _uiState.update { it.copy(content = newContent) }
    }

    fun addNumberedList() {
        val currentContent = _uiState.value.content
        val newContent = TextFieldValue(
            text = currentContent.text + "\n1. ",
            selection = currentContent.selection
        )
        _uiState.update { it.copy(content = newContent) }
    }

    fun addHeading() {
        val currentContent = _uiState.value.content
        val newContent = TextFieldValue(
            text = currentContent.text + "\n## ",
            selection = currentContent.selection
        )
        _uiState.update { it.copy(content = newContent) }
    }

    fun saveAsDraft() {
        viewModelScope.launch {
            saveEntryInternal(isDraft = true)
        }
    }

    fun saveEntry() {
        viewModelScope.launch {
            saveEntryInternal(isDraft = false)
        }
    }
    
    // Template functions
    fun updateCreatedAt(dateTime: LocalDateTime) {
        _uiState.update { it.copy(createdAt = dateTime, hasChanges = true) }
    }
    
    fun showTemplateDialog() {
        _uiState.update { it.copy(showTemplateDialog = true) }
    }
    
    fun hideTemplateDialog() {
        _uiState.update { it.copy(showTemplateDialog = false) }
    }
    
    fun applyTemplate(template: EntryTemplate) {
        _uiState.update { state ->
            state.copy(
                title = template.defaultTitle ?: state.title,
                content = TextFieldValue(
                    text = template.promptText ?: ""
                ),
                category = template.category ?: state.category,
                showTemplateDialog = false,
                hasChanges = true
            )
        }
    }

    private suspend fun saveEntryInternal(isDraft: Boolean) {
        val state = _uiState.value
        
        android.util.Log.d("EditorViewModel", "Saving entry - location: ${state.location?.let { "lat=${it.latitude}, lng=${it.longitude}, place=${it.placeName}" } ?: "null"}")
        
        val entry = JournalEntry(
            id = originalEntry?.id ?: 0,
            title = state.title.takeIf { it.isNotBlank() },
            plainContent = state.content.text,
            category = state.category,
            createdAt = state.createdAt,
            updatedAt = LocalDateTime.now(),
            mood = state.mood,
            weather = state.weather,
            location = state.location,
            tags = state.tags,
            people = state.people,
            trackerValues = state.trackerValues,
            isDraft = isDraft,
            wordCount = state.content.text.split("\\s+".toRegex()).size
        )

        val entryId = if (originalEntry == null) {
            val newId = journalRepository.createEntry(entry)
            android.util.Log.d("EditorViewModel", "Created new entry with ID: $newId")
            newId
        } else {
            journalRepository.updateEntry(entry)
            android.util.Log.d("EditorViewModel", "Updated entry with ID: ${entry.id}")
            entry.id
        }
        
        // Save tags, people, and trackers
        journalRepository.setTagsForEntry(entryId, state.tags.map { it.id })
        journalRepository.setPeopleForEntry(entryId, state.people.map { it.id })
        journalRepository.setTrackerValues(entryId, state.trackerValues)
        
        // Save media files
        state.media.forEach { mediaItem ->
            try {
                val savedMedia = mediaRepository.saveMediaFile(mediaItem.uri, entryId, mediaItem.type)
                mediaRepository.addMedia(savedMedia)
            } catch (e: Exception) {
                // Log error but continue saving other media
            }
        }

        _uiState.update { it.copy(isSaved = true) }
        
        // Update Today widget with today's entry count
        updateTodayWidget()
    }
    
    private suspend fun updateTodayWidget() {
        try {
            val today = LocalDate.now()
            val startOfDay = today.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endOfDay = today.plusDays(1).atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
            val todayEntries = journalRepository.getEntriesInDateRange(startOfDay, endOfDay)
            TodayWidgetProvider.updateTodayEntries(context, todayEntries.size)
        } catch (e: Exception) {
            // Ignore widget update errors
        }
    }
}

data class EditorUiState(
    val title: String = "",
    val content: TextFieldValue = TextFieldValue(),
    val category: Category? = null,
    val mood: Mood? = null,
    val weather: Weather? = null,
    val location: Location? = null,
    val media: List<MediaItem> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val people: List<Person> = emptyList(),
    val trackerValues: Map<Long, String> = emptyMap(),
    val selectedTrackers: List<CustomTracker> = emptyList(), // Trackers user has added to this entry
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isDraft: Boolean = false,
    
    // Available selections
    val availableCategories: List<Category> = emptyList(),
    val availableTags: List<Tag> = emptyList(),
    val availablePeople: List<Person> = emptyList(),
    val trackers: List<CustomTracker> = emptyList(),
    val templates: List<EntryTemplate> = EntryTemplate.BUILT_IN_TEMPLATES,
    
    // UI state
    val isLoadingWeather: Boolean = false,
    val isLoadingLocation: Boolean = false,
    val showLocationPicker: Boolean = false,
    val locationSearchResults: List<Location> = emptyList(),
    val isRecording: Boolean = false,
    val recordingDuration: Long = 0,
    val hasChanges: Boolean = false,
    val isSaved: Boolean = false,
    val showTemplateDialog: Boolean = false,
    val error: String? = null,
    val weatherError: String? = null
) {
    val canSave: Boolean
        get() = content.text.isNotBlank() || title.isNotBlank()
}

data class MediaItem(
    val uri: String,
    val type: MediaType
)
