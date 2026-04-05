package com.dairum.data.repository

import android.content.Context
import com.dairum.domain.model.*
import com.dairum.domain.repository.ChatExportRepository
import com.dairum.domain.repository.ChatSessionRepository
import com.dairum.domain.repository.ChatMessageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ChatExportRepository for exporting conversations.
 * Currently exports to text/JSON formats. PDF support can be added later.
 */
@Singleton
class ChatExportRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val chatSessionRepository: ChatSessionRepository,
    private val chatMessageRepository: ChatMessageRepository
) : ChatExportRepository {
    
    companion object {
        private const val TAG = "ChatExportRepository"
    }
    
    private val exportsDir: File
        get() = File(context.filesDir, "exports/chat").also { it.mkdirs() }
    
    override suspend fun exportToText(
        sessionId: String,
        options: ChatExportOptions
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val sessionWithMessages = chatSessionRepository.getSessionWithMessages(sessionId)
                ?: return@withContext Result.failure(Exception("Session not found"))
            
            val content = buildTextExport(sessionWithMessages, options)
            Result.success(content)
        } catch (e: Exception) {
            Timber.e(e, "Error exporting to text")
            Result.failure(e)
        }
    }
    
    override suspend fun exportToPdf(
        sessionId: String,
        options: ChatExportOptions
    ): Result<File> = withContext(Dispatchers.IO) {
        // For now, export as formatted text file
        // Full PDF implementation can be added if iText library is included
        try {
            val sessionWithMessages = chatSessionRepository.getSessionWithMessages(sessionId)
                ?: return@withContext Result.failure(Exception("Session not found"))
            
            val filename = "chat_${sessionId.take(8)}_${System.currentTimeMillis()}.txt"
            val file = File(exportsDir, filename)
            
            val content = buildTextExport(sessionWithMessages, options)
            file.writeText(content)
            
            Timber.d("Exported conversation to file: ${file.absolutePath}")
            Result.success(file)
        } catch (e: Exception) {
            Timber.e(e, "Error exporting conversation")
            Result.failure(e)
        }
    }
    
    override suspend fun exportToJson(
        sessionId: String,
        options: ChatExportOptions
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val sessionWithMessages = chatSessionRepository.getSessionWithMessages(sessionId)
                ?: return@withContext Result.failure(Exception("Session not found"))
            
            val session = sessionWithMessages.session
            val messages = sessionWithMessages.messages
                .filter { options.includeSystemMessages || it.role != MessageRole.SYSTEM }
            
            val json = buildString {
                appendLine("{")
                appendLine("  \"session\": {")
                appendLine("    \"id\": \"${session.id}\",")
                appendLine("    \"title\": \"${session.displayTitle.escapeJson()}\",")
                appendLine("    \"createdAt\": \"${session.createdAt}\",")
                appendLine("    \"messageCount\": ${session.messageCount}")
                appendLine("  },")
                appendLine("  \"messages\": [")
                
                messages.forEachIndexed { index, message ->
                    appendLine("    {")
                    appendLine("      \"id\": ${message.id},")
                    appendLine("      \"role\": \"${message.role.name.lowercase()}\",")
                    appendLine("      \"content\": \"${message.content.escapeJson()}\"")
                    if (options.includeTimestamps) {
                        deleteRange(length - 1, length)
                        appendLine(",")
                        appendLine("      \"timestamp\": \"${message.createdAt}\"")
                    }
                    if (options.includeMetadata && message.tokensUsed > 0) {
                        deleteRange(length - 1, length)
                        appendLine(",")
                        appendLine("      \"tokensUsed\": ${message.tokensUsed},")
                        appendLine("      \"generationTimeMs\": ${message.generationTimeMs}")
                    }
                    append("    }")
                    if (index < messages.size - 1) append(",")
                    appendLine()
                }
                
                appendLine("  ]")
                appendLine("}")
            }
            
            Result.success(json)
        } catch (e: Exception) {
            Timber.e(e, "Error exporting to JSON")
            Result.failure(e)
        }
    }
    
    override suspend fun saveExportToFile(
        content: String,
        filename: String,
        format: ChatExportFormat
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val extension = when (format) {
                ChatExportFormat.PLAIN_TEXT -> "txt"
                ChatExportFormat.MARKDOWN -> "md"
                ChatExportFormat.JSON -> "json"
                ChatExportFormat.PDF -> "pdf"
            }
            
            val file = File(exportsDir, "$filename.$extension")
            file.writeText(content)
            
            Timber.d("Saved export to: ${file.absolutePath}")
            Result.success(file)
        } catch (e: Exception) {
            Timber.e(e, "Error saving export file")
            Result.failure(e)
        }
    }
    
    private fun buildTextExport(
        sessionWithMessages: ChatSessionWithMessages,
        options: ChatExportOptions
    ): String {
        val session = sessionWithMessages.session
        val messages = sessionWithMessages.messages
            .filter { options.includeSystemMessages || it.role != MessageRole.SYSTEM }
        
        return buildString {
            appendLine("=".repeat(60))
            appendLine("Conversation: ${session.displayTitle}")
            appendLine("Date: ${session.createdAt.format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"))}")
            appendLine("Messages: ${session.messageCount}")
            appendLine("=".repeat(60))
            appendLine()
            
            messages.forEach { message ->
                val role = when (message.role) {
                    MessageRole.USER -> "You"
                    MessageRole.ASSISTANT -> "Assistant"
                    MessageRole.SYSTEM -> "System"
                }
                
                if (options.includeTimestamps) {
                    appendLine("[$role - ${message.createdAt.format(DateTimeFormatter.ofPattern("h:mm a"))}]")
                } else {
                    appendLine("[$role]")
                }
                appendLine(message.content)
                appendLine()
            }
        }
    }
    
    private fun String.escapeJson(): String {
        return this
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
