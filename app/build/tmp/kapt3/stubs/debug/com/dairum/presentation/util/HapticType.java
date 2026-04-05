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
 * Types of haptic feedback available in the app
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\r\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r\u00a8\u0006\u000e"}, d2 = {"Lcom/dairum/presentation/util/HapticType;", "", "(Ljava/lang/String;I)V", "LightTap", "MediumTap", "HeavyTap", "Success", "Warning", "Error", "Selection", "TextHandle", "LongPress", "SwipeAction", "Delete", "app_debug"})
public enum HapticType {
    /*public static final*/ LightTap /* = new LightTap() */,
    /*public static final*/ MediumTap /* = new MediumTap() */,
    /*public static final*/ HeavyTap /* = new HeavyTap() */,
    /*public static final*/ Success /* = new Success() */,
    /*public static final*/ Warning /* = new Warning() */,
    /*public static final*/ Error /* = new Error() */,
    /*public static final*/ Selection /* = new Selection() */,
    /*public static final*/ TextHandle /* = new TextHandle() */,
    /*public static final*/ LongPress /* = new LongPress() */,
    /*public static final*/ SwipeAction /* = new SwipeAction() */,
    /*public static final*/ Delete /* = new Delete() */;
    
    HapticType() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.presentation.util.HapticType> getEntries() {
        return null;
    }
}