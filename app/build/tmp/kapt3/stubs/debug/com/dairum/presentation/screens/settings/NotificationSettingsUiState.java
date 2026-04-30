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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b,\b\u0086\b\u0018\u00002\u00020\u0001B\u00c7\u0001\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\r\u001a\u00020\b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0014\u001a\u00020\b\u0012\u001a\b\u0002\u0010\u0015\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u00160\u0003\u0012\b\b\u0002\u0010\u0017\u001a\u00020\n\u0012\b\b\u0002\u0010\u0018\u001a\u00020\n\u00a2\u0006\u0002\u0010\u0019J\u000f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u0011H\u00c6\u0003J\t\u0010/\u001a\u00020\u0011H\u00c6\u0003J\t\u00100\u001a\u00020\u0011H\u00c6\u0003J\t\u00101\u001a\u00020\bH\u00c6\u0003J\u001b\u00102\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u00160\u0003H\u00c6\u0003J\t\u00103\u001a\u00020\nH\u00c6\u0003J\t\u00104\u001a\u00020\nH\u00c6\u0003J\u000f\u00105\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00c6\u0003J\t\u00106\u001a\u00020\bH\u00c6\u0003J\u000b\u00107\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u00108\u001a\u00020\bH\u00c6\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u0010:\u001a\u00020\bH\u00c6\u0003J\t\u0010;\u001a\u00020\bH\u00c6\u0003J\t\u0010<\u001a\u00020\bH\u00c6\u0003J\u00cb\u0001\u0010=\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\u0013\u001a\u00020\u00112\b\b\u0002\u0010\u0014\u001a\u00020\b2\u001a\b\u0002\u0010\u0015\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u00160\u00032\b\b\u0002\u0010\u0017\u001a\u00020\n2\b\b\u0002\u0010\u0018\u001a\u00020\nH\u00c6\u0001J\u0013\u0010>\u001a\u00020\b2\b\u0010?\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010@\u001a\u00020\u0011H\u00d6\u0001J\t\u0010A\u001a\u00020\nH\u00d6\u0001R\u0011\u0010\u0018\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR#\u0010\u0015\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u00160\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0017\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001bR\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001bR\u0011\u0010\r\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010!R\u0011\u0010\u000f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010!R\u0011\u0010\u0013\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010$R\u0011\u0010\u0012\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010$R\u0011\u0010\u0014\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010!R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001dR\u0011\u0010\u000e\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010!R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001dR\u0013\u0010\f\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001bR\u0011\u0010\u000b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010!\u00a8\u0006B"}, d2 = {"Lcom/dairum/presentation/screens/settings/NotificationSettingsUiState;", "", "reminderConfigs", "", "Lcom/dairum/domain/model/ReminderConfig;", "smartReminders", "Lcom/dairum/domain/model/SmartReminder;", "isLoading", "", "error", "", "testNotificationSent", "testNotificationMessage", "hasNotificationPermission", "showPermissionRequest", "moodCheckInEnabled", "moodCheckInIntervalHours", "", "moodCheckInStartHour", "moodCheckInEndHour", "multipleDailyRemindersEnabled", "dailyReminderTimes", "Lkotlin/Pair;", "dailyReminderTitle", "dailyReminderMessage", "(Ljava/util/List;Ljava/util/List;ZLjava/lang/String;ZLjava/lang/String;ZZZIIIZLjava/util/List;Ljava/lang/String;Ljava/lang/String;)V", "getDailyReminderMessage", "()Ljava/lang/String;", "getDailyReminderTimes", "()Ljava/util/List;", "getDailyReminderTitle", "getError", "getHasNotificationPermission", "()Z", "getMoodCheckInEnabled", "getMoodCheckInEndHour", "()I", "getMoodCheckInIntervalHours", "getMoodCheckInStartHour", "getMultipleDailyRemindersEnabled", "getReminderConfigs", "getShowPermissionRequest", "getSmartReminders", "getTestNotificationMessage", "getTestNotificationSent", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class NotificationSettingsUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.ReminderConfig> reminderConfigs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.SmartReminder> smartReminders = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    private final boolean testNotificationSent = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String testNotificationMessage = null;
    private final boolean hasNotificationPermission = false;
    private final boolean showPermissionRequest = false;
    private final boolean moodCheckInEnabled = false;
    private final int moodCheckInIntervalHours = 0;
    private final int moodCheckInStartHour = 0;
    private final int moodCheckInEndHour = 0;
    private final boolean multipleDailyRemindersEnabled = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> dailyReminderTimes = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String dailyReminderTitle = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String dailyReminderMessage = null;
    
    public NotificationSettingsUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ReminderConfig> reminderConfigs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.SmartReminder> smartReminders, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, boolean testNotificationSent, @org.jetbrains.annotations.Nullable()
    java.lang.String testNotificationMessage, boolean hasNotificationPermission, boolean showPermissionRequest, boolean moodCheckInEnabled, int moodCheckInIntervalHours, int moodCheckInStartHour, int moodCheckInEndHour, boolean multipleDailyRemindersEnabled, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> dailyReminderTimes, @org.jetbrains.annotations.NotNull()
    java.lang.String dailyReminderTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String dailyReminderMessage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ReminderConfig> getReminderConfigs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.SmartReminder> getSmartReminders() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public final boolean getTestNotificationSent() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTestNotificationMessage() {
        return null;
    }
    
    public final boolean getHasNotificationPermission() {
        return false;
    }
    
    public final boolean getShowPermissionRequest() {
        return false;
    }
    
    public final boolean getMoodCheckInEnabled() {
        return false;
    }
    
    public final int getMoodCheckInIntervalHours() {
        return 0;
    }
    
    public final int getMoodCheckInStartHour() {
        return 0;
    }
    
    public final int getMoodCheckInEndHour() {
        return 0;
    }
    
    public final boolean getMultipleDailyRemindersEnabled() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> getDailyReminderTimes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDailyReminderTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDailyReminderMessage() {
        return null;
    }
    
    public NotificationSettingsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ReminderConfig> component1() {
        return null;
    }
    
    public final int component10() {
        return 0;
    }
    
    public final int component11() {
        return 0;
    }
    
    public final int component12() {
        return 0;
    }
    
    public final boolean component13() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.SmartReminder> component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
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
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.settings.NotificationSettingsUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ReminderConfig> reminderConfigs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.SmartReminder> smartReminders, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, boolean testNotificationSent, @org.jetbrains.annotations.Nullable()
    java.lang.String testNotificationMessage, boolean hasNotificationPermission, boolean showPermissionRequest, boolean moodCheckInEnabled, int moodCheckInIntervalHours, int moodCheckInStartHour, int moodCheckInEndHour, boolean multipleDailyRemindersEnabled, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Integer>> dailyReminderTimes, @org.jetbrains.annotations.NotNull()
    java.lang.String dailyReminderTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String dailyReminderMessage) {
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