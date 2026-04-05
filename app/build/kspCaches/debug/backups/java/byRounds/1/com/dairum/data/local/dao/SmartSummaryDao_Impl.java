package com.dairum.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dairum.data.local.converter.Converters;
import com.dairum.data.local.entity.SmartSummaryEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
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
public final class SmartSummaryDao_Impl implements SmartSummaryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SmartSummaryEntity> __insertionAdapterOfSmartSummaryEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<SmartSummaryEntity> __deletionAdapterOfSmartSummaryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSummaryById;

  public SmartSummaryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSmartSummaryEntity = new EntityInsertionAdapter<SmartSummaryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `smart_summaries` (`id`,`summaryType`,`periodStart`,`periodEnd`,`title`,`summary`,`keyPointsJson`,`moodOverview`,`topTagsJson`,`topPeopleJson`,`wordCount`,`entryCount`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SmartSummaryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSummaryType());
        final Long _tmp = __converters.fromLocalDateTime(entity.getPeriodStart());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        final Long _tmp_1 = __converters.fromLocalDateTime(entity.getPeriodEnd());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_1);
        }
        statement.bindString(5, entity.getTitle());
        statement.bindString(6, entity.getSummary());
        statement.bindString(7, entity.getKeyPointsJson());
        if (entity.getMoodOverview() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMoodOverview());
        }
        statement.bindString(9, entity.getTopTagsJson());
        statement.bindString(10, entity.getTopPeopleJson());
        statement.bindLong(11, entity.getWordCount());
        statement.bindLong(12, entity.getEntryCount());
        final Long _tmp_2 = __converters.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_2);
        }
      }
    };
    this.__deletionAdapterOfSmartSummaryEntity = new EntityDeletionOrUpdateAdapter<SmartSummaryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `smart_summaries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SmartSummaryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSummaryById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM smart_summaries WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSummary(final SmartSummaryEntity summary,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSmartSummaryEntity.insertAndReturnId(summary);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSummary(final SmartSummaryEntity summary,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSmartSummaryEntity.handle(summary);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSummaryById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSummaryById.acquire();
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
          __preparedStmtOfDeleteSummaryById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SmartSummaryEntity>> getAllSummaries() {
    final String _sql = "SELECT * FROM smart_summaries ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"smart_summaries"}, new Callable<List<SmartSummaryEntity>>() {
      @Override
      @NonNull
      public List<SmartSummaryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSummaryType = CursorUtil.getColumnIndexOrThrow(_cursor, "summaryType");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfKeyPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyPointsJson");
          final int _cursorIndexOfMoodOverview = CursorUtil.getColumnIndexOrThrow(_cursor, "moodOverview");
          final int _cursorIndexOfTopTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topTagsJson");
          final int _cursorIndexOfTopPeopleJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topPeopleJson");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SmartSummaryEntity> _result = new ArrayList<SmartSummaryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmartSummaryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSummaryType;
            _tmpSummaryType = _cursor.getString(_cursorIndexOfSummaryType);
            final LocalDateTime _tmpPeriodStart;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfPeriodStart)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfPeriodStart);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodStart = _tmp_1;
            }
            final LocalDateTime _tmpPeriodEnd;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPeriodEnd)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfPeriodEnd);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodEnd = _tmp_3;
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final String _tmpKeyPointsJson;
            _tmpKeyPointsJson = _cursor.getString(_cursorIndexOfKeyPointsJson);
            final String _tmpMoodOverview;
            if (_cursor.isNull(_cursorIndexOfMoodOverview)) {
              _tmpMoodOverview = null;
            } else {
              _tmpMoodOverview = _cursor.getString(_cursorIndexOfMoodOverview);
            }
            final String _tmpTopTagsJson;
            _tmpTopTagsJson = _cursor.getString(_cursorIndexOfTopTagsJson);
            final String _tmpTopPeopleJson;
            _tmpTopPeopleJson = _cursor.getString(_cursorIndexOfTopPeopleJson);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_5;
            }
            _item = new SmartSummaryEntity(_tmpId,_tmpSummaryType,_tmpPeriodStart,_tmpPeriodEnd,_tmpTitle,_tmpSummary,_tmpKeyPointsJson,_tmpMoodOverview,_tmpTopTagsJson,_tmpTopPeopleJson,_tmpWordCount,_tmpEntryCount,_tmpCreatedAt);
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
  public Flow<List<SmartSummaryEntity>> getSummariesByType(final String summaryType) {
    final String _sql = "SELECT * FROM smart_summaries WHERE summaryType = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, summaryType);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"smart_summaries"}, new Callable<List<SmartSummaryEntity>>() {
      @Override
      @NonNull
      public List<SmartSummaryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSummaryType = CursorUtil.getColumnIndexOrThrow(_cursor, "summaryType");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfKeyPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyPointsJson");
          final int _cursorIndexOfMoodOverview = CursorUtil.getColumnIndexOrThrow(_cursor, "moodOverview");
          final int _cursorIndexOfTopTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topTagsJson");
          final int _cursorIndexOfTopPeopleJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topPeopleJson");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SmartSummaryEntity> _result = new ArrayList<SmartSummaryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmartSummaryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSummaryType;
            _tmpSummaryType = _cursor.getString(_cursorIndexOfSummaryType);
            final LocalDateTime _tmpPeriodStart;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfPeriodStart)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfPeriodStart);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodStart = _tmp_1;
            }
            final LocalDateTime _tmpPeriodEnd;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPeriodEnd)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfPeriodEnd);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodEnd = _tmp_3;
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final String _tmpKeyPointsJson;
            _tmpKeyPointsJson = _cursor.getString(_cursorIndexOfKeyPointsJson);
            final String _tmpMoodOverview;
            if (_cursor.isNull(_cursorIndexOfMoodOverview)) {
              _tmpMoodOverview = null;
            } else {
              _tmpMoodOverview = _cursor.getString(_cursorIndexOfMoodOverview);
            }
            final String _tmpTopTagsJson;
            _tmpTopTagsJson = _cursor.getString(_cursorIndexOfTopTagsJson);
            final String _tmpTopPeopleJson;
            _tmpTopPeopleJson = _cursor.getString(_cursorIndexOfTopPeopleJson);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_5;
            }
            _item = new SmartSummaryEntity(_tmpId,_tmpSummaryType,_tmpPeriodStart,_tmpPeriodEnd,_tmpTitle,_tmpSummary,_tmpKeyPointsJson,_tmpMoodOverview,_tmpTopTagsJson,_tmpTopPeopleJson,_tmpWordCount,_tmpEntryCount,_tmpCreatedAt);
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
  public Flow<List<SmartSummaryEntity>> getRecentSummaries(final int limit) {
    final String _sql = "SELECT * FROM smart_summaries ORDER BY createdAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"smart_summaries"}, new Callable<List<SmartSummaryEntity>>() {
      @Override
      @NonNull
      public List<SmartSummaryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSummaryType = CursorUtil.getColumnIndexOrThrow(_cursor, "summaryType");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfKeyPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyPointsJson");
          final int _cursorIndexOfMoodOverview = CursorUtil.getColumnIndexOrThrow(_cursor, "moodOverview");
          final int _cursorIndexOfTopTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topTagsJson");
          final int _cursorIndexOfTopPeopleJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topPeopleJson");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SmartSummaryEntity> _result = new ArrayList<SmartSummaryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmartSummaryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSummaryType;
            _tmpSummaryType = _cursor.getString(_cursorIndexOfSummaryType);
            final LocalDateTime _tmpPeriodStart;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfPeriodStart)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfPeriodStart);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodStart = _tmp_1;
            }
            final LocalDateTime _tmpPeriodEnd;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPeriodEnd)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfPeriodEnd);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodEnd = _tmp_3;
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final String _tmpKeyPointsJson;
            _tmpKeyPointsJson = _cursor.getString(_cursorIndexOfKeyPointsJson);
            final String _tmpMoodOverview;
            if (_cursor.isNull(_cursorIndexOfMoodOverview)) {
              _tmpMoodOverview = null;
            } else {
              _tmpMoodOverview = _cursor.getString(_cursorIndexOfMoodOverview);
            }
            final String _tmpTopTagsJson;
            _tmpTopTagsJson = _cursor.getString(_cursorIndexOfTopTagsJson);
            final String _tmpTopPeopleJson;
            _tmpTopPeopleJson = _cursor.getString(_cursorIndexOfTopPeopleJson);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_5;
            }
            _item = new SmartSummaryEntity(_tmpId,_tmpSummaryType,_tmpPeriodStart,_tmpPeriodEnd,_tmpTitle,_tmpSummary,_tmpKeyPointsJson,_tmpMoodOverview,_tmpTopTagsJson,_tmpTopPeopleJson,_tmpWordCount,_tmpEntryCount,_tmpCreatedAt);
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
  public Object getSummaryById(final long id,
      final Continuation<? super SmartSummaryEntity> $completion) {
    final String _sql = "SELECT * FROM smart_summaries WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SmartSummaryEntity>() {
      @Override
      @Nullable
      public SmartSummaryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSummaryType = CursorUtil.getColumnIndexOrThrow(_cursor, "summaryType");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfKeyPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyPointsJson");
          final int _cursorIndexOfMoodOverview = CursorUtil.getColumnIndexOrThrow(_cursor, "moodOverview");
          final int _cursorIndexOfTopTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topTagsJson");
          final int _cursorIndexOfTopPeopleJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topPeopleJson");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final SmartSummaryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSummaryType;
            _tmpSummaryType = _cursor.getString(_cursorIndexOfSummaryType);
            final LocalDateTime _tmpPeriodStart;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfPeriodStart)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfPeriodStart);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodStart = _tmp_1;
            }
            final LocalDateTime _tmpPeriodEnd;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPeriodEnd)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfPeriodEnd);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodEnd = _tmp_3;
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final String _tmpKeyPointsJson;
            _tmpKeyPointsJson = _cursor.getString(_cursorIndexOfKeyPointsJson);
            final String _tmpMoodOverview;
            if (_cursor.isNull(_cursorIndexOfMoodOverview)) {
              _tmpMoodOverview = null;
            } else {
              _tmpMoodOverview = _cursor.getString(_cursorIndexOfMoodOverview);
            }
            final String _tmpTopTagsJson;
            _tmpTopTagsJson = _cursor.getString(_cursorIndexOfTopTagsJson);
            final String _tmpTopPeopleJson;
            _tmpTopPeopleJson = _cursor.getString(_cursorIndexOfTopPeopleJson);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_5;
            }
            _result = new SmartSummaryEntity(_tmpId,_tmpSummaryType,_tmpPeriodStart,_tmpPeriodEnd,_tmpTitle,_tmpSummary,_tmpKeyPointsJson,_tmpMoodOverview,_tmpTopTagsJson,_tmpTopPeopleJson,_tmpWordCount,_tmpEntryCount,_tmpCreatedAt);
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
  public Object getSummaryForPeriod(final String type, final LocalDateTime periodStart,
      final Continuation<? super SmartSummaryEntity> $completion) {
    final String _sql = "SELECT * FROM smart_summaries WHERE summaryType = ? AND periodStart = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    _argIndex = 2;
    final Long _tmp = __converters.fromLocalDateTime(periodStart);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SmartSummaryEntity>() {
      @Override
      @Nullable
      public SmartSummaryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSummaryType = CursorUtil.getColumnIndexOrThrow(_cursor, "summaryType");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfKeyPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyPointsJson");
          final int _cursorIndexOfMoodOverview = CursorUtil.getColumnIndexOrThrow(_cursor, "moodOverview");
          final int _cursorIndexOfTopTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topTagsJson");
          final int _cursorIndexOfTopPeopleJson = CursorUtil.getColumnIndexOrThrow(_cursor, "topPeopleJson");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final SmartSummaryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSummaryType;
            _tmpSummaryType = _cursor.getString(_cursorIndexOfSummaryType);
            final LocalDateTime _tmpPeriodStart;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfPeriodStart)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfPeriodStart);
            }
            final LocalDateTime _tmp_2 = __converters.toLocalDateTime(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodStart = _tmp_2;
            }
            final LocalDateTime _tmpPeriodEnd;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfPeriodEnd)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfPeriodEnd);
            }
            final LocalDateTime _tmp_4 = __converters.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpPeriodEnd = _tmp_4;
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final String _tmpKeyPointsJson;
            _tmpKeyPointsJson = _cursor.getString(_cursorIndexOfKeyPointsJson);
            final String _tmpMoodOverview;
            if (_cursor.isNull(_cursorIndexOfMoodOverview)) {
              _tmpMoodOverview = null;
            } else {
              _tmpMoodOverview = _cursor.getString(_cursorIndexOfMoodOverview);
            }
            final String _tmpTopTagsJson;
            _tmpTopTagsJson = _cursor.getString(_cursorIndexOfTopTagsJson);
            final String _tmpTopPeopleJson;
            _tmpTopPeopleJson = _cursor.getString(_cursorIndexOfTopPeopleJson);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_6;
            }
            _result = new SmartSummaryEntity(_tmpId,_tmpSummaryType,_tmpPeriodStart,_tmpPeriodEnd,_tmpTitle,_tmpSummary,_tmpKeyPointsJson,_tmpMoodOverview,_tmpTopTagsJson,_tmpTopPeopleJson,_tmpWordCount,_tmpEntryCount,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
