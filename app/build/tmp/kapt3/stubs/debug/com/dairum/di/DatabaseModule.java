package com.dairum.di;

import android.content.Context;
import androidx.room.Room;
import com.dairum.data.local.dao.*;
import com.dairum.data.local.database.DatabaseCallback;
import com.dairum.data.local.database.DairumDatabase;
import com.dairum.data.local.database.Migrations;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import timber.log.Timber;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0092\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0013\u001a\u00020\u00062\b\b\u0001\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010 \u001a\u00020!2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\"\u001a\u00020#2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010$\u001a\u00020%2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010&\u001a\u00020\'2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010(\u001a\u00020)2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010*\u001a\u00020+2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010,\u001a\u00020-2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010.\u001a\u00020/2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u00060"}, d2 = {"Lcom/dairum/di/DatabaseModule;", "", "()V", "provideAIConversationDao", "Lcom/dairum/data/local/dao/AIConversationDao;", "database", "Lcom/dairum/data/local/database/DairumDatabase;", "provideAIModelDao", "Lcom/dairum/data/local/dao/AIModelDao;", "provideCategoryDao", "Lcom/dairum/data/local/dao/CategoryDao;", "provideChatEmbeddingDao", "Lcom/dairum/data/local/dao/ChatEmbeddingDao;", "provideChatMessageDao", "Lcom/dairum/data/local/dao/ChatMessageDao;", "provideChatSessionDao", "Lcom/dairum/data/local/dao/ChatSessionDao;", "provideConversationContextDao", "Lcom/dairum/data/local/dao/ConversationContextDao;", "provideDatabase", "context", "Landroid/content/Context;", "provideExportDao", "Lcom/dairum/data/local/dao/ExportDao;", "provideJournalEmbeddingDao", "Lcom/dairum/data/local/dao/JournalEmbeddingDao;", "provideJournalEntryDao", "Lcom/dairum/data/local/dao/JournalEntryDao;", "provideMediaDao", "Lcom/dairum/data/local/dao/MediaDao;", "provideMemoryDao", "Lcom/dairum/data/local/dao/MemoryDao;", "provideNotificationDao", "Lcom/dairum/data/local/dao/NotificationDao;", "providePatternResultDao", "Lcom/dairum/data/local/dao/PatternResultDao;", "providePersonDao", "Lcom/dairum/data/local/dao/PersonDao;", "provideReflectionMemoryDao", "Lcom/dairum/data/local/dao/ReflectionMemoryDao;", "provideSmartSummaryDao", "Lcom/dairum/data/local/dao/SmartSummaryDao;", "provideTagDao", "Lcom/dairum/data/local/dao/TagDao;", "provideTrackerDao", "Lcom/dairum/data/local/dao/TrackerDao;", "provideWeeklyReflectionDao", "Lcom/dairum/data/local/dao/WeeklyReflectionDao;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class DatabaseModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.dairum.di.DatabaseModule INSTANCE = null;
    
    private DatabaseModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.database.DairumDatabase provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.JournalEntryDao provideJournalEntryDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.CategoryDao provideCategoryDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.TagDao provideTagDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.MediaDao provideMediaDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.PersonDao providePersonDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.TrackerDao provideTrackerDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.ExportDao provideExportDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.NotificationDao provideNotificationDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.MemoryDao provideMemoryDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.AIModelDao provideAIModelDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.JournalEmbeddingDao provideJournalEmbeddingDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.WeeklyReflectionDao provideWeeklyReflectionDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.PatternResultDao providePatternResultDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.SmartSummaryDao provideSmartSummaryDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.AIConversationDao provideAIConversationDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.ChatSessionDao provideChatSessionDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.ChatMessageDao provideChatMessageDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.ChatEmbeddingDao provideChatEmbeddingDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.ReflectionMemoryDao provideReflectionMemoryDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.local.dao.ConversationContextDao provideConversationContextDao(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.database.DairumDatabase database) {
        return null;
    }
}