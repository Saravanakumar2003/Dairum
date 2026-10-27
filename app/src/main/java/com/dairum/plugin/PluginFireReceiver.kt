package com.dairum.plugin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.repository.BackupRepository
import com.dairum.domain.repository.ExportRepository
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.SettingsRepository
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
 * Tasker/Locale/MacroDroid plugin fire receiver.
 * This is the BroadcastReceiver that gets triggered when the automation app
 * executes the configured Diarum action.
 */
@AndroidEntryPoint
class PluginFireReceiver : BroadcastReceiver() {
    
    private val mainHandler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var exportRepository: ExportRepository

    @Inject
    lateinit var journalRepository: JournalRepository

    @Inject
    lateinit var backupRepository: BackupRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Coroutine exception in PluginFireReceiver", throwable)
    }
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)
    
    // Store context for toast display
    private var appContext: Context? = null

    override fun onReceive(context: Context, intent: Intent) {
        appContext = context.applicationContext
        try {
            // Never log extras: they carry the automation token.
            val action = intent.action
            if (action != ACTION_FIRE_SETTING && action != MACRODROID_ACTION_FIRE) {
                Log.d(TAG, "Ignoring unknown action: $action")
                return
            }

            val bundle = bundleFrom(intent)
            if (bundle == null) {
                Log.w(TAG, "Rejected request: no configuration bundle with an action")
                return
            }

            val pendingResult = goAsync()

            scope.launch {
                try {
                    // Authenticate first: nothing else runs for an unauthenticated caller.
                    if (!settingsRepository.verifyAutomationToken(bundle.getString(PluginEditActivity.BUNDLE_TOKEN))) {
                        Log.w(TAG, "Rejected request: automation token missing, wrong or not set")
                        showToast("Diarum: automation request rejected (token)")
                        return@launch
                    }

                    // Check if automation is enabled in settings
                    val isEnabled = settingsRepository.getTaskerEnabledFlow().first()
                    if (!isEnabled) {
                        Log.d(TAG, "Automation is disabled in settings")
                        return@launch
                    }

                    val actionType = bundle.getString(PluginEditActivity.BUNDLE_ACTION)
                    Log.d(TAG, "Executing action: $actionType")

                    when (actionType) {
                        PluginEditActivity.ACTION_EXPORT -> handleExport(bundle)
                        PluginEditActivity.ACTION_BACKUP -> handleBackup(bundle)
                        PluginEditActivity.ACTION_METADATA -> handleMetadata(bundle)
                        else -> Log.w(TAG, "Unknown action: $actionType")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error executing automation action", e)
                } finally {
                    pendingResult.finish()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onReceive", e)
        }
    }

    /** The Locale/Tasker bundle, or (MacroDroid) the same keys as direct extras. Null without an action key. */
    private fun bundleFrom(intent: Intent): Bundle? {
        intent.getBundleExtra(PluginEditActivity.EXTRA_BUNDLE)?.let { b ->
            return b.takeIf { it.containsKey(PluginEditActivity.BUNDLE_ACTION) }
        }
        val extras = intent.extras ?: return null
        if (!extras.containsKey(PluginEditActivity.BUNDLE_ACTION)) return null
        return Bundle().apply {
            putString(PluginEditActivity.BUNDLE_ACTION, extras.getString(PluginEditActivity.BUNDLE_ACTION))
            putString(PluginEditActivity.BUNDLE_TOKEN, extras.getString(PluginEditActivity.BUNDLE_TOKEN))
            putString(PluginEditActivity.BUNDLE_FORMAT, extras.getString(PluginEditActivity.BUNDLE_FORMAT, "pdf"))
            putString(PluginEditActivity.BUNDLE_PERIOD, extras.getString(PluginEditActivity.BUNDLE_PERIOD, "all"))
            putBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, extras.getBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, true))
            if (extras.containsKey(PluginEditActivity.BUNDLE_TITLES)) {
                putBoolean(PluginEditActivity.BUNDLE_TITLES, extras.getBoolean(PluginEditActivity.BUNDLE_TITLES, true))
            }
            extras.getString(PluginEditActivity.BUNDLE_EXPORT_PATH)?.let {
                putString(PluginEditActivity.BUNDLE_EXPORT_PATH, it)
            }
        }
    }

    private suspend fun handleMetadata(bundle: Bundle) {
        val includeTitles = if (bundle.containsKey(PluginEditActivity.BUNDLE_TITLES)) {
            bundle.getBoolean(PluginEditActivity.BUNDLE_TITLES, true)
        } else {
            settingsRepository.getKithIncludeTitlesFlow().first()
        }
        exportRepository.exportKithMetadata(includeTitles).fold(
            onSuccess = { showToast("Metadata export complete: ${it.fileName}") },
            onFailure = { showToast("Metadata export failed: ${it.message}") }
        )
    }

    private suspend fun handleExport(bundle: Bundle) {
        Log.d(TAG, "handleExport started")
        
        val formatStr = bundle.getString(PluginEditActivity.BUNDLE_FORMAT) ?: "pdf"
        val periodStr = bundle.getString(PluginEditActivity.BUNDLE_PERIOD) ?: "all"
        val includeMedia = bundle.getBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, true)
        val customExportPath = bundle.getString(PluginEditActivity.BUNDLE_EXPORT_PATH)
        
        Log.d(TAG, "Export params - format: $formatStr, period: $periodStr, includeMedia: $includeMedia, customPath: $customExportPath")
        
        // Parse format
        val format = when (formatStr.lowercase()) {
            "txt", "text" -> ExportFormat.TXT
            "pdf" -> ExportFormat.PDF
            "json" -> ExportFormat.JSON
            "csv" -> ExportFormat.CSV
            else -> ExportFormat.PDF
        }
        
        // Calculate date range
        val (startDate, endDate) = calculateDateRange(periodStr)
        Log.d(TAG, "Date range - start: $startDate, end: $endDate")
        
        // Get entries
        try {
            val entries = if (startDate != null && endDate != null) {
                Log.d(TAG, "Getting entries in date range")
                journalRepository.getEntriesInDateRangeWithDetails(startDate, endDate)
            } else {
                Log.d(TAG, "Getting all entries")
                journalRepository.getAllEntries().first()
            }
            
            Log.d(TAG, "Found ${entries.size} entries to export")
            
            if (entries.isEmpty()) {
                Log.w(TAG, "No entries found for export")
                showToast("No journal entries to export")
                return
            }
            
            showToast("Exporting ${entries.size} entries to ${formatStr.uppercase()}...")
            
            // Perform export
            Log.d(TAG, "Starting export...")
            val result = exportRepository.exportEntries(
                entries = entries,
                format = format,
                includeMedia = includeMedia
            )
            
            result.fold(
                onSuccess = { history ->
                    Log.i(TAG, "Export successful: ${history.fileName} at ${history.filePath}")
                    
                    // If custom export path specified, copy the file there
                    if (!customExportPath.isNullOrBlank()) {
                        try {
                            val customDir = java.io.File(customExportPath)
                            if (!customDir.exists()) {
                                customDir.mkdirs()
                            }
                            val destFile = java.io.File(customDir, history.fileName)
                            
                            // Try to copy from the original export location
                            val sourcePath = history.filePath
                            if (sourcePath.startsWith("content://")) {
                                // File is a content URI, use ContentResolver
                                val sourceUri = android.net.Uri.parse(sourcePath)
                                appContext?.contentResolver?.openInputStream(sourceUri)?.use { input ->
                                    destFile.outputStream().use { output ->
                                        input.copyTo(output)
                                    }
                                }
                            } else {
                                // File is a direct path
                                val sourceFile = java.io.File(sourcePath)
                                if (sourceFile.exists()) {
                                    sourceFile.copyTo(destFile, overwrite = true)
                                }
                            }
                            Log.i(TAG, "File copied to custom path: ${destFile.absolutePath}")
                            showToast("Export complete: ${destFile.absolutePath}")
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to copy to custom path: ${e.message}", e)
                            showToast("Export done but copy to custom path failed: ${e.message}")
                        }
                    } else {
                        showToast("Export complete: ${history.fileName}")
                    }
                },
                onFailure = { error ->
                    Log.e(TAG, "Export failed", error)
                    showToast("Export failed: ${error.message}")
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error during export", e)
            showToast("Export error: ${e.message}")
        }
    }
    
    private fun showToast(message: String) {
        mainHandler.post {
            appContext?.let { ctx ->
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private suspend fun handleBackup(bundle: Bundle) {
        Log.d(TAG, "handleBackup started")
        
        val includeMedia = bundle.getBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, true)
        val customExportPath = bundle.getString(PluginEditActivity.BUNDLE_EXPORT_PATH)
        
        showToast("Starting backup...")
        
        try {
            backupRepository.createBackup(
                destinationPath = customExportPath,
                includeMedia = includeMedia
            )
            Log.i(TAG, "Backup completed successfully")
            val pathMsg = if (!customExportPath.isNullOrBlank()) " to $customExportPath" else ""
            showToast("Backup completed successfully$pathMsg")
        } catch (e: Exception) {
            Log.e(TAG, "Backup failed", e)
            showToast("Backup failed: ${e.message}")
        }
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

    companion object {
        private const val TAG = "PluginFireReceiver"
        // Standard Locale/Tasker fire action
        const val ACTION_FIRE_SETTING = "com.twofortyfouram.locale.intent.action.FIRE_SETTING"
        // MacroDroid fire action
        const val MACRODROID_ACTION_FIRE = "com.arlosoft.macrodroid.intent.action.FIRE_SETTING"
    }
}
