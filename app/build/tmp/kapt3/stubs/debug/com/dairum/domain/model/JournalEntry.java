package com.dairum.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model for a journal entry
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b5\b\u0086\b\u0018\u00002\u00020\u0001B\u00eb\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0013\u0012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u0012\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015\u0012\u000e\b\u0002\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0015\u0012\u0014\b\u0002\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u001c\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u001e\u0012\b\b\u0002\u0010\u001f\u001a\u00020\u001e\u0012\b\b\u0002\u0010 \u001a\u00020\u001e\u0012\b\b\u0002\u0010!\u001a\u00020\u001e\u0012\b\b\u0002\u0010\"\u001a\u00020#\u00a2\u0006\u0002\u0010$J\t\u0010@\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010A\u001a\u0004\u0018\u00010\u0013H\u00c6\u0003J\u000f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015H\u00c6\u0003J\u000f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015H\u00c6\u0003J\u000f\u0010D\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0015H\u00c6\u0003J\u0015\u0010E\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u001cH\u00c6\u0003J\t\u0010F\u001a\u00020\u001eH\u00c6\u0003J\t\u0010G\u001a\u00020\u001eH\u00c6\u0003J\t\u0010H\u001a\u00020\u001eH\u00c6\u0003J\t\u0010I\u001a\u00020\u001eH\u00c6\u0003J\t\u0010J\u001a\u00020#H\u00c6\u0003J\u000b\u0010K\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010L\u001a\u00020\u0007H\u00c6\u0003J\t\u0010M\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010N\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u0010O\u001a\u00020\fH\u00c6\u0003J\t\u0010P\u001a\u00020\fH\u00c6\u0003J\u000b\u0010Q\u001a\u0004\u0018\u00010\u000fH\u00c6\u0003J\u000b\u0010R\u001a\u0004\u0018\u00010\u0011H\u00c6\u0003J\u00ef\u0001\u0010S\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\f2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u00152\u000e\b\u0002\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00152\u0014\b\u0002\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u001e2\b\b\u0002\u0010 \u001a\u00020\u001e2\b\b\u0002\u0010!\u001a\u00020\u001e2\b\b\u0002\u0010\"\u001a\u00020#H\u00c6\u0001J\u0013\u0010T\u001a\u00020\u001e2\b\u0010U\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010V\u001a\u00020#H\u00d6\u0001J\t\u0010W\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0011\u0010\u001f\u001a\u00020\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010-R\u0011\u0010 \u001a\u00020\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010-R\u0011\u0010\u001d\u001a\u00020\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010-R\u0011\u0010!\u001a\u00020\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010-R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u00103R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u00101R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u00106R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00101R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u00106R\u001d\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010:R\u0011\u0010\r\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010*R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010=R\u0011\u0010\"\u001a\u00020#\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010?\u00a8\u0006X"}, d2 = {"Lcom/dairum/domain/model/JournalEntry;", "", "id", "", "title", "", "content", "Lcom/dairum/domain/model/RichContent;", "plainContent", "category", "Lcom/dairum/domain/model/Category;", "createdAt", "Ljava/time/LocalDateTime;", "updatedAt", "mood", "Lcom/dairum/domain/model/Mood;", "weather", "Lcom/dairum/domain/model/Weather;", "location", "Lcom/dairum/domain/model/Location;", "media", "", "Lcom/dairum/domain/model/Media;", "tags", "Lcom/dairum/domain/model/Tag;", "people", "Lcom/dairum/domain/model/Person;", "trackerValues", "", "isFavorite", "", "isArchived", "isDraft", "isPinned", "wordCount", "", "(JLjava/lang/String;Lcom/dairum/domain/model/RichContent;Ljava/lang/String;Lcom/dairum/domain/model/Category;Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;Lcom/dairum/domain/model/Mood;Lcom/dairum/domain/model/Weather;Lcom/dairum/domain/model/Location;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/Map;ZZZZI)V", "getCategory", "()Lcom/dairum/domain/model/Category;", "getContent", "()Lcom/dairum/domain/model/RichContent;", "getCreatedAt", "()Ljava/time/LocalDateTime;", "getId", "()J", "()Z", "getLocation", "()Lcom/dairum/domain/model/Location;", "getMedia", "()Ljava/util/List;", "getMood", "()Lcom/dairum/domain/model/Mood;", "getPeople", "getPlainContent", "()Ljava/lang/String;", "getTags", "getTitle", "getTrackerValues", "()Ljava/util/Map;", "getUpdatedAt", "getWeather", "()Lcom/dairum/domain/model/Weather;", "getWordCount", "()I", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class JournalEntry {
    private final long id = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.RichContent content = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String plainContent = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Category category = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime createdAt = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDateTime updatedAt = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Mood mood = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Weather weather = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.domain.model.Location location = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Media> media = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Tag> tags = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.Person> people = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.Long, java.lang.String> trackerValues = null;
    private final boolean isFavorite = false;
    private final boolean isArchived = false;
    private final boolean isDraft = false;
    private final boolean isPinned = false;
    private final int wordCount = 0;
    
    public JournalEntry(long id, @org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.RichContent content, @org.jetbrains.annotations.NotNull()
    java.lang.String plainContent, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood mood, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Weather weather, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Media> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> trackerValues, boolean isFavorite, boolean isArchived, boolean isDraft, boolean isPinned, int wordCount) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.RichContent getContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPlainContent() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Category getCategory() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Mood getMood() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Weather getWeather() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Location getLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Media> getMedia() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> getTags() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> getPeople() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Long, java.lang.String> getTrackerValues() {
        return null;
    }
    
    public final boolean isFavorite() {
        return false;
    }
    
    public final boolean isArchived() {
        return false;
    }
    
    public final boolean isDraft() {
        return false;
    }
    
    public final boolean isPinned() {
        return false;
    }
    
    public final int getWordCount() {
        return 0;
    }
    
    public JournalEntry() {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Location component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Media> component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Long, java.lang.String> component14() {
        return null;
    }
    
    public final boolean component15() {
        return false;
    }
    
    public final boolean component16() {
        return false;
    }
    
    public final boolean component17() {
        return false;
    }
    
    public final boolean component18() {
        return false;
    }
    
    public final int component19() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.RichContent component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Category component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Mood component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Weather component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.JournalEntry copy(long id, @org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.RichContent content, @org.jetbrains.annotations.NotNull()
    java.lang.String plainContent, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood mood, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Weather weather, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Media> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> trackerValues, boolean isFavorite, boolean isArchived, boolean isDraft, boolean isPinned, int wordCount) {
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