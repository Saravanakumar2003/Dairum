package com.dairum.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer

/**
 * Shimmer effect components for loading states.
 * Provides skeleton placeholders that match the actual UI components.
 */

/**
 * Shimmer brush for gradient animation
 */
@Composable
fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
    )
    
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )
}

/**
 * Shimmer placeholder for EntryCard
 */
@Composable
fun ShimmerEntryCard(
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(shimmer),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShimmerBox(width = 100.dp, height = 14.dp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ShimmerCircle(size = 20.dp)
                    ShimmerCircle(size = 24.dp)
                    ShimmerCircle(size = 20.dp)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Title
            ShimmerBox(width = 200.dp, height = 20.dp)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Content preview - 3 lines
            ShimmerBox(width = 280.dp, height = 14.dp)
            Spacer(modifier = Modifier.height(6.dp))
            ShimmerBox(width = 260.dp, height = 14.dp)
            Spacer(modifier = Modifier.height(6.dp))
            ShimmerBox(width = 180.dp, height = 14.dp)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Tags row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerBox(width = 60.dp, height = 24.dp, cornerRadius = 12.dp)
                ShimmerBox(width = 80.dp, height = 24.dp, cornerRadius = 12.dp)
                ShimmerBox(width = 50.dp, height = 24.dp, cornerRadius = 12.dp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Footer row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerBox(width = 80.dp, height = 20.dp, cornerRadius = 10.dp)
                ShimmerBox(width = 100.dp, height = 14.dp)
            }
        }
    }
}

/**
 * Shimmer placeholder for Category chip
 */
@Composable
fun ShimmerCategoryChip(
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    
    Box(
        modifier = modifier
            .shimmer(shimmer)
            .size(width = 100.dp, height = 80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

/**
 * Shimmer placeholder for Tag chip
 */
@Composable
fun ShimmerTagChip(
    modifier: Modifier = Modifier,
    width: Dp = 70.dp
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    
    Box(
        modifier = modifier
            .shimmer(shimmer)
            .size(width = width, height = 28.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

/**
 * Shimmer placeholder for Stats card
 */
@Composable
fun ShimmerStatsCard(
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(shimmer),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShimmerCircle(size = 48.dp)
            Spacer(modifier = Modifier.height(8.dp))
            ShimmerBox(width = 60.dp, height = 24.dp)
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerBox(width = 80.dp, height = 14.dp)
        }
    }
}

/**
 * Shimmer placeholder for Memory card
 */
@Composable
fun ShimmerMemoryCard(
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .shimmer(shimmer),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top row with icon and favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerCircle(size = 32.dp)
                ShimmerCircle(size = 24.dp)
            }
            
            // Bottom content
            Column {
                ShimmerBox(width = 150.dp, height = 20.dp)
                Spacer(modifier = Modifier.height(8.dp))
                ShimmerBox(width = 200.dp, height = 14.dp)
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerBox(width = 100.dp, height = 12.dp)
            }
        }
    }
}

/**
 * Shimmer placeholder for Person avatar
 */
@Composable
fun ShimmerPersonItem(
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(shimmer)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ShimmerCircle(size = 48.dp)
        Column {
            ShimmerBox(width = 120.dp, height = 16.dp)
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerBox(width = 80.dp, height = 12.dp)
        }
    }
}

/**
 * Shimmer loading list for home screen
 */
@Composable
fun ShimmerEntryList(
    itemCount: Int = 5,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(itemCount) {
            ShimmerEntryCard()
        }
    }
}

/**
 * Shimmer loading grid for categories
 */
@Composable
fun ShimmerCategoryGrid(
    columns: Int = 3,
    rowCount: Int = 2,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(rowCount) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(columns) {
                    ShimmerCategoryChip(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Basic shimmer box shape
 */
@Composable
fun ShimmerBox(
    width: Dp,
    height: Dp,
    cornerRadius: Dp = 4.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

/**
 * Basic shimmer circle shape
 */
@Composable
fun ShimmerCircle(
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

/**
 * Shimmer full screen loading
 */
@Composable
fun ShimmerFullScreenLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header section
        ShimmerBox(width = 150.dp, height = 28.dp)
        Spacer(modifier = Modifier.height(8.dp))
        
        // Categories row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(4) {
                ShimmerCategoryChip(
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Entry list
        ShimmerEntryList(itemCount = 4)
    }
}
