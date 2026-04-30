package com.dairum.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
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
 * executes the configured Diarum action.
 */
@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 62\u00020\u0001:\u00016B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010#\u001a\u0012\u0012\u0006\u0012\u0004\u0018\u00010%\u0012\u0006\u0012\u0004\u0018\u00010%0$2\u0006\u0010&\u001a\u00020\'H\u0002J\u0016\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0082@\u00a2\u0006\u0002\u0010,J\u0016\u0010-\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0082@\u00a2\u0006\u0002\u0010,J\u0010\u0010.\u001a\u00020/2\u0006\u0010*\u001a\u00020+H\u0002J\u0018\u00100\u001a\u00020)2\u0006\u00101\u001a\u00020\u00042\u0006\u00102\u001a\u000203H\u0016J\u0010\u00104\u001a\u00020)2\u0006\u00105\u001a\u00020\'H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\r\u001a\u00020\u000e8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001e\u0010\u0013\u001a\u00020\u00148\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u001d\u001a\u00020\u001e8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"\u00a8\u00067"}, d2 = {"Lcom/dairum/plugin/PluginFireReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "appContext", "Landroid/content/Context;", "backupRepository", "Lcom/dairum/domain/repository/BackupRepository;", "getBackupRepository", "()Lcom/dairum/domain/repository/BackupRepository;", "setBackupRepository", "(Lcom/dairum/domain/repository/BackupRepository;)V", "exceptionHandler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "exportRepository", "Lcom/dairum/domain/repository/ExportRepository;", "getExportRepository", "()Lcom/dairum/domain/repository/ExportRepository;", "setExportRepository", "(Lcom/dairum/domain/repository/ExportRepository;)V", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "getJournalRepository", "()Lcom/dairum/domain/repository/JournalRepository;", "setJournalRepository", "(Lcom/dairum/domain/repository/JournalRepository;)V", "mainHandler", "Landroid/os/Handler;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "settingsRepository", "Lcom/dairum/domain/repository/SettingsRepository;", "getSettingsRepository", "()Lcom/dairum/domain/repository/SettingsRepository;", "setSettingsRepository", "(Lcom/dairum/domain/repository/SettingsRepository;)V", "calculateDateRange", "Lkotlin/Pair;", "", "periodStr", "", "handleBackup", "", "bundle", "Landroid/os/Bundle;", "(Landroid/os/Bundle;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "handleExport", "isBundleValid", "", "onReceive", "context", "intent", "Landroid/content/Intent;", "showToast", "message", "Companion", "app_debug"})
public final class PluginFireReceiver extends android.content.BroadcastReceiver {
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler mainHandler = null;
    @javax.inject.Inject()
    public com.dairum.domain.repository.ExportRepository exportRepository;
    @javax.inject.Inject()
    public com.dairum.domain.repository.JournalRepository journalRepository;
    @javax.inject.Inject()
    public com.dairum.domain.repository.BackupRepository backupRepository;
    @javax.inject.Inject()
    public com.dairum.domain.repository.SettingsRepository settingsRepository;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineExceptionHandler exceptionHandler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.Nullable()
    private android.content.Context appContext;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "PluginFireReceiver";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_FIRE_SETTING = "com.twofortyfouram.locale.intent.action.FIRE_SETTING";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MACRODROID_ACTION_FIRE = "com.arlosoft.macrodroid.intent.action.FIRE_SETTING";
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
    
    private final void showToast(java.lang.String message) {
    }
    
    private final java.lang.Object handleBackup(android.os.Bundle bundle, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Long, java.lang.Long> calculateDateRange(java.lang.String periodStr) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/dairum/plugin/PluginFireReceiver$Companion;", "", "()V", "ACTION_FIRE_SETTING", "", "MACRODROID_ACTION_FIRE", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}