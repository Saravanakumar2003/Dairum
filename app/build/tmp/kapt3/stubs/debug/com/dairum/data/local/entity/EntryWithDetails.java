package com.dairum.data.local.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

/**
 * Entry with all related data
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BO\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0007\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0007\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\n0\u0007H\u00c6\u0003J\u000f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\f0\u0007H\u00c6\u0003J\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0007H\u00c6\u0003J_\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00072\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00072\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0007H\u00c6\u0001J\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020$H\u00d6\u0001J\t\u0010%\u001a\u00020&H\u00d6\u0001R\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001c\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u001c\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015R\u001c\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0015R\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0015\u00a8\u0006\'"}, d2 = {"Lcom/dairum/data/local/entity/EntryWithDetails;", "", "entry", "Lcom/dairum/data/local/entity/JournalEntryEntity;", "category", "Lcom/dairum/data/local/entity/CategoryEntity;", "media", "", "Lcom/dairum/data/local/entity/MediaEntity;", "tags", "Lcom/dairum/data/local/entity/TagEntity;", "people", "Lcom/dairum/data/local/entity/PersonEntity;", "trackerValues", "Lcom/dairum/data/local/entity/TrackerValueEntity;", "(Lcom/dairum/data/local/entity/JournalEntryEntity;Lcom/dairum/data/local/entity/CategoryEntity;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getCategory", "()Lcom/dairum/data/local/entity/CategoryEntity;", "getEntry", "()Lcom/dairum/data/local/entity/JournalEntryEntity;", "getMedia", "()Ljava/util/List;", "getPeople", "getTags", "getTrackerValues", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class EntryWithDetails {
    @androidx.room.Embedded()
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.entity.JournalEntryEntity entry = null;
    @androidx.room.Relation(parentColumn = "categoryId", entityColumn = "id")
    @org.jetbrains.annotations.Nullable()
    private final com.dairum.data.local.entity.CategoryEntity category = null;
    @androidx.room.Relation(parentColumn = "id", entityColumn = "entryId")
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.data.local.entity.MediaEntity> media = null;
    @androidx.room.Relation(parentColumn = "id", entityColumn = "id", associateBy = @androidx.room.Junction(value = com.dairum.data.local.entity.EntryTagCrossRef.class, parentColumn = "entryId", entityColumn = "tagId"))
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.data.local.entity.TagEntity> tags = null;
    @androidx.room.Relation(parentColumn = "id", entityColumn = "id", associateBy = @androidx.room.Junction(value = com.dairum.data.local.entity.EntryPersonCrossRef.class, parentColumn = "entryId", entityColumn = "personId"))
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.data.local.entity.PersonEntity> people = null;
    @androidx.room.Relation(parentColumn = "id", entityColumn = "entryId")
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.data.local.entity.TrackerValueEntity> trackerValues = null;
    
    public EntryWithDetails(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.JournalEntryEntity entry, @org.jetbrains.annotations.Nullable()
    com.dairum.data.local.entity.CategoryEntity category, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.MediaEntity> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.TagEntity> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.PersonEntity> people, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.TrackerValueEntity> trackerValues) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.entity.JournalEntryEntity getEntry() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.data.local.entity.CategoryEntity getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.data.local.entity.MediaEntity> getMedia() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.data.local.entity.TagEntity> getTags() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.data.local.entity.PersonEntity> getPeople() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.data.local.entity.TrackerValueEntity> getTrackerValues() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.entity.JournalEntryEntity component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dairum.data.local.entity.CategoryEntity component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.data.local.entity.MediaEntity> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.data.local.entity.TagEntity> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.data.local.entity.PersonEntity> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.data.local.entity.TrackerValueEntity> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.entity.EntryWithDetails copy(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.JournalEntryEntity entry, @org.jetbrains.annotations.Nullable()
    com.dairum.data.local.entity.CategoryEntity category, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.MediaEntity> media, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.TagEntity> tags, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.PersonEntity> people, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.data.local.entity.TrackerValueEntity> trackerValues) {
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