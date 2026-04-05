package com.dairum.data.repository

import android.content.Context
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
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

private val Context.apiKeyDataStore: DataStore<Preferences> by preferencesDataStore(name = "api_keys")

@Singleton
class ApiKeyRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ApiKeyRepository {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "encrypted_api_keys",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
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
