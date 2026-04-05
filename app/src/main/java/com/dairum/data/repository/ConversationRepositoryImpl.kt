package com.dairum.data.repository

import com.dairum.data.local.dao.*
import com.dairum.data.local.entity.*
import com.dairum.domain.model.*
import com.dairum.domain.repository.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ChatSessionRepository
 */
@Singleton
class ChatSessionRepositoryImpl @Inject constructor(
    private val chatSessionDao: ChatSessionDao,
    private val chatMessageDao: ChatMessageDao
) : ChatSessionRepository {
    
    override fun getAllSessions(): Flow<List<ChatSession>> {
        return chatSessionDao.getAllSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getSavedSessions(): Flow<List<ChatSession>> {
        return chatSessionDao.getSavedSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getActiveSessions(): Flow<List<ChatSession>> {
        return chatSessionDao.getActiveSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getSessionById(sessionId: String): ChatSession? {
        return chatSessionDao.getSessionById(sessionId)?.toDomain()
    }
    
    override fun observeSession(sessionId: String): Flow<ChatSession?> {
        return chatSessionDao.observeSession(sessionId).map { it?.toDomain() }
    }
    
    override suspend fun getSessionWithMessages(sessionId: String): ChatSessionWithMessages? {
        return chatSessionDao.getSessionWithMessages(sessionId)?.let { relation ->
            ChatSessionWithMessages(
                session = relation.session.toDomain(),
                messages = relation.messages.map { it.toDomain() }
            )
        }
    }
    
    override fun observeSessionWithMessages(sessionId: String): Flow<ChatSessionWithMessages?> {
        return chatSessionDao.observeSessionWithMessages(sessionId).map { relation ->
            relation?.let {
                ChatSessionWithMessages(
                    session = it.session.toDomain(),
                    messages = it.messages.map { msg -> msg.toDomain() }
                )
            }
        }
    }
    
    override suspend fun createSession(session: ChatSession) {
        chatSessionDao.insertSession(session.toEntity())
    }
    
    override suspend fun updateSession(session: ChatSession) {
        chatSessionDao.updateSession(session.toEntity())
    }
    
    override suspend fun updateSessionTitle(sessionId: String, title: String) {
        chatSessionDao.updateSessionTitle(sessionId, title, LocalDateTime.now())
    }
    
    override suspend fun toggleSaveStatus(sessionId: String, isSaved: Boolean) {
        chatSessionDao.updateSavedStatus(sessionId, isSaved, LocalDateTime.now())
    }
    
    override suspend fun archiveSession(sessionId: String) {
        chatSessionDao.updateArchivedStatus(sessionId, true, LocalDateTime.now())
    }
    
    override suspend fun deleteSession(sessionId: String) {
        chatSessionDao.deleteSessionById(sessionId)
    }
    
    override suspend fun deleteOldArchivedSessions(olderThan: LocalDateTime) {
        chatSessionDao.deleteOldArchivedSessions(olderThan)
    }
    
    override suspend fun getSessionCount(): Int {
        return chatSessionDao.getSessionCount()
    }
    
    // Extension functions for mapping
    private fun ChatSessionEntity.toDomain() = ChatSession(
        id = id,
        title = title,
        summary = summary,
        isSaved = isSaved,
        isArchived = isArchived,
        messageCount = messageCount,
        lastMessageAt = lastMessageAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    
    private fun ChatSession.toEntity() = ChatSessionEntity(
        id = id,
        title = title,
        summary = summary,
        isSaved = isSaved,
        isArchived = isArchived,
        messageCount = messageCount,
        lastMessageAt = lastMessageAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    
    private fun ChatMessageEntity.toDomain() = ChatMessage(
        id = id,
        sessionId = sessionId,
        role = MessageRole.valueOf(role.uppercase()),
        content = content,
        tokensUsed = tokensUsed,
        generationTimeMs = generationTimeMs,
        isEdited = isEdited,
        metadata = null, // No metadata parsing in session repository
        createdAt = createdAt
    )
}

/**
 * Implementation of ChatMessageRepository
 */
@Singleton
class ChatMessageRepositoryImpl @Inject constructor(
    private val chatMessageDao: ChatMessageDao,
    private val chatSessionDao: ChatSessionDao,
    private val moshi: Moshi
) : ChatMessageRepository {
    
    private val metadataAdapter = moshi.adapter(MessageMetadata::class.java)
    
    override fun getMessagesForSession(sessionId: String): Flow<List<ChatMessage>> {
        return chatMessageDao.getMessagesForSession(sessionId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getMessagesForSessionList(sessionId: String): List<ChatMessage> {
        return chatMessageDao.getMessagesForSessionList(sessionId).map { it.toDomain() }
    }
    
    override suspend fun getRecentMessages(sessionId: String, limit: Int): List<ChatMessage> {
        return chatMessageDao.getRecentMessages(sessionId, limit)
            .reversed() // Return in chronological order
            .map { it.toDomain() }
    }
    
    override suspend fun getLastNMessages(sessionId: String, limit: Int): List<ChatMessage> {
        return chatMessageDao.getLastNMessages(sessionId, limit).map { it.toDomain() }
    }
    
    override suspend fun getMessageById(messageId: Long): ChatMessage? {
        return chatMessageDao.getMessageById(messageId)?.toDomain()
    }
    
    override suspend fun addMessage(message: ChatMessage): Long {
        val id = chatMessageDao.insertMessage(message.toEntity())
        // Update session message count and last message time
        chatSessionDao.incrementMessageCount(
            message.sessionId,
            message.createdAt,
            LocalDateTime.now()
        )
        return id
    }
    
    override suspend fun addMessages(messages: List<ChatMessage>) {
        chatMessageDao.insertMessages(messages.map { it.toEntity() })
    }
    
    override suspend fun updateMessageContent(messageId: Long, content: String) {
        chatMessageDao.updateMessageContent(messageId, content)
    }
    
    override suspend fun deleteMessage(messageId: Long) {
        chatMessageDao.deleteMessageById(messageId)
    }
    
    override suspend fun deleteMessagesForSession(sessionId: String) {
        chatMessageDao.deleteMessagesForSession(sessionId)
    }
    
    override suspend fun getMessageCountForSession(sessionId: String): Int {
        return chatMessageDao.getMessageCountForSession(sessionId)
    }
    
    override suspend fun getTotalTokensForSession(sessionId: String): Int {
        return chatMessageDao.getTotalTokensForSession(sessionId) ?: 0
    }
    
    private fun ChatMessageEntity.toDomain() = ChatMessage(
        id = id,
        sessionId = sessionId,
        role = MessageRole.valueOf(role.uppercase()),
        content = content,
        tokensUsed = tokensUsed,
        generationTimeMs = generationTimeMs,
        isEdited = isEdited,
        metadata = metadata?.let { 
            try {
                metadataAdapter.fromJson(it)
            } catch (e: Exception) {
                null
            }
        },
        createdAt = createdAt
    )
    
    private fun ChatMessage.toEntity() = ChatMessageEntity(
        id = id,
        sessionId = sessionId,
        role = role.name.lowercase(),
        content = content,
        tokensUsed = tokensUsed,
        generationTimeMs = generationTimeMs,
        isEdited = isEdited,
        metadata = metadata?.let { metadataAdapter.toJson(it) },
        createdAt = createdAt
    )
}

/**
 * Implementation of ChatEmbeddingRepository
 */
@Singleton
class ChatEmbeddingRepositoryImpl @Inject constructor(
    private val chatEmbeddingDao: ChatEmbeddingDao
) : ChatEmbeddingRepository {
    
    override suspend fun getEmbeddingForMessage(messageId: Long): FloatArray? {
        return chatEmbeddingDao.getEmbeddingForMessage(messageId)?.let {
            ChatEmbeddingEntity.byteArrayToFloatArray(it.embedding)
        }
    }
    
    override suspend fun getEmbeddingsForSession(sessionId: String): Map<Long, FloatArray> {
        return chatEmbeddingDao.getEmbeddingsForSession(sessionId).associate {
            it.messageId to ChatEmbeddingEntity.byteArrayToFloatArray(it.embedding)
        }
    }
    
    override suspend fun getAllEmbeddings(): Map<Long, FloatArray> {
        return chatEmbeddingDao.getAllEmbeddings().associate {
            it.messageId to ChatEmbeddingEntity.byteArrayToFloatArray(it.embedding)
        }
    }
    
    override suspend fun saveEmbedding(messageId: Long, sessionId: String, embedding: FloatArray, model: String) {
        chatEmbeddingDao.insertEmbedding(
            ChatEmbeddingEntity(
                messageId = messageId,
                sessionId = sessionId,
                embedding = ChatEmbeddingEntity.floatArrayToByteArray(embedding),
                embeddingDimension = embedding.size,
                model = model
            )
        )
    }
    
    override suspend fun saveEmbeddings(embeddings: List<Triple<Long, String, FloatArray>>, model: String) {
        chatEmbeddingDao.insertEmbeddings(
            embeddings.map { (messageId, sessionId, embedding) ->
                ChatEmbeddingEntity(
                    messageId = messageId,
                    sessionId = sessionId,
                    embedding = ChatEmbeddingEntity.floatArrayToByteArray(embedding),
                    embeddingDimension = embedding.size,
                    model = model
                )
            }
        )
    }
    
    override suspend fun deleteEmbeddingForMessage(messageId: Long) {
        chatEmbeddingDao.deleteEmbeddingForMessage(messageId)
    }
    
    override suspend fun deleteEmbeddingsForSession(sessionId: String) {
        chatEmbeddingDao.deleteEmbeddingsForSession(sessionId)
    }
    
    override suspend fun deleteObsoleteEmbeddings(currentModel: String) {
        chatEmbeddingDao.deleteObsoleteEmbeddings(currentModel)
    }
    
    override suspend fun getEmbeddingCount(): Int {
        return chatEmbeddingDao.getEmbeddingCount()
    }
}

/**
 * Implementation of ReflectionMemoryRepository
 */
@Singleton
class ReflectionMemoryRepositoryImpl @Inject constructor(
    private val reflectionMemoryDao: ReflectionMemoryDao,
    private val moshi: Moshi
) : ReflectionMemoryRepository {
    
    private val longListAdapter = moshi.adapter<List<Long>>(
        Types.newParameterizedType(List::class.java, Long::class.javaObjectType)
    )
    private val stringListAdapter = moshi.adapter<List<String>>(
        Types.newParameterizedType(List::class.java, String::class.java)
    )
    
    override fun getActiveReflections(): Flow<List<ReflectionMemory>> {
        return reflectionMemoryDao.getActiveReflections().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getActiveReflectionsList(): List<ReflectionMemory> {
        return reflectionMemoryDao.getActiveReflectionsList().map { it.toDomain() }
    }
    
    override suspend fun getReflectionsWithEmbeddings(): List<ReflectionMemory> {
        return reflectionMemoryDao.getReflectionsWithEmbeddings().map { it.toDomain() }
    }
    
    override fun getReflectionsByCategory(category: ReflectionCategory): Flow<List<ReflectionMemory>> {
        return reflectionMemoryDao.getReflectionsByCategory(category.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getReflectionsByCategoryList(category: ReflectionCategory): List<ReflectionMemory> {
        return reflectionMemoryDao.getReflectionsByCategoryList(category.name).map { it.toDomain() }
    }
    
    override suspend fun getReflectionById(id: Long): ReflectionMemory? {
        return reflectionMemoryDao.getReflectionById(id)?.toDomain()
    }
    
    override suspend fun getRecentReflections(limit: Int): List<ReflectionMemory> {
        return reflectionMemoryDao.getRecentReflections(limit).map { it.toDomain() }
    }
    
    override suspend fun getHighConfidenceReflections(minConfidence: Float): List<ReflectionMemory> {
        return reflectionMemoryDao.getHighConfidenceReflections(minConfidence).map { it.toDomain() }
    }
    
    override suspend fun saveReflection(reflection: ReflectionMemory): Long {
        return reflectionMemoryDao.insertReflection(reflection.toEntity())
    }
    
    override suspend fun saveReflections(reflections: List<ReflectionMemory>) {
        reflectionMemoryDao.insertReflections(reflections.map { it.toEntity() })
    }
    
    override suspend fun updateReflection(reflection: ReflectionMemory) {
        reflectionMemoryDao.updateReflection(reflection.toEntity())
    }
    
    override suspend fun updateLastReferenced(id: Long) {
        reflectionMemoryDao.updateLastReferenced(id, LocalDateTime.now())
    }
    
    override suspend fun incrementOccurrenceCount(id: Long) {
        reflectionMemoryDao.incrementOccurrenceCount(id, LocalDateTime.now())
    }
    
    override suspend fun updateActiveStatus(id: Long, isActive: Boolean) {
        reflectionMemoryDao.updateActiveStatus(id, isActive, LocalDateTime.now())
    }
    
    override suspend fun updateEmbedding(id: Long, embedding: FloatArray, model: String) {
        reflectionMemoryDao.updateEmbedding(
            id,
            ChatEmbeddingEntity.floatArrayToByteArray(embedding),
            model,
            LocalDateTime.now()
        )
    }
    
    override suspend fun deleteReflection(id: Long) {
        reflectionMemoryDao.deleteReflectionById(id)
    }
    
    override suspend fun deleteWeakReflections(minConfidence: Float, minOccurrences: Int) {
        reflectionMemoryDao.deleteWeakReflections(minConfidence, minOccurrences)
    }
    
    override suspend fun getActiveReflectionCount(): Int {
        return reflectionMemoryDao.getActiveReflectionCount()
    }
    
    override suspend fun getReflectionsByCategories(categories: List<ReflectionCategory>, limit: Int): List<ReflectionMemory> {
        return reflectionMemoryDao.getReflectionsByCategories(
            categories.map { it.name },
            limit
        ).map { it.toDomain() }
    }
    
    private fun ReflectionMemoryEntity.toDomain() = ReflectionMemory(
        id = id,
        content = content,
        category = ReflectionCategory.valueOf(category),
        confidence = confidence,
        sourceMessageIds = sourceMessageIds?.let { 
            try { longListAdapter.fromJson(it) } catch (e: Exception) { emptyList() }
        } ?: emptyList(),
        sourceSessionIds = sourceSessionIds?.let {
            try { stringListAdapter.fromJson(it) } catch (e: Exception) { emptyList() }
        } ?: emptyList(),
        occurrenceCount = occurrenceCount,
        lastReferenced = lastReferenced,
        isActive = isActive,
        embedding = embedding?.let { ChatEmbeddingEntity.byteArrayToFloatArray(it) },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    
    private fun ReflectionMemory.toEntity() = ReflectionMemoryEntity(
        id = id,
        content = content,
        category = category.name,
        confidence = confidence,
        sourceMessageIds = longListAdapter.toJson(sourceMessageIds),
        sourceSessionIds = stringListAdapter.toJson(sourceSessionIds),
        occurrenceCount = occurrenceCount,
        lastReferenced = lastReferenced,
        isActive = isActive,
        embedding = embedding?.let { ChatEmbeddingEntity.floatArrayToByteArray(it) },
        embeddingModel = null,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

/**
 * Implementation of ConversationContextRepository
 */
@Singleton
class ConversationContextRepositoryImpl @Inject constructor(
    private val conversationContextDao: ConversationContextDao,
    private val moshi: Moshi
) : ConversationContextRepository {
    
    private val stringListAdapter = moshi.adapter<List<String>>(
        Types.newParameterizedType(List::class.java, String::class.java)
    )
    private val stringMapAdapter = moshi.adapter<Map<String, String>>(
        Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
    )
    
    override suspend fun getContextForSession(sessionId: String): ConversationContext? {
        return conversationContextDao.getContextForSession(sessionId)?.toDomain()
    }
    
    override fun observeContextForSession(sessionId: String): Flow<ConversationContext?> {
        return conversationContextDao.observeContextForSession(sessionId).map { it?.toDomain() }
    }
    
    override suspend fun saveContext(context: ConversationContext) {
        conversationContextDao.insertContext(context.toEntity())
    }
    
    override suspend fun updateTopics(sessionId: String, topics: List<String>) {
        conversationContextDao.updateTopics(
            sessionId,
            stringListAdapter.toJson(topics),
            LocalDateTime.now()
        )
    }
    
    override suspend fun updateEmotionalTone(sessionId: String, tone: String) {
        conversationContextDao.updateEmotionalTone(sessionId, tone, LocalDateTime.now())
    }
    
    override suspend fun updateDiscussionDepth(sessionId: String, depth: Int) {
        conversationContextDao.updateDiscussionDepth(sessionId, depth, LocalDateTime.now())
    }
    
    override suspend fun updateLastSummaryTime(sessionId: String, time: LocalDateTime) {
        conversationContextDao.updateLastSummaryTime(sessionId, time, LocalDateTime.now())
    }
    
    override suspend fun deleteContextForSession(sessionId: String) {
        conversationContextDao.deleteContextForSession(sessionId)
    }
    
    private fun ConversationContextEntity.toDomain() = ConversationContext(
        id = id,
        sessionId = sessionId,
        currentTopics = currentTopics?.let {
            try { stringListAdapter.fromJson(it) } catch (e: Exception) { emptyList() }
        } ?: emptyList(),
        emotionalTone = emotionalTone,
        pendingQuestions = pendingQuestions?.let {
            try { stringListAdapter.fromJson(it) } catch (e: Exception) { emptyList() }
        } ?: emptyList(),
        userPreferences = userPreferences?.let {
            try { stringMapAdapter.fromJson(it) } catch (e: Exception) { emptyMap() }
        } ?: emptyMap(),
        discussionDepth = discussionDepth,
        lastSummaryAt = lastSummaryAt,
        updatedAt = updatedAt
    )
    
    private fun ConversationContext.toEntity() = ConversationContextEntity(
        id = id,
        sessionId = sessionId,
        currentTopics = stringListAdapter.toJson(currentTopics),
        emotionalTone = emotionalTone,
        pendingQuestions = stringListAdapter.toJson(pendingQuestions),
        userPreferences = stringMapAdapter.toJson(userPreferences),
        discussionDepth = discussionDepth,
        lastSummaryAt = lastSummaryAt,
        updatedAt = updatedAt
    )
}
