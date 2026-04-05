package com.dairum.data.local.converter;

import androidx.room.TypeConverter;
import com.dairum.data.local.entity.ExportFormat;
import com.dairum.data.local.entity.MediaType;
import com.dairum.data.local.entity.TrackerType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0019\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\u0005\u001a\u0004\u0018\u00010\tH\u0007\u00a2\u0006\u0002\u0010\nJ\u001a\u0010\u000b\u001a\u0004\u0018\u00010\u00042\u000e\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\fH\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000eH\u0007J\u001a\u0010\u000f\u001a\u0004\u0018\u00010\u00042\u000e\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\fH\u0007J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0011H\u0007J\u0010\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0019\u0010\u0013\u001a\u0004\u0018\u00010\t2\b\u0010\u0005\u001a\u0004\u0018\u00010\bH\u0007\u00a2\u0006\u0002\u0010\u0014J\u001a\u0010\u0015\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\f2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0007J\u0010\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u001a\u0010\u0017\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\f2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0007J\u0010\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u0004H\u0007\u00a8\u0006\u0019"}, d2 = {"Lcom/dairum/data/local/converter/Converters;", "", "()V", "fromExportFormat", "", "value", "Lcom/dairum/data/local/entity/ExportFormat;", "fromLocalDateTime", "", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;)Ljava/lang/Long;", "fromLongList", "", "fromMediaType", "Lcom/dairum/data/local/entity/MediaType;", "fromStringList", "fromTrackerType", "Lcom/dairum/data/local/entity/TrackerType;", "toExportFormat", "toLocalDateTime", "(Ljava/lang/Long;)Ljava/time/LocalDateTime;", "toLongList", "toMediaType", "toStringList", "toTrackerType", "app_debug"})
public final class Converters {
    
    public Converters() {
        super();
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long fromLocalDateTime(@org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime toLocalDateTime(@org.jetbrains.annotations.Nullable()
    java.lang.Long value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromMediaType(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.MediaType value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.entity.MediaType toMediaType(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromTrackerType(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.TrackerType value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.entity.TrackerType toTrackerType(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromExportFormat(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.ExportFormat value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.entity.ExportFormat toExportFormat(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String fromLongList(@org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.Long> value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.Long> toLongList(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String fromStringList(@org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.String> value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.String> toStringList(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
        return null;
    }
}