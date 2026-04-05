package com.dairum.presentation.screens.stats;

import androidx.lifecycle.ViewModel;
import com.dairum.data.local.dao.JournalEntryDao;
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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\"\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00160\u00152\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018H\u0002J\u000e\u0010\u001a\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010\u001cJ\u000e\u0010\u001d\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010\u001cJ\u000e\u0010\u001e\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010\u001cJ\u000e\u0010\u001f\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010\u001cJ\b\u0010 \u001a\u00020\u001bH\u0002J\u000e\u0010!\u001a\u00020\u001b2\u0006\u0010\"\u001a\u00020#J\u000e\u0010$\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010\u001cJ\u000e\u0010%\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010\u001cR\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006&"}, d2 = {"Lcom/dairum/presentation/screens/stats/StatsViewModel;", "Landroidx/lifecycle/ViewModel;", "entryDao", "Lcom/dairum/data/local/dao/JournalEntryDao;", "tagRepository", "Lcom/dairum/domain/repository/TagRepository;", "personRepository", "Lcom/dairum/domain/repository/PersonRepository;", "mediaRepository", "Lcom/dairum/domain/repository/MediaRepository;", "trackerRepository", "Lcom/dairum/domain/repository/TrackerRepository;", "(Lcom/dairum/data/local/dao/JournalEntryDao;Lcom/dairum/domain/repository/TagRepository;Lcom/dairum/domain/repository/PersonRepository;Lcom/dairum/domain/repository/MediaRepository;Lcom/dairum/domain/repository/TrackerRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/stats/StatsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "calculateStreaks", "Lkotlin/Pair;", "", "dates", "", "", "loadActivityStats", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadJournalStats", "loadMoodStats", "loadPeopleStats", "loadStats", "loadStatsForRange", "range", "Lcom/dairum/presentation/screens/stats/TimeRange;", "loadTagStats", "loadTrackerStats", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class StatsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.JournalEntryDao entryDao = null;
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
    
    @javax.inject.Inject()
    public StatsViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.JournalEntryDao entryDao, @org.jetbrains.annotations.NotNull()
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
    
    private final void loadStats() {
    }
    
    private final java.lang.Object loadJournalStats(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadMoodStats(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadActivityStats(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadTagStats(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object loadPeopleStats(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
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
    
    private final kotlin.Pair<java.lang.Integer, java.lang.Integer> calculateStreaks(java.util.List<java.lang.String> dates) {
        return null;
    }
}