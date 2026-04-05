package com.dairum.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model for reflection memory - insights about the user
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0014\n\u0002\b)\b\u0086\b\u0018\u00002\u00020\u0001B\u0089\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000b\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0010\u00a2\u0006\u0002\u0010\u0017J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u0014H\u00c6\u0003J\t\u0010.\u001a\u00020\u0010H\u00c6\u0003J\t\u0010/\u001a\u00020\u0010H\u00c6\u0003J\t\u00100\u001a\u00020\u0005H\u00c6\u0003J\t\u00101\u001a\u00020\u0007H\u00c6\u0003J\t\u00102\u001a\u00020\tH\u00c6\u0003J\u000f\u00103\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u00c6\u0003J\u000f\u00104\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bH\u00c6\u0003J\t\u00105\u001a\u00020\u000eH\u00c6\u0003J\u000b\u00106\u001a\u0004\u0018\u00010\u0010H\u00c6\u0003J\t\u00107\u001a\u00020\u0012H\u00c6\u0003J\u0091\u0001\u00108\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000b2\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00102\b\b\u0002\u0010\u0016\u001a\u00020\u0010H\u00c6\u0001J\u0013\u00109\u001a\u00020\u00122\b\u0010:\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010;\u001a\u00020\u000eH\u0016J\t\u0010<\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0015\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0013\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010$R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001fR\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010)R\u0011\u0010\u0016\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001f\u00a8\u0006="}, d2 = {"Lcom/dairum/domain/model/ReflectionMemory;", "", "id", "", "content", "", "category", "Lcom/dairum/domain/model/ReflectionCategory;", "confidence", "", "sourceMessageIds", "", "sourceSessionIds", "occurrenceCount", "", "lastReferenced", "Ljava/time/LocalDateTime;", "isActive", "", "embedding", "", "createdAt", "updatedAt", "(JLjava/lang/String;Lcom/dairum/domain/model/ReflectionCategory;FLjava/util/List;Ljava/util/List;ILjava/time/LocalDateTime;Z[FLjava/time/LocalDateTime;Ljava/time/LocalDateTime;)V", "getCategory", "()Lcom/dairum/domain/model/ReflectionCategory;", "getConfidence", "()F", "getContent", "()Ljava/lang/String;", "getCreatedAt", "()Ljava/time/LocalDateTime;", "getEmbedding", "()[F", "getId", "()J", "()Z", "getLastReferenced", "getOccurrenceCount", "()I", "getSourceMessageIds", "()Ljava/util/List;", "getSourceSessionIds", "getUpdatedAt", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class ReflectionMemory {
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String content = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.ReflectionCategory category = null;
    private final float confidence = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Long> sourceMessageIds = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> sourceSessionIds = null;
    private final int occurrenceCount = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.time.LocalDateTime lastReferenced = null;
    private final boolean isActive = false;
    @org.jetbrains.annotations.Nullable()
    private final float[] embedding = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime createdAt = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime updatedAt = null;
    
    public ReflectionMemory(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionCategory category, float confidence, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> sourceMessageIds, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> sourceSessionIds, int occurrenceCount, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime lastReferenced, boolean isActive, @org.jetbrains.annotations.Nullable()
    float[] embedding, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.ReflectionCategory getCategory() {
        return null;
    }
    
    public final float getConfidence() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Long> getSourceMessageIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getSourceSessionIds() {
        return null;
    }
    
    public final int getOccurrenceCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime getLastReferenced() {
        return null;
    }
    
    public final boolean isActive() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final float[] getEmbedding() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime getCreatedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime getUpdatedAt() {
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
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final float[] component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.ReflectionCategory component3() {
        return null;
    }
    
    public final float component4() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Long> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component6() {
        return null;
    }
    
    public final int component7() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime component8() {
        return null;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.ReflectionMemory copy(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.ReflectionCategory category, float confidence, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> sourceMessageIds, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> sourceSessionIds, int occurrenceCount, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime lastReferenced, boolean isActive, @org.jetbrains.annotations.Nullable()
    float[] embedding, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}