package com.dairum.domain.model;

import java.time.LocalDateTime;

/**
 * Prompt components built from memory retrieval
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u0006\u0010\u0011\u001a\u00020\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\bH\u00c6\u0003J;\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001b\u001a\u00020\bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000b\u00a8\u0006\u001d"}, d2 = {"Lcom/dairum/domain/model/ConversationPrompt;", "", "systemInstruction", "", "recentConversation", "relevantMemories", "userQuestion", "totalTokens", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V", "getRecentConversation", "()Ljava/lang/String;", "getRelevantMemories", "getSystemInstruction", "getTotalTokens", "()I", "getUserQuestion", "build", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class ConversationPrompt {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String systemInstruction = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String recentConversation = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String relevantMemories = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String userQuestion = null;
    private final int totalTokens = 0;
    
    public ConversationPrompt(@org.jetbrains.annotations.NotNull()
    java.lang.String systemInstruction, @org.jetbrains.annotations.NotNull()
    java.lang.String recentConversation, @org.jetbrains.annotations.NotNull()
    java.lang.String relevantMemories, @org.jetbrains.annotations.NotNull()
    java.lang.String userQuestion, int totalTokens) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSystemInstruction() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRecentConversation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRelevantMemories() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUserQuestion() {
        return null;
    }
    
    public final int getTotalTokens() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String build() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.ConversationPrompt copy(@org.jetbrains.annotations.NotNull()
    java.lang.String systemInstruction, @org.jetbrains.annotations.NotNull()
    java.lang.String recentConversation, @org.jetbrains.annotations.NotNull()
    java.lang.String relevantMemories, @org.jetbrains.annotations.NotNull()
    java.lang.String userQuestion, int totalTokens) {
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