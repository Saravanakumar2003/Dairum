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

/**
 * Haptic feedback controller
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0016\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\b\u0010\r\u001a\u00020\nH\u0002J\b\u0010\u000e\u001a\u00020\nH\u0002J\b\u0010\u000f\u001a\u00020\nH\u0002J\b\u0010\u0010\u001a\u00020\nH\u0002J\b\u0010\u0011\u001a\u00020\nH\u0002J\b\u0010\u0012\u001a\u00020\nH\u0002J\b\u0010\u0013\u001a\u00020\nH\u0002J\b\u0010\u0014\u001a\u00020\nH\u0002J\b\u0010\u0015\u001a\u00020\nH\u0002J\b\u0010\u0016\u001a\u00020\nH\u0002J\b\u0010\u0017\u001a\u00020\nH\u0002J\u0018\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/dairum/presentation/util/HapticController;", "", "context", "Landroid/content/Context;", "view", "Landroid/view/View;", "(Landroid/content/Context;Landroid/view/View;)V", "vibrator", "Landroid/os/Vibrator;", "perform", "", "type", "Lcom/dairum/presentation/util/HapticType;", "performDelete", "performError", "performHeavyTap", "performLightTap", "performLongPress", "performMediumTap", "performSelection", "performSuccess", "performSwipeAction", "performTextHandle", "performWarning", "vibrate", "durationMs", "", "effectId", "", "vibratePattern", "pattern", "", "app_debug"})
public final class HapticController {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.view.View view = null;
    @org.jetbrains.annotations.Nullable()
    private final android.os.Vibrator vibrator = null;
    
    public HapticController(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.view.View view) {
        super();
    }
    
    /**
     * Perform haptic feedback of the specified type
     */
    public final void perform(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.util.HapticType type) {
    }
    
    private final void performLightTap() {
    }
    
    private final void performMediumTap() {
    }
    
    private final void performHeavyTap() {
    }
    
    private final void performSuccess() {
    }
    
    private final void performWarning() {
    }
    
    private final void performError() {
    }
    
    private final void performSelection() {
    }
    
    private final void performTextHandle() {
    }
    
    private final void performLongPress() {
    }
    
    private final void performSwipeAction() {
    }
    
    private final void performDelete() {
    }
    
    private final void vibrate(long durationMs, int effectId) {
    }
    
    private final void vibratePattern(long[] pattern) {
    }
}