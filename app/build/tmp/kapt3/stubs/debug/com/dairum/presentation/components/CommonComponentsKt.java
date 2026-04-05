package com.dairum.presentation.components;

import androidx.compose.foundation.layout.ExperimentalLayoutApi;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.compose.ui.unit.Dp;
import com.dairum.domain.model.*;
import com.dairum.presentation.theme.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000v\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u001a\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001aL\u0010\u0006\u001a\u00020\u00012\u0011\u0010\u0007\u001a\r\u0012\u0004\u0012\u00020\u00010\b\u00a2\u0006\u0002\b\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0015\b\u0002\u0010\r\u001a\u000f\u0012\u0004\u0012\u00020\u0001\u0018\u00010\b\u00a2\u0006\u0002\b\t2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a@\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0007\u001a2\u0010\u0015\u001a\u00020\u00012\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0014\u0010\u0018\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0017\u0012\u0004\u0012\u00020\u00010\u00192\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a(\u0010\u001a\u001a\u00020\u00012\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c2\u0006\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a8\u0010 \u001a\u00020\u00012\u0006\u0010!\u001a\u00020\u00172\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\"\u001a\u00020#2\b\b\u0002\u0010$\u001a\u00020\u0014H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b%\u0010&\u001a2\u0010\'\u001a\u00020\u00012\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0014\u0010\u0018\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0017\u0012\u0004\u0012\u00020\u00010\u00192\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a>\u0010(\u001a\u00020\u00012\u0006\u0010)\u001a\u00020*2\u0010\b\u0002\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\b2\u0010\b\u0002\u0010+\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\b2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a8\u0010,\u001a\u00020\u00012\f\u0010-\u001a\b\u0012\u0004\u0012\u00020*0\u001c2\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0016\b\u0002\u0010.\u001a\u0010\u0012\u0004\u0012\u00020*\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0019H\u0007\u001a\u001a\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u0002012\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u0010\u00102\u001a\u00020\u000b2\u0006\u00103\u001a\u000204H\u0002\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00065"}, d2 = {"CategoryBadge", "", "category", "Lcom/dairum/domain/model/Category;", "modifier", "Landroidx/compose/ui/Modifier;", "EmptyState", "icon", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "title", "", "message", "action", "EntryCard", "entry", "Lcom/dairum/domain/model/JournalEntry;", "onClick", "onFavoriteClick", "handleClickInternally", "", "ExtendedMoodGrid", "selectedMood", "Lcom/dairum/domain/model/Mood;", "onMoodSelected", "Lkotlin/Function1;", "MediaThumbnailRow", "media", "", "Lcom/dairum/domain/model/Media;", "totalCount", "", "MoodIndicator", "mood", "size", "Landroidx/compose/ui/unit/Dp;", "showLabel", "MoodIndicator-d8LSEHM", "(Lcom/dairum/domain/model/Mood;Landroidx/compose/ui/Modifier;FZ)V", "MoodSelector", "TagChip", "tag", "Lcom/dairum/domain/model/Tag;", "onRemove", "TagChipRow", "tags", "onTagClick", "WeatherBadge", "weather", "Lcom/dairum/domain/model/Weather;", "formatEntryDate", "dateTime", "Ljava/time/LocalDateTime;", "app_debug"})
@kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
public final class CommonComponentsKt {
    
    /**
     * Entry card for timeline and list views
     */
    @androidx.compose.runtime.Composable()
    public static final void EntryCard(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.JournalEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onFavoriteClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean handleClickInternally) {
    }
    
    /**
     * Mood selector showing all 30 moods in a grid
     */
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    public static final void MoodSelector(@org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood selectedMood, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.dairum.domain.model.Mood, kotlin.Unit> onMoodSelected, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Extended mood grid with categorized moods
     */
    @androidx.compose.runtime.Composable()
    public static final void ExtendedMoodGrid(@org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood selectedMood, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.dairum.domain.model.Mood, kotlin.Unit> onMoodSelected, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Weather badge
     */
    @androidx.compose.runtime.Composable()
    public static final void WeatherBadge(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Weather weather, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Category badge
     */
    @androidx.compose.runtime.Composable()
    public static final void CategoryBadge(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Tag chip
     */
    @androidx.compose.runtime.Composable()
    public static final void TagChip(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Tag tag, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Row of tag chips
     */
    @androidx.compose.runtime.Composable()
    public static final void TagChipRow(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super com.dairum.domain.model.Tag, kotlin.Unit> onTagClick) {
    }
    
    /**
     * Media thumbnail row
     */
    @androidx.compose.runtime.Composable()
    public static final void MediaThumbnailRow(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Media> media, int totalCount, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Empty state
     */
    @androidx.compose.runtime.Composable()
    public static final void EmptyState(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> icon, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> action, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    private static final java.lang.String formatEntryDate(java.time.LocalDateTime dateTime) {
        return null;
    }
}