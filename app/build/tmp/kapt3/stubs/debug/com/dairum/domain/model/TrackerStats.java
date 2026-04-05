package com.dairum.domain.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BA\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\b\u0012\b\u0010\n\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u000fJ\u0010\u0010\u001c\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u000fJ\u0010\u0010\u001d\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001e\u001a\u00020\fH\u00c6\u0003JV\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001\u00a2\u0006\u0002\u0010 J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020\fH\u00d6\u0001J\t\u0010%\u001a\u00020&H\u00d6\u0001R\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0015\u0010\n\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u0013\u0010\u000fR\u0015\u0010\t\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u0014\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006\'"}, d2 = {"Lcom/dairum/domain/model/TrackerStats;", "", "tracker", "Lcom/dairum/domain/model/CustomTracker;", "values", "", "Lcom/dairum/domain/model/TrackerDataPoint;", "average", "", "min", "max", "count", "", "(Lcom/dairum/domain/model/CustomTracker;Ljava/util/List;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;I)V", "getAverage", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getCount", "()I", "getMax", "getMin", "getTracker", "()Lcom/dairum/domain/model/CustomTracker;", "getValues", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(Lcom/dairum/domain/model/CustomTracker;Ljava/util/List;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;I)Lcom/dairum/domain/model/TrackerStats;", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class TrackerStats {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.CustomTracker tracker = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.TrackerDataPoint> values = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double average = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double min = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double max = null;
    private final int count = 0;
    
    public TrackerStats(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.CustomTracker tracker, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.TrackerDataPoint> values, @org.jetbrains.annotations.Nullable()
    java.lang.Double average, @org.jetbrains.annotations.Nullable()
    java.lang.Double min, @org.jetbrains.annotations.Nullable()
    java.lang.Double max, int count) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.CustomTracker getTracker() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.TrackerDataPoint> getValues() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getAverage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getMin() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getMax() {
        return null;
    }
    
    public final int getCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.CustomTracker component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.TrackerDataPoint> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component5() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.TrackerStats copy(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.CustomTracker tracker, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.TrackerDataPoint> values, @org.jetbrains.annotations.Nullable()
    java.lang.Double average, @org.jetbrains.annotations.Nullable()
    java.lang.Double min, @org.jetbrains.annotations.Nullable()
    java.lang.Double max, int count) {
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