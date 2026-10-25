package com.dairum.data.search

import com.dairum.domain.model.JournalEntry
import com.dairum.domain.repository.JournalRepository
import kotlinx.coroutines.flow.first
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A journal entry matched by [ContextualSearchService], with a relevance score and a short
 * breakdown of what it matched on (tag/person/location/date/content) for display purposes.
 */
data class ContextualMatch(
    val entry: JournalEntry,
    val score: Float,
    val matchedOn: List<String>
)

/**
 * Keyword/entity-aware entry search: matches on title/content text, tag names, person names,
 * location names/addresses, and the entry's date - not just full-text search. Used both by the
 * AI chat's memory retrieval (so it can answer "when was I in Thanjavur?") and by the standalone
 * Contextual Search screen.
 */
@Singleton
class ContextualSearchService @Inject constructor(
    private val journalRepository: JournalRepository
) {
    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d yyyy EEEE MMM")
    }

    suspend fun search(query: String, limit: Int = 20): List<ContextualMatch> {
        val tokens = tokenize(query)
        if (tokens.isEmpty()) return emptyList()

        val entries = journalRepository.getAllEntries().first()
        return entries
            .mapNotNull { entry -> score(entry, tokens) }
            .sortedByDescending { it.score }
            .take(limit)
    }

    fun tokenize(query: String): List<String> {
        return query.lowercase()
            .split(Regex("[^\\p{L}\\p{N}]+"))
            .filter { it.length >= 3 }
            .distinct()
    }

    private fun score(entry: JournalEntry, tokens: List<String>): ContextualMatch? {
        var score = 0f
        val matched = mutableListOf<String>()

        val content = entry.plainContent.lowercase()
        val title = entry.title?.lowercase().orEmpty()
        val locations = listOfNotNull(entry.location) + entry.additionalLocations
        val dateText = entry.createdAt.format(DATE_FORMATTER).lowercase()

        tokens.forEach { token ->
            if (title.contains(token)) {
                score += 3f
                matched.add("title")
            }
            if (content.contains(token)) {
                score += 1f
                matched.add("content")
            }
            entry.tags.forEach { tag ->
                if (tag.name.lowercase().contains(token)) {
                    score += 3f
                    matched.add("tag: ${tag.name}")
                }
            }
            entry.people.forEach { person ->
                if (person.name.lowercase().contains(token)) {
                    score += 3f
                    matched.add("person: ${person.name}")
                }
            }
            locations.forEach { loc ->
                val place = "${loc.placeName.orEmpty()} ${loc.address.orEmpty()}".lowercase()
                if (place.isNotBlank() && place.contains(token)) {
                    score += 3f
                    matched.add("location: ${loc.placeName ?: loc.address}")
                }
            }
            if (dateText.contains(token)) {
                score += 2f
                matched.add("date")
            }
        }

        return if (score > 0f) ContextualMatch(entry, score, matched.distinct()) else null
    }
}
