package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.*;
import kotlinx.coroutines.flow.Flow;
import java.time.LocalDateTime;

/**
 * DAO for AI model management
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\u000eH\'J\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\t0\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010\u0012\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\u000eH\'J\u0018\u0010\u0014\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0015\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\u000e2\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u0017\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u0018\u001a\u00020\u00032\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\t0\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010\u001b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J&\u0010\u001c\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010 J\u001e\u0010!\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020#H\u00a7@\u00a2\u0006\u0002\u0010$J\u0016\u0010%\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006&"}, d2 = {"Lcom/dairum/data/local/dao/AIModelDao;", "", "clearDefaultForType", "", "modelType", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteModel", "model", "Lcom/dairum/data/local/entity/AIModelEntity;", "(Lcom/dairum/data/local/entity/AIModelEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteModelById", "modelId", "getAllModels", "Lkotlinx/coroutines/flow/Flow;", "", "getAllModelsList", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDefaultModel", "getDownloadedModels", "getModelById", "id", "getModelsByType", "insertModel", "insertModels", "models", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setAsDefault", "updateDownloadStatus", "isDownloaded", "", "filePath", "(Ljava/lang/String;ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastUsed", "lastUsedAt", "Ljava/time/LocalDateTime;", "(Ljava/lang/String;Ljava/time/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateModel", "app_debug"})
@androidx.room.Dao()
public abstract interface AIModelDao {
    
    @androidx.room.Query(value = "SELECT * FROM ai_models ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.AIModelEntity>> getAllModels();
    
    @androidx.room.Query(value = "SELECT * FROM ai_models ORDER BY name ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllModelsList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.AIModelEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM ai_models WHERE modelType = :modelType ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.AIModelEntity>> getModelsByType(@org.jetbrains.annotations.NotNull()
    java.lang.String modelType);
    
    @androidx.room.Query(value = "SELECT * FROM ai_models WHERE isDownloaded = 1 ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.AIModelEntity>> getDownloadedModels();
    
    @androidx.room.Query(value = "SELECT * FROM ai_models WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getModelById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.AIModelEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM ai_models WHERE isDefault = 1 AND modelType = :modelType LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDefaultModel(@org.jetbrains.annotations.NotNull()
    java.lang.String modelType, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.AIModelEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertModel(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.AIModelEntity model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertModels(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.AIModelEntity> models, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateModel(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.AIModelEntity model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE ai_models SET isDefault = 0 WHERE modelType = :modelType")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearDefaultForType(@org.jetbrains.annotations.NotNull()
    java.lang.String modelType, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE ai_models SET isDefault = 1 WHERE id = :modelId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setAsDefault(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE ai_models SET isDownloaded = :isDownloaded, filePath = :filePath WHERE id = :modelId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateDownloadStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, boolean isDownloaded, @org.jetbrains.annotations.NotNull()
    java.lang.String filePath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE ai_models SET lastUsedAt = :lastUsedAt WHERE id = :modelId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastUsed(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime lastUsedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteModel(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.AIModelEntity model, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM ai_models WHERE id = :modelId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteModelById(@org.jetbrains.annotations.NotNull()
    java.lang.String modelId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}