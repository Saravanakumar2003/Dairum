package com.dairum.presentation.screens.settings;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.ExportConfig;
import com.dairum.domain.model.ExportFormat;
import com.dairum.domain.repository.ExportRepository;
import com.dairum.domain.repository.JournalRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fJ\u0006\u0010\u0011\u001a\u00020\u000fJ\u0006\u0010\u0012\u001a\u00020\u000fJ\'\u0010\u0013\u001a\u00020\u000f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\u0002\u0010\u0019J\u001f\u0010\u001a\u001a\u00020\u000f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0015\u00a2\u0006\u0002\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u000fH\u0002J\b\u0010\u001d\u001a\u00020\u000fH\u0002J\u000e\u0010\u001e\u001a\u00020\u000f2\u0006\u0010\u001f\u001a\u00020 J\u0015\u0010!\u001a\u00020\u000f2\b\u0010\"\u001a\u0004\u0018\u00010\u0015\u00a2\u0006\u0002\u0010#J\u000e\u0010$\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020&J\u000e\u0010\'\u001a\u00020\u000f2\u0006\u0010(\u001a\u00020 J\u000e\u0010)\u001a\u00020\u000f2\u0006\u0010*\u001a\u00020+J\u000e\u0010,\u001a\u00020\u000f2\u0006\u0010-\u001a\u00020+J\u0015\u0010.\u001a\u00020\u000f2\b\u0010\"\u001a\u0004\u0018\u00010\u0015\u00a2\u0006\u0002\u0010#J\u0006\u0010/\u001a\u00020\u000fJ\u000e\u00100\u001a\u00020\u000f2\u0006\u00101\u001a\u00020+R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u00062"}, d2 = {"Lcom/dairum/presentation/screens/settings/ExportSettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "exportRepository", "Lcom/dairum/domain/repository/ExportRepository;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "(Lcom/dairum/domain/repository/ExportRepository;Lcom/dairum/domain/repository/JournalRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/settings/ExportSettingsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearError", "", "clearSharePath", "clearSuccess", "exportNow", "exportToExternalLocation", "startDate", "", "endDate", "destinationUri", "Landroid/net/Uri;", "(Ljava/lang/Long;Ljava/lang/Long;Landroid/net/Uri;)V", "exportWithDateRange", "(Ljava/lang/Long;Ljava/lang/Long;)V", "loadConfig", "saveConfig", "setAutoExportEnabled", "enabled", "", "setEndDate", "timestamp", "(Ljava/lang/Long;)V", "setExportFormat", "format", "Lcom/dairum/domain/model/ExportFormat;", "setIncludeMedia", "include", "setScheduleHour", "hour", "", "setScheduleMinute", "minute", "setStartDate", "shareLastExport", "toggleScheduleDay", "dayOfWeek", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ExportSettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ExportRepository exportRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.settings.ExportSettingsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.settings.ExportSettingsUiState> uiState = null;
    
    @javax.inject.Inject()
    public ExportSettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ExportRepository exportRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.settings.ExportSettingsUiState> getUiState() {
        return null;
    }
    
    private final void loadConfig() {
    }
    
    public final void setAutoExportEnabled(boolean enabled) {
    }
    
    public final void setScheduleHour(int hour) {
    }
    
    public final void setScheduleMinute(int minute) {
    }
    
    public final void toggleScheduleDay(int dayOfWeek) {
    }
    
    public final void setExportFormat(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat format) {
    }
    
    public final void setIncludeMedia(boolean include) {
    }
    
    private final void saveConfig() {
    }
    
    public final void exportNow() {
    }
    
    public final void exportWithDateRange(@org.jetbrains.annotations.Nullable()
    java.lang.Long startDate, @org.jetbrains.annotations.Nullable()
    java.lang.Long endDate) {
    }
    
    public final void shareLastExport() {
    }
    
    public final void clearSharePath() {
    }
    
    public final void exportToExternalLocation(@org.jetbrains.annotations.Nullable()
    java.lang.Long startDate, @org.jetbrains.annotations.Nullable()
    java.lang.Long endDate, @org.jetbrains.annotations.NotNull()
    android.net.Uri destinationUri) {
    }
    
    public final void setStartDate(@org.jetbrains.annotations.Nullable()
    java.lang.Long timestamp) {
    }
    
    public final void setEndDate(@org.jetbrains.annotations.Nullable()
    java.lang.Long timestamp) {
    }
    
    public final void clearError() {
    }
    
    public final void clearSuccess() {
    }
}