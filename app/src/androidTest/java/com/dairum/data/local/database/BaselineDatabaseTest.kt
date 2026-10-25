package com.dairum.data.local.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Baseline: does the current code open a real (scrubbed) on-disk database, and is that database
 * healthy afterwards? Runs before any sync schema work so later migrations have a known-good start.
 *
 * The fixture is NOT in git. Generate it with scripts/db/scrub_db.py and drop it in
 * app/src/androidTest/assets/fixtures/ (see the README there). Without it, the fixture test is
 * skipped and only the fresh-database test runs.
 */
@RunWith(AndroidJUnit4::class)
class BaselineDatabaseTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val targetContext: Context = instrumentation.targetContext
    private val testContext: Context = instrumentation.context

    @Before
    fun setUp() {
        targetContext.deleteDatabase(DB_NAME)
    }

    @After
    fun tearDown() {
        targetContext.deleteDatabase(DB_NAME)
        File(targetContext.cacheDir, FIXTURE_COPY).delete()
    }

    @Test
    fun freshDatabase_opensAndPassesIntegrityChecks() {
        val db = buildDatabase(prepackaged = null)
        try {
            val sdb = db.openHelper.writableDatabase
            assertHealthy(sdb)
        } finally {
            db.close()
        }
    }

    @Test
    fun scrubbedRealDatabase_opensWithCurrentMigrations() {
        val fixtureAvailable = testContext.assets.list(FIXTURE_DIR)?.contains(FIXTURE_NAME) == true
        assumeTrue("No $FIXTURE_DIR/$FIXTURE_NAME in androidTest assets; skipping", fixtureAvailable)

        val copy = File(targetContext.cacheDir, FIXTURE_COPY).also { it.delete() }
        testContext.assets.open("$FIXTURE_DIR/$FIXTURE_NAME").use { input ->
            copy.outputStream().use { input.copyTo(it) }
        }

        val fixtureVersion = readUserVersion(copy)
        val countsBefore = readTableCounts(copy)
        println("BaselineDatabaseTest: fixture user_version=$fixtureVersion, tables=${countsBefore.size}")
        assertTrue(
            "Fixture user_version=$fixtureVersion is newer than the app's schema; Room would refuse to open it",
            fixtureVersion <= CURRENT_VERSION
        )

        val db = buildDatabase(prepackaged = copy)
        try {
            val sdb = db.openHelper.writableDatabase // throws if identity hash / migrations don't line up
            assertEquals(CURRENT_VERSION, sdb.version)

            // Row counts: opening (and any migrations) must not lose or invent rows in existing tables.
            val countsAfter = readTableCounts(sdb)
            countsBefore.forEach { (table, before) ->
                assertEquals("Row count changed for $table", before, countsAfter[table])
            }

            // Optional sidecar written by the scrub script: counts of the scrubbed file.
            readExpectedCounts()?.forEach { (table, expected) ->
                assertEquals("Row count differs from scrub-time count for $table", expected, countsAfter[table])
            }

            // Read every entry through Room (converters, enums, relations) so bad data fails loudly.
            runBlocking {
                val dao = db.journalEntryDao()
                rowIds(sdb, "journal_entries").forEach { id ->
                    dao.getWithDetails(id)
                }
                db.personDao().getAll()
            }

            assertHealthy(sdb)
        } finally {
            db.close()
        }
    }

    // ---- helpers -------------------------------------------------------------------------

    private fun buildDatabase(prepackaged: File?): DairumDatabase {
        val builder = Room.databaseBuilder(targetContext, DairumDatabase::class.java, DB_NAME)
            .addMigrations(*Migrations.ALL_MIGRATIONS)
        if (prepackaged != null) builder.createFromFile(prepackaged)
        return builder.build()
    }

    private fun assertHealthy(db: SupportSQLiteDatabase) {
        val integrity = db.queryStrings("PRAGMA integrity_check")
        assertEquals("PRAGMA integrity_check", listOf("ok"), integrity)

        // foreign_key_check columns: table, rowid, parent, fkid. Report aggregates only (no row data).
        val violations = mutableMapOf<String, Int>()
        db.query("PRAGMA foreign_key_check").use { c ->
            while (c.moveToNext()) {
                val key = "${c.getString(0)} -> ${c.getString(2)}"
                violations[key] = (violations[key] ?: 0) + 1
            }
        }
        assertTrue("PRAGMA foreign_key_check violations: $violations", violations.isEmpty())
    }

    private fun SupportSQLiteDatabase.queryStrings(sql: String): List<String> =
        query(sql).use { c -> buildList { while (c.moveToNext()) add(c.getString(0)) } }

    private fun userTables(db: SupportSQLiteDatabase): List<String> = db.queryStrings(
        "SELECT name FROM sqlite_master WHERE type='table' " +
            "AND name NOT LIKE 'sqlite_%' AND name NOT IN ('android_metadata','room_master_table') ORDER BY name"
    )

    private fun readTableCounts(db: SupportSQLiteDatabase): Map<String, Long> =
        userTables(db).associateWith { t ->
            db.query("SELECT COUNT(*) FROM `$t`").use { c -> c.moveToFirst(); c.getLong(0) }
        }

    private fun readTableCounts(file: File): Map<String, Long> {
        val raw = SQLiteDatabase.openDatabase(file.path, null, SQLiteDatabase.OPEN_READONLY)
        try {
            val tables = raw.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' " +
                    "AND name NOT LIKE 'sqlite_%' AND name NOT IN ('android_metadata','room_master_table') ORDER BY name",
                null
            ).use { c -> buildList { while (c.moveToNext()) add(c.getString(0)) } }
            return tables.associateWith { t ->
                raw.rawQuery("SELECT COUNT(*) FROM `$t`", null).use { c -> c.moveToFirst(); c.getLong(0) }
            }
        } finally {
            raw.close()
        }
    }

    private fun readUserVersion(file: File): Int {
        val raw = SQLiteDatabase.openDatabase(file.path, null, SQLiteDatabase.OPEN_READONLY)
        try {
            return raw.version
        } finally {
            raw.close()
        }
    }

    private fun rowIds(db: SupportSQLiteDatabase, table: String): List<Long> =
        db.query("SELECT id FROM `$table` ORDER BY id").use { c ->
            buildList { while (c.moveToNext()) add(c.getLong(0)) }
        }

    private fun readExpectedCounts(): Map<String, Long>? {
        if (testContext.assets.list(FIXTURE_DIR)?.contains(COUNTS_NAME) != true) return null
        return testContext.assets.open("$FIXTURE_DIR/$COUNTS_NAME").bufferedReader().useLines { lines ->
            lines.filter { it.isNotBlank() }.associate {
                val (table, count) = it.split('\t')
                table to count.trim().toLong()
            }
        }
    }

    private companion object {
        const val DB_NAME = "baseline-test.db"
        const val FIXTURE_COPY = "baseline-fixture-copy.db"
        const val FIXTURE_DIR = "fixtures"
        const val FIXTURE_NAME = "dairum_scrubbed.db"
        const val COUNTS_NAME = "dairum_scrubbed.counts.tsv"
        const val CURRENT_VERSION = 5 // keep in step with @Database(version = ...)
    }
}
