package com.dairum.data.ai.model

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dairum.data.ai.embedding.EmbeddingService
import com.dairum.data.ai.llm.LocalLLM
import com.dairum.domain.model.AIModel
import com.dairum.domain.model.ModelType
import com.dairum.domain.repository.AIModelRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

private val Context.aiPreferences: DataStore<Preferences> by preferencesDataStore(name = "ai_preferences")

/**
 * Model manager for handling model downloads, imports, and switching
 */
@Singleton
class ModelManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val modelRepository: AIModelRepository,
    private val embeddingService: EmbeddingService,
    private val localLLM: LocalLLM,
    private val okHttpClient: OkHttpClient
) {
    
    companion object {
        private const val TAG = "ModelManager"
        
        private val KEY_SELECTED_LLM = stringPreferencesKey("selected_llm_model")
        private val KEY_SELECTED_EMBEDDING = stringPreferencesKey("selected_embedding_model")
        
        // Default models that can be downloaded
        val DEFAULT_MODELS = listOf(
            AIModel(
                id = "tinyllama-1.1b-q4",
                name = "TinyLlama 1.1B (Q4)",
                description = "Small and fast model, good for basic tasks. 4-bit quantized.",
                filePath = "",
                sizeBytes = 650_000_000, // ~650MB
                modelType = ModelType.LLM,
                quantization = "Q4_K_M",
                contextLength = 2048,
                isDefault = true,
                isDownloaded = false,
                downloadUrl = "https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf"
            ),
            AIModel(
                id = "phi-2-q4",
                name = "Phi-2 (Q4)",
                description = "Microsoft's Phi-2, good reasoning capabilities. 4-bit quantized.",
                filePath = "",
                sizeBytes = 1_600_000_000, // ~1.6GB
                modelType = ModelType.LLM,
                quantization = "Q4_K_M",
                contextLength = 2048,
                isDefault = false,
                isDownloaded = false,
                downloadUrl = "https://huggingface.co/TheBloke/phi-2-GGUF/resolve/main/phi-2.Q4_K_M.gguf"
            ),
            // Embedding Models
            AIModel(
                id = "minilm-embedding",
                name = "MiniLM-L6 Embedding",
                description = "Lightweight sentence embedding model for semantic search. ~90MB",
                filePath = "",
                sizeBytes = 90_000_000, // ~90MB
                modelType = ModelType.EMBEDDING,
                quantization = null,
                contextLength = 256,
                isDefault = true,
                isDownloaded = false,
                downloadUrl = "https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2/resolve/main/onnx/model.onnx"
            ),
            AIModel(
                id = "bge-small-embedding",
                name = "BGE-Small",
                description = "BAAI BGE Small embedding model. Better quality, ~130MB",
                filePath = "",
                sizeBytes = 130_000_000, // ~130MB
                modelType = ModelType.EMBEDDING,
                quantization = null,
                contextLength = 512,
                isDefault = false,
                isDownloaded = false,
                downloadUrl = "https://huggingface.co/BAAI/bge-small-en-v1.5/resolve/main/onnx/model.onnx"
            ),
            AIModel(
                id = "gte-small-embedding",
                name = "GTE-Small",
                description = "Alibaba GTE Small embedding model. High quality, ~60MB",
                filePath = "",
                sizeBytes = 60_000_000, // ~60MB
                modelType = ModelType.EMBEDDING,
                quantization = null,
                contextLength = 512,
                isDefault = false,
                isDownloaded = false,
                downloadUrl = "https://huggingface.co/thenlper/gte-small/resolve/main/onnx/model.onnx"
            )
        )
    }
    
    private val modelsDir: File
        get() = File(context.filesDir, "models").also { it.mkdirs() }
    
    private val llmModelsDir: File
        get() = File(modelsDir, "llm").also { it.mkdirs() }
    
    private val embeddingModelsDir: File
        get() = File(modelsDir, "embedding").also { it.mkdirs() }
    
    /**
     * Initialize default models in the registry
     */
    suspend fun initializeDefaultModels() {
        modelRepository.initializeDefaultModels()
    }
    
    /**
     * Get the currently selected LLM model ID
     */
    fun getSelectedLLMModelId(): Flow<String?> {
        return context.aiPreferences.data.map { preferences ->
            preferences[KEY_SELECTED_LLM]
        }
    }
    
    /**
     * Get the currently selected embedding model ID
     */
    fun getSelectedEmbeddingModelId(): Flow<String?> {
        return context.aiPreferences.data.map { preferences ->
            preferences[KEY_SELECTED_EMBEDDING]
        }
    }
    
    /**
     * Set the selected LLM model
     */
    suspend fun selectLLMModel(modelId: String) {
        context.aiPreferences.edit { preferences ->
            preferences[KEY_SELECTED_LLM] = modelId
        }
        modelRepository.setAsDefault(modelId, ModelType.LLM)
        
        // Load the model
        val model = modelRepository.getModelById(modelId)
        if (model != null && model.isDownloaded) {
            localLLM.loadModel(model.filePath, model.contextLength)
        }
    }
    
    /**
     * Set the selected embedding model
     */
    suspend fun selectEmbeddingModel(modelId: String) {
        context.aiPreferences.edit { preferences ->
            preferences[KEY_SELECTED_EMBEDDING] = modelId
        }
        modelRepository.setAsDefault(modelId, ModelType.EMBEDDING)
        
        // Load the model
        val model = modelRepository.getModelById(modelId)
        if (model != null && model.isDownloaded) {
            embeddingService.loadModel(model.filePath)
        }
    }
    
    /**
     * Download a model from URL
     */
    suspend fun downloadModel(
        modelId: String,
        onProgress: (bytesDownloaded: Long, totalBytes: Long) -> Unit
    ): Result<AIModel> = withContext(Dispatchers.IO) {
        try {
            val model = modelRepository.getModelById(modelId)
                ?: return@withContext Result.failure(Exception("Model not found: $modelId"))
            
            if (model.downloadUrl.isNullOrBlank()) {
                return@withContext Result.failure(Exception("No download URL for model"))
            }
            
            val targetDir = when (model.modelType) {
                ModelType.LLM -> llmModelsDir
                ModelType.EMBEDDING -> embeddingModelsDir
            }
            
            // Use model ID + extension for unique filenames
            // This prevents conflicts when multiple models have same URL filename (e.g., model.onnx)
            val urlFileName = model.downloadUrl.substringAfterLast("/")
            val extension = urlFileName.substringAfterLast(".", "")
            val fileName = if (extension.isNotEmpty()) "${model.id}.$extension" else "${model.id}_$urlFileName"
            val targetFile = File(targetDir, fileName)
            
            Timber.d("Downloading model to: ${targetFile.absolutePath}")
            
            val request = Request.Builder()
                .url(model.downloadUrl)
                .build()
            
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("Download failed: ${response.code}"))
                }
                
                val body = response.body ?: return@withContext Result.failure(Exception("Empty response"))
                val contentLength = body.contentLength()
                
                FileOutputStream(targetFile).use { output ->
                    body.byteStream().use { input ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        var totalBytesRead = 0L
                        
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            totalBytesRead += bytesRead
                            onProgress(totalBytesRead, contentLength)
                        }
                    }
                }
            }
            
            // Update model in repository
            modelRepository.updateDownloadStatus(modelId, true, targetFile.absolutePath)
            
            val updatedModel = model.copy(
                isDownloaded = true,
                filePath = targetFile.absolutePath
            )
            
            Timber.d("Model downloaded successfully: ${targetFile.absolutePath}")
            Result.success(updatedModel)
        } catch (e: Exception) {
            Timber.e(e, "Failed to download model")
            Result.failure(e)
        }
    }
    
    /**
     * Import a model from a file URI
     */
    suspend fun importModel(
        name: String,
        description: String,
        sourceUri: String,
        modelType: ModelType
    ): Result<AIModel> = withContext(Dispatchers.IO) {
        try {
            val sourceFile = File(sourceUri)
            if (!sourceFile.exists()) {
                return@withContext Result.failure(Exception("Source file not found"))
            }
            
            val targetDir = when (modelType) {
                ModelType.LLM -> llmModelsDir
                ModelType.EMBEDDING -> embeddingModelsDir
            }
            
            val targetFile = File(targetDir, sourceFile.name)
            
            // Copy file to app storage
            sourceFile.copyTo(targetFile, overwrite = true)
            
            // Detect quantization from filename
            val quantization = detectQuantization(sourceFile.name)
            
            // Create model entry
            val modelId = "custom-${System.currentTimeMillis()}"
            val model = AIModel(
                id = modelId,
                name = name,
                description = description,
                filePath = targetFile.absolutePath,
                sizeBytes = targetFile.length(),
                modelType = modelType,
                quantization = quantization,
                contextLength = 2048,
                isDefault = false,
                isDownloaded = true,
                downloadUrl = null
            )
            
            modelRepository.saveModel(model)
            
            Timber.d("Model imported successfully: ${model.name}")
            Result.success(model)
        } catch (e: Exception) {
            Timber.e(e, "Failed to import model")
            Result.failure(e)
        }
    }
    
    /**
     * Import a model from a content URI (Android document picker)
     */
    suspend fun importModelFromUri(
        uri: Uri,
        name: String,
        modelType: ModelType
    ): Result<AIModel> = withContext(Dispatchers.IO) {
        try {
            val targetDir = when (modelType) {
                ModelType.LLM -> llmModelsDir
                ModelType.EMBEDDING -> embeddingModelsDir
            }
            
            // Ensure directory exists
            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }
            
            // Get filename from URI
            val fileName = getFileNameFromUri(uri) ?: "model_${System.currentTimeMillis()}.${getExtensionForType(modelType)}"
            val targetFile = File(targetDir, fileName)
            
            Timber.d("Importing model from URI: $uri")
            Timber.d("Target directory: ${targetDir.absolutePath}, exists: ${targetDir.exists()}")
            Timber.d("Target file: ${targetFile.absolutePath}")
            
            // Copy file from URI to app storage
            var bytesCopied = 0L
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(targetFile).use { outputStream ->
                    bytesCopied = inputStream.copyTo(outputStream, bufferSize = 8192)
                }
            } ?: return@withContext Result.failure(Exception("Could not open file"))
            
            // Verify the file was copied correctly
            if (!targetFile.exists()) {
                return@withContext Result.failure(Exception("File was not saved to ${targetFile.absolutePath}"))
            }
            
            val fileSize = targetFile.length()
            Timber.d("File copied successfully: ${targetFile.absolutePath}, size: $fileSize bytes, copied: $bytesCopied bytes")
            
            if (fileSize == 0L) {
                targetFile.delete()
                return@withContext Result.failure(Exception("Copied file is empty"))
            }
            
            // Detect quantization from filename
            val quantization = detectQuantization(fileName)
            
            // Determine description based on model type
            val description = when (modelType) {
                ModelType.LLM -> "Custom LLM model imported from device"
                ModelType.EMBEDDING -> "Custom embedding model imported from device"
            }
            
            // Create model entry
            val modelId = "imported-${System.currentTimeMillis()}"
            val model = AIModel(
                id = modelId,
                name = name.ifBlank { fileName.substringBeforeLast(".") },
                description = description,
                filePath = targetFile.absolutePath,
                sizeBytes = targetFile.length(),
                modelType = modelType,
                quantization = quantization,
                contextLength = if (modelType == ModelType.EMBEDDING) 512 else 2048,
                isDefault = false,
                isDownloaded = true,
                downloadUrl = null
            )
            
            Timber.d("Saving imported model to database:")
            Timber.d("  ID: ${model.id}")
            Timber.d("  Name: ${model.name}")
            Timber.d("  FilePath: ${model.filePath}")
            Timber.d("  Size: ${model.sizeBytes} bytes")
            Timber.d("  Type: ${model.modelType}")
            Timber.d("  IsDownloaded: ${model.isDownloaded}")
            
            modelRepository.saveModel(model)
            
            // Verify model was saved correctly
            val savedModel = modelRepository.getModelById(modelId)
            Timber.d("Verified saved model: filePath=${savedModel?.filePath}, isDownloaded=${savedModel?.isDownloaded}")
            
            Result.success(model)
        } catch (e: Exception) {
            Timber.e(e, "Failed to import model from URI")
            Result.failure(e)
        }
    }
    
    private fun getFileNameFromUri(uri: Uri): String? {
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex >= 0) {
                    cursor.getString(nameIndex)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun getExtensionForType(modelType: ModelType): String {
        return when (modelType) {
            ModelType.LLM -> "gguf"
            ModelType.EMBEDDING -> "onnx"
        }
    }
    
    /**
     * Delete a model and its file
     */
    suspend fun deleteModel(modelId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val model = modelRepository.getModelById(modelId)
            if (model != null && model.filePath.isNotBlank()) {
                val file = File(model.filePath)
                if (file.exists()) {
                    file.delete()
                }
            }
            
            modelRepository.deleteModel(modelId)
            Timber.d("Model deleted: $modelId")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete model")
            Result.failure(e)
        }
    }
    
    /**
     * Load the currently selected models
     */
    suspend fun loadSelectedModels(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Load LLM
            val llmId = getSelectedLLMModelId().first()
            if (llmId != null) {
                val llmModel = modelRepository.getModelById(llmId)
                if (llmModel != null && llmModel.isDownloaded) {
                    localLLM.loadModel(llmModel.filePath, llmModel.contextLength)
                }
            } else {
                // Try loading default
                val defaultLLM = modelRepository.getDefaultModel(ModelType.LLM)
                if (defaultLLM != null && defaultLLM.isDownloaded) {
                    localLLM.loadModel(defaultLLM.filePath, defaultLLM.contextLength)
                }
            }
            
            // Load embedding model
            val embeddingId = getSelectedEmbeddingModelId().first()
            if (embeddingId != null) {
                val embeddingModel = modelRepository.getModelById(embeddingId)
                if (embeddingModel != null && embeddingModel.isDownloaded) {
                    embeddingService.loadModel(embeddingModel.filePath)
                }
            } else {
                // Try loading default
                embeddingService.loadDefaultModel()
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to load selected models")
            Result.failure(e)
        }
    }
    
    /**
     * Get storage usage for models
     */
    fun getStorageUsage(): StorageUsage {
        val llmSize = llmModelsDir.walkTopDown()
            .filter { it.isFile }
            .sumOf { it.length() }
        
        val embeddingSize = embeddingModelsDir.walkTopDown()
            .filter { it.isFile }
            .sumOf { it.length() }
        
        return StorageUsage(
            llmModelsBytes = llmSize,
            embeddingModelsBytes = embeddingSize,
            totalBytes = llmSize + embeddingSize
        )
    }
    
    private fun detectQuantization(fileName: String): String? {
        val patterns = listOf(
            "Q2_K", "Q3_K_S", "Q3_K_M", "Q3_K_L",
            "Q4_0", "Q4_1", "Q4_K_S", "Q4_K_M",
            "Q5_0", "Q5_1", "Q5_K_S", "Q5_K_M",
            "Q6_K", "Q8_0"
        )
        
        val upperName = fileName.uppercase()
        return patterns.find { upperName.contains(it) }
    }
}

data class StorageUsage(
    val llmModelsBytes: Long,
    val embeddingModelsBytes: Long,
    val totalBytes: Long
) {
    val totalFormatted: String
        get() {
            val mb = totalBytes / (1024.0 * 1024.0)
            val gb = mb / 1024.0
            return if (gb >= 1) {
                String.format("%.1f GB", gb)
            } else {
                String.format("%.1f MB", mb)
            }
        }
}
