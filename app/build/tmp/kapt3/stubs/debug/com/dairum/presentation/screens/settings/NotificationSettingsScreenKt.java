package com.dairum.presentation.screens.settings;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.dairum.domain.model.ReminderConfig;
import com.dairum.domain.model.ReminderType;
import com.dairum.domain.model.SmartReminder;
import com.dairum.domain.model.SmartReminderType;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000x\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u00aa\u0001\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0091\u0001\u0010\u0004\u001a\u008c\u0001\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\r\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\u000b0\u000e\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u009d\u0001\u0010\u0010\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u000326\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\u0013\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0014\u0012\u0004\u0012\u00020\u00010\u00122M\u0010\u0015\u001aI\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\u0013\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0014\u0012\u0015\u0012\u0013\u0018\u00010\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0017\u0012\u0004\u0012\u00020\u00010\u0016H\u0003\u001a\u0098\u0001\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u000b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032`\u0010\u001e\u001a\\\u0012\u0013\u0012\u00110\u001a\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b( \u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(!\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\"\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(#\u0012\u0004\u0012\u00020\u00010\u001fH\u0003\u001a \u0010$\u001a\u00020\u00012\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010&\u001a\u00020\'H\u0007\u001a@\u0010(\u001a\u00020\u00012\u0006\u0010)\u001a\u00020*2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00010,2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a2\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u0002012\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00010,2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a8\u00102\u001a\u00020\u00012\u0006\u00103\u001a\u0002042\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\b\b\u0002\u0010 \u001a\u00020\u001a2\f\u00105\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a\u0016\u00106\u001a\u00020\u00062\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000eH\u0002\u00a8\u00067"}, d2 = {"AddReminderDialog", "", "onDismiss", "Lkotlin/Function0;", "onCreate", "Lkotlin/Function6;", "", "Lkotlin/ParameterName;", "name", "title", "message", "", "hour", "minute", "", "days", "AddSmartReminderDialog", "onCreateBirthday", "Lkotlin/Function2;", "", "date", "onCreateAnniversary", "Lkotlin/Function3;", "description", "MoodCheckInConfigDialog", "isEnabled", "", "intervalHours", "startHour", "endHour", "onSave", "Lkotlin/Function4;", "enabled", "interval", "start", "end", "NotificationSettingsScreen", "onNavigateUp", "viewModel", "Lcom/dairum/presentation/screens/settings/NotificationSettingsViewModel;", "ReminderConfigItem", "config", "Lcom/dairum/domain/model/ReminderConfig;", "onToggle", "Lkotlin/Function1;", "onTimeClick", "onDelete", "SmartReminderItem", "reminder", "Lcom/dairum/domain/model/SmartReminder;", "SmartReminderTypeCard", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "onClick", "formatDays", "app_debug"})
public final class NotificationSettingsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void NotificationSettingsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateUp, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.settings.NotificationSettingsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReminderConfigItem(com.dairum.domain.model.ReminderConfig config, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onToggle, kotlin.jvm.functions.Function0<kotlin.Unit> onTimeClick, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SmartReminderTypeCard(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String description, boolean enabled, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SmartReminderItem(com.dairum.domain.model.SmartReminder reminder, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onToggle, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void AddReminderDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function6<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.util.List<java.lang.Integer>, kotlin.Unit> onCreate) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void AddSmartReminderDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.Long, kotlin.Unit> onCreateBirthday, kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.Long, ? super java.lang.String, kotlin.Unit> onCreateAnniversary) {
    }
    
    private static final java.lang.String formatDays(java.util.List<java.lang.Integer> days) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MoodCheckInConfigDialog(boolean isEnabled, int intervalHours, int startHour, int endHour, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function4<? super java.lang.Boolean, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onSave) {
    }
}