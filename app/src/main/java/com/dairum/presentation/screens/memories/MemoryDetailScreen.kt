package com.dairum.presentation.screens.memories

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dairum.domain.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
@Suppress("UNUSED_PARAMETER")
fun MemoryDetailScreen(
    memoryId: Long,
    onBackClick: () -> Unit,
    onEntryClick: (Long) -> Unit,
    onPhotoClick: (Long) -> Unit = {},
    viewModel: MemoryDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showAddMilestoneDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.memory?.title ?: "Memory",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        uiState.memory?.let { memory ->
                            Text(
                                text = formatDateRange(memory.startDate, memory.endDate),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    uiState.memory?.let { memory ->
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                if (memory.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (memory.isFavorite) MaterialTheme.colorScheme.error else LocalContentColor.current
                            )
                        }
                    }
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                    }
                },
                scrollBehavior = scrollBehavior
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
            
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.Error,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(uiState.error!!)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onBackClick) {
                            Text("Go Back")
                        }
                    }
                }
            }
            
            uiState.memory != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Tab Row
                    ScrollableTabRow(
                        selectedTabIndex = uiState.activeTab.ordinal,
                        edgePadding = 16.dp
                    ) {
                        MemoryTab.values().forEach { tab ->
                            Tab(
                                selected = uiState.activeTab == tab,
                                onClick = { viewModel.setActiveTab(tab) },
                                text = {
                                    Text(
                                        when (tab) {
                                            MemoryTab.OVERVIEW -> "Overview"
                                            MemoryTab.ENTRIES -> "Entries (${uiState.entries.size})"
                                            MemoryTab.GALLERY -> "Gallery (${uiState.photos.size})"
                                            MemoryTab.INSIGHTS -> "Insights"
                                            MemoryTab.TIMELINE -> "Timeline"
                                        }
                                    )
                                },
                                icon = {
                                    Icon(
                                        when (tab) {
                                            MemoryTab.OVERVIEW -> Icons.Outlined.Dashboard
                                            MemoryTab.ENTRIES -> Icons.AutoMirrored.Outlined.Article
                                            MemoryTab.GALLERY -> Icons.Outlined.PhotoLibrary
                                            MemoryTab.INSIGHTS -> Icons.Outlined.Insights
                                            MemoryTab.TIMELINE -> Icons.Outlined.Timeline
                                        },
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                    
                    // Tab Content
                    when (uiState.activeTab) {
                        MemoryTab.OVERVIEW -> OverviewTab(
                            memory = uiState.memory!!,
                            entries = uiState.entries,
                            insights = uiState.insights,
                            people = uiState.people,
                            locations = uiState.locations,
                            photos = uiState.photos,
                            onEntryClick = onEntryClick,
                            onPhotoClick = onPhotoClick
                        )
                        MemoryTab.ENTRIES -> EntriesTab(
                            entriesByDate = uiState.entriesByDate,
                            availableEntries = uiState.availableEntries,
                            onEntryClick = onEntryClick,
                            onRemoveEntry = viewModel::removeEntry,
                            onAddEntries = viewModel::addEntries,
                            onLoadAvailableEntries = viewModel::loadAvailableEntries
                        )
                        MemoryTab.GALLERY -> GalleryTab(
                            photos = uiState.photos,
                            onPhotoClick = onPhotoClick
                        )
                        MemoryTab.INSIGHTS -> InsightsTab(
                            insights = uiState.insights,
                            memory = uiState.memory!!,
                            moodJourney = uiState.moodJourney,
                            tags = uiState.tags
                        )
                        MemoryTab.TIMELINE -> TimelineTab(
                            memory = uiState.memory!!,
                            entries = uiState.entries,
                            milestones = uiState.memory!!.milestones,
                            onEntryClick = onEntryClick,
                            onAddMilestone = { showAddMilestoneDialog = true },
                            onDeleteMilestone = viewModel::deleteMilestone
                        )
                    }
                }
            }
        }
    }
    
    // Delete Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Memory") },
            text = { 
                Text("Are you sure you want to delete this memory? The journal entries will not be deleted.") 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteMemory()
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
    
    // Edit Dialog
    if (showEditDialog && uiState.memory != null) {
        EditMemoryDialog(
            memory = uiState.memory!!,
            onDismiss = { showEditDialog = false },
            onSave = { title, description ->
                viewModel.updateMemory(title, description)
                showEditDialog = false
            }
        )
    }
    
    // Add Milestone Dialog
    if (showAddMilestoneDialog) {
        AddMilestoneDialog(
            onDismiss = { showAddMilestoneDialog = false },
            onAdd = { title, description, date ->
                viewModel.addMilestone(title, description, date)
                showAddMilestoneDialog = false
            }
        )
    }
}

@Composable
private fun OverviewTab(
    memory: Memory,
    entries: List<JournalEntry>,
    insights: MemoryInsights?,
    people: List<Person>,
    locations: List<Location>,
    photos: List<Media>,
    onEntryClick: (Long) -> Unit,
    onPhotoClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Description
        if (!memory.description.isNullOrBlank()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = memory.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        
        // Quick Stats
        item {
            QuickStatsCard(memory = memory, insights = insights)
        }
        
        // Photo Preview
        if (photos.isNotEmpty()) {
            item {
                PhotoPreviewSection(
                    photos = photos.take(6),
                    totalCount = photos.size,
                    onPhotoClick = onPhotoClick
                )
            }
        }
        
        // Mood Journey Preview
        if (insights?.avgMood != null) {
            item {
                MoodSummaryCard(insights = insights)
            }
        }
        
        // People Involved
        if (people.isNotEmpty()) {
            item {
                PeopleSection(people = people)
            }
        }
        
        // Locations
        if (locations.isNotEmpty()) {
            item {
                LocationsSection(locations = locations)
            }
        }
        
        // Highlights
        if (memory.highlights.isNotEmpty()) {
            item {
                HighlightsCard(highlights = memory.highlights)
            }
        }
        
        // Recent Entries Preview
        if (entries.isNotEmpty()) {
            item {
                Text(
                    text = "Recent Entries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(entries.take(3)) { entry ->
                EntryPreviewCard(
                    entry = entry,
                    onClick = { onEntryClick(entry.id) }
                )
            }
        }
        
        // Bottom spacing for navigation bar
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun QuickStatsCard(
    memory: Memory,
    insights: MemoryInsights?
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "At a Glance",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickStatItem(
                    value = memory.entryCount.toString(),
                    label = "Entries",
                    icon = Icons.AutoMirrored.Outlined.Article
                )
                QuickStatItem(
                    value = formatNumber(memory.wordCount),
                    label = "Words",
                    icon = Icons.Outlined.TextFields
                )
                QuickStatItem(
                    value = memory.photoCount.toString(),
                    label = "Photos",
                    icon = Icons.Outlined.Photo
                )
                QuickStatItem(
                    value = memory.duration,
                    label = "Duration",
                    icon = Icons.Outlined.CalendarToday
                )
            }
            
            if (insights != null) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    insights.avgMood?.let { mood ->
                        QuickStatItem(
                            value = String.format("%.1f", mood),
                            label = "Avg Mood",
                            icon = Icons.Outlined.Mood
                        )
                    }
                    QuickStatItem(
                        value = "${insights.writingFrequency.toInt()}%",
                        label = "Active Days",
                        icon = Icons.AutoMirrored.Outlined.TrendingUp
                    )
                    QuickStatItem(
                        value = insights.longestStreak.toString(),
                        label = "Best Streak",
                        icon = Icons.Outlined.LocalFireDepartment
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickStatItem(
    value: String,
    label: String,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun PhotoPreviewSection(
    photos: List<Media>,
    totalCount: Int,
    onPhotoClick: (Long) -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Photos",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                if (totalCount > 6) {
                    TextButton(onClick = { }) {
                        Text("View All ($totalCount)")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(photos) { photo ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photo.uri)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onPhotoClick(photo.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodSummaryCard(insights: MemoryInsights) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Mood,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Emotional Journey",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getMoodEmoji(insights.avgMood?.toInt() ?: 3),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Average",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                    )
                }
                
                insights.moodVariability?.let { variability ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when {
                                variability < 0.5f -> "Stable"
                                variability < 1.0f -> "Varied"
                                else -> "Dynamic"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Emotional Range",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
                
                insights.bestMoodEntry?.let { entry ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = entry.createdAt.format(DateTimeFormatter.ofPattern("MMM d")),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Best Day",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PeopleSection(people: List<Person>) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.People, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "People in This Memory",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(people) { person ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(60.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.size(48.dp)
                        ) {
                            if (person.photoUri != null) {
                                AsyncImage(
                                    model = person.photoUri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = person.name.first().uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = person.name,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationsSection(locations: List<Location>) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Places Visited",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            locations.take(5).forEach { location ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Place,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = location.placeName ?: location.address ?: "Unknown Location",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            if (locations.size > 5) {
                Text(
                    text = "+${locations.size - 5} more locations",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun HighlightsCard(highlights: List<String>) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Memory Highlights",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            highlights.forEach { highlight ->
                Text(
                    text = "• $highlight",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntryPreviewCard(
    entry: JournalEntry,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Date badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = entry.createdAt.dayOfMonth.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = entry.createdAt.format(DateTimeFormatter.ofPattern("MMM")),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.title ?: entry.createdAt.format(DateTimeFormatter.ofPattern("EEEE")),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    entry.mood?.let { mood ->
                        Text(
                            text = mood.emoji,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
                
                if (entry.plainContent.isNotBlank()) {
                    Text(
                        text = entry.plainContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (entry.media.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Photo,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${entry.media.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Text(
                        text = "${entry.wordCount} words",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EntriesTab(
    entriesByDate: Map<LocalDate, List<JournalEntry>>,
    availableEntries: List<JournalEntry>,
    onEntryClick: (Long) -> Unit,
    onRemoveEntry: (Long) -> Unit,
    onAddEntries: (List<Long>) -> Unit,
    onLoadAvailableEntries: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        if (entriesByDate.isEmpty()) {
            EmptyStateMessage(
                icon = Icons.AutoMirrored.Outlined.Article,
                title = "No Entries Yet",
                message = "This memory doesn't have any journal entries."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                entriesByDate.forEach { (date, entries) ->
                    item(key = "date_$date") {
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(
                        items = entries,
                        key = { it.id }
                    ) { entry ->
                        SwipeableEntryCard(
                            entry = entry,
                            onClick = { onEntryClick(entry.id) },
                            onRemove = { onRemoveEntry(entry.id) }
                        )
                    }
                }
            }
        }
        
        // Floating Action Button for adding entries
        FloatingActionButton(
            onClick = { 
                onLoadAvailableEntries()
                showAddDialog = true 
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Entries"
            )
        }
    }
    
    // Add Entries Dialog
    if (showAddDialog) {
        AddEntriesDialog(
            availableEntries = availableEntries,
            onDismiss = { showAddDialog = false },
            onConfirm = { selectedIds ->
                onAddEntries(selectedIds)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEntriesDialog(
    availableEntries: List<JournalEntry>,
    onDismiss: () -> Unit,
    onConfirm: (List<Long>) -> Unit
) {
    var selectedEntryIds by remember { mutableStateOf(setOf<Long>()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text("Add Entries to Memory") 
        },
        text = {
            Column {
                if (availableEntries.isEmpty()) {
                    Text(
                        text = "No entries available in this date range that aren't already in this memory.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Select entries to add:",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(availableEntries) { entry ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedEntryIds = if (entry.id in selectedEntryIds) {
                                            selectedEntryIds - entry.id
                                        } else {
                                            selectedEntryIds + entry.id
                                        }
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = entry.id in selectedEntryIds,
                                    onCheckedChange = { checked ->
                                        selectedEntryIds = if (checked) {
                                            selectedEntryIds + entry.id
                                        } else {
                                            selectedEntryIds - entry.id
                                        }
                                    }
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        entry.mood?.let { mood ->
                                            Text(
                                                text = mood.emoji,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                        Text(
                                            text = entry.title ?: entry.createdAt.format(
                                                DateTimeFormatter.ofPattern("MMM d, h:mm a")
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Text(
                                        text = entry.plainContent.take(50) + if (entry.plainContent.length > 50) "..." else "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedEntryIds.toList()) },
                enabled = selectedEntryIds.isNotEmpty()
            ) {
                Text("Add (${selectedEntryIds.size})")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableEntryCard(
    entry: JournalEntry,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    entry.mood?.let { mood ->
                        Text(
                            text = mood.emoji,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    
                    Text(
                        text = entry.title ?: entry.createdAt.format(DateTimeFormatter.ofPattern("h:mm a")),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                if (entry.plainContent.isNotBlank()) {
                    Text(
                        text = entry.plainContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${entry.wordCount} words",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (entry.media.isNotEmpty()) {
                        Text(
                            text = "• ${entry.media.size} photos",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            IconButton(onClick = { showRemoveDialog = true }) {
                Icon(
                    Icons.Outlined.RemoveCircleOutline,
                    contentDescription = "Remove from memory",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
    
    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Remove Entry") },
            text = { Text("Remove this entry from the memory? The entry itself will not be deleted.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemove()
                        showRemoveDialog = false
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun GalleryTab(
    photos: List<Media>,
    onPhotoClick: (Long) -> Unit
) {
    if (photos.isEmpty()) {
        EmptyStateMessage(
            icon = Icons.Outlined.PhotoLibrary,
            title = "No Photos",
            message = "This memory doesn't have any photos yet."
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(photos) { photo ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photo.uri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onPhotoClick(photo.id) }
                )
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
@Suppress("UNUSED_PARAMETER")
private fun InsightsTab(
    insights: MemoryInsights?,
    memory: Memory,
    moodJourney: List<MoodPoint>, // Reserved for future mood chart visualization
    tags: List<Tag>
) {
    if (insights == null) {
        EmptyStateMessage(
            icon = Icons.Outlined.Insights,
            title = "No Insights Yet",
            message = "Add more entries to see insights about this memory."
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Writing Activity
            item {
                InsightCard(
                    title = "Writing Activity",
                    icon = Icons.Outlined.Edit
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InsightRow(
                            label = "Total Words Written",
                            value = formatNumber(insights.totalWords)
                        )
                        InsightRow(
                            label = "Average Entry Length",
                            value = "${insights.avgWordCount} words"
                        )
                        InsightRow(
                            label = "Writing Frequency",
                            value = "${insights.writingDays} of ${insights.totalDays} days (${insights.writingFrequency.toInt()}%)"
                        )
                        InsightRow(
                            label = "Longest Writing Streak",
                            value = "${insights.longestStreak} days"
                        )
                        insights.mostActiveHour?.let { hour ->
                            InsightRow(
                                label = "Most Active Writing Time",
                                value = formatHour(hour)
                            )
                        }
                        insights.mostProductiveDay?.let { day ->
                            InsightRow(
                                label = "Most Productive Day",
                                value = day.lowercase().replaceFirstChar { it.uppercase() }
                            )
                        }
                    }
                }
            }
            
            // Emotional Insights
            if (insights.avgMood != null) {
                item {
                    InsightCard(
                        title = "Emotional Insights",
                        icon = Icons.Outlined.Psychology
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Average Mood", style = MaterialTheme.typography.bodyMedium)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = getMoodEmoji(insights.avgMood.toInt()),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = getMoodLabel(insights.avgMood),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            
                            insights.moodVariability?.let { variability ->
                                InsightRow(
                                    label = "Emotional Stability",
                                    value = when {
                                        variability < 0.5f -> "Very Stable"
                                        variability < 1.0f -> "Moderately Varied"
                                        variability < 1.5f -> "Dynamic"
                                        else -> "Highly Variable"
                                    }
                                )
                            }
                            
                            insights.bestMoodEntry?.let { entry ->
                                InsightRow(
                                    label = "Happiest Day",
                                    value = entry.createdAt.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
                                )
                            }
                        }
                    }
                }
            }
            
            // Media Insights
            if (insights.photoCount > 0) {
                item {
                    InsightCard(
                        title = "Media Captured",
                        icon = Icons.Outlined.CameraAlt
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            InsightRow(
                                label = "Total Photos",
                                value = "${insights.photoCount}"
                            )
                            InsightRow(
                                label = "Entries with Photos",
                                value = "${insights.entriesWithPhotos}"
                            )
                            InsightRow(
                                label = "Unique Locations",
                                value = "${insights.uniqueLocations}"
                            )
                        }
                    }
                }
            }
            
            // Tags Used
            if (tags.isNotEmpty()) {
                item {
                    InsightCard(
                        title = "Topics & Tags",
                        icon = Icons.Outlined.LocalOffer
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            tags.forEach { tag ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(tag.name) }
                                )
                            }
                        }
                    }
                }
            }
            
            // Summary Card
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Memory Summary",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = buildMemorySummary(memory, insights),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun InsightCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            content()
        }
    }
}

@Composable
private fun InsightRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TimelineTab(
    memory: Memory,
    entries: List<JournalEntry>,
    milestones: List<MemoryMilestone>,
    onEntryClick: (Long) -> Unit,
    onAddMilestone: () -> Unit,
    onDeleteMilestone: (Long) -> Unit
) {
    // Combine entries and milestones into timeline items
    data class TimelineItem(
        val date: LocalDateTime,
        val type: String, // "entry" or "milestone"
        val entry: JournalEntry? = null,
        val milestone: MemoryMilestone? = null
    )
    
    val timelineItems = (
        entries.map { TimelineItem(it.createdAt, "entry", entry = it) } +
        milestones.map { TimelineItem(it.milestoneDate, "milestone", milestone = it) }
    ).sortedBy { it.date }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Add milestone button
        item {
            OutlinedButton(
                onClick = onAddMilestone,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Milestone")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Timeline header - start date
        item {
            TimelineMarker(
                title = "Memory Started",
                subtitle = memory.startDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                isStart = true
            )
        }
        
        // Timeline items
        items(timelineItems) { item ->
            Row(modifier = Modifier.fillMaxWidth()) {
                // Timeline line
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(80.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .fillMaxHeight()
                            .align(Alignment.Center)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(
                                if (item.type == "milestone") 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                            .align(Alignment.Center)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Content
                when (item.type) {
                    "entry" -> item.entry?.let { entry ->
                        Card(
                            onClick = { onEntryClick(entry.id) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = entry.createdAt.format(DateTimeFormatter.ofPattern("MMM d, h:mm a")),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    entry.mood?.let { mood ->
                                        Text(mood.emoji)
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = entry.title ?: "Journal Entry",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                    "milestone" -> item.milestone?.let { milestone ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = milestone.milestoneDate.format(DateTimeFormatter.ofPattern("MMM d")),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = milestone.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    milestone.description?.let { desc ->
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { onDeleteMilestone(milestone.id) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.Close,
                                        contentDescription = "Delete milestone",
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Timeline footer - end date
        item {
            TimelineMarker(
                title = "Memory Ended",
                subtitle = memory.endDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                isEnd = true
            )
        }
        
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun TimelineMarker(
    title: String,
    subtitle: String,
    isStart: Boolean = false,
    isEnd: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(
                        if (isStart) MaterialTheme.colorScheme.primary
                        else if (isEnd) MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.outline
                    )
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (isStart || isEnd) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyStateMessage(
    icon: ImageVector,
    title: String,
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditMemoryDialog(
    memory: Memory,
    onDismiss: () -> Unit,
    onSave: (String, String?) -> Unit
) {
    var title by remember { mutableStateOf(memory.title) }
    var description by remember { mutableStateOf(memory.description ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Memory") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title, description.takeIf { it.isNotBlank() }) },
                enabled = title.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMilestoneDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String?, LocalDateTime) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDateTime.now()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Milestone") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Milestone Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Date: ${date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onAdd(title, description.takeIf { it.isNotBlank() }, date) },
                enabled = title.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Helper functions
private fun formatDateRange(start: LocalDateTime, end: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return if (start.year == end.year && start.month == end.month) {
        if (start.dayOfMonth == end.dayOfMonth) {
            start.format(formatter)
        } else {
            "${start.dayOfMonth} - ${end.format(formatter)}"
        }
    } else {
        "${start.format(formatter)} - ${end.format(formatter)}"
    }
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}

private fun formatHour(hour: Int): String {
    return when {
        hour == 0 -> "12 AM"
        hour < 12 -> "$hour AM"
        hour == 12 -> "12 PM"
        else -> "${hour - 12} PM"
    }
}

private fun getMoodEmoji(level: Int): String {
    return when (level) {
        1 -> "😢"
        2 -> "😔"
        3 -> "😐"
        4 -> "🙂"
        5 -> "😄"
        else -> "😐"
    }
}

private fun getMoodLabel(avgMood: Float): String {
    return when {
        avgMood < 1.5f -> "Difficult Period"
        avgMood < 2.5f -> "Challenging"
        avgMood < 3.5f -> "Balanced"
        avgMood < 4.5f -> "Good Times"
        else -> "Wonderful"
    }
}

private fun buildMemorySummary(memory: Memory, insights: MemoryInsights): String {
    val parts = mutableListOf<String>()
    
    parts.add("This ${memory.duration} memory spans from ${memory.startDate.format(DateTimeFormatter.ofPattern("MMMM d"))} to ${memory.endDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}.")
    
    if (insights.totalWords > 0) {
        parts.add("You wrote ${formatNumber(insights.totalWords)} words across ${memory.entryCount} entries.")
    }
    
    insights.avgMood?.let { mood ->
        val moodDesc = getMoodLabel(mood)
        parts.add("Overall, this was a $moodDesc.")
    }
    
    if (insights.longestStreak > 1) {
        parts.add("Your longest writing streak was ${insights.longestStreak} consecutive days.")
    }
    
    if (insights.photoCount > 0) {
        parts.add("You captured ${insights.photoCount} photos to remember these moments.")
    }
    
    if (insights.uniqueLocations > 1) {
        parts.add("This memory took place across ${insights.uniqueLocations} different locations.")
    }
    
    return parts.joinToString(" ")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}
