package com.dairum.presentation.screens.settings;

import android.content.Context;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.EntryTemplate;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\"%\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0006\u0010\u0007\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\b"}, d2 = {"templateDataStore", "Landroidx/datastore/core/DataStore;", "Landroidx/datastore/preferences/core/Preferences;", "Landroid/content/Context;", "getTemplateDataStore", "(Landroid/content/Context;)Landroidx/datastore/core/DataStore;", "templateDataStore$delegate", "Lkotlin/properties/ReadOnlyProperty;", "app_debug"})
public final class TemplateSettingsViewModelKt {
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.properties.ReadOnlyProperty templateDataStore$delegate = null;
    
    private static final androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> getTemplateDataStore(android.content.Context $this$templateDataStore) {
        return null;
    }
}