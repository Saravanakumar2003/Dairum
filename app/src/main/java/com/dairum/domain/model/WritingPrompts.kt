package com.dairum.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

/**
 * Writing prompts to help users overcome writer's block
 * and encourage regular journaling.
 */

/**
 * A writing prompt with category and metadata
 */
data class WritingPrompt(
    val id: Int,
    val text: String,
    val category: PromptCategory,
    val relatedMood: MoodCategory? = null,
    val isSeasonalFor: Season? = null,
    val isSpecialFor: SpecialOccasion? = null
)

/**
 * Categories of writing prompts
 */
enum class PromptCategory {
    REFLECTION,      // Looking back at experiences
    GRATITUDE,       // What you're thankful for
    GOALS,           // Plans and aspirations
    CREATIVITY,      // Creative and imaginative prompts
    RELATIONSHIPS,   // People in your life
    SELF_DISCOVERY,  // Understanding yourself
    MINDFULNESS,     // Present moment awareness
    GROWTH,          // Personal development
    MEMORIES,        // Past experiences
    FUTURE,          // Looking ahead
    DAILY_LIFE,      // Everyday moments
    EMOTIONS         // Exploring feelings
}

/**
 * Seasons for seasonal prompts
 */
enum class Season {
    SPRING, SUMMER, FALL, WINTER
}

/**
 * Special occasions for themed prompts
 */
enum class SpecialOccasion {
    NEW_YEAR,
    VALENTINES,
    MOTHERS_DAY,
    FATHERS_DAY,
    BIRTHDAY,
    THANKSGIVING,
    CHRISTMAS,
    END_OF_YEAR,
    MONDAY_MOTIVATION,
    FRIDAY_REFLECTION,
    WEEKEND
}

/**
 * Repository of writing prompts
 */
object WritingPrompts {
    
