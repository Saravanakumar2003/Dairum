package com.dairum.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Empty state components for when there's no data to display.
 * Includes animated illustrations and helpful messages.
 */

/**
 * Sealed class representing different empty state types
 */
sealed class EmptyStateType(
    val icon: ImageVector,
    val title: String,
    val message: String,
    val actionLabel: String? = null
) {
    object NoEntries : EmptyStateType(
        icon = Icons.Outlined.EditNote,
        title = "Start Your Journey",
        message = "Your journal is empty. Write your first entry and begin documenting your life's moments.",
        actionLabel = "Write Entry"
    )
    
    object NoSearchResults : EmptyStateType(
        icon = Icons.Outlined.SearchOff,
        title = "No Results Found",
        message = "Try adjusting your search or using different keywords."
    )
    
    object NoFavorites : EmptyStateType(
        icon = Icons.Outlined.FavoriteBorder,
        title = "No Favorites Yet",
        message = "Mark your special entries as favorites to find them here quickly.",
        actionLabel = "View Entries"
    )
    
    object NoMediaFiles : EmptyStateType(
        icon = Icons.Outlined.PhotoLibrary,
        title = "No Media Files",
        message = "Add photos, videos, or voice notes to your entries to see them here."
    )
    
    object NoMemories : EmptyStateType(
        icon = Icons.Outlined.CollectionsBookmark,
        title = "Create Your First Memory",
        message = "Group related entries together to create meaningful memory collections.",
        actionLabel = "Create Memory"
    )
    
    object NoTags : EmptyStateType(
        icon = Icons.Outlined.Label,
        title = "No Tags Yet",
        message = "Create tags to organize and categorize your journal entries.",
        actionLabel = "Create Tag"
    )
    
    object NoPeople : EmptyStateType(
        icon = Icons.Outlined.People,
        title = "No People Added",
        message = "Add people you write about frequently to track mentions easily.",
        actionLabel = "Add Person"
    )
    
    object NoTrackers : EmptyStateType(
        icon = Icons.Outlined.Timeline,
        title = "No Trackers Set Up",
        message = "Create custom trackers to monitor habits, moods, or activities over time.",
        actionLabel = "Create Tracker"
    )
    
    object NoStats : EmptyStateType(
        icon = Icons.Outlined.BarChart,
        title = "Not Enough Data",
        message = "Write more entries to unlock meaningful insights and statistics."
    )
    
    object NoExports : EmptyStateType(
        icon = Icons.Outlined.FileDownload,
        title = "No Export History",
        message = "Export your journal to PDF or text format to see history here.",
        actionLabel = "Export Now"
    )
    
    object NoNotifications : EmptyStateType(
        icon = Icons.Outlined.Notifications,
        title = "No Notifications",
        message = "You're all caught up! Set up reminders to stay on track with journaling.",
        actionLabel = "Set Reminder"
    )
    
    object NoDrafts : EmptyStateType(
        icon = Icons.Outlined.Drafts,
        title = "No Drafts",
        message = "Unfinished entries will appear here so you can complete them later."
    )
    
    object NoArchivedEntries : EmptyStateType(
        icon = Icons.Outlined.Archive,
        title = "No Archived Entries",
        message = "Archive old entries to declutter your timeline while keeping them safe."
    )
    
    data class Custom(
        private val customIcon: ImageVector,
        private val customTitle: String,
        private val customMessage: String,
        private val customActionLabel: String? = null
    ) : EmptyStateType(customIcon, customTitle, customMessage, customActionLabel)
}

/**
 * Main empty state component with animated illustration
 */
@Composable
fun EmptyState(
    type: EmptyStateType,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated illustration
        EmptyStateIllustration(type = type)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title
        Text(
            text = type.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Message
        Text(
            text = type.message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 280.dp)
        )
        
        // Optional action button
        if (type.actionLabel != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onActionClick,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = type.actionLabel)
            }
        }
    }
}

/**
 * Animated illustration for empty states
 */
@Composable
fun EmptyStateIllustration(
    type: EmptyStateType,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "empty_state_anim")
    
    // Floating animation
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    // Pulse animation for background
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    // Alpha animation
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Box(
        modifier = modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        // Background decorative circles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            
            // Outer ring
            drawCircle(
                color = Color(0xFFE8DEF8).copy(alpha = alpha * 0.5f),
                radius = 55.dp.toPx() * pulseScale,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2.dp.toPx())
            )
            
            // Inner filled circle
            drawCircle(
                color = Color(0xFFF3EDF7),
                radius = 45.dp.toPx(),
                center = Offset(centerX, centerY)
            )
            
            // Decorative dots
            val dotCount = 6
            val dotRadius = 3.dp.toPx()
            val orbitRadius = 52.dp.toPx()
            
            repeat(dotCount) { i ->
                val angle = (i * 360f / dotCount + (alpha * 30f)) * (Math.PI.toFloat() / 180f)
                val x = centerX + orbitRadius * cos(angle)
                val y = centerY + orbitRadius * sin(angle)
                drawCircle(
                    color = Color(0xFFD0BCFF).copy(alpha = 0.7f),
                    radius = dotRadius,
                    center = Offset(x, y)
                )
            }
        }
        
        // Main icon
        Icon(
            imageVector = type.icon,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .offset(y = (-floatOffset).dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Simple empty state with just icon and text
 */
@Composable
fun EmptyStateSimple(
    icon: ImageVector,
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .alpha(0.5f),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Error state component
 */
@Composable
fun ErrorState(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Try Again")
            }
        }
    }
}

// Private easing function
private val EaseInOutQuad = CubicBezierEasing(0.455f, 0.03f, 0.515f, 0.955f)
