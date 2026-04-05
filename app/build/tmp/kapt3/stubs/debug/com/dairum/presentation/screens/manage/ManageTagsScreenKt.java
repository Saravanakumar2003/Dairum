package com.dairum.presentation.screens.manage;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import com.dairum.R;
import com.dairum.domain.model.Tag;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a \u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001aC\u0010\u0006\u001a\u00020\u00012\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032!\u0010\n\u001a\u001d\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a,\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u00a8\u0006\u0012"}, d2 = {"ManageTagsScreen", "", "onBackClick", "Lkotlin/Function0;", "viewModel", "Lcom/dairum/presentation/screens/manage/ManageTagsViewModel;", "TagEditDialog", "tag", "Lcom/dairum/domain/model/Tag;", "onDismiss", "onSave", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "TagItem", "onEditClick", "onDeleteClick", "app_debug"})
public final class ManageTagsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ManageTagsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.manage.ManageTagsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TagItem(com.dairum.domain.model.Tag tag, kotlin.jvm.functions.Function0<kotlin.Unit> onEditClick, kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TagEditDialog(com.dairum.domain.model.Tag tag, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSave) {
    }
}