package com.dairum.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model for a journal entry
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b:\b\u0086\b\u0018\u00002\u00020\u0001B\u0081\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\r\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016\u0012\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0016\u0012\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0016\u0012\u0014\b\u0002\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u001d\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u001f\u0012\b\b\u0002\u0010 \u001a\u00020\u001f\u0012\b\b\u0002\u0010!\u001a\u00020\u001f\u0012\b\b\u0002\u0010\"\u001a\u00020\u001f\u0012\b\b\u0002\u0010#\u001a\u00020$\u0012\n\b\u0002\u0010%\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010&J\t\u0010D\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010E\u001a\u0004\u0018\u00010\u0012H\u00c6\u0003J\u000b\u0010F\u001a\u0004\u0018\u00010\u0014H\u00c6\u0003J\u000f\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u00c6\u0003J\u000f\u0010H\u001a\b\u0012\u0004\u0012\u00020\u00190\u0016H\u00c6\u0003J\u000f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0016H\u00c6\u0003J\u0015\u0010J\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u001dH\u00c6\u0003J\t\u0010K\u001a\u00020\u001fH\u00c6\u0003J\t\u0010L\u001a\u00020\u001fH\u00c6\u0003J\t\u0010M\u001a\u00020\u001fH\u00c6\u0003J\t\u0010N\u001a\u00020\u001fH\u00c6\u0003J\u000b\u0010O\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010P\u001a\u00020$H\u00c6\u0003J\u000b\u0010Q\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010R\u001a\u00020\u0007H\u00c6\u0003J\t\u0010S\u001a\u00020\u0005H\u00c6\u0003J\t\u0010T\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010U\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u0010V\u001a\u00020\rH\u00c6\u0003J\t\u0010W\u001a\u00020\rH\u00c6\u0003J\u000b\u0010X\u001a\u0004\u0018\u00010\u0010H\u00c6\u0003J\u0085\u0002\u0010Y\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\r2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u00162\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00162\u0014\b\u0002\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010 \u001a\u00020\u001f2\b\b\u0002\u0010!\u001a\u00020\u001f2\b\b\u0002\u0010\"\u001a\u00020\u001f2\b\b\u0002\u0010#\u001a\u00020$2\n\b\u0002\u0010%\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010Z\u001a\u00020\u001f2\b\u0010[\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\\\u001a\u00020$H\u00d6\u0001J\t\u0010]\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010%\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0011\u0010\t\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010(R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0011\u0010 \u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u00102R\u0011\u0010!\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u00102R\u0011\u0010\u001e\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u00102R\u0011\u0010\"\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u00102R\u0013\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u00106R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u00106R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010(R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u00106R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010(R\u001d\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010>R\u0011\u0010\u000e\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010.R\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010AR\u0011\u0010#\u001a\u00020$\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010C\u00a8\u0006^"}, d2 = {"Lcom/dairum/domain/model/JournalEntry;", "", "id", "", "title", "", "content", "Lcom/dairum/domain/model/RichContent;", "plainContent", "formattingJson", "category", "Lcom/dairum/domain/model/Category;", "createdAt", "Ljava/time/LocalDateTime;", "updatedAt", "mood", "Lcom/dairum/domain/model/Mood;", "weather", "Lcom/dairum/domain/model/Weather;", "location", "Lcom/dairum/domain/model/Location;", "media", "", "Lcom/dairum/domain/model/Media;", "tags", "Lcom/dairum/domain/model/Tag;", "people", "Lcom/dairum/domain/model/Person;", "trackerValues", "", "isFavorite", "", "isArchived", "isDraft", "isPinned", "wordCount", "", "backgroundColor", "(JLjava/lang/String;Lcom/dairum/domain/model/RichContent;Ljava/lang/String;Ljava/lang/String;Lcom/dairum/domain/model/Category;Ljava/time/LocalDateTime;Ljava/time/LocalDateTime;Lcom/dairum/domain/model/Mood;Lcom/dairum/domain/model/Weather;Lcom/dairum/domain/model/Location;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/Map;ZZZZILjava/lang/String;)V", "getBackgroundColor", "()Ljava/lang/String;", "getCategory", "()Lcom/dairum/domain/model/Category;", "getContent", "()Lcom/dairum/domain/model/RichContent;", "getCreatedAt", "()Ljava/time/LocalDateTime;", "getFormattingJson", "getId", "()J", "()Z", "getLocation", "()Lcom/dairum/domain/model/Location;", "getMedia", "()Ljava/util/List;", "getMood", "()Lcom/dairum/domain/model/Mood;", "getPeople", "getPlainContent", "getTags", "getTitle", "getTrackerValues", "()Ljava/util/Map;", "getUpdatedAt", "getWeather", "()Lcom/dairum/domain/model/Weather;", "getWordCount", "()I", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class JournalEntry {
    private final long id = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.RichContent content = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String plainContent = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String formattingJson = null;
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
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String backgroundColor = null;
    
    public JournalEntry(long id, @org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.RichContent content, @org.jetbrains.annotations.NotNull()
    java.lang.String plainContent, @org.jetbrains.annotations.NotNull()
    java.lang.String formattingJson, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood mood, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Weather weather, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Media> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> trackerValues, boolean isFavorite, boolean isArchived, boolean isDraft, boolean isPinned, int wordCount, @org.jetbrains.annotations.Nullable()
    java.lang.String backgroundColor) {
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFormattingJson() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getBackgroundColor() {
        return null;
    }
    
    public JournalEntry() {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Weather component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Location component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Media> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Tag> component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.Person> component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Long, java.lang.String> component15() {
        return null;
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
    
    public final boolean component19() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    public final int component20() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component21() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Category component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.domain.model.Mood component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.JournalEntry copy(long id, @org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.RichContent content, @org.jetbrains.annotations.NotNull()
    java.lang.String plainContent, @org.jetbrains.annotations.NotNull()
    java.lang.String formattingJson, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime createdAt, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime updatedAt, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Mood mood, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Weather weather, @org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.Location location, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Media> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Tag> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.Person> people, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> trackerValues, boolean isFavorite, boolean isArchived, boolean isDraft, boolean isPinned, int wordCount, @org.jetbrains.annotations.Nullable()
    java.lang.String backgroundColor) {
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