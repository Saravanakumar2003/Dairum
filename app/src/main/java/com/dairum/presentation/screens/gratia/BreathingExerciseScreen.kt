package com.dairum.presentation.screens.gratia

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.data.preferences.VisualEffect
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingExerciseScreen(
    onBackClick: () -> Unit,
    viewModel: BreathingExerciseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    // Breathing animation scale driven by phase
    val breathTarget = when (state.phase) {
        BreathPhase.BREATHE_IN -> 1.4f
        BreathPhase.HOLD_IN -> 1.4f
        BreathPhase.BREATHE_OUT -> 0.7f
        BreathPhase.HOLD_OUT -> 0.7f
        else -> 1f
    }
    val breathDuration = when (state.phase) {
        BreathPhase.BREATHE_IN -> state.getInhale() * 1000
        BreathPhase.BREATHE_OUT -> state.getExhale() * 1000
        else -> 500
    }
    val breathScale by animateFloatAsState(
        targetValue = breathTarget,
        animationSpec = tween(durationMillis = breathDuration, easing = EaseInOutSine),
        label = "breathScale"
    )

    val glowAlpha by rememberInfiniteTransition(label = "glow").animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(2000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "glowAlpha"
    )

    // Phase color
    val phaseColor by animateColorAsState(
        targetValue = when (state.phase) {
            BreathPhase.BREATHE_IN -> Color(0xFF4FC3F7)   // Light blue
            BreathPhase.HOLD_IN -> Color(0xFF81C784)      // Green
            BreathPhase.BREATHE_OUT -> Color(0xFFBA68C8)   // Purple
            BreathPhase.HOLD_OUT -> Color(0xFFFFB74D)      // Amber
            BreathPhase.FINISHED -> Color(0xFFFFD54F)      // Gold
            else -> primaryColor
        },
        animationSpec = tween(800),
        label = "phaseColor"
    )

    val visualEffect by viewModel.visualEffect.collectAsState()
    val visualEffectIntensity by viewModel.visualEffectIntensity.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        phaseColor.copy(alpha = 0.08f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        // Visual effects overlay during exercise
        if (state.isRunning && visualEffect != VisualEffect.NONE) {
            VisualEffectOverlay(
                effect = visualEffect,
                intensity = visualEffectIntensity
            )
        }

        // Breathing particles
        if (state.isRunning) {
            BreathingParticles(phaseColor = phaseColor, breathScale = breathScale)
        }

        if (state.phase == BreathPhase.IDLE && !state.isRunning && state.phase != BreathPhase.FINISHED) {
            // ====== TECHNIQUE SELECTION SCREEN ======
            TechniqueSelectionContent(
                state = state,
                onBackClick = onBackClick,
                onSelectTechnique = { viewModel.selectTechnique(it) },
                onStart = { viewModel.start() },
                onToggleVoice = { viewModel.toggleVoiceGuidance(it) },
                onToggleMusic = { viewModel.toggleBackgroundMusic(it) },
                onUpdateInhale = { viewModel.updateCustomInhale(it) },
                onUpdateHoldIn = { viewModel.updateCustomHoldIn(it) },
                onUpdateExhale = { viewModel.updateCustomExhale(it) },
                onUpdateHoldOut = { viewModel.updateCustomHoldOut(it) },
                onUpdateCycles = { viewModel.updateCustomCycles(it) }
            )
        } else {
            // ====== ACTIVE EXERCISE SCREEN ======
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
                    IconButton(onClick = { viewModel.stop(); onBackClick() }) {
                        Icon(Icons.Default.Close, "Close")
                    }
                    Text(
                        state.technique.label,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        IconButton(onClick = {
                            viewModel.toggleBackgroundMusic(!state.backgroundMusicEnabled)
                        }) {
                            Icon(
                                if (state.backgroundMusicEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                                "Music",
                                tint = if (state.backgroundMusicEnabled) phaseColor
                                       else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = {
                            viewModel.toggleVoiceGuidance(!state.voiceGuidance)
                        }) {
                            Icon(
                                if (state.voiceGuidance) Icons.Default.RecordVoiceOver else Icons.Default.VoiceOverOff,
                                "Voice",
                                tint = if (state.voiceGuidance) phaseColor
                                       else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Progress
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = phaseColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Text(
                    "Cycle ${state.currentCycle + 1} of ${state.totalCycles}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Main breathing circle
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.phase != BreathPhase.FINISHED) {
                        // Outer glow ring
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .scale(breathScale * 1.1f)
                                .alpha(glowAlpha * 0.2f)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(phaseColor.copy(alpha = 0.3f), Color.Transparent)
                                    ),
                                    CircleShape
                                )
                        )

                        // Middle ring
                        Box(
                            modifier = Modifier
                                .size(220.dp)
                                .scale(breathScale)
                                .alpha(glowAlpha * 0.4f)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(phaseColor.copy(alpha = 0.15f), Color.Transparent)
                                    ),
                                    CircleShape
                                )
                        )

                        // Core circle with ring
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .scale(breathScale),
                            contentAlignment = Alignment.Center
                        ) {
                            // Animated ring
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val radius = size.minDimension / 2
                                drawCircle(
                                    color = phaseColor.copy(alpha = 0.2f),
                                    radius = radius,
                                    style = Stroke(width = 6f)
                                )
                                // Progress arc based on phase time
                                val phaseMax = when (state.phase) {
                                    BreathPhase.BREATHE_IN -> state.getInhale()
                                    BreathPhase.HOLD_IN -> state.getHoldIn()
                                    BreathPhase.BREATHE_OUT -> state.getExhale()
                                    BreathPhase.HOLD_OUT -> state.getHoldOut()
                                    else -> 1
                                }
                                val sweep = if (phaseMax > 0) {
                                    360f * (1f - state.phaseTimeLeft.toFloat() / phaseMax)
                                } else 360f
                                drawArc(
                                    color = phaseColor,
                                    startAngle = -90f,
                                    sweepAngle = sweep,
                                    useCenter = false,
                                    style = Stroke(width = 6f, cap = StrokeCap.Round)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Timer
                                Text(
                                    text = "${state.phaseTimeLeft}",
                                    style = MaterialTheme.typography.displayLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = phaseColor
                                )
                            }
                        }

                        // Phase label below circle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp)
                        ) {
                            AnimatedContent(
                                targetState = state.phaseLabel,
                                transitionSpec = {
                                    fadeIn(tween(400)) + slideInVertically { it / 2 } togetherWith
                                            fadeOut(tween(200))
                                },
                                label = "phaseLabel"
                            ) { label ->
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = phaseColor
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Phase pattern indicator
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                PhaseIndicatorDot(
                                    label = "In ${state.getInhale()}s",
                                    isActive = state.phase == BreathPhase.BREATHE_IN,
                                    color = Color(0xFF4FC3F7)
                                )
                                if (state.getHoldIn() > 0) {
                                    PhaseIndicatorDot(
                                        label = "Hold ${state.getHoldIn()}s",
                                        isActive = state.phase == BreathPhase.HOLD_IN,
                                        color = Color(0xFF81C784)
                                    )
                                }
                                PhaseIndicatorDot(
                                    label = "Out ${state.getExhale()}s",
                                    isActive = state.phase == BreathPhase.BREATHE_OUT,
                                    color = Color(0xFFBA68C8)
                                )
                                if (state.getHoldOut() > 0) {
                                    PhaseIndicatorDot(
                                        label = "Hold ${state.getHoldOut()}s",
                                        isActive = state.phase == BreathPhase.HOLD_OUT,
                                        color = Color(0xFFFFB74D)
                                    )
                                }
                            }
                        }
                    } else {
                        // FINISHED state
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Text("🧘", fontSize = 64.sp)
                            Text(
                                "Session Complete",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "You completed ${state.totalCycles} cycles of ${state.technique.label}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.stop() },
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth(0.6f)
                            ) {
                                Icon(Icons.Default.Replay, null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Try Again")
                            }
                            OutlinedButton(
                                onClick = onBackClick,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth(0.6f)
                            ) {
                                Text("Done")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stop button at bottom
                if (state.isRunning) {
                    FilledTonalButton(
                        onClick = { viewModel.stop() },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(bottom = 32.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Icon(Icons.Default.Stop, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Stop")
                    }
                }
            }
        }
    }
}

// ========== Technique Selection Content ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TechniqueSelectionContent(
    state: BreathingState,
    onBackClick: () -> Unit,
    onSelectTechnique: (BreathingTechnique) -> Unit,
    onStart: () -> Unit,
    onToggleVoice: (Boolean) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onUpdateInhale: (Int) -> Unit,
    onUpdateHoldIn: (Int) -> Unit,
    onUpdateExhale: (Int) -> Unit,
    onUpdateHoldOut: (Int) -> Unit,
    onUpdateCycles: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Breathing Exercise") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Choose a Technique",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Technique cards
            items(BreathingTechnique.entries.toList()) { technique ->
                TechniqueCard(
                    technique = technique,
                    isSelected = state.technique == technique,
                    onClick = { onSelectTechnique(technique) }
                )
            }

            // Custom settings
            if (state.technique == BreathingTechnique.CUSTOM) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item {
                    Text(
                        "Custom Pattern",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    CustomPatternCard(
                        inhale = state.customInhale,
                        holdIn = state.customHoldIn,
                        exhale = state.customExhale,
                        holdOut = state.customHoldOut,
                        cycles = state.customCycles,
                        onUpdateInhale = onUpdateInhale,
                        onUpdateHoldIn = onUpdateHoldIn,
                        onUpdateExhale = onUpdateExhale,
                        onUpdateHoldOut = onUpdateHoldOut,
                        onUpdateCycles = onUpdateCycles
                    )
                }
            }

            // Options
            item { Spacer(modifier = Modifier.height(4.dp)) }
            item {
                Text(
                    "Options",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.RecordVoiceOver, null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Voice Guidance", style = MaterialTheme.typography.titleSmall)
                                    Text("\"Breathe in... Hold... Breathe out...\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Switch(checked = state.voiceGuidance, onCheckedChange = onToggleVoice)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MusicNote, null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Background Music", style = MaterialTheme.typography.titleSmall)
                                    Text("Ambient calming tones",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Switch(checked = state.backgroundMusicEnabled, onCheckedChange = onToggleMusic)
                        }
                    }
                }
            }

            // Start button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Begin ${state.technique.label}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

// ========== Technique Card ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TechniqueCard(
    technique: BreathingTechnique,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = if (isSelected) CardDefaults.outlinedCardBorder() else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(technique.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    technique.label,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    technique.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (technique != BreathingTechnique.CUSTOM) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        buildString {
                            append("${technique.inhaleSeconds}s in")
                            if (technique.holdAfterInhaleSeconds > 0) append(" → ${technique.holdAfterInhaleSeconds}s hold")
                            append(" → ${technique.exhaleSeconds}s out")
                            if (technique.holdAfterExhaleSeconds > 0) append(" → ${technique.holdAfterExhaleSeconds}s hold")
                            append(" × ${technique.cycles}")
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle, null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ========== Custom Pattern Card ==========
@Composable
private fun CustomPatternCard(
    inhale: Int, holdIn: Int, exhale: Int, holdOut: Int, cycles: Int,
    onUpdateInhale: (Int) -> Unit, onUpdateHoldIn: (Int) -> Unit,
    onUpdateExhale: (Int) -> Unit, onUpdateHoldOut: (Int) -> Unit,
    onUpdateCycles: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimingRow("Breathe In", inhale, Color(0xFF4FC3F7), onUpdateInhale)
            TimingRow("Hold (In)", holdIn, Color(0xFF81C784), onUpdateHoldIn)
            TimingRow("Breathe Out", exhale, Color(0xFFBA68C8), onUpdateExhale)
            TimingRow("Hold (Out)", holdOut, Color(0xFFFFB74D), onUpdateHoldOut)
            HorizontalDivider()
            TimingRow("Cycles", cycles, MaterialTheme.colorScheme.primary, onUpdateCycles, max = 20)
        }
    }
}

@Composable
private fun TimingRow(
    label: String,
    value: Int,
    color: Color,
    onUpdate: (Int) -> Unit,
    max: Int = 15
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = { if (value > 0) onUpdate(value - 1) },
            modifier = Modifier.size(36.dp),
            enabled = value > 0
        ) {
            Icon(Icons.Default.Remove, "Decrease", modifier = Modifier.size(18.dp))
        }
        Text(
            "${value}s",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.Center
        )
        IconButton(
            onClick = { if (value < max) onUpdate(value + 1) },
            modifier = Modifier.size(36.dp),
            enabled = value < max
        ) {
            Icon(Icons.Default.Add, "Increase", modifier = Modifier.size(18.dp))
        }
    }
}

