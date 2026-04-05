package com.dairum.data.repository;

import com.dairum.data.local.dao.*;
import com.dairum.data.local.entity.*;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.JournalRepository;
import kotlinx.coroutines.flow.Flow;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00be\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010$\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u001e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0018H\u0096@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0014\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001dH\u0016J\u0014\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001dH\u0016J\u0014\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001dH\u0016J\u001c\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001d2\u0006\u0010\"\u001a\u00020\u0012H\u0016J\u001c\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001d2\u0006\u0010$\u001a\u00020%H\u0016J\u001c\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001d2\u0006\u0010\'\u001a\u00020\u0012H\u0016J$\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00180\u001e2\u0006\u0010)\u001a\u00020\u00122\u0006\u0010*\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010+J$\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001d2\u0006\u0010-\u001a\u00020\u00122\u0006\u0010.\u001a\u00020\u0012H\u0016J\u001c\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001d2\u0006\u00100\u001a\u000201H\u0016J\u0018\u00102\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0011\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u000e\u00103\u001a\u00020%H\u0096@\u00a2\u0006\u0002\u00104J\u0018\u00105\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u001d2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0014\u00106\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001dH\u0016J\u0012\u00107\u001a\u0004\u0018\u0001082\u0006\u00109\u001a\u000201H\u0002J\u0012\u0010:\u001a\u0004\u0018\u00010;2\u0006\u00109\u001a\u000201H\u0002J\u001c\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001e0\u001d2\u0006\u0010=\u001a\u000201H\u0016J\u0010\u0010>\u001a\u0002012\u0006\u0010?\u001a\u000208H\u0002J\u0010\u0010@\u001a\u0002012\u0006\u0010A\u001a\u00020;H\u0002J$\u0010B\u001a\u00020\u00102\u0006\u0010C\u001a\u00020\u00122\f\u0010D\u001a\b\u0012\u0004\u0012\u00020\u00120\u001eH\u0096@\u00a2\u0006\u0002\u0010EJ$\u0010F\u001a\u00020\u00102\u0006\u0010C\u001a\u00020\u00122\f\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00120\u001eH\u0096@\u00a2\u0006\u0002\u0010EJ*\u0010H\u001a\u00020\u00102\u0006\u0010C\u001a\u00020\u00122\u0012\u0010I\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u0002010JH\u0096@\u00a2\u0006\u0002\u0010KJ\u001e\u0010L\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010M\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u001e\u0010N\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010O\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010P\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u0018H\u0096@\u00a2\u0006\u0002\u0010\u0019J\f\u0010Q\u001a\u00020R*\u00020SH\u0002J\f\u0010Q\u001a\u00020\u0018*\u00020TH\u0002J\f\u0010Q\u001a\u00020U*\u00020VH\u0002J\f\u0010Q\u001a\u00020W*\u00020XH\u0002J\f\u0010Q\u001a\u00020Y*\u00020ZH\u0002J\f\u0010Q\u001a\u00020[*\u00020\\H\u0002J\f\u0010]\u001a\u00020\u0018*\u00020^H\u0002J\f\u0010_\u001a\u00020^*\u00020\u0018H\u0002J\f\u0010`\u001a\u00020\u0012*\u00020aH\u0002J\f\u0010b\u001a\u00020a*\u00020\u0012H\u0002R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006c"}, d2 = {"Lcom/dairum/data/repository/JournalRepositoryImpl;", "Lcom/dairum/domain/repository/JournalRepository;", "entryDao", "Lcom/dairum/data/local/dao/JournalEntryDao;", "tagDao", "Lcom/dairum/data/local/dao/TagDao;", "personDao", "Lcom/dairum/data/local/dao/PersonDao;", "trackerDao", "Lcom/dairum/data/local/dao/TrackerDao;", "mediaDao", "Lcom/dairum/data/local/dao/MediaDao;", "categoryDao", "Lcom/dairum/data/local/dao/CategoryDao;", "(Lcom/dairum/data/local/dao/JournalEntryDao;Lcom/dairum/data/local/dao/TagDao;Lcom/dairum/data/local/dao/PersonDao;Lcom/dairum/data/local/dao/TrackerDao;Lcom/dairum/data/local/dao/MediaDao;Lcom/dairum/data/local/dao/CategoryDao;)V", "archiveEntry", "", "id", "", "isArchived", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createEntry", "entry", "Lcom/dairum/domain/model/JournalEntry;", "(Lcom/dairum/domain/model/JournalEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEntry", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEntries", "Lkotlinx/coroutines/flow/Flow;", "", "getArchivedEntries", "getDraftEntries", "getEntriesByCategory", "categoryId", "getEntriesByMood", "moodLevel", "", "getEntriesByTag", "tagId", "getEntriesInDateRange", "startDate", "endDate", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntriesInDateRangeFlow", "startTime", "endTime", "getEntriesOnThisDay", "monthDay", "", "getEntry", "getEntryCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntryFlow", "getFavoriteEntries", "parseLocationJson", "Lcom/dairum/domain/model/Location;", "json", "parseWeatherJson", "Lcom/dairum/domain/model/Weather;", "searchEntries", "query", "serializeLocationJson", "location", "serializeWeatherJson", "weather", "setPeopleForEntry", "entryId", "personIds", "(JLjava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTagsForEntry", "tagIds", "setTrackerValues", "values", "", "(JLjava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleFavorite", "isFavorite", "togglePin", "isPinned", "updateEntry", "toDomain", "Lcom/dairum/domain/model/Category;", "Lcom/dairum/data/local/entity/CategoryEntity;", "Lcom/dairum/data/local/entity/EntryWithDetails;", "Lcom/dairum/domain/model/Media;", "Lcom/dairum/data/local/entity/MediaEntity;", "Lcom/dairum/domain/model/MediaType;", "Lcom/dairum/data/local/entity/MediaType;", "Lcom/dairum/domain/model/Person;", "Lcom/dairum/data/local/entity/PersonEntity;", "Lcom/dairum/domain/model/Tag;", "Lcom/dairum/data/local/entity/TagEntity;", "toDomainBasic", "Lcom/dairum/data/local/entity/JournalEntryEntity;", "toEntity", "toEpochMillis", "Ljava/time/LocalDateTime;", "toLocalDateTime", "app_debug"})
public final class JournalRepositoryImpl implements com.dairum.domain.repository.JournalRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.JournalEntryDao entryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.TagDao tagDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.PersonDao personDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.TrackerDao trackerDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.MediaDao mediaDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.CategoryDao categoryDao = null;
    
    @javax.inject.Inject()
    public JournalRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.JournalEntryDao entryDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.TagDao tagDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.PersonDao personDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.TrackerDao trackerDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.MediaDao mediaDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.CategoryDao categoryDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createEntry(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.JournalEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateEntry(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.JournalEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteEntry(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEntry(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.JournalEntry> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.model.JournalEntry> getEntryFlow(long id) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEntryCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getAllEntries() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesByCategory(long categoryId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesByMood(int moodLevel) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesByTag(long tagId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getFavoriteEntries() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getDraftEntries() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getArchivedEntries() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> searchEntries(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesInDateRangeFlow(long startTime, long endTime) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getEntriesInDateRange(long startDate, long endDate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.JournalEntry>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesOnThisDay(@org.jetbrains.annotations.NotNull()
    java.lang.String monthDay) {
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
    public java.lang.Object archiveEntry(long id, boolean isArchived, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object togglePin(long id, boolean isPinned, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setTagsForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> tagIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setPeopleForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> personIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setTrackerValues(long entryId, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> values, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.JournalEntry toDomain(com.dairum.data.local.entity.EntryWithDetails $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.JournalEntry toDomainBasic(com.dairum.data.local.entity.JournalEntryEntity $this$toDomainBasic) {
        return null;
    }
    
    private final com.dairum.data.local.entity.JournalEntryEntity toEntity(com.dairum.domain.model.JournalEntry $this$toEntity) {
        return null;
    }
    
    private final com.dairum.domain.model.Category toDomain(com.dairum.data.local.entity.CategoryEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.Tag toDomain(com.dairum.data.local.entity.TagEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.Media toDomain(com.dairum.data.local.entity.MediaEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.Person toDomain(com.dairum.data.local.entity.PersonEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.MediaType toDomain(com.dairum.data.local.entity.MediaType $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.Weather parseWeatherJson(java.lang.String json) {
        return null;
    }
    
    private final com.dairum.domain.model.Location parseLocationJson(java.lang.String json) {
        return null;
    }
    
    private final java.lang.String serializeWeatherJson(com.dairum.domain.model.Weather weather) {
        return null;
    }
    
    private final java.lang.String serializeLocationJson(com.dairum.domain.model.Location location) {
        return null;
    }
    
    private final java.time.LocalDateTime toLocalDateTime(long $this$toLocalDateTime) {
        return null;
    }
    
    private final long toEpochMillis(java.time.LocalDateTime $this$toEpochMillis) {
        return 0L;
    }
}