package com.dairum.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dairum.data.local.entity.CustomTrackerEntity
import com.dairum.data.local.entity.JournalEntryEntity
import com.dairum.data.local.entity.MediaEntity
import com.dairum.data.local.entity.MediaType
import com.dairum.data.local.entity.PersonEntity
import com.dairum.data.local.entity.TagEntity
import com.dairum.data.local.entity.TrackerType
import com.dairum.domain.model.JournalEntry
import com.dairum.testing.InMemoryDatabaseRule
import com.dairum.testing.TestClock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RunWith(AndroidJUnit4::class)
class JournalRepositoryWriteTest {

    @get:Rule
    val dbRule = InMemoryDatabaseRule()

    private val clock = TestClock(startMillis = T0)
    private lateinit var repo: JournalRepositoryImpl

    private val db get() = dbRule.db
    private val entryDao get() = db.journalEntryDao()
    private val tagDao get() = db.tagDao()
    private val personDao get() = db.personDao()
    private val trackerDao get() = db.trackerDao()
    private val mediaDao get() = db.mediaDao()

    @Before
    fun setUp() {
        repo = JournalRepositoryImpl(
            entryDao, tagDao, personDao, trackerDao, mediaDao, db.categoryDao(), db, clock
        )
    }

    // ---- updatedAt bumps ---------------------------------------------------------------

    @Test
    fun toggleFavorite_bumpsUpdatedAt() = runBlocking {
        val id = insertEntry()
        clock.advanceTo(T1)
        repo.toggleFavorite(id, true)
        val e = entryDao.getById(id)!!
        assertTrue(e.isFavorite)
        assertEquals(T1, e.updatedAt)
        assertEquals(T0, e.createdAt)
    }

    @Test
    fun archiveEntry_bumpsUpdatedAt() = runBlocking {
        val id = insertEntry()
        clock.advanceTo(T1)
        repo.archiveEntry(id, true)
        val e = entryDao.getById(id)!!
        assertTrue(e.isArchived)
        assertEquals(T1, e.updatedAt)
    }

    @Test
    fun togglePin_bumpsUpdatedAt() = runBlocking {
        val id = insertEntry()
        clock.advanceTo(T1)
        repo.togglePin(id, true)
        val e = entryDao.getById(id)!!
        assertTrue(e.isPinned)
        assertEquals(T1, e.updatedAt)
    }

    @Test
    fun softDelete_setsDeletedAt_andBumpsUpdatedAt() = runBlocking {
        val id = insertEntry()
        clock.advanceTo(T1)
        repo.softDelete(id)
        val e = entryDao.getById(id)!!
        assertEquals(T1, e.deletedAt)
        assertEquals(T1, e.updatedAt)
    }

    @Test
    fun restoreEntry_clearsDeletedAt_andBumpsUpdatedAt() = runBlocking {
        val id = insertEntry()
        clock.advanceTo(T1)
        repo.softDelete(id)
        clock.advanceTo(T2)
        repo.restoreEntry(id)
        val e = entryDao.getById(id)!!
        assertNull(e.deletedAt)
        assertEquals(T2, e.updatedAt)
    }

    // ---- mapper: deletedAt -------------------------------------------------------------

    @Test
    fun createEntry_carriesDeletedAtFromTheDomainObject() = runBlocking {
        val id = repo.createEntry(JournalEntry(title = "t", deletedAt = 777L))
        assertEquals(777L, entryDao.getById(id)!!.deletedAt)
    }

    @Test
    fun updateEntry_onATrashedEntry_keepsItInTheTrash() = runBlocking {
        val id = insertEntry()
        clock.advanceTo(T1)
        repo.softDelete(id)
        // The editor builds a fresh domain object with no deletedAt and calls updateEntry.
        repo.updateEntry(JournalEntry(id = id, title = "edited", createdAt = ldt(T0), updatedAt = ldt(T2)))
        val e = entryDao.getById(id)!!
        assertEquals("edited", e.title)
        assertEquals("update must not resurrect a trashed entry", T1, e.deletedAt)
    }

    @Test
    fun domainRoundTrip_preservesDeletedAt() = runBlocking {
        val id = insertEntry()
        clock.advanceTo(T1)
        repo.softDelete(id)
        val loaded = repo.getEntry(id)!!
        assertEquals(T1, loaded.deletedAt)
        repo.updateEntry(loaded.copy(title = "x"))
        assertEquals(T1, entryDao.getById(id)!!.deletedAt)
    }

