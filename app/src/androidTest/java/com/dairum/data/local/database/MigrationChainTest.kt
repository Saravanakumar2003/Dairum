package com.dairum.data.local.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.testing.MigrationTestHelper
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Scaffold for migration tests. Schema JSONs in app/schemas are packaged as androidTest assets
 * (see app/build.gradle.kts), which is where MigrationTestHelper reads them from.
 *
 * Caveat: those JSONs are regenerated on every build, so version N.json reflects the entities as of
 * the last build, not necessarily what shipped as version N. This test therefore answers "does the
 * migration chain in Migrations.kt produce a schema that matches the current entities", not "does
 * it upgrade the database that is on the phone". BaselineDatabaseTest covers the latter.
 */
@RunWith(AndroidJUnit4::class)
class MigrationChainTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        DairumDatabase::class.java
    )

    // Ignored: 1.json to 4.json were regenerated from today's entities, so the "version 1" database
    // MigrationTestHelper builds already has columns that migration 1->2 adds (it fails with
    // "duplicate column name: deletedAt"). That says nothing about real databases. The real
    // migration test arrives with the 5->6 migration and starts from the frozen 5.json baseline.
    @Ignore("1-4.json were regenerated from current entities; real test starts from frozen 5.json with the 5->6 migration")
    @Test
    fun migrateFromVersion1_matchesCurrentSchema() {
        helper.createDatabase(TEST_DB, 1).close()
        helper.runMigrationsAndValidate(TEST_DB, 5, true, *Migrations.ALL_MIGRATIONS).close()
    }

    private companion object {
        const val TEST_DB = "migration-chain-test.db"
    }
}
