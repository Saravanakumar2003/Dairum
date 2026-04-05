package com.dairum.presentation.screens.files;

import android.net.Uri;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.lazy.grid.GridCells;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import com.dairum.R;
import com.dairum.domain.model.MediaType;
import com.dairum.domain.repository.MediaWithEntryInfo;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.text.SimpleDateFormat;
import java.util.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\\\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a*\u0010\u0006\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\\\u0010\u000b\u001a\u00020\u00012\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a \u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016H\u0003\u001a$\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u001a2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a8\u0010\u001b\u001a\u00020\u00012\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001a0\b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a,\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u00032\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a>\u0010!\u001a\u00020\u00012\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a,\u0010#\u001a\u00020\u00012\u0006\u0010$\u001a\u00020\u00032\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a,\u0010&\u001a\u00020\u00012\u0006\u0010$\u001a\u00020\u00032\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a>\u0010\'\u001a\u00020\u00012\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a*\u0010)\u001a\u00020\u00012\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\u001e\u0010+\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a \u0010-\u001a\u00020\u00012\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u00142\u0006\u00101\u001a\u00020\u0014H\u0002\u001a\u0010\u00102\u001a\u00020\u00142\u0006\u00103\u001a\u000204H\u0002\u001a\u0010\u00105\u001a\u00020\u00142\u0006\u00106\u001a\u00020\nH\u0002\u001a\u0010\u00107\u001a\u00020\u00142\u0006\u00108\u001a\u00020\nH\u0002\u001a\u0010\u00109\u001a\u00020\u00142\u0006\u0010:\u001a\u00020\nH\u0002\u001a\u0010\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020\u0014H\u0002\u00a8\u0006>"}, d2 = {"AttachmentItem", "", "attachment", "Lcom/dairum/domain/repository/MediaWithEntryInfo;", "onGoToEntry", "Lkotlin/Function0;", "AttachmentsList", "attachments", "", "Lkotlin/Function1;", "", "FilesScreen", "onPhotoClick", "onVideoClick", "onEntryClick", "onLocationMapClick", "viewModel", "Lcom/dairum/presentation/screens/files/FilesViewModel;", "LocationAreaHeader", "areaName", "", "placeCount", "", "entryCount", "LocationItem", "locationWithEntries", "Lcom/dairum/presentation/screens/files/LocationWithEntries;", "LocationsList", "locations", "onMapClick", "PhotoViewerDialog", "photo", "onDismiss", "PhotosGrid", "photos", "VideoCard", "video", "onClick", "VideoViewerDialog", "VideosGrid", "videos", "VoiceList", "voiceNotes", "VoiceNoteItem", "voiceNote", "downloadFile", "context", "Landroid/content/Context;", "uri", "fileName", "extractAreaName", "location", "Lcom/dairum/domain/model/Location;", "formatDate", "timestamp", "formatDuration", "millis", "formatFileSize", "bytes", "getFileIcon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "mimeType", "app_debug"})
public final class FilesScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void FilesScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPhotoClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onVideoClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLocationMapClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.files.FilesViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PhotosGrid(java.util.List<com.dairum.domain.repository.MediaWithEntryInfo> photos, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPhotoClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onGoToEntry) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void PhotoViewerDialog(com.dairum.domain.repository.MediaWithEntryInfo photo, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function0<kotlin.Unit> onGoToEntry) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VideosGrid(java.util.List<com.dairum.domain.repository.MediaWithEntryInfo> videos, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onVideoClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onGoToEntry) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VideoCard(com.dairum.domain.repository.MediaWithEntryInfo video, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onGoToEntry) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void VideoViewerDialog(com.dairum.domain.repository.MediaWithEntryInfo video, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function0<kotlin.Unit> onGoToEntry) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VoiceList(java.util.List<com.dairum.domain.repository.MediaWithEntryInfo> voiceNotes, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onGoToEntry) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VoiceNoteItem(com.dairum.domain.repository.MediaWithEntryInfo voiceNote, kotlin.jvm.functions.Function0<kotlin.Unit> onGoToEntry) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AttachmentsList(java.util.List<com.dairum.domain.repository.MediaWithEntryInfo> attachments, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onGoToEntry) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AttachmentItem(com.dairum.domain.repository.MediaWithEntryInfo attachment, kotlin.jvm.functions.Function0<kotlin.Unit> onGoToEntry) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationsList(java.util.List<com.dairum.presentation.screens.files.LocationWithEntries> locations, kotlin.jvm.functions.Function0<kotlin.Unit> onMapClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onGoToEntry) {
    }
    
    /**
     * Extract area name from location (city or region)
     */
    private static final java.lang.String extractAreaName(com.dairum.domain.model.Location location) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationAreaHeader(java.lang.String areaName, int placeCount, int entryCount) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationItem(com.dairum.presentation.screens.files.LocationWithEntries locationWithEntries, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onGoToEntry) {
    }
    
    private static final java.lang.String formatDuration(long millis) {
        return null;
    }
    
    private static final java.lang.String formatDate(long timestamp) {
        return null;
    }
    
    private static final java.lang.String formatFileSize(long bytes) {
        return null;
    }
    
    private static final androidx.compose.ui.graphics.vector.ImageVector getFileIcon(java.lang.String mimeType) {
        return null;
    }
    
    private static final void downloadFile(android.content.Context context, java.lang.String uri, java.lang.String fileName) {
    }
}