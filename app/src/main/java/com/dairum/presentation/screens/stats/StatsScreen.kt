package com.dairum.presentation.screens.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dairum.R
import com.dairum.domain.model.*
import com.dairum.presentation.components.MoodIndicator
import com.dairum.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var selectedTab by remember { mutableStateOf(StatsTab.OVERVIEW) }
    var selectedTimeRange by remember { mutableStateOf(TimeRange.MONTH) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_stats)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab row
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatsTab.values().forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.title) }
                    )
                }
            }
            
            // Time range selector
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TimeRange.values().toList()) { range ->
                    FilterChip(
                        selected = selectedTimeRange == range,
                        onClick = { 
                            selectedTimeRange = range
                            viewModel.loadStatsForRange(range)
                        },
                        label = { Text(range.label) }
                    )
                }
            }
            
            // Content with loading/empty states
            when {
                uiState.isLoading -> {
                    // Show shimmer loading state
                    ShimmerStatsContent()
                }
                else -> {
                    when (selectedTab) {
                        StatsTab.OVERVIEW -> OverviewContent(stats = uiState.journalStats)
                        StatsTab.ENTRIES -> EntriesContent(stats = uiState.activityStats)
                        StatsTab.MOOD -> MoodContent(stats = uiState.moodStats)
                        StatsTab.TAGS -> TagsContent(stats = uiState.tagStats)
                        StatsTab.PEOPLE -> PeopleContent(stats = uiState.peopleStats)
                        StatsTab.TRACKERS -> TrackersContent(stats = uiState.trackerStats)
                    }
                }
            }
        }
    }
}

@Composable
private fun ShimmerStatsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Shimmer cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            com.dairum.presentation.components.ShimmerStatsCard(modifier = Modifier.weight(1f))
            com.dairum.presentation.components.ShimmerStatsCard(modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            com.dairum.presentation.components.ShimmerStatsCard(modifier = Modifier.weight(1f))
            com.dairum.presentation.components.ShimmerStatsCard(modifier = Modifier.weight(1f))
        }
        // Larger shimmer card for chart
        com.dairum.presentation.components.ShimmerStatsCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@Composable
