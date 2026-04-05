package com.dairum.presentation.screens.editor;

import android.content.Context;
import android.net.Uri;
import androidx.compose.ui.text.input.TextFieldValue;
import androidx.lifecycle.ViewModel;
import com.dairum.data.service.LocationService;
import com.dairum.data.service.VoiceRecorderService;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.*;
import com.dairum.widget.TodayWidgetProvider;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.*;
import org.json.JSONArray;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\bR\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u00fb\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u0012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f\u0012\u0014\b\u0002\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00030\u0016\u0012\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u000f\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u001b\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u001d\u0012\u000e\b\u0002\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f\u0012\u000e\b\u0002\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f\u0012\u000e\b\u0002\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00140\u000f\u0012\u000e\b\u0002\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00190\u000f\u0012\u000e\b\u0002\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u000f\u0012\b\b\u0002\u0010$\u001a\u00020\u001d\u0012\b\b\u0002\u0010%\u001a\u00020\u001d\u0012\b\b\u0002\u0010&\u001a\u00020\u001d\u0012\u000e\b\u0002\u0010\'\u001a\b\u0012\u0004\u0012\u00020\r0\u000f\u0012\b\b\u0002\u0010(\u001a\u00020\u001d\u0012\b\b\u0002\u0010)\u001a\u00020\u0017\u0012\b\b\u0002\u0010*\u001a\u00020\u001d\u0012\b\b\u0002\u0010+\u001a\u00020\u001d\u0012\b\b\u0002\u0010,\u001a\u00020\u001d\u0012\n\b\u0002\u0010-\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010.\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010/J\t\u0010U\u001a\u00020\u0003H\u00c6\u0003J\u0015\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00030\u0016H\u00c6\u0003J\u000f\u0010W\u001a\b\u0012\u0004\u0012\u00020\u00190\u000fH\u00c6\u0003J\t\u0010X\u001a\u00020\u001bH\u00c6\u0003J\t\u0010Y\u001a\u00020\u001dH\u00c6\u0003J\u000f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\u00070\u000fH\u00c6\u0003J\u000f\u0010[\u001a\b\u0012\u0004\u0012\u00020\u00120\u000fH\u00c6\u0003J\u000f\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u00140\u000fH\u00c6\u0003J\u000f\u0010]\u001a\b\u0012\u0004\u0012\u00020\u00190\u000fH\u00c6\u0003J\u000f\u0010^\u001a\b\u0012\u0004\u0012\u00020#0\u000fH\u00c6\u0003J\t\u0010_\u001a\u00020\u001dH\u00c6\u0003J\t\u0010`\u001a\u00020\u0005H\u00c6\u0003J\t\u0010a\u001a\u00020\u001dH\u00c6\u0003J\t\u0010b\u001a\u00020\u001dH\u00c6\u0003J\u000f\u0010c\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH\u00c6\u0003J\t\u0010d\u001a\u00020\u001dH\u00c6\u0003J\t\u0010e\u001a\u00020\u0017H\u00c6\u0003J\t\u0010f\u001a\u00020\u001dH\u00c6\u0003J\t\u0010g\u001a\u00020\u001dH\u00c6\u0003J\t\u0010h\u001a\u00020\u001dH\u00c6\u0003J\u000b\u0010i\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010j\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010k\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010l\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u0010m\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\u000b\u0010n\u001a\u0004\u0018\u00010\rH\u00c6\u0003J\u000f\u0010o\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u00c6\u0003J\u000f\u0010p\u001a\b\u0012\u0004\u0012\u00020\u00120\u000fH\u00c6\u0003J\u000f\u0010q\u001a\b\u0012\u0004\u0012\u00020\u00140\u000fH\u00c6\u0003J\u00ff\u0002\u0010r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f2\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f2\u0014\b\u0002\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00030\u00162\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u000f2\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\b\b\u0002\u0010\u001c\u001a\u00020\u001d2\u000e\b\u0002\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f2\u000e\b\u0002\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f2\u000e\b\u0002\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00140\u000f2\u000e\b\u0002\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00190\u000f2\u000e\b\u0002\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u000f2\b\b\u0002\u0010$\u001a\u00020\u001d2\b\b\u0002\u0010%\u001a\u00020\u001d2\b\b\u0002\u0010&\u001a\u00020\u001d2\u000e\b\u0002\u0010\'\u001a\b\u0012\u0004\u0012\u00020\r0\u000f2\b\b\u0002\u0010(\u001a\u00020\u001d2\b\b\u0002\u0010)\u001a\u00020\u00172\b\b\u0002\u0010*\u001a\u00020\u001d2\b\b\u0002\u0010+\u001a\u00020\u001d2\b\b\u0002\u0010,\u001a\u00020\u001d2\n\b\u0002\u0010-\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010.\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010s\u001a\u00020\u001d2\b\u0010t\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010u\u001a\u00020vH\u00d6\u0001J\t\u0010w\u001a\u00020\u0003H\u00d6\u0001R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00140\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u00101R\u0017\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00101R\u0011\u00104\u001a\u00020\u001d8F\u00a2\u0006\u0006\u001a\u0004\b5\u00106R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010:R\u0011\u0010\u001a\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010<R\u0013\u0010-\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010>R\u0011\u0010*\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u00106R\u0011\u0010\u001c\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u00106R\u0011\u0010%\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u00106R\u0011\u0010$\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u00106R\u0011\u0010(\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u00106R\u0011\u0010+\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u00106R\u0013\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010AR\u0017\u0010\'\u001a\b\u0012\u0004\u0012\u00020\r0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u00101R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u00101R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010ER\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u00101R\u0011\u0010)\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010HR\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u00101R\u0011\u0010&\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u00106R\u0011\u0010,\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u00106R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u00101R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u00101R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u0010>R\u001d\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00030\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\bO\u0010PR\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00190\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bQ\u00101R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\bR\u0010SR\u0013\u0010.\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bT\u0010>\u00a8\u0006x"}, d2 = {"Lcom/dairum/presentation/screens/editor/EditorUiState;", "", "title", "", "content", "Landroidx/compose/ui/text/input/TextFieldValue;", "category", "Lcom/dairum/domain/model/Category;", "mood", "Lcom/dairum/domain/model/Mood;", "weather", "Lcom/dairum/domain/model/Weather;", "location", "Lcom/dairum/domain/model/Location;", "media", "", "Lcom/dairum/presentation/screens/editor/MediaItem;", "tags", "Lcom/dairum/domain/model/Tag;", "people", "Lcom/dairum/domain/model/Person;", "trackerValues", "", "", "selectedTrackers", "Lcom/dairum/domain/model/CustomTracker;", "createdAt", "Ljava/time/LocalDateTime;", "isDraft", "", "availableCategories", "availableTags", "availablePeople", "trackers", "templates", "Lcom/dairum/domain/model/EntryTemplate;", "isLoadingWeather", "isLoadingLocation", "showLocationPicker", "locationSearchResults", "isRecording", "recordingDuration", "hasChanges", "isSaved", "showTemplateDialog", "error", "weatherError", "(Ljava/lang/String;Landroidx/compose/ui/text/input/TextFieldValue;Lcom/dairum/domain/model/Category;Lcom/dairum/domain/model/Mood;Lcom/dairum/domain/model/Weather;Lcom/dairum/domain/model/Location;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/Map;Ljava/util/List;Ljava/time/LocalDateTime;ZLjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;ZZZLjava/util/List;ZJZZZLjava/lang/String;Ljava/lang/String;)V", "getAvailableCategories", "()Ljava/util/List;", "getAvailablePeople", "getAvailableTags", "canSave", "getCanSave", "()Z", "getCategory", "()Lcom/dairum/domain/model/Category;", "getContent", "()Landroidx/compose/ui/text/input/TextFieldValue;", "getCreatedAt", "()Ljava/time/LocalDateTime;", "getError", "()Ljava/lang/String;", "getHasChanges", "getLocation", "()Lcom/dairum/domain/model/Location;", "getLocationSearchResults", "getMedia", "getMood", "()Lcom/dairum/domain/model/Mood;", "getPeople", "getRecordingDuration", "()J", "getSelectedTrackers", "getShowLocationPicker", "getShowTemplateDialog", "getTags", "getTemplates", "getTitle", "getTrackerValues", "()Ljava/util/Map;", "getTrackers", "getWeather", "()Lcom/dairum/domain/model/Weather;", "getWeatherError", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class EditorUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.text.input.TextFieldValue content = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Category category = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Mood mood = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Weather weather = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Location location = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.presentation.screens.editor.MediaItem> media = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Tag> tags = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Person> people = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.Long, java.lang.String> trackerValues = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.CustomTracker> selectedTrackers = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime createdAt = null;
    private final boolean isDraft = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Category> availableCategories = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Tag> availableTags = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Person> availablePeople = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.CustomTracker> trackers = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.EntryTemplate> templates = null;
    private final boolean isLoadingWeather = false;
    private final boolean isLoadingLocation = false;
    private final boolean showLocationPicker = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Location> locationSearchResults = null;
    private final boolean isRecording = false;
    private final long recordingDuration = 0L;
    private final boolean hasChanges = false;
    private final boolean isSaved = false;
    private final boolean showTemplateDialog = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String weatherError = null;
    
    public EditorUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.text.input.TextFieldValue content, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood mood, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Weather weather, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.presentation.screens.editor.MediaItem> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> trackerValues, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.CustomTracker> selectedTrackers, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, boolean isDraft, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Category> availableCategories, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> availableTags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> availablePeople, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.CustomTracker> trackers, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.EntryTemplate> templates, boolean isLoadingWeather, boolean isLoadingLocation, boolean showLocationPicker, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Location> locationSearchResults, boolean isRecording, long recordingDuration, boolean hasChanges, boolean isSaved, boolean showTemplateDialog, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String weatherError) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.text.input.TextFieldValue getContent() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Category getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Mood getMood() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Weather getWeather() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Location getLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.presentation.screens.editor.MediaItem> getMedia() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> getTags() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> getPeople() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Long, java.lang.String> getTrackerValues() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.CustomTracker> getSelectedTrackers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime getCreatedAt() {
        return null;
    }
    
    public final boolean isDraft() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Category> getAvailableCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> getAvailableTags() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> getAvailablePeople() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.CustomTracker> getTrackers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.EntryTemplate> getTemplates() {
        return null;
    }
    
    public final boolean isLoadingWeather() {
        return false;
    }
    
    public final boolean isLoadingLocation() {
        return false;
    }
    
    public final boolean getShowLocationPicker() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Location> getLocationSearchResults() {
        return null;
    }
    
    public final boolean isRecording() {
        return false;
    }
    
    public final long getRecordingDuration() {
        return 0L;
    }
    
    public final boolean getHasChanges() {
        return false;
    }
    
    public final boolean isSaved() {
        return false;
    }
    
    public final boolean getShowTemplateDialog() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getWeatherError() {
        return null;
    }
    
    public final boolean getCanSave() {
        return false;
    }
    
    public EditorUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Long, java.lang.String> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.CustomTracker> component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component12() {
        return null;
    }
    
    public final boolean component13() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Category> component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.CustomTracker> component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.EntryTemplate> component18() {
        return null;
    }
    
    public final boolean component19() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.text.input.TextFieldValue component2() {
        return null;
    }
    
    public final boolean component20() {
        return false;
    }
    
    public final boolean component21() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Location> component22() {
        return null;
    }
    
    public final boolean component23() {
        return false;
    }
    
    public final long component24() {
        return 0L;
    }
    
    public final boolean component25() {
        return false;
    }
    
    public final boolean component26() {
        return false;
    }
    
    public final boolean component27() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component28() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component29() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Category component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Mood component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Weather component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Location component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.presentation.screens.editor.MediaItem> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.editor.EditorUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.text.input.TextFieldValue content, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood mood, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Weather weather, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.presentation.screens.editor.MediaItem> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> trackerValues, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.CustomTracker> selectedTrackers, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, boolean isDraft, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Category> availableCategories, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> availableTags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> availablePeople, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.CustomTracker> trackers, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.EntryTemplate> templates, boolean isLoadingWeather, boolean isLoadingLocation, boolean showLocationPicker, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Location> locationSearchResults, boolean isRecording, long recordingDuration, boolean hasChanges, boolean isSaved, boolean showTemplateDialog, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String weatherError) {
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