package com.dairum.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Represents a background color option for journal entries.
 * 
 * @param id Unique identifier for the color used in database storage
 * @param name Human-readable name for display in UI
 * @param color The actual compose Color value
 */
data class EntryBackgroundColor(
    val id: String,
    val name: String,
    val color: Color
)

/**
 * Predefined background colors for journal entries.
 * Provides both light and dark theme variants for each color.
 * 
 * Usage:
 * - Access colors via [lightColors] or [darkColors] based on the current theme
 * - Use [fromId] to retrieve a specific color by its ID
 * - The [id] property should be stored in the database to persist the user's choice
 */
object EntryBackgroundColors {
    /**
     * Light theme background colors with subtle pastel tones
     * that work well with dark text.
     */
    val lightColors = listOf(
        // Warm colors
        EntryBackgroundColor("rose", "Rose", Color(0xFFFCE4EC)),
        EntryBackgroundColor("coral", "Coral", Color(0xFFFFEBEE)),
        EntryBackgroundColor("peach", "Peach", Color(0xFFFFF3E0)),
        EntryBackgroundColor("amber", "Amber", Color(0xFFFFF8E1)),
        EntryBackgroundColor("cream", "Cream", Color(0xFFFFFDE7)),
        // Green/Nature colors
        EntryBackgroundColor("lime", "Lime", Color(0xFFF1F8E9)),
        EntryBackgroundColor("mint", "Mint", Color(0xFFE8F5E9)),
        EntryBackgroundColor("sage", "Sage", Color(0xFFE0F2F1)),
        EntryBackgroundColor("teal", "Teal", Color(0xFFE0F7FA)),
        // Blue/Cool colors
        EntryBackgroundColor("sky", "Sky", Color(0xFFE1F5FE)),
        EntryBackgroundColor("aqua", "Aqua", Color(0xFFE3F2FD)),
        EntryBackgroundColor("lavender", "Lavender", Color(0xFFEDE7F6)),
        EntryBackgroundColor("violet", "Violet", Color(0xFFF3E5F5)),
        // Neutral colors  
        EntryBackgroundColor("slate", "Slate", Color(0xFFECEFF1)),
        EntryBackgroundColor("stone", "Stone", Color(0xFFF5F5F5)),
        EntryBackgroundColor("blush", "Blush", Color(0xFFFCE4E6))
    )
    
    /**
     * Dark theme background colors with deeper tones
     * that work well with light text while maintaining
     * color identity.
     */
    val darkColors = listOf(
        // Warm colors
        EntryBackgroundColor("rose", "Rose", Color(0xFF4A1C2B)),
        EntryBackgroundColor("coral", "Coral", Color(0xFF4A1F1F)),
        EntryBackgroundColor("peach", "Peach", Color(0xFF3D2B1A)),
        EntryBackgroundColor("amber", "Amber", Color(0xFF3D2E1E)),
        EntryBackgroundColor("cream", "Cream", Color(0xFF3D3A1A)),
        // Green/Nature colors
        EntryBackgroundColor("lime", "Lime", Color(0xFF1E3A1F)),
        EntryBackgroundColor("mint", "Mint", Color(0xFF1B3A28)),
        EntryBackgroundColor("sage", "Sage", Color(0xFF1A3C3D)),
        EntryBackgroundColor("teal", "Teal", Color(0xFF163B42)),
        // Blue/Cool colors
        EntryBackgroundColor("sky", "Sky", Color(0xFF1A2F42)),
        EntryBackgroundColor("aqua", "Aqua", Color(0xFF1A2B42)),
        EntryBackgroundColor("lavender", "Lavender", Color(0xFF2A1F42)),
        EntryBackgroundColor("violet", "Violet", Color(0xFF2E1F3D)),
        // Neutral colors
        EntryBackgroundColor("slate", "Slate", Color(0xFF2B3035)),
        EntryBackgroundColor("stone", "Stone", Color(0xFF2A2A2A)),
        EntryBackgroundColor("blush", "Blush", Color(0xFF3D2A2B))
    )
    
    /**
     * Retrieves a background color by its ID.
     * 
     * @param id The color identifier (e.g., "rose", "sky")
     * @param isDark Whether to return the dark theme variant
     * @return The matching [EntryBackgroundColor] or null if not found
     */
    fun fromId(id: String, isDark: Boolean = false): EntryBackgroundColor? {
        val colors = if (isDark) darkColors else lightColors
        return colors.find { it.id == id }
    }
    
    /**
     * Returns appropriate colors based on the theme.
     * 
     * @param isDark Whether the dark theme is active
     * @return List of background colors for the current theme
     */
    fun getColorsForTheme(isDark: Boolean): List<EntryBackgroundColor> {
        return if (isDark) darkColors else lightColors
    }
}
