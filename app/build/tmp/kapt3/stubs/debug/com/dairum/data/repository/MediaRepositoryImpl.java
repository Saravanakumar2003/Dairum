package com.dairum.data.repository;

import android.content.Context;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;
import com.dairum.data.local.dao.MediaDao;
import com.dairum.data.local.entity.MediaEntity;
import com.dairum.domain.model.Media;
import com.dairum.domain.model.MediaType;
import com.dairum.domain.repository.MediaRepository;
import com.dairum.domain.repository.MediaWithEntryInfo;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.Flow;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u000bJ\"\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\n0\rH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u0011H\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0014\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\r0\u001dH\u0016J\u0016\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0096@\u00a2\u0006\u0002\u0010#J\u0012\u0010$\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u001a\u001a\u00020%H\u0002J\u0010\u0010&\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020%H\u0002J\u0018\u0010\'\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0018\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u001c\u0010(\u001a\b\u0012\u0004\u0012\u00020\n0\r2\u0006\u0010\u0016\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u0013J\u001c\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\r0\u001d2\u0006\u0010\u0016\u001a\u00020\bH\u0016J\u0010\u0010*\u001a\u00020\u00112\u0006\u0010+\u001a\u00020\u0011H\u0002J\u0014\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\r0\u001dH\u0016J\u0016\u0010-\u001a\u00020\b2\u0006\u0010!\u001a\u00020\"H\u0096@\u00a2\u0006\u0002\u0010#J\u000e\u0010.\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010/J\u0014\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\r0\u001dH\u0016J\u0014\u00101\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\r0\u001dH\u0016J&\u00102\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010!\u001a\u00020\"H\u0096@\u00a2\u0006\u0002\u00103J\u0016\u00104\u001a\u00020\u00152\u0006\u0010\t\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u000bJ\f\u00105\u001a\u00020\n*\u000206H\u0002J\f\u00105\u001a\u00020\"*\u000207H\u0002J\f\u00108\u001a\u00020\u001e*\u000209H\u0002J\f\u0010:\u001a\u000206*\u00020\nH\u0002J\f\u0010:\u001a\u000207*\u00020\"H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006;"}, d2 = {"Lcom/dairum/data/repository/MediaRepositoryImpl;", "Lcom/dairum/domain/repository/MediaRepository;", "mediaDao", "Lcom/dairum/data/local/dao/MediaDao;", "context", "Landroid/content/Context;", "(Lcom/dairum/data/local/dao/MediaDao;Landroid/content/Context;)V", "addMedia", "", "media", "Lcom/dairum/domain/model/Media;", "(Lcom/dairum/domain/model/Media;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addMediaList", "", "mediaList", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createThumbnail", "", "mediaId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllForEntry", "", "entryId", "deleteMedia", "id", "deleteMediaFile", "uri", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAttachmentsByEntryFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/dairum/domain/repository/MediaWithEntryInfo;", "getCountByType", "", "type", "Lcom/dairum/domain/model/MediaType;", "(Lcom/dairum/domain/model/MediaType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFileName", "Landroid/net/Uri;", "getFileSize", "getMediaById", "getMediaForEntry", "getMediaForEntryFlow", "getMimeTypeFromExtension", "fileName", "getPhotosByEntryFlow", "getSizeByType", "getTotalSize", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getVideosByEntryFlow", "getVoiceNotesByEntryFlow", "saveMediaFile", "(Ljava/lang/String;JLcom/dairum/domain/model/MediaType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMedia", "toDomain", "Lcom/dairum/data/local/entity/MediaEntity;", "Lcom/dairum/data/local/entity/MediaType;", "toDomainWithEntry", "Lcom/dairum/data/local/entity/MediaWithEntry;", "toEntity", "app_debug"})
public final class MediaRepositoryImpl implements com.dairum.domain.repository.MediaRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.MediaDao mediaDao = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    @javax.inject.Inject()
    public MediaRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.MediaDao mediaDao, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object addMedia(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Media media, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object addMediaList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Media> mediaList, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateMedia(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Media media, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteMedia(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteAllForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getMediaById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.Media> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getMediaForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.Media>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Media>> getMediaForEntryFlow(long entryId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.MediaWithEntryInfo>> getPhotosByEntryFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.MediaWithEntryInfo>> getVideosByEntryFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.MediaWithEntryInfo>> getVoiceNotesByEntryFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.MediaWithEntryInfo>> getAttachmentsByEntryFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getCountByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MediaType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTotalSize(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getSizeByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MediaType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveMediaFile(@org.jetbrains.annotations.NotNull()
    java.lang.String uri, long entryId, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MediaType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.Media> $completion) {
        return null;
    }
    
    private final java.lang.String getFileName(android.net.Uri uri) {
        return null;
    }
    
    private final long getFileSize(android.net.Uri uri) {
        return 0L;
    }
    
    private final java.lang.String getMimeTypeFromExtension(java.lang.String fileName) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createThumbnail(long mediaId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteMediaFile(@org.jetbrains.annotations.NotNull()
    java.lang.String uri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.Media toDomain(com.dairum.data.local.entity.MediaEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.repository.MediaWithEntryInfo toDomainWithEntry(com.dairum.data.local.entity.MediaWithEntry $this$toDomainWithEntry) {
        return null;
    }
    
    private final com.dairum.data.local.entity.MediaEntity toEntity(com.dairum.domain.model.Media $this$toEntity) {
        return null;
    }
    
    private final com.dairum.domain.model.MediaType toDomain(com.dairum.data.local.entity.MediaType $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.MediaType toEntity(com.dairum.domain.model.MediaType $this$toEntity) {
        return null;
    }
}