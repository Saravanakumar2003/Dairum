package com.dairum.domain.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Repository of writing prompts
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004J\u0006\u0010\b\u001a\u00020\tJ\u0010\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\fJ\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u000e\u001a\u00020\u0007J\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0005J\u0012\u0010\u0013\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u000b\u001a\u00020\fJ\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u000b\u001a\u00020\fR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/dairum/domain/model/WritingPrompts;", "", "()V", "allPrompts", "", "Lcom/dairum/domain/model/WritingPrompt;", "getAllCategories", "Lcom/dairum/domain/model/PromptCategory;", "getPromptCount", "", "getPromptOfTheDay", "date", "Ljava/time/LocalDate;", "getPromptsByCategory", "category", "getPromptsForMood", "moodCategory", "Lcom/dairum/domain/model/MoodCategory;", "getRandomPrompt", "getSeasonalPrompt", "getSpecialOccasionPrompt", "app_debug"})
public final class WritingPrompts {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.dairum.domain.model.WritingPrompt> allPrompts = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.domain.model.WritingPrompts INSTANCE = null;
    
    private WritingPrompts() {
        super();
    }
    
    /**
     * Get the prompt of the day based on date
     */
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.WritingPrompt getPromptOfTheDay(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate date) {
        return null;
    }
    
    /**
     * Get a random prompt
     */
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.WritingPrompt getRandomPrompt() {
        return null;
    }
    
    /**
     * Get prompt by category
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.WritingPrompt> getPromptsByCategory(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.PromptCategory category) {
        return null;
    }
    
    /**
     * Get mood-based prompts
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.WritingPrompt> getPromptsForMood(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MoodCategory moodCategory) {
        return null;
    }
    
    /**
     * Get seasonal prompt
     */
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.WritingPrompt getSeasonalPrompt(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate date) {
        return null;
    }
    
    /**
     * Get special occasion prompt
     */
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.WritingPrompt getSpecialOccasionPrompt(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate date) {
        return null;
    }
    
    /**
     * Get all categories
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.PromptCategory> getAllCategories() {
        return null;
    }
    
    /**
     * Get prompt count
     */
    public final int getPromptCount() {
        return 0;
    }
}