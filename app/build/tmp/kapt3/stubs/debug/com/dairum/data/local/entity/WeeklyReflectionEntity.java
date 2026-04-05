package com.dairum.data.local.entity;

import androidx.room.*;
import com.dairum.domain.model.*;
import java.time.LocalDateTime;

/**
 * Entity for weekly reflections
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b2\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B\u0087\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\f\u0012\u0006\u0010\u000e\u001a\u00020\b\u0012\u0006\u0010\u000f\u001a\u00020\b\u0012\u0006\u0010\u0010\u001a\u00020\b\u0012\u0006\u0010\u0011\u001a\u00020\b\u0012\u0006\u0010\u0012\u001a\u00020\b\u0012\u0006\u0010\u0013\u001a\u00020\b\u0012\u0006\u0010\u0014\u001a\u00020\f\u0012\u0006\u0010\u0015\u001a\u00020\f\u0012\u0006\u0010\u0016\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0017J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\bH\u00c6\u0003J\t\u0010/\u001a\u00020\bH\u00c6\u0003J\t\u00100\u001a\u00020\bH\u00c6\u0003J\t\u00101\u001a\u00020\bH\u00c6\u0003J\t\u00102\u001a\u00020\fH\u00c6\u0003J\t\u00103\u001a\u00020\fH\u00c6\u0003J\t\u00104\u001a\u00020\u0005H\u00c6\u0003J\t\u00105\u001a\u00020\u0005H\u00c6\u0003J\t\u00106\u001a\u00020\u0005H\u00c6\u0003J\t\u00107\u001a\u00020\bH\u00c6\u0003J\t\u00108\u001a\u00020\nH\u00c6\u0003J\t\u00109\u001a\u00020\fH\u00c6\u0003J\t\u0010:\u001a\u00020\fH\u00c6\u0003J\t\u0010;\u001a\u00020\bH\u00c6\u0003J\t\u0010<\u001a\u00020\bH\u00c6\u0003J\u00a9\u0001\u0010=\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0010\u001a\u00020\b2\b\b\u0002\u0010\u0011\u001a\u00020\b2\b\b\u0002\u0010\u0012\u001a\u00020\b2\b\b\u0002\u0010\u0013\u001a\u00020\b2\b\b\u0002\u0010\u0014\u001a\u00020\f2\b\b\u0002\u0010\u0015\u001a\u00020\f2\b\b\u0002\u0010\u0016\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010>\u001a\u00020?2\b\u0010@\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010A\u001a\u00020\fH\u00d6\u0001J\t\u0010B\u001a\u00020\bH\u00d6\u0001R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0016\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u000e\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0014\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\u0012\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001dR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\u0011\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001dR\u0011\u0010\u000f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001dR\u0011\u0010\r\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001fR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001fR\u0011\u0010\u0010\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001dR\u0011\u0010\u0013\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001dR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001dR\u0011\u0010\u0015\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001fR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001b\u00a8\u0006C"}, d2 = {"Lcom/dairum/data/local/entity/WeeklyReflectionEntity;", "", "id", "", "weekStartDate", "Ljava/time/LocalDateTime;", "weekEndDate", "summary", "", "averageMood", "", "moodRangeMin", "", "moodRangeMax", "dominantMood", "moodDistributionJson", "moodTrend", "keyThemesJson", "highlightsJson", "suggestionsJson", "entryCount", "totalWordCount", "createdAt", "(JLjava/time/LocalDateTime;Ljava/time/LocalDateTime;Ljava/lang/String;FIILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IILjava/time/LocalDateTime;)V", "getAverageMood", "()F", "getCreatedAt", "()Ljava/time/LocalDateTime;", "getDominantMood", "()Ljava/lang/String;", "getEntryCount", "()I", "getHighlightsJson", "getId", "()J", "getKeyThemesJson", "getMoodDistributionJson", "getMoodRangeMax", "getMoodRangeMin", "getMoodTrend", "getSuggestionsJson", "getSummary", "getTotalWordCount", "getWeekEndDate", "getWeekStartDate", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "weekly_reflections", indices = {@androidx.room.Index(value = {"weekStartDate"}, unique = true)})
public final class WeeklyReflectionEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime weekStartDate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime weekEndDate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String summary = null;
    private final float averageMood = 0.0F;
    private final int moodRangeMin = 0;
    private final int moodRangeMax = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String dominantMood = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String moodDistributionJson = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String moodTrend = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String keyThemesJson = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String highlightsJson = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String suggestionsJson = null;
    private final int entryCount = 0;
    private final int totalWordCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime createdAt = null;
    
    public WeeklyReflectionEntity(long id, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime weekStartDate, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime weekEndDate, @org.jetbrains.annotations.NotNull()
    java.lang.String summary, float averageMood, int moodRangeMin, int moodRangeMax, @org.jetbrains.annotations.NotNull()
    java.lang.String dominantMood, @org.jetbrains.annotations.NotNull()
    java.lang.String moodDistributionJson, @org.jetbrains.annotations.NotNull()
    java.lang.String moodTrend, @org.jetbrains.annotations.NotNull()
    java.lang.String keyThemesJson, @org.jetbrains.annotations.NotNull()
    java.lang.String highlightsJson, @org.jetbrains.annotations.NotNull()
    java.lang.String suggestionsJson, int entryCount, int totalWordCount, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime getWeekStartDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime getWeekEndDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSummary() {
        return null;
    }
    
    public final float getAverageMood() {
        return 0.0F;
    }
    
    public final int getMoodRangeMin() {
        return 0;
    }
    
    public final int getMoodRangeMax() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDominantMood() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMoodDistributionJson() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMoodTrend() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getKeyThemesJson() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHighlightsJson() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSuggestionsJson() {
        return null;
    }
    
    public final int getEntryCount() {
        return 0;
    }
    
    public final int getTotalWordCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime getCreatedAt() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    public final int component14() {
        return 0;
    }
    
    public final int component15() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final float component5() {
        return 0.0F;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.entity.WeeklyReflectionEntity copy(long id, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime weekStartDate, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime weekEndDate, @org.jetbrains.annotations.NotNull()
    java.lang.String summary, float averageMood, int moodRangeMin, int moodRangeMax, @org.jetbrains.annotations.NotNull()
    java.lang.String dominantMood, @org.jetbrains.annotations.NotNull()
    java.lang.String moodDistributionJson, @org.jetbrains.annotations.NotNull()
    java.lang.String moodTrend, @org.jetbrains.annotations.NotNull()
    java.lang.String keyThemesJson, @org.jetbrains.annotations.NotNull()
    java.lang.String highlightsJson, @org.jetbrains.annotations.NotNull()
    java.lang.String suggestionsJson, int entryCount, int totalWordCount, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt) {
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