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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b%\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u00c9\u0001\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u001a\b\u0002\u0010\u0007\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0005\u0012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0005\u0012\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0005\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0005\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0005\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u001c\u00a2\u0006\u0002\u0010\u001dJ\u000b\u00101\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u00102\u001a\u0004\u0018\u00010\u0016H\u00c6\u0003J\t\u00103\u001a\u00020\u0018H\u00c6\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u001aH\u00c6\u0003J\t\u00105\u001a\u00020\u001cH\u00c6\u0003J\u000f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u001b\u00107\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\bH\u00c6\u0003J\u000f\u00108\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u00109\u001a\b\u0012\u0004\u0012\u00020\f0\u0005H\u00c6\u0003J\u000f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0005H\u00c6\u0003J\u000f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00100\u0005H\u00c6\u0003J\u000f\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00120\u0005H\u00c6\u0003J\u000f\u0010=\u001a\b\u0012\u0004\u0012\u00020\u00140\u0005H\u00c6\u0003J\u00cd\u0001\u0010>\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u001a\b\u0002\u0010\u0007\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00052\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00052\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u00052\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u00052\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u00052\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u00182\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\b\u0002\u0010\u001b\u001a\u00020\u001cH\u00c6\u0001J\u0013\u0010?\u001a\u00020\u00182\b\u0010@\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010A\u001a\u00020BH\u00d6\u0001J\t\u0010C\u001a\u00020\u001aH\u00d6\u0001R\u0011\u0010\u001b\u001a\u00020\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010!R#\u0010\u0007\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0013\u0010\u0019\u001a\u0004\u0018\u00010\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010)R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010!R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010!R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010!R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010!R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010!\u00a8\u0006D"}, d2 = {"Lcom/dairum/presentation/screens/memories/MemoryDetailUiState;", "", "memory", "Lcom/dairum/domain/model/Memory;", "entries", "", "Lcom/dairum/domain/model/JournalEntry;", "entriesByDate", "", "Ljava/time/LocalDate;", "availableEntries", "photos", "Lcom/dairum/domain/model/Media;", "locations", "Lcom/dairum/domain/model/Location;", "people", "Lcom/dairum/domain/model/Person;", "tags", "Lcom/dairum/domain/model/Tag;", "moodJourney", "Lcom/dairum/presentation/screens/memories/MoodPoint;", "insights", "Lcom/dairum/presentation/screens/memories/MemoryInsights;", "isLoading", "", "error", "", "activeTab", "Lcom/dairum/presentation/screens/memories/MemoryTab;", "(Lcom/dairum/domain/model/Memory;Ljava/util/List;Ljava/util/Map;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lcom/dairum/presentation/screens/memories/MemoryInsights;ZLjava/lang/String;Lcom/dairum/presentation/screens/memories/MemoryTab;)V", "getActiveTab", "()Lcom/dairum/presentation/screens/memories/MemoryTab;", "getAvailableEntries", "()Ljava/util/List;", "getEntries", "getEntriesByDate", "()Ljava/util/Map;", "getError", "()Ljava/lang/String;", "getInsights", "()Lcom/dairum/presentation/screens/memories/MemoryInsights;", "()Z", "getLocations", "getMemory", "()Lcom/dairum/domain/model/Memory;", "getMoodJourney", "getPeople", "getPhotos", "getTags", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class MemoryDetailUiState {
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Memory memory = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.JournalEntry> entries = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.time.LocalDate, java.util.List<com.dairum.domain.model.JournalEntry>> entriesByDate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.JournalEntry> availableEntries = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Media> photos = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Location> locations = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Person> people = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Tag> tags = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.presentation.screens.memories.MoodPoint> moodJourney = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.presentation.screens.memories.MemoryInsights insights = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.presentation.screens.memories.MemoryTab activeTab = null;
    
    public MemoryDetailUiState(@org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Memory memory, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.time.LocalDate, ? extends java.util.List<com.dairum.domain.model.JournalEntry>> entriesByDate, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> availableEntries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Media> photos, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Location> locations, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.presentation.screens.memories.MoodPoint> moodJourney, @org.jetbrains.annotations.Nullable()
    com.dairum.presentation.screens.memories.MemoryInsights insights, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.memories.MemoryTab activeTab) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Memory getMemory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> getEntries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.time.LocalDate, java.util.List<com.dairum.domain.model.JournalEntry>> getEntriesByDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> getAvailableEntries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Media> getPhotos() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Location> getLocations() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> getPeople() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> getTags() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.presentation.screens.memories.MoodPoint> getMoodJourney() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.presentation.screens.memories.MemoryInsights getInsights() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.memories.MemoryTab getActiveTab() {
        return null;
    }
    
    public MemoryDetailUiState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Memory component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.presentation.screens.memories.MemoryInsights component10() {
        return null;
    }
    
    public final boolean component11() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.memories.MemoryTab component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.time.LocalDate, java.util.List<com.dairum.domain.model.JournalEntry>> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Media> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Location> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.presentation.screens.memories.MoodPoint> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.memories.MemoryDetailUiState copy(@org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Memory memory, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.time.LocalDate, ? extends java.util.List<com.dairum.domain.model.JournalEntry>> entriesByDate, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> availableEntries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Media> photos, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Location> locations, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.presentation.screens.memories.MoodPoint> moodJourney, @org.jetbrains.annotations.Nullable()
    com.dairum.presentation.screens.memories.MemoryInsights insights, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.memories.MemoryTab activeTab) {
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