package com.dairum.data.repository;

import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.MediaDao;
import com.dairum.data.local.dao.MemoryDao;
import com.dairum.data.local.entity.*;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.MemoryRepository;
import kotlinx.coroutines.flow.Flow;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ$\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000eH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010 \u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u001c\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\u000e2\u0006\u0010\u000b\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u0014\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u000e0$H\u0016J\u001c\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0\u000e0$2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0014\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u000e0$H\u0016J\u001c\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u000e0$2\u0006\u0010)\u001a\u00020\fH\u0016J$\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u000e0$2\u0006\u0010+\u001a\u00020\f2\u0006\u0010,\u001a\u00020\fH\u0016J\u0018\u0010-\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001d\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u0018\u0010.\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0$2\u0006\u0010\u001d\u001a\u00020\fH\u0016J\u001c\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u000e0$2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u000e\u00100\u001a\u000201H\u0096@\u00a2\u0006\u0002\u00102J\u000e\u00103\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u00102J\u001c\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u000e0$2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0016\u00105\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u001e\u00106\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010)\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u00107J\u001c\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u000e0$2\u0006\u00109\u001a\u00020\"H\u0016J\u001c\u0010:\u001a\b\u0012\u0004\u0012\u00020&0\u000e2\u0006\u0010\u000b\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u001e\u0010;\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\f2\u0006\u0010<\u001a\u00020=H\u0096@\u00a2\u0006\u0002\u0010>J\u0016\u0010?\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010@\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010A\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0017J\f\u0010B\u001a\u00020&*\u00020CH\u0002J\f\u0010D\u001a\u00020E*\u00020FH\u0002J<\u0010D\u001a\u00020\u001a*\u00020G2\u000e\b\u0002\u0010H\u001a\b\u0012\u0004\u0012\u00020&0\u000e2\u000e\b\u0002\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00160\u000e2\u000e\b\u0002\u0010J\u001a\b\u0012\u0004\u0012\u00020\u00120\u000eH\u0002J\f\u0010D\u001a\u00020\u0016*\u00020KH\u0002J\f\u0010D\u001a\u00020\u0012*\u00020LH\u0002J\f\u0010M\u001a\u00020G*\u00020\u001aH\u0002J\f\u0010M\u001a\u00020K*\u00020\u0016H\u0002J\f\u0010M\u001a\u00020L*\u00020\u0012H\u0002J\u0012\u0010N\u001a\u00020&*\u00020CH\u0082@\u00a2\u0006\u0002\u0010OR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006P"}, d2 = {"Lcom/dairum/data/repository/MemoryRepositoryImpl;", "Lcom/dairum/domain/repository/MemoryRepository;", "memoryDao", "Lcom/dairum/data/local/dao/MemoryDao;", "journalEntryDao", "Lcom/dairum/data/local/dao/JournalEntryDao;", "mediaDao", "Lcom/dairum/data/local/dao/MediaDao;", "(Lcom/dairum/data/local/dao/MemoryDao;Lcom/dairum/data/local/dao/JournalEntryDao;Lcom/dairum/data/local/dao/MediaDao;)V", "addEntriesToMemory", "", "memoryId", "", "entryIds", "", "(JLjava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addMemoryTracker", "tracker", "Lcom/dairum/domain/model/MemoryTracker;", "(Lcom/dairum/domain/model/MemoryTracker;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addMilestone", "milestone", "Lcom/dairum/domain/model/MemoryMilestone;", "(Lcom/dairum/domain/model/MemoryMilestone;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createMemory", "memory", "Lcom/dairum/domain/model/Memory;", "(Lcom/dairum/domain/model/Memory;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMemory", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMemoryTracker", "deleteMilestone", "generateMemoryHighlights", "", "getAllMemoriesFlow", "Lkotlinx/coroutines/flow/Flow;", "getEntriesForMemory", "Lcom/dairum/domain/model/JournalEntry;", "getFavoriteMemoriesFlow", "getMemoriesForEntry", "entryId", "getMemoriesInDateRange", "startDate", "endDate", "getMemoryById", "getMemoryByIdFlow", "getMilestonesForMemory", "getTotalMemoryCount", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTotalTimeSpentInMemories", "getTrackersForMemory", "recalculateMemoryStats", "removeEntryFromMemory", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchMemories", "query", "suggestEntriesForMemory", "toggleFavorite", "isFavorite", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMemory", "updateMemoryTracker", "updateMilestone", "toBasicDomain", "Lcom/dairum/data/local/entity/JournalEntryEntity;", "toDomain", "Lcom/dairum/domain/model/Media;", "Lcom/dairum/data/local/entity/MediaEntity;", "Lcom/dairum/data/local/entity/MemoryEntity;", "entries", "milestones", "trackers", "Lcom/dairum/data/local/entity/MemoryMilestoneEntity;", "Lcom/dairum/data/local/entity/MemoryTrackerEntity;", "toEntity", "toEntryWithMedia", "(Lcom/dairum/data/local/entity/JournalEntryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class MemoryRepositoryImpl implements com.dairum.domain.repository.MemoryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.MemoryDao memoryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.JournalEntryDao journalEntryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.MediaDao mediaDao = null;
    
    @javax.inject.Inject()
    public MemoryRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.MemoryDao memoryDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.JournalEntryDao journalEntryDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.MediaDao mediaDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createMemory(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Memory memory, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateMemory(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Memory memory, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteMemory(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getMemoryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.Memory> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.model.Memory> getMemoryByIdFlow(long id) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> getAllMemoriesFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> getFavoriteMemoriesFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> getMemoriesInDateRange(long startDate, long endDate) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> searchMemories(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object addEntriesToMemory(long memoryId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> entryIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object removeEntryFromMemory(long memoryId, long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesForMemory(long memoryId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Memory>> getMemoriesForEntry(long entryId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object addMilestone(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryMilestone milestone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateMilestone(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryMilestone milestone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteMilestone(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.MemoryMilestone>> getMilestonesForMemory(long memoryId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object addMemoryTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryTracker tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateMemoryTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryTracker tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteMemoryTracker(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.MemoryTracker>> getTrackersForMemory(long memoryId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object toggleFavorite(long id, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object recalculateMemoryStats(long memoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTotalMemoryCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTotalTimeSpentInMemories(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object suggestEntriesForMemory(long memoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.JournalEntry>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object generateMemoryHighlights(long memoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion) {
        return null;
    }
    
    private final com.dairum.data.local.entity.MemoryEntity toEntity(com.dairum.domain.model.Memory $this$toEntity) {
        return null;
    }
    
    private final com.dairum.domain.model.Memory toDomain(com.dairum.data.local.entity.MemoryEntity $this$toDomain, java.util.List<com.dairum.domain.model.JournalEntry> entries, java.util.List<com.dairum.domain.model.MemoryMilestone> milestones, java.util.List<com.dairum.domain.model.MemoryTracker> trackers) {
        return null;
    }
    
    private final com.dairum.data.local.entity.MemoryMilestoneEntity toEntity(com.dairum.domain.model.MemoryMilestone $this$toEntity) {
        return null;
    }
    
    private final com.dairum.domain.model.MemoryMilestone toDomain(com.dairum.data.local.entity.MemoryMilestoneEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.MemoryTrackerEntity toEntity(com.dairum.domain.model.MemoryTracker $this$toEntity) {
        return null;
    }
    
    private final com.dairum.domain.model.MemoryTracker toDomain(com.dairum.data.local.entity.MemoryTrackerEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.JournalEntry toBasicDomain(com.dairum.data.local.entity.JournalEntryEntity $this$toBasicDomain) {
        return null;
    }
    
    /**
     * Convert JournalEntryEntity to JournalEntry with media included
     */
    private final java.lang.Object toEntryWithMedia(com.dairum.data.local.entity.JournalEntryEntity $this$toEntryWithMedia, kotlin.coroutines.Continuation<? super com.dairum.domain.model.JournalEntry> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.Media toDomain(com.dairum.data.local.entity.MediaEntity $this$toDomain) {
        return null;
    }
}