package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * Repository for AI-generated content (reflections, patterns, summaries)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000f\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\f\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000eH&J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u000f0\u000eH&J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u000f0\u000eH&J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\u00102\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000e2\u0006\u0010\u0017\u001a\u00020\u0018H&J\u001c\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000e2\u0006\u0010\u001a\u001a\u00020\u001bH&J\u001c\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u000f0\u000e2\u0006\u0010\u001a\u001a\u00020\u001bH&J\u001c\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u000f0\u000e2\u0006\u0010\u001a\u001a\u00020\u001bH&J\u0018\u0010\u001e\u001a\u0004\u0018\u00010\u00122\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u001f\u001a\u0004\u0018\u00010\u00122\u0006\u0010 \u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u000f0\u000e2\u0006\u0010\"\u001a\u00020#H&J\u0018\u0010$\u001a\u0004\u0018\u00010\u00142\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ \u0010%\u001a\u0004\u0018\u00010\u00142\u0006\u0010&\u001a\u00020#2\u0006\u0010\'\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010(J\u0016\u0010)\u001a\u00020\t2\u0006\u0010*\u001a\u00020\u0010H\u00a6@\u00a2\u0006\u0002\u0010+J\u0016\u0010,\u001a\u00020\t2\u0006\u0010-\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010.J\u0016\u0010/\u001a\u00020\t2\u0006\u00100\u001a\u00020\u0014H\u00a6@\u00a2\u0006\u0002\u00101\u00a8\u00062"}, d2 = {"Lcom/dairum/domain/repository/AIContentRepository;", "", "deleteOldPatterns", "", "olderThan", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deletePattern", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteReflection", "deleteSummary", "getAllPatterns", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/dairum/domain/model/PatternResult;", "getAllReflections", "Lcom/dairum/domain/model/WeeklyReflection;", "getAllSummaries", "Lcom/dairum/domain/model/SmartSummary;", "getPatternById", "getPatternsByType", "patternType", "Lcom/dairum/domain/model/PatternType;", "getRecentPatterns", "limit", "", "getRecentReflections", "getRecentSummaries", "getReflectionById", "getReflectionForWeek", "weekStart", "getSummariesByType", "summaryType", "Lcom/dairum/domain/model/SummaryType;", "getSummaryById", "getSummaryForPeriod", "type", "periodStart", "(Lcom/dairum/domain/model/SummaryType;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "savePattern", "pattern", "(Lcom/dairum/domain/model/PatternResult;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveReflection", "reflection", "(Lcom/dairum/domain/model/WeeklyReflection;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveSummary", "summary", "(Lcom/dairum/domain/model/SmartSummary;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface AIContentRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.WeeklyReflection>> getAllReflections();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.WeeklyReflection>> getRecentReflections(int limit);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.WeeklyReflection> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReflectionForWeek(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime weekStart, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.WeeklyReflection> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveReflection(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.WeeklyReflection reflection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteReflection(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.PatternResult>> getAllPatterns();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.PatternResult>> getPatternsByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.PatternType patternType);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.PatternResult>> getRecentPatterns(int limit);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPatternById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.PatternResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object savePattern(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.PatternResult pattern, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deletePattern(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteOldPatterns(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime olderThan, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartSummary>> getAllSummaries();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartSummary>> getSummariesByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SummaryType summaryType);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.SmartSummary>> getRecentSummaries(int limit);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSummaryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.SmartSummary> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSummaryForPeriod(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SummaryType type, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime periodStart, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.SmartSummary> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveSummary(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.SmartSummary summary, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSummary(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}