    private val allPrompts = listOf(
        // Reflection prompts
        WritingPrompt(1, "What was the highlight of your day and why did it stand out?", PromptCategory.REFLECTION),
        WritingPrompt(2, "Describe a moment today when you felt truly present.", PromptCategory.REFLECTION),
        WritingPrompt(3, "What did you learn about yourself today?", PromptCategory.REFLECTION),
        WritingPrompt(4, "If you could relive one moment from today, which would it be?", PromptCategory.REFLECTION),
        WritingPrompt(5, "What challenged you today and how did you handle it?", PromptCategory.REFLECTION),
        
        // Gratitude prompts
        WritingPrompt(10, "List three things you're grateful for right now.", PromptCategory.GRATITUDE),
        WritingPrompt(11, "Who made a positive impact on your day and how?", PromptCategory.GRATITUDE),
        WritingPrompt(12, "What small pleasure brought you joy today?", PromptCategory.GRATITUDE),
        WritingPrompt(13, "What ability or skill are you thankful to have?", PromptCategory.GRATITUDE),
        WritingPrompt(14, "Describe a meal, view, or experience you appreciated recently.", PromptCategory.GRATITUDE),
        
        // Goals prompts
        WritingPrompt(20, "What's one thing you want to accomplish this week?", PromptCategory.GOALS),
        WritingPrompt(21, "What small step can you take tomorrow toward a big goal?", PromptCategory.GOALS),
        WritingPrompt(22, "Describe your ideal day one year from now.", PromptCategory.GOALS),
        WritingPrompt(23, "What habit would you like to build or break?", PromptCategory.GOALS),
        WritingPrompt(24, "What would you do if you knew you couldn't fail?", PromptCategory.GOALS),
        
        // Creativity prompts
        WritingPrompt(30, "If you could have any superpower for a day, what would you choose and why?", PromptCategory.CREATIVITY),
        WritingPrompt(31, "Write a letter to your future self.", PromptCategory.CREATIVITY),
        WritingPrompt(32, "If your mood today were a color, what would it be? Describe it.", PromptCategory.CREATIVITY),
        WritingPrompt(33, "Create a soundtrack for your week - what songs would be on it?", PromptCategory.CREATIVITY),
        WritingPrompt(34, "If you could have dinner with anyone, living or dead, who would it be?", PromptCategory.CREATIVITY),
        
        // Relationships prompts
        WritingPrompt(40, "Who do you want to connect with more deeply and why?", PromptCategory.RELATIONSHIPS),
        WritingPrompt(41, "Describe someone who inspires you.", PromptCategory.RELATIONSHIPS),
        WritingPrompt(42, "What conversation do you need to have but have been avoiding?", PromptCategory.RELATIONSHIPS),
        WritingPrompt(43, "How did you show love or appreciation to someone today?", PromptCategory.RELATIONSHIPS),
        WritingPrompt(44, "Write about a friendship that has shaped who you are.", PromptCategory.RELATIONSHIPS),
        
        // Self-discovery prompts
        WritingPrompt(50, "What makes you feel most alive?", PromptCategory.SELF_DISCOVERY),
        WritingPrompt(51, "Describe your perfect weekend.", PromptCategory.SELF_DISCOVERY),
        WritingPrompt(52, "What are your core values? How do you live by them?", PromptCategory.SELF_DISCOVERY),
        WritingPrompt(53, "What frightens you and why?", PromptCategory.SELF_DISCOVERY),
        WritingPrompt(54, "What would you tell your younger self?", PromptCategory.SELF_DISCOVERY),
        
        // Mindfulness prompts
        WritingPrompt(60, "Describe five things you can see, hear, and feel right now.", PromptCategory.MINDFULNESS),
        WritingPrompt(61, "What does peace feel like to you?", PromptCategory.MINDFULNESS),
        WritingPrompt(62, "Write about a moment of stillness today.", PromptCategory.MINDFULNESS),
        WritingPrompt(63, "How does your body feel right now? Scan from head to toe.", PromptCategory.MINDFULNESS),
        WritingPrompt(64, "What sounds are around you? How do they make you feel?", PromptCategory.MINDFULNESS),
        
        // Growth prompts
        WritingPrompt(70, "What mistake taught you an important lesson?", PromptCategory.GROWTH),
        WritingPrompt(71, "How have you changed in the past year?", PromptCategory.GROWTH),
        WritingPrompt(72, "What area of your life needs the most attention right now?", PromptCategory.GROWTH),
        WritingPrompt(73, "What's something you've been putting off? Why?", PromptCategory.GROWTH),
        WritingPrompt(74, "Describe a time you stepped outside your comfort zone.", PromptCategory.GROWTH),
        
        // Memory prompts
        WritingPrompt(80, "Describe your happiest childhood memory.", PromptCategory.MEMORIES),
        WritingPrompt(81, "What's a tradition from your past you'd like to revive?", PromptCategory.MEMORIES),
        WritingPrompt(82, "Write about a place that holds special meaning to you.", PromptCategory.MEMORIES),
        WritingPrompt(83, "Describe a moment that changed the course of your life.", PromptCategory.MEMORIES),
        WritingPrompt(84, "What smell instantly transports you to a memory?", PromptCategory.MEMORIES),
        
        // Future prompts
        WritingPrompt(90, "Where do you see yourself in five years?", PromptCategory.FUTURE),
        WritingPrompt(91, "What legacy do you want to leave?", PromptCategory.FUTURE),
        WritingPrompt(92, "What exciting possibility is on your horizon?", PromptCategory.FUTURE),
        WritingPrompt(93, "If money were no object, how would you spend your time?", PromptCategory.FUTURE),
        WritingPrompt(94, "What adventure do you want to have in your lifetime?", PromptCategory.FUTURE),
        
        // Daily life prompts
        WritingPrompt(100, "Describe your morning routine. What works? What doesn't?", PromptCategory.DAILY_LIFE),
        WritingPrompt(101, "What did you eat today? How did it make you feel?", PromptCategory.DAILY_LIFE),
        WritingPrompt(102, "What's on your mind right now?", PromptCategory.DAILY_LIFE),
        WritingPrompt(103, "Describe the weather today and how it affected your mood.", PromptCategory.DAILY_LIFE),
        WritingPrompt(104, "What's something ordinary that you found beautiful today?", PromptCategory.DAILY_LIFE),
        
        // Emotion prompts
        WritingPrompt(110, "What emotion dominated your day? Explore it.", PromptCategory.EMOTIONS, relatedMood = MoodCategory.MIXED),
        WritingPrompt(111, "When did you last feel truly happy? Describe that moment.", PromptCategory.EMOTIONS, relatedMood = MoodCategory.POSITIVE),
        WritingPrompt(112, "What worries are you carrying? Write them out.", PromptCategory.EMOTIONS, relatedMood = MoodCategory.NEGATIVE),
        WritingPrompt(113, "Describe a time you felt proud of yourself.", PromptCategory.EMOTIONS, relatedMood = MoodCategory.POSITIVE),
        WritingPrompt(114, "What brings you comfort when you're feeling down?", PromptCategory.EMOTIONS, relatedMood = MoodCategory.NEGATIVE),
        
        // Seasonal prompts
        WritingPrompt(120, "What new beginnings are you hoping for this spring?", PromptCategory.REFLECTION, isSeasonalFor = Season.SPRING),
        WritingPrompt(121, "How do you want to make the most of summer?", PromptCategory.GOALS, isSeasonalFor = Season.SUMMER),
        WritingPrompt(122, "What are you letting go of this fall?", PromptCategory.GROWTH, isSeasonalFor = Season.FALL),
        WritingPrompt(123, "What cozy moments are you looking forward to this winter?", PromptCategory.GRATITUDE, isSeasonalFor = Season.WINTER),
        
        // Special occasion prompts
        WritingPrompt(130, "What intentions are you setting for the new year?", PromptCategory.GOALS, isSpecialFor = SpecialOccasion.NEW_YEAR),
        WritingPrompt(131, "Reflect on this year. What are you most proud of?", PromptCategory.REFLECTION, isSpecialFor = SpecialOccasion.END_OF_YEAR),
        WritingPrompt(132, "What gives you motivation to start the week strong?", PromptCategory.GOALS, isSpecialFor = SpecialOccasion.MONDAY_MOTIVATION),
        WritingPrompt(133, "How would you describe your week? What went well?", PromptCategory.REFLECTION, isSpecialFor = SpecialOccasion.FRIDAY_REFLECTION),
        WritingPrompt(134, "How do you plan to recharge this weekend?", PromptCategory.MINDFULNESS, isSpecialFor = SpecialOccasion.WEEKEND)
    )
    
