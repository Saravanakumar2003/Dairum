package com.dairum.presentation.screens.editor

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.data.service.LocationService
import com.dairum.data.service.VoiceRecorderService
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import com.dairum.presentation.theme.EntryBackgroundColor
import com.dairum.presentation.theme.EntryBackgroundColors
import com.dairum.widget.TodayWidgetProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private val voiceRecorderService: VoiceRecorderService,
    private val locationNicknameRepository: LocationNicknameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private var originalEntry: JournalEntry? = null
    private var isNewEntry: Boolean = true
    
    // Autosave related
    private var autosaveJob: Job? = null
    private var tempEntryId: Long? = null
    private val autosaveIntervalMs = 5000L // 5 seconds
    
    companion object {
        private val CUSTOM_TEMPLATES_KEY = stringPreferencesKey("custom_templates")
    }
    
    init {
        loadCategories()
        loadTags()
        loadPeople()
        loadTrackers()
        loadTemplates()
        loadLocationNicknames()
        
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
        
        // Start observing content changes for autosave
        viewModelScope.launch {
            uiState
                .map { it.content.text to it.title }
                .distinctUntilChanged()
                .collect { (content, title) ->
                    if (content.isNotBlank() || title.isNotBlank()) {
                        scheduleAutosave()
                    }
                }
        }
        
        // Note: Auto-fetch for location is handled by EditorScreen's LaunchedEffect
        // to avoid race condition when editing existing entries
    }

    private fun loadLocationNicknames() {
        viewModelScope.launch {
            locationNicknameRepository.getAllFlow().collect { nicknames ->
                _uiState.update {
                    it.copy(locationNicknames = nicknames.associate { n -> n.locationKey to n.nickname })
                }
            }
        }
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
                
                // Parse formatting JSON
                val formatting = if (it.formattingJson.isNotBlank()) {
                    try {
                        TextFormatting.fromJson(it.formattingJson)
                    } catch (e: Exception) {
                        TextFormatting()
                    }
                } else {
                    TextFormatting()
                }
                
                _uiState.update { state ->
                    state.copy(
                        title = it.title ?: "",
                        content = TextFieldValue(it.plainContent),
                        textFormatting = formatting,
                        category = it.category,
                        mood = it.mood,
                        weather = it.weather,
                        location = it.location,
                        additionalLocations = it.additionalLocations,
                        media = it.media.map { m -> MediaItem(uri = m.uri, type = m.type, inlineKey = m.inlineKey) },
                        tags = it.tags,
                        people = it.people,
                        trackerValues = it.trackerValues,
                        selectedTrackers = selectedTrackers,
                        createdAt = it.createdAt,
                        isDraft = it.isDraft,
                        backgroundColor = it.backgroundColor?.let { bgId -> 
                            EntryBackgroundColors.fromId(bgId, false) 
                        }
                    )
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title, hasChanges = true) }
        scheduleAutosave()
    }

    fun updateContent(content: TextFieldValue) {
        val oldContent = _uiState.value.content
        val oldText = oldContent.text
        val newText = content.text
        
        // Calculate position and delta for formatting adjustment
        val delta = newText.length - oldText.length
        var changePosition = 0
        
        if (delta != 0) {
            // Find where the change occurred
            val minLen = minOf(oldText.length, newText.length)
            while (changePosition < minLen && oldText[changePosition] == newText[changePosition]) {
                changePosition++
            }
        } else {
            changePosition = content.selection.start
        }
        
        // Adjust formatting spans
        val adjustedFormatting = _uiState.value.textFormatting.adjustForTextChange(changePosition, delta)
        
        val mentionTrigger = detectMentionTrigger(content)
        
        _uiState.update { 
            it.copy(
                content = content, 
                textFormatting = adjustedFormatting,
                hasChanges = true,
                mentionQuery = mentionTrigger?.query,
                mentionStartIndex = mentionTrigger?.start
            ) 
        }
        scheduleAutosave()
    }

    private data class MentionTrigger(val start: Int, val query: String)

    /**
     * Looks backward from the cursor for an unclosed "@" trigger (Twitter/Slack-style mention),
     * e.g. typing "@ali" returns MentionTrigger(start of '@', query="ali").
     */
    private fun detectMentionTrigger(content: TextFieldValue): MentionTrigger? {
        if (content.selection.start != content.selection.end) return null
        val cursor = content.selection.end
        val text = content.text
        var i = cursor - 1
        while (i >= 0) {
            val c = text[i]
            if (c == '@') {
                if (i > 0) {
                    val prev = text[i - 1]
                    if (!prev.isWhitespace()) return null // e.g. part of an email address
                }
                val query = text.substring(i + 1, cursor)
                if (query.contains('\n')) return null
                return MentionTrigger(start = i, query = query)
            }
            if (c.isWhitespace()) return null
            i--
        }
        return null
    }

    /**
     * Replaces the active "@query" with the chosen mention, styles it as a colored chip, and
     * links the referenced person/tag/location to this entry.
     */
    fun selectMentionCandidate(candidate: MentionCandidate) {
        val start = _uiState.value.mentionStartIndex ?: return
        val content = _uiState.value.content
        val cursor = content.selection.end
        if (cursor < start) return
        if (!candidate.isAvailable) return // Informational "not added" hint - not selectable
        
        val insertion = "@${candidate.displayName} "
        val newText = content.text.substring(0, start) + insertion + content.text.substring(cursor)
        val newCursor = start + insertion.length
        val newContent = TextFieldValue(newText, TextRange(newCursor))
        
        val delta = insertion.length - (cursor - start)
        var formatting = _uiState.value.textFormatting.adjustForTextChange(start, delta)
        val style = when (candidate.type) {
            MentionType.PERSON -> SpanStyle.MENTION_PERSON
            MentionType.LOCATION -> SpanStyle.MENTION_LOCATION
            MentionType.TAG -> SpanStyle.MENTION_TAG
            MentionType.EMOTION -> SpanStyle.MENTION_EMOTION
        }
        val mentionSpan = TextSpan(start, start + 1 + candidate.displayName.length, style)
        formatting = formatting.copy(spans = formatting.spans + mentionSpan)
        
        _uiState.update {
            it.copy(
                content = newContent,
                textFormatting = formatting,
                mentionQuery = null,
                mentionStartIndex = null,
                hasChanges = true
            )
        }
        scheduleAutosave()
        
        when (candidate.type) {
            MentionType.PERSON -> candidate.personId?.let { id ->
                _uiState.value.availablePeople.find { it.id == id }?.let { addPerson(it) }
            }
            MentionType.TAG -> candidate.tagId?.let { id ->
                _uiState.value.availableTags.find { it.id == id }?.let { addTag(it) }
            }
            MentionType.LOCATION -> Unit // Already part of this entry - nothing further to link
            MentionType.EMOTION -> Unit // Purely stylistic - doesn't overwrite the entry's single mood field
        }
    }

    fun dismissMentionPopup() {
        _uiState.update { it.copy(mentionQuery = null, mentionStartIndex = null) }
    }

    /** Creates a brand-new tag on the fly and inserts it as a mention ("Create tag '...'" option). */
    fun createMentionTagAndInsert(name: String) {
        viewModelScope.launch {
            val tag = tagRepository.createOrGet(name)
            selectMentionCandidate(MentionCandidate(MentionType.TAG, tag.name, tagId = tag.id))
        }
    }

    /** Creates a brand-new person on the fly and inserts it as a mention ("Create person '...'" option). */
    fun createMentionPersonAndInsert(name: String) {
        viewModelScope.launch {
            val personId = personRepository.create(Person(name = name))
            val person = personRepository.getById(personId)
            if (person != null) {
                selectMentionCandidate(MentionCandidate(MentionType.PERSON, person.name, personId = person.id))
            }
        }
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

    /** Inserts an image marker chip at the cursor so a photo appears in the flow of the text. */
    fun insertInlineImage(uri: Uri) {
        val key = java.util.UUID.randomUUID().toString().take(6)
        val marker = "[IMG:$key]"
        val currentContent = _uiState.value.content
        val insertPos = currentContent.selection.end.coerceIn(0, currentContent.text.length)
        val insertion = "\n$marker\n"
        val newText = currentContent.text.substring(0, insertPos) + insertion + currentContent.text.substring(insertPos)
        val newCursor = insertPos + insertion.length
        val newContent = TextFieldValue(newText, TextRange(newCursor))
        
        val markerStart = insertPos + 1 // Skip the leading newline
        val shiftedFormatting = _uiState.value.textFormatting.adjustForTextChange(insertPos, insertion.length)
        val newSpan = TextSpan(markerStart, markerStart + marker.length, SpanStyle.INLINE_IMAGE)
        val newFormatting = shiftedFormatting.copy(spans = shiftedFormatting.spans + newSpan)
        
        val mediaItem = MediaItem(uri = uri.toString(), type = MediaType.PHOTO, inlineKey = key)
        val currentMedia = _uiState.value.media + mediaItem
        
        _uiState.update {
            it.copy(
                content = newContent,
                textFormatting = newFormatting,
                media = currentMedia,
                hasChanges = true
            )
        }
        scheduleAutosave()
    }

    fun removeMedia(media: MediaItem) {
        val currentMedia = _uiState.value.media.toMutableList()
        currentMedia.remove(media)
        
        var content = _uiState.value.content
        var formatting = _uiState.value.textFormatting
        
        val inlineKey = media.inlineKey
        if (inlineKey != null) {
            val marker = "[IMG:$inlineKey]"
            val idx = content.text.indexOf(marker)
            if (idx >= 0) {
                val newText = content.text.removeRange(idx, idx + marker.length)
                val newCursor = idx.coerceAtMost(newText.length)
                content = TextFieldValue(newText, TextRange(newCursor))
                formatting = formatting.adjustForTextChange(idx, -marker.length)
                    .copy(spans = formatting.spans.filterNot { it.style == SpanStyle.INLINE_IMAGE && it.start == idx })
            }
        }
        
        _uiState.update {
            it.copy(media = currentMedia, content = content, textFormatting = formatting, hasChanges = true)
        }
        scheduleAutosave()
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
        val token = "[${now.format(formatter)}]"
        
        val currentContent = _uiState.value.content
        val insertPos = currentContent.text.length
        val newContent = TextFieldValue(
            text = currentContent.text + token + " ",
            selection = currentContent.selection
        )
        // Existing spans all end at or before insertPos, so no adjustment is needed - just append the chip span.
        val newSpan = TextSpan(insertPos, insertPos + token.length, SpanStyle.TIMESTAMP)
        val newFormatting = _uiState.value.textFormatting.copy(
            spans = _uiState.value.textFormatting.spans + newSpan
        )
        _uiState.update { it.copy(content = newContent, textFormatting = newFormatting, hasChanges = true) }
        scheduleAutosave()
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

    /** Adds an extra location beyond the primary one (feature: multiple locations per entry). */
    fun addLocation(location: Location) {
        val isSameAsPrimary = _uiState.value.location?.let {
            it.latitude == location.latitude && it.longitude == location.longitude
        } ?: false
        val list = _uiState.value.additionalLocations
        val alreadyPresent = list.any { it.latitude == location.latitude && it.longitude == location.longitude }
        if (!isSameAsPrimary && !alreadyPresent) {
            _uiState.update { it.copy(additionalLocations = list + location, hasChanges = true) }
            scheduleAutosave()
        }
    }

    fun removeAdditionalLocation(location: Location) {
        _uiState.update { it.copy(additionalLocations = it.additionalLocations - location, hasChanges = true) }
        scheduleAutosave()
    }

    fun addCurrentLocationAsAdditional() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingLocation = true) }
            locationService.getCurrentLocation().fold(
                onSuccess = { loc ->
                    addLocation(loc)
                    _uiState.update { it.copy(isLoadingLocation = false) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoadingLocation = false) }
                }
            )
        }
    }

    fun addLocationFromCoordinatesAsAdditional(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingLocation = true) }
            val address = locationService.getAddressFromCoordinates(latitude, longitude)
            val placeName = address?.split(",")?.firstOrNull()?.trim()
            addLocation(Location(latitude = latitude, longitude = longitude, address = address, placeName = placeName))
            _uiState.update { it.copy(isLoadingLocation = false) }
        }
    }

    fun toggleAdditionalLocationPicker() {
        _uiState.update { it.copy(showAdditionalLocationPicker = !it.showAdditionalLocationPicker) }
    }

    fun showNicknameDialog(location: Location) {
        _uiState.update { it.copy(showNicknameDialogFor = location) }
    }

    fun dismissNicknameDialog() {
        _uiState.update { it.copy(showNicknameDialogFor = null) }
    }

    /** Nicknames are stored globally so they persist across every entry that references this place. */
    fun setLocationNickname(location: Location, nickname: String) {
        viewModelScope.launch {
            locationNicknameRepository.setNickname(location, nickname)
            _uiState.update { it.copy(showNicknameDialogFor = null) }
        }
    }

    fun removeLocationNickname(location: Location) {
        viewModelScope.launch {
            locationNicknameRepository.removeNickname(location)
            _uiState.update { it.copy(showNicknameDialogFor = null) }
        }
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

    // Text formatting - using span-based formatting
    fun toggleBold() {
        applyTextStyle(SpanStyle.BOLD)
    }

    fun toggleItalic() {
        applyTextStyle(SpanStyle.ITALIC)
    }

    fun toggleUnderline() {
        applyTextStyle(SpanStyle.UNDERLINE)
    }
    
    fun applyHighlight(highlightStyle: SpanStyle) {
        applyTextStyle(highlightStyle)
        _uiState.update { it.copy(showHighlightPicker = false) }
    }
    
    fun applyFont(fontStyle: SpanStyle?) {
        // First remove any existing font styles from selection
        val selection = _uiState.value.content.selection
        if (selection.start < selection.end) {
            val currentFormatting = _uiState.value.textFormatting
            var newFormatting = currentFormatting
            
            // Remove existing font styles in selection range
            listOf(SpanStyle.FONT_SERIF, SpanStyle.FONT_MONO, SpanStyle.FONT_CURSIVE).forEach { style ->
                val spansToRemove = currentFormatting.getSpansOfStyle(style).filter { span ->
                    span.start < selection.end && span.end > selection.start
                }
                spansToRemove.forEach { span ->
                    newFormatting = newFormatting.copy(spans = newFormatting.spans - span)
                }
            }
            
            // Apply new font style if not default
            if (fontStyle != null) {
                newFormatting = newFormatting.toggleSpan(selection.start, selection.end, fontStyle)
            }
            
            _uiState.update { 
                it.copy(
                    textFormatting = newFormatting,
                    showFontPicker = false,
                    hasChanges = true
                )
            }
            scheduleAutosave()
        } else {
            _uiState.update { it.copy(showFontPicker = false) }
        }
    }
    
    fun showHighlightPicker() {
        _uiState.update { it.copy(showHighlightPicker = true) }
    }
    
    fun hideHighlightPicker() {
        _uiState.update { it.copy(showHighlightPicker = false) }
    }
    
    fun showFontPicker() {
        _uiState.update { it.copy(showFontPicker = true) }
    }
    
    fun hideFontPicker() {
        _uiState.update { it.copy(showFontPicker = false) }
    }
    
    /**
     * Applies a text style (bold, italic, underline, highlight, font) to the selected text.
     */
    private fun applyTextStyle(style: SpanStyle) {
        val selection = _uiState.value.content.selection
        
        if (selection.start < selection.end) {
            // Text is selected - toggle the style on selection
            val currentFormatting = _uiState.value.textFormatting
            val newFormatting = currentFormatting.toggleSpan(selection.start, selection.end, style)
            
            _uiState.update { state ->
                state.copy(
                    textFormatting = newFormatting,
                    hasChanges = true
                )
            }
            scheduleAutosave()
        }
        // If no selection, do nothing (user needs to select text first)
    }
    
    /**
     * Called when text content changes - adjusts formatting spans accordingly
     */
    fun onContentChange(newContent: TextFieldValue) {
        val oldContent = _uiState.value.content
        val oldText = oldContent.text
        val newText = newContent.text
        
        // Calculate the change delta
        val delta = newText.length - oldText.length
        val changePosition = if (delta != 0) {
            // Find where the change occurred
            val minLen = minOf(oldText.length, newText.length)
            var pos = 0
            while (pos < minLen && oldText[pos] == newText[pos]) {
                pos++
            }
            pos
        } else {
            newContent.selection.start
        }
        
        // Adjust formatting spans for the text change
        val adjustedFormatting = _uiState.value.textFormatting.adjustForTextChange(changePosition, delta)
        
        _uiState.update { state ->
            state.copy(
                content = newContent,
                textFormatting = adjustedFormatting,
                hasChanges = true
            )
        }
        scheduleAutosave()
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
            // Cancel any pending autosave
            autosaveJob?.cancel()
            saveEntryInternal(isDraft = false)
        }
    }
    
    // Autosave functions
    private fun scheduleAutosave() {
        autosaveJob?.cancel()
        autosaveJob = viewModelScope.launch {
            delay(autosaveIntervalMs)
            performAutosave()
        }
    }
    
    private suspend fun performAutosave() {
        val state = _uiState.value
        
        // Only autosave if there's content
        if (state.content.text.isBlank() && state.title.isBlank()) {
            android.util.Log.d("EditorViewModel", "Autosave skipped - no content")
            return
        }
        
        android.util.Log.d("EditorViewModel", "performAutosave started - tempEntryId=$tempEntryId, originalEntry=${originalEntry?.id}")
        
        // Update autosave status
        _uiState.update { it.copy(autosaveStatus = AutosaveStatus.SAVING) }
        
        try {
            val entry = JournalEntry(
                id = tempEntryId ?: originalEntry?.id ?: 0,
                title = state.title.takeIf { it.isNotBlank() },
                plainContent = state.content.text,
                formattingJson = state.textFormatting.toJson(),
                category = state.category,
                createdAt = state.createdAt,
                updatedAt = LocalDateTime.now(),
                mood = state.mood,
                weather = state.weather,
                location = state.location,
                additionalLocations = state.additionalLocations,
                tags = state.tags,
                people = state.people,
                trackerValues = state.trackerValues,
                isDraft = false, // Autosaved entries are saved as regular entries
                wordCount = state.content.text.split("\\s+".toRegex()).filter { it.isNotBlank() }.size,
                backgroundColor = state.backgroundColor?.id
            )
            
            val savedId = if (tempEntryId == null && originalEntry == null) {
                // First autosave - create new entry
                android.util.Log.d("EditorViewModel", "Autosave: Creating new entry")
                val newId = journalRepository.createEntry(entry)
                tempEntryId = newId
                android.util.Log.d("EditorViewModel", "Autosave: Created entry with id=$newId")
                newId
            } else {
                // Update existing
                val entryId = tempEntryId ?: originalEntry?.id ?: 0
                android.util.Log.d("EditorViewModel", "Autosave: Updating existing entry id=$entryId")
                journalRepository.updateEntry(entry.copy(id = entryId))
                android.util.Log.d("EditorViewModel", "Autosave: Updated entry id=$entryId")
                entryId
            }
            
            // Save related data
            journalRepository.setTagsForEntry(savedId, state.tags.map { it.id })
            journalRepository.setPeopleForEntry(savedId, state.people.map { it.id })
            journalRepository.setTrackerValues(savedId, state.trackerValues)
            
            android.util.Log.d("EditorViewModel", "Autosave complete for entry id=$savedId")
            
            _uiState.update { 
                it.copy(
                    autosaveStatus = AutosaveStatus.SAVED,
                    lastAutosaveTime = LocalDateTime.now()
                ) 
            }
            
            // Reset status after a delay
            delay(2000)
            _uiState.update { it.copy(autosaveStatus = AutosaveStatus.IDLE) }
            
        } catch (e: Exception) {
            android.util.Log.e("EditorViewModel", "Autosave failed", e)
            _uiState.update { it.copy(autosaveStatus = AutosaveStatus.ERROR) }
            delay(2000)
            _uiState.update { it.copy(autosaveStatus = AutosaveStatus.IDLE) }
        }
    }
    
    // Background color functions
    fun setBackgroundColor(color: EntryBackgroundColor?) {
        _uiState.update { it.copy(backgroundColor = color, hasChanges = true) }
    }
    
    fun showBackgroundColorPicker() {
        _uiState.update { it.copy(showBackgroundColorPicker = true) }
    }
    
    fun hideBackgroundColorPicker() {
        _uiState.update { it.copy(showBackgroundColorPicker = false) }
    }
    
    // Template functions
    fun updateCreatedAt(dateTime: LocalDateTime) {
        android.util.Log.d("EditorViewModel", "updateCreatedAt called with: $dateTime")
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
        
        android.util.Log.d("EditorViewModel", "Saving entry - createdAt: ${state.createdAt}, isDraft: $isDraft")
        
        val entry = JournalEntry(
            id = tempEntryId ?: originalEntry?.id ?: 0,
            title = state.title.takeIf { it.isNotBlank() },
            plainContent = state.content.text,
            formattingJson = state.textFormatting.toJson(),
            category = state.category,
            createdAt = state.createdAt,
            updatedAt = LocalDateTime.now(),
            mood = state.mood,
            weather = state.weather,
            location = state.location,
            additionalLocations = state.additionalLocations,
            tags = state.tags,
            people = state.people,
            trackerValues = state.trackerValues,
            isDraft = isDraft,
            wordCount = state.content.text.split("\\s+".toRegex()).filter { it.isNotBlank() }.size,
            backgroundColor = state.backgroundColor?.id
        )

        val entryId = if (tempEntryId == null && originalEntry == null) {
            val newId = journalRepository.createEntry(entry)
            android.util.Log.d("EditorViewModel", "Created new entry with ID: $newId")
            newId
        } else {
            journalRepository.updateEntry(entry)
            android.util.Log.d("EditorViewModel", "Updated entry with ID: ${entry.id}")
            entry.id
        }
        
        // Clear temp entry ID after successful save
        tempEntryId = null
        
        // Save tags, people, and trackers
        journalRepository.setTagsForEntry(entryId, state.tags.map { it.id })
        journalRepository.setPeopleForEntry(entryId, state.people.map { it.id })
        journalRepository.setTrackerValues(entryId, state.trackerValues)
        
        // Save media files - diff against what's already persisted so media the user removed
        // (e.g. deleted an inline image) is actually deleted instead of lingering forever, and
        // so unchanged media isn't re-copied/re-inserted as a duplicate on every save.
        val existingMedia = mediaRepository.getMediaForEntry(entryId)
        val currentUris = state.media.map { it.uri }.toSet()
        val existingUris = existingMedia.map { it.uri }.toSet()
        
        existingMedia.filter { it.uri !in currentUris }.forEach { stale ->
            try {
                mediaRepository.deleteMediaFile(stale.uri)
            } catch (e: Exception) {
                // File may already be gone; still remove the DB row below
            }
            mediaRepository.deleteMedia(stale.id)
        }
        
        state.media.filter { it.uri !in existingUris }.forEach { mediaItem ->
            try {
                val savedMedia = mediaRepository.saveMediaFile(mediaItem.uri, entryId, mediaItem.type)
                    .copy(inlineKey = mediaItem.inlineKey)
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

enum class AutosaveStatus {
    IDLE,
    SAVING,
    SAVED,
    ERROR
}

data class EditorUiState(
    val title: String = "",
    val content: TextFieldValue = TextFieldValue(),
    val textFormatting: TextFormatting = TextFormatting(), // Rich text formatting spans
    val category: Category? = null,
    val mood: Mood? = null,
    val weather: Weather? = null,
    val location: Location? = null,
    val additionalLocations: List<Location> = emptyList(),
    val media: List<MediaItem> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val people: List<Person> = emptyList(),
    val trackerValues: Map<Long, String> = emptyMap(),
    val selectedTrackers: List<CustomTracker> = emptyList(), // Trackers user has added to this entry
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isDraft: Boolean = false,
    val backgroundColor: EntryBackgroundColor? = null, // Entry background color
    
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
    val showAdditionalLocationPicker: Boolean = false,
    val locationSearchResults: List<Location> = emptyList(),
    val isRecording: Boolean = false,
    val recordingDuration: Long = 0,
    val hasChanges: Boolean = false,
    val isSaved: Boolean = false,
    val showTemplateDialog: Boolean = false,
    val showBackgroundColorPicker: Boolean = false,
    val showHighlightPicker: Boolean = false,
    val showFontPicker: Boolean = false,
    val error: String? = null,
    val weatherError: String? = null,
    
    // Autosave state
    val autosaveStatus: AutosaveStatus = AutosaveStatus.IDLE,
    val lastAutosaveTime: LocalDateTime? = null,
    
    // Mention (@person/location/tag/emotion) autocomplete state
    val mentionQuery: String? = null,
    val mentionStartIndex: Int? = null,
    
    // Location nicknames (persistent across entries) and the nickname-edit dialog target
    val locationNicknames: Map<String, String> = emptyMap(),
    val showNicknameDialogFor: Location? = null
) {
    val canSave: Boolean
        get() = content.text.isNotBlank() || title.isNotBlank()
        
    val autosaveStatusText: String
        get() = when (autosaveStatus) {
            AutosaveStatus.IDLE -> ""
            AutosaveStatus.SAVING -> "Saving..."
            AutosaveStatus.SAVED -> lastAutosaveTime?.let { 
                "Saved at ${it.format(DateTimeFormatter.ofPattern("h:mm a"))}"
            } ?: "Saved"
            AutosaveStatus.ERROR -> "Save failed"
        }

    /** Nickname if set for this location, otherwise its place name/address. */
    fun displayNameFor(location: Location): String {
        return locationNicknames[location.nicknameKey()] ?: location.placeName ?: location.address ?: "Location"
    }

    /**
     * Combined, filtered @-mention suggestions across people, locations, tags, and emotions.
     * Locations only ever surface places already added to THIS entry - mentioning a place never
     * creates a new one. If nothing in the entry matches, a non-selectable hint is shown instead.
     */
    val mentionCandidates: List<MentionCandidate>
        get() {
            val query = mentionQuery ?: return emptyList()
            val people = availablePeople
                .filter { it.name.contains(query, ignoreCase = true) }
                .take(6)
                .map { MentionCandidate(MentionType.PERSON, it.name, personId = it.id) }
            
            val entryLocations = (listOfNotNull(location) + additionalLocations).distinct()
            val matchingLocations = entryLocations.filter { loc ->
                query.isBlank() || displayNameFor(loc).contains(query, ignoreCase = true)
            }
            val locations = if (matchingLocations.isNotEmpty()) {
                matchingLocations.take(6).map {
                    MentionCandidate(MentionType.LOCATION, displayNameFor(it), location = it, isAvailable = true)
                }
            } else if (query.isNotBlank()) {
                // Nothing in this entry matches - make that explicit instead of silently doing nothing
                listOf(MentionCandidate(MentionType.LOCATION, query, isAvailable = false))
            } else {
                emptyList()
            }
            
            val tags = availableTags
                .filter { it.name.contains(query, ignoreCase = true) }
                .take(6)
                .map { MentionCandidate(MentionType.TAG, it.name, tagId = it.id) }
            val moods = MoodOption.values()
                .filter { it.label.contains(query, ignoreCase = true) }
                .take(6)
                .map { MentionCandidate(MentionType.EMOTION, it.label, emoji = it.emoji) }
            return people + locations + tags + moods
        }
}

enum class MentionType { PERSON, LOCATION, TAG, EMOTION }

data class MentionCandidate(
    val type: MentionType,
    val displayName: String,
    val personId: Long? = null,
    val tagId: Long? = null,
    val emoji: String? = null,
    val location: Location? = null,
    val isAvailable: Boolean = true // false = informational "not added to this entry" hint
)

data class MediaItem(
    val uri: String,
    val type: MediaType,
    val inlineKey: String? = null // Non-null when this photo is embedded inline within the text
)
