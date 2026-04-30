package com.dairum.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import com.dairum.data.local.dao.ExportDao;
import com.dairum.data.local.entity.ExportConfigEntity;
import com.dairum.data.local.entity.ExportHistoryEntity;
import com.dairum.domain.model.ExportConfig;
import com.dairum.domain.model.ExportFormat;
import com.dairum.domain.model.ExportHistory;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.MediaType;
import com.dairum.domain.repository.ExportRepository;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.Flow;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u000f\u001a\u00020\u0010H\u0082@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0015J,\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001d\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020!H\u0096@\u00a2\u0006\u0002\u0010\"J&\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020)H\u0082@\u00a2\u0006\u0002\u0010*J\u0016\u0010+\u001a\u00020\u00102\u0006\u0010,\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010-\u001a\u00020\u00102\u0006\u0010,\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u0010\u0010.\u001a\u00020\u001a2\u0006\u0010/\u001a\u00020\u001aH\u0002J\u0012\u00100\u001a\u00020\u001a2\b\u0010/\u001a\u0004\u0018\u00010\u001aH\u0002J:\u00101\u001a\b\u0012\u0004\u0012\u00020$0\u00172\f\u00102\u001a\b\u0012\u0004\u0012\u0002030\f2\u0006\u0010(\u001a\u00020)2\u0006\u00104\u001a\u00020\u0018H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b5\u00106J*\u00107\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00172\f\u00102\u001a\b\u0012\u0004\u0012\u0002030\fH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b8\u00109JB\u0010:\u001a\b\u0012\u0004\u0012\u00020$0\u00172\f\u00102\u001a\b\u0012\u0004\u0012\u0002030\f2\u0006\u0010(\u001a\u00020)2\u0006\u00104\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001cH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b;\u0010<J*\u0010=\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00172\f\u00102\u001a\b\u0012\u0004\u0012\u0002030\fH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b>\u00109J2\u0010?\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00172\f\u00102\u001a\b\u0012\u0004\u0012\u0002030\f2\u0006\u00104\u001a\u00020\u0018H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b@\u0010AJ*\u0010B\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00172\f\u00102\u001a\b\u0012\u0004\u0012\u0002030\fH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bC\u00109J\u0012\u0010D\u001a\u0004\u0018\u00010\u001c2\u0006\u0010E\u001a\u00020\u001aH\u0002J\u0014\u0010F\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020!0\f0GH\u0016J\u0014\u0010H\u001a\b\u0012\u0004\u0012\u00020!0\fH\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0010\u0010I\u001a\u0004\u0018\u00010!H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010J\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020$0\f0GH\u0016J\u001c\u0010K\u001a\b\u0012\u0004\u0012\u00020$0\f2\u0006\u0010L\u001a\u00020\'H\u0096@\u00a2\u0006\u0002\u0010MJ&\u0010N\u001a\u00020\u001a2\u0006\u0010O\u001a\u00020\b2\u0006\u0010E\u001a\u00020\u001a2\u0006\u0010P\u001a\u00020\u001aH\u0082@\u00a2\u0006\u0002\u0010QJ\u001e\u0010R\u001a\u00020\u00102\u0006\u0010,\u001a\u00020\u00142\u0006\u0010S\u001a\u00020\u0018H\u0096@\u00a2\u0006\u0002\u0010TJ\u000e\u0010U\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010V\u001a\u00020\u00102\u0006\u0010 \u001a\u00020!H\u0096@\u00a2\u0006\u0002\u0010\"J\f\u0010W\u001a\u00020!*\u00020XH\u0002J\f\u0010W\u001a\u00020)*\u00020YH\u0002J\f\u0010W\u001a\u00020$*\u00020ZH\u0002J\f\u0010[\u001a\u00020X*\u00020!H\u0002J\f\u0010[\u001a\u00020Y*\u00020)H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\f8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000e\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\\"}, d2 = {"Lcom/dairum/data/repository/ExportRepositoryImpl;", "Lcom/dairum/domain/repository/ExportRepository;", "exportDao", "Lcom/dairum/data/local/dao/ExportDao;", "context", "Landroid/content/Context;", "(Lcom/dairum/data/local/dao/ExportDao;Landroid/content/Context;)V", "internalExportDir", "Ljava/io/File;", "getInternalExportDir", "()Ljava/io/File;", "mediaDirs", "", "getMediaDirs", "()Ljava/util/List;", "cleanupOldExports", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearOldHistory", "beforeTimestamp", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "copyExportToUri", "Lkotlin/Result;", "", "exportFilePath", "", "destinationUri", "Landroid/net/Uri;", "copyExportToUri-0E7RQCE", "(Ljava/lang/String;Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createConfig", "config", "Lcom/dairum/domain/model/ExportConfig;", "(Lcom/dairum/domain/model/ExportConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createHistoryEntry", "Lcom/dairum/domain/model/ExportHistory;", "pathWithSize", "entriesCount", "", "format", "Lcom/dairum/domain/model/ExportFormat;", "(Ljava/lang/String;ILcom/dairum/domain/model/ExportFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteConfig", "id", "deleteHistory", "escapeCsv", "value", "escapeJson", "exportEntries", "entries", "Lcom/dairum/domain/model/JournalEntry;", "includeMedia", "exportEntries-BWLJW6A", "(Ljava/util/List;Lcom/dairum/domain/model/ExportFormat;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToCsv", "exportToCsv-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToExternalUri", "exportToExternalUri-yxL6bBk", "(Ljava/util/List;Lcom/dairum/domain/model/ExportFormat;ZLandroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToJson", "exportToJson-gIAlu-s", "exportToPdf", "exportToPdf-0E7RQCE", "(Ljava/util/List;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToTxt", "exportToTxt-gIAlu-s", "findExistingFile", "fileName", "getAllConfigsFlow", "Lkotlinx/coroutines/flow/Flow;", "getEnabledConfigs", "getExportConfig", "getExportHistoryFlow", "getRecentHistory", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveToDocumentsDiarum", "tempFile", "mimeType", "(Ljava/io/File;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleConfigEnabled", "isEnabled", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "triggerImmediateExport", "updateConfig", "toDomain", "Lcom/dairum/data/local/entity/ExportConfigEntity;", "Lcom/dairum/data/local/entity/ExportFormat;", "Lcom/dairum/data/local/entity/ExportHistoryEntity;", "toEntity", "app_debug"})
public final class ExportRepositoryImpl implements com.dairum.domain.repository.ExportRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.ExportDao exportDao = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    @javax.inject.Inject()
    public ExportRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.ExportDao exportDao, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final java.io.File getInternalExportDir() {
        return null;
    }
    
    private final java.util.List<java.io.File> getMediaDirs() {
        return null;
    }
    
    /**
     * Save a file to Documents/diarum folder using MediaStore for Android 10+
     * or legacy file system approach for older versions.
     * Returns the file path or URI string.
     */
    private final java.lang.Object saveToDocumentsDiarum(java.io.File tempFile, java.lang.String fileName, java.lang.String mimeType, kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    /**
     * Find an existing file in Documents/diarum by name
     */
    private final android.net.Uri findExistingFile(java.lang.String fileName) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateConfig(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteConfig(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getExportConfig(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.ExportConfig> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ExportConfig>> getAllConfigsFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEnabledConfigs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ExportConfig>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object toggleConfigEnabled(long id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.String escapeJson(java.lang.String value) {
        return null;
    }
    
    private final java.lang.String escapeCsv(java.lang.String value) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object triggerImmediateExport(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.ExportHistory>> getExportHistoryFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getRecentHistory(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.ExportHistory>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteHistory(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearOldHistory(long beforeTimestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object createHistoryEntry(java.lang.String pathWithSize, int entriesCount, com.dairum.domain.model.ExportFormat format, kotlin.coroutines.Continuation<? super com.dairum.domain.model.ExportHistory> $completion) {
        return null;
    }
    
    /**
     * Keep only the last 5 exports in internal storage and history
     */
    private final java.lang.Object cleanupOldExports(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.ExportConfig toDomain(com.dairum.data.local.entity.ExportConfigEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.ExportConfigEntity toEntity(com.dairum.domain.model.ExportConfig $this$toEntity) {
        return null;
    }
    
    private final com.dairum.domain.model.ExportHistory toDomain(com.dairum.data.local.entity.ExportHistoryEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.ExportFormat toDomain(com.dairum.data.local.entity.ExportFormat $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.ExportFormat toEntity(com.dairum.domain.model.ExportFormat $this$toEntity) {
        return null;
    }
}