package com.dairum.presentation.screens.settings;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import com.dairum.domain.model.EntryTemplate;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\u001a8\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u000e\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00072\u000e\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0007H\u0003\u001a\u0082\u0001\u0010\t\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032`\u0010\n\u001a\\\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a \u0010\u0013\u001a\u00020\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0007\u00a8\u0006\u0017"}, d2 = {"TemplateCard", "", "template", "Lcom/dairum/domain/model/EntryTemplate;", "isBuiltIn", "", "onEdit", "Lkotlin/Function0;", "onDelete", "TemplateEditDialog", "onSave", "Lkotlin/Function4;", "", "Lkotlin/ParameterName;", "name", "icon", "title", "content", "onDismiss", "TemplateSettingsScreen", "onBack", "viewModel", "Lcom/dairum/presentation/screens/settings/TemplateSettingsViewModel;", "app_debug"})
public final class TemplateSettingsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void TemplateSettingsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.settings.TemplateSettingsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TemplateCard(com.dairum.domain.model.EntryTemplate template, boolean isBuiltIn, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TemplateEditDialog(com.dairum.domain.model.EntryTemplate template, kotlin.jvm.functions.Function4<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
}