package com.dairum.domain.model;

import java.time.LocalDateTime;

/**
 * Memory retrieval result combining different memory types
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0003\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\u000f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00c6\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00c6\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\fH\u00c6\u0003JS\u0010\u001a\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\fH\u00d6\u0001J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006!"}, d2 = {"Lcom/dairum/domain/model/MemoryRetrievalResult;", "", "shortTermMessages", "", "Lcom/dairum/domain/model/ChatMessage;", "longTermChatMessages", "Lcom/dairum/domain/model/ScoredChatMessage;", "journalEntries", "Lcom/dairum/domain/model/RetrievalResult;", "reflectionMemories", "Lcom/dairum/domain/model/ScoredReflection;", "totalTokenEstimate", "", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;I)V", "getJournalEntries", "()Ljava/util/List;", "getLongTermChatMessages", "getReflectionMemories", "getShortTermMessages", "getTotalTokenEstimate", "()I", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class MemoryRetrievalResult {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.ChatMessage> shortTermMessages = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.ScoredChatMessage> longTermChatMessages = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.RetrievalResult> journalEntries = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.ScoredReflection> reflectionMemories = null;
    private final int totalTokenEstimate = 0;
    
    public MemoryRetrievalResult(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ChatMessage> shortTermMessages, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ScoredChatMessage> longTermChatMessages, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.RetrievalResult> journalEntries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ScoredReflection> reflectionMemories, int totalTokenEstimate) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ChatMessage> getShortTermMessages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ScoredChatMessage> getLongTermChatMessages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.RetrievalResult> getJournalEntries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ScoredReflection> getReflectionMemories() {
        return null;
    }
    
    public final int getTotalTokenEstimate() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ChatMessage> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ScoredChatMessage> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.RetrievalResult> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.ScoredReflection> component4() {
        return null;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.MemoryRetrievalResult copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ChatMessage> shortTermMessages, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ScoredChatMessage> longTermChatMessages, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.RetrievalResult> journalEntries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.ScoredReflection> reflectionMemories, int totalTokenEstimate) {
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