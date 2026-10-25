package com.dairum.presentation.screens.gratia

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.Affirmation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffirmationFolderDetailScreen(
    onBackClick: () -> Unit,
    onPlayClick: (Long) -> Unit,
    viewModel: AffirmationFolderViewModel = hiltViewModel()
) {
    val folder by viewModel.folder.collectAsState()
    val affirmations by viewModel.affirmations.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateBackgroundAudio(it.toString()) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("${folder?.emoji ?: "✨"} ${folder?.name ?: "Affirmations"}")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add affirmation")
                    }
                }
            )
        },
        floatingActionButton = {
            if (affirmations.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { folder?.let { onPlayClick(it.id) } },
                    icon = { Icon(Icons.Default.PlayArrow, null) },
                    text = { Text("Play") },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Background audio setting
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Background Audio",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = if (folder?.backgroundAudioUri != null) "Audio set"
                                       else "Tap to add calming background music",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (folder?.backgroundAudioUri != null) {
                            IconButton(onClick = { viewModel.updateBackgroundAudio(null) }) {
                                Icon(Icons.Default.Close, "Remove audio",
                                    modifier = Modifier.size(18.dp))
                            }
                        }
                        IconButton(onClick = { audioPickerLauncher.launch("audio/*") }) {
                            Icon(Icons.Default.FileOpen, "Select audio")
                        }
                    }
                }
            }

            // Stats
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
                                Icons.Outlined.SelfImprovement,
                                contentDescription = null,
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
                    AffirmationCard(
                        affirmation = affirmation,
                        onDelete = { viewModel.deleteAffirmation(affirmation) },
                        onToggleAudio = { uri ->
                            viewModel.updateAffirmation(affirmation.copy(audioUri = uri))
                        },
                        onToggleTts = {
                            viewModel.updateAffirmation(
                                affirmation.copy(useTts = !affirmation.useTts)
                            )
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddAffirmationDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { text ->
                viewModel.addAffirmation(text)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AffirmationCard(
    affirmation: Affirmation,
    onDelete: () -> Unit,
    onToggleAudio: (String?) -> Unit,
    onToggleTts: () -> Unit
) {
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onToggleAudio(uri?.toString())
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = affirmation.text,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TTS toggle
                FilterChip(
                    selected = affirmation.useTts,
                    onClick = onToggleTts,
                    label = { Text("TTS", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = if (affirmation.useTts) {
                        { Icon(Icons.Default.VolumeUp, null, Modifier.size(16.dp)) }
                    } else null,
                    modifier = Modifier.height(28.dp)
                )

                // Audio
                if (affirmation.audioUri != null) {
                    FilterChip(
                        selected = true,
                        onClick = { onToggleAudio(null) },
                        label = { Text("Audio", style = MaterialTheme.typography.labelSmall) },
                        leadingIcon = { Icon(Icons.Default.Audiotrack, null, Modifier.size(16.dp)) },
                        modifier = Modifier.height(28.dp)
                    )
                } else {
                    AssistChip(
                        onClick = { audioPickerLauncher.launch("audio/*") },
                        label = { Text("Add Audio", style = MaterialTheme.typography.labelSmall) },
                        leadingIcon = { Icon(Icons.Default.Add, null, Modifier.size(16.dp)) },
                        modifier = Modifier.height(28.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun AddAffirmationDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
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
                onClick = { onAdd(text) },
                enabled = text.isNotBlank()
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
