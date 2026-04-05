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
 * Fallback LLM implementation when native library is not available.
 * Returns errors indicating native library is not available.
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016JZ\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u000f2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\u0014H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0015\u0010\u0016JL\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00060\u00182\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u000f2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\u0014H\u0016J\n\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\n\u0010\u001b\u001a\u0004\u0018\u00010\u0006H\u0016J\b\u0010\u001c\u001a\u00020\u0004H\u0016J\b\u0010\u001d\u001a\u00020\u0004H\u0016J,\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\b0\n2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\rH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b!\u0010\"J\u000e\u0010#\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010$R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006%"}, d2 = {"Lcom/dairum/data/ai/llm/FallbackLLM;", "Lcom/dairum/data/ai/llm/LocalLLM;", "()V", "isLoaded", "", "modelName", "", "cancelGeneration", "", "generate", "Lkotlin/Result;", "prompt", "maxTokens", "", "temperature", "", "topP", "topK", "repeatPenalty", "stopTokens", "", "generate-eH_QyT8", "(Ljava/lang/String;IFFIFLjava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateStream", "Lkotlinx/coroutines/flow/Flow;", "getModelInfo", "Lcom/dairum/data/ai/llm/ModelInfo;", "getModelName", "isModelLoaded", "isNativeImplementation", "loadModel", "path", "contextSize", "loadModel-0E7RQCE", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "unloadModel", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class FallbackLLM implements com.dairum.data.ai.llm.LocalLLM {
    private boolean isLoaded = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String modelName;
    
    @javax.inject.Inject()
    public FallbackLLM() {
        super();
    }
    
    @java.lang.Override()
    public boolean isNativeImplementation() {
        return false;
    }
    
    @java.lang.Override()
    public boolean isModelLoaded() {
        return false;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.String getModelName() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object unloadModel(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.String> generateStream(@org.jetbrains.annotations.NotNull()
    java.lang.String prompt, int maxTokens, float temperature, float topP, int topK, float repeatPenalty, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> stopTokens) {
        return null;
    }
    
    @java.lang.Override()
    public void cancelGeneration() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public com.dairum.data.ai.llm.ModelInfo getModelInfo() {
        return null;
    }
}