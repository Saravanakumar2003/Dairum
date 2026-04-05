package com.dairum.data.local.dao

import androidx.room.*
import com.dairum.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * DAO for chat sessions
 */
@Dao
interface ChatSessionDao {
    
    @Query("SELECT * FROM chat_sessions ORDER BY lastMessageAt DESC")
    fun getAllSessions(): Flow<List<ChatSessionEntity>>
    
    @Query("SELECT * FROM chat_sessions WHERE isSaved = 1 ORDER BY lastMessageAt DESC")
    fun getSavedSessions(): Flow<List<ChatSessionEntity>>
    
    @Query("SELECT * FROM chat_sessions WHERE isArchived = 0 ORDER BY lastMessageAt DESC")
    fun getActiveSessions(): Flow<List<ChatSessionEntity>>
    
    @Query("SELECT * FROM chat_sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: String): ChatSessionEntity?
    
    @Query("SELECT * FROM chat_sessions WHERE id = :sessionId")
    fun observeSession(sessionId: String): Flow<ChatSessionEntity?>
    
    @Transaction
    @Query("SELECT * FROM chat_sessions WHERE id = :sessionId")
    suspend fun getSessionWithMessages(sessionId: String): ChatSessionWithMessagesRelation?
    
    @Transaction
    @Query("SELECT * FROM chat_sessions WHERE id = :sessionId")
    fun observeSessionWithMessages(sessionId: String): Flow<ChatSessionWithMessagesRelation?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ChatSessionEntity)
    
    @Update
    suspend fun updateSession(session: ChatSessionEntity)
    
