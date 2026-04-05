package com.dairum.presentation.screens.settings;

import android.content.Context;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.dairum.domain.repository.BackupInfo;
import com.dairum.domain.repository.BackupRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.io.File;
import java.io.FileOutputStream;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u0006\u0010\u000f\u001a\u00020\rJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0012J\b\u0010\u0013\u001a\u00020\rH\u0002J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0016J\u0016\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bJ\u0010\u0010\u001c\u001a\u00020\r2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0012J\u000e\u0010\u001e\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0012R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\""}, d2 = {"Lcom/dairum/presentation/screens/settings/BackupRestoreViewModel;", "Landroidx/lifecycle/ViewModel;", "backupRepository", "Lcom/dairum/domain/repository/BackupRepository;", "(Lcom/dairum/domain/repository/BackupRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/settings/BackupRestoreUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearMessages", "", "clearSharePath", "createBackup", "deleteBackup", "filePath", "", "loadAvailableBackups", "restoreBackup", "backupInfo", "Lcom/dairum/domain/repository/BackupInfo;", "restoreFromUri", "uri", "Landroid/net/Uri;", "context", "Landroid/content/Context;", "setBackupDestination", "destination", "setIncludeMedia", "include", "", "shareBackup", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class BackupRestoreViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.BackupRepository backupRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.settings.BackupRestoreUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.settings.BackupRestoreUiState> uiState = null;
    
    @javax.inject.Inject()
    public BackupRestoreViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.BackupRepository backupRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.settings.BackupRestoreUiState> getUiState() {
        return null;
    }
    
    private final void loadAvailableBackups() {
    }
    
    public final void setIncludeMedia(boolean include) {
    }
    
    public final void setBackupDestination(@org.jetbrains.annotations.Nullable()
    java.lang.String destination) {
    }
    
    public final void createBackup() {
    }
    
    public final void restoreBackup(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.BackupInfo backupInfo) {
    }
    
    public final void restoreFromUri(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void shareBackup(@org.jetbrains.annotations.NotNull()
    java.lang.String filePath) {
    }
    
    public final void clearSharePath() {
    }
    
    public final void deleteBackup(@org.jetbrains.annotations.NotNull()
    java.lang.String filePath) {
    }
    
    public final void clearMessages() {
    }
}