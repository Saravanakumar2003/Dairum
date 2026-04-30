package com.dairum.presentation.screens.favorites;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.Memory;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MemoryRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0002J\u000e\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u0013R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0016"}, d2 = {"Lcom/dairum/presentation/screens/favorites/FavoritesViewModel;", "Landroidx/lifecycle/ViewModel;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "memoryRepository", "Lcom/dairum/domain/repository/MemoryRepository;", "(Lcom/dairum/domain/repository/JournalRepository;Lcom/dairum/domain/repository/MemoryRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/favorites/FavoritesUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadFavoriteMemories", "", "loadFavorites", "toggleFavorite", "entryId", "", "toggleMemoryFavorite", "memoryId", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class FavoritesViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.MemoryRepository memoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.favorites.FavoritesUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.favorites.FavoritesUiState> uiState = null;
    
    @javax.inject.Inject()
    public FavoritesViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MemoryRepository memoryRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.favorites.FavoritesUiState> getUiState() {
        return null;
    }
    
    private final void loadFavorites() {
    }
    
    private final void loadFavoriteMemories() {
    }
    
    public final void toggleFavorite(long entryId) {
    }
    
    public final void toggleMemoryFavorite(long memoryId) {
    }
}