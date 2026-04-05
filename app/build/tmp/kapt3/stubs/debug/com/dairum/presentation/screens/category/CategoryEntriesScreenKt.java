package com.dairum.presentation.screens.category;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.dairum.domain.model.Category;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a<\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a\u0018\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0003\u001a\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002\u001a\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\fH\u0002\u00a8\u0006\u0014"}, d2 = {"CategoryEntriesScreen", "", "categoryId", "", "onBackClick", "Lkotlin/Function0;", "onEntryClick", "Lkotlin/Function1;", "viewModel", "Lcom/dairum/presentation/screens/category/CategoryEntriesViewModel;", "CategoryStatsHeader", "category", "Lcom/dairum/domain/model/Category;", "entryCount", "", "formatDateHeader", "", "date", "Ljava/time/LocalDate;", "getCategoryEmoji", "app_debug"})
public final class CategoryEntriesScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CategoryEntriesScreen(long categoryId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.category.CategoryEntriesViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CategoryStatsHeader(com.dairum.domain.model.Category category, int entryCount) {
    }
    
    private static final java.lang.String formatDateHeader(java.time.LocalDate date) {
        return null;
    }
    
    private static final java.lang.String getCategoryEmoji(com.dairum.domain.model.Category category) {
        return null;
    }
}