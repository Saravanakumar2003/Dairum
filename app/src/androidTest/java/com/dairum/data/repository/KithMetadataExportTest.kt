package com.dairum.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dairum.data.local.entity.CategoryEntity
import com.dairum.data.local.entity.CustomTrackerEntity
import com.dairum.data.local.entity.JournalEntryEntity
import com.dairum.data.local.entity.MediaEntity
import com.dairum.data.local.entity.MediaType
import com.dairum.data.local.entity.PersonEntity
import com.dairum.data.local.entity.TagEntity
import com.dairum.data.local.entity.TrackerType
import com.dairum.data.local.entity.TrackerValueEntity
import com.dairum.domain.model.ExportFormat
import com.dairum.presentation.screens.settings.ManualExportViewModel
import com.dairum.testing.ExportTestSupport
import com.dairum.testing.InMemoryDatabaseRule
import com.dairum.testing.TestClock
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.TimeZone

@RunWith(AndroidJUnit4::class)
class KithMetadataExportTest {

    @get:Rule
    val dbRule = InMemoryDatabaseRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val originalZone: TimeZone = TimeZone.getDefault()

    private lateinit var journal: JournalRepositoryImpl
    private var ann = 0L
    private var bob = 0L
    private var carl = 0L
    private val ids = mutableMapOf<String, Long>()

    // Distinct instants, oldest first: e2 < e7 < e8 < e1 < e6.
    private val t = mapOf("e2" to 1_600_000_000_123L, "e7" to 1_650_000_000_000L, "e8" to 1_680_000_000_999L,
        "e1" to 1_700_000_000_456L, "e6" to 1_710_000_000_007L)

    @Before
    fun seed() { runBlocking {
        val db = dbRule.db
        journal = JournalRepositoryImpl(db.journalEntryDao(), db.tagDao(), db.personDao(), db.trackerDao(), db.mediaDao(), db.categoryDao(), db, TestClock())
        val cat = db.categoryDao().insert(CategoryEntity(name = "Travel", color = 0))
        val tagB = db.tagDao().insert(TagEntity(name = "tag-b"))
        val tagA = db.tagDao().insert(TagEntity(name = "Tag-a"))
        ann = db.personDao().insert(PersonEntity(name = "Ann Example", relationship = "Sister", email = "ann-mark@example.com",
            phone = "+91-MARK-PHONE", notes = "NOTES-MARK-40", birthday = 123_456_789L, photoUri = "/x/MARK-PHOTO.jpg", createdAt = 1L))
        bob = db.personDao().insert(PersonEntity(name = "Bob Example", relationship = null, createdAt = 1L))
        carl = db.personDao().insert(PersonEntity(name = "Carl OnlyInTrash", relationship = "Boss", createdAt = 1L))

        fun e(title: String?, createdAt: Long, cat: Long? = null, mood: Int? = null, archived: Boolean = false,
              draft: Boolean = false, deleted: Long? = null, rich: Boolean = false) = JournalEntryEntity(
            title = title, createdAt = createdAt, updatedAt = createdAt, categoryId = cat, moodLevel = mood,
            isArchived = archived, isDraft = draft, deletedAt = deleted,
            content = if (rich) "[{\"start\":0,\"end\":3,\"style\":\"BOLD\"}]FMT-MARK-55" else "",
            plainContent = if (rich) "BODY-MARK-91c2 the private diary text" else "",
            moodIcon = if (rich) "ICON-MARK" else null,
            weatherData = if (rich) "{\"condition\":\"WX-MARK-12\"}" else null,
            locationData = if (rich) "{\"placeName\":\"LOC-MARK-88\",\"latitude\":1.5}" else null,
            additionalLocationsData = if (rich) "{\"place\":\"LOC2-MARK-89\"}" else null,
            isFavorite = rich, isPinned = rich, backgroundColor = if (rich) "BG-MARK" else null, wordCount = if (rich) 77 else 0
        )
        val dao = db.journalEntryDao()
        ids["e1"] = dao.insert(e("TITLE-MARK-7f3a", t.getValue("e1"), cat, mood = 4, rich = true))
        ids["e2"] = dao.insert(e(null, t.getValue("e2"), mood = 31))          // out of the 1-30 range -> mood null
        ids["e7"] = dao.insert(e("Mood zero", t.getValue("e7"), mood = 0))    // 0 -> null
        ids["e8"] = dao.insert(e("Mood one", t.getValue("e8"), mood = 1))     // 1 -> Happy
        ids["e6"] = dao.insert(e("Mood thirty", t.getValue("e6"), mood = 30)) // 30 -> Motivated
        ids["archived"] = dao.insert(e("ARCHIVED-MARK", 1_000L, archived = true))
        ids["draft"] = dao.insert(e("DRAFT-MARK", 1_001L, draft = true))
        ids["trashed"] = dao.insert(e("TRASHED-MARK", 1_002L, deleted = 2_000L))

        journal.setTagsForEntry(ids.getValue("e1"), listOf(tagB, tagA))
        journal.setPeopleForEntry(ids.getValue("e1"), listOf(bob, ann))
        journal.setPeopleForEntry(ids.getValue("trashed"), listOf(carl))
        db.mediaDao().insert(MediaEntity(entryId = ids.getValue("e1"), type = MediaType.PHOTO, uri = "/data/MEDIA-MARK-33.jpg",
            fileName = "MEDIA-MARK-33.jpg", mimeType = "image/jpeg", sizeBytes = 1, caption = "CAPTION-MARK-34"))
        val tracker = db.trackerDao().insertTracker(CustomTrackerEntity(name = "TRACKER-MARK-35", type = TrackerType.TEXT))
        db.trackerDao().insertValue(TrackerValueEntity(entryId = ids.getValue("e1"), trackerId = tracker, value = "TRK-MARK-66"))
    } }

