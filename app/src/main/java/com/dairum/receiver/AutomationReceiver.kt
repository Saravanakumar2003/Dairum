package com.dairum.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.repository.BackupRepository
import com.dairum.domain.repository.ExportRepository
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.SettingsRepository
import com.dairum.domain.repository.TaskerPeriod
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * BroadcastReceiver to handle automation intents from Tasker, MacroDroid, etc.
 * 
 * Supported Actions:
 * - com.dairum.action.EXPORT: Trigger a journal export
 * - com.dairum.action.BACKUP: Create a full backup
 * 
 * Export Extras:
 * - format (String): "txt" or "pdf"
 * - period (String): "all", "day", "week", "month"
 * - include_media (Boolean): Include photos in PDF
 * - start_date (Long): Custom start timestamp
 * - end_date (Long): Custom end timestamp
 * 
 * Backup Extras:
 * - include_media (Boolean): Include media files
 */
@AndroidEntryPoint
class AutomationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var exportRepository: ExportRepository

    @Inject
    lateinit var journalRepository: JournalRepository

    @Inject
    lateinit var backupRepository: BackupRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Coroutine exception in AutomationReceiver", throwable)
    }
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        
        scope.launch {
            try {
                // Check if dependencies are properly initialized
                if (!::settingsRepository.isInitialized || 
                    !::exportRepository.isInitialized ||
                    !::journalRepository.isInitialized ||
                    !::backupRepository.isInitialized) {
                    Log.e(TAG, "Dependencies not initialized")
                    sendResultBroadcast(context, false, "App not properly initialized")
                    pendingResult.finish()
                    return@launch
                }
                
                // Check if automation is enabled
                val isEnabled = settingsRepository.getTaskerEnabledFlow().first()
                if (!isEnabled) {
                    sendResultBroadcast(context, false, "Automation is disabled in Diarum settings")
                    pendingResult.finish()
                    return@launch
                }
                
                Log.d(TAG, "Processing action: ${intent.action}")
                
                when (intent.action) {
                    ACTION_EXPORT -> handleExport(context, intent)
                    ACTION_BACKUP -> handleBackup(context, intent)
                    else -> sendResultBroadcast(context, false, "Unknown action: ${intent.action}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing automation request", e)
                sendResultBroadcast(context, false, "Error: ${e.message}")
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun handleExport(context: Context, intent: Intent) {
        // Get parameters with defaults from settings
        val defaultFormat = settingsRepository.getTaskerDefaultFormatFlow().first()
        val defaultPeriod = settingsRepository.getTaskerDefaultPeriodFlow().first()
        val defaultIncludeMedia = settingsRepository.getTaskerDefaultIncludeMediaFlow().first()
        
        val formatStr = intent.getStringExtra(EXTRA_FORMAT) ?: defaultFormat.name.lowercase()
        val periodStr = intent.getStringExtra(EXTRA_PERIOD) ?: defaultPeriod.value
        val includeMedia = intent.getBooleanExtra(EXTRA_INCLUDE_MEDIA, defaultIncludeMedia)
        val customStartDate = if (intent.hasExtra(EXTRA_START_DATE)) {
            intent.getLongExtra(EXTRA_START_DATE, 0L)
        } else null
        val customEndDate = if (intent.hasExtra(EXTRA_END_DATE)) {
            intent.getLongExtra(EXTRA_END_DATE, 0L)
        } else null
        
        // Parse format
        val format = when (formatStr.lowercase()) {
            "txt", "text" -> ExportFormat.TXT
            "pdf" -> ExportFormat.PDF
            else -> {
                sendResultBroadcast(context, false, "Invalid format: $formatStr")
                return
            }
        }
        
        // Calculate date range
        val (startDate, endDate) = if (customStartDate != null || customEndDate != null) {
            customStartDate to customEndDate
        } else {
            calculateDateRange(periodStr)
        }
        
        // Get entries
        val entries = if (startDate != null && endDate != null) {
            journalRepository.getEntriesInDateRangeWithDetails(startDate, endDate)
        } else {
            journalRepository.getAllEntries().first()
        }
        
        if (entries.isEmpty()) {
            sendResultBroadcast(context, false, "No entries found for the specified period")
            return
        }
        
        // Perform export
        val result = exportRepository.exportEntries(
            entries = entries,
            format = format,
            includeMedia = includeMedia
        )
        
        result.fold(
            onSuccess = { exportHistory ->
                sendResultBroadcast(
                    context, 
                    true, 
                    "Export complete: ${entries.size} entries to ${exportHistory.fileName}",
                    mapOf(
                        "file_path" to exportHistory.filePath,
                        "file_name" to exportHistory.fileName,
                        "entry_count" to entries.size.toString()
                    )
                )
            },
            onFailure = { e ->
                sendResultBroadcast(context, false, "Export failed: ${e.message}")
            }
        )
    }

    private suspend fun handleBackup(context: Context, intent: Intent) {
        val includeMedia = intent.getBooleanExtra(EXTRA_INCLUDE_MEDIA, true)
        
        val result = backupRepository.createBackup(
            destinationPath = null,
            includeMedia = includeMedia
        )
        
        result.fold(
            onSuccess = { backupResult ->
                sendResultBroadcast(
                    context,
                    true,
                    "Backup complete: ${backupResult.entriesCount} entries saved",
                    mapOf(
                        "file_path" to backupResult.filePath,
                        "file_name" to backupResult.fileName,
                        "entry_count" to backupResult.entriesCount.toString(),
                        "media_count" to backupResult.mediaCount.toString()
                    )
                )
            },
            onFailure = { e ->
                sendResultBroadcast(context, false, "Backup failed: ${e.message}")
            }
        )
    }

    private fun calculateDateRange(periodStr: String): Pair<Long?, Long?> {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        
        return when (periodStr.lowercase()) {
            "all" -> null to null
            "day" -> {
                calendar.timeInMillis = now
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis to now
            }
            "week" -> {
                calendar.timeInMillis = now
                calendar.add(Calendar.WEEK_OF_YEAR, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis to now
            }
            "month" -> {
                calendar.timeInMillis = now
                calendar.add(Calendar.MONTH, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis to now
            }
            else -> null to null
        }
    }

    private fun sendResultBroadcast(
        context: Context,
        success: Boolean,
        message: String,
        extras: Map<String, String> = emptyMap()
    ) {
        val resultIntent = Intent(ACTION_RESULT).apply {
            putExtra(EXTRA_RESULT_SUCCESS, success)
            putExtra(EXTRA_RESULT_MESSAGE, message)
            extras.forEach { (key, value) ->
                putExtra("result_$key", value)
            }
            setPackage(context.packageName)
        }
        context.sendBroadcast(resultIntent)
        
        // Also send Tasker-compatible result
        if (success) {
            val taskerResultIntent = Intent("com.twofortyfouram.locale.intent.action.FIRE_SETTING")
            taskerResultIntent.putExtra("com.twofortyfouram.locale.intent.extra.BUNDLE", android.os.Bundle().apply {
                putString("message", message)
                putBoolean("success", true)
            })
            context.sendBroadcast(taskerResultIntent)
        }
    }

    companion object {
        private const val TAG = "AutomationReceiver"
        const val ACTION_EXPORT = "com.dairum.action.EXPORT"
        const val ACTION_BACKUP = "com.dairum.action.BACKUP"
        const val ACTION_RESULT = "com.dairum.action.RESULT"
        
        const val EXTRA_FORMAT = "format"
        const val EXTRA_PERIOD = "period"
        const val EXTRA_INCLUDE_MEDIA = "include_media"
        const val EXTRA_START_DATE = "start_date"
        const val EXTRA_END_DATE = "end_date"
        
        const val EXTRA_RESULT_SUCCESS = "result_success"
        const val EXTRA_RESULT_MESSAGE = "result_message"
    }
}
