package com.dairum.presentation.screens.settings;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.ExportFormat;
import com.dairum.domain.repository.SettingsRepository;
import com.dairum.domain.repository.TaskerPeriod;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B7\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J;\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\u00032\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\f\u00a8\u0006\u001e"}, d2 = {"Lcom/dairum/presentation/screens/settings/TaskerIntegrationUiState;", "", "isEnabled", "", "defaultFormat", "Lcom/dairum/domain/model/ExportFormat;", "defaultPeriod", "Lcom/dairum/domain/repository/TaskerPeriod;", "defaultIncludeMedia", "copySuccess", "(ZLcom/dairum/domain/model/ExportFormat;Lcom/dairum/domain/repository/TaskerPeriod;ZZ)V", "getCopySuccess", "()Z", "getDefaultFormat", "()Lcom/dairum/domain/model/ExportFormat;", "getDefaultIncludeMedia", "getDefaultPeriod", "()Lcom/dairum/domain/repository/TaskerPeriod;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class TaskerIntegrationUiState {
    private final boolean isEnabled = false;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.ExportFormat defaultFormat = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.TaskerPeriod defaultPeriod = null;
    private final boolean defaultIncludeMedia = false;
    private final boolean copySuccess = false;
    
    public TaskerIntegrationUiState(boolean isEnabled, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat defaultFormat, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TaskerPeriod defaultPeriod, boolean defaultIncludeMedia, boolean copySuccess) {
        super();
    }
    
    public final boolean isEnabled() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.ExportFormat getDefaultFormat() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.repository.TaskerPeriod getDefaultPeriod() {
        return null;
    }
    
    public final boolean getDefaultIncludeMedia() {
        return false;
    }
    
    public final boolean getCopySuccess() {
        return false;
    }
    
    public TaskerIntegrationUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.ExportFormat component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.repository.TaskerPeriod component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.settings.TaskerIntegrationUiState copy(boolean isEnabled, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ExportFormat defaultFormat, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TaskerPeriod defaultPeriod, boolean defaultIncludeMedia, boolean copySuccess) {
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