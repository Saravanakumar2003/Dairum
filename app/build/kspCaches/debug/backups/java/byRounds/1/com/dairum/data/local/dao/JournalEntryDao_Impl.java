package com.dairum.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dairum.data.local.converter.Converters;
import com.dairum.data.local.entity.CategoryEntity;
import com.dairum.data.local.entity.EntryWithDetails;
import com.dairum.data.local.entity.JournalEntryEntity;
import com.dairum.data.local.entity.MediaEntity;
import com.dairum.data.local.entity.MediaType;
import com.dairum.data.local.entity.PersonEntity;
import com.dairum.data.local.entity.TagEntity;
import com.dairum.data.local.entity.TrackerValueEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class JournalEntryDao_Impl implements JournalEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<JournalEntryEntity> __insertionAdapterOfJournalEntryEntity;

  private final EntityDeletionOrUpdateAdapter<JournalEntryEntity> __deletionAdapterOfJournalEntryEntity;

  private final EntityDeletionOrUpdateAdapter<JournalEntryEntity> __updateAdapterOfJournalEntryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFavorite;

  private final SharedSQLiteStatement __preparedStmtOfUpdateArchived;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePinned;

  private final Converters __converters = new Converters();

  public JournalEntryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfJournalEntryEntity = new EntityInsertionAdapter<JournalEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `journal_entries` (`id`,`title`,`content`,`plainContent`,`categoryId`,`createdAt`,`updatedAt`,`moodLevel`,`moodIcon`,`weatherData`,`locationData`,`isFavorite`,`isArchived`,`isDraft`,`isPinned`,`wordCount`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JournalEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        statement.bindString(3, entity.getContent());
        statement.bindString(4, entity.getPlainContent());
        if (entity.getCategoryId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoryId());
        }
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        if (entity.getMoodLevel() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getMoodLevel());
        }
        if (entity.getMoodIcon() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getMoodIcon());
        }
        if (entity.getWeatherData() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getWeatherData());
        }
        if (entity.getLocationData() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getLocationData());
        }
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(12, _tmp);
        final int _tmp_1 = entity.isArchived() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        final int _tmp_2 = entity.isDraft() ? 1 : 0;
        statement.bindLong(14, _tmp_2);
        final int _tmp_3 = entity.isPinned() ? 1 : 0;
        statement.bindLong(15, _tmp_3);
        statement.bindLong(16, entity.getWordCount());
      }
    };
    this.__deletionAdapterOfJournalEntryEntity = new EntityDeletionOrUpdateAdapter<JournalEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `journal_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JournalEntryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfJournalEntryEntity = new EntityDeletionOrUpdateAdapter<JournalEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `journal_entries` SET `id` = ?,`title` = ?,`content` = ?,`plainContent` = ?,`categoryId` = ?,`createdAt` = ?,`updatedAt` = ?,`moodLevel` = ?,`moodIcon` = ?,`weatherData` = ?,`locationData` = ?,`isFavorite` = ?,`isArchived` = ?,`isDraft` = ?,`isPinned` = ?,`wordCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JournalEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        statement.bindString(3, entity.getContent());
        statement.bindString(4, entity.getPlainContent());
        if (entity.getCategoryId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoryId());
        }
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        if (entity.getMoodLevel() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getMoodLevel());
        }
        if (entity.getMoodIcon() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getMoodIcon());
        }
        if (entity.getWeatherData() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getWeatherData());
        }
        if (entity.getLocationData() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getLocationData());
        }
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(12, _tmp);
        final int _tmp_1 = entity.isArchived() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        final int _tmp_2 = entity.isDraft() ? 1 : 0;
        statement.bindLong(14, _tmp_2);
        final int _tmp_3 = entity.isPinned() ? 1 : 0;
        statement.bindLong(15, _tmp_3);
        statement.bindLong(16, entity.getWordCount());
        statement.bindLong(17, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM journal_entries WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateFavorite = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE journal_entries SET isFavorite = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateArchived = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE journal_entries SET isArchived = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePinned = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE journal_entries SET isPinned = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final JournalEntryEntity entry,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfJournalEntryEntity.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final JournalEntryEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfJournalEntryEntity.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final JournalEntryEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfJournalEntryEntity.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFavorite(final long id, final boolean isFavorite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavorite.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFavorite.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateArchived(final long id, final boolean isArchived,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateArchived.acquire();
        int _argIndex = 1;
        final int _tmp = isArchived ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateArchived.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePinned(final long id, final boolean isPinned,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePinned.acquire();
        int _argIndex = 1;
        final int _tmp = isPinned ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdatePinned.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super JournalEntryEntity> $completion) {
    final String _sql = "SELECT * FROM journal_entries WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<JournalEntryEntity>() {
      @Override
      @Nullable
      public JournalEntryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final JournalEntryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _result = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<JournalEntryEntity> getByIdFlow(final long id) {
    final String _sql = "SELECT * FROM journal_entries WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<JournalEntryEntity>() {
      @Override
      @Nullable
      public JournalEntryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final JournalEntryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _result = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getWithDetails(final long id,
      final Continuation<? super EntryWithDetails> $completion) {
    final String _sql = "SELECT * FROM journal_entries WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<EntryWithDetails>() {
      @Override
      @Nullable
      public EntryWithDetails call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
            final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
            final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
            final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
            final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
            final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
            final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<ArrayList<MediaEntity>> _collectionMedia = new LongSparseArray<ArrayList<MediaEntity>>();
            final LongSparseArray<ArrayList<TagEntity>> _collectionTags = new LongSparseArray<ArrayList<TagEntity>>();
            final LongSparseArray<ArrayList<PersonEntity>> _collectionPeople = new LongSparseArray<ArrayList<PersonEntity>>();
            final LongSparseArray<ArrayList<TrackerValueEntity>> _collectionTrackerValues = new LongSparseArray<ArrayList<TrackerValueEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionMedia.containsKey(_tmpKey_1)) {
                _collectionMedia.put(_tmpKey_1, new ArrayList<MediaEntity>());
              }
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionTags.containsKey(_tmpKey_2)) {
                _collectionTags.put(_tmpKey_2, new ArrayList<TagEntity>());
              }
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionPeople.containsKey(_tmpKey_3)) {
                _collectionPeople.put(_tmpKey_3, new ArrayList<PersonEntity>());
              }
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionTrackerValues.containsKey(_tmpKey_4)) {
                _collectionTrackerValues.put(_tmpKey_4, new ArrayList<TrackerValueEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomDairumDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipmediaAscomDairumDataLocalEntityMediaEntity(_collectionMedia);
            __fetchRelationshiptagsAscomDairumDataLocalEntityTagEntity(_collectionTags);
            __fetchRelationshippeopleAscomDairumDataLocalEntityPersonEntity(_collectionPeople);
            __fetchRelationshiptrackerValuesAscomDairumDataLocalEntityTrackerValueEntity(_collectionTrackerValues);
            final EntryWithDetails _result;
            if (_cursor.moveToFirst()) {
              final JournalEntryEntity _tmpEntry;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              if (_cursor.isNull(_cursorIndexOfTitle)) {
                _tmpTitle = null;
              } else {
                _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              }
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpPlainContent;
              _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              final Integer _tmpMoodLevel;
              if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
                _tmpMoodLevel = null;
              } else {
                _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
              }
              final String _tmpMoodIcon;
              if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
                _tmpMoodIcon = null;
              } else {
                _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
              }
              final String _tmpWeatherData;
              if (_cursor.isNull(_cursorIndexOfWeatherData)) {
                _tmpWeatherData = null;
              } else {
                _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
              }
              final String _tmpLocationData;
              if (_cursor.isNull(_cursorIndexOfLocationData)) {
                _tmpLocationData = null;
              } else {
                _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
              }
              final boolean _tmpIsFavorite;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
              _tmpIsFavorite = _tmp != 0;
              final boolean _tmpIsArchived;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
              _tmpIsArchived = _tmp_1 != 0;
              final boolean _tmpIsDraft;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
              _tmpIsDraft = _tmp_2 != 0;
              final boolean _tmpIsPinned;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp_3 != 0;
              final int _tmpWordCount;
              _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
              _tmpEntry = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_5 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_5);
              } else {
                _tmpCategory = null;
              }
              final ArrayList<MediaEntity> _tmpMediaCollection;
              final long _tmpKey_6;
              _tmpKey_6 = _cursor.getLong(_cursorIndexOfId);
              _tmpMediaCollection = _collectionMedia.get(_tmpKey_6);
              final ArrayList<TagEntity> _tmpTagsCollection;
              final long _tmpKey_7;
              _tmpKey_7 = _cursor.getLong(_cursorIndexOfId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_7);
              final ArrayList<PersonEntity> _tmpPeopleCollection;
              final long _tmpKey_8;
              _tmpKey_8 = _cursor.getLong(_cursorIndexOfId);
              _tmpPeopleCollection = _collectionPeople.get(_tmpKey_8);
              final ArrayList<TrackerValueEntity> _tmpTrackerValuesCollection;
              final long _tmpKey_9;
              _tmpKey_9 = _cursor.getLong(_cursorIndexOfId);
              _tmpTrackerValuesCollection = _collectionTrackerValues.get(_tmpKey_9);
              _result = new EntryWithDetails(_tmpEntry,_tmpCategory,_tmpMediaCollection,_tmpTagsCollection,_tmpPeopleCollection,_tmpTrackerValuesCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<EntryWithDetails> getWithDetailsFlow(final long id) {
    final String _sql = "SELECT * FROM journal_entries WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "media", "entry_tags",
        "tags", "entry_people", "people", "tracker_values",
        "journal_entries"}, new Callable<EntryWithDetails>() {
      @Override
      @Nullable
      public EntryWithDetails call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
            final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
            final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
            final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
            final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
            final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
            final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<ArrayList<MediaEntity>> _collectionMedia = new LongSparseArray<ArrayList<MediaEntity>>();
            final LongSparseArray<ArrayList<TagEntity>> _collectionTags = new LongSparseArray<ArrayList<TagEntity>>();
            final LongSparseArray<ArrayList<PersonEntity>> _collectionPeople = new LongSparseArray<ArrayList<PersonEntity>>();
            final LongSparseArray<ArrayList<TrackerValueEntity>> _collectionTrackerValues = new LongSparseArray<ArrayList<TrackerValueEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionMedia.containsKey(_tmpKey_1)) {
                _collectionMedia.put(_tmpKey_1, new ArrayList<MediaEntity>());
              }
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionTags.containsKey(_tmpKey_2)) {
                _collectionTags.put(_tmpKey_2, new ArrayList<TagEntity>());
              }
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionPeople.containsKey(_tmpKey_3)) {
                _collectionPeople.put(_tmpKey_3, new ArrayList<PersonEntity>());
              }
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionTrackerValues.containsKey(_tmpKey_4)) {
                _collectionTrackerValues.put(_tmpKey_4, new ArrayList<TrackerValueEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomDairumDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipmediaAscomDairumDataLocalEntityMediaEntity(_collectionMedia);
            __fetchRelationshiptagsAscomDairumDataLocalEntityTagEntity(_collectionTags);
            __fetchRelationshippeopleAscomDairumDataLocalEntityPersonEntity(_collectionPeople);
            __fetchRelationshiptrackerValuesAscomDairumDataLocalEntityTrackerValueEntity(_collectionTrackerValues);
            final EntryWithDetails _result;
            if (_cursor.moveToFirst()) {
              final JournalEntryEntity _tmpEntry;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              if (_cursor.isNull(_cursorIndexOfTitle)) {
                _tmpTitle = null;
              } else {
                _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              }
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpPlainContent;
              _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              final Integer _tmpMoodLevel;
              if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
                _tmpMoodLevel = null;
              } else {
                _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
              }
              final String _tmpMoodIcon;
              if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
                _tmpMoodIcon = null;
              } else {
                _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
              }
              final String _tmpWeatherData;
              if (_cursor.isNull(_cursorIndexOfWeatherData)) {
                _tmpWeatherData = null;
              } else {
                _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
              }
              final String _tmpLocationData;
              if (_cursor.isNull(_cursorIndexOfLocationData)) {
                _tmpLocationData = null;
              } else {
                _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
              }
              final boolean _tmpIsFavorite;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
              _tmpIsFavorite = _tmp != 0;
              final boolean _tmpIsArchived;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
              _tmpIsArchived = _tmp_1 != 0;
              final boolean _tmpIsDraft;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
              _tmpIsDraft = _tmp_2 != 0;
              final boolean _tmpIsPinned;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp_3 != 0;
              final int _tmpWordCount;
              _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
              _tmpEntry = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_5 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_5);
              } else {
                _tmpCategory = null;
              }
              final ArrayList<MediaEntity> _tmpMediaCollection;
              final long _tmpKey_6;
              _tmpKey_6 = _cursor.getLong(_cursorIndexOfId);
              _tmpMediaCollection = _collectionMedia.get(_tmpKey_6);
              final ArrayList<TagEntity> _tmpTagsCollection;
              final long _tmpKey_7;
              _tmpKey_7 = _cursor.getLong(_cursorIndexOfId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_7);
              final ArrayList<PersonEntity> _tmpPeopleCollection;
              final long _tmpKey_8;
              _tmpKey_8 = _cursor.getLong(_cursorIndexOfId);
              _tmpPeopleCollection = _collectionPeople.get(_tmpKey_8);
              final ArrayList<TrackerValueEntity> _tmpTrackerValuesCollection;
              final long _tmpKey_9;
              _tmpKey_9 = _cursor.getLong(_cursorIndexOfId);
              _tmpTrackerValuesCollection = _collectionTrackerValues.get(_tmpKey_9);
              _result = new EntryWithDetails(_tmpEntry,_tmpCategory,_tmpMediaCollection,_tmpTagsCollection,_tmpPeopleCollection,_tmpTrackerValuesCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<JournalEntryEntity>> getAllEntriesFlow() {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<EntryWithDetails>> getAllEntriesWithDetailsFlow() {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "media", "entry_tags",
        "tags", "entry_people", "people", "tracker_values",
        "journal_entries"}, new Callable<List<EntryWithDetails>>() {
      @Override
      @NonNull
      public List<EntryWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
            final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
            final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
            final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
            final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
            final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
            final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<ArrayList<MediaEntity>> _collectionMedia = new LongSparseArray<ArrayList<MediaEntity>>();
            final LongSparseArray<ArrayList<TagEntity>> _collectionTags = new LongSparseArray<ArrayList<TagEntity>>();
            final LongSparseArray<ArrayList<PersonEntity>> _collectionPeople = new LongSparseArray<ArrayList<PersonEntity>>();
            final LongSparseArray<ArrayList<TrackerValueEntity>> _collectionTrackerValues = new LongSparseArray<ArrayList<TrackerValueEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionMedia.containsKey(_tmpKey_1)) {
                _collectionMedia.put(_tmpKey_1, new ArrayList<MediaEntity>());
              }
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionTags.containsKey(_tmpKey_2)) {
                _collectionTags.put(_tmpKey_2, new ArrayList<TagEntity>());
              }
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionPeople.containsKey(_tmpKey_3)) {
                _collectionPeople.put(_tmpKey_3, new ArrayList<PersonEntity>());
              }
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionTrackerValues.containsKey(_tmpKey_4)) {
                _collectionTrackerValues.put(_tmpKey_4, new ArrayList<TrackerValueEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomDairumDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipmediaAscomDairumDataLocalEntityMediaEntity(_collectionMedia);
            __fetchRelationshiptagsAscomDairumDataLocalEntityTagEntity(_collectionTags);
            __fetchRelationshippeopleAscomDairumDataLocalEntityPersonEntity(_collectionPeople);
            __fetchRelationshiptrackerValuesAscomDairumDataLocalEntityTrackerValueEntity(_collectionTrackerValues);
            final List<EntryWithDetails> _result = new ArrayList<EntryWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final EntryWithDetails _item;
              final JournalEntryEntity _tmpEntry;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              if (_cursor.isNull(_cursorIndexOfTitle)) {
                _tmpTitle = null;
              } else {
                _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              }
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpPlainContent;
              _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              final Integer _tmpMoodLevel;
              if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
                _tmpMoodLevel = null;
              } else {
                _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
              }
              final String _tmpMoodIcon;
              if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
                _tmpMoodIcon = null;
              } else {
                _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
              }
              final String _tmpWeatherData;
              if (_cursor.isNull(_cursorIndexOfWeatherData)) {
                _tmpWeatherData = null;
              } else {
                _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
              }
              final String _tmpLocationData;
              if (_cursor.isNull(_cursorIndexOfLocationData)) {
                _tmpLocationData = null;
              } else {
                _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
              }
              final boolean _tmpIsFavorite;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
              _tmpIsFavorite = _tmp != 0;
              final boolean _tmpIsArchived;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
              _tmpIsArchived = _tmp_1 != 0;
              final boolean _tmpIsDraft;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
              _tmpIsDraft = _tmp_2 != 0;
              final boolean _tmpIsPinned;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp_3 != 0;
              final int _tmpWordCount;
              _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
              _tmpEntry = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_5 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_5);
              } else {
                _tmpCategory = null;
              }
              final ArrayList<MediaEntity> _tmpMediaCollection;
              final long _tmpKey_6;
              _tmpKey_6 = _cursor.getLong(_cursorIndexOfId);
              _tmpMediaCollection = _collectionMedia.get(_tmpKey_6);
              final ArrayList<TagEntity> _tmpTagsCollection;
              final long _tmpKey_7;
              _tmpKey_7 = _cursor.getLong(_cursorIndexOfId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_7);
              final ArrayList<PersonEntity> _tmpPeopleCollection;
              final long _tmpKey_8;
              _tmpKey_8 = _cursor.getLong(_cursorIndexOfId);
              _tmpPeopleCollection = _collectionPeople.get(_tmpKey_8);
              final ArrayList<TrackerValueEntity> _tmpTrackerValuesCollection;
              final long _tmpKey_9;
              _tmpKey_9 = _cursor.getLong(_cursorIndexOfId);
              _tmpTrackerValuesCollection = _collectionTrackerValues.get(_tmpKey_9);
              _item = new EntryWithDetails(_tmpEntry,_tmpCategory,_tmpMediaCollection,_tmpTagsCollection,_tmpPeopleCollection,_tmpTrackerValuesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getEntriesPaginated(final int limit, final int offset,
      final Continuation<? super List<JournalEntryEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "        LIMIT ? OFFSET ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 2;
    _statement.bindLong(_argIndex, offset);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<JournalEntryEntity>> getEntriesByCategoryFlow(final long categoryId) {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE categoryId = ? AND isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<JournalEntryEntity>> getEntriesByMoodFlow(final int moodLevel) {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE moodLevel = ? AND isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, moodLevel);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<JournalEntryEntity>> getFavoriteEntriesFlow() {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE isFavorite = 1 AND isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<JournalEntryEntity>> getDraftsFlow() {
    final String _sql = "SELECT * FROM journal_entries WHERE isDraft = 1 ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<JournalEntryEntity>> getArchivedEntriesFlow() {
    final String _sql = "SELECT * FROM journal_entries WHERE isArchived = 1 ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<JournalEntryEntity>> searchEntries(final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE (title LIKE '%' || ? || '%' OR plainContent LIKE '%' || ? || '%')\n"
            + "        AND isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<JournalEntryEntity>> getEntriesInDateRange(final long startTime,
      final long endTime) {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE createdAt >= ? AND createdAt < ?\n"
            + "        AND isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getEntriesInDateRangeSync(final long startTime, final long endTime,
      final Continuation<? super List<JournalEntryEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE createdAt >= ? AND createdAt < ?\n"
            + "        AND isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<JournalEntryEntity>> getEntriesOnThisDay(final String monthDay) {
    final String _sql = "\n"
            + "        SELECT * FROM journal_entries \n"
            + "        WHERE strftime('%m-%d', createdAt/1000, 'unixepoch', 'localtime') = ?\n"
            + "        AND isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, monthDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM journal_entries WHERE isArchived = 0 AND isDraft = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getEntryCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM journal_entries";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCountSince(final long startTime,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM journal_entries \n"
            + "        WHERE createdAt >= ? AND isArchived = 0 AND isDraft = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAverageWordCount(final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT AVG(wordCount) FROM journal_entries \n"
            + "        WHERE isArchived = 0 AND isDraft = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalWordCount(final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT SUM(wordCount) FROM journal_entries \n"
            + "        WHERE isArchived = 0 AND isDraft = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMoodDistribution(final Continuation<? super List<MoodCount>> $completion) {
    final String _sql = "\n"
            + "        SELECT moodLevel, COUNT(*) as count \n"
            + "        FROM journal_entries \n"
            + "        WHERE moodLevel IS NOT NULL AND isArchived = 0 AND isDraft = 0\n"
            + "        GROUP BY moodLevel\n"
            + "        ORDER BY moodLevel\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MoodCount>>() {
      @Override
      @NonNull
      public List<MoodCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMoodLevel = 0;
          final int _cursorIndexOfCount = 1;
          final List<MoodCount> _result = new ArrayList<MoodCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MoodCount _item;
            final int _tmpMoodLevel;
            _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new MoodCount(_tmpMoodLevel,_tmpCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDailyCounts(final long startTime,
      final Continuation<? super List<DateCount>> $completion) {
    final String _sql = "\n"
            + "        SELECT date(createdAt/1000, 'unixepoch', 'localtime') as date, COUNT(*) as count\n"
            + "        FROM journal_entries\n"
            + "        WHERE createdAt >= ? AND isArchived = 0 AND isDraft = 0\n"
            + "        GROUP BY date\n"
            + "        ORDER BY date\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DateCount>>() {
      @Override
      @NonNull
      public List<DateCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = 0;
          final int _cursorIndexOfCount = 1;
          final List<DateCount> _result = new ArrayList<DateCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DateCount _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new DateCount(_tmpDate,_tmpCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getHourlyDistribution(final Continuation<? super List<HourCount>> $completion) {
    final String _sql = "\n"
            + "        SELECT CAST(strftime('%H', createdAt/1000, 'unixepoch', 'localtime') AS INTEGER) as hour, \n"
            + "               COUNT(*) as count\n"
            + "        FROM journal_entries\n"
            + "        WHERE isArchived = 0 AND isDraft = 0\n"
            + "        GROUP BY hour\n"
            + "        ORDER BY hour\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HourCount>>() {
      @Override
      @NonNull
      public List<HourCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHour = 0;
          final int _cursorIndexOfCount = 1;
          final List<HourCount> _result = new ArrayList<HourCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HourCount _item;
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new HourCount(_tmpHour,_tmpCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getWeekdayDistribution(final Continuation<? super List<WeekdayCount>> $completion) {
    final String _sql = "\n"
            + "        SELECT CAST(strftime('%w', createdAt/1000, 'unixepoch', 'localtime') AS INTEGER) as dayOfWeek,\n"
            + "               COUNT(*) as count\n"
            + "        FROM journal_entries\n"
            + "        WHERE isArchived = 0 AND isDraft = 0\n"
            + "        GROUP BY dayOfWeek\n"
            + "        ORDER BY dayOfWeek\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WeekdayCount>>() {
      @Override
      @NonNull
      public List<WeekdayCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDayOfWeek = 0;
          final int _cursorIndexOfCount = 1;
          final List<WeekdayCount> _result = new ArrayList<WeekdayCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WeekdayCount _item;
            final int _tmpDayOfWeek;
            _tmpDayOfWeek = _cursor.getInt(_cursorIndexOfDayOfWeek);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new WeekdayCount(_tmpDayOfWeek,_tmpCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDistinctEntryDates(final Continuation<? super List<String>> $completion) {
    final String _sql = "\n"
            + "        SELECT DISTINCT date(createdAt/1000, 'unixepoch', 'localtime') as date\n"
            + "        FROM journal_entries\n"
            + "        WHERE isArchived = 0 AND isDraft = 0\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            final String _tmp;
            _tmp = _cursor.getString(0);
            _item = _tmp;
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getEntryCountBetween(final long startTime, final long endTime,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM journal_entries \n"
            + "        WHERE createdAt >= ? AND createdAt < ?\n"
            + "        AND isArchived = 0 AND isDraft = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipcategoriesAscomDairumDataLocalEntityCategoryEntity(
      @NonNull final LongSparseArray<CategoryEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipcategoriesAscomDairumDataLocalEntityCategoryEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`name`,`color`,`icon`,`sortOrder`,`isDefault`,`createdAt` FROM `categories` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfColor = 2;
      final int _cursorIndexOfIcon = 3;
      final int _cursorIndexOfSortOrder = 4;
      final int _cursorIndexOfIsDefault = 5;
      final int _cursorIndexOfCreatedAt = 6;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final CategoryEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final int _tmpColor;
          _tmpColor = _cursor.getInt(_cursorIndexOfColor);
          final String _tmpIcon;
          _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
          final int _tmpSortOrder;
          _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
          final boolean _tmpIsDefault;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
          _tmpIsDefault = _tmp != 0;
          final long _tmpCreatedAt;
          _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
          _item_1 = new CategoryEntity(_tmpId,_tmpName,_tmpColor,_tmpIcon,_tmpSortOrder,_tmpIsDefault,_tmpCreatedAt);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipmediaAscomDairumDataLocalEntityMediaEntity(
      @NonNull final LongSparseArray<ArrayList<MediaEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipmediaAscomDairumDataLocalEntityMediaEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`entryId`,`type`,`uri`,`thumbnailUri`,`fileName`,`mimeType`,`sizeBytes`,`duration`,`width`,`height`,`sortOrder`,`caption`,`createdAt` FROM `media` WHERE `entryId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "entryId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfEntryId = 1;
      final int _cursorIndexOfType = 2;
      final int _cursorIndexOfUri = 3;
      final int _cursorIndexOfThumbnailUri = 4;
      final int _cursorIndexOfFileName = 5;
      final int _cursorIndexOfMimeType = 6;
      final int _cursorIndexOfSizeBytes = 7;
      final int _cursorIndexOfDuration = 8;
      final int _cursorIndexOfWidth = 9;
      final int _cursorIndexOfHeight = 10;
      final int _cursorIndexOfSortOrder = 11;
      final int _cursorIndexOfCaption = 12;
      final int _cursorIndexOfCreatedAt = 13;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<MediaEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final MediaEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpEntryId;
          _tmpEntryId = _cursor.getLong(_cursorIndexOfEntryId);
          final MediaType _tmpType;
          final String _tmp;
          _tmp = _cursor.getString(_cursorIndexOfType);
          _tmpType = __converters.toMediaType(_tmp);
          final String _tmpUri;
          _tmpUri = _cursor.getString(_cursorIndexOfUri);
          final String _tmpThumbnailUri;
          if (_cursor.isNull(_cursorIndexOfThumbnailUri)) {
            _tmpThumbnailUri = null;
          } else {
            _tmpThumbnailUri = _cursor.getString(_cursorIndexOfThumbnailUri);
          }
          final String _tmpFileName;
          _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
          final String _tmpMimeType;
          _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
          final long _tmpSizeBytes;
          _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
          final Long _tmpDuration;
          if (_cursor.isNull(_cursorIndexOfDuration)) {
            _tmpDuration = null;
          } else {
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
          }
          final Integer _tmpWidth;
          if (_cursor.isNull(_cursorIndexOfWidth)) {
            _tmpWidth = null;
          } else {
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
          }
          final Integer _tmpHeight;
          if (_cursor.isNull(_cursorIndexOfHeight)) {
            _tmpHeight = null;
          } else {
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
          }
          final int _tmpSortOrder;
          _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
          final String _tmpCaption;
          if (_cursor.isNull(_cursorIndexOfCaption)) {
            _tmpCaption = null;
          } else {
            _tmpCaption = _cursor.getString(_cursorIndexOfCaption);
          }
          final long _tmpCreatedAt;
          _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
          _item_1 = new MediaEntity(_tmpId,_tmpEntryId,_tmpType,_tmpUri,_tmpThumbnailUri,_tmpFileName,_tmpMimeType,_tmpSizeBytes,_tmpDuration,_tmpWidth,_tmpHeight,_tmpSortOrder,_tmpCaption,_tmpCreatedAt);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshiptagsAscomDairumDataLocalEntityTagEntity(
      @NonNull final LongSparseArray<ArrayList<TagEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshiptagsAscomDairumDataLocalEntityTagEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `tags`.`id` AS `id`,`tags`.`name` AS `name`,`tags`.`color` AS `color`,`tags`.`usageCount` AS `usageCount`,`tags`.`createdAt` AS `createdAt`,_junction.`entryId` FROM `entry_tags` AS _junction INNER JOIN `tags` ON (_junction.`tagId` = `tags`.`id`) WHERE _junction.`entryId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      // _junction.entryId;
      final int _itemKeyIndex = 5;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfColor = 2;
      final int _cursorIndexOfUsageCount = 3;
      final int _cursorIndexOfCreatedAt = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<TagEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final TagEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final Integer _tmpColor;
          if (_cursor.isNull(_cursorIndexOfColor)) {
            _tmpColor = null;
          } else {
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
          }
          final int _tmpUsageCount;
          _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
          final long _tmpCreatedAt;
          _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
          _item_1 = new TagEntity(_tmpId,_tmpName,_tmpColor,_tmpUsageCount,_tmpCreatedAt);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshippeopleAscomDairumDataLocalEntityPersonEntity(
      @NonNull final LongSparseArray<ArrayList<PersonEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshippeopleAscomDairumDataLocalEntityPersonEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `people`.`id` AS `id`,`people`.`name` AS `name`,`people`.`relationship` AS `relationship`,`people`.`photoUri` AS `photoUri`,`people`.`notes` AS `notes`,`people`.`email` AS `email`,`people`.`phone` AS `phone`,`people`.`birthday` AS `birthday`,`people`.`importantDates` AS `importantDates`,`people`.`isFavorite` AS `isFavorite`,`people`.`mentionCount` AS `mentionCount`,`people`.`createdAt` AS `createdAt`,_junction.`entryId` FROM `entry_people` AS _junction INNER JOIN `people` ON (_junction.`personId` = `people`.`id`) WHERE _junction.`entryId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      // _junction.entryId;
      final int _itemKeyIndex = 12;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfRelationship = 2;
      final int _cursorIndexOfPhotoUri = 3;
      final int _cursorIndexOfNotes = 4;
      final int _cursorIndexOfEmail = 5;
      final int _cursorIndexOfPhone = 6;
      final int _cursorIndexOfBirthday = 7;
      final int _cursorIndexOfImportantDates = 8;
      final int _cursorIndexOfIsFavorite = 9;
      final int _cursorIndexOfMentionCount = 10;
      final int _cursorIndexOfCreatedAt = 11;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<PersonEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final PersonEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final String _tmpRelationship;
          if (_cursor.isNull(_cursorIndexOfRelationship)) {
            _tmpRelationship = null;
          } else {
            _tmpRelationship = _cursor.getString(_cursorIndexOfRelationship);
          }
          final String _tmpPhotoUri;
          if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
            _tmpPhotoUri = null;
          } else {
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
          }
          final String _tmpNotes;
          if (_cursor.isNull(_cursorIndexOfNotes)) {
            _tmpNotes = null;
          } else {
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
          }
          final String _tmpEmail;
          if (_cursor.isNull(_cursorIndexOfEmail)) {
            _tmpEmail = null;
          } else {
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
          }
          final String _tmpPhone;
          if (_cursor.isNull(_cursorIndexOfPhone)) {
            _tmpPhone = null;
          } else {
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
          }
          final Long _tmpBirthday;
          if (_cursor.isNull(_cursorIndexOfBirthday)) {
            _tmpBirthday = null;
          } else {
            _tmpBirthday = _cursor.getLong(_cursorIndexOfBirthday);
          }
          final String _tmpImportantDates;
          if (_cursor.isNull(_cursorIndexOfImportantDates)) {
            _tmpImportantDates = null;
          } else {
            _tmpImportantDates = _cursor.getString(_cursorIndexOfImportantDates);
          }
          final boolean _tmpIsFavorite;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
          _tmpIsFavorite = _tmp != 0;
          final int _tmpMentionCount;
          _tmpMentionCount = _cursor.getInt(_cursorIndexOfMentionCount);
          final long _tmpCreatedAt;
          _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
          _item_1 = new PersonEntity(_tmpId,_tmpName,_tmpRelationship,_tmpPhotoUri,_tmpNotes,_tmpEmail,_tmpPhone,_tmpBirthday,_tmpImportantDates,_tmpIsFavorite,_tmpMentionCount,_tmpCreatedAt);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshiptrackerValuesAscomDairumDataLocalEntityTrackerValueEntity(
      @NonNull final LongSparseArray<ArrayList<TrackerValueEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshiptrackerValuesAscomDairumDataLocalEntityTrackerValueEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`entryId`,`trackerId`,`value`,`createdAt` FROM `tracker_values` WHERE `entryId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "entryId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfEntryId = 1;
      final int _cursorIndexOfTrackerId = 2;
      final int _cursorIndexOfValue = 3;
      final int _cursorIndexOfCreatedAt = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<TrackerValueEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final TrackerValueEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpEntryId;
          _tmpEntryId = _cursor.getLong(_cursorIndexOfEntryId);
          final long _tmpTrackerId;
          _tmpTrackerId = _cursor.getLong(_cursorIndexOfTrackerId);
          final String _tmpValue;
          _tmpValue = _cursor.getString(_cursorIndexOfValue);
          final long _tmpCreatedAt;
          _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
          _item_1 = new TrackerValueEntity(_tmpId,_tmpEntryId,_tmpTrackerId,_tmpValue,_tmpCreatedAt);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
