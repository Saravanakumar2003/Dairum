package com.dairum.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.R
import com.dairum.domain.model.Location
import com.dairum.presentation.components.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onEntryClick: (Long) -> Unit,
    onNewEntry: () -> Unit,
    onCategoryClick: (Long) -> Unit,
    onTagClick: (Long) -> Unit,
    onMoodClick: (Int) -> Unit,
    onStoryClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCreateMemoryWithEntries: (title: String, description: String?, entryIds: List<Long>) -> Unit = { _, _, _ -> },
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val viewMode by viewModel.viewMode.collectAsState()
    
    var showViewModeMenu by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showCreateMemoryDialog by remember { mutableStateOf(false) }
    var showAddToMemoryDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    
    // Show export result snackbar and trigger share
    LaunchedEffect(uiState.exportFilePath) {
        uiState.exportFilePath?.let { path ->
            val file = java.io.File(path)
            if (file.exists()) {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
            } else {
                // Try as content URI
                try {
                    val uri = Uri.parse(path)
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
                } catch (_: Exception) {
                    snackbarHostState.showSnackbar("PDF exported but could not share")
                }
            }
            viewModel.clearExportResult()
        }
    }
    
    // Reset selection mode when navigating away
    LaunchedEffect(viewMode) {
        if (uiState.isSelectionMode) {
            viewModel.clearSelection()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            when {
                uiState.isSelectionMode -> {
                    // Selection mode top bar
                    TopAppBar(
                        title = {
                            Text("${uiState.selectedEntryIds.size} selected")
                        },
                        navigationIcon = {
                            IconButton(onClick = { viewModel.clearSelection() }) {
                                Icon(Icons.Default.Close, contentDescription = "Cancel selection")
                            }
                        },
                        actions = {
                            // Select all
                            IconButton(onClick = { viewModel.selectAllEntries() }) {
                                Icon(Icons.Outlined.SelectAll, contentDescription = "Select all")
                            }
                            // Add to existing memory
                            IconButton(
                                onClick = { showAddToMemoryDialog = true },
                                enabled = uiState.selectedEntryIds.isNotEmpty() && uiState.memories.isNotEmpty()
                            ) {
                                Icon(Icons.Outlined.Bookmark, contentDescription = "Add to Memory")
                            }
                            // Create new memory
                            IconButton(
                                onClick = { showCreateMemoryDialog = true },
                                enabled = uiState.selectedEntryIds.isNotEmpty()
                            ) {
                                Icon(Icons.Outlined.CollectionsBookmark, contentDescription = "Create Memory")
                            }
                            // Export to PDF
                            IconButton(
                                onClick = { viewModel.exportSelectedToPdf() },
                                enabled = uiState.selectedEntryIds.isNotEmpty() && !uiState.isExporting
                            ) {
                                if (uiState.isExporting) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(Icons.Outlined.PictureAsPdf, contentDescription = "Export PDF")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
                isSearchActive -> {
                    // Search bar
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { 
                            searchQuery = it
                            viewModel.searchEntries(it)
                        },
                        onSearch = { 
                            viewModel.searchEntries(searchQuery)
                        },
                        active = isSearchActive,
                        onActiveChange = { isSearchActive = it },
                        leadingIcon = {
                            IconButton(onClick = { 
                                isSearchActive = false 
                                searchQuery = ""
                                viewModel.clearSearch()
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { 
                                    searchQuery = ""
                                    viewModel.clearSearch()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        placeholder = { Text("Search journal entries...") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Search suggestions
                        if (searchQuery.isNotEmpty() && uiState.entries.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(uiState.entries.take(5)) { entry ->
                                    ListItem(
                                        headlineContent = { Text(entry.title ?: "Untitled") },
                                        supportingContent = { 
                                            Text(
                                                entry.plainContent.take(100),
                                                maxLines = 2
                                            ) 
                                        },
                                        leadingContent = {
                                            entry.mood?.let { 
                                                Text(it.emoji, style = MaterialTheme.typography.titleLarge) 
                                            }
                                        },
                                        modifier = Modifier.combinedClickable(
                                            onClick = { 
                                                isSearchActive = false
                                                onEntryClick(entry.id) 
                                            }
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "Dairum",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d")),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        actions = {
                            // Refresh button
                            IconButton(
                                onClick = { viewModel.refresh() },
                                enabled = !uiState.isRefreshing
                            ) {
                                if (uiState.isRefreshing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh"
                                    )
                                }
                            }
                            
                            IconButton(onClick = { isSearchActive = true }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.action_search)
                                )
                            }
                    
                            Box {
                                IconButton(onClick = { showViewModeMenu = true }) {
                                    Icon(
                                        imageVector = when (viewMode) {
                                            ViewMode.TIMELINE -> Icons.Outlined.ViewAgenda
                                            ViewMode.CALENDAR -> Icons.Outlined.CalendarMonth
                                            ViewMode.CATEGORY -> Icons.Outlined.Folder
                                            ViewMode.TAG -> Icons.Outlined.Tag
                                            ViewMode.MOOD -> Icons.Outlined.Mood
                                            ViewMode.PEOPLE -> Icons.Outlined.People
                                            ViewMode.LOCATION -> Icons.Outlined.LocationOn
                                            ViewMode.STORY -> Icons.Outlined.AutoStories
                                        },
                                        contentDescription = "View mode"
                                    )
                                }
                        
                                DropdownMenu(
                                    expanded = showViewModeMenu,
                                    onDismissRequest = { showViewModeMenu = false }
                                ) {
                                    ViewMode.values().forEach { mode ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    when (mode) {
                                                        ViewMode.TIMELINE -> stringResource(R.string.view_timeline)
                                                        ViewMode.CALENDAR -> "Calendar"
                                                        ViewMode.CATEGORY -> stringResource(R.string.view_category)
                                                        ViewMode.TAG -> stringResource(R.string.view_tag)
                                                        ViewMode.MOOD -> stringResource(R.string.view_mood)
                                                        ViewMode.PEOPLE -> "People"
                                                        ViewMode.LOCATION -> "Location"
                                                        ViewMode.STORY -> stringResource(R.string.view_story)
                                                    }
                                                )
                                            },
                                            onClick = {
                                                if (mode == ViewMode.STORY) {
                                                    onStoryClick()
                                                } else {
                                                    viewModel.setViewMode(mode)
                                                }
                                                showViewModeMenu = false
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = when (mode) {
                                                        ViewMode.TIMELINE -> Icons.Outlined.ViewAgenda
                                                        ViewMode.CALENDAR -> Icons.Outlined.CalendarMonth
                                                        ViewMode.CATEGORY -> Icons.Outlined.Folder
                                                        ViewMode.TAG -> Icons.Outlined.Tag
                                                        ViewMode.MOOD -> Icons.Outlined.Mood
                                                        ViewMode.PEOPLE -> Icons.Outlined.People
                                                        ViewMode.LOCATION -> Icons.Outlined.LocationOn
                                                        ViewMode.STORY -> Icons.Outlined.AutoStories
                                                    },
                                                    contentDescription = null
                                                )
                                            },
                                            trailingIcon = if (mode == viewMode) {
                                                {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null
                                                    )
                                                }
                                            } else null
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        val pullToRefreshState = rememberPullToRefreshState()
        
        LaunchedEffect(uiState.isRefreshing) {
            if (!uiState.isRefreshing) {
                pullToRefreshState.endRefresh()
            }
        }
        
        LaunchedEffect(pullToRefreshState.isRefreshing) {
            if (pullToRefreshState.isRefreshing) {
                viewModel.refresh()
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            when {
                uiState.isLoading -> {
                    // Shimmer loading skeleton
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        ShimmerEntryList(itemCount = 5)
                    }
                }
                
                uiState.entries.isEmpty() -> {
                    EmptyState(
                        icon = {
                            Icon(
                            imageVector = Icons.Outlined.EditNote,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = stringResource(R.string.empty_entries),
                    message = stringResource(R.string.empty_entries_message),
                    action = {
                        Button(onClick = onNewEntry) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Write your first entry")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            else -> {
                when (viewMode) {
                    ViewMode.TIMELINE -> TimelineView(
                        entries = uiState.entries,
                        selectedIds = uiState.selectedEntryIds,
                        isSelectionMode = uiState.isSelectionMode,
                        onEntryClick = { id ->
                            if (uiState.isSelectionMode) {
                                viewModel.toggleEntrySelection(id)
                            } else {
                                onEntryClick(id)
                            }
                        },
                        onEntryLongClick = { id ->
                            viewModel.toggleEntrySelection(id)
                        },
                        onFavoriteClick = { viewModel.toggleFavorite(it) },
                        onPinClick = { viewModel.togglePin(it) },
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    ViewMode.CALENDAR -> CalendarView(
                        entries = uiState.entries,
                        onEntryClick = onEntryClick,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    ViewMode.CATEGORY -> CategoryView(
                        categories = uiState.categories,
                        onCategoryClick = onCategoryClick,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    ViewMode.TAG -> TagView(
                        tags = uiState.tags,
                        onTagClick = onTagClick,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    ViewMode.MOOD -> MoodView(
                        entries = uiState.entries,
                        onMoodClick = onMoodClick,
                        onEntryClick = onEntryClick,
                        onFavoriteClick = { viewModel.toggleFavorite(it) },
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    ViewMode.PEOPLE -> PeopleView(
                        entries = uiState.entries,
                        people = uiState.people,
                        onEntryClick = onEntryClick,
                        onFavoriteClick = { viewModel.toggleFavorite(it) },
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    ViewMode.LOCATION -> LocationView(
                        entries = uiState.entries,
                        onEntryClick = onEntryClick,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    ViewMode.STORY -> {
                        // Will navigate to StoryScreen
                    }
                }
            }
            }
            
            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
        
        // Error snackbar
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar
                viewModel.dismissError()
            }
        }
    }
    
    // Create Memory Dialog
    if (showCreateMemoryDialog) {
        CreateMemoryFromEntriesDialog(
            selectedCount = uiState.selectedEntryIds.size,
            onDismiss = { showCreateMemoryDialog = false },
            onCreate = { title, description ->
                onCreateMemoryWithEntries(title, description, uiState.selectedEntryIds.toList())
                viewModel.clearSelection()
                showCreateMemoryDialog = false
            }
        )
    }
    
    // Add to Existing Memory Dialog
    if (showAddToMemoryDialog) {
        AddToMemoryDialog(
            memories = uiState.memories,
            selectedCount = uiState.selectedEntryIds.size,
            onDismiss = { showAddToMemoryDialog = false },
            onMemorySelected = { memoryId ->
                viewModel.addEntriesToMemory(memoryId)
                showAddToMemoryDialog = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimelineView(
    entries: List<com.dairum.domain.model.JournalEntry>,
    selectedIds: Set<Long>,
    isSelectionMode: Boolean,
    onEntryClick: (Long) -> Unit,
    onEntryLongClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onPinClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Separate pinned and regular entries
    val pinnedEntries = entries.filter { it.isPinned }
    val regularEntries = entries.filter { !it.isPinned }
    
    // Group regular entries by date
    val groupedEntries = regularEntries.groupBy { entry ->
        entry.createdAt.toLocalDate()
    }
    
    // Calculate weekly insights
    val weeklyInsights = remember(entries) {
        calculateWeeklyInsights(entries)
    }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Weekly Insights Card
        if (entries.isNotEmpty()) {
            item(key = "weekly_insights") {
                WeeklyInsightsCard(insights = weeklyInsights)
            }
        }
        
        // Pinned Entries Section
        if (pinnedEntries.isNotEmpty()) {
            item(key = "pinned_header") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PushPin,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pinned",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            items(
                items = pinnedEntries,
                key = { "pinned_${it.id}" }
            ) { entry ->
                val isSelected = selectedIds.contains(entry.id)
                PinnedEntryCard(
                    entry = entry,
                    isSelected = isSelected,
                    isSelectionMode = isSelectionMode,
                    onEntryClick = onEntryClick,
                    onEntryLongClick = onEntryLongClick,
                    onFavoriteClick = onFavoriteClick,
                    onPinClick = onPinClick
                )
            }
        }
        
        groupedEntries.forEach { (date, dateEntries) ->
            item(key = "header_$date") {
                Text(
                    text = formatDateHeader(date),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(
                items = dateEntries,
                key = { it.id }
            ) { entry ->
                val isSelected = selectedIds.contains(entry.id)
                
                // Use swipe actions when not in selection mode
                if (isSelectionMode) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { onEntryClick(entry.id) },
                                onLongClick = { onEntryLongClick(entry.id) }
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { onEntryClick(entry.id) },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .then(
                                        if (isSelected) {
                                            Modifier.background(
                                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                                shape = MaterialTheme.shapes.medium
                                            )
                                        } else {
                                            Modifier
                                        }
                                    )
                            ) {
                                EntryCard(
                                    entry = entry,
                                    onClick = { onEntryClick(entry.id) },
                                    onFavoriteClick = { onFavoriteClick(entry.id) },
                                    handleClickInternally = false
                                )
                            }
                        }
                    }
                } else {
                    // Regular entry card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { onEntryClick(entry.id) },
                                onLongClick = { onEntryLongClick(entry.id) }
                            )
                    ) {
                        EntryCard(
                            entry = entry,
                            onClick = { onEntryClick(entry.id) },
                            onFavoriteClick = { onFavoriteClick(entry.id) },
                            handleClickInternally = false
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMemoryFromEntriesDialog(
    selectedCount: Int,
    onDismiss: () -> Unit,
    onCreate: (title: String, description: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Memory") },
        text = {
            Column {
                Text(
                    text = "$selectedCount entries selected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Memory Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    if (title.isNotBlank()) {
                        onCreate(title, description.takeIf { it.isNotBlank() })
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Create")
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
private fun AddToMemoryDialog(
    memories: List<com.dairum.domain.model.Memory>,
    selectedCount: Int,
    onDismiss: () -> Unit,
    onMemorySelected: (Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add to Memory") },
        text = {
            Column {
                Text(
                    text = "Add $selectedCount entries to:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (memories.isEmpty()) {
                    Text(
                        text = "No memories available. Create one first.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(memories) { memory ->
                            Card(
                                onClick = { onMemorySelected(memory.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Memory icon
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = memory.icon ?: "📚",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.width(12.dp))
                                    
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = memory.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "${memory.entryCount} entries • ${memory.duration}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    
                                    Icon(
                                        Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
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

// Helper function to get emoji for a category
private fun getCategoryEmoji(category: com.dairum.domain.model.Category): String {
    // Check if icon field has an emoji (first character code > typical emoji start)
    if (category.icon.isNotEmpty()) {
        val firstChar = category.icon.codePointAt(0)
        if (firstChar > 0x1F000) {
            return category.icon
        }
    }
    
    // Map common category names to emojis
    return when (category.name.lowercase()) {
        "personal", "diary" -> "📔"
        "work", "job", "office" -> "💼"
        "travel", "trips", "vacation" -> "✈️"
        "health", "fitness", "wellness" -> "💪"
        "food", "recipes", "cooking" -> "🍴"
        "family" -> "👨‍👩‍👧‍👦"
        "friends" -> "👥"
        "hobbies" -> "🎨"
        "learning", "study", "education" -> "📚"
        "finance", "money", "budget" -> "💰"
        "ideas", "thoughts" -> "💡"
        "dreams" -> "🌙"
        "goals", "achievements" -> "🎯"
        "gratitude" -> "🙏"
        "love", "relationships" -> "❤️"
        "memories" -> "📷"
        "music" -> "🎵"
        "sports" -> "⚽"
        "nature", "outdoors" -> "🌿"
        "pets", "animals" -> "🐾"
        "books", "reading" -> "📖"
        "movies", "entertainment" -> "🎬"
        "art", "creative" -> "🎨"
        "tech", "technology" -> "💻"
        "shopping" -> "🛍️"
        "events", "special" -> "🎉"
        else -> "📁"
    }
}

/**
 * Maps icon name strings to Material Icon vectors for category display
 */
private fun getCategoryIconVector(iconName: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (iconName) {
        "folder" -> Icons.Default.Folder
        "edit_note" -> Icons.Default.Edit
        "person" -> Icons.Default.Person
        "work" -> Icons.Default.Work
        "flight" -> Icons.Default.Flight
        "favorite" -> Icons.Default.Favorite
        "flag" -> Icons.Default.Flag
        "star" -> Icons.Default.Star
        "home" -> Icons.Default.Home
        "school" -> Icons.Default.School
        "fitness_center" -> Icons.Default.FitnessCenter
        "restaurant" -> Icons.Default.Restaurant
        "shopping_cart" -> Icons.Default.ShoppingCart
        "music_note" -> Icons.Default.MusicNote
        "book" -> Icons.Default.Book
        "camera" -> Icons.Default.CameraAlt
        "pets" -> Icons.Default.Pets
        "sports" -> Icons.Default.Sports
        "beach" -> Icons.Default.BeachAccess
        "local_cafe" -> Icons.Default.LocalCafe
        "palette" -> Icons.Default.Palette
        "code" -> Icons.Default.Code
        "science" -> Icons.Default.Science
        "psychology" -> Icons.Default.Psychology
        "celebration" -> Icons.Default.Celebration
        "nature" -> Icons.Default.Nature
        "spa" -> Icons.Default.Spa
        "directions_car" -> Icons.Default.DirectionsCar
        "movie" -> Icons.Default.Movie
        "night_life" -> Icons.Default.Nightlife
        "volunteer_activism" -> Icons.Default.VolunteerActivism
        "lightbulb" -> Icons.Default.Lightbulb
        "self_improvement" -> Icons.Default.SelfImprovement
        "groups" -> Icons.Default.Groups
        "savings" -> Icons.Default.Savings
        "medical_services" -> Icons.Default.MedicalServices
        else -> Icons.Default.Folder
    }
}

@Composable
private fun CategoryView(
    categories: List<com.dairum.domain.model.Category>,
    onCategoryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories,
            key = { it.id }
        ) { category ->
            Card(
                onClick = { onCategoryClick(category.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Colored circle with icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color(category.color),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIconVector(category.icon),
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${category.entryCount} entries",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
}

@Composable
private fun TagView(
    tags: List<com.dairum.domain.model.Tag>,
    onTagClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Tags",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        item {
            // Tag cloud
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        onClick = { onTagClick(tag.id) }
                    )
                }
            }
        }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MoodView(
    entries: List<com.dairum.domain.model.JournalEntry>,
    onMoodClick: (Int) -> Unit,
    onEntryClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter entries with moods
    val entriesWithMood = entries.filter { it.mood != null }
    
    // Group all moods by category
    val moodsByCategory = entriesWithMood.groupBy { entry ->
        val mood = entry.mood!!
        // Use moodType to correctly determine extended vs basic
        if (mood.moodType == com.dairum.domain.model.MoodType.EXTENDED) {
            com.dairum.domain.model.MoodOption.fromId(mood.level)?.category 
                ?: com.dairum.domain.model.MoodCategory.NEUTRAL
        } else {
            when (mood.level) {
                5, 4 -> com.dairum.domain.model.MoodCategory.POSITIVE
                3 -> com.dairum.domain.model.MoodCategory.NEUTRAL
                2, 1 -> com.dairum.domain.model.MoodCategory.NEGATIVE
                else -> com.dairum.domain.model.MoodCategory.NEUTRAL
            }
        }
    }
    
    // Count moods by MoodOption (for extended) and level (for basic)
    val moodCounts = entriesWithMood
        .groupBy { entry ->
            val mood = entry.mood!!
            if (mood.moodType == com.dairum.domain.model.MoodType.EXTENDED) {
                com.dairum.domain.model.MoodOption.fromId(mood.level)?.let { 
                    it.emoji to it.label 
                } ?: ("😐" to "Unknown")
            } else {
                mood.emoji to mood.label
            }
        }
        .mapValues { it.value.size }
        .entries
        .sortedByDescending { it.value }
    
    // Selected mood filter state
    var selectedMoodFilter by remember { mutableStateOf<Pair<String, String>?>(null) }
    
    // Filtered entries based on selected mood
    val filteredEntries = remember(selectedMoodFilter, entriesWithMood) {
        if (selectedMoodFilter == null) null
        else entriesWithMood.filter { entry ->
            val mood = entry.mood!!
            val (emoji, label) = if (mood.moodType == com.dairum.domain.model.MoodType.EXTENDED) {
                com.dairum.domain.model.MoodOption.fromId(mood.level)?.let {
                    it.emoji to it.label
                } ?: ("😐" to "Unknown")
            } else {
                mood.emoji to mood.label
            }
            emoji == selectedMoodFilter!!.first && label == selectedMoodFilter!!.second
        }.sortedByDescending { it.createdAt }
    }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mood Overview Card
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Mood Overview",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${entriesWithMood.size} entries with mood tracked",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Category summary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        com.dairum.domain.model.MoodCategory.values().forEach { category ->
                            val count = moodsByCategory[category]?.size ?: 0
                            val (emoji, label) = when (category) {
                                com.dairum.domain.model.MoodCategory.POSITIVE -> "😊" to "Positive"
                                com.dairum.domain.model.MoodCategory.NEUTRAL -> "😐" to "Neutral"
                                com.dairum.domain.model.MoodCategory.NEGATIVE -> "😔" to "Negative"
                                com.dairum.domain.model.MoodCategory.MIXED -> "🤔" to "Mixed"
                            }
                            
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = emoji, style = MaterialTheme.typography.headlineSmall)
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$count",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Top moods
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Top Moods",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (moodCounts.isEmpty()) {
                        Text(
                            text = "Start tracking your mood to see insights",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        moodCounts.take(6).forEach { (moodInfo, count) ->
                            val (emoji, label) = moodInfo
                            val isSelected = selectedMoodFilter == moodInfo
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.small)
                                    .clickable {
                                        selectedMoodFilter = if (isSelected) null else moodInfo
                                    }
                                    .then(
                                        if (isSelected) Modifier.background(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            MaterialTheme.shapes.small
                                        ) else Modifier
                                    )
                                    .padding(vertical = 4.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = emoji,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = "$count",
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Entries by category
        com.dairum.domain.model.MoodCategory.values().forEach { category ->
            val categoryEntries = moodsByCategory[category] ?: emptyList()
            if (categoryEntries.isNotEmpty()) {
                val (emoji, label) = when (category) {
                    com.dairum.domain.model.MoodCategory.POSITIVE -> "😊" to "Positive"
                    com.dairum.domain.model.MoodCategory.NEUTRAL -> "😐" to "Neutral"
                    com.dairum.domain.model.MoodCategory.NEGATIVE -> "😔" to "Negative"
                    com.dairum.domain.model.MoodCategory.MIXED -> "🤔" to "Mixed"
                }
                
                item(key = "mood_category_header_$category") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    ) {
                        Text(text = emoji, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$label (${categoryEntries.size})",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                // Show mood chips for this category
                item(key = "mood_category_chips_$category") {
                    val moodsInCategory = categoryEntries
                        .groupBy { entry ->
                            val mood = entry.mood!!
                            if (mood.moodType == com.dairum.domain.model.MoodType.EXTENDED) {
                                com.dairum.domain.model.MoodOption.fromId(mood.level)?.let {
                                    it.emoji to it.label
                                } ?: ("😐" to "Unknown")
                            } else {
                                mood.emoji to mood.label
                            }
                        }
                    
                    androidx.compose.foundation.layout.FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        moodsInCategory.forEach { (moodInfo, moodEntries) ->
                            val (moodEmoji, moodLabel) = moodInfo
                            val isSelected = selectedMoodFilter == moodInfo
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                                       else MaterialTheme.colorScheme.surfaceVariant,
                                onClick = { 
                                    selectedMoodFilter = if (isSelected) null else moodInfo
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = moodEmoji)
                                    Text(
                                        text = moodLabel,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = "${moodEntries.size}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Show recent entries
                items(
                    items = categoryEntries.sortedByDescending { it.createdAt }.take(3),
                    key = { "mood_entry_${category}_${it.id}" }
                ) { entry ->
                    EntryCard(
                        entry = entry,
                        onClick = { onEntryClick(entry.id) },
                        onFavoriteClick = { onFavoriteClick(entry.id) }
                    )
                }
            }
        }
        
        // Selected mood entries section
        if (filteredEntries != null) {
            item(key = "selected_mood_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${selectedMoodFilter!!.first} ${selectedMoodFilter!!.second} — ${filteredEntries.size} entries",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    TextButton(onClick = { selectedMoodFilter = null }) {
                        Text("Clear")
                    }
                }
            }
            
            items(
                items = filteredEntries,
                key = { "filtered_mood_entry_${it.id}" }
            ) { entry ->
                EntryCard(
                    entry = entry,
                    onClick = { onEntryClick(entry.id) },
                    onFavoriteClick = { onFavoriteClick(entry.id) }
                )
            }
        }
    }
}

@Composable
private fun PeopleView(
    entries: List<com.dairum.domain.model.JournalEntry>,
    people: List<com.dairum.domain.model.Person>,
    onEntryClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Map of person ID to their entries
    val personEntryMap = remember(entries, people) {
        val map = mutableMapOf<Long, MutableList<com.dairum.domain.model.JournalEntry>>()
        entries.forEach { entry ->
            entry.people.forEach { person ->
                map.getOrPut(person.id) { mutableListOf() }.add(entry)
            }
        }
        map
    }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // People overview card
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "People in Your Journal",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${people.size}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "People",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val entriesWithPeople = entries.count { it.people.isNotEmpty() }
                            Text(
                                text = "$entriesWithPeople",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Mentions",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // People list
        val sortedPeople = people.sortedByDescending { personEntryMap[it.id]?.size ?: 0 }
        
        if (sortedPeople.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.People,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No people added yet",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Add people to your entries to track who you spend time with",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(sortedPeople) { person ->
                var expanded by remember { mutableStateOf(false) }
                val personEntries = personEntryMap[person.id] ?: emptyList()
                
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = !expanded }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = person.name.take(2).uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = person.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    if (person.isFavorite) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "Favorite",
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                person.relationship?.let { relationship ->
                                    Text(
                                        text = relationship,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "${personEntries.size} mentions",
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
                        if (expanded && personEntries.isNotEmpty()) {
                            HorizontalDivider()
                            
                            personEntries.sortedByDescending { it.createdAt }.forEach { entry ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onEntryClick(entry.id) }
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
                                                DateTimeFormatter.ofPattern("MMM d, yyyy")
                                            ),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    
                                    IconButton(
                                        onClick = { onFavoriteClick(entry.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (entry.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                            contentDescription = "Favorite",
                                            modifier = Modifier.size(18.dp),
                                            tint = if (entry.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDateHeader(date: LocalDate): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    
    return when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
    }
}

// Weekly Insights Data Class
private data class WeeklyInsights(
    val entriesThisWeek: Int,
    val wordCountThisWeek: Int,
    val streakDays: Int,
    val topMood: com.dairum.domain.model.Mood?,
    val comparisonText: String,
    val journaledDaysThisWeek: Int
)

private fun calculateWeeklyInsights(entries: List<com.dairum.domain.model.JournalEntry>): WeeklyInsights {
    val today = LocalDate.now()
    val startOfWeek = today.minusDays(today.dayOfWeek.value.toLong() - 1)
    
    // Filter entries from this week
    val weekEntries = entries.filter { entry ->
        entry.createdAt.toLocalDate() >= startOfWeek
    }
    
    // Calculate entries count
    val entriesThisWeek = weekEntries.size
    
    // Calculate word count
    val wordCountThisWeek = weekEntries.sumOf { it.wordCount }
    
    // Calculate journaled days
    val journaledDaysThisWeek = weekEntries.map { it.createdAt.toLocalDate() }.toSet().size
    
    // Calculate streak (consecutive days with entries)
    var streakDays = 0
    var checkDate = today
    while (true) {
        val hasEntry = entries.any { it.createdAt.toLocalDate() == checkDate }
        if (hasEntry) {
            streakDays++
            checkDate = checkDate.minusDays(1)
        } else {
            break
        }
    }
    
    // Find top mood
    val topMood = weekEntries
        .mapNotNull { it.mood }
        .groupingBy { it }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key
    
    // Generate comparison text
    val previousWeekStart = startOfWeek.minusWeeks(1)
    val previousWeekEntries = entries.filter { entry ->
        val date = entry.createdAt.toLocalDate()
        date >= previousWeekStart && date < startOfWeek
    }.size
    
    val comparisonText = when {
        previousWeekEntries == 0 -> "Keep going!"
        entriesThisWeek > previousWeekEntries -> "↑ ${entriesThisWeek - previousWeekEntries} more than last week"
        entriesThisWeek < previousWeekEntries -> "↓ ${previousWeekEntries - entriesThisWeek} less than last week"
        else -> "Same as last week"
    }
    
    return WeeklyInsights(
        entriesThisWeek = entriesThisWeek,
        wordCountThisWeek = wordCountThisWeek,
        streakDays = streakDays,
        topMood = topMood,
        comparisonText = comparisonText,
        journaledDaysThisWeek = journaledDaysThisWeek
    )
}

@Composable
private fun WeeklyInsightsCard(
    insights: WeeklyInsights,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "This Week",
                    style = MaterialTheme.typography.titleMedium
                )
                
                if (insights.streakDays > 0) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "🔥",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = "${insights.streakDays} day streak",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Entries count
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${insights.entriesThisWeek}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Entries",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Words written
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${insights.wordCountThisWeek}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Words",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Days journaled
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${insights.journaledDaysThisWeek}/7",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Days",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Top mood
                insights.topMood?.let { mood ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = mood.emoji,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Top Mood",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = insights.comparisonText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PinnedEntryCard(
    entry: com.dairum.domain.model.JournalEntry,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onEntryClick: (Long) -> Unit,
    onEntryLongClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onPinClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onEntryClick(entry.id) },
                onLongClick = { onEntryLongClick(entry.id) }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row with date, mood, pin indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PushPin,
                        contentDescription = "Pinned",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = entry.createdAt.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    entry.mood?.let { mood ->
                        MoodIndicator(mood = mood, size = 20.dp)
                    }
                    
                    IconButton(
                        onClick = { onFavoriteClick(entry.id) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (entry.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            modifier = Modifier.size(16.dp),
                            tint = if (entry.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    IconButton(
                        onClick = { onPinClick(entry.id) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PushPin,
                            contentDescription = "Unpin",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Title
            entry.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            // Content preview
            if (entry.plainContent.isNotBlank()) {
                Text(
                    text = entry.plainContent,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Category badge
            entry.category?.let { category ->
                Spacer(modifier = Modifier.height(8.dp))
                CategoryBadge(category = category)
            }
        }
    }
}

@Composable
private fun LocationView(
    entries: List<com.dairum.domain.model.JournalEntry>,
    onEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    
    // Debug logging
    android.util.Log.d("LocationView", "Total entries received: ${entries.size}")
    entries.forEachIndexed { index, entry ->
        android.util.Log.d("LocationView", "Entry[$index] id=${entry.id}: location=${entry.location?.let { "lat=${it.latitude}, lng=${it.longitude}" } ?: "null"}")
    }
    
    // Group entries by location
    val entriesWithLocation = remember(entries) {
        val filtered = entries.filter { it.location != null }
        android.util.Log.d("LocationView", "Entries with location: ${filtered.size}")
        filtered
    }
    
    val locationGroups = remember(entriesWithLocation) {
        entriesWithLocation
            .groupBy { entry ->
                val loc = entry.location!!
                // Round to 3 decimal places to group nearby locations
                "${(loc.latitude * 1000).toInt()}_${(loc.longitude * 1000).toInt()}"
            }
            .map { (_, groupEntries) ->
                val representativeLocation = groupEntries.first().location!!
                LocationGroup(
                    location = representativeLocation,
                    entries = groupEntries.sortedByDescending { it.createdAt }
                )
            }
            .sortedByDescending { it.entries.size }
    }
    
    var selectedLocation by remember { mutableStateOf<LocationGroup?>(null) }
    var isMapExpanded by remember { mutableStateOf(false) }
    
    // Initialize OSMDroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }
    
    Column(modifier = modifier.fillMaxSize()) {
        // Stats card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Location Overview",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${locationGroups.size}",
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
                            text = "${entriesWithLocation.size}",
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
        
        if (entriesWithLocation.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "No Location Data",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Add locations to your entries to see them here on the map",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Map view
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isMapExpanded) Modifier.weight(1f)
                        else Modifier.height(250.dp)
                    )
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AndroidView(
                    factory = { ctx ->
                        MapView(ctx).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setZoom(12.0)
                            
                            // Center on first location
                            locationGroups.firstOrNull()?.let { group ->
                                controller.setCenter(
                                    GeoPoint(group.location.latitude, group.location.longitude)
                                )
                            }
                        }
                    },
                    update = { mapView ->
                        mapView.overlays.clear()
                        
                        locationGroups.forEach { group ->
                            val marker = Marker(mapView).apply {
                                position = GeoPoint(group.location.latitude, group.location.longitude)
                                title = group.location.placeName ?: group.location.address ?: "Location"
                                snippet = "${group.entries.size} entries"
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                setOnMarkerClickListener { _, _ ->
                                    selectedLocation = group
                                    true
                                }
                            }
                            mapView.overlays.add(marker)
                        }
                        
                        mapView.invalidate()
                    },
                    modifier = Modifier.fillMaxSize()
                )
                
                // Expand/Collapse button
                FilledTonalIconButton(
                    onClick = { isMapExpanded = !isMapExpanded },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isMapExpanded) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        contentDescription = if (isMapExpanded) "Collapse map" else "Expand map",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Location list - hide when map is expanded
            if (!isMapExpanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = locationGroups,
                    key = { "${it.location.latitude}_${it.location.longitude}" }
                ) { group ->
                    LocationGroupCard(
                        group = group,
                        isSelected = selectedLocation == group,
                        onEntryClick = onEntryClick,
                        onClick = { selectedLocation = if (selectedLocation == group) null else group }
                    )
                }
            }
            }
        }
    }
}

private data class LocationGroup(
    val location: Location,
    val entries: List<com.dairum.domain.model.JournalEntry>
)

@Composable
private fun LocationGroupCard(
    group: LocationGroup,
    isSelected: Boolean,
    onEntryClick: (Long) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = if (isSelected) 
                        MaterialTheme.colorScheme.onPrimaryContainer 
                    else 
                        MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.location.placeName ?: group.location.address ?: "Unknown Location",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (group.location.address != null && group.location.placeName != null) {
                        Text(
                            text = group.location.address!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) 
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                AssistChip(
                    onClick = {},
                    label = { Text("${group.entries.size}") },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Article,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
            
            // Show entries when selected
            if (isSelected) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                
                group.entries.take(3).forEach { entry ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEntryClick(entry.id) },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = entry.title ?: entry.plainContent.take(30),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = entry.createdAt.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                if (group.entries.size > 3) {
                    Text(
                        text = "+${group.entries.size - 3} more entries",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarView(
    entries: List<com.dairum.domain.model.JournalEntry>,
    onEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    
    val entriesByDate = remember(entries) {
        entries.groupBy { it.createdAt.toLocalDate() }
    }
    
    val daysInMonth = selectedMonth.lengthOfMonth()
    val firstDayOfWeek = selectedMonth.atDay(1).dayOfWeek.value % 7 // Sunday = 0
    
    Column(modifier = modifier.padding(16.dp)) {
        // Month navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { selectedMonth = selectedMonth.minusMonths(1) }) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous month")
            }
            Text(
                text = selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { selectedMonth = selectedMonth.plusMonths(1) }) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Next month")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Day-of-week headers
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Calendar grid
        val totalCells = firstDayOfWeek + daysInMonth
        val rows = (totalCells + 6) / 7
        
        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0..6) {
                    val cellIndex = row * 7 + col
                    val day = cellIndex - firstDayOfWeek + 1
                    
                    if (day in 1..daysInMonth) {
                        val date = selectedMonth.atDay(day)
                        val hasEntries = entriesByDate.containsKey(date)
                        val entryCount = entriesByDate[date]?.size ?: 0
                        val isSelected = selectedDate == date
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .then(
                                    if (isSelected) Modifier.background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        CircleShape
                                    ) else Modifier
                                )
                                .clickable { selectedDate = if (isSelected) null else date },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$day",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                           else MaterialTheme.colorScheme.onSurface
                                )
                                if (hasEntries) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(
                                                MaterialTheme.colorScheme.primary,
                                                CircleShape
                                            )
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
        
        // Entries for selected date
        selectedDate?.let { date ->
            val dayEntries = entriesByDate[date] ?: emptyList()
            if (dayEntries.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d")),
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                dayEntries.forEach { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onEntryClick(entry.id) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            entry.mood?.let { 
                                Text(it.emoji, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Column {
                                Text(
                                    text = entry.title ?: "Untitled",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = entry.createdAt.format(DateTimeFormatter.ofPattern("h:mm a")),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
