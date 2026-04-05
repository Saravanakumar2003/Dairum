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
 * llama.cpp based LLM implementation using JNI
 *
 * This implementation uses llama.cpp through JNI bindings.
 * The native library is built automatically via CMake during the Gradle build.
 *
 * To set up:
 * 1. Run ./scripts/setup_llama.sh to clone llama.cpp
 * 2. Build the project in Android Studio (native code compiles automatically)
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0016\n\u0002\b\u000b\b\u0007\u0018\u0000 >2\u00020\u0001:\u0001>B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0010H\u0004JZ\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\u00132\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00182\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t0\u001dH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001e\u0010\u001fJL\u0010 \u001a\b\u0012\u0004\u0012\u00020\t0!2\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00182\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t0\u001dH\u0016J\n\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\n\u0010$\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010%\u001a\u00020\u0006H\u0016J\b\u0010&\u001a\u00020\u0006H\u0016J,\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00100\u00132\u0006\u0010(\u001a\u00020\t2\u0006\u0010)\u001a\u00020\u0016H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b*\u0010+J\u0011\u0010,\u001a\u00020\u00102\u0006\u0010-\u001a\u00020\u000eH\u0082 J\u0011\u0010.\u001a\u00020\u00102\u0006\u0010-\u001a\u00020\u000eH\u0082 JT\u0010/\u001a\u00020\t2\u0006\u0010-\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00182\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t00H\u0082 \u00a2\u0006\u0002\u00101J\u0013\u00102\u001a\u0004\u0018\u00010\t2\u0006\u0010-\u001a\u00020\u000eH\u0082 J\u0013\u00103\u001a\u0004\u0018\u0001042\u0006\u0010-\u001a\u00020\u000eH\u0082 J\t\u00105\u001a\u00020\u000eH\u0082 J\u0011\u00106\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u000eH\u0082 J)\u00107\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020\t2\u0006\u0010)\u001a\u00020\u00162\u0006\u00108\u001a\u00020\u0016H\u0082 JT\u00109\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00182\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t00H\u0082 \u00a2\u0006\u0002\u0010:J\u0011\u0010;\u001a\u00020\u00102\u0006\u0010-\u001a\u00020\u000eH\u0082 J\u000e\u0010<\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010=R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006?"}, d2 = {"Lcom/dairum/data/ai/llm/LlamaCppLLM;", "Lcom/dairum/data/ai/llm/LocalLLM;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "isCancelled", "", "isLoaded", "modelName", "", "modelPath", "mutex", "Lkotlinx/coroutines/sync/Mutex;", "nativeHandle", "", "cancelGeneration", "", "finalize", "generate", "Lkotlin/Result;", "prompt", "maxTokens", "", "temperature", "", "topP", "topK", "repeatPenalty", "stopTokens", "", "generate-eH_QyT8", "(Ljava/lang/String;IFFIFLjava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateStream", "Lkotlinx/coroutines/flow/Flow;", "getModelInfo", "Lcom/dairum/data/ai/llm/ModelInfo;", "getModelName", "isModelLoaded", "isNativeImplementation", "loadModel", "path", "contextSize", "loadModel-0E7RQCE", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "nativeCancelGeneration", "handle", "nativeDestroy", "nativeGenerate", "", "(JLjava/lang/String;IFFIF[Ljava/lang/String;)Ljava/lang/String;", "nativeGenerateToken", "nativeGetModelInfo", "", "nativeInit", "nativeIsModelLoaded", "nativeLoadModel", "threads", "nativeStartGeneration", "(JLjava/lang/String;IFFIF[Ljava/lang/String;)Z", "nativeUnloadModel", "unloadModel", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class LlamaCppLLM implements com.dairum.data.ai.llm.LocalLLM {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "LlamaCppLLM";
    @kotlin.jvm.Volatile()
    private static volatile boolean nativeLibraryLoaded = false;
    private long nativeHandle = 0L;
    private boolean isLoaded = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String modelName;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String modelPath;
    private boolean isCancelled = false;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.sync.Mutex mutex = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.ai.llm.LlamaCppLLM.Companion Companion = null;
    
    @javax.inject.Inject()
    public LlamaCppLLM(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final native long nativeInit() {
        return 0L;
    }
    
    private final native void nativeDestroy(long handle) {
    }
    
    private final native boolean nativeLoadModel(long handle, java.lang.String path, int contextSize, int threads) {
        return false;
    }
    
    private final native void nativeUnloadModel(long handle) {
    }
    
    private final native boolean nativeIsModelLoaded(long handle) {
        return false;
    }
    
    private final native java.lang.String nativeGenerate(long handle, java.lang.String prompt, int maxTokens, float temperature, float topP, int topK, float repeatPenalty, java.lang.String[] stopTokens) {
        return null;
    }
    
    private final native java.lang.String nativeGenerateToken(long handle) {
        return null;
    }
    
    private final native boolean nativeStartGeneration(long handle, java.lang.String prompt, int maxTokens, float temperature, float topP, int topK, float repeatPenalty, java.lang.String[] stopTokens) {
        return false;
    }
    
    private final native void nativeCancelGeneration(long handle) {
    }
    
    private final native long[] nativeGetModelInfo(long handle) {
        return null;
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
    
    protected final void finalize() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/dairum/data/ai/llm/LlamaCppLLM$Companion;", "", "()V", "TAG", "", "nativeLibraryLoaded", "", "isNativeLibraryAvailable", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean isNativeLibraryAvailable() {
            return false;
        }
    }
}