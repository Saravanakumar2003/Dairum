package com.dairum.di

import android.content.Context
import androidx.room.Room
import com.dairum.data.local.dao.*
import com.dairum.data.local.database.DatabaseCallback
import com.dairum.data.local.database.DairumDatabase
import com.dairum.data.local.database.Migrations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DairumDatabase {
        Timber.d("Creating DairumDatabase")
        
        return Room.databaseBuilder(
            context.applicationContext,
            DairumDatabase::class.java,
            DairumDatabase.DATABASE_NAME
        )
            .addCallback(DatabaseCallback(context))
            .addMigrations(*Migrations.ALL_MIGRATIONS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideJournalEntryDao(database: DairumDatabase): JournalEntryDao {
        return database.journalEntryDao()
    }
    
    @Provides
    @Singleton
    fun provideCategoryDao(database: DairumDatabase): CategoryDao {
        return database.categoryDao()
    }
    
    @Provides
    @Singleton
    fun provideTagDao(database: DairumDatabase): TagDao {
        return database.tagDao()
    }
    
    @Provides
    @Singleton
    fun provideMediaDao(database: DairumDatabase): MediaDao {
        return database.mediaDao()
    }
    
    @Provides
    @Singleton
    fun providePersonDao(database: DairumDatabase): PersonDao {
        return database.personDao()
    }
    
    @Provides
    @Singleton
    fun provideTrackerDao(database: DairumDatabase): TrackerDao {
        return database.trackerDao()
    }
    
    @Provides
    @Singleton
    fun provideExportDao(database: DairumDatabase): ExportDao {
        return database.exportDao()
    }
    
    @Provides
    @Singleton
    fun provideNotificationDao(database: DairumDatabase): NotificationDao {
        return database.notificationDao()
    }
    
    @Provides
    @Singleton
    fun provideMemoryDao(database: DairumDatabase): MemoryDao {
        return database.memoryDao()
    }
    
    // AI DAOs
    
    @Provides
    @Singleton
    fun provideAIModelDao(database: DairumDatabase): AIModelDao {
        return database.aiModelDao()
    }
    
    @Provides
    @Singleton
    fun provideJournalEmbeddingDao(database: DairumDatabase): JournalEmbeddingDao {
        return database.journalEmbeddingDao()
    }
    
    @Provides
    @Singleton
    fun provideWeeklyReflectionDao(database: DairumDatabase): WeeklyReflectionDao {
        return database.weeklyReflectionDao()
    }
    
    @Provides
    @Singleton
    fun providePatternResultDao(database: DairumDatabase): PatternResultDao {
        return database.patternResultDao()
    }
    
    @Provides
    @Singleton
    fun provideSmartSummaryDao(database: DairumDatabase): SmartSummaryDao {
        return database.smartSummaryDao()
    }
    
    @Provides
    @Singleton
    fun provideAIConversationDao(database: DairumDatabase): AIConversationDao {
        return database.aiConversationDao()
    }
    
    // Conversation DAOs
    
    @Provides
    @Singleton
    fun provideChatSessionDao(database: DairumDatabase): ChatSessionDao {
        return database.chatSessionDao()
    }
    
    @Provides
    @Singleton
    fun provideChatMessageDao(database: DairumDatabase): ChatMessageDao {
        return database.chatMessageDao()
    }
    
    @Provides
    @Singleton
    fun provideChatEmbeddingDao(database: DairumDatabase): ChatEmbeddingDao {
        return database.chatEmbeddingDao()
    }
    
    @Provides
    @Singleton
    fun provideReflectionMemoryDao(database: DairumDatabase): ReflectionMemoryDao {
        return database.reflectionMemoryDao()
    }
    
    @Provides
    @Singleton
    fun provideConversationContextDao(database: DairumDatabase): ConversationContextDao {
        return database.conversationContextDao()
    }
}
