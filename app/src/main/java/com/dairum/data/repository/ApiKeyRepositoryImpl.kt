package com.dairum.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.dairum.domain.repository.ApiKeyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyStore
import javax.inject.Inject
import javax.inject.Singleton

private val Context.apiKeyDataStore: DataStore<Preferences> by preferencesDataStore(name = "api_keys")

@Singleton
class ApiKeyRepositoryImpl internal constructor(
    private val context: Context,
    private val fileName: String,
    private val stateFileName: String,
    private val masterKeyAlias: String
) : ApiKeyRepository {

    /** Production constructor; the internal one lets tests use their own files and Keystore alias. */
    @Inject
    constructor(@ApplicationContext context: Context) :
        this(context, FILE_NAME, STATE_FILE_NAME, MasterKey.DEFAULT_MASTER_KEY_ALIAS)

    private val encryptedPrefs: SharedPreferences = openEncryptedPrefs()

    /**
     * Opens the encrypted key store. If the file exists but cannot be decrypted (a restored backup, a
     * reinstall or a new phone: the Android Keystore master key never travels, which surfaces as
     * AEADBadTagException and friends), the file is deleted and recreated empty instead of crashing
     * every screen that needs this repository. The user is told once to re-enter their keys.
     */
    private fun openEncryptedPrefs(): SharedPreferences {
        try {
            return createEncryptedPrefs()
        } catch (e: Exception) { // GeneralSecurityException, IOException, Tink/protobuf runtime errors
            Timber.w(e, "Encrypted API-key preferences are unreadable; resetting them")
        }
        context.deleteSharedPreferences(fileName)
        stateStore().edit().putBoolean(KEY_RESET_NOTICE_PENDING, true).commit()
        return try {
            createEncryptedPrefs()
        } catch (e: Exception) {
            // The master key itself may be unusable: drop it too and start from nothing.
            Timber.w(e, "Master key unusable; deleting it and starting over")
            deleteMasterKey()
            context.deleteSharedPreferences(fileName)
            createEncryptedPrefs()
        }
    }

    private fun createEncryptedPrefs(): SharedPreferences {
        val masterKey = MasterKey.Builder(context, masterKeyAlias)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            fileName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun deleteMasterKey() {
        try {
            KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
                .deleteEntry(masterKeyAlias)
        } catch (e: Exception) {
            Timber.w(e, "Could not delete the master key")
        }
    }

    /** Plain (not encrypted) flag file: it holds no secrets and must survive a key reset. */
    private fun stateStore(): SharedPreferences =
        context.getSharedPreferences(stateFileName, Context.MODE_PRIVATE)

    override suspend fun consumeKeysResetNotice(): Boolean = withContext(Dispatchers.IO) {
        val pending = stateStore().getBoolean(KEY_RESET_NOTICE_PENDING, false)
        if (pending) stateStore().edit().putBoolean(KEY_RESET_NOTICE_PENDING, false).commit()
        pending
    }

    companion object {
        /** File names under shared_prefs/ (without ".xml"); both are excluded from every backup. */
        const val FILE_NAME = "encrypted_api_keys"
        const val STATE_FILE_NAME = "api_key_state"
        private const val KEY_RESET_NOTICE_PENDING = "reset_notice_pending"
        private const val KEY_GOOGLE_MAPS = "google_maps_api_key"
        private const val KEY_WEATHER = "weather_api_key"
        private const val KEY_GOOGLE_CALENDAR = "google_calendar_api_key"
    }

    // ============ GOOGLE MAPS ============

    override fun getGoogleMapsApiKey(): Flow<String?> = flow {
        emit(encryptedPrefs.getString(KEY_GOOGLE_MAPS, null))
    }

    override suspend fun setGoogleMapsApiKey(key: String?) {
        withContext(Dispatchers.IO) {
            if (key.isNullOrBlank()) {
                encryptedPrefs.edit().remove(KEY_GOOGLE_MAPS).apply()
            } else {
                encryptedPrefs.edit().putString(KEY_GOOGLE_MAPS, key).apply()
            }
        }
    }

    override suspend fun validateGoogleMapsKey(key: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Simple validation - check if key format looks correct
                // Real validation would require a test API call
                key.length >= 30 && key.startsWith("AIza")
            } catch (e: Exception) {
                false
            }
        }
    }

    // ============ WEATHER API ============

    override fun getWeatherApiKey(): Flow<String?> = flow {
        emit(encryptedPrefs.getString(KEY_WEATHER, null))
    }

    override suspend fun setWeatherApiKey(key: String?) {
        withContext(Dispatchers.IO) {
            if (key.isNullOrBlank()) {
                encryptedPrefs.edit().remove(KEY_WEATHER).apply()
            } else {
                encryptedPrefs.edit().putString(KEY_WEATHER, key).apply()
            }
        }
    }

    override suspend fun validateWeatherKey(key: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Test the weather API key with a simple request
                val url = URL("https://api.weatherapi.com/v1/current.json?key=$key&q=London")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                
                val responseCode = connection.responseCode
                connection.disconnect()
                
                responseCode == HttpURLConnection.HTTP_OK
            } catch (e: Exception) {
                // If key looks valid format-wise, consider it potentially valid
                key.length >= 20
            }
        }
    }

    // ============ GOOGLE CALENDAR ============

    override fun getGoogleCalendarApiKey(): Flow<String?> = flow {
        emit(encryptedPrefs.getString(KEY_GOOGLE_CALENDAR, null))
    }

    override suspend fun setGoogleCalendarApiKey(key: String?) {
        withContext(Dispatchers.IO) {
            if (key.isNullOrBlank()) {
                encryptedPrefs.edit().remove(KEY_GOOGLE_CALENDAR).apply()
            } else {
                encryptedPrefs.edit().putString(KEY_GOOGLE_CALENDAR, key).apply()
            }
        }
    }

    override suspend fun validateGoogleCalendarKey(key: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // OAuth client ID format validation
                key.contains(".apps.googleusercontent.com") || 
                (key.length >= 30 && key.startsWith("AIza"))
            } catch (e: Exception) {
                false
            }
        }
    }
}