// ========== Phase Indicator Dot ==========
@Composable
private fun PhaseIndicatorDot(label: String, isActive: Boolean, color: Color) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.1f else 0.9f,
        animationSpec = tween(300),
        label = "dotScale"
    )
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isActive) color.copy(alpha = 0.2f)
               else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.scale(scale)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) color else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

// ========== Breathing Particles ==========
@Composable
private fun BreathingParticles(phaseColor: Color, breathScale: Float) {
    val time by rememberInfiniteTransition(label = "breathParticles").animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(60000, easing = LinearEasing)),
        label = "bpTime"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2
        val cy = size.height / 2
        val count = 20
        for (i in 0 until count) {
            val angle = (i.toFloat() / count) * 2f * PI.toFloat() + time * 0.003f
            val radius = 120f * breathScale + sin(time * 0.01f + i.toFloat()).toFloat() * 40f
            val x = cx + cos(angle.toDouble()).toFloat() * radius
            val y = cy + sin(angle.toDouble()).toFloat() * radius
            val alpha = 0.1f + 0.2f * ((sin(time * 0.02f + i * 0.5f).toFloat() + 1f) / 2f)
            drawCircle(
                color = phaseColor.copy(alpha = alpha),
                radius = 4f + sin(time * 0.015f + i * 0.3f).toFloat() * 3f,
                center = Offset(x, y)
            )
        }
    }
}
