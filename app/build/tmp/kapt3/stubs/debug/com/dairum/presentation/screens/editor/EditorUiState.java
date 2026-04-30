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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b[\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u00c5\u0003\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u0012\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0011\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0011\u0012\u0014\b\u0002\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00030\u0018\u0012\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0011\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u001d\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u001f\u0012\n\b\u0002\u0010 \u001a\u0004\u0018\u00010!\u0012\u000e\b\u0002\u0010\"\u001a\b\u0012\u0004\u0012\u00020\t0\u0011\u0012\u000e\b\u0002\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00140\u0011\u0012\u000e\b\u0002\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00160\u0011\u0012\u000e\b\u0002\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0011\u0012\u000e\b\u0002\u0010&\u001a\b\u0012\u0004\u0012\u00020\'0\u0011\u0012\b\b\u0002\u0010(\u001a\u00020\u001f\u0012\b\b\u0002\u0010)\u001a\u00020\u001f\u0012\b\b\u0002\u0010*\u001a\u00020\u001f\u0012\u000e\b\u0002\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011\u0012\b\b\u0002\u0010,\u001a\u00020\u001f\u0012\b\b\u0002\u0010-\u001a\u00020\u0019\u0012\b\b\u0002\u0010.\u001a\u00020\u001f\u0012\b\b\u0002\u0010/\u001a\u00020\u001f\u0012\b\b\u0002\u00100\u001a\u00020\u001f\u0012\b\b\u0002\u00101\u001a\u00020\u001f\u0012\b\b\u0002\u00102\u001a\u00020\u001f\u0012\b\b\u0002\u00103\u001a\u00020\u001f\u0012\n\b\u0002\u00104\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u00105\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u00106\u001a\u000207\u0012\n\b\u0002\u00108\u001a\u0004\u0018\u00010\u001d\u00a2\u0006\u0002\u00109J\t\u0010k\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010l\u001a\b\u0012\u0004\u0012\u00020\u00160\u0011H\u00c6\u0003J\u0015\u0010m\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00030\u0018H\u00c6\u0003J\u000f\u0010n\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0011H\u00c6\u0003J\t\u0010o\u001a\u00020\u001dH\u00c6\u0003J\t\u0010p\u001a\u00020\u001fH\u00c6\u0003J\u000b\u0010q\u001a\u0004\u0018\u00010!H\u00c6\u0003J\u000f\u0010r\u001a\b\u0012\u0004\u0012\u00020\t0\u0011H\u00c6\u0003J\u000f\u0010s\u001a\b\u0012\u0004\u0012\u00020\u00140\u0011H\u00c6\u0003J\u000f\u0010t\u001a\b\u0012\u0004\u0012\u00020\u00160\u0011H\u00c6\u0003J\u000f\u0010u\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0011H\u00c6\u0003J\t\u0010v\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010w\u001a\b\u0012\u0004\u0012\u00020\'0\u0011H\u00c6\u0003J\t\u0010x\u001a\u00020\u001fH\u00c6\u0003J\t\u0010y\u001a\u00020\u001fH\u00c6\u0003J\t\u0010z\u001a\u00020\u001fH\u00c6\u0003J\u000f\u0010{\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011H\u00c6\u0003J\t\u0010|\u001a\u00020\u001fH\u00c6\u0003J\t\u0010}\u001a\u00020\u0019H\u00c6\u0003J\t\u0010~\u001a\u00020\u001fH\u00c6\u0003J\t\u0010\u007f\u001a\u00020\u001fH\u00c6\u0003J\n\u0010\u0080\u0001\u001a\u00020\u001fH\u00c6\u0003J\n\u0010\u0081\u0001\u001a\u00020\u0007H\u00c6\u0003J\n\u0010\u0082\u0001\u001a\u00020\u001fH\u00c6\u0003J\n\u0010\u0083\u0001\u001a\u00020\u001fH\u00c6\u0003J\n\u0010\u0084\u0001\u001a\u00020\u001fH\u00c6\u0003J\f\u0010\u0085\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\f\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\n\u0010\u0087\u0001\u001a\u000207H\u00c6\u0003J\f\u0010\u0088\u0001\u001a\u0004\u0018\u00010\u001dH\u00c6\u0003J\f\u0010\u0089\u0001\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\f\u0010\u008a\u0001\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\f\u0010\u008b\u0001\u001a\u0004\u0018\u00010\rH\u00c6\u0003J\f\u0010\u008c\u0001\u001a\u0004\u0018\u00010\u000fH\u00c6\u0003J\u0010\u0010\u008d\u0001\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u00c6\u0003J\u0010\u0010\u008e\u0001\u001a\b\u0012\u0004\u0012\u00020\u00140\u0011H\u00c6\u0003J\u00ca\u0003\u0010\u008f\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u00112\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00112\u0014\b\u0002\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00030\u00182\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00112\b\b\u0002\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001f2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010!2\u000e\b\u0002\u0010\"\u001a\b\u0012\u0004\u0012\u00020\t0\u00112\u000e\b\u0002\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00140\u00112\u000e\b\u0002\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00160\u00112\u000e\b\u0002\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00112\u000e\b\u0002\u0010&\u001a\b\u0012\u0004\u0012\u00020\'0\u00112\b\b\u0002\u0010(\u001a\u00020\u001f2\b\b\u0002\u0010)\u001a\u00020\u001f2\b\b\u0002\u0010*\u001a\u00020\u001f2\u000e\b\u0002\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00112\b\b\u0002\u0010,\u001a\u00020\u001f2\b\b\u0002\u0010-\u001a\u00020\u00192\b\b\u0002\u0010.\u001a\u00020\u001f2\b\b\u0002\u0010/\u001a\u00020\u001f2\b\b\u0002\u00100\u001a\u00020\u001f2\b\b\u0002\u00101\u001a\u00020\u001f2\b\b\u0002\u00102\u001a\u00020\u001f2\b\b\u0002\u00103\u001a\u00020\u001f2\n\b\u0002\u00104\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u00105\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u00106\u001a\u0002072\n\b\u0002\u00108\u001a\u0004\u0018\u00010\u001dH\u00c6\u0001J\u0015\u0010\u0090\u0001\u001a\u00020\u001f2\t\u0010\u0091\u0001\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u000b\u0010\u0092\u0001\u001a\u00030\u0093\u0001H\u00d6\u0001J\n\u0010\u0094\u0001\u001a\u00020\u0003H\u00d6\u0001R\u0011\u00106\u001a\u000207\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010;R\u0011\u0010<\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b=\u0010>R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020\t0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010@R\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00160\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010@R\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00140\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010@R\u0013\u0010 \u001a\u0004\u0018\u00010!\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010DR\u0011\u0010E\u001a\u00020\u001f8F\u00a2\u0006\u0006\u001a\u0004\bF\u0010GR\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010IR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u0010KR\u0011\u0010\u001c\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u0010MR\u0013\u00104\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u0010>R\u0011\u0010.\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\bO\u0010GR\u0011\u0010\u001e\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010GR\u0011\u0010)\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010GR\u0011\u0010(\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010GR\u0011\u0010,\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010GR\u0011\u0010/\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010GR\u0013\u00108\u001a\u0004\u0018\u00010\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\bP\u0010MR\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bQ\u0010RR\u0017\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bS\u0010@R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bT\u0010@R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\bU\u0010VR\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bW\u0010@R\u0011\u0010-\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\bX\u0010YR\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bZ\u0010@R\u0011\u00101\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b[\u0010GR\u0011\u00103\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b\\\u0010GR\u0011\u00102\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b]\u0010GR\u0011\u0010*\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b^\u0010GR\u0011\u00100\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b_\u0010GR\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b`\u0010@R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\'0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\ba\u0010@R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\bb\u0010cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bd\u0010>R\u001d\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00030\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\be\u0010fR\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bg\u0010@R\u0013\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\bh\u0010iR\u0013\u00105\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bj\u0010>\u00a8\u0006\u0095\u0001"}, d2 = {"Lcom/dairum/presentation/screens/editor/EditorUiState;", "", "title", "", "content", "Landroidx/compose/ui/text/input/TextFieldValue;", "textFormatting", "Lcom/dairum/presentation/screens/editor/TextFormatting;", "category", "Lcom/dairum/domain/model/Category;", "mood", "Lcom/dairum/domain/model/Mood;", "weather", "Lcom/dairum/domain/model/Weather;", "location", "Lcom/dairum/domain/model/Location;", "media", "", "Lcom/dairum/presentation/screens/editor/MediaItem;", "tags", "Lcom/dairum/domain/model/Tag;", "people", "Lcom/dairum/domain/model/Person;", "trackerValues", "", "", "selectedTrackers", "Lcom/dairum/domain/model/CustomTracker;", "createdAt", "Ljava/time/LocalDateTime;", "isDraft", "", "backgroundColor", "Lcom/dairum/presentation/theme/EntryBackgroundColor;", "availableCategories", "availableTags", "availablePeople", "trackers", "templates", "Lcom/dairum/domain/model/EntryTemplate;", "isLoadingWeather", "isLoadingLocation", "showLocationPicker", "locationSearchResults", "isRecording", "recordingDuration", "hasChanges", "isSaved", "showTemplateDialog", "showBackgroundColorPicker", "showHighlightPicker", "showFontPicker", "error", "weatherError", "autosaveStatus", "Lcom/dairum/presentation/screens/editor/AutosaveStatus;", "lastAutosaveTime", "(Ljava/lang/String;Landroidx/compose/ui/text/input/TextFieldValue;Lcom/dairum/presentation/screens/editor/TextFormatting;Lcom/dairum/domain/model/Category;Lcom/dairum/domain/model/Mood;Lcom/dairum/domain/model/Weather;Lcom/dairum/domain/model/Location;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/Map;Ljava/util/List;Ljava/time/LocalDateTime;ZLcom/dairum/presentation/theme/EntryBackgroundColor;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;ZZZLjava/util/List;ZJZZZZZZLjava/lang/String;Ljava/lang/String;Lcom/dairum/presentation/screens/editor/AutosaveStatus;Ljava/time/LocalDateTime;)V", "getAutosaveStatus", "()Lcom/dairum/presentation/screens/editor/AutosaveStatus;", "autosaveStatusText", "getAutosaveStatusText", "()Ljava/lang/String;", "getAvailableCategories", "()Ljava/util/List;", "getAvailablePeople", "getAvailableTags", "getBackgroundColor", "()Lcom/dairum/presentation/theme/EntryBackgroundColor;", "canSave", "getCanSave", "()Z", "getCategory", "()Lcom/dairum/domain/model/Category;", "getContent", "()Landroidx/compose/ui/text/input/TextFieldValue;", "getCreatedAt", "()Ljava/time/LocalDateTime;", "getError", "getHasChanges", "getLastAutosaveTime", "getLocation", "()Lcom/dairum/domain/model/Location;", "getLocationSearchResults", "getMedia", "getMood", "()Lcom/dairum/domain/model/Mood;", "getPeople", "getRecordingDuration", "()J", "getSelectedTrackers", "getShowBackgroundColorPicker", "getShowFontPicker", "getShowHighlightPicker", "getShowLocationPicker", "getShowTemplateDialog", "getTags", "getTemplates", "getTextFormatting", "()Lcom/dairum/presentation/screens/editor/TextFormatting;", "getTitle", "getTrackerValues", "()Ljava/util/Map;", "getTrackers", "getWeather", "()Lcom/dairum/domain/model/Weather;", "getWeatherError", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class EditorUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.text.input.TextFieldValue content = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.presentation.screens.editor.TextFormatting textFormatting = null;
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
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.presentation.theme.EntryBackgroundColor backgroundColor = null;
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
    private final boolean showBackgroundColorPicker = false;
    private final boolean showHighlightPicker = false;
    private final boolean showFontPicker = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String weatherError = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.presentation.screens.editor.AutosaveStatus autosaveStatus = null;
    @org.jetbrains.annotations.Nullable()
    private final java.time.LocalDateTime lastAutosaveTime = null;
    
    public EditorUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.text.input.TextFieldValue content, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.editor.TextFormatting textFormatting, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood mood, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Weather weather, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.presentation.screens.editor.MediaItem> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> trackerValues, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.CustomTracker> selectedTrackers, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, boolean isDraft, @org.jetbrains.annotations.Nullable()
    com.dairum.presentation.theme.EntryBackgroundColor backgroundColor, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Category> availableCategories, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> availableTags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> availablePeople, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.CustomTracker> trackers, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.EntryTemplate> templates, boolean isLoadingWeather, boolean isLoadingLocation, boolean showLocationPicker, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Location> locationSearchResults, boolean isRecording, long recordingDuration, boolean hasChanges, boolean isSaved, boolean showTemplateDialog, boolean showBackgroundColorPicker, boolean showHighlightPicker, boolean showFontPicker, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String weatherError, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.editor.AutosaveStatus autosaveStatus, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime lastAutosaveTime) {
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
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.editor.TextFormatting getTextFormatting() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.presentation.theme.EntryBackgroundColor getBackgroundColor() {
        return null;
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
    
    public final boolean getShowBackgroundColorPicker() {
        return false;
    }
    
    public final boolean getShowHighlightPicker() {
        return false;
    }
    
    public final boolean getShowFontPicker() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.editor.AutosaveStatus getAutosaveStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime getLastAutosaveTime() {
        return null;
    }
    
    public final boolean getCanSave() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAutosaveStatusText() {
        return null;
    }
    
    public EditorUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Long, java.lang.String> component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.CustomTracker> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component13() {
        return null;
    }
    
    public final boolean component14() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.presentation.theme.EntryBackgroundColor component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Category> component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.CustomTracker> component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.text.input.TextFieldValue component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.EntryTemplate> component20() {
        return null;
    }
    
    public final boolean component21() {
        return false;
    }
    
    public final boolean component22() {
        return false;
    }
    
    public final boolean component23() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Location> component24() {
        return null;
    }
    
    public final boolean component25() {
        return false;
    }
    
    public final long component26() {
        return 0L;
    }
    
    public final boolean component27() {
        return false;
    }
    
    public final boolean component28() {
        return false;
    }
    
    public final boolean component29() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.editor.TextFormatting component3() {
        return null;
    }
    
    public final boolean component30() {
        return false;
    }
    
    public final boolean component31() {
        return false;
    }
    
    public final boolean component32() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component33() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component34() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.editor.AutosaveStatus component35() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime component36() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Category component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Mood component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Weather component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Location component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.presentation.screens.editor.MediaItem> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.editor.EditorUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.text.input.TextFieldValue content, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.editor.TextFormatting textFormatting, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood mood, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Weather weather, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.presentation.screens.editor.MediaItem> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> trackerValues, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.CustomTracker> selectedTrackers, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, boolean isDraft, @org.jetbrains.annotations.Nullable()
    com.dairum.presentation.theme.EntryBackgroundColor backgroundColor, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Category> availableCategories, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> availableTags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> availablePeople, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.CustomTracker> trackers, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.EntryTemplate> templates, boolean isLoadingWeather, boolean isLoadingLocation, boolean showLocationPicker, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Location> locationSearchResults, boolean isRecording, long recordingDuration, boolean hasChanges, boolean isSaved, boolean showTemplateDialog, boolean showBackgroundColorPicker, boolean showHighlightPicker, boolean showFontPicker, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.String weatherError, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.editor.AutosaveStatus autosaveStatus, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime lastAutosaveTime) {
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