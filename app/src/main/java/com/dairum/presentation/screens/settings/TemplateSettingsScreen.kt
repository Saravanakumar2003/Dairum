package com.dairum.presentation.screens.settings

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.EntryTemplate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateSettingsScreen(
    onBack: () -> Unit,
    viewModel: TemplateSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var editingTemplate by remember { mutableStateOf<EntryTemplate?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom Templates") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showCreateDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Template")
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
            // Built-in templates section
            item {
                Text(
                    text = "Built-in Templates",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            items(EntryTemplate.BUILT_IN_TEMPLATES) { template ->
                TemplateCard(
                    template = template,
                    isBuiltIn = true,
                    onEdit = null,
                    onDelete = null
                )
            }
            
            // Custom templates section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Custom Templates",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            if (uiState.customTemplates.isEmpty()) {
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
                                imageVector = Icons.Outlined.Description,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No custom templates yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { showCreateDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Create Template")
                            }
                        }
                    }
                }
            } else {
                items(uiState.customTemplates) { template ->
                    TemplateCard(
                        template = template,
                        isBuiltIn = false,
                        onEdit = { editingTemplate = template },
                        onDelete = { viewModel.deleteTemplate(template.id) }
                    )
                }
            }
        }
    }
    
    // Create/Edit dialog
    if (showCreateDialog || editingTemplate != null) {
        TemplateEditDialog(
            template = editingTemplate,
            onSave = { name, icon, title, content ->
                if (editingTemplate != null) {
                    viewModel.updateTemplate(
                        editingTemplate!!.copy(
                            name = name,
                            icon = icon,
                            defaultTitle = title,
                            promptText = content
                        )
                    )
                } else {
                    viewModel.createTemplate(name, icon, title, content)
                }
                showCreateDialog = false
                editingTemplate = null
            },
            onDismiss = {
                showCreateDialog = false
                editingTemplate = null
            }
        )
    }
}

@Composable
private fun TemplateCard(
    template: EntryTemplate,
    isBuiltIn: Boolean,
    onEdit: (() -> Unit)?,
    onDelete: (() -> Unit)?
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
            // Icon
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = template.icon,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.titleMedium
                )
                template.description?.let { desc ->
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                template.defaultTitle?.let { title ->
                    Text(
                        text = "Default title: $title",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Actions
            if (!isBuiltIn) {
                onEdit?.let {
                    IconButton(onClick = it) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
                onDelete?.let {
                    IconButton(onClick = it) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            } else {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "Built-in",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TemplateEditDialog(
    template: EntryTemplate?,
    onSave: (name: String, icon: String, title: String, content: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(template?.name ?: "") }
    var icon by remember { mutableStateOf(template?.icon ?: "📝") }
    var defaultTitle by remember { mutableStateOf(template?.defaultTitle ?: "") }
    var promptText by remember { mutableStateOf(template?.promptText ?: "") }
    
    val commonIcons = listOf("📝", "📓", "💭", "🎯", "💡", "🔥", "⭐", "❤️", "🌟", "✨", "📖", "🎨")
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(if (template == null) "Create Template" else "Edit Template")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icon selection
                Text(
                    text = "Icon",
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    commonIcons.take(6).forEach { emoji ->
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = if (icon == emoji) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { icon = emoji }
                        ) {
                            Text(
                                text = emoji,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    commonIcons.drop(6).forEach { emoji ->
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = if (icon == emoji) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { icon = emoji }
                        ) {
                            Text(
                                text = emoji,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
                
                // Template name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Template Name") },
                    placeholder = { Text("e.g., Weekly Reflection") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Default title
                OutlinedTextField(
                    value = defaultTitle,
                    onValueChange = { defaultTitle = it },
                    label = { Text("Default Entry Title") },
                    placeholder = { Text("e.g., My Weekly Reflection") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Prompt text
                OutlinedTextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    label = { Text("Template Content") },
                    placeholder = { Text("Enter your template prompts...\n\nUse line breaks for structure") },
                    minLines = 5,
                    maxLines = 8,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, icon, defaultTitle, promptText) },
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
