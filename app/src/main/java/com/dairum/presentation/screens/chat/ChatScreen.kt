package com.dairum.presentation.screens.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.domain.model.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onNavigateBack: () -> Unit,
    onNavigateToModels: () -> Unit,
    onNavigateToGuardrails: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Auto-scroll to the true bottom (including the streaming bubble/typing indicator) when it changes
    val bottomItemCount = uiState.messages.size + when {
        uiState.isGenerating && uiState.streamingContent.isNotBlank() -> 1
        uiState.isGenerating -> 1 // processing status row or typing indicator
        else -> 0
    }
    LaunchedEffect(bottomItemCount, uiState.streamingContent) {
        if (bottomItemCount > 0) {
            listState.animateScrollToItem(bottomItemCount - 1)
        }
    }
    
    // Show error snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            val canRetry = uiState.lastFailedInput != null
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = if (canRetry) "Retry" else null
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.retryLastMessage()
            }
            viewModel.clearError()
        }
    }
    
    // Show success snackbar
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSuccessMessage()
        }
    }
    
    Scaffold(
        topBar = {
            ChatTopBar(
                title = uiState.currentSession?.displayTitle ?: "Chat",
                isSaved = uiState.isSaved,
                onNavigateBack = onNavigateBack,
                onToggleSave = viewModel::toggleSave,
                onNewConversation = viewModel::startNewConversation,
                onShowSessions = viewModel::toggleSessionsList,
                onShowMemoryInsights = viewModel::toggleMemoryInsights,
                onShowGuardrails = onNavigateToGuardrails
            )
        },
        bottomBar = {
            ChatInputBar(
                input = uiState.currentInput,
                onInputChange = viewModel::updateInput,
                onSend = viewModel::sendMessageStreaming,
                onCancel = viewModel::cancelGeneration,
                isGenerating = uiState.isGenerating,
                canSend = uiState.canSend,
                isAIReady = uiState.isAIReady,
                onNavigateToModels = onNavigateToModels
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isInitializing) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.messages.isEmpty() && !uiState.isGenerating) {
                EmptyChatContent(
                    isAIReady = uiState.isAIReady,
                    modelName = uiState.selectedLLMName,
                    reflectionCount = uiState.reflectionCount,
                    onNavigateToModels = onNavigateToModels,
                    onSuggestionClick = { suggestion -> viewModel.updateInput(suggestion) }
                )
            } else {
                ChatMessagesList(
                    messages = uiState.messages,
                    streamingContent = uiState.streamingContent,
                    isGenerating = uiState.isGenerating,
                    processingStatus = uiState.processingStatus,
                    listState = listState,
                    onViewPrompt = viewModel::viewPromptForMessage
                )
            }
            
            // Sessions drawer
            AnimatedVisibility(
                visible = uiState.showSessionsList,
                enter = slideInHorizontally() + fadeIn(),
                exit = slideOutHorizontally() + fadeOut()
            ) {
                SessionsDrawer(
                    sessions = uiState.savedSessions,
                    currentSessionId = uiState.currentSession?.id,
                    onSelectSession = viewModel::switchToSession,
                    onDeleteSession = viewModel::deleteSession,
                    onDismiss = viewModel::toggleSessionsList
                )
            }
            
            // Memory insights overlay
            AnimatedVisibility(
                visible = uiState.showMemoryInsights,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                MemoryInsightsSheet(
                    reflections = uiState.recentReflections,
                    reflectionCount = uiState.reflectionCount,
                    onGenerateReflections = viewModel::generateReflections,
                    onAddMemory = viewModel::showAddMemoryDialog,
                    onDeleteMemory = viewModel::deleteMemory,
                    onDismiss = viewModel::toggleMemoryInsights
                )
            }
        }
    }
    
    // Add memory dialog
    if (uiState.isAddingMemory) {
        AddMemoryDialog(
            onSave = { viewModel.addMemory(it) },
            onDismiss = viewModel::dismissAddMemoryDialog
        )
    }
    
    // Prompt transparency dialog
    uiState.viewingPrompt?.let { prompt ->
        PromptViewerDialog(prompt = prompt, onDismiss = viewModel::dismissPromptView)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(
    title: String,
    isSaved: Boolean,
    onNavigateBack: () -> Unit,
    onToggleSave: () -> Unit,
    onNewConversation: () -> Unit,
    onShowSessions: () -> Unit,
    onShowMemoryInsights: () -> Unit,
    onShowGuardrails: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = onToggleSave) {
                Icon(
                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = if (isSaved) "Unsave" else "Save",
                    tint = if (isSaved) MaterialTheme.colorScheme.primary else LocalContentColor.current
                )
            }
            IconButton(onClick = onShowGuardrails) {
                Icon(Icons.Outlined.Shield, contentDescription = "AI Guardrails")
            }
            IconButton(onClick = onShowMemoryInsights) {
                Icon(Icons.Outlined.Psychology, contentDescription = "Memory Insights")
            }
            IconButton(onClick = onShowSessions) {
                Icon(Icons.Default.History, contentDescription = "History")
            }
            IconButton(onClick = onNewConversation) {
                Icon(Icons.Default.Add, contentDescription = "New Conversation")
            }
        }
    )
}

