package com.dairum.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.repository.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLORS = booleanPreferencesKey("dynamic_colors")
        val DEFAULT_CATEGORY_ID = longPreferencesKey("default_category_id")
        val AUTO_FETCH_WEATHER = booleanPreferencesKey("auto_fetch_weather")
        val AUTO_FETCH_LOCATION = booleanPreferencesKey("auto_fetch_location")
        val TIME_FORMAT = stringPreferencesKey("time_format")
        val DATE_FORMAT = stringPreferencesKey("date_format")
        val WEIGHT_UNIT = stringPreferencesKey("weight_unit")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperature_unit")
        val APP_LOCK_ENABLED = booleanPreferencesKey("app_lock_enabled")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val PIN_HASH = stringPreferencesKey("pin_hash")
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        // Tasker/Automation settings
        val TASKER_ENABLED = booleanPreferencesKey("tasker_enabled")
        val TASKER_DEFAULT_FORMAT = stringPreferencesKey("tasker_default_format")
        val TASKER_DEFAULT_PERIOD = stringPreferencesKey("tasker_default_period")
        val TASKER_DEFAULT_INCLUDE_MEDIA = booleanPreferencesKey("tasker_default_include_media")
        val EXPORT_KITH_INCLUDE_TITLES = booleanPreferencesKey("export_kith_include_titles")
    }

    private val dataStore = context.dataStore

    override fun getThemeModeFlow(): Flow<ThemeMode> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                val value = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
                ThemeMode.valueOf(value)
            }
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    override fun getDynamicColorsFlow(): Flow<Boolean> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.DYNAMIC_COLORS] ?: true
            }
    }

    override suspend fun setDynamicColors(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_COLORS] = enabled
        }
    }

    override fun getDefaultCategoryIdFlow(): Flow<Long?> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.DEFAULT_CATEGORY_ID]
            }
    }

    override suspend fun setDefaultCategoryId(id: Long?) {
        dataStore.edit { preferences ->
            if (id != null) {
                preferences[PreferencesKeys.DEFAULT_CATEGORY_ID] = id
            } else {
                preferences.remove(PreferencesKeys.DEFAULT_CATEGORY_ID)
            }
        }
    }

    override fun getWeatherAutoFetchFlow(): Flow<Boolean> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.AUTO_FETCH_WEATHER] ?: true
            }
    }

    override suspend fun setWeatherAutoFetch(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_FETCH_WEATHER] = enabled
        }
    }

    override fun getLocationAutoFetchFlow(): Flow<Boolean> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.AUTO_FETCH_LOCATION] ?: false
            }
    }

    override suspend fun setLocationAutoFetch(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_FETCH_LOCATION] = enabled
        }
    }

    override fun getTimeFormat(): Flow<TimeFormat> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                val value = preferences[PreferencesKeys.TIME_FORMAT] ?: TimeFormat.TWELVE_HOUR.name
                TimeFormat.valueOf(value)
            }
    }

    override suspend fun setTimeFormat(format: TimeFormat) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIME_FORMAT] = format.name
        }
    }

    override fun getDateFormat(): Flow<DateFormat> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                val value = preferences[PreferencesKeys.DATE_FORMAT] ?: DateFormat.MDY.name
                DateFormat.valueOf(value)
            }
    }

    override suspend fun setDateFormat(format: DateFormat) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DATE_FORMAT] = format.name
        }
    }

    override fun getWeightUnit(): Flow<WeightUnit> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                val value = preferences[PreferencesKeys.WEIGHT_UNIT] ?: WeightUnit.KG.name
                WeightUnit.valueOf(value)
            }
    }

    override suspend fun setWeightUnit(unit: WeightUnit) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.WEIGHT_UNIT] = unit.name
        }
    }

    override fun getTemperatureUnit(): Flow<TemperatureUnit> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                val value = preferences[PreferencesKeys.TEMPERATURE_UNIT] ?: TemperatureUnit.CELSIUS.name
                TemperatureUnit.valueOf(value)
            }
    }

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TEMPERATURE_UNIT] = unit.name
        }
    }

    override fun getAppLockEnabledFlow(): Flow<Boolean> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.APP_LOCK_ENABLED] ?: false
            }
    }

    override suspend fun setAppLockEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_LOCK_ENABLED] = enabled
        }
    }

    override fun getBiometricEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.BIOMETRIC_ENABLED] ?: false
            }
    }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.BIOMETRIC_ENABLED] = enabled
        }
    }

    override fun getPinHash(): Flow<String?> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.PIN_HASH]
            }
    }

    override suspend fun setPinHash(hash: String?) {
        dataStore.edit { preferences ->
            if (hash != null) {
                preferences[PreferencesKeys.PIN_HASH] = hash
            } else {
                preferences.remove(PreferencesKeys.PIN_HASH)
            }
        }
    }

    override fun isFirstLaunch(): Flow<Boolean> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.FIRST_LAUNCH] ?: true
            }
    }

    override suspend fun setFirstLaunchComplete() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_LAUNCH] = false
        }
    }
    
    // ============ TASKER/AUTOMATION SETTINGS ============
    
    override fun getTaskerEnabledFlow(): Flow<Boolean> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.TASKER_ENABLED] ?: false
            }
    }

    override suspend fun setTaskerEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TASKER_ENABLED] = enabled
        }
    }

    override fun getTaskerDefaultFormatFlow(): Flow<ExportFormat> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                val value = preferences[PreferencesKeys.TASKER_DEFAULT_FORMAT] ?: ExportFormat.PDF.name
                try {
                    ExportFormat.valueOf(value)
                } catch (e: Exception) {
                    ExportFormat.PDF
                }
            }
    }

    override suspend fun setTaskerDefaultFormat(format: ExportFormat) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TASKER_DEFAULT_FORMAT] = format.name
        }
    }

    override fun getTaskerDefaultPeriodFlow(): Flow<TaskerPeriod> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                val value = preferences[PreferencesKeys.TASKER_DEFAULT_PERIOD] ?: TaskerPeriod.ALL.name
                try {
                    TaskerPeriod.valueOf(value)
                } catch (e: Exception) {
                    TaskerPeriod.ALL
                }
            }
    }

    override suspend fun setTaskerDefaultPeriod(period: TaskerPeriod) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TASKER_DEFAULT_PERIOD] = period.name
        }
    }

    override fun getTaskerDefaultIncludeMediaFlow(): Flow<Boolean> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences ->
                preferences[PreferencesKeys.TASKER_DEFAULT_INCLUDE_MEDIA] ?: true
            }
    }

    override suspend fun setTaskerDefaultIncludeMedia(include: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TASKER_DEFAULT_INCLUDE_MEDIA] = include
        }
    }

    override fun getKithIncludeTitlesFlow(): Flow<Boolean> {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { preferences -> preferences[PreferencesKeys.EXPORT_KITH_INCLUDE_TITLES] ?: true }
    }

    override suspend fun setKithIncludeTitles(include: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.EXPORT_KITH_INCLUDE_TITLES] = include
        }
    }
}