    /**
     * Get the prompt of the day based on date
     */
    fun getPromptOfTheDay(date: LocalDate = LocalDate.now()): WritingPrompt {
        // Use day of year to get a consistent prompt for each day
        val index = date.dayOfYear % allPrompts.size
        return allPrompts[index]
    }
    
    /**
     * Get a random prompt
     */
    fun getRandomPrompt(): WritingPrompt {
        return allPrompts.random()
    }
    
    /**
     * Get prompt by category
     */
    fun getPromptsByCategory(category: PromptCategory): List<WritingPrompt> {
        return allPrompts.filter { it.category == category }
    }
    
    /**
     * Get mood-based prompts
     */
    fun getPromptsForMood(moodCategory: MoodCategory): List<WritingPrompt> {
        return allPrompts.filter { it.relatedMood == moodCategory }
    }
    
    /**
     * Get seasonal prompt
     */
    fun getSeasonalPrompt(date: LocalDate = LocalDate.now()): WritingPrompt? {
        val season = when (date.month) {
            Month.MARCH, Month.APRIL, Month.MAY -> Season.SPRING
            Month.JUNE, Month.JULY, Month.AUGUST -> Season.SUMMER
            Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER -> Season.FALL
            else -> Season.WINTER
        }
        return allPrompts.filter { it.isSeasonalFor == season }.randomOrNull()
    }
    
    /**
     * Get special occasion prompt
     */
    fun getSpecialOccasionPrompt(date: LocalDate = LocalDate.now()): WritingPrompt? {
        val occasion = when {
            date.dayOfMonth == 1 && date.month == Month.JANUARY -> SpecialOccasion.NEW_YEAR
            date.dayOfMonth == 31 && date.month == Month.DECEMBER -> SpecialOccasion.END_OF_YEAR
            date.dayOfWeek == DayOfWeek.MONDAY -> SpecialOccasion.MONDAY_MOTIVATION
            date.dayOfWeek == DayOfWeek.FRIDAY -> SpecialOccasion.FRIDAY_REFLECTION
            date.dayOfWeek in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY) -> SpecialOccasion.WEEKEND
            else -> null
        }
        
        return occasion?.let { occ ->
            allPrompts.filter { it.isSpecialFor == occ }.randomOrNull()
        }
    }
    
    /**
     * Get all categories
     */
    fun getAllCategories(): List<PromptCategory> = PromptCategory.entries
    
    /**
     * Get prompt count
     */
    fun getPromptCount(): Int = allPrompts.size
}
