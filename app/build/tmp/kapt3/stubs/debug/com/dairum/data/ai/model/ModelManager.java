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

/**
 * Model manager for handling model downloads, imports, and switching
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\b\u0007\u0018\u0000 B2\u00020\u0001:\u0001BB1\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ$\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u0012\u0010\u001c\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001d\u001a\u00020\u0019H\u0002J\\\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00162\u0006\u0010\u0018\u001a\u00020\u001926\u0010 \u001a2\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b#\u0012\b\b$\u0012\u0004\b\b(%\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b#\u0012\b\b$\u0012\u0004\b\b(&\u0012\u0004\u0012\u00020\u00170!H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\'\u0010(J\u0010\u0010)\u001a\u00020\u00192\u0006\u0010*\u001a\u00020+H\u0002J\u0012\u0010,\u001a\u0004\u0018\u00010\u00192\u0006\u0010-\u001a\u00020.H\u0002J\u000e\u0010/\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001900J\u000e\u00101\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001900J\u0006\u00102\u001a\u000203J<\u00104\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00162\u0006\u0010$\u001a\u00020\u00192\u0006\u00105\u001a\u00020\u00192\u0006\u00106\u001a\u00020\u00192\u0006\u0010*\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b7\u00108J4\u00109\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00162\u0006\u0010-\u001a\u00020.2\u0006\u0010$\u001a\u00020\u00192\u0006\u0010*\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b:\u0010;J\u000e\u0010<\u001a\u00020\u0017H\u0086@\u00a2\u0006\u0002\u0010=J\u001c\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b?\u0010=J\u0016\u0010@\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010A\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u001bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000e8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u000e8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\u00020\u000e8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0010R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006C"}, d2 = {"Lcom/dairum/data/ai/model/ModelManager;", "", "context", "Landroid/content/Context;", "modelRepository", "Lcom/dairum/domain/repository/AIModelRepository;", "embeddingService", "Lcom/dairum/data/ai/embedding/EmbeddingService;", "localLLM", "Lcom/dairum/data/ai/llm/LocalLLM;", "okHttpClient", "Lokhttp3/OkHttpClient;", "(Landroid/content/Context;Lcom/dairum/domain/repository/AIModelRepository;Lcom/dairum/data/ai/embedding/EmbeddingService;Lcom/dairum/data/ai/llm/LocalLLM;Lokhttp3/OkHttpClient;)V", "embeddingModelsDir", "Ljava/io/File;", "getEmbeddingModelsDir", "()Ljava/io/File;", "llmModelsDir", "getLlmModelsDir", "modelsDir", "getModelsDir", "deleteModel", "Lkotlin/Result;", "", "modelId", "", "deleteModel-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "detectQuantization", "fileName", "downloadModel", "Lcom/dairum/domain/model/AIModel;", "onProgress", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "bytesDownloaded", "totalBytes", "downloadModel-0E7RQCE", "(Ljava/lang/String;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getExtensionForType", "modelType", "Lcom/dairum/domain/model/ModelType;", "getFileNameFromUri", "uri", "Landroid/net/Uri;", "getSelectedEmbeddingModelId", "Lkotlinx/coroutines/flow/Flow;", "getSelectedLLMModelId", "getStorageUsage", "Lcom/dairum/data/ai/model/StorageUsage;", "importModel", "description", "sourceUri", "importModel-yxL6bBk", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/dairum/domain/model/ModelType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "importModelFromUri", "importModelFromUri-BWLJW6A", "(Landroid/net/Uri;Ljava/lang/String;Lcom/dairum/domain/model/ModelType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "initializeDefaultModels", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadSelectedModels", "loadSelectedModels-IoAF18A", "selectEmbeddingModel", "selectLLMModel", "Companion", "app_debug"})
public final class ModelManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.AIModelRepository modelRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.embedding.EmbeddingService embeddingService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.llm.LocalLLM localLLM = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient okHttpClient = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ModelManager";
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_SELECTED_LLM = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_SELECTED_EMBEDDING = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.dairum.domain.model.AIModel> DEFAULT_MODELS = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.ai.model.ModelManager.Companion Companion = null;
    
    @javax.inject.Inject()
    public ModelManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.AIModelRepository modelRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.embedding.EmbeddingService embeddingService, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.llm.LocalLLM localLLM, @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient) {
        super();
    }
    
    private final java.io.File getModelsDir() {
        return null;
    }
    
    private final java.io.File getLlmModelsDir() {
        return null;
    }
    
    private final java.io.File getEmbeddingModelsDir() {
        return null;
    }
    
    /**
     * Initialize default models in the registry
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object initializeDefaultModels(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Get the currently selected LLM model ID
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getSelectedLLMModelId() {
        return null;
    }
    
    /**
     * Get the currently selected embedding model ID
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getSelectedEmbeddingModelId() {
        return null;
    }
    
    /**
     * Set the selected LLM model
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object selectLLMModel(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Set the selected embedding model
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object selectEmbeddingModel(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.String getFileNameFromUri(android.net.Uri uri) {
        return null;
    }
    
    private final java.lang.String getExtensionForType(com.dairum.domain.model.ModelType modelType) {
        return null;
    }
    
    /**
     * Get storage usage for models
     */
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.ai.model.StorageUsage getStorageUsage() {
        return null;
    }
    
    private final java.lang.String detectQuantization(java.lang.String fileName) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/dairum/data/ai/model/ModelManager$Companion;", "", "()V", "DEFAULT_MODELS", "", "Lcom/dairum/domain/model/AIModel;", "getDEFAULT_MODELS", "()Ljava/util/List;", "KEY_SELECTED_EMBEDDING", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "KEY_SELECTED_LLM", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.dairum.domain.model.AIModel> getDEFAULT_MODELS() {
            return null;
        }
    }
}