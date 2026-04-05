package com.dairum.presentation.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.HapticFeedbackConstants;
import android.view.View;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.hapticfeedback.HapticFeedback;
import androidx.compose.ui.hapticfeedback.HapticFeedbackType;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\b\u0010\u0000\u001a\u00020\u0001H\u0007\u001a\b\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\n\u0010\u0004\u001a\u00020\u0005*\u00020\u0003\u001a\n\u0010\u0006\u001a\u00020\u0005*\u00020\u0003\u00a8\u0006\u0007"}, d2 = {"rememberHapticController", "Lcom/dairum/presentation/util/HapticController;", "rememberHapticFeedback", "Landroidx/compose/ui/hapticfeedback/HapticFeedback;", "performClick", "", "performSelect", "app_debug"})
public final class HapticFeedbackKt {
    
    /**
     * Remember a HapticController for use in Compose
     */
    @androidx.compose.runtime.Composable()
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.presentation.util.HapticController rememberHapticController() {
        return null;
    }
    
    /**
     * Extension function for quick haptic feedback in composables
     */
    @androidx.compose.runtime.Composable()
    @org.jetbrains.annotations.NotNull()
    public static final androidx.compose.ui.hapticfeedback.HapticFeedback rememberHapticFeedback() {
        return null;
    }
    
    /**
     * Perform standard Compose haptic feedback
     */
    public static final void performClick(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.hapticfeedback.HapticFeedback $this$performClick) {
    }
    
    public static final void performSelect(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.hapticfeedback.HapticFeedback $this$performSelect) {
    }
}