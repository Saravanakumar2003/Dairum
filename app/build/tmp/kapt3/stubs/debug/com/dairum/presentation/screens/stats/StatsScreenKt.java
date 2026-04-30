package com.dairum.presentation.screens.stats;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.text.font.FontWeight;
import com.dairum.R;
import com.dairum.domain.model.*;
import com.dairum.presentation.theme.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000~\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0003\u001a&\u0010\u0004\u001a\u00020\u00012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u00062\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001a \u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0007H\u0003\u001a \u0010\u0010\u001a\u00020\u00012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001a,\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u000e2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00122\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0003\u001a:\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001f\u001a\u0012\u0010 \u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010!H\u0003\u001a\u0018\u0010\"\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0003\u001a\u0012\u0010&\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\'H\u0003\u001a\b\u0010(\u001a\u00020\u0001H\u0003\u001a*\u0010)\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001a\u0012\u0010+\u001a\u00020\u00012\b\b\u0002\u0010,\u001a\u00020-H\u0007\u001a\u0012\u0010.\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010/H\u0003\u001a\u0016\u00100\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002010\u0012H\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00062"}, d2 = {"EntriesContent", "", "stats", "Lcom/dairum/domain/model/EntryActivityStats;", "HourlyBarChart", "data", "", "", "modifier", "Landroidx/compose/ui/Modifier;", "MediaStatItem", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "label", "", "count", "MoodBarChart", "distribution", "", "Lcom/dairum/domain/model/MoodDistributionItem;", "MoodCategorySection", "title", "moods", "Lcom/dairum/domain/model/MoodOption;", "MoodCategoryStat", "emoji", "percentage", "", "color", "Landroidx/compose/ui/graphics/Color;", "MoodCategoryStat-xwkQ0AY", "(Ljava/lang/String;Ljava/lang/String;IFJ)V", "MoodContent", "Lcom/dairum/domain/model/MoodStats;", "OverviewContent", "Lcom/dairum/domain/model/JournalStats;", "selectedRange", "Lcom/dairum/presentation/screens/stats/TimeRange;", "PeopleContent", "Lcom/dairum/domain/model/PeopleStats;", "ShimmerStatsContent", "StatCard", "value", "StatsScreen", "viewModel", "Lcom/dairum/presentation/screens/stats/StatsViewModel;", "TagsContent", "Lcom/dairum/domain/model/TagStats;", "TrackersContent", "Lcom/dairum/domain/model/TrackerStats;", "app_debug"})
public final class StatsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    public static final void StatsScreen(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.stats.StatsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ShimmerStatsContent() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OverviewContent(com.dairum.domain.model.JournalStats stats, com.dairum.presentation.screens.stats.TimeRange selectedRange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EntriesContent(com.dairum.domain.model.EntryActivityStats stats) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void MoodContent(com.dairum.domain.model.MoodStats stats) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TagsContent(com.dairum.domain.model.TagStats stats) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PeopleContent(com.dairum.domain.model.PeopleStats stats) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TrackersContent(java.util.List<com.dairum.domain.model.TrackerStats> stats) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatCard(java.lang.String title, java.lang.String value, androidx.compose.ui.graphics.vector.ImageVector icon, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MediaStatItem(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String label, int count) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HourlyBarChart(java.util.Map<java.lang.Integer, java.lang.Integer> data, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MoodBarChart(java.util.List<com.dairum.domain.model.MoodDistributionItem> distribution, androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void MoodCategorySection(java.lang.String title, java.util.List<? extends com.dairum.domain.model.MoodOption> moods, java.util.List<com.dairum.domain.model.MoodDistributionItem> distribution) {
    }
}