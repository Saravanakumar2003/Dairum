package com.dairum.data.ai.embedding

import android.content.Context
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.nio.LongBuffer
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for generating text embeddings
 */
interface EmbeddingService {
    /**
     * Check if the embedding model is loaded
     */
    fun isModelLoaded(): Boolean
    
    /**
     * Load the embedding model from a file path
     */
    suspend fun loadModel(modelPath: String): Result<Unit>
    
    /**
     * Load the default bundled embedding model
     */
    suspend fun loadDefaultModel(): Result<Unit>
    
    /**
     * Unload the current model to free memory
     */
    suspend fun unloadModel()
    
    /**
     * Generate embedding for a single text
     */
    suspend fun generateEmbedding(text: String): Result<FloatArray>
    
    /**
     * Generate embeddings for multiple texts (batch processing)
     */
    suspend fun generateEmbeddings(texts: List<String>): Result<List<FloatArray>>
    
    /**
     * Get the embedding dimension of the current model
     */
    fun getEmbeddingDimension(): Int
    
    /**
     * Get the model name
     */
    fun getModelName(): String
}

/**
 * ONNX Runtime based embedding service using MiniLM or similar models
 */
@Singleton
class ONNXEmbeddingService @Inject constructor(
    private val context: Context
) : EmbeddingService {
    
    private var ortEnvironment: OrtEnvironment? = null
    private var ortSession: OrtSession? = null
    private var isLoaded = false
    private var currentModelPath: String? = null
    private var embeddingDimension: Int = 384 // Default for MiniLM
    private var modelName: String = "unknown"
    
    private val mutex = Mutex()
    
    companion object {
        private const val TAG = "ONNXEmbeddingService"
        private const val MAX_SEQUENCE_LENGTH = 256
        private const val DEFAULT_MODEL_NAME = "all-MiniLM-L6-v2"
        private const val DEFAULT_EMBEDDING_DIM = 384
        
        // Special tokens for BERT-style models
        private const val PAD_TOKEN_ID = 0L
        private const val UNK_TOKEN_ID = 100L   // [UNK] token
        private const val CLS_TOKEN_ID = 101L   // [CLS] token
        private const val SEP_TOKEN_ID = 102L   // [SEP] token
        
        // Vocabulary size for BERT-based models (e.g., MiniLM uses BERT vocab)
        // Valid token IDs are 0 to VOCAB_SIZE-1
        private const val VOCAB_SIZE = 30522
        
        // Common word to token ID mapping for most frequent English words
        // This is a simplified vocabulary - real tokenizers use WordPiece
        private val COMMON_WORDS = mapOf(
            "the" to 1996L, "a" to 1037L, "is" to 2003L, "it" to 2009L, "i" to 1045L,
            "to" to 2000L, "and" to 1998L, "of" to 1997L, "was" to 2001L, "that" to 2008L,
            "in" to 1999L, "for" to 2005L, "on" to 2006L, "with" to 2007L, "as" to 2004L,
            "at" to 2012L, "be" to 2022L, "this" to 2023L, "have" to 2031L, "from" to 2013L,
            "or" to 2030L, "an" to 2019L, "by" to 2011L, "not" to 2025L, "but" to 2021L,
            "what" to 2054L, "all" to 2035L, "were" to 2020L, "we" to 2057L, "when" to 2043L,
            "your" to 2115L, "can" to 2064L, "there" to 2045L, "use" to 2224L, "each" to 2169L,
            "which" to 2029L, "do" to 2079L, "how" to 2129L, "their" to 2037L, "if" to 2065L,
            "will" to 2097L, "up" to 2039L, "other" to 2060L, "about" to 2055L, "out" to 2041L,
            "many" to 2116L, "then" to 2059L, "them" to 2068L, "these" to 2122L, "so" to 2061L,
            "some" to 2070L, "her" to 2014L, "would" to 2052L, "make" to 2191L, "him" to 2032L,
            "into" to 2046L, "time" to 2051L, "has" to 2038L, "look" to 2298L, "more" to 2062L,
            "go" to 2175L, "come" to 2272L, "could" to 2071L, "my" to 2026L, "than" to 2084L,
            "first" to 2034L, "been" to 2042L, "its" to 2049L, "who" to 2040L, "now" to 2085L,
            "people" to 2111L, "made" to 2081L, "over" to 2058L, "did" to 2106L, "down" to 2091L,
            "only" to 2069L, "way" to 2126L, "find" to 2424L, "day" to 2154L, "get" to 2131L,
            "just" to 2074L, "new" to 2047L, "take" to 2202L, "know" to 2113L, "back" to 2067L,
            "good" to 2204L, "man" to 2158L, "year" to 2095L, "after" to 2044L, "think" to 2228L,
            "also" to 2036L, "give" to 2507L, "most" to 2087L, "us" to 2149L, "where" to 2073L,
            "here" to 2182L, "say" to 2360L, "she" to 2016L, "he" to 2002L, "me" to 2033L,
            "even" to 2130L, "any" to 2151L, "today" to 2651L, "feel" to 2514L, "work" to 2147L,
            "life" to 2166L, "love" to 2293L, "great" to 2307L, "well" to 2092L, "because" to 2138L,
            "very" to 2200L, "see" to 2156L, "really" to 2428L, "like" to 2066L, "want" to 2215L,
            "being" to 2108L, "still" to 2145L, "need" to 2342L, "should" to 2323L, "call" to 2655L,
            "going" to 2183L, "try" to 3046L, "thing" to 2518L, "through" to 2083L, "long" to 2146L,
            "much" to 2172L, "before" to 2077L, "never" to 2196L, "world" to 2088L, "own" to 2219L,
            "last" to 2197L, "same" to 2168L, "another" to 2178L, "place" to 2173L, "around" to 2105L,
            "thought" to 2245L, "help" to 2393L, "always" to 2467L, "keep" to 2562L, "start" to 2707L
        )
    }
    
    override fun isModelLoaded(): Boolean = isLoaded
    
    override suspend fun loadModel(modelPath: String): Result<Unit> = withContext(Dispatchers.IO) {
        mutex.withLock {
            try {
                Timber.d("Loading embedding model from: $modelPath")
                
                // Unload previous model if loaded
                if (isLoaded) {
                    unloadModelInternal()
                }
                
                val modelFile = File(modelPath)
                if (!modelFile.exists()) {
                    return@withContext Result.failure(Exception("Model file not found: $modelPath"))
                }
                
                ortEnvironment = OrtEnvironment.getEnvironment()
                val sessionOptions = OrtSession.SessionOptions().apply {
                    // Use NNAPI for hardware acceleration on Android
                    // addNnapi() // Uncomment when NNAPI is available
                    setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT)
                    setIntraOpNumThreads(4)
                }
                
                ortSession = ortEnvironment?.createSession(modelPath, sessionOptions)
                currentModelPath = modelPath
                isLoaded = true
                modelName = modelFile.nameWithoutExtension
                
                // Detect embedding dimension from model output
                ortSession?.outputNames?.firstOrNull()?.let { outputName ->
                    ortSession?.outputInfo?.get(outputName)?.let { info ->
                        // Try to get dimension from output shape
                        // Default to 384 for MiniLM-like models
                        embeddingDimension = DEFAULT_EMBEDDING_DIM
                    }
                }
                
                Timber.d("Embedding model loaded successfully: $modelName, dimension: $embeddingDimension")
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load embedding model")
                unloadModelInternal()
                Result.failure(e)
            }
        }
    }
    
    override suspend fun loadDefaultModel(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Copy default model from assets to internal storage if not exists
            val modelDir = File(context.filesDir, "models/embedding")
            modelDir.mkdirs()
            
            val modelFile = File(modelDir, "minilm.onnx")
            
            if (!modelFile.exists()) {
                // Check if model exists in assets
                try {
                    context.assets.open("models/minilm.onnx").use { input ->
                        modelFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    Timber.d("Copied default embedding model from assets")
                } catch (e: Exception) {
                    Timber.w("Default embedding model not found in assets, needs to be downloaded")
                    return@withContext Result.failure(
                        Exception("Default embedding model not found. Please download a model first.")
                    )
                }
            }
            
            loadModel(modelFile.absolutePath)
        } catch (e: Exception) {
            Timber.e(e, "Failed to load default embedding model")
            Result.failure(e)
        }
    }
    
    override suspend fun unloadModel() {
        mutex.withLock {
            unloadModelInternal()
        }
    }
    
    private fun unloadModelInternal() {
        try {
            ortSession?.close()
            ortEnvironment?.close()
        } catch (e: Exception) {
            Timber.e(e, "Error closing ONNX session")
        }
        ortSession = null
        ortEnvironment = null
        isLoaded = false
        currentModelPath = null
    }
    
    override suspend fun generateEmbedding(text: String): Result<FloatArray> = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (!isLoaded || ortSession == null || ortEnvironment == null) {
                return@withContext Result.failure(Exception("Model not loaded"))
            }
            
            try {
                val embedding = runInference(listOf(text)).first()
                Result.success(embedding)
            } catch (e: Exception) {
                Timber.e(e, "Failed to generate embedding")
                Result.failure(e)
            }
        }
    }
    
    override suspend fun generateEmbeddings(texts: List<String>): Result<List<FloatArray>> = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (!isLoaded || ortSession == null || ortEnvironment == null) {
                return@withContext Result.failure(Exception("Model not loaded"))
            }
            
            try {
                val embeddings = runInference(texts)
                Result.success(embeddings)
            } catch (e: Exception) {
                Timber.e(e, "Failed to generate embeddings")
                Result.failure(e)
            }
        }
    }
    
    private fun runInference(texts: List<String>): List<FloatArray> {
        val env = ortEnvironment ?: throw Exception("ORT Environment not initialized")
        val session = ortSession ?: throw Exception("ORT Session not initialized")
        
        val batchSize = texts.size
        
        // Tokenize inputs (simplified tokenization - in production, use actual tokenizer)
        val (inputIds, attentionMask, tokenTypeIds) = tokenize(texts)
        
        // Create input tensors
        val inputIdsTensor = OnnxTensor.createTensor(
            env,
            LongBuffer.wrap(inputIds),
            longArrayOf(batchSize.toLong(), MAX_SEQUENCE_LENGTH.toLong())
        )
        
        val attentionMaskTensor = OnnxTensor.createTensor(
            env,
            LongBuffer.wrap(attentionMask),
            longArrayOf(batchSize.toLong(), MAX_SEQUENCE_LENGTH.toLong())
        )
        
        val tokenTypeIdsTensor = OnnxTensor.createTensor(
            env,
            LongBuffer.wrap(tokenTypeIds),
            longArrayOf(batchSize.toLong(), MAX_SEQUENCE_LENGTH.toLong())
        )
        
        // Prepare inputs map
        val inputs = mapOf(
            "input_ids" to inputIdsTensor,
            "attention_mask" to attentionMaskTensor,
            "token_type_ids" to tokenTypeIdsTensor
        )
        
        // Run inference
        val results = session.run(inputs)
        
        // Extract embeddings from output
        // For sentence transformers, we typically use mean pooling of token embeddings
        val embeddings = mutableListOf<FloatArray>()
        
        results.use { output ->
            // Get the output tensor (usually "last_hidden_state" or "sentence_embedding")
            val outputTensor = output[0]?.value
            
            when (outputTensor) {
                is Array<*> -> {
                    // Handle 3D output [batch, sequence, hidden] - need to mean pool
                    @Suppress("UNCHECKED_CAST")
                    val tensor3d = outputTensor as Array<Array<FloatArray>>
                    for (batchIdx in 0 until batchSize) {
                        val embedding = meanPool(tensor3d[batchIdx], attentionMask, batchIdx)
                        embeddings.add(normalize(embedding))
                    }
                }
                is FloatArray -> {
                    // Handle 2D output [batch, hidden] - direct embeddings
                    // This shouldn't normally happen but handle it anyway
                    embeddings.add(normalize(outputTensor))
                }
                else -> {
                    throw Exception("Unexpected output tensor type: ${outputTensor?.javaClass}")
                }
            }
        }
        
        // Clean up tensors
        inputIdsTensor.close()
        attentionMaskTensor.close()
        tokenTypeIdsTensor.close()
        
        return embeddings
    }
    
    /**
     * Simplified tokenization - in production, use actual BPE tokenizer
     * Tokenize texts using a simplified approach.
     * Uses known common words from BERT vocabulary, with hash-based IDs for others.
     * For production, integrate a proper WordPiece tokenizer.
     */
    private fun tokenize(texts: List<String>): Triple<LongArray, LongArray, LongArray> {
        val batchSize = texts.size
        val inputIds = LongArray(batchSize * MAX_SEQUENCE_LENGTH) { PAD_TOKEN_ID }
        val attentionMask = LongArray(batchSize * MAX_SEQUENCE_LENGTH) { 0L }
        val tokenTypeIds = LongArray(batchSize * MAX_SEQUENCE_LENGTH) { 0L }
        
        texts.forEachIndexed { batchIdx, text ->
            val offset = batchIdx * MAX_SEQUENCE_LENGTH
            
            // Add CLS token
            inputIds[offset] = CLS_TOKEN_ID
            attentionMask[offset] = 1L
            
            // Tokenize text - convert to lowercase and split on non-alphanumeric
            val words = text.lowercase()
                .replace(Regex("[^a-z0-9\\s']"), " ")
                .split(Regex("\\s+"))
                .filter { it.isNotBlank() }
            
            var position = 1
            for (word in words) {
                if (position >= MAX_SEQUENCE_LENGTH - 1) break
                
                // Look up token ID - try common words first, then use bounded hash
                val tokenId = COMMON_WORDS[word] ?: run {
                    // For unknown words, use a hash within valid vocabulary range
                    // Reserve IDs 999-30000 for hashed tokens (avoiding special tokens)
                    val hash = word.hashCode().toLong() and 0x7FFFFFFF
                    (hash % (VOCAB_SIZE - 1000)) + 999
                }
                
                // Ensure token ID is within valid range
                val safeTokenId = tokenId.coerceIn(0, VOCAB_SIZE - 1L)
                
                inputIds[offset + position] = safeTokenId
                attentionMask[offset + position] = 1L
                position++
            }
            
            // Add SEP token
            if (position < MAX_SEQUENCE_LENGTH) {
                inputIds[offset + position] = SEP_TOKEN_ID
                attentionMask[offset + position] = 1L
            }
        }
        
        return Triple(inputIds, attentionMask, tokenTypeIds)
    }
    
    /**
     * Mean pooling over non-padded tokens
     */
    private fun meanPool(tokenEmbeddings: Array<FloatArray>, attentionMask: LongArray, batchIdx: Int): FloatArray {
        val hiddenSize = tokenEmbeddings[0].size
        val result = FloatArray(hiddenSize)
        val offset = batchIdx * MAX_SEQUENCE_LENGTH
        
        var count = 0
        for (tokenIdx in tokenEmbeddings.indices) {
            if (attentionMask[offset + tokenIdx] == 1L) {
                for (i in 0 until hiddenSize) {
                    result[i] += tokenEmbeddings[tokenIdx][i]
                }
                count++
            }
        }
        
        if (count > 0) {
            for (i in 0 until hiddenSize) {
                result[i] = result[i] / count.toFloat()
            }
        }
        
        return result
    }
    
    /**
     * L2 normalize the embedding
     */
    private fun normalize(embedding: FloatArray): FloatArray {
        var norm = 0f
        for (value in embedding) {
            norm += value * value
        }
        norm = kotlin.math.sqrt(norm)
        
        if (norm > 0) {
            for (i in embedding.indices) {
                embedding[i] /= norm
            }
        }
        
        return embedding
    }
    
    override fun getEmbeddingDimension(): Int = embeddingDimension
    
    override fun getModelName(): String = modelName
}
