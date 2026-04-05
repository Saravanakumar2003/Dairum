package com.dairum.presentation.screens.tag

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.Tag
import com.dairum.presentation.components.EmptyState
import com.dairum.presentation.components.EntryCard
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagEntriesScreen(
    tagId: Long,
    onEntryClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TagEntriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(tagId) {
        viewModel.loadEntriesForTag(tagId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = null,
                                tint = uiState.tag?.color?.let { Color(it) } 
                                    ?: MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = uiState.tag?.name ?: "Tag",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Text(
                            text = "${uiState.entries.size} entries",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = uiState.tag?.color?.let { Color(it).copy(alpha = 0.1f) }
                        ?: MaterialTheme.colorScheme.surface
                )
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
            
            uiState.entries.isEmpty() -> {
                EmptyState(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Tag,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "No Entries",
                    message = "No entries found with this tag",
                    modifier = Modifier.padding(paddingValues)
                )
            }
            
            else -> {
                TagEntriesContent(
                    entries = uiState.entries,
                    tag = uiState.tag,
                    onEntryClick = onEntryClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun TagEntriesContent(
    entries: List<JournalEntry>,
    tag: Tag?,
    onEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Group entries by date
    val groupedEntries = entries.groupBy { entry ->
        entry.createdAt.toLocalDate()
    }.toSortedMap(reverseOrder())
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tag stats card
        item {
            TagStatsCard(
                tag = tag,
                entryCount = entries.size,
                firstUsed = entries.minByOrNull { it.createdAt }?.createdAt,
                lastUsed = entries.maxByOrNull { it.createdAt }?.createdAt
            )
        }
        
        // Entries grouped by date
        groupedEntries.forEach { (date, dateEntries) ->
            item(key = "date_$date") {
                Text(
                    text = formatDateHeader(date),
                    style = MaterialTheme.typography.titleSmall,
                    color = tag?.color?.let { Color(it) } ?: MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(
                items = dateEntries,
                key = { it.id }
            ) { entry ->
                EntryCard(
                    entry = entry,
                    onClick = { onEntryClick(entry.id) },
                    onFavoriteClick = { /* TODO */ }
                )
            }
        }
        
        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun TagStatsCard(
    tag: Tag?,
    entryCount: Int,
    firstUsed: java.time.LocalDateTime?,
    lastUsed: java.time.LocalDateTime?
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = tag?.color?.let { Color(it).copy(alpha = 0.15f) }
                ?: MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tag Statistics",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "#${tag?.name ?: ""}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = tag?.color?.let { Color(it) } ?: MaterialTheme.colorScheme.primary
                    )
                }
                
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "$entryCount",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = tag?.color?.let { Color(it) } ?: MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "entries",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            if (firstUsed != null && lastUsed != null) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "First used",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = firstUsed.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Last used",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = lastUsed.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

private fun formatDateHeader(date: java.time.LocalDate): String {
    val today = java.time.LocalDate.now()
    val yesterday = today.minusDays(1)
    
    return when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
    }
}
