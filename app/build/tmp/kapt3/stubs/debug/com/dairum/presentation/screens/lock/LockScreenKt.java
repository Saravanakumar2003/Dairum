package com.dairum.presentation.screens.lock;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.biometric.BiometricManager;
import androidx.compose.animation.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.hapticfeedback.HapticFeedbackType;
import androidx.compose.ui.text.style.TextAlign;
import androidx.fragment.app.FragmentActivity;
import com.dairum.data.security.AppLockManager;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000J\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a0\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001aC\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\u0015\b\u0002\u0010\u000e\u001a\u000f\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0007\u00a2\u0006\u0002\b\u000fH\u0003\u001aD\u0010\u0010\u001a\u00020\u00012\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u000e\u0010\u0014\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001a\"\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001aH\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u00122\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0002\u00a8\u0006\u001f"}, d2 = {"LockScreen", "", "appLockManager", "Lcom/dairum/data/security/AppLockManager;", "biometricEnabled", "", "onUnlocked", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "LockScreenKey", "text", "", "onClick", "content", "Landroidx/compose/runtime/Composable;", "LockScreenKeypad", "onDigit", "Lkotlin/Function1;", "onBackspace", "onBiometric", "LockScreenPinDots", "length", "", "hasError", "triggerBiometric", "activity", "Landroidx/fragment/app/FragmentActivity;", "onSuccess", "onError", "onFallbackToPin", "app_debug"})
public final class LockScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void LockScreen(@org.jetbrains.annotations.NotNull()
    com.dairum.data.security.AppLockManager appLockManager, boolean biometricEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onUnlocked, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LockScreenPinDots(int length, boolean hasError, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LockScreenKeypad(kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDigit, kotlin.jvm.functions.Function0<kotlin.Unit> onBackspace, kotlin.jvm.functions.Function0<kotlin.Unit> onBiometric, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LockScreenKey(java.lang.String text, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    private static final void triggerBiometric(com.dairum.data.security.AppLockManager appLockManager, androidx.fragment.app.FragmentActivity activity, kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onError, kotlin.jvm.functions.Function0<kotlin.Unit> onFallbackToPin) {
    }
}