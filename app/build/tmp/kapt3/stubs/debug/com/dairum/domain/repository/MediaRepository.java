package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\"\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\fH\u00a6@\u00a2\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\b0\u0018H&J\u0016\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u00a6@\u00a2\u0006\u0002\u0010\u001eJ\u0018\u0010\u001f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0013\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u0011\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u001c\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\b0\u00182\u0006\u0010\u0011\u001a\u00020\u0003H&J\u0014\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\b0\u0018H&J\u0016\u0010#\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u001dH\u00a6@\u00a2\u0006\u0002\u0010\u001eJ\u000e\u0010$\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010%J\u0014\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\b0\u0018H&J\u0014\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\b0\u0018H&J&\u0010(\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u001dH\u00a6@\u00a2\u0006\u0002\u0010)J\u0016\u0010*\u001a\u00020\u00102\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006+"}, d2 = {"Lcom/dairum/domain/repository/MediaRepository;", "", "addMedia", "", "media", "Lcom/dairum/domain/model/Media;", "(Lcom/dairum/domain/model/Media;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addMediaList", "", "mediaList", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createThumbnail", "", "mediaId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllForEntry", "", "entryId", "deleteMedia", "id", "deleteMediaFile", "uri", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAttachmentsByEntryFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/dairum/domain/repository/MediaWithEntryInfo;", "getCountByType", "", "type", "Lcom/dairum/domain/model/MediaType;", "(Lcom/dairum/domain/model/MediaType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMediaById", "getMediaForEntry", "getMediaForEntryFlow", "getPhotosByEntryFlow", "getSizeByType", "getTotalSize", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getVideosByEntryFlow", "getVoiceNotesByEntryFlow", "saveMediaFile", "(Ljava/lang/String;JLcom/dairum/domain/model/MediaType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMedia", "app_debug"})
public abstract interface MediaRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addMedia(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Media media, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addMediaList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Media> mediaList, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMedia(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Media media, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMedia(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMediaById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.Media> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMediaForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.Media>> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Media>> getMediaForEntryFlow(long entryId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.MediaWithEntryInfo>> getPhotosByEntryFlow();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.MediaWithEntryInfo>> getVideosByEntryFlow();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.MediaWithEntryInfo>> getVoiceNotesByEntryFlow();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.repository.MediaWithEntryInfo>> getAttachmentsByEntryFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCountByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MediaType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalSize(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSizeByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MediaType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveMediaFile(@org.jetbrains.annotations.NotNull()
    java.lang.String uri, long entryId, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MediaType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.Media> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createThumbnail(long mediaId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMediaFile(@org.jetbrains.annotations.NotNull()
    java.lang.String uri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}