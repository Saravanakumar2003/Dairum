package com.dairum.domain.model;

/**
 * Statistics models for the Stats screen
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b>\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u00f7\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u001b\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u0015\u00a2\u0006\u0002\u0010\u001fJ\t\u0010?\u001a\u00020\u0003H\u00c6\u0003J\t\u0010@\u001a\u00020\u0003H\u00c6\u0003J\t\u0010A\u001a\u00020\u0003H\u00c6\u0003J\t\u0010B\u001a\u00020\u0003H\u00c6\u0003J\t\u0010C\u001a\u00020\u0003H\u00c6\u0003J\t\u0010D\u001a\u00020\u0012H\u00c6\u0003J\t\u0010E\u001a\u00020\u0012H\u00c6\u0003J\t\u0010F\u001a\u00020\u0015H\u00c6\u0003J\t\u0010G\u001a\u00020\u000bH\u00c6\u0003J\t\u0010H\u001a\u00020\u0015H\u00c6\u0003J\t\u0010I\u001a\u00020\u0003H\u00c6\u0003J\t\u0010J\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010K\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010$J\t\u0010L\u001a\u00020\u001bH\u00c6\u0003J\t\u0010M\u001a\u00020\u0003H\u00c6\u0003J\t\u0010N\u001a\u00020\u0003H\u00c6\u0003J\t\u0010O\u001a\u00020\u0015H\u00c6\u0003J\t\u0010P\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010R\u001a\u00020\u0003H\u00c6\u0003J\t\u0010S\u001a\u00020\u0003H\u00c6\u0003J\t\u0010T\u001a\u00020\u0003H\u00c6\u0003J\t\u0010U\u001a\u00020\u000bH\u00c6\u0003J\t\u0010V\u001a\u00020\u0003H\u00c6\u0003J\u0080\u0002\u0010W\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00122\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u0017\u001a\u00020\u00152\b\b\u0002\u0010\u0018\u001a\u00020\u00032\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\b\b\u0002\u0010\u001c\u001a\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\u00032\b\b\u0002\u0010\u001e\u001a\u00020\u0015H\u00c6\u0001\u00a2\u0006\u0002\u0010XJ\u0013\u0010Y\u001a\u00020Z2\b\u0010[\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\\\u001a\u00020\u0003H\u00d6\u0001J\t\u0010]\u001a\u00020^H\u00d6\u0001R\u0011\u0010\u0016\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010!R\u0015\u0010\u0019\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010%\u001a\u0004\b#\u0010$R\u0011\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u0017\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\'R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0018\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010*R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010*R\u0011\u0010\u001d\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010*R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010*R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010*R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010*R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010*R\u0011\u0010\u001e\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\'R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010*R\u0011\u0010\u001c\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010*R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010*R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010*R\u0011\u0010\u0013\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u00104R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010*R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010*R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010*R\u0011\u0010\u001a\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010>\u00a8\u0006_"}, d2 = {"Lcom/dairum/domain/model/JournalStats;", "", "totalEntries", "", "entriesThisWeek", "entriesThisMonth", "entriesThisYear", "entriesInCustomRange", "currentStreak", "longestStreak", "averageWordCount", "", "totalWordCount", "totalPhotos", "totalVideos", "totalVoiceNotes", "totalAttachments", "storageUsed", "", "totalStorageUsed", "comparisonToLastMonth", "", "averageEntriesPerWeek", "consistencyScore", "daysSinceLastEntry", "bestWritingDayOfWeek", "writingTimePreference", "Lcom/dairum/domain/model/WritingTimePreference;", "totalDaysJournaling", "entriesLastMonth", "monthlyGrowth", "(IIIIIIIDIIIIIJJFDFILjava/lang/Integer;Lcom/dairum/domain/model/WritingTimePreference;IIF)V", "getAverageEntriesPerWeek", "()D", "getAverageWordCount", "getBestWritingDayOfWeek", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getComparisonToLastMonth", "()F", "getConsistencyScore", "getCurrentStreak", "()I", "getDaysSinceLastEntry", "getEntriesInCustomRange", "getEntriesLastMonth", "getEntriesThisMonth", "getEntriesThisWeek", "getEntriesThisYear", "getLongestStreak", "getMonthlyGrowth", "getStorageUsed", "()J", "getTotalAttachments", "getTotalDaysJournaling", "getTotalEntries", "getTotalPhotos", "getTotalStorageUsed", "getTotalVideos", "getTotalVoiceNotes", "getTotalWordCount", "getWritingTimePreference", "()Lcom/dairum/domain/model/WritingTimePreference;", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(IIIIIIIDIIIIIJJFDFILjava/lang/Integer;Lcom/dairum/domain/model/WritingTimePreference;IIF)Lcom/dairum/domain/model/JournalStats;", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class JournalStats {
    private final int totalEntries = 0;
    private final int entriesThisWeek = 0;
    private final int entriesThisMonth = 0;
    private final int entriesThisYear = 0;
    private final int entriesInCustomRange = 0;
    private final int currentStreak = 0;
    private final int longestStreak = 0;
    private final double averageWordCount = 0.0;
    private final int totalWordCount = 0;
    private final int totalPhotos = 0;
    private final int totalVideos = 0;
    private final int totalVoiceNotes = 0;
    private final int totalAttachments = 0;
    private final long storageUsed = 0L;
    private final long totalStorageUsed = 0L;
    private final float comparisonToLastMonth = 0.0F;
    private final double averageEntriesPerWeek = 0.0;
    private final float consistencyScore = 0.0F;
    private final int daysSinceLastEntry = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer bestWritingDayOfWeek = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.WritingTimePreference writingTimePreference = null;
    private final int totalDaysJournaling = 0;
    private final int entriesLastMonth = 0;
    private final float monthlyGrowth = 0.0F;
    
    public JournalStats(int totalEntries, int entriesThisWeek, int entriesThisMonth, int entriesThisYear, int entriesInCustomRange, int currentStreak, int longestStreak, double averageWordCount, int totalWordCount, int totalPhotos, int totalVideos, int totalVoiceNotes, int totalAttachments, long storageUsed, long totalStorageUsed, float comparisonToLastMonth, double averageEntriesPerWeek, float consistencyScore, int daysSinceLastEntry, @org.jetbrains.annotations.Nullable()
    java.lang.Integer bestWritingDayOfWeek, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.WritingTimePreference writingTimePreference, int totalDaysJournaling, int entriesLastMonth, float monthlyGrowth) {
        super();
    }
    
    public final int getTotalEntries() {
        return 0;
    }
    
    public final int getEntriesThisWeek() {
        return 0;
    }
    
    public final int getEntriesThisMonth() {
        return 0;
    }
    
    public final int getEntriesThisYear() {
        return 0;
    }
    
    public final int getEntriesInCustomRange() {
        return 0;
    }
    
    public final int getCurrentStreak() {
        return 0;
    }
    
    public final int getLongestStreak() {
        return 0;
    }
    
    public final double getAverageWordCount() {
        return 0.0;
    }
    
    public final int getTotalWordCount() {
        return 0;
    }
    
    public final int getTotalPhotos() {
        return 0;
    }
    
    public final int getTotalVideos() {
        return 0;
    }
    
    public final int getTotalVoiceNotes() {
        return 0;
    }
    
    public final int getTotalAttachments() {
        return 0;
    }
    
    public final long getStorageUsed() {
        return 0L;
    }
    
    public final long getTotalStorageUsed() {
        return 0L;
    }
    
    public final float getComparisonToLastMonth() {
        return 0.0F;
    }
    
    public final double getAverageEntriesPerWeek() {
        return 0.0;
    }
    
    public final float getConsistencyScore() {
        return 0.0F;
    }
    
    public final int getDaysSinceLastEntry() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getBestWritingDayOfWeek() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.WritingTimePreference getWritingTimePreference() {
        return null;
    }
    
    public final int getTotalDaysJournaling() {
        return 0;
    }
    
    public final int getEntriesLastMonth() {
        return 0;
    }
    
    public final float getMonthlyGrowth() {
        return 0.0F;
    }
    
    public JournalStats() {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int component10() {
        return 0;
    }
    
    public final int component11() {
        return 0;
    }
    
    public final int component12() {
        return 0;
    }
    
    public final int component13() {
        return 0;
    }
    
    public final long component14() {
        return 0L;
    }
    
    public final long component15() {
        return 0L;
    }
    
    public final float component16() {
        return 0.0F;
    }
    
    public final double component17() {
        return 0.0;
    }
    
    public final float component18() {
        return 0.0F;
    }
    
    public final int component19() {
        return 0;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component20() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.WritingTimePreference component21() {
        return null;
    }
    
    public final int component22() {
        return 0;
    }
    
    public final int component23() {
        return 0;
    }
    
    public final float component24() {
        return 0.0F;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int component5() {
        return 0;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final double component8() {
        return 0.0;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.JournalStats copy(int totalEntries, int entriesThisWeek, int entriesThisMonth, int entriesThisYear, int entriesInCustomRange, int currentStreak, int longestStreak, double averageWordCount, int totalWordCount, int totalPhotos, int totalVideos, int totalVoiceNotes, int totalAttachments, long storageUsed, long totalStorageUsed, float comparisonToLastMonth, double averageEntriesPerWeek, float consistencyScore, int daysSinceLastEntry, @org.jetbrains.annotations.Nullable()
    java.lang.Integer bestWritingDayOfWeek, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.WritingTimePreference writingTimePreference, int totalDaysJournaling, int entriesLastMonth, float monthlyGrowth) {
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