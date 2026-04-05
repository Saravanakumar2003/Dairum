package com.dairum.data.repository;

import com.dairum.data.ai.model.ModelManager;
import com.dairum.data.local.dao.*;
import com.dairum.data.local.entity.*;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.*;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of AIContentRepository
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0012\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0002J\u0010\u0010\"\u001a\u00020#2\u0006\u0010 \u001a\u00020$H\u0002J\u0010\u0010%\u001a\u00020&2\u0006\u0010 \u001a\u00020\'H\u0002J\u0014\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\u00120)H\u0016J\u0014\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020#0\u00120)H\u0016J\u0014\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0\u00120)H\u0016J\u0018\u0010,\u001a\u0004\u0018\u00010\u001f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u001c\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\u00120)2\u0006\u0010.\u001a\u00020/H\u0016J\u001c\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\u00120)2\u0006\u00101\u001a\u00020\u000fH\u0016J\u001c\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020#0\u00120)2\u0006\u00101\u001a\u00020\u000fH\u0016J\u001c\u00103\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0\u00120)2\u0006\u00101\u001a\u00020\u000fH\u0016J\u0018\u00104\u001a\u0004\u0018\u00010#2\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0018\u00105\u001a\u0004\u0018\u00010#2\u0006\u00106\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0017J\u001c\u00107\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0\u00120)2\u0006\u00108\u001a\u000209H\u0016J\u0018\u0010:\u001a\u0004\u0018\u00010&2\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ \u0010;\u001a\u0004\u0018\u00010&2\u0006\u0010<\u001a\u0002092\u0006\u0010=\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010>J\u0010\u0010?\u001a\u00020!2\u0006\u0010@\u001a\u00020\u001fH\u0002J\u0010\u0010A\u001a\u00020$2\u0006\u0010B\u001a\u00020#H\u0002J\u0016\u0010C\u001a\u00020\u001a2\u0006\u0010@\u001a\u00020\u001fH\u0096@\u00a2\u0006\u0002\u0010DJ\u0016\u0010E\u001a\u00020\u001a2\u0006\u0010B\u001a\u00020#H\u0096@\u00a2\u0006\u0002\u0010FJ\u0016\u0010G\u001a\u00020\u001a2\u0006\u0010H\u001a\u00020&H\u0096@\u00a2\u0006\u0002\u0010IJ\u0010\u0010J\u001a\u00020\'2\u0006\u0010H\u001a\u00020&H\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000Rb\u0010\u000b\u001aV\u0012$\u0012\"\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f \u0010*\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f\u0018\u00010\r0\r \u0010**\u0012$\u0012\"\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f \u0010*\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f\u0018\u00010\r0\r\u0018\u00010\f0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000RJ\u0010\u0011\u001a>\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\u000e \u0010*\n\u0012\u0004\u0012\u00020\u000e\u0018\u00010\u00120\u0012 \u0010*\u001e\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\u000e \u0010*\n\u0012\u0004\u0012\u00020\u000e\u0018\u00010\u00120\u0012\u0018\u00010\f0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006K"}, d2 = {"Lcom/dairum/data/repository/AIContentRepositoryImpl;", "Lcom/dairum/domain/repository/AIContentRepository;", "reflectionDao", "Lcom/dairum/data/local/dao/WeeklyReflectionDao;", "patternDao", "Lcom/dairum/data/local/dao/PatternResultDao;", "summaryDao", "Lcom/dairum/data/local/dao/SmartSummaryDao;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Lcom/dairum/data/local/dao/WeeklyReflectionDao;Lcom/dairum/data/local/dao/PatternResultDao;Lcom/dairum/data/local/dao/SmartSummaryDao;Lcom/squareup/moshi/Moshi;)V", "stringIntMapAdapter", "Lcom/squareup/moshi/JsonAdapter;", "", "", "", "kotlin.jvm.PlatformType", "stringListAdapter", "", "deleteOldPatterns", "", "olderThan", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deletePattern", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteReflection", "deleteSummary", "entityToPattern", "Lcom/dairum/domain/model/PatternResult;", "entity", "Lcom/dairum/data/local/entity/PatternResultEntity;", "entityToReflection", "Lcom/dairum/domain/model/WeeklyReflection;", "Lcom/dairum/data/local/entity/WeeklyReflectionEntity;", "entityToSummary", "Lcom/dairum/domain/model/SmartSummary;", "Lcom/dairum/data/local/entity/SmartSummaryEntity;", "getAllPatterns", "Lkotlinx/coroutines/flow/Flow;", "getAllReflections", "getAllSummaries", "getPatternById", "getPatternsByType", "patternType", "Lcom/dairum/domain/model/PatternType;", "getRecentPatterns", "limit", "getRecentReflections", "getRecentSummaries", "getReflectionById", "getReflectionForWeek", "weekStart", "getSummariesByType", "summaryType", "Lcom/dairum/domain/model/SummaryType;", "getSummaryById", "getSummaryForPeriod", "type", "periodStart", "(Lcom/dairum/domain/model/SummaryType;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "patternToEntity", "pattern", "reflectionToEntity", "reflection", "savePattern", "(Lcom/dairum/domain/model/PatternResult;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveReflection", "(Lcom/dairum/domain/model/WeeklyReflection;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveSummary", "summary", "(Lcom/dairum/domain/model/SmartSummary;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "summaryToEntity", "app_debug"})
public final class AIContentRepositoryImpl implements com.dairum.domain.repository.AIContentRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.WeeklyReflectionDao reflectionDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.PatternResultDao patternDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.SmartSummaryDao summaryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.squareup.moshi.Moshi moshi = null;
    private final com.squareup.moshi.JsonAdapter<java.util.List<java.lang.String>> stringListAdapter = null;
    private final com.squareup.moshi.JsonAdapter<java.util.Map<java.lang.String, java.lang.Integer>> stringIntMapAdapter = null;
    
    @javax.inject.Inject()
    public AIContentRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.WeeklyReflectionDao reflectionDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.PatternResultDao patternDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.SmartSummaryDao summaryDao, @org.jetbrains.annotations.NotNull()
    com.squareup.moshi.Moshi moshi) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.WeeklyReflection>> getAllReflections() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.WeeklyReflection>> getRecentReflections(int limit) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getReflectionById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.WeeklyReflection> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getReflectionForWeek(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime weekStart, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.WeeklyReflection> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.WeeklyReflection reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteReflection(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.WeeklyReflection entityToReflection(com.dairum.data.local.entity.WeeklyReflectionEntity entity) {
        return null;
    }
    
    private final com.dairum.data.local.entity.WeeklyReflectionEntity reflectionToEntity(com.dairum.domain.model.WeeklyReflection reflection) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.PatternResult>> getAllPatterns() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.PatternResult>> getPatternsByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.PatternType patternType) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.PatternResult>> getRecentPatterns(int limit) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getPatternById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.PatternResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object savePattern(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.PatternResult pattern, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deletePattern(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteOldPatterns(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime olderThan, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.PatternResult entityToPattern(com.dairum.data.local.entity.PatternResultEntity entity) {
        return null;
    }
    
    private final com.dairum.data.local.entity.PatternResultEntity patternToEntity(com.dairum.domain.model.PatternResult pattern) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartSummary>> getAllSummaries() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartSummary>> getSummariesByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SummaryType summaryType) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartSummary>> getRecentSummaries(int limit) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getSummaryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.SmartSummary> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getSummaryForPeriod(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SummaryType type, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime periodStart, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.SmartSummary> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveSummary(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartSummary summary, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteSummary(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.SmartSummary entityToSummary(com.dairum.data.local.entity.SmartSummaryEntity entity) {
        return null;
    }
    
    private final com.dairum.data.local.entity.SmartSummaryEntity summaryToEntity(com.dairum.domain.model.SmartSummary summary) {
        return null;
    }
}