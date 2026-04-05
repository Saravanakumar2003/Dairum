package com.dairum.presentation.screens.manage;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import com.dairum.R;
import com.dairum.domain.model.Category;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\u001am\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\b2K\u0010\t\u001aG\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\u000e\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00040\nH\u0003\u001a:\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00040\bH\u0003\u001a \u0010\u0015\u001a\u00020\u00042\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0007\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"ExtendedCategoryColors", "", "Landroidx/compose/ui/graphics/Color;", "CategoryEditDialog", "", "category", "Lcom/dairum/domain/model/Category;", "onDismiss", "Lkotlin/Function0;", "onSave", "Lkotlin/Function3;", "", "Lkotlin/ParameterName;", "name", "", "color", "icon", "CategoryItem", "onEditClick", "onDeleteClick", "onSetDefaultClick", "ManageCategoriesScreen", "onBackClick", "viewModel", "Lcom/dairum/presentation/screens/manage/ManageCategoriesViewModel;", "app_debug"})
public final class ManageCategoriesScreenKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<androidx.compose.ui.graphics.Color> ExtendedCategoryColors = null;
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    public static final void ManageCategoriesScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.manage.ManageCategoriesViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CategoryItem(com.dairum.domain.model.Category category, kotlin.jvm.functions.Function0<kotlin.Unit> onEditClick, kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteClick, kotlin.jvm.functions.Function0<kotlin.Unit> onSetDefaultClick) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void CategoryEditDialog(com.dairum.domain.model.Category category, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.Integer, ? super java.lang.String, kotlin.Unit> onSave) {
    }
}