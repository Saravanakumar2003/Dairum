package com.dairum.testing

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.dairum.data.local.database.DairumDatabase
import org.junit.rules.ExternalResource

/** A fresh in-memory [DairumDatabase] per test. Foreign keys are enforced, as in production. */
class InMemoryDatabaseRule : ExternalResource() {
    lateinit var db: DairumDatabase
        private set

    override fun before() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, DairumDatabase::class.java).build()
    }

    override fun after() {
        db.close()
    }
}
