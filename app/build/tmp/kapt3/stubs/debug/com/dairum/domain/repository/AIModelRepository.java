package com.dairum.domain.repository;

import com.dairum.domain.model.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * Repository for managing AI models
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u0014\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nH&J\u0018\u0010\r\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u00a6@\u00a2\u0006\u0002\u0010\u0010J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nH&J\u0018\u0010\u0012\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0013\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n2\u0006\u0010\u000e\u001a\u00020\u000fH&J\u000e\u0010\u0016\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\fH\u00a6@\u00a2\u0006\u0002\u0010\u001aJ\u001e\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000fH\u00a6@\u00a2\u0006\u0002\u0010\u001cJ&\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010 J\u0016\u0010!\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0014\u00a8\u0006\""}, d2 = {"Lcom/dairum/domain/repository/AIModelRepository;", "", "deleteModel", "", "modelId", "", "deleteFile", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllModels", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/dairum/domain/model/AIModel;", "getDefaultModel", "modelType", "Lcom/dairum/domain/model/ModelType;", "(Lcom/dairum/domain/model/ModelType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDownloadedModels", "getModelById", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getModelsByType", "initializeDefaultModels", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveModel", "model", "(Lcom/dairum/domain/model/AIModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setAsDefault", "(Ljava/lang/String;Lcom/dairum/domain/model/ModelType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateDownloadStatus", "isDownloaded", "filePath", "(Ljava/lang/String;ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastUsed", "app_debug"})
public abstract interface AIModelRepository {
    
    /**
     * Get all available AI models
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.AIModel>> getAllModels();
    
    /**
     * Get models by type (LLM or EMBEDDING)
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.AIModel>> getModelsByType(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ModelType modelType);
    
    /**
     * Get all downloaded models
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.AIModel>> getDownloadedModels();
    
    /**
     * Get a specific model by ID
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getModelById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.AIModel> $completion);
    
    /**
     * Get the default model for a type
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDefaultModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ModelType modelType, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.AIModel> $completion);
    
    /**
     * Save a model (insert or update)
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.AIModel model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Set a model as the default for its type
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setAsDefault(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ModelType modelType, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update download status
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateDownloadStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, boolean isDownloaded, @org.jetbrains.annotations.NotNull()
    java.lang.String filePath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Update last used timestamp
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastUsed(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete a model (and optionally its file)
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteModel(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, boolean deleteFile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Initialize default models if none exist
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object initializeDefaultModels(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Repository for managing AI models
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}