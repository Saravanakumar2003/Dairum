package com.dairum.data.local.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.testing.MigrationTestHelper
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

    @Test
    fun migrateFromVersion1_matchesCurrentSchema() {
        helper.createDatabase(TEST_DB, 1).close()
        helper.runMigrationsAndValidate(TEST_DB, 5, true, *Migrations.ALL_MIGRATIONS).close()
    }

    private companion object {
        const val TEST_DB = "migration-chain-test.db"
    }
}
