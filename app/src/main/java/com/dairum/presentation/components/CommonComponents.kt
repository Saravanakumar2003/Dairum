@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.dairum.presentation.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dairum.domain.model.*
import com.dairum.presentation.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Entry card for timeline and list views
 */
@Composable
fun EntryCard(
    entry: JournalEntry,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    handleClickInternally: Boolean = true
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (handleClickInternally) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row with date, category, mood
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date and time
                Text(
                    text = formatEntryDate(entry.createdAt),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Weather icon
                    entry.weather?.let { weather ->
                        WeatherBadge(weather = weather)
                    }
                    
                    // Mood indicator
                    entry.mood?.let { mood ->
                        MoodIndicator(mood = mood)
                    }
                    
                    // Favorite button
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (entry.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (entry.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Title
            entry.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            // Content preview
            if (entry.plainContent.isNotBlank()) {
                Text(
                    text = entry.plainContent,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Media thumbnails
            if (entry.media.isNotEmpty()) {
                MediaThumbnailRow(
                    media = entry.media.take(4),
                    totalCount = entry.media.size
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Tags
            if (entry.tags.isNotEmpty()) {
                TagChipRow(tags = entry.tags.take(5))
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Footer with category and location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category badge
                entry.category?.let { category ->
                    CategoryBadge(category = category)
                }
                
                // Location
                entry.location?.let { location ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = location.placeName ?: location.address ?: "Location",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

/**
 * Mood indicator circle
 */
@Composable
fun MoodIndicator(
    mood: Mood,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    showLabel: Boolean = false
) {
    // Determine color based on mood type and category
    val color = when (mood.moodType) {
        MoodType.BASIC -> when (mood.level) {
            1 -> MoodTerrible
            2 -> MoodBad
            3 -> MoodOkay
            4 -> MoodGood
            5 -> MoodAmazing
            else -> MaterialTheme.colorScheme.outline
        }
        MoodType.EXTENDED -> {
            val category = MoodOption.fromId(mood.level)?.category
            when (category) {
                MoodCategory.POSITIVE -> MoodAmazing
                MoodCategory.NEUTRAL -> MoodOkay
                MoodCategory.NEGATIVE -> MoodBad
                MoodCategory.MIXED -> MoodOkay
                null -> MaterialTheme.colorScheme.outline
            }
        }
    }
    
    if (showLabel) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mood.emoji,
                    style = if (size >= 40.dp) MaterialTheme.typography.titleLarge else MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mood.label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Box(
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mood.emoji,
                style = if (size >= 40.dp) MaterialTheme.typography.titleLarge else MaterialTheme.typography.labelSmall
            )
        }
    }
}

/**
 * Mood selector showing all 30 moods in a grid
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodSelector(
    selectedMood: Mood?,
    onMoodSelected: (Mood?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // All moods in a flow row
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MoodOption.values().forEach { moodOption ->
                val mood = Mood(level = moodOption.id, moodType = MoodType.EXTENDED)
                val isSelected = selectedMood?.level == moodOption.id && 
                    selectedMood.moodType == MoodType.EXTENDED
                
                Surface(
                    modifier = Modifier.clickable { 
                        onMoodSelected(if (isSelected) null else mood)
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = moodOption.emoji)
                        Text(
                            text = moodOption.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Extended mood grid with categorized moods
 */
@Composable
fun ExtendedMoodGrid(
    selectedMood: Mood?,
    onMoodSelected: (Mood?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        MoodCategory.values().forEach { category ->
            val moods = MoodOption.getByCategory(category)
            
            Text(
                text = when (category) {
                    MoodCategory.POSITIVE -> "😊 Positive"
                    MoodCategory.NEUTRAL -> "😐 Neutral"
                    MoodCategory.NEGATIVE -> "😔 Negative"
                    MoodCategory.MIXED -> "🤔 Other"
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            // Grid of mood chips
            androidx.compose.foundation.layout.FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                moods.forEach { moodOption ->
                    val mood = Mood(level = moodOption.id, moodType = MoodType.EXTENDED)
                    val isSelected = selectedMood?.level == moodOption.id && 
                        selectedMood.moodType == MoodType.EXTENDED
                    
                    Surface(
                        modifier = Modifier.clickable { 
                            onMoodSelected(if (isSelected) null else mood)
                        },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = moodOption.emoji)
                            Text(
                                text = moodOption.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * Weather badge
 */
@Composable
fun WeatherBadge(
    weather: Weather,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AsyncImage(
            model = weather.icon,
            contentDescription = weather.condition,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = "${weather.temperature.toInt()}°",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Category badge
 */
@Composable
fun CategoryBadge(
    category: Category,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = Color(category.color).copy(alpha = 0.2f)
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            color = Color(category.color),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * Tag chip
 */
@Composable
fun TagChip(
    tag: Tag,
    onClick: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val chipModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }
    
    Surface(
        modifier = chipModifier,
        shape = RoundedCornerShape(16.dp),
        color = (tag.color?.let { Color(it) } ?: MaterialTheme.colorScheme.primary).copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "#${tag.name}",
                style = MaterialTheme.typography.labelMedium,
                color = tag.color?.let { Color(it) } ?: MaterialTheme.colorScheme.primary
            )
            
            onRemove?.let {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove tag",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = it),
                    tint = tag.color?.let { Color(it) } ?: MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Row of tag chips
 */
@Composable
fun TagChipRow(
    tags: List<Tag>,
    modifier: Modifier = Modifier,
    onTagClick: ((Tag) -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            TagChip(
                tag = tag,
                onClick = onTagClick?.let { { it(tag) } }
            )
        }
    }
}

/**
 * Media thumbnail row
 */
@Composable
fun MediaThumbnailRow(
    media: List<Media>,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        media.forEachIndexed { index, item ->
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                when (item.type) {
                    MediaType.PHOTO, MediaType.VIDEO -> {
                        AsyncImage(
                            model = item.thumbnailUri ?: item.uri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        
                        if (item.type == MediaType.VIDEO) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(24.dp),
                                tint = Color.White
                            )
                        }
                    }
                    MediaType.VOICE -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    MediaType.ATTACHMENT -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                
                // Show count if there are more items
                if (index == media.lastIndex && totalCount > media.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${totalCount - media.size}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Empty state
 */
@Composable
fun EmptyState(
    icon: @Composable () -> Unit,
    title: String,
    message: String,
    action: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        action?.let {
            Spacer(modifier = Modifier.height(24.dp))
            it()
        }
    }
}

// Helper function
private fun formatEntryDate(dateTime: LocalDateTime): String {
    val now = LocalDateTime.now()
    val daysDiff = ChronoUnit.DAYS.between(dateTime.toLocalDate(), now.toLocalDate())
    
    return when {
        daysDiff == 0L -> {
            val hoursDiff = ChronoUnit.HOURS.between(dateTime, now)
            val minutesDiff = ChronoUnit.MINUTES.between(dateTime, now)
            when {
                minutesDiff < 1 -> "Just now"
                minutesDiff < 60 -> "$minutesDiff min ago"
                hoursDiff < 24 -> "$hoursDiff hours ago"
                else -> dateTime.format(DateTimeFormatter.ofPattern("h:mm a"))
            }
        }
        daysDiff == 1L -> "Yesterday"
        daysDiff < 7 -> dateTime.format(DateTimeFormatter.ofPattern("EEEE"))
        else -> dateTime.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
    }
}
