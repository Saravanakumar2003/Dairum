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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0082\b\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003JG\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00d6\u0001J\t\u0010 \u001a\u00020\tH\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000f\u00a8\u0006!"}, d2 = {"Lcom/dairum/presentation/screens/home/WeeklyInsights;", "", "entriesThisWeek", "", "wordCountThisWeek", "streakDays", "topMood", "Lcom/dairum/domain/model/Mood;", "comparisonText", "", "journaledDaysThisWeek", "(IIILcom/dairum/domain/model/Mood;Ljava/lang/String;I)V", "getComparisonText", "()Ljava/lang/String;", "getEntriesThisWeek", "()I", "getJournaledDaysThisWeek", "getStreakDays", "getTopMood", "()Lcom/dairum/domain/model/Mood;", "getWordCountThisWeek", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
final class WeeklyInsights {
    private final int entriesThisWeek = 0;
    private final int wordCountThisWeek = 0;
    private final int streakDays = 0;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Mood topMood = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String comparisonText = null;
    private final int journaledDaysThisWeek = 0;
    
    public WeeklyInsights(int entriesThisWeek, int wordCountThisWeek, int streakDays, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood topMood, @org.jetbrains.annotations.NotNull()
    java.lang.String comparisonText, int journaledDaysThisWeek) {
        super();
    }
    
    public final int getEntriesThisWeek() {
        return 0;
    }
    
    public final int getWordCountThisWeek() {
        return 0;
    }
    
    public final int getStreakDays() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Mood getTopMood() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getComparisonText() {
        return null;
    }
    
    public final int getJournaledDaysThisWeek() {
        return 0;
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int component3() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Mood component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.home.WeeklyInsights copy(int entriesThisWeek, int wordCountThisWeek, int streakDays, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood topMood, @org.jetbrains.annotations.NotNull()
    java.lang.String comparisonText, int journaledDaysThisWeek) {
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