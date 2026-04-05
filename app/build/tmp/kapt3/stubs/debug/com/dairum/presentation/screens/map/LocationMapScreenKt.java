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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000P\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u001e\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a\u001e\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a2\u0010\f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a4\u0010\u0011\u001a\u00020\u00012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00132\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0003\u001a4\u0010\u0015\u001a\u00020\u00012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0007\u001a \u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001dH\u0003\u00a8\u0006\u001f"}, d2 = {"EmptyLocationState", "", "modifier", "Landroidx/compose/ui/Modifier;", "EntryListItem", "entry", "Lcom/dairum/domain/model/JournalEntry;", "onClick", "Lkotlin/Function0;", "LocationCard", "locationWithEntries", "Lcom/dairum/presentation/screens/map/LocationWithEntries;", "LocationEntriesSheet", "onEntryClick", "Lkotlin/Function1;", "", "onClose", "LocationListView", "locations", "", "onLocationClick", "LocationMapScreen", "onBackClick", "viewModel", "Lcom/dairum/presentation/screens/map/LocationMapViewModel;", "StatItem", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "value", "", "label", "app_debug"})
public final class LocationMapScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void LocationMapScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.map.LocationMapViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatItem(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String value, java.lang.String label) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EmptyLocationState(androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationListView(java.util.List<com.dairum.presentation.screens.map.LocationWithEntries> locations, kotlin.jvm.functions.Function1<? super com.dairum.presentation.screens.map.LocationWithEntries, kotlin.Unit> onLocationClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationCard(com.dairum.presentation.screens.map.LocationWithEntries locationWithEntries, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationEntriesSheet(com.dairum.presentation.screens.map.LocationWithEntries locationWithEntries, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function0<kotlin.Unit> onClose) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EntryListItem(com.dairum.domain.model.JournalEntry entry, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}