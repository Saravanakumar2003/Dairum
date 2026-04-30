package com.dairum.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.dairum.domain.model.ExportFormat;
import com.dairum.domain.repository.BackupRepository;
import com.dairum.domain.repository.ExportRepository;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.SettingsRepository;
import com.dairum.domain.repository.TaskerPeriod;
import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.Dispatchers;
import java.util.Calendar;
import javax.inject.Inject;

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
@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0002\b\u0007\u0018\u0000 32\u00020\u0001:\u00013B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\u001f\u001a\u0012\u0012\u0006\u0012\u0004\u0018\u00010!\u0012\u0006\u0012\u0004\u0018\u00010!0 2\u0006\u0010\"\u001a\u00020#H\u0002J\u001e\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020)H\u0082@\u00a2\u0006\u0002\u0010*J\u001e\u0010+\u001a\u00020%2\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020)H\u0082@\u00a2\u0006\u0002\u0010*J\u0018\u0010,\u001a\u00020%2\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020)H\u0016J6\u0010-\u001a\u00020%2\u0006\u0010&\u001a\u00020\'2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020#2\u0014\b\u0002\u00101\u001a\u000e\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020#02H\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0011\u001a\u00020\u00128\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u00020\u001a8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001e\u00a8\u00064"}, d2 = {"Lcom/dairum/receiver/AutomationReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "backupRepository", "Lcom/dairum/domain/repository/BackupRepository;", "getBackupRepository", "()Lcom/dairum/domain/repository/BackupRepository;", "setBackupRepository", "(Lcom/dairum/domain/repository/BackupRepository;)V", "exceptionHandler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "exportRepository", "Lcom/dairum/domain/repository/ExportRepository;", "getExportRepository", "()Lcom/dairum/domain/repository/ExportRepository;", "setExportRepository", "(Lcom/dairum/domain/repository/ExportRepository;)V", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "getJournalRepository", "()Lcom/dairum/domain/repository/JournalRepository;", "setJournalRepository", "(Lcom/dairum/domain/repository/JournalRepository;)V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "settingsRepository", "Lcom/dairum/domain/repository/SettingsRepository;", "getSettingsRepository", "()Lcom/dairum/domain/repository/SettingsRepository;", "setSettingsRepository", "(Lcom/dairum/domain/repository/SettingsRepository;)V", "calculateDateRange", "Lkotlin/Pair;", "", "periodStr", "", "handleBackup", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "(Landroid/content/Context;Landroid/content/Intent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "handleExport", "onReceive", "sendResultBroadcast", "success", "", "message", "extras", "", "Companion", "app_debug"})
public final class AutomationReceiver extends android.content.BroadcastReceiver {
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
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "AutomationReceiver";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_EXPORT = "com.dairum.action.EXPORT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_BACKUP = "com.dairum.action.BACKUP";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_RESULT = "com.dairum.action.RESULT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_FORMAT = "format";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PERIOD = "period";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_INCLUDE_MEDIA = "include_media";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_START_DATE = "start_date";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_END_DATE = "end_date";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_RESULT_SUCCESS = "result_success";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_RESULT_MESSAGE = "result_message";
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.receiver.AutomationReceiver.Companion Companion = null;
    
    public AutomationReceiver() {
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
    
    private final java.lang.Object handleExport(android.content.Context context, android.content.Intent intent, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object handleBackup(android.content.Context context, android.content.Intent intent, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Long, java.lang.Long> calculateDateRange(java.lang.String periodStr) {
        return null;
    }
    
    private final void sendResultBroadcast(android.content.Context context, boolean success, java.lang.String message, java.util.Map<java.lang.String, java.lang.String> extras) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/dairum/receiver/AutomationReceiver$Companion;", "", "()V", "ACTION_BACKUP", "", "ACTION_EXPORT", "ACTION_RESULT", "EXTRA_END_DATE", "EXTRA_FORMAT", "EXTRA_INCLUDE_MEDIA", "EXTRA_PERIOD", "EXTRA_RESULT_MESSAGE", "EXTRA_RESULT_SUCCESS", "EXTRA_START_DATE", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}