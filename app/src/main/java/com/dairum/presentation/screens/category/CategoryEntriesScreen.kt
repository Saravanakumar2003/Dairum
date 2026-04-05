package com.dairum.presentation.screens.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.Category
import com.dairum.presentation.components.EmptyState
import com.dairum.presentation.components.EntryCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEntriesScreen(
    categoryId: Long,
    onBackClick: () -> Unit,
    onEntryClick: (Long) -> Unit,
    viewModel: CategoryEntriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(categoryId) {
        viewModel.loadCategory(categoryId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        uiState.category?.let { category ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        Color(category.color).copy(alpha = 0.2f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = getCategoryEmoji(category),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Text(category.name)
                        } ?: Text("Category")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
            uiState.error != null -> {
                EmptyState(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Error,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    title = "Error",
                    message = uiState.error ?: "Failed to load category"
                )
            }
            uiState.entries.isEmpty() -> {
                EmptyState(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.FolderOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "No entries",
                    message = "No journal entries in this category yet"
                )
            }
            else -> {
                // Group entries by date
                val groupedEntries = uiState.entries.groupBy { entry ->
                    entry.createdAt.toLocalDate()
                }.toSortedMap(compareByDescending { it })
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Category stats header
                    item {
                        CategoryStatsHeader(
                            category = uiState.category!!,
                            entryCount = uiState.entries.size
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    groupedEntries.forEach { (date, entries) ->
                        item(key = "header_$date") {
                            Text(
                                text = formatDateHeader(date),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(
                            items = entries,
                            key = { it.id }
                        ) { entry ->
                            EntryCard(
                                entry = entry,
                                onClick = { onEntryClick(entry.id) },
                                onFavoriteClick = { viewModel.toggleFavorite(entry.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryStatsHeader(
    category: Category,
    entryCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(category.color).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = entryCount.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(category.color)
                )
                Text(
                    text = "Entries",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatDateHeader(date: LocalDate): String {
    val today = LocalDate.now()
    return when {
        date == today -> "Today"
        date == today.minusDays(1) -> "Yesterday"
        date.year == today.year -> date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))
        else -> date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
    }
}

// Helper function to get emoji for a category
private fun getCategoryEmoji(category: Category): String {
    // Check if icon field has an emoji
    if (category.icon.length <= 2 && category.icon.any { it.code > 0x1F300 }) {
        return category.icon
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
