package com.dairum.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.dairum.domain.model.NotificationPriority;
import com.dairum.domain.model.ReminderType;
import com.dairum.domain.model.SmartReminderType;

/**
 * Junction table for entry-tag many-to-many relationship
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u0006\u0013"}, d2 = {"Lcom/dairum/data/local/entity/EntryTagCrossRef;", "", "entryId", "", "tagId", "(JJ)V", "getEntryId", "()J", "getTagId", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
@androidx.room.Entity(tableName = "entry_tags", primaryKeys = {"entryId", "tagId"}, foreignKeys = {@androidx.room.ForeignKey(entity = com.dairum.data.local.entity.JournalEntryEntity.class, parentColumns = {"id"}, childColumns = {"entryId"}, onDelete = 5), @androidx.room.ForeignKey(entity = com.dairum.data.local.entity.TagEntity.class, parentColumns = {"id"}, childColumns = {"tagId"}, onDelete = 5)}, indices = {@androidx.room.Index(value = {"entryId"}), @androidx.room.Index(value = {"tagId"})})
public final class EntryTagCrossRef {
    private final long entryId = 0L;
    private final long tagId = 0L;
    
    public EntryTagCrossRef(long entryId, long tagId) {
        super();
    }
    
    public final long getEntryId() {
        return 0L;
    }
    
    public final long getTagId() {
        return 0L;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.entity.EntryTagCrossRef copy(long entryId, long tagId) {
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