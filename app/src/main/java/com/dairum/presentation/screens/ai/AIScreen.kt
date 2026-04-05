package com.dairum.presentation.screens.ai

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import timber.log.Timber
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIScreen(
    viewModel: AIViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    
    // Import state
    var showImportDialog by remember { mutableStateOf(false) }
    var importModelType by remember { mutableStateOf(ModelType.LLM) }
    var importModelName by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    
    // File picker for LLM models (.gguf)
    val llmFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            selectedUri = it
            importModelType = ModelType.LLM
            importModelName = ""
            showImportDialog = true
        }
    }
    
    // File picker for embedding models (.onnx)
    val embeddingFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            selectedUri = it
            importModelType = ModelType.EMBEDDING
            importModelName = ""
            showImportDialog = true
        }
    }
    
    LaunchedEffect(Unit) {
        viewModel.initializeIfNeeded()
    }
    
    val context = LocalContext.current
    
    // Share conversation function
    fun shareConversation() {
        val shareText = viewModel.getShareableConversation()
        if (shareText.isNotBlank()) {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(sendIntent, "Share Conversation"))
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Assistant") },
                actions = {
                    // Share button - only visible when there are messages
                    if (uiState.conversationMessages.isNotEmpty() && uiState.selectedTab == AITab.CHAT) {
                        IconButton(onClick = { shareConversation() }) {
                            Icon(Icons.Default.Share, contentDescription = "Share Chat")
                        }
                    }
                    IconButton(onClick = { viewModel.clearConversation() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "New Chat")
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
            // Tab Row
            TabRow(
                selectedTabIndex = uiState.selectedTab.ordinal
            ) {
                AITab.entries.forEach { tab ->
                    Tab(
                        selected = uiState.selectedTab == tab,
                        onClick = { viewModel.selectTab(tab) },
                        text = { Text(tab.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        icon = {
                            Icon(
                                when (tab) {
                                    AITab.CHAT -> Icons.Outlined.Chat
                                    AITab.REFLECTIONS -> Icons.Outlined.Psychology
                                    AITab.PATTERNS -> Icons.Outlined.TrendingUp
                                    AITab.MODELS -> Icons.Outlined.Settings
                                },
                                contentDescription = null
                            )
                        }
                    )
                }
            }
            
            // Content based on selected tab
            AnimatedContent(
                targetState = uiState.selectedTab,
                modifier = Modifier.weight(1f)
            ) { tab ->
                when (tab) {
                    AITab.CHAT -> ChatTab(
                        uiState = uiState,
                        onQueryChange = viewModel::updateQuery,
                        onSendMessage = viewModel::sendMessage,
                        onCopyMessage = { message ->
                            viewModel.getShareableMessage(message)
                        }
                    )
                    AITab.REFLECTIONS -> ReflectionsTab(
                        uiState = uiState,
                        onGenerateReflection = viewModel::generateWeeklyReflection,
                        onGenerateSummary = viewModel::generateMonthlySummary,
                        onDeleteReflection = viewModel::deleteReflection,
                        onDeleteSummary = viewModel::deleteSummary
                    )
                    AITab.PATTERNS -> PatternsTab(
                        uiState = uiState,
                        onDetectPatterns = viewModel::detectPatterns,
                        onDeletePattern = viewModel::deletePattern
                    )
                    AITab.MODELS -> ModelsTab(
                        uiState = uiState,
                        onDownloadModel = viewModel::downloadModel,
                        onSelectLLM = viewModel::selectLLMModel,
                        onSelectEmbedding = viewModel::selectEmbeddingModel,
                        onDeleteModel = viewModel::deleteModel,
                        onGenerateEmbeddings = viewModel::generateAllEmbeddings,
                        onImportLLM = { llmFilePicker.launch(arrayOf("*/*")) },
                        onImportEmbedding = { embeddingFilePicker.launch(arrayOf("*/*")) }
                    )
                }
            }
            
            // Import Model Dialog
            if (showImportDialog && selectedUri != null) {
                AlertDialog(
                    onDismissRequest = { 
                        showImportDialog = false
                        selectedUri = null
                    },
                    title = {
                        Text("Import ${if (importModelType == ModelType.LLM) "LLM" else "Embedding"} Model")
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                "Enter a name for this model:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            OutlinedTextField(
                                value = importModelName,
                                onValueChange = { importModelName = it },
                                label = { Text("Model Name") },
                                placeholder = { Text("e.g., TinyLlama Custom") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                "The model will be copied to app storage.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                selectedUri?.let { uri ->
                                    viewModel.importModel(uri, importModelName, importModelType)
                                }
                                showImportDialog = false
                                selectedUri = null
                            }
                        ) {
                            Text("Import")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { 
                            showImportDialog = false
                            selectedUri = null
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }
            
            // Error snackbar
            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Dismiss")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatTab(
    uiState: AIUiState,
    onQueryChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onCopyMessage: (ChatMessage) -> String
) {
    val listState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.conversationMessages.size) {
        if (uiState.conversationMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.conversationMessages.size - 1)
        }
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Warning when native LLM is not available
        if (!uiState.isNativeLLMAvailable) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "AI Chat Unavailable",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            "Native LLM library is not installed. Import embedding models under Models tab for semantic search functionality.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
        
        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.conversationMessages.isEmpty()) {
                item {
                    EmptyConversationPrompt()
                }
            }
            
            items(uiState.conversationMessages) { message ->
                ChatBubble(
                    message = message,
                    onLongPress = {
                        val text = onCopyMessage(message)
                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(text))
                        // Show toast feedback
                        android.widget.Toast.makeText(context, "Copied to clipboard", android.widget.Toast.LENGTH_SHORT).show()
                    }
                )
            }
            
            // Streaming response
            if (uiState.isGenerating && uiState.streamingResponse.isNotBlank()) {
                item {
                    ChatBubble(
                        message = ChatMessage(
                            role = "assistant",
                            content = uiState.streamingResponse,
                            isStreaming = true
                        )
                    )
                }
            }
            
            // Loading indicator
            if (uiState.isGenerating && uiState.streamingResponse.isBlank()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Text(
                                "Thinking...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // Input field
        ChatInput(
            query = uiState.currentQuery,
            onQueryChange = onQueryChange,
            onSend = onSendMessage,
            isEnabled = !uiState.isGenerating && uiState.isNativeLLMAvailable
        )
    }
}

@Composable
private fun EmptyConversationPrompt() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.Psychology,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Ask Your Journal",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Ask questions about your entries, moods, and patterns. The AI will search your journal for relevant context.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // Example prompts
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ExamplePrompt("When was I happiest last month?")
            ExamplePrompt("What activities improve my mood?")
            ExamplePrompt("Summarize my week")
        }
    }
}

@Composable
private fun ExamplePrompt(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Text(
            text = "\"$text\"",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatBubble(
    message: ChatMessage,
    onLongPress: () -> Unit = {}
) {
    val isUser = message.role == "user"
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = onLongPress
                ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                
                if (message.sources.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Based on ${message.sources.size} entries",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                
                if (message.isStreaming) {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatInput(
    query: String,
    onQueryChange: (String) -> Unit,
    onSend: () -> Unit,
    isEnabled: Boolean
) {
    Surface(
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask your journal...") },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                enabled = isEnabled,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() })
            )
            
            FilledIconButton(
                onClick = onSend,
                enabled = isEnabled && query.isNotBlank()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
private fun ReflectionsTab(
    uiState: AIUiState,
    onGenerateReflection: () -> Unit,
    onGenerateSummary: () -> Unit,
    onDeleteReflection: (WeeklyReflection) -> Unit,
    onDeleteSummary: (SmartSummary) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Error message
        if (uiState.error != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
        
        item {
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onGenerateReflection,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Weekly Reflection")
                }
                
                OutlinedButton(
                    onClick = onGenerateSummary,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Summarize, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Monthly Summary")
                }
            }
        }
        
        if (uiState.isLoading) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        "Generating content...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "This may take up to 90 seconds on-device",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // Weekly reflections
        if (uiState.weeklyReflections.isNotEmpty()) {
            item {
                Text(
                    "Recent Reflections",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(uiState.weeklyReflections) { reflection ->
                ReflectionCard(
                    reflection = reflection,
                    onDelete = { onDeleteReflection(reflection) }
                )
            }
        }
        
        // Summaries
        if (uiState.summaries.isNotEmpty()) {
            item {
                Text(
                    "Summaries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(uiState.summaries) { summary ->
                SummaryCard(
                    summary = summary,
                    onDelete = { onDeleteSummary(summary) }
                )
            }
        }
        
        // Empty state
        if (!uiState.isLoading && uiState.weeklyReflections.isEmpty() && uiState.summaries.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.Psychology,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No reflections yet",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Generate a weekly reflection to get AI insights",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ReflectionCard(
    reflection: WeeklyReflection,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d")
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Reflection?") },
            text = { Text("This will permanently delete this weekly reflection.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Week of ${reflection.weekStartDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Mood trend indicator
                    Icon(
                        when (reflection.moodTrend.trend) {
                            TrendDirection.IMPROVING -> Icons.Default.TrendingUp
                            TrendDirection.DECLINING -> Icons.Default.TrendingDown
                            TrendDirection.STABLE -> Icons.Default.TrendingFlat
                            TrendDirection.FLUCTUATING -> Icons.Default.ShowChart
                        },
                        contentDescription = null,
                        tint = when (reflection.moodTrend.trend) {
                            TrendDirection.IMPROVING -> Color(0xFF4CAF50)
                            TrendDirection.DECLINING -> Color(0xFFF44336)
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            Text(
                reflection.summary.take(200) + if (reflection.summary.length > 200) "..." else "",
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Key themes
            if (reflection.keyThemes.isNotEmpty()) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    reflection.keyThemes.take(3).forEach { theme ->
                        AssistChip(
                            onClick = { },
                            label = { Text(theme.take(20)) }
                        )
                    }
                }
            }
            
            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${reflection.entryCount} entries",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${reflection.totalWordCount} words",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Avg mood: ${String.format("%.1f", reflection.moodTrend.averageMood)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(
    summary: SmartSummary,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Summary?") },
            text = { Text("This will permanently delete this summary.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    summary.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Text(
                summary.summary.take(150) + if (summary.summary.length > 150) "..." else "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun PatternsTab(
    uiState: AIUiState,
    onDetectPatterns: () -> Unit,
    onDeletePattern: (PatternResult) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Error message
        if (uiState.error != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
        
        item {
            Button(
                onClick = onDetectPatterns,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Detect Patterns")
            }
        }
        
        if (uiState.isLoading) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        "Analyzing journal entries...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "This may take up to 90 seconds on-device",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        if (uiState.patterns.isEmpty() && !uiState.isLoading) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.TrendingUp,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No patterns detected yet",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Click the button above to analyze your entries",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        items(uiState.patterns) { pattern ->
            PatternCard(
                pattern = pattern,
                onDelete = { onDeletePattern(pattern) }
            )
        }
    }
}

@Composable
private fun PatternCard(
    pattern: PatternResult,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Pattern?") },
            text = { Text("This will permanently delete this detected pattern.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        when (pattern.patternType) {
                            PatternType.MOOD_CORRELATION -> Icons.Default.Mood
                            PatternType.TIME_PATTERN -> Icons.Default.Schedule
                            PatternType.ACTIVITY_IMPACT -> Icons.Default.DirectionsRun
                            PatternType.PEOPLE_IMPACT -> Icons.Default.People
                            PatternType.LOCATION_PATTERN -> Icons.Default.Place
                            PatternType.WEATHER_CORRELATION -> Icons.Default.Cloud
                            PatternType.WRITING_HABIT -> Icons.Default.Edit
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        pattern.patternType.name.replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Text(
                pattern.description,
                style = MaterialTheme.typography.bodyMedium
            )
            
            LinearProgressIndicator(
                progress = { pattern.confidence },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Confidence: ${(pattern.confidence * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ModelsTab(
    uiState: AIUiState,
    onDownloadModel: (String) -> Unit,
    onSelectLLM: (String) -> Unit,
    onSelectEmbedding: (String) -> Unit,
    onDeleteModel: (String) -> Unit,
    onGenerateEmbeddings: () -> Unit,
    onImportLLM: () -> Unit,
    onImportEmbedding: () -> Unit
) {
    var showHelpDialog by remember { mutableStateOf(false) }
    
    // Debug logging
    LaunchedEffect(uiState.embeddingStats) {
        Timber.d("ModelsTab received embeddingStats: count=${uiState.embeddingStats.embeddingCount}, total=${uiState.embeddingStats.totalEntries}")
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Getting Started / Help Section
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Getting Started with AI Models",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Download AI models to enable offline journaling features like:\n" +
                        "• Ask questions about your journal entries\n" +
                        "• Generate weekly reflections\n" +
                        "• Detect patterns in your writing\n" +
                        "• Get smart summaries",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { showHelpDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Help, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("How to Download & Use Models")
                    }
                }
            }
        }
        
        // Storage usage
        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Storage",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Used: ${uiState.storageUsage?.totalFormatted ?: "0 MB"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        // Embedding generation
        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Embeddings",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        // Status indicator
                        if (uiState.embeddingStats.allGenerated) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Complete",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    "Ready",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        } else if (uiState.embeddingStats.pendingCount > 0) {
                            Text(
                                "${uiState.embeddingStats.pendingCount} pending",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${uiState.embeddingStats.embeddingCount} / ${uiState.embeddingStats.totalEntries} entries indexed",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Progress bar (always show for context)
                    if (uiState.embeddingStats.totalEntries > 0) {
                        LinearProgressIndicator(
                            progress = { 
                                uiState.embeddingStats.embeddingCount.toFloat() / uiState.embeddingStats.totalEntries.coerceAtLeast(1)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // Active generation progress
                    if (uiState.embeddingProgress?.isGenerating == true) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Text(
                                "Generating... ${uiState.embeddingProgress.current} / ${uiState.embeddingProgress.total}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // Success message
                    if (uiState.successMessage != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = uiState.successMessage,
                                modifier = Modifier.padding(8.dp),
                                color = Color(0xFF2E7D32),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // Error message
                    if (uiState.error != null && uiState.embeddingProgress?.isGenerating != true) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = uiState.error,
                                modifier = Modifier.padding(8.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // Generate/Regenerate button
                    Button(
                        onClick = onGenerateEmbeddings,
                        enabled = uiState.embeddingProgress?.isGenerating != true && uiState.embeddingStats.totalEntries > 0,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.embeddingProgress?.isGenerating == true) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generating...")
                        } else if (uiState.embeddingStats.allGenerated) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Regenerate All")
                        } else if (uiState.embeddingStats.pendingCount > 0) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate ${uiState.embeddingStats.pendingCount} Missing")
                        } else {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Embeddings")
                        }
                    }
                    
                    // Info text
                    if (uiState.embeddingStats.totalEntries == 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No journal entries yet. Embeddings will be generated automatically when you create entries.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // LLM Models
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Language Models",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = onImportLLM,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.FileOpen,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Import .gguf", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        
        items(uiState.llmModels) { model ->
            ModelCard(
                model = model,
                isSelected = model.id == uiState.selectedLLMId,
                onDownload = { onDownloadModel(model.id) },
                onSelect = { onSelectLLM(model.id) },
                onDelete = { onDeleteModel(model.id) },
                isLoading = uiState.isLoading
            )
        }
        
        // Embedding Models
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Embedding Models",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = onImportEmbedding,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.FileOpen,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Import .onnx", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        
        items(uiState.embeddingModels) { model ->
            ModelCard(
                model = model,
                isSelected = model.id == uiState.selectedEmbeddingId,
                onDownload = { onDownloadModel(model.id) },
                onSelect = { onSelectEmbedding(model.id) },
                onDelete = { onDeleteModel(model.id) },
                isLoading = uiState.isLoading
            )
        }
    }
    
    // Help Dialog
    if (showHelpDialog) {
        ModelHelpDialog(onDismiss = { showHelpDialog = false })
    }
}

@Composable
private fun ModelHelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("How to Use AI Models")
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Supported Models Section
                Text(
                    "✓ Supported Model Formats",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "• GGUF format for LLM models (llama.cpp compatible)\n" +
                    "• ONNX format for embedding models\n" +
                    "• Quantized models recommended (Q4_K_M, Q5_K_M)",
                    style = MaterialTheme.typography.bodySmall
                )
                
                HorizontalDivider()
                
                // Recommended Models Section
                Text(
                    "⭐ Recommended Models",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "For LLM (conversation/analysis):\n" +
                    "• TinyLlama 1.1B Q4 (~650MB) - Fast, basic tasks\n" +
                    "• Phi-2 Q4 (~1.6GB) - Better reasoning\n" +
                    "• Gemma-2B Q4 (~1.5GB) - Balanced\n" +
                    "• Mistral-7B Q4 (~4GB) - Best quality\n\n" +
                    "For Embeddings (search):\n" +
                    "• MiniLM-L6-v2 (~90MB) - Default, fast\n" +
                    "• BGE-small (~130MB) - Better accuracy",
                    style = MaterialTheme.typography.bodySmall
                )
                
                HorizontalDivider()
                
                // Where to Download Section
                Text(
                    "📥 Where to Download",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Hugging Face (recommended):\n" +
                    "• huggingface.co/TheBloke - GGUF models\n" +
                    "• huggingface.co/sentence-transformers - Embeddings\n\n" +
                    "Look for files ending in:\n" +
                    "• .gguf for LLM models\n" +
                    "• .onnx for embedding models",
                    style = MaterialTheme.typography.bodySmall
                )
                
                HorizontalDivider()
                
                // How to Use Downloaded Models
                Text(
                    "📱 Using Downloaded Models",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "1. Download model file to your phone\n" +
                    "2. Tap 'Download' button in app to fetch from URL\n" +
                    "   OR place files manually in:\n" +
                    "   Android/data/com.dairum/files/models/\n" +
                    "3. Select the model to activate it\n" +
                    "4. Generate embeddings for your entries",
                    style = MaterialTheme.typography.bodySmall
                )
                
                HorizontalDivider()
                
                // Important Notes
                Text(
                    "⚠️ Important Notes",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    "• Larger models = better quality but slower\n" +
                    "• Models run entirely on-device (offline)\n" +
                    "• First run may be slow while loading\n" +
                    "• Ensure 2-5GB free storage\n" +
                    "• Battery usage varies by model size",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it")
            }
        }
    )
}

@Composable
private fun ModelCard(
    model: AIModel,
    isSelected: Boolean,
    onDownload: () -> Unit,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    isLoading: Boolean
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        model.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        model.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                if (isSelected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    model.sizeFormatted,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                model.quantization?.let {
                    AssistChip(
                        onClick = { },
                        label = { Text(it, style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (model.isDownloaded) {
                    Button(
                        onClick = onSelect,
                        enabled = !isLoading && !isSelected,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isSelected) "Active" else "Select")
                    }
                    
                    OutlinedButton(
                        onClick = onDelete,
                        enabled = !isLoading && !isSelected
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                } else {
                    Button(
                        onClick = onDownload,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download")
                    }
                }
            }
        }
    }
}

// ============================================================================
// Standalone AI Sub-Screens (accessed from More page)
// ============================================================================

/**
 * Standalone Reflections Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIReflectionsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AIViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.initializeIfNeeded()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reflections") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            ReflectionsTab(
                uiState = uiState,
                onGenerateReflection = viewModel::generateWeeklyReflection,
                onGenerateSummary = viewModel::generateMonthlySummary,
                onDeleteReflection = viewModel::deleteReflection,
                onDeleteSummary = viewModel::deleteSummary
            )
        }
    }
}

/**
 * Standalone Patterns Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIPatternsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AIViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.initializeIfNeeded()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patterns") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            PatternsTab(
                uiState = uiState,
                onDetectPatterns = viewModel::detectPatterns,
                onDeletePattern = viewModel::deletePattern
            )
        }
    }
}

/**
 * Standalone AI Settings Screen (Models)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AISettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AIViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // File picker for LLM models (.gguf)
    val llmFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: android.net.Uri? ->
        uri?.let { viewModel.importModel(it, "", ModelType.LLM) }
    }
    
    // File picker for embedding models (.onnx)
    val embeddingFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: android.net.Uri? ->
        uri?.let { viewModel.importModel(it, "", ModelType.EMBEDDING) }
    }
    
    LaunchedEffect(Unit) {
        viewModel.initializeIfNeeded()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            ModelsTab(
                uiState = uiState,
                onDownloadModel = viewModel::downloadModel,
                onSelectLLM = viewModel::selectLLMModel,
                onSelectEmbedding = viewModel::selectEmbeddingModel,
                onDeleteModel = viewModel::deleteModel,
                onGenerateEmbeddings = viewModel::generateAllEmbeddings,
                onImportLLM = { llmFilePicker.launch(arrayOf("*/*")) },
                onImportEmbedding = { embeddingFilePicker.launch(arrayOf("*/*")) }
            )
        }
    }
}