    @After
    fun restoreZone() = TimeZone.setDefault(originalZone)

    private fun strictParse(text: String): Map<String, Any?> {
        @Suppress("UNCHECKED_CAST")
        return Moshi.Builder().build().adapter(Any::class.java).fromJson(text) as Map<String, Any?>
    }

    private fun allKeys(node: Any?, into: MutableSet<String> = mutableSetOf()): Set<String> {
        when (node) {
            is Map<*, *> -> node.forEach { (k, v) -> into.add(k as String); allKeys(v, into) }
            is List<*> -> node.forEach { allKeys(it, into) }
        }
        return into
    }

    private suspend fun export(titles: Boolean): Pair<String, Map<String, Any?>> {
        val history = ExportTestSupport.exportRepo(dbRule.db, context).exportKithMetadata(titles).getOrThrow()
        try {
            val text = ExportTestSupport.read(context, history.filePath)
            return text to strictParse(text)
        } finally {
            ExportTestSupport.delete(context, history.filePath)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun entries(doc: Map<String, Any?>) = doc["entries"] as List<Map<String, Any?>>

    @Test
    fun theFileHasExactlyTheSpecifiedShape_andParsesStrictly() = runBlocking {
        val (text, doc) = export(titles = true)

        assertEquals(listOf("format", "version", "exportedAtEpochMs", "tz", "appVersion", "options", "entries", "people"), doc.keys.toList())
        assertEquals("dairum-metadata", doc["format"])
        assertEquals(1.0, doc["version"])
        assertEquals(1_700_000_000_000.0, doc["exportedAtEpochMs"]) // the injected clock
        assertEquals(ZoneId.systemDefault().id, doc["tz"])
        assertTrue((doc["appVersion"] as String).isNotBlank())
        assertEquals(mapOf("titles" to true), doc["options"])
        assertFalse("compact JSON has no newlines", text.contains('\n'))

        val first = entries(doc).first { (it["id"] as Double).toLong() == ids.getValue("e2") }
        assertEquals(listOf("id", "createdAtEpochMs", "createdAtLocal", "title", "mood", "category", "tags", "people"), first.keys.toList())
    }

    @Test
    fun noBodyFormattingMediaLocationWeatherTrackersFlagsOrContactDetailsEverAppear() = runBlocking {
        val (text, doc) = export(titles = true)

        val markers = listOf("BODY-MARK-91c2", "FMT-MARK-55", "MEDIA-MARK-33", "CAPTION-MARK-34", "LOC-MARK-88", "LOC2-MARK-89",
            "WX-MARK-12", "ann-mark@example.com", "MARK-PHONE", "NOTES-MARK-40", "MARK-PHOTO", "TRK-MARK-66", "TRACKER-MARK-35",
            "ICON-MARK", "BG-MARK", "TRASHED-MARK", "ARCHIVED-MARK", "DRAFT-MARK", "Carl OnlyInTrash", "123456789")
        markers.forEach { assertFalse("leaked: $it", text.contains(it)) }

        val forbiddenKeys = setOf("content", "plainContent", "formattingJson", "media", "weather", "location", "latitude", "longitude",
            "placeName", "trackerValues", "isFavorite", "isArchived", "isDraft", "isPinned", "backgroundColor", "moodLevel", "level",
            "moodIcon", "email", "phone", "notes", "birthday", "photoUri", "wordCount", "updatedAt", "importantDates", "deletedAt")
        assertEquals("forbidden keys present", emptySet<String>(), allKeys(doc).intersect(forbiddenKeys))
        assertTrue(text.contains("TITLE-MARK-7f3a")) // titles are on, so this one is expected
    }

    @Test
    fun titlesOff_omitsTheKeyEverywhere_andTheTitleTextIsGone() = runBlocking {
        val (text, doc) = export(titles = false)

        assertEquals(mapOf("titles" to false), doc["options"])
        entries(doc).forEach { assertFalse("title key present", it.containsKey("title")) }
        assertFalse(text.contains("TITLE-MARK-7f3a"))
        assertFalse(text.contains("Mood thirty"))
        assertEquals(5, entries(doc).size)
        // everything else is still there
        val e1 = entries(doc).first { (it["id"] as Double).toLong() == ids.getValue("e1") }
        assertEquals(listOf("id", "createdAtEpochMs", "createdAtLocal", "mood", "category", "tags", "people"), e1.keys.toList())
    }

    @Test
    fun entriesAreTheSameSelectionAsAll_oldestFirst_withMoodCategoryTagsAndPeople() = runBlocking {
        val (_, doc) = export(titles = true)
        val list = entries(doc)

        val allIds = journal.getAllEntries().first().map { it.id }.toSet()
        assertEquals("same selection as the All export", allIds, list.map { (it["id"] as Double).toLong() }.toSet())
        assertEquals(5, list.size)
        assertEquals("oldest first", listOf("e2", "e7", "e8", "e1", "e6").map { ids.getValue(it) }, list.map { (it["id"] as Double).toLong() })

        val byId = list.associateBy { (it["id"] as Double).toLong() }
        val e1 = byId.getValue(ids.getValue("e1"))
        assertEquals("TITLE-MARK-7f3a", e1["title"])
        assertEquals(mapOf("label" to "Grateful", "emoji" to "🙏"), e1["mood"])
        assertEquals("Travel", e1["category"])
        assertEquals("tags sorted by name, case-insensitively", listOf("Tag-a", "tag-b"), e1["tags"])
        assertEquals(listOf(mapOf("id" to ann.toDouble(), "name" to "Ann Example"), mapOf("id" to bob.toDouble(), "name" to "Bob Example")), e1["people"])

        assertNull("level 31 is not a mood", byId.getValue(ids.getValue("e2"))["mood"])
        assertNull("level 0 is not a mood", byId.getValue(ids.getValue("e7"))["mood"])
        assertEquals("Happy", (byId.getValue(ids.getValue("e8"))["mood"] as Map<*, *>)["label"])
        assertEquals("Motivated", (byId.getValue(ids.getValue("e6"))["mood"] as Map<*, *>)["label"])
        assertNull(byId.getValue(ids.getValue("e2"))["category"])
        assertEquals(emptyList<Any?>(), byId.getValue(ids.getValue("e2"))["tags"])
        assertTrue(byId.getValue(ids.getValue("e2")).containsKey("title") && byId.getValue(ids.getValue("e2"))["title"] == null)
    }

    @Test
    fun peopleListHoldsOnlyPeopleThatExportedEntriesReference_withIdNameRelationship() = runBlocking {
        val (_, doc) = export(titles = true)

        @Suppress("UNCHECKED_CAST")
        val people = doc["people"] as List<Map<String, Any?>>
        assertEquals("Carl is only on a trashed entry", 2, people.size)
        assertEquals(listOf(ann.toDouble(), bob.toDouble()), people.map { it["id"] })
        people.forEach { assertEquals(listOf("id", "name", "relationship"), it.keys.toList()) }
        assertEquals("Sister", people[0]["relationship"])
        assertTrue(people[1].containsKey("relationship") && people[1]["relationship"] == null)
        // every person id used by an entry is in the people list
        val referenced = entries(doc).flatMap { e -> (e["people"] as List<*>).map { (it as Map<*, *>)["id"] } }.toSet()
        assertEquals(people.map { it["id"] }.toSet(), referenced)
    }

    @Test
    fun aTimezoneChangeKeepsCreatedAtEpochMsAndOnlyChangesTheLocalTextAndTz() = runBlocking {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"))
        val (_, kolkata) = export(titles = true)
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"))
        val (_, newYork) = export(titles = true)

        assertEquals("Asia/Kolkata", kolkata["tz"])
        assertEquals("America/New_York", newYork["tz"])
        val a = entries(kolkata); val b = entries(newYork)
        assertEquals(a.size, b.size)
        a.zip(b).forEach { (x, y) ->
            assertEquals("same entry", x["id"], y["id"])
            assertEquals("epoch ms is zone-independent", x["createdAtEpochMs"], y["createdAtEpochMs"])
            assertNotEquals("local text follows the zone", x["createdAtLocal"], y["createdAtLocal"])
            val ms = (x["createdAtEpochMs"] as Double).toLong()
            fun iso(zone: String) = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.of(zone)))
            assertEquals(iso("Asia/Kolkata"), x["createdAtLocal"])
            assertEquals(iso("America/New_York"), y["createdAtLocal"])
            assertFalse("no offset in the local text", (x["createdAtLocal"] as String).let { it.endsWith("Z") || it.contains('+') })
        }
        // and the epoch is exactly what is stored, to the millisecond
        assertEquals(t.getValue("e1").toDouble(), a.first { (it["id"] as Double).toLong() == ids.getValue("e1") }["createdAtEpochMs"])
        assertEquals(t.getValue("e2").toDouble(), a.first()["createdAtEpochMs"])
    }

