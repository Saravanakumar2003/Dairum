package com.dairum.presentation.screens.editor;

import android.Manifest;
import android.net.Uri;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.animation.core.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.graphics.SolidColor;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.TextFieldValue;
import androidx.compose.ui.text.style.TextOverflow;
import com.dairum.R;
import com.dairum.domain.model.*;
import com.dairum.presentation.components.*;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u00a6\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a4\u0010\u0006\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001aB\u0010\f\u001a\u00020\u00012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\b2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a=\u0010\u0012\u001a\u00020\u00012\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0007\u00a2\u0006\u0002\u0010\u0019\u001a9\u0010\u001a\u001a\u00020\u00012\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u001e2\u0011\u0010\u001f\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b H\u0003\u001a@\u0010!\u001a\u00020\u00012\b\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010$\u001a\u00020%2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a\u0094\u0001\u0010(\u001a\u00020\u00012\b\u0010)\u001a\u0004\u0018\u00010#2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020#0\b2\u0006\u0010$\u001a\u00020%2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020,\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020\u00010\t2\u0018\u0010.\u001a\u0014\u0012\u0004\u0012\u000200\u0012\u0004\u0012\u000200\u0012\u0004\u0012\u00020\u00010/2\f\u00101\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a(\u00103\u001a\u00020\u00012\u0006\u0010\"\u001a\u00020#2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a(\u00104\u001a\u00020\u00012\u0006\u00105\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a(\u00106\u001a\u00020\u00012\u0006\u00107\u001a\u0002082\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a6\u00109\u001a\u00020\u00012\b\u0010:\u001a\u0004\u0018\u0001082\u0014\u0010;\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u000108\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001aZ\u0010<\u001a\u00020\u00012\f\u0010=\u001a\b\u0012\u0004\u0012\u00020>0\b2\f\u0010?\u001a\b\u0012\u0004\u0012\u00020>0\b2\u0012\u0010@\u001a\u000e\u0012\u0004\u0012\u00020>\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010A\u001a\u000e\u0012\u0004\u0012\u00020,\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a(\u0010B\u001a\u00020\u00012\u0006\u0010C\u001a\u00020>2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001aZ\u0010D\u001a\u00020\u00012\f\u0010E\u001a\b\u0012\u0004\u0012\u00020F0\b2\f\u0010G\u001a\b\u0012\u0004\u0012\u00020F0\b2\u0012\u0010H\u001a\u000e\u0012\u0004\u0012\u00020F\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010I\u001a\u000e\u0012\u0004\u0012\u00020,\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a8\u0010J\u001a\u00020\u00012\f\u0010K\u001a\b\u0012\u0004\u0012\u00020L0\b2\u0012\u0010M\u001a\u000e\u0012\u0004\u0012\u00020L\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001aD\u0010N\u001a\u00020\u00012\u0006\u0010O\u001a\u00020P2\u0006\u0010Q\u001a\u00020,2\u0012\u0010R\u001a\u000e\u0012\u0004\u0012\u00020,\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001aF\u0010S\u001a\u00020\u00012\f\u0010T\u001a\b\u0012\u0004\u0012\u00020P0\b2\f\u0010U\u001a\b\u0012\u0004\u0012\u00020P0\b2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020P\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a6\u0010W\u001a\u00020\u00012\u0006\u0010X\u001a\u00020\u00142\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a<\u0010[\u001a\u00020\u00012\b\u0010\\\u001a\u0004\u0018\u00010]2\u0006\u0010$\u001a\u00020%2\b\u0010^\u001a\u0004\u0018\u00010,2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a\u0010\u0010_\u001a\u00020,2\u0006\u0010`\u001a\u00020\u0014H\u0002\u001a\u0010\u0010a\u001a\u00020,2\u0006\u0010b\u001a\u00020\u0014H\u0002\u00a8\u0006c"}, d2 = {"AudioFileItem", "", "audioFile", "Lcom/dairum/presentation/screens/editor/MediaItem;", "onRemove", "Lkotlin/Function0;", "AudioFilesSection", "audioFiles", "", "Lkotlin/Function1;", "modifier", "Landroidx/compose/ui/Modifier;", "CategoryPickerDialog", "categories", "Lcom/dairum/domain/model/Category;", "selectedCategory", "onCategorySelected", "onDismiss", "EditorScreen", "entryId", "", "onBackClick", "onEntrySaved", "viewModel", "Lcom/dairum/presentation/screens/editor/EditorViewModel;", "(Ljava/lang/Long;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lcom/dairum/presentation/screens/editor/EditorViewModel;)V", "FlowRow", "horizontalArrangement", "Landroidx/compose/foundation/layout/Arrangement$Horizontal;", "verticalArrangement", "Landroidx/compose/foundation/layout/Arrangement$Vertical;", "content", "Landroidx/compose/runtime/Composable;", "LocationInfoBox", "location", "Lcom/dairum/domain/model/Location;", "isLoading", "", "onRefresh", "onEdit", "LocationPickerDialog", "currentLocation", "searchResults", "onSearch", "", "onLocationSelected", "onLocationPickedFromMap", "Lkotlin/Function2;", "", "onGetCurrentLocation", "onRemoveLocation", "LocationPreview", "MediaThumbnail", "media", "MoodChip", "mood", "Lcom/dairum/domain/model/Mood;", "MoodPickerDialog", "selectedMood", "onMoodSelected", "PeoplePickerDialog", "availablePeople", "Lcom/dairum/domain/model/Person;", "selectedPeople", "onPersonSelected", "onPersonCreated", "PersonChip", "person", "TagPickerDialog", "availableTags", "Lcom/dairum/domain/model/Tag;", "selectedTags", "onTagSelected", "onTagCreated", "TemplatePickerDialog", "templates", "Lcom/dairum/domain/model/EntryTemplate;", "onTemplateSelected", "TrackerInput", "tracker", "Lcom/dairum/domain/model/CustomTracker;", "value", "onValueChange", "TrackerPickerDialog", "availableTrackers", "selectedTrackers", "onTrackerSelected", "VoiceRecordingCard", "duration", "onCancel", "onStop", "WeatherInfoBox", "weather", "Lcom/dairum/domain/model/Weather;", "error", "formatDuration", "millis", "formatRecordingDuration", "milliseconds", "app_debug"})
public final class EditorScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    public static final void EditorScreen(@org.jetbrains.annotations.Nullable()
    java.lang.Long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onEntrySaved, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.editor.EditorViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MediaThumbnail(com.dairum.presentation.screens.editor.MediaItem media, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PersonChip(com.dairum.domain.model.Person person, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TrackerInput(com.dairum.domain.model.CustomTracker tracker, java.lang.String value, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VoiceRecordingCard(long duration, kotlin.jvm.functions.Function0<kotlin.Unit> onCancel, kotlin.jvm.functions.Function0<kotlin.Unit> onStop, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AudioFilesSection(java.util.List<com.dairum.presentation.screens.editor.MediaItem> audioFiles, kotlin.jvm.functions.Function1<? super com.dairum.presentation.screens.editor.MediaItem, kotlin.Unit> onRemove, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AudioFileItem(com.dairum.presentation.screens.editor.MediaItem audioFile, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove) {
    }
    
    private static final java.lang.String formatRecordingDuration(long milliseconds) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationPreview(com.dairum.domain.model.Location location, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CategoryPickerDialog(java.util.List<com.dairum.domain.model.Category> categories, com.dairum.domain.model.Category selectedCategory, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.Category, kotlin.Unit> onCategorySelected, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TagPickerDialog(java.util.List<com.dairum.domain.model.Tag> availableTags, java.util.List<com.dairum.domain.model.Tag> selectedTags, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.Tag, kotlin.Unit> onTagSelected, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onTagCreated, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TrackerPickerDialog(java.util.List<com.dairum.domain.model.CustomTracker> availableTrackers, java.util.List<com.dairum.domain.model.CustomTracker> selectedTrackers, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.CustomTracker, kotlin.Unit> onTrackerSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationPickerDialog(com.dairum.domain.model.Location currentLocation, java.util.List<com.dairum.domain.model.Location> searchResults, boolean isLoading, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSearch, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.Location, kotlin.Unit> onLocationSelected, kotlin.jvm.functions.Function2<? super java.lang.Double, ? super java.lang.Double, kotlin.Unit> onLocationPickedFromMap, kotlin.jvm.functions.Function0<kotlin.Unit> onGetCurrentLocation, kotlin.jvm.functions.Function0<kotlin.Unit> onRemoveLocation, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void FlowRow(androidx.compose.ui.Modifier modifier, androidx.compose.foundation.layout.Arrangement.Horizontal horizontalArrangement, androidx.compose.foundation.layout.Arrangement.Vertical verticalArrangement, kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    private static final java.lang.String formatDuration(long millis) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationInfoBox(com.dairum.domain.model.Location location, boolean isLoading, kotlin.jvm.functions.Function0<kotlin.Unit> onRefresh, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void WeatherInfoBox(com.dairum.domain.model.Weather weather, boolean isLoading, java.lang.String error, kotlin.jvm.functions.Function0<kotlin.Unit> onRefresh, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PeoplePickerDialog(java.util.List<com.dairum.domain.model.Person> availablePeople, java.util.List<com.dairum.domain.model.Person> selectedPeople, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.Person, kotlin.Unit> onPersonSelected, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPersonCreated, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TemplatePickerDialog(java.util.List<com.dairum.domain.model.EntryTemplate> templates, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.EntryTemplate, kotlin.Unit> onTemplateSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MoodChip(com.dairum.domain.model.Mood mood, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void MoodPickerDialog(com.dairum.domain.model.Mood selectedMood, kotlin.jvm.functions.Function1<? super com.dairum.domain.model.Mood, kotlin.Unit> onMoodSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
}