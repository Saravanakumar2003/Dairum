package com.dairum.data.local.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.dairum.data.local.dao.AIConversationDao;
import com.dairum.data.local.dao.AIConversationDao_Impl;
import com.dairum.data.local.dao.AIModelDao;
import com.dairum.data.local.dao.AIModelDao_Impl;
import com.dairum.data.local.dao.CategoryDao;
import com.dairum.data.local.dao.CategoryDao_Impl;
import com.dairum.data.local.dao.ChatEmbeddingDao;
import com.dairum.data.local.dao.ChatEmbeddingDao_Impl;
import com.dairum.data.local.dao.ChatMessageDao;
import com.dairum.data.local.dao.ChatMessageDao_Impl;
import com.dairum.data.local.dao.ChatSessionDao;
import com.dairum.data.local.dao.ChatSessionDao_Impl;
import com.dairum.data.local.dao.ConversationContextDao;
import com.dairum.data.local.dao.ConversationContextDao_Impl;
import com.dairum.data.local.dao.ExportDao;
import com.dairum.data.local.dao.ExportDao_Impl;
import com.dairum.data.local.dao.JournalEmbeddingDao;
import com.dairum.data.local.dao.JournalEmbeddingDao_Impl;
import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.JournalEntryDao_Impl;
import com.dairum.data.local.dao.MediaDao;
import com.dairum.data.local.dao.MediaDao_Impl;
import com.dairum.data.local.dao.MemoryDao;
import com.dairum.data.local.dao.MemoryDao_Impl;
import com.dairum.data.local.dao.NotificationDao;
import com.dairum.data.local.dao.NotificationDao_Impl;
import com.dairum.data.local.dao.PatternResultDao;
import com.dairum.data.local.dao.PatternResultDao_Impl;
import com.dairum.data.local.dao.PersonDao;
import com.dairum.data.local.dao.PersonDao_Impl;
import com.dairum.data.local.dao.ReflectionMemoryDao;
import com.dairum.data.local.dao.ReflectionMemoryDao_Impl;
import com.dairum.data.local.dao.SmartSummaryDao;
import com.dairum.data.local.dao.SmartSummaryDao_Impl;
import com.dairum.data.local.dao.TagDao;
import com.dairum.data.local.dao.TagDao_Impl;
import com.dairum.data.local.dao.TrackerDao;
import com.dairum.data.local.dao.TrackerDao_Impl;
import com.dairum.data.local.dao.WeeklyReflectionDao;
import com.dairum.data.local.dao.WeeklyReflectionDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DairumDatabase_Impl extends DairumDatabase {
  private volatile JournalEntryDao _journalEntryDao;

  private volatile CategoryDao _categoryDao;

  private volatile TagDao _tagDao;

  private volatile MediaDao _mediaDao;

  private volatile PersonDao _personDao;

  private volatile TrackerDao _trackerDao;

  private volatile ExportDao _exportDao;

  private volatile NotificationDao _notificationDao;

  private volatile MemoryDao _memoryDao;

  private volatile AIModelDao _aIModelDao;

  private volatile JournalEmbeddingDao _journalEmbeddingDao;

  private volatile WeeklyReflectionDao _weeklyReflectionDao;

  private volatile PatternResultDao _patternResultDao;

  private volatile SmartSummaryDao _smartSummaryDao;

  private volatile AIConversationDao _aIConversationDao;

  private volatile ChatSessionDao _chatSessionDao;

  private volatile ChatMessageDao _chatMessageDao;

  private volatile ChatEmbeddingDao _chatEmbeddingDao;

  private volatile ReflectionMemoryDao _reflectionMemoryDao;

  private volatile ConversationContextDao _conversationContextDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `journal_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `content` TEXT NOT NULL, `plainContent` TEXT NOT NULL, `categoryId` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `moodLevel` INTEGER, `moodIcon` TEXT, `weatherData` TEXT, `locationData` TEXT, `isFavorite` INTEGER NOT NULL, `isArchived` INTEGER NOT NULL, `isDraft` INTEGER NOT NULL, `isPinned` INTEGER NOT NULL, `wordCount` INTEGER NOT NULL, `backgroundColor` TEXT)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_journal_entries_categoryId` ON `journal_entries` (`categoryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_journal_entries_createdAt` ON `journal_entries` (`createdAt`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_journal_entries_moodLevel` ON `journal_entries` (`moodLevel`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_journal_entries_isFavorite` ON `journal_entries` (`isFavorite`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_journal_entries_isArchived` ON `journal_entries` (`isArchived`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `color` INTEGER NOT NULL, `icon` TEXT NOT NULL, `sortOrder` INTEGER NOT NULL, `isDefault` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tags` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `color` INTEGER, `usageCount` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tags_name` ON `tags` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `entry_tags` (`entryId` INTEGER NOT NULL, `tagId` INTEGER NOT NULL, PRIMARY KEY(`entryId`, `tagId`), FOREIGN KEY(`entryId`) REFERENCES `journal_entries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`tagId`) REFERENCES `tags`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_entry_tags_entryId` ON `entry_tags` (`entryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_entry_tags_tagId` ON `entry_tags` (`tagId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `media` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `entryId` INTEGER NOT NULL, `type` TEXT NOT NULL, `uri` TEXT NOT NULL, `thumbnailUri` TEXT, `fileName` TEXT NOT NULL, `mimeType` TEXT NOT NULL, `sizeBytes` INTEGER NOT NULL, `duration` INTEGER, `width` INTEGER, `height` INTEGER, `sortOrder` INTEGER NOT NULL, `caption` TEXT, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`entryId`) REFERENCES `journal_entries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_entryId` ON `media` (`entryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_type` ON `media` (`type`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_createdAt` ON `media` (`createdAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `custom_trackers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `icon` TEXT NOT NULL, `unit` TEXT, `minValue` REAL, `maxValue` REAL, `options` TEXT, `isBuiltIn` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, `sortOrder` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tracker_values` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `entryId` INTEGER NOT NULL, `trackerId` INTEGER NOT NULL, `value` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`entryId`) REFERENCES `journal_entries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`trackerId`) REFERENCES `custom_trackers`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tracker_values_entryId` ON `tracker_values` (`entryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tracker_values_trackerId` ON `tracker_values` (`trackerId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `people` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `relationship` TEXT, `photoUri` TEXT, `notes` TEXT, `email` TEXT, `phone` TEXT, `birthday` INTEGER, `importantDates` TEXT, `isFavorite` INTEGER NOT NULL, `mentionCount` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_people_name` ON `people` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `entry_people` (`entryId` INTEGER NOT NULL, `personId` INTEGER NOT NULL, PRIMARY KEY(`entryId`, `personId`), FOREIGN KEY(`entryId`) REFERENCES `journal_entries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`personId`) REFERENCES `people`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_entry_people_entryId` ON `entry_people` (`entryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_entry_people_personId` ON `entry_people` (`personId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `export_configs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `format` TEXT NOT NULL, `scheduleHour` INTEGER NOT NULL, `scheduleMinute` INTEGER NOT NULL, `scheduleDays` TEXT NOT NULL, `isEnabled` INTEGER NOT NULL, `exportPath` TEXT, `includeMedia` INTEGER NOT NULL, `lastExportAt` INTEGER, `lastExportStatus` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `export_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `configId` INTEGER, `format` TEXT NOT NULL, `filePath` TEXT NOT NULL, `fileName` TEXT NOT NULL, `entriesCount` INTEGER NOT NULL, `fileSize` INTEGER NOT NULL, `isSuccess` INTEGER NOT NULL, `errorMessage` TEXT, `exportedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_export_history_exportedAt` ON `export_history` (`exportedAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reminder_configs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `isEnabled` INTEGER NOT NULL, `scheduleHour` INTEGER NOT NULL, `scheduleMinute` INTEGER NOT NULL, `scheduleDays` TEXT NOT NULL, `notificationTitle` TEXT NOT NULL, `notificationMessage` TEXT NOT NULL, `priority` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `smart_reminders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `reminderType` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `reminderDate` INTEGER NOT NULL, `repeatYearly` INTEGER NOT NULL, `relatedPersonId` INTEGER, `relatedEntryId` INTEGER, `isEnabled` INTEGER NOT NULL, `notifyDaysBefore` INTEGER NOT NULL, `lastNotifiedAt` INTEGER, `metadata` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_smart_reminders_reminderDate` ON `smart_reminders` (`reminderDate`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_smart_reminders_reminderType` ON `smart_reminders` (`reminderType`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notification_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `sourceId` INTEGER NOT NULL, `title` TEXT NOT NULL, `message` TEXT NOT NULL, `wasTapped` INTEGER NOT NULL, `sentAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_notification_history_sentAt` ON `notification_history` (`sentAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `memories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `coverImageUri` TEXT, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `location` TEXT, `locationData` TEXT, `isFavorite` INTEGER NOT NULL, `color` INTEGER, `icon` TEXT, `tags` TEXT, `moodSummary` TEXT, `totalTimeSpent` INTEGER NOT NULL, `entryCount` INTEGER NOT NULL, `wordCount` INTEGER NOT NULL, `photoCount` INTEGER NOT NULL, `videoCount` INTEGER NOT NULL, `peopleInvolved` TEXT, `highlights` TEXT, `trackerSummary` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memories_startDate` ON `memories` (`startDate`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memories_endDate` ON `memories` (`endDate`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memories_isFavorite` ON `memories` (`isFavorite`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `memory_entries` (`memoryId` INTEGER NOT NULL, `entryId` INTEGER NOT NULL, `sortOrder` INTEGER NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`memoryId`, `entryId`), FOREIGN KEY(`memoryId`) REFERENCES `memories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`entryId`) REFERENCES `journal_entries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memory_entries_memoryId` ON `memory_entries` (`memoryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memory_entries_entryId` ON `memory_entries` (`entryId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `memory_milestones` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `memoryId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `milestoneDate` INTEGER NOT NULL, `relatedEntryId` INTEGER, `icon` TEXT, `sortOrder` INTEGER NOT NULL, FOREIGN KEY(`memoryId`) REFERENCES `memories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memory_milestones_memoryId` ON `memory_milestones` (`memoryId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `memory_trackers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `memoryId` INTEGER NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `icon` TEXT, `unit` TEXT, `goalValue` TEXT, `currentValue` TEXT, `progress` REAL NOT NULL, FOREIGN KEY(`memoryId`) REFERENCES `memories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memory_trackers_memoryId` ON `memory_trackers` (`memoryId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `mood_checkin_config` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `isEnabled` INTEGER NOT NULL, `intervalHours` INTEGER NOT NULL, `startHour` INTEGER NOT NULL, `endHour` INTEGER NOT NULL, `lastPromptAt` INTEGER, `enabledDays` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ai_models` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `filePath` TEXT NOT NULL, `sizeBytes` INTEGER NOT NULL, `modelType` TEXT NOT NULL, `quantization` TEXT, `contextLength` INTEGER NOT NULL, `isDefault` INTEGER NOT NULL, `isDownloaded` INTEGER NOT NULL, `downloadUrl` TEXT, `createdAt` INTEGER NOT NULL, `lastUsedAt` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ai_models_modelType` ON `ai_models` (`modelType`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ai_models_isDefault` ON `ai_models` (`isDefault`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `journal_embeddings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `entryId` INTEGER NOT NULL, `embedding` BLOB NOT NULL, `embeddingDimension` INTEGER NOT NULL, `model` TEXT NOT NULL, `version` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`entryId`) REFERENCES `journal_entries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_journal_embeddings_entryId` ON `journal_embeddings` (`entryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_journal_embeddings_model` ON `journal_embeddings` (`model`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `weekly_reflections` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `weekStartDate` INTEGER NOT NULL, `weekEndDate` INTEGER NOT NULL, `summary` TEXT NOT NULL, `averageMood` REAL NOT NULL, `moodRangeMin` INTEGER NOT NULL, `moodRangeMax` INTEGER NOT NULL, `dominantMood` TEXT NOT NULL, `moodDistributionJson` TEXT NOT NULL, `moodTrend` TEXT NOT NULL, `keyThemesJson` TEXT NOT NULL, `highlightsJson` TEXT NOT NULL, `suggestionsJson` TEXT NOT NULL, `entryCount` INTEGER NOT NULL, `totalWordCount` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_weekly_reflections_weekStartDate` ON `weekly_reflections` (`weekStartDate`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pattern_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patternType` TEXT NOT NULL, `description` TEXT NOT NULL, `confidence` REAL NOT NULL, `evidenceJson` TEXT NOT NULL, `insightsJson` TEXT NOT NULL, `detectedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_pattern_results_patternType` ON `pattern_results` (`patternType`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_pattern_results_detectedAt` ON `pattern_results` (`detectedAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `smart_summaries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `summaryType` TEXT NOT NULL, `periodStart` INTEGER NOT NULL, `periodEnd` INTEGER NOT NULL, `title` TEXT NOT NULL, `summary` TEXT NOT NULL, `keyPointsJson` TEXT NOT NULL, `moodOverview` TEXT, `topTagsJson` TEXT NOT NULL, `topPeopleJson` TEXT NOT NULL, `wordCount` INTEGER NOT NULL, `entryCount` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_smart_summaries_summaryType` ON `smart_summaries` (`summaryType`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_smart_summaries_periodStart_periodEnd` ON `smart_summaries` (`periodStart`, `periodEnd`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ai_conversations` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sessionId` TEXT NOT NULL, `role` TEXT NOT NULL, `content` TEXT NOT NULL, `sourceEntryIdsJson` TEXT, `tokensUsed` INTEGER NOT NULL, `generationTimeMs` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ai_conversations_sessionId` ON `ai_conversations` (`sessionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ai_conversations_createdAt` ON `ai_conversations` (`createdAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chat_sessions` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `summary` TEXT, `isSaved` INTEGER NOT NULL, `isArchived` INTEGER NOT NULL, `messageCount` INTEGER NOT NULL, `lastMessageAt` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chat_sessions_createdAt` ON `chat_sessions` (`createdAt`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chat_sessions_isSaved` ON `chat_sessions` (`isSaved`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chat_sessions_isArchived` ON `chat_sessions` (`isArchived`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chat_messages` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sessionId` TEXT NOT NULL, `role` TEXT NOT NULL, `content` TEXT NOT NULL, `tokensUsed` INTEGER NOT NULL, `generationTimeMs` INTEGER NOT NULL, `isEdited` INTEGER NOT NULL, `metadata` TEXT, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`sessionId`) REFERENCES `chat_sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chat_messages_sessionId` ON `chat_messages` (`sessionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chat_messages_createdAt` ON `chat_messages` (`createdAt`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chat_messages_role` ON `chat_messages` (`role`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chat_embeddings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `messageId` INTEGER NOT NULL, `sessionId` TEXT NOT NULL, `embedding` BLOB NOT NULL, `embeddingDimension` INTEGER NOT NULL, `model` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`messageId`) REFERENCES `chat_messages`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_chat_embeddings_messageId` ON `chat_embeddings` (`messageId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chat_embeddings_sessionId` ON `chat_embeddings` (`sessionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chat_embeddings_model` ON `chat_embeddings` (`model`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reflection_memories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT NOT NULL, `category` TEXT NOT NULL, `confidence` REAL NOT NULL, `sourceMessageIds` TEXT, `sourceSessionIds` TEXT, `occurrenceCount` INTEGER NOT NULL, `lastReferenced` INTEGER, `isActive` INTEGER NOT NULL, `embedding` BLOB, `embeddingModel` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reflection_memories_category` ON `reflection_memories` (`category`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reflection_memories_createdAt` ON `reflection_memories` (`createdAt`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reflection_memories_confidence` ON `reflection_memories` (`confidence`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reflection_memories_isActive` ON `reflection_memories` (`isActive`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `conversation_context` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sessionId` TEXT NOT NULL, `currentTopics` TEXT, `emotionalTone` TEXT, `pendingQuestions` TEXT, `userPreferences` TEXT, `discussionDepth` INTEGER NOT NULL, `lastSummaryAt` INTEGER, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`sessionId`) REFERENCES `chat_sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_conversation_context_sessionId` ON `conversation_context` (`sessionId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd2d98dd1ca3a5bdd53fa85267eab944c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `journal_entries`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `tags`");
        db.execSQL("DROP TABLE IF EXISTS `entry_tags`");
        db.execSQL("DROP TABLE IF EXISTS `media`");
        db.execSQL("DROP TABLE IF EXISTS `custom_trackers`");
        db.execSQL("DROP TABLE IF EXISTS `tracker_values`");
        db.execSQL("DROP TABLE IF EXISTS `people`");
        db.execSQL("DROP TABLE IF EXISTS `entry_people`");
        db.execSQL("DROP TABLE IF EXISTS `export_configs`");
        db.execSQL("DROP TABLE IF EXISTS `export_history`");
        db.execSQL("DROP TABLE IF EXISTS `reminder_configs`");
        db.execSQL("DROP TABLE IF EXISTS `smart_reminders`");
        db.execSQL("DROP TABLE IF EXISTS `notification_history`");
        db.execSQL("DROP TABLE IF EXISTS `memories`");
        db.execSQL("DROP TABLE IF EXISTS `memory_entries`");
        db.execSQL("DROP TABLE IF EXISTS `memory_milestones`");
        db.execSQL("DROP TABLE IF EXISTS `memory_trackers`");
        db.execSQL("DROP TABLE IF EXISTS `mood_checkin_config`");
        db.execSQL("DROP TABLE IF EXISTS `ai_models`");
        db.execSQL("DROP TABLE IF EXISTS `journal_embeddings`");
        db.execSQL("DROP TABLE IF EXISTS `weekly_reflections`");
        db.execSQL("DROP TABLE IF EXISTS `pattern_results`");
        db.execSQL("DROP TABLE IF EXISTS `smart_summaries`");
        db.execSQL("DROP TABLE IF EXISTS `ai_conversations`");
        db.execSQL("DROP TABLE IF EXISTS `chat_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `chat_messages`");
        db.execSQL("DROP TABLE IF EXISTS `chat_embeddings`");
        db.execSQL("DROP TABLE IF EXISTS `reflection_memories`");
        db.execSQL("DROP TABLE IF EXISTS `conversation_context`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsJournalEntries = new HashMap<String, TableInfo.Column>(17);
        _columnsJournalEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("plainContent", new TableInfo.Column("plainContent", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("moodLevel", new TableInfo.Column("moodLevel", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("moodIcon", new TableInfo.Column("moodIcon", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("weatherData", new TableInfo.Column("weatherData", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("locationData", new TableInfo.Column("locationData", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("isArchived", new TableInfo.Column("isArchived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("isDraft", new TableInfo.Column("isDraft", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("isPinned", new TableInfo.Column("isPinned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("wordCount", new TableInfo.Column("wordCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("backgroundColor", new TableInfo.Column("backgroundColor", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysJournalEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesJournalEntries = new HashSet<TableInfo.Index>(5);
        _indicesJournalEntries.add(new TableInfo.Index("index_journal_entries_categoryId", false, Arrays.asList("categoryId"), Arrays.asList("ASC")));
        _indicesJournalEntries.add(new TableInfo.Index("index_journal_entries_createdAt", false, Arrays.asList("createdAt"), Arrays.asList("ASC")));
        _indicesJournalEntries.add(new TableInfo.Index("index_journal_entries_moodLevel", false, Arrays.asList("moodLevel"), Arrays.asList("ASC")));
        _indicesJournalEntries.add(new TableInfo.Index("index_journal_entries_isFavorite", false, Arrays.asList("isFavorite"), Arrays.asList("ASC")));
        _indicesJournalEntries.add(new TableInfo.Index("index_journal_entries_isArchived", false, Arrays.asList("isArchived"), Arrays.asList("ASC")));
        final TableInfo _infoJournalEntries = new TableInfo("journal_entries", _columnsJournalEntries, _foreignKeysJournalEntries, _indicesJournalEntries);
        final TableInfo _existingJournalEntries = TableInfo.read(db, "journal_entries");
        if (!_infoJournalEntries.equals(_existingJournalEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "journal_entries(com.dairum.data.local.entity.JournalEntryEntity).\n"
                  + " Expected:\n" + _infoJournalEntries + "\n"
                  + " Found:\n" + _existingJournalEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(7);
        _columnsCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("color", new TableInfo.Column("color", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("icon", new TableInfo.Column("icon", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("isDefault", new TableInfo.Column("isDefault", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.dairum.data.local.entity.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsTags = new HashMap<String, TableInfo.Column>(5);
        _columnsTags.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTags.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTags.put("color", new TableInfo.Column("color", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTags.put("usageCount", new TableInfo.Column("usageCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTags.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTags = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTags = new HashSet<TableInfo.Index>(1);
        _indicesTags.add(new TableInfo.Index("index_tags_name", true, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoTags = new TableInfo("tags", _columnsTags, _foreignKeysTags, _indicesTags);
        final TableInfo _existingTags = TableInfo.read(db, "tags");
        if (!_infoTags.equals(_existingTags)) {
          return new RoomOpenHelper.ValidationResult(false, "tags(com.dairum.data.local.entity.TagEntity).\n"
                  + " Expected:\n" + _infoTags + "\n"
                  + " Found:\n" + _existingTags);
        }
        final HashMap<String, TableInfo.Column> _columnsEntryTags = new HashMap<String, TableInfo.Column>(2);
        _columnsEntryTags.put("entryId", new TableInfo.Column("entryId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntryTags.put("tagId", new TableInfo.Column("tagId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEntryTags = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysEntryTags.add(new TableInfo.ForeignKey("journal_entries", "CASCADE", "NO ACTION", Arrays.asList("entryId"), Arrays.asList("id")));
        _foreignKeysEntryTags.add(new TableInfo.ForeignKey("tags", "CASCADE", "NO ACTION", Arrays.asList("tagId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesEntryTags = new HashSet<TableInfo.Index>(2);
        _indicesEntryTags.add(new TableInfo.Index("index_entry_tags_entryId", false, Arrays.asList("entryId"), Arrays.asList("ASC")));
        _indicesEntryTags.add(new TableInfo.Index("index_entry_tags_tagId", false, Arrays.asList("tagId"), Arrays.asList("ASC")));
        final TableInfo _infoEntryTags = new TableInfo("entry_tags", _columnsEntryTags, _foreignKeysEntryTags, _indicesEntryTags);
        final TableInfo _existingEntryTags = TableInfo.read(db, "entry_tags");
        if (!_infoEntryTags.equals(_existingEntryTags)) {
          return new RoomOpenHelper.ValidationResult(false, "entry_tags(com.dairum.data.local.entity.EntryTagCrossRef).\n"
                  + " Expected:\n" + _infoEntryTags + "\n"
                  + " Found:\n" + _existingEntryTags);
        }
        final HashMap<String, TableInfo.Column> _columnsMedia = new HashMap<String, TableInfo.Column>(14);
        _columnsMedia.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("entryId", new TableInfo.Column("entryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("thumbnailUri", new TableInfo.Column("thumbnailUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("fileName", new TableInfo.Column("fileName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("mimeType", new TableInfo.Column("mimeType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("sizeBytes", new TableInfo.Column("sizeBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("duration", new TableInfo.Column("duration", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("width", new TableInfo.Column("width", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("height", new TableInfo.Column("height", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("caption", new TableInfo.Column("caption", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedia = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMedia.add(new TableInfo.ForeignKey("journal_entries", "CASCADE", "NO ACTION", Arrays.asList("entryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMedia = new HashSet<TableInfo.Index>(3);
        _indicesMedia.add(new TableInfo.Index("index_media_entryId", false, Arrays.asList("entryId"), Arrays.asList("ASC")));
        _indicesMedia.add(new TableInfo.Index("index_media_type", false, Arrays.asList("type"), Arrays.asList("ASC")));
        _indicesMedia.add(new TableInfo.Index("index_media_createdAt", false, Arrays.asList("createdAt"), Arrays.asList("ASC")));
        final TableInfo _infoMedia = new TableInfo("media", _columnsMedia, _foreignKeysMedia, _indicesMedia);
        final TableInfo _existingMedia = TableInfo.read(db, "media");
        if (!_infoMedia.equals(_existingMedia)) {
          return new RoomOpenHelper.ValidationResult(false, "media(com.dairum.data.local.entity.MediaEntity).\n"
                  + " Expected:\n" + _infoMedia + "\n"
                  + " Found:\n" + _existingMedia);
        }
        final HashMap<String, TableInfo.Column> _columnsCustomTrackers = new HashMap<String, TableInfo.Column>(12);
        _columnsCustomTrackers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("icon", new TableInfo.Column("icon", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("unit", new TableInfo.Column("unit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("minValue", new TableInfo.Column("minValue", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("maxValue", new TableInfo.Column("maxValue", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("options", new TableInfo.Column("options", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("isBuiltIn", new TableInfo.Column("isBuiltIn", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomTrackers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCustomTrackers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCustomTrackers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCustomTrackers = new TableInfo("custom_trackers", _columnsCustomTrackers, _foreignKeysCustomTrackers, _indicesCustomTrackers);
        final TableInfo _existingCustomTrackers = TableInfo.read(db, "custom_trackers");
        if (!_infoCustomTrackers.equals(_existingCustomTrackers)) {
          return new RoomOpenHelper.ValidationResult(false, "custom_trackers(com.dairum.data.local.entity.CustomTrackerEntity).\n"
                  + " Expected:\n" + _infoCustomTrackers + "\n"
                  + " Found:\n" + _existingCustomTrackers);
        }
        final HashMap<String, TableInfo.Column> _columnsTrackerValues = new HashMap<String, TableInfo.Column>(5);
        _columnsTrackerValues.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackerValues.put("entryId", new TableInfo.Column("entryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackerValues.put("trackerId", new TableInfo.Column("trackerId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackerValues.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackerValues.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrackerValues = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysTrackerValues.add(new TableInfo.ForeignKey("journal_entries", "CASCADE", "NO ACTION", Arrays.asList("entryId"), Arrays.asList("id")));
        _foreignKeysTrackerValues.add(new TableInfo.ForeignKey("custom_trackers", "CASCADE", "NO ACTION", Arrays.asList("trackerId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTrackerValues = new HashSet<TableInfo.Index>(2);
        _indicesTrackerValues.add(new TableInfo.Index("index_tracker_values_entryId", false, Arrays.asList("entryId"), Arrays.asList("ASC")));
        _indicesTrackerValues.add(new TableInfo.Index("index_tracker_values_trackerId", false, Arrays.asList("trackerId"), Arrays.asList("ASC")));
        final TableInfo _infoTrackerValues = new TableInfo("tracker_values", _columnsTrackerValues, _foreignKeysTrackerValues, _indicesTrackerValues);
        final TableInfo _existingTrackerValues = TableInfo.read(db, "tracker_values");
        if (!_infoTrackerValues.equals(_existingTrackerValues)) {
          return new RoomOpenHelper.ValidationResult(false, "tracker_values(com.dairum.data.local.entity.TrackerValueEntity).\n"
                  + " Expected:\n" + _infoTrackerValues + "\n"
                  + " Found:\n" + _existingTrackerValues);
        }
        final HashMap<String, TableInfo.Column> _columnsPeople = new HashMap<String, TableInfo.Column>(12);
        _columnsPeople.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("relationship", new TableInfo.Column("relationship", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("photoUri", new TableInfo.Column("photoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("birthday", new TableInfo.Column("birthday", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("importantDates", new TableInfo.Column("importantDates", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("mentionCount", new TableInfo.Column("mentionCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPeople = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPeople = new HashSet<TableInfo.Index>(1);
        _indicesPeople.add(new TableInfo.Index("index_people_name", false, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoPeople = new TableInfo("people", _columnsPeople, _foreignKeysPeople, _indicesPeople);
        final TableInfo _existingPeople = TableInfo.read(db, "people");
        if (!_infoPeople.equals(_existingPeople)) {
          return new RoomOpenHelper.ValidationResult(false, "people(com.dairum.data.local.entity.PersonEntity).\n"
                  + " Expected:\n" + _infoPeople + "\n"
                  + " Found:\n" + _existingPeople);
        }
        final HashMap<String, TableInfo.Column> _columnsEntryPeople = new HashMap<String, TableInfo.Column>(2);
        _columnsEntryPeople.put("entryId", new TableInfo.Column("entryId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEntryPeople.put("personId", new TableInfo.Column("personId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEntryPeople = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysEntryPeople.add(new TableInfo.ForeignKey("journal_entries", "CASCADE", "NO ACTION", Arrays.asList("entryId"), Arrays.asList("id")));
        _foreignKeysEntryPeople.add(new TableInfo.ForeignKey("people", "CASCADE", "NO ACTION", Arrays.asList("personId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesEntryPeople = new HashSet<TableInfo.Index>(2);
        _indicesEntryPeople.add(new TableInfo.Index("index_entry_people_entryId", false, Arrays.asList("entryId"), Arrays.asList("ASC")));
        _indicesEntryPeople.add(new TableInfo.Index("index_entry_people_personId", false, Arrays.asList("personId"), Arrays.asList("ASC")));
        final TableInfo _infoEntryPeople = new TableInfo("entry_people", _columnsEntryPeople, _foreignKeysEntryPeople, _indicesEntryPeople);
        final TableInfo _existingEntryPeople = TableInfo.read(db, "entry_people");
        if (!_infoEntryPeople.equals(_existingEntryPeople)) {
          return new RoomOpenHelper.ValidationResult(false, "entry_people(com.dairum.data.local.entity.EntryPersonCrossRef).\n"
                  + " Expected:\n" + _infoEntryPeople + "\n"
                  + " Found:\n" + _existingEntryPeople);
        }
        final HashMap<String, TableInfo.Column> _columnsExportConfigs = new HashMap<String, TableInfo.Column>(12);
        _columnsExportConfigs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("format", new TableInfo.Column("format", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("scheduleHour", new TableInfo.Column("scheduleHour", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("scheduleMinute", new TableInfo.Column("scheduleMinute", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("scheduleDays", new TableInfo.Column("scheduleDays", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("exportPath", new TableInfo.Column("exportPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("includeMedia", new TableInfo.Column("includeMedia", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("lastExportAt", new TableInfo.Column("lastExportAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("lastExportStatus", new TableInfo.Column("lastExportStatus", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportConfigs.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExportConfigs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExportConfigs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExportConfigs = new TableInfo("export_configs", _columnsExportConfigs, _foreignKeysExportConfigs, _indicesExportConfigs);
        final TableInfo _existingExportConfigs = TableInfo.read(db, "export_configs");
        if (!_infoExportConfigs.equals(_existingExportConfigs)) {
          return new RoomOpenHelper.ValidationResult(false, "export_configs(com.dairum.data.local.entity.ExportConfigEntity).\n"
                  + " Expected:\n" + _infoExportConfigs + "\n"
                  + " Found:\n" + _existingExportConfigs);
        }
        final HashMap<String, TableInfo.Column> _columnsExportHistory = new HashMap<String, TableInfo.Column>(10);
        _columnsExportHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportHistory.put("configId", new TableInfo.Column("configId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportHistory.put("format", new TableInfo.Column("format", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportHistory.put("filePath", new TableInfo.Column("filePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportHistory.put("fileName", new TableInfo.Column("fileName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportHistory.put("entriesCount", new TableInfo.Column("entriesCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportHistory.put("fileSize", new TableInfo.Column("fileSize", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportHistory.put("isSuccess", new TableInfo.Column("isSuccess", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportHistory.put("errorMessage", new TableInfo.Column("errorMessage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExportHistory.put("exportedAt", new TableInfo.Column("exportedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExportHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExportHistory = new HashSet<TableInfo.Index>(1);
        _indicesExportHistory.add(new TableInfo.Index("index_export_history_exportedAt", false, Arrays.asList("exportedAt"), Arrays.asList("ASC")));
        final TableInfo _infoExportHistory = new TableInfo("export_history", _columnsExportHistory, _foreignKeysExportHistory, _indicesExportHistory);
        final TableInfo _existingExportHistory = TableInfo.read(db, "export_history");
        if (!_infoExportHistory.equals(_existingExportHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "export_history(com.dairum.data.local.entity.ExportHistoryEntity).\n"
                  + " Expected:\n" + _infoExportHistory + "\n"
                  + " Found:\n" + _existingExportHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsReminderConfigs = new HashMap<String, TableInfo.Column>(13);
        _columnsReminderConfigs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("scheduleHour", new TableInfo.Column("scheduleHour", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("scheduleMinute", new TableInfo.Column("scheduleMinute", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("scheduleDays", new TableInfo.Column("scheduleDays", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("notificationTitle", new TableInfo.Column("notificationTitle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("notificationMessage", new TableInfo.Column("notificationMessage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminderConfigs.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReminderConfigs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReminderConfigs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReminderConfigs = new TableInfo("reminder_configs", _columnsReminderConfigs, _foreignKeysReminderConfigs, _indicesReminderConfigs);
        final TableInfo _existingReminderConfigs = TableInfo.read(db, "reminder_configs");
        if (!_infoReminderConfigs.equals(_existingReminderConfigs)) {
          return new RoomOpenHelper.ValidationResult(false, "reminder_configs(com.dairum.data.local.entity.ReminderConfigEntity).\n"
                  + " Expected:\n" + _infoReminderConfigs + "\n"
                  + " Found:\n" + _existingReminderConfigs);
        }
        final HashMap<String, TableInfo.Column> _columnsSmartReminders = new HashMap<String, TableInfo.Column>(13);
        _columnsSmartReminders.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("reminderType", new TableInfo.Column("reminderType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("reminderDate", new TableInfo.Column("reminderDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("repeatYearly", new TableInfo.Column("repeatYearly", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("relatedPersonId", new TableInfo.Column("relatedPersonId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("relatedEntryId", new TableInfo.Column("relatedEntryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("notifyDaysBefore", new TableInfo.Column("notifyDaysBefore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("lastNotifiedAt", new TableInfo.Column("lastNotifiedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("metadata", new TableInfo.Column("metadata", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartReminders.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSmartReminders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSmartReminders = new HashSet<TableInfo.Index>(2);
        _indicesSmartReminders.add(new TableInfo.Index("index_smart_reminders_reminderDate", false, Arrays.asList("reminderDate"), Arrays.asList("ASC")));
        _indicesSmartReminders.add(new TableInfo.Index("index_smart_reminders_reminderType", false, Arrays.asList("reminderType"), Arrays.asList("ASC")));
        final TableInfo _infoSmartReminders = new TableInfo("smart_reminders", _columnsSmartReminders, _foreignKeysSmartReminders, _indicesSmartReminders);
        final TableInfo _existingSmartReminders = TableInfo.read(db, "smart_reminders");
        if (!_infoSmartReminders.equals(_existingSmartReminders)) {
          return new RoomOpenHelper.ValidationResult(false, "smart_reminders(com.dairum.data.local.entity.SmartReminderEntity).\n"
                  + " Expected:\n" + _infoSmartReminders + "\n"
                  + " Found:\n" + _existingSmartReminders);
        }
        final HashMap<String, TableInfo.Column> _columnsNotificationHistory = new HashMap<String, TableInfo.Column>(7);
        _columnsNotificationHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationHistory.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationHistory.put("sourceId", new TableInfo.Column("sourceId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationHistory.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationHistory.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationHistory.put("wasTapped", new TableInfo.Column("wasTapped", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationHistory.put("sentAt", new TableInfo.Column("sentAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotificationHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotificationHistory = new HashSet<TableInfo.Index>(1);
        _indicesNotificationHistory.add(new TableInfo.Index("index_notification_history_sentAt", false, Arrays.asList("sentAt"), Arrays.asList("ASC")));
        final TableInfo _infoNotificationHistory = new TableInfo("notification_history", _columnsNotificationHistory, _foreignKeysNotificationHistory, _indicesNotificationHistory);
        final TableInfo _existingNotificationHistory = TableInfo.read(db, "notification_history");
        if (!_infoNotificationHistory.equals(_existingNotificationHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "notification_history(com.dairum.data.local.entity.NotificationHistoryEntity).\n"
                  + " Expected:\n" + _infoNotificationHistory + "\n"
                  + " Found:\n" + _existingNotificationHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsMemories = new HashMap<String, TableInfo.Column>(23);
        _columnsMemories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("coverImageUri", new TableInfo.Column("coverImageUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("endDate", new TableInfo.Column("endDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("locationData", new TableInfo.Column("locationData", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("color", new TableInfo.Column("color", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("icon", new TableInfo.Column("icon", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("tags", new TableInfo.Column("tags", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("moodSummary", new TableInfo.Column("moodSummary", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("totalTimeSpent", new TableInfo.Column("totalTimeSpent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("entryCount", new TableInfo.Column("entryCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("wordCount", new TableInfo.Column("wordCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("photoCount", new TableInfo.Column("photoCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("videoCount", new TableInfo.Column("videoCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("peopleInvolved", new TableInfo.Column("peopleInvolved", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("highlights", new TableInfo.Column("highlights", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("trackerSummary", new TableInfo.Column("trackerSummary", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMemories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMemories = new HashSet<TableInfo.Index>(3);
        _indicesMemories.add(new TableInfo.Index("index_memories_startDate", false, Arrays.asList("startDate"), Arrays.asList("ASC")));
        _indicesMemories.add(new TableInfo.Index("index_memories_endDate", false, Arrays.asList("endDate"), Arrays.asList("ASC")));
        _indicesMemories.add(new TableInfo.Index("index_memories_isFavorite", false, Arrays.asList("isFavorite"), Arrays.asList("ASC")));
        final TableInfo _infoMemories = new TableInfo("memories", _columnsMemories, _foreignKeysMemories, _indicesMemories);
        final TableInfo _existingMemories = TableInfo.read(db, "memories");
        if (!_infoMemories.equals(_existingMemories)) {
          return new RoomOpenHelper.ValidationResult(false, "memories(com.dairum.data.local.entity.MemoryEntity).\n"
                  + " Expected:\n" + _infoMemories + "\n"
                  + " Found:\n" + _existingMemories);
        }
        final HashMap<String, TableInfo.Column> _columnsMemoryEntries = new HashMap<String, TableInfo.Column>(4);
        _columnsMemoryEntries.put("memoryId", new TableInfo.Column("memoryId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryEntries.put("entryId", new TableInfo.Column("entryId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryEntries.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryEntries.put("addedAt", new TableInfo.Column("addedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMemoryEntries = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysMemoryEntries.add(new TableInfo.ForeignKey("memories", "CASCADE", "NO ACTION", Arrays.asList("memoryId"), Arrays.asList("id")));
        _foreignKeysMemoryEntries.add(new TableInfo.ForeignKey("journal_entries", "CASCADE", "NO ACTION", Arrays.asList("entryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMemoryEntries = new HashSet<TableInfo.Index>(2);
        _indicesMemoryEntries.add(new TableInfo.Index("index_memory_entries_memoryId", false, Arrays.asList("memoryId"), Arrays.asList("ASC")));
        _indicesMemoryEntries.add(new TableInfo.Index("index_memory_entries_entryId", false, Arrays.asList("entryId"), Arrays.asList("ASC")));
        final TableInfo _infoMemoryEntries = new TableInfo("memory_entries", _columnsMemoryEntries, _foreignKeysMemoryEntries, _indicesMemoryEntries);
        final TableInfo _existingMemoryEntries = TableInfo.read(db, "memory_entries");
        if (!_infoMemoryEntries.equals(_existingMemoryEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "memory_entries(com.dairum.data.local.entity.MemoryEntryCrossRef).\n"
                  + " Expected:\n" + _infoMemoryEntries + "\n"
                  + " Found:\n" + _existingMemoryEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsMemoryMilestones = new HashMap<String, TableInfo.Column>(8);
        _columnsMemoryMilestones.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryMilestones.put("memoryId", new TableInfo.Column("memoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryMilestones.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryMilestones.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryMilestones.put("milestoneDate", new TableInfo.Column("milestoneDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryMilestones.put("relatedEntryId", new TableInfo.Column("relatedEntryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryMilestones.put("icon", new TableInfo.Column("icon", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryMilestones.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMemoryMilestones = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMemoryMilestones.add(new TableInfo.ForeignKey("memories", "CASCADE", "NO ACTION", Arrays.asList("memoryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMemoryMilestones = new HashSet<TableInfo.Index>(1);
        _indicesMemoryMilestones.add(new TableInfo.Index("index_memory_milestones_memoryId", false, Arrays.asList("memoryId"), Arrays.asList("ASC")));
        final TableInfo _infoMemoryMilestones = new TableInfo("memory_milestones", _columnsMemoryMilestones, _foreignKeysMemoryMilestones, _indicesMemoryMilestones);
        final TableInfo _existingMemoryMilestones = TableInfo.read(db, "memory_milestones");
        if (!_infoMemoryMilestones.equals(_existingMemoryMilestones)) {
          return new RoomOpenHelper.ValidationResult(false, "memory_milestones(com.dairum.data.local.entity.MemoryMilestoneEntity).\n"
                  + " Expected:\n" + _infoMemoryMilestones + "\n"
                  + " Found:\n" + _existingMemoryMilestones);
        }
        final HashMap<String, TableInfo.Column> _columnsMemoryTrackers = new HashMap<String, TableInfo.Column>(9);
        _columnsMemoryTrackers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryTrackers.put("memoryId", new TableInfo.Column("memoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryTrackers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryTrackers.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryTrackers.put("icon", new TableInfo.Column("icon", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryTrackers.put("unit", new TableInfo.Column("unit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryTrackers.put("goalValue", new TableInfo.Column("goalValue", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryTrackers.put("currentValue", new TableInfo.Column("currentValue", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemoryTrackers.put("progress", new TableInfo.Column("progress", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMemoryTrackers = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMemoryTrackers.add(new TableInfo.ForeignKey("memories", "CASCADE", "NO ACTION", Arrays.asList("memoryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMemoryTrackers = new HashSet<TableInfo.Index>(1);
        _indicesMemoryTrackers.add(new TableInfo.Index("index_memory_trackers_memoryId", false, Arrays.asList("memoryId"), Arrays.asList("ASC")));
        final TableInfo _infoMemoryTrackers = new TableInfo("memory_trackers", _columnsMemoryTrackers, _foreignKeysMemoryTrackers, _indicesMemoryTrackers);
        final TableInfo _existingMemoryTrackers = TableInfo.read(db, "memory_trackers");
        if (!_infoMemoryTrackers.equals(_existingMemoryTrackers)) {
          return new RoomOpenHelper.ValidationResult(false, "memory_trackers(com.dairum.data.local.entity.MemoryTrackerEntity).\n"
                  + " Expected:\n" + _infoMemoryTrackers + "\n"
                  + " Found:\n" + _existingMemoryTrackers);
        }
        final HashMap<String, TableInfo.Column> _columnsMoodCheckinConfig = new HashMap<String, TableInfo.Column>(9);
        _columnsMoodCheckinConfig.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodCheckinConfig.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodCheckinConfig.put("intervalHours", new TableInfo.Column("intervalHours", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodCheckinConfig.put("startHour", new TableInfo.Column("startHour", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodCheckinConfig.put("endHour", new TableInfo.Column("endHour", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodCheckinConfig.put("lastPromptAt", new TableInfo.Column("lastPromptAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodCheckinConfig.put("enabledDays", new TableInfo.Column("enabledDays", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodCheckinConfig.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodCheckinConfig.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMoodCheckinConfig = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMoodCheckinConfig = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMoodCheckinConfig = new TableInfo("mood_checkin_config", _columnsMoodCheckinConfig, _foreignKeysMoodCheckinConfig, _indicesMoodCheckinConfig);
        final TableInfo _existingMoodCheckinConfig = TableInfo.read(db, "mood_checkin_config");
        if (!_infoMoodCheckinConfig.equals(_existingMoodCheckinConfig)) {
          return new RoomOpenHelper.ValidationResult(false, "mood_checkin_config(com.dairum.data.local.entity.MoodCheckInConfigEntity).\n"
                  + " Expected:\n" + _infoMoodCheckinConfig + "\n"
                  + " Found:\n" + _existingMoodCheckinConfig);
        }
        final HashMap<String, TableInfo.Column> _columnsAiModels = new HashMap<String, TableInfo.Column>(13);
        _columnsAiModels.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("filePath", new TableInfo.Column("filePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("sizeBytes", new TableInfo.Column("sizeBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("modelType", new TableInfo.Column("modelType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("quantization", new TableInfo.Column("quantization", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("contextLength", new TableInfo.Column("contextLength", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("isDefault", new TableInfo.Column("isDefault", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("isDownloaded", new TableInfo.Column("isDownloaded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("downloadUrl", new TableInfo.Column("downloadUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiModels.put("lastUsedAt", new TableInfo.Column("lastUsedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAiModels = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAiModels = new HashSet<TableInfo.Index>(2);
        _indicesAiModels.add(new TableInfo.Index("index_ai_models_modelType", false, Arrays.asList("modelType"), Arrays.asList("ASC")));
        _indicesAiModels.add(new TableInfo.Index("index_ai_models_isDefault", false, Arrays.asList("isDefault"), Arrays.asList("ASC")));
        final TableInfo _infoAiModels = new TableInfo("ai_models", _columnsAiModels, _foreignKeysAiModels, _indicesAiModels);
        final TableInfo _existingAiModels = TableInfo.read(db, "ai_models");
        if (!_infoAiModels.equals(_existingAiModels)) {
          return new RoomOpenHelper.ValidationResult(false, "ai_models(com.dairum.data.local.entity.AIModelEntity).\n"
                  + " Expected:\n" + _infoAiModels + "\n"
                  + " Found:\n" + _existingAiModels);
        }
        final HashMap<String, TableInfo.Column> _columnsJournalEmbeddings = new HashMap<String, TableInfo.Column>(8);
        _columnsJournalEmbeddings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEmbeddings.put("entryId", new TableInfo.Column("entryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEmbeddings.put("embedding", new TableInfo.Column("embedding", "BLOB", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEmbeddings.put("embeddingDimension", new TableInfo.Column("embeddingDimension", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEmbeddings.put("model", new TableInfo.Column("model", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEmbeddings.put("version", new TableInfo.Column("version", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEmbeddings.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEmbeddings.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysJournalEmbeddings = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysJournalEmbeddings.add(new TableInfo.ForeignKey("journal_entries", "CASCADE", "NO ACTION", Arrays.asList("entryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesJournalEmbeddings = new HashSet<TableInfo.Index>(2);
        _indicesJournalEmbeddings.add(new TableInfo.Index("index_journal_embeddings_entryId", true, Arrays.asList("entryId"), Arrays.asList("ASC")));
        _indicesJournalEmbeddings.add(new TableInfo.Index("index_journal_embeddings_model", false, Arrays.asList("model"), Arrays.asList("ASC")));
        final TableInfo _infoJournalEmbeddings = new TableInfo("journal_embeddings", _columnsJournalEmbeddings, _foreignKeysJournalEmbeddings, _indicesJournalEmbeddings);
        final TableInfo _existingJournalEmbeddings = TableInfo.read(db, "journal_embeddings");
        if (!_infoJournalEmbeddings.equals(_existingJournalEmbeddings)) {
          return new RoomOpenHelper.ValidationResult(false, "journal_embeddings(com.dairum.data.local.entity.JournalEmbeddingEntity).\n"
                  + " Expected:\n" + _infoJournalEmbeddings + "\n"
                  + " Found:\n" + _existingJournalEmbeddings);
        }
        final HashMap<String, TableInfo.Column> _columnsWeeklyReflections = new HashMap<String, TableInfo.Column>(16);
        _columnsWeeklyReflections.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("weekStartDate", new TableInfo.Column("weekStartDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("weekEndDate", new TableInfo.Column("weekEndDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("summary", new TableInfo.Column("summary", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("averageMood", new TableInfo.Column("averageMood", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("moodRangeMin", new TableInfo.Column("moodRangeMin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("moodRangeMax", new TableInfo.Column("moodRangeMax", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("dominantMood", new TableInfo.Column("dominantMood", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("moodDistributionJson", new TableInfo.Column("moodDistributionJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("moodTrend", new TableInfo.Column("moodTrend", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("keyThemesJson", new TableInfo.Column("keyThemesJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("highlightsJson", new TableInfo.Column("highlightsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("suggestionsJson", new TableInfo.Column("suggestionsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("entryCount", new TableInfo.Column("entryCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("totalWordCount", new TableInfo.Column("totalWordCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklyReflections.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWeeklyReflections = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWeeklyReflections = new HashSet<TableInfo.Index>(1);
        _indicesWeeklyReflections.add(new TableInfo.Index("index_weekly_reflections_weekStartDate", true, Arrays.asList("weekStartDate"), Arrays.asList("ASC")));
        final TableInfo _infoWeeklyReflections = new TableInfo("weekly_reflections", _columnsWeeklyReflections, _foreignKeysWeeklyReflections, _indicesWeeklyReflections);
        final TableInfo _existingWeeklyReflections = TableInfo.read(db, "weekly_reflections");
        if (!_infoWeeklyReflections.equals(_existingWeeklyReflections)) {
          return new RoomOpenHelper.ValidationResult(false, "weekly_reflections(com.dairum.data.local.entity.WeeklyReflectionEntity).\n"
                  + " Expected:\n" + _infoWeeklyReflections + "\n"
                  + " Found:\n" + _existingWeeklyReflections);
        }
        final HashMap<String, TableInfo.Column> _columnsPatternResults = new HashMap<String, TableInfo.Column>(7);
        _columnsPatternResults.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternResults.put("patternType", new TableInfo.Column("patternType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternResults.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternResults.put("confidence", new TableInfo.Column("confidence", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternResults.put("evidenceJson", new TableInfo.Column("evidenceJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternResults.put("insightsJson", new TableInfo.Column("insightsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatternResults.put("detectedAt", new TableInfo.Column("detectedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPatternResults = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPatternResults = new HashSet<TableInfo.Index>(2);
        _indicesPatternResults.add(new TableInfo.Index("index_pattern_results_patternType", false, Arrays.asList("patternType"), Arrays.asList("ASC")));
        _indicesPatternResults.add(new TableInfo.Index("index_pattern_results_detectedAt", false, Arrays.asList("detectedAt"), Arrays.asList("ASC")));
        final TableInfo _infoPatternResults = new TableInfo("pattern_results", _columnsPatternResults, _foreignKeysPatternResults, _indicesPatternResults);
        final TableInfo _existingPatternResults = TableInfo.read(db, "pattern_results");
        if (!_infoPatternResults.equals(_existingPatternResults)) {
          return new RoomOpenHelper.ValidationResult(false, "pattern_results(com.dairum.data.local.entity.PatternResultEntity).\n"
                  + " Expected:\n" + _infoPatternResults + "\n"
                  + " Found:\n" + _existingPatternResults);
        }
        final HashMap<String, TableInfo.Column> _columnsSmartSummaries = new HashMap<String, TableInfo.Column>(13);
        _columnsSmartSummaries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("summaryType", new TableInfo.Column("summaryType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("periodStart", new TableInfo.Column("periodStart", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("periodEnd", new TableInfo.Column("periodEnd", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("summary", new TableInfo.Column("summary", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("keyPointsJson", new TableInfo.Column("keyPointsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("moodOverview", new TableInfo.Column("moodOverview", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("topTagsJson", new TableInfo.Column("topTagsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("topPeopleJson", new TableInfo.Column("topPeopleJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("wordCount", new TableInfo.Column("wordCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("entryCount", new TableInfo.Column("entryCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmartSummaries.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSmartSummaries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSmartSummaries = new HashSet<TableInfo.Index>(2);
        _indicesSmartSummaries.add(new TableInfo.Index("index_smart_summaries_summaryType", false, Arrays.asList("summaryType"), Arrays.asList("ASC")));
        _indicesSmartSummaries.add(new TableInfo.Index("index_smart_summaries_periodStart_periodEnd", false, Arrays.asList("periodStart", "periodEnd"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoSmartSummaries = new TableInfo("smart_summaries", _columnsSmartSummaries, _foreignKeysSmartSummaries, _indicesSmartSummaries);
        final TableInfo _existingSmartSummaries = TableInfo.read(db, "smart_summaries");
        if (!_infoSmartSummaries.equals(_existingSmartSummaries)) {
          return new RoomOpenHelper.ValidationResult(false, "smart_summaries(com.dairum.data.local.entity.SmartSummaryEntity).\n"
                  + " Expected:\n" + _infoSmartSummaries + "\n"
                  + " Found:\n" + _existingSmartSummaries);
        }
        final HashMap<String, TableInfo.Column> _columnsAiConversations = new HashMap<String, TableInfo.Column>(8);
        _columnsAiConversations.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiConversations.put("sessionId", new TableInfo.Column("sessionId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiConversations.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiConversations.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiConversations.put("sourceEntryIdsJson", new TableInfo.Column("sourceEntryIdsJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiConversations.put("tokensUsed", new TableInfo.Column("tokensUsed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiConversations.put("generationTimeMs", new TableInfo.Column("generationTimeMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAiConversations.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAiConversations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAiConversations = new HashSet<TableInfo.Index>(2);
        _indicesAiConversations.add(new TableInfo.Index("index_ai_conversations_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        _indicesAiConversations.add(new TableInfo.Index("index_ai_conversations_createdAt", false, Arrays.asList("createdAt"), Arrays.asList("ASC")));
        final TableInfo _infoAiConversations = new TableInfo("ai_conversations", _columnsAiConversations, _foreignKeysAiConversations, _indicesAiConversations);
        final TableInfo _existingAiConversations = TableInfo.read(db, "ai_conversations");
        if (!_infoAiConversations.equals(_existingAiConversations)) {
          return new RoomOpenHelper.ValidationResult(false, "ai_conversations(com.dairum.data.local.entity.AIConversationEntity).\n"
                  + " Expected:\n" + _infoAiConversations + "\n"
                  + " Found:\n" + _existingAiConversations);
        }
        final HashMap<String, TableInfo.Column> _columnsChatSessions = new HashMap<String, TableInfo.Column>(9);
        _columnsChatSessions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatSessions.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatSessions.put("summary", new TableInfo.Column("summary", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatSessions.put("isSaved", new TableInfo.Column("isSaved", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatSessions.put("isArchived", new TableInfo.Column("isArchived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatSessions.put("messageCount", new TableInfo.Column("messageCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatSessions.put("lastMessageAt", new TableInfo.Column("lastMessageAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatSessions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatSessions.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChatSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChatSessions = new HashSet<TableInfo.Index>(3);
        _indicesChatSessions.add(new TableInfo.Index("index_chat_sessions_createdAt", false, Arrays.asList("createdAt"), Arrays.asList("ASC")));
        _indicesChatSessions.add(new TableInfo.Index("index_chat_sessions_isSaved", false, Arrays.asList("isSaved"), Arrays.asList("ASC")));
        _indicesChatSessions.add(new TableInfo.Index("index_chat_sessions_isArchived", false, Arrays.asList("isArchived"), Arrays.asList("ASC")));
        final TableInfo _infoChatSessions = new TableInfo("chat_sessions", _columnsChatSessions, _foreignKeysChatSessions, _indicesChatSessions);
        final TableInfo _existingChatSessions = TableInfo.read(db, "chat_sessions");
        if (!_infoChatSessions.equals(_existingChatSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "chat_sessions(com.dairum.data.local.entity.ChatSessionEntity).\n"
                  + " Expected:\n" + _infoChatSessions + "\n"
                  + " Found:\n" + _existingChatSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsChatMessages = new HashMap<String, TableInfo.Column>(9);
        _columnsChatMessages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("sessionId", new TableInfo.Column("sessionId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("tokensUsed", new TableInfo.Column("tokensUsed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("generationTimeMs", new TableInfo.Column("generationTimeMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("isEdited", new TableInfo.Column("isEdited", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("metadata", new TableInfo.Column("metadata", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChatMessages = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysChatMessages.add(new TableInfo.ForeignKey("chat_sessions", "CASCADE", "NO ACTION", Arrays.asList("sessionId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesChatMessages = new HashSet<TableInfo.Index>(3);
        _indicesChatMessages.add(new TableInfo.Index("index_chat_messages_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        _indicesChatMessages.add(new TableInfo.Index("index_chat_messages_createdAt", false, Arrays.asList("createdAt"), Arrays.asList("ASC")));
        _indicesChatMessages.add(new TableInfo.Index("index_chat_messages_role", false, Arrays.asList("role"), Arrays.asList("ASC")));
        final TableInfo _infoChatMessages = new TableInfo("chat_messages", _columnsChatMessages, _foreignKeysChatMessages, _indicesChatMessages);
        final TableInfo _existingChatMessages = TableInfo.read(db, "chat_messages");
        if (!_infoChatMessages.equals(_existingChatMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "chat_messages(com.dairum.data.local.entity.ChatMessageEntity).\n"
                  + " Expected:\n" + _infoChatMessages + "\n"
                  + " Found:\n" + _existingChatMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsChatEmbeddings = new HashMap<String, TableInfo.Column>(7);
        _columnsChatEmbeddings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatEmbeddings.put("messageId", new TableInfo.Column("messageId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatEmbeddings.put("sessionId", new TableInfo.Column("sessionId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatEmbeddings.put("embedding", new TableInfo.Column("embedding", "BLOB", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatEmbeddings.put("embeddingDimension", new TableInfo.Column("embeddingDimension", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatEmbeddings.put("model", new TableInfo.Column("model", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatEmbeddings.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChatEmbeddings = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysChatEmbeddings.add(new TableInfo.ForeignKey("chat_messages", "CASCADE", "NO ACTION", Arrays.asList("messageId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesChatEmbeddings = new HashSet<TableInfo.Index>(3);
        _indicesChatEmbeddings.add(new TableInfo.Index("index_chat_embeddings_messageId", true, Arrays.asList("messageId"), Arrays.asList("ASC")));
        _indicesChatEmbeddings.add(new TableInfo.Index("index_chat_embeddings_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        _indicesChatEmbeddings.add(new TableInfo.Index("index_chat_embeddings_model", false, Arrays.asList("model"), Arrays.asList("ASC")));
        final TableInfo _infoChatEmbeddings = new TableInfo("chat_embeddings", _columnsChatEmbeddings, _foreignKeysChatEmbeddings, _indicesChatEmbeddings);
        final TableInfo _existingChatEmbeddings = TableInfo.read(db, "chat_embeddings");
        if (!_infoChatEmbeddings.equals(_existingChatEmbeddings)) {
          return new RoomOpenHelper.ValidationResult(false, "chat_embeddings(com.dairum.data.local.entity.ChatEmbeddingEntity).\n"
                  + " Expected:\n" + _infoChatEmbeddings + "\n"
                  + " Found:\n" + _existingChatEmbeddings);
        }
        final HashMap<String, TableInfo.Column> _columnsReflectionMemories = new HashMap<String, TableInfo.Column>(13);
        _columnsReflectionMemories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("confidence", new TableInfo.Column("confidence", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("sourceMessageIds", new TableInfo.Column("sourceMessageIds", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("sourceSessionIds", new TableInfo.Column("sourceSessionIds", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("occurrenceCount", new TableInfo.Column("occurrenceCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("lastReferenced", new TableInfo.Column("lastReferenced", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("embedding", new TableInfo.Column("embedding", "BLOB", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("embeddingModel", new TableInfo.Column("embeddingModel", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflectionMemories.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReflectionMemories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReflectionMemories = new HashSet<TableInfo.Index>(4);
        _indicesReflectionMemories.add(new TableInfo.Index("index_reflection_memories_category", false, Arrays.asList("category"), Arrays.asList("ASC")));
        _indicesReflectionMemories.add(new TableInfo.Index("index_reflection_memories_createdAt", false, Arrays.asList("createdAt"), Arrays.asList("ASC")));
        _indicesReflectionMemories.add(new TableInfo.Index("index_reflection_memories_confidence", false, Arrays.asList("confidence"), Arrays.asList("ASC")));
        _indicesReflectionMemories.add(new TableInfo.Index("index_reflection_memories_isActive", false, Arrays.asList("isActive"), Arrays.asList("ASC")));
        final TableInfo _infoReflectionMemories = new TableInfo("reflection_memories", _columnsReflectionMemories, _foreignKeysReflectionMemories, _indicesReflectionMemories);
        final TableInfo _existingReflectionMemories = TableInfo.read(db, "reflection_memories");
        if (!_infoReflectionMemories.equals(_existingReflectionMemories)) {
          return new RoomOpenHelper.ValidationResult(false, "reflection_memories(com.dairum.data.local.entity.ReflectionMemoryEntity).\n"
                  + " Expected:\n" + _infoReflectionMemories + "\n"
                  + " Found:\n" + _existingReflectionMemories);
        }
        final HashMap<String, TableInfo.Column> _columnsConversationContext = new HashMap<String, TableInfo.Column>(9);
        _columnsConversationContext.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversationContext.put("sessionId", new TableInfo.Column("sessionId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversationContext.put("currentTopics", new TableInfo.Column("currentTopics", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversationContext.put("emotionalTone", new TableInfo.Column("emotionalTone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversationContext.put("pendingQuestions", new TableInfo.Column("pendingQuestions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversationContext.put("userPreferences", new TableInfo.Column("userPreferences", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversationContext.put("discussionDepth", new TableInfo.Column("discussionDepth", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversationContext.put("lastSummaryAt", new TableInfo.Column("lastSummaryAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversationContext.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysConversationContext = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysConversationContext.add(new TableInfo.ForeignKey("chat_sessions", "CASCADE", "NO ACTION", Arrays.asList("sessionId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesConversationContext = new HashSet<TableInfo.Index>(1);
        _indicesConversationContext.add(new TableInfo.Index("index_conversation_context_sessionId", true, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        final TableInfo _infoConversationContext = new TableInfo("conversation_context", _columnsConversationContext, _foreignKeysConversationContext, _indicesConversationContext);
        final TableInfo _existingConversationContext = TableInfo.read(db, "conversation_context");
        if (!_infoConversationContext.equals(_existingConversationContext)) {
          return new RoomOpenHelper.ValidationResult(false, "conversation_context(com.dairum.data.local.entity.ConversationContextEntity).\n"
                  + " Expected:\n" + _infoConversationContext + "\n"
                  + " Found:\n" + _existingConversationContext);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d2d98dd1ca3a5bdd53fa85267eab944c", "e70d76296170094d4e4c31636a7759d9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "journal_entries","categories","tags","entry_tags","media","custom_trackers","tracker_values","people","entry_people","export_configs","export_history","reminder_configs","smart_reminders","notification_history","memories","memory_entries","memory_milestones","memory_trackers","mood_checkin_config","ai_models","journal_embeddings","weekly_reflections","pattern_results","smart_summaries","ai_conversations","chat_sessions","chat_messages","chat_embeddings","reflection_memories","conversation_context");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `journal_entries`");
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `tags`");
      _db.execSQL("DELETE FROM `entry_tags`");
      _db.execSQL("DELETE FROM `media`");
      _db.execSQL("DELETE FROM `custom_trackers`");
      _db.execSQL("DELETE FROM `tracker_values`");
      _db.execSQL("DELETE FROM `people`");
      _db.execSQL("DELETE FROM `entry_people`");
      _db.execSQL("DELETE FROM `export_configs`");
      _db.execSQL("DELETE FROM `export_history`");
      _db.execSQL("DELETE FROM `reminder_configs`");
      _db.execSQL("DELETE FROM `smart_reminders`");
      _db.execSQL("DELETE FROM `notification_history`");
      _db.execSQL("DELETE FROM `memories`");
      _db.execSQL("DELETE FROM `memory_entries`");
      _db.execSQL("DELETE FROM `memory_milestones`");
      _db.execSQL("DELETE FROM `memory_trackers`");
      _db.execSQL("DELETE FROM `mood_checkin_config`");
      _db.execSQL("DELETE FROM `ai_models`");
      _db.execSQL("DELETE FROM `journal_embeddings`");
      _db.execSQL("DELETE FROM `weekly_reflections`");
      _db.execSQL("DELETE FROM `pattern_results`");
      _db.execSQL("DELETE FROM `smart_summaries`");
      _db.execSQL("DELETE FROM `ai_conversations`");
      _db.execSQL("DELETE FROM `chat_sessions`");
      _db.execSQL("DELETE FROM `chat_messages`");
      _db.execSQL("DELETE FROM `chat_embeddings`");
      _db.execSQL("DELETE FROM `reflection_memories`");
      _db.execSQL("DELETE FROM `conversation_context`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(JournalEntryDao.class, JournalEntryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TagDao.class, TagDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MediaDao.class, MediaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PersonDao.class, PersonDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TrackerDao.class, TrackerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExportDao.class, ExportDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NotificationDao.class, NotificationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MemoryDao.class, MemoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AIModelDao.class, AIModelDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(JournalEmbeddingDao.class, JournalEmbeddingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WeeklyReflectionDao.class, WeeklyReflectionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PatternResultDao.class, PatternResultDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SmartSummaryDao.class, SmartSummaryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AIConversationDao.class, AIConversationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChatSessionDao.class, ChatSessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChatMessageDao.class, ChatMessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChatEmbeddingDao.class, ChatEmbeddingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReflectionMemoryDao.class, ReflectionMemoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ConversationContextDao.class, ConversationContextDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public JournalEntryDao journalEntryDao() {
    if (_journalEntryDao != null) {
      return _journalEntryDao;
    } else {
      synchronized(this) {
        if(_journalEntryDao == null) {
          _journalEntryDao = new JournalEntryDao_Impl(this);
        }
        return _journalEntryDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public TagDao tagDao() {
    if (_tagDao != null) {
      return _tagDao;
    } else {
      synchronized(this) {
        if(_tagDao == null) {
          _tagDao = new TagDao_Impl(this);
        }
        return _tagDao;
      }
    }
  }

  @Override
  public MediaDao mediaDao() {
    if (_mediaDao != null) {
      return _mediaDao;
    } else {
      synchronized(this) {
        if(_mediaDao == null) {
          _mediaDao = new MediaDao_Impl(this);
        }
        return _mediaDao;
      }
    }
  }

  @Override
  public PersonDao personDao() {
    if (_personDao != null) {
      return _personDao;
    } else {
      synchronized(this) {
        if(_personDao == null) {
          _personDao = new PersonDao_Impl(this);
        }
        return _personDao;
      }
    }
  }

  @Override
  public TrackerDao trackerDao() {
    if (_trackerDao != null) {
      return _trackerDao;
    } else {
      synchronized(this) {
        if(_trackerDao == null) {
          _trackerDao = new TrackerDao_Impl(this);
        }
        return _trackerDao;
      }
    }
  }

  @Override
  public ExportDao exportDao() {
    if (_exportDao != null) {
      return _exportDao;
    } else {
      synchronized(this) {
        if(_exportDao == null) {
          _exportDao = new ExportDao_Impl(this);
        }
        return _exportDao;
      }
    }
  }

  @Override
  public NotificationDao notificationDao() {
    if (_notificationDao != null) {
      return _notificationDao;
    } else {
      synchronized(this) {
        if(_notificationDao == null) {
          _notificationDao = new NotificationDao_Impl(this);
        }
        return _notificationDao;
      }
    }
  }

  @Override
  public MemoryDao memoryDao() {
    if (_memoryDao != null) {
      return _memoryDao;
    } else {
      synchronized(this) {
        if(_memoryDao == null) {
          _memoryDao = new MemoryDao_Impl(this);
        }
        return _memoryDao;
      }
    }
  }

  @Override
  public AIModelDao aiModelDao() {
    if (_aIModelDao != null) {
      return _aIModelDao;
    } else {
      synchronized(this) {
        if(_aIModelDao == null) {
          _aIModelDao = new AIModelDao_Impl(this);
        }
        return _aIModelDao;
      }
    }
  }

  @Override
  public JournalEmbeddingDao journalEmbeddingDao() {
    if (_journalEmbeddingDao != null) {
      return _journalEmbeddingDao;
    } else {
      synchronized(this) {
        if(_journalEmbeddingDao == null) {
          _journalEmbeddingDao = new JournalEmbeddingDao_Impl(this);
        }
        return _journalEmbeddingDao;
      }
    }
  }

  @Override
  public WeeklyReflectionDao weeklyReflectionDao() {
    if (_weeklyReflectionDao != null) {
      return _weeklyReflectionDao;
    } else {
      synchronized(this) {
        if(_weeklyReflectionDao == null) {
          _weeklyReflectionDao = new WeeklyReflectionDao_Impl(this);
        }
        return _weeklyReflectionDao;
      }
    }
  }

  @Override
  public PatternResultDao patternResultDao() {
    if (_patternResultDao != null) {
      return _patternResultDao;
    } else {
      synchronized(this) {
        if(_patternResultDao == null) {
          _patternResultDao = new PatternResultDao_Impl(this);
        }
        return _patternResultDao;
      }
    }
  }

  @Override
  public SmartSummaryDao smartSummaryDao() {
    if (_smartSummaryDao != null) {
      return _smartSummaryDao;
    } else {
      synchronized(this) {
        if(_smartSummaryDao == null) {
          _smartSummaryDao = new SmartSummaryDao_Impl(this);
        }
        return _smartSummaryDao;
      }
    }
  }

  @Override
  public AIConversationDao aiConversationDao() {
    if (_aIConversationDao != null) {
      return _aIConversationDao;
    } else {
      synchronized(this) {
        if(_aIConversationDao == null) {
          _aIConversationDao = new AIConversationDao_Impl(this);
        }
        return _aIConversationDao;
      }
    }
  }

  @Override
  public ChatSessionDao chatSessionDao() {
    if (_chatSessionDao != null) {
      return _chatSessionDao;
    } else {
      synchronized(this) {
        if(_chatSessionDao == null) {
          _chatSessionDao = new ChatSessionDao_Impl(this);
        }
        return _chatSessionDao;
      }
    }
  }

  @Override
  public ChatMessageDao chatMessageDao() {
    if (_chatMessageDao != null) {
      return _chatMessageDao;
    } else {
      synchronized(this) {
        if(_chatMessageDao == null) {
          _chatMessageDao = new ChatMessageDao_Impl(this);
        }
        return _chatMessageDao;
      }
    }
  }

  @Override
  public ChatEmbeddingDao chatEmbeddingDao() {
    if (_chatEmbeddingDao != null) {
      return _chatEmbeddingDao;
    } else {
      synchronized(this) {
        if(_chatEmbeddingDao == null) {
          _chatEmbeddingDao = new ChatEmbeddingDao_Impl(this);
        }
        return _chatEmbeddingDao;
      }
    }
  }

  @Override
  public ReflectionMemoryDao reflectionMemoryDao() {
    if (_reflectionMemoryDao != null) {
      return _reflectionMemoryDao;
    } else {
      synchronized(this) {
        if(_reflectionMemoryDao == null) {
          _reflectionMemoryDao = new ReflectionMemoryDao_Impl(this);
        }
        return _reflectionMemoryDao;
      }
    }
  }

  @Override
  public ConversationContextDao conversationContextDao() {
    if (_conversationContextDao != null) {
      return _conversationContextDao;
    } else {
      synchronized(this) {
        if(_conversationContextDao == null) {
          _conversationContextDao = new ConversationContextDao_Impl(this);
        }
        return _conversationContextDao;
      }
    }
  }
}
