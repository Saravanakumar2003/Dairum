package com.dairum.presentation.screens.entry;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import com.dairum.R;
import com.dairum.domain.model.*;
import com.dairum.presentation.components.*;
import com.dairum.presentation.screens.editor.TextFormatting;
import com.dairum.presentation.theme.EntryBackgroundColors;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000~\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u0010\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0003\u001a\u0010\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0003\u001a<\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0007\u001aH\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00032\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u0006\u0010\u0015\u001a\u00020\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00010\u000eH\u0003\u001a\u0010\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u001bH\u0003\u001a\u0016\u0010\u001c\u001a\u00020\u00012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H\u0003\u001a\u0010\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0003\u001a\u0010\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0003\u001a\u0016\u0010\u001f\u001a\u00020\u00012\f\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u0014H\u0003\u001a\u0016\u0010\"\u001a\u00020\u00012\f\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u0014H\u0003\u001a\u0016\u0010%\u001a\u00020\u00012\f\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u0014H\u0003\u001a\u0016\u0010&\u001a\u00020\u00012\f\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u0014H\u0003\u001a\u0018\u0010\'\u001a\u00020\u00012\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0003\u001a*\u0010,\u001a\u00020\u00012\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020+0.2\f\u0010/\u001a\b\u0012\u0004\u0012\u00020)0\u0014H\u0003\u001a\u0010\u00100\u001a\u00020\u00012\u0006\u00101\u001a\u00020\u0003H\u0003\u001a \u00102\u001a\u00020\u00012\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u00020+2\u0006\u0010*\u001a\u00020+H\u0003\u001a\u001c\u00106\u001a\u00020\u00012\b\u00107\u001a\u0004\u0018\u0001082\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0003\u001a\u0010\u00109\u001a\u00020+2\u0006\u0010:\u001a\u00020\nH\u0002\u001a\u0010\u0010;\u001a\u00020+2\u0006\u0010<\u001a\u00020\nH\u0002\u00a8\u0006="}, d2 = {"AttachmentCard", "", "attachment", "Lcom/dairum/domain/model/Media;", "EntryFooter", "entry", "Lcom/dairum/domain/model/JournalEntry;", "EntryHeader", "EntryViewScreen", "entryId", "", "onBackClick", "Lkotlin/Function0;", "onEditClick", "Lkotlin/Function1;", "viewModel", "Lcom/dairum/presentation/screens/entry/EntryViewViewModel;", "FullscreenMediaViewer", "media", "allPhotos", "", "initialIndex", "", "onDismiss", "onIndexChange", "LocationSection", "location", "Lcom/dairum/domain/model/Location;", "MediaGallery", "ModernEntryFooter", "ModernEntryHeader", "ModernPeopleSection", "people", "Lcom/dairum/domain/model/Person;", "ModernTagsSection", "tags", "Lcom/dairum/domain/model/Tag;", "PeopleSection", "TagsSection", "TrackerValueItem", "tracker", "Lcom/dairum/domain/model/CustomTracker;", "value", "", "TrackersSection", "trackerValues", "", "trackers", "VoiceNoteCard", "voiceNote", "WeatherDetailItem", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "label", "WeatherLocationSection", "weather", "Lcom/dairum/domain/model/Weather;", "formatDuration", "milliseconds", "formatFileSize", "bytes", "app_debug"})
public final class EntryViewScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    public static final void EntryViewScreen(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEditClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.entry.EntryViewViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EntryHeader(com.dairum.domain.model.JournalEntry entry) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MediaGallery(java.util.List<com.dairum.domain.model.Media> media) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VoiceNoteCard(com.dairum.domain.model.Media voiceNote) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AttachmentCard(com.dairum.domain.model.Media attachment) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TagsSection(java.util.List<com.dairum.domain.model.Tag> tags) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PeopleSection(java.util.List<com.dairum.domain.model.Person> people) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TrackersSection(java.util.Map<java.lang.Long, java.lang.String> trackerValues, java.util.List<com.dairum.domain.model.CustomTracker> trackers) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TrackerValueItem(com.dairum.domain.model.CustomTracker tracker, java.lang.String value) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void WeatherLocationSection(com.dairum.domain.model.Weather weather, com.dairum.domain.model.Location location) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void WeatherDetailItem(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String label, java.lang.String value) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationSection(com.dairum.domain.model.Location location) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EntryFooter(com.dairum.domain.model.JournalEntry entry) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModernEntryHeader(com.dairum.domain.model.JournalEntry entry) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void ModernTagsSection(java.util.List<com.dairum.domain.model.Tag> tags) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void ModernPeopleSection(java.util.List<com.dairum.domain.model.Person> people) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModernEntryFooter(com.dairum.domain.model.JournalEntry entry) {
    }
    
    private static final java.lang.String formatDuration(long milliseconds) {
        return null;
    }
    
    private static final java.lang.String formatFileSize(long bytes) {
        return null;
    }
    
    /**
     * Fullscreen media viewer with swipe navigation
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void FullscreenMediaViewer(com.dairum.domain.model.Media media, java.util.List<com.dairum.domain.model.Media> allPhotos, int initialIndex, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onIndexChange) {
    }
}