package com.dairum.presentation.screens.tag;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.Tag;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000R\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a>\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a<\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\t2\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a.\u0010\u0012\u001a\u00020\u00012\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0016H\u0003\u001a\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0002\u00a8\u0006\u001c"}, d2 = {"TagEntriesContent", "", "entries", "", "Lcom/dairum/domain/model/JournalEntry;", "tag", "Lcom/dairum/domain/model/Tag;", "onEntryClick", "Lkotlin/Function1;", "", "modifier", "Landroidx/compose/ui/Modifier;", "TagEntriesScreen", "tagId", "onBackClick", "Lkotlin/Function0;", "viewModel", "Lcom/dairum/presentation/screens/tag/TagEntriesViewModel;", "TagStatsCard", "entryCount", "", "firstUsed", "Ljava/time/LocalDateTime;", "lastUsed", "formatDateHeader", "", "date", "Ljava/time/LocalDate;", "app_debug"})
public final class TagEntriesScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void TagEntriesScreen(long tagId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.tag.TagEntriesViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TagEntriesContent(java.util.List<com.dairum.domain.model.JournalEntry> entries, com.dairum.domain.model.Tag tag, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEntryClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TagStatsCard(com.dairum.domain.model.Tag tag, int entryCount, java.time.LocalDateTime firstUsed, java.time.LocalDateTime lastUsed) {
    }
    
    private static final java.lang.String formatDateHeader(java.time.LocalDate date) {
        return null;
    }
}