    @Test
    fun theExportScreenIgnoresAnyDateRange_andRemembersIncludeTitles() = runBlocking {
        val settings = ExportTestSupport.settingsRepository(context)
        val before = settings.getKithIncludeTitlesFlow().first()
        try {
            val vm = ManualExportViewModel(ExportTestSupport.exportRepo(dbRule.db, context), journal, settings)
            vm.setExportFormat(ExportFormat.KITH_METADATA)
            vm.setStartDate(5L) // a range that matches nothing: a snapshot must ignore it
            vm.setEndDate(6L)
            vm.setIncludeTitles(false)
            vm.exportEntries()
            val state = withTimeout(20_000) { vm.uiState.first { it.exportSuccess || it.error != null } }
            assertEquals(null, state.error)
            assertEquals(5, state.lastExportEntryCount)
            val path = state.lastExportPath!!
            try {
                val doc = strictParse(ExportTestSupport.read(context, path))
                assertEquals(5, entries(doc).size)
                assertEquals(mapOf("titles" to false), doc["options"])
            } finally {
                ExportTestSupport.delete(context, path)
            }
            // saved: a brand-new view model (and the repository) now reports titles off
            assertEquals(false, settings.getKithIncludeTitlesFlow().first())
            val vm2 = ManualExportViewModel(ExportTestSupport.exportRepo(dbRule.db, context), journal, settings)
            assertEquals(false, withTimeout(10_000) { vm2.uiState.first { !it.includeTitles } }.includeTitles)
        } finally {
            settings.setKithIncludeTitles(before)
        }
    }
}
