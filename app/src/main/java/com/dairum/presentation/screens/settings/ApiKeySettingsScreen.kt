package com.dairum.presentation.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeySettingsScreen(
    onNavigateUp: () -> Unit,
    viewModel: ApiKeySettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("API Configuration") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Introduction Card
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "About API Keys",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "API keys are required to enable Maps and Calendar features. " +
                                   "Your keys are stored securely on your device and are never shared.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            // Google Maps API Key
            item {
                ApiKeyCard(
                    title = "Google Maps API Key",
                    description = "Required for location features and map display",
                    apiKey = uiState.googleMapsApiKey,
                    onApiKeyChange = viewModel::setGoogleMapsApiKey,
                    isValid = uiState.isGoogleMapsKeyValid,
                    instructions = listOf(
                        "1. Go to Google Cloud Console",
                        "2. Create a new project or select existing",
                        "3. Enable 'Maps SDK for Android'",
                        "4. Go to 'Credentials' and create an API key",
                        "5. Restrict the key to your app's package name"
                    ),
                    learnMoreUrl = "https://developers.google.com/maps/documentation/android-sdk/get-api-key",
                    onLearnMore = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        context.startActivity(intent)
                    }
                )
            }
            
            // Weather API Key (already configured in build.gradle, but can be overridden)
            item {
                ApiKeyCard(
                    title = "Weather API Key",
                    description = "Required for automatic weather tracking",
                    apiKey = uiState.weatherApiKey,
                    onApiKeyChange = viewModel::setWeatherApiKey,
                    isValid = uiState.isWeatherKeyValid,
                    instructions = listOf(
                        "1. Go to weatherapi.com",
                        "2. Sign up for a free account",
                        "3. Copy your API key from the dashboard",
                        "4. Free tier allows 1M calls/month"
                    ),
                    learnMoreUrl = "https://www.weatherapi.com/docs/",
                    onLearnMore = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        context.startActivity(intent)
                    }
                )
            }
            
            // Google Calendar API Key (for future calendar integration)
            item {
                ApiKeyCard(
                    title = "Google Calendar API (Optional)",
                    description = "For calendar integration and syncing events to entries",
                    apiKey = uiState.googleCalendarApiKey,
                    onApiKeyChange = viewModel::setGoogleCalendarApiKey,
                    isValid = uiState.isGoogleCalendarKeyValid,
                    instructions = listOf(
                        "1. Go to Google Cloud Console",
                        "2. Enable 'Google Calendar API'",
                        "3. Create OAuth 2.0 credentials",
                        "4. Configure consent screen",
                        "5. Copy the Client ID"
                    ),
                    learnMoreUrl = "https://developers.google.com/calendar/api/quickstart/android",
                    onLearnMore = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        context.startActivity(intent)
                    },
                    isOptional = true
                )
            }
            
            // Security Notice
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Security,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Security",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• API keys are encrypted and stored locally\n" +
                                   "• Keys are never transmitted to our servers\n" +
                                   "• We recommend restricting API keys to this app only\n" +
                                   "• You can revoke keys anytime from the respective console",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Test Connection Button
            item {
                Button(
                    onClick = { viewModel.testConnections() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isTesting
                ) {
                    if (uiState.isTesting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (uiState.isTesting) "Testing..." else "Test API Connections")
                }
            }
            
            // Status message
            uiState.testResult?.let { result ->
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (result.success) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (result.success) Icons.Default.CheckCircle else Icons.Default.Error,
                                contentDescription = null,
                                tint = if (result.success) 
                                    MaterialTheme.colorScheme.onPrimaryContainer 
                                else 
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = result.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (result.success) 
                                    MaterialTheme.colorScheme.onPrimaryContainer 
                                else 
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ApiKeyCard(
    title: String,
    description: String,
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    isValid: Boolean?,
    instructions: List<String>,
    learnMoreUrl: String,
    onLearnMore: (String) -> Unit,
    isOptional: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var showKey by remember { mutableStateOf(false) }
    
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        if (isOptional) {
                            Spacer(modifier = Modifier.width(8.dp))
                            AssistChip(
                                onClick = { },
                                label = { Text("Optional", style = MaterialTheme.typography.labelSmall) },
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                isValid?.let { valid ->
                    Icon(
                        if (valid) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = if (valid) "Valid" else "Invalid",
                        tint = if (valid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = apiKey,
                onValueChange = onApiKeyChange,
                label = { Text("API Key") },
                singleLine = true,
                visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Row {
                        IconButton(onClick = { showKey = !showKey }) {
                            Icon(
                                if (showKey) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = if (showKey) "Hide" else "Show"
                            )
                        }
                        if (apiKey.isNotEmpty()) {
                            IconButton(onClick = { onApiKeyChange("") }) {
                                Icon(Icons.Outlined.Clear, contentDescription = "Clear")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Expandable instructions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "How to get this API key",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        instructions.forEach { instruction ->
                            Text(
                                text = instruction,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        TextButton(
                            onClick = { onLearnMore(learnMoreUrl) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(
                                Icons.Outlined.OpenInNew,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Learn More")
                        }
                    }
                }
            }
        }
    }
}
