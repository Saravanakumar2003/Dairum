package com.dairum.presentation.components;

import androidx.compose.animation.core.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.style.TextAlign;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a,\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0010\b\u0002\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a\u001a\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a\"\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a,\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000f2\u0010\b\u0002\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"EaseInOutQuad", "Landroidx/compose/animation/core/CubicBezierEasing;", "EmptyState", "", "type", "Lcom/dairum/presentation/components/EmptyStateType;", "onActionClick", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "EmptyStateIllustration", "EmptyStateSimple", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "message", "", "ErrorState", "onRetry", "app_debug"})
public final class EmptyStateComponentsKt {
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.animation.core.CubicBezierEasing EaseInOutQuad = null;
    
    /**
     * Main empty state component with animated illustration
     */
    @androidx.compose.runtime.Composable()
    public static final void EmptyState(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.components.EmptyStateType type, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onActionClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Animated illustration for empty states
     */
    @androidx.compose.runtime.Composable()
    public static final void EmptyStateIllustration(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.components.EmptyStateType type, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Simple empty state with just icon and text
     */
    @androidx.compose.runtime.Composable()
    public static final void EmptyStateSimple(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.graphics.vector.ImageVector icon, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Error state component
     */
    @androidx.compose.runtime.Composable()
    public static final void ErrorState(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRetry, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}