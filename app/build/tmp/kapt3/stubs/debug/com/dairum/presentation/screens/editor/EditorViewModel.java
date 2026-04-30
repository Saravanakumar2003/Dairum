package com.dairum.presentation.screens.editor;

import android.content.Context;
import android.net.Uri;
import androidx.compose.ui.text.input.TextFieldValue;
import androidx.lifecycle.ViewModel;
import com.dairum.data.service.LocationService;
import com.dairum.data.service.VoiceRecorderService;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.*;
import com.dairum.presentation.theme.EntryBackgroundColor;
import com.dairum.presentation.theme.EntryBackgroundColors;
import com.dairum.widget.TodayWidgetProvider;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.*;
import org.json.JSONArray;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0086\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u0000 \u0099\u00012\u00020\u0001:\u0002\u0099\u0001Ba\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u00a2\u0006\u0002\u0010\u0018J\u0006\u0010*\u001a\u00020+J\u0006\u0010,\u001a\u00020+J\u0016\u0010-\u001a\u00020+2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u000201J\u0006\u00102\u001a\u00020+J\u000e\u00103\u001a\u00020+2\u0006\u00104\u001a\u000205J\u000e\u00106\u001a\u00020+2\u0006\u00107\u001a\u000208J\u000e\u00109\u001a\u00020+2\u0006\u0010:\u001a\u00020;J\u0010\u0010<\u001a\u00020+2\b\u0010=\u001a\u0004\u0018\u00010>J\u000e\u0010?\u001a\u00020+2\u0006\u0010@\u001a\u00020>J\u000e\u0010A\u001a\u00020+2\u0006\u0010B\u001a\u00020CJ\u0010\u0010D\u001a\u00020+2\u0006\u0010E\u001a\u00020>H\u0002J\b\u0010F\u001a\u00020+H\u0002J\u0006\u0010G\u001a\u00020+J\u0006\u0010H\u001a\u00020+J\u000e\u0010I\u001a\u00020+2\u0006\u0010J\u001a\u00020KJ\u000e\u0010L\u001a\u00020+2\u0006\u0010J\u001a\u00020KJ\u0006\u0010M\u001a\u00020+J\u0006\u0010N\u001a\u00020+J\u0018\u0010O\u001a\u00020+2\u0006\u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u00020QH\u0002J\u0006\u0010S\u001a\u00020+J\u0006\u0010T\u001a\u00020+J\u0006\u0010U\u001a\u00020+J\u0006\u0010V\u001a\u00020+J\u0006\u0010W\u001a\u00020+J\b\u0010X\u001a\u00020+H\u0002J\u000e\u0010Y\u001a\u00020+2\u0006\u0010Z\u001a\u00020\u001dJ\b\u0010[\u001a\u00020+H\u0002J\b\u0010\\\u001a\u00020+H\u0002J\b\u0010]\u001a\u00020+H\u0002J\b\u0010^\u001a\u00020+H\u0002J\u000e\u0010_\u001a\u00020+2\u0006\u0010`\u001a\u00020aJ\u0016\u0010b\u001a\b\u0012\u0004\u0012\u00020C0c2\u0006\u0010d\u001a\u00020KH\u0002J\u000e\u0010e\u001a\u00020+H\u0082@\u00a2\u0006\u0002\u0010fJ\u0006\u0010g\u001a\u00020+J\u000e\u0010h\u001a\u00020+2\u0006\u0010i\u001a\u00020jJ\u000e\u0010k\u001a\u00020+2\u0006\u00104\u001a\u000205J\u000e\u0010l\u001a\u00020+2\u0006\u00107\u001a\u000208J\u000e\u0010m\u001a\u00020+2\u0006\u0010:\u001a\u00020;J\u0006\u0010n\u001a\u00020+J\u0006\u0010o\u001a\u00020+J\u0016\u0010p\u001a\u00020+2\u0006\u0010q\u001a\u00020!H\u0082@\u00a2\u0006\u0002\u0010rJ\b\u0010s\u001a\u00020+H\u0002J\u000e\u0010t\u001a\u00020+2\u0006\u0010u\u001a\u00020KJ\u0010\u0010v\u001a\u00020+2\b\u0010w\u001a\u0004\u0018\u00010xJ\u000e\u0010y\u001a\u00020+2\u0006\u0010z\u001a\u00020{J\u0016\u0010|\u001a\u00020+2\u0006\u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u00020QJ\u0006\u0010}\u001a\u00020+J\u0006\u0010~\u001a\u00020+J\u0006\u0010\u007f\u001a\u00020+J\u0007\u0010\u0080\u0001\u001a\u00020+J\u0007\u0010\u0081\u0001\u001a\u00020+J\u0007\u0010\u0082\u0001\u001a\u00020+J\u0007\u0010\u0083\u0001\u001a\u00020+J\u0007\u0010\u0084\u0001\u001a\u00020+J\u0007\u0010\u0085\u0001\u001a\u00020+J\u0007\u0010\u0086\u0001\u001a\u00020+J\u0011\u0010\u0087\u0001\u001a\u00020+2\b\u0010\u0088\u0001\u001a\u00030\u0089\u0001J\u0010\u0010\u008a\u0001\u001a\u00020+2\u0007\u0010\u008b\u0001\u001a\u00020aJ\u0011\u0010\u008c\u0001\u001a\u00020+2\b\u0010\u008d\u0001\u001a\u00030\u008e\u0001J\u0013\u0010\u008f\u0001\u001a\u00020+2\n\u0010\u0090\u0001\u001a\u0005\u0018\u00010\u0091\u0001J\u0007\u0010\u0092\u0001\u001a\u00020+J\u0010\u0010\u0093\u0001\u001a\u00020+2\u0007\u0010\u0094\u0001\u001a\u00020KJ\u000f\u0010\u0095\u0001\u001a\u00020+H\u0082@\u00a2\u0006\u0002\u0010fJ\u0019\u0010\u0096\u0001\u001a\u00020+2\u0007\u0010\u0097\u0001\u001a\u00020\u001d2\u0007\u0010\u0098\u0001\u001a\u00020KR\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010$\u001a\u0004\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010%R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\u001b0\'\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u009a\u0001"}, d2 = {"Lcom/dairum/presentation/screens/editor/EditorViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "categoryRepository", "Lcom/dairum/domain/repository/CategoryRepository;", "tagRepository", "Lcom/dairum/domain/repository/TagRepository;", "personRepository", "Lcom/dairum/domain/repository/PersonRepository;", "trackerRepository", "Lcom/dairum/domain/repository/TrackerRepository;", "mediaRepository", "Lcom/dairum/domain/repository/MediaRepository;", "weatherRepository", "Lcom/dairum/domain/repository/WeatherRepository;", "settingsRepository", "Lcom/dairum/domain/repository/SettingsRepository;", "locationService", "Lcom/dairum/data/service/LocationService;", "voiceRecorderService", "Lcom/dairum/data/service/VoiceRecorderService;", "(Landroid/content/Context;Lcom/dairum/domain/repository/JournalRepository;Lcom/dairum/domain/repository/CategoryRepository;Lcom/dairum/domain/repository/TagRepository;Lcom/dairum/domain/repository/PersonRepository;Lcom/dairum/domain/repository/TrackerRepository;Lcom/dairum/domain/repository/MediaRepository;Lcom/dairum/domain/repository/WeatherRepository;Lcom/dairum/domain/repository/SettingsRepository;Lcom/dairum/data/service/LocationService;Lcom/dairum/data/service/VoiceRecorderService;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/editor/EditorUiState;", "autosaveIntervalMs", "", "autosaveJob", "Lkotlinx/coroutines/Job;", "isNewEntry", "", "originalEntry", "Lcom/dairum/domain/model/JournalEntry;", "tempEntryId", "Ljava/lang/Long;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addBulletList", "", "addHeading", "addMedia", "uri", "Landroid/net/Uri;", "type", "Lcom/dairum/domain/model/MediaType;", "addNumberedList", "addPerson", "person", "Lcom/dairum/domain/model/Person;", "addTag", "tag", "Lcom/dairum/domain/model/Tag;", "addTracker", "tracker", "Lcom/dairum/domain/model/CustomTracker;", "applyFont", "fontStyle", "Lcom/dairum/presentation/screens/editor/SpanStyle;", "applyHighlight", "highlightStyle", "applyTemplate", "template", "Lcom/dairum/domain/model/EntryTemplate;", "applyTextStyle", "style", "autoFetchLocationAndWeather", "cancelVoiceRecording", "clearLocationSearch", "createAndAddPerson", "name", "", "createAndAddTag", "fetchCurrentLocation", "fetchWeather", "fetchWeatherForLocation", "latitude", "", "longitude", "hideBackgroundColorPicker", "hideFontPicker", "hideHighlightPicker", "hideTemplateDialog", "insertTimestamp", "loadCategories", "loadEntry", "entryId", "loadPeople", "loadTags", "loadTemplates", "loadTrackers", "onContentChange", "newContent", "Landroidx/compose/ui/text/input/TextFieldValue;", "parseTemplatesFromJson", "", "json", "performAutosave", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "removeLocation", "removeMedia", "media", "Lcom/dairum/presentation/screens/editor/MediaItem;", "removePerson", "removeTag", "removeTracker", "saveAsDraft", "saveEntry", "saveEntryInternal", "isDraft", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scheduleAutosave", "searchLocations", "query", "setBackgroundColor", "color", "Lcom/dairum/presentation/theme/EntryBackgroundColor;", "setLocation", "location", "Lcom/dairum/domain/model/Location;", "setLocationFromCoordinates", "showBackgroundColorPicker", "showFontPicker", "showHighlightPicker", "showTemplateDialog", "startVoiceRecording", "stopVoiceRecording", "toggleBold", "toggleItalic", "toggleLocationPicker", "toggleUnderline", "updateCategory", "category", "Lcom/dairum/domain/model/Category;", "updateContent", "content", "updateCreatedAt", "dateTime", "Ljava/time/LocalDateTime;", "updateMood", "mood", "Lcom/dairum/domain/model/Mood;", "updateRecordingAmplitude", "updateTitle", "title", "updateTodayWidget", "updateTrackerValue", "trackerId", "value", "Companion", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class EditorViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.CategoryRepository categoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.TagRepository tagRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.PersonRepository personRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.TrackerRepository trackerRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.MediaRepository mediaRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.WeatherRepository weatherRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.SettingsRepository settingsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.service.LocationService locationService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.service.VoiceRecorderService voiceRecorderService = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.editor.EditorUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.editor.EditorUiState> uiState = null;
    @org.jetbrains.annotations.Nullable()
    private com.dairum.domain.model.JournalEntry originalEntry;
    private boolean isNewEntry = true;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job autosaveJob;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Long tempEntryId;
    private final long autosaveIntervalMs = 5000L;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> CUSTOM_TEMPLATES_KEY = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.presentation.screens.editor.EditorViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public EditorViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.CategoryRepository categoryRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TagRepository tagRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.PersonRepository personRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TrackerRepository trackerRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MediaRepository mediaRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.WeatherRepository weatherRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.SettingsRepository settingsRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.data.service.LocationService locationService, @org.jetbrains.annotations.NotNull()
    com.dairum.data.service.VoiceRecorderService voiceRecorderService) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.editor.EditorUiState> getUiState() {
        return null;
    }
    
    private final void autoFetchLocationAndWeather() {
    }
    
    private final void fetchWeatherForLocation(double latitude, double longitude) {
    }
    
    private final void loadCategories() {
    }
    
    private final void loadTags() {
    }
    
    private final void loadPeople() {
    }
    
    private final void loadTrackers() {
    }
    
    private final void loadTemplates() {
    }
    
    private final java.util.List<com.dairum.domain.model.EntryTemplate> parseTemplatesFromJson(java.lang.String json) {
        return null;
    }
    
    public final void loadEntry(long entryId) {
    }
    
    public final void updateTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String title) {
    }
    
    public final void updateContent(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.text.input.TextFieldValue content) {
    }
    
    public final void updateCategory(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Category category) {
    }
    
    public final void updateMood(@org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood mood) {
    }
    
    public final void addTag(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Tag tag) {
    }
    
    public final void removeTag(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Tag tag) {
    }
    
    public final void createAndAddTag(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    public final void addPerson(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Person person) {
    }
    
    public final void createAndAddPerson(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    public final void removePerson(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Person person) {
    }
    
    public final void addMedia(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MediaType type) {
    }
    
    public final void removeMedia(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.editor.MediaItem media) {
    }
    
    public final void updateTrackerValue(long trackerId, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void addTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.CustomTracker tracker) {
    }
    
    public final void removeTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.CustomTracker tracker) {
    }
    
    public final void insertTimestamp() {
    }
    
    public final void fetchWeather() {
    }
    
    public final void fetchCurrentLocation() {
    }
    
    public final void setLocation(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Location location) {
    }
    
    public final void setLocationFromCoordinates(double latitude, double longitude) {
    }
    
    public final void searchLocations(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void clearLocationSearch() {
    }
    
    public final void toggleLocationPicker() {
    }
    
    public final void removeLocation() {
    }
    
    public final void startVoiceRecording() {
    }
    
    public final void stopVoiceRecording() {
    }
    
    public final void cancelVoiceRecording() {
    }
    
    public final void updateRecordingAmplitude() {
    }
    
    public final void toggleBold() {
    }
    
    public final void toggleItalic() {
    }
    
    public final void toggleUnderline() {
    }
    
    public final void applyHighlight(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.editor.SpanStyle highlightStyle) {
    }
    
    public final void applyFont(@org.jetbrains.annotations.Nullable()
    com.dairum.presentation.screens.editor.SpanStyle fontStyle) {
    }
    
    public final void showHighlightPicker() {
    }
    
    public final void hideHighlightPicker() {
    }
    
    public final void showFontPicker() {
    }
    
    public final void hideFontPicker() {
    }
    
    /**
     * Applies a text style (bold, italic, underline, highlight, font) to the selected text.
     */
    private final void applyTextStyle(com.dairum.presentation.screens.editor.SpanStyle style) {
    }
    
    /**
     * Called when text content changes - adjusts formatting spans accordingly
     */
    public final void onContentChange(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.text.input.TextFieldValue newContent) {
    }
    
    public final void addBulletList() {
    }
    
    public final void addNumberedList() {
    }
    
    public final void addHeading() {
    }
    
    public final void saveAsDraft() {
    }
    
    public final void saveEntry() {
    }
    
    private final void scheduleAutosave() {
    }
    
    private final java.lang.Object performAutosave(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void setBackgroundColor(@org.jetbrains.annotations.Nullable()
    com.dairum.presentation.theme.EntryBackgroundColor color) {
    }
    
    public final void showBackgroundColorPicker() {
    }
    
    public final void hideBackgroundColorPicker() {
    }
    
    public final void updateCreatedAt(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime dateTime) {
    }
    
    public final void showTemplateDialog() {
    }
    
    public final void hideTemplateDialog() {
    }
    
    public final void applyTemplate(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.EntryTemplate template) {
    }
    
    private final java.lang.Object saveEntryInternal(boolean isDraft, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object updateTodayWidget(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/dairum/presentation/screens/editor/EditorViewModel$Companion;", "", "()V", "CUSTOM_TEMPLATES_KEY", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}