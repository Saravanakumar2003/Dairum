package com.dairum.presentation.screens.favorites;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.Memory;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MemoryRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B/\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\bH\u00c6\u0003J3\u0010\u0011\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\b\b\u0002\u0010\u0007\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\b2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\r\u00a8\u0006\u0018"}, d2 = {"Lcom/dairum/presentation/screens/favorites/FavoritesUiState;", "", "entries", "", "Lcom/dairum/domain/model/JournalEntry;", "favoriteMemories", "Lcom/dairum/domain/model/Memory;", "isLoading", "", "(Ljava/util/List;Ljava/util/List;Z)V", "getEntries", "()Ljava/util/List;", "getFavoriteMemories", "()Z", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class FavoritesUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.JournalEntry> entries = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Memory> favoriteMemories = null;
    private final boolean isLoading = false;
    
    public FavoritesUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Memory> favoriteMemories, boolean isLoading) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> getEntries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Memory> getFavoriteMemories() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    public FavoritesUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Memory> component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.favorites.FavoritesUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Memory> favoriteMemories, boolean isLoading) {
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