private fun OverviewContent(stats: JournalStats) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    title = stringResource(R.string.stats_total_entries),
                    value = stats.totalEntries.toString(),
                    icon = Icons.Outlined.EditNote,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = stringResource(R.string.stats_this_month),
                    value = stats.entriesThisMonth.toString(),
                    icon = Icons.Outlined.CalendarMonth,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    title = stringResource(R.string.stats_current_streak),
                    value = "${stats.currentStreak} ${stringResource(R.string.stats_days)}",
                    icon = Icons.Outlined.LocalFireDepartment,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = stringResource(R.string.stats_longest_streak),
                    value = "${stats.longestStreak} ${stringResource(R.string.stats_days)}",
                    icon = Icons.Outlined.EmojiEvents,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Media stats
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Media",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MediaStatItem(
                            icon = Icons.Outlined.Photo,
                            label = "Photos",
                            count = stats.totalPhotos
                        )
                        MediaStatItem(
                            icon = Icons.Outlined.Videocam,
                            label = "Videos",
                            count = stats.totalVideos
                        )
                        MediaStatItem(
                            icon = Icons.Outlined.Mic,
                            label = "Voice",
                            count = stats.totalVoiceNotes
                        )
                        MediaStatItem(
                            icon = Icons.Outlined.AttachFile,
                            label = "Files",
                            count = stats.totalAttachments
                        )
                    }
                }
            }
        }
        
        // Word count stats
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Writing Stats",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Total Words",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "%,d".format(stats.totalWordCount),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Avg. per Entry",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "%.0f words".format(stats.averageWordCount),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EntriesContent(stats: EntryActivityStats?) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Activity chart placeholder
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Hourly Distribution",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (stats != null && stats.hourlyDistribution.isNotEmpty()) {
                        HourlyBarChart(
                            data = stats.hourlyDistribution,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No activity data yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // Most active time
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Most Active Time",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    stats?.mostActiveHour?.let { hour ->
                        Text(
                            text = "%02d:00 - %02d:00".format(hour, (hour + 1) % 24),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } ?: Text(
                        text = "Not enough data",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // Day of week distribution
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Entries by Day",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        days.forEachIndexed { index, day ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val count = stats?.weekdayDistribution?.get(index + 1) ?: 0
                                Text(
                                    text = count.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MoodContent(stats: MoodStats?) {
    val distribution = stats?.distribution ?: emptyList()
    val totalMoods = distribution.sumOf { it.count }
    
    // Group moods by category
    val positiveCount = distribution.filter { item ->
        MoodOption.fromId(item.level)?.category == MoodCategory.POSITIVE
    }.sumOf { it.count }
    val negativeCount = distribution.filter { item ->
        MoodOption.fromId(item.level)?.category == MoodCategory.NEGATIVE
    }.sumOf { it.count }
    val neutralCount = distribution.filter { item ->
        MoodOption.fromId(item.level)?.category == MoodCategory.NEUTRAL
    }.sumOf { it.count }
    val mixedCount = distribution.filter { item ->
        MoodOption.fromId(item.level)?.category == MoodCategory.MIXED
    }.sumOf { it.count }
    
    // Top moods
    val topMoods = distribution
        .sortedByDescending { it.count }
        .take(5)
        .mapNotNull { item ->
            MoodOption.fromId(item.level)?.let { option ->
                option to item
            }
        }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mood Category Summary - shows distribution across positive/negative/neutral
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Mood Distribution",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (totalMoods > 0) {
                        // Stacked bar showing positive/neutral/negative
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            if (positiveCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .weight(positiveCount.toFloat())
                                        .fillMaxHeight()
                                        .background(MoodAmazing)
                                )
                            }
                            if (neutralCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .weight(neutralCount.toFloat())
                                        .fillMaxHeight()
                                        .background(MoodOkay)
                                )
                            }
                            if (mixedCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .weight(mixedCount.toFloat())
                                        .fillMaxHeight()
                                        .background(MoodBad)
                                )
                            }
                            if (negativeCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .weight(negativeCount.toFloat())
                                        .fillMaxHeight()
                                        .background(MoodTerrible)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Category breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            MoodCategoryStat(
                                emoji = "😊",
                                label = "Positive",
                                count = positiveCount,
                                percentage = positiveCount.toFloat() / totalMoods,
                                color = MoodAmazing
                            )
                            MoodCategoryStat(
                                emoji = "😐",
                                label = "Neutral",
                                count = neutralCount,
                                percentage = neutralCount.toFloat() / totalMoods,
                                color = MoodOkay
                            )
                            MoodCategoryStat(
                                emoji = "🤔",
                                label = "Mixed",
                                count = mixedCount,
                                percentage = mixedCount.toFloat() / totalMoods,
                                color = MoodBad
                            )
                            MoodCategoryStat(
                                emoji = "😔",
                                label = "Negative",
                                count = negativeCount,
                                percentage = negativeCount.toFloat() / totalMoods,
                                color = MoodTerrible
                            )
                        }
                    } else {
                        Text(
                            text = "No mood data yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Top Moods section
        if (topMoods.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Top Moods",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        topMoods.forEachIndexed { index, (moodOption, item) ->
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.width(24.dp)
                                )
                                
                                Text(
                                    text = moodOption.emoji,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = moodOption.label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    
                                    // Progress bar
                                    val maxCount = topMoods.firstOrNull()?.second?.count ?: 1
                                    val progress = item.count.toFloat() / maxCount
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${item.count}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${(item.percentage * 100).toInt()}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Extended Mood Distribution (scrollable)
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "All Moods",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Extended mood tracking",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val distribution = stats?.distribution ?: emptyList()
                    
                    // Positive moods
                    MoodCategorySection(
                        title = "😊 Positive",
                        moods = MoodOption.values().filter { it.category == MoodCategory.POSITIVE },
                        distribution = distribution
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Neutral moods
                    MoodCategorySection(
                        title = "😐 Neutral",
                        moods = MoodOption.values().filter { it.category == MoodCategory.NEUTRAL },
                        distribution = distribution
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Negative moods
                    MoodCategorySection(
                        title = "😔 Negative",
                        moods = MoodOption.values().filter { it.category == MoodCategory.NEGATIVE },
                        distribution = distribution
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Mixed moods
                    MoodCategorySection(
                        title = "🤔 Mixed",
                        moods = MoodOption.values().filter { it.category == MoodCategory.MIXED },
                        distribution = distribution
                    )
                }
            }
        }
        
        // Most Common Mood (replaces average mood which doesn't make sense for extended moods)
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Most Common Mood",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val mostCommon = stats?.mostCommonMood?.let { moodId ->
                        MoodOption.fromId(moodId)
                    }
                    
                    if (mostCommon != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = mostCommon.emoji,
                                style = MaterialTheme.typography.displaySmall
                            )
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column {
                                Text(
                                    text = mostCommon.label,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = when (mostCommon.category) {
                                        MoodCategory.POSITIVE -> "Positive mood"
                                        MoodCategory.NEUTRAL -> "Neutral mood"
                                        MoodCategory.NEGATIVE -> "Negative mood"
                                        MoodCategory.MIXED -> "Mixed mood"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Not enough data",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Total entries with moods
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Entries with Mood",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$totalMoods",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodCategoryStat(
    emoji: String,
    label: String,
    count: Int,
    percentage: Float,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${(percentage * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
private fun TagsContent(stats: TagStats?) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Top Tags",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        val topTags = stats?.topTags ?: emptyList()
        items(topTags.take(10)) { tagUsage ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${tagUsage.tag.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Text(
                        text = "${tagUsage.count} entries",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        if (topTags.isEmpty()) {
            item {
                Text(
                    text = "No tags used yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PeopleContent(stats: PeopleStats?) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    title = "Total People",
                    value = (stats?.totalPeople ?: 0).toString(),
                    icon = Icons.Outlined.People,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Most Mentioned",
                    value = stats?.topPeople?.firstOrNull()?.person?.name ?: "-",
                    icon = Icons.Outlined.PersonPin,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Text(
                text = "People Mentioned",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        val topPeople = stats?.topPeople ?: emptyList()
        items(topPeople.take(15)) { mention ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mention.person.name.take(1).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = mention.person.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        mention.person.relationship?.let { rel ->
                            Text(
                                text = rel,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${mention.count}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "mentions",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        if (topPeople.isEmpty()) {
            item {
                Text(
                    text = "No people mentioned yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TrackersContent(stats: List<TrackerStats>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(stats) { trackerStat ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = trackerStat.tracker.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Entries",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = trackerStat.count.toString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        
                        trackerStat.average?.let { avg ->
                            Column {
                                Text(
                                    text = "Average",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "%.1f".format(avg) + (trackerStat.tracker.unit?.let { " $it" } ?: ""),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        
                        trackerStat.min?.let { min ->
                            Column {
                                Text(
                                    text = "Min",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "%.1f".format(min),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        
                        trackerStat.max?.let { max ->
                            Column {
                                Text(
                                    text = "Max",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "%.1f".format(max),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }
        
        if (stats.isEmpty()) {
            item {
                Text(
                    text = "No tracker data yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MediaStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    count: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

enum class StatsTab(val title: String) {
    OVERVIEW("Overview"),
    ENTRIES("Entries"),
    MOOD("Mood"),
    TAGS("Tags"),
    PEOPLE("People"),
    TRACKERS("Trackers")
}

enum class TimeRange(val label: String) {
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year"),
    ALL_TIME("All Time")
}

@Composable
private fun HourlyBarChart(
    data: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val maxValue = data.values.maxOrNull()?.toFloat() ?: 1f
    
    Canvas(modifier = modifier) {
        val barWidth = size.width / 24f
        val maxHeight = size.height * 0.8f
        
        for (hour in 0 until 24) {
            val count = data[hour] ?: 0
            val barHeight = if (maxValue > 0) (count / maxValue) * maxHeight else 0f
            
            // Background bar
            drawRoundRect(
                color = surfaceVariant,
                topLeft = Offset(hour * barWidth + 2, size.height - maxHeight),
                size = Size(barWidth - 4, maxHeight),
                cornerRadius = CornerRadius(4f, 4f)
            )
            
            // Data bar
            if (barHeight > 0) {
                drawRoundRect(
                    color = primaryColor,
                    topLeft = Offset(hour * barWidth + 2, size.height - barHeight),
                    size = Size(barWidth - 4, barHeight),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
        }
    }
}

@Composable
private fun MoodBarChart(
    distribution: List<MoodDistributionItem>,
    modifier: Modifier = Modifier
) {
    val moodColors = listOf(
        MoodTerrible, MoodBad, MoodOkay, MoodGood, MoodAmazing
    )
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val maxValue = distribution.maxOfOrNull { it.count }?.toFloat() ?: 1f
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        for (level in 1..5) {
            val item = distribution.find { it.level == level }
            val count = item?.count ?: 0
            val percentage = if (maxValue > 0) count / maxValue else 0f
            val barHeight = (percentage * 100).coerceIn(8f, 100f)
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height((barHeight).dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(if (count > 0) moodColors[level - 1] else surfaceVariant)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = Mood(level).emoji,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MoodCategorySection(
    title: String,
    moods: List<MoodOption>,
    distribution: List<MoodDistributionItem>
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            moods.forEach { moodOption ->
                val count = distribution.find { it.level == moodOption.id }?.count ?: 0
                
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (count > 0) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = moodOption.emoji,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = moodOption.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (count > 0) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                        if (count > 0) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = count.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary
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
