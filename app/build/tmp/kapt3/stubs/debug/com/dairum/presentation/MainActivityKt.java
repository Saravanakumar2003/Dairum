package com.dairum.presentation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\"\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a \u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\bH\u0007\u001a(\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\bH\u0007\"\u0017\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004\u00a8\u0006\r"}, d2 = {"bottomNavItems", "", "Lcom/dairum/presentation/BottomNavItemData;", "getBottomNavItems", "()Ljava/util/List;", "DairumApp", "", "initialDestination", "", "initialPrompt", "DairumAppWithLock", "appLockManager", "Lcom/dairum/data/security/AppLockManager;", "app_debug"})
public final class MainActivityKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.dairum.presentation.BottomNavItemData> bottomNavItems = null;
    
    @androidx.compose.runtime.Composable()
    public static final void DairumAppWithLock(@org.jetbrains.annotations.NotNull()
    com.dairum.data.security.AppLockManager appLockManager, @org.jetbrains.annotations.Nullable()
    java.lang.String initialDestination, @org.jetbrains.annotations.Nullable()
    java.lang.String initialPrompt) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void DairumApp(@org.jetbrains.annotations.Nullable()
    java.lang.String initialDestination, @org.jetbrains.annotations.Nullable()
    java.lang.String initialPrompt) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.dairum.presentation.BottomNavItemData> getBottomNavItems() {
        return null;
    }
}