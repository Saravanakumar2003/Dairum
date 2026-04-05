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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b)\b\u0086\b\u0018\u00002\u00020\u0001B\u0099\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\b\b\u0002\u0010\r\u001a\u00020\n\u0012\b\b\u0002\u0010\u000e\u001a\u00020\n\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\f\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\f\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\f\u0012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\u0002\u0010\u0017J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u0010.\u001a\u00020\u0012H\u00c6\u0003J\u000b\u0010/\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\u000f\u00100\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015H\u00c6\u0003J\u0010\u00101\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0019J\u0010\u00102\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0019J\t\u00103\u001a\u00020\bH\u00c6\u0003J\t\u00104\u001a\u00020\nH\u00c6\u0003J\u000b\u00105\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u00106\u001a\u00020\nH\u00c6\u0003J\t\u00107\u001a\u00020\nH\u00c6\u0003J\u000b\u00108\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\u00a2\u0001\u00109\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\r\u001a\u00020\n2\b\b\u0002\u0010\u000e\u001a\u00020\n2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\f2\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015H\u00c6\u0001\u00a2\u0006\u0002\u0010:J\u0013\u0010;\u001a\u00020\n2\b\u0010<\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010=\u001a\u00020\u0012H\u00d6\u0001J\t\u0010>\u001a\u00020\fH\u00d6\u0001R\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u001a\u001a\u0004\b\u0018\u0010\u0019R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u000e\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010#R\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010#R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0013\u0010\u0013\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001cR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u001a\u001a\u0004\b+\u0010\u0019\u00a8\u0006?"}, d2 = {"Lcom/dairum/presentation/screens/settings/ManualExportUiState;", "", "selectedPeriod", "Lcom/dairum/presentation/screens/settings/DatePeriod;", "startDate", "", "endDate", "exportFormat", "Lcom/dairum/domain/model/ExportFormat;", "includeMedia", "", "exportDestination", "", "isExporting", "exportSuccess", "error", "lastExportPath", "lastExportEntryCount", "", "shareFilePath", "exportHistory", "", "Lcom/dairum/domain/model/ExportHistory;", "(Lcom/dairum/presentation/screens/settings/DatePeriod;Ljava/lang/Long;Ljava/lang/Long;Lcom/dairum/domain/model/ExportFormat;ZLjava/lang/String;ZZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/util/List;)V", "getEndDate", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getError", "()Ljava/lang/String;", "getExportDestination", "getExportFormat", "()Lcom/dairum/domain/model/ExportFormat;", "getExportHistory", "()Ljava/util/List;", "getExportSuccess", "()Z", "getIncludeMedia", "getLastExportEntryCount", "()I", "getLastExportPath", "getSelectedPeriod", "()Lcom/dairum/presentation/screens/settings/DatePeriod;", "getShareFilePath", "getStartDate", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Lcom/dairum/presentation/screens/settings/DatePeriod;Ljava/lang/Long;Ljava/lang/Long;Lcom/dairum/domain/model/ExportFormat;ZLjava/lang/String;ZZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/util/List;)Lcom/dairum/presentation/screens/settings/ManualExportUiState;", "equals", "other", "hashCode", "toString", "app_debug"})
public final class ManualExportUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.presentation.screens.settings.DatePeriod selectedPeriod = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long startDate = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long endDate = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.ExportFormat exportFormat = null;
    private final boolean includeMedia = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String exportDestination = null;
    private final boolean isExporting = false;
    private final boolean exportSuccess = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String lastExportPath = null;
    private final int lastExportEntryCount = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String shareFilePath = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.ExportHistory> exportHistory = null;
    
    public ManualExportUiState(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.settings.DatePeriod selectedPeriod, @org.jetbrains.annotations.Nullable()
    java.lang.Long startDate, @org.jetbrains.annotations.Nullable()
    java.lang.Long endDate, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat exportFormat, boolean includeMedia, @org.jetbrains.annotations.Nullable()
    java.lang.String exportDestination, boolean isExporting, boolean exportSuccess, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String lastExportPath, int lastExportEntryCount, @org.jetbrains.annotations.Nullable()
    java.lang.String shareFilePath, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ExportHistory> exportHistory) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.settings.DatePeriod getSelectedPeriod() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getStartDate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getEndDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.ExportFormat getExportFormat() {
        return null;
    }
    
    public final boolean getIncludeMedia() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getExportDestination() {
        return null;
    }
    
    public final boolean isExporting() {
        return false;
    }
    
    public final boolean getExportSuccess() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLastExportPath() {
        return null;
    }
    
    public final int getLastExportEntryCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getShareFilePath() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ExportHistory> getExportHistory() {
        return null;
    }
    
    public ManualExportUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.settings.DatePeriod component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    public final int component11() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ExportHistory> component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.ExportFormat component4() {
        return null;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.settings.ManualExportUiState copy(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.settings.DatePeriod selectedPeriod, @org.jetbrains.annotations.Nullable()
    java.lang.Long startDate, @org.jetbrains.annotations.Nullable()
    java.lang.Long endDate, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat exportFormat, boolean includeMedia, @org.jetbrains.annotations.Nullable()
    java.lang.String exportDestination, boolean isExporting, boolean exportSuccess, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String lastExportPath, int lastExportEntryCount, @org.jetbrains.annotations.Nullable()
    java.lang.String shareFilePath, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ExportHistory> exportHistory) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}