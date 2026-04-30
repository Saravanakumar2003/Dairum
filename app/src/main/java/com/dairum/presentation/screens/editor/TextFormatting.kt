package com.dairum.presentation.screens.editor

import androidx.compose.ui.graphics.Color
import org.json.JSONArray
import org.json.JSONObject

/**
 * Represents a text formatting span with start/end positions and style.
 */
data class TextSpan(
    val start: Int,
    val end: Int,
    val style: SpanStyle
)

/**
 * Available text styling options
 */
enum class SpanStyle {
    BOLD,
    ITALIC,
    UNDERLINE,
    HIGHLIGHT_YELLOW,
    HIGHLIGHT_GREEN,
    HIGHLIGHT_BLUE,
    HIGHLIGHT_PINK,
    HIGHLIGHT_ORANGE,
    FONT_SERIF,
    FONT_MONO,
    FONT_CURSIVE,
    // Additional custom font styles
    FONT_SANS_SERIF,
    FONT_HANDWRITING,
    FONT_SMALL,
    FONT_LARGE,
    FONT_EXTRA_LARGE
}

/**
 * Manages text formatting spans for an entry.
 * Stores list of spans that overlay on plain text.
 */
data class TextFormatting(
    val spans: List<TextSpan> = emptyList()
) {
    /**
     * Add or toggle a span. If selection already has this style, remove it.
     */
    fun toggleSpan(start: Int, end: Int, style: SpanStyle): TextFormatting {
        if (start >= end) return this
        
        val newSpan = TextSpan(start, end, style)
        
        // Check if this exact span exists - if so, remove it (toggle off)
        val existing = spans.find { 
            it.start == start && it.end == end && it.style == style 
        }
        
        return if (existing != null) {
            copy(spans = spans - existing)
        } else {
            // Add new span, merging overlapping spans of same style
            val merged = mergeSpans(spans + newSpan, style)
            copy(spans = merged)
        }
    }
    
    /**
     * Get all styles active at a given position
     */
    fun getStylesAt(position: Int): Set<SpanStyle> {
        return spans.filter { position >= it.start && position < it.end }
            .map { it.style }
            .toSet()
    }
    
    /**
     * Get spans of a specific style
     */
    fun getSpansOfStyle(style: SpanStyle): List<TextSpan> {
        return spans.filter { it.style == style }
    }
    
    /**
     * Clear all formatting
     */
    fun clear(): TextFormatting = TextFormatting()
    
    /**
     * Adjust spans after text insertion/deletion
     * @param position Where the change happened
     * @param delta Positive for insertion, negative for deletion
     */
    fun adjustForTextChange(position: Int, delta: Int): TextFormatting {
        val adjustedSpans = spans.mapNotNull { span ->
            when {
                // Span is entirely before the change - keep as is
                span.end <= position -> span
                
                // Span is entirely after the change - shift both positions
                span.start >= position -> span.copy(
                    start = (span.start + delta).coerceAtLeast(0),
                    end = (span.end + delta).coerceAtLeast(span.start + delta)
                )
                
                // Change is inside the span - adjust end position
                span.start < position && span.end > position -> {
                    val newEnd = (span.end + delta).coerceAtLeast(span.start)
                    if (newEnd > span.start) span.copy(end = newEnd) else null
                }
                
                else -> span
            }
        }.filter { it.start < it.end }
        
        return copy(spans = adjustedSpans)
    }
    
    /**
     * Serialize to JSON for storage
     */
    fun toJson(): String {
        val jsonArray = JSONArray()
        spans.forEach { span ->
            val obj = JSONObject()
            obj.put("start", span.start)
            obj.put("end", span.end)
            obj.put("style", span.style.name)
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }
    
    companion object {
        /**
         * Parse from JSON
         */
        fun fromJson(jsonString: String?): TextFormatting {
            return try {
                if (jsonString.isNullOrBlank()) {
                    TextFormatting()
                } else {
                    val jsonArray = JSONArray(jsonString)
                    val spans = mutableListOf<TextSpan>()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val span = TextSpan(
                            start = obj.getInt("start"),
                            end = obj.getInt("end"),
                            style = SpanStyle.valueOf(obj.getString("style"))
                        )
                        spans.add(span)
                    }
                    TextFormatting(spans)
                }
            } catch (e: Exception) {
                TextFormatting()
            }
        }
        
        /**
         * Merge overlapping spans of the same style
         */
        private fun mergeSpans(spans: List<TextSpan>, targetStyle: SpanStyle): List<TextSpan> {
            val (targetSpans, otherSpans) = spans.partition { it.style == targetStyle }
            
            if (targetSpans.isEmpty()) return otherSpans
            
            val sorted = targetSpans.sortedBy { it.start }
            val merged = mutableListOf<TextSpan>()
            var current = sorted.first()
            
            for (i in 1 until sorted.size) {
                val next = sorted[i]
                current = if (current.end >= next.start) {
                    // Overlapping or adjacent - merge
                    current.copy(end = maxOf(current.end, next.end))
                } else {
                    // Not overlapping - add current and move to next
                    merged.add(current)
                    next
                }
            }
            merged.add(current)
            
            return otherSpans + merged
        }
    }
}

