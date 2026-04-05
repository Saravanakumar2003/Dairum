package com.dairum.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.dairum.domain.model.ExportFormat;
import com.dairum.domain.repository.TaskerPeriod;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Tasker/Locale/MacroDroid plugin configuration activity.
 * This activity is shown when the user selects Dairum as an action in their automation app.
 * The user can configure export settings which are then saved and passed back to the automation app.
 */
@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J(\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0002J\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0014J(\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0002\u00a8\u0006\u0010"}, d2 = {"Lcom/dairum/plugin/PluginEditActivity;", "Landroidx/activity/ComponentActivity;", "()V", "generateBlurb", "", "action", "format", "period", "includeMedia", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "saveConfiguration", "Companion", "app_debug"})
public final class PluginEditActivity extends androidx.activity.ComponentActivity {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_BUNDLE = "com.twofortyfouram.locale.intent.extra.BUNDLE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_STRING_BLURB = "com.twofortyfouram.locale.intent.extra.BLURB";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BUNDLE_ACTION = "action";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BUNDLE_FORMAT = "format";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BUNDLE_PERIOD = "period";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BUNDLE_INCLUDE_MEDIA = "include_media";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_EXPORT = "export";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_BACKUP = "backup";
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.plugin.PluginEditActivity.Companion Companion = null;
    
    public PluginEditActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void saveConfiguration(java.lang.String action, java.lang.String format, java.lang.String period, boolean includeMedia) {
    }
    
    private final java.lang.String generateBlurb(java.lang.String action, java.lang.String format, java.lang.String period, boolean includeMedia) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/dairum/plugin/PluginEditActivity$Companion;", "", "()V", "ACTION_BACKUP", "", "ACTION_EXPORT", "BUNDLE_ACTION", "BUNDLE_FORMAT", "BUNDLE_INCLUDE_MEDIA", "BUNDLE_PERIOD", "EXTRA_BUNDLE", "EXTRA_STRING_BLURB", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}