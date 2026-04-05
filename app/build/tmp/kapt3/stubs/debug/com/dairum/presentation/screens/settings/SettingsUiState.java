package com.dairum.presentation.screens.settings;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.Category;
import com.dairum.domain.repository.CategoryRepository;
import com.dairum.domain.repository.SettingsRepository;
import com.dairum.domain.repository.ThemeMode;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BM\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0005H\u00c6\u0003JQ\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u00052\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020!H\u00d6\u0001J\t\u0010\"\u001a\u00020#H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000e\u00a8\u0006$"}, d2 = {"Lcom/dairum/presentation/screens/settings/SettingsUiState;", "", "themeMode", "Lcom/dairum/domain/repository/ThemeMode;", "useDynamicColors", "", "weatherAutoFetch", "locationAutoFetch", "appLockEnabled", "defaultCategory", "Lcom/dairum/domain/model/Category;", "isLoading", "(Lcom/dairum/domain/repository/ThemeMode;ZZZZLcom/dairum/domain/model/Category;Z)V", "getAppLockEnabled", "()Z", "getDefaultCategory", "()Lcom/dairum/domain/model/Category;", "getLocationAutoFetch", "getThemeMode", "()Lcom/dairum/domain/repository/ThemeMode;", "getUseDynamicColors", "getWeatherAutoFetch", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class SettingsUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ThemeMode themeMode = null;
    private final boolean useDynamicColors = false;
    private final boolean weatherAutoFetch = false;
    private final boolean locationAutoFetch = false;
    private final boolean appLockEnabled = false;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Category defaultCategory = null;
    private final boolean isLoading = false;
    
    public SettingsUiState(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ThemeMode themeMode, boolean useDynamicColors, boolean weatherAutoFetch, boolean locationAutoFetch, boolean appLockEnabled, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category defaultCategory, boolean isLoading) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.repository.ThemeMode getThemeMode() {
        return null;
    }
    
    public final boolean getUseDynamicColors() {
        return false;
    }
    
    public final boolean getWeatherAutoFetch() {
        return false;
    }
    
    public final boolean getLocationAutoFetch() {
        return false;
    }
    
    public final boolean getAppLockEnabled() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Category getDefaultCategory() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    public SettingsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.repository.ThemeMode component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Category component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.settings.SettingsUiState copy(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ThemeMode themeMode, boolean useDynamicColors, boolean weatherAutoFetch, boolean locationAutoFetch, boolean appLockEnabled, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category defaultCategory, boolean isLoading) {
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