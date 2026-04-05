package com.dairum.presentation.screens.editor

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dairum.R
import com.dairum.domain.model.*
import com.dairum.presentation.components.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditorScreen(
    entryId: Long?,
    onBackClick: () -> Unit,
    onEntrySaved: () -> Unit,
    viewModel: EditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val contentFocusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showMoodDialog by remember { mutableStateOf(false) }
    var showTagDialog by remember { mutableStateOf(false) }
    var showPeopleDialog by remember { mutableStateOf(false) }
    var showTrackerDialog by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    var showMediaOptions by remember { mutableStateOf(false) }
    var showLocationPermissionRationale by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    // Media pickers
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { viewModel.addMedia(it, MediaType.PHOTO) }
    }
    
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.addMedia(it, MediaType.VIDEO) }
    }
    
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.addMedia(it, MediaType.ATTACHMENT) }
    }
    
    // Audio file picker
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.addMedia(it, MediaType.VOICE) }
    }
    
    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        
        if (fineLocationGranted || coarseLocationGranted) {
            viewModel.fetchCurrentLocation()
        }
    }
    
    // Function to check and request location permission
    fun requestLocationWithPermission() {
        when {
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == 
                android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                viewModel.fetchCurrentLocation()
            }
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == 
                android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                viewModel.fetchCurrentLocation()
            }
            else -> {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
    
    // Load entry if editing
    LaunchedEffect(entryId) {
        entryId?.let { viewModel.loadEntry(it) }
    }
    
    // Auto-fetch location for new entries with permission
    LaunchedEffect(Unit) {
        if (entryId == null) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == 
                android.content.pm.PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == 
                android.content.pm.PackageManager.PERMISSION_GRANTED) {
                viewModel.fetchCurrentLocation()
            }
        }
    }
    
    // Handle save success
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onEntrySaved()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (entryId != null) "Edit Entry" else "New Entry"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.hasChanges) {
                            showDiscardDialog = true
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.action_close)
                        )
                    }
                },
                actions = {
                    // Template button (always visible for new entries)
                    if (entryId == null) {
                        IconButton(onClick = { viewModel.showTemplateDialog() }) {
                            Icon(
                                imageVector = Icons.Outlined.Description,
                                contentDescription = "Use Template"
                            )
                        }
                    }
                    
                    // Save as draft
                    if (!uiState.isDraft) {
                        TextButton(onClick = { viewModel.saveAsDraft() }) {
                            Text("Draft")
                        }
                    }
                    
                    // Save button
                    Button(
                        onClick = { viewModel.saveEntry() },
                        enabled = uiState.canSave
                    ) {
                        Text(stringResource(R.string.action_save))
                    }
                }
            )
        },
        bottomBar = {
            // Formatting toolbar
            Surface(
                tonalElevation = 3.dp
            ) {
                Column {
                    // Media & trackers bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Timestamp button
                        IconButton(onClick = { viewModel.insertTimestamp() }) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = stringResource(R.string.entry_add_timestamp)
                            )
                        }
                        
                        // Photo
                        IconButton(onClick = { photoPickerLauncher.launch("image/*") }) {
                            Icon(
                                imageVector = Icons.Outlined.Photo,
                                contentDescription = stringResource(R.string.entry_add_photo)
                            )
                        }
                        
                        // Video
                        IconButton(onClick = { videoPickerLauncher.launch("video/*") }) {
                            Icon(
                                imageVector = Icons.Outlined.Videocam,
                                contentDescription = stringResource(R.string.entry_add_video)
                            )
                        }
                        
                        // Audio Upload
                        IconButton(onClick = { 
                            audioPickerLauncher.launch("audio/*")
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.AudioFile,
                                contentDescription = "Add Audio"
                            )
                        }
                        
                        // Attachment
                        IconButton(onClick = { filePickerLauncher.launch("*/*") }) {
                            Icon(
                                imageVector = Icons.Outlined.AttachFile,
                                contentDescription = stringResource(R.string.entry_add_file)
                            )
                        }
                        
                        // Location
                        IconButton(
                            onClick = {
                                if (uiState.location == null) {
                                    // If no location set, request permission and get current location
                                    requestLocationWithPermission()
                                }
                                viewModel.toggleLocationPicker()
                            }
                        ) {
                            if (uiState.isLoadingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = if (uiState.location != null) {
                                        Icons.Filled.LocationOn
                                    } else {
                                        Icons.Outlined.LocationOn
                                    },
                                    contentDescription = stringResource(R.string.entry_add_location),
                                    tint = if (uiState.location != null) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        LocalContentColor.current
                                    }
                                )
                            }
                        }
                    }
                    
                    HorizontalDivider()
                    
                    // Text formatting bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = { viewModel.toggleBold() }) {
                            Icon(Icons.Outlined.FormatBold, contentDescription = "Bold")
                        }
                        IconButton(onClick = { viewModel.toggleItalic() }) {
                            Icon(Icons.Outlined.FormatItalic, contentDescription = "Italic")
                        }
                        IconButton(onClick = { viewModel.toggleUnderline() }) {
                            Icon(Icons.Outlined.FormatUnderlined, contentDescription = "Underline")
                        }
                        IconButton(onClick = { viewModel.addBulletList() }) {
                            Icon(Icons.Outlined.FormatListBulleted, contentDescription = "Bullet list")
                        }
                        IconButton(onClick = { viewModel.addNumberedList() }) {
                            Icon(Icons.Outlined.FormatListNumbered, contentDescription = "Numbered list")
                        }
                        IconButton(onClick = { viewModel.addHeading() }) {
                            Icon(Icons.Outlined.Title, contentDescription = "Heading")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Show audio files
            if (uiState.media.any { it.type == MediaType.VOICE }) {
                AudioFilesSection(
                    audioFiles = uiState.media.filter { it.type == MediaType.VOICE },
                    onRemove = { viewModel.removeMedia(it) },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // Date row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showDatePicker = true }
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = uiState.createdAt.format(
                        DateTimeFormatter.ofPattern("EEE, MMM d 'at' h:mm a")
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Change date",
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Location Box
            LocationInfoBox(
                location = uiState.location,
                isLoading = uiState.isLoadingLocation,
                onRefresh = { requestLocationWithPermission() },
                onEdit = { viewModel.toggleLocationPicker() }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Weather Box
            WeatherInfoBox(
                weather = uiState.weather,
                isLoading = uiState.isLoadingWeather,
                error = uiState.weatherError,
                onRefresh = { viewModel.fetchWeather() }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Category selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showCategoryDialog = true }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Folder,
                    contentDescription = null,
                    tint = uiState.category?.let { Color(it.color) }
                        ?: MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = uiState.category?.name ?: stringResource(R.string.entry_select_category),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (uiState.category != null) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Title input
            BasicTextField(
                value = uiState.title,
                onValueChange = { viewModel.updateTitle(it) },
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box {
                        if (uiState.title.isEmpty()) {
                            Text(
                                text = stringResource(R.string.entry_title_hint),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Content input
            BasicTextField(
                value = uiState.content,
                onValueChange = { viewModel.updateContent(it) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp)
                    .focusRequester(contentFocusRequester),
                decorationBox = { innerTextField ->
                    Box {
                        if (uiState.content.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.entry_content_hint),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Media preview
            if (uiState.media.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.media) { media ->
                        MediaThumbnail(
                            media = media,
                            onRemove = { viewModel.removeMedia(media) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            HorizontalDivider()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.entry_add_tags),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                IconButton(onClick = { showTagDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add tag")
                }
            }
            
            if (uiState.tags.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.tags.forEach { tag ->
                        TagChip(
                            tag = tag,
                            onRemove = { viewModel.removeTag(tag) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // People
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.entry_add_people),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                IconButton(onClick = { showPeopleDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add person")
                }
            }
            
            if (uiState.people.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.people) { person ->
                        PersonChip(
                            person = person,
                            onRemove = { viewModel.removePerson(person) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Custom trackers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trackers",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                IconButton(onClick = { showTrackerDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add tracker")
                }
            }
            
            uiState.selectedTrackers.forEach { tracker ->
                TrackerInput(
                    tracker = tracker,
                    value = uiState.trackerValues[tracker.id] ?: "",
                    onValueChange = { viewModel.updateTrackerValue(tracker.id, it) },
                    onRemove = { viewModel.removeTracker(tracker) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mood section with add button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.entry_select_mood),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                IconButton(onClick = { showMoodDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add mood")
                }
            }
            
            // Show selected mood if any
            uiState.mood?.let { mood ->
                MoodChip(
                    mood = mood,
                    onRemove = { viewModel.updateMood(null) }
                )
            }
            
            // Location preview
            uiState.location?.let { location ->
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                
                LocationPreview(
                    location = location,
                    onRemove = { viewModel.removeLocation() }
                )
            }
            
            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for toolbar
        }
    }
    
    // Discard dialog
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text(stringResource(R.string.entry_unsaved_changes)) },
            text = { Text(stringResource(R.string.entry_discard_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showDiscardDialog = false
                    onBackClick()
                }) {
                    Text(stringResource(R.string.action_discard))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }
    
    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.createdAt
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        val currentTime = uiState.createdAt.toLocalTime()
                        val newDateTime = java.time.LocalDateTime.of(selectedDate, currentTime)
                        viewModel.updateCreatedAt(newDateTime)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    // Category dialog
    if (showCategoryDialog) {
        CategoryPickerDialog(
            categories = uiState.availableCategories,
            selectedCategory = uiState.category,
            onCategorySelected = { 
                viewModel.updateCategory(it)
                showCategoryDialog = false
            },
            onDismiss = { showCategoryDialog = false }
        )
    }
    
    // Tag dialog
    if (showTagDialog) {
        TagPickerDialog(
            availableTags = uiState.availableTags,
            selectedTags = uiState.tags,
            onTagSelected = { viewModel.addTag(it) },
            onTagCreated = { viewModel.createAndAddTag(it) },
            onDismiss = { showTagDialog = false }
        )
    }
    
    // Tracker dialog
    if (showTrackerDialog) {
        TrackerPickerDialog(
            availableTrackers = uiState.trackers,
            selectedTrackers = uiState.selectedTrackers,
            onTrackerSelected = { viewModel.addTracker(it) },
            onDismiss = { showTrackerDialog = false }
        )
    }
    
    // People picker dialog
    if (showPeopleDialog) {
        PeoplePickerDialog(
            availablePeople = uiState.availablePeople,
            selectedPeople = uiState.people,
            onPersonSelected = { viewModel.addPerson(it) },
            onPersonCreated = { viewModel.createAndAddPerson(it) },
            onDismiss = { showPeopleDialog = false }
        )
    }
    
    // Mood picker dialog
    if (showMoodDialog) {
        MoodPickerDialog(
            selectedMood = uiState.mood,
            onMoodSelected = { 
                viewModel.updateMood(it)
                showMoodDialog = false
            },
            onDismiss = { showMoodDialog = false }
        )
    }
    
    // Location picker dialog
    if (uiState.showLocationPicker) {
        LocationPickerDialog(
            currentLocation = uiState.location,
            searchResults = uiState.locationSearchResults,
            isLoading = uiState.isLoadingLocation,
            onSearch = { viewModel.searchLocations(it) },
            onLocationSelected = { 
                viewModel.setLocation(it)
                viewModel.toggleLocationPicker()
            },
            onLocationPickedFromMap = { lat, lng ->
                android.util.Log.d("EditorScreen", "Location picked from map: lat=$lat, lng=$lng")
                viewModel.setLocationFromCoordinates(lat, lng)
            },
            onGetCurrentLocation = { viewModel.fetchCurrentLocation() },
            onRemoveLocation = { viewModel.removeLocation() },
            onDismiss = { 
                viewModel.clearLocationSearch()
                viewModel.toggleLocationPicker() 
            }
        )
    }
    
    // Template picker dialog
    if (uiState.showTemplateDialog) {
        TemplatePickerDialog(
            templates = uiState.templates,
            onTemplateSelected = { viewModel.applyTemplate(it) },
            onDismiss = { viewModel.hideTemplateDialog() }
        )
    }
}

@Composable
private fun MediaThumbnail(
    media: MediaItem,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(80.dp)) {
        when (media.type) {
            MediaType.PHOTO, MediaType.VIDEO -> {
                AsyncImage(
                    model = media.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                if (media.type == MediaType.VIDEO) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp),
                        tint = Color.White
                    )
                }
            }
            MediaType.VOICE -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            MediaType.ATTACHMENT -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.InsertDriveFile,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
        
        // Remove button
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun PersonChip(
    person: Person,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(start = 4.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                if (person.photoUri != null) {
                    AsyncImage(
                        model = person.photoUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = person.name.first().uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = person.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Spacer(modifier = Modifier.width(4.dp))
            
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier
                    .size(16.dp)
                    .clickable(onClick = onRemove),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun TrackerInput(
    tracker: CustomTracker,
    value: String,
    onValueChange: (String) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (tracker.type) {
                            TrackerType.NUMBER -> Icons.Outlined.Numbers
                            TrackerType.TEXT -> Icons.Outlined.TextFields
                            TrackerType.BOOLEAN -> Icons.Outlined.CheckBox
                            TrackerType.RATING -> Icons.Outlined.Star
                            TrackerType.WEIGHT -> Icons.Outlined.FitnessCenter
                            TrackerType.PEOPLE -> Icons.Outlined.People
                            TrackerType.SELECT -> Icons.Outlined.List
                        },
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tracker.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove tracker",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            when (tracker.type) {
                TrackerType.TEXT -> {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Enter value") }
                    )
                }
                TrackerType.NUMBER, TrackerType.WEIGHT -> {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Enter number") },
                        suffix = tracker.unit?.let { { Text(it) } }
                    )
                }
                TrackerType.RATING -> {
                    // Star rating
                    Row {
                        for (i in 1..5) {
                            IconButton(
                                onClick = { onValueChange(i.toString()) }
                            ) {
                                Icon(
                                    imageVector = if (i <= (value.toIntOrNull() ?: 0)) {
                                        Icons.Filled.Star
                                    } else {
                                        Icons.Outlined.Star
                                    },
                                    contentDescription = null,
                                    tint = if (i <= (value.toIntOrNull() ?: 0)) {
                                        Color(0xFFFFD700)
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }
                        }
                    }
                }
                TrackerType.BOOLEAN -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (value == "true") "Yes" else "No",
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = value == "true",
                            onCheckedChange = { onValueChange(it.toString()) }
                        )
                    }
                }
                TrackerType.SELECT -> {
                    // Show options if available
                    tracker.options?.let { options ->
                        options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onValueChange(option) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = value == option,
                                    onClick = { onValueChange(option) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = option)
                            }
                        }
                    } ?: OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                else -> {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }
    }
}

// Modern Voice Recording Composables

@Composable
private fun VoiceRecordingCard(
    duration: Long,
    onCancel: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "recording")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with recording indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Animated recording dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                MaterialTheme.colorScheme.error.copy(alpha = pulseAlpha),
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Recording Voice Note",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Timer display (large and centered)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = formatRecordingDuration(duration),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Waveform placeholder (simple animated bars)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until 20) {
                    val barHeight by infiniteTransition.animateFloat(
                        initialValue = 8f,
                        targetValue = 32f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(300 + (i * 50), easing = EaseInOut),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "bar_$i"
                    )
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(barHeight.dp)
                            .background(
                                MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel button
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancel")
                }
                
                // Stop button
                Button(
                    onClick = onStop,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Filled.Stop,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Recording")
                }
            }
        }
    }
}

@Composable
private fun AudioFilesSection(
    audioFiles: List<MediaItem>,
    onRemove: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.AudioFile,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Audio Files (${audioFiles.size})",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        audioFiles.forEach { audioFile ->
            AudioFileItem(
                audioFile = audioFile,
                onRemove = { onRemove(audioFile) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AudioFileItem(
    audioFile: MediaItem,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var player by remember { mutableStateOf<android.media.MediaPlayer?>(null) }
    
    DisposableEffect(audioFile.uri) {
        onDispose {
            player?.release()
            player = null
        }
    }
    
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play/Pause button
            FilledIconButton(
                onClick = { 
                    if (isPlaying) {
                        player?.pause()
                        isPlaying = false
                    } else {
                        if (player == null) {
                            try {
                                player = android.media.MediaPlayer().apply {
                                    setDataSource(context, android.net.Uri.parse(audioFile.uri))
                                    prepare()
                                    setOnCompletionListener {
                                        isPlaying = false
                                    }
                                }
                            } catch (e: Exception) {
                                return@FilledIconButton
                            }
                        }
                        player?.start()
                        isPlaying = true
                    }
                },
                modifier = Modifier.size(44.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Audio file info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Audio File",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = audioFile.uri.substringAfterLast("/"),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Delete button
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun formatRecordingDuration(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
private fun LocationPreview(
    location: Location,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = location.placeName ?: "Selected Location",
                    style = MaterialTheme.typography.bodyMedium
                )
                location.address?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove"
                )
            }
        }
    }
}

@Composable
private fun CategoryPickerDialog(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.entry_select_category)) },
        text = {
            Column {
                categories.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategorySelected(category) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(category.color))
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = category.name,
                            modifier = Modifier.weight(1f)
                        )
                        
                        if (selectedCategory?.id == category.id) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}

@Composable
private fun TagPickerDialog(
    availableTags: List<Tag>,
    selectedTags: List<Tag>,
    onTagSelected: (Tag) -> Unit,
    onTagCreated: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newTagName by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.entry_add_tags)) },
        text = {
            Column {
                // New tag input
                OutlinedTextField(
                    value = newTagName,
                    onValueChange = { newTagName = it },
                    placeholder = { Text("Create new tag") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        if (newTagName.isNotBlank()) {
                            IconButton(onClick = {
                                onTagCreated(newTagName)
                                newTagName = ""
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Existing tags
                availableTags.forEach { tag ->
                    val isSelected = selectedTags.any { it.id == tag.id }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                if (!isSelected) onTagSelected(tag)
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "#${tag.name}",
                            modifier = Modifier.weight(1f),
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                        
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        },
        dismissButton = {}
    )
}

@Composable
private fun TrackerPickerDialog(
    availableTrackers: List<CustomTracker>,
    selectedTrackers: List<CustomTracker>,
    onTrackerSelected: (CustomTracker) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Tracker") },
        text = {
            if (availableTrackers.isEmpty()) {
                Text(
                    text = "No trackers configured. Go to Settings > Manage Trackers to create trackers.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column {
                    availableTrackers.forEach { tracker ->
                        val isSelected = selectedTrackers.any { it.id == tracker.id }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    if (!isSelected) {
                                        onTrackerSelected(tracker)
                                    }
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (tracker.type) {
                                    TrackerType.NUMBER -> Icons.Outlined.Numbers
                                    TrackerType.TEXT -> Icons.Outlined.TextFields
                                    TrackerType.BOOLEAN -> Icons.Outlined.CheckBox
                                    TrackerType.RATING -> Icons.Outlined.Star
                                    TrackerType.WEIGHT -> Icons.Outlined.FitnessCenter
                                    TrackerType.PEOPLE -> Icons.Outlined.People
                                    TrackerType.SELECT -> Icons.Outlined.List
                                },
                                contentDescription = null,
                                tint = if (isSelected) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tracker.name,
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                                Text(
                                    text = tracker.type.name.lowercase().replaceFirstChar { it.uppercase() } +
                                            (tracker.unit?.let { " ($it)" } ?: ""),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        },
        dismissButton = {}
    )
}

@Composable
private fun LocationPickerDialog(
    currentLocation: Location?,
    searchResults: List<Location>,
    isLoading: Boolean,
    onSearch: (String) -> Unit,
    onLocationSelected: (Location) -> Unit,
    onLocationPickedFromMap: (Double, Double) -> Unit,
    onGetCurrentLocation: () -> Unit,
    onRemoveLocation: () -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showMapPicker by remember { mutableStateOf(false) }
    var mapPickerLocation by remember { 
        mutableStateOf(currentLocation?.let { GeoPoint(it.latitude, it.longitude) } 
            ?: GeoPoint(0.0, 0.0)) 
    }
    
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    
    // Initialize OSMDroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }
    
    if (showMapPicker) {
        // Full-screen map picker dialog
        AlertDialog(
            onDismissRequest = { showMapPicker = false },
            modifier = Modifier.fillMaxSize(),
            title = { 
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showMapPicker = false }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                    Text("Pick Location on Map")
                }
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AndroidView(
                        factory = { ctx ->
                            MapView(ctx).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(true)
                                controller.setZoom(15.0)
                                
                                // Center on current location or default
                                val startPoint = if (currentLocation != null) {
                                    GeoPoint(currentLocation.latitude, currentLocation.longitude)
                                } else {
                                    GeoPoint(37.7749, -122.4194) // Default to San Francisco
                                }
                                controller.setCenter(startPoint)
                                mapPickerLocation = startPoint
                                
                                // Add initial marker
                                val marker = Marker(this).apply {
                                    position = startPoint
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    title = "Selected Location"
                                    isDraggable = true
                                    setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                                        override fun onMarkerDrag(marker: Marker?) {
                                            marker?.position?.let { pos ->
                                                mapPickerLocation = pos
                                            }
                                        }
                                        override fun onMarkerDragEnd(marker: Marker?) {
                                            marker?.position?.let { pos ->
                                                mapPickerLocation = pos
                                            }
                                        }
                                        override fun onMarkerDragStart(marker: Marker?) {}
                                    })
                                }
                                overlays.add(marker)
                                
                                // Allow tapping to move marker
                                setOnTouchListener { _, event ->
                                    if (event.action == android.view.MotionEvent.ACTION_UP) {
                                        val projection = projection
                                        val tappedPoint = projection.fromPixels(
                                            event.x.toInt(), 
                                            event.y.toInt()
                                        ) as GeoPoint
                                        
                                        // Update marker position
                                        overlays.filterIsInstance<Marker>().firstOrNull()?.let {
                                            it.position = tappedPoint
                                            mapPickerLocation = tappedPoint
                                            invalidate()
                                        }
                                    }
                                    false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Center crosshair indicator
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                            .offset(y = (-24).dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    // Coordinates display
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Text(
                            text = "📍 ${String.format("%.5f", mapPickerLocation.latitude)}, ${String.format("%.5f", mapPickerLocation.longitude)}",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onLocationPickedFromMap(mapPickerLocation.latitude, mapPickerLocation.longitude)
                        showMapPicker = false
                    }
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Use This Location")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMapPicker = false }) {
                    Text("Cancel")
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Location") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Current location display
                    if (currentLocation != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = currentLocation.placeName ?: "Current Location",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    currentLocation.address?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                            maxLines = 2
                                        )
                                    }
                                }
                                IconButton(onClick = onRemoveLocation) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                        
                        // Mini map preview of current location
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            AndroidView(
                                factory = { ctx ->
                                    MapView(ctx).apply {
                                        setTileSource(TileSourceFactory.MAPNIK)
                                        setMultiTouchControls(false)
                                        controller.setZoom(14.0)
                                        controller.setCenter(GeoPoint(currentLocation.latitude, currentLocation.longitude))
                                        
                                        val marker = Marker(this).apply {
                                            position = GeoPoint(currentLocation.latitude, currentLocation.longitude)
                                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                        }
                                        overlays.add(marker)
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    
                    // Action buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Get current location button
                        OutlinedButton(
                            onClick = onGetCurrentLocation,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(Icons.Outlined.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Current", style = MaterialTheme.typography.labelMedium)
                        }
                        
                        // Pick on map button
                        OutlinedButton(
                            onClick = { showMapPicker = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Outlined.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pick on Map", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    
                    HorizontalDivider()
                    
                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            onSearch(it)
                        },
                        placeholder = { Text("Search for a place...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Outlined.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { 
                                    searchQuery = ""
                                    onSearch("") 
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        }
                    )
                    
                    // Search results
                    if (searchResults.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            searchResults.take(5).forEach { location ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onLocationSelected(location) },
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Place,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = location.placeName ?: "Unknown Place",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            location.address?.let {
                                                Text(
                                                    text = it,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Done")
                }
            },
            dismissButton = {}
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = { content() }
    )
}

private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
private fun LocationInfoBox(
    location: Location?,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locationColor = Color(0xFF4CAF50) // Green for location
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = locationColor.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, locationColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(locationColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = locationColor
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = locationColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Location",
                    style = MaterialTheme.typography.labelSmall,
                    color = locationColor
                )
                if (location != null) {
                    Text(
                        text = location.placeName ?: location.address ?: "${location.latitude}, ${location.longitude}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                    if (location.address != null && location.placeName != null) {
                        Text(
                            text = location.address,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                } else {
                    Text(
                        text = if (isLoading) "Getting location..." else "Tap to add location",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Row {
                IconButton(onClick = onRefresh, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.MyLocation,
                        contentDescription = "Get current location",
                        tint = locationColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit location",
                        tint = locationColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherInfoBox(
    weather: Weather?,
    isLoading: Boolean,
    error: String?,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val weatherColor = Color(0xFF2196F3) // Blue for weather
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = weatherColor.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, weatherColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(weatherColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = weatherColor
                    )
                } else if (weather != null) {
                    AsyncImage(
                        model = weather.icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.WbSunny,
                        contentDescription = null,
                        tint = weatherColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Weather",
                    style = MaterialTheme.typography.labelSmall,
                    color = weatherColor
                )
                if (weather != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${weather.temperature.toInt()}°C",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = weather.condition,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row {
                        Text(
                            text = "Feels like ${weather.feelsLike.toInt()}°C",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "💧 ${weather.humidity}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else if (error != null) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        maxLines = 2
                    )
                } else {
                    Text(
                        text = if (isLoading) "Loading weather..." else "Tap to add weather",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            IconButton(onClick = onRefresh, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "Refresh weather",
                    tint = weatherColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun PeoplePickerDialog(
    availablePeople: List<Person>,
    selectedPeople: List<Person>,
    onPersonSelected: (Person) -> Unit,
    onPersonCreated: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newPersonName by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.entry_add_people)) },
        text = {
            Column {
                // New person input
                OutlinedTextField(
                    value = newPersonName,
                    onValueChange = { newPersonName = it },
                    placeholder = { Text("Add new person") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Outlined.PersonAdd, contentDescription = null)
                    },
                    trailingIcon = {
                        if (newPersonName.isNotBlank()) {
                            IconButton(onClick = {
                                onPersonCreated(newPersonName)
                                newPersonName = ""
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Existing people
                if (availablePeople.isEmpty()) {
                    Text(
                        text = "No people added yet. Create one above!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    availablePeople.forEach { person ->
                        val isSelected = selectedPeople.any { it.id == person.id }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    if (!isSelected) onPersonSelected(person)
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = person.name.take(1).uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Text(
                                text = person.name,
                                modifier = Modifier.weight(1f),
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                            
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        },
        dismissButton = {}
    )
}

@Composable
private fun TemplatePickerDialog(
    templates: List<EntryTemplate>,
    onTemplateSelected: (EntryTemplate) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text("Choose a Template")
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(templates.size) { index ->
                    val template = templates[index]
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTemplateSelected(template) },
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = template.icon,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = template.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                template.description?.let { desc ->
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun MoodChip(
    mood: Mood,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val moodOption = MoodOption.fromId(mood.level)
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = moodOption?.emoji ?: mood.emoji,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = moodOption?.label ?: "Mood ${mood.level}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove mood",
                modifier = Modifier
                    .size(18.dp)
                    .clickable(onClick = onRemove),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MoodPickerDialog(
    selectedMood: Mood?,
    onMoodSelected: (Mood?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Mood") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                MoodCategory.values().forEach { category ->
                    val moods = MoodOption.getByCategory(category)
                    
                    Text(
                        text = when (category) {
                            MoodCategory.POSITIVE -> "😊 Positive"
                            MoodCategory.NEUTRAL -> "😐 Neutral"
                            MoodCategory.NEGATIVE -> "😔 Negative"
                            MoodCategory.MIXED -> "🤔 Other"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        moods.forEach { moodOption ->
                            val mood = Mood(level = moodOption.id, moodType = MoodType.EXTENDED)
                            val isSelected = selectedMood?.level == moodOption.id
                            
                            Surface(
                                modifier = Modifier.clickable { 
                                    onMoodSelected(if (isSelected) null else mood)
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = moodOption.emoji)
                                    Text(
                                        text = moodOption.label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) {
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
