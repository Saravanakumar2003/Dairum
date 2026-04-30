package com.dairum.presentation.screens.stats;

import androidx.lifecycle.ViewModel;
import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.PersonDao;
import com.dairum.data.local.dao.TagDao;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.MediaRepository;
import com.dairum.domain.repository.PersonRepository;
import com.dairum.domain.repository.TagRepository;
import com.dairum.domain.repository.TrackerRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001f\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Bo\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\u0002\u0010\u0015J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\u000f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u00c6\u0003J\t\u0010-\u001a\u00020\u0010H\u00c6\u0003J\u000b\u0010.\u001a\u0004\u0018\u00010\u0012H\u00c6\u0003J\t\u0010/\u001a\u00020\u0014H\u00c6\u0003Js\u00100\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u00c6\u0001J\u0013\u00101\u001a\u00020\u00102\b\u00102\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00103\u001a\u000204H\u00d6\u0001J\t\u00105\u001a\u00020\u0012H\u00d6\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u001aR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&\u00a8\u00066"}, d2 = {"Lcom/dairum/presentation/screens/stats/StatsUiState;", "", "journalStats", "Lcom/dairum/domain/model/JournalStats;", "moodStats", "Lcom/dairum/domain/model/MoodStats;", "activityStats", "Lcom/dairum/domain/model/EntryActivityStats;", "tagStats", "Lcom/dairum/domain/model/TagStats;", "peopleStats", "Lcom/dairum/domain/model/PeopleStats;", "trackerStats", "", "Lcom/dairum/domain/model/TrackerStats;", "isLoading", "", "error", "", "selectedTimeRange", "Lcom/dairum/presentation/screens/stats/TimeRange;", "(Lcom/dairum/domain/model/JournalStats;Lcom/dairum/domain/model/MoodStats;Lcom/dairum/domain/model/EntryActivityStats;Lcom/dairum/domain/model/TagStats;Lcom/dairum/domain/model/PeopleStats;Ljava/util/List;ZLjava/lang/String;Lcom/dairum/presentation/screens/stats/TimeRange;)V", "getActivityStats", "()Lcom/dairum/domain/model/EntryActivityStats;", "getError", "()Ljava/lang/String;", "()Z", "getJournalStats", "()Lcom/dairum/domain/model/JournalStats;", "getMoodStats", "()Lcom/dairum/domain/model/MoodStats;", "getPeopleStats", "()Lcom/dairum/domain/model/PeopleStats;", "getSelectedTimeRange", "()Lcom/dairum/presentation/screens/stats/TimeRange;", "getTagStats", "()Lcom/dairum/domain/model/TagStats;", "getTrackerStats", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class StatsUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.JournalStats journalStats = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.MoodStats moodStats = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.EntryActivityStats activityStats = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.TagStats tagStats = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.PeopleStats peopleStats = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.TrackerStats> trackerStats = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.presentation.screens.stats.TimeRange selectedTimeRange = null;
    
    public StatsUiState(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.JournalStats journalStats, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.MoodStats moodStats, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.EntryActivityStats activityStats, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.TagStats tagStats, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.PeopleStats peopleStats, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.TrackerStats> trackerStats, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.stats.TimeRange selectedTimeRange) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.JournalStats getJournalStats() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.MoodStats getMoodStats() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.EntryActivityStats getActivityStats() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.TagStats getTagStats() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.PeopleStats getPeopleStats() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.TrackerStats> getTrackerStats() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.stats.TimeRange getSelectedTimeRange() {
        return null;
    }
    
    public StatsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.JournalStats component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.MoodStats component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.EntryActivityStats component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.TagStats component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.PeopleStats component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.TrackerStats> component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.stats.TimeRange component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.stats.StatsUiState copy(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.JournalStats journalStats, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.MoodStats moodStats, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.EntryActivityStats activityStats, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.TagStats tagStats, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.PeopleStats peopleStats, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.TrackerStats> trackerStats, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.stats.TimeRange selectedTimeRange) {
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