@Composable
private fun ChatInputBar(
    input: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onCancel: () -> Unit,
    isGenerating: Boolean,
    canSend: Boolean,
    isAIReady: Boolean,
    onNavigateToModels: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (!isAIReady) {
                AssistChip(
                    onClick = onNavigateToModels,
                    label = { Text("Load AI Model") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = onInputChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { 
                        Text(
                            if (isAIReady) "Share your thoughts..." 
                            else "Load a model to start chatting"
                        )
                    },
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { if (canSend) onSend() }),
                    enabled = isAIReady && !isGenerating,
                    shape = RoundedCornerShape(24.dp)
                )
                
                if (isGenerating) {
                    FilledTonalIconButton(
                        onClick = onCancel,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = "Stop")
                    }
                } else {
                    FilledIconButton(
                        onClick = onSend,
                        enabled = canSend,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatMessagesList(
    messages: List<ChatMessage>,
    streamingContent: String,
    isGenerating: Boolean,
    processingStatus: String?,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onViewPrompt: (ChatMessage) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            ChatMessageBubble(message = message, onViewPrompt = onViewPrompt)
        }
        
        // Show streaming content
        if (isGenerating && streamingContent.isNotBlank()) {
            item {
                ChatMessageBubble(
                    message = ChatMessage(
                        id = -2,
                        sessionId = "",
                        role = MessageRole.ASSISTANT,
                        content = streamingContent
                    ),
                    isStreaming = true
                )
            }
        }
        
        // Show processing status
        if (isGenerating && processingStatus != null) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = processingStatus,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // Typing indicator
        if (isGenerating && streamingContent.isBlank() && processingStatus == null) {
            item {
                TypingIndicator()
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    isStreaming: Boolean = false,
    onViewPrompt: (ChatMessage) -> Unit = {}
) {
    val isUser = message.role == MessageRole.USER
    val clipboardManager = LocalClipboardManager.current
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // AI avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            modifier = Modifier.widthIn(max = 300.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
                color = if (isUser) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = if (isUser) 0.dp else 1.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = message.content,
                        color = if (isUser) 
                            MaterialTheme.colorScheme.onPrimary 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    if (isStreaming) {
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                        )
                    }
                }
            }
            
            // Timestamp and actions
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = message.createdAt.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                
                if (!isUser && message.id > 0) {
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(message.content))
                        },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Outlined.ContentCopy,
                            contentDescription = "Copy",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
                
                if (!isUser && message.metadata?.promptUsed != null) {
                    IconButton(
                        onClick = { onViewPrompt(message) },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Code,
                            contentDescription = "View prompt sent to the AI",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
        
        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // User avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
private fun TypingIndicator() {
    val transition = rememberInfiniteTransition(label = "typing")
    Row(
        modifier = Modifier.padding(start = 40.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(3) { index ->
            val alpha by transition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 600, delayMillis = index * 150),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_$index"
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha)
                    )
            )
        }
    }
}

