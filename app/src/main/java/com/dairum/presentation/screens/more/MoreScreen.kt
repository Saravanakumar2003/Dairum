package com.dairum.presentation.screens.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    onNavigateToFiles: () -> Unit,
    @Suppress("UNUSED_PARAMETER") onNavigateToSettings: () -> Unit,
    onNavigateToLocationMap: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToTags: () -> Unit,
    onNavigateToTrackers: () -> Unit,
    onNavigateToPeopleDirectory: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToAIChat: () -> Unit = {},
    onNavigateToAIReflections: () -> Unit = {},
    onNavigateToAIPatterns: () -> Unit = {},
    onNavigateToAISettings: () -> Unit = {},
    onNavigateToAIGuardrails: () -> Unit = {},
    onNavigateToContextualSearch: () -> Unit = {},
    onNavigateToTemplates: () -> Unit = {},
    onNavigateToExport: () -> Unit = {},
    onNavigateToBackupRestore: () -> Unit = {},
    onNavigateToAutomation: () -> Unit = {},
    onNavigateToSecurity: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToApiKeys: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToTrash: () -> Unit = {},
    onNavigateToGratiaSettings: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {}
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
                MoreMenuItem(
                    icon = Icons.Outlined.Shield,
                    title = "AI Guardrails",
                    subtitle = "Edit the system prompt and hallucination guardrails",
                    onClick = onNavigateToAIGuardrails
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
                    icon = Icons.Outlined.ManageSearch,
                    title = "Advanced Search",
                    subtitle = "Search by text, tags, people, dates, mood & more",
                    onClick = onNavigateToSearch
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.TravelExplore,
                    title = "Contextual Search",
                    subtitle = "Find entries by memory, key event, person, or place",
                    onClick = onNavigateToContextualSearch
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
            
            // ============ TEMPLATES & EXPORT ============
            item {
                Text(
                    "Templates & Export",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Description,
                    title = "Custom Templates",
                    subtitle = "Create your own journal templates",
                    onClick = onNavigateToTemplates
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.FileDownload,
                    title = "Export Entries",
                    subtitle = "Export to PDF or Text file",
                    onClick = onNavigateToExport
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Backup,
                    title = "Backup & Restore",
                    subtitle = "Full app data backup",
                    onClick = onNavigateToBackupRestore
                )
            }
            
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
            
            // ============ SETTINGS ============
            item {
                Text(
                    "Settings",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.SelfImprovement,
                    title = "TTS & Media Settings",
                    subtitle = "TTS voice, speed, background music",
                    onClick = onNavigateToGratiaSettings
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.AutoAwesome,
                    title = "Automation",
                    subtitle = "Tasker, MacroDroid integration",
                    onClick = onNavigateToAutomation
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Lock,
                    title = "Security",
                    subtitle = "App lock, biometric authentication",
                    onClick = onNavigateToSecurity
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifications",
                    subtitle = "Reminders, birthdays, anniversaries",
                    onClick = onNavigateToNotifications
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Key,
                    title = "API Keys",
                    subtitle = "Configure Maps and Calendar API keys",
                    onClick = onNavigateToApiKeys
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Info,
                    title = "About",
                    subtitle = "Version info and credits",
                    onClick = onNavigateToAbout
                )
            }
            
            item {
                MoreMenuItem(
                    icon = Icons.Outlined.Delete,
                    title = "Trash",
                    subtitle = "Recently deleted entries",
                    onClick = onNavigateToTrash
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon in a tinted container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
