package com.dairum.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dairum.data.local.entity.CategoryEntity
import com.dairum.data.local.entity.JournalEntryEntity
import com.dairum.data.local.entity.MediaEntity
import com.dairum.data.local.entity.MediaType
import com.dairum.data.local.entity.PersonEntity
import com.dairum.data.local.entity.TagEntity
import com.dairum.testing.ExportTestSupport
import com.dairum.testing.InMemoryDatabaseRule
import com.dairum.domain.model.ExportFormat
import com.dairum.presentation.screens.settings.ManualExportViewModel
import com.dairum.testing.TestClock
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** A date-range export must carry category, tags and people, exactly like the "All" export. */
@RunWith(AndroidJUnit4::class)
class DateRangeExportTest {

    @get:Rule
    val dbRule = InMemoryDatabaseRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var journal: JournalRepositoryImpl
    private var inRangeId = 0L

    private val rangeStart = 10_000L
    private val rangeEnd = 20_000L

    @Before
    fun seed() { runBlocking {
        val db = dbRule.db
        journal = JournalRepositoryImpl(
            db.journalEntryDao(), db.tagDao(), db.personDao(), db.trackerDao(), db.mediaDao(), db.categoryDao(), db, TestClock()
        )
        val category = db.categoryDao().insert(CategoryEntity(name = "Travel", color = 0))
        val tagA = db.tagDao().insert(TagEntity(name = "trek"))
        val tagB = db.tagDao().insert(TagEntity(name = "sunrise"))
        val ann = db.personDao().insert(PersonEntity(name = "Ann Example", createdAt = 1L))
        val bob = db.personDao().insert(PersonEntity(name = "Bob Example", createdAt = 1L))

        fun entry(title: String, createdAt: Long, archived: Boolean = false, draft: Boolean = false, deleted: Long? = null) =
            JournalEntryEntity(title = title, createdAt = createdAt, updatedAt = createdAt, categoryId = category,
                isArchived = archived, isDraft = draft, deletedAt = deleted)

        inRangeId = db.journalEntryDao().insert(entry("in range", 15_000L))
        db.journalEntryDao().insert(entry("archived in range", 15_100L, archived = true))
        db.journalEntryDao().insert(entry("draft in range", 15_200L, draft = true))
        db.journalEntryDao().insert(entry("trashed in range", 15_300L, deleted = 16_000L))
        db.journalEntryDao().insert(entry("before range", 9_999L))
        db.journalEntryDao().insert(entry("at range end (exclusive)", rangeEnd))

        journal.setTagsForEntry(inRangeId, listOf(tagA, tagB))
        journal.setPeopleForEntry(inRangeId, listOf(ann, bob))
        db.mediaDao().insert(
            MediaEntity(entryId = inRangeId, type = MediaType.PHOTO, uri = "/x/p.jpg", fileName = "p.jpg", mimeType = "image/jpeg", sizeBytes = 1)
        )
    } }

    @Test
    fun rangeWithDetails_hasCategoryTagsPeopleAndMedia_andSelectsLikeTheAllExport() = runBlocking {
        val list = journal.getEntriesInDateRangeWithDetails(rangeStart, rangeEnd)

        assertEquals("archived, draft, trashed and out-of-range entries stay out", listOf("in range"), list.map { it.title })
        val e = list.single()
        assertEquals("Travel", e.category?.name)
        assertEquals(setOf("trek", "sunrise"), e.tags.map { it.name }.toSet())
        assertEquals(setOf("Ann Example", "Bob Example"), e.people.map { it.name }.toSet())
        assertEquals(1, e.media.size)
    }

    @Test
    fun theOldBasicRangeLookupStillLacksThem_whichIsWhyExportsMustNotUseIt() = runBlocking {
        val basic = journal.getEntriesInDateRangeFlow(rangeStart, rangeEnd).first().single()
        assertTrue(basic.tags.isEmpty() && basic.people.isEmpty())
        assertEquals(null, basic.category)
    }

    @Test
    fun jsonExportOfADateRange_carriesCategoryTagsAndPeople_likeTheAllExport() = runBlocking {
        val exporter = ExportTestSupport.exportRepo(dbRule.db, context)
        val ranged = journal.getEntriesInDateRangeWithDetails(rangeStart, rangeEnd)
        val all = journal.getAllEntries().first().filter { it.id == inRangeId }

        suspend fun exportedEntry(entries: List<com.dairum.domain.model.JournalEntry>): Map<*, *> {
            val result = exporter.exportToJson(entries).getOrThrow()
            try {
                val doc = Moshi.Builder().build().adapter(Any::class.java).fromJson(ExportTestSupport.read(context, result)) as Map<*, *>
                return (doc["entries"] as List<*>).single() as Map<*, *>
            } finally {
                ExportTestSupport.delete(context, result)
            }
        }

        val fromRange = exportedEntry(ranged)
        val fromAll = exportedEntry(all)

        assertEquals("Travel", (fromRange["category"] as Map<*, *>)["name"])
        assertEquals(2, (fromRange["tags"] as List<*>).size)
        assertEquals(2, (fromRange["people"] as List<*>).size)
        assertNotNull(fromRange["media"])
        assertEquals(fromAll["category"], fromRange["category"])
        assertEquals(fromAll["tags"], fromRange["tags"])
        assertEquals(fromAll["people"], fromRange["people"])
    }

    @Test
    fun manualExportScreenWithACustomRange_exportsCategoryTagsAndPeople() = runBlocking {
        // The real caller: ManualExportViewModel over the real repositories.
        val vm = ManualExportViewModel(
            ExportTestSupport.exportRepo(dbRule.db, context), journal, ExportTestSupport.settingsRepository(context)
        )
        vm.setStartDate(rangeStart)
        vm.setEndDate(rangeEnd)
        vm.setExportFormat(ExportFormat.JSON)
        vm.exportEntries()

        val state = withTimeout(20_000) { vm.uiState.first { it.exportSuccess || it.error != null } }
        assertEquals(null, state.error)
        val path = state.lastExportPath!!
        try {
            val doc = Moshi.Builder().build().adapter(Any::class.java).fromJson(ExportTestSupport.read(context, path)) as Map<*, *>
            val entry = (doc["entries"] as List<*>).single() as Map<*, *>
            assertEquals("Travel", (entry["category"] as Map<*, *>)["name"])
            assertEquals(2, (entry["tags"] as List<*>).size)
            assertEquals(2, (entry["people"] as List<*>).size)
        } finally {
            ExportTestSupport.delete(context, path)
        }
    }
}
