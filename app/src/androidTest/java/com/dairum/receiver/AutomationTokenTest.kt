package com.dairum.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dairum.data.repository.SettingsRepositoryImpl
import com.dairum.plugin.PluginEditActivity
import com.dairum.plugin.PluginFireReceiver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

/**
 * The Tasker/MacroDroid receivers are exported with no permission, so the automation token is the
 * only gate. These tests send real broadcasts to the real receivers (debug app, same process).
 * They write to the app's real settings store and restore it afterwards.
 */
@RunWith(AndroidJUnit4::class)
class AutomationTokenTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val settings = SettingsRepositoryImpl(context)
    private var wasEnabled = false
    private val createdFiles = mutableListOf<String>()
    private var baseline = emptySet<String>()

    // Unique so a leak into logcat or a file cannot be a coincidence.
    private val token = "tok-" + java.util.UUID.randomUUID().toString()

    @Before
    fun setUp() = runBlocking<Unit> {
        wasEnabled = settings.getTaskerEnabledFlow().first()
        settings.setTaskerEnabled(true)
        settings.clearAutomationToken()
        baseline = (metadataFileUris() + exportFileUris()).toSet()
    }

    @After
    fun tearDown() = runBlocking<Unit> {
        settings.clearAutomationToken()
        settings.setTaskerEnabled(wasEnabled)
        createdFiles.forEach { runCatching { context.contentResolver.delete(Uri.parse(it), null, null) } }
        newFiles().forEach { runCatching { context.contentResolver.delete(Uri.parse(it), null, null) } }
    }

    // ---------- the stored token ----------

    @Test
    fun token_is_hashed_verified_and_cleared() = runBlocking<Unit> {
        assertFalse(settings.isAutomationTokenSet())
        assertFalse("nothing set: every candidate fails", settings.verifyAutomationToken(token))
        assertFalse(settings.verifyAutomationToken(null))

        settings.setAutomationToken(token)
        assertTrue(settings.isAutomationTokenSet())
        assertTrue(settings.verifyAutomationToken(token))
        assertFalse(settings.verifyAutomationToken(token + "x"))
        assertFalse(settings.verifyAutomationToken(""))
        assertFalse(settings.verifyAutomationToken(null))

        // Only a hash is stored: the plaintext is nowhere in the settings file.
        val dsFile = java.io.File(context.filesDir, "datastore/settings.preferences_pb")
        assertTrue(dsFile.exists())
        assertFalse(String(dsFile.readBytes(), Charsets.ISO_8859_1).contains(token))

        settings.clearAutomationToken()
        assertFalse(settings.isAutomationTokenSet())
        assertFalse(settings.verifyAutomationToken(token))
    }

    @Test
    fun short_token_is_refused() = runBlocking<Unit> {
        val failed = runCatching { settings.setAutomationToken("short") }.isFailure
        assertTrue(failed)
        assertFalse(settings.isAutomationTokenSet())
    }

    // ---------- AutomationReceiver (intent actions) ----------

    @Test
    fun intent_rejected_while_no_token_is_set() {
        val result = sendIntentAndAwaitResult(Intent(AutomationReceiver.ACTION_METADATA).putExtra("token", token))
        assertEquals(false, result.getBoolean(AutomationReceiver.EXTRA_RESULT_SUCCESS))
        assertTrue(result.getString(AutomationReceiver.EXTRA_RESULT_MESSAGE)!!.startsWith("Unauthorized"))
        assertNull(result.getString("result_file_path"))
    }

    @Test
    fun intent_rejected_for_missing_or_wrong_token_for_every_action() = runBlocking<Unit> {
        settings.setAutomationToken(token)
        for (action in listOf(AutomationReceiver.ACTION_METADATA, AutomationReceiver.ACTION_EXPORT, AutomationReceiver.ACTION_BACKUP)) {
            for (extra in listOf<String?>(null, "", token + "x", "wrong-token-123")) {
                val intent = Intent(action)
                if (extra != null) intent.putExtra("token", extra)
                val result = sendIntentAndAwaitResult(intent)
                assertEquals("$action with token=$extra", false, result.getBoolean(AutomationReceiver.EXTRA_RESULT_SUCCESS))
                assertTrue(result.getString(AutomationReceiver.EXTRA_RESULT_MESSAGE)!!.startsWith("Unauthorized"))
                assertNull("no work was done", result.getString("result_file_path"))
            }
        }
        assertTrue("no file was written", newFiles().isEmpty())
    }

    @Test
    fun intent_metadata_with_correct_token_writes_the_file() = runBlocking<Unit> {
        settings.setAutomationToken(token)
        val result = sendIntentAndAwaitResult(
            Intent(AutomationReceiver.ACTION_METADATA).putExtra("token", token).putExtra("titles", false)
        )
        assertEquals(result.getString(AutomationReceiver.EXTRA_RESULT_MESSAGE), true, result.getBoolean(AutomationReceiver.EXTRA_RESULT_SUCCESS))
        val path = result.getString("result_file_path")
        assertNotNull(path)
        createdFiles += path!!
        val text = context.contentResolver.openInputStream(Uri.parse(path))!!.use { it.readBytes().toString(Charsets.UTF_8) }
        assertTrue(text.startsWith("{\"format\":\"dairum-metadata\""))
        assertTrue("titles=false is honoured", text.contains("\"titles\":false"))
        assertFalse("the token is not in the exported file", text.contains(token))
    }

    @Test
    fun intent_still_needs_automation_enabled() = runBlocking<Unit> {
        settings.setAutomationToken(token)
        settings.setTaskerEnabled(false)
        val result = sendIntentAndAwaitResult(Intent(AutomationReceiver.ACTION_METADATA).putExtra("token", token))
        assertEquals(false, result.getBoolean(AutomationReceiver.EXTRA_RESULT_SUCCESS))
        assertTrue(result.getString(AutomationReceiver.EXTRA_RESULT_MESSAGE)!!.contains("disabled"))
    }

    // ---------- PluginFireReceiver (Tasker/Locale/MacroDroid plugin) ----------

    @Test
    fun plugin_rejects_no_token_set_wrong_token_and_bare_intents() = runBlocking<Unit> {
        // 1. no token set at all
        sendPlugin(bundleOf(PluginEditActivity.ACTION_METADATA, token))
        // 2. token set, wrong / missing token
        settings.setAutomationToken(token)
        sendPlugin(bundleOf(PluginEditActivity.ACTION_METADATA, token + "x"))
        sendPlugin(bundleOf(PluginEditActivity.ACTION_METADATA, null))
        // 3. the old behaviour: a bare FIRE_SETTING intent ran a default PDF export
        context.sendBroadcast(Intent(PluginFireReceiver.ACTION_FIRE_SETTING).setPackage(context.packageName))
        // 4. direct extras (MacroDroid style) with a wrong token
        context.sendBroadcast(
            Intent(PluginFireReceiver.MACRODROID_ACTION_FIRE).setPackage(context.packageName)
                .putExtra(PluginEditActivity.BUNDLE_ACTION, PluginEditActivity.ACTION_METADATA)
                .putExtra(PluginEditActivity.BUNDLE_TOKEN, "nope-nope-nope")
        )
        Thread.sleep(3_000)
        assertTrue("no file written: ${newFiles()}", newFiles().isEmpty())
    }

    @Test
    fun plugin_metadata_with_correct_token_runs_and_token_is_never_logged() = runBlocking<Unit> {
        settings.setAutomationToken(token)
        // wrong token first, so the rejection path is in the log too
        sendPlugin(bundleOf(PluginEditActivity.ACTION_METADATA, token + "x"))
        sendPlugin(bundleOf(PluginEditActivity.ACTION_METADATA, token))
        // direct extras variant
        context.sendBroadcast(
            Intent(PluginFireReceiver.MACRODROID_ACTION_FIRE).setPackage(context.packageName)
                .putExtra(PluginEditActivity.BUNDLE_ACTION, PluginEditActivity.ACTION_METADATA)
                .putExtra(PluginEditActivity.BUNDLE_TOKEN, token)
        )
        // Intent actions too, so every code path that sees the token has logged by the time we read.
        sendIntentAndAwaitResult(Intent(AutomationReceiver.ACTION_METADATA).putExtra("token", token + "x"))

        val deadline = System.currentTimeMillis() + 20_000
        while (newFiles().size < 2 && System.currentTimeMillis() < deadline) Thread.sleep(250)
        assertTrue("correct token (bundle and direct extras) wrote files", newFiles().size >= 2)

        val log = Runtime.getRuntime().exec(arrayOf("logcat", "-d")).inputStream.bufferedReader().readText()
        assertTrue("sanity: logcat is readable and has our receivers' lines", log.contains("PluginFireReceiver") || log.contains("AutomationReceiver"))
        assertFalse("the token must never appear in logcat", log.contains(token))
    }

    // ---------- helpers ----------

    private fun bundleOf(action: String, token: String?) = Bundle().apply {
        putString(PluginEditActivity.BUNDLE_ACTION, action)
        if (token != null) putString(PluginEditActivity.BUNDLE_TOKEN, token)
    }

    private fun sendPlugin(bundle: Bundle) {
        context.sendBroadcast(
            Intent(PluginFireReceiver.ACTION_FIRE_SETTING).setPackage(context.packageName)
                .putExtra(PluginEditActivity.EXTRA_BUNDLE, bundle)
        )
    }

    /** Sends the intent to our own receiver and returns the result broadcast it answers with. */
    private fun sendIntentAndAwaitResult(intent: Intent): Bundle {
        val queue = LinkedBlockingQueue<Bundle>()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context, i: Intent) { queue.add(i.extras ?: Bundle()) }
        }
        ContextCompat.registerReceiver(
            context, receiver, IntentFilter(AutomationReceiver.ACTION_RESULT), ContextCompat.RECEIVER_NOT_EXPORTED
        )
        try {
            context.sendBroadcast(intent.setPackage(context.packageName))
            return queue.poll(30, TimeUnit.SECONDS) ?: error("no result broadcast within 30 s")
        } finally {
            context.unregisterReceiver(receiver)
        }
    }

    private fun metadataFileUris(): List<String> = ownFileUris("dairum_metadata_%")

    private fun exportFileUris(): List<String> = ownFileUris("diarum_export_%")

    private fun newFiles(): List<String> = (metadataFileUris() + exportFileUris()).filterNot { it in baseline }

    private fun ownFileUris(namePattern: String): List<String> {
        val out = mutableListOf<String>()
        val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        context.contentResolver.query(
            collection, arrayOf(MediaStore.Files.FileColumns._ID),
            "${MediaStore.Files.FileColumns.DISPLAY_NAME} LIKE ?", arrayOf(namePattern), null
        )?.use { c ->
            while (c.moveToNext()) out += android.content.ContentUris.withAppendedId(collection, c.getLong(0)).toString()
        }
        return out
    }
}
