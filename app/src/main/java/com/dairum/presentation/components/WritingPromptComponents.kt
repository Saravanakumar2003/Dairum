package com.dairum.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dairum.domain.model.PromptCategory
import com.dairum.domain.model.WritingPrompt
import com.dairum.domain.model.WritingPrompts

/**
 * Writing prompts components for inspiration and journaling guidance.
 */

/**
 * Daily prompt card displayed on home screen
 */
@Composable
fun DailyPromptCard(
    onUsePrompt: (WritingPrompt) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPrompt by remember { mutableStateOf(WritingPrompts.getPromptOfTheDay()) }
    var isRefreshing by remember { mutableStateOf(false) }
    
    val rotation by animateFloatAsState(
        targetValue = if (isRefreshing) 360f else 0f,
        animationSpec = tween(durationMillis = 300),
        finishedListener = { isRefreshing = false },
        label = "refresh_rotation"
    )
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Writing Prompt",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                // Refresh button
                IconButton(
                    onClick = {
                        isRefreshing = true
                        currentPrompt = WritingPrompts.getRandomPrompt()
                        onRefresh()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "New prompt",
                        modifier = Modifier
                            .size(18.dp)
                            .rotate(rotation),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Prompt text
            Text(
                text = "\"${currentPrompt.text}\"",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Category badge and use button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryBadgeSmall(category = currentPrompt.category)
                
                FilledTonalButton(
                    onClick = { onUsePrompt(currentPrompt) },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EditNote,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Writing")
                }
            }
        }
    }
}

/**
 * Compact prompt chip for inline display
 */
@Composable
fun PromptChip(
    prompt: WritingPrompt,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = getCategoryIcon(prompt.category),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = prompt.text.take(50) + if (prompt.text.length > 50) "..." else "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1
            )
        }
    }
}

/**
 * Prompt selector carousel
 */
@Composable
fun PromptCarousel(
    prompts: List<WritingPrompt>,
    onPromptSelect: (WritingPrompt) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(prompts) { prompt ->
            PromptCarouselCard(
                prompt = prompt,
                onClick = { onPromptSelect(prompt) }
            )
        }
    }
}

/**
 * Individual prompt card in carousel
 */
@Composable
private fun PromptCarouselCard(
    prompt: WritingPrompt,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(280.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = getCategoryColor(prompt.category).copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = getCategoryIcon(prompt.category),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = getCategoryColor(prompt.category)
                )
                Text(
                    text = prompt.category.name.replace("_", " ").lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelMedium,
                    color = getCategoryColor(prompt.category)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = prompt.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                minLines = 2,
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onClick) {
                    Text("Use this prompt")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Category filter chips for prompts
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PromptCategoryFilter(
    selectedCategory: PromptCategory?,
    onCategorySelect: (PromptCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // All option
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelect(null) },
            label = { Text("All") },
            leadingIcon = if (selectedCategory == null) {
                { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) }
            } else null
        )
        
        PromptCategory.entries.forEach { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelect(category) },
                label = { 
                    Text(
                        category.name.replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() }
                    ) 
                },
                leadingIcon = {
                    Icon(
                        imageVector = getCategoryIcon(category),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
    }
}

/**
 * Small category badge
 */
@Composable
private fun CategoryBadgeSmall(
    category: PromptCategory,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = getCategoryColor(category).copy(alpha = 0.2f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = getCategoryIcon(category),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = getCategoryColor(category)
            )
            Text(
                text = category.name.replace("_", " ").lowercase()
                    .replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelSmall,
                color = getCategoryColor(category)
            )
        }
    }
}

/**
 * Get icon for prompt category
 */
private fun getCategoryIcon(category: PromptCategory): ImageVector {
    return when (category) {
        PromptCategory.REFLECTION -> Icons.Outlined.Psychology
        PromptCategory.GRATITUDE -> Icons.Outlined.Favorite
        PromptCategory.GOALS -> Icons.Outlined.Flag
        PromptCategory.CREATIVITY -> Icons.Outlined.Palette
        PromptCategory.RELATIONSHIPS -> Icons.Outlined.People
        PromptCategory.SELF_DISCOVERY -> Icons.Outlined.Explore
        PromptCategory.MINDFULNESS -> Icons.Outlined.SelfImprovement
        PromptCategory.GROWTH -> Icons.Outlined.TrendingUp
        PromptCategory.MEMORIES -> Icons.Outlined.Photo
        PromptCategory.FUTURE -> Icons.Outlined.RocketLaunch
        PromptCategory.DAILY_LIFE -> Icons.Outlined.Today
        PromptCategory.EMOTIONS -> Icons.Outlined.SentimentSatisfied
    }
}

/**
 * Get color for prompt category
 */
@Composable
private fun getCategoryColor(category: PromptCategory): Color {
    return when (category) {
        PromptCategory.REFLECTION -> Color(0xFF6750A4)
        PromptCategory.GRATITUDE -> Color(0xFFE91E63)
        PromptCategory.GOALS -> Color(0xFF4CAF50)
        PromptCategory.CREATIVITY -> Color(0xFFFF9800)
        PromptCategory.RELATIONSHIPS -> Color(0xFF2196F3)
        PromptCategory.SELF_DISCOVERY -> Color(0xFF9C27B0)
        PromptCategory.MINDFULNESS -> Color(0xFF00BCD4)
        PromptCategory.GROWTH -> Color(0xFF8BC34A)
        PromptCategory.MEMORIES -> Color(0xFF795548)
        PromptCategory.FUTURE -> Color(0xFF3F51B5)
        PromptCategory.DAILY_LIFE -> Color(0xFF607D8B)
        PromptCategory.EMOTIONS -> Color(0xFFFF5722)
    }
}

/**
 * Expandable prompt suggestions panel
 */
@Composable
fun PromptSuggestionsPanel(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onPromptSelect: (WritingPrompt) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<PromptCategory?>(null) }
    val prompts = remember(selectedCategory) {
        if (selectedCategory != null) {
            WritingPrompts.getPromptsByCategory(selectedCategory!!)
        } else {
            (1..10).map { WritingPrompts.getRandomPrompt() }.distinctBy { it.id }
        }
    }
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Toggle header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isExpanded) 0.dp else 16.dp,
                bottomEnd = if (isExpanded) 0.dp else 16.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Need inspiration?",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }
        }
        
        // Expandable content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Category filter
                    PromptCategoryFilter(
                        selectedCategory = selectedCategory,
                        onCategorySelect = { selectedCategory = it }
                    )
                    
                    // Prompts carousel
                    PromptCarousel(
                        prompts = prompts,
                        onPromptSelect = onPromptSelect
                    )
                }
            }
        }
    }
}
