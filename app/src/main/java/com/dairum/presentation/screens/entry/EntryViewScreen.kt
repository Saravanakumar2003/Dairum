package com.dairum.presentation.screens.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dairum.R
import com.dairum.domain.model.*
import com.dairum.presentation.components.*
import com.dairum.presentation.screens.editor.TextFormatting
import com.dairum.presentation.screens.editor.toAnnotatedString
import com.dairum.presentation.theme.EntryBackgroundColors
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EntryViewScreen(
    entryId: Long,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: EntryViewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showShareOptions by remember { mutableStateOf(false) }

    LaunchedEffect(entryId) {
        viewModel.loadEntry(entryId)
    }
    
    // Share function
    fun shareEntry() {
        val entry = uiState.entry ?: return
        val shareText = buildString {
            entry.title?.let { append("$it\n\n") }
            append(entry.plainContent)
            append("\n\n")
            entry.mood?.let { append("Mood: ${it.emoji} ${it.label}\n") }
            entry.location?.let { append("📍 ${it.placeName ?: it.address ?: "Unknown location"}\n") }
            if (entry.tags.isNotEmpty()) {
                append("Tags: ${entry.tags.joinToString { "#${it.name}" }}\n")
            }
            if (entry.people.isNotEmpty()) {
                append("With: ${entry.people.joinToString { it.name }}\n")
            }
            append("\n— From Dairum Journal")
        }
        
        val sendIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = android.content.Intent.createChooser(sendIntent, "Share journal entry")
        context.startActivity(shareIntent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Share button
                    IconButton(onClick = { shareEntry() }) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share"
                        )
                    }
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (uiState.entry?.isFavorite == true) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = "Toggle favorite",
                            tint = if (uiState.entry?.isFavorite == true) {
                                Color.Red
                            } else {
                                LocalContentColor.current
                            }
                        )
                    }
                    IconButton(onClick = { uiState.entry?.let { onEditClick(it.id) } }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(R.string.action_edit)
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.action_delete)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.entry == null -> {
                EmptyState(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Error,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    },
                    title = "Entry not found",
                    message = "This journal entry could not be loaded"
                )
            }
            else -> {
                val entry = uiState.entry!!
                val isDarkTheme = isSystemInDarkTheme()
                
                // Get the background color for this entry
                val backgroundColor = entry.backgroundColor?.let { colorId ->
                    EntryBackgroundColors.fromId(colorId, isDarkTheme)?.color
                } ?: MaterialTheme.colorScheme.background
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                ) {
                    // Modern Header Card
                    ModernEntryHeader(entry = entry)
                    
                    // Media gallery with improved spacing
                    if (uiState.media.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        MediaGallery(media = uiState.media)
                    }
                    
                    // Content
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Title with gradient underline effect
                        entry.title?.let { title ->
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        // Content text with formatting applied
                        val formatting = remember(entry.formattingJson) {
                            TextFormatting.fromJson(entry.formattingJson)
                        }
                        val formattedText = remember(entry.plainContent, formatting) {
                            formatting.toAnnotatedString(entry.plainContent)
                        }
                        
                        Text(
                            text = formattedText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.87f)
                        )
                        
                        // Tags with modern chips
                        if (entry.tags.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(28.dp))
                            ModernTagsSection(tags = entry.tags)
                        }
                        
                        // People section
                        if (entry.people.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            ModernPeopleSection(people = entry.people)
                        }
                        
                        // Trackers with improved cards
                        if (entry.trackerValues.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            TrackersSection(
                                trackerValues = entry.trackerValues,
                                trackers = uiState.trackers
                            )
                        }
                        
                        // Weather & Location section with modern card
                        if (entry.weather != null || entry.location != null) {
                            Spacer(modifier = Modifier.height(20.dp))
                            WeatherLocationSection(
                                weather = entry.weather,
                                location = entry.location
                            )
                        }
                        
                        // Modern footer with word count
                        Spacer(modifier = Modifier.height(32.dp))
                        ModernEntryFooter(entry = entry)
                        
                        // Bottom spacing
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Entry") },
            text = { Text("Are you sure you want to delete this journal entry? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteEntry()
                        showDeleteDialog = false
                        onBackClick()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun EntryHeader(entry: JournalEntry) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Date and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = entry.createdAt.format(
                            DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = entry.createdAt.format(
                            DateTimeFormatter.ofPattern("h:mm a")
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Mood indicator
                entry.mood?.let { mood ->
                    MoodIndicator(
                        mood = mood,
                        size = 48.dp,
                        showLabel = true
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Category and Weather row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category chip
                entry.category?.let { category ->
                    AssistChip(
                        onClick = { },
                        label = { Text(category.name) },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(category.color), CircleShape)
                            )
                        }
                    )
                }
                
                // Weather badge
                entry.weather?.let { weather ->
                    WeatherBadge(weather = weather)
                }
            }
        }
    }
}

