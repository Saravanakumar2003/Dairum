package com.dairum.presentation.screens.memories;

import androidx.compose.animation.*;
import androidx.compose.foundation.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.lazy.*;
import androidx.compose.foundation.lazy.grid.*;
import androidx.compose.foundation.pager.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import coil.request.ImageRequest;
import com.dairum.domain.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u00c8\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u0007\n\u0000\u001a>\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\u0018\u0010\u0007\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u0003\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a8\u0010\n\u001a\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062 \u0010\u000b\u001a\u001c\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\fH\u0003\u001a:\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\u001a\u0010\u0012\u001a\u0016\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0004\u0012\u00020\u00010\u0013H\u0003\u001a \u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\rH\u0003\u001a\u0080\u0001\u0010\u0019\u001a\u00020\u00012\u0018\u0010\u001a\u001a\u0014\u0012\u0004\u0012\u00020\u001c\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u001b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\u0018\u0010\u001f\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u0003\u0012\u0004\u0012\u00020\u00010\b2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0003\u001a\u001e\u0010!\u001a\u00020\u00012\u0006\u0010\"\u001a\u00020\u00042\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0003\u001a/\u0010$\u001a\u00020\u00012\b\b\u0002\u0010%\u001a\u00020&2\b\b\u0002\u0010\'\u001a\u00020(2\u0011\u0010)\u001a\r\u0012\u0004\u0012\u00020\u00010\u0006\u00a2\u0006\u0002\b*H\u0007\u001a*\u0010+\u001a\u00020\u00012\f\u0010,\u001a\b\u0012\u0004\u0012\u00020-0\u00032\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a\u0016\u0010/\u001a\u00020\u00012\f\u00100\u001a\b\u0012\u0004\u0012\u00020\r0\u0003H\u0003\u001a6\u00101\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u00162\u001c\u0010)\u001a\u0018\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020\u00010\b\u00a2\u0006\u0002\b*\u00a2\u0006\u0002\b3H\u0003\u001a\u0018\u00104\u001a\u00020\u00012\u0006\u00105\u001a\u00020\r2\u0006\u00106\u001a\u00020\rH\u0003\u001a6\u00107\u001a\u00020\u00012\b\u00108\u001a\u0004\u0018\u0001092\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010:\u001a\b\u0012\u0004\u0012\u00020;0\u00032\f\u0010<\u001a\b\u0012\u0004\u0012\u00020=0\u0003H\u0003\u001a\u0016\u0010>\u001a\u00020\u00012\f\u0010?\u001a\b\u0012\u0004\u0012\u00020@0\u0003H\u0003\u001aR\u0010A\u001a\u00020\u00012\u0006\u0010B\u001a\u00020\t2\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\u0014\b\u0002\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010D\u001a\u00020EH\u0007\u001a\u0010\u0010F\u001a\u00020\u00012\u0006\u00108\u001a\u000209H\u0003\u001az\u0010G\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010H\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\u00108\u001a\u0004\u0018\u0001092\f\u0010I\u001a\b\u0012\u0004\u0012\u00020J0\u00032\f\u0010?\u001a\b\u0012\u0004\u0012\u00020@0\u00032\f\u0010,\u001a\b\u0012\u0004\u0012\u00020-0\u00032\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a\u0016\u0010K\u001a\u00020\u00012\f\u0010I\u001a\b\u0012\u0004\u0012\u00020J0\u0003H\u0003\u001a2\u0010L\u001a\u00020\u00012\f\u0010,\u001a\b\u0012\u0004\u0012\u00020-0\u00032\u0006\u0010M\u001a\u00020N2\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a \u0010O\u001a\u00020\u00012\u0006\u00106\u001a\u00020\r2\u0006\u00105\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0016H\u0003\u001a\u001a\u0010P\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\b\u00108\u001a\u0004\u0018\u000109H\u0003\u001a,\u0010Q\u001a\u00020\u00012\u0006\u0010\"\u001a\u00020\u00042\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010R\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0003\u001a,\u0010S\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010U\u001a\u00020V2\b\b\u0002\u0010W\u001a\u00020VH\u0003\u001ab\u0010X\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010H\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020Z0\u00032\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\f\u0010[\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\u0012\u0010\\\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a\u0018\u0010]\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u00108\u001a\u000209H\u0002\u001a\u0018\u0010^\u001a\u00020\r2\u0006\u0010_\u001a\u00020\u000e2\u0006\u0010`\u001a\u00020\u000eH\u0002\u001a\u0010\u0010a\u001a\u00020\r2\u0006\u0010b\u001a\u00020NH\u0002\u001a\u0010\u0010c\u001a\u00020\r2\u0006\u0010d\u001a\u00020NH\u0002\u001a\u0010\u0010e\u001a\u00020\r2\u0006\u0010f\u001a\u00020NH\u0002\u001a\u0010\u0010g\u001a\u00020\r2\u0006\u0010h\u001a\u00020iH\u0002\u00a8\u0006j"}, d2 = {"AddEntriesDialog", "", "availableEntries", "", "Lcom/dairum/domain/model/JournalEntry;", "onDismiss", "Lkotlin/Function0;", "onConfirm", "Lkotlin/Function1;", "", "AddMilestoneDialog", "onAdd", "Lkotlin/Function3;", "", "Ljava/time/LocalDateTime;", "EditMemoryDialog", "memory", "Lcom/dairum/domain/model/Memory;", "onSave", "Lkotlin/Function2;", "EmptyStateMessage", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "title", "message", "EntriesTab", "entriesByDate", "", "Ljava/time/LocalDate;", "onEntryClick", "onRemoveEntry", "onAddEntries", "onLoadAvailableEntries", "EntryPreviewCard", "entry", "onClick", "FlowRow", "horizontalArrangement", "Landroidx/compose/foundation/layout/Arrangement$Horizontal;", "verticalArrangement", "Landroidx/compose/foundation/layout/Arrangement$Vertical;", "content", "Landroidx/compose/runtime/Composable;", "GalleryTab", "photos", "Lcom/dairum/domain/model/Media;", "onPhotoClick", "HighlightsCard", "highlights", "InsightCard", "Landroidx/compose/foundation/layout/ColumnScope;", "Lkotlin/ExtensionFunctionType;", "InsightRow", "label", "value", "InsightsTab", "insights", "Lcom/dairum/presentation/screens/memories/MemoryInsights;", "moodJourney", "Lcom/dairum/presentation/screens/memories/MoodPoint;", "tags", "Lcom/dairum/domain/model/Tag;", "LocationsSection", "locations", "Lcom/dairum/domain/model/Location;", "MemoryDetailScreen", "memoryId", "onBackClick", "viewModel", "Lcom/dairum/presentation/screens/memories/MemoryDetailViewModel;", "MoodSummaryCard", "OverviewTab", "entries", "people", "Lcom/dairum/domain/model/Person;", "PeopleSection", "PhotoPreviewSection", "totalCount", "", "QuickStatItem", "QuickStatsCard", "SwipeableEntryCard", "onRemove", "TimelineMarker", "subtitle", "isStart", "", "isEnd", "TimelineTab", "milestones", "Lcom/dairum/domain/model/MemoryMilestone;", "onAddMilestone", "onDeleteMilestone", "buildMemorySummary", "formatDateRange", "start", "end", "formatHour", "hour", "formatNumber", "number", "getMoodEmoji", "level", "getMoodLabel", "avgMood", "", "app_debug"})
public final class MemoryDetailScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    @kotlin.Suppress(names = {"UNUSED_PARAMETER"})
    public static final void MemoryDetailScreen(long memoryId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPhotoClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.memories.MemoryDetailViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OverviewTab(com.dairum.domain.model.Memory memory, java.util.List<com.dairum.domain.model.JournalEntry> entries, com.dairum.presentation.screens.memories.MemoryInsights insights, java.util.List<com.dairum.domain.model.Person> people, java.util.List<com.dairum.domain.model.Location> locations, java.util.List<com.dairum.domain.model.Media> photos, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPhotoClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void QuickStatsCard(com.dairum.domain.model.Memory memory, com.dairum.presentation.screens.memories.MemoryInsights insights) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void QuickStatItem(java.lang.String value, java.lang.String label, androidx.compose.ui.graphics.vector.ImageVector icon) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PhotoPreviewSection(java.util.List<com.dairum.domain.model.Media> photos, int totalCount, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPhotoClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MoodSummaryCard(com.dairum.presentation.screens.memories.MemoryInsights insights) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PeopleSection(java.util.List<com.dairum.domain.model.Person> people) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationsSection(java.util.List<com.dairum.domain.model.Location> locations) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HighlightsCard(java.util.List<java.lang.String> highlights) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void EntryPreviewCard(com.dairum.domain.model.JournalEntry entry, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EntriesTab(java.util.Map<java.time.LocalDate, ? extends java.util.List<com.dairum.domain.model.JournalEntry>> entriesByDate, java.util.List<com.dairum.domain.model.JournalEntry> availableEntries, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onRemoveEntry, kotlin.jvm.functions.Function1<? super java.util.List<java.lang.Long>, kotlin.Unit> onAddEntries, kotlin.jvm.functions.Function0<kotlin.Unit> onLoadAvailableEntries) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void AddEntriesDialog(java.util.List<com.dairum.domain.model.JournalEntry> availableEntries, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.util.List<java.lang.Long>, kotlin.Unit> onConfirm) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void SwipeableEntryCard(com.dairum.domain.model.JournalEntry entry, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void GalleryTab(java.util.List<com.dairum.domain.model.Media> photos, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPhotoClick) {
    }
    
    @androidx.compose.runtime.Composable()
    @kotlin.Suppress(names = {"UNUSED_PARAMETER"})
    private static final void InsightsTab(com.dairum.presentation.screens.memories.MemoryInsights insights, com.dairum.domain.model.Memory memory, java.util.List<com.dairum.presentation.screens.memories.MoodPoint> moodJourney, java.util.List<com.dairum.domain.model.Tag> tags) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void InsightCard(java.lang.String title, androidx.compose.ui.graphics.vector.ImageVector icon, kotlin.jvm.functions.Function1<? super androidx.compose.foundation.layout.ColumnScope, kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void InsightRow(java.lang.String label, java.lang.String value) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TimelineTab(com.dairum.domain.model.Memory memory, java.util.List<com.dairum.domain.model.JournalEntry> entries, java.util.List<com.dairum.domain.model.MemoryMilestone> milestones, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function0<kotlin.Unit> onAddMilestone, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onDeleteMilestone) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TimelineMarker(java.lang.String title, java.lang.String subtitle, boolean isStart, boolean isEnd) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EmptyStateMessage(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String message) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void EditMemoryDialog(com.dairum.domain.model.Memory memory, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onSave) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void AddMilestoneDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.String, ? super java.time.LocalDateTime, kotlin.Unit> onAdd) {
    }
    
    private static final java.lang.String formatDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return null;
    }
    
    private static final java.lang.String formatNumber(int number) {
        return null;
    }
    
    private static final java.lang.String formatHour(int hour) {
        return null;
    }
    
    private static final java.lang.String getMoodEmoji(int level) {
        return null;
    }
    
    private static final java.lang.String getMoodLabel(float avgMood) {
        return null;
    }
    
    private static final java.lang.String buildMemorySummary(com.dairum.domain.model.Memory memory, com.dairum.presentation.screens.memories.MemoryInsights insights) {
        return null;
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    public static final void FlowRow(@org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.layout.Arrangement.Horizontal horizontalArrangement, @org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.layout.Arrangement.Vertical verticalArrangement, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
}