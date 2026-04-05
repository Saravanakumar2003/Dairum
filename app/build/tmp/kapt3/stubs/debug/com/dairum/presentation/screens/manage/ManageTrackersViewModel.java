package com.dairum.presentation.screens.manage;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.CustomTracker;
import com.dairum.domain.model.TrackerType;
import com.dairum.domain.repository.TrackerRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u000fJ\u000e\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u0015J\b\u0010\u0016\u001a\u00020\rH\u0002J\u0016\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u001cR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u001d"}, d2 = {"Lcom/dairum/presentation/screens/manage/ManageTrackersViewModel;", "Landroidx/lifecycle/ViewModel;", "trackerRepository", "Lcom/dairum/domain/repository/TrackerRepository;", "(Lcom/dairum/domain/repository/TrackerRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/manage/ManageTrackersUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "createTracker", "", "name", "", "type", "Lcom/dairum/domain/model/TrackerType;", "unit", "deleteTracker", "id", "", "loadTrackers", "toggleActive", "isActive", "", "updateTracker", "tracker", "Lcom/dairum/domain/model/CustomTracker;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ManageTrackersViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.TrackerRepository trackerRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.manage.ManageTrackersUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.manage.ManageTrackersUiState> uiState = null;
    
    @javax.inject.Inject()
    public ManageTrackersViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TrackerRepository trackerRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.manage.ManageTrackersUiState> getUiState() {
        return null;
    }
    
    private final void loadTrackers() {
    }
    
    public final void createTracker(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.TrackerType type, @org.jetbrains.annotations.Nullable()
    java.lang.String unit) {
    }
    
    public final void updateTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.CustomTracker tracker) {
    }
    
    public final void deleteTracker(long id) {
    }
    
    public final void toggleActive(long id, boolean isActive) {
    }
}