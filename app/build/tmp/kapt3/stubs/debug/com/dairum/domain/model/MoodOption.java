package com.dairum.domain.model;

import java.time.LocalDateTime;

/**
 * Extended mood options with emojis
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b(\b\u0086\u0081\u0002\u0018\u0000 /2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001/B\'\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rj\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019j\u0002\b\u001aj\u0002\b\u001bj\u0002\b\u001cj\u0002\b\u001dj\u0002\b\u001ej\u0002\b\u001fj\u0002\b j\u0002\b!j\u0002\b\"j\u0002\b#j\u0002\b$j\u0002\b%j\u0002\b&j\u0002\b\'j\u0002\b(j\u0002\b)j\u0002\b*j\u0002\b+j\u0002\b,j\u0002\b-j\u0002\b.\u00a8\u00060"}, d2 = {"Lcom/dairum/domain/model/MoodOption;", "", "id", "", "label", "", "emoji", "category", "Lcom/dairum/domain/model/MoodCategory;", "(Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Lcom/dairum/domain/model/MoodCategory;)V", "getCategory", "()Lcom/dairum/domain/model/MoodCategory;", "getEmoji", "()Ljava/lang/String;", "getId", "()I", "getLabel", "HAPPY", "AMAZING", "EXCITED", "GRATEFUL", "LOVE", "PEACEFUL", "HOPEFUL", "PROUD", "CONTENT", "INSPIRED", "NEUTRAL", "CALM", "THOUGHTFUL", "TIRED", "BORED", "SAD", "ANXIOUS", "STRESSED", "ANGRY", "FRUSTRATED", "FEARFUL", "LONELY", "DISAPPOINTED", "OVERWHELMED", "DEPRESSED", "CONFUSED", "NERVOUS", "SURPRISED", "NOSTALGIC", "MOTIVATED", "Companion", "app_debug"})
public enum MoodOption {
    /*public static final*/ HAPPY /* = new HAPPY(0, null, null, null) */,
    /*public static final*/ AMAZING /* = new AMAZING(0, null, null, null) */,
    /*public static final*/ EXCITED /* = new EXCITED(0, null, null, null) */,
    /*public static final*/ GRATEFUL /* = new GRATEFUL(0, null, null, null) */,
    /*public static final*/ LOVE /* = new LOVE(0, null, null, null) */,
    /*public static final*/ PEACEFUL /* = new PEACEFUL(0, null, null, null) */,
    /*public static final*/ HOPEFUL /* = new HOPEFUL(0, null, null, null) */,
    /*public static final*/ PROUD /* = new PROUD(0, null, null, null) */,
    /*public static final*/ CONTENT /* = new CONTENT(0, null, null, null) */,
    /*public static final*/ INSPIRED /* = new INSPIRED(0, null, null, null) */,
    /*public static final*/ NEUTRAL /* = new NEUTRAL(0, null, null, null) */,
    /*public static final*/ CALM /* = new CALM(0, null, null, null) */,
    /*public static final*/ THOUGHTFUL /* = new THOUGHTFUL(0, null, null, null) */,
    /*public static final*/ TIRED /* = new TIRED(0, null, null, null) */,
    /*public static final*/ BORED /* = new BORED(0, null, null, null) */,
    /*public static final*/ SAD /* = new SAD(0, null, null, null) */,
    /*public static final*/ ANXIOUS /* = new ANXIOUS(0, null, null, null) */,
    /*public static final*/ STRESSED /* = new STRESSED(0, null, null, null) */,
    /*public static final*/ ANGRY /* = new ANGRY(0, null, null, null) */,
    /*public static final*/ FRUSTRATED /* = new FRUSTRATED(0, null, null, null) */,
    /*public static final*/ FEARFUL /* = new FEARFUL(0, null, null, null) */,
    /*public static final*/ LONELY /* = new LONELY(0, null, null, null) */,
    /*public static final*/ DISAPPOINTED /* = new DISAPPOINTED(0, null, null, null) */,
    /*public static final*/ OVERWHELMED /* = new OVERWHELMED(0, null, null, null) */,
    /*public static final*/ DEPRESSED /* = new DEPRESSED(0, null, null, null) */,
    /*public static final*/ CONFUSED /* = new CONFUSED(0, null, null, null) */,
    /*public static final*/ NERVOUS /* = new NERVOUS(0, null, null, null) */,
    /*public static final*/ SURPRISED /* = new SURPRISED(0, null, null, null) */,
    /*public static final*/ NOSTALGIC /* = new NOSTALGIC(0, null, null, null) */,
    /*public static final*/ MOTIVATED /* = new MOTIVATED(0, null, null, null) */;
    private final int id = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String label = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String emoji = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.MoodCategory category = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.domain.model.MoodOption.Companion Companion = null;
    
    MoodOption(int id, java.lang.String label, java.lang.String emoji, com.dairum.domain.model.MoodCategory category) {
    }
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmoji() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.MoodCategory getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.domain.model.MoodOption> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\u0006\u0010\t\u001a\u00020\n\u00a8\u0006\u000b"}, d2 = {"Lcom/dairum/domain/model/MoodOption$Companion;", "", "()V", "fromId", "Lcom/dairum/domain/model/MoodOption;", "id", "", "getByCategory", "", "category", "Lcom/dairum/domain/model/MoodCategory;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.dairum.domain.model.MoodOption fromId(int id) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.dairum.domain.model.MoodOption> getByCategory(@org.jetbrains.annotations.NotNull()
        com.dairum.domain.model.MoodCategory category) {
            return null;
        }
    }
}