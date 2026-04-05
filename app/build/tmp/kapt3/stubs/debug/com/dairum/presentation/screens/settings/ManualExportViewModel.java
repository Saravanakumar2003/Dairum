package com.dairum.presentation.screens.settings;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.ExportFormat;
import com.dairum.domain.model.ExportHistory;
import com.dairum.domain.repository.ExportRepository;
import com.dairum.domain.repository.JournalRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.util.Calendar;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fJ\u0006\u0010\u0011\u001a\u00020\u000fJ\u0006\u0010\u0012\u001a\u00020\u000fJ\b\u0010\u0013\u001a\u00020\u000fH\u0002J\u000e\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u0016J\u0015\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019\u00a2\u0006\u0002\u0010\u001aJ\u0010\u0010\u001b\u001a\u00020\u000f2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dJ\u000e\u0010\u001e\u001a\u00020\u000f2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020#J\u0015\u0010$\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019\u00a2\u0006\u0002\u0010\u001aJ\u000e\u0010%\u001a\u00020\u000f2\u0006\u0010&\u001a\u00020\u001dJ\u0006\u0010\'\u001a\u00020\u000fR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006("}, d2 = {"Lcom/dairum/presentation/screens/settings/ManualExportViewModel;", "Landroidx/lifecycle/ViewModel;", "exportRepository", "Lcom/dairum/domain/repository/ExportRepository;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "(Lcom/dairum/domain/repository/ExportRepository;Lcom/dairum/domain/repository/JournalRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/settings/ManualExportUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearError", "", "clearSharePath", "clearSuccess", "exportEntries", "loadExportHistory", "setDatePeriod", "period", "Lcom/dairum/presentation/screens/settings/DatePeriod;", "setEndDate", "timestamp", "", "(Ljava/lang/Long;)V", "setExportDestination", "destination", "", "setExportFormat", "format", "Lcom/dairum/domain/model/ExportFormat;", "setIncludeMedia", "include", "", "setStartDate", "shareExport", "filePath", "shareLastExport", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ManualExportViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ExportRepository exportRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.settings.ManualExportUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.settings.ManualExportUiState> uiState = null;
    
    @javax.inject.Inject()
    public ManualExportViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ExportRepository exportRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.settings.ManualExportUiState> getUiState() {
        return null;
    }
    
    private final void loadExportHistory() {
    }
    
    public final void setDatePeriod(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.settings.DatePeriod period) {
    }
    
    public final void setStartDate(@org.jetbrains.annotations.Nullable()
    java.lang.Long timestamp) {
    }
    
    public final void setEndDate(@org.jetbrains.annotations.Nullable()
    java.lang.Long timestamp) {
    }
    
    public final void setExportFormat(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat format) {
    }
    
    public final void setIncludeMedia(boolean include) {
    }
    
    public final void setExportDestination(@org.jetbrains.annotations.Nullable()
    java.lang.String destination) {
    }
    
    public final void exportEntries() {
    }
    
    public final void shareLastExport() {
    }
    
    public final void shareExport(@org.jetbrains.annotations.NotNull()
    java.lang.String filePath) {
    }
    
    public final void clearSharePath() {
    }
    
    public final void clearError() {
    }
    
    public final void clearSuccess() {
    }
}