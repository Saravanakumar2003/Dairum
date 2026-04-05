package com.dairum.presentation.screens.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    onNavigateToFiles: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToLocationMap: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToTags: () -> Unit,
    onNavigateToTrackers: () -> Unit,
    onNavigateToPeopleDirectory: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToAIChat: () -> Unit = {},
    onNavigateToAIReflections: () -> Unit = {},
    onNavigateToAIPatterns: () -> Unit = {},
    onNavigateToAISettings: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("More") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ============ AI FEATURES ============
            item {
                Text(
                    "AI Features",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Chat,
                    title = "AI Chat",
                    subtitle = "Ask questions about your journal entries",
                    onClick = onNavigateToAIChat
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Psychology,
                    title = "Reflections",
                    subtitle = "Weekly reflections and summaries",
                    onClick = onNavigateToAIReflections
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.TrendingUp,
                    title = "Patterns",
                    subtitle = "Discover patterns in your journaling",
                    onClick = onNavigateToAIPatterns
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.SmartToy,
                    title = "AI Settings",
                    subtitle = "Manage AI models and embeddings",
                    onClick = onNavigateToAISettings
                )
            }
            
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
            
            // ============ CONTENT ============
            item {
                Text(
                    "Content",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Favorite,
                    title = "Favorites",
                    subtitle = "Your hearted and bookmarked entries",
                    onClick = onNavigateToFavorites
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Folder,
                    title = "Files",
                    subtitle = "View photos, videos, voice notes & attachments",
                    onClick = onNavigateToFiles
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Map,
                    title = "Location Map",
                    subtitle = "View all entries on a map",
                    onClick = onNavigateToLocationMap
                )
            }
            
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
            
            // ============ ORGANIZE ============
            item {
                Text(
                    "Organize",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Category,
                    title = "Categories",
                    subtitle = "Organize entries with color-coded categories",
                    onClick = onNavigateToCategories
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Tag,
                    title = "Tags",
                    subtitle = "Create and manage tags for entries",
                    onClick = onNavigateToTags
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.TrackChanges,
                    title = "Trackers",
                    subtitle = "Custom metrics like mood, habits, activities",
                    onClick = onNavigateToTrackers
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.People,
                    title = "People Directory",
                    subtitle = "Manage people with notes, contact info & photos",
                    onClick = onNavigateToPeopleDirectory
                )
            }
            
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
            
            // ============ SETTINGS ============
            item {
                Text(
                    "Settings & Preferences",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Settings,
                    title = "Settings",
                    subtitle = "Templates, backup, security & more",
                    onClick = onNavigateToSettings
                )
            }
        }
    }
}

@Composable
private fun MoreMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    subtitle,
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
