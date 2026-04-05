package com.dairum.data.repository;

import android.content.Context;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.*;
import com.dairum.domain.model.ExportFormat;
import com.dairum.domain.repository.*;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.Flow;
import java.io.IOException;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b%\u0018\u00002\u00020\u0001:\u0001IB\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\u000e\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\u000e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\tH\u0016J\u0010\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\tH\u0016J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\u000e\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\u0010\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\tH\u0016J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\tH\u0016J\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\tH\u0016J\u000e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\u000e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\tH\u0016J\u000e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\tH\u0016J\u000e\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\tH\u0016J\u000e\u0010 \u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\u000e\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\tH\u0016J\u000e\u0010#\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\u0016\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\'J\u0016\u0010(\u001a\u00020%2\u0006\u0010&\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\'J\u0016\u0010)\u001a\u00020%2\u0006\u0010*\u001a\u00020\rH\u0096@\u00a2\u0006\u0002\u0010+J\u0018\u0010,\u001a\u00020%2\b\u0010-\u001a\u0004\u0018\u00010\u000fH\u0096@\u00a2\u0006\u0002\u0010.J\u0016\u0010/\u001a\u00020%2\u0006\u0010&\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\'J\u000e\u00100\u001a\u00020%H\u0096@\u00a2\u0006\u0002\u00101J\u0016\u00102\u001a\u00020%2\u0006\u0010&\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\'J\u0018\u00103\u001a\u00020%2\b\u00104\u001a\u0004\u0018\u00010\u0013H\u0096@\u00a2\u0006\u0002\u00105J\u0016\u00106\u001a\u00020%2\u0006\u0010*\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0002\u00107J\u0016\u00108\u001a\u00020%2\u0006\u00109\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\'J\u0016\u0010:\u001a\u00020%2\u0006\u0010;\u001a\u00020\u0018H\u0096@\u00a2\u0006\u0002\u0010<J\u0016\u0010=\u001a\u00020%2\u0006\u0010&\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\'J\u0016\u0010>\u001a\u00020%2\u0006\u0010?\u001a\u00020\u001bH\u0096@\u00a2\u0006\u0002\u0010@J\u0016\u0010A\u001a\u00020%2\u0006\u0010B\u001a\u00020\u001dH\u0096@\u00a2\u0006\u0002\u0010CJ\u0016\u0010D\u001a\u00020%2\u0006\u0010*\u001a\u00020\u001fH\u0096@\u00a2\u0006\u0002\u0010EJ\u0016\u0010F\u001a\u00020%2\u0006\u0010&\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\'J\u0016\u0010G\u001a\u00020%2\u0006\u0010?\u001a\u00020\"H\u0096@\u00a2\u0006\u0002\u0010HR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006J"}, d2 = {"Lcom/dairum/data/repository/SettingsRepositoryImpl;", "Lcom/dairum/domain/repository/SettingsRepository;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "dataStore", "Landroidx/datastore/core/DataStore;", "Landroidx/datastore/preferences/core/Preferences;", "getAppLockEnabledFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getBiometricEnabled", "getDateFormat", "Lcom/dairum/domain/repository/DateFormat;", "getDefaultCategoryIdFlow", "", "getDynamicColorsFlow", "getLocationAutoFetchFlow", "getPinHash", "", "getTaskerDefaultFormatFlow", "Lcom/dairum/domain/model/ExportFormat;", "getTaskerDefaultIncludeMediaFlow", "getTaskerDefaultPeriodFlow", "Lcom/dairum/domain/repository/TaskerPeriod;", "getTaskerEnabledFlow", "getTemperatureUnit", "Lcom/dairum/domain/repository/TemperatureUnit;", "getThemeModeFlow", "Lcom/dairum/domain/repository/ThemeMode;", "getTimeFormat", "Lcom/dairum/domain/repository/TimeFormat;", "getWeatherAutoFetchFlow", "getWeightUnit", "Lcom/dairum/domain/repository/WeightUnit;", "isFirstLaunch", "setAppLockEnabled", "", "enabled", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setBiometricEnabled", "setDateFormat", "format", "(Lcom/dairum/domain/repository/DateFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setDefaultCategoryId", "id", "(Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setDynamicColors", "setFirstLaunchComplete", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setLocationAutoFetch", "setPinHash", "hash", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTaskerDefaultFormat", "(Lcom/dairum/domain/model/ExportFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTaskerDefaultIncludeMedia", "include", "setTaskerDefaultPeriod", "period", "(Lcom/dairum/domain/repository/TaskerPeriod;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTaskerEnabled", "setTemperatureUnit", "unit", "(Lcom/dairum/domain/repository/TemperatureUnit;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setThemeMode", "mode", "(Lcom/dairum/domain/repository/ThemeMode;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTimeFormat", "(Lcom/dairum/domain/repository/TimeFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setWeatherAutoFetch", "setWeightUnit", "(Lcom/dairum/domain/repository/WeightUnit;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "PreferencesKeys", "app_debug"})
public final class SettingsRepositoryImpl implements com.dairum.domain.repository.SettingsRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> dataStore = null;
    
    @javax.inject.Inject()
    public SettingsRepositoryImpl(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.ThemeMode> getThemeModeFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setThemeMode(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ThemeMode mode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Boolean> getDynamicColorsFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setDynamicColors(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Long> getDefaultCategoryIdFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setDefaultCategoryId(@org.jetbrains.annotations.Nullable()
    java.lang.Long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Boolean> getWeatherAutoFetchFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setWeatherAutoFetch(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Boolean> getLocationAutoFetchFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setLocationAutoFetch(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.TimeFormat> getTimeFormat() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setTimeFormat(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TimeFormat format, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.DateFormat> getDateFormat() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setDateFormat(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.DateFormat format, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.WeightUnit> getWeightUnit() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setWeightUnit(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.WeightUnit unit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.TemperatureUnit> getTemperatureUnit() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setTemperatureUnit(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TemperatureUnit unit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Boolean> getAppLockEnabledFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setAppLockEnabled(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Boolean> getBiometricEnabled() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setBiometricEnabled(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.String> getPinHash() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setPinHash(@org.jetbrains.annotations.Nullable()
    java.lang.String hash, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Boolean> isFirstLaunch() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setFirstLaunchComplete(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Boolean> getTaskerEnabledFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setTaskerEnabled(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.model.ExportFormat> getTaskerDefaultFormatFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setTaskerDefaultFormat(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat format, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.dairum.domain.repository.TaskerPeriod> getTaskerDefaultPeriodFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setTaskerDefaultPeriod(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TaskerPeriod period, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Boolean> getTaskerDefaultIncludeMediaFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setTaskerDefaultIncludeMedia(boolean include, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0018\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0007R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0007R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0007R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0007R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0007R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0007R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0007R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0007R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0007R\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0007R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0007R\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0007R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u0007R\u0017\u0010(\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0007\u00a8\u0006*"}, d2 = {"Lcom/dairum/data/repository/SettingsRepositoryImpl$PreferencesKeys;", "", "()V", "APP_LOCK_ENABLED", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "getAPP_LOCK_ENABLED", "()Landroidx/datastore/preferences/core/Preferences$Key;", "AUTO_FETCH_LOCATION", "getAUTO_FETCH_LOCATION", "AUTO_FETCH_WEATHER", "getAUTO_FETCH_WEATHER", "BIOMETRIC_ENABLED", "getBIOMETRIC_ENABLED", "DATE_FORMAT", "", "getDATE_FORMAT", "DEFAULT_CATEGORY_ID", "", "getDEFAULT_CATEGORY_ID", "DYNAMIC_COLORS", "getDYNAMIC_COLORS", "FIRST_LAUNCH", "getFIRST_LAUNCH", "PIN_HASH", "getPIN_HASH", "TASKER_DEFAULT_FORMAT", "getTASKER_DEFAULT_FORMAT", "TASKER_DEFAULT_INCLUDE_MEDIA", "getTASKER_DEFAULT_INCLUDE_MEDIA", "TASKER_DEFAULT_PERIOD", "getTASKER_DEFAULT_PERIOD", "TASKER_ENABLED", "getTASKER_ENABLED", "TEMPERATURE_UNIT", "getTEMPERATURE_UNIT", "THEME_MODE", "getTHEME_MODE", "TIME_FORMAT", "getTIME_FORMAT", "WEIGHT_UNIT", "getWEIGHT_UNIT", "app_debug"})
    static final class PreferencesKeys {
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> THEME_MODE = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> DYNAMIC_COLORS = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> DEFAULT_CATEGORY_ID = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> AUTO_FETCH_WEATHER = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> AUTO_FETCH_LOCATION = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> TIME_FORMAT = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> DATE_FORMAT = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> WEIGHT_UNIT = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> TEMPERATURE_UNIT = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> APP_LOCK_ENABLED = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> BIOMETRIC_ENABLED = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> PIN_HASH = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> FIRST_LAUNCH = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> TASKER_ENABLED = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> TASKER_DEFAULT_FORMAT = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> TASKER_DEFAULT_PERIOD = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> TASKER_DEFAULT_INCLUDE_MEDIA = null;
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.data.repository.SettingsRepositoryImpl.PreferencesKeys INSTANCE = null;
        
        private PreferencesKeys() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getTHEME_MODE() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getDYNAMIC_COLORS() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> getDEFAULT_CATEGORY_ID() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getAUTO_FETCH_WEATHER() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getAUTO_FETCH_LOCATION() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getTIME_FORMAT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getDATE_FORMAT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getWEIGHT_UNIT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getTEMPERATURE_UNIT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getAPP_LOCK_ENABLED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getBIOMETRIC_ENABLED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getPIN_HASH() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getFIRST_LAUNCH() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getTASKER_ENABLED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getTASKER_DEFAULT_FORMAT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getTASKER_DEFAULT_PERIOD() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getTASKER_DEFAULT_INCLUDE_MEDIA() {
            return null;
        }
    }
}