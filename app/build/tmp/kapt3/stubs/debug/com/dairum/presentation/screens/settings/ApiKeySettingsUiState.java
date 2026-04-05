package com.dairum.presentation.screens.settings;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.repository.ApiKeyRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Bg\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0007\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0012J\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0012J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0012J\t\u0010\u001e\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\rH\u00c6\u0003Jp\u0010!\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00072\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\rH\u00c6\u0001\u00a2\u0006\u0002\u0010\"J\u0013\u0010#\u001a\u00020\u00072\b\u0010$\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010%\u001a\u00020&H\u00d6\u0001J\t\u0010\'\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0015\u0010\t\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\t\u0010\u0012R\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\u0006\u0010\u0012R\u0011\u0010\n\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0014R\u0011\u0010\u000b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0014R\u0015\u0010\b\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\b\u0010\u0012R\u0013\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010\u00a8\u0006("}, d2 = {"Lcom/dairum/presentation/screens/settings/ApiKeySettingsUiState;", "", "googleMapsApiKey", "", "weatherApiKey", "googleCalendarApiKey", "isGoogleMapsKeyValid", "", "isWeatherKeyValid", "isGoogleCalendarKeyValid", "isLoading", "isTesting", "testResult", "Lcom/dairum/presentation/screens/settings/TestResult;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;ZZLcom/dairum/presentation/screens/settings/TestResult;)V", "getGoogleCalendarApiKey", "()Ljava/lang/String;", "getGoogleMapsApiKey", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "()Z", "getTestResult", "()Lcom/dairum/presentation/screens/settings/TestResult;", "getWeatherApiKey", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;ZZLcom/dairum/presentation/screens/settings/TestResult;)Lcom/dairum/presentation/screens/settings/ApiKeySettingsUiState;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class ApiKeySettingsUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String googleMapsApiKey = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String weatherApiKey = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String googleCalendarApiKey = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Boolean isGoogleMapsKeyValid = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Boolean isWeatherKeyValid = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Boolean isGoogleCalendarKeyValid = null;
    private final boolean isLoading = false;
    private final boolean isTesting = false;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.presentation.screens.settings.TestResult testResult = null;
    
    public ApiKeySettingsUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String googleMapsApiKey, @org.jetbrains.annotations.NotNull()
    java.lang.String weatherApiKey, @org.jetbrains.annotations.NotNull()
    java.lang.String googleCalendarApiKey, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean isGoogleMapsKeyValid, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean isWeatherKeyValid, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean isGoogleCalendarKeyValid, boolean isLoading, boolean isTesting, @org.jetbrains.annotations.Nullable()
    com.dairum.presentation.screens.settings.TestResult testResult) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGoogleMapsApiKey() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWeatherApiKey() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGoogleCalendarApiKey() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean isGoogleMapsKeyValid() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean isWeatherKeyValid() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean isGoogleCalendarKeyValid() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    public final boolean isTesting() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.presentation.screens.settings.TestResult getTestResult() {
        return null;
    }
    
    public ApiKeySettingsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.presentation.screens.settings.TestResult component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.settings.ApiKeySettingsUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String googleMapsApiKey, @org.jetbrains.annotations.NotNull()
    java.lang.String weatherApiKey, @org.jetbrains.annotations.NotNull()
    java.lang.String googleCalendarApiKey, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean isGoogleMapsKeyValid, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean isWeatherKeyValid, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean isGoogleCalendarKeyValid, boolean isLoading, boolean isTesting, @org.jetbrains.annotations.Nullable()
    com.dairum.presentation.screens.settings.TestResult testResult) {
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