@Composable
private fun EmptyChatContent(
    isAIReady: Boolean,
    modelName: String?,
    reflectionCount: Int,
    onNavigateToModels: () -> Unit,
    onSuggestionClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Your Thinking Companion",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Have deep conversations about ethics, philosophy, life, and ideas. I'll remember our discussions and grow to understand your thinking.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        if (reflectionCount > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            AssistChip(
                onClick = { },
                label = { Text("$reflectionCount insights learned") },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Psychology,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (!isAIReady) {
            OutlinedButton(onClick = onNavigateToModels) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Download AI Model")
            }
        } else {
            Text(
                text = "Using: ${modelName ?: "Unknown model"}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Conversation starters
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Try asking about:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                val suggestions = listOf(
                    "What makes a decision truly ethical?",
                    "How do I find meaning in everyday life?",
                    "What patterns do you notice in my thinking?"
                )
                
                suggestions.forEach { suggestion ->
                    SuggestionChip(
                        onClick = { onSuggestionClick(suggestion) },
                        label = { 
                            Text(
                                suggestion,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionsDrawer(
    sessions: List<ChatSession>,
    currentSessionId: String?,
    onSelectSession: (ChatSession) -> Unit,
    onDeleteSession: (ChatSession) -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp),
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Saved Conversations",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
            
            HorizontalDivider()
            
            if (sessions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No saved conversations yet.\nSave a conversation to see it here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(sessions, key = { it.id }) { session ->
                        SessionListItem(
                            session = session,
                            isSelected = session.id == currentSessionId,
                            onClick = { onSelectSession(session) },
                            onDelete = { onDeleteSession(session) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionListItem(
    session: ChatSession,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) 
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) 
        else 
            Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.displayTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${session.messageCount} messages • ${session.createdAt.format(DateTimeFormatter.ofPattern("MMM d"))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun MemoryInsightsSheet(
    reflections: List<ReflectionMemory>,
    reflectionCount: Int,
    onGenerateReflections: () -> Unit,
    onAddMemory: () -> Unit = {},
    onDeleteMemory: (ReflectionMemory) -> Unit = {},
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Handle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                )
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Memory Insights",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "$reflectionCount insights learned about you",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row {
                    IconButton(onClick = onAddMemory) {
                        Icon(Icons.Default.Add, contentDescription = "Add a memory")
                    }
                    IconButton(onClick = onGenerateReflections) {
                        Icon(Icons.Default.Refresh, contentDescription = "Generate")
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }
            
            HorizontalDivider()
            
            if (reflections.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No insights yet.\nAs we chat, I'll learn about your thinking patterns, values, and interests. Tap + to add a memory yourself.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reflections, key = { it.id }) { reflection ->
                        ReflectionCard(
                            reflection = reflection,
                            onDelete = if (reflection.category == ReflectionCategory.USER_PROVIDED) {
                                { onDeleteMemory(reflection) }
                            } else null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReflectionCard(reflection: ReflectionMemory, onDelete: (() -> Unit)? = null) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = { },
                    label = { 
                        Text(
                            reflection.category.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier.height(24.dp)
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (reflection.category != ReflectionCategory.USER_PROVIDED) {
                        Text(
                            text = "${(reflection.confidence * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (onDelete != null) {
                        IconButton(onClick = onDelete, modifier = Modifier.size(20.dp)) {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = "Delete memory",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = reflection.content,
                style = MaterialTheme.typography.bodyMedium
            )
            
            if (reflection.occurrenceCount > 1) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Observed ${reflection.occurrenceCount} times",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun AddMemoryDialog(
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a memory") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Tell the AI something to remember for future conversations, e.g. \"My sister's name is Priya\".",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Type a fact or memory...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(text) }, enabled = text.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun PromptViewerDialog(
    prompt: String,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Prompt sent to the AI") },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = prompt,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { clipboardManager.setText(AnnotatedString(prompt)) }) {
                Text("Copy")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

