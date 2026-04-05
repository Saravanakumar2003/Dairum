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
import com.dairum.data.local.entity.WeeklyReflectionEntity;
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
public final class WeeklyReflectionDao_Impl implements WeeklyReflectionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WeeklyReflectionEntity> __insertionAdapterOfWeeklyReflectionEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<WeeklyReflectionEntity> __deletionAdapterOfWeeklyReflectionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteReflectionById;

  public WeeklyReflectionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWeeklyReflectionEntity = new EntityInsertionAdapter<WeeklyReflectionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `weekly_reflections` (`id`,`weekStartDate`,`weekEndDate`,`summary`,`averageMood`,`moodRangeMin`,`moodRangeMax`,`dominantMood`,`moodDistributionJson`,`moodTrend`,`keyThemesJson`,`highlightsJson`,`suggestionsJson`,`entryCount`,`totalWordCount`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WeeklyReflectionEntity entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.fromLocalDateTime(entity.getWeekStartDate());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        final Long _tmp_1 = __converters.fromLocalDateTime(entity.getWeekEndDate());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp_1);
        }
        statement.bindString(4, entity.getSummary());
        statement.bindDouble(5, entity.getAverageMood());
        statement.bindLong(6, entity.getMoodRangeMin());
        statement.bindLong(7, entity.getMoodRangeMax());
        statement.bindString(8, entity.getDominantMood());
        statement.bindString(9, entity.getMoodDistributionJson());
        statement.bindString(10, entity.getMoodTrend());
        statement.bindString(11, entity.getKeyThemesJson());
        statement.bindString(12, entity.getHighlightsJson());
        statement.bindString(13, entity.getSuggestionsJson());
        statement.bindLong(14, entity.getEntryCount());
        statement.bindLong(15, entity.getTotalWordCount());
        final Long _tmp_2 = __converters.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, _tmp_2);
        }
      }
    };
    this.__deletionAdapterOfWeeklyReflectionEntity = new EntityDeletionOrUpdateAdapter<WeeklyReflectionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `weekly_reflections` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WeeklyReflectionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteReflectionById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM weekly_reflections WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertReflection(final WeeklyReflectionEntity reflection,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWeeklyReflectionEntity.insertAndReturnId(reflection);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteReflection(final WeeklyReflectionEntity reflection,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfWeeklyReflectionEntity.handle(reflection);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteReflectionById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteReflectionById.acquire();
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
          __preparedStmtOfDeleteReflectionById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WeeklyReflectionEntity>> getAllReflections() {
    final String _sql = "SELECT * FROM weekly_reflections ORDER BY weekStartDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"weekly_reflections"}, new Callable<List<WeeklyReflectionEntity>>() {
      @Override
      @NonNull
      public List<WeeklyReflectionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekStartDate");
          final int _cursorIndexOfWeekEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekEndDate");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfAverageMood = CursorUtil.getColumnIndexOrThrow(_cursor, "averageMood");
          final int _cursorIndexOfMoodRangeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "moodRangeMin");
          final int _cursorIndexOfMoodRangeMax = CursorUtil.getColumnIndexOrThrow(_cursor, "moodRangeMax");
          final int _cursorIndexOfDominantMood = CursorUtil.getColumnIndexOrThrow(_cursor, "dominantMood");
          final int _cursorIndexOfMoodDistributionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "moodDistributionJson");
          final int _cursorIndexOfMoodTrend = CursorUtil.getColumnIndexOrThrow(_cursor, "moodTrend");
          final int _cursorIndexOfKeyThemesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyThemesJson");
          final int _cursorIndexOfHighlightsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "highlightsJson");
          final int _cursorIndexOfSuggestionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "suggestionsJson");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfTotalWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalWordCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<WeeklyReflectionEntity> _result = new ArrayList<WeeklyReflectionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WeeklyReflectionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDateTime _tmpWeekStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfWeekStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfWeekStartDate);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpWeekStartDate = _tmp_1;
            }
            final LocalDateTime _tmpWeekEndDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfWeekEndDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfWeekEndDate);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpWeekEndDate = _tmp_3;
            }
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final float _tmpAverageMood;
            _tmpAverageMood = _cursor.getFloat(_cursorIndexOfAverageMood);
            final int _tmpMoodRangeMin;
            _tmpMoodRangeMin = _cursor.getInt(_cursorIndexOfMoodRangeMin);
            final int _tmpMoodRangeMax;
            _tmpMoodRangeMax = _cursor.getInt(_cursorIndexOfMoodRangeMax);
            final String _tmpDominantMood;
            _tmpDominantMood = _cursor.getString(_cursorIndexOfDominantMood);
            final String _tmpMoodDistributionJson;
            _tmpMoodDistributionJson = _cursor.getString(_cursorIndexOfMoodDistributionJson);
            final String _tmpMoodTrend;
            _tmpMoodTrend = _cursor.getString(_cursorIndexOfMoodTrend);
            final String _tmpKeyThemesJson;
            _tmpKeyThemesJson = _cursor.getString(_cursorIndexOfKeyThemesJson);
            final String _tmpHighlightsJson;
            _tmpHighlightsJson = _cursor.getString(_cursorIndexOfHighlightsJson);
            final String _tmpSuggestionsJson;
            _tmpSuggestionsJson = _cursor.getString(_cursorIndexOfSuggestionsJson);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpTotalWordCount;
            _tmpTotalWordCount = _cursor.getInt(_cursorIndexOfTotalWordCount);
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
            _item = new WeeklyReflectionEntity(_tmpId,_tmpWeekStartDate,_tmpWeekEndDate,_tmpSummary,_tmpAverageMood,_tmpMoodRangeMin,_tmpMoodRangeMax,_tmpDominantMood,_tmpMoodDistributionJson,_tmpMoodTrend,_tmpKeyThemesJson,_tmpHighlightsJson,_tmpSuggestionsJson,_tmpEntryCount,_tmpTotalWordCount,_tmpCreatedAt);
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
  public Flow<List<WeeklyReflectionEntity>> getRecentReflections(final int limit) {
    final String _sql = "SELECT * FROM weekly_reflections ORDER BY weekStartDate DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"weekly_reflections"}, new Callable<List<WeeklyReflectionEntity>>() {
      @Override
      @NonNull
      public List<WeeklyReflectionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekStartDate");
          final int _cursorIndexOfWeekEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekEndDate");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfAverageMood = CursorUtil.getColumnIndexOrThrow(_cursor, "averageMood");
          final int _cursorIndexOfMoodRangeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "moodRangeMin");
          final int _cursorIndexOfMoodRangeMax = CursorUtil.getColumnIndexOrThrow(_cursor, "moodRangeMax");
          final int _cursorIndexOfDominantMood = CursorUtil.getColumnIndexOrThrow(_cursor, "dominantMood");
          final int _cursorIndexOfMoodDistributionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "moodDistributionJson");
          final int _cursorIndexOfMoodTrend = CursorUtil.getColumnIndexOrThrow(_cursor, "moodTrend");
          final int _cursorIndexOfKeyThemesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyThemesJson");
          final int _cursorIndexOfHighlightsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "highlightsJson");
          final int _cursorIndexOfSuggestionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "suggestionsJson");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfTotalWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalWordCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<WeeklyReflectionEntity> _result = new ArrayList<WeeklyReflectionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WeeklyReflectionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDateTime _tmpWeekStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfWeekStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfWeekStartDate);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpWeekStartDate = _tmp_1;
            }
            final LocalDateTime _tmpWeekEndDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfWeekEndDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfWeekEndDate);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpWeekEndDate = _tmp_3;
            }
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final float _tmpAverageMood;
            _tmpAverageMood = _cursor.getFloat(_cursorIndexOfAverageMood);
            final int _tmpMoodRangeMin;
            _tmpMoodRangeMin = _cursor.getInt(_cursorIndexOfMoodRangeMin);
            final int _tmpMoodRangeMax;
            _tmpMoodRangeMax = _cursor.getInt(_cursorIndexOfMoodRangeMax);
            final String _tmpDominantMood;
            _tmpDominantMood = _cursor.getString(_cursorIndexOfDominantMood);
            final String _tmpMoodDistributionJson;
            _tmpMoodDistributionJson = _cursor.getString(_cursorIndexOfMoodDistributionJson);
            final String _tmpMoodTrend;
            _tmpMoodTrend = _cursor.getString(_cursorIndexOfMoodTrend);
            final String _tmpKeyThemesJson;
            _tmpKeyThemesJson = _cursor.getString(_cursorIndexOfKeyThemesJson);
            final String _tmpHighlightsJson;
            _tmpHighlightsJson = _cursor.getString(_cursorIndexOfHighlightsJson);
            final String _tmpSuggestionsJson;
            _tmpSuggestionsJson = _cursor.getString(_cursorIndexOfSuggestionsJson);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpTotalWordCount;
            _tmpTotalWordCount = _cursor.getInt(_cursorIndexOfTotalWordCount);
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
            _item = new WeeklyReflectionEntity(_tmpId,_tmpWeekStartDate,_tmpWeekEndDate,_tmpSummary,_tmpAverageMood,_tmpMoodRangeMin,_tmpMoodRangeMax,_tmpDominantMood,_tmpMoodDistributionJson,_tmpMoodTrend,_tmpKeyThemesJson,_tmpHighlightsJson,_tmpSuggestionsJson,_tmpEntryCount,_tmpTotalWordCount,_tmpCreatedAt);
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
  public Object getReflectionById(final long id,
      final Continuation<? super WeeklyReflectionEntity> $completion) {
    final String _sql = "SELECT * FROM weekly_reflections WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WeeklyReflectionEntity>() {
      @Override
      @Nullable
      public WeeklyReflectionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekStartDate");
          final int _cursorIndexOfWeekEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekEndDate");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfAverageMood = CursorUtil.getColumnIndexOrThrow(_cursor, "averageMood");
          final int _cursorIndexOfMoodRangeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "moodRangeMin");
          final int _cursorIndexOfMoodRangeMax = CursorUtil.getColumnIndexOrThrow(_cursor, "moodRangeMax");
          final int _cursorIndexOfDominantMood = CursorUtil.getColumnIndexOrThrow(_cursor, "dominantMood");
          final int _cursorIndexOfMoodDistributionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "moodDistributionJson");
          final int _cursorIndexOfMoodTrend = CursorUtil.getColumnIndexOrThrow(_cursor, "moodTrend");
          final int _cursorIndexOfKeyThemesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyThemesJson");
          final int _cursorIndexOfHighlightsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "highlightsJson");
          final int _cursorIndexOfSuggestionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "suggestionsJson");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfTotalWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalWordCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final WeeklyReflectionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDateTime _tmpWeekStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfWeekStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfWeekStartDate);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpWeekStartDate = _tmp_1;
            }
            final LocalDateTime _tmpWeekEndDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfWeekEndDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfWeekEndDate);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpWeekEndDate = _tmp_3;
            }
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final float _tmpAverageMood;
            _tmpAverageMood = _cursor.getFloat(_cursorIndexOfAverageMood);
            final int _tmpMoodRangeMin;
            _tmpMoodRangeMin = _cursor.getInt(_cursorIndexOfMoodRangeMin);
            final int _tmpMoodRangeMax;
            _tmpMoodRangeMax = _cursor.getInt(_cursorIndexOfMoodRangeMax);
            final String _tmpDominantMood;
            _tmpDominantMood = _cursor.getString(_cursorIndexOfDominantMood);
            final String _tmpMoodDistributionJson;
            _tmpMoodDistributionJson = _cursor.getString(_cursorIndexOfMoodDistributionJson);
            final String _tmpMoodTrend;
            _tmpMoodTrend = _cursor.getString(_cursorIndexOfMoodTrend);
            final String _tmpKeyThemesJson;
            _tmpKeyThemesJson = _cursor.getString(_cursorIndexOfKeyThemesJson);
            final String _tmpHighlightsJson;
            _tmpHighlightsJson = _cursor.getString(_cursorIndexOfHighlightsJson);
            final String _tmpSuggestionsJson;
            _tmpSuggestionsJson = _cursor.getString(_cursorIndexOfSuggestionsJson);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpTotalWordCount;
            _tmpTotalWordCount = _cursor.getInt(_cursorIndexOfTotalWordCount);
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
            _result = new WeeklyReflectionEntity(_tmpId,_tmpWeekStartDate,_tmpWeekEndDate,_tmpSummary,_tmpAverageMood,_tmpMoodRangeMin,_tmpMoodRangeMax,_tmpDominantMood,_tmpMoodDistributionJson,_tmpMoodTrend,_tmpKeyThemesJson,_tmpHighlightsJson,_tmpSuggestionsJson,_tmpEntryCount,_tmpTotalWordCount,_tmpCreatedAt);
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
  public Object getReflectionForWeek(final LocalDateTime weekStart,
      final Continuation<? super WeeklyReflectionEntity> $completion) {
    final String _sql = "SELECT * FROM weekly_reflections WHERE weekStartDate = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final Long _tmp = __converters.fromLocalDateTime(weekStart);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WeeklyReflectionEntity>() {
      @Override
      @Nullable
      public WeeklyReflectionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekStartDate");
          final int _cursorIndexOfWeekEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekEndDate");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfAverageMood = CursorUtil.getColumnIndexOrThrow(_cursor, "averageMood");
          final int _cursorIndexOfMoodRangeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "moodRangeMin");
          final int _cursorIndexOfMoodRangeMax = CursorUtil.getColumnIndexOrThrow(_cursor, "moodRangeMax");
          final int _cursorIndexOfDominantMood = CursorUtil.getColumnIndexOrThrow(_cursor, "dominantMood");
          final int _cursorIndexOfMoodDistributionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "moodDistributionJson");
          final int _cursorIndexOfMoodTrend = CursorUtil.getColumnIndexOrThrow(_cursor, "moodTrend");
          final int _cursorIndexOfKeyThemesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyThemesJson");
          final int _cursorIndexOfHighlightsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "highlightsJson");
          final int _cursorIndexOfSuggestionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "suggestionsJson");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfTotalWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalWordCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final WeeklyReflectionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDateTime _tmpWeekStartDate;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfWeekStartDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfWeekStartDate);
            }
            final LocalDateTime _tmp_2 = __converters.toLocalDateTime(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpWeekStartDate = _tmp_2;
            }
            final LocalDateTime _tmpWeekEndDate;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfWeekEndDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfWeekEndDate);
            }
            final LocalDateTime _tmp_4 = __converters.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpWeekEndDate = _tmp_4;
            }
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            final float _tmpAverageMood;
            _tmpAverageMood = _cursor.getFloat(_cursorIndexOfAverageMood);
            final int _tmpMoodRangeMin;
            _tmpMoodRangeMin = _cursor.getInt(_cursorIndexOfMoodRangeMin);
            final int _tmpMoodRangeMax;
            _tmpMoodRangeMax = _cursor.getInt(_cursorIndexOfMoodRangeMax);
            final String _tmpDominantMood;
            _tmpDominantMood = _cursor.getString(_cursorIndexOfDominantMood);
            final String _tmpMoodDistributionJson;
            _tmpMoodDistributionJson = _cursor.getString(_cursorIndexOfMoodDistributionJson);
            final String _tmpMoodTrend;
            _tmpMoodTrend = _cursor.getString(_cursorIndexOfMoodTrend);
            final String _tmpKeyThemesJson;
            _tmpKeyThemesJson = _cursor.getString(_cursorIndexOfKeyThemesJson);
            final String _tmpHighlightsJson;
            _tmpHighlightsJson = _cursor.getString(_cursorIndexOfHighlightsJson);
            final String _tmpSuggestionsJson;
            _tmpSuggestionsJson = _cursor.getString(_cursorIndexOfSuggestionsJson);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpTotalWordCount;
            _tmpTotalWordCount = _cursor.getInt(_cursorIndexOfTotalWordCount);
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
            _result = new WeeklyReflectionEntity(_tmpId,_tmpWeekStartDate,_tmpWeekEndDate,_tmpSummary,_tmpAverageMood,_tmpMoodRangeMin,_tmpMoodRangeMax,_tmpDominantMood,_tmpMoodDistributionJson,_tmpMoodTrend,_tmpKeyThemesJson,_tmpHighlightsJson,_tmpSuggestionsJson,_tmpEntryCount,_tmpTotalWordCount,_tmpCreatedAt);
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
