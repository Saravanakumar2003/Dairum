package com.dairum.data.ai.usecase

import com.dairum.domain.model.JournalEntry
import com.dairum.domain.model.Mood
import com.dairum.domain.model.MoodOption
import com.dairum.domain.model.MoodType
import com.dairum.domain.model.PatternEvidence
import com.dairum.domain.model.PatternResult
import com.dairum.domain.model.PatternType
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

/**
 * Computes patterns directly from real entry data (mood/day/tag/person/location/weather
 * correlations) instead of asking an LLM to invent both the analysis and the description from
 * scratch. Works even when no AI model is loaded, and every confidence score reflects an actual
 * sample size and effect size rather than a hardcoded guess.
 */
@Singleton
class StatisticalPatternDetector @Inject constructor() {

    companion object {
        private const val MIN_SAMPLE_SIZE = 3
        private const val MIN_MOOD_DIFFERENCE = 0.4
    }

    fun detect(entries: List<JournalEntry>): List<PatternResult> {
        val moodEntries = entries.filter { it.mood != null }
        if (moodEntries.size < 5) return emptyList()

        val overallAvg = moodEntries.map { moodScore(it.mood!!) }.average()
        val patterns = mutableListOf<PatternResult>()

        patterns += dayOfWeekPatterns(moodEntries, overallAvg)
        patterns += peopleImpactPatterns(moodEntries, overallAvg)
        patterns += tagImpactPatterns(moodEntries, overallAvg)
        patterns += locationPatterns(moodEntries, overallAvg)
        patterns += weatherPatterns(moodEntries, overallAvg)

        return patterns.sortedByDescending { it.confidence }.take(8)
    }

    private fun moodScore(mood: Mood): Double = when (mood.moodType) {
        MoodType.BASIC -> mood.level.toDouble()
        MoodType.EXTENDED -> MoodOption.normalizedScore(mood.level).toDouble()
    }

    private fun confidenceFor(sampleSize: Int, diff: Double): Float {
        val sampleFactor = (sampleSize / 20f).coerceAtMost(0.4f)
        val effectFactor = (abs(diff) / 4f).toFloat().coerceAtMost(0.25f)
        return (0.3f + sampleFactor + effectFactor).coerceIn(0.3f, 0.95f)
    }

    private fun evidenceFor(entries: List<JournalEntry>, factor: String, value: String): List<PatternEvidence> {
        return entries.take(5).map { entry ->
            PatternEvidence(
                entryId = entry.id,
                date = entry.createdAt,
                factor = factor,
                value = value,
                mood = entry.mood?.level
            )
        }
    }

