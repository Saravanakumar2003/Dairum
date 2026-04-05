package com.dairum.presentation.screens.home;

import androidx.compose.foundation.ExperimentalFoundationApi;
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
import com.dairum.R;
import com.dairum.domain.model.Location;
import com.dairum.presentation.components.*;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u00ac\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a@\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a4\u0010\f\u001a\u00020\u00012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00032\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001aX\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b28\u0010\u0013\u001a4\u0012\u0013\u0012\u00110\u0015\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0015\u0012\u0013\u0018\u00010\u0015\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0019\u0012\u0004\u0012\u00020\u00010\u0014H\u0003\u001a9\u0010\u001a\u001a\u00020\u00012\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u001e2\u0011\u0010\u001f\u001a\r\u0012\u0004\u0012\u00020\u00010\b\u00a2\u0006\u0002\b H\u0003\u001a\u00e3\u0001\u0010!\u001a\u00020\u00012\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\n2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00010\b2U\b\u0002\u0010(\u001aO\u0012\u0013\u0012\u00110\u0015\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0015\u0012\u0013\u0018\u00010\u0015\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0019\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\u000b0\u0003\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(*\u0012\u0004\u0012\u00020\u00010)2\b\b\u0002\u0010+\u001a\u00020,H\u0007\u001a:\u0010-\u001a\u00020\u00012\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u0002012\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a4\u00103\u001a\u00020\u00012\f\u00104\u001a\b\u0012\u0004\u0012\u0002050\u00032\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001a\\\u00106\u001a\u00020\u00012\f\u00104\u001a\b\u0012\u0004\u0012\u0002050\u00032\u0012\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001aV\u00108\u001a\u00020\u00012\f\u00104\u001a\b\u0012\u0004\u0012\u0002050\u00032\f\u00109\u001a\b\u0012\u0004\u0012\u00020:0\u00032\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001az\u0010;\u001a\u00020\u00012\u0006\u0010<\u001a\u0002052\u0006\u00100\u001a\u0002012\u0006\u0010=\u001a\u0002012\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010>\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010?\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001a4\u0010@\u001a\u00020\u00012\f\u0010A\u001a\b\u0012\u0004\u0012\u00020B0\u00032\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001a\u0088\u0001\u0010C\u001a\u00020\u00012\f\u00104\u001a\b\u0012\u0004\u0012\u0002050\u00032\f\u0010D\u001a\b\u0012\u0004\u0012\u00020\u000b0E2\u0006\u0010=\u001a\u0002012\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010>\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\u0014\b\u0002\u0010?\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001a\u001a\u0010F\u001a\u00020\u00012\u0006\u0010G\u001a\u00020H2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001a\u0016\u0010I\u001a\u00020H2\f\u00104\u001a\b\u0012\u0004\u0012\u0002050\u0003H\u0002\u001a\u0010\u0010J\u001a\u00020\u00152\u0006\u0010K\u001a\u00020LH\u0002\u001a\u0010\u0010M\u001a\u00020\u00152\u0006\u0010N\u001a\u00020\u000eH\u0002\u00a8\u0006O"}, d2 = {"AddToMemoryDialog", "", "memories", "", "Lcom/dairum/domain/model/Memory;", "selectedCount", "", "onDismiss", "Lkotlin/Function0;", "onMemorySelected", "Lkotlin/Function1;", "", "CategoryView", "categories", "Lcom/dairum/domain/model/Category;", "onCategoryClick", "modifier", "Landroidx/compose/ui/Modifier;", "CreateMemoryFromEntriesDialog", "onCreate", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "title", "description", "FlowRow", "horizontalArrangement", "Landroidx/compose/foundation/layout/Arrangement$Horizontal;", "verticalArrangement", "Landroidx/compose/foundation/layout/Arrangement$Vertical;", "content", "Landroidx/compose/runtime/Composable;", "HomeScreen", "onEntryClick", "onNewEntry", "onTagClick", "onMoodClick", "onStoryClick", "onSearchClick", "onCreateMemoryWithEntries", "Lkotlin/Function3;", "entryIds", "viewModel", "Lcom/dairum/presentation/screens/home/HomeViewModel;", "LocationGroupCard", "group", "Lcom/dairum/presentation/screens/home/LocationGroup;", "isSelected", "", "onClick", "LocationView", "entries", "Lcom/dairum/domain/model/JournalEntry;", "MoodView", "onFavoriteClick", "PeopleView", "people", "Lcom/dairum/domain/model/Person;", "PinnedEntryCard", "entry", "isSelectionMode", "onEntryLongClick", "onPinClick", "TagView", "tags", "Lcom/dairum/domain/model/Tag;", "TimelineView", "selectedIds", "", "WeeklyInsightsCard", "insights", "Lcom/dairum/presentation/screens/home/WeeklyInsights;", "calculateWeeklyInsights", "formatDateHeader", "date", "Ljava/time/LocalDate;", "getCategoryEmoji", "category", "app_debug"})
public final class HomeScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.ExperimentalFoundationApi.class, androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNewEntry, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onCategoryClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onTagClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onMoodClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onStoryClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSearchClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.String, ? super java.util.List<java.lang.Long>, kotlin.Unit> onCreateMemoryWithEntries, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.home.HomeViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void TimelineView(java.util.List<com.dairum.domain.model.JournalEntry> entries, java.util.Set<java.lang.Long> selectedIds, boolean isSelectionMode, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryLongClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onFavoriteClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPinClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void CreateMemoryFromEntriesDialog(int selectedCount, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onCreate) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void AddToMemoryDialog(java.util.List<com.dairum.domain.model.Memory> memories, int selectedCount, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onMemorySelected) {
    }
    
    private static final java.lang.String getCategoryEmoji(com.dairum.domain.model.Category category) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CategoryView(java.util.List<com.dairum.domain.model.Category> categories, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onCategoryClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TagView(java.util.List<com.dairum.domain.model.Tag> tags, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onTagClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void FlowRow(androidx.compose.ui.Modifier modifier, androidx.compose.foundation.layout.Arrangement.Horizontal horizontalArrangement, androidx.compose.foundation.layout.Arrangement.Vertical verticalArrangement, kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void MoodView(java.util.List<com.dairum.domain.model.JournalEntry> entries, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onMoodClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onFavoriteClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PeopleView(java.util.List<com.dairum.domain.model.JournalEntry> entries, java.util.List<com.dairum.domain.model.Person> people, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onFavoriteClick, androidx.compose.ui.Modifier modifier) {
    }
    
    private static final java.lang.String formatDateHeader(java.time.LocalDate date) {
        return null;
    }
    
    private static final com.dairum.presentation.screens.home.WeeklyInsights calculateWeeklyInsights(java.util.List<com.dairum.domain.model.JournalEntry> entries) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void WeeklyInsightsCard(com.dairum.presentation.screens.home.WeeklyInsights insights, androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void PinnedEntryCard(com.dairum.domain.model.JournalEntry entry, boolean isSelected, boolean isSelectionMode, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryLongClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onFavoriteClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPinClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationView(java.util.List<com.dairum.domain.model.JournalEntry> entries, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationGroupCard(com.dairum.presentation.screens.home.LocationGroup group, boolean isSelected, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}