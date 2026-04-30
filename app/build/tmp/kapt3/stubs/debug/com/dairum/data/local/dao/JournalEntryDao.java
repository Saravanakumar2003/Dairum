package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH\'J\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\r0\fH\'J\u0014\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH\'J\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0018\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\f2\u0006\u0010\b\u001a\u00020\tH\'J\u0016\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\r2\u0006\u0010\u0018\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\rH\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0014\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH\'J\u001c\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\u001f\u001a\u00020\tH\'J\u001c\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010!\u001a\u00020\u0017H\'J$\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010#\u001a\u00020\tH\'J$\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\r2\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010#\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010%J\u001c\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\'\u001a\u00020\u001cH\'J$\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00050\r2\u0006\u0010)\u001a\u00020\u00172\u0006\u0010*\u001a\u00020\u0017H\u00a7@\u00a2\u0006\u0002\u0010+J\u000e\u0010,\u001a\u00020\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u001e\u0010-\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010#\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010%J\u0014\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH\'J\u0014\u0010/\u001a\b\u0012\u0004\u0012\u0002000\rH\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u001c\u00101\u001a\b\u0012\u0004\u0012\u0002000\r2\u0006\u0010\u0018\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u00102\u001a\b\u0012\u0004\u0012\u0002030\rH\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u001c\u00104\u001a\b\u0012\u0004\u0012\u0002030\r2\u0006\u0010\u0018\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u000e\u00105\u001a\u00020\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0010\u00106\u001a\u0004\u0018\u00010\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0014\u00107\u001a\b\u0012\u0004\u0012\u0002080\rH\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u001c\u00109\u001a\b\u0012\u0004\u0012\u0002080\r2\u0006\u0010\u0018\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010:\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010;\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\f2\u0006\u0010\b\u001a\u00020\tH\'J\u0016\u0010<\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010>\u001a\u00020\u001cH\'J\u0016\u0010?\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010@\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010A\u001a\u00020BH\u00a7@\u00a2\u0006\u0002\u0010CJ\u001e\u0010D\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010E\u001a\u00020BH\u00a7@\u00a2\u0006\u0002\u0010CJ\u001e\u0010F\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010G\u001a\u00020BH\u00a7@\u00a2\u0006\u0002\u0010C\u00a8\u0006H"}, d2 = {"Lcom/dairum/data/local/dao/JournalEntryDao;", "", "delete", "", "entry", "Lcom/dairum/data/local/entity/JournalEntryEntity;", "(Lcom/dairum/data/local/entity/JournalEntryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEntriesFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getAllEntriesWithDetailsFlow", "Lcom/dairum/data/local/entity/EntryWithDetails;", "getArchivedEntriesFlow", "getAverageWordCount", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "getByIdFlow", "getCountSince", "", "startTime", "getDailyCounts", "Lcom/dairum/data/local/dao/DateCount;", "getDistinctEntryDates", "", "getDraftsFlow", "getEntriesByCategoryFlow", "categoryId", "getEntriesByMoodFlow", "moodLevel", "getEntriesInDateRange", "endTime", "getEntriesInDateRangeSync", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntriesOnThisDay", "monthDay", "getEntriesPaginated", "limit", "offset", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntryCount", "getEntryCountBetween", "getFavoriteEntriesFlow", "getHourlyDistribution", "Lcom/dairum/data/local/dao/HourCount;", "getHourlyDistributionSince", "getMoodDistribution", "Lcom/dairum/data/local/dao/MoodCount;", "getMoodDistributionSince", "getTotalCount", "getTotalWordCount", "getWeekdayDistribution", "Lcom/dairum/data/local/dao/WeekdayCount;", "getWeekdayDistributionSince", "getWithDetails", "getWithDetailsFlow", "insert", "searchEntries", "query", "update", "updateArchived", "isArchived", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateFavorite", "isFavorite", "updatePinned", "isPinned", "app_debug"})
@androidx.room.Dao()
public abstract interface JournalEntryDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.JournalEntryEntity entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.JournalEntryEntity entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.JournalEntryEntity entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM journal_entries WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM journal_entries WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.JournalEntryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM journal_entries WHERE id = :id")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.data.local.entity.JournalEntryEntity> getByIdFlow(long id);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM journal_entries WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getWithDetails(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.EntryWithDetails> $completion);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM journal_entries WHERE id = :id")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.data.local.entity.EntryWithDetails> getWithDetailsFlow(long id);
    
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> getAllEntriesFlow();
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.EntryWithDetails>> getAllEntriesWithDetailsFlow();
    
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n        LIMIT :limit OFFSET :offset\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntriesPaginated(int limit, int offset, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE categoryId = :categoryId AND isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> getEntriesByCategoryFlow(long categoryId);
    
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE moodLevel = :moodLevel AND isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> getEntriesByMoodFlow(int moodLevel);
    
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE isFavorite = 1 AND isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> getFavoriteEntriesFlow();
    
    @androidx.room.Query(value = "UPDATE journal_entries SET isFavorite = :isFavorite WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateFavorite(long id, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM journal_entries WHERE isDraft = 1 ORDER BY updatedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> getDraftsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM journal_entries WHERE isArchived = 1 ORDER BY updatedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> getArchivedEntriesFlow();
    
    @androidx.room.Query(value = "UPDATE journal_entries SET isArchived = :isArchived WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateArchived(long id, boolean isArchived, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE journal_entries SET isPinned = :isPinned WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updatePinned(long id, boolean isPinned, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE (title LIKE \'%\' || :query || \'%\' OR plainContent LIKE \'%\' || :query || \'%\')\n        AND isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> searchEntries(@org.jetbrains.annotations.NotNull()
    java.lang.String query);
    
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE createdAt >= :startTime AND createdAt < :endTime\n        AND isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> getEntriesInDateRange(long startTime, long endTime);
    
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE createdAt >= :startTime AND createdAt < :endTime\n        AND isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntriesInDateRangeSync(long startTime, long endTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM journal_entries \n        WHERE strftime(\'%m-%d\', createdAt/1000, \'unixepoch\', \'localtime\') = :monthDay\n        AND isArchived = 0 AND isDraft = 0\n        ORDER BY createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.JournalEntryEntity>> getEntriesOnThisDay(@org.jetbrains.annotations.NotNull()
    java.lang.String monthDay);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM journal_entries WHERE isArchived = 0 AND isDraft = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM journal_entries")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntryCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM journal_entries \n        WHERE createdAt >= :startTime AND isArchived = 0 AND isDraft = 0\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCountSince(long startTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "\n        SELECT AVG(wordCount) FROM journal_entries \n        WHERE isArchived = 0 AND isDraft = 0\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAverageWordCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Double> $completion);
    
    @androidx.room.Query(value = "\n        SELECT SUM(wordCount) FROM journal_entries \n        WHERE isArchived = 0 AND isDraft = 0\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalWordCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "\n        SELECT moodLevel, COUNT(*) as count \n        FROM journal_entries \n        WHERE moodLevel IS NOT NULL AND isArchived = 0 AND isDraft = 0\n        GROUP BY moodLevel\n        ORDER BY moodLevel\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMoodDistribution(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.dao.MoodCount>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT moodLevel, COUNT(*) as count \n        FROM journal_entries \n        WHERE moodLevel IS NOT NULL AND isArchived = 0 AND isDraft = 0\n        AND createdAt >= :startTime\n        GROUP BY moodLevel\n        ORDER BY moodLevel\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMoodDistributionSince(long startTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.dao.MoodCount>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT date(createdAt/1000, \'unixepoch\', \'localtime\') as date, COUNT(*) as count\n        FROM journal_entries\n        WHERE createdAt >= :startTime AND isArchived = 0 AND isDraft = 0\n        GROUP BY date\n        ORDER BY date\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDailyCounts(long startTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.dao.DateCount>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT CAST(strftime(\'%H\', createdAt/1000, \'unixepoch\', \'localtime\') AS INTEGER) as hour, \n               COUNT(*) as count\n        FROM journal_entries\n        WHERE isArchived = 0 AND isDraft = 0\n        GROUP BY hour\n        ORDER BY hour\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHourlyDistribution(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.dao.HourCount>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT CAST(strftime(\'%H\', createdAt/1000, \'unixepoch\', \'localtime\') AS INTEGER) as hour, \n               COUNT(*) as count\n        FROM journal_entries\n        WHERE isArchived = 0 AND isDraft = 0 AND createdAt >= :startTime\n        GROUP BY hour\n        ORDER BY hour\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHourlyDistributionSince(long startTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.dao.HourCount>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT CAST(strftime(\'%w\', createdAt/1000, \'unixepoch\', \'localtime\') AS INTEGER) as dayOfWeek,\n               COUNT(*) as count\n        FROM journal_entries\n        WHERE isArchived = 0 AND isDraft = 0\n        GROUP BY dayOfWeek\n        ORDER BY dayOfWeek\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getWeekdayDistribution(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.dao.WeekdayCount>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT CAST(strftime(\'%w\', createdAt/1000, \'unixepoch\', \'localtime\') AS INTEGER) as dayOfWeek,\n               COUNT(*) as count\n        FROM journal_entries\n        WHERE isArchived = 0 AND isDraft = 0 AND createdAt >= :startTime\n        GROUP BY dayOfWeek\n        ORDER BY dayOfWeek\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getWeekdayDistributionSince(long startTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.dao.WeekdayCount>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT DISTINCT date(createdAt/1000, \'unixepoch\', \'localtime\') as date\n        FROM journal_entries\n        WHERE isArchived = 0 AND isDraft = 0\n        ORDER BY date DESC\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDistinctEntryDates(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM journal_entries \n        WHERE createdAt >= :startTime AND createdAt < :endTime\n        AND isArchived = 0 AND isDraft = 0\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntryCountBetween(long startTime, long endTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}