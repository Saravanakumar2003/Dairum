package com.dairum.data.ai.usecase

import com.dairum.data.ai.llm.LocalLLM
import com.dairum.data.ai.llm.NativeLibraryNotAvailableException
import com.dairum.data.ai.prompt.PromptTemplates
import com.dairum.data.ai.retrieval.RetrievalService
import com.dairum.domain.model.*
import com.dairum.domain.repository.AIContentRepository
import com.dairum.domain.repository.EmbeddingRepository
import com.dairum.domain.repository.JournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AI Orchestrator that coordinates retrieval and generation for various use cases
 */
@Singleton
class AIOrchestrator @Inject constructor(
    private val retrievalService: RetrievalService,
    private val localLLM: LocalLLM,
    private val journalRepository: JournalRepository,
    private val aiContentRepository: AIContentRepository,
    private val embeddingRepository: EmbeddingRepository,
    private val statisticalPatternDetector: StatisticalPatternDetector
) {
    
    companion object {
        private const val TAG = "AIOrchestrator"
        private const val DEFAULT_TOP_K = 5
        private const val MAX_CONTEXT_ENTRIES = 10
    }
    
    // Helper to convert LocalDateTime to epoch millis
    private fun LocalDateTime.toEpochMillis(): Long {
        return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    
    // Helper to get entries in date range
    private suspend fun getEntriesInRange(startDate: LocalDateTime, endDate: LocalDateTime): List<JournalEntry> {
        return journalRepository.getEntriesInDateRange(
            startDate.toEpochMillis(),
            endDate.toEpochMillis()
        )
    }
    
    // Helper to get all entries
    private suspend fun getAllEntriesList(): List<JournalEntry> {
        return journalRepository.getAllEntries().first()
    }
    
    /**
     * Ask a question about the journal
     */
    suspend fun askJournal(query: String, maxResults: Int = DEFAULT_TOP_K): AIResponse {
        return withContext(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            
            try {
                // Check if LLM is loaded
                if (!localLLM.isModelLoaded()) {
                    return@withContext AIResponse(
                        content = "",
                        error = "AI model not loaded. Please download and select a model in the Models tab."
                    )
                }
                
                // Check if native LLM is available
                if (!localLLM.isNativeImplementation()) {
                    return@withContext AIResponse(
                        content = "",
                        error = "Native AI library not available. The app needs to be rebuilt with llama.cpp support."
                    )
                }
                
                // Check if embeddings exist
                val embeddingCount = embeddingRepository.getEmbeddingCount()
                if (embeddingCount == 0) {
                    return@withContext AIResponse(
                        content = "No embeddings have been generated yet. Please go to the Models tab and click 'Generate Embeddings' to enable AI search.",
                        sources = emptyList()
                    )
                }
                
                // Retrieve relevant entries
                val retrievalResult = retrievalService.searchRelevantEntries(query, maxResults)
                if (retrievalResult.isFailure) {
                    return@withContext AIResponse(
                        content = "",
                        error = "Failed to search entries: ${retrievalResult.exceptionOrNull()?.message}"
                    )
                }
                
                val relevantEntries = retrievalResult.getOrThrow()
                
                if (relevantEntries.isEmpty()) {
                    return@withContext AIResponse(
                        content = "I couldn't find any relevant entries matching your question. Try asking about something specific you've written about, or rephrase your question.",
                        sources = emptyList()
                    )
                }
                
                // Build prompt
                val prompt = PromptTemplates.buildPrompt(
                    template = PromptTemplates.ASK_JOURNAL,
                    entries = relevantEntries.map { it.entry },
                    additionalContext = mapOf("query" to query)
                )
                
                // Generate response with stop tokens to prevent role-playing
                val generationResult = localLLM.generate(
                    prompt = prompt,
                    maxTokens = PromptTemplates.ASK_JOURNAL.maxTokens,
                    temperature = PromptTemplates.ASK_JOURNAL.temperature,
                    stopTokens = PromptTemplates.ASK_JOURNAL.stopTokens
                )
                
                val elapsed = System.currentTimeMillis() - startTime
                
                if (generationResult.isFailure) {
                    val error = generationResult.exceptionOrNull()
                    val errorMessage = when (error) {
                        is NativeLibraryNotAvailableException -> "Native AI library not available"
                        else -> "Failed to generate response: ${error?.message}"
                    }
                    return@withContext AIResponse(
                        content = "",
                        error = errorMessage
                    )
                }
                
                AIResponse(
                    content = generationResult.getOrThrow().trim(),
                    sources = relevantEntries.map { it.entry },
                    confidence = relevantEntries.firstOrNull()?.score ?: 0f,
                    generationTimeMs = elapsed
                )
            } catch (e: Exception) {
                Timber.e(e, "Error in askJournal")
                AIResponse(content = "", error = e.message)
            }
        }
    }
    
    /**
     * Ask journal with streaming response
     */
    fun askJournalStream(query: String, maxResults: Int = DEFAULT_TOP_K): Flow<Pair<String, AIResponse?>> = flow {
        try {
            // Retrieve relevant entries first
            val retrievalResult = retrievalService.searchRelevantEntries(query, maxResults)
            if (retrievalResult.isFailure) {
                emit("" to AIResponse(content = "", error = "Search failed"))
                return@flow
            }
            
            val relevantEntries = retrievalResult.getOrThrow()
            
            if (relevantEntries.isEmpty()) {
                emit("No relevant entries found." to null)
                return@flow
            }
            
            // Build prompt
            val prompt = PromptTemplates.buildPrompt(
                template = PromptTemplates.ASK_JOURNAL,
                entries = relevantEntries.map { it.entry },
                additionalContext = mapOf("query" to query)
            )
            
            // Stream generation with stop tokens to prevent role-playing
            val responseBuilder = StringBuilder()
            val startTime = System.currentTimeMillis()
            
            localLLM.generateStream(
                prompt = prompt,
                maxTokens = PromptTemplates.ASK_JOURNAL.maxTokens,
                temperature = PromptTemplates.ASK_JOURNAL.temperature,
                stopTokens = PromptTemplates.ASK_JOURNAL.stopTokens
            ).collect { token ->
                responseBuilder.append(token)
                emit(token to null)
            }
            
            val elapsed = System.currentTimeMillis() - startTime
            
            // Emit final response
            emit("" to AIResponse(
                content = responseBuilder.toString(),
                sources = relevantEntries.map { it.entry },
                generationTimeMs = elapsed
            ))
        } catch (e: Exception) {
            Timber.e(e, "Error in askJournalStream")
            emit("" to AIResponse(content = "", error = e.message))
        }
    }.flowOn(Dispatchers.Default)
    
    /**
     * Generate weekly reflection
     */
    suspend fun generateWeeklyReflection(
        weekStart: LocalDateTime = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    ): Result<WeeklyReflection> = withContext(Dispatchers.Default) {
        try {
            val weekEnd = weekStart.plusDays(7)
            
            // Get entries for the week
            val entries = getEntriesInRange(weekStart, weekEnd)
            
            if (entries.isEmpty()) {
                return@withContext Result.failure(Exception("No entries found for this week"))
            }
            
            // Calculate mood statistics
            val moods = entries.mapNotNull { it.mood?.level }
            val avgMood = if (moods.isNotEmpty()) moods.average().toFloat() else 0f
            val moodRange = if (moods.isNotEmpty()) moods.min() to moods.max() else 0 to 0
            val dominantMood = entries.mapNotNull { it.mood?.label }
                .groupingBy { it }
                .eachCount()
                .maxByOrNull { it.value }
                ?.key ?: "Unknown"
            
            val moodDistribution = entries.mapNotNull { it.mood?.label }
                .groupingBy { it }
                .eachCount()
            
            // Determine trend
            val firstHalfMoods = entries.take(entries.size / 2).mapNotNull { it.mood?.level }
            val secondHalfMoods = entries.drop(entries.size / 2).mapNotNull { it.mood?.level }
            val trend = when {
                firstHalfMoods.isEmpty() || secondHalfMoods.isEmpty() -> TrendDirection.STABLE
                secondHalfMoods.average() > firstHalfMoods.average() + 0.5 -> TrendDirection.IMPROVING
                secondHalfMoods.average() < firstHalfMoods.average() - 0.5 -> TrendDirection.DECLINING
                else -> TrendDirection.STABLE
            }
            
            // Build prompt
            val dateFormatter = DateTimeFormatter.ofPattern("MMMM d")
            val dateRange = "${weekStart.format(dateFormatter)} - ${weekEnd.format(dateFormatter)}"
            
            val topTags = entries.flatMap { it.tags }.groupingBy { it.name }.eachCount()
                .entries.sortedByDescending { it.value }.take(3).map { it.key }
            val topPeople = entries.flatMap { it.people }.groupingBy { it.name }.eachCount()
                .entries.sortedByDescending { it.value }.take(3).map { it.key }
            
            // Generate reflection - use the LLM for a warmer narrative summary when available,
            // but fall back to a grounded template so reflections still work without an AI model
            val responseText = if (localLLM.isModelLoaded()) {
                val prompt = PromptTemplates.buildPrompt(
                    template = PromptTemplates.WEEKLY_REFLECTION,
                    entries = entries,
                    additionalContext = mapOf(
                        "date_range" to dateRange,
                        "avg_mood" to String.format("%.1f", avgMood),
                        "mood_range" to "${moodRange.first} - ${moodRange.second}",
                        "dominant_mood" to dominantMood
                    )
                )
                val generationResult = localLLM.generate(
                    prompt = prompt,
                    maxTokens = PromptTemplates.WEEKLY_REFLECTION.maxTokens,
                    temperature = PromptTemplates.WEEKLY_REFLECTION.temperature
                )
                generationResult.getOrNull() ?: buildFallbackWeeklySummary(
                    dateRange, entries.size, entries.sumOf { it.wordCount }, avgMood, dominantMood, trend, topTags, topPeople
                )
            } else {
                buildFallbackWeeklySummary(
                    dateRange, entries.size, entries.sumOf { it.wordCount }, avgMood, dominantMood, trend, topTags, topPeople
                )
            }
            
            // Parse response into structured reflection
            val reflection = WeeklyReflection(
                weekStartDate = weekStart,
                weekEndDate = weekEnd,
                summary = responseText,
                moodTrend = MoodTrend(
                    averageMood = avgMood,
                    moodRange = moodRange,
                    dominantMood = dominantMood,
                    moodDistribution = moodDistribution,
                    trend = trend
                ),
                keyThemes = extractKeyThemes(responseText).ifEmpty { topTags },
                highlights = extractHighlights(responseText).ifEmpty { topPeople.map { "Spent meaningful time with $it" } },
                suggestions = extractSuggestions(responseText),
                entryCount = entries.size,
                totalWordCount = entries.sumOf { it.wordCount }
            )
            
            // Save to repository
            aiContentRepository.saveReflection(reflection)
            
            Result.success(reflection)
        } catch (e: Exception) {
            Timber.e(e, "Error generating weekly reflection")
            Result.failure(e)
        }
    }
    
    /**
     * Detect patterns in journal entries.
     *
     * Statistical, data-grounded patterns (mood vs day-of-week/person/tag/location/weather) are
     * computed first and are always available even without an AI model loaded. The LLM-narrated
     * pass is only used as a fallback when there isn't enough mood data for statistics to find
     * anything, since that path's confidence/evidence are far less reliable (see parsePatterns).
     */
    suspend fun detectPatterns(
        startDate: LocalDateTime = LocalDateTime.now().minusMonths(1),
        endDate: LocalDateTime = LocalDateTime.now()
    ): Result<List<PatternResult>> = withContext(Dispatchers.Default) {
        try {
            val entries = getEntriesInRange(startDate, endDate)
            
            if (entries.size < 5) {
                return@withContext Result.failure(
                    Exception("Need at least 5 entries for pattern detection")
                )
            }
            
            val statisticalPatterns = statisticalPatternDetector.detect(entries)
            if (statisticalPatterns.isNotEmpty()) {
                statisticalPatterns.forEach { pattern -> aiContentRepository.savePattern(pattern) }
                return@withContext Result.success(statisticalPatterns)
            }
            
            // Fallback: not enough mood data for statistics - ask the LLM instead, if available
            if (!localLLM.isModelLoaded()) {
                return@withContext Result.failure(
                    Exception("Not enough mood data for patterns yet. Log moods with your entries, or load an AI model for a narrative analysis.")
                )
            }
            
            // Build prompt
            val prompt = PromptTemplates.buildPrompt(
                template = PromptTemplates.PATTERN_DETECTION,
                entries = entries.take(MAX_CONTEXT_ENTRIES)
            )
            
            // Generate analysis
            val generationResult = localLLM.generate(
                prompt = prompt,
                maxTokens = PromptTemplates.PATTERN_DETECTION.maxTokens,
                temperature = PromptTemplates.PATTERN_DETECTION.temperature
            )
            
            if (generationResult.isFailure) {
                return@withContext Result.failure(generationResult.exceptionOrNull()!!)
            }
            
            val responseText = generationResult.getOrThrow()
            
            // Parse response into patterns
            val patterns = parsePatterns(responseText, entries)
            
            // Save patterns
            patterns.forEach { pattern ->
                aiContentRepository.savePattern(pattern)
            }
            
            Result.success(patterns)
        } catch (e: Exception) {
            Timber.e(e, "Error detecting patterns")
            Result.failure(e)
        }
    }
    
    /**
     * Generate a smart summary
     */
    suspend fun generateSummary(
        type: SummaryType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Result<SmartSummary> = withContext(Dispatchers.Default) {
        try {
            val entries = getEntriesInRange(startDate, endDate)
            
            if (entries.isEmpty()) {
                return@withContext Result.failure(Exception("No entries found for this period"))
            }
            
            // Select template based on type
            val template = when (type) {
                SummaryType.DAILY -> PromptTemplates.DAILY_SUMMARY
                SummaryType.MONTHLY -> PromptTemplates.MONTHLY_SUMMARY
                else -> PromptTemplates.MONTHLY_SUMMARY
            }
            
            // Get top tags and people
            val topTags = entries.flatMap { it.tags }
                .groupingBy { it.name }
                .eachCount()
                .entries
                .sortedByDescending { it.value }
                .take(5)
                .map { it.key }
            
            val topPeople = entries.flatMap { it.people }
                .groupingBy { it.name }
                .eachCount()
                .entries
                .sortedByDescending { it.value }
                .take(5)
                .map { it.key }
            
            // Build prompt
            val dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            val prompt = PromptTemplates.buildPrompt(
                template = template,
                entries = entries.take(MAX_CONTEXT_ENTRIES),
                additionalContext = mapOf(
                    "month_year" to startDate.format(dateFormatter),
                    "top_tags" to topTags.joinToString(", "),
                    "top_people" to topPeople.joinToString(", ")
                )
            )
            
            // Generate summary
            val generationResult = localLLM.generate(
                prompt = prompt,
                maxTokens = template.maxTokens,
                temperature = template.temperature
            )
            
            if (generationResult.isFailure) {
                return@withContext Result.failure(generationResult.exceptionOrNull()!!)
            }
            
            val responseText = generationResult.getOrThrow()
            
            // Calculate mood overview
            val moods = entries.mapNotNull { it.mood }
            val moodOverview = if (moods.isNotEmpty()) {
                val avgMood = moods.mapNotNull { m -> m.level.takeIf { it in 1..5 } }.average()
                "Average mood: ${String.format("%.1f", avgMood)}/5"
            } else null
            
            val summary = SmartSummary(
                summaryType = type,
                period = DateRange(startDate, endDate),
                title = "${type.name.lowercase().replaceFirstChar { it.uppercase() }} Summary",
                summary = responseText,
                keyPoints = extractKeyPoints(responseText),
                moodOverview = moodOverview,
                topTags = topTags,
                topPeople = topPeople,
                wordCount = entries.sumOf { it.wordCount },
                entryCount = entries.size
            )
            
            // Save summary
            aiContentRepository.saveSummary(summary)
            
            Result.success(summary)
        } catch (e: Exception) {
            Timber.e(e, "Error generating summary")
            Result.failure(e)
        }
    }
    
    /**
     * Analyze mood patterns
     */
    suspend fun analyzeMood(
        startDate: LocalDateTime = LocalDateTime.now().minusMonths(1),
        endDate: LocalDateTime = LocalDateTime.now()
    ): AIResponse = withContext(Dispatchers.Default) {
        try {
            val entries = getEntriesInRange(startDate, endDate)
                .filter { it.mood != null }
            
            if (entries.isEmpty()) {
                return@withContext AIResponse(
                    content = "No entries with mood data found in this period.",
                    sources = emptyList()
                )
            }
            
            // Calculate mood statistics
            val moods = entries.mapNotNull { it.mood?.level }
            val moodList = entries.mapNotNull { it.mood?.label }.distinct().joinToString(", ")
            val avgMood = moods.average()
            
            val bestEntry = entries.maxByOrNull { it.mood?.level ?: 0 }
            val worstEntry = entries.minByOrNull { it.mood?.level ?: 5 }
            
            val dateFormatter = DateTimeFormatter.ofPattern("MMM d")
            
            // Build prompt
            val prompt = PromptTemplates.buildPrompt(
                template = PromptTemplates.MOOD_ANALYSIS,
                entries = entries.take(MAX_CONTEXT_ENTRIES),
                additionalContext = mapOf(
                    "mood_list" to moodList,
                    "avg_mood" to String.format("%.1f", avgMood),
                    "best_day" to (bestEntry?.createdAt?.format(dateFormatter) ?: "Unknown"),
                    "worst_day" to (worstEntry?.createdAt?.format(dateFormatter) ?: "Unknown")
                )
            )
            
            // Generate analysis
            val generationResult = localLLM.generate(
                prompt = prompt,
                maxTokens = PromptTemplates.MOOD_ANALYSIS.maxTokens,
                temperature = PromptTemplates.MOOD_ANALYSIS.temperature
            )
            
            if (generationResult.isFailure) {
                return@withContext AIResponse(
                    content = "",
                    error = generationResult.exceptionOrNull()?.message
                )
            }
            
            AIResponse(
                content = generationResult.getOrThrow(),
                sources = entries
            )
        } catch (e: Exception) {
            Timber.e(e, "Error analyzing mood")
            AIResponse(content = "", error = e.message)
        }
    }
    
    // Helper functions for parsing AI responses
    
    /** Grounded, template-based summary used when no AI model is loaded (or generation fails),
     * so weekly reflections aren't entirely gated behind downloading a model. */
    private fun buildFallbackWeeklySummary(
        dateRange: String,
        entryCount: Int,
        wordCount: Int,
        avgMood: Float,
        dominantMood: String,
        trend: TrendDirection,
        topTags: List<String>,
        topPeople: List<String>
    ): String {
        val trendPhrase = when (trend) {
            TrendDirection.IMPROVING -> "trending upward as the week went on"
            TrendDirection.DECLINING -> "trending lower as the week went on"
            TrendDirection.FLUCTUATING -> "fluctuating throughout the week"
            TrendDirection.STABLE -> "fairly steady throughout the week"
        }
        return buildString {
            append("This week ($dateRange) you wrote $entryCount ${if (entryCount == 1) "entry" else "entries"} ")
            append("totaling $wordCount words. ")
            append("Your average mood was ${"%.1f".format(avgMood)}/5, most often $dominantMood, $trendPhrase. ")
            if (topTags.isNotEmpty()) append("You wrote most about ${topTags.joinToString(", ")}. ")
            if (topPeople.isNotEmpty()) append("You mentioned ${topPeople.joinToString(", ")} often. ")
        }.trim()
    }
    
    private fun extractKeyThemes(text: String): List<String> {
        // Simple extraction - look for numbered items or bullet points
        val patterns = listOf(
            Regex("(?:themes?|patterns?).*?:.*?\\n((?:\\s*[-•\\d.].*\\n?)+)", RegexOption.IGNORE_CASE),
            Regex("\\d\\.\\s*(.+)")
        )
        
        return patterns.flatMap { pattern ->
            pattern.findAll(text).flatMap { match ->
                match.groupValues.drop(1).map { it.trim() }
            }.toList()
        }.take(5)
    }
    
    private fun extractHighlights(text: String): List<String> {
        val pattern = Regex("(?:highlights?|accomplishments?).*?:.*?\\n((?:\\s*[-•\\d.].*\\n?)+)", RegexOption.IGNORE_CASE)
        return pattern.find(text)?.groupValues?.getOrNull(1)
            ?.split("\n")
            ?.map { it.trim().trimStart('-', '•', '1', '2', '3', '4', '5', '.', ' ') }
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }
    
    private fun extractSuggestions(text: String): List<String> {
        val pattern = Regex("(?:suggestions?|recommendations?).*?:.*?\\n((?:\\s*[-•\\d.].*\\n?)+)", RegexOption.IGNORE_CASE)
        return pattern.find(text)?.groupValues?.getOrNull(1)
            ?.split("\n")
            ?.map { it.trim().trimStart('-', '•', '1', '2', '3', '4', '5', '.', ' ') }
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }
    
    private fun extractKeyPoints(text: String): List<String> {
        return text.split("\n")
            .map { it.trim() }
            .filter { it.isNotBlank() && (it.startsWith("-") || it.startsWith("•") || it.matches(Regex("^\\d\\..+"))) }
            .map { it.trimStart('-', '•', '1', '2', '3', '4', '5', '.', ' ') }
            .take(5)
    }
    
    private fun parsePatterns(text: String, entries: List<JournalEntry>): List<PatternResult> {
        val patterns = mutableListOf<PatternResult>()
        
        // Extract different pattern types from the response
        val sections = text.split(Regex("\\d\\.\\s"))
        
        sections.filter { it.isNotBlank() }.forEachIndexed { index, section ->
            val patternType = when {
                section.contains("mood", ignoreCase = true) -> PatternType.MOOD_CORRELATION
                section.contains("time", ignoreCase = true) || section.contains("day", ignoreCase = true) -> PatternType.TIME_PATTERN
                section.contains("activit", ignoreCase = true) || section.contains("habit", ignoreCase = true) -> PatternType.ACTIVITY_IMPACT
                section.contains("people", ignoreCase = true) || section.contains("person", ignoreCase = true) -> PatternType.PEOPLE_IMPACT
                else -> PatternType.MOOD_CORRELATION
            }
            
            patterns.add(
                PatternResult(
                    patternType = patternType,
                    description = section.take(500).trim(),
                    confidence = 0.7f,
                    evidence = emptyList(), // Would need more sophisticated parsing
                    insights = listOf(section.take(200).trim())
                )
            )
        }
        
        return patterns.take(5)
    }
}
