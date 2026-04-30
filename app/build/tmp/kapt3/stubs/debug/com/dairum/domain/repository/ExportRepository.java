package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J,\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000e\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J:\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\tH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001f\u0010 J*\u0010!\u001a\b\u0012\u0004\u0012\u00020\u000b0\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\"\u0010#JB\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00180\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\rH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b%\u0010&J*\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u000b0\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b(\u0010#J2\u0010)\u001a\b\u0012\u0004\u0012\u00020\u000b0\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010\u001e\u001a\u00020\tH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b*\u0010+J*\u0010,\u001a\b\u0012\u0004\u0012\u00020\u000b0\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b-\u0010#J\u0014\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u001a0/H&J\u0014\u00100\u001a\b\u0012\u0004\u0012\u00020\u00120\u001aH\u00a6@\u00a2\u0006\u0002\u00101J\u0010\u00102\u001a\u0004\u0018\u00010\u0012H\u00a6@\u00a2\u0006\u0002\u00101J\u0014\u00103\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001a0/H&J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020\u00180\u001a2\u0006\u00105\u001a\u000206H\u00a6@\u00a2\u0006\u0002\u00107J\u001e\u00108\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u00109\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010:J\u000e\u0010;\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u00101J\u0016\u0010<\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010\u0013\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006="}, d2 = {"Lcom/dairum/domain/repository/ExportRepository;", "", "clearOldHistory", "", "beforeTimestamp", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "copyExportToUri", "Lkotlin/Result;", "", "exportFilePath", "", "destinationUri", "Landroid/net/Uri;", "copyExportToUri-0E7RQCE", "(Ljava/lang/String;Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createConfig", "config", "Lcom/dairum/domain/model/ExportConfig;", "(Lcom/dairum/domain/model/ExportConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteConfig", "id", "deleteHistory", "exportEntries", "Lcom/dairum/domain/model/ExportHistory;", "entries", "", "Lcom/dairum/domain/model/JournalEntry;", "format", "Lcom/dairum/domain/model/ExportFormat;", "includeMedia", "exportEntries-BWLJW6A", "(Ljava/util/List;Lcom/dairum/domain/model/ExportFormat;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToCsv", "exportToCsv-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToExternalUri", "exportToExternalUri-yxL6bBk", "(Ljava/util/List;Lcom/dairum/domain/model/ExportFormat;ZLandroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToJson", "exportToJson-gIAlu-s", "exportToPdf", "exportToPdf-0E7RQCE", "(Ljava/util/List;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToTxt", "exportToTxt-gIAlu-s", "getAllConfigsFlow", "Lkotlinx/coroutines/flow/Flow;", "getEnabledConfigs", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getExportConfig", "getExportHistoryFlow", "getRecentHistory", "limit", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleConfigEnabled", "isEnabled", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "triggerImmediateExport", "updateConfig", "app_debug"})
public abstract interface ExportRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteConfig(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getExportConfig(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ExportConfig> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ExportConfig>> getAllConfigsFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEnabledConfigs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ExportConfig>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object toggleConfigEnabled(long id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object triggerImmediateExport(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ExportHistory>> getExportHistoryFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentHistory(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ExportHistory>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteHistory(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearOldHistory(long beforeTimestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}