package com.dairum.data.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.dairum.data.local.converter.Converters;
import com.dairum.data.local.dao.*;
import com.dairum.data.local.entity.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 +2\u00020\u0001:\u0001+B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&J\b\u0010\u000f\u001a\u00020\u0010H&J\b\u0010\u0011\u001a\u00020\u0012H&J\b\u0010\u0013\u001a\u00020\u0014H&J\b\u0010\u0015\u001a\u00020\u0016H&J\b\u0010\u0017\u001a\u00020\u0018H&J\b\u0010\u0019\u001a\u00020\u001aH&J\b\u0010\u001b\u001a\u00020\u001cH&J\b\u0010\u001d\u001a\u00020\u001eH&J\b\u0010\u001f\u001a\u00020 H&J\b\u0010!\u001a\u00020\"H&J\b\u0010#\u001a\u00020$H&J\b\u0010%\u001a\u00020&H&J\b\u0010\'\u001a\u00020(H&J\b\u0010)\u001a\u00020*H&\u00a8\u0006,"}, d2 = {"Lcom/dairum/data/local/database/DairumDatabase;", "Landroidx/room/RoomDatabase;", "()V", "aiConversationDao", "Lcom/dairum/data/local/dao/AIConversationDao;", "aiModelDao", "Lcom/dairum/data/local/dao/AIModelDao;", "categoryDao", "Lcom/dairum/data/local/dao/CategoryDao;", "chatEmbeddingDao", "Lcom/dairum/data/local/dao/ChatEmbeddingDao;", "chatMessageDao", "Lcom/dairum/data/local/dao/ChatMessageDao;", "chatSessionDao", "Lcom/dairum/data/local/dao/ChatSessionDao;", "conversationContextDao", "Lcom/dairum/data/local/dao/ConversationContextDao;", "exportDao", "Lcom/dairum/data/local/dao/ExportDao;", "journalEmbeddingDao", "Lcom/dairum/data/local/dao/JournalEmbeddingDao;", "journalEntryDao", "Lcom/dairum/data/local/dao/JournalEntryDao;", "mediaDao", "Lcom/dairum/data/local/dao/MediaDao;", "memoryDao", "Lcom/dairum/data/local/dao/MemoryDao;", "notificationDao", "Lcom/dairum/data/local/dao/NotificationDao;", "patternResultDao", "Lcom/dairum/data/local/dao/PatternResultDao;", "personDao", "Lcom/dairum/data/local/dao/PersonDao;", "reflectionMemoryDao", "Lcom/dairum/data/local/dao/ReflectionMemoryDao;", "smartSummaryDao", "Lcom/dairum/data/local/dao/SmartSummaryDao;", "tagDao", "Lcom/dairum/data/local/dao/TagDao;", "trackerDao", "Lcom/dairum/data/local/dao/TrackerDao;", "weeklyReflectionDao", "Lcom/dairum/data/local/dao/WeeklyReflectionDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.dairum.data.local.entity.JournalEntryEntity.class, com.dairum.data.local.entity.CategoryEntity.class, com.dairum.data.local.entity.TagEntity.class, com.dairum.data.local.entity.EntryTagCrossRef.class, com.dairum.data.local.entity.MediaEntity.class, com.dairum.data.local.entity.CustomTrackerEntity.class, com.dairum.data.local.entity.TrackerValueEntity.class, com.dairum.data.local.entity.PersonEntity.class, com.dairum.data.local.entity.EntryPersonCrossRef.class, com.dairum.data.local.entity.ExportConfigEntity.class, com.dairum.data.local.entity.ExportHistoryEntity.class, com.dairum.data.local.entity.ReminderConfigEntity.class, com.dairum.data.local.entity.SmartReminderEntity.class, com.dairum.data.local.entity.NotificationHistoryEntity.class, com.dairum.data.local.entity.MemoryEntity.class, com.dairum.data.local.entity.MemoryEntryCrossRef.class, com.dairum.data.local.entity.MemoryMilestoneEntity.class, com.dairum.data.local.entity.MemoryTrackerEntity.class, com.dairum.data.local.entity.MoodCheckInConfigEntity.class, com.dairum.data.local.entity.AIModelEntity.class, com.dairum.data.local.entity.JournalEmbeddingEntity.class, com.dairum.data.local.entity.WeeklyReflectionEntity.class, com.dairum.data.local.entity.PatternResultEntity.class, com.dairum.data.local.entity.SmartSummaryEntity.class, com.dairum.data.local.entity.AIConversationEntity.class, com.dairum.data.local.entity.ChatSessionEntity.class, com.dairum.data.local.entity.ChatMessageEntity.class, com.dairum.data.local.entity.ChatEmbeddingEntity.class, com.dairum.data.local.entity.ReflectionMemoryEntity.class, com.dairum.data.local.entity.ConversationContextEntity.class}, version = 1, exportSchema = true)
@androidx.room.TypeConverters(value = {com.dairum.data.local.converter.Converters.class})
public abstract class DairumDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DATABASE_NAME = "dairum.db";
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.data.local.database.DairumDatabase.Companion Companion = null;
    
    public DairumDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.JournalEntryDao journalEntryDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.CategoryDao categoryDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.TagDao tagDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.MediaDao mediaDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.PersonDao personDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.TrackerDao trackerDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.ExportDao exportDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.NotificationDao notificationDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.MemoryDao memoryDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.AIModelDao aiModelDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.JournalEmbeddingDao journalEmbeddingDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.WeeklyReflectionDao weeklyReflectionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.PatternResultDao patternResultDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.SmartSummaryDao smartSummaryDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.AIConversationDao aiConversationDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.ChatSessionDao chatSessionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.ChatMessageDao chatMessageDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.ChatEmbeddingDao chatEmbeddingDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.ReflectionMemoryDao reflectionMemoryDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.data.local.dao.ConversationContextDao conversationContextDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/dairum/data/local/database/DairumDatabase$Companion;", "", "()V", "DATABASE_NAME", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}