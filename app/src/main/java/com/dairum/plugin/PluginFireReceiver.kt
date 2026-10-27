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
            Log.d(TAG, "========================================")
            Log.d(TAG, "PluginFireReceiver.onReceive called")
            Log.d(TAG, "Action: ${intent.action}")
            Log.d(TAG, "Intent extras keys: ${intent.extras?.keySet()?.joinToString()}")
            
            // Log all extras for debugging
            intent.extras?.let { extras ->
                for (key in extras.keySet()) {
                    val value = extras.get(key)
                    Log.d(TAG, "  Extra: $key = $value (${value?.javaClass?.simpleName})")
                }
            }
            
            // Verify the intent action - support both standard and MacroDroid actions
            val action = intent.action
            if (action != ACTION_FIRE_SETTING && action != MACRODROID_ACTION_FIRE) {
                Log.d(TAG, "Ignoring unknown action: $action")
                return
            }
            
            Log.d(TAG, "Action recognized, proceeding with export...")
            
            // Try to get the bundle with our configuration
            // First try the standard Locale/Tasker bundle extra
            var bundle = intent.getBundleExtra(PluginEditActivity.EXTRA_BUNDLE)
            Log.d(TAG, "Bundle from EXTRA_BUNDLE: ${bundle != null}")
            
            // If no bundle, try to extract values directly from intent extras (MacroDroid compatibility)
            if (bundle == null) {
                Log.d(TAG, "No bundle found, checking direct extras for MacroDroid compatibility")
                val extras = intent.extras
                if (extras != null) {
                    // MacroDroid may send with different key formats - try multiple approaches
                    val hasAction = extras.containsKey(PluginEditActivity.BUNDLE_ACTION)
                    val hasNestedBundle = extras.get(PluginEditActivity.EXTRA_BUNDLE) is Bundle
                    
                    Log.d(TAG, "Has direct action key: $hasAction, Has nested bundle: $hasNestedBundle")
                    
                    if (hasAction) {
                        // Create a bundle from the direct extras
                        bundle = Bundle().apply {
                            putString(PluginEditActivity.BUNDLE_ACTION, extras.getString(PluginEditActivity.BUNDLE_ACTION))
                            putString(PluginEditActivity.BUNDLE_FORMAT, extras.getString(PluginEditActivity.BUNDLE_FORMAT, "pdf"))
                            putString(PluginEditActivity.BUNDLE_PERIOD, extras.getString(PluginEditActivity.BUNDLE_PERIOD, "all"))
                            putBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, extras.getBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, true))
                            extras.getString(PluginEditActivity.BUNDLE_EXPORT_PATH)?.let {
                                putString(PluginEditActivity.BUNDLE_EXPORT_PATH, it)
                            }
                        }
                        Log.d(TAG, "Created bundle from direct extras")
                    } else {
                        // Try to create a default export action if no config found
                        Log.d(TAG, "No action key found, creating default export bundle")
                        bundle = Bundle().apply {
                            putString(PluginEditActivity.BUNDLE_ACTION, PluginEditActivity.ACTION_EXPORT)
                            putString(PluginEditActivity.BUNDLE_FORMAT, "pdf")
                            putString(PluginEditActivity.BUNDLE_PERIOD, "all")
                            putBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, true)
                        }
                    }
                }
            }
            
            if (bundle == null) {
                Log.w(TAG, "Could not create bundle from intent - using default export config")
                // Create default bundle for export
                bundle = Bundle().apply {
                    putString(PluginEditActivity.BUNDLE_ACTION, PluginEditActivity.ACTION_EXPORT)
                    putString(PluginEditActivity.BUNDLE_FORMAT, "pdf")
                    putString(PluginEditActivity.BUNDLE_PERIOD, "all")
                    putBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, true)
                }
            }
            
            // Validate the bundle
            if (!isBundleValid(bundle)) {
                Log.w(TAG, "Invalid bundle - missing required keys")
                return
            }
            
            val pendingResult = goAsync()
            
            scope.launch {
                try {
                    // Check if dependencies are properly initialized
                    if (!::settingsRepository.isInitialized || 
                        !::exportRepository.isInitialized ||
                        !::journalRepository.isInitialized ||
                        !::backupRepository.isInitialized) {
                        Log.e(TAG, "Dependencies not initialized")
                        pendingResult.finish()
                        return@launch
                    }
                    
                    // Check if automation is enabled in settings
                    val isEnabled = settingsRepository.getTaskerEnabledFlow().first()
                    if (!isEnabled) {
                        Log.d(TAG, "Automation is disabled in settings")
                        pendingResult.finish()
                        return@launch
                    }
                    
                    val actionType = bundle.getString(PluginEditActivity.BUNDLE_ACTION) 
                        ?: PluginEditActivity.ACTION_EXPORT
                    
                    Log.d(TAG, "Executing action: $actionType")
                    
                    when (actionType) {
                        PluginEditActivity.ACTION_EXPORT -> handleExport(bundle)
                        PluginEditActivity.ACTION_BACKUP -> handleBackup(bundle)
                        else -> Log.w(TAG, "Unknown action: $actionType")
                    }
                    
                    Log.d(TAG, "Action completed successfully")
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
    
    private fun isBundleValid(bundle: Bundle): Boolean {
        // Check that required keys exist
        val hasAction = bundle.containsKey(PluginEditActivity.BUNDLE_ACTION)
        Log.d(TAG, "Bundle validation - has action key: $hasAction, keys: ${bundle.keySet()}")
        return hasAction
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
