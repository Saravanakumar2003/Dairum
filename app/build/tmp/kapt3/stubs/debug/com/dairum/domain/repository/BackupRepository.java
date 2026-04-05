package com.dairum.domain.repository;

import kotlinx.coroutines.flow.Flow;
import java.io.File;

/**
 * Repository for backup and restore functionality
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J2\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\t\u0010\nJ\u0016\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00100\u000fH&J\b\u0010\u0012\u001a\u00020\u0013H&J.\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u00032\u0006\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\bH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0018\u0010\u0019J\u0018\u0010\u001a\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0016\u001a\u00020\u0013H\u00a6@\u00a2\u0006\u0002\u0010\u001b\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001c"}, d2 = {"Lcom/dairum/domain/repository/BackupRepository;", "", "createBackup", "Lkotlin/Result;", "Lcom/dairum/domain/repository/BackupResult;", "destinationPath", "", "includeMedia", "", "createBackup-0E7RQCE", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteBackup", "backupPath", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAvailableBackups", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/dairum/domain/repository/BackupInfo;", "getBackupDirectory", "Ljava/io/File;", "restoreBackup", "Lcom/dairum/domain/repository/RestoreResult;", "backupFile", "mergeData", "restoreBackup-0E7RQCE", "(Ljava/io/File;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "validateBackup", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface BackupRepository {
    
    /**
     * Validates a backup file
     * @return BackupInfo if valid, null otherwise
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object validateBackup(@org.jetbrains.annotations.NotNull()
    java.io.File backupFile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.repository.BackupInfo> $completion);
    
    /**
     * Gets list of available backups
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.BackupInfo>> getAvailableBackups();
    
    /**
     * Deletes a backup file
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteBackup(@org.jetbrains.annotations.NotNull()
    java.lang.String backupPath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    /**
     * Gets the backup directory
     */
    @org.jetbrains.annotations.NotNull()
    public abstract java.io.File getBackupDirectory();
    
    /**
     * Repository for backup and restore functionality
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}