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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dairum.data.local.converter.Converters;
import com.dairum.data.local.entity.ReflectionMemoryEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class ReflectionMemoryDao_Impl implements ReflectionMemoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReflectionMemoryEntity> __insertionAdapterOfReflectionMemoryEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ReflectionMemoryEntity> __deletionAdapterOfReflectionMemoryEntity;

  private final EntityDeletionOrUpdateAdapter<ReflectionMemoryEntity> __updateAdapterOfReflectionMemoryEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastReferenced;

  private final SharedSQLiteStatement __preparedStmtOfIncrementOccurrenceCount;

  private final SharedSQLiteStatement __preparedStmtOfUpdateActiveStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateConfidence;

  private final SharedSQLiteStatement __preparedStmtOfUpdateEmbedding;

  private final SharedSQLiteStatement __preparedStmtOfDeleteReflectionById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteWeakReflections;

  public ReflectionMemoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReflectionMemoryEntity = new EntityInsertionAdapter<ReflectionMemoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reflection_memories` (`id`,`content`,`category`,`confidence`,`sourceMessageIds`,`sourceSessionIds`,`occurrenceCount`,`lastReferenced`,`isActive`,`embedding`,`embeddingModel`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReflectionMemoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getContent());
        statement.bindString(3, entity.getCategory());
        statement.bindDouble(4, entity.getConfidence());
        if (entity.getSourceMessageIds() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSourceMessageIds());
        }
        if (entity.getSourceSessionIds() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSourceSessionIds());
        }
        statement.bindLong(7, entity.getOccurrenceCount());
        final Long _tmp = __converters.fromLocalDateTime(entity.getLastReferenced());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp);
        }
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        if (entity.getEmbedding() == null) {
          statement.bindNull(10);
        } else {
          statement.bindBlob(10, entity.getEmbedding());
        }
        if (entity.getEmbeddingModel() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getEmbeddingModel());
        }
        final Long _tmp_2 = __converters.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, _tmp_2);
        }
        final Long _tmp_3 = __converters.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_3);
        }
      }
    };
    this.__deletionAdapterOfReflectionMemoryEntity = new EntityDeletionOrUpdateAdapter<ReflectionMemoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `reflection_memories` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReflectionMemoryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfReflectionMemoryEntity = new EntityDeletionOrUpdateAdapter<ReflectionMemoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reflection_memories` SET `id` = ?,`content` = ?,`category` = ?,`confidence` = ?,`sourceMessageIds` = ?,`sourceSessionIds` = ?,`occurrenceCount` = ?,`lastReferenced` = ?,`isActive` = ?,`embedding` = ?,`embeddingModel` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReflectionMemoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getContent());
        statement.bindString(3, entity.getCategory());
        statement.bindDouble(4, entity.getConfidence());
        if (entity.getSourceMessageIds() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSourceMessageIds());
        }
        if (entity.getSourceSessionIds() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSourceSessionIds());
        }
        statement.bindLong(7, entity.getOccurrenceCount());
        final Long _tmp = __converters.fromLocalDateTime(entity.getLastReferenced());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp);
        }
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        if (entity.getEmbedding() == null) {
          statement.bindNull(10);
        } else {
          statement.bindBlob(10, entity.getEmbedding());
        }
        if (entity.getEmbeddingModel() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getEmbeddingModel());
        }
        final Long _tmp_2 = __converters.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, _tmp_2);
        }
        final Long _tmp_3 = __converters.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_3);
        }
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateLastReferenced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reflection_memories SET lastReferenced = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementOccurrenceCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reflection_memories SET occurrenceCount = occurrenceCount + 1, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateActiveStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reflection_memories SET isActive = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateConfidence = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reflection_memories SET confidence = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateEmbedding = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE reflection_memories \n"
                + "        SET embedding = ?, embeddingModel = ?, updatedAt = ? \n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteReflectionById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reflection_memories WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteWeakReflections = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reflection_memories WHERE confidence < ? AND occurrenceCount < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertReflection(final ReflectionMemoryEntity reflection,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfReflectionMemoryEntity.insertAndReturnId(reflection);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertReflections(final List<ReflectionMemoryEntity> reflections,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfReflectionMemoryEntity.insert(reflections);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteReflection(final ReflectionMemoryEntity reflection,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfReflectionMemoryEntity.handle(reflection);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReflection(final ReflectionMemoryEntity reflection,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfReflectionMemoryEntity.handle(reflection);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastReferenced(final long id, final LocalDateTime lastReferenced,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastReferenced.acquire();
        int _argIndex = 1;
        final Long _tmp = __converters.fromLocalDateTime(lastReferenced);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
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
          __preparedStmtOfUpdateLastReferenced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementOccurrenceCount(final long id, final LocalDateTime updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementOccurrenceCount.acquire();
        int _argIndex = 1;
        final Long _tmp = __converters.fromLocalDateTime(updatedAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
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
          __preparedStmtOfIncrementOccurrenceCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateActiveStatus(final long id, final boolean isActive,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateActiveStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isActive ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        final Long _tmp_1 = __converters.fromLocalDateTime(updatedAt);
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp_1);
        }
        _argIndex = 3;
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
          __preparedStmtOfUpdateActiveStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateConfidence(final long id, final float confidence,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateConfidence.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, confidence);
        _argIndex = 2;
        final Long _tmp = __converters.fromLocalDateTime(updatedAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 3;
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
          __preparedStmtOfUpdateConfidence.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEmbedding(final long id, final byte[] embedding, final String model,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateEmbedding.acquire();
        int _argIndex = 1;
        _stmt.bindBlob(_argIndex, embedding);
        _argIndex = 2;
        _stmt.bindString(_argIndex, model);
        _argIndex = 3;
        final Long _tmp = __converters.fromLocalDateTime(updatedAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 4;
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
          __preparedStmtOfUpdateEmbedding.release(_stmt);
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
  public Object deleteWeakReflections(final float minConfidence, final int minOccurrences,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteWeakReflections.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, minConfidence);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, minOccurrences);
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
          __preparedStmtOfDeleteWeakReflections.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReflectionMemoryEntity>> getActiveReflections() {
    final String _sql = "SELECT * FROM reflection_memories WHERE isActive = 1 ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reflection_memories"}, new Callable<List<ReflectionMemoryEntity>>() {
      @Override
      @NonNull
      public List<ReflectionMemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfSourceMessageIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageIds");
          final int _cursorIndexOfSourceSessionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceSessionIds");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfLastReferenced = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReferenced");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfEmbedding = CursorUtil.getColumnIndexOrThrow(_cursor, "embedding");
          final int _cursorIndexOfEmbeddingModel = CursorUtil.getColumnIndexOrThrow(_cursor, "embeddingModel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReflectionMemoryEntity> _result = new ArrayList<ReflectionMemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReflectionMemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpSourceMessageIds;
            if (_cursor.isNull(_cursorIndexOfSourceMessageIds)) {
              _tmpSourceMessageIds = null;
            } else {
              _tmpSourceMessageIds = _cursor.getString(_cursorIndexOfSourceMessageIds);
            }
            final String _tmpSourceSessionIds;
            if (_cursor.isNull(_cursorIndexOfSourceSessionIds)) {
              _tmpSourceSessionIds = null;
            } else {
              _tmpSourceSessionIds = _cursor.getString(_cursorIndexOfSourceSessionIds);
            }
            final int _tmpOccurrenceCount;
            _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            final LocalDateTime _tmpLastReferenced;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastReferenced)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastReferenced);
            }
            _tmpLastReferenced = __converters.toLocalDateTime(_tmp);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final byte[] _tmpEmbedding;
            if (_cursor.isNull(_cursorIndexOfEmbedding)) {
              _tmpEmbedding = null;
            } else {
              _tmpEmbedding = _cursor.getBlob(_cursorIndexOfEmbedding);
            }
            final String _tmpEmbeddingModel;
            if (_cursor.isNull(_cursorIndexOfEmbeddingModel)) {
              _tmpEmbeddingModel = null;
            } else {
              _tmpEmbeddingModel = _cursor.getString(_cursorIndexOfEmbeddingModel);
            }
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_3;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_5;
            }
            _item = new ReflectionMemoryEntity(_tmpId,_tmpContent,_tmpCategory,_tmpConfidence,_tmpSourceMessageIds,_tmpSourceSessionIds,_tmpOccurrenceCount,_tmpLastReferenced,_tmpIsActive,_tmpEmbedding,_tmpEmbeddingModel,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getActiveReflectionsList(
      final Continuation<? super List<ReflectionMemoryEntity>> $completion) {
    final String _sql = "SELECT * FROM reflection_memories WHERE isActive = 1 ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReflectionMemoryEntity>>() {
      @Override
      @NonNull
      public List<ReflectionMemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfSourceMessageIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageIds");
          final int _cursorIndexOfSourceSessionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceSessionIds");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfLastReferenced = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReferenced");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfEmbedding = CursorUtil.getColumnIndexOrThrow(_cursor, "embedding");
          final int _cursorIndexOfEmbeddingModel = CursorUtil.getColumnIndexOrThrow(_cursor, "embeddingModel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReflectionMemoryEntity> _result = new ArrayList<ReflectionMemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReflectionMemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpSourceMessageIds;
            if (_cursor.isNull(_cursorIndexOfSourceMessageIds)) {
              _tmpSourceMessageIds = null;
            } else {
              _tmpSourceMessageIds = _cursor.getString(_cursorIndexOfSourceMessageIds);
            }
            final String _tmpSourceSessionIds;
            if (_cursor.isNull(_cursorIndexOfSourceSessionIds)) {
              _tmpSourceSessionIds = null;
            } else {
              _tmpSourceSessionIds = _cursor.getString(_cursorIndexOfSourceSessionIds);
            }
            final int _tmpOccurrenceCount;
            _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            final LocalDateTime _tmpLastReferenced;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastReferenced)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastReferenced);
            }
            _tmpLastReferenced = __converters.toLocalDateTime(_tmp);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final byte[] _tmpEmbedding;
            if (_cursor.isNull(_cursorIndexOfEmbedding)) {
              _tmpEmbedding = null;
            } else {
              _tmpEmbedding = _cursor.getBlob(_cursorIndexOfEmbedding);
            }
            final String _tmpEmbeddingModel;
            if (_cursor.isNull(_cursorIndexOfEmbeddingModel)) {
              _tmpEmbeddingModel = null;
            } else {
              _tmpEmbeddingModel = _cursor.getString(_cursorIndexOfEmbeddingModel);
            }
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_3;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_5;
            }
            _item = new ReflectionMemoryEntity(_tmpId,_tmpContent,_tmpCategory,_tmpConfidence,_tmpSourceMessageIds,_tmpSourceSessionIds,_tmpOccurrenceCount,_tmpLastReferenced,_tmpIsActive,_tmpEmbedding,_tmpEmbeddingModel,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getReflectionsWithEmbeddings(
      final Continuation<? super List<ReflectionMemoryEntity>> $completion) {
    final String _sql = "SELECT * FROM reflection_memories WHERE isActive = 1 AND embedding IS NOT NULL ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReflectionMemoryEntity>>() {
      @Override
      @NonNull
      public List<ReflectionMemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfSourceMessageIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageIds");
          final int _cursorIndexOfSourceSessionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceSessionIds");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfLastReferenced = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReferenced");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfEmbedding = CursorUtil.getColumnIndexOrThrow(_cursor, "embedding");
          final int _cursorIndexOfEmbeddingModel = CursorUtil.getColumnIndexOrThrow(_cursor, "embeddingModel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReflectionMemoryEntity> _result = new ArrayList<ReflectionMemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReflectionMemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpSourceMessageIds;
            if (_cursor.isNull(_cursorIndexOfSourceMessageIds)) {
              _tmpSourceMessageIds = null;
            } else {
              _tmpSourceMessageIds = _cursor.getString(_cursorIndexOfSourceMessageIds);
            }
            final String _tmpSourceSessionIds;
            if (_cursor.isNull(_cursorIndexOfSourceSessionIds)) {
              _tmpSourceSessionIds = null;
            } else {
              _tmpSourceSessionIds = _cursor.getString(_cursorIndexOfSourceSessionIds);
            }
            final int _tmpOccurrenceCount;
            _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            final LocalDateTime _tmpLastReferenced;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastReferenced)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastReferenced);
            }
            _tmpLastReferenced = __converters.toLocalDateTime(_tmp);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final byte[] _tmpEmbedding;
            if (_cursor.isNull(_cursorIndexOfEmbedding)) {
              _tmpEmbedding = null;
            } else {
              _tmpEmbedding = _cursor.getBlob(_cursorIndexOfEmbedding);
            }
            final String _tmpEmbeddingModel;
            if (_cursor.isNull(_cursorIndexOfEmbeddingModel)) {
              _tmpEmbeddingModel = null;
            } else {
              _tmpEmbeddingModel = _cursor.getString(_cursorIndexOfEmbeddingModel);
            }
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_3;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_5;
            }
            _item = new ReflectionMemoryEntity(_tmpId,_tmpContent,_tmpCategory,_tmpConfidence,_tmpSourceMessageIds,_tmpSourceSessionIds,_tmpOccurrenceCount,_tmpLastReferenced,_tmpIsActive,_tmpEmbedding,_tmpEmbeddingModel,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<ReflectionMemoryEntity>> getReflectionsByCategory(final String category) {
    final String _sql = "SELECT * FROM reflection_memories WHERE category = ? AND isActive = 1 ORDER BY confidence DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reflection_memories"}, new Callable<List<ReflectionMemoryEntity>>() {
      @Override
      @NonNull
      public List<ReflectionMemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfSourceMessageIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageIds");
          final int _cursorIndexOfSourceSessionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceSessionIds");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfLastReferenced = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReferenced");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfEmbedding = CursorUtil.getColumnIndexOrThrow(_cursor, "embedding");
          final int _cursorIndexOfEmbeddingModel = CursorUtil.getColumnIndexOrThrow(_cursor, "embeddingModel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReflectionMemoryEntity> _result = new ArrayList<ReflectionMemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReflectionMemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpSourceMessageIds;
            if (_cursor.isNull(_cursorIndexOfSourceMessageIds)) {
              _tmpSourceMessageIds = null;
            } else {
              _tmpSourceMessageIds = _cursor.getString(_cursorIndexOfSourceMessageIds);
            }
            final String _tmpSourceSessionIds;
            if (_cursor.isNull(_cursorIndexOfSourceSessionIds)) {
              _tmpSourceSessionIds = null;
            } else {
              _tmpSourceSessionIds = _cursor.getString(_cursorIndexOfSourceSessionIds);
            }
            final int _tmpOccurrenceCount;
            _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            final LocalDateTime _tmpLastReferenced;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastReferenced)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastReferenced);
            }
            _tmpLastReferenced = __converters.toLocalDateTime(_tmp);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final byte[] _tmpEmbedding;
            if (_cursor.isNull(_cursorIndexOfEmbedding)) {
              _tmpEmbedding = null;
            } else {
              _tmpEmbedding = _cursor.getBlob(_cursorIndexOfEmbedding);
            }
            final String _tmpEmbeddingModel;
            if (_cursor.isNull(_cursorIndexOfEmbeddingModel)) {
              _tmpEmbeddingModel = null;
            } else {
              _tmpEmbeddingModel = _cursor.getString(_cursorIndexOfEmbeddingModel);
            }
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_3;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_5;
            }
            _item = new ReflectionMemoryEntity(_tmpId,_tmpContent,_tmpCategory,_tmpConfidence,_tmpSourceMessageIds,_tmpSourceSessionIds,_tmpOccurrenceCount,_tmpLastReferenced,_tmpIsActive,_tmpEmbedding,_tmpEmbeddingModel,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getReflectionsByCategoryList(final String category,
      final Continuation<? super List<ReflectionMemoryEntity>> $completion) {
    final String _sql = "SELECT * FROM reflection_memories WHERE category = ? AND isActive = 1 ORDER BY confidence DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReflectionMemoryEntity>>() {
      @Override
      @NonNull
      public List<ReflectionMemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfSourceMessageIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageIds");
          final int _cursorIndexOfSourceSessionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceSessionIds");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfLastReferenced = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReferenced");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfEmbedding = CursorUtil.getColumnIndexOrThrow(_cursor, "embedding");
          final int _cursorIndexOfEmbeddingModel = CursorUtil.getColumnIndexOrThrow(_cursor, "embeddingModel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReflectionMemoryEntity> _result = new ArrayList<ReflectionMemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReflectionMemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpSourceMessageIds;
            if (_cursor.isNull(_cursorIndexOfSourceMessageIds)) {
              _tmpSourceMessageIds = null;
            } else {
              _tmpSourceMessageIds = _cursor.getString(_cursorIndexOfSourceMessageIds);
            }
            final String _tmpSourceSessionIds;
            if (_cursor.isNull(_cursorIndexOfSourceSessionIds)) {
              _tmpSourceSessionIds = null;
            } else {
              _tmpSourceSessionIds = _cursor.getString(_cursorIndexOfSourceSessionIds);
            }
            final int _tmpOccurrenceCount;
            _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            final LocalDateTime _tmpLastReferenced;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastReferenced)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastReferenced);
            }
            _tmpLastReferenced = __converters.toLocalDateTime(_tmp);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final byte[] _tmpEmbedding;
            if (_cursor.isNull(_cursorIndexOfEmbedding)) {
              _tmpEmbedding = null;
            } else {
              _tmpEmbedding = _cursor.getBlob(_cursorIndexOfEmbedding);
            }
            final String _tmpEmbeddingModel;
            if (_cursor.isNull(_cursorIndexOfEmbeddingModel)) {
              _tmpEmbeddingModel = null;
            } else {
              _tmpEmbeddingModel = _cursor.getString(_cursorIndexOfEmbeddingModel);
            }
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_3;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_5;
            }
            _item = new ReflectionMemoryEntity(_tmpId,_tmpContent,_tmpCategory,_tmpConfidence,_tmpSourceMessageIds,_tmpSourceSessionIds,_tmpOccurrenceCount,_tmpLastReferenced,_tmpIsActive,_tmpEmbedding,_tmpEmbeddingModel,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getReflectionById(final long id,
      final Continuation<? super ReflectionMemoryEntity> $completion) {
    final String _sql = "SELECT * FROM reflection_memories WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReflectionMemoryEntity>() {
      @Override
      @Nullable
      public ReflectionMemoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfSourceMessageIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageIds");
          final int _cursorIndexOfSourceSessionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceSessionIds");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfLastReferenced = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReferenced");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfEmbedding = CursorUtil.getColumnIndexOrThrow(_cursor, "embedding");
          final int _cursorIndexOfEmbeddingModel = CursorUtil.getColumnIndexOrThrow(_cursor, "embeddingModel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ReflectionMemoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpSourceMessageIds;
            if (_cursor.isNull(_cursorIndexOfSourceMessageIds)) {
              _tmpSourceMessageIds = null;
            } else {
              _tmpSourceMessageIds = _cursor.getString(_cursorIndexOfSourceMessageIds);
            }
            final String _tmpSourceSessionIds;
            if (_cursor.isNull(_cursorIndexOfSourceSessionIds)) {
              _tmpSourceSessionIds = null;
            } else {
              _tmpSourceSessionIds = _cursor.getString(_cursorIndexOfSourceSessionIds);
            }
            final int _tmpOccurrenceCount;
            _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            final LocalDateTime _tmpLastReferenced;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastReferenced)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastReferenced);
            }
            _tmpLastReferenced = __converters.toLocalDateTime(_tmp);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final byte[] _tmpEmbedding;
            if (_cursor.isNull(_cursorIndexOfEmbedding)) {
              _tmpEmbedding = null;
            } else {
              _tmpEmbedding = _cursor.getBlob(_cursorIndexOfEmbedding);
            }
            final String _tmpEmbeddingModel;
            if (_cursor.isNull(_cursorIndexOfEmbeddingModel)) {
              _tmpEmbeddingModel = null;
            } else {
              _tmpEmbeddingModel = _cursor.getString(_cursorIndexOfEmbeddingModel);
            }
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_3;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_5;
            }
            _result = new ReflectionMemoryEntity(_tmpId,_tmpContent,_tmpCategory,_tmpConfidence,_tmpSourceMessageIds,_tmpSourceSessionIds,_tmpOccurrenceCount,_tmpLastReferenced,_tmpIsActive,_tmpEmbedding,_tmpEmbeddingModel,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getRecentReflections(final int limit,
      final Continuation<? super List<ReflectionMemoryEntity>> $completion) {
    final String _sql = "SELECT * FROM reflection_memories ORDER BY updatedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReflectionMemoryEntity>>() {
      @Override
      @NonNull
      public List<ReflectionMemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfSourceMessageIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageIds");
          final int _cursorIndexOfSourceSessionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceSessionIds");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfLastReferenced = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReferenced");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfEmbedding = CursorUtil.getColumnIndexOrThrow(_cursor, "embedding");
          final int _cursorIndexOfEmbeddingModel = CursorUtil.getColumnIndexOrThrow(_cursor, "embeddingModel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReflectionMemoryEntity> _result = new ArrayList<ReflectionMemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReflectionMemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpSourceMessageIds;
            if (_cursor.isNull(_cursorIndexOfSourceMessageIds)) {
              _tmpSourceMessageIds = null;
            } else {
              _tmpSourceMessageIds = _cursor.getString(_cursorIndexOfSourceMessageIds);
            }
            final String _tmpSourceSessionIds;
            if (_cursor.isNull(_cursorIndexOfSourceSessionIds)) {
              _tmpSourceSessionIds = null;
            } else {
              _tmpSourceSessionIds = _cursor.getString(_cursorIndexOfSourceSessionIds);
            }
            final int _tmpOccurrenceCount;
            _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            final LocalDateTime _tmpLastReferenced;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastReferenced)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastReferenced);
            }
            _tmpLastReferenced = __converters.toLocalDateTime(_tmp);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final byte[] _tmpEmbedding;
            if (_cursor.isNull(_cursorIndexOfEmbedding)) {
              _tmpEmbedding = null;
            } else {
              _tmpEmbedding = _cursor.getBlob(_cursorIndexOfEmbedding);
            }
            final String _tmpEmbeddingModel;
            if (_cursor.isNull(_cursorIndexOfEmbeddingModel)) {
              _tmpEmbeddingModel = null;
            } else {
              _tmpEmbeddingModel = _cursor.getString(_cursorIndexOfEmbeddingModel);
            }
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_3;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_5;
            }
            _item = new ReflectionMemoryEntity(_tmpId,_tmpContent,_tmpCategory,_tmpConfidence,_tmpSourceMessageIds,_tmpSourceSessionIds,_tmpOccurrenceCount,_tmpLastReferenced,_tmpIsActive,_tmpEmbedding,_tmpEmbeddingModel,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getHighConfidenceReflections(final float minConfidence,
      final Continuation<? super List<ReflectionMemoryEntity>> $completion) {
    final String _sql = "SELECT * FROM reflection_memories WHERE confidence >= ? AND isActive = 1 ORDER BY confidence DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindDouble(_argIndex, minConfidence);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReflectionMemoryEntity>>() {
      @Override
      @NonNull
      public List<ReflectionMemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfSourceMessageIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageIds");
          final int _cursorIndexOfSourceSessionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceSessionIds");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfLastReferenced = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReferenced");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfEmbedding = CursorUtil.getColumnIndexOrThrow(_cursor, "embedding");
          final int _cursorIndexOfEmbeddingModel = CursorUtil.getColumnIndexOrThrow(_cursor, "embeddingModel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReflectionMemoryEntity> _result = new ArrayList<ReflectionMemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReflectionMemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpSourceMessageIds;
            if (_cursor.isNull(_cursorIndexOfSourceMessageIds)) {
              _tmpSourceMessageIds = null;
            } else {
              _tmpSourceMessageIds = _cursor.getString(_cursorIndexOfSourceMessageIds);
            }
            final String _tmpSourceSessionIds;
            if (_cursor.isNull(_cursorIndexOfSourceSessionIds)) {
              _tmpSourceSessionIds = null;
            } else {
              _tmpSourceSessionIds = _cursor.getString(_cursorIndexOfSourceSessionIds);
            }
            final int _tmpOccurrenceCount;
            _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            final LocalDateTime _tmpLastReferenced;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastReferenced)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastReferenced);
            }
            _tmpLastReferenced = __converters.toLocalDateTime(_tmp);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final byte[] _tmpEmbedding;
            if (_cursor.isNull(_cursorIndexOfEmbedding)) {
              _tmpEmbedding = null;
            } else {
              _tmpEmbedding = _cursor.getBlob(_cursorIndexOfEmbedding);
            }
            final String _tmpEmbeddingModel;
            if (_cursor.isNull(_cursorIndexOfEmbeddingModel)) {
              _tmpEmbeddingModel = null;
            } else {
              _tmpEmbeddingModel = _cursor.getString(_cursorIndexOfEmbeddingModel);
            }
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_3;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_5;
            }
            _item = new ReflectionMemoryEntity(_tmpId,_tmpContent,_tmpCategory,_tmpConfidence,_tmpSourceMessageIds,_tmpSourceSessionIds,_tmpOccurrenceCount,_tmpLastReferenced,_tmpIsActive,_tmpEmbedding,_tmpEmbeddingModel,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getActiveReflectionCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM reflection_memories WHERE isActive = 1";
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
  public Object getTotalReflectionCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM reflection_memories";
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
  public Object getReflectionsByCategories(final List<String> categories, final int limit,
      final Continuation<? super List<ReflectionMemoryEntity>> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("\n");
    _stringBuilder.append("        SELECT * FROM reflection_memories ");
    _stringBuilder.append("\n");
    _stringBuilder.append("        WHERE category IN (");
    final int _inputSize = categories.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") AND isActive = 1 ");
    _stringBuilder.append("\n");
    _stringBuilder.append("        ORDER BY confidence DESC ");
    _stringBuilder.append("\n");
    _stringBuilder.append("        LIMIT ");
    _stringBuilder.append("?");
    _stringBuilder.append("\n");
    _stringBuilder.append("    ");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : categories) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    _argIndex = 1 + _inputSize;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReflectionMemoryEntity>>() {
      @Override
      @NonNull
      public List<ReflectionMemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfSourceMessageIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceMessageIds");
          final int _cursorIndexOfSourceSessionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceSessionIds");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfLastReferenced = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReferenced");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfEmbedding = CursorUtil.getColumnIndexOrThrow(_cursor, "embedding");
          final int _cursorIndexOfEmbeddingModel = CursorUtil.getColumnIndexOrThrow(_cursor, "embeddingModel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReflectionMemoryEntity> _result = new ArrayList<ReflectionMemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReflectionMemoryEntity _item_1;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final float _tmpConfidence;
            _tmpConfidence = _cursor.getFloat(_cursorIndexOfConfidence);
            final String _tmpSourceMessageIds;
            if (_cursor.isNull(_cursorIndexOfSourceMessageIds)) {
              _tmpSourceMessageIds = null;
            } else {
              _tmpSourceMessageIds = _cursor.getString(_cursorIndexOfSourceMessageIds);
            }
            final String _tmpSourceSessionIds;
            if (_cursor.isNull(_cursorIndexOfSourceSessionIds)) {
              _tmpSourceSessionIds = null;
            } else {
              _tmpSourceSessionIds = _cursor.getString(_cursorIndexOfSourceSessionIds);
            }
            final int _tmpOccurrenceCount;
            _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            final LocalDateTime _tmpLastReferenced;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastReferenced)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastReferenced);
            }
            _tmpLastReferenced = __converters.toLocalDateTime(_tmp);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final byte[] _tmpEmbedding;
            if (_cursor.isNull(_cursorIndexOfEmbedding)) {
              _tmpEmbedding = null;
            } else {
              _tmpEmbedding = _cursor.getBlob(_cursorIndexOfEmbedding);
            }
            final String _tmpEmbeddingModel;
            if (_cursor.isNull(_cursorIndexOfEmbeddingModel)) {
              _tmpEmbeddingModel = null;
            } else {
              _tmpEmbeddingModel = _cursor.getString(_cursorIndexOfEmbeddingModel);
            }
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_3 = __converters.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_3;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_5;
            }
            _item_1 = new ReflectionMemoryEntity(_tmpId,_tmpContent,_tmpCategory,_tmpConfidence,_tmpSourceMessageIds,_tmpSourceSessionIds,_tmpOccurrenceCount,_tmpLastReferenced,_tmpIsActive,_tmpEmbedding,_tmpEmbeddingModel,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item_1);
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
