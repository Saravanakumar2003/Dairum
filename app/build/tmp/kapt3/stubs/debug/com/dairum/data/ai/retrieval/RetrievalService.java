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
 * Interface for semantic search and retrieval
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J,\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\b\u0010\tJ>\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u00032\u0006\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0012H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0013\u0010\u0014JX\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00100\u00032:\b\u0002\u0010\u0016\u001a4\u0012\u0013\u0012\u00110\u0010\u00a2\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(\u001a\u0012\u0013\u0012\u00110\u0010\u00a2\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u001c\u0018\u00010\u0017H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001d\u0010\u001eJ\u000e\u0010\u001f\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010 J>\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u00032\u0006\u0010\"\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010#\u001a\u00020\u0004H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b$\u0010%JN\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u00032\u0006\u0010\"\u001a\u00020\u00062\u0006\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020(2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010#\u001a\u00020\u0004H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b*\u0010+J$\u0010,\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00032\u0006\u0010-\u001a\u00020.H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b/\u00100J*\u00101\u001a\b\u0012\u0004\u0012\u00020\u00100\u00032\f\u00102\u001a\b\u0012\u0004\u0012\u00020.0\u000bH\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b3\u00104\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00065"}, d2 = {"Lcom/dairum/data/ai/retrieval/RetrievalService;", "", "computeSimilarity", "Lkotlin/Result;", "", "text1", "", "text2", "computeSimilarity-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findSimilarEntries", "", "Lcom/dairum/domain/model/RetrievalResult;", "entryId", "", "topK", "", "excludeSelf", "", "findSimilarEntries-BWLJW6A", "(JIZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateMissingEmbeddings", "onProgress", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "current", "total", "", "generateMissingEmbeddings-gIAlu-s", "(Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "needsRegeneration", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchRelevantEntries", "query", "minSimilarity", "searchRelevantEntries-BWLJW6A", "(Ljava/lang/String;IFLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchRelevantEntriesInRange", "startDate", "Ljava/time/LocalDateTime;", "endDate", "searchRelevantEntriesInRange-hUnOzRk", "(Ljava/lang/String;Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;IFLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEntryEmbedding", "entry", "Lcom/dairum/domain/model/JournalEntry;", "updateEntryEmbedding-gIAlu-s", "(Lcom/dairum/domain/model/JournalEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEntryEmbeddings", "entries", "updateEntryEmbeddings-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface RetrievalService {
    
    /**
     * Check if embeddings need to be regenerated (e.g., model changed)
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object needsRegeneration(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    /**
     * Interface for semantic search and retrieval
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}