package com.dairum.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dairum.data.local.entity.JournalEntryEntity
import com.dairum.data.local.entity.MemoryEntity
import com.dairum.data.local.entity.PersonEntity
import com.dairum.testing.ExportTestSupport
import com.dairum.testing.InMemoryDatabaseRule
import com.dairum.testing.TestClock
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExportJsonValidityTest {

    @get:Rule
    val dbRule = InMemoryDatabaseRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    /** Moshi's reader is strict: trailing commas, bare "1,4" and trailing garbage all throw. */
    private fun strictParse(text: String): Map<String, Any?> {
        @Suppress("UNCHECKED_CAST")
        return Moshi.Builder().build().adapter(Any::class.java).fromJson(text) as Map<String, Any?>
    }

    @Test
    fun memoriesWithSeveralPeople_exportAsProperJsonArrays_andTheWholeDocumentParsesStrictly() = runBlocking {
        val db = dbRule.db
        db.journalEntryDao().insert(JournalEntryEntity(title = "e", createdAt = 1_000L, updatedAt = 1_000L))
        listOf("Ann", "Bob", "Cy").forEach { db.personDao().insert(PersonEntity(name = it, createdAt = 1L)) }
        // The app stores peopleInvolved as a comma list; cover several, one, none, and a bracketed form.
        db.memoryDao().insertMemory(MemoryEntity(title = "several", startDate = 1, endDate = 2, peopleInvolved = "1,2,3"))
        db.memoryDao().insertMemory(MemoryEntity(title = "one", startDate = 1, endDate = 2, peopleInvolved = "5"))
        db.memoryDao().insertMemory(MemoryEntity(title = "none", startDate = 1, endDate = 2, peopleInvolved = null))
        db.memoryDao().insertMemory(MemoryEntity(title = "bracketed", startDate = 1, endDate = 2, peopleInvolved = "[7,8]"))

        val journal = JournalRepositoryImpl(
            db.journalEntryDao(), db.tagDao(), db.personDao(), db.trackerDao(), db.mediaDao(), db.categoryDao(), db, TestClock()
        )
        val entries = journal.getAllEntries().first()
        val result = ExportTestSupport.exportRepo(db, context).exportToJson(entries).getOrThrow()
        try {
            val text = ExportTestSupport.read(context, result)
            val doc = strictParse(text) // throws if the document is not valid JSON

            @Suppress("UNCHECKED_CAST")
            val memories = (doc["memories"] as List<Map<String, Any?>>).associateBy { it["title"] as String }
            assertEquals(listOf(1.0, 2.0, 3.0), memories.getValue("several")["peopleInvolved"])
            assertEquals(listOf(5.0), memories.getValue("one")["peopleInvolved"])
            assertEquals(emptyList<Any?>(), memories.getValue("none")["peopleInvolved"])
            assertEquals(listOf(7.0, 8.0), memories.getValue("bracketed")["peopleInvolved"])
            assertTrue("the old bare form must be gone", !text.contains("\"peopleInvolved\": 1,2,3"))
            assertEquals(1, (doc["entries"] as List<*>).size)
        } finally {
            ExportTestSupport.delete(context, result)
        }
    }
}
