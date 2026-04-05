package com.dairum.presentation.screens.settings;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.MoodCheckInConfig;
import com.dairum.domain.repository.NotificationRepository;
import com.dairum.domain.repository.PersonRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.LocalDateTime;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u000f\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u0006\u0010\u0010\u001a\u00020\u000fJ\u0006\u0010\u0011\u001a\u00020\u000fJ \u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0014J\u001e\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J<\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001e0!J\u0006\u0010\"\u001a\u00020\u000fJ\u001e\u0010#\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u00142\u0006\u0010$\u001a\u00020%J\u000e\u0010&\u001a\u00020\u000f2\u0006\u0010\'\u001a\u00020\u0016J\u000e\u0010(\u001a\u00020\u000f2\u0006\u0010\'\u001a\u00020\u0016J\b\u0010)\u001a\u00020\u000fH\u0002J\b\u0010*\u001a\u00020\u000fH\u0002J\b\u0010+\u001a\u00020\u000fH\u0002J\u000e\u0010,\u001a\u00020\u000f2\u0006\u0010-\u001a\u00020.J\u0006\u0010/\u001a\u00020\u000fJ\u0006\u00100\u001a\u00020\u000fJ\u000e\u00101\u001a\u00020\u000f2\u0006\u00102\u001a\u00020.J\u000e\u00103\u001a\u00020\u000f2\u0006\u00104\u001a\u00020\u001eJ\u0016\u00105\u001a\u00020\u000f2\u0006\u00106\u001a\u00020\u001e2\u0006\u00107\u001a\u00020\u001eJ\u0016\u00108\u001a\u00020\u000f2\u0006\u0010\'\u001a\u00020\u00162\u0006\u00109\u001a\u00020.J\u0016\u0010:\u001a\u00020\u000f2\u0006\u0010\'\u001a\u00020\u00162\u0006\u00109\u001a\u00020.J\u001c\u0010;\u001a\u00020\u000f2\u0006\u0010\'\u001a\u00020\u00162\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001e0!J\u001e\u0010<\u001a\u00020\u000f2\u0006\u0010\'\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006="}, d2 = {"Lcom/dairum/presentation/screens/settings/NotificationSettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "notificationRepository", "Lcom/dairum/domain/repository/NotificationRepository;", "personRepository", "Lcom/dairum/domain/repository/PersonRepository;", "(Lcom/dairum/domain/repository/NotificationRepository;Lcom/dairum/domain/repository/PersonRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/settings/NotificationSettingsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "checkNotificationPermission", "", "clearPermissionRequest", "clearTestNotificationSent", "createAnniversaryReminder", "title", "", "date", "", "description", "createBirthdayReminder", "personId", "name", "createCustomReminder", "message", "hour", "", "minute", "days", "", "createDefaultReminders", "createGenericReminder", "dateTime", "Ljava/time/LocalDateTime;", "deleteReminder", "reminderId", "deleteSmartReminder", "loadMoodCheckInConfig", "loadReminders", "loadSmartReminders", "onPermissionResult", "granted", "", "scheduleNotifications", "sendTestNotification", "setMoodCheckInEnabled", "enabled", "setMoodCheckInInterval", "hours", "setMoodCheckInTimeRange", "startHour", "endHour", "toggleReminder", "isEnabled", "toggleSmartReminder", "updateReminderDays", "updateReminderTime", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class NotificationSettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.NotificationRepository notificationRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.PersonRepository personRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.settings.NotificationSettingsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.settings.NotificationSettingsUiState> uiState = null;
    
    @javax.inject.Inject()
    public NotificationSettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.NotificationRepository notificationRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.PersonRepository personRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.settings.NotificationSettingsUiState> getUiState() {
        return null;
    }
    
    private final void checkNotificationPermission() {
    }
    
    public final void onPermissionResult(boolean granted) {
    }
    
    private final void loadReminders() {
    }
    
    private final void loadSmartReminders() {
    }
    
    private final void loadMoodCheckInConfig() {
    }
    
    public final void createDefaultReminders() {
    }
    
    public final void toggleReminder(long reminderId, boolean isEnabled) {
    }
    
    public final void updateReminderTime(long reminderId, int hour, int minute) {
    }
    
    public final void updateReminderDays(long reminderId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Integer> days) {
    }
    
    public final void deleteReminder(long reminderId) {
    }
    
    public final void createCustomReminder(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, int hour, int minute, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Integer> days) {
    }
    
    public final void toggleSmartReminder(long reminderId, boolean isEnabled) {
    }
    
    public final void createBirthdayReminder(long personId, @org.jetbrains.annotations.NotNull()
    java.lang.String name, long date) {
    }
    
    public final void createAnniversaryReminder(@org.jetbrains.annotations.NotNull()
    java.lang.String title, long date, @org.jetbrains.annotations.Nullable()
    java.lang.String description) {
    }
    
    public final void deleteSmartReminder(long reminderId) {
    }
    
    public final void scheduleNotifications() {
    }
    
    public final void sendTestNotification() {
    }
    
    public final void clearTestNotificationSent() {
    }
    
    public final void clearPermissionRequest() {
    }
    
    public final void setMoodCheckInEnabled(boolean enabled) {
    }
    
    public final void setMoodCheckInInterval(int hours) {
    }
    
    public final void setMoodCheckInTimeRange(int startHour, int endHour) {
    }
    
    public final void createGenericReminder(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime dateTime) {
    }
}