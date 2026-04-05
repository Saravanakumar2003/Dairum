package com.dairum.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.dairum.domain.model.NotificationPriority;
import com.dairum.domain.model.ReminderType;
import com.dairum.domain.model.SmartReminderType;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2 = {"Lcom/dairum/data/local/entity/MemoryTrackerType;", "", "(Ljava/lang/String;I)V", "COUNT", "DURATION", "DISTANCE", "EXPENSE", "RATING", "CUSTOM", "app_debug"})
public enum MemoryTrackerType {
    /*public static final*/ COUNT /* = new COUNT() */,
    /*public static final*/ DURATION /* = new DURATION() */,
    /*public static final*/ DISTANCE /* = new DISTANCE() */,
    /*public static final*/ EXPENSE /* = new EXPENSE() */,
    /*public static final*/ RATING /* = new RATING() */,
    /*public static final*/ CUSTOM /* = new CUSTOM() */;
    
    MemoryTrackerType() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.data.local.entity.MemoryTrackerType> getEntries() {
        return null;
    }
}