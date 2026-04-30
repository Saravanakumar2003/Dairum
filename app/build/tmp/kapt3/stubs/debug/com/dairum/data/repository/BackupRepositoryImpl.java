package com.dairum.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import com.dairum.data.local.database.DairumDatabase;
import com.dairum.domain.repository.BackupInfo;
import com.dairum.domain.repository.BackupRepository;
import com.dairum.domain.repository.BackupResult;
import com.dairum.domain.repository.RestoreResult;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.Flow;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000 /2\u00020\u0001:\u0001/B!\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\u0011\u001a\u00020\u0012H\u0002J.\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u00142\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u0014\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020!0\u000e0 H\u0016J\b\u0010\"\u001a\u00020\nH\u0016J,\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u00142\u0006\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\'\u0010(J\u001e\u0010)\u001a\u00020\u00172\u0006\u0010*\u001a\u00020\n2\u0006\u0010+\u001a\u00020\u0017H\u0082@\u00a2\u0006\u0002\u0010,J\u0018\u0010-\u001a\u0004\u0018\u00010!2\u0006\u0010%\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010.R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\b\u0012\u0004\u0012\u00020\n0\u000e8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00060"}, d2 = {"Lcom/dairum/data/repository/BackupRepositoryImpl;", "Lcom/dairum/domain/repository/BackupRepository;", "context", "Landroid/content/Context;", "database", "Lcom/dairum/data/local/database/DairumDatabase;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Landroid/content/Context;Lcom/dairum/data/local/database/DairumDatabase;Lcom/squareup/moshi/Moshi;)V", "internalBackupDir", "Ljava/io/File;", "getInternalBackupDir", "()Ljava/io/File;", "mediaDirs", "", "getMediaDirs", "()Ljava/util/List;", "cleanupOldBackups", "", "createBackup", "Lkotlin/Result;", "Lcom/dairum/domain/repository/BackupResult;", "destinationPath", "", "includeMedia", "", "createBackup-0E7RQCE", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteBackup", "backupPath", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAvailableBackups", "Lkotlinx/coroutines/flow/Flow;", "Lcom/dairum/domain/repository/BackupInfo;", "getBackupDirectory", "restoreBackup", "Lcom/dairum/domain/repository/RestoreResult;", "backupFile", "mergeData", "restoreBackup-0E7RQCE", "(Ljava/io/File;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveToDocumentsDairum", "tempFile", "fileName", "(Ljava/io/File;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "validateBackup", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class BackupRepositoryImpl implements com.dairum.domain.repository.BackupRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.database.DairumDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.squareup.moshi.Moshi moshi = null;
    private static final int BACKUP_VERSION = 1;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.repository.BackupRepositoryImpl.Companion Companion = null;
    
    @javax.inject.Inject()
    public BackupRepositoryImpl(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database, @org.jetbrains.annotations.NotNull()
    com.squareup.moshi.Moshi moshi) {
        super();
    }
    
    private final java.io.File getInternalBackupDir() {
        return null;
    }
    
    private final java.util.List<java.io.File> getMediaDirs() {
        return null;
    }
    
    /**
     * Save backup file to Documents/dairum folder
     */
    private final java.lang.Object saveToDocumentsDairum(java.io.File tempFile, java.lang.String fileName, kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    /**
     * Keep only the last 5 backups in internal storage
     */
    private final void cleanupOldBackups() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object validateBackup(@org.jetbrains.annotations.NotNull()
    java.io.File backupFile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.repository.BackupInfo> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.BackupInfo>> getAvailableBackups() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteBackup(@org.jetbrains.annotations.NotNull()
    java.lang.String backupPath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.io.File getBackupDirectory() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/dairum/data/repository/BackupRepositoryImpl$Companion;", "", "()V", "BACKUP_VERSION", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}