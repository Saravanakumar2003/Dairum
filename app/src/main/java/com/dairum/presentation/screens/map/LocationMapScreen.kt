package com.dairum.presentation.screens.map

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Point
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.Location
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMapScreen(
    onEntryClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: LocationMapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf<LocationWithEntries?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var mapViewMode by remember { mutableStateOf(MapViewMode.MAP) }
    
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    
    // Initialize OSMDroid Configuration
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Location Map")
                        Text(
                            text = "${uiState.locations.size} places • ${uiState.totalEntries} entries",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Toggle between map and list view
                    IconButton(onClick = { 
                        mapViewMode = if (mapViewMode == MapViewMode.MAP) 
                            MapViewMode.LIST else MapViewMode.MAP 
                    }) {
                        Icon(
                            imageVector = if (mapViewMode == MapViewMode.MAP) 
                                Icons.Default.List else Icons.Default.Map,
                            contentDescription = "Toggle view"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            uiState.locations.isEmpty() -> {
                EmptyLocationState(modifier = Modifier.padding(paddingValues))
            }
            
            mapViewMode == MapViewMode.LIST -> {
                LocationListView(
                    locations = uiState.locations,
                    onLocationClick = { location ->
                        selectedLocation = location
                        showBottomSheet = true
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // OSMDroid MapView using AndroidView
                    AndroidView(
                        factory = { ctx ->
                            MapView(ctx).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(true)
                                controller.setZoom(12.0)
                                
                                // Center on first location if available
                                uiState.locations.firstOrNull()?.let { loc ->
                                    controller.setCenter(
                                        GeoPoint(loc.location.latitude, loc.location.longitude)
                                    )
                                }
                            }
                        },
                        update = { mapView ->
                            // Clear existing markers
                            mapView.overlays.clear()
                            
                            // Add markers for each location
                            uiState.locations.forEach { locationWithEntries ->
                                val marker = Marker(mapView).apply {
                                    position = GeoPoint(
                                        locationWithEntries.location.latitude,
                                        locationWithEntries.location.longitude
                                    )
                                    title = locationWithEntries.location.placeName 
                                        ?: locationWithEntries.location.address 
                                        ?: "Location"
                                    snippet = "${locationWithEntries.entries.size} entries"
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    setOnMarkerClickListener { _, _ ->
                                        selectedLocation = locationWithEntries
                                        showBottomSheet = true
                                        true
                                    }
                                }
                                mapView.overlays.add(marker)
                            }
                            
                            // Refresh the map
                            mapView.invalidate()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Quick stats card
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StatItem(
                                icon = Icons.Outlined.Place,
                                value = "${uiState.locations.size}",
                                label = "Places"
                            )
                            StatItem(
                                icon = Icons.Outlined.EditNote,
                                value = "${uiState.totalEntries}",
                                label = "Entries"
                            )
                        }
                    }
                }
            }
        }
        
        // Bottom sheet for selected location
        if (showBottomSheet && selectedLocation != null) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }
            ) {
                LocationEntriesSheet(
                    locationWithEntries = selectedLocation!!,
                    onEntryClick = { entryId ->
                        showBottomSheet = false
                        onEntryClick(entryId)
                    },
                    onClose = { showBottomSheet = false }
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyLocationState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOff,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No Locations Yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Add locations to your journal entries to see them on the map. " +
                   "Track your memories across different places!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun LocationListView(
    locations: List<LocationWithEntries>,
    onLocationClick: (LocationWithEntries) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(locations) { locationWithEntries ->
            LocationCard(
                locationWithEntries = locationWithEntries,
                onClick = { onLocationClick(locationWithEntries) }
            )
        }
    }
}

@Composable
private fun LocationCard(
    locationWithEntries: LocationWithEntries,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location icon with entry count badge
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
                
                // Entry count badge
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp)
                ) {
                    Text("${locationWithEntries.entries.size}")
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
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Show latest entry date
                locationWithEntries.entries.maxByOrNull { it.createdAt }?.let { latestEntry ->
                    Text(
                        text = "Latest: ${latestEntry.createdAt.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}",
                        style = MaterialTheme.typography.labelSmall,
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

@Composable
private fun LocationEntriesSheet(
    locationWithEntries: LocationWithEntries,
    onEntryClick: (Long) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = locationWithEntries.location.placeName 
                        ?: locationWithEntries.location.address 
                        ?: "Location",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${locationWithEntries.entries.size} entries at this location",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        HorizontalDivider()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Entries list
        LazyColumn(
            modifier = Modifier.heightIn(max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(locationWithEntries.entries) { entry ->
                EntryListItem(
                    entry = entry,
                    onClick = { onEntryClick(entry.id) }
                )
            }
        }
    }
}

@Composable
private fun EntryListItem(
    entry: JournalEntry,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mood indicator or date
            entry.mood?.let { mood ->
                Text(
                    text = mood.emoji,
                    style = MaterialTheme.typography.headlineSmall
                )
            } ?: run {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = entry.createdAt.dayOfMonth.toString(),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.title ?: "Untitled",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = entry.createdAt.format(
                        DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (entry.plainContent.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.plainContent.take(100),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private enum class MapViewMode {
    MAP, LIST
}

data class LocationWithEntries(
    val location: Location,
    val entries: List<JournalEntry>
)
