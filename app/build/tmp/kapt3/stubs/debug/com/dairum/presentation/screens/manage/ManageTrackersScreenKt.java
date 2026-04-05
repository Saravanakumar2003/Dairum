package com.dairum.presentation.screens.manage;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import com.dairum.R;
import com.dairum.domain.model.CustomTracker;
import com.dairum.domain.model.TrackerType;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a \u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001ao\u0010\u0006\u001a\u00020\u00012\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032M\u0010\n\u001aI\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u0010\u0012\u0015\u0012\u0013\u0018\u00010\f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a>\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u000e\u0010\u0014\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00032\u000e\u0010\u0015\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0003H\u0003\u00a8\u0006\u0016"}, d2 = {"ManageTrackersScreen", "", "onBackClick", "Lkotlin/Function0;", "viewModel", "Lcom/dairum/presentation/screens/manage/ManageTrackersViewModel;", "TrackerEditDialog", "tracker", "Lcom/dairum/domain/model/CustomTracker;", "onDismiss", "onSave", "Lkotlin/Function3;", "", "Lkotlin/ParameterName;", "name", "Lcom/dairum/domain/model/TrackerType;", "type", "unit", "TrackerItem", "onToggle", "onEditClick", "onDeleteClick", "app_debug"})
public final class ManageTrackersScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ManageTrackersScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.manage.ManageTrackersViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TrackerItem(com.dairum.domain.model.CustomTracker tracker, kotlin.jvm.functions.Function0<kotlin.Unit> onToggle, kotlin.jvm.functions.Function0<kotlin.Unit> onEditClick, kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteClick) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void TrackerEditDialog(com.dairum.domain.model.CustomTracker tracker, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function3<? super java.lang.String, ? super com.dairum.domain.model.TrackerType, ? super java.lang.String, kotlin.Unit> onSave) {
    }
}