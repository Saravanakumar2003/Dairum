package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.MediaEntity;
import com.dairum.data.local.entity.MediaType;
import com.dairum.data.local.entity.MediaWithEntry;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0016\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000eH\'J\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u00052\u0006\u0010\f\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\'J\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u000f2\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0006\u0010\b\u001a\u00020\tH\'J\u001c\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\'J,\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u0018\u0010 \u001a\u0004\u0018\u00010\t2\u0006\u0010\u0014\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u000e\u0010!\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\"J\u0010\u0010#\u001a\u0004\u0018\u00010\tH\u00a7@\u00a2\u0006\u0002\u0010\"J\u0016\u0010$\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\"\u0010%\u001a\b\u0012\u0004\u0012\u00020\t0\u000f2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00050\u000fH\u00a7@\u00a2\u0006\u0002\u0010&J\u0016\u0010\'\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010(\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\t2\u0006\u0010)\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010*\u00a8\u0006+"}, d2 = {"Lcom/dairum/data/local/dao/MediaDao;", "", "delete", "", "media", "Lcom/dairum/data/local/entity/MediaEntity;", "(Lcom/dairum/data/local/entity/MediaEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllForEntry", "entryId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteById", "id", "getAllMediaWithEntryFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/dairum/data/local/entity/MediaWithEntry;", "getById", "getCountByType", "", "type", "Lcom/dairum/data/local/entity/MediaType;", "(Lcom/dairum/data/local/entity/MediaType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFirstMediaForEntry", "getMediaByTypeFlow", "getMediaForEntry", "getMediaForEntryFlow", "getMediaWithEntryByTypeFlow", "getMediaWithEntryByTypePaginated", "limit", "offset", "(Lcom/dairum/data/local/entity/MediaType;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSizeByType", "getTotalCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTotalSize", "insert", "insertAll", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "updateSortOrder", "sortOrder", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface MediaDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MediaEntity media, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.MediaEntity> media, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MediaEntity media, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MediaEntity media, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM media WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM media WHERE entryId = :entryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM media WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.MediaEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM media WHERE entryId = :entryId ORDER BY sortOrder ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMediaForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.MediaEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM media WHERE entryId = :entryId ORDER BY sortOrder ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MediaEntity>> getMediaForEntryFlow(long entryId);
    
    @androidx.room.Query(value = "SELECT * FROM media WHERE type = :type ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MediaEntity>> getMediaByTypeFlow(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MediaType type);
    
    @androidx.room.Query(value = "\n        SELECT m.*, e.title as entryTitle, e.createdAt as entryCreatedAt\n        FROM media m\n        INNER JOIN journal_entries e ON m.entryId = e.id\n        WHERE m.type = :type\n        ORDER BY m.createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MediaWithEntry>> getMediaWithEntryByTypeFlow(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MediaType type);
    
    @androidx.room.Query(value = "\n        SELECT m.*, e.title as entryTitle, e.createdAt as entryCreatedAt\n        FROM media m\n        INNER JOIN journal_entries e ON m.entryId = e.id\n        WHERE m.type = :type\n        ORDER BY m.createdAt DESC\n        LIMIT :limit OFFSET :offset\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMediaWithEntryByTypePaginated(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MediaType type, int limit, int offset, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.MediaWithEntry>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT m.*, e.title as entryTitle, e.createdAt as entryCreatedAt\n        FROM media m\n        INNER JOIN journal_entries e ON m.entryId = e.id\n        ORDER BY m.createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MediaWithEntry>> getAllMediaWithEntryFlow();
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM media WHERE type = :type")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCountByType(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MediaType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM media")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT SUM(sizeBytes) FROM media")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalSize(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT SUM(sizeBytes) FROM media WHERE type = :type")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSizeByType(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MediaType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM media \n        WHERE entryId = :entryId \n        ORDER BY sortOrder ASC \n        LIMIT 1\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getFirstMediaForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.MediaEntity> $completion);
    
    @androidx.room.Query(value = "UPDATE media SET sortOrder = :sortOrder WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSortOrder(long id, int sortOrder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}