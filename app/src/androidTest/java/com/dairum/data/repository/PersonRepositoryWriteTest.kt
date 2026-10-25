package com.dairum.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dairum.domain.model.Person
import com.dairum.testing.InMemoryDatabaseRule
import com.dairum.testing.TestClock
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersonRepositoryWriteTest {

    @get:Rule
    val dbRule = InMemoryDatabaseRule()

    private val clock = TestClock(startMillis = 1_000L)
    private lateinit var repo: PersonRepositoryImpl

    @Before
    fun setUp() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        repo = PersonRepositoryImpl(dbRule.db.personDao(), moshi, clock)
    }

    @Test
    fun create_stampsCreatedAtFromTheClock() = runBlocking {
        val id = repo.create(Person(name = "Ann"))
        assertEquals(1_000L, dbRule.db.personDao().getById(id)!!.createdAt)
    }

    @Test
    fun update_withAPersonReadFromTheDatabase_keepsCreatedAt() = runBlocking {
        val id = repo.create(Person(name = "Ann"))
        clock.advanceTo(9_000L)
        val loaded = repo.getById(id)!!
        assertEquals(1_000L, loaded.createdAt)

        repo.update(loaded.copy(name = "Anne"))

        val row = dbRule.db.personDao().getById(id)!!
        assertEquals("Anne", row.name)
        assertEquals(1_000L, row.createdAt)
    }

    // This is what the Manage People edit dialog does: it builds a fresh Person(id = ...) with no
    // createdAt, which used to reset it to "now" on every edit.
    @Test
    fun update_withAFreshPersonObject_keepsStoredCreatedAt() = runBlocking {
        val id = repo.create(Person(name = "Ann"))
        clock.advanceTo(9_000L)

        repo.update(Person(id = id, name = "Anne", relationship = "friend"))

        val row = dbRule.db.personDao().getById(id)!!
        assertEquals("friend", row.relationship)
        assertEquals(1_000L, row.createdAt)
    }

    @Test
    fun update_withAPersonFromTheCountedList_keepsCreatedAt() = runBlocking {
        val id = repo.create(Person(name = "Ann"))
        clock.advanceTo(9_000L)
        val fromList = repo.getAllWithCountFlow().first().single()
        assertEquals(1_000L, fromList.createdAt)

        repo.update(fromList.copy(notes = "met at work"))

        assertEquals(1_000L, dbRule.db.personDao().getById(id)!!.createdAt)
    }

    @Test
    fun create_withExplicitCreatedAt_keepsIt() = runBlocking {
        val id = repo.create(Person(name = "Old friend", createdAt = 123L))
        assertEquals(123L, dbRule.db.personDao().getById(id)!!.createdAt)
    }
}
