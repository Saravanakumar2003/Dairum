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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\r\u001a\u00020\u000eH\u0082@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u0013J,\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001b\u0010\u001cJ\u0016\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u001e\u001a\u00020\u001fH\u0096@\u00a2\u0006\u0002\u0010 J&\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u00182\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\'H\u0082@\u00a2\u0006\u0002\u0010(J\u0016\u0010)\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010+\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u0013J:\u0010,\u001a\b\u0012\u0004\u0012\u00020\"0\u00152\f\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.2\u0006\u0010&\u001a\u00020\'2\u0006\u00100\u001a\u00020\u0016H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b1\u00102JB\u00103\u001a\b\u0012\u0004\u0012\u00020\"0\u00152\f\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.2\u0006\u0010&\u001a\u00020\'2\u0006\u00100\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b4\u00105J2\u00106\u001a\b\u0012\u0004\u0012\u00020\u00180\u00152\f\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.2\u0006\u00100\u001a\u00020\u0016H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b7\u00108J*\u00109\u001a\b\u0012\u0004\u0012\u00020\u00180\u00152\f\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b:\u0010;J\u0014\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0.0=H\u0016J\u0014\u0010>\u001a\b\u0012\u0004\u0012\u00020\u001f0.H\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u0010\u0010?\u001a\u0004\u0018\u00010\u001fH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u0014\u0010@\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0.0=H\u0016J\u001c\u0010A\u001a\b\u0012\u0004\u0012\u00020\"0.2\u0006\u0010B\u001a\u00020%H\u0096@\u00a2\u0006\u0002\u0010CJ&\u0010D\u001a\u00020\u00182\u0006\u0010E\u001a\u00020\b2\u0006\u0010F\u001a\u00020\u00182\u0006\u0010G\u001a\u00020\u0018H\u0082@\u00a2\u0006\u0002\u0010HJ\u001e\u0010I\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020\u00122\u0006\u0010J\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010KJ\u000e\u0010L\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010M\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u001fH\u0096@\u00a2\u0006\u0002\u0010 J\f\u0010N\u001a\u00020\u001f*\u00020OH\u0002J\f\u0010N\u001a\u00020\'*\u00020PH\u0002J\f\u0010N\u001a\u00020\"*\u00020QH\u0002J\f\u0010R\u001a\u00020O*\u00020\u001fH\u0002J\f\u0010R\u001a\u00020P*\u00020\'H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\n\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006S"}, d2 = {"Lcom/dairum/data/repository/ExportRepositoryImpl;", "Lcom/dairum/domain/repository/ExportRepository;", "exportDao", "Lcom/dairum/data/local/dao/ExportDao;", "context", "Landroid/content/Context;", "(Lcom/dairum/data/local/dao/ExportDao;Landroid/content/Context;)V", "internalExportDir", "Ljava/io/File;", "getInternalExportDir", "()Ljava/io/File;", "mediaDir", "getMediaDir", "cleanupOldExports", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearOldHistory", "beforeTimestamp", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "copyExportToUri", "Lkotlin/Result;", "", "exportFilePath", "", "destinationUri", "Landroid/net/Uri;", "copyExportToUri-0E7RQCE", "(Ljava/lang/String;Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createConfig", "config", "Lcom/dairum/domain/model/ExportConfig;", "(Lcom/dairum/domain/model/ExportConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createHistoryEntry", "Lcom/dairum/domain/model/ExportHistory;", "pathWithSize", "entriesCount", "", "format", "Lcom/dairum/domain/model/ExportFormat;", "(Ljava/lang/String;ILcom/dairum/domain/model/ExportFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteConfig", "id", "deleteHistory", "exportEntries", "entries", "", "Lcom/dairum/domain/model/JournalEntry;", "includeMedia", "exportEntries-BWLJW6A", "(Ljava/util/List;Lcom/dairum/domain/model/ExportFormat;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToExternalUri", "exportToExternalUri-yxL6bBk", "(Ljava/util/List;Lcom/dairum/domain/model/ExportFormat;ZLandroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToPdf", "exportToPdf-0E7RQCE", "(Ljava/util/List;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToTxt", "exportToTxt-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllConfigsFlow", "Lkotlinx/coroutines/flow/Flow;", "getEnabledConfigs", "getExportConfig", "getExportHistoryFlow", "getRecentHistory", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveToDocumentsDairum", "tempFile", "fileName", "mimeType", "(Ljava/io/File;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleConfigEnabled", "isEnabled", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "triggerImmediateExport", "updateConfig", "toDomain", "Lcom/dairum/data/local/entity/ExportConfigEntity;", "Lcom/dairum/data/local/entity/ExportFormat;", "Lcom/dairum/data/local/entity/ExportHistoryEntity;", "toEntity", "app_debug"})
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
    
    private final java.io.File getMediaDir() {
        return null;
    }
    
    /**
     * Save a file to Documents/dairum folder using MediaStore for Android 10+
     * or legacy file system approach for older versions.
     * Returns the file path or URI string.
     */
    private final java.lang.Object saveToDocumentsDairum(java.io.File tempFile, java.lang.String fileName, java.lang.String mimeType, kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
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