    // ---- set* bump the parent, in one transaction ----------------------------------------

    @Test
    fun setTagsForEntry_bumpsParent_andAdjustsUsageCounts() = runBlocking {
        val entryId = insertEntry()
        val a = tagDao.insert(TagEntity(name = "a"))
        val b = tagDao.insert(TagEntity(name = "b"))
        clock.advanceTo(T1)
        repo.setTagsForEntry(entryId, listOf(a, b))
        assertEquals(T1, entryDao.getById(entryId)!!.updatedAt)
        assertEquals(setOf(a, b), tagDao.getTagIdsForEntry(entryId).toSet())
        assertEquals(1, tagDao.getById(a)!!.usageCount)

        clock.advanceTo(T2)
        repo.setTagsForEntry(entryId, listOf(a))
        assertEquals(T2, entryDao.getById(entryId)!!.updatedAt)
        assertEquals(listOf(a), tagDao.getTagIdsForEntry(entryId))
        assertEquals(0, tagDao.getById(b)!!.usageCount)
    }

    @Test
    fun setPeopleForEntry_bumpsParent_andAdjustsMentionCounts() = runBlocking {
        val entryId = insertEntry()
        val p1 = personDao.insert(PersonEntity(name = "Ann", createdAt = T0))
        val p2 = personDao.insert(PersonEntity(name = "Bob", createdAt = T0))
        clock.advanceTo(T1)
        repo.setPeopleForEntry(entryId, listOf(p1, p2))
        assertEquals(T1, entryDao.getById(entryId)!!.updatedAt)
        assertEquals(setOf(p1, p2), personDao.getPeopleForEntry(entryId).map { it.id }.toSet())
        assertEquals(1, personDao.getById(p1)!!.mentionCount)

        clock.advanceTo(T2)
        repo.setPeopleForEntry(entryId, listOf(p1))
        assertEquals(T2, entryDao.getById(entryId)!!.updatedAt)
        assertEquals(0, personDao.getById(p2)!!.mentionCount)
    }

    @Test
    fun setTrackerValues_bumpsParent() = runBlocking {
        val entryId = insertEntry()
        val tracker = trackerDao.insertTracker(CustomTrackerEntity(name = "mood", type = TrackerType.TEXT))
        clock.advanceTo(T1)
        repo.setTrackerValues(entryId, mapOf(tracker to "calm"))
        assertEquals(T1, entryDao.getById(entryId)!!.updatedAt)
        assertEquals("calm", trackerDao.getValuesForEntry(entryId).single().value)
    }

    // ---- atomicity: a failure mid-way leaves the old data and the old updatedAt ----------

    @Test
    fun setTagsForEntry_failureMidway_keepsOldLinksCountsAndUpdatedAt() = runBlocking {
        val entryId = insertEntry()
        val a = tagDao.insert(TagEntity(name = "a"))
        val b = tagDao.insert(TagEntity(name = "b"))
        repo.setTagsForEntry(entryId, listOf(a, b))
        val before = entryDao.getById(entryId)!!
        clock.advanceTo(T2)

        // Tag 999999 does not exist: the foreign key fails after the old links were already deleted
        // and the first new link inserted.
        expectConstraintFailure { repo.setTagsForEntry(entryId, listOf(a, 999_999L)) }

        assertEquals(setOf(a, b), tagDao.getTagIdsForEntry(entryId).toSet())
        assertEquals(1, tagDao.getById(a)!!.usageCount)
        assertEquals(1, tagDao.getById(b)!!.usageCount)
        assertEquals(before.updatedAt, entryDao.getById(entryId)!!.updatedAt)
    }

    @Test
    fun setPeopleForEntry_failureMidway_keepsOldLinksCountsAndUpdatedAt() = runBlocking {
        val entryId = insertEntry()
        val p1 = personDao.insert(PersonEntity(name = "Ann", createdAt = T0))
        val p2 = personDao.insert(PersonEntity(name = "Bob", createdAt = T0))
        repo.setPeopleForEntry(entryId, listOf(p1, p2))
        val before = entryDao.getById(entryId)!!
        clock.advanceTo(T2)

        expectConstraintFailure { repo.setPeopleForEntry(entryId, listOf(p1, 999_999L)) }

        assertEquals(setOf(p1, p2), personDao.getPeopleForEntry(entryId).map { it.id }.toSet())
        assertEquals(1, personDao.getById(p1)!!.mentionCount)
        assertEquals(1, personDao.getById(p2)!!.mentionCount)
        assertEquals(before.updatedAt, entryDao.getById(entryId)!!.updatedAt)
    }

