package com.dairum.presentation.screens.home;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.Category;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.Memory;
import com.dairum.domain.model.Person;
import com.dairum.domain.model.Tag;
import com.dairum.domain.repository.CategoryRepository;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MemoryRepository;
import com.dairum.domain.repository.PersonRepository;
import com.dairum.domain.repository.TagRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0010\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u001eJ\u0006\u0010!\u001a\u00020\u001cJ\u0006\u0010\"\u001a\u00020\u001cJ\u0006\u0010#\u001a\u00020\u001cJ\u0006\u0010$\u001a\u00020\u001cJ\u000e\u0010%\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u001eJ\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013J\b\u0010\'\u001a\u00020\u001cH\u0002J\b\u0010(\u001a\u00020\u001cH\u0002J\b\u0010)\u001a\u00020\u001cH\u0002J\b\u0010*\u001a\u00020\u001cH\u0002J\b\u0010+\u001a\u00020\u001cH\u0002J\u0006\u0010,\u001a\u00020\u001cJ\u000e\u0010-\u001a\u00020\u001c2\u0006\u0010.\u001a\u00020/J\u0006\u00100\u001a\u00020\u001cJ\u000e\u00101\u001a\u00020\u001c2\u0006\u00102\u001a\u00020\u0011J\u000e\u00103\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u001eJ\u000e\u00104\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u001eJ\u000e\u00105\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u001eR\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00110\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0018\u00a8\u00066"}, d2 = {"Lcom/dairum/presentation/screens/home/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "categoryRepository", "Lcom/dairum/domain/repository/CategoryRepository;", "tagRepository", "Lcom/dairum/domain/repository/TagRepository;", "personRepository", "Lcom/dairum/domain/repository/PersonRepository;", "memoryRepository", "Lcom/dairum/domain/repository/MemoryRepository;", "(Lcom/dairum/domain/repository/JournalRepository;Lcom/dairum/domain/repository/CategoryRepository;Lcom/dairum/domain/repository/TagRepository;Lcom/dairum/domain/repository/PersonRepository;Lcom/dairum/domain/repository/MemoryRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/home/HomeUiState;", "_viewMode", "Lcom/dairum/presentation/screens/home/ViewMode;", "allEntries", "", "Lcom/dairum/domain/model/JournalEntry;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "viewMode", "getViewMode", "addEntriesToMemory", "", "memoryId", "", "archiveEntry", "entryId", "clearFilter", "clearSearch", "clearSelection", "dismissError", "duplicateEntry", "getSelectedEntries", "loadCategories", "loadEntries", "loadMemories", "loadPeople", "loadTags", "refresh", "searchEntries", "query", "", "selectAllEntries", "setViewMode", "mode", "toggleEntrySelection", "toggleFavorite", "togglePin", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.CategoryRepository categoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.TagRepository tagRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.PersonRepository personRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.MemoryRepository memoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.home.HomeUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.home.HomeUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.home.ViewMode> _viewMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.home.ViewMode> viewMode = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.dairum.domain.model.JournalEntry> allEntries;
    
    @javax.inject.Inject()
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.CategoryRepository categoryRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TagRepository tagRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.PersonRepository personRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MemoryRepository memoryRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.home.HomeUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.home.ViewMode> getViewMode() {
        return null;
    }
    
    private final void loadEntries() {
    }
    
    private final void loadMemories() {
    }
    
    private final void loadCategories() {
    }
    
    private final void loadTags() {
    }
    
    private final void loadPeople() {
    }
    
    public final void setViewMode(@org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.home.ViewMode mode) {
    }
    
    public final void toggleFavorite(long entryId) {
    }
    
    public final void togglePin(long entryId) {
    }
    
    public final void archiveEntry(long entryId) {
    }
    
    public final void duplicateEntry(long entryId) {
    }
    
    public final void clearFilter() {
    }
    
    public final void searchEntries(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void clearSearch() {
    }
    
    public final void dismissError() {
    }
    
    public final void toggleEntrySelection(long entryId) {
    }
    
    public final void selectAllEntries() {
    }
    
    public final void clearSelection() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> getSelectedEntries() {
        return null;
    }
    
    /**
     * Add selected entries to an existing memory
     */
    public final void addEntriesToMemory(long memoryId) {
    }
    
    public final void refresh() {
    }
}