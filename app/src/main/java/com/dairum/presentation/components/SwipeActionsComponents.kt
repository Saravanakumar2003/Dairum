package com.dairum.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * Swipeable actions for entry cards.
 * Left swipe: Archive/Delete
 * Right swipe: Favorite/Edit
 */

/**
 * Swipe action data
 */
data class SwipeAction(
    val icon: ImageVector,
    val label: String,
    val backgroundColor: Color,
    val iconTint: Color = Color.White,
    val onAction: () -> Unit
)

/**
 * Swipeable entry card wrapper
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableEntryCard(
    onFavorite: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit,
    isFavorite: Boolean = false,
    isArchived: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    // Right swipe - Favorite
                    onFavorite()
                    false // Don't dismiss, just toggle
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    // Left swipe - Archive
                    onArchive()
                    false // Don't dismiss, just archive
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.4f }
    )
    
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            SwipeBackground(
                dismissState = dismissState,
                isFavorite = isFavorite,
                isArchived = isArchived
            )
        },
        content = { content() }
    )
}

/**
 * Background content shown during swipe
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBackground(
    dismissState: SwipeToDismissBoxState,
    isFavorite: Boolean,
    isArchived: Boolean
) {
    val direction = dismissState.dismissDirection
    
    val color by animateColorAsState(
        when (direction) {
            SwipeToDismissBoxValue.StartToEnd -> if (isFavorite) {
                Color(0xFFFF6B6B) // Unfavorite - red
            } else {
                Color(0xFFFF4081) // Favorite - pink
            }
            SwipeToDismissBoxValue.EndToStart -> if (isArchived) {
                Color(0xFF4CAF50) // Unarchive - green
            } else {
                Color(0xFFFF9800) // Archive - orange
            }
            else -> Color.Transparent
        },
        label = "swipe_color"
    )
    
    val icon = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> if (isFavorite) {
            Icons.Default.FavoriteBorder
        } else {
            Icons.Default.Favorite
        }
        SwipeToDismissBoxValue.EndToStart -> if (isArchived) {
            Icons.Default.Unarchive
        } else {
            Icons.Default.Archive
        }
        else -> Icons.Default.Circle
    }
    
    val label = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> if (isFavorite) "Remove Favorite" else "Add to Favorites"
        SwipeToDismissBoxValue.EndToStart -> if (isArchived) "Unarchive" else "Archive"
        else -> ""
    }
    
    val alignment = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }
    
    val scale by animateFloatAsState(
        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
        label = "icon_scale"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .padding(horizontal = 24.dp),
        contentAlignment = alignment
    ) {
        if (direction != SwipeToDismissBoxValue.Settled) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (direction == SwipeToDismissBoxValue.StartToEnd) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier
                            .size(28.dp)
                            .scale(scale),
                        tint = Color.White
                    )
                    Text(
                        text = label,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                } else {
                    Text(
                        text = label,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier
                            .size(28.dp)
                            .scale(scale),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Quick actions row that appears on long press
 */
@Composable
fun QuickActionsRow(
    onEdit: () -> Unit,
    onFavorite: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    isFavorite: Boolean,
    isArchived: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickActionButton(
                icon = Icons.Outlined.Edit,
                label = "Edit",
                onClick = onEdit
            )
            
            QuickActionButton(
                icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                label = if (isFavorite) "Unfavorite" else "Favorite",
                onClick = onFavorite,
                tint = if (isFavorite) Color(0xFFFF4081) else MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            QuickActionButton(
                icon = if (isArchived) Icons.Outlined.Unarchive else Icons.Outlined.Archive,
                label = if (isArchived) "Restore" else "Archive",
                onClick = onArchive
            )
            
            QuickActionButton(
                icon = Icons.Outlined.Share,
                label = "Share",
                onClick = onShare
            )
            
            QuickActionButton(
                icon = Icons.Outlined.Delete,
                label = "Delete",
                onClick = onDelete,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * Individual quick action button
 */
@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = tint
        )
    }
}

/**
 * Confirmation dialog for destructive actions
 */
@Composable
fun DeleteConfirmationDialog(
    title: String = "Delete Entry",
    message: String = "Are you sure you want to delete this entry? This action cannot be undone.",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.DeleteForever,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Archive confirmation dialog
 */
@Composable
fun ArchiveConfirmationDialog(
    isArchiving: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = if (isArchiving) Icons.Outlined.Archive else Icons.Outlined.Unarchive,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(text = if (isArchiving) "Archive Entry" else "Restore Entry")
        },
        text = {
            Text(
                text = if (isArchiving) {
                    "Archived entries won't appear in your timeline but can be restored later."
                } else {
                    "This entry will be restored to your timeline."
                }
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(if (isArchiving) "Archive" else "Restore")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
