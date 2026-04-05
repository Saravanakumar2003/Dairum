package com.dairum.presentation.screens.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.Location;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import java.time.format.DateTimeFormatter;
import android.graphics.Color;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J#\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0016"}, d2 = {"Lcom/dairum/presentation/screens/map/LocationWithEntries;", "", "location", "Lcom/dairum/domain/model/Location;", "entries", "", "Lcom/dairum/domain/model/JournalEntry;", "(Lcom/dairum/domain/model/Location;Ljava/util/List;)V", "getEntries", "()Ljava/util/List;", "getLocation", "()Lcom/dairum/domain/model/Location;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class LocationWithEntries {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.Location location = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.JournalEntry> entries = null;
    
    public LocationWithEntries(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.Location getLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> getEntries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.Location component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.map.LocationWithEntries copy(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries) {
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