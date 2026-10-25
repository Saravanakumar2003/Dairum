package com.dairum.presentation.screens.gratia

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.AffirmationFolder
import com.dairum.domain.model.Affirmation
import com.dairum.domain.model.GratitudeFolder
import com.dairum.domain.model.GratitudePrompt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GratiaScreen(
    onWriteEntry: (String) -> Unit,
    onPlayAffirmation: (Long) -> Unit,
    onNavigateToBreathing: () -> Unit,
    viewModel: GratiaViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val gratitudeFolders by viewModel.gratitudeFolders.collectAsState()
    val affirmationFolders by viewModel.affirmationFolders.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    
    // Inline detail state
    var selectedGratitudeFolder by remember { mutableStateOf<GratitudeFolder?>(null) }
    var selectedAffirmationFolder by remember { mutableStateOf<AffirmationFolder?>(null) }
    val isDetailView = selectedGratitudeFolder != null || selectedAffirmationFolder != null

    Scaffold(
        floatingActionButton = {
            if (!isDetailView) {
                FloatingActionButton(
                    onClick = { showCreateDialog = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create folder")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isDetailView) {
                // Show inline detail view
                if (selectedGratitudeFolder != null) {
                    InlineGratitudeFolderDetail(
                        folder = selectedGratitudeFolder!!,
                        onBackClick = { selectedGratitudeFolder = null },
                        onWriteClick = { prompt -> onWriteEntry(prompt) },
                        viewModel = viewModel
                    )
                } else if (selectedAffirmationFolder != null) {
                    InlineAffirmationFolderDetail(
                        folder = selectedAffirmationFolder!!,
                        onBackClick = { selectedAffirmationFolder = null },
                        onPlayClick = { folderId -> onPlayAffirmation(folderId) },
                        viewModel = viewModel
                    )
                }
            } else {
                // Header
                Text(
                    text = "Gratia",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                // Tabs
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Gratitude") },
                        icon = { Icon(Icons.Outlined.FavoriteBorder, null) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Affirmation") },
                        icon = { Icon(Icons.Outlined.SelfImprovement, null) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Breathe") },
                        icon = { Icon(Icons.Outlined.Air, null) }
                    )
                }

                when (selectedTab) {
                    0 -> GratitudeTab(
                        folders = gratitudeFolders,
                        onFolderClick = { folder -> selectedGratitudeFolder = folder },
                        onDeleteFolder = { viewModel.deleteGratitudeFolder(it) }
                    )
                    1 -> AffirmationTab(
                        folders = affirmationFolders,
                        onFolderClick = { folder -> selectedAffirmationFolder = folder },
                        onDeleteFolder = { viewModel.deleteAffirmationFolder(it) }
                    )
                    2 -> BreathingTab(
                        onStartBreathing = onNavigateToBreathing
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateFolderDialog(
            isGratitude = selectedTab == 0,
            onDismiss = { showCreateDialog = false },
            onCreate = { name, emoji, description ->
                if (selectedTab == 0) {
                    viewModel.createGratitudeFolder(name, emoji, description)
                } else {
                    viewModel.createAffirmationFolder(name, emoji, description)
                }
                showCreateDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InlineGratitudeFolderDetail(
    folder: GratitudeFolder,
    onBackClick: () -> Unit,
    onWriteClick: (String) -> Unit,
    viewModel: GratiaViewModel
) {
    val prompts by viewModel.currentPrompts.collectAsState()
    val randomPrompt by viewModel.randomPrompt.collectAsState()
    var showAddPromptDialog by remember { mutableStateOf(false) }

    LaunchedEffect(folder.id) {
        viewModel.loadPromptsForFolder(folder.id)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "${folder.emoji} ${folder.name}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showAddPromptDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add prompt")
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Shuffle card
            item {
                Card(
                    onClick = { viewModel.shufflePrompt(folder.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Shuffle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Shuffle & Get a Random Prompt",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Random prompt result
            if (randomPrompt != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "✨", style = MaterialTheme.typography.headlineLarge)
                            Text(
                                text = randomPrompt!!.prompt,
                                style = MaterialTheme.typography.bodyLarge,
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Button(
                                onClick = {
                                    onWriteClick(randomPrompt!!.prompt)
                                    viewModel.clearRandomPrompt()
                                }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Write")
                            }
                        }
                    }
                }
            }

            // Prompt count
            item {
                Text(
                    text = "${prompts.size} prompts",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (prompts.isEmpty()) {
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
                            Text("No prompts yet", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "Add prompts to reflect on",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(prompts, key = { it.id }) { prompt ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = prompt.prompt,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { onWriteClick(prompt.prompt) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit, "Write",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(
                                onClick = { viewModel.deletePrompt(prompt) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Delete, "Delete",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddPromptDialog) {
        var text by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddPromptDialog = false },
            title = { Text("Add Prompt") },
            text = {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Gratitude prompt") },
                    placeholder = { Text("e.g., What made you smile today?") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.addPrompt(folder.id, text); showAddPromptDialog = false },
                    enabled = text.isNotBlank()
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showAddPromptDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InlineAffirmationFolderDetail(
    folder: AffirmationFolder,
    onBackClick: () -> Unit,
    onPlayClick: (Long) -> Unit,
    viewModel: GratiaViewModel
) {
    val affirmations by viewModel.currentAffirmations.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(folder.id) {
        viewModel.loadAffirmationsForFolder(folder.id)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "${folder.emoji} ${folder.name}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add affirmation")
            }
            if (affirmations.isNotEmpty()) {
                FilledTonalIconButton(onClick = { onPlayClick(folder.id) }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "${affirmations.size} affirmations",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (affirmations.isEmpty()) {
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
                                Icons.Outlined.SelfImprovement, null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No affirmations yet", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "Add affirmations to start your practice",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(affirmations, key = { it.id }) { affirmation ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = affirmation.text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.deleteAffirmation(affirmation) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Delete, "Delete",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var text by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Affirmation") },
            text = {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Affirmation text") },
                    placeholder = { Text("e.g., I am worthy of love and happiness") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.addAffirmation(folder.id, text); showAddDialog = false },
                    enabled = text.isNotBlank()
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun GratitudeTab(
    folders: List<GratitudeFolder>,
    onFolderClick: (GratitudeFolder) -> Unit,
    onDeleteFolder: (GratitudeFolder) -> Unit
) {
    if (folders.isEmpty()) {
        EmptyState(
            icon = Icons.Outlined.FavoriteBorder,
            title = "No gratitude folders yet",
            subtitle = "Create a folder to start your gratitude practice"
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(folders, key = { it.id }) { folder ->
                FolderCard(
                    emoji = folder.emoji,
                    name = folder.name,
                    description = folder.description,
                    onClick = { onFolderClick(folder) },
                    onDelete = { onDeleteFolder(folder) }
                )
            }
        }
    }
}

@Composable
private fun AffirmationTab(
    folders: List<AffirmationFolder>,
    onFolderClick: (AffirmationFolder) -> Unit,
    onDeleteFolder: (AffirmationFolder) -> Unit
) {
    if (folders.isEmpty()) {
        EmptyState(
            icon = Icons.Outlined.SelfImprovement,
            title = "No affirmation folders yet",
            subtitle = "Create a folder to organize your affirmations"
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(folders, key = { it.id }) { folder ->
                FolderCard(
                    emoji = folder.emoji,
                    name = folder.name,
                    description = folder.description,
                    onClick = { onFolderClick(folder) },
                    onDelete = { onDeleteFolder(folder) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderCard(
    emoji: String,
    name: String,
    description: String?,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete folder?") },
            text = { Text("This will delete the folder and all its contents.") },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteConfirm = false }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateFolderDialog(
    isGratitude: Boolean,
    onDismiss: () -> Unit,
    onCreate: (name: String, emoji: String, description: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf(if (isGratitude) "🙏" else "✨") }
    var description by remember { mutableStateOf("") }

    val emojiOptions = if (isGratitude) {
        listOf("🙏", "💛", "🌟", "🌈", "🌻", "❤️", "🕊️", "🌺", "🍀", "☀️")
    } else {
        listOf("✨", "💪", "🦋", "🔥", "💎", "🌊", "⭐", "🎯", "🏆", "💫")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isGratitude) "New Gratitude Folder" else "New Affirmation Folder") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Choose an icon", style = MaterialTheme.typography.labelMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    emojiOptions.take(5).forEach { e ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (emoji == e) MaterialTheme.colorScheme.primaryContainer
                                   else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { emoji = e }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = e, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    emojiOptions.drop(5).forEach { e ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (emoji == e) MaterialTheme.colorScheme.primaryContainer
                                   else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { emoji = e }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = e, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onCreate(name, emoji, description.ifBlank { null }) },
                enabled = name.isNotBlank()
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun BreathingTab(
    onStartBreathing: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(text = "🌬️", style = MaterialTheme.typography.displayLarge)
            Text(
                text = "Breathing Exercises",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text(
                text = "Practice mindful breathing with guided techniques. Choose from Box Breathing, 4-7-8, and more.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Technique previews
            val techniques = listOf(
                "📦" to "Box Breathing",
                "😌" to "4-7-8 Relaxing",
                "🎨" to "Custom"
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                techniques.chunked(3).forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { (emoji, name) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerLow,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(emoji, style = MaterialTheme.typography.headlineSmall)
                                    Text(
                                        name,
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onStartBreathing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.PlayArrow, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Start Breathing Exercise",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }
    }
}
