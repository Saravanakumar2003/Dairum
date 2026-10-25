package com.dairum.presentation.screens.trash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dairum.data.local.entity.JournalEntryEntity
import com.dairum.data.repository.JournalRepositoryImpl
import com.dairum.testing.InMemoryDatabaseRule
import com.dairum.testing.TestClock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

/** Drives the real TrashScreen + TrashViewModel + repository over an in-memory database. */
@RunWith(AndroidJUnit4::class)
class TrashScreenTest {

    private val dbRule = InMemoryDatabaseRule()
    private val composeRule = createComposeRule()

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(dbRule).around(composeRule)

    private var entryId = 0L

    @Before
    fun setUp() {
        val db = dbRule.db
        val repo = JournalRepositoryImpl(
            db.journalEntryDao(), db.tagDao(), db.personDao(), db.trackerDao(),
            db.mediaDao(), db.categoryDao(), db, TestClock()
        )
        runBlocking {
            entryId = db.journalEntryDao().insert(
                JournalEntryEntity(title = "Doomed entry", plainContent = "body", createdAt = 1L, updatedAt = 1L)
            )
            repo.softDelete(entryId)
        }
        composeRule.setContent { TrashScreen(onNavigateBack = {}, viewModel = TrashViewModel(repo)) }
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTextCount("Doomed entry") > 0
        }
    }

    @Test
    fun deletePermanently_asksFirst_andCancelKeepsTheEntry() {
        composeRule.onNodeWithContentDescription("Delete permanently").performClick()

        composeRule.onNodeWithText("Delete permanently?").assertIsDisplayed()
        composeRule.onNodeWithText("This cannot be undone.", substring = true).assertIsDisplayed()
        assertNotNull("nothing is deleted before confirming", row())

        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Doomed entry").assertIsDisplayed()
        assertNotNull(row())
    }

    @Test
    fun deletePermanently_confirmDeletesTheEntry() {
        composeRule.onNodeWithContentDescription("Delete permanently").performClick()
        composeRule.onNodeWithText("Delete").performClick()

        composeRule.waitUntil(5_000) { row() == null }
        assertNull(row())
    }

    @Test
    fun emptyTrash_asksFirst_andCancelKeepsTheEntry() {
        composeRule.onNodeWithContentDescription("Empty trash").performClick()

        composeRule.onNodeWithText("Empty Trash?").assertIsDisplayed()
        composeRule.onNodeWithText("This cannot be undone.", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.waitForIdle()

        assertNotNull(row())
    }

    @Test
    fun emptyTrash_confirmDeletesTheEntry() {
        composeRule.onNodeWithContentDescription("Empty trash").performClick()
        composeRule.onNodeWithText("Empty").performClick()

        composeRule.waitUntil(5_000) { row() == null }
        assertNull(row())
    }

    private fun row() = runBlocking { dbRule.db.journalEntryDao().getById(entryId) }

    private fun androidx.compose.ui.test.junit4.ComposeContentTestRule.onAllNodesWithTextCount(text: String): Int =
        onAllNodes(androidx.compose.ui.test.hasText(text)).fetchSemanticsNodes().size
}
