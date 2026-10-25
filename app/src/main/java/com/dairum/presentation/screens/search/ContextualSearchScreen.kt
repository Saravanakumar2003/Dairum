package com.dairum.presentation.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.presentation.components.EntryCard

/**
 * "Contextual Search" (More tab): find diary entries by memory, keyword, or key event involving
 * a person, location, tag, or date - not just plain text matching.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContextualSearchScreen(
    onNavigateBack: () -> Unit,
    onEntryClick: (Long) -> Unit,
    viewModel: ContextualSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contextual Search") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::updateQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search by memory, keyword, person, place...") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                singleLine = true
            )

            when {
                uiState.query.isBlank() -> {
                    EmptyPrompt(
                        text = "Try \"beach with Priya\", \"Thanjavur\", or \"birthday 2025\" - " +
                            "this searches content, tags, people, locations, and dates together."
                    )
                }
                uiState.isSearching -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.hasSearched && uiState.results.isEmpty() -> {
                    EmptyPrompt(text = "No entries matched \"${uiState.query}\".")
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.results, key = { it.entry.id }) { match ->
                            Column {
                                EntryCard(
                                    entry = match.entry,
                                    onClick = { onEntryClick(match.entry.id) },
                                    onFavoriteClick = {
                                        viewModel.toggleFavorite(match.entry.id, !match.entry.isFavorite)
                                    }
                                )
                                if (match.matchedOn.isNotEmpty()) {
                                    Text(
                                        text = "Matched: ${match.matchedOn.take(4).joinToString(" • ")}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyPrompt(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
