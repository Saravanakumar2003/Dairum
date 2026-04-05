package com.dairum.presentation.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.core.content.getSystemService

/**
 * Haptic feedback utility for providing tactile feedback throughout the app.
 * Supports different feedback types for various interactions.
 */

/**
 * Types of haptic feedback available in the app
 */
enum class HapticType {
    /** Light tap feedback - for selections, toggles */
    LightTap,
    
    /** Medium tap feedback - for confirmations */
    MediumTap,
    
    /** Heavy tap feedback - for important actions */
    HeavyTap,
    
    /** Success feedback - for completed actions */
    Success,
    
    /** Warning feedback - for cautionary actions */
    Warning,
    
    /** Error feedback - for failed actions */
    Error,
    
    /** Selection change feedback */
    Selection,
    
    /** Text selection feedback */
    TextHandle,
    
    /** Long press feedback */
    LongPress,
    
    /** Swipe action feedback */
    SwipeAction,
    
    /** Delete action feedback */
    Delete
}

/**
 * Haptic feedback controller
 */
class HapticController(
    private val context: Context,
    private val view: View
) {
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.getSystemService<VibratorManager>()?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
    
    /**
     * Perform haptic feedback of the specified type
     */
    fun perform(type: HapticType) {
        when (type) {
            HapticType.LightTap -> performLightTap()
            HapticType.MediumTap -> performMediumTap()
            HapticType.HeavyTap -> performHeavyTap()
            HapticType.Success -> performSuccess()
            HapticType.Warning -> performWarning()
            HapticType.Error -> performError()
            HapticType.Selection -> performSelection()
            HapticType.TextHandle -> performTextHandle()
            HapticType.LongPress -> performLongPress()
            HapticType.SwipeAction -> performSwipeAction()
            HapticType.Delete -> performDelete()
        }
    }
    
    private fun performLightTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        } else {
            vibrate(10L, VibrationEffect.EFFECT_TICK)
        }
    }
    
    private fun performMediumTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } else {
            vibrate(20L, VibrationEffect.EFFECT_CLICK)
        }
    }
    
    private fun performHeavyTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
        } else {
            vibrate(30L, VibrationEffect.EFFECT_HEAVY_CLICK)
        }
    }
    
    private fun performSuccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } else {
            // Double tap pattern for success
            vibratePattern(longArrayOf(0, 15, 50, 15))
        }
    }
    
    private fun performWarning() {
        // Triple short vibration for warning
        vibratePattern(longArrayOf(0, 10, 30, 10, 30, 10))
    }
    
    private fun performError() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
        } else {
            // Strong buzz for error
            vibrate(50L, VibrationEffect.EFFECT_DOUBLE_CLICK)
        }
    }
    
    private fun performSelection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_TICK)
        } else {
            vibrate(5L, VibrationEffect.EFFECT_TICK)
        }
    }
    
    private fun performTextHandle() {
        view.performHapticFeedback(HapticFeedbackConstants.TEXT_HANDLE_MOVE)
    }
    
    private fun performLongPress() {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
    
    private fun performSwipeAction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.GESTURE_START)
        } else {
            vibrate(15L, VibrationEffect.EFFECT_CLICK)
        }
    }
    
    private fun performDelete() {
        // Distinctive pattern for delete
        vibratePattern(longArrayOf(0, 20, 40, 30))
    }
    
    private fun vibrate(durationMs: Long, effectId: Int) {
        vibrator?.let { v ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                v.vibrate(VibrationEffect.createPredefined(effectId))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(durationMs)
            }
        }
    }
    
    private fun vibratePattern(pattern: LongArray) {
        vibrator?.let { v ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(pattern, -1)
            }
        }
    }
}

/**
 * Remember a HapticController for use in Compose
 */
@Composable
fun rememberHapticController(): HapticController {
    val context = LocalContext.current
    val view = LocalView.current
    
    return remember(context, view) {
        HapticController(context, view)
    }
}

/**
 * Extension function for quick haptic feedback in composables
 */
@Composable
fun rememberHapticFeedback(): HapticFeedback {
    return LocalHapticFeedback.current
}

/**
 * Perform standard Compose haptic feedback
 */
fun HapticFeedback.performClick() {
    performHapticFeedback(HapticFeedbackType.LongPress)
}

fun HapticFeedback.performSelect() {
    performHapticFeedback(HapticFeedbackType.TextHandleMove)
}
