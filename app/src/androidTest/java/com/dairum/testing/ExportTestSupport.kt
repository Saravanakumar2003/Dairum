package com.dairum.testing

import android.content.Context
import android.net.Uri
import com.dairum.data.local.database.DairumDatabase
import com.dairum.data.repository.ExportRepositoryImpl
import java.io.File

/** Builds the exporter over an in-memory database and reads/cleans up what it wrote. */
object ExportTestSupport {

    fun exportRepo(db: DairumDatabase, context: Context) = ExportRepositoryImpl(
        db.exportDao(), db.categoryDao(), db.tagDao(), db.personDao(), db.trackerDao(),
        db.mediaDao(), db.memoryDao(), db.notificationDao(), db.gratiaDao(), context
    )

    /** The exporters return "<path or content:// uri>|<size>". */
    private fun location(result: String): String = result.substringBeforeLast('|')

    fun read(context: Context, result: String): String {
        val loc = location(result)
        return if (loc.startsWith("content://")) {
            context.contentResolver.openInputStream(Uri.parse(loc))!!.use { it.readBytes().toString(Charsets.UTF_8) }
        } else {
            File(loc).readText()
        }
    }

    fun delete(context: Context, result: String) {
        val loc = location(result)
        if (loc.startsWith("content://")) context.contentResolver.delete(Uri.parse(loc), null, null) else File(loc).delete()
    }
}
