package com.dairum.presentation.screens.favorites;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.dairum.R;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.Memory;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001aF\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a,\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00042\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0003\u001aF\u0010\u000f\u001a\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a,\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00112\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0003\u001a4\u0010\u0015\u001a\u00020\u00012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0007\u001a<\u0010\u0015\u001a\u00020\u00012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\u0014\b\u0002\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0007\u00a8\u0006\u0019"}, d2 = {"FavoriteEntriesContent", "", "entries", "", "Lcom/dairum/domain/model/JournalEntry;", "isLoading", "", "onEntryClick", "Lkotlin/Function1;", "", "onToggleFavorite", "FavoriteEntryCard", "entry", "onClick", "Lkotlin/Function0;", "FavoriteMemoriesContent", "memories", "Lcom/dairum/domain/model/Memory;", "onMemoryClick", "FavoriteMemoryCard", "memory", "FavoritesScreen", "onNavigateBack", "viewModel", "Lcom/dairum/presentation/screens/favorites/FavoritesViewModel;", "app_debug"})
public final class FavoritesScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void FavoritesScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onMemoryClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.favorites.FavoritesViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void FavoritesScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.favorites.FavoritesViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FavoriteEntriesContent(java.util.List<com.dairum.domain.model.JournalEntry> entries, boolean isLoading, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onToggleFavorite) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FavoriteMemoriesContent(java.util.List<com.dairum.domain.model.Memory> memories, boolean isLoading, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onMemoryClick, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onToggleFavorite) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FavoriteEntryCard(com.dairum.domain.model.JournalEntry entry, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleFavorite) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FavoriteMemoryCard(com.dairum.domain.model.Memory memory, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleFavorite) {
    }
}