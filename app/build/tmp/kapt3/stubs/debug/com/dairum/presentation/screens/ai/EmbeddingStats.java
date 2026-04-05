package com.dairum.presentation.screens.ai;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.data.ai.model.StorageUsage;
import com.dairum.data.ai.retrieval.RetrievalService;
import com.dairum.data.ai.usecase.AIOrchestrator;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.AIContentRepository;
import com.dairum.domain.repository.AIModelRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import timber.log.Timber;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0007J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J)\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\t2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\r\u00a8\u0006\u001c"}, d2 = {"Lcom/dairum/presentation/screens/ai/EmbeddingStats;", "", "embeddingCount", "", "totalEntries", "lastGenerated", "Ljava/time/LocalDateTime;", "(IILjava/time/LocalDateTime;)V", "allGenerated", "", "getAllGenerated", "()Z", "getEmbeddingCount", "()I", "getLastGenerated", "()Ljava/time/LocalDateTime;", "pendingCount", "getPendingCount", "getTotalEntries", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "toString", "", "app_debug"})
public final class EmbeddingStats {
    private final int embeddingCount = 0;
    private final int totalEntries = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.time.LocalDateTime lastGenerated = null;
    
    public EmbeddingStats(int embeddingCount, int totalEntries, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime lastGenerated) {
        super();
    }
    
    public final int getEmbeddingCount() {
        return 0;
    }
    
    public final int getTotalEntries() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime getLastGenerated() {
        return null;
    }
    
    public final boolean getAllGenerated() {
        return false;
    }
    
    public final int getPendingCount() {
        return 0;
    }
    
    public EmbeddingStats() {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.ai.EmbeddingStats copy(int embeddingCount, int totalEntries, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime lastGenerated) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}