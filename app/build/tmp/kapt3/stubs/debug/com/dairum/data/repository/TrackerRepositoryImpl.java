package com.dairum.data.repository;

import com.dairum.data.local.dao.TrackerDao;
import com.dairum.data.local.entity.CustomTrackerEntity;
import com.dairum.data.local.entity.TrackerValueEntity;
import com.dairum.domain.model.CustomTracker;
import com.dairum.domain.model.TrackerDataPoint;
import com.dairum.domain.model.TrackerStats;
import com.dairum.domain.model.TrackerType;
import com.dairum.domain.repository.TrackerRepository;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\rJ\u001e\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00140\u0013H\u0016J\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00140\u0013H\u0016J\u0018\u0010\u0018\u001a\u0004\u0018\u00010\b2\u0006\u0010\f\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0010\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\rJ\"\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u001d0\u001c2\u0006\u0010\u000f\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\rJ\"\u0010\u001e\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u001d0\u001c0\u00132\u0006\u0010\u000f\u001a\u00020\u0006H\u0016J&\u0010\u001f\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u001dH\u0096@\u00a2\u0006\u0002\u0010!J\u001e\u0010\"\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010#\u001a\u00020$H\u0096@\u00a2\u0006\u0002\u0010%J\u0016\u0010&\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\'\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010(\u001a\u00020)H\u0096@\u00a2\u0006\u0002\u0010*J\f\u0010+\u001a\u00020\b*\u00020,H\u0002J\f\u0010+\u001a\u00020-*\u00020.H\u0002J\f\u0010/\u001a\u00020,*\u00020\bH\u0002J\f\u0010/\u001a\u00020.*\u00020-H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/dairum/data/repository/TrackerRepositoryImpl;", "Lcom/dairum/domain/repository/TrackerRepository;", "trackerDao", "Lcom/dairum/data/local/dao/TrackerDao;", "(Lcom/dairum/data/local/dao/TrackerDao;)V", "createTracker", "", "tracker", "Lcom/dairum/domain/model/CustomTracker;", "(Lcom/dairum/domain/model/CustomTracker;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTracker", "", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTrackerValue", "entryId", "trackerId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveTrackersFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getAllTrackers", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllTrackersFlow", "getTrackerById", "getTrackerStats", "Lcom/dairum/domain/model/TrackerStats;", "getTrackerValuesForEntry", "", "", "getTrackerValuesForEntryFlow", "setTrackerValue", "value", "(JJLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleTrackerActive", "isActive", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTracker", "updateTrackerSortOrder", "sortOrder", "", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toDomain", "Lcom/dairum/data/local/entity/CustomTrackerEntity;", "Lcom/dairum/domain/model/TrackerType;", "Lcom/dairum/data/local/entity/TrackerType;", "toEntity", "app_debug"})
public final class TrackerRepositoryImpl implements com.dairum.domain.repository.TrackerRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.TrackerDao trackerDao = null;
    
    @javax.inject.Inject()
    public TrackerRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.TrackerDao trackerDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.CustomTracker tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.CustomTracker tracker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteTracker(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTrackerById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.CustomTracker> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllTrackers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.CustomTracker>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.CustomTracker>> getActiveTrackersFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.CustomTracker>> getAllTrackersFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object toggleTrackerActive(long id, boolean isActive, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateTrackerSortOrder(long id, int sortOrder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setTrackerValue(long entryId, long trackerId, @org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteTrackerValue(long entryId, long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTrackerValuesForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.Long, java.lang.String>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.Map<java.lang.Long, java.lang.String>> getTrackerValuesForEntryFlow(long entryId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTrackerStats(long trackerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.TrackerStats> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.CustomTracker toDomain(com.dairum.data.local.entity.CustomTrackerEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.CustomTrackerEntity toEntity(com.dairum.domain.model.CustomTracker $this$toEntity) {
        return null;
    }
    
    private final com.dairum.domain.model.TrackerType toDomain(com.dairum.data.local.entity.TrackerType $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.TrackerType toEntity(com.dairum.domain.model.TrackerType $this$toEntity) {
        return null;
    }
}