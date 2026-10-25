package com.dairum.presentation.screens.gratia

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.data.preferences.BackgroundMusicOption
import com.dairum.data.preferences.VisualEffect
import com.dairum.data.preferences.VoiceGender
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GratiaSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: GratiaSettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val availableVoices by viewModel.availableVoices.collectAsState()
    val testingVoice by viewModel.testingVoice.collectAsState()
    val testingMusic by viewModel.testingMusic.collectAsState()
    val testingVisual by viewModel.testingVisual.collectAsState()
    val testingAll by viewModel.testingAll.collectAsState()

    var showVoiceDialog by remember { mutableStateOf(false) }
    var showMusicDialog by remember { mutableStateOf(false) }
    var showVisualDialog by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }
    var showCustomMusicNameDialog by remember { mutableStateOf(false) }
    var pendingMusicUri by remember { mutableStateOf<String?>(null) }

    var showVisualPreview by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val musicFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            pendingMusicUri = it.toString()
            showCustomMusicNameDialog = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TTS & Media Settings") },
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // ============ VOICE SETTINGS ============
                item {
                    SectionHeader(title = "Voice", icon = Icons.Default.RecordVoiceOver)
                }

                item {
                    SettingItem(
                        title = "Voice Type",
                        subtitle = "${settings.voiceGender.label} — ${settings.voiceGender.description}",
                        icon = Icons.Default.Person,
                        onClick = { showGenderDialog = true }
                    )
                }

                item {
                    SettingSlider(
                        title = "Speech Speed",
                        value = settings.ttsSpeed,
                        valueRange = 0.25f..2.0f,
                        valueLabel = String.format("%.1fx", settings.ttsSpeed),
                        onValueChange = { viewModel.updateTtsSpeed(it) },
                        icon = Icons.Default.Speed
                    )
                }

                item {
                    SettingSlider(
                        title = "Speech Pitch",
                        value = settings.ttsPitch,
                        valueRange = 0.5f..2.0f,
                        valueLabel = String.format("%.1f", settings.ttsPitch),
                        onValueChange = { viewModel.updateTtsPitch(it) },
                        icon = Icons.Default.Tune
                    )
                }

                item {
                    SettingItem(
                        title = "Voice Engine",
                        subtitle = if (settings.ttsVoice.isEmpty()) "System default"
                                  else availableVoices.find { it.name == settings.ttsVoice }?.displayName
                                      ?: settings.ttsVoice,
                        icon = Icons.Default.SettingsVoice,
                        onClick = { showVoiceDialog = true }
                    )
                }

                item {
                    TestButton(
                        label = if (testingVoice) "Stop Voice" else "Test Voice",
                        icon = if (testingVoice) Icons.Default.Stop else Icons.Default.PlayArrow,
                        isActive = testingVoice,
                        onClick = { viewModel.testVoice() }
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
                item { HorizontalDivider() }

                // ============ BACKGROUND MUSIC ============
                item {
                    SectionHeader(title = "Background Music", icon = Icons.Default.MusicNote)
                }

                item {
                    SettingItem(
                        title = "Music Style",
                        subtitle = settings.backgroundMusic.label,
                        icon = Icons.Default.QueueMusic,
                        onClick = { showMusicDialog = true }
                    )
                }

                item {
                    SettingItem(
                        title = "Add Custom Audio",
                        subtitle = "${settings.customMusicList.size} custom track${if (settings.customMusicList.size != 1) "s" else ""} added",
                        icon = Icons.Default.AudioFile,
                        onClick = { musicFilePicker.launch(arrayOf("audio/*")) }
                    )
                }

                // Show custom music list
                if (settings.customMusicList.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                settings.customMusicList.forEachIndexed { index, entry ->
                                    val isSelected = settings.backgroundMusic == BackgroundMusicOption.CUSTOM &&
                                            settings.selectedCustomMusicIndex == index
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.selectCustomMusic(index) }
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            if (isSelected) Icons.Default.CheckCircle
                                            else Icons.Default.MusicNote,
                                            null,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            entry.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(
                                            onClick = { viewModel.removeCustomMusic(index) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Close, "Remove",
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                    if (index < settings.customMusicList.lastIndex) {
                                        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    SettingSlider(
                        title = "Music Volume",
                        value = settings.backgroundMusicVolume,
                        valueRange = 0.05f..0.5f,
                        valueLabel = "${(settings.backgroundMusicVolume * 100).toInt()}%",
                        onValueChange = { viewModel.updateBackgroundMusicVolume(it) },
                        icon = Icons.Default.VolumeDown
                    )
                }

                item {
                    TestButton(
                        label = if (testingMusic) "Stop Music" else "Test Music",
                        icon = if (testingMusic) Icons.Default.Stop else Icons.Default.PlayArrow,
                        isActive = testingMusic,
                        onClick = { viewModel.testMusic() }
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
                item { HorizontalDivider() }

                // ============ VISUAL EFFECTS ============
                item {
                    SectionHeader(title = "Visual Effects", icon = Icons.Default.AutoAwesome)
                }

                item {
                    SettingItem(
                        title = "Ambient Effect",
                        subtitle = if (settings.visualEffect == VisualEffect.NONE) "None"
                                  else "${settings.visualEffect.emoji} ${settings.visualEffect.label}",
                        icon = Icons.Default.Palette,
                        onClick = { showVisualDialog = true }
                    )
                }

                if (settings.visualEffect != VisualEffect.NONE) {
                    item {
                        SettingSlider(
                            title = "Effect Intensity",
                            value = settings.visualEffectIntensity,
                            valueRange = 0.1f..1.0f,
                            valueLabel = "${(settings.visualEffectIntensity * 100).toInt()}%",
                            onValueChange = { viewModel.updateVisualEffectIntensity(it) },
                            icon = Icons.Default.Brightness6
                        )
                    }
                }

                item {
                    SettingSwitch(
                        title = "Breathing Animation",
                        subtitle = "Show calming breathing animation during playback",
                        checked = settings.showBreathingAnimation,
                        onCheckedChange = { viewModel.updateShowBreathing(it) },
                        icon = Icons.Default.Air
                    )
                }

                item {
                    TestButton(
                        label = if (testingVisual) "Stop Visual" else "Preview Visual Effect",
                        icon = if (testingVisual) Icons.Default.Stop else Icons.Default.Visibility,
                        isActive = testingVisual,
                        onClick = {
                            if (settings.visualEffect != VisualEffect.NONE) {
                                showVisualPreview = true
                            }
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
                item { HorizontalDivider() }

                // ============ PLAYBACK SETTINGS ============
                item {
                    SectionHeader(title = "Playback", icon = Icons.Default.PlayCircle)
                }

                item {
                    SettingSlider(
                        title = "Pause Between Affirmations",
                        value = settings.pauseBetweenAffirmations.toFloat(),
                        valueRange = 1f..10f,
                        steps = 8,
                        valueLabel = "${settings.pauseBetweenAffirmations}s",
                        onValueChange = { viewModel.updatePauseBetween(it.toInt()) },
                        icon = Icons.Default.Timer
                    )
                }

                item {
                    SettingSwitch(
                        title = "Auto-play on Open",
                        subtitle = "Start playing automatically when opening a session",
                        checked = settings.autoPlayOnOpen,
                        onCheckedChange = { viewModel.updateAutoPlay(it) },
                        icon = Icons.Default.PlayCircle
                    )
                }

                item {
                    SettingSwitch(
                        title = "Repeat Session",
                        subtitle = "Loop back to the first affirmation when finished",
                        checked = settings.repeatSession,
                        onCheckedChange = { viewModel.updateRepeatSession(it) },
                        icon = Icons.Default.Repeat
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
                item { HorizontalDivider() }

                // ============ TEST ALL ============
                item {
                    SectionHeader(title = "Test Everything", icon = Icons.Default.Science)
                }

                item {
                    TestAllButton(
                        isActive = testingAll,
                        onClick = { viewModel.testAll() }
                    )
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }

    // Voice Gender dialog
    if (showGenderDialog) {
        AlertDialog(
            onDismissRequest = { showGenderDialog = false },
            title = { Text("Voice Type") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    VoiceGender.entries.forEach { gender ->
                        GenderOption(
                            gender = gender,
                            isSelected = settings.voiceGender == gender,
                            onClick = {
                                viewModel.updateVoiceGender(gender)
                                showGenderDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGenderDialog = false }) { Text("Close") }
            }
        )
    }

    // Voice selection dialog
    if (showVoiceDialog) {
        val filteredVoices = viewModel.getFilteredVoices(settings.voiceGender)
        AlertDialog(
            onDismissRequest = { showVoiceDialog = false },
            title = {
                Column {
                    Text("Select Voice")
                    Text(
                        "Showing ${settings.voiceGender.label} voices",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    item {
                        VoiceOption(
                            name = "System Default",
                            isSelected = settings.ttsVoice.isEmpty(),
                            onClick = {
                                viewModel.updateTtsVoice("")
                                showVoiceDialog = false
                            }
                        )
                    }
                    items(filteredVoices) { voice ->
                        VoiceOption(
                            name = voice.displayName,
                            isSelected = settings.ttsVoice == voice.name,
                            isNetwork = voice.isNetworkRequired,
                            onClick = {
                                viewModel.updateTtsVoice(voice.name)
                                showVoiceDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showVoiceDialog = false }) { Text("Close") }
            }
        )
    }

    // Music selection dialog
    if (showMusicDialog) {
        AlertDialog(
            onDismissRequest = { showMusicDialog = false },
            title = { Text("Background Music") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    BackgroundMusicOption.entries.forEach { option ->
                        MusicOption(
                            option = option,
                            isSelected = settings.backgroundMusic == option,
                            onClick = {
                                viewModel.updateBackgroundMusic(option)
                                if (option == BackgroundMusicOption.CUSTOM && settings.customMusicUri.isEmpty()) {
                                    musicFilePicker.launch(arrayOf("audio/*"))
                                }
                                showMusicDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMusicDialog = false }) { Text("Close") }
            }
        )
    }

    // Visual effect selection dialog
    if (showVisualDialog) {
        AlertDialog(
            onDismissRequest = { showVisualDialog = false },
            title = { Text("Visual Effect") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    VisualEffect.entries.forEach { effect ->
                        VisualEffectOption(
                            effect = effect,
                            isSelected = settings.visualEffect == effect,
                            onClick = {
                                viewModel.updateVisualEffect(effect)
                                showVisualDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showVisualDialog = false }) { Text("Close") }
            }
        )
    }

    // Fullscreen visual effect preview
    if (showVisualPreview && settings.visualEffect != VisualEffect.NONE) {
        Dialog(
            onDismissRequest = { showVisualPreview = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0D1B2A),
                                Color(0xFF1B2838),
                                Color(0xFF0D1B2A)
                            )
                        )
                    )
            ) {
                VisualEffectOverlay(
                    effect = settings.visualEffect,
                    intensity = settings.visualEffectIntensity
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${settings.visualEffect.emoji} ${settings.visualEffect.label}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Tap anywhere to close",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
                // Tap anywhere to dismiss
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showVisualPreview = false }
                )
            }
        }
    }

    // Custom music name dialog
    if (showCustomMusicNameDialog && pendingMusicUri != null) {
        var musicName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = {
                showCustomMusicNameDialog = false
                pendingMusicUri = null
            },
            title = { Text("Name Your Track") },
            text = {
                OutlinedTextField(
                    value = musicName,
                    onValueChange = { musicName = it },
                    label = { Text("Track name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val name = musicName.ifBlank { "Custom Track ${settings.customMusicList.size + 1}" }
                        viewModel.addCustomMusic(name, pendingMusicUri!!)
                        viewModel.selectCustomMusic(settings.customMusicList.size) // select the new one
                        showCustomMusicNameDialog = false
                        pendingMusicUri = null
                    }
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showCustomMusicNameDialog = false
                    pendingMusicUri = null
                }) { Text("Cancel") }
            }
        )
    }
}

// ========== Section Header ==========
@Composable
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

// ========== Test Buttons ==========
@Composable
private fun TestButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val animColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        label = "testBtnColor"
    )

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = animColor)
    ) {
        Icon(icon, null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun TestAllButton(isActive: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            if (isActive) Icons.Default.Stop else Icons.Default.RocketLaunch,
            null, modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            if (isActive) "Stop All Tests" else "Test Voice + Music + Visual",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

// ========== Visual Effects Overlay ==========
@Composable
fun VisualEffectOverlay(effect: VisualEffect, intensity: Float) {
    val particleCount = (30 * intensity).toInt().coerceAtLeast(5)
    when (effect) {
        VisualEffect.SNOW -> ParticleEffect(
            count = particleCount, color = Color.White.copy(alpha = 0.8f),
            minSize = 3f, maxSize = 8f, speedY = 0.5f..2f, speedX = -0.3f..0.3f, sway = true
        )
        VisualEffect.RAIN -> ParticleEffect(
            count = particleCount * 2, color = Color(0xFF87CEEB).copy(alpha = 0.6f),
            minSize = 1f, maxSize = 3f, speedY = 4f..8f, speedX = 0.5f..1.5f, elongated = true
        )
        VisualEffect.FIREFLIES -> ParticleEffect(
            count = (particleCount * 0.5f).toInt().coerceAtLeast(3),
            color = Color(0xFFFFD700).copy(alpha = 0.7f),
            minSize = 3f, maxSize = 7f, speedY = -0.5f..0.5f, speedX = -0.5f..0.5f,
            glow = true, sway = true
        )
        VisualEffect.AURORA -> AuroraEffect(intensity)
        VisualEffect.SAKURA -> ParticleEffect(
            count = particleCount, color = Color(0xFFFFB7C5).copy(alpha = 0.7f),
            minSize = 5f, maxSize = 12f, speedY = 0.8f..2.5f, speedX = -1f..1f,
            sway = true, rotate = true
        )
        VisualEffect.STARS -> ParticleEffect(
            count = particleCount, color = Color.White.copy(alpha = 0.9f),
            minSize = 1f, maxSize = 4f, speedY = 0f..0f, speedX = 0f..0f, twinkle = true
        )
        VisualEffect.BUBBLES -> ParticleEffect(
            count = (particleCount * 0.6f).toInt().coerceAtLeast(3),
            color = Color(0xFF87CEEB).copy(alpha = 0.3f),
            minSize = 8f, maxSize = 25f, speedY = -1.5f..-0.3f, speedX = -0.3f..0.3f,
            sway = true, hollow = true
        )
        VisualEffect.NONE -> {}
    }
}

@Composable
private fun ParticleEffect(
    count: Int, color: Color, minSize: Float, maxSize: Float,
    speedY: ClosedFloatingPointRange<Float>, speedX: ClosedFloatingPointRange<Float>,
    sway: Boolean = false, elongated: Boolean = false, glow: Boolean = false,
    rotate: Boolean = false, twinkle: Boolean = false, hollow: Boolean = false
) {
    data class Particle(
        val x: Float, val y: Float, val size: Float,
        val vx: Float, val vy: Float, val phase: Float
    )

    val particles = remember {
        List(count) {
            Particle(
                x = Random.nextFloat(), y = Random.nextFloat(),
                size = Random.nextFloat() * (maxSize - minSize) + minSize,
                vx = Random.nextFloat() * (speedX.endInclusive - speedX.start) + speedX.start,
                vy = Random.nextFloat() * (speedY.endInclusive - speedY.start) + speedY.start,
                phase = Random.nextFloat() * 6.28f
            )
        }
    }

    val time by rememberInfiniteTransition(label = "particles").animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(100000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        particles.forEachIndexed { i, p ->
            val t = time + i * 0.1f
            val swayOffset = if (sway) sin(t * 0.02f + p.phase) * 20f else 0f
            val alpha = if (twinkle) (0.3f + 0.7f * ((sin(t * 0.05f + p.phase) + 1f) / 2f)) else 1f
            var px = (p.x * w + p.vx * t + swayOffset) % w
            var py = (p.y * h + p.vy * t) % h
            if (px < 0) px += w
            if (py < 0) py += h
            val drawColor = color.copy(alpha = color.alpha * alpha)
            if (elongated) {
                drawLine(drawColor, Offset(px, py), Offset(px + p.vx * 3, py + p.size * 4), p.size * 0.5f)
            } else if (hollow) {
                drawCircle(drawColor, p.size, Offset(px, py),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f))
            } else {
                if (glow) {
                    drawCircle(drawColor.copy(alpha = drawColor.alpha * 0.3f), p.size * 3, Offset(px, py))
                }
                drawCircle(drawColor, p.size, Offset(px, py))
            }
        }
    }
}

@Composable
private fun AuroraEffect(intensity: Float) {
    val time by rememberInfiniteTransition(label = "aurora").animateFloat(
        initialValue = 0f, targetValue = 6.28f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "auroraTime"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val colors = listOf(
            Color(0xFF00FF87).copy(alpha = 0.15f * intensity),
            Color(0xFF60EFFF).copy(alpha = 0.12f * intensity),
            Color(0xFFA855F7).copy(alpha = 0.1f * intensity)
        )
        for ((idx, col) in colors.withIndex()) {
            val yBase = h * 0.2f + idx * h * 0.15f
            for (x in 0..w.toInt() step 4) {
                val xf = x.toFloat()
                val wave = sin(xf * 0.003f + time + idx * 1.5f) * h * 0.1f
                drawCircle(col, 30f + sin(xf * 0.01f + time) * 15f, Offset(xf, yBase + wave))
            }
        }
    }
}

// ========== Setting Components ==========
@Composable
private fun SettingSlider(
    title: String, value: Float, valueRange: ClosedFloatingPointRange<Float>,
    valueLabel: String, onValueChange: (Float) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector, steps: Int = 0
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                Text(valueLabel, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Slider(value = value, onValueChange = onValueChange, valueRange = valueRange,
                steps = steps, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun SettingItem(
    title: String, subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SettingSwitch(
    title: String, subtitle: String, checked: Boolean,
    onCheckedChange: (Boolean) -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
private fun GenderOption(gender: VoiceGender, isSelected: Boolean, onClick: () -> Unit) {
    val emoji = when (gender) {
        VoiceGender.AUTO -> "🤖"; VoiceGender.MALE -> "🧑"; VoiceGender.FEMALE -> "👩"
    }
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(gender.label, style = MaterialTheme.typography.titleSmall)
                Text(gender.description, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
private fun VoiceOption(name: String, isSelected: Boolean, isNetwork: Boolean = false, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            if (isNetwork) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text("AI", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
        }
    }
}

@Composable
private fun MusicOption(option: BackgroundMusicOption, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(option.label, style = MaterialTheme.typography.bodyMedium)
                Text(option.description, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun VisualEffectOption(effect: VisualEffect, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (effect.emoji.isNotEmpty()) {
                Text(effect.emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(effect.label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp))
            }
        }
    }
}
