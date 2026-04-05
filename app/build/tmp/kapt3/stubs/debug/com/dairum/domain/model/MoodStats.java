package com.dairum.domain.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010$\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BG\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00060\f\u00a2\u0006\u0002\u0010\rJ\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0006H\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00c6\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\nH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0016J\u0015\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00060\fH\u00c6\u0003JZ\u0010\u001d\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\u0014\b\u0002\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00060\fH\u00c6\u0001\u00a2\u0006\u0002\u0010\u001eJ\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020\nH\u00d6\u0001J\t\u0010#\u001a\u00020$H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001d\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00060\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u0015\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\n\n\u0002\u0010\u0017\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006%"}, d2 = {"Lcom/dairum/domain/model/MoodStats;", "", "distribution", "", "Lcom/dairum/domain/model/MoodDistributionItem;", "averageMood", "", "moodTrend", "Lcom/dairum/domain/model/MoodTrendPoint;", "mostCommonMood", "", "moodByDayOfWeek", "", "(Ljava/util/List;DLjava/util/List;Ljava/lang/Integer;Ljava/util/Map;)V", "getAverageMood", "()D", "getDistribution", "()Ljava/util/List;", "getMoodByDayOfWeek", "()Ljava/util/Map;", "getMoodTrend", "getMostCommonMood", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "copy", "(Ljava/util/List;DLjava/util/List;Ljava/lang/Integer;Ljava/util/Map;)Lcom/dairum/domain/model/MoodStats;", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class MoodStats {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.MoodDistributionItem> distribution = null;
    private final double averageMood = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dairum.domain.model.MoodTrendPoint> moodTrend = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer mostCommonMood = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.Integer, java.lang.Double> moodByDayOfWeek = null;
    
    public MoodStats(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.MoodDistributionItem> distribution, double averageMood, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.MoodTrendPoint> moodTrend, @org.jetbrains.annotations.Nullable()
    java.lang.Integer mostCommonMood, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Integer, java.lang.Double> moodByDayOfWeek) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.MoodDistributionItem> getDistribution() {
        return null;
    }
    
    public final double getAverageMood() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.MoodTrendPoint> getMoodTrend() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getMostCommonMood() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Integer, java.lang.Double> getMoodByDayOfWeek() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.MoodDistributionItem> component1() {
        return null;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dairum.domain.model.MoodTrendPoint> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Integer, java.lang.Double> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.MoodStats copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.MoodDistributionItem> distribution, double averageMood, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.MoodTrendPoint> moodTrend, @org.jetbrains.annotations.Nullable()
    java.lang.Integer mostCommonMood, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Integer, java.lang.Double> moodByDayOfWeek) {
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