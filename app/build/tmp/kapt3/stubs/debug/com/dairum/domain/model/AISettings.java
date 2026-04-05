package com.dairum.domain.model;

import java.time.LocalDateTime;

/**
 * AI generation settings
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b!\b\u0086\b\u0018\u00002\u00020\u0001Bc\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u0012\b\b\u0002\u0010\f\u001a\u00020\n\u0012\b\b\u0002\u0010\r\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000e\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000fJ\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0006H\u00c6\u0003J\t\u0010 \u001a\u00020\bH\u00c6\u0003J\t\u0010!\u001a\u00020\nH\u00c6\u0003J\t\u0010\"\u001a\u00020\nH\u00c6\u0003J\t\u0010#\u001a\u00020\nH\u00c6\u0003J\t\u0010$\u001a\u00020\u0006H\u00c6\u0003J\t\u0010%\u001a\u00020\nH\u00c6\u0003Jg\u0010&\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\u00062\b\b\u0002\u0010\u000e\u001a\u00020\nH\u00c6\u0001J\u0013\u0010\'\u001a\u00020\n2\b\u0010(\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010)\u001a\u00020\u0006H\u00d6\u0001J\t\u0010*\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u000b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u000e\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u0011\u0010\f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001aR\u0011\u0010\r\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0018\u00a8\u0006+"}, d2 = {"Lcom/dairum/domain/model/AISettings;", "", "selectedModelId", "", "selectedEmbeddingModelId", "maxContextEntries", "", "embeddingSimilarityThreshold", "", "autoUpdateEmbeddings", "", "cacheResponses", "enablePatternDetection", "weeklyReflectionDay", "enableNotifications", "(Ljava/lang/String;Ljava/lang/String;IFZZZIZ)V", "getAutoUpdateEmbeddings", "()Z", "getCacheResponses", "getEmbeddingSimilarityThreshold", "()F", "getEnableNotifications", "getEnablePatternDetection", "getMaxContextEntries", "()I", "getSelectedEmbeddingModelId", "()Ljava/lang/String;", "getSelectedModelId", "getWeeklyReflectionDay", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class AISettings {
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String selectedModelId = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String selectedEmbeddingModelId = null;
    private final int maxContextEntries = 0;
    private final float embeddingSimilarityThreshold = 0.0F;
    private final boolean autoUpdateEmbeddings = false;
    private final boolean cacheResponses = false;
    private final boolean enablePatternDetection = false;
    private final int weeklyReflectionDay = 0;
    private final boolean enableNotifications = false;
    
    public AISettings(@org.jetbrains.annotations.Nullable()
    java.lang.String selectedModelId, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedEmbeddingModelId, int maxContextEntries, float embeddingSimilarityThreshold, boolean autoUpdateEmbeddings, boolean cacheResponses, boolean enablePatternDetection, int weeklyReflectionDay, boolean enableNotifications) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSelectedModelId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSelectedEmbeddingModelId() {
        return null;
    }
    
    public final int getMaxContextEntries() {
        return 0;
    }
    
    public final float getEmbeddingSimilarityThreshold() {
        return 0.0F;
    }
    
    public final boolean getAutoUpdateEmbeddings() {
        return false;
    }
    
    public final boolean getCacheResponses() {
        return false;
    }
    
    public final boolean getEnablePatternDetection() {
        return false;
    }
    
    public final int getWeeklyReflectionDay() {
        return 0;
    }
    
    public final boolean getEnableNotifications() {
        return false;
    }
    
    public AISettings() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final float component4() {
        return 0.0F;
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final boolean component6() {
        return false;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.AISettings copy(@org.jetbrains.annotations.Nullable()
    java.lang.String selectedModelId, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedEmbeddingModelId, int maxContextEntries, float embeddingSimilarityThreshold, boolean autoUpdateEmbeddings, boolean cacheResponses, boolean enablePatternDetection, int weeklyReflectionDay, boolean enableNotifications) {
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