@Composable
private fun MediaGallery(media: List<Media>) {
    if (media.isEmpty()) return
    
    var selectedMedia by remember { mutableStateOf<Media?>(null) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    
    val photos = media.filter { it.type == MediaType.PHOTO }
    val videos = media.filter { it.type == MediaType.VIDEO }
    val voiceNotes = media.filter { it.type == MediaType.VOICE }
    val attachments = media.filter { it.type == MediaType.ATTACHMENT }
    
    Column {
        // Photos
        if (photos.isNotEmpty()) {
            if (photos.size == 1) {
                AsyncImage(
                    model = photos[0].uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .clickable { 
                            selectedMedia = photos[0]
                            selectedIndex = 0
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(photos.size) { index ->
                        val photo = photos[index]
                        AsyncImage(
                            model = photo.thumbnailUri ?: photo.uri,
                            contentDescription = photo.caption,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { 
                                    selectedMedia = photo
                                    selectedIndex = index
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        
        // Videos
        if (videos.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(videos.size) { index ->
                    val video = videos[index]
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedMedia = video },
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = video.thumbnailUri ?: video.uri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = "Play video",
                            modifier = Modifier.size(48.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
        
        // Voice notes
        if (voiceNotes.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                voiceNotes.forEach { voice ->
                    VoiceNoteCard(voiceNote = voice)
                }
            }
        }
        
        // Attachments
        if (attachments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                attachments.forEach { attachment ->
                    AttachmentCard(attachment = attachment)
                }
            }
        }
    }
    
    // Fullscreen Media Viewer Dialog
    selectedMedia?.let { media ->
        FullscreenMediaViewer(
            media = media,
            allPhotos = photos,
            initialIndex = selectedIndex,
            onDismiss = { selectedMedia = null },
            onIndexChange = { newIndex -> selectedIndex = newIndex }
        )
    }
}

@Composable
private fun VoiceNoteCard(voiceNote: Media) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Mic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = voiceNote.fileName,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                voiceNote.duration?.let { duration ->
                    Text(
                        text = formatDuration(duration),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = { /* Play audio */ }) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play"
                )
            }
        }
    }
}

@Composable
private fun AttachmentCard(attachment: Media) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.AttachFile,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = attachment.fileName,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatFileSize(attachment.sizeBytes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { /* Open file */ }) {
                Icon(
                    imageVector = Icons.Outlined.OpenInNew,
                    contentDescription = "Open"
                )
            }
        }
    }
}

@Composable
private fun TagsSection(tags: List<Tag>) {
    Column {
        Text(
            text = "Tags",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tags) { tag ->
                AssistChip(
                    onClick = { },
                    label = { Text(tag.name) },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(tag.color ?: 0xFF808080.toInt()), CircleShape)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun PeopleSection(people: List<Person>) {
    Column {
        Text(
            text = "People",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(people) { person ->
                AssistChip(
                    onClick = { },
                    label = { Text(person.name) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TrackersSection(
    trackerValues: Map<Long, String>,
    trackers: List<CustomTracker>
) {
    Column {
        Text(
            text = "Trackers",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        trackerValues.forEach { (trackerId, value) ->
            val tracker = trackers.find { it.id == trackerId }
            if (tracker != null) {
                TrackerValueItem(tracker = tracker, value = value)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun TrackerValueItem(tracker: CustomTracker, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = when (tracker.type) {
                    TrackerType.NUMBER -> Icons.Outlined.Numbers
                    TrackerType.BOOLEAN -> Icons.Outlined.CheckBox
                    TrackerType.RATING -> Icons.Outlined.Star
                    TrackerType.TEXT -> Icons.Outlined.TextFields
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
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Text(
            text = when (tracker.type) {
                TrackerType.BOOLEAN -> if (value == "true") "Yes" else "No"
                TrackerType.NUMBER -> "$value ${tracker.unit ?: ""}"
                TrackerType.RATING -> "★".repeat(value.toIntOrNull() ?: 0)
                else -> value
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun WeatherLocationSection(
    weather: Weather?,
    location: Location?
) {
    val context = LocalContext.current
    
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Environment",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Weather section
                weather?.let { w ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Weather icon
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = w.icon,
                                contentDescription = w.condition,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Weather info
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${w.temperature.toInt()}°C / ${w.temperatureFahrenheit.toInt()}°F",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = w.condition,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    // Weather details row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        WeatherDetailItem(
                            icon = Icons.Outlined.Thermostat,
                            label = "Feels Like",
                            value = "${w.feelsLike.toInt()}°C"
                        )
                        WeatherDetailItem(
                            icon = Icons.Outlined.WaterDrop,
                            label = "Humidity",
                            value = "${w.humidity}%"
                        )
                        w.locationName?.let { name ->
                            WeatherDetailItem(
                                icon = Icons.Outlined.LocationOn,
                                label = "Weather @",
                                value = name
                            )
                        }
                    }
                }
                
                // Divider if both weather and location present
                if (weather != null && location != null) {
                    HorizontalDivider()
                }
                
                // Location section
                location?.let { loc ->
                    // Initialize OSMDroid
                    LaunchedEffect(Unit) {
                        Configuration.getInstance().userAgentValue = context.packageName
                    }
                    
                    // Helper function to open location in maps
                    val openInMaps: () -> Unit = {
                        android.util.Log.d("EntryViewScreen", "Opening location in maps: lat=${loc.latitude}, lng=${loc.longitude}, place=${loc.placeName}")
                        try {
                            // Try Google Maps first with geo: URI
                            val gmmIntentUri = android.net.Uri.parse(
                                "geo:${loc.latitude},${loc.longitude}?q=${loc.latitude},${loc.longitude}(${java.net.URLEncoder.encode(loc.placeName ?: "Location", "UTF-8")})"
                            )
                            val mapIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                android.util.Log.d("EntryViewScreen", "Opening with Google Maps app")
                                context.startActivity(mapIntent)
                            } else {
                                // Fallback to browser
                                android.util.Log.d("EntryViewScreen", "Google Maps not found, opening in browser")
                                val browserUri = android.net.Uri.parse(
                                    "https://www.google.com/maps/search/?api=1&query=${loc.latitude},${loc.longitude}"
                                )
                                context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, browserUri))
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("EntryViewScreen", "Error opening maps: ${e.message}")
                            // Ultimate fallback
                            try {
                                val browserUri = android.net.Uri.parse(
                                    "https://www.google.com/maps/search/?api=1&query=${loc.latitude},${loc.longitude}"
                                )
                                context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, browserUri))
                            } catch (e2: Exception) {
                                android.util.Log.e("EntryViewScreen", "Failed to open browser: ${e2.message}")
                            }
                        }
                    }
                    
                    // Map preview - clickable to open in maps
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { openInMaps() }
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                MapView(ctx).apply {
                                    setTileSource(TileSourceFactory.MAPNIK)
                                    setMultiTouchControls(true)
                                    controller.setZoom(15.0)
                                    controller.setCenter(GeoPoint(loc.latitude, loc.longitude))
                                    
                                    // Add marker
                                    val marker = Marker(this).apply {
                                        position = GeoPoint(loc.latitude, loc.longitude)
                                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                        title = loc.placeName ?: "Location"
                                    }
                                    overlays.add(marker)
                                }
                            },
                            update = { mapView ->
                                mapView.controller.setCenter(GeoPoint(loc.latitude, loc.longitude))
                                mapView.invalidate()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { openInMaps() }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            loc.placeName?.let { name ->
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            loc.address?.let { address ->
                                Text(
                                    text = address,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "📍 ${String.format("%.4f", loc.latitude)}, ${String.format("%.4f", loc.longitude)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.OpenInNew,
                            contentDescription = "Open in maps",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LocationSection(location: Location) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                location.placeName?.let { name ->
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                location.address?.let { address ->
                    Text(
                        text = address,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = { /* Open in maps */ }) {
                Icon(
                    imageVector = Icons.Outlined.Map,
                    contentDescription = "Open in maps"
                )
            }
        }
    }
}

@Composable
private fun EntryFooter(entry: JournalEntry) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${entry.wordCount} words",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (entry.updatedAt != entry.createdAt) {
            Text(
                text = "Edited ${entry.updatedAt.format(DateTimeFormatter.ofPattern("MMM d, h:mm a"))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Modern UI Components

@Composable
private fun ModernEntryHeader(entry: JournalEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Date row with mood
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    // Large day number
                    Text(
                        text = entry.createdAt.dayOfMonth.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    // Month and year
                    Text(
                        text = entry.createdAt.format(
                            DateTimeFormatter.ofPattern("MMMM yyyy")
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // Day of week
                    Text(
                        text = entry.createdAt.format(
                            DateTimeFormatter.ofPattern("EEEE")
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Mood indicator (large and prominent)
                entry.mood?.let { mood ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = mood.emoji,
                                    style = MaterialTheme.typography.displaySmall
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = mood.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Time and category row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time chip
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = entry.createdAt.format(
                                DateTimeFormatter.ofPattern("h:mm a")
                            ),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                
                // Category chip
                entry.category?.let { category ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(category.color).copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(category.color), CircleShape)
                            )
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(category.color)
                            )
                        }
                    }
                }
                
                // Weather badge (if available)
                entry.weather?.let { weather ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AsyncImage(
                                model = weather.icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "${weather.temperature.toInt()}°",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ModernTagsSection(tags: List<Tag>) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Tag,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Tags",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = (tag.color?.let { Color(it) } ?: MaterialTheme.colorScheme.primary).copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "#",
                            style = MaterialTheme.typography.labelLarge,
                            color = tag.color?.let { Color(it) } ?: MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = tag.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ModernPeopleSection(people: List<Person>) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.People,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "People",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            people.forEach { person ->
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Avatar circle
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = person.name.take(1).uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                        Text(
                            text = person.name,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernEntryFooter(entry: JournalEntry) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.TextFields,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${entry.wordCount} words",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (entry.updatedAt != entry.createdAt) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Edited ${entry.updatedAt.format(DateTimeFormatter.ofPattern("MMM d, h:mm a"))}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun formatDuration(milliseconds: Long): String {
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
    }
}

/**
 * Fullscreen media viewer with swipe navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FullscreenMediaViewer(
    media: Media,
    allPhotos: List<Media>,
    initialIndex: Int,
    onDismiss: () -> Unit,
    onIndexChange: (Int) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(initialIndex) }
    
    // Use Dialog for fullscreen overlay
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Current photo
            if (media.type == MediaType.PHOTO && allPhotos.isNotEmpty()) {
                AsyncImage(
                    model = allPhotos.getOrNull(currentIndex)?.uri ?: media.uri,
                    contentDescription = allPhotos.getOrNull(currentIndex)?.caption,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                // Single photo or video
                AsyncImage(
                    model = media.uri,
                    contentDescription = media.caption,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                
                // Play button overlay for videos
                if (media.type == MediaType.VIDEO) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = "Play video",
                            modifier = Modifier.size(72.dp),
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            
            // Top bar with close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(8.dp)
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
                
                if (allPhotos.size > 1) {
                    Text(
                        text = "${currentIndex + 1} / ${allPhotos.size}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                // Placeholder for symmetry
                Spacer(modifier = Modifier.size(48.dp))
            }
            
            // Navigation buttons for multiple photos
            if (allPhotos.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous button
                    if (currentIndex > 0) {
                        IconButton(
                            onClick = {
                                currentIndex--
                                onIndexChange(currentIndex)
                            },
                            modifier = Modifier
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Previous",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(48.dp))
                    }
                    
                    // Next button
                    if (currentIndex < allPhotos.size - 1) {
                        IconButton(
                            onClick = {
                                currentIndex++
                                onIndexChange(currentIndex)
                            },
                            modifier = Modifier
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Next",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(48.dp))
                    }
                }
            }
            
            // Caption at bottom
            media.caption?.let { caption ->
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    Text(
                        text = caption,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
