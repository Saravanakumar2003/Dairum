package com.dairum.presentation.screens.memories;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MemoryRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.LocalDateTime;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u0017J(\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001c\u001a\u00020\u001dJ2\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u001a2\b\u0010#\u001a\u0004\u0018\u00010\u001aJ\u0006\u0010$\u001a\u00020\u0013J8\u0010%\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001a2\u0006\u0010&\u001a\u00020\u001d2\u0006\u0010\'\u001a\u00020\u001d2\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u0017J\u000e\u0010(\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010)\u001a\u00020\u00132\u0006\u0010*\u001a\u00020\u0015J\u000e\u0010+\u001a\u00020\u00132\u0006\u0010,\u001a\u00020\u0015J\u000e\u0010-\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010.\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010/\u001a\u00020\u0013J\b\u00100\u001a\u00020\u0013H\u0002J\u0016\u00101\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u00102\u001a\u00020\u0015J\u000e\u00103\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u00104\u001a\u00020\u00132\u0006\u00105\u001a\u00020\u001aJ\u0006\u00106\u001a\u00020\u0013J\u000e\u00107\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u00108\u001a\u00020\u00132\u0006\u00109\u001a\u00020\tJ\u000e\u0010:\u001a\u00020\u00132\u0006\u0010;\u001a\u00020<R\u0016\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000f\u00a8\u0006="}, d2 = {"Lcom/dairum/presentation/screens/memories/MemoriesViewModel;", "Landroidx/lifecycle/ViewModel;", "memoryRepository", "Lcom/dairum/domain/repository/MemoryRepository;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "(Lcom/dairum/domain/repository/MemoryRepository;Lcom/dairum/domain/repository/JournalRepository;)V", "_selectedMemory", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/domain/model/Memory;", "_uiState", "Lcom/dairum/presentation/screens/memories/MemoriesUiState;", "selectedMemory", "Lkotlinx/coroutines/flow/StateFlow;", "getSelectedMemory", "()Lkotlinx/coroutines/flow/StateFlow;", "uiState", "getUiState", "addEntriesToMemory", "", "memoryId", "", "entryIds", "", "addMilestone", "title", "", "description", "date", "Ljava/time/LocalDateTime;", "addTracker", "name", "type", "Lcom/dairum/domain/model/MemoryTrackerType;", "unit", "goalValue", "clearSelectedMemory", "createMemory", "startDate", "endDate", "deleteMemory", "deleteMilestone", "milestoneId", "deleteTracker", "trackerId", "generateHighlights", "getSuggestedEntries", "hideCreateDialog", "loadMemories", "removeEntryFromMemory", "entryId", "selectMemory", "setSearchQuery", "query", "showCreateDialog", "toggleFavorite", "updateMemory", "memory", "updateTracker", "tracker", "Lcom/dairum/domain/model/MemoryTracker;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MemoriesViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.MemoryRepository memoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.memories.MemoriesUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.memories.MemoriesUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.domain.model.Memory> _selectedMemory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.domain.model.Memory> selectedMemory = null;
    
    @javax.inject.Inject()
    public MemoriesViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MemoryRepository memoryRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.memories.MemoriesUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.domain.model.Memory> getSelectedMemory() {
        return null;
    }
    
    private final void loadMemories() {
    }
    
    public final void selectMemory(long memoryId) {
    }
    
    public final void clearSelectedMemory() {
    }
    
    public final void createMemory(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startDate, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endDate, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> entryIds) {
    }
    
    public final void updateMemory(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Memory memory) {
    }
    
    public final void deleteMemory(long memoryId) {
    }
    
    public final void toggleFavorite(long memoryId) {
    }
    
    public final void addEntriesToMemory(long memoryId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> entryIds) {
    }
    
    public final void removeEntryFromMemory(long memoryId, long entryId) {
    }
    
    public final void addMilestone(long memoryId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime date) {
    }
    
    public final void deleteMilestone(long milestoneId) {
    }
    
    public final void addTracker(long memoryId, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryTrackerType type, @org.jetbrains.annotations.Nullable()
    java.lang.String unit, @org.jetbrains.annotations.Nullable()
    java.lang.String goalValue) {
    }
    
    public final void updateTracker(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MemoryTracker tracker) {
    }
    
    public final void deleteTracker(long trackerId) {
    }
    
    public final void getSuggestedEntries(long memoryId) {
    }
    
    public final void generateHighlights(long memoryId) {
    }
    
    public final void showCreateDialog() {
    }
    
    public final void hideCreateDialog() {
    }
    
    public final void setSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
}