package com.dairum.presentation.screens.memories;

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
import com.dairum.domain.model.Memory;
import com.dairum.domain.model.MemoryTrackerType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\\\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0000\u001az\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032b\u0010\u0004\u001a^\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\r\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u0012\u0010\u000e\u001a\u00020\u00012\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0003\u001a\u0016\u0010\u0011\u001a\u00020\u00012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0003\u001a:\u0010\u0015\u001a\u00020\u00012\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00010\u00172\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00010\u00172\b\b\u0002\u0010\u001a\u001a\u00020\u001bH\u0007\u001a<\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u00142\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u000e\b\u0002\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a \u0010!\u001a\u00020\u00012\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u0006H\u0003\u001a \u0010&\u001a\u00020\u00012\u0006\u0010$\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020#H\u0003\u001a\u0018\u0010\'\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u000b2\u0006\u0010)\u001a\u00020\u000bH\u0002\u001a\u0010\u0010*\u001a\u00020\u00062\u0006\u0010+\u001a\u00020,H\u0002\u00a8\u0006-"}, d2 = {"CreateMemoryDialog", "", "onDismiss", "Lkotlin/Function0;", "onCreate", "Lkotlin/Function4;", "", "Lkotlin/ParameterName;", "name", "title", "description", "Ljava/time/LocalDateTime;", "startDate", "endDate", "EmptyMemoriesState", "modifier", "Landroidx/compose/ui/Modifier;", "MemoriesOverviewCard", "memories", "", "Lcom/dairum/domain/model/Memory;", "MemoriesScreen", "onMemoryClick", "Lkotlin/Function1;", "", "onEntryClick", "viewModel", "Lcom/dairum/presentation/screens/memories/MemoriesViewModel;", "MemoryCard", "memory", "onClick", "onFavoriteClick", "onDeleteClick", "MemoryStatChip", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "value", "label", "StatItem", "formatDateRange", "start", "end", "formatNumber", "number", "", "app_debug"})
public final class MemoriesScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void MemoriesScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onMemoryClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.memories.MemoriesViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EmptyMemoriesState(androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MemoriesOverviewCard(java.util.List<com.dairum.domain.model.Memory> memories) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatItem(java.lang.String value, java.lang.String label, androidx.compose.ui.graphics.vector.ImageVector icon) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void MemoryCard(com.dairum.domain.model.Memory memory, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onFavoriteClick, kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MemoryStatChip(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String value, java.lang.String label) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void CreateMemoryDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function4<? super java.lang.String, ? super java.lang.String, ? super java.time.LocalDateTime, ? super java.time.LocalDateTime, kotlin.Unit> onCreate) {
    }
    
    private static final java.lang.String formatDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return null;
    }
    
    private static final java.lang.String formatNumber(int number) {
        return null;
    }
}