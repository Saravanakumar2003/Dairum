package com.dairum.data.ai.retrieval;

import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.domain.model.JournalEmbedding;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.RetrievalResult;
import com.dairum.domain.repository.AIModelRepository;
import com.dairum.domain.repository.EmbeddingRepository;
import com.dairum.domain.repository.JournalRepository;
import kotlinx.coroutines.Dispatchers;
import timber.log.Timber;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of retrieval service using cosine similarity
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010$\n\u0002\u0010\t\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\f\b\u0007\u0018\u0000 L2\u00020\u0001:\u0001LB/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J,\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u00182\u0006\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u0014H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001c\u0010\u001dJ\u0018\u0010\u001e\u001a\u00020\u00192\u0006\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u0012H\u0002J\u001c\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\u0018H\u0082@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b#\u0010$J:\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0&0\u00182\u0006\u0010(\u001a\u00020\u00112\u0006\u0010)\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020+H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b,\u0010-JV\u0010.\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001828\u0010/\u001a4\u0012\u0013\u0012\u00110\u000e\u00a2\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(3\u0012\u0013\u0012\u00110\u000e\u00a2\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(4\u0012\u0004\u0012\u00020\"\u0018\u000100H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b5\u00106J\b\u00107\u001a\u00020\"H\u0002J\u001a\u00108\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00120\u0010H\u0082@\u00a2\u0006\u0002\u0010$J\u000e\u00109\u001a\u00020+H\u0096@\u00a2\u0006\u0002\u0010$J:\u0010:\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0&0\u00182\u0006\u0010;\u001a\u00020\u00142\u0006\u0010)\u001a\u00020\u000e2\u0006\u0010<\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b=\u0010>JJ\u0010?\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0&0\u00182\u0006\u0010;\u001a\u00020\u00142\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020A2\u0006\u0010)\u001a\u00020\u000e2\u0006\u0010<\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bC\u0010DJ$\u0010E\u001a\b\u0012\u0004\u0012\u00020\"0\u00182\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bF\u0010GJ*\u0010H\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00182\f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00160&H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bJ\u0010KR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00120\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006M"}, d2 = {"Lcom/dairum/data/ai/retrieval/LocalRetrievalService;", "Lcom/dairum/data/ai/retrieval/RetrievalService;", "embeddingService", "Lcom/dairum/data/ai/embedding/EmbeddingService;", "embeddingRepository", "Lcom/dairum/domain/repository/EmbeddingRepository;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "modelManager", "Lcom/dairum/data/ai/model/ModelManager;", "modelRepository", "Lcom/dairum/domain/repository/AIModelRepository;", "(Lcom/dairum/data/ai/embedding/EmbeddingService;Lcom/dairum/domain/repository/EmbeddingRepository;Lcom/dairum/domain/repository/JournalRepository;Lcom/dairum/data/ai/model/ModelManager;Lcom/dairum/domain/repository/AIModelRepository;)V", "cacheVersion", "", "embeddingCache", "", "", "", "buildEntryText", "", "entry", "Lcom/dairum/domain/model/JournalEntry;", "computeSimilarity", "Lkotlin/Result;", "", "text1", "text2", "computeSimilarity-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cosineSimilarity", "a", "b", "ensureEmbeddingModelLoaded", "", "ensureEmbeddingModelLoaded-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findSimilarEntries", "", "Lcom/dairum/domain/model/RetrievalResult;", "entryId", "topK", "excludeSelf", "", "findSimilarEntries-BWLJW6A", "(JIZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateMissingEmbeddings", "onProgress", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "current", "total", "generateMissingEmbeddings-gIAlu-s", "(Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "invalidateCache", "loadEmbeddingsToCache", "needsRegeneration", "searchRelevantEntries", "query", "minSimilarity", "searchRelevantEntries-BWLJW6A", "(Ljava/lang/String;IFLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchRelevantEntriesInRange", "startDate", "Ljava/time/LocalDateTime;", "endDate", "searchRelevantEntriesInRange-hUnOzRk", "(Ljava/lang/String;Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;IFLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEntryEmbedding", "updateEntryEmbedding-gIAlu-s", "(Lcom/dairum/domain/model/JournalEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEntryEmbeddings", "entries", "updateEntryEmbeddings-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class LocalRetrievalService implements com.dairum.data.ai.retrieval.RetrievalService {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.embedding.EmbeddingService embeddingService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.EmbeddingRepository embeddingRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.ai.model.ModelManager modelManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.AIModelRepository modelRepository = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "LocalRetrievalService";
    private static final int BATCH_SIZE = 32;
    @org.jetbrains.annotations.NotNull()
    private java.util.Map<java.lang.Long, float[]> embeddingCache;
    private int cacheVersion = 0;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.ai.retrieval.LocalRetrievalService.Companion Companion = null;
    
    @javax.inject.Inject()
    public LocalRetrievalService(@org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.embedding.EmbeddingService embeddingService, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.EmbeddingRepository embeddingRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.data.ai.model.ModelManager modelManager, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.AIModelRepository modelRepository) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object needsRegeneration(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Build searchable text from entry
     */
    private final java.lang.String buildEntryText(com.dairum.domain.model.JournalEntry entry) {
        return null;
    }
    
    /**
     * Load embeddings into memory cache
     */
    private final java.lang.Object loadEmbeddingsToCache(kotlin.coroutines.Continuation<? super java.util.Map<java.lang.Long, float[]>> $completion) {
        return null;
    }
    
    private final void invalidateCache() {
    }
    
    /**
     * Compute cosine similarity between two vectors
     */
    private final float cosineSimilarity(float[] a, float[] b) {
        return 0.0F;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/dairum/data/ai/retrieval/LocalRetrievalService$Companion;", "", "()V", "BATCH_SIZE", "", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}