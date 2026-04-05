package com.dairum.presentation.screens.memories;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MemoryRepository;
import com.dairum.domain.repository.MediaRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0017J \u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001c\u001a\u00020\u001dJ\u001e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u0017H\u0002J\u0006\u0010$\u001a\u00020\u0015J\u000e\u0010%\u001a\u00020\u00152\u0006\u0010&\u001a\u00020\u000fJ\u0006\u0010\'\u001a\u00020\u0015J\u0006\u0010(\u001a\u00020\u0015J\b\u0010)\u001a\u00020\u0015H\u0002J\u000e\u0010*\u001a\u00020\u00152\u0006\u0010+\u001a\u00020\u000fJ\u000e\u0010,\u001a\u00020\u00152\u0006\u0010-\u001a\u00020.J\u0006\u0010/\u001a\u00020\u0015J\u0018\u00100\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001aR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\r0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u00061"}, d2 = {"Lcom/dairum/presentation/screens/memories/MemoryDetailViewModel;", "Landroidx/lifecycle/ViewModel;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "memoryRepository", "Lcom/dairum/domain/repository/MemoryRepository;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "mediaRepository", "Lcom/dairum/domain/repository/MediaRepository;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/dairum/domain/repository/MemoryRepository;Lcom/dairum/domain/repository/JournalRepository;Lcom/dairum/domain/repository/MediaRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/memories/MemoryDetailUiState;", "memoryId", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addEntries", "", "entryIds", "", "addMilestone", "title", "", "description", "date", "Ljava/time/LocalDateTime;", "calculateInsights", "Lcom/dairum/presentation/screens/memories/MemoryInsights;", "memory", "Lcom/dairum/domain/model/Memory;", "entries", "Lcom/dairum/domain/model/JournalEntry;", "deleteMemory", "deleteMilestone", "milestoneId", "generateHighlights", "loadAvailableEntries", "loadMemory", "removeEntry", "entryId", "setActiveTab", "tab", "Lcom/dairum/presentation/screens/memories/MemoryTab;", "toggleFavorite", "updateMemory", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MemoryDetailViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.MemoryRepository memoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.MediaRepository mediaRepository = null;
    private final long memoryId = 0L;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.memories.MemoryDetailUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.memories.MemoryDetailUiState> uiState = null;
    
    @javax.inject.Inject()
    public MemoryDetailViewModel(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MemoryRepository memoryRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MediaRepository mediaRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.memories.MemoryDetailUiState> getUiState() {
        return null;
    }
    
    private final void loadMemory() {
    }
    
    private final com.dairum.presentation.screens.memories.MemoryInsights calculateInsights(com.dairum.domain.model.Memory memory, java.util.List<com.dairum.domain.model.JournalEntry> entries) {
        return null;
    }
    
    public final void toggleFavorite() {
    }
    
    public final void updateMemory(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description) {
    }
    
    public final void deleteMemory() {
    }
    
    public final void removeEntry(long entryId) {
    }
    
    public final void addMilestone(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime date) {
    }
    
    public final void deleteMilestone(long milestoneId) {
    }
    
    /**
     * Load all entries that can be added to this memory (no date restriction)
     */
    public final void loadAvailableEntries() {
    }
    
    /**
     * Add entries to this memory
     */
    public final void addEntries(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> entryIds) {
    }
    
    public final void generateHighlights() {
    }
    
    public final void setActiveTab(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.memories.MemoryTab tab) {
    }
}