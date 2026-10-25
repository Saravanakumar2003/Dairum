package com.dairum.presentation.screens.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.repository.BackupInfo
import com.dairum.domain.repository.BackupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupRepository: BackupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BackupRestoreUiState())
    val uiState: StateFlow<BackupRestoreUiState> = _uiState.asStateFlow()

    init {
        loadAvailableBackups()
    }

    private fun loadAvailableBackups() {
        viewModelScope.launch {
            backupRepository.getAvailableBackups().collect { backups ->
                _uiState.update { it.copy(availableBackups = backups) }
            }
        }
    }

    fun setIncludeMedia(include: Boolean) {
        _uiState.update { it.copy(includeMediaInBackup = include) }
    }

    fun setBackupDestination(destination: String?) {
        _uiState.update { it.copy(backupDestination = destination) }
    }

    fun createBackup() {
        viewModelScope.launch {
            _uiState.update { it.copy(isBackingUp = true) }
            
            try {
                val result = backupRepository.createBackup(
                    destinationPath = _uiState.value.backupDestination,
                    includeMedia = _uiState.value.includeMediaInBackup
                )
                
                result.fold(
                    onSuccess = { backupResult ->
                        _uiState.update { 
                            it.copy(
                                isBackingUp = false,
                                successMessage = "Backup created successfully! ${backupResult.entriesCount} entries saved.",
                                lastBackupPath = backupResult.filePath
                            )
                        }
                        // Refresh backup list
                        loadAvailableBackups()
                    },
                    onFailure = { e ->
                        _uiState.update { 
                            it.copy(
                                isBackingUp = false,
                                error = e.message ?: "Failed to create backup"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isBackingUp = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun restoreBackup(backupInfo: BackupInfo) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRestoring = true) }
            
            try {
                val backupFile = File(backupInfo.filePath)
                val result = backupRepository.restoreBackup(backupFile, mergeData = false)
                
                result.fold(
                    onSuccess = { restoreResult ->
                        _uiState.update { 
                            it.copy(
                                isRestoring = false,
                                successMessage = "Restore complete! ${restoreResult.entriesRestored} entries restored. Please restart the app.",
                                requiresRestart = true
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.update { 
                            it.copy(
                                isRestoring = false,
                                error = e.message ?: "Failed to restore backup"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isRestoring = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun restoreFromUri(uri: Uri, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRestoring = true) }
            
            try {
                // Copy the file to a temporary location first
                val tempFile = File(context.cacheDir, "restore_temp.zip")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                }
                
                // Validate the backup
                val backupInfo = backupRepository.validateBackup(tempFile)
                if (backupInfo == null) {
                    tempFile.delete()
                    _uiState.update { 
                        it.copy(
                            isRestoring = false,
                            error = "Invalid backup file"
                        )
                    }
                    return@launch
                }
                
                // Restore
                val result = backupRepository.restoreBackup(tempFile, mergeData = false)
                tempFile.delete()
                
                result.fold(
                    onSuccess = { restoreResult ->
                        _uiState.update { 
                            it.copy(
                                isRestoring = false,
                                successMessage = "Restore complete! ${restoreResult.entriesRestored} entries restored. Please restart the app.",
                                requiresRestart = true
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.update { 
                            it.copy(
                                isRestoring = false,
                                error = e.message ?: "Failed to restore backup"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isRestoring = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun shareBackup(filePath: String) {
        _uiState.update { it.copy(shareBackupPath = filePath) }
    }

    fun clearSharePath() {
        _uiState.update { it.copy(shareBackupPath = null) }
    }

    fun deleteBackup(filePath: String) {
        viewModelScope.launch {
            val success = backupRepository.deleteBackup(filePath)
            if (success) {
                loadAvailableBackups()
                _uiState.update { it.copy(successMessage = "Backup deleted") }
            } else {
                _uiState.update { it.copy(error = "Failed to delete backup") }
            }
        }
    }

    fun createCompleteExport() {
        viewModelScope.launch {
            _uiState.update { it.copy(isBackingUp = true) }
            try {
                val result = backupRepository.createCompleteExport(
                    includeMedia = _uiState.value.includeMediaInBackup
                )
                result.fold(
                    onSuccess = { backupResult ->
                        _uiState.update {
                            it.copy(
                                isBackingUp = false,
                                successMessage = "Complete export created! ${backupResult.entriesCount} entries, ${backupResult.mediaCount} media files."
                            )
                        }
                        loadAvailableBackups()
                    },
                    onFailure = { e ->
                        _uiState.update {
                            it.copy(
                                isBackingUp = false,
                                error = e.message ?: "Failed to create complete export"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isBackingUp = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }

    fun importCompleteExport(uri: Uri, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRestoring = true) }
            try {
                val tempFile = File(context.cacheDir, "import_complete_temp.zip")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                }
                
                val result = backupRepository.importCompleteExport(tempFile)
                tempFile.delete()
                
                result.fold(
                    onSuccess = { restoreResult ->
                        _uiState.update {
                            it.copy(
                                isRestoring = false,
                                successMessage = "Import complete! ${restoreResult.mediaRestored} media files restored. Please restart the app.",
                                requiresRestart = true
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.update {
                            it.copy(
                                isRestoring = false,
                                error = e.message ?: "Failed to import"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isRestoring = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { 
            it.copy(
                successMessage = null,
                error = null
            )
        }
    }
}

data class BackupRestoreUiState(
    val includeMediaInBackup: Boolean = true,
    val backupDestination: String? = null,
    val isBackingUp: Boolean = false,
    val isRestoring: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null,
    val lastBackupPath: String? = null,
    val shareBackupPath: String? = null,
    val availableBackups: List<BackupInfo> = emptyList(),
    val requiresRestart: Boolean = false
)
