package com.dairum.data.ai.llm;

import android.content.Context;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.TimeoutCancellationException;
import kotlinx.coroutines.flow.Flow;
import timber.log.Timber;
import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Interface for local LLM inference
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&Jf\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\t2\b\b\u0002\u0010\u000e\u001a\u00020\u000b2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0010H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0011\u0010\u0012JX\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\u00142\u0006\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\t2\b\b\u0002\u0010\u000e\u001a\u00020\u000b2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0010H&J\n\u0010\u0015\u001a\u0004\u0018\u00010\u0016H&J\n\u0010\u0017\u001a\u0004\u0018\u00010\u0006H&J\b\u0010\u0018\u001a\u00020\u0019H&J\b\u0010\u001a\u001a\u00020\u0019H&J.\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00030\u00052\u0006\u0010\u001c\u001a\u00020\u00062\b\b\u0002\u0010\u001d\u001a\u00020\tH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u000e\u0010 \u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010!\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\""}, d2 = {"Lcom/dairum/data/ai/llm/LocalLLM;", "", "cancelGeneration", "", "generate", "Lkotlin/Result;", "", "prompt", "maxTokens", "", "temperature", "", "topP", "topK", "repeatPenalty", "stopTokens", "", "generate-eH_QyT8", "(Ljava/lang/String;IFFIFLjava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateStream", "Lkotlinx/coroutines/flow/Flow;", "getModelInfo", "Lcom/dairum/data/ai/llm/ModelInfo;", "getModelName", "isModelLoaded", "", "isNativeImplementation", "loadModel", "path", "contextSize", "loadModel-0E7RQCE", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "unloadModel", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface LocalLLM {
    
    /**
     * Check if this is the native LLM implementation (vs fallback)
     */
    public abstract boolean isNativeImplementation();
    
    /**
     * Check if a model is loaded
     */
    public abstract boolean isModelLoaded();
    
    /**
     * Get the current model name
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.String getModelName();
    
    /**
     * Unload the current model
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object unloadModel(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Generate text with streaming output
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.String> generateStream(@org.jetbrains.annotations.NotNull()
    java.lang.String prompt, int maxTokens, float temperature, float topP, int topK, float repeatPenalty, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> stopTokens);
    
    /**
     * Cancel the current generation
     */
    public abstract void cancelGeneration();
    
    /**
     * Get model info
     */
    @org.jetbrains.annotations.Nullable()
    public abstract com.dairum.data.ai.llm.ModelInfo getModelInfo();
    
    /**
     * Interface for local LLM inference
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}