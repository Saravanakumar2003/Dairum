package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.EntryPersonCrossRef;
import com.dairum.data.local.entity.PersonEntity;
import com.dairum.data.local.entity.PersonWithCount;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00130\u0016H\'J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00130\u0016H\'J\u0018\u0010\u0019\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\t0\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\u00132\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00130\u00162\u0006\u0010\f\u001a\u00020\u0005H\'J\u001c\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00180\u00132\u0006\u0010\u001e\u001a\u00020\u001fH\u00a7@\u00a2\u0006\u0002\u0010 J\u0016\u0010!\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\"\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010#\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J&\u0010$\u001a\b\u0012\u0004\u0012\u00020\t0\u00132\u0006\u0010%\u001a\u00020&2\b\b\u0002\u0010\u001e\u001a\u00020\u001fH\u00a7@\u00a2\u0006\u0002\u0010\'J\u0016\u0010(\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001e\u0010)\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010*\u001a\u00020+H\u00a7@\u00a2\u0006\u0002\u0010,\u00a8\u0006-"}, d2 = {"Lcom/dairum/data/local/dao/PersonDao;", "", "decrementMentionCount", "", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "person", "Lcom/dairum/data/local/entity/PersonEntity;", "(Lcom/dairum/data/local/entity/PersonEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllPeopleForEntry", "entryId", "deleteById", "deleteEntryPerson", "crossRef", "Lcom/dairum/data/local/entity/EntryPersonCrossRef;", "(Lcom/dairum/data/local/entity/EntryPersonCrossRef;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllWithCountFlow", "Lcom/dairum/data/local/entity/PersonWithCount;", "getById", "getFavoritePeople", "getPeopleForEntry", "getPeopleForEntryFlow", "getTopPeople", "limit", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "incrementMentionCount", "insert", "insertEntryPerson", "search", "query", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "updateFavorite", "isFavorite", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface PersonDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.PersonEntity person, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.PersonEntity person, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.PersonEntity person, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM people WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM people WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.PersonEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM people ORDER BY isFavorite DESC, mentionCount DESC, name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.PersonEntity>> getAllFlow();
    
    @androidx.room.Query(value = "SELECT * FROM people ORDER BY mentionCount DESC, name ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.PersonEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM people WHERE name LIKE \'%\' || :query || \'%\' ORDER BY mentionCount DESC LIMIT :limit")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object search(@org.jetbrains.annotations.NotNull()
    java.lang.String query, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.PersonEntity>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT p.*, COUNT(ep.entryId) as entryCount \n        FROM people p\n        LEFT JOIN entry_people ep ON p.id = ep.personId\n        LEFT JOIN journal_entries e ON ep.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0\n        GROUP BY p.id\n        ORDER BY entryCount DESC, p.name ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.PersonWithCount>> getAllWithCountFlow();
    
    @androidx.room.Query(value = "\n        SELECT p.*, COUNT(ep.entryId) as entryCount \n        FROM people p\n        LEFT JOIN entry_people ep ON p.id = ep.personId\n        LEFT JOIN journal_entries e ON ep.entryId = e.id AND e.isArchived = 0 AND e.isDraft = 0\n        GROUP BY p.id\n        ORDER BY entryCount DESC\n        LIMIT :limit\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTopPeople(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.PersonWithCount>> $completion);
    
    @androidx.room.Query(value = "UPDATE people SET mentionCount = mentionCount + 1 WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementMentionCount(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE people SET mentionCount = mentionCount - 1 WHERE id = :id AND mentionCount > 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object decrementMentionCount(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE people SET isFavorite = :isFavorite WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateFavorite(long id, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEntryPerson(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.EntryPersonCrossRef crossRef, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEntryPerson(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.EntryPersonCrossRef crossRef, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM entry_people WHERE entryId = :entryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllPeopleForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT p.* FROM people p\n        INNER JOIN entry_people ep ON p.id = ep.personId\n        WHERE ep.entryId = :entryId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPeopleForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.PersonEntity>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT p.* FROM people p\n        INNER JOIN entry_people ep ON p.id = ep.personId\n        WHERE ep.entryId = :entryId\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.PersonEntity>> getPeopleForEntryFlow(long entryId);
    
    @androidx.room.Query(value = "SELECT * FROM people WHERE isFavorite = 1 ORDER BY name ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getFavoritePeople(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.PersonEntity>> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}