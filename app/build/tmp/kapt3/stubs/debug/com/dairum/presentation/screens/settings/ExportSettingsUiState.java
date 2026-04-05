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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b1\b\u0086\b\u0018\u00002\u00020\u0001B\u00b5\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0014\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0014\u00a2\u0006\u0002\u0010\u0018J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\t\u00100\u001a\u00020\u0005H\u00c6\u0003J\t\u00101\u001a\u00020\u0005H\u00c6\u0003J\t\u00102\u001a\u00020\u0005H\u00c6\u0003J\u000b\u00103\u001a\u0004\u0018\u00010\u0014H\u00c6\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u0014H\u00c6\u0003J\t\u00105\u001a\u00020\u0007H\u00c6\u0003J\u000b\u00106\u001a\u0004\u0018\u00010\u0014H\u00c6\u0003J\t\u00107\u001a\u00020\u0005H\u00c6\u0003J\t\u00108\u001a\u00020\u0007H\u00c6\u0003J\t\u00109\u001a\u00020\u0007H\u00c6\u0003J\u000f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00070\nH\u00c6\u0003J\t\u0010;\u001a\u00020\fH\u00c6\u0003J\t\u0010<\u001a\u00020\u0005H\u00c6\u0003J\u0010\u0010=\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001eJ\u0010\u0010>\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001eJ\u00be\u0001\u0010?\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u00052\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00052\b\b\u0002\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00052\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00142\b\b\u0002\u0010\u0016\u001a\u00020\u00072\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0014H\u00c6\u0001\u00a2\u0006\u0002\u0010@J\u0013\u0010A\u001a\u00020\u00052\b\u0010B\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010C\u001a\u00020\u0007H\u00d6\u0001J\t\u0010D\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0015\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u001f\u001a\u0004\b\u001d\u0010\u001eR\u0013\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u0012\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001aR\u0011\u0010\r\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001aR\u0011\u0010\u0011\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u001aR\u0011\u0010\u0010\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u001aR\u0011\u0010\u0016\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010!R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\'R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\'R\u0013\u0010\u0017\u001a\u0004\u0018\u00010\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010!R\u0015\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u001f\u001a\u0004\b.\u0010\u001e\u00a8\u0006E"}, d2 = {"Lcom/dairum/presentation/screens/settings/ExportSettingsUiState;", "", "configId", "", "autoExportEnabled", "", "scheduleHour", "", "scheduleMinute", "scheduleDays", "", "exportFormat", "Lcom/dairum/domain/model/ExportFormat;", "includeMedia", "startDate", "endDate", "isLoading", "isExporting", "exportSuccess", "error", "", "lastExportPath", "lastExportEntryCount", "shareFilePath", "(JZIILjava/util/List;Lcom/dairum/domain/model/ExportFormat;ZLjava/lang/Long;Ljava/lang/Long;ZZZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;)V", "getAutoExportEnabled", "()Z", "getConfigId", "()J", "getEndDate", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getError", "()Ljava/lang/String;", "getExportFormat", "()Lcom/dairum/domain/model/ExportFormat;", "getExportSuccess", "getIncludeMedia", "getLastExportEntryCount", "()I", "getLastExportPath", "getScheduleDays", "()Ljava/util/List;", "getScheduleHour", "getScheduleMinute", "getShareFilePath", "getStartDate", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JZIILjava/util/List;Lcom/dairum/domain/model/ExportFormat;ZLjava/lang/Long;Ljava/lang/Long;ZZZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;)Lcom/dairum/presentation/screens/settings/ExportSettingsUiState;", "equals", "other", "hashCode", "toString", "app_debug"})
public final class ExportSettingsUiState {
    private final long configId = 0L;
    private final boolean autoExportEnabled = false;
    private final int scheduleHour = 0;
    private final int scheduleMinute = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Integer> scheduleDays = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.ExportFormat exportFormat = null;
    private final boolean includeMedia = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long startDate = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long endDate = null;
    private final boolean isLoading = false;
    private final boolean isExporting = false;
    private final boolean exportSuccess = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String lastExportPath = null;
    private final int lastExportEntryCount = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String shareFilePath = null;
    
    public ExportSettingsUiState(long configId, boolean autoExportEnabled, int scheduleHour, int scheduleMinute, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Integer> scheduleDays, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat exportFormat, boolean includeMedia, @org.jetbrains.annotations.Nullable()
    java.lang.Long startDate, @org.jetbrains.annotations.Nullable()
    java.lang.Long endDate, boolean isLoading, boolean isExporting, boolean exportSuccess, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String lastExportPath, int lastExportEntryCount, @org.jetbrains.annotations.Nullable()
    java.lang.String shareFilePath) {
        super();
    }
    
    public final long getConfigId() {
        return 0L;
    }
    
    public final boolean getAutoExportEnabled() {
        return false;
    }
    
    public final int getScheduleHour() {
        return 0;
    }
    
    public final int getScheduleMinute() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> getScheduleDays() {
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
    public final java.lang.Long getStartDate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getEndDate() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
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
    
    public ExportSettingsUiState() {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final boolean component10() {
        return false;
    }
    
    public final boolean component11() {
        return false;
    }
    
    public final boolean component12() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component14() {
        return null;
    }
    
    public final int component15() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component16() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.ExportFormat component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.settings.ExportSettingsUiState copy(long configId, boolean autoExportEnabled, int scheduleHour, int scheduleMinute, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Integer> scheduleDays, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat exportFormat, boolean includeMedia, @org.jetbrains.annotations.Nullable()
    java.lang.Long startDate, @org.jetbrains.annotations.Nullable()
    java.lang.Long endDate, boolean isLoading, boolean isExporting, boolean exportSuccess, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String lastExportPath, int lastExportEntryCount, @org.jetbrains.annotations.Nullable()
    java.lang.String shareFilePath) {
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