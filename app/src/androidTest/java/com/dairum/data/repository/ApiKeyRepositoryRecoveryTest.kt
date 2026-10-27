package com.dairum.data.repository

import android.content.Context
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dairum.testing.InMemoryDatabaseRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.security.KeyStore
import java.util.zip.ZipFile

/**
 * Uses its own prefs files and Keystore aliases so it never touches the keys of the app under test.
 */
@RunWith(AndroidJUnit4::class)
class ApiKeyRepositoryRecoveryTest {

    @get:Rule
    val dbRule = InMemoryDatabaseRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val file = "enc_keys_recovery_test"
    private val state = "enc_keys_recovery_state"
    private val alias = "dairum_recovery_test_master"
    private val foreignAlias = "dairum_recovery_test_foreign"
    private val foreignFile = "enc_keys_recovery_foreign"

    private fun repo() = ApiKeyRepositoryImpl(context, file, state, alias)

    @Before
    fun clean() = wipe()

    @After
    fun tearDown() = wipe()

    private fun wipe() {
        listOf(file, state, foreignFile).forEach { context.deleteSharedPreferences(it) }
        val ks = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        listOf(alias, foreignAlias).forEach { if (ks.containsAlias(it)) ks.deleteEntry(it) }
    }

    /** An encrypted prefs file whose keyset belongs to a Keystore key this "install" does not have. */
    private fun plantFileEncryptedWithAnotherKey() {
        val foreignKey = MasterKey.Builder(context, foreignAlias).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        EncryptedSharedPreferences.create(
            context, foreignFile, foreignKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ).edit().putString("weather_api_key", "SECRET-FROM-ANOTHER-INSTALL").commit()
        val prefsDir = File(context.applicationInfo.dataDir, "shared_prefs")
        File(prefsDir, "$foreignFile.xml").copyTo(File(prefsDir, "$file.xml"), overwrite = true)
    }

    @Test
    fun unreadableFile_isResetInsteadOfCrashing_andTheUserIsToldOnce() = runBlocking {
        plantFileEncryptedWithAnotherKey()

        val repo = repo() // used to throw AEADBadTagException here

        assertNull("the unreadable key must not leak or survive", repo.getWeatherApiKey().first())
        assertTrue("first read of the notice", repo.consumeKeysResetNotice())
        assertFalse("the notice is one-time", repo.consumeKeysResetNotice())

        repo.setWeatherApiKey("fresh-key-123")
        assertEquals("fresh-key-123", repo.getWeatherApiKey().first())
        // A new instance (like the next app start) reads the recreated store without another reset.
        val next = repo()
        assertEquals("fresh-key-123", next.getWeatherApiKey().first())
        assertFalse(next.consumeKeysResetNotice())
    }

    @Test
    fun healthyFile_isKeptAndNoNoticeIsShown() = runBlocking {
        val first = repo()
        first.setWeatherApiKey("keep-me-456")

        val second = repo()

        assertEquals("keep-me-456", second.getWeatherApiKey().first())
        assertFalse(second.consumeKeysResetNotice())
    }

    // ---- backups never carry the encrypted key file -------------------------------------------

    @Test
    fun backupPredicate_excludesTheEncryptedFileAndItsNoticeFlagOnly() {
        assertTrue(BackupRepositoryImpl.isExcludedPreferenceFile("encrypted_api_keys.xml"))
        assertTrue(BackupRepositoryImpl.isExcludedPreferenceFile("api_key_state.xml"))
        assertFalse(BackupRepositoryImpl.isExcludedPreferenceFile("widget_today_prefs.xml"))
        assertFalse(BackupRepositoryImpl.isExcludedPreferenceFile("encrypted_api_keys_backup.xml"))
    }

    @Test
    fun inAppBackupZip_hasNoEncryptedKeyFile_butKeepsOtherPreferences() = runBlocking {
        // Make sure the real encrypted file exists (creating it is harmless), plus an unrelated prefs file.
        ApiKeyRepositoryImpl(context)
        context.getSharedPreferences("backup_test_other_prefs", Context.MODE_PRIVATE).edit().putString("k", "v").commit()
        val outDir = File(context.cacheDir, "backup-test").apply { deleteRecursively(); mkdirs() }
        try {
            val backups = BackupRepositoryImpl(context, dbRule.db, Moshi.Builder().add(KotlinJsonAdapterFactory()).build())
            val result = backups.createBackup(destinationPath = outDir.absolutePath, includeMedia = false)
            assertTrue("backup failed: ${result.exceptionOrNull()}", result.isSuccess)

            val zip = ZipFile(File(result.getOrThrow().filePath))
            val names = zip.entries().toList().map { it.name }
            zip.close()
            assertFalse("encrypted key file must not be in the ZIP: $names", names.any { it.endsWith("encrypted_api_keys.xml") })
            assertFalse(names.any { it.endsWith("api_key_state.xml") })
            assertTrue("other preferences are still backed up", names.contains("config/preferences/backup_test_other_prefs.xml"))
        } finally {
            outDir.deleteRecursively()
            context.deleteSharedPreferences("backup_test_other_prefs")
        }
    }
}
