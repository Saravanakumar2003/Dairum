package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\nJ\u001e\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u0010H&J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u0013J\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u0010H&J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\u00052\u0006\u0010\t\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u0016\u001a\u00020\u00172\u0006\u0010\r\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\nJ\"\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u001a0\u00192\u0006\u0010\f\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\nJ\"\u0010\u001b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u001a0\u00190\u00102\u0006\u0010\f\u001a\u00020\u0003H&J&\u0010\u001c\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u001aH\u00a6@\u00a2\u0006\u0002\u0010\u001eJ\u001e\u0010\u001f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010 \u001a\u00020!H\u00a6@\u00a2\u0006\u0002\u0010\"J\u0016\u0010#\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010$\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010%\u001a\u00020&H\u00a6@\u00a2\u0006\u0002\u0010\'\u00a8\u0006("}, d2 = {"Lcom/dairum/domain/repository/TrackerRepository;", "", "createTracker", "", "tracker", "Lcom/dairum/domain/model/CustomTracker;", "(Lcom/dairum/domain/model/CustomTracker;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTracker", "", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTrackerValue", "entryId", "trackerId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveTrackersFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getAllTrackers", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllTrackersFlow", "getTrackerById", "getTrackerStats", "Lcom/dairum/domain/model/TrackerStats;", "getTrackerValuesForEntry", "", "", "getTrackerValuesForEntryFlow", "setTrackerValue", "value", "(JJLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleTrackerActive", "isActive", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTracker", "updateTrackerSortOrder", "sortOrder", "", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface TrackerRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.CustomTracker tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.CustomTracker tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTracker(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTrackerById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.CustomTracker> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllTrackers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.CustomTracker>> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.CustomTracker>> getActiveTrackersFlow();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.CustomTracker>> getAllTrackersFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object toggleTrackerActive(long id, boolean isActive, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTrackerSortOrder(long id, int sortOrder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setTrackerValue(long entryId, long trackerId, @org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTrackerValue(long entryId, long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTrackerValuesForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.Long, java.lang.String>> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.Map<java.lang.Long, java.lang.String>> getTrackerValuesForEntryFlow(long entryId);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTrackerStats(long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.TrackerStats> $completion);
}