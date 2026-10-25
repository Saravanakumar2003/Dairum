package com.dairum.presentation.screens.memories

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.text.StaticLayout
import android.text.TextPaint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.*
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.MemoryRepository
import com.dairum.domain.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class MemoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val memoryRepository: MemoryRepository,
    private val journalRepository: JournalRepository,
    private val mediaRepository: MediaRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val memoryId: Long = savedStateHandle.get<Long>("memoryId") ?: 0L

    private val _uiState = MutableStateFlow(MemoryDetailUiState())
    val uiState: StateFlow<MemoryDetailUiState> = _uiState.asStateFlow()

    init {
        loadMemory()
    }

    private fun loadMemory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            memoryRepository.getMemoryByIdFlow(memoryId).collect { memory ->
                if (memory != null) {
                    // Load entries with full details
                    val entries = memory.entries
                    
                    // Calculate insights
                    val insights = calculateInsights(memory, entries)
                    
                    // Group entries by date
                    val entriesByDate = entries.groupBy { entry ->
                        entry.createdAt.toLocalDate()
                    }.toSortedMap(reverseOrder())
                    
                    // Get all photos from entries
                    val allPhotos = entries.flatMap { it.media.filter { m -> m.type == MediaType.PHOTO } }
                    
                    // Get unique locations
                    val locations = entries.mapNotNull { it.location }.distinctBy { 
                        "${it.latitude},${it.longitude}" 
                    }
                    
                    // Get people mentioned
                    val people = entries.flatMap { it.people }.distinctBy { it.id }
                    
                    // Get all tags used
                    val allTags = entries.flatMap { it.tags }.distinctBy { it.id }
                    
                    // Mood journey
                    val moodJourney = entries
                        .filter { it.mood != null }
                        .sortedBy { it.createdAt }
                        .map { MoodPoint(it.createdAt, it.mood!!.level) }
                    
                    _uiState.update {
                        it.copy(
                            memory = memory,
                            entries = entries,
                            entriesByDate = entriesByDate,
                            photos = allPhotos,
                            locations = locations,
                            people = people,
                            tags = allTags,
                            moodJourney = moodJourney,
                            insights = insights,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Memory not found") }
                }
            }
        }
    }

    private fun calculateInsights(memory: Memory, entries: List<JournalEntry>): MemoryInsights {
        val totalDays = ChronoUnit.DAYS.between(memory.startDate, memory.endDate) + 1
        
        // Writing frequency
        val writingDays = entries.map { it.createdAt.toLocalDate() }.distinct().size
        val writingFrequency = if (totalDays > 0) (writingDays.toFloat() / totalDays * 100) else 0f
        
        // Average entry length
        val avgWordCount = if (entries.isNotEmpty()) entries.sumOf { it.wordCount } / entries.size else 0
        
        // Most active hour
        val hourCounts = entries.groupBy { it.createdAt.hour }.mapValues { it.value.size }
        val mostActiveHour = hourCounts.maxByOrNull { it.value }?.key
        
        // Mood stats - normalize extended mood IDs to 1-5 scale
        val moods = entries.mapNotNull { it.mood?.level }
        val normalizedMoods = moods.map { MoodOption.normalizedScore(it) }
        val avgMood = if (normalizedMoods.isNotEmpty()) normalizedMoods.average().toFloat() else null
        val moodVariability = if (normalizedMoods.size > 1) {
            val mean = normalizedMoods.average()
            val variance = normalizedMoods.map { (it - mean) * (it - mean) }.average()
            kotlin.math.sqrt(variance).toFloat()
        } else null
        
        // Best day
        val bestMoodEntry = entries.maxByOrNull { it.mood?.level ?: 0 }
        
        // Most productive day
        val entriesByDay = entries.groupBy { it.createdAt.dayOfWeek.name }
        val mostProductiveDay = entriesByDay.maxByOrNull { it.value.size }?.key
        
        // Photo stats
        val photoCount = entries.sumOf { it.media.count { m -> m.type == MediaType.PHOTO } }
        val entriesWithPhotos = entries.count { it.media.any { m -> m.type == MediaType.PHOTO } }
        
        // Longest entry
        val longestEntry = entries.maxByOrNull { it.wordCount }
        
        // Streak info
        val sortedDates = entries.map { it.createdAt.toLocalDate() }.distinct().sorted()
        var longestStreak = 1
        var currentStreak = 1
        for (i in 1 until sortedDates.size) {
            if (ChronoUnit.DAYS.between(sortedDates[i-1], sortedDates[i]) == 1L) {
                currentStreak++
                if (currentStreak > longestStreak) longestStreak = currentStreak
            } else {
                currentStreak = 1
            }
        }
        
        return MemoryInsights(
            totalDays = totalDays.toInt(),
            writingDays = writingDays,
            writingFrequency = writingFrequency,
            avgWordCount = avgWordCount,
            mostActiveHour = mostActiveHour,
            avgMood = avgMood,
            moodVariability = moodVariability,
            bestMoodEntry = bestMoodEntry,
            mostProductiveDay = mostProductiveDay,
            photoCount = photoCount,
            entriesWithPhotos = entriesWithPhotos,
            longestEntry = longestEntry,
            longestStreak = longestStreak,
            totalWords = entries.sumOf { it.wordCount },
            uniqueLocations = entries.mapNotNull { it.location }.distinctBy { 
                "${it.latitude},${it.longitude}" 
            }.size
        )
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _uiState.value.memory?.let { memory ->
                memoryRepository.toggleFavorite(memory.id, !memory.isFavorite)
            }
        }
    }

    fun updateMemory(title: String, description: String?) {
        viewModelScope.launch {
            _uiState.value.memory?.let { memory ->
                val updated = memory.copy(title = title, description = description)
                memoryRepository.updateMemory(updated)
            }
        }
    }

    fun deleteMemory() {
        viewModelScope.launch {
            memoryRepository.deleteMemory(memoryId)
        }
    }

    fun removeEntry(entryId: Long) {
        viewModelScope.launch {
            memoryRepository.removeEntryFromMemory(memoryId, entryId)
        }
    }

    fun addMilestone(title: String, description: String?, date: LocalDateTime) {
        viewModelScope.launch {
            memoryRepository.addMilestone(
                MemoryMilestone(
                    memoryId = memoryId,
                    title = title,
                    description = description,
                    milestoneDate = date
                )
            )
        }
    }

    fun deleteMilestone(milestoneId: Long) {
        viewModelScope.launch {
            memoryRepository.deleteMilestone(milestoneId)
        }
    }

    fun reorderMilestone(milestoneId: Long, direction: Int) {
        viewModelScope.launch {
            val milestones = _uiState.value.memory?.milestones?.sortedBy { it.sortOrder } ?: return@launch
            val index = milestones.indexOfFirst { it.id == milestoneId }
            if (index < 0) return@launch
            val newIndex = index + direction
            if (newIndex < 0 || newIndex >= milestones.size) return@launch
            
            // Swap sort orders
            val current = milestones[index]
            val target = milestones[newIndex]
            memoryRepository.updateMilestone(current.copy(sortOrder = target.sortOrder))
            memoryRepository.updateMilestone(target.copy(sortOrder = current.sortOrder))
        }
    }
    
    /**
     * Load all entries that can be added to this memory (no date restriction)
     */
    fun loadAvailableEntries() {
        viewModelScope.launch {
            // Get ALL entries that aren't already in the memory
            journalRepository.getAllEntries().collect { allEntries ->
                val existingEntryIds = _uiState.value.entries.map { it.id }.toSet()
                val availableEntries = allEntries
                    .filter { it.id !in existingEntryIds }
                    .sortedByDescending { it.createdAt }
                
                _uiState.update { it.copy(availableEntries = availableEntries) }
            }
        }
    }
    
    /**
     * Add entries to this memory
     */
    fun addEntries(entryIds: List<Long>) {
        viewModelScope.launch {
            memoryRepository.addEntriesToMemory(memoryId, entryIds)
            // Reload memory to get updated entries
            loadMemory()
        }
    }

    fun generateHighlights() {
        viewModelScope.launch {
            val highlights = memoryRepository.generateMemoryHighlights(memoryId)
            _uiState.value.memory?.let { memory ->
                memoryRepository.updateMemory(memory.copy(highlights = highlights))
            }
        }
    }

    fun setActiveTab(tab: MemoryTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun exportMemoryToPdf() {
        viewModelScope.launch {
            val state = _uiState.value
            val memory = state.memory ?: return@launch
            _uiState.update { it.copy(isExporting = true) }
            
            val result = withContext(Dispatchers.IO) {
                try {
                    generateMemoryPdf(memory, state.entries, state.insights, state.people, state.photos, state.moodJourney)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            
            _uiState.update {
                it.copy(
                    isExporting = false,
                    exportResult = result.fold(
                        onSuccess = { "Memory PDF exported successfully" },
                        onFailure = { e -> "Export failed: ${e.message}" }
                    ),
                    exportFilePath = result.getOrNull()
                )
            }
        }
    }

    fun clearExportResult() {
        _uiState.update { it.copy(exportResult = null, exportFilePath = null) }
    }

    private fun generateMemoryPdf(
        memory: Memory,
        entries: List<JournalEntry>,
        insights: MemoryInsights?,
        people: List<Person>,
        photos: List<Media>,
        moodJourney: List<MoodPoint>
    ): Result<String> {
        val pageWidth = 595
        val pageHeight = 842
        val margin = 40f
        val contentWidth = pageWidth - (margin * 2)
        
        val document = PdfDocument()
        var pageNumber = 1
        var currentY = margin
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas
        
        val titlePaint = TextPaint().apply { textSize = 28f; isFakeBoldText = true; color = android.graphics.Color.parseColor("#1a1a1a") }
        val subtitlePaint = TextPaint().apply { textSize = 14f; color = android.graphics.Color.parseColor("#666666") }
        val headingPaint = TextPaint().apply { textSize = 18f; isFakeBoldText = true; color = android.graphics.Color.parseColor("#333333") }
        val subheadPaint = TextPaint().apply { textSize = 14f; isFakeBoldText = true; color = android.graphics.Color.parseColor("#444444") }
        val bodyPaint = TextPaint().apply { textSize = 11f; color = android.graphics.Color.parseColor("#333333") }
        val smallPaint = TextPaint().apply { textSize = 9f; color = android.graphics.Color.parseColor("#888888") }
        val emojiPaint = TextPaint().apply { textSize = 20f }
        val linePaint = android.graphics.Paint().apply { color = android.graphics.Color.parseColor("#e0e0e0"); strokeWidth = 1f }
        val accentPaint = android.graphics.Paint().apply { color = android.graphics.Color.parseColor("#6750A4") }
        
        fun startNewPage(): android.graphics.Canvas {
            document.finishPage(page)
            pageNumber++
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            page = document.startPage(pageInfo)
            currentY = margin
            return page.canvas
        }
        
        fun checkSpace(needed: Float): android.graphics.Canvas {
            if (currentY + needed > pageHeight - margin) return startNewPage()
            return canvas
        }
        
        // ===== COVER PAGE =====
        currentY = 200f
        // Title
        val titleLayout = StaticLayout.Builder.obtain(memory.title, 0, memory.title.length, titlePaint, contentWidth.toInt())
            .setAlignment(android.text.Layout.Alignment.ALIGN_CENTER).build()
        canvas.save(); canvas.translate(margin, currentY); titleLayout.draw(canvas); canvas.restore()
        currentY += titleLayout.height + 16f
        
        // Date range
        val dateRange = "${memory.startDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))} — ${memory.endDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}"
        val dateLayout = StaticLayout.Builder.obtain(dateRange, 0, dateRange.length, subtitlePaint, contentWidth.toInt())
            .setAlignment(android.text.Layout.Alignment.ALIGN_CENTER).build()
        canvas.save(); canvas.translate(margin, currentY); dateLayout.draw(canvas); canvas.restore()
        currentY += dateLayout.height + 12f
        
        // Duration & count
        val statsLine = "${memory.duration} • ${memory.entryCount} entries • ${photos.size} photos"
        val statsLayout = StaticLayout.Builder.obtain(statsLine, 0, statsLine.length, subtitlePaint, contentWidth.toInt())
            .setAlignment(android.text.Layout.Alignment.ALIGN_CENTER).build()
        canvas.save(); canvas.translate(margin, currentY); statsLayout.draw(canvas); canvas.restore()
        currentY += statsLayout.height + 24f
        
        // Description
        memory.description?.let { desc ->
            val descLayout = StaticLayout.Builder.obtain(desc, 0, desc.length, bodyPaint, contentWidth.toInt())
                .setAlignment(android.text.Layout.Alignment.ALIGN_CENTER).build()
            canvas.save(); canvas.translate(margin, currentY); descLayout.draw(canvas); canvas.restore()
            currentY += descLayout.height + 16f
        }
        
        // ===== OVERVIEW PAGE =====
        canvas = startNewPage()
        canvas.drawText("📊 Memory Overview", margin, currentY + 18f, headingPaint)
        currentY += 36f
        canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
        currentY += 16f
        
        insights?.let { ins ->
            val overviewItems = listOfNotNull(
                "Total Days" to "${ins.totalDays}",
                "Writing Days" to "${ins.writingDays} (${String.format("%.0f", ins.writingFrequency)}%)",
                "Total Words" to "${ins.totalWords}",
                "Avg. Words/Entry" to "${ins.avgWordCount}",
                ins.avgMood?.let { "Average Mood" to "${getMoodLabel(it)} (${String.format("%.1f", it)}/5)" },
                "Photos" to "${ins.photoCount}",
                "Unique Locations" to "${ins.uniqueLocations}",
                if (ins.longestStreak > 1) "Longest Streak" to "${ins.longestStreak} days" else null,
                ins.mostActiveHour?.let { "Most Active Hour" to formatHour(it) },
                ins.mostProductiveDay?.let { "Most Productive Day" to it.lowercase().replaceFirstChar { c -> c.uppercase() } }
            )
            
            overviewItems.forEach { (label, value) ->
                canvas = checkSpace(20f)
                canvas.drawText(label, margin, currentY + 11f, subheadPaint)
                canvas.drawText(value, margin + 250f, currentY + 11f, bodyPaint)
                currentY += 22f
            }
        }
        
        // ===== ENTRIES =====
        canvas = startNewPage()
        canvas.drawText("📝 Journal Entries", margin, currentY + 18f, headingPaint)
        currentY += 36f
        canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
        currentY += 16f
        
        entries.sortedBy { it.createdAt }.forEach { entry ->
            canvas = checkSpace(80f)
            
            // Date
            canvas.drawText(
                entry.createdAt.format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a")),
                margin, currentY + 9f, smallPaint
            )
            currentY += 16f
            
            // Title + mood
            val entryTitle = buildString {
                entry.mood?.let { append("${it.emoji} ") }
                append(entry.title ?: "Untitled")
            }
            canvas.drawText(entryTitle, margin, currentY + 14f, subheadPaint)
            currentY += 22f
            
            // Content (trimmed)
            val content = entry.plainContent.take(300)
            if (content.isNotBlank()) {
                val contentLayout = StaticLayout.Builder.obtain(content, 0, content.length, bodyPaint, contentWidth.toInt()).build()
                canvas = checkSpace(contentLayout.height.toFloat() + 8f)
                canvas.save(); canvas.translate(margin, currentY); contentLayout.draw(canvas); canvas.restore()
                currentY += contentLayout.height + 8f
            }
            
            currentY += 12f
            canvas.drawLine(margin + 40f, currentY, pageWidth - margin - 40f, currentY, linePaint)
            currentY += 12f
        }
        
        // ===== MOOD SUMMARY =====
        if (moodJourney.isNotEmpty()) {
            canvas = startNewPage()
            canvas.drawText("😊 Mood Journey", margin, currentY + 18f, headingPaint)
            currentY += 36f
            canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
            currentY += 16f
            
            // Mood distribution by category
            val moodCounts = entries.mapNotNull { it.mood?.level }.groupBy { id ->
                MoodOption.fromId(id)?.category
            }.mapValues { it.value.size }
            
            listOf(
                MoodCategory.POSITIVE to "😊 Positive",
                MoodCategory.NEUTRAL to "😐 Neutral",
                MoodCategory.NEGATIVE to "😔 Negative",
                MoodCategory.MIXED to "🤔 Mixed"
            ).forEach { (cat, label) ->
                val count = moodCounts[cat] ?: 0
                canvas = checkSpace(20f)
                canvas.drawText(label, margin, currentY + 11f, bodyPaint)
                canvas.drawText("$count entries", margin + 200f, currentY + 11f, bodyPaint)
                currentY += 22f
            }
            
            currentY += 16f
            
            // Mood per entry list
            canvas.drawText("Mood on Each Entry:", margin, currentY + 14f, subheadPaint)
            currentY += 24f
            
            entries.sortedBy { it.createdAt }.forEach { entry ->
                entry.mood?.let { mood ->
                    canvas = checkSpace(18f)
                    val moodLine = "${mood.emoji} ${mood.label} — ${entry.title ?: "Untitled"} (${entry.createdAt.format(DateTimeFormatter.ofPattern("MMM d"))})"
                    canvas.drawText(moodLine, margin + 8f, currentY + 11f, bodyPaint)
                    currentY += 18f
                }
            }
        }
        
        // ===== PEOPLE =====
        if (people.isNotEmpty()) {
            canvas = checkSpace(60f)
            if (currentY > margin + 40f) {
                currentY += 24f
            }
            canvas.drawText("👥 People Involved", margin, currentY + 18f, headingPaint)
            currentY += 36f
            canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
            currentY += 16f
            
            people.forEach { person ->
                canvas = checkSpace(20f)
                canvas.drawText("• ${person.name}", margin + 8f, currentY + 11f, bodyPaint)
                person.relationship?.let { rel ->
                    canvas.drawText("($rel)", margin + 200f, currentY + 11f, smallPaint)
                }
                currentY += 20f
            }
        }
        
        // ===== GALLERY =====
        if (photos.isNotEmpty()) {
            canvas = startNewPage()
            canvas.drawText("📸 Gallery", margin, currentY + 18f, headingPaint)
            currentY += 36f
            canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
            currentY += 16f
            
            canvas.drawText("${photos.size} photos captured during this memory", margin, currentY + 11f, bodyPaint)
            currentY += 24f
            
            // Try to load and draw photos in a grid
            val thumbSize = 120
            val spacing = 12f
            val cols = ((contentWidth + spacing) / (thumbSize + spacing)).toInt().coerceAtLeast(1)
            
            photos.take(24).forEachIndexed { index, media ->
                val col = index % cols
                val isNewRow = col == 0 && index > 0
                
                if (isNewRow) currentY += thumbSize + spacing
                canvas = checkSpace(thumbSize.toFloat() + spacing)
                
                val x = margin + col * (thumbSize + spacing)
                try {
                    val uri = android.net.Uri.parse(media.thumbnailUri ?: media.uri)
                    val inputStream = context.contentResolver.openInputStream(uri)
                    inputStream?.use { stream ->
                        val bitmap = android.graphics.BitmapFactory.decodeStream(stream)
                        if (bitmap != null) {
                            val scaled = android.graphics.Bitmap.createScaledBitmap(bitmap, thumbSize, thumbSize, true)
                            canvas.drawBitmap(scaled, x, currentY, null)
                            scaled.recycle()
                            bitmap.recycle()
                        }
                    }
                } catch (_: Exception) {
                    // Draw placeholder
                    val rectPaint = android.graphics.Paint().apply { 
                        color = android.graphics.Color.parseColor("#f0f0f0")
                        style = android.graphics.Paint.Style.FILL
                    }
                    canvas.drawRect(x, currentY, x + thumbSize, currentY + thumbSize, rectPaint)
                    canvas.drawText("📷", x + 50f, currentY + 65f, emojiPaint)
                }
            }
            currentY += thumbSize + spacing + 16f
        }
        
        // ===== TIMELINE & MILESTONES =====
        val milestones = memory.milestones
        if (milestones.isNotEmpty()) {
            canvas = startNewPage()
            canvas.drawText("🏆 Timeline & Milestones", margin, currentY + 18f, headingPaint)
            currentY += 36f
            canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
            currentY += 16f
            
            // Start marker
            canvas.drawText("▶ Memory Started — ${memory.startDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}", margin, currentY + 11f, bodyPaint)
            currentY += 24f
            
            milestones.sortedBy { it.milestoneDate }.forEach { milestone ->
                canvas = checkSpace(50f)
                
                // Draw milestone marker
                canvas.drawCircle(margin + 8f, currentY + 8f, 6f, accentPaint)
                canvas.drawText(
                    milestone.milestoneDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    margin + 24f, currentY + 9f, smallPaint
                )
                currentY += 16f
                canvas.drawText("⭐ ${milestone.title}", margin + 24f, currentY + 11f, subheadPaint)
                currentY += 18f
                milestone.description?.let { desc ->
                    canvas.drawText(desc, margin + 24f, currentY + 11f, bodyPaint)
                    currentY += 18f
                }
                currentY += 8f
            }
            
            // End marker
            canvas.drawText("◼ Memory Ended — ${memory.endDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}", margin, currentY + 11f, bodyPaint)
            currentY += 24f
        }
        
        // Finish
        document.finishPage(page)
        
        val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now())
        val fileName = "memory_${memory.title.take(20).replace(Regex("[^a-zA-Z0-9]"), "_")}_$timestamp.pdf"
        val exportDir = File(context.filesDir, "exports").also { it.mkdirs() }
        val file = File(exportDir, fileName)
        
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        
        // Save to MediaStore for sharing
        try {
            val values = android.content.ContentValues().apply {
                put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOCUMENTS + "/Dairum")
            }
            val uri = context.contentResolver.insert(android.provider.MediaStore.Files.getContentUri("external"), values)
            uri?.let { mediaUri ->
                context.contentResolver.openOutputStream(mediaUri)?.use { outputStream ->
                    file.inputStream().use { it.copyTo(outputStream) }
                }
            }
        } catch (_: Exception) { /* Internal file still available */ }
        
        return Result.success(file.absolutePath)
    }

    private fun getMoodLabel(avgMood: Float): String {
        return when {
            avgMood < 1.5f -> "Difficult"
            avgMood < 2.5f -> "Challenging"
            avgMood < 3.5f -> "Balanced"
            avgMood < 4.5f -> "Good"
            else -> "Wonderful"
        }
    }

    private fun formatHour(hour: Int): String {
        return when {
            hour == 0 -> "12 AM"
            hour < 12 -> "$hour AM"
            hour == 12 -> "12 PM"
            else -> "${hour - 12} PM"
        }
    }
}