    @Query("UPDATE chat_sessions SET title = :title, updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun updateSessionTitle(sessionId: String, title: String, updatedAt: LocalDateTime)
    
    @Query("UPDATE chat_sessions SET isSaved = :isSaved, updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun updateSavedStatus(sessionId: String, isSaved: Boolean, updatedAt: LocalDateTime)
    
    @Query("UPDATE chat_sessions SET isArchived = :isArchived, updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun updateArchivedStatus(sessionId: String, isArchived: Boolean, updatedAt: LocalDateTime)
    
    @Query("UPDATE chat_sessions SET messageCount = messageCount + 1, lastMessageAt = :lastMessageAt, updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun incrementMessageCount(sessionId: String, lastMessageAt: LocalDateTime, updatedAt: LocalDateTime)
    
    @Query("UPDATE chat_sessions SET summary = :summary, updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun updateSessionSummary(sessionId: String, summary: String, updatedAt: LocalDateTime)
    
    @Delete
    suspend fun deleteSession(session: ChatSessionEntity)
    
    @Query("DELETE FROM chat_sessions WHERE id = :sessionId")
    suspend fun deleteSessionById(sessionId: String)
    
    @Query("DELETE FROM chat_sessions WHERE isArchived = 1 AND updatedAt < :beforeDate")
    suspend fun deleteOldArchivedSessions(beforeDate: LocalDateTime)
    
    @Query("SELECT COUNT(*) FROM chat_sessions")
    suspend fun getSessionCount(): Int
    
    @Query("SELECT COUNT(*) FROM chat_sessions WHERE isSaved = 1")
    suspend fun getSavedSessionCount(): Int
}

/**
 * DAO for chat messages
 */
@Dao
interface ChatMessageDao {
    
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessageEntity>>
    
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    suspend fun getMessagesForSessionList(sessionId: String): List<ChatMessageEntity>
    
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentMessages(sessionId: String, limit: Int): List<ChatMessageEntity>
    
    @Query("SELECT * FROM chat_messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: Long): ChatMessageEntity?
    
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId AND role = :role ORDER BY createdAt DESC")
    suspend fun getMessagesByRole(sessionId: String, role: String): List<ChatMessageEntity>
    
    @Query("""
        SELECT * FROM chat_messages 
        WHERE sessionId = :sessionId 
        ORDER BY createdAt DESC 
        LIMIT :limit
    """)
    suspend fun getLastNMessages(sessionId: String, limit: Int): List<ChatMessageEntity>
    
    @Insert
    suspend fun insertMessage(message: ChatMessageEntity): Long
    
    @Insert
    suspend fun insertMessages(messages: List<ChatMessageEntity>)
    
    @Update
    suspend fun updateMessage(message: ChatMessageEntity)
    
    @Query("UPDATE chat_messages SET content = :content, isEdited = 1 WHERE id = :messageId")
    suspend fun updateMessageContent(messageId: Long, content: String)
    
    @Delete
    suspend fun deleteMessage(message: ChatMessageEntity)
    
    @Query("DELETE FROM chat_messages WHERE id = :messageId")
    suspend fun deleteMessageById(messageId: Long)
    
    @Query("DELETE FROM chat_messages WHERE sessionId = :sessionId")
    suspend fun deleteMessagesForSession(sessionId: String)
    
    @Query("SELECT COUNT(*) FROM chat_messages WHERE sessionId = :sessionId")
    suspend fun getMessageCountForSession(sessionId: String): Int
    
    @Query("SELECT COUNT(*) FROM chat_messages")
    suspend fun getTotalMessageCount(): Int
    
    @Query("SELECT SUM(tokensUsed) FROM chat_messages WHERE sessionId = :sessionId")
    suspend fun getTotalTokensForSession(sessionId: String): Int?
}

/**
 * DAO for chat message embeddings
 */
@Dao
interface ChatEmbeddingDao {
    
    @Query("SELECT * FROM chat_embeddings WHERE messageId = :messageId")
    suspend fun getEmbeddingForMessage(messageId: Long): ChatEmbeddingEntity?
    
    @Query("SELECT * FROM chat_embeddings WHERE sessionId = :sessionId")
    suspend fun getEmbeddingsForSession(sessionId: String): List<ChatEmbeddingEntity>
    
    @Query("SELECT * FROM chat_embeddings")
    suspend fun getAllEmbeddings(): List<ChatEmbeddingEntity>
    
    @Query("SELECT messageId FROM chat_embeddings")
    suspend fun getAllEmbeddedMessageIds(): List<Long>
    
    @Query("SELECT COUNT(*) FROM chat_embeddings")
    suspend fun getEmbeddingCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmbedding(embedding: ChatEmbeddingEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmbeddings(embeddings: List<ChatEmbeddingEntity>)
    
    @Query("DELETE FROM chat_embeddings WHERE messageId = :messageId")
    suspend fun deleteEmbeddingForMessage(messageId: Long)
    
    @Query("DELETE FROM chat_embeddings WHERE sessionId = :sessionId")
    suspend fun deleteEmbeddingsForSession(sessionId: String)
    
    @Query("DELETE FROM chat_embeddings WHERE model != :currentModel")
    suspend fun deleteObsoleteEmbeddings(currentModel: String)
    
    @Query("DELETE FROM chat_embeddings")
    suspend fun deleteAllEmbeddings()
}

/**
 * DAO for reflection memories
 */
@Dao
interface ReflectionMemoryDao {
    
    @Query("SELECT * FROM reflection_memories WHERE isActive = 1 ORDER BY updatedAt DESC")
    fun getActiveReflections(): Flow<List<ReflectionMemoryEntity>>
    
    @Query("SELECT * FROM reflection_memories WHERE isActive = 1 ORDER BY updatedAt DESC")
    suspend fun getActiveReflectionsList(): List<ReflectionMemoryEntity>
    
    @Query("SELECT * FROM reflection_memories WHERE isActive = 1 AND embedding IS NOT NULL ORDER BY updatedAt DESC")
    suspend fun getReflectionsWithEmbeddings(): List<ReflectionMemoryEntity>
    
    @Query("SELECT * FROM reflection_memories WHERE category = :category AND isActive = 1 ORDER BY confidence DESC")
    fun getReflectionsByCategory(category: String): Flow<List<ReflectionMemoryEntity>>
    
    @Query("SELECT * FROM reflection_memories WHERE category = :category AND isActive = 1 ORDER BY confidence DESC")
    suspend fun getReflectionsByCategoryList(category: String): List<ReflectionMemoryEntity>
    
    @Query("SELECT * FROM reflection_memories WHERE id = :id")
    suspend fun getReflectionById(id: Long): ReflectionMemoryEntity?
    
    @Query("SELECT * FROM reflection_memories ORDER BY updatedAt DESC LIMIT :limit")
    suspend fun getRecentReflections(limit: Int): List<ReflectionMemoryEntity>
    
    @Query("SELECT * FROM reflection_memories WHERE confidence >= :minConfidence AND isActive = 1 ORDER BY confidence DESC")
    suspend fun getHighConfidenceReflections(minConfidence: Float): List<ReflectionMemoryEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(reflection: ReflectionMemoryEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflections(reflections: List<ReflectionMemoryEntity>)
    
    @Update
    suspend fun updateReflection(reflection: ReflectionMemoryEntity)
    
    @Query("UPDATE reflection_memories SET lastReferenced = :lastReferenced WHERE id = :id")
    suspend fun updateLastReferenced(id: Long, lastReferenced: LocalDateTime)
    
    @Query("UPDATE reflection_memories SET occurrenceCount = occurrenceCount + 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun incrementOccurrenceCount(id: Long, updatedAt: LocalDateTime)
    
    @Query("UPDATE reflection_memories SET isActive = :isActive, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateActiveStatus(id: Long, isActive: Boolean, updatedAt: LocalDateTime)
    
    @Query("UPDATE reflection_memories SET confidence = :confidence, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateConfidence(id: Long, confidence: Float, updatedAt: LocalDateTime)
    
    @Query("""
        UPDATE reflection_memories 
        SET embedding = :embedding, embeddingModel = :model, updatedAt = :updatedAt 
        WHERE id = :id
    """)
    suspend fun updateEmbedding(id: Long, embedding: ByteArray, model: String, updatedAt: LocalDateTime)
    
    @Delete
    suspend fun deleteReflection(reflection: ReflectionMemoryEntity)
    
    @Query("DELETE FROM reflection_memories WHERE id = :id")
    suspend fun deleteReflectionById(id: Long)
    
    @Query("DELETE FROM reflection_memories WHERE confidence < :minConfidence AND occurrenceCount < :minOccurrences")
    suspend fun deleteWeakReflections(minConfidence: Float, minOccurrences: Int)
    
    @Query("SELECT COUNT(*) FROM reflection_memories WHERE isActive = 1")
    suspend fun getActiveReflectionCount(): Int
    
    @Query("SELECT COUNT(*) FROM reflection_memories")
    suspend fun getTotalReflectionCount(): Int
    
    @Query("""
        SELECT * FROM reflection_memories 
        WHERE category IN (:categories) AND isActive = 1 
        ORDER BY confidence DESC 
        LIMIT :limit
    """)
    suspend fun getReflectionsByCategories(categories: List<String>, limit: Int): List<ReflectionMemoryEntity>
}

/**
 * DAO for conversation context
 */
@Dao
interface ConversationContextDao {
    
    @Query("SELECT * FROM conversation_context WHERE sessionId = :sessionId")
    suspend fun getContextForSession(sessionId: String): ConversationContextEntity?
    
    @Query("SELECT * FROM conversation_context WHERE sessionId = :sessionId")
    fun observeContextForSession(sessionId: String): Flow<ConversationContextEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContext(context: ConversationContextEntity)
    
    @Update
    suspend fun updateContext(context: ConversationContextEntity)
    
    @Query("""
        UPDATE conversation_context 
        SET currentTopics = :topics, updatedAt = :updatedAt 
        WHERE sessionId = :sessionId
    """)
    suspend fun updateTopics(sessionId: String, topics: String, updatedAt: LocalDateTime)
    
    @Query("""
        UPDATE conversation_context 
        SET emotionalTone = :tone, updatedAt = :updatedAt 
        WHERE sessionId = :sessionId
    """)
    suspend fun updateEmotionalTone(sessionId: String, tone: String, updatedAt: LocalDateTime)
    
    @Query("""
        UPDATE conversation_context 
        SET discussionDepth = :depth, updatedAt = :updatedAt 
        WHERE sessionId = :sessionId
    """)
    suspend fun updateDiscussionDepth(sessionId: String, depth: Int, updatedAt: LocalDateTime)
    
    @Query("""
        UPDATE conversation_context 
        SET lastSummaryAt = :lastSummaryAt, updatedAt = :updatedAt 
        WHERE sessionId = :sessionId
    """)
    suspend fun updateLastSummaryTime(sessionId: String, lastSummaryAt: LocalDateTime, updatedAt: LocalDateTime)
    
    @Delete
    suspend fun deleteContext(context: ConversationContextEntity)
    
    @Query("DELETE FROM conversation_context WHERE sessionId = :sessionId")
    suspend fun deleteContextForSession(sessionId: String)
}
