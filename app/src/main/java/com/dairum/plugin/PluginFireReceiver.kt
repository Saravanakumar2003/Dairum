package com.dairum.plugin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dairum.domain.model.ExportFormat
import com.dairum.domain.repository.BackupRepository
import com.dairum.domain.repository.ExportRepository
import com.dairum.domain.repository.JournalRepository
import com.dairum.domain.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
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
 * executes the configured Dairum action.
 */
@AndroidEntryPoint
class PluginFireReceiver : BroadcastReceiver() {

    @Inject
    lateinit var exportRepository: ExportRepository

    @Inject
    lateinit var journalRepository: JournalRepository

    @Inject
    lateinit var backupRepository: BackupRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        // Verify the intent action
        if (intent.action != ACTION_FIRE_SETTING) {
            return
        }
        
        // Get the bundle with our configuration
        val bundle = intent.getBundleExtra(PluginEditActivity.EXTRA_BUNDLE) ?: return
        
        // Validate the bundle
        if (!isBundleValid(bundle)) {
            return
        }
        
        val pendingResult = goAsync()
        
        scope.launch {
            try {
                // Check if automation is enabled in settings
                val isEnabled = settingsRepository.getTaskerEnabledFlow().first()
                if (!isEnabled) {
                    pendingResult.finish()
                    return@launch
                }
                
                val action = bundle.getString(PluginEditActivity.BUNDLE_ACTION) 
                    ?: PluginEditActivity.ACTION_EXPORT
                
                when (action) {
                    PluginEditActivity.ACTION_EXPORT -> handleExport(bundle)
                    PluginEditActivity.ACTION_BACKUP -> handleBackup(bundle)
                }
            } catch (e: Exception) {
                // Log error but don't crash
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }
    
    private fun isBundleValid(bundle: Bundle): Boolean {
        // Check that required keys exist
        return bundle.containsKey(PluginEditActivity.BUNDLE_ACTION)
    }
    
    private suspend fun handleExport(bundle: Bundle) {
        val formatStr = bundle.getString(PluginEditActivity.BUNDLE_FORMAT) ?: "pdf"
        val periodStr = bundle.getString(PluginEditActivity.BUNDLE_PERIOD) ?: "all"
        val includeMedia = bundle.getBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, true)
        
        // Parse format
        val format = when (formatStr.lowercase()) {
            "txt", "text" -> ExportFormat.TXT
            "pdf" -> ExportFormat.PDF
            else -> ExportFormat.PDF
        }
        
        // Calculate date range
        val (startDate, endDate) = calculateDateRange(periodStr)
        
        // Get entries
        val entries = if (startDate != null && endDate != null) {
            journalRepository.getEntriesInDateRangeFlow(startDate, endDate).first()
        } else {
            journalRepository.getAllEntries().first()
        }
        
        if (entries.isEmpty()) {
            return
        }
        
        // Perform export
        exportRepository.exportEntries(
            entries = entries,
            format = format,
            includeMedia = includeMedia
        )
    }
    
    private suspend fun handleBackup(bundle: Bundle) {
        val includeMedia = bundle.getBoolean(PluginEditActivity.BUNDLE_INCLUDE_MEDIA, true)
        
        backupRepository.createBackup(
            destinationPath = null,
            includeMedia = includeMedia
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

    companion object {
        // Standard Locale/Tasker fire action
        const val ACTION_FIRE_SETTING = "com.twofortyfouram.locale.intent.action.FIRE_SETTING"
    }
}
