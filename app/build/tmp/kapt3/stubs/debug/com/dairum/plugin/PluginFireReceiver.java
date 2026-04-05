package com.dairum.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.dairum.domain.model.ExportFormat;
import com.dairum.domain.repository.BackupRepository;
import com.dairum.domain.repository.ExportRepository;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.SettingsRepository;
import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.Dispatchers;
import java.util.Calendar;
import javax.inject.Inject;

/**
 * Tasker/Locale/MacroDroid plugin fire receiver.
 * This is the BroadcastReceiver that gets triggered when the automation app
 * executes the configured Dairum action.
 */
@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 /2\u00020\u0001:\u0001/B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\u001d\u001a\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u001f\u0012\u0006\u0012\u0004\u0018\u00010\u001f0\u001e2\u0006\u0010 \u001a\u00020!H\u0002J\u0016\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0082@\u00a2\u0006\u0002\u0010&J\u0016\u0010\'\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0082@\u00a2\u0006\u0002\u0010&J\u0010\u0010(\u001a\u00020)2\u0006\u0010$\u001a\u00020%H\u0002J\u0018\u0010*\u001a\u00020#2\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.H\u0016R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0017\u001a\u00020\u00188\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c\u00a8\u00060"}, d2 = {"Lcom/dairum/plugin/PluginFireReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "backupRepository", "Lcom/dairum/domain/repository/BackupRepository;", "getBackupRepository", "()Lcom/dairum/domain/repository/BackupRepository;", "setBackupRepository", "(Lcom/dairum/domain/repository/BackupRepository;)V", "exportRepository", "Lcom/dairum/domain/repository/ExportRepository;", "getExportRepository", "()Lcom/dairum/domain/repository/ExportRepository;", "setExportRepository", "(Lcom/dairum/domain/repository/ExportRepository;)V", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "getJournalRepository", "()Lcom/dairum/domain/repository/JournalRepository;", "setJournalRepository", "(Lcom/dairum/domain/repository/JournalRepository;)V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "settingsRepository", "Lcom/dairum/domain/repository/SettingsRepository;", "getSettingsRepository", "()Lcom/dairum/domain/repository/SettingsRepository;", "setSettingsRepository", "(Lcom/dairum/domain/repository/SettingsRepository;)V", "calculateDateRange", "Lkotlin/Pair;", "", "periodStr", "", "handleBackup", "", "bundle", "Landroid/os/Bundle;", "(Landroid/os/Bundle;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "handleExport", "isBundleValid", "", "onReceive", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "Companion", "app_debug"})
public final class PluginFireReceiver extends android.content.BroadcastReceiver {
    @javax.inject.Inject()
    public com.dairum.domain.repository.ExportRepository exportRepository;
    @javax.inject.Inject()
    public com.dairum.domain.repository.JournalRepository journalRepository;
    @javax.inject.Inject()
    public com.dairum.domain.repository.BackupRepository backupRepository;
    @javax.inject.Inject()
    public com.dairum.domain.repository.SettingsRepository settingsRepository;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_FIRE_SETTING = "com.twofortyfouram.locale.intent.action.FIRE_SETTING";
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.plugin.PluginFireReceiver.Companion Companion = null;
    
    public PluginFireReceiver() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.repository.ExportRepository getExportRepository() {
        return null;
    }
    
    public final void setExportRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ExportRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.repository.JournalRepository getJournalRepository() {
        return null;
    }
    
    public final void setJournalRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.repository.BackupRepository getBackupRepository() {
        return null;
    }
    
    public final void setBackupRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.BackupRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.repository.SettingsRepository getSettingsRepository() {
        return null;
    }
    
    public final void setSettingsRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.SettingsRepository p0) {
    }
    
    @java.lang.Override()
    public void onReceive(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
    
    private final boolean isBundleValid(android.os.Bundle bundle) {
        return false;
    }
    
    private final java.lang.Object handleExport(android.os.Bundle bundle, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object handleBackup(android.os.Bundle bundle, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Long, java.lang.Long> calculateDateRange(java.lang.String periodStr) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/dairum/plugin/PluginFireReceiver$Companion;", "", "()V", "ACTION_FIRE_SETTING", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}