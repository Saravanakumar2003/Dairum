package com.dairum.presentation.screens.files

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.dairum.R
import com.dairum.domain.model.MediaType
import com.dairum.domain.repository.MediaWithEntryInfo
import com.dairum.presentation.components.EmptyState
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(
    onPhotoClick: (Long) -> Unit,
    onVideoClick: (Long) -> Unit,
    onEntryClick: (Long) -> Unit,
    onLocationMapClick: () -> Unit,
    viewModel: FilesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var selectedTab by remember { mutableStateOf(FilesTab.PHOTOS) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_files)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab row
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilesTab.values().forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(tab.title)
                            }
                        }
                    )
                }
            }
            
            // Content
            when (selectedTab) {
                FilesTab.PHOTOS -> PhotosGrid(
                    photos = uiState.photos,
                    onPhotoClick = onPhotoClick,
                    onGoToEntry = onEntryClick
                )
                FilesTab.VIDEOS -> VideosGrid(
                    videos = uiState.videos,
                    onVideoClick = onVideoClick,
                    onGoToEntry = onEntryClick
                )
                FilesTab.VOICE -> VoiceList(
                    voiceNotes = uiState.voiceNotes,
                    onGoToEntry = onEntryClick
                )
                FilesTab.ATTACHMENTS -> AttachmentsList(
                    attachments = uiState.attachments,
                    onGoToEntry = onEntryClick
                )
                FilesTab.LOCATIONS -> LocationsList(
                    locations = uiState.locations,
                    onMapClick = onLocationMapClick,
                    onGoToEntry = onEntryClick
                )
            }
        }
    }
}