/**
 * Highlight color options for the UI
 */
data class HighlightColor(
    val style: SpanStyle,
    val name: String,
    val color: Color
)

val highlightColors = listOf(
    HighlightColor(SpanStyle.HIGHLIGHT_YELLOW, "Yellow", Color(0xFFFFEB3B)),
    HighlightColor(SpanStyle.HIGHLIGHT_GREEN, "Green", Color(0xFF8BC34A)),
    HighlightColor(SpanStyle.HIGHLIGHT_BLUE, "Blue", Color(0xFF03A9F4)),
    HighlightColor(SpanStyle.HIGHLIGHT_PINK, "Pink", Color(0xFFE91E63)),
    HighlightColor(SpanStyle.HIGHLIGHT_ORANGE, "Orange", Color(0xFFFF9800))
)

/**
 * Font options for the UI
 */
data class FontOption(
    val style: SpanStyle?,
    val name: String,
    val displayName: String
)

val fontOptions = listOf(
    FontOption(null, "default", "Default"),
    FontOption(SpanStyle.FONT_SERIF, "serif", "Serif"),
    FontOption(SpanStyle.FONT_MONO, "mono", "Mono"),
    FontOption(SpanStyle.FONT_CURSIVE, "cursive", "Cursive"),
    FontOption(SpanStyle.FONT_SANS_SERIF, "sans", "Sans Serif"),
    FontOption(SpanStyle.FONT_HANDWRITING, "hand", "Handwriting"),
    FontOption(SpanStyle.FONT_SMALL, "small", "Small"),
    FontOption(SpanStyle.FONT_LARGE, "large", "Large"),
    FontOption(SpanStyle.FONT_EXTRA_LARGE, "xlarge", "Extra Large")
)

/**
 * Build an AnnotatedString with the formatting applied
 */
fun TextFormatting.toAnnotatedString(
    text: String,
    baseStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle.Default
): androidx.compose.ui.text.AnnotatedString {
    return androidx.compose.ui.text.buildAnnotatedString {
        append(text)
        
        spans.forEach { span ->
            val start = span.start.coerceIn(0, text.length)
            val end = span.end.coerceIn(start, text.length)
            
            if (start < end) {
                val style = when (span.style) {
                    SpanStyle.BOLD -> androidx.compose.ui.text.SpanStyle(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    SpanStyle.ITALIC -> androidx.compose.ui.text.SpanStyle(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    SpanStyle.UNDERLINE -> androidx.compose.ui.text.SpanStyle(
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                    )
                    SpanStyle.HIGHLIGHT_YELLOW -> androidx.compose.ui.text.SpanStyle(
                        background = Color(0xFFFFEB3B)
                    )
                    SpanStyle.HIGHLIGHT_GREEN -> androidx.compose.ui.text.SpanStyle(
                        background = Color(0xFF8BC34A)
                    )
                    SpanStyle.HIGHLIGHT_BLUE -> androidx.compose.ui.text.SpanStyle(
                        background = Color(0xFF03A9F4)
                    )
                    SpanStyle.HIGHLIGHT_PINK -> androidx.compose.ui.text.SpanStyle(
                        background = Color(0xFFE91E63)
                    )
                    SpanStyle.HIGHLIGHT_ORANGE -> androidx.compose.ui.text.SpanStyle(
                        background = Color(0xFFFF9800)
                    )
                    SpanStyle.FONT_SERIF -> androidx.compose.ui.text.SpanStyle(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                    )
                    SpanStyle.FONT_MONO -> androidx.compose.ui.text.SpanStyle(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    SpanStyle.FONT_CURSIVE -> androidx.compose.ui.text.SpanStyle(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Cursive
                    )
                    SpanStyle.FONT_SANS_SERIF -> androidx.compose.ui.text.SpanStyle(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif
                    )
                    SpanStyle.FONT_HANDWRITING -> androidx.compose.ui.text.SpanStyle(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Cursive // Cursive as handwriting proxy
                    )
                    SpanStyle.FONT_SMALL -> androidx.compose.ui.text.SpanStyle(
                        fontSize = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp)
                    )
                    SpanStyle.FONT_LARGE -> androidx.compose.ui.text.SpanStyle(
                        fontSize = androidx.compose.ui.unit.TextUnit(20f, androidx.compose.ui.unit.TextUnitType.Sp)
                    )
                    SpanStyle.FONT_EXTRA_LARGE -> androidx.compose.ui.text.SpanStyle(
                        fontSize = androidx.compose.ui.unit.TextUnit(26f, androidx.compose.ui.unit.TextUnitType.Sp)
                    )
                }
                addStyle(style, start, end)
            }
        }
    }
}
