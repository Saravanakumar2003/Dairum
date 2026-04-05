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
import com.dairum.data.local.entity.PatternResultEntity;
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
public final class PatternResultDao_Impl implements PatternResultDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PatternResultEntity> __insertionAdapterOfPatternResultEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<PatternResultEntity> __deletionAdapterOfPatternResultEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldPatterns;

  public PatternResultDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPatternResultEntity = new EntityInsertionAdapter<PatternResultEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pattern_results` (`id`,`patternType`,`description`,`confidence`,`evidenceJson`,`insightsJson`,`detectedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PatternResultEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPatternType());
        statement.bindString(3, entity.getDescription());
        statement.bindDouble(4, entity.getConfidence());
        statement.bindString(5, entity.getEvidenceJson());
        statement.bindString(6, entity.getInsightsJson());
        final Long _tmp = __converters.fromLocalDateTime(entity.getDetectedAt());
        if (_tmp == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp);
        }
      }
    };
    this.__deletionAdapterOfPatternResultEntity = new EntityDeletionOrUpdateAdapter<PatternResultEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `pattern_results` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PatternResultEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteOldPatterns = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pattern_results WHERE detectedAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertPattern(final PatternResultEntity pattern,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPatternResultEntity.insertAndReturnId(pattern);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePattern(final PatternResultEntity pattern,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPatternResultEntity.handle(pattern);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldPatterns(final LocalDateTime beforeDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldPatterns.acquire();
        int _argIndex = 1;
        final Long _tmp = __converters.fromLocalDateTime(beforeDate);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
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
          __preparedStmtOfDeleteOldPatterns.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PatternResultEntity>> getAllPatterns() {
    final String _sql = "SELECT * FROM pattern_results ORDER BY detectedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pattern_results"}, new Callable<List<PatternResultEntity>>() {
      @Override
      @NonNull
      public List<PatternResultEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatternType = CursorUtil.getColumnIndexOrThrow(_cursor, "patternType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfEvidenceJson = CursorUtil.getColumnIndexOrThrow(_cursor, "evidenceJson");
          final int _cursorIndexOfInsightsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "insightsJson");
          final int _cursorIndexOfDetectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedAt");
          final List<PatternResultEntity> _result = new ArrayList<PatternResultEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatternResultEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPatternType;
            _tmpPatternType = _cursor.getString(_cursorIndexOfPatternType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpEvidenceJson;
            _tmpEvidenceJson = _cursor.getString(_cursorIndexOfEvidenceJson);
            final String _tmpInsightsJson;
            _tmpInsightsJson = _cursor.getString(_cursorIndexOfInsightsJson);
            final LocalDateTime _tmpDetectedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDetectedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDetectedAt);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDetectedAt = _tmp_1;
            }
            _item = new PatternResultEntity(_tmpId,_tmpPatternType,_tmpDescription,_tmpConfidence,_tmpEvidenceJson,_tmpInsightsJson,_tmpDetectedAt);
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
  public Flow<List<PatternResultEntity>> getPatternsByType(final String patternType) {
    final String _sql = "SELECT * FROM pattern_results WHERE patternType = ? ORDER BY detectedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, patternType);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pattern_results"}, new Callable<List<PatternResultEntity>>() {
      @Override
      @NonNull
      public List<PatternResultEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatternType = CursorUtil.getColumnIndexOrThrow(_cursor, "patternType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfEvidenceJson = CursorUtil.getColumnIndexOrThrow(_cursor, "evidenceJson");
          final int _cursorIndexOfInsightsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "insightsJson");
          final int _cursorIndexOfDetectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedAt");
          final List<PatternResultEntity> _result = new ArrayList<PatternResultEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatternResultEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPatternType;
            _tmpPatternType = _cursor.getString(_cursorIndexOfPatternType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpEvidenceJson;
            _tmpEvidenceJson = _cursor.getString(_cursorIndexOfEvidenceJson);
            final String _tmpInsightsJson;
            _tmpInsightsJson = _cursor.getString(_cursorIndexOfInsightsJson);
            final LocalDateTime _tmpDetectedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDetectedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDetectedAt);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDetectedAt = _tmp_1;
            }
            _item = new PatternResultEntity(_tmpId,_tmpPatternType,_tmpDescription,_tmpConfidence,_tmpEvidenceJson,_tmpInsightsJson,_tmpDetectedAt);
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
  public Flow<List<PatternResultEntity>> getRecentPatterns(final int limit) {
    final String _sql = "SELECT * FROM pattern_results ORDER BY detectedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pattern_results"}, new Callable<List<PatternResultEntity>>() {
      @Override
      @NonNull
      public List<PatternResultEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatternType = CursorUtil.getColumnIndexOrThrow(_cursor, "patternType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfEvidenceJson = CursorUtil.getColumnIndexOrThrow(_cursor, "evidenceJson");
          final int _cursorIndexOfInsightsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "insightsJson");
          final int _cursorIndexOfDetectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedAt");
          final List<PatternResultEntity> _result = new ArrayList<PatternResultEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatternResultEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPatternType;
            _tmpPatternType = _cursor.getString(_cursorIndexOfPatternType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpEvidenceJson;
            _tmpEvidenceJson = _cursor.getString(_cursorIndexOfEvidenceJson);
            final String _tmpInsightsJson;
            _tmpInsightsJson = _cursor.getString(_cursorIndexOfInsightsJson);
            final LocalDateTime _tmpDetectedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDetectedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDetectedAt);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDetectedAt = _tmp_1;
            }
            _item = new PatternResultEntity(_tmpId,_tmpPatternType,_tmpDescription,_tmpConfidence,_tmpEvidenceJson,_tmpInsightsJson,_tmpDetectedAt);
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
  public Object getPatternById(final long id,
      final Continuation<? super PatternResultEntity> $completion) {
    final String _sql = "SELECT * FROM pattern_results WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PatternResultEntity>() {
      @Override
      @Nullable
      public PatternResultEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatternType = CursorUtil.getColumnIndexOrThrow(_cursor, "patternType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfEvidenceJson = CursorUtil.getColumnIndexOrThrow(_cursor, "evidenceJson");
          final int _cursorIndexOfInsightsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "insightsJson");
          final int _cursorIndexOfDetectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedAt");
          final PatternResultEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPatternType;
            _tmpPatternType = _cursor.getString(_cursorIndexOfPatternType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpEvidenceJson;
            _tmpEvidenceJson = _cursor.getString(_cursorIndexOfEvidenceJson);
            final String _tmpInsightsJson;
            _tmpInsightsJson = _cursor.getString(_cursorIndexOfInsightsJson);
            final LocalDateTime _tmpDetectedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDetectedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDetectedAt);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDetectedAt = _tmp_1;
            }
            _result = new PatternResultEntity(_tmpId,_tmpPatternType,_tmpDescription,_tmpConfidence,_tmpEvidenceJson,_tmpInsightsJson,_tmpDetectedAt);
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