@Composable
private fun PhotosGrid(
    photos: List<MediaWithEntryInfo>,
    onPhotoClick: (Long) -> Unit,
    onGoToEntry: (Long) -> Unit
) {
    var selectedPhoto by remember { mutableStateOf<MediaWithEntryInfo?>(null) }
    var showPhotoViewer by remember { mutableStateOf(false) }
    
    if (photos.isEmpty()) {
        EmptyState(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Photo,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = stringResource(R.string.files_empty),
            message = "Photos from your journal entries will appear here"
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(photos) { photo ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.small)
                        .clickable { 
                            selectedPhoto = photo
                            showPhotoViewer = true
                        }
                ) {
                    AsyncImage(
                        model = photo.media.thumbnailUri ?: photo.media.uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
    
    // Photo Viewer Dialog
    if (showPhotoViewer && selectedPhoto != null) {
        PhotoViewerDialog(
            photo = selectedPhoto!!,
            onDismiss = { showPhotoViewer = false },
            onGoToEntry = { 
                showPhotoViewer = false
                onGoToEntry(selectedPhoto!!.media.entryId)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoViewerDialog(
    photo: MediaWithEntryInfo,
    onDismiss: () -> Unit,
    onGoToEntry: () -> Unit
) {
    val context = LocalContext.current
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column {
                // Photo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        model = photo.media.uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
                
                // Info and actions
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = photo.entryTitle ?: "Untitled Entry",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = formatDate(photo.entryCreatedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(onClick = onDismiss) {
                            Text("Close")
                        }
                        
                        OutlinedButton(onClick = {
                            downloadFile(context, photo.media.uri, photo.media.fileName ?: "photo.jpg")
                        }) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = onGoToEntry,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Article, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Go to Entry")
                    }
                }
            }
        }
    }
}

@Composable
private fun VideosGrid(
    videos: List<MediaWithEntryInfo>,
    onVideoClick: (Long) -> Unit,
    onGoToEntry: (Long) -> Unit
) {
    var selectedVideo by remember { mutableStateOf<MediaWithEntryInfo?>(null) }
    var showVideoViewer by remember { mutableStateOf(false) }
    
    if (videos.isEmpty()) {
        EmptyState(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Videocam,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = stringResource(R.string.files_empty),
            message = "Videos from your journal entries will appear here"
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(videos) { video ->
                VideoCard(
                    video = video,
                    onClick = {
                        selectedVideo = video
                        showVideoViewer = true
                    },
                    onGoToEntry = { onGoToEntry(video.media.entryId) }
                )
            }
        }
    }
    
    // Video Viewer Dialog
    if (showVideoViewer && selectedVideo != null) {
        VideoViewerDialog(
            video = selectedVideo!!,
            onDismiss = { showVideoViewer = false },
            onGoToEntry = { 
                showVideoViewer = false
                onGoToEntry(selectedVideo!!.media.entryId)
            }
        )
    }
}

@Composable
private fun VideoCard(
    video: MediaWithEntryInfo,
    onClick: () -> Unit,
    onGoToEntry: () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Column {
            Box(
                modifier = Modifier.aspectRatio(16f / 9f)
            ) {
                AsyncImage(
                    model = video.media.thumbnailUri ?: video.media.uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Play icon
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    tint = Color.White
                )
                
                // Duration badge
                video.media.duration?.let { duration ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = formatDuration(duration),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            // Entry info with go to entry button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.entryTitle ?: "Untitled",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formatDate(video.entryCreatedAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(
                    onClick = onGoToEntry,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = stringResource(R.string.files_go_to_entry),
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoViewerDialog(
    video: MediaWithEntryInfo,
    onDismiss: () -> Unit,
    onGoToEntry: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    
    // Create ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(video.media.uri))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }
    
    // Clean up player when dialog is dismissed
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    // Observe player state
    LaunchedEffect(exoPlayer) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
        })
    }
    
    AlertDialog(
        onDismissRequest = {
            exoPlayer.release()
            onDismiss()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column {
                // Video player
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Black)
                        .clickable {
                            if (isPlaying) {
                                exoPlayer.pause()
                            } else {
                                exoPlayer.play()
                            }
                        }
                ) {
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = exoPlayer
                                useController = true
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                // Info and actions
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = video.entryTitle ?: "Untitled Entry",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        video.media.duration?.let { duration ->
                            Text(
                                text = formatDuration(duration),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text("•", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = formatDate(video.entryCreatedAt),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(onClick = {
                            exoPlayer.release()
                            onDismiss()
                        }) {
                            Text("Close")
                        }
                        
                        OutlinedButton(onClick = {
                            downloadFile(context, video.media.uri, video.media.fileName ?: "video.mp4")
                        }) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            exoPlayer.release()
                            onGoToEntry()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Article, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Go to Entry")
                    }
                }
            }
        }
    }
}

@Composable
private fun VoiceList(
    voiceNotes: List<MediaWithEntryInfo>,
    onGoToEntry: (Long) -> Unit
) {
    if (voiceNotes.isEmpty()) {
        EmptyState(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = stringResource(R.string.files_empty),
            message = "Voice notes from your journal entries will appear here"
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(voiceNotes) { voice ->
                VoiceNoteItem(
                    voiceNote = voice,
                    onGoToEntry = { onGoToEntry(voice.media.entryId) }
                )
            }
        }
    }
}

@Composable
private fun VoiceNoteItem(
    voiceNote: MediaWithEntryInfo,
    onGoToEntry: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var player by remember { mutableStateOf<android.media.MediaPlayer?>(null) }
    
    DisposableEffect(voiceNote.media.uri) {
        onDispose {
            player?.release()
            player = null
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play button
            FilledIconButton(
                onClick = { 
                    if (isPlaying) {
                        player?.pause()
                        isPlaying = false
                    } else {
                        if (player == null) {
                            try {
                                player = android.media.MediaPlayer().apply {
                                    setDataSource(context, Uri.parse(voiceNote.media.uri))
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
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play"
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = voiceNote.entryTitle ?: "Audio File",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    voiceNote.media.duration?.let { duration ->
                        Text(
                            text = formatDuration(duration),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = formatDate(voiceNote.entryCreatedAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Download button
            IconButton(onClick = {
                downloadFile(context, voiceNote.media.uri, voiceNote.media.fileName ?: "audio.mp3")
            }) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download"
                )
            }
            
            // Go to entry
            IconButton(onClick = onGoToEntry) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = stringResource(R.string.files_go_to_entry)
                )
            }
        }
    }
}

@Composable
private fun AttachmentsList(
    attachments: List<MediaWithEntryInfo>,
    onGoToEntry: (Long) -> Unit
) {
    if (attachments.isEmpty()) {
        EmptyState(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AttachFile,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = stringResource(R.string.files_empty),
            message = "Attachments from your journal entries will appear here"
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(attachments) { attachment ->
                AttachmentItem(
                    attachment = attachment,
                    onGoToEntry = { onGoToEntry(attachment.media.entryId) }
                )
            }
        }
    }
}

@Composable
private fun AttachmentItem(
    attachment: MediaWithEntryInfo,
    onGoToEntry: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // File icon
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = getFileIcon(attachment.media.mimeType),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = attachment.media.fileName,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = formatFileSize(attachment.media.sizeBytes),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = formatDate(attachment.entryCreatedAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Download button
            IconButton(onClick = {
                downloadFile(context, attachment.media.uri, attachment.media.fileName)
            }) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download"
                )
            }
            
            // Go to entry
            IconButton(onClick = onGoToEntry) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = stringResource(R.string.files_go_to_entry)
                )
            }
        }
    }
}

@Composable
private fun LocationsList(
    locations: List<LocationWithEntries>,
    onMapClick: () -> Unit,
    onGoToEntry: (Long) -> Unit
) {
    // Group locations by area (city/region extracted from address)
    val groupedLocations = remember(locations) {
        locations.groupBy { locationWithEntries ->
            extractAreaName(locationWithEntries.location)
        }.toSortedMap()
    }
    
    if (locations.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Map button (still show even when no locations)
            Button(
                onClick = onMapClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Map, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("View All on Map")
            }
            
            EmptyState(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                title = stringResource(R.string.files_empty),
                message = "Locations from your journal entries will appear here"
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Map button
            item {
                Button(
                    onClick = onMapClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View All ${locations.size} Places on Map")
                }
            }
            
            // Statistics card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${groupedLocations.size}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Areas",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${locations.size}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Places",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${locations.sumOf { it.entries.size }}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Entries",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            // Grouped location sections
            groupedLocations.forEach { (areaName, areaLocations) ->
                item(key = "area_header_$areaName") {
                    LocationAreaHeader(
                        areaName = areaName,
                        placeCount = areaLocations.size,
                        entryCount = areaLocations.sumOf { it.entries.size }
                    )
                }
                
                items(
                    items = areaLocations,
                    key = { "location_${it.location.latitude}_${it.location.longitude}" }
                ) { locationWithEntries ->
                    LocationItem(
                        locationWithEntries = locationWithEntries,
                        onGoToEntry = onGoToEntry
                    )
                }
                
                item(key = "spacer_$areaName") {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Extract area name from location (city or region)
 */
private fun extractAreaName(location: com.dairum.domain.model.Location): String {
    // Try to extract city/area from address
    val address = location.address ?: location.placeName ?: ""
    
    // Common patterns: "Street, City, State" or "Place, City"
    val parts = address.split(",").map { it.trim() }
    
    return when {
        parts.size >= 2 -> {
            // Get second-to-last or last part as area name
            val cityPart = parts.getOrNull(parts.size - 2) ?: parts.last()
            cityPart.ifBlank { "Unknown Area" }
        }
        parts.isNotEmpty() -> parts.first().ifBlank { "Unknown Area" }
        else -> "Unknown Area"
    }
}

@Composable
private fun LocationAreaHeader(
    areaName: String,
    placeCount: Int,
    entryCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Place,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = areaName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "$placeCount places • $entryCount entries",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
                    
@Composable
private fun LocationItem(
    locationWithEntries: LocationWithEntries,
    onGoToEntry: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val geoPoint = remember { 
        GeoPoint(locationWithEntries.location.latitude, locationWithEntries.location.longitude) 
    }
    
    // Initialize OSMDroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Mini map preview using OSMDroid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            ) {
                AndroidView(
                    factory = { ctx ->
                        MapView(ctx).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(false)
                            controller.setZoom(15.0)
                            controller.setCenter(geoPoint)
                            
                            // Add marker
                            val marker = Marker(this).apply {
                                position = geoPoint
                                title = locationWithEntries.location.placeName 
                                    ?: locationWithEntries.location.address 
                                    ?: "Location"
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            }
                            overlays.add(marker)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Location icon with count badge
                Box {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = locationWithEntries.location.placeName 
                            ?: locationWithEntries.location.address 
                            ?: "Unknown Location",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    locationWithEntries.location.address?.let { address ->
                        if (address != locationWithEntries.location.placeName) {
                            Text(
                                text = address,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    
                    Text(
                        text = "${locationWithEntries.entries.size} entries",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Expanded section showing entries
            if (expanded) {
                HorizontalDivider()
                
                locationWithEntries.entries.take(5).forEach { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onGoToEntry(entry.id) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        entry.mood?.let { mood ->
                            Text(
                                text = mood.emoji,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = entry.title ?: "Untitled",
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = entry.createdAt.format(
                                    java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = stringResource(R.string.files_go_to_entry),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (locationWithEntries.entries.size > 5) {
                    Text(
                        text = "+${locationWithEntries.entries.size - 5} more entries",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

enum class FilesTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    PHOTOS("Photos", Icons.Outlined.Photo),
    VIDEOS("Videos", Icons.Outlined.Videocam),
    VOICE("Voice", Icons.Outlined.Mic),
    ATTACHMENTS("Files", Icons.Outlined.AttachFile),
    LOCATIONS("Locations", Icons.Outlined.LocationOn)
}

private fun formatDuration(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%d:%02d".format(minutes, remainingSeconds)
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> "%.1f MB".format(bytes / (1024.0 * 1024.0))
    }
}

private fun getFileIcon(mimeType: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when {
        mimeType.startsWith("application/pdf") -> Icons.Default.PictureAsPdf
        mimeType.startsWith("application/") && mimeType.contains("document") -> Icons.Default.Description
        mimeType.startsWith("application/") && mimeType.contains("sheet") -> Icons.Default.TableChart
        mimeType.startsWith("text/") -> Icons.Default.Article
        else -> Icons.Default.InsertDriveFile
    }
}

private fun downloadFile(context: android.content.Context, uri: String, fileName: String) {
    try {
        val sourceUri = android.net.Uri.parse(uri)
        
        // Handle different URI schemes
        val inputStream: java.io.InputStream? = when {
            // If it's a file:// URI or a direct file path
            sourceUri.scheme == "file" || sourceUri.scheme == null -> {
                val path = sourceUri.path ?: uri
                val file = java.io.File(path)
                if (file.exists()) {
                    java.io.FileInputStream(file)
                } else {
                    null
                }
            }
            // If it's a content:// URI, use ContentResolver
            sourceUri.scheme == "content" -> {
                context.contentResolver.openInputStream(sourceUri)
            }
            else -> {
                // Try as a direct file path
                val file = java.io.File(uri)
                if (file.exists()) {
                    java.io.FileInputStream(file)
                } else {
                    null
                }
            }
        }
        
        if (inputStream == null) {
            android.widget.Toast.makeText(
                context,
                "Failed to open file",
                android.widget.Toast.LENGTH_SHORT
            ).show()
            return
        }
        
        // Use MediaStore for Android 10+ (API 29)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // Save to Documents/Data Exports/Dairum folder using MediaStore
            val contentValues = android.content.ContentValues().apply {
                put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, "Dairum_$fileName")
                put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, 
                    android.os.Environment.DIRECTORY_DOCUMENTS + "/Data Exports/Dairum")
            }
            
            // Determine mime type
            val mimeType = if (sourceUri.scheme == "content") {
                context.contentResolver.getType(sourceUri) ?: "application/octet-stream"
            } else {
                // Guess mime type from extension
                val extension = fileName.substringAfterLast('.', "")
                when (extension.lowercase()) {
                    "pdf" -> "application/pdf"
                    "txt" -> "text/plain"
                    "jpg", "jpeg" -> "image/jpeg"
                    "png" -> "image/png"
                    "gif" -> "image/gif"
                    "mp4" -> "video/mp4"
                    "mp3" -> "audio/mpeg"
                    "doc", "docx" -> "application/msword"
                    "xls", "xlsx" -> "application/vnd.ms-excel"
                    else -> "application/octet-stream"
                }
            }
            contentValues.put(android.provider.MediaStore.MediaColumns.MIME_TYPE, mimeType)
            
            val collection = android.provider.MediaStore.Files.getContentUri("external")
            val newUri = context.contentResolver.insert(collection, contentValues)
            
            if (newUri != null) {
                context.contentResolver.openOutputStream(newUri)?.use { output ->
                    inputStream.use { input ->
                        input.copyTo(output)
                    }
                }
                android.widget.Toast.makeText(
                    context,
                    "Saved to Documents/Data Exports/Dairum: Dairum_$fileName",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            } else {
                inputStream.close()
                android.widget.Toast.makeText(
                    context,
                    "Failed to create file",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Legacy approach for older Android versions
            val documentsDir = android.os.Environment.getExternalStoragePublicDirectory(
                android.os.Environment.DIRECTORY_DOCUMENTS
            )
            val dairumDir = java.io.File(java.io.File(documentsDir, "Data Exports"), "Dairum").also { it.mkdirs() }
            val destFile = java.io.File(dairumDir, "Dairum_$fileName")
            
            inputStream.use { input ->
                java.io.FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            android.widget.Toast.makeText(
                context,
                "Saved to Documents/Data Exports/Dairum: ${destFile.name}",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        android.widget.Toast.makeText(
            context,
            "Failed to save file: ${e.message}",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}