    @Test
    fun setTrackerValues_failureMidway_keepsOldValuesAndUpdatedAt() = runBlocking {
        val entryId = insertEntry()
        val t1 = trackerDao.insertTracker(CustomTrackerEntity(name = "a", type = TrackerType.TEXT))
        repo.setTrackerValues(entryId, mapOf(t1 to "old"))
        val before = entryDao.getById(entryId)!!
        clock.advanceTo(T2)

        // First pair succeeds ("new" replaces "old"), second names a tracker that does not exist.
        expectConstraintFailure {
            repo.setTrackerValues(entryId, linkedMapOf(t1 to "new", 999_999L to "x"))
        }

        assertEquals("old", trackerDao.getValuesForEntry(entryId).single().value)
        assertEquals(before.updatedAt, entryDao.getById(entryId)!!.updatedAt)
    }

    // ---- delete forever / empty trash remove the media files ---------------------------------

    @Test
    fun deleteEntry_removesRowsAndMediaFiles() = runBlocking {
        val entryId = insertEntry()
        val photo = tempFile("photo.jpg")
        val thumb = tempFile("thumb.jpg")
        insertMedia(entryId, photo, thumb)
        val other = insertEntry()
        val otherFile = tempFile("other.jpg")
        insertMedia(other, otherFile)

        repo.deleteEntry(entryId)

        assertNull(entryDao.getById(entryId))
        assertTrue(mediaDao.getMediaForEntry(entryId).isEmpty())
        assertFalse(photo.exists())
        assertFalse(thumb.exists())
        assertTrue("another entry's file must survive", otherFile.exists())
        assertNotNull(entryDao.getById(other))
    }

    @Test
    fun emptyTrash_removesOnlyTrashedEntriesAndTheirMediaFiles() = runBlocking {
        val trashed = insertEntry()
        val trashedFile = tempFile("trashed.jpg")
        insertMedia(trashed, trashedFile)
        val live = insertEntry()
        val liveFile = tempFile("live.jpg")
        insertMedia(live, liveFile)
        repo.softDelete(trashed)

        repo.emptyTrash()

        assertNull(entryDao.getById(trashed))
        assertFalse(trashedFile.exists())
        assertNotNull(entryDao.getById(live))
        assertTrue(liveFile.exists())
        assertEquals(1, mediaDao.getMediaForEntry(live).size)
    }

    @Test
    fun softDelete_keepsMediaFilesSoRestoreWorks() = runBlocking {
        val entryId = insertEntry()
        val photo = tempFile("photo.jpg")
        insertMedia(entryId, photo)
        repo.softDelete(entryId)
        assertTrue(photo.exists())
        repo.restoreEntry(entryId)
        assertTrue(photo.exists())
        assertEquals(1, mediaDao.getMediaForEntry(entryId).size)
    }

    // ---- helpers -------------------------------------------------------------------------

    private suspend fun insertEntry(): Long =
        entryDao.insert(JournalEntryEntity(title = "e", createdAt = T0, updatedAt = T0))

    private suspend fun insertMedia(entryId: Long, file: File, thumb: File? = null) {
        mediaDao.insert(
            MediaEntity(
                entryId = entryId, type = MediaType.PHOTO, uri = file.absolutePath,
                thumbnailUri = thumb?.absolutePath, fileName = file.name,
                mimeType = "image/jpeg", sizeBytes = file.length()
            )
        )
    }

    private fun tempFile(name: String): File {
        val dir = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "journal-write-test")
        dir.mkdirs()
        return File(dir, "${System.nanoTime()}-$name").also { it.writeText("x") }
    }

    private suspend fun expectConstraintFailure(block: suspend () -> Unit) {
        try {
            block()
            fail("expected a foreign key failure")
        } catch (e: SQLiteConstraintException) {
            // expected
        }
    }

    private fun ldt(epochMillis: Long): LocalDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault())

    private companion object {
        const val T0 = 1_000L
        const val T1 = 2_000L
        const val T2 = 3_000L
    }
}
