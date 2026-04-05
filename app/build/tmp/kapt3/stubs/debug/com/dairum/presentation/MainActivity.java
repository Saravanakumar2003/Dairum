package com.dairum.presentation;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import com.dairum.R;
import com.dairum.data.security.AppLockManager;
import com.dairum.presentation.navigation.Screen;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0014R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/dairum/presentation/MainActivity;", "Landroidx/activity/ComponentActivity;", "()V", "appLockManager", "Lcom/dairum/data/security/AppLockManager;", "getAppLockManager", "()Lcom/dairum/data/security/AppLockManager;", "setAppLockManager", "(Lcom/dairum/data/security/AppLockManager;)V", "keepSplashScreen", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class MainActivity extends androidx.activity.ComponentActivity {
    @javax.inject.Inject()
    public com.dairum.data.security.AppLockManager appLockManager;
    private boolean keepSplashScreen = true;
    
    public MainActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.security.AppLockManager getAppLockManager() {
        return null;
    }
    
    public final void setAppLockManager(@org.jetbrains.annotations.NotNull()
    com.dairum.data.security.AppLockManager p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
}