data class MemoryDetailUiState(
    val memory: Memory? = null,
    val entries: List<JournalEntry> = emptyList(),
    val entriesByDate: Map<java.time.LocalDate, List<JournalEntry>> = emptyMap(),
    val availableEntries: List<JournalEntry> = emptyList(),
    val photos: List<Media> = emptyList(),
    val locations: List<Location> = emptyList(),
    val people: List<Person> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val moodJourney: List<MoodPoint> = emptyList(),
    val insights: MemoryInsights? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val activeTab: MemoryTab = MemoryTab.OVERVIEW,
    val isExporting: Boolean = false,
    val exportResult: String? = null,
    val exportFilePath: String? = null
)

enum class MemoryTab {
    OVERVIEW, ENTRIES, GALLERY, INSIGHTS, TIMELINE
}

data class MoodPoint(
    val date: LocalDateTime,
    val moodLevel: Int
)

data class MemoryInsights(
    val totalDays: Int,
    val writingDays: Int,
    val writingFrequency: Float,
    val avgWordCount: Int,
    val mostActiveHour: Int?,
    val avgMood: Float?,
    val moodVariability: Float?,
    val bestMoodEntry: JournalEntry?,
    val mostProductiveDay: String?,
    val photoCount: Int,
    val entriesWithPhotos: Int,
    val longestEntry: JournalEntry?,
    val longestStreak: Int,
    val totalWords: Int,
    val uniqueLocations: Int
)
