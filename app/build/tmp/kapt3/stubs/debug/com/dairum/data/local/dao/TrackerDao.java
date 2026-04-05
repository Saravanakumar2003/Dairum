package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.CustomTrackerEntity;
import com.dairum.data.local.entity.TrackerType;
import com.dairum.data.local.entity.TrackerValueEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\t0\u0016H\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u0014\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00160\u0019H\'J\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\t0\u0016H\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u0014\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00160\u0019H\'J\u0018\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u0018\u0010 \u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010!\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\"\u001a\u0004\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010#\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00162\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010$\u001a\b\u0012\u0004\u0012\u00020\t0\u00162\u0006\u0010%\u001a\u00020&H\u00a7@\u00a2\u0006\u0002\u0010\'J\u0016\u0010(\u001a\u00020\u001f2\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J \u0010)\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010*\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00162\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00160\u00192\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u001c\u0010,\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00162\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00160\u00192\u0006\u0010\u0013\u001a\u00020\u0005H\'J\u0016\u0010.\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010/\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0010J\"\u00100\u001a\b\u0012\u0004\u0012\u00020\u00050\u00162\f\u00101\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0016H\u00a7@\u00a2\u0006\u0002\u00102J\u0016\u00103\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001e\u00104\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u00052\u0006\u00105\u001a\u000206H\u00a7@\u00a2\u0006\u0002\u00107J\u001e\u00108\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u00052\u0006\u00109\u001a\u00020\u001fH\u00a7@\u00a2\u0006\u0002\u0010:J\u0016\u0010;\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006<"}, d2 = {"Lcom/dairum/data/local/dao/TrackerDao;", "", "deleteAllValuesForEntry", "", "entryId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTracker", "tracker", "Lcom/dairum/data/local/entity/CustomTrackerEntity;", "(Lcom/dairum/data/local/entity/CustomTrackerEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTrackerById", "id", "deleteValue", "value", "Lcom/dairum/data/local/entity/TrackerValueEntity;", "(Lcom/dairum/data/local/entity/TrackerValueEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteValueById", "deleteValueForEntryAndTracker", "trackerId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveTrackers", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveTrackersFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllTrackers", "getAllTrackersFlow", "getAverageValue", "", "getMaxTrackerSortOrder", "", "getMaxValue", "getMinValue", "getTrackerById", "getTrackerValuesWithEntryDate", "getTrackersByType", "type", "Lcom/dairum/data/local/entity/TrackerType;", "(Lcom/dairum/data/local/entity/TrackerType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getValueCount", "getValueForEntryAndTracker", "getValuesForEntry", "getValuesForEntryFlow", "getValuesForTracker", "getValuesForTrackerFlow", "insertTracker", "insertValue", "insertValues", "values", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTracker", "updateTrackerActive", "isActive", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTrackerSortOrder", "sortOrder", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateValue", "app_debug"})
@androidx.room.Dao()
public abstract interface TrackerDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.CustomTrackerEntity tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.CustomTrackerEntity tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.CustomTrackerEntity tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM custom_trackers WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTrackerById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM custom_trackers WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTrackerById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.CustomTrackerEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM custom_trackers WHERE isActive = 1 ORDER BY sortOrder ASC, name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.CustomTrackerEntity>> getActiveTrackersFlow();
    
    @androidx.room.Query(value = "SELECT * FROM custom_trackers ORDER BY sortOrder ASC, name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.CustomTrackerEntity>> getAllTrackersFlow();
    
    @androidx.room.Query(value = "SELECT * FROM custom_trackers ORDER BY sortOrder ASC, name ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllTrackers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.CustomTrackerEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM custom_trackers WHERE isActive = 1 ORDER BY sortOrder ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveTrackers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.CustomTrackerEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM custom_trackers WHERE type = :type AND isActive = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTrackersByType(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.TrackerType type, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.CustomTrackerEntity>> $completion);
    
    @androidx.room.Query(value = "UPDATE custom_trackers SET isActive = :isActive WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTrackerActive(long id, boolean isActive, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE custom_trackers SET sortOrder = :sortOrder WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTrackerSortOrder(long id, int sortOrder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT MAX(sortOrder) FROM custom_trackers")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMaxTrackerSortOrder(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertValue(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.TrackerValueEntity value, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertValues(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.TrackerValueEntity> values, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Long>> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateValue(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.TrackerValueEntity value, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteValue(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.TrackerValueEntity value, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM tracker_values WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteValueById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM tracker_values WHERE entryId = :entryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllValuesForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM tracker_values WHERE entryId = :entryId AND trackerId = :trackerId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteValueForEntryAndTracker(long entryId, long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM tracker_values WHERE entryId = :entryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getValuesForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.TrackerValueEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM tracker_values WHERE entryId = :entryId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.TrackerValueEntity>> getValuesForEntryFlow(long entryId);
    
    @androidx.room.Query(value = "SELECT * FROM tracker_values WHERE trackerId = :trackerId ORDER BY createdAt DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getValuesForTracker(long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.TrackerValueEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM tracker_values WHERE trackerId = :trackerId ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.TrackerValueEntity>> getValuesForTrackerFlow(long trackerId);
    
    @androidx.room.Query(value = "\n        SELECT tv.* FROM tracker_values tv\n        INNER JOIN journal_entries e ON tv.entryId = e.id\n        WHERE tv.trackerId = :trackerId AND e.isArchived = 0 AND e.isDraft = 0\n        ORDER BY e.createdAt DESC\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTrackerValuesWithEntryDate(long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.TrackerValueEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM tracker_values WHERE entryId = :entryId AND trackerId = :trackerId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getValueForEntryAndTracker(long entryId, long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.TrackerValueEntity> $completion);
    
    @androidx.room.Query(value = "\n        SELECT AVG(CAST(value AS REAL)) FROM tracker_values \n        WHERE trackerId = :trackerId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAverageValue(long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Double> $completion);
    
    @androidx.room.Query(value = "\n        SELECT MIN(CAST(value AS REAL)) FROM tracker_values \n        WHERE trackerId = :trackerId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMinValue(long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Double> $completion);
    
    @androidx.room.Query(value = "\n        SELECT MAX(CAST(value AS REAL)) FROM tracker_values \n        WHERE trackerId = :trackerId\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMaxValue(long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Double> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM tracker_values WHERE trackerId = :trackerId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getValueCount(long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}