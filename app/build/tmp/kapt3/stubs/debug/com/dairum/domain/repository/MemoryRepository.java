package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000fH\u00a6@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013H\u00a6@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0017J\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00072\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0017J\u0014\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\u001dH&J\u001c\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\u00070\u001d2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0014\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\u001dH&J\u001c\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\u001d2\u0006\u0010\"\u001a\u00020\u0005H&J$\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\u001d2\u0006\u0010$\u001a\u00020\u00052\u0006\u0010%\u001a\u00020\u0005H&J\u0018\u0010&\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0016\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0017J\u0018\u0010\'\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u001d2\u0006\u0010\u0016\u001a\u00020\u0005H&J\u001c\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u001d2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u000e\u0010)\u001a\u00020*H\u00a6@\u00a2\u0006\u0002\u0010+J\u000e\u0010,\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010+J\u001c\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00070\u001d2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0016\u0010.\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0017J\u001e\u0010/\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u00100J\u001c\u00101\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\u001d2\u0006\u00102\u001a\u00020\u001bH&J\u001c\u00103\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00072\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0017J\u001e\u00104\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u00105\u001a\u000206H\u00a6@\u00a2\u0006\u0002\u00107J\u0016\u00108\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0013H\u00a6@\u00a2\u0006\u0002\u0010\u0014J\u0016\u00109\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010:\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u00a6@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006;"}, d2 = {"Lcom/dairum/domain/repository/MemoryRepository;", "", "addEntriesToMemory", "", "memoryId", "", "entryIds", "", "(JLjava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addMemoryTracker", "tracker", "Lcom/dairum/domain/model/MemoryTracker;", "(Lcom/dairum/domain/model/MemoryTracker;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addMilestone", "milestone", "Lcom/dairum/domain/model/MemoryMilestone;", "(Lcom/dairum/domain/model/MemoryMilestone;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createMemory", "memory", "Lcom/dairum/domain/model/Memory;", "(Lcom/dairum/domain/model/Memory;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMemory", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMemoryTracker", "deleteMilestone", "generateMemoryHighlights", "", "getAllMemoriesFlow", "Lkotlinx/coroutines/flow/Flow;", "getEntriesForMemory", "Lcom/dairum/domain/model/JournalEntry;", "getFavoriteMemoriesFlow", "getMemoriesForEntry", "entryId", "getMemoriesInDateRange", "startDate", "endDate", "getMemoryById", "getMemoryByIdFlow", "getMilestonesForMemory", "getTotalMemoryCount", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTotalTimeSpentInMemories", "getTrackersForMemory", "recalculateMemoryStats", "removeEntryFromMemory", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchMemories", "query", "suggestEntriesForMemory", "toggleFavorite", "isFavorite", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMemory", "updateMemoryTracker", "updateMilestone", "app_debug"})
public abstract interface MemoryRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createMemory(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Memory memory, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMemory(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Memory memory, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMemory(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMemoryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.Memory> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.model.Memory> getMemoryByIdFlow(long id);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> getAllMemoriesFlow();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> getFavoriteMemoriesFlow();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> getMemoriesInDateRange(long startDate, long endDate);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> searchMemories(@org.jetbrains.annotations.NotNull()
    java.lang.String query);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addEntriesToMemory(long memoryId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> entryIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object removeEntryFromMemory(long memoryId, long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesForMemory(long memoryId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> getMemoriesForEntry(long entryId);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addMilestone(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryMilestone milestone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMilestone(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryMilestone milestone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMilestone(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.MemoryMilestone>> getMilestonesForMemory(long memoryId);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addMemoryTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryTracker tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMemoryTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryTracker tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMemoryTracker(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.MemoryTracker>> getTrackersForMemory(long memoryId);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object toggleFavorite(long id, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object recalculateMemoryStats(long memoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalMemoryCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalTimeSpentInMemories(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object suggestEntriesForMemory(long memoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.JournalEntry>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object generateMemoryHighlights(long memoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion);
}