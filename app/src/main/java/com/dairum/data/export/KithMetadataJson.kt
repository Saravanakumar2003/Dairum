package com.dairum.data.export

import com.dairum.data.local.entity.EntryMetadataRow
import com.dairum.domain.model.MoodOption
import com.squareup.moshi.JsonWriter
import okio.Buffer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Renders the "Metadata for Kith" file: titles, mood, category, tags and people only.
 *
 * The input rows are a narrow projection (id, createdAt, title, categoryId, moodLevel plus category,
 * tags and people), so body text, formatting spans, media, weather, location, trackers, flags,
 * contact details, notes and birthdays never reach this code, and nothing here can leak them.
 * Output is compact, strict JSON written with Moshi's JsonWriter in a fixed key order.
 */
object KithMetadataJson {
    const val FORMAT = "dairum-metadata"
    const val VERSION = 1L

    fun render(
        rows: List<EntryMetadataRow>,
        includeTitles: Boolean,
        exportedAtEpochMs: Long,
        zone: ZoneId,
        appVersion: String
    ): String {
        val ordered = rows.sortedWith(compareBy({ it.entry.createdAt }, { it.entry.id }))
        val buffer = Buffer()
        JsonWriter.of(buffer).use { w ->
            w.serializeNulls = true
            w.beginObject()
            w.name("format").value(FORMAT)
            w.name("version").value(VERSION)
            w.name("exportedAtEpochMs").value(exportedAtEpochMs)
            w.name("tz").value(zone.id)
            w.name("appVersion").value(appVersion)
            w.name("options").beginObject().name("titles").value(includeTitles).endObject()

            w.name("entries").beginArray()
            for (row in ordered) {
                val e = row.entry
                w.beginObject()
                w.name("id").value(e.id)
                w.name("createdAtEpochMs").value(e.createdAt)
                w.name("createdAtLocal").value(localIso(e.createdAt, zone))
                if (includeTitles) w.name("title").value(e.title)
                w.name("mood")
                val mood = e.moodLevel?.let { MoodOption.fromId(it) } // ids 1-30 only; the raw level is never written
                if (mood != null) {
                    w.beginObject().name("label").value(mood.label).name("emoji").value(mood.emoji).endObject()
                } else {
                    w.nullValue()
                }
                w.name("category").value(row.category?.name)
                w.name("tags").beginArray()
                row.tags.map { it.name }.sortedWith(compareBy({ it.lowercase() }, { it })).forEach { w.value(it) }
                w.endArray()
                w.name("people").beginArray()
                row.people.sortedBy { it.id }.forEach { p ->
                    w.beginObject().name("id").value(p.id).name("name").value(p.name).endObject()
                }
                w.endArray()
                w.endObject()
            }
            w.endArray()

            // Only people that an exported entry refers to.
            val referenced = ordered.flatMap { it.people }.associateBy { it.id }.values.sortedBy { it.id }
            w.name("people").beginArray()
            for (p in referenced) {
                w.beginObject()
                w.name("id").value(p.id)
                w.name("name").value(p.name)
                w.name("relationship").value(p.relationship)
                w.endObject()
            }
            w.endArray()
            w.endObject()
        }
        return buffer.readUtf8()
    }

    /** ISO local date-time without an offset, e.g. 2024-06-02T21:40:05.077, in [zone]. */
    internal fun localIso(epochMs: Long, zone: ZoneId): String =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMs), zone).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}
