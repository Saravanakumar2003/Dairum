package com.dairum.data.ai.model;

import android.content.Context;
import android.net.Uri;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.domain.model.AIModel;
import com.dairum.domain.model.ModelType;
import com.dairum.domain.repository.AIModelRepository;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.Flow;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import timber.log.Timber;
import java.io.File;
import java.io.FileOutputStream;
import javax.inject.Inject;
import javax.inject.Singleton;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\"%\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0006\u0010\u0007\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\b"}, d2 = {"aiPreferences", "Landroidx/datastore/core/DataStore;", "Landroidx/datastore/preferences/core/Preferences;", "Landroid/content/Context;", "getAiPreferences", "(Landroid/content/Context;)Landroidx/datastore/core/DataStore;", "aiPreferences$delegate", "Lkotlin/properties/ReadOnlyProperty;", "app_debug"})
public final class ModelManagerKt {
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.properties.ReadOnlyProperty aiPreferences$delegate = null;
    
    private static final androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> getAiPreferences(android.content.Context $this$aiPreferences) {
        return null;
    }
}