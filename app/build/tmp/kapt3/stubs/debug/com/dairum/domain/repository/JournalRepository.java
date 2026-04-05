package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010$\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u0010H&J\u0014\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u0010H&J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u0010H&J\u001c\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u00102\u0006\u0010\u0015\u001a\u00020\u0005H&J\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u00102\u0006\u0010\u0017\u001a\u00020\u0018H&J\u001c\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u00102\u0006\u0010\u001a\u001a\u00020\u0005H&J$\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00112\u0006\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u001eJ$\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u00102\u0006\u0010 \u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u0005H&J\u001c\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u00102\u0006\u0010#\u001a\u00020$H&J\u0018\u0010%\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010&\u001a\u00020\u0018H\u00a6@\u00a2\u0006\u0002\u0010\'J\u0018\u0010(\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u00102\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0014\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u0010H&J\u001c\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u00102\u0006\u0010+\u001a\u00020$H&J$\u0010,\u001a\u00020\u00032\u0006\u0010-\u001a\u00020\u00052\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00050\u0011H\u00a6@\u00a2\u0006\u0002\u0010/J$\u00100\u001a\u00020\u00032\u0006\u0010-\u001a\u00020\u00052\f\u00101\u001a\b\u0012\u0004\u0012\u00020\u00050\u0011H\u00a6@\u00a2\u0006\u0002\u0010/J*\u00102\u001a\u00020\u00032\u0006\u0010-\u001a\u00020\u00052\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020$04H\u00a6@\u00a2\u0006\u0002\u00105J\u001e\u00106\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u00107\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u001e\u00108\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u00109\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010:\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\f\u00a8\u0006;"}, d2 = {"Lcom/dairum/domain/repository/JournalRepository;", "", "archiveEntry", "", "id", "", "isArchived", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createEntry", "entry", "Lcom/dairum/domain/model/JournalEntry;", "(Lcom/dairum/domain/model/JournalEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEntry", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEntries", "Lkotlinx/coroutines/flow/Flow;", "", "getArchivedEntries", "getDraftEntries", "getEntriesByCategory", "categoryId", "getEntriesByMood", "moodLevel", "", "getEntriesByTag", "tagId", "getEntriesInDateRange", "startDate", "endDate", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntriesInDateRangeFlow", "startTime", "endTime", "getEntriesOnThisDay", "monthDay", "", "getEntry", "getEntryCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntryFlow", "getFavoriteEntries", "searchEntries", "query", "setPeopleForEntry", "entryId", "personIds", "(JLjava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTagsForEntry", "tagIds", "setTrackerValues", "values", "", "(JLjava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleFavorite", "isFavorite", "togglePin", "isPinned", "updateEntry", "app_debug"})
public abstract interface JournalRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createEntry(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.JournalEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEntry(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.JournalEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEntry(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntry(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.JournalEntry> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.model.JournalEntry> getEntryFlow(long id);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntryCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getAllEntries();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesByCategory(long categoryId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesByMood(int moodLevel);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesByTag(long tagId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getFavoriteEntries();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getDraftEntries();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getArchivedEntries();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> searchEntries(@org.jetbrains.annotations.NotNull()
    java.lang.String query);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesInDateRangeFlow(long startTime, long endTime);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntriesInDateRange(long startDate, long endDate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.JournalEntry>> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesOnThisDay(@org.jetbrains.annotations.NotNull()
    java.lang.String monthDay);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object toggleFavorite(long id, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object togglePin(long id, boolean isPinned, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object archiveEntry(long id, boolean isArchived, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setTagsForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> tagIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setPeopleForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> personIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setTrackerValues(long entryId, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> values, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}