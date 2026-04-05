package com.dairum.presentation.screens.manage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.R
import com.dairum.domain.model.CustomTracker
import com.dairum.domain.model.TrackerType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTrackersScreen(
    onBackClick: () -> Unit,
    viewModel: ManageTrackersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTracker by remember { mutableStateOf<CustomTracker?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_manage_trackers)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Tracker")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Built-in Trackers",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(uiState.trackers.filter { it.isBuiltIn }) { tracker ->
                TrackerItem(
                    tracker = tracker,
                    onToggle = { viewModel.toggleActive(tracker.id, !tracker.isActive) },
                    onEditClick = null,
                    onDeleteClick = null
                )
            }

            item {
                Text(
                    text = "Custom Trackers",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(uiState.trackers.filter { !it.isBuiltIn }) { tracker ->
                TrackerItem(
                    tracker = tracker,
                    onToggle = { viewModel.toggleActive(tracker.id, !tracker.isActive) },
                    onEditClick = { editingTracker = tracker },
                    onDeleteClick = { viewModel.deleteTracker(tracker.id) }
                )
            }
        }
    }

    if (showAddDialog) {
        TrackerEditDialog(
            tracker = null,
            onDismiss = { showAddDialog = false },
            onSave = { name, type, unit ->
                viewModel.createTracker(name, type, unit)
                showAddDialog = false
            }
        )
    }

    editingTracker?.let { tracker ->
        TrackerEditDialog(
            tracker = tracker,
            onDismiss = { editingTracker = null },
            onSave = { name, type, unit ->
                viewModel.updateTracker(tracker.copy(name = name, type = type, unit = unit))
                editingTracker = null
            }
        )
    }
}

@Composable
private fun TrackerItem(
    tracker: CustomTracker,
    onToggle: () -> Unit,
    onEditClick: (() -> Unit)?,
    onDeleteClick: (() -> Unit)?
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (tracker.type) {
                    TrackerType.NUMBER -> Icons.Default.Numbers
                    TrackerType.TEXT -> Icons.Default.TextFields
                    TrackerType.BOOLEAN -> Icons.Default.CheckBox
                    TrackerType.RATING -> Icons.Default.Star
                    TrackerType.WEIGHT -> Icons.Default.FitnessCenter
                    TrackerType.PEOPLE -> Icons.Default.People
                    TrackerType.SELECT -> Icons.Default.List
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tracker.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = tracker.type.name.lowercase().replaceFirstChar { it.uppercase() } +
                            (tracker.unit?.let { " ($it)" } ?: ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = tracker.isActive,
                onCheckedChange = { onToggle() }
            )

            if (onEditClick != null || onDeleteClick != null) {
                var showMenu by remember { mutableStateOf(false) }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        onEditClick?.let {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    showMenu = false
                                    it()
                                },
                                leadingIcon = { Icon(Icons.Default.Edit, null) }
                            )
                        }
                        onDeleteClick?.let {
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    showMenu = false
                                    it()
                                },
                                leadingIcon = { Icon(Icons.Default.Delete, null) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackerEditDialog(
    tracker: CustomTracker?,
    onDismiss: () -> Unit,
    onSave: (name: String, type: TrackerType, unit: String?) -> Unit
) {
    var name by remember { mutableStateOf(tracker?.name ?: "") }
    var selectedType by remember { mutableStateOf(tracker?.type ?: TrackerType.NUMBER) }
    var unit by remember { mutableStateOf(tracker?.unit ?: "") }
    var expandedType by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (tracker == null) "New Tracker" else "Edit Tracker") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expandedType,
                    onExpandedChange = { expandedType = it }
                ) {
                    OutlinedTextField(
                        value = selectedType.name.lowercase().replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        listOf(TrackerType.NUMBER, TrackerType.TEXT, TrackerType.BOOLEAN, TrackerType.RATING)
                            .forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                    onClick = {
                                        selectedType = type
                                        expandedType = false
                                    }
                                )
                            }
                    }
                }

                if (selectedType == TrackerType.NUMBER) {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Unit (optional)") },
                        placeholder = { Text("e.g., kg, steps, hours") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, selectedType, unit.takeIf { it.isNotBlank() }) },
                enabled = name.isNotBlank()
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