    private fun dayOfWeekPatterns(entries: List<JournalEntry>, overallAvg: Double): List<PatternResult> {
        return entries.groupBy { it.createdAt.dayOfWeek }
            .filter { (_, group) -> group.size >= MIN_SAMPLE_SIZE }
            .mapNotNull { (day, group) ->
                val avg = group.map { moodScore(it.mood!!) }.average()
                val diff = avg - overallAvg
                if (abs(diff) < MIN_MOOD_DIFFERENCE) return@mapNotNull null
                val dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault())
                val direction = if (diff > 0) "better" else "lower"
                PatternResult(
                    patternType = PatternType.TIME_PATTERN,
                    description = "Your mood tends to be $direction on ${dayName}s (avg ${"%.1f".format(avg)}/5 vs ${"%.1f".format(overallAvg)}/5 overall).",
                    confidence = confidenceFor(group.size, diff),
                    evidence = evidenceFor(group, "day_of_week", dayName),
                    insights = listOf("Based on ${group.size} entries written on ${dayName}s")
                )
            }
    }

    private fun peopleImpactPatterns(entries: List<JournalEntry>, overallAvg: Double): List<PatternResult> {
        val byPerson = mutableMapOf<String, MutableList<JournalEntry>>()
        entries.forEach { entry -> entry.people.forEach { person -> byPerson.getOrPut(person.name) { mutableListOf() }.add(entry) } }
        return byPerson.filter { it.value.size >= MIN_SAMPLE_SIZE }
            .mapNotNull { (name, group) ->
                val avg = group.map { moodScore(it.mood!!) }.average()
                val diff = avg - overallAvg
                if (abs(diff) < MIN_MOOD_DIFFERENCE) return@mapNotNull null
                val direction = if (diff > 0) "happier" else "more subdued"
                PatternResult(
                    patternType = PatternType.PEOPLE_IMPACT,
                    description = "You tend to feel $direction in entries that mention $name (avg ${"%.1f".format(avg)}/5 vs ${"%.1f".format(overallAvg)}/5 overall).",
                    confidence = confidenceFor(group.size, diff),
                    evidence = evidenceFor(group, "person", name),
                    insights = listOf("Based on ${group.size} entries mentioning $name")
                )
            }
    }

    private fun tagImpactPatterns(entries: List<JournalEntry>, overallAvg: Double): List<PatternResult> {
        val byTag = mutableMapOf<String, MutableList<JournalEntry>>()
        entries.forEach { entry -> entry.tags.forEach { tag -> byTag.getOrPut(tag.name) { mutableListOf() }.add(entry) } }
        return byTag.filter { it.value.size >= MIN_SAMPLE_SIZE }
            .mapNotNull { (name, group) ->
                val avg = group.map { moodScore(it.mood!!) }.average()
                val diff = avg - overallAvg
                if (abs(diff) < MIN_MOOD_DIFFERENCE) return@mapNotNull null
                val direction = if (diff > 0) "boosts" else "tends to lower"
                PatternResult(
                    patternType = PatternType.ACTIVITY_IMPACT,
                    description = "\"$name\" $direction your mood (avg ${"%.1f".format(avg)}/5 vs ${"%.1f".format(overallAvg)}/5 overall).",
                    confidence = confidenceFor(group.size, diff),
                    evidence = evidenceFor(group, "tag", name),
                    insights = listOf("Based on ${group.size} entries tagged \"$name\"")
                )
            }
    }

    private fun locationPatterns(entries: List<JournalEntry>, overallAvg: Double): List<PatternResult> {
        val byPlace = mutableMapOf<String, MutableList<JournalEntry>>()
        entries.forEach { entry ->
            val locations = listOfNotNull(entry.location) + entry.additionalLocations
            locations.forEach loc@{ loc ->
                val name = loc.placeName ?: loc.address ?: return@loc
                byPlace.getOrPut(name) { mutableListOf() }.add(entry)
            }
        }
        return byPlace.filter { it.value.size >= MIN_SAMPLE_SIZE }
            .mapNotNull { (name, group) ->
                val avg = group.map { moodScore(it.mood!!) }.average()
                val diff = avg - overallAvg
                if (abs(diff) < MIN_MOOD_DIFFERENCE) return@mapNotNull null
                val direction = if (diff > 0) "one of your happy places" else "linked to a lower mood"
                PatternResult(
                    patternType = PatternType.LOCATION_PATTERN,
                    description = "$name seems to be $direction (avg ${"%.1f".format(avg)}/5 vs ${"%.1f".format(overallAvg)}/5 overall).",
                    confidence = confidenceFor(group.size, diff),
                    evidence = evidenceFor(group, "location", name),
                    insights = listOf("Based on ${group.size} entries at $name")
                )
            }
    }

    private fun weatherPatterns(entries: List<JournalEntry>, overallAvg: Double): List<PatternResult> {
        return entries.filter { it.weather != null }
            .groupBy { it.weather!!.condition }
            .filter { it.value.size >= MIN_SAMPLE_SIZE }
            .mapNotNull { (condition, group) ->
                val avg = group.map { moodScore(it.mood!!) }.average()
                val diff = avg - overallAvg
                if (abs(diff) < MIN_MOOD_DIFFERENCE) return@mapNotNull null
                val direction = if (diff > 0) "lifts" else "dampens"
                PatternResult(
                    patternType = PatternType.WEATHER_CORRELATION,
                    description = "$condition weather $direction your mood (avg ${"%.1f".format(avg)}/5 vs ${"%.1f".format(overallAvg)}/5 overall).",
                    confidence = confidenceFor(group.size, diff),
                    evidence = evidenceFor(group, "weather", condition),
                    insights = listOf("Based on ${group.size} entries logged during $condition weather")
                )
            }
    }
}
