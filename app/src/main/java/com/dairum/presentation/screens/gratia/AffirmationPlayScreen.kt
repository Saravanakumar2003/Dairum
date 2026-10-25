package com.dairum.presentation.screens.gratia

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffirmationPlayScreen(
    onBackClick: () -> Unit,
    onWriteJournal: (String) -> Unit,
    viewModel: AffirmationPlayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        // Visual effects overlay
        if (state.isPlaying && state.visualEffect != com.dairum.data.preferences.VisualEffect.NONE) {
            VisualEffectOverlay(
                effect = state.visualEffect,
                intensity = state.visualEffectIntensity
            )
        }

        // Decorative circles
        if (state.isPlaying && state.showBreathingAnimation) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .scale(breathScale)
                    .alpha(glowAlpha * 0.3f)
                    .align(Alignment.Center)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(breathScale * 0.9f)
                    .alpha(glowAlpha * 0.4f)
                    .align(Alignment.Center)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                tertiaryColor.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.Close, "Close")
                }
                Text(
                    text = state.folder?.name ?: "",
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    // Background music toggle
                    IconButton(onClick = {
                        viewModel.toggleBackgroundMusic(!state.backgroundMusicEnabled)
                    }) {
                        Icon(
                            if (state.backgroundMusicEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                            contentDescription = "Background music",
                            tint = if (state.backgroundMusicEnabled) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // Natural voice toggle
                    IconButton(onClick = {
                        viewModel.toggleNaturalVoice(!state.useNaturalVoice)
                    }) {
                        Icon(
                            if (state.useNaturalVoice) Icons.Default.RecordVoiceOver else Icons.Default.VoiceOverOff,
                            contentDescription = "Natural voice",
                            tint = if (state.useNaturalVoice) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Progress
            if (state.affirmations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Text(
                    text = "${state.currentIndex + 1} / ${state.affirmations.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Main content area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isFinished) {
                    // Finished state
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Text(
                            text = "🧘",
                            fontSize = 64.sp
                        )
                        Text(
                            text = "Session Complete",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Take a moment to reflect on how you feel",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onWriteJournal(viewModel.getJournalPrompt()) },
                            modifier = Modifier.fillMaxWidth(0.7f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Edit, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Write in Journal")
                        }
                    }
                } else if (state.currentAffirmation != null) {
                    // Current affirmation display
                    AnimatedContent(
                        targetState = state.currentIndex,
                        transitionSpec = {
                            fadeIn(tween(600)) + slideInVertically { it / 3 } togetherWith
                                    fadeOut(tween(300)) + slideOutVertically { -it / 3 }
                        },
                        label = "affirmation"
                    ) { _ ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "\"",
                                style = MaterialTheme.typography.displayLarge,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                            Text(
                                text = state.currentAffirmation!!.text,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    lineHeight = 36.sp
                                ),
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                } else {
                    // Not started
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = state.folder?.emoji ?: "✨",
                            fontSize = 64.sp
                        )
                        Text(
                            text = "Ready to begin?",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "${state.affirmations.size} affirmations",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Play controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!state.isFinished) {
                    // Previous
                    IconButton(
                        onClick = { viewModel.skipPrevious() },
                        enabled = state.currentIndex > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            modifier = Modifier.size(32.dp),
                            tint = if (state.currentIndex > 0) MaterialTheme.colorScheme.onSurface
                                   else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }

                    // Play/Pause
                    FilledTonalIconButton(
                        onClick = {
                            if (state.isPlaying) viewModel.pause() else {
                                if (state.currentIndex == 0 && !state.isPlaying) viewModel.play()
                                else viewModel.resume()
                            }
                        },
                        modifier = Modifier.size(72.dp)
                    ) {
                        Icon(
                            imageVector = if (state.isPlaying) Icons.Default.Pause
                                         else Icons.Default.PlayArrow,
                            contentDescription = if (state.isPlaying) "Pause" else "Play",
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Next
                    IconButton(
                        onClick = { viewModel.skipNext() },
                        enabled = state.currentIndex < state.affirmations.size - 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next",
                            modifier = Modifier.size(32.dp),
                            tint = if (state.currentIndex < state.affirmations.size - 1) 
                                MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}
