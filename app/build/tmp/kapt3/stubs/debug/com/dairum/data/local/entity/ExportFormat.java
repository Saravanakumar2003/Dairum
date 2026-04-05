package com.dairum.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.dairum.domain.model.NotificationPriority;
import com.dairum.domain.model.ReminderType;
import com.dairum.domain.model.SmartReminderType;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/dairum/data/local/entity/ExportFormat;", "", "(Ljava/lang/String;I)V", "TXT", "PDF", "app_debug"})
public enum ExportFormat {
    /*public static final*/ TXT /* = new TXT() */,
    /*public static final*/ PDF /* = new PDF() */;
    
    ExportFormat() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.data.local.entity.ExportFormat> getEntries() {
        return null;
    }
}