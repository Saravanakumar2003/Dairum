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
import com.dairum.domain.model.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTagsScreen(
    onBackClick: () -> Unit,
    viewModel: ManageTagsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTag by remember { mutableStateOf<Tag?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_manage_tags)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Tag")
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
            items(uiState.tags) { tag ->
                TagItem(
                    tag = tag,
                    onEditClick = { editingTag = tag },
                    onDeleteClick = { viewModel.deleteTag(tag.id) }
                )
            }
        }
    }

    if (showAddDialog) {
        TagEditDialog(
            tag = null,
            onDismiss = { showAddDialog = false },
            onSave = { name ->
                viewModel.createTag(name)
                showAddDialog = false
            }
        )
    }

    editingTag?.let { tag ->
        TagEditDialog(
            tag = tag,
            onDismiss = { editingTag = null },
            onSave = { name ->
                viewModel.updateTag(tag.copy(name = name))
                editingTag = null
            }
        )
    }
}

@Composable
private fun TagItem(
    tag: Tag,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
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
                Icons.Default.Tag,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "#${tag.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${tag.usageCount} entries",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
private fun TagEditDialog(
    tag: Tag?,
    onDismiss: () -> Unit,
    onSave: (name: String) -> Unit
) {
    var name by remember { mutableStateOf(tag?.name ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (tag == null) "New Tag" else "Edit Tag") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it.replace(" ", "_").lowercase() },
                label = { Text("Tag name") },
                prefix = { Text("#") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name) },
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
