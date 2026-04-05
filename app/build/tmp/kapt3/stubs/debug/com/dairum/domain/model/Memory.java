package com.dairum.domain.model;

import java.time.LocalDateTime;

/**
 * Memory - a collection of related journal entries
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\bE\b\u0086\b\u0018\u00002\u00020\u0001B\u00af\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0005\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u0014\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0011\u0012\u000e\b\u0002\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u0012\u000e\b\u0002\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00050\u0014\u0012\u0016\b\u0002\u0010\u001e\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u001f\u0012\u000e\b\u0002\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u0014\u0012\u000e\b\u0002\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u0014\u0012\u000e\b\u0002\u0010$\u001a\b\u0012\u0004\u0012\u00020%0\u0014\u00a2\u0006\u0002\u0010&J\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010P\u001a\u0004\u0018\u00010\u0011H\u00c6\u0003\u00a2\u0006\u0002\u0010,J\u000b\u0010Q\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000f\u0010R\u001a\b\u0012\u0004\u0012\u00020\u00050\u0014H\u00c6\u0003J\u000b\u0010S\u001a\u0004\u0018\u00010\u0016H\u00c6\u0003J\t\u0010T\u001a\u00020\u0003H\u00c6\u0003J\t\u0010U\u001a\u00020\u0011H\u00c6\u0003J\t\u0010V\u001a\u00020\u0011H\u00c6\u0003J\t\u0010W\u001a\u00020\u0011H\u00c6\u0003J\t\u0010X\u001a\u00020\u0011H\u00c6\u0003J\u000f\u0010Y\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010[\u001a\b\u0012\u0004\u0012\u00020\u00050\u0014H\u00c6\u0003J\u0017\u0010\\\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u001fH\u00c6\u0003J\u000f\u0010]\u001a\b\u0012\u0004\u0012\u00020!0\u0014H\u00c6\u0003J\u000f\u0010^\u001a\b\u0012\u0004\u0012\u00020#0\u0014H\u00c6\u0003J\u000f\u0010_\u001a\b\u0012\u0004\u0012\u00020%0\u0014H\u00c6\u0003J\u000b\u0010`\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010a\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010b\u001a\u00020\tH\u00c6\u0003J\t\u0010c\u001a\u00020\tH\u00c6\u0003J\u000b\u0010d\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010e\u001a\u0004\u0018\u00010\rH\u00c6\u0003J\t\u0010f\u001a\u00020\u000fH\u00c6\u0003J\u00be\u0002\u0010g\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00052\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u00032\b\b\u0002\u0010\u0018\u001a\u00020\u00112\b\b\u0002\u0010\u0019\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u00112\b\b\u0002\u0010\u001b\u001a\u00020\u00112\u000e\b\u0002\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u000e\b\u0002\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00050\u00142\u0016\b\u0002\u0010\u001e\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u001f2\u000e\b\u0002\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u00142\u000e\b\u0002\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u00142\u000e\b\u0002\u0010$\u001a\b\u0012\u0004\u0012\u00020%0\u0014H\u00c6\u0001\u00a2\u0006\u0002\u0010hJ\u0013\u0010i\u001a\u00020\u000f2\b\u0010j\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010k\u001a\u00020\u0011H\u00d6\u0001J\t\u0010l\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\'\u001a\u0004\u0018\u00010(8F\u00a2\u0006\u0006\u001a\u0004\b)\u0010*R\u0015\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u00a2\u0006\n\n\u0002\u0010-\u001a\u0004\b+\u0010,R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010/R\u0011\u00101\u001a\u00020\u00058F\u00a2\u0006\u0006\u001a\u0004\b2\u0010/R\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u00106R\u0011\u0010\u0018\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00050\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u00106R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010/R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010<R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010=R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010/R\u0013\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010@R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u00106R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010CR\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u00106R\u0011\u0010\u001a\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u00108R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u00104R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u00106R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010/R\u0011\u0010\u0017\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010<R\u001f\u0010\u001e\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u0010KR\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020%0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u00106R\u0011\u0010\u001b\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u00108R\u0011\u0010\u0019\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u00108\u00a8\u0006m"}, d2 = {"Lcom/dairum/domain/model/Memory;", "", "id", "", "title", "", "description", "coverImageUri", "startDate", "Ljava/time/LocalDateTime;", "endDate", "location", "locationData", "Lcom/dairum/domain/model/Location;", "isFavorite", "", "color", "", "icon", "tags", "", "moodSummary", "Lcom/dairum/domain/model/MoodSummary;", "totalTimeSpent", "entryCount", "wordCount", "photoCount", "videoCount", "peopleInvolved", "highlights", "trackerSummary", "", "entries", "Lcom/dairum/domain/model/JournalEntry;", "milestones", "Lcom/dairum/domain/model/MemoryMilestone;", "trackers", "Lcom/dairum/domain/model/MemoryTracker;", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;Ljava/lang/String;Lcom/dairum/domain/model/Location;ZLjava/lang/Integer;Ljava/lang/String;Ljava/util/List;Lcom/dairum/domain/model/MoodSummary;JIIIILjava/util/List;Ljava/util/List;Ljava/util/Map;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "averageMood", "", "getAverageMood", "()Ljava/lang/Float;", "getColor", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getCoverImageUri", "()Ljava/lang/String;", "getDescription", "duration", "getDuration", "getEndDate", "()Ljava/time/LocalDateTime;", "getEntries", "()Ljava/util/List;", "getEntryCount", "()I", "getHighlights", "getIcon", "getId", "()J", "()Z", "getLocation", "getLocationData", "()Lcom/dairum/domain/model/Location;", "getMilestones", "getMoodSummary", "()Lcom/dairum/domain/model/MoodSummary;", "getPeopleInvolved", "getPhotoCount", "getStartDate", "getTags", "getTitle", "getTotalTimeSpent", "getTrackerSummary", "()Ljava/util/Map;", "getTrackers", "getVideoCount", "getWordCount", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;Ljava/lang/String;Lcom/dairum/domain/model/Location;ZLjava/lang/Integer;Ljava/lang/String;Ljava/util/List;Lcom/dairum/domain/model/MoodSummary;JIIIILjava/util/List;Ljava/util/List;Ljava/util/Map;Ljava/util/List;Ljava/util/List;Ljava/util/List;)Lcom/dairum/domain/model/Memory;", "equals", "other", "hashCode", "toString", "app_debug"})
public final class Memory {
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String description = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String coverImageUri = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime startDate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime endDate = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String location = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Location locationData = null;
    private final boolean isFavorite = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer color = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String icon = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> tags = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.MoodSummary moodSummary = null;
    private final long totalTimeSpent = 0L;
    private final int entryCount = 0;
    private final int wordCount = 0;
    private final int photoCount = 0;
    private final int videoCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Long> peopleInvolved = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> highlights = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.Map<java.lang.String, java.lang.String> trackerSummary = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.JournalEntry> entries = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.MemoryMilestone> milestones = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.MemoryTracker> trackers = null;
    
    public Memory(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.lang.String coverImageUri, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startDate, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endDate, @org.jetbrains.annotations.Nullable()
    java.lang.String location, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location locationData, boolean isFavorite, @org.jetbrains.annotations.Nullable()
    java.lang.Integer color, @org.jetbrains.annotations.Nullable()
    java.lang.String icon, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> tags, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.MoodSummary moodSummary, long totalTimeSpent, int entryCount, int wordCount, int photoCount, int videoCount, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> peopleInvolved, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> highlights, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, java.lang.String> trackerSummary, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.MemoryMilestone> milestones, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.MemoryTracker> trackers) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCoverImageUri() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime getStartDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime getEndDate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Location getLocationData() {
        return null;
    }
    
    public final boolean isFavorite() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getColor() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getIcon() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getTags() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.MoodSummary getMoodSummary() {
        return null;
    }
    
    public final long getTotalTimeSpent() {
        return 0L;
    }
    
    public final int getEntryCount() {
        return 0;
    }
    
    public final int getWordCount() {
        return 0;
    }
    
    public final int getPhotoCount() {
        return 0;
    }
    
    public final int getVideoCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Long> getPeopleInvolved() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getHighlights() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Map<java.lang.String, java.lang.String> getTrackerSummary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> getEntries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.MemoryMilestone> getMilestones() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.MemoryTracker> getTrackers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDuration() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Float getAverageMood() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.MoodSummary component13() {
        return null;
    }
    
    public final long component14() {
        return 0L;
    }
    
    public final int component15() {
        return 0;
    }
    
    public final int component16() {
        return 0;
    }
    
    public final int component17() {
        return 0;
    }
    
    public final int component18() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Long> component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component20() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Map<java.lang.String, java.lang.String> component21() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.JournalEntry> component22() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.MemoryMilestone> component23() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.MemoryTracker> component24() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Location component8() {
        return null;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.Memory copy(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.lang.String coverImageUri, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startDate, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endDate, @org.jetbrains.annotations.Nullable()
    java.lang.String location, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location locationData, boolean isFavorite, @org.jetbrains.annotations.Nullable()
    java.lang.Integer color, @org.jetbrains.annotations.Nullable()
    java.lang.String icon, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> tags, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.MoodSummary moodSummary, long totalTimeSpent, int entryCount, int wordCount, int photoCount, int videoCount, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> peopleInvolved, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> highlights, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, java.lang.String> trackerSummary, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.JournalEntry> entries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.MemoryMilestone> milestones, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.MemoryTracker> trackers) {
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