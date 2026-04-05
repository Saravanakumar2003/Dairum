package com.dairum.data.security;

import android.content.Context;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.dairum.domain.repository.SettingsRepository;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.Flow;
import java.security.MessageDigest;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J>\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\r0\u000f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\fJ$\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00102\b\b\u0002\u0010\u0015\u001a\u00020\u00102\b\b\u0002\u0010\u0016\u001a\u00020\u0010J\u0006\u0010\u0017\u001a\u00020\u0018J\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00180\u001aJ\u0010\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0010H\u0002J\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00180\u001aJ\u0006\u0010\u001e\u001a\u00020\u0018J\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00180\u001aJ\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00180\u001aJ\u0006\u0010!\u001a\u00020\u0018J\u000e\u0010\"\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010#J\u0016\u0010$\u001a\u00020\r2\u0006\u0010%\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010&J\u0016\u0010\'\u001a\u00020\r2\u0006\u0010%\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010&J\u0016\u0010(\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010)J\u0016\u0010*\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010)R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/dairum/data/security/AppLockManager;", "", "context", "Landroid/content/Context;", "settingsRepository", "Lcom/dairum/domain/repository/SettingsRepository;", "(Landroid/content/Context;Lcom/dairum/domain/repository/SettingsRepository;)V", "createBiometricPrompt", "Landroidx/biometric/BiometricPrompt;", "activity", "Landroidx/fragment/app/FragmentActivity;", "onSuccess", "Lkotlin/Function0;", "", "onError", "Lkotlin/Function1;", "", "onFailed", "createPromptInfo", "Landroidx/biometric/BiometricPrompt$PromptInfo;", "title", "subtitle", "negativeButtonText", "hasBiometricHardware", "", "hasPinSetup", "Lkotlinx/coroutines/flow/Flow;", "hashPin", "pin", "isAppLockEnabled", "isBiometricAvailable", "isBiometricEnabled", "isLockConfigured", "isStrongBiometricAvailable", "removePin", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setAppLockEnabled", "enabled", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setBiometricEnabled", "setPin", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "verifyPin", "app_debug"})
public final class AppLockManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.SettingsRepository settingsRepository = null;
    
    @javax.inject.Inject()
    public AppLockManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.SettingsRepository settingsRepository) {
        super();
    }
    
    /**
     * Check if biometric authentication is available on this device
     * Checks for both BIOMETRIC_STRONG and BIOMETRIC_WEAK for wider device compatibility
     */
    public final boolean isBiometricAvailable() {
        return false;
    }
    
    /**
     * Check if strong biometric is available (fingerprint, face ID with proper depth sensor)
     */
    public final boolean isStrongBiometricAvailable() {
        return false;
    }
    
    /**
     * Check if biometric hardware exists (even if not enrolled)
     */
    public final boolean hasBiometricHardware() {
        return false;
    }
    
    /**
     * Flow to observe if app lock is enabled
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isAppLockEnabled() {
        return null;
    }
    
    /**
     * Flow to observe if biometric is enabled for app lock
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isBiometricEnabled() {
        return null;
    }
    
    /**
     * Check if a PIN has been set up
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> hasPinSetup() {
        return null;
    }
    
    /**
     * Check if app lock is configured (enabled and PIN/biometric is set up)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isLockConfigured() {
        return null;
    }
    
    /**
     * Set up or change the PIN
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Verify if the provided PIN matches the stored hash
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object verifyPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Remove the PIN
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object removePin(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Enable or disable app lock
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setAppLockEnabled(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Enable or disable biometric authentication
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setBiometricEnabled(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Hash a PIN using SHA-256
     */
    private final java.lang.String hashPin(java.lang.String pin) {
        return null;
    }
    
    /**
     * Create a BiometricPrompt for authentication
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.biometric.BiometricPrompt createBiometricPrompt(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onError, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onFailed) {
        return null;
    }
    
    /**
     * Create prompt info for biometric dialog
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.biometric.BiometricPrompt.PromptInfo createPromptInfo(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String subtitle, @org.jetbrains.annotations.NotNull()
    java.lang.String negativeButtonText) {
        return null;
    }
}