package com.dairum.presentation.screens.memories;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MemoryRepository;
import com.dairum.domain.repository.MediaRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b0\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u0089\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\u0006\u0010\u0010\u001a\u00020\u0003\u0012\b\u0010\u0011\u001a\u0004\u0018\u00010\f\u0012\u0006\u0010\u0012\u001a\u00020\u0003\u0012\u0006\u0010\u0013\u001a\u00020\u0003\u0012\u0006\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0015J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00100\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u00101\u001a\u00020\u0003H\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\u0006H\u00c6\u0003J\t\u00106\u001a\u00020\u0003H\u00c6\u0003J\u0010\u00107\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\"J\u0010\u00108\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0017J\u0010\u00109\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0017J\u000b\u0010:\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\u000b\u0010;\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003J\u00b0\u0001\u0010<\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010=J\u0013\u0010>\u001a\u00020?2\b\u0010@\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010A\u001a\u00020\u0003H\u00d6\u0001J\t\u0010B\u001a\u00020\u000eH\u00d6\u0001R\u0015\u0010\t\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001aR\u0013\u0010\u0011\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001aR\u0015\u0010\n\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b \u0010\u0017R\u0015\u0010\b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010#\u001a\u0004\b!\u0010\"R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001aR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001aR\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001aR\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001aR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,\u00a8\u0006C"}, d2 = {"Lcom/dairum/presentation/screens/memories/MemoryInsights;", "", "totalDays", "", "writingDays", "writingFrequency", "", "avgWordCount", "mostActiveHour", "avgMood", "moodVariability", "bestMoodEntry", "Lcom/dairum/domain/model/JournalEntry;", "mostProductiveDay", "", "photoCount", "entriesWithPhotos", "longestEntry", "longestStreak", "totalWords", "uniqueLocations", "(IIFILjava/lang/Integer;Ljava/lang/Float;Ljava/lang/Float;Lcom/dairum/domain/model/JournalEntry;Ljava/lang/String;IILcom/dairum/domain/model/JournalEntry;III)V", "getAvgMood", "()Ljava/lang/Float;", "Ljava/lang/Float;", "getAvgWordCount", "()I", "getBestMoodEntry", "()Lcom/dairum/domain/model/JournalEntry;", "getEntriesWithPhotos", "getLongestEntry", "getLongestStreak", "getMoodVariability", "getMostActiveHour", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getMostProductiveDay", "()Ljava/lang/String;", "getPhotoCount", "getTotalDays", "getTotalWords", "getUniqueLocations", "getWritingDays", "getWritingFrequency", "()F", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(IIFILjava/lang/Integer;Ljava/lang/Float;Ljava/lang/Float;Lcom/dairum/domain/model/JournalEntry;Ljava/lang/String;IILcom/dairum/domain/model/JournalEntry;III)Lcom/dairum/presentation/screens/memories/MemoryInsights;", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class MemoryInsights {
    private final int totalDays = 0;
    private final int writingDays = 0;
    private final float writingFrequency = 0.0F;
    private final int avgWordCount = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer mostActiveHour = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Float avgMood = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Float moodVariability = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.JournalEntry bestMoodEntry = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String mostProductiveDay = null;
    private final int photoCount = 0;
    private final int entriesWithPhotos = 0;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.JournalEntry longestEntry = null;
    private final int longestStreak = 0;
    private final int totalWords = 0;
    private final int uniqueLocations = 0;
    
    public MemoryInsights(int totalDays, int writingDays, float writingFrequency, int avgWordCount, @org.jetbrains.annotations.Nullable()
    java.lang.Integer mostActiveHour, @org.jetbrains.annotations.Nullable()
    java.lang.Float avgMood, @org.jetbrains.annotations.Nullable()
    java.lang.Float moodVariability, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.JournalEntry bestMoodEntry, @org.jetbrains.annotations.Nullable()
    java.lang.String mostProductiveDay, int photoCount, int entriesWithPhotos, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.JournalEntry longestEntry, int longestStreak, int totalWords, int uniqueLocations) {
        super();
    }
    
    public final int getTotalDays() {
        return 0;
    }
    
    public final int getWritingDays() {
        return 0;
    }
    
    public final float getWritingFrequency() {
        return 0.0F;
    }
    
    public final int getAvgWordCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getMostActiveHour() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Float getAvgMood() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Float getMoodVariability() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.JournalEntry getBestMoodEntry() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMostProductiveDay() {
        return null;
    }
    
    public final int getPhotoCount() {
        return 0;
    }
    
    public final int getEntriesWithPhotos() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.JournalEntry getLongestEntry() {
        return null;
    }
    
    public final int getLongestStreak() {
        return 0;
    }
    
    public final int getTotalWords() {
        return 0;
    }
    
    public final int getUniqueLocations() {
        return 0;
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
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.JournalEntry component12() {
        return null;
    }
    
    public final int component13() {
        return 0;
    }
    
    public final int component14() {
        return 0;
    }
    
    public final int component15() {
        return 0;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final float component3() {
        return 0.0F;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Float component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Float component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.JournalEntry component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.memories.MemoryInsights copy(int totalDays, int writingDays, float writingFrequency, int avgWordCount, @org.jetbrains.annotations.Nullable()
    java.lang.Integer mostActiveHour, @org.jetbrains.annotations.Nullable()
    java.lang.Float avgMood, @org.jetbrains.annotations.Nullable()
    java.lang.Float moodVariability, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.JournalEntry bestMoodEntry, @org.jetbrains.annotations.Nullable()
    java.lang.String mostProductiveDay, int photoCount, int entriesWithPhotos, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.JournalEntry longestEntry, int longestStreak, int totalWords, int uniqueLocations) {
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