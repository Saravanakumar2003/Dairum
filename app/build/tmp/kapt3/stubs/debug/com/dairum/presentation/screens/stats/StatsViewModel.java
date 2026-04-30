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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u000f\b\u0007\u0018\u00002\u00020\u0001B?\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\"\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020!0 2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#H\u0002J\u0010\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0015H\u0002J\u0016\u0010\'\u001a\u00020(2\u0006\u0010&\u001a\u00020\u0015H\u0082@\u00a2\u0006\u0002\u0010)J\u0016\u0010*\u001a\u00020(2\u0006\u0010&\u001a\u00020\u0015H\u0082@\u00a2\u0006\u0002\u0010)J\u001e\u0010+\u001a\u00020(2\u0006\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u0017H\u0082@\u00a2\u0006\u0002\u0010.J\u0016\u0010/\u001a\u00020(2\u0006\u0010&\u001a\u00020\u0015H\u0082@\u00a2\u0006\u0002\u0010)J\u0016\u00100\u001a\u00020(2\u0006\u0010&\u001a\u00020\u0015H\u0082@\u00a2\u0006\u0002\u0010)J\b\u00101\u001a\u00020(H\u0002J\u0016\u00102\u001a\u00020(2\u0006\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u0017J\u000e\u00103\u001a\u00020(2\u0006\u0010&\u001a\u00020\u0015J\u0016\u00104\u001a\u00020(2\u0006\u0010&\u001a\u00020\u0015H\u0082@\u00a2\u0006\u0002\u0010)J\u000e\u00105\u001a\u00020(H\u0082@\u00a2\u0006\u0002\u00106R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00130\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001e\u00a8\u00067"}, d2 = {"Lcom/dairum/presentation/screens/stats/StatsViewModel;", "Landroidx/lifecycle/ViewModel;", "entryDao", "Lcom/dairum/data/local/dao/JournalEntryDao;", "tagDao", "Lcom/dairum/data/local/dao/TagDao;", "personDao", "Lcom/dairum/data/local/dao/PersonDao;", "tagRepository", "Lcom/dairum/domain/repository/TagRepository;", "personRepository", "Lcom/dairum/domain/repository/PersonRepository;", "mediaRepository", "Lcom/dairum/domain/repository/MediaRepository;", "trackerRepository", "Lcom/dairum/domain/repository/TrackerRepository;", "(Lcom/dairum/data/local/dao/JournalEntryDao;Lcom/dairum/data/local/dao/TagDao;Lcom/dairum/data/local/dao/PersonDao;Lcom/dairum/domain/repository/TagRepository;Lcom/dairum/domain/repository/PersonRepository;Lcom/dairum/domain/repository/MediaRepository;Lcom/dairum/domain/repository/TrackerRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/stats/StatsUiState;", "currentTimeRange", "Lcom/dairum/presentation/screens/stats/TimeRange;", "customRangeEnd", "", "customRangeStart", "trackerCollectorJob", "Lkotlinx/coroutines/Job;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "calculateStreaks", "Lkotlin/Pair;", "", "dates", "", "", "getStartTimeForRange", "range", "loadActivityStats", "", "(Lcom/dairum/presentation/screens/stats/TimeRange;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadJournalStats", "loadJournalStatsCustom", "startMillis", "endMillis", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadMoodStats", "loadPeopleStats", "loadStats", "loadStatsForCustomRange", "loadStatsForRange", "loadTagStats", "loadTrackerStats", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class StatsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.JournalEntryDao entryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.TagDao tagDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.PersonDao personDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.TagRepository tagRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.PersonRepository personRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.MediaRepository mediaRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.TrackerRepository trackerRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.stats.StatsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.stats.StatsUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private com.dairum.presentation.screens.stats.TimeRange currentTimeRange = com.dairum.presentation.screens.stats.TimeRange.ALL_TIME;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job trackerCollectorJob;
    private long customRangeStart = 0L;
    private long customRangeEnd;
    
    @javax.inject.Inject()
    public StatsViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.JournalEntryDao entryDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.TagDao tagDao, @org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.PersonDao personDao, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TagRepository tagRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.PersonRepository personRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MediaRepository mediaRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TrackerRepository trackerRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.stats.StatsUiState> getUiState() {
        return null;
    }
    
    /**
     * Get the start timestamp for the given time range
     */
    private final long getStartTimeForRange(com.dairum.presentation.screens.stats.TimeRange range) {
        return 0L;
    }
    
    private final void loadStats() {
    }
    
    private final java.lang.Object loadJournalStats(com.dairum.presentation.screens.stats.TimeRange range, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadMoodStats(com.dairum.presentation.screens.stats.TimeRange range, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadActivityStats(com.dairum.presentation.screens.stats.TimeRange range, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadTagStats(com.dairum.presentation.screens.stats.TimeRange range, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadPeopleStats(com.dairum.presentation.screens.stats.TimeRange range, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadTrackerStats(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Load stats for a specific time range
     */
    public final void loadStatsForRange(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.stats.TimeRange range) {
    }
    
    /**
     * Load stats for a custom date range
     */
    public final void loadStatsForCustomRange(long startMillis, long endMillis) {
    }
    
    private final java.lang.Object loadJournalStatsCustom(long startMillis, long endMillis, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Integer, java.lang.Integer> calculateStreaks(java.util.List<java.lang.String> dates) {
        return null;
    }
}