package com.dairum.presentation.screens.ai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.GuardrailRule

/**
 * Lets the user see, edit, and add/remove the guardrails that constrain the AI chat's behavior,
 * plus edit the underlying system prompt directly.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIGuardrailsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AIGuardrailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Guardrails") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "System Prompt",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "This is the base instruction the AI always follows. Edit it to change its personality or focus.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.editedSystemPrompt,
                    onValueChange = viewModel::updateEditedSystemPrompt,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 6,
                    maxLines = 12,
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = viewModel::saveSystemPrompt) {
                        Text("Save")
                    }
                    OutlinedButton(onClick = viewModel::resetSystemPrompt) {
                        Icon(Icons.Filled.Restore, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset to default")
                    }
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "Guardrail Rules",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Strict rules appended to every request. Toggle, remove, or add your own to control hallucination, tone, and length.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(uiState.settings.rules, key = { it.id }) { rule ->
                GuardrailRuleCard(
                    rule = rule,
                    onToggle = { viewModel.toggleRule(rule) },
                    onRemove = { viewModel.removeRule(rule) }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.newRuleText,
                        onValueChange = viewModel::updateNewRuleText,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Add a new guardrail rule...") },
                        minLines = 1,
                        maxLines = 3
                    )
                    FilledIconButton(
                        onClick = viewModel::addRule,
                        enabled = uiState.newRuleText.isNotBlank()
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add rule")
                    }
                }
            }

            item {
                TextButton(onClick = viewModel::resetToDefaults) {
                    Icon(Icons.Filled.Restore, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reset all guardrails to defaults")
                }
            }
        }
    }
}

@Composable
private fun GuardrailRuleCard(
    rule: GuardrailRule,
    onToggle: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (rule.isEnabled) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rule.text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (rule.isEnabled) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                },
                modifier = Modifier.weight(1f)
            )
            Switch(checked = rule.isEnabled, onCheckedChange = { onToggle() })
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Remove rule",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}
