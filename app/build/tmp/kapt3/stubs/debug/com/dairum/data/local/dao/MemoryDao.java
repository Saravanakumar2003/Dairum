package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000e\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u0010H\'J\u001c\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00110\u00102\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u0010H\'J\u001c\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u00102\u0006\u0010\u0019\u001a\u00020\u0005H\'J$\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u00102\u0006\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u0005H\'J\u0018\u0010\u001d\u001a\u0004\u0018\u00010\u00122\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u001e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00102\u0006\u0010\b\u001a\u00020\u0005H\'J\u001c\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\u00110\u00102\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0010\u0010!\u001a\u0004\u0018\u00010\u0016H\u00a7@\u00a2\u0006\u0002\u0010\"J\u000e\u0010#\u001a\u00020\u0016H\u00a7@\u00a2\u0006\u0002\u0010\"J\u0010\u0010$\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00a2\u0006\u0002\u0010\"J\u001c\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0\u00110\u00102\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\'\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010)J\u001c\u0010*\u001a\u00020\u00032\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0011H\u00a7@\u00a2\u0006\u0002\u0010,J\u0016\u0010-\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010.\u001a\u00020\u00052\u0006\u0010/\u001a\u00020&H\u00a7@\u00a2\u0006\u0002\u00100J\u0016\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020 H\u00a7@\u00a2\u0006\u0002\u00103J\u001e\u00104\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u00105J\u001c\u00106\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u00102\u0006\u00107\u001a\u000208H\'J(\u00109\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010:\u001a\u00020;2\b\b\u0002\u0010<\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010=J\u0016\u0010>\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010)J@\u0010?\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010@\u001a\u00020\u00162\u0006\u0010A\u001a\u00020\u00162\u0006\u0010B\u001a\u00020\u00162\u0006\u0010C\u001a\u00020\u00162\b\b\u0002\u0010<\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010DJ\u0016\u0010E\u001a\u00020\u00032\u0006\u0010/\u001a\u00020&H\u00a7@\u00a2\u0006\u0002\u00100J\u0016\u0010F\u001a\u00020\u00032\u0006\u00102\u001a\u00020 H\u00a7@\u00a2\u0006\u0002\u00103\u00a8\u0006G"}, d2 = {"Lcom/dairum/data/local/dao/MemoryDao;", "", "deleteAllEntriesFromMemory", "", "memoryId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMemory", "id", "deleteMemoryEntry", "crossRef", "Lcom/dairum/data/local/entity/MemoryEntryCrossRef;", "(Lcom/dairum/data/local/entity/MemoryEntryCrossRef;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMemoryTracker", "deleteMilestone", "getAllMemoriesFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/dairum/data/local/entity/MemoryEntity;", "getEntriesForMemory", "Lcom/dairum/data/local/entity/JournalEntryEntity;", "getEntryCountForMemory", "", "getFavoriteMemoriesFlow", "getMemoriesForEntry", "entryId", "getMemoriesInDateRange", "startTime", "endTime", "getMemoryById", "getMemoryByIdFlow", "getMilestonesForMemory", "Lcom/dairum/data/local/entity/MemoryMilestoneEntity;", "getTotalEntriesInMemories", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTotalMemoryCount", "getTotalTimeSpentInMemories", "getTrackersForMemory", "Lcom/dairum/data/local/entity/MemoryTrackerEntity;", "insertMemory", "memory", "(Lcom/dairum/data/local/entity/MemoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMemoryEntries", "crossRefs", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMemoryEntry", "insertMemoryTracker", "tracker", "(Lcom/dairum/data/local/entity/MemoryTrackerEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMilestone", "milestone", "(Lcom/dairum/data/local/entity/MemoryMilestoneEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "removeEntryFromMemory", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchMemories", "query", "", "updateFavorite", "isFavorite", "", "updatedAt", "(JZJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMemory", "updateMemoryStats", "entryCount", "wordCount", "photoCount", "videoCount", "(JIIIIJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMemoryTracker", "updateMilestone", "app_debug"})
@androidx.room.Dao()
public abstract interface MemoryDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMemory(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MemoryEntity memory, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMemory(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MemoryEntity memory, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM memories WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMemory(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM memories WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMemoryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.MemoryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM memories WHERE id = :id")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.data.local.entity.MemoryEntity> getMemoryByIdFlow(long id);
    
    @androidx.room.Query(value = "SELECT * FROM memories ORDER BY startDate DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MemoryEntity>> getAllMemoriesFlow();
    
    @androidx.room.Query(value = "SELECT * FROM memories WHERE isFavorite = 1 ORDER BY startDate DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MemoryEntity>> getFavoriteMemoriesFlow();
    
    @androidx.room.Query(value = "\n        SELECT * FROM memories \n        WHERE startDate >= :startTime AND endDate <= :endTime\n        ORDER BY startDate DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MemoryEntity>> getMemoriesInDateRange(long startTime, long endTime);
    
    @androidx.room.Query(value = "UPDATE memories SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateFavorite(long id, boolean isFavorite, long updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMemoryEntry(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MemoryEntryCrossRef crossRef, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMemoryEntries(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.MemoryEntryCrossRef> crossRefs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMemoryEntry(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MemoryEntryCrossRef crossRef, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM memory_entries WHERE memoryId = :memoryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllEntriesFromMemory(long memoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM memory_entries WHERE memoryId = :memoryId AND entryId = :entryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object removeEntryFromMemory(long memoryId, long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT e.* FROM journal_entries e\n        INNER JOIN memory_entries me ON e.id = me.entryId\n        WHERE me.memoryId = :memoryId\n        ORDER BY me.sortOrder ASC, e.createdAt ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> getEntriesForMemory(long memoryId);
    
    @androidx.room.Query(value = "\n        SELECT m.* FROM memories m\n        INNER JOIN memory_entries me ON m.id = me.memoryId\n        WHERE me.entryId = :entryId\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MemoryEntity>> getMemoriesForEntry(long entryId);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM memory_entries WHERE memoryId = :memoryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntryCountForMemory(long memoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMilestone(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MemoryMilestoneEntity milestone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMilestone(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MemoryMilestoneEntity milestone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM memory_milestones WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMilestone(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM memory_milestones WHERE memoryId = :memoryId ORDER BY milestoneDate ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MemoryMilestoneEntity>> getMilestonesForMemory(long memoryId);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMemoryTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MemoryTrackerEntity tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMemoryTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MemoryTrackerEntity tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM memory_trackers WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMemoryTracker(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM memory_trackers WHERE memoryId = :memoryId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MemoryTrackerEntity>> getTrackersForMemory(long memoryId);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM memories")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalMemoryCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "\n        SELECT SUM(entryCount) FROM memories\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalEntriesInMemories(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "\n        SELECT SUM(totalTimeSpent) FROM memories\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalTimeSpentInMemories(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM memories \n        WHERE title LIKE \'%\' || :query || \'%\' OR description LIKE \'%\' || :query || \'%\'\n        ORDER BY startDate DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.MemoryEntity>> searchMemories(@org.jetbrains.annotations.NotNull()
    java.lang.String query);
    
    @androidx.room.Query(value = "\n        UPDATE memories SET \n            entryCount = :entryCount,\n            wordCount = :wordCount,\n            photoCount = :photoCount,\n            videoCount = :videoCount,\n            updatedAt = :updatedAt\n        WHERE id = :memoryId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMemoryStats(long memoryId, int entryCount, int wordCount, int photoCount, int videoCount, long updatedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}