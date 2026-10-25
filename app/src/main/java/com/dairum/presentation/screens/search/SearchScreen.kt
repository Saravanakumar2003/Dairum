package com.dairum.presentation.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onEntryClick: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle export messages
    LaunchedEffect(uiState.exportSuccess) {
        if (uiState.exportSuccess) {
            snackbarHostState.showSnackbar("Export saved to Documents/Data Exports/Dairum")
            viewModel.clearExportState()
        }
    }
    LaunchedEffect(uiState.exportError) {
        uiState.exportError?.let { error ->
            snackbarHostState.showSnackbar("Export failed: $error")
            viewModel.clearExportState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { 
                            query = it
                            viewModel.search(it)
                        },
                        placeholder = { Text("Search entries...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { 
                                    query = ""
                                    viewModel.search("")
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(20.dp))
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                        shape = RoundedCornerShape(28.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Advanced filter toggle with badge
                    BadgedBox(
                        badge = {
                            if (uiState.activeFilterCount > 0) {
                                Badge { Text("${uiState.activeFilterCount}") }
                            }
                        }
                    ) {
                        IconButton(onClick = { viewModel.toggleAdvancedFilters() }) {
                            Icon(
                                if (uiState.showAdvancedFilters) Icons.Default.FilterListOff else Icons.Default.Tune,
                                contentDescription = "Advanced filters"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Advanced Filters Panel
            AnimatedVisibility(
                visible = uiState.showAdvancedFilters,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                AdvancedFiltersPanel(
                    uiState = uiState,
                    onToggleFavorites = { viewModel.toggleFavoritesFilter() },
                    onToggleHasMedia = { viewModel.toggleHasMediaFilter() },
                    onToggleHasLocation = { viewModel.toggleHasLocationFilter() },
                    onToggleHasWeather = { viewModel.toggleHasWeatherFilter() },
                    onSetCategory = { viewModel.setCategoryFilter(it) },
                    onSetMood = { viewModel.setMoodFilter(it) },
                    onToggleTag = { viewModel.toggleTagFilter(it) },
                    onTogglePerson = { viewModel.togglePersonFilter(it) },
                    onToggleTracker = { viewModel.toggleTrackerFilter(it) },
                    onSetTrackerValueQuery = { viewModel.setTrackerValueQuery(it) },
                    onSetDateFrom = { viewModel.setDateFrom(it) },
                    onSetDateTo = { viewModel.setDateTo(it) },
                    onSetMinWordCount = { viewModel.setMinWordCount(it) },
                    onSetMaxWordCount = { viewModel.setMaxWordCount(it) },
                    onSetSortOrder = { viewModel.setSortOrder(it) },
                    onClearAll = { 
                        query = ""
                        viewModel.clearAllFilters() 
                    }
                )
            }
            
            // Result count & sort indicator + export
            if (uiState.results.isNotEmpty() || uiState.hasActiveFilters || query.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${uiState.resultCount} ${if (uiState.resultCount == 1) "result" else "results"}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (uiState.isSearching) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        }
                        if (uiState.results.isNotEmpty()) {
                            var showExportMenu by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { showExportMenu = true }) {
                                    if (uiState.isExporting) {
                                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                    } else {
                                        Icon(
                                            Icons.Outlined.FileDownload,
                                            contentDescription = "Export results",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                DropdownMenu(expanded = showExportMenu, onDismissRequest = { showExportMenu = false }) {
                                    DropdownMenuItem(
                                        text = { Text("Export as Plain Text") },
                                        onClick = {
                                            showExportMenu = false
                                            viewModel.exportSearchResults(com.dairum.domain.model.ExportFormat.TXT)
                                        },
                                        leadingIcon = { Icon(Icons.Outlined.Description, contentDescription = null, modifier = Modifier.size(20.dp)) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Export as PDF with Photos") },
                                        onClick = {
                                            showExportMenu = false
                                            viewModel.exportSearchResults(com.dairum.domain.model.ExportFormat.PDF, includeMedia = true)
                                        },
                                        leadingIcon = { Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, modifier = Modifier.size(20.dp)) }
                                    )
                                }
                            }
                        }
                    }
                }
                HorizontalDivider()
            }
            
            // Results
            if (uiState.results.isEmpty() && (query.isNotEmpty() || uiState.hasActiveFilters)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No entries found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Try adjusting your search or filters",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                        if (uiState.hasActiveFilters) {
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(onClick = {
                                query = ""
                                viewModel.clearAllFilters()
                            }) {
                                Text("Clear all filters")
                            }
                        }
                    }
                }
            } else if (uiState.results.isEmpty()) {
                // Empty state - no search yet
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.ManageSearch,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Search your journal",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Search by text, or use filters for tags, people, dates, mood, and more",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.results, key = { it.id }) { entry ->
                        SearchResultCard(entry = entry, onClick = { onEntryClick(entry.id) })
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AdvancedFiltersPanel(
    uiState: SearchUiState,
    onToggleFavorites: () -> Unit,
    onToggleHasMedia: () -> Unit,
    onToggleHasLocation: () -> Unit,
    onToggleHasWeather: () -> Unit,
    onSetCategory: (Long) -> Unit,
    onSetMood: (Int) -> Unit,
    onToggleTag: (Long) -> Unit,
    onTogglePerson: (Long) -> Unit,
    onToggleTracker: (Long) -> Unit,
    onSetTrackerValueQuery: (String) -> Unit,
    onSetDateFrom: (LocalDate?) -> Unit,
    onSetDateTo: (LocalDate?) -> Unit,
    onSetMinWordCount: (Int?) -> Unit,
    onSetMaxWordCount: (Int?) -> Unit,
    onSetSortOrder: (SearchSortOrder) -> Unit,
    onClearAll: () -> Unit
) {
    var showDateFromPicker by remember { mutableStateOf(false) }
    var showDateToPicker by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }
    var showTagPicker by remember { mutableStateOf(false) }
    var showPeoplePicker by remember { mutableStateOf(false) }
    var showTrackerPicker by remember { mutableStateOf(false) }
    
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with clear & sort
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Advanced Filters",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Sort
                        Box {
                            AssistChip(
                                onClick = { showSortMenu = true },
                                label = { Text(uiState.sortOrder.label, style = MaterialTheme.typography.labelSmall) },
                                leadingIcon = { Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            )
                            DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                                SearchSortOrder.entries.forEach { order ->
                                    DropdownMenuItem(
                                        text = { Text(order.label) },
                                        onClick = { onSetSortOrder(order); showSortMenu = false },
                                        leadingIcon = if (uiState.sortOrder == order) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                                        } else null
                                    )
                                }
                            }
                        }
                        if (uiState.hasActiveFilters) {
                            TextButton(onClick = onClearAll) { Text("Clear all") }
                        }
                    }
                }
            }
            
            // Quick Filters
            item {
                FilterSectionHeader("Quick Filters")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = uiState.filterFavorites,
                        onClick = onToggleFavorites,
                        label = { Text("★ Favorites") }
                    )
                    FilterChip(
                        selected = uiState.filterHasMedia,
                        onClick = onToggleHasMedia,
                        label = { Text("📷 Has Media") }
                    )
                    FilterChip(
                        selected = uiState.filterHasLocation,
                        onClick = onToggleHasLocation,
                        label = { Text("📍 Has Location") }
                    )
                    FilterChip(
                        selected = uiState.filterHasWeather,
                        onClick = onToggleHasWeather,
                        label = { Text("🌤 Has Weather") }
                    )
                }
            }
            
            // Categories
            if (uiState.categories.isNotEmpty()) {
                item {
                    FilterSectionHeader("Categories")
                    val selectedCategory = uiState.categories.find { it.id == uiState.selectedCategoryId }
                    AssistChip(
                        onClick = { showCategoryPicker = true },
                        label = { Text(selectedCategory?.name ?: "Select category") },
                        leadingIcon = {
                            if (selectedCategory != null) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(selectedCategory.color))
                                )
                            } else {
                                Icon(Icons.Outlined.Category, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        },
                        trailingIcon = {
                            if (selectedCategory != null) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(16.dp).clickable { onSetCategory(selectedCategory.id) }
                                )
                            }
                        }
                    )
                }
            }
            
            // Mood
            item {
                FilterSectionHeader("Mood")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(1 to "😢", 2 to "😔", 3 to "😐", 4 to "🙂", 5 to "😄").forEach { (level, emoji) ->
                        FilterChip(
                            selected = uiState.selectedMoodLevel == level,
                            onClick = { onSetMood(level) },
                            label = { Text(emoji) }
                        )
                    }
                }
            }
            
            // Tags
            if (uiState.allTags.isNotEmpty()) {
                item {
                    FilterSectionHeader("Tags")
                    val selectedCount = uiState.selectedTagIds.size
                    AssistChip(
                        onClick = { showTagPicker = true },
                        label = { Text(if (selectedCount > 0) "$selectedCount tag${if (selectedCount > 1) "s" else ""} selected" else "Select tags") },
                        leadingIcon = { Icon(Icons.Outlined.Tag, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        trailingIcon = if (selectedCount > 0) {
                            { Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp).clickable { uiState.selectedTagIds.forEach { onToggleTag(it) } }) }
                        } else null
                    )
                }
            }
            
            // People
            if (uiState.allPeople.isNotEmpty()) {
                item {
                    FilterSectionHeader("People")
                    val selectedCount = uiState.selectedPersonIds.size
                    AssistChip(
                        onClick = { showPeoplePicker = true },
                        label = { Text(if (selectedCount > 0) "$selectedCount ${if (selectedCount > 1) "people" else "person"} selected" else "Select people") },
                        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        trailingIcon = if (selectedCount > 0) {
                            { Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp).clickable { uiState.selectedPersonIds.forEach { onTogglePerson(it) } }) }
                        } else null
                    )
                }
            }
            
            // Trackers
            if (uiState.allTrackers.isNotEmpty()) {
                item {
                    FilterSectionHeader("Trackers")
                    val selectedCount = uiState.selectedTrackerIds.size
                    AssistChip(
                        onClick = { showTrackerPicker = true },
                        label = { Text(if (selectedCount > 0) "$selectedCount tracker${if (selectedCount > 1) "s" else ""} selected" else "Select trackers") },
                        leadingIcon = { Icon(Icons.Outlined.TrackChanges, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        trailingIcon = if (selectedCount > 0) {
                            { Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp).clickable { uiState.selectedTrackerIds.forEach { onToggleTracker(it) } }) }
                        } else null
                    )
                    
                    // Sub-tracker value search
                    if (uiState.selectedTrackerIds.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val selectedTrackerNames = uiState.allTrackers
                            .filter { it.id in uiState.selectedTrackerIds }
                            .joinToString(", ") { it.name }
                        OutlinedTextField(
                            value = uiState.trackerValueQuery,
                            onValueChange = onSetTrackerValueQuery,
                            placeholder = { Text("Search within $selectedTrackerNames...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                            trailingIcon = {
                                if (uiState.trackerValueQuery.isNotEmpty()) {
                                    IconButton(onClick = { onSetTrackerValueQuery("") }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            textStyle = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            
            // Date Range
            item {
                FilterSectionHeader("Date Range")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showDateFromPicker = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Outlined.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(
                                text = uiState.dateFrom?.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) ?: "From",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (uiState.dateFrom != null) MaterialTheme.colorScheme.onSurface
                                       else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (uiState.dateFrom != null) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(16.dp).clickable { onSetDateFrom(null) }
                                )
                            }
                        }
                    }
                    OutlinedCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showDateToPicker = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Outlined.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(
                                text = uiState.dateTo?.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) ?: "To",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (uiState.dateTo != null) MaterialTheme.colorScheme.onSurface
                                       else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (uiState.dateTo != null) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(16.dp).clickable { onSetDateTo(null) }
                                )
                            }
                        }
                    }
                }
                
                // Quick date presets
                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val today = LocalDate.now()
                    AssistChip(onClick = { onSetDateFrom(today); onSetDateTo(today) }, label = { Text("Today") })
                    AssistChip(onClick = { onSetDateFrom(today.minusDays(7)); onSetDateTo(today) }, label = { Text("Last 7 days") })
                    AssistChip(onClick = { onSetDateFrom(today.minusDays(30)); onSetDateTo(today) }, label = { Text("Last 30 days") })
                    AssistChip(onClick = { onSetDateFrom(today.minusMonths(3)); onSetDateTo(today) }, label = { Text("Last 3 months") })
                    AssistChip(onClick = {
                        onSetDateFrom(today.withDayOfYear(1))
                        onSetDateTo(today)
                    }, label = { Text("This year") })
                }
            }
            
            // Word Count
            item {
                FilterSectionHeader("Word Count")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.minWordCount?.toString() ?: "",
                        onValueChange = { onSetMinWordCount(it.toIntOrNull()) },
                        label = { Text("Min") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Text("—", style = MaterialTheme.typography.bodyLarge)
                    OutlinedTextField(
                        value = uiState.maxWordCount?.toString() ?: "",
                        onValueChange = { onSetMaxWordCount(it.toIntOrNull()) },
                        label = { Text("Max") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
    
    // Date pickers
    if (showDateFromPicker) {
        DatePickerDialog(
            initialDate = uiState.dateFrom ?: LocalDate.now(),
            onDateSelected = { onSetDateFrom(it); showDateFromPicker = false },
            onDismiss = { showDateFromPicker = false },
            title = "From date"
        )
    }
    if (showDateToPicker) {
        DatePickerDialog(
            initialDate = uiState.dateTo ?: LocalDate.now(),
            onDateSelected = { onSetDateTo(it); showDateToPicker = false },
            onDismiss = { showDateToPicker = false },
            title = "To date"
        )
    }
    
    // Category picker (single select)
    if (showCategoryPicker) {
        SelectableListDialog(
            title = "Select Category",
            items = uiState.categories.map { SelectableItem(it.id, it.name, it.id == uiState.selectedCategoryId) },
            multiSelect = false,
            onItemToggle = { id -> onSetCategory(id) },
            onDismiss = { showCategoryPicker = false }
        )
    }
    
    // Tag picker
    if (showTagPicker) {
        SelectableListDialog(
            title = "Select Tags",
            items = uiState.allTags.map { SelectableItem(it.id, "#${it.name}", it.id in uiState.selectedTagIds) },
            multiSelect = true,
            onItemToggle = { id -> onToggleTag(id) },
            onDismiss = { showTagPicker = false }
        )
    }
    
    // People picker
    if (showPeoplePicker) {
        SelectableListDialog(
            title = "Select People",
            items = uiState.allPeople.map { SelectableItem(it.id, it.name, it.id in uiState.selectedPersonIds) },
            multiSelect = true,
            onItemToggle = { id -> onTogglePerson(id) },
            onDismiss = { showPeoplePicker = false }
        )
    }
    
    // Tracker picker
    if (showTrackerPicker) {
        SelectableListDialog(
            title = "Select Trackers",
            items = uiState.allTrackers.map { SelectableItem(it.id, it.name, it.id in uiState.selectedTrackerIds) },
            multiSelect = true,
            onItemToggle = { id -> onToggleTracker(id) },
            onDismiss = { showTrackerPicker = false }
        )
    }
}

@Composable
private fun FilterSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    title: String
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(java.time.ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    )
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val date = java.time.Instant.ofEpochMilli(millis)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate()
                    onDateSelected(date)
                }
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = { Text(title, modifier = Modifier.padding(start = 24.dp, top = 16.dp)) }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchResultCard(
    entry: com.dairum.domain.model.JournalEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top row: mood + title + date
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                entry.mood?.let {
                    Text(it.emoji, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.title ?: "Untitled",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = entry.createdAt.format(DateTimeFormatter.ofPattern("MMM d, yyyy · h:mm a")),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (entry.isFavorite) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Red.copy(alpha = 0.7f)
                    )
                }
            }
            
            // Content preview
            if (entry.plainContent.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.plainContent.take(150),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Tracker values (if any)
            if (entry.trackerValues.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    entry.trackerValues.entries.take(3).forEach { (_, value) ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(value.take(20), style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.height(24.dp),
                            icon = { Icon(Icons.Outlined.TrackChanges, contentDescription = null, modifier = Modifier.size(12.dp)) }
                        )
                    }
                }
            }
            
            // Metadata row: tags, people, category, word count, media indicator
            val hasMetadata = entry.tags.isNotEmpty() || entry.people.isNotEmpty() || 
                entry.category != null || entry.wordCount > 0 || entry.media.isNotEmpty()
            if (hasMetadata) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    entry.category?.let { cat ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(cat.name, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.height(24.dp),
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(cat.color))
                                )
                            }
                        )
                    }
                    entry.tags.take(3).forEach { tag ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text("#${tag.name}", style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                    if (entry.tags.size > 3) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text("+${entry.tags.size - 3}", style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                    entry.people.take(2).forEach { person ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(person.name, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.height(24.dp),
                            icon = { Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(12.dp)) }
                        )
                    }
                    if (entry.wordCount > 0) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text("${entry.wordCount}w", style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                    if (entry.media.isNotEmpty()) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text("${entry.media.size} 📎", style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class SelectableItem(val id: Long, val label: String, val selected: Boolean)

@Composable
private fun SelectableListDialog(
    title: String,
    items: List<SelectableItem>,
    multiSelect: Boolean,
    onItemToggle: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = remember(items, searchQuery) {
        if (searchQuery.isBlank()) items
        else items.filter { it.label.contains(searchQuery, ignoreCase = true) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredItems, key = { it.id }) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onItemToggle(item.id)
                                    if (!multiSelect) onDismiss()
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (multiSelect) {
                                Checkbox(checked = item.selected, onCheckedChange = { onItemToggle(item.id) })
                            } else {
                                RadioButton(selected = item.selected, onClick = { onItemToggle(item.id); onDismiss() })
                            }
                            Text(item.label, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        }
    )
}
