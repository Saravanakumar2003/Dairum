package com.dairum.data.service

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceRecorderService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaRecorder: MediaRecorder? = null
    private var currentFilePath: String? = null
    private var recordingStartTime: Long = 0
    
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()
    
    private val _recordingDuration = MutableStateFlow(0L)
    val recordingDuration: StateFlow<Long> = _recordingDuration.asStateFlow()
    
    private val voiceNotesDir: File by lazy {
        File(context.filesDir, "voice").also { it.mkdirs() }
    }

    @Suppress("DEPRECATION")
    fun startRecording(): Result<String> {
        if (_isRecording.value) {
            return Result.failure(IllegalStateException("Already recording"))
        }
        
        return try {
            val fileName = "voice_${UUID.randomUUID()}.m4a"
            val filePath = File(voiceNotesDir, fileName).absolutePath
            currentFilePath = filePath
            
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(filePath)
                prepare()
                start()
            }
            
            recordingStartTime = System.currentTimeMillis()
            _isRecording.value = true
            _recordingDuration.value = 0
            
            Result.success(filePath)
        } catch (e: Exception) {
            cleanup()
            Result.failure(e)
        }
    }

    fun stopRecording(): Result<VoiceRecordingResult> {
        if (!_isRecording.value) {
            return Result.failure(IllegalStateException("Not recording"))
        }
        
        return try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            
            val duration = System.currentTimeMillis() - recordingStartTime
            val filePath = currentFilePath!!
            val file = File(filePath)
            
            val result = VoiceRecordingResult(
                filePath = filePath,
                fileName = file.name,
                duration = duration,
                fileSize = file.length()
            )
            
            cleanup()
            Result.success(result)
        } catch (e: Exception) {
            cleanup()
            Result.failure(e)
        }
    }

    fun cancelRecording() {
        if (!_isRecording.value) return
        
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            
            // Delete the partial recording
            currentFilePath?.let { File(it).delete() }
        } catch (e: Exception) {
            // Ignore errors during cancellation
        } finally {
            cleanup()
        }
    }
    
    fun updateDuration() {
        if (_isRecording.value) {
            _recordingDuration.value = System.currentTimeMillis() - recordingStartTime
        }
    }

    private fun cleanup() {
        mediaRecorder = null
        currentFilePath = null
        _isRecording.value = false
        _recordingDuration.value = 0
        recordingStartTime = 0
    }

    fun getMaxAmplitude(): Int {
        return try {
            mediaRecorder?.maxAmplitude ?: 0
        } catch (e: Exception) {
            0
        }
    }
}

data class VoiceRecordingResult(
    val filePath: String,
    val fileName: String,
    val duration: Long,
    val fileSize: Long
)
