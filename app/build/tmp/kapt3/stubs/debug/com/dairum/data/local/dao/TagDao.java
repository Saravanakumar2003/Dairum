package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.EntryTagCrossRef;
import com.dairum.data.local.entity.TagEntity;
import com.dairum.data.local.entity.TagWithCount;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\r\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00130\u0016H\'J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00130\u0016H\'J\u0018\u0010\u0019\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u001a\u001a\u0004\u0018\u00010\t2\u0006\u0010\u001b\u001a\u00020\u001cH\u00a7@\u00a2\u0006\u0002\u0010\u001dJ\u001c\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00050\u00132\u0006\u0010\u001f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00050\u00132\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010!\u001a\b\u0012\u0004\u0012\u00020\t0\u00132\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00130\u00162\u0006\u0010\f\u001a\u00020\u0005H\'J\u001c\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00180\u00132\u0006\u0010$\u001a\u00020%H\u00a7@\u00a2\u0006\u0002\u0010&J$\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00180\u00132\u0006\u0010$\u001a\u00020%2\u0006\u0010(\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010)J\u0016\u0010*\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010+\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010,\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010-\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ&\u0010.\u001a\b\u0012\u0004\u0012\u00020\t0\u00132\u0006\u0010/\u001a\u00020\u001c2\b\b\u0002\u0010$\u001a\u00020%H\u00a7@\u00a2\u0006\u0002\u00100J\u0016\u00101\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u00062"}, d2 = {"Lcom/dairum/data/local/dao/TagDao;", "", "decrementUsageCount", "", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "tag", "Lcom/dairum/data/local/entity/TagEntity;", "(Lcom/dairum/data/local/entity/TagEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllTagsForEntry", "entryId", "deleteById", "deleteEntryTag", "crossRef", "Lcom/dairum/data/local/entity/EntryTagCrossRef;", "(Lcom/dairum/data/local/entity/EntryTagCrossRef;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllWithCountFlow", "Lcom/dairum/data/local/entity/TagWithCount;", "getById", "getByName", "name", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntryIdsForTag", "tagId", "getTagIdsForEntry", "getTagsForEntry", "getTagsForEntryFlow", "getTopTags", "limit", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTopTagsSince", "startTime", "(IJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "incrementUsageCount", "insert", "insertEntryTag", "insertOrIgnore", "search", "query", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface TagDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.TagEntity tag, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertOrIgnore(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.TagEntity tag, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.TagEntity tag, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.TagEntity tag, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM tags WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM tags WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.TagEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM tags WHERE name = :name LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByName(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.TagEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM tags ORDER BY usageCount DESC, name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.TagEntity>> getAllFlow();
    
    @androidx.room.Query(value = "SELECT * FROM tags ORDER BY usageCount DESC, name ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.TagEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM tags WHERE name LIKE \'%\' || :query || \'%\' ORDER BY usageCount DESC LIMIT :limit")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object search(@org.jetbrains.annotations.NotNull()
    java.lang.String query, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.TagEntity>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT t.*, COUNT(et.entryId) as entryCount \n        FROM tags t\n        LEFT JOIN entry_tags et ON t.id = et.tagId\n        LEFT JOIN journal_entries e ON et.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0\n        GROUP BY t.id\n        ORDER BY entryCount DESC, t.name ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.TagWithCount>> getAllWithCountFlow();
    
    @androidx.room.Query(value = "\n        SELECT t.*, COUNT(et.entryId) as entryCount \n        FROM tags t\n        LEFT JOIN entry_tags et ON t.id = et.tagId\n        LEFT JOIN journal_entries e ON et.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0\n        GROUP BY t.id\n        ORDER BY entryCount DESC\n        LIMIT :limit\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTopTags(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.TagWithCount>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT t.*, COUNT(et.entryId) as entryCount \n        FROM tags t\n        LEFT JOIN entry_tags et ON t.id = et.tagId\n        LEFT JOIN journal_entries e ON et.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0\n            AND e.createdAt >= :startTime\n        GROUP BY t.id\n        ORDER BY entryCount DESC\n        LIMIT :limit\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTopTagsSince(int limit, long startTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.TagWithCount>> $completion);
    
    @androidx.room.Query(value = "UPDATE tags SET usageCount = usageCount + 1 WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementUsageCount(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE tags SET usageCount = usageCount - 1 WHERE id = :id AND usageCount > 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object decrementUsageCount(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEntryTag(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.EntryTagCrossRef crossRef, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEntryTag(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.EntryTagCrossRef crossRef, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM entry_tags WHERE entryId = :entryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllTagsForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT tagId FROM entry_tags WHERE entryId = :entryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTagIdsForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT t.* FROM tags t\n        INNER JOIN entry_tags et ON t.id = et.tagId\n        WHERE et.entryId = :entryId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTagsForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.TagEntity>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT t.* FROM tags t\n        INNER JOIN entry_tags et ON t.id = et.tagId\n        WHERE et.entryId = :entryId\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.TagEntity>> getTagsForEntryFlow(long entryId);
    
    @androidx.room.Query(value = "\n        SELECT e.id FROM journal_entries e\n        INNER JOIN entry_tags et ON e.id = et.entryId\n        WHERE et.tagId = :tagId AND e.isArchived = 0 AND e.isDraft = 0\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntryIdsForTag(long tagId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}