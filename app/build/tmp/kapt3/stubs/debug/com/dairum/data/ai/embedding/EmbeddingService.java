package com.dairum.data.ai.embedding;

import android.content.Context;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import kotlinx.coroutines.Dispatchers;
import timber.log.Timber;
import java.io.File;
import java.nio.LongBuffer;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Interface for generating text embeddings
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0007\u0010\bJ0\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\n0\u00032\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\nH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\f\u0010\rJ\b\u0010\u000e\u001a\u00020\u000fH&J\b\u0010\u0010\u001a\u00020\u0006H&J\b\u0010\u0011\u001a\u00020\u0012H&J\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0003H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0015\u0010\u0016J$\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00140\u00032\u0006\u0010\u0018\u001a\u00020\u0006H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0019\u0010\bJ\u000e\u0010\u001a\u001a\u00020\u0014H\u00a6@\u00a2\u0006\u0002\u0010\u0016\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001b"}, d2 = {"Lcom/dairum/data/ai/embedding/EmbeddingService;", "", "generateEmbedding", "Lkotlin/Result;", "", "text", "", "generateEmbedding-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateEmbeddings", "", "texts", "generateEmbeddings-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEmbeddingDimension", "", "getModelName", "isModelLoaded", "", "loadDefaultModel", "", "loadDefaultModel-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadModel", "modelPath", "loadModel-gIAlu-s", "unloadModel", "app_debug"})
public abstract interface EmbeddingService {
    
    /**
     * Check if the embedding model is loaded
     */
    public abstract boolean isModelLoaded();
    
    /**
     * Unload the current model to free memory
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object unloadModel(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Get the embedding dimension of the current model
     */
    public abstract int getEmbeddingDimension();
    
    /**
     * Get the model name
     */
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getModelName();
}