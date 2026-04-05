package com.dairum.data.ai.prompt

import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.PromptTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Prompt templates for various AI use cases
 */
object PromptTemplates {
    
    private val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    
    /**
     * General journal question prompt - with first-person context understanding
     */
    val ASK_JOURNAL = PromptTemplate(
        name = "ask_journal",
        systemPrompt = """You are a thoughtful AI assistant helping a journal author reflect on their life.

CRITICAL RULES - FOLLOW EXACTLY:
1. YOU ARE THE AI ASSISTANT. The human is the journal author.
2. NEVER write as if you are the user. NEVER say "As a user..." or "I wrote..."
3. NEVER generate fake dialogue or scripts (no "User:", "Me:", "Human:", "Assistant:" labels)
4. NEVER roleplay both sides of a conversation
5. Keep responses SHORT (3-5 sentences max)
6. End with ONE thoughtful follow-up question

CONTEXT UNDERSTANDING:
- When user says "my weapon", "my letter", "my work" - they mean things from THEIR journals
- Example: "is my weapon bad?" = the bomb/weapon mentioned in their journal, NOT a physical weapon

RESPONSE FORMAT:
- Directly address their question using their journal content
- Be empathetic and insightful
- End with a single reflective question to deepen the conversation""",
        userPromptTemplate = """Question from the journal author: {query}

Relevant journal entries (written by the author):
{entries}

Respond in 3-5 sentences, then ask ONE thoughtful follow-up question.""",
        maxTokens = 180,
        temperature = 0.7f,
        stopTokens = listOf("User:", "Human:", "Me:", "Assistant:", "AI:", "\n\nUser", "\n\nHuman", "\n\nMe")
    )
    
    /**
     * Weekly reflection prompt
     */
    val WEEKLY_REFLECTION = PromptTemplate(
        name = "weekly_reflection",
        systemPrompt = """You are a thoughtful life coach helping the user reflect on their week through their journal entries.
Analyze the entries to identify mood patterns, key themes, accomplishments, and areas for growth.
Be supportive and constructive in your observations.""",
        userPromptTemplate = """Please provide a weekly reflection based on the following journal entries from {date_range}:

{entries}

Mood Statistics:
- Average mood: {avg_mood}
- Mood range: {mood_range}
- Dominant mood: {dominant_mood}

Please provide:
1. A brief summary of the week
2. Key themes or patterns you noticed
3. Highlights and accomplishments
4. Gentle suggestions for the coming week

Keep the reflection warm, personal, and actionable.""",
        maxTokens = 384,
        temperature = 0.7f
    )
    
    /**
     * Pattern detection prompt
     */
    val PATTERN_DETECTION = PromptTemplate(
        name = "pattern_detection",
        systemPrompt = """You are an analytical assistant that helps identify patterns in journal entries.
Look for correlations between moods, activities, people, time of day, and other factors.
Be specific with evidence and focus on actionable insights.""",
        userPromptTemplate = """Analyze the following journal entries to identify patterns:

{entries}

Please identify:
1. Mood patterns and what seems to influence them
2. Activities or habits that correlate with better/worse moods
3. People who appear frequently and their apparent impact
4. Time-based patterns (days of week, times of day)
5. Any other notable patterns

For each pattern, cite specific entries as evidence.""",
        maxTokens = 384,
        temperature = 0.5f
    )
    
    /**
     * Daily summary prompt
     */
    val DAILY_SUMMARY = PromptTemplate(
        name = "daily_summary",
        systemPrompt = """You are a personal assistant summarizing the user's day from their journal entries.
Create a concise but complete summary highlighting key events, emotions, and reflections.""",
        userPromptTemplate = """Summarize the day ({date}) based on these journal entries:

{entries}

Create a brief daily summary including:
- Main events and activities
- Overall mood and emotions
- Key thoughts or reflections
- Notable people or places mentioned

Keep it concise (2-3 paragraphs).""",
        maxTokens = 384,
        temperature = 0.7f
    )
    
    /**
     * Monthly summary prompt
     */
    val MONTHLY_SUMMARY = PromptTemplate(
        name = "monthly_summary",
        systemPrompt = """You are a reflective assistant creating a monthly overview from journal entries.
Identify major themes, milestones, emotional journey, and growth areas.""",
        userPromptTemplate = """Create a monthly summary for {month_year} based on these entries:

{entries}

Entry Statistics:
- Total entries: {entry_count}
- Total words: {word_count}
- Most common tags: {top_tags}
- Most mentioned people: {top_people}

Please provide:
1. Month overview (2-3 sentences)
2. Major themes and events
3. Emotional journey through the month
4. Personal growth or insights
5. Memorable moments

Be thoughtful and encouraging.""",
        maxTokens = 768,
        temperature = 0.7f
    )
    
    /**
     * Mood analysis prompt
     */
    val MOOD_ANALYSIS = PromptTemplate(
        name = "mood_analysis",
        systemPrompt = """You are an empathetic assistant analyzing mood patterns in journal entries.
Help the user understand their emotional patterns with compassion and actionable insights.""",
        userPromptTemplate = """Analyze the mood patterns in these journal entries:

{entries}

Mood data:
- Moods recorded: {mood_list}
- Average mood: {avg_mood}
- Best day: {best_day}
- Challenging day: {worst_day}

Please provide:
1. Overall emotional assessment
2. What seems to contribute to positive moods
3. Potential triggers for negative moods
4. Suggestions for emotional well-being
5. Encouragement and affirmations

Be compassionate and constructive.""",
        maxTokens = 512,
        temperature = 0.7f
    )
    
    /**
     * Build prompt from template with entry context
     */
    fun buildPrompt(
        template: PromptTemplate,
        entries: List<JournalEntry>,
        additionalContext: Map<String, String> = emptyMap()
    ): String {
        val entriesText = formatEntries(entries)
        
        var prompt = template.userPromptTemplate
            .replace("{entries}", entriesText)
            .replace("{query}", additionalContext["query"] ?: "")
            .replace("{date}", additionalContext["date"] ?: LocalDateTime.now().format(dateFormatter))
            .replace("{date_range}", additionalContext["date_range"] ?: "")
            .replace("{month_year}", additionalContext["month_year"] ?: "")
            .replace("{entry_count}", entries.size.toString())
            .replace("{word_count}", entries.sumOf { it.wordCount }.toString())
        
        // Add statistics if available
        additionalContext.forEach { (key, value) ->
            prompt = prompt.replace("{$key}", value)
        }
        
        // Format as chat conversation
        return buildString {
            appendLine(template.systemPrompt)
            appendLine()
            appendLine("User: $prompt")
            appendLine()
            appendLine("Assistant:")
        }
    }
    
    /**
     * Format entries for inclusion in prompts
     */
    fun formatEntries(entries: List<JournalEntry>): String {
        if (entries.isEmpty()) return "No entries available."
        
        return entries.joinToString("\n\n---\n\n") { entry ->
            buildString {
                // Date and title
                appendLine("Date: ${entry.createdAt.format(dateFormatter)} at ${entry.createdAt.format(timeFormatter)}")
                entry.title?.let { appendLine("Title: $it") }
                
                // Mood
                entry.mood?.let { appendLine("Mood: ${it.emoji} ${it.label}") }
                
                // Location
                entry.location?.placeName?.let { appendLine("Location: $it") }
                
                // Weather
                entry.weather?.let { appendLine("Weather: ${it.condition}, ${it.temperature}°C") }
                
                // Content
                appendLine()
                appendLine(entry.plainContent.take(1000)) // Limit content length
                
                // Tags
                if (entry.tags.isNotEmpty()) {
                    appendLine()
                    appendLine("Tags: ${entry.tags.joinToString(", ") { it.name }}")
                }
                
                // People
                if (entry.people.isNotEmpty()) {
                    appendLine("People: ${entry.people.joinToString(", ") { it.name }}")
                }
            }
        }
    }
    
    /**
     * Build a condensed context for limited token budget
     */
    fun buildCondensedContext(entries: List<JournalEntry>, maxEntries: Int = 5): String {
        val selected = entries.take(maxEntries)
        
        return selected.joinToString("\n") { entry ->
            val date = entry.createdAt.format(DateTimeFormatter.ofPattern("MM/dd"))
            val mood = entry.mood?.emoji ?: "📝"
            val preview = entry.plainContent.take(100).replace("\n", " ")
            "$date $mood: $preview..."
        }
    }
}
