package com.dairum.data.ai.llm

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for local LLM inference
 */
interface LocalLLM {
    
    /**
     * Check if this is the native LLM implementation (vs fallback)
     */
    fun isNativeImplementation(): Boolean
    
    /**
     * Check if a model is loaded
     */
    fun isModelLoaded(): Boolean
    
    /**
     * Get the current model name
     */
    fun getModelName(): String?
    
    /**
     * Load a GGUF model from file path
     */
    suspend fun loadModel(path: String, contextSize: Int = 2048): Result<Unit>
    
    /**
     * Unload the current model
     */
    suspend fun unloadModel()
    
    /**
     * Generate text completion
     */
    suspend fun generate(
        prompt: String,
        maxTokens: Int = 512,
        temperature: Float = 0.7f,
        topP: Float = 0.9f,
        topK: Int = 40,
        repeatPenalty: Float = 1.1f,
        stopTokens: List<String> = emptyList()
    ): Result<String>
    
    /**
     * Generate text with streaming output
     */
    fun generateStream(
        prompt: String,
        maxTokens: Int = 512,
        temperature: Float = 0.7f,
        topP: Float = 0.9f,
        topK: Int = 40,
        repeatPenalty: Float = 1.1f,
        stopTokens: List<String> = emptyList()
    ): Flow<String>
    
    /**
     * Cancel the current generation
     */
    fun cancelGeneration()
    
    /**
     * Get model info
     */
    fun getModelInfo(): ModelInfo?
}

/**
 * Model information
 */
