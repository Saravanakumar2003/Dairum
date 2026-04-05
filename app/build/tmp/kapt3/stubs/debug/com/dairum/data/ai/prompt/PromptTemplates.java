package com.dairum.data.ai.prompt;

import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.PromptTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Prompt templates for various AI use cases
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0015\u001a\u00020\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u00182\b\b\u0002\u0010\u001a\u001a\u00020\u001bJ2\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u00042\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u00182\u0014\b\u0002\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00160\u001fJ\u0014\u0010 \u001a\u00020\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0016\u0010\u0011\u001a\n \u0013*\u0004\u0018\u00010\u00120\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0014\u001a\n \u0013*\u0004\u0018\u00010\u00120\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/dairum/data/ai/prompt/PromptTemplates;", "", "()V", "ASK_JOURNAL", "Lcom/dairum/domain/model/PromptTemplate;", "getASK_JOURNAL", "()Lcom/dairum/domain/model/PromptTemplate;", "DAILY_SUMMARY", "getDAILY_SUMMARY", "MONTHLY_SUMMARY", "getMONTHLY_SUMMARY", "MOOD_ANALYSIS", "getMOOD_ANALYSIS", "PATTERN_DETECTION", "getPATTERN_DETECTION", "WEEKLY_REFLECTION", "getWEEKLY_REFLECTION", "dateFormatter", "Ljava/time/format/DateTimeFormatter;", "kotlin.jvm.PlatformType", "timeFormatter", "buildCondensedContext", "", "entries", "", "Lcom/dairum/domain/model/JournalEntry;", "maxEntries", "", "buildPrompt", "template", "additionalContext", "", "formatEntries", "app_debug"})
public final class PromptTemplates {
    private static final java.time.format.DateTimeFormatter dateFormatter = null;
    private static final java.time.format.DateTimeFormatter timeFormatter = null;
    
    /**
     * General journal question prompt - with first-person context understanding
     */
    @org.jetbrains.annotations.NotNull()
    private static final com.dairum.domain.model.PromptTemplate ASK_JOURNAL = null;
    
    /**
     * Weekly reflection prompt
     */
    @org.jetbrains.annotations.NotNull()
    private static final com.dairum.domain.model.PromptTemplate WEEKLY_REFLECTION = null;
    
    /**
     * Pattern detection prompt
     */
    @org.jetbrains.annotations.NotNull()
    private static final com.dairum.domain.model.PromptTemplate PATTERN_DETECTION = null;
    
    /**
     * Daily summary prompt
     */
    @org.jetbrains.annotations.NotNull()
    private static final com.dairum.domain.model.PromptTemplate DAILY_SUMMARY = null;
    
    /**
     * Monthly summary prompt
     */
    @org.jetbrains.annotations.NotNull()
    private static final com.dairum.domain.model.PromptTemplate MONTHLY_SUMMARY = null;
    
    /**
     * Mood analysis prompt
     */
    @org.jetbrains.annotations.NotNull()
    private static final com.dairum.domain.model.PromptTemplate MOOD_ANALYSIS = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.ai.prompt.PromptTemplates INSTANCE = null;
    
    private PromptTemplates() {
        super();
    }
    
    /**
     * General journal question prompt - with first-person context understanding
     */
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.PromptTemplate getASK_JOURNAL() {
        return null;
    }
    
    /**
     * Weekly reflection prompt
     */
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.PromptTemplate getWEEKLY_REFLECTION() {
        return null;
    }
    
    /**
     * Pattern detection prompt
     */
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.PromptTemplate getPATTERN_DETECTION() {
        return null;
    }
    
    /**
     * Daily summary prompt
     */
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.PromptTemplate getDAILY_SUMMARY() {
        return null;
    }
    
    /**
     * Monthly summary prompt
     */
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.PromptTemplate getMONTHLY_SUMMARY() {
        return null;
    }
    
    /**
     * Mood analysis prompt
     */
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.PromptTemplate getMOOD_ANALYSIS() {
        return null;
    }
    
    /**
     * Build prompt from template with entry context
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String buildPrompt(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.PromptTemplate template, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.String> additionalContext) {
        return null;
    }
    
    /**
     * Format entries for inclusion in prompts
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatEntries(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries) {
        return null;
    }
    
    /**
     * Build a condensed context for limited token budget
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String buildCondensedContext(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries, int maxEntries) {
        return null;
    }
}