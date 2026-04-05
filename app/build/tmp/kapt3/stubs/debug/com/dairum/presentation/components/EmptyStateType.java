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

/**
 * Sealed class representing different empty state types
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001cB+\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\bR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u0082\u0001\u000e\u001d\u001e\u001f !\"#$%&\'()*\u00a8\u0006+"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType;", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "title", "", "message", "actionLabel", "(Landroidx/compose/ui/graphics/vector/ImageVector;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getActionLabel", "()Ljava/lang/String;", "getIcon", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "getMessage", "getTitle", "Custom", "NoArchivedEntries", "NoDrafts", "NoEntries", "NoExports", "NoFavorites", "NoMediaFiles", "NoMemories", "NoNotifications", "NoPeople", "NoSearchResults", "NoStats", "NoTags", "NoTrackers", "Lcom/dairum/presentation/components/EmptyStateType$Custom;", "Lcom/dairum/presentation/components/EmptyStateType$NoArchivedEntries;", "Lcom/dairum/presentation/components/EmptyStateType$NoDrafts;", "Lcom/dairum/presentation/components/EmptyStateType$NoEntries;", "Lcom/dairum/presentation/components/EmptyStateType$NoExports;", "Lcom/dairum/presentation/components/EmptyStateType$NoFavorites;", "Lcom/dairum/presentation/components/EmptyStateType$NoMediaFiles;", "Lcom/dairum/presentation/components/EmptyStateType$NoMemories;", "Lcom/dairum/presentation/components/EmptyStateType$NoNotifications;", "Lcom/dairum/presentation/components/EmptyStateType$NoPeople;", "Lcom/dairum/presentation/components/EmptyStateType$NoSearchResults;", "Lcom/dairum/presentation/components/EmptyStateType$NoStats;", "Lcom/dairum/presentation/components/EmptyStateType$NoTags;", "Lcom/dairum/presentation/components/EmptyStateType$NoTrackers;", "app_debug"})
public abstract class EmptyStateType {
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector icon = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String message = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String actionLabel = null;
    
    private EmptyStateType(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String message, java.lang.String actionLabel) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getIcon() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getActionLabel() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\bJ\t\u0010\t\u001a\u00020\u0003H\u00c2\u0003J\t\u0010\n\u001a\u00020\u0005H\u00c2\u0003J\t\u0010\u000b\u001a\u00020\u0005H\u00c2\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u00c2\u0003J3\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0005H\u00d6\u0001R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$Custom;", "Lcom/dairum/presentation/components/EmptyStateType;", "customIcon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "customTitle", "", "customMessage", "customActionLabel", "(Landroidx/compose/ui/graphics/vector/ImageVector;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class Custom extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        private final androidx.compose.ui.graphics.vector.ImageVector customIcon = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String customTitle = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String customMessage = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String customActionLabel = null;
        
        public Custom(@org.jetbrains.annotations.NotNull()
        androidx.compose.ui.graphics.vector.ImageVector customIcon, @org.jetbrains.annotations.NotNull()
        java.lang.String customTitle, @org.jetbrains.annotations.NotNull()
        java.lang.String customMessage, @org.jetbrains.annotations.Nullable()
        java.lang.String customActionLabel) {
        }
        
        private final androidx.compose.ui.graphics.vector.ImageVector component1() {
            return null;
        }
        
        private final java.lang.String component2() {
            return null;
        }
        
        private final java.lang.String component3() {
            return null;
        }
        
        private final java.lang.String component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.dairum.presentation.components.EmptyStateType.Custom copy(@org.jetbrains.annotations.NotNull()
        androidx.compose.ui.graphics.vector.ImageVector customIcon, @org.jetbrains.annotations.NotNull()
        java.lang.String customTitle, @org.jetbrains.annotations.NotNull()
        java.lang.String customMessage, @org.jetbrains.annotations.Nullable()
        java.lang.String customActionLabel) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoArchivedEntries;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoArchivedEntries extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoArchivedEntries INSTANCE = null;
        
        private NoArchivedEntries() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoDrafts;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoDrafts extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoDrafts INSTANCE = null;
        
        private NoDrafts() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoEntries;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoEntries extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoEntries INSTANCE = null;
        
        private NoEntries() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoExports;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoExports extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoExports INSTANCE = null;
        
        private NoExports() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoFavorites;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoFavorites extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoFavorites INSTANCE = null;
        
        private NoFavorites() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoMediaFiles;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoMediaFiles extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoMediaFiles INSTANCE = null;
        
        private NoMediaFiles() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoMemories;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoMemories extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoMemories INSTANCE = null;
        
        private NoMemories() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoNotifications;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoNotifications extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoNotifications INSTANCE = null;
        
        private NoNotifications() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoPeople;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoPeople extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoPeople INSTANCE = null;
        
        private NoPeople() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoSearchResults;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoSearchResults extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoSearchResults INSTANCE = null;
        
        private NoSearchResults() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoStats;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoStats extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoStats INSTANCE = null;
        
        private NoStats() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoTags;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoTags extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoTags INSTANCE = null;
        
        private NoTags() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/dairum/presentation/components/EmptyStateType$NoTrackers;", "Lcom/dairum/presentation/components/EmptyStateType;", "()V", "app_debug"})
    public static final class NoTrackers extends com.dairum.presentation.components.EmptyStateType {
        @org.jetbrains.annotations.NotNull()
        public static final com.dairum.presentation.components.EmptyStateType.NoTrackers INSTANCE = null;
        
        private NoTrackers() {
        }
    }
}