data class ModelInfo(
    val name: String,
    val contextLength: Int,
    val embeddingLength: Int,
    val vocabSize: Int,
    val sizeBytes: Long
)

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
@Singleton
class LlamaCppLLM @Inject constructor(
    private val context: Context
) : LocalLLM {
    
    companion object {
        private const val TAG = "LlamaCppLLM"
        
        // Track if native library loaded successfully
        @Volatile
        private var nativeLibraryLoaded = false
        
        // Load native library
        init {
            try {
                System.loadLibrary("llama-android")
                nativeLibraryLoaded = true
                Timber.d("llama-android native library loaded successfully")
            } catch (e: UnsatisfiedLinkError) {
                nativeLibraryLoaded = false
                Timber.e(e, "Failed to load llama-android native library. Run ./scripts/setup_llama.sh and rebuild.")
            }
        }
        
        fun isNativeLibraryAvailable(): Boolean = nativeLibraryLoaded
    }
    
    // JNI native methods
    private external fun nativeInit(): Long
    private external fun nativeDestroy(handle: Long)
    private external fun nativeLoadModel(handle: Long, path: String, contextSize: Int, threads: Int): Boolean
    private external fun nativeUnloadModel(handle: Long)
    private external fun nativeIsModelLoaded(handle: Long): Boolean
    private external fun nativeGenerate(
        handle: Long,
        prompt: String,
        maxTokens: Int,
        temperature: Float,
        topP: Float,
        topK: Int,
        repeatPenalty: Float,
        stopTokens: Array<String>
    ): String
    private external fun nativeGenerateToken(handle: Long): String?
    private external fun nativeStartGeneration(
        handle: Long,
        prompt: String,
        maxTokens: Int,
        temperature: Float,
        topP: Float,
        topK: Int,
        repeatPenalty: Float,
        stopTokens: Array<String>
    ): Boolean
    private external fun nativeCancelGeneration(handle: Long)
    private external fun nativeGetModelInfo(handle: Long): LongArray? // [contextLen, embeddingLen, vocabSize]
    
    private var nativeHandle: Long = 0
    private var isLoaded = false
    private var modelName: String? = null
    private var modelPath: String? = null
    private var isCancelled = false
    
    private val mutex = Mutex()
    
    init {
        if (nativeLibraryLoaded) {
            try {
                nativeHandle = nativeInit()
                Timber.d("LlamaCpp initialized with handle: $nativeHandle")
            } catch (e: Exception) {
                Timber.e(e, "Failed to initialize LlamaCpp")
                nativeHandle = 0
            }
        } else {
            Timber.w("Native library not loaded - LLM functionality unavailable")
            nativeHandle = 0
        }
    }
    
    override fun isNativeImplementation(): Boolean = nativeLibraryLoaded && nativeHandle != 0L
    
    override fun isModelLoaded(): Boolean = isLoaded && nativeHandle != 0L
    
    override fun getModelName(): String? = modelName
    
    override suspend fun loadModel(path: String, contextSize: Int): Result<Unit> = withContext(Dispatchers.IO) {
        // Check if native library is available
        if (!nativeLibraryLoaded) {
            return@withContext Result.failure(
                NativeLibraryNotAvailableException(
                    "Native LLM library is not available. Run ./scripts/setup_llama.sh and rebuild the app."
                )
            )
        }
        
        mutex.withLock {
            try {
                if (nativeHandle == 0L) {
                    nativeHandle = nativeInit()
                }
                
                val modelFile = File(path)
                if (!modelFile.exists()) {
                    return@withContext Result.failure(Exception("Model file not found: $path"))
                }
                
                // Unload previous model
                if (isLoaded) {
                    nativeUnloadModel(nativeHandle)
                    isLoaded = false
                }
                
                Timber.d("Loading GGUF model from: $path")
                
                // Determine optimal thread count (limit to 4 for mobile)
                val threads = Runtime.getRuntime().availableProcessors().coerceIn(1, 4)
                
                val success = nativeLoadModel(nativeHandle, path, contextSize, threads)
                
                if (success) {
                    isLoaded = true
                    modelPath = path
                    modelName = modelFile.nameWithoutExtension
                    Timber.d("Model loaded successfully: $modelName")
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to load model - check if the GGUF file is valid"))
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading model")
                Result.failure(e)
            }
        }
    }
    
    override suspend fun unloadModel() {
        mutex.withLock {
            if (nativeHandle != 0L && isLoaded) {
                nativeUnloadModel(nativeHandle)
                isLoaded = false
                modelName = null
                modelPath = null
                Timber.d("Model unloaded")
            }
        }
    }
    
    override suspend fun generate(
        prompt: String,
        maxTokens: Int,
        temperature: Float,
        topP: Float,
        topK: Int,
        repeatPenalty: Float,
        stopTokens: List<String>
    ): Result<String> = withContext(Dispatchers.Default) {
        if (!nativeLibraryLoaded) {
            return@withContext Result.failure(
                NativeLibraryNotAvailableException("Native LLM library not available")
            )
        }
        
        // Timeout of 90 seconds for generation - mobile devices are slow
        val timeoutMs = 90_000L
        
        try {
            withTimeout(timeoutMs) {
                mutex.withLock {
                    if (!isLoaded || nativeHandle == 0L) {
                        return@withTimeout Result.failure(Exception("Model not loaded"))
                    }
                    
                    try {
                        isCancelled = false
                        
                        Timber.d("Generating response for prompt: ${prompt.take(100)}...")
                        val startTime = System.currentTimeMillis()
                        
                        val response = nativeGenerate(
                            nativeHandle,
                            prompt,
                            maxTokens,
                            temperature,
                            topP,
                            topK,
                            repeatPenalty,
                            stopTokens.toTypedArray()
                        )
                        
                        val elapsed = System.currentTimeMillis() - startTime
                        Timber.d("Generation completed in ${elapsed}ms, ${response.length} chars")
                        
                        Result.success(response)
                    } catch (e: Exception) {
                        Timber.e(e, "Generation failed")
                        Result.failure(e)
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            Timber.w("Generation timed out after ${timeoutMs}ms")
            isCancelled = true
            Result.failure(Exception("Generation timed out. The model may be too large for this device. Try a smaller model like TinyLlama."))
        }
    }
    
    override fun generateStream(
        prompt: String,
        maxTokens: Int,
        temperature: Float,
        topP: Float,
        topK: Int,
        repeatPenalty: Float,
        stopTokens: List<String>
    ): Flow<String> = flow {
        if (!nativeLibraryLoaded) {
            throw NativeLibraryNotAvailableException("Native LLM library not available")
        }
        
        if (!isLoaded || nativeHandle == 0L) {
            throw Exception("Model not loaded")
        }
        
        isCancelled = false
        
        val success = nativeStartGeneration(
            nativeHandle,
            prompt,
            maxTokens,
            temperature,
            topP,
            topK,
            repeatPenalty,
            stopTokens.toTypedArray()
        )
        
        if (!success) {
            throw Exception("Failed to start generation")
        }
        
        while (!isCancelled) {
            val token = nativeGenerateToken(nativeHandle)
            if (token == null) {
                break // Generation complete
            }
            emit(token)
        }
    }.flowOn(Dispatchers.Default)
    
    override fun cancelGeneration() {
        isCancelled = true
        if (nativeHandle != 0L) {
            nativeCancelGeneration(nativeHandle)
        }
    }
    
    override fun getModelInfo(): ModelInfo? {
        if (!isLoaded || nativeHandle == 0L) return null
        
        return try {
            val info = nativeGetModelInfo(nativeHandle)
            if (info != null && info.size >= 3) {
                ModelInfo(
                    name = modelName ?: "Unknown",
                    contextLength = info[0].toInt(),
                    embeddingLength = info[1].toInt(),
                    vocabSize = info[2].toInt(),
                    sizeBytes = modelPath?.let { File(it).length() } ?: 0
                )
            } else null
        } catch (e: Exception) {
            Timber.e(e, "Failed to get model info")
            null
        }
    }
    
    protected fun finalize() {
        if (nativeHandle != 0L) {
            nativeDestroy(nativeHandle)
            nativeHandle = 0
        }
    }
}

/**
 * Fallback LLM implementation when native library is not available.
 * Returns errors indicating native library is not available.
 */
@Singleton
class FallbackLLM @Inject constructor() : LocalLLM {
    
    private var isLoaded = false
    private var modelName: String? = null
    
    override fun isNativeImplementation(): Boolean = false
    
    override fun isModelLoaded(): Boolean = isLoaded
    
    override fun getModelName(): String? = modelName
    
    override suspend fun loadModel(path: String, contextSize: Int): Result<Unit> {
        val modelFile = File(path)
        if (!modelFile.exists()) {
            return Result.failure(Exception("Model file not found"))
        }
        // Mark as loaded but note that native library isn't available
        isLoaded = true
        modelName = modelFile.nameWithoutExtension
        Timber.w("FallbackLLM: Model info loaded but native library (libllama-android.so) is not available")
        return Result.success(Unit)
    }
    
    override suspend fun unloadModel() {
        isLoaded = false
        modelName = null
    }
    
    override suspend fun generate(
        prompt: String,
        maxTokens: Int,
        temperature: Float,
        topP: Float,
        topK: Int,
        repeatPenalty: Float,
        stopTokens: List<String>
    ): Result<String> {
        // Return a failure indicating native library is not available
        return Result.failure(
            NativeLibraryNotAvailableException(
                "Native LLM library (libllama-android.so) is not available. " +
                "The app needs to be built with the llama.cpp native library to enable AI chat functionality. " +
                "Embedding-based search still works."
            )
        )
    }
    
    override fun generateStream(
        prompt: String,
        maxTokens: Int,
        temperature: Float,
        topP: Float,
        topK: Int,
        repeatPenalty: Float,
        stopTokens: List<String>
    ): Flow<String> = flow {
        throw NativeLibraryNotAvailableException(
            "Native LLM library is not available for streaming generation."
        )
    }
    
    override fun cancelGeneration() {
        // No-op for fallback
    }
    
    override fun getModelInfo(): ModelInfo? {
        return if (isLoaded) {
            ModelInfo(
                name = modelName ?: "Fallback (Native library unavailable)",
                contextLength = 0,
                embeddingLength = 0,
                vocabSize = 0,
                sizeBytes = 0
            )
        } else null
    }
}

/**
 * Exception thrown when native LLM library is not available
 */
class NativeLibraryNotAvailableException(message: String) : Exception(message)
