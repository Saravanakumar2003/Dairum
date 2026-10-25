package com.dairum.presentation.screens.editor

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    FONT_EXTRA_LARGE,
    // Chip-style spans: rendered with a rounded background drawn separately (see drawChipBackgrounds)
    TIMESTAMP,
    MENTION_PERSON,
    MENTION_LOCATION,
    MENTION_TAG,
    MENTION_EMOTION,
    INLINE_IMAGE
}

/** Chip-style spans get a custom-drawn rounded background instead of a plain SpanStyle background. */
val CHIP_SPAN_STYLES = setOf(
    SpanStyle.TIMESTAMP,
    SpanStyle.MENTION_PERSON,
    SpanStyle.MENTION_LOCATION,
    SpanStyle.MENTION_TAG,
    SpanStyle.MENTION_EMOTION,
    SpanStyle.INLINE_IMAGE
)

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
                    SpanStyle.TIMESTAMP -> androidx.compose.ui.text.SpanStyle(
                        color = Color(0xFF00695C),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                    SpanStyle.MENTION_PERSON -> androidx.compose.ui.text.SpanStyle(
                        color = Color(0xFF1565C0),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                    SpanStyle.MENTION_LOCATION -> androidx.compose.ui.text.SpanStyle(
                        color = Color(0xFF2E7D32),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                    SpanStyle.MENTION_TAG -> androidx.compose.ui.text.SpanStyle(
                        color = Color(0xFF6A1B9A),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                    SpanStyle.MENTION_EMOTION -> androidx.compose.ui.text.SpanStyle(
                        color = Color(0xFFAD1457),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                    SpanStyle.INLINE_IMAGE -> androidx.compose.ui.text.SpanStyle(
                        color = Color(0xFFE65100),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                }
                addStyle(style, start, end)
            }
        }
    }
}

private val TIMESTAMP_TOKEN_REGEX = Regex("""\[\d{1,2}:\d{2}\s?[AaPp][Mm]\]""")

/**
 * Adds TIMESTAMP chip spans for any legacy bracketed time tokens (e.g. "[6:00 AM]") in [text] that
 * aren't already covered by an explicit span. Lets old entries (saved before chip styling existed)
 * still render with the rounded timestamp chip.
 */
fun TextFormatting.withDetectedTimestamps(text: String): TextFormatting {
    if (text.isEmpty()) return this
    val alreadyCovered = spans.filter { it.style in CHIP_SPAN_STYLES }
    val detected = mutableListOf<TextSpan>()
    TIMESTAMP_TOKEN_REGEX.findAll(text).forEach { match ->
        val start = match.range.first
        val end = match.range.last + 1
        if (alreadyCovered.none { it.start == start && it.end == end }) {
            detected.add(TextSpan(start, end, SpanStyle.TIMESTAMP))
        }
    }
    return if (detected.isEmpty()) this else copy(spans = spans + detected)
}

/** A resolved rounded-chip background range ready to be drawn. */
data class ChipBackground(val start: Int, val end: Int, val color: Color)

/** Returns the rounded-background color for chip-style spans, or null for regular formatting spans. */
fun chipBackgroundColorFor(style: SpanStyle, isDark: Boolean): Color? = when (style) {
    SpanStyle.TIMESTAMP -> if (isDark) Color(0xFF0D3B36) else Color(0xFFB2DFDB)
    SpanStyle.MENTION_PERSON -> if (isDark) Color(0xFF163A5E) else Color(0xFFDCEAFF)
    SpanStyle.MENTION_LOCATION -> if (isDark) Color(0xFF1B3D1E) else Color(0xFFDCF5DE)
    SpanStyle.MENTION_TAG -> if (isDark) Color(0xFF3D1F4D) else Color(0xFFF0DEFF)
    SpanStyle.MENTION_EMOTION -> if (isDark) Color(0xFF4D1F33) else Color(0xFFFFDCE9)
    SpanStyle.INLINE_IMAGE -> if (isDark) Color(0xFF4A2E10) else Color(0xFFFFE7C2)
    else -> null
}

fun TextFormatting.chipBackgrounds(isDark: Boolean): List<ChipBackground> {
    if (spans.isEmpty()) return emptyList()
    return spans.mapNotNull { span ->
        chipBackgroundColorFor(span.style, isDark)?.let { color -> ChipBackground(span.start, span.end, color) }
    }
}

/** Extracts the spans overlapping [start, end) and rebases their positions to start at 0. Used when
 * a single stored text/formatting pair is split into multiple rendered segments (e.g. around inline images). */
fun TextFormatting.sliceFor(start: Int, end: Int): TextFormatting {
    if (start >= end) return TextFormatting()
    val sliced = spans.mapNotNull { span ->
        val s = span.start.coerceIn(start, end)
        val e = span.end.coerceIn(start, end)
        if (s < e) TextSpan(s - start, e - start, span.style) else null
    }
    return TextFormatting(sliced)
}

/**
 * Draws rounded-rect backgrounds behind [chips] ranges using [textLayoutResult], so chip spans
 * (timestamps, mentions, inline image markers) look like real pill/chip UI instead of plain text
 * or a sharp-cornered SpanStyle background. Must be applied to the same Text whose layout was used.
 */
fun Modifier.drawChipBackgrounds(
    textLayoutResult: androidx.compose.ui.text.TextLayoutResult?,
    chips: List<ChipBackground>,
    cornerRadius: androidx.compose.ui.unit.Dp = 6.dp
): Modifier {
    if (textLayoutResult == null || chips.isEmpty()) return this
    return this.then(
        Modifier.drawBehind {
            val radiusPx = cornerRadius.toPx()
            val textLength = textLayoutResult.layoutInput.text.length
            chips.forEach { chip ->
                val start = chip.start.coerceIn(0, textLength)
                val end = chip.end.coerceIn(start, textLength)
                if (start >= end) return@forEach
                try {
                    val startBox = textLayoutResult.getBoundingBox(start)
                    val endBox = textLayoutResult.getBoundingBox(end - 1)
                    if (kotlin.math.abs(startBox.top - endBox.top) < 1f) {
                        drawRoundRect(
                            color = chip.color,
                            topLeft = androidx.compose.ui.geometry.Offset(startBox.left, startBox.top),
                            size = androidx.compose.ui.geometry.Size(endBox.right - startBox.left, startBox.height),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(radiusPx, radiusPx)
                        )
                    } else {
                        val startLine = textLayoutResult.getLineForOffset(start)
                        val endLine = textLayoutResult.getLineForOffset(end - 1)
                        for (line in startLine..endLine) {
                            val left = if (line == startLine) startBox.left else textLayoutResult.getLineLeft(line)
                            val right = if (line == endLine) endBox.right else textLayoutResult.getLineRight(line)
                            val top = textLayoutResult.getLineTop(line)
                            val bottom = textLayoutResult.getLineBottom(line)
                            drawRoundRect(
                                color = chip.color,
                                topLeft = androidx.compose.ui.geometry.Offset(left, top),
                                size = androidx.compose.ui.geometry.Size(right - left, bottom - top),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(radiusPx, radiusPx)
                            )
                        }
                    }
                } catch (e: Exception) {
                    // Stale layout mid-recomposition; skip this chip for this frame.
                }
            }
        }
    )
}
