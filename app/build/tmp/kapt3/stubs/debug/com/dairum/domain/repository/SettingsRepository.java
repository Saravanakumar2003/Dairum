package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b$\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u000e\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0003H&J\u0010\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0003H&J\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u000e\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u0010\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0003H&J\u000e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0003H&J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u000e\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0003H&J\u000e\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u0003H&J\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u0003H&J\u000e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0003H&J\u000e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u000e\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u0003H&J\u000e\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u0016\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010!J\u0016\u0010#\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010%J\u0018\u0010&\u001a\u00020\u001f2\b\u0010\'\u001a\u0004\u0018\u00010\tH\u00a6@\u00a2\u0006\u0002\u0010(J\u0016\u0010)\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010!J\u000e\u0010*\u001a\u00020\u001fH\u00a6@\u00a2\u0006\u0002\u0010+J\u0016\u0010,\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010!J\u0018\u0010-\u001a\u00020\u001f2\b\u0010.\u001a\u0004\u0018\u00010\rH\u00a6@\u00a2\u0006\u0002\u0010/J\u0016\u00100\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u000fH\u00a6@\u00a2\u0006\u0002\u00101J\u0016\u00102\u001a\u00020\u001f2\u0006\u00103\u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010!J\u0016\u00104\u001a\u00020\u001f2\u0006\u00105\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u00106J\u0016\u00107\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010!J\u0016\u00108\u001a\u00020\u001f2\u0006\u00109\u001a\u00020\u0015H\u00a6@\u00a2\u0006\u0002\u0010:J\u0016\u0010;\u001a\u00020\u001f2\u0006\u0010<\u001a\u00020\u0017H\u00a6@\u00a2\u0006\u0002\u0010=J\u0016\u0010>\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u0019H\u00a6@\u00a2\u0006\u0002\u0010?J\u0016\u0010@\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010!J\u0016\u0010A\u001a\u00020\u001f2\u0006\u00109\u001a\u00020\u001cH\u00a6@\u00a2\u0006\u0002\u0010B\u00a8\u0006C"}, d2 = {"Lcom/dairum/domain/repository/SettingsRepository;", "", "getAppLockEnabledFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getBiometricEnabled", "getDateFormat", "Lcom/dairum/domain/repository/DateFormat;", "getDefaultCategoryIdFlow", "", "getDynamicColorsFlow", "getLocationAutoFetchFlow", "getPinHash", "", "getTaskerDefaultFormatFlow", "Lcom/dairum/domain/model/ExportFormat;", "getTaskerDefaultIncludeMediaFlow", "getTaskerDefaultPeriodFlow", "Lcom/dairum/domain/repository/TaskerPeriod;", "getTaskerEnabledFlow", "getTemperatureUnit", "Lcom/dairum/domain/repository/TemperatureUnit;", "getThemeModeFlow", "Lcom/dairum/domain/repository/ThemeMode;", "getTimeFormat", "Lcom/dairum/domain/repository/TimeFormat;", "getWeatherAutoFetchFlow", "getWeightUnit", "Lcom/dairum/domain/repository/WeightUnit;", "isFirstLaunch", "setAppLockEnabled", "", "enabled", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setBiometricEnabled", "setDateFormat", "format", "(Lcom/dairum/domain/repository/DateFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setDefaultCategoryId", "id", "(Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setDynamicColors", "setFirstLaunchComplete", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setLocationAutoFetch", "setPinHash", "hash", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTaskerDefaultFormat", "(Lcom/dairum/domain/model/ExportFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTaskerDefaultIncludeMedia", "include", "setTaskerDefaultPeriod", "period", "(Lcom/dairum/domain/repository/TaskerPeriod;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTaskerEnabled", "setTemperatureUnit", "unit", "(Lcom/dairum/domain/repository/TemperatureUnit;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setThemeMode", "mode", "(Lcom/dairum/domain/repository/ThemeMode;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTimeFormat", "(Lcom/dairum/domain/repository/TimeFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setWeatherAutoFetch", "setWeightUnit", "(Lcom/dairum/domain/repository/WeightUnit;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface SettingsRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.ThemeMode> getThemeModeFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setThemeMode(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ThemeMode mode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> getDynamicColorsFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setDynamicColors(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Long> getDefaultCategoryIdFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setDefaultCategoryId(@org.jetbrains.annotations.Nullable()
    java.lang.Long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> getWeatherAutoFetchFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setWeatherAutoFetch(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> getLocationAutoFetchFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setLocationAutoFetch(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.TimeFormat> getTimeFormat();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setTimeFormat(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TimeFormat format, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.DateFormat> getDateFormat();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setDateFormat(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.DateFormat format, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.WeightUnit> getWeightUnit();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setWeightUnit(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.WeightUnit unit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> getAppLockEnabledFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setAppLockEnabled(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> getBiometricEnabled();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setBiometricEnabled(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.String> getPinHash();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setPinHash(@org.jetbrains.annotations.Nullable()
    java.lang.String hash, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.TemperatureUnit> getTemperatureUnit();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setTemperatureUnit(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TemperatureUnit unit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> isFirstLaunch();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setFirstLaunchComplete(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> getTaskerEnabledFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setTaskerEnabled(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.model.ExportFormat> getTaskerDefaultFormatFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setTaskerDefaultFormat(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat format, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.TaskerPeriod> getTaskerDefaultPeriodFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setTaskerDefaultPeriod(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TaskerPeriod period, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> getTaskerDefaultIncludeMediaFlow();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setTaskerDefaultIncludeMedia(boolean include, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}