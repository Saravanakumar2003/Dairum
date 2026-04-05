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
import com.dairum.data.local.entity.ConversationContextEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ConversationContextDao_Impl implements ConversationContextDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ConversationContextEntity> __insertionAdapterOfConversationContextEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ConversationContextEntity> __deletionAdapterOfConversationContextEntity;

  private final EntityDeletionOrUpdateAdapter<ConversationContextEntity> __updateAdapterOfConversationContextEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTopics;

  private final SharedSQLiteStatement __preparedStmtOfUpdateEmotionalTone;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDiscussionDepth;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastSummaryTime;

  private final SharedSQLiteStatement __preparedStmtOfDeleteContextForSession;

  public ConversationContextDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfConversationContextEntity = new EntityInsertionAdapter<ConversationContextEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `conversation_context` (`id`,`sessionId`,`currentTopics`,`emotionalTone`,`pendingQuestions`,`userPreferences`,`discussionDepth`,`lastSummaryAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ConversationContextEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSessionId());
        if (entity.getCurrentTopics() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCurrentTopics());
        }
        if (entity.getEmotionalTone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmotionalTone());
        }
        if (entity.getPendingQuestions() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPendingQuestions());
        }
        if (entity.getUserPreferences() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUserPreferences());
        }
        statement.bindLong(7, entity.getDiscussionDepth());
        final Long _tmp = __converters.fromLocalDateTime(entity.getLastSummaryAt());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp);
        }
        final Long _tmp_1 = __converters.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_1 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_1);
        }
      }
    };
    this.__deletionAdapterOfConversationContextEntity = new EntityDeletionOrUpdateAdapter<ConversationContextEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `conversation_context` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ConversationContextEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfConversationContextEntity = new EntityDeletionOrUpdateAdapter<ConversationContextEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `conversation_context` SET `id` = ?,`sessionId` = ?,`currentTopics` = ?,`emotionalTone` = ?,`pendingQuestions` = ?,`userPreferences` = ?,`discussionDepth` = ?,`lastSummaryAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ConversationContextEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSessionId());
        if (entity.getCurrentTopics() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCurrentTopics());
        }
        if (entity.getEmotionalTone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmotionalTone());
        }
        if (entity.getPendingQuestions() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPendingQuestions());
        }
        if (entity.getUserPreferences() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUserPreferences());
        }
        statement.bindLong(7, entity.getDiscussionDepth());
        final Long _tmp = __converters.fromLocalDateTime(entity.getLastSummaryAt());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp);
        }
        final Long _tmp_1 = __converters.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_1 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_1);
        }
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateTopics = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE conversation_context \n"
                + "        SET currentTopics = ?, updatedAt = ? \n"
                + "        WHERE sessionId = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateEmotionalTone = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE conversation_context \n"
                + "        SET emotionalTone = ?, updatedAt = ? \n"
                + "        WHERE sessionId = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateDiscussionDepth = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE conversation_context \n"
                + "        SET discussionDepth = ?, updatedAt = ? \n"
                + "        WHERE sessionId = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastSummaryTime = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE conversation_context \n"
                + "        SET lastSummaryAt = ?, updatedAt = ? \n"
                + "        WHERE sessionId = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteContextForSession = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM conversation_context WHERE sessionId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertContext(final ConversationContextEntity context,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfConversationContextEntity.insert(context);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteContext(final ConversationContextEntity context,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfConversationContextEntity.handle(context);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateContext(final ConversationContextEntity context,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfConversationContextEntity.handle(context);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTopics(final String sessionId, final String topics,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTopics.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, topics);
        _argIndex = 2;
        final Long _tmp = __converters.fromLocalDateTime(updatedAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, sessionId);
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
          __preparedStmtOfUpdateTopics.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEmotionalTone(final String sessionId, final String tone,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateEmotionalTone.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, tone);
        _argIndex = 2;
        final Long _tmp = __converters.fromLocalDateTime(updatedAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, sessionId);
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
          __preparedStmtOfUpdateEmotionalTone.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDiscussionDepth(final String sessionId, final int depth,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDiscussionDepth.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, depth);
        _argIndex = 2;
        final Long _tmp = __converters.fromLocalDateTime(updatedAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, sessionId);
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
          __preparedStmtOfUpdateDiscussionDepth.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastSummaryTime(final String sessionId, final LocalDateTime lastSummaryAt,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastSummaryTime.acquire();
        int _argIndex = 1;
        final Long _tmp = __converters.fromLocalDateTime(lastSummaryAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 2;
        final Long _tmp_1 = __converters.fromLocalDateTime(updatedAt);
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp_1);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, sessionId);
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
          __preparedStmtOfUpdateLastSummaryTime.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteContextForSession(final String sessionId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteContextForSession.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, sessionId);
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
          __preparedStmtOfDeleteContextForSession.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getContextForSession(final String sessionId,
      final Continuation<? super ConversationContextEntity> $completion) {
    final String _sql = "SELECT * FROM conversation_context WHERE sessionId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ConversationContextEntity>() {
      @Override
      @Nullable
      public ConversationContextEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfCurrentTopics = CursorUtil.getColumnIndexOrThrow(_cursor, "currentTopics");
          final int _cursorIndexOfEmotionalTone = CursorUtil.getColumnIndexOrThrow(_cursor, "emotionalTone");
          final int _cursorIndexOfPendingQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "pendingQuestions");
          final int _cursorIndexOfUserPreferences = CursorUtil.getColumnIndexOrThrow(_cursor, "userPreferences");
          final int _cursorIndexOfDiscussionDepth = CursorUtil.getColumnIndexOrThrow(_cursor, "discussionDepth");
          final int _cursorIndexOfLastSummaryAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSummaryAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ConversationContextEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSessionId;
            _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            final String _tmpCurrentTopics;
            if (_cursor.isNull(_cursorIndexOfCurrentTopics)) {
              _tmpCurrentTopics = null;
            } else {
              _tmpCurrentTopics = _cursor.getString(_cursorIndexOfCurrentTopics);
            }
            final String _tmpEmotionalTone;
            if (_cursor.isNull(_cursorIndexOfEmotionalTone)) {
              _tmpEmotionalTone = null;
            } else {
              _tmpEmotionalTone = _cursor.getString(_cursorIndexOfEmotionalTone);
            }
            final String _tmpPendingQuestions;
            if (_cursor.isNull(_cursorIndexOfPendingQuestions)) {
              _tmpPendingQuestions = null;
            } else {
              _tmpPendingQuestions = _cursor.getString(_cursorIndexOfPendingQuestions);
            }
            final String _tmpUserPreferences;
            if (_cursor.isNull(_cursorIndexOfUserPreferences)) {
              _tmpUserPreferences = null;
            } else {
              _tmpUserPreferences = _cursor.getString(_cursorIndexOfUserPreferences);
            }
            final int _tmpDiscussionDepth;
            _tmpDiscussionDepth = _cursor.getInt(_cursorIndexOfDiscussionDepth);
            final LocalDateTime _tmpLastSummaryAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastSummaryAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastSummaryAt);
            }
            _tmpLastSummaryAt = __converters.toLocalDateTime(_tmp);
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_2 = __converters.toLocalDateTime(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_2;
            }
            _result = new ConversationContextEntity(_tmpId,_tmpSessionId,_tmpCurrentTopics,_tmpEmotionalTone,_tmpPendingQuestions,_tmpUserPreferences,_tmpDiscussionDepth,_tmpLastSummaryAt,_tmpUpdatedAt);
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
  public Flow<ConversationContextEntity> observeContextForSession(final String sessionId) {
    final String _sql = "SELECT * FROM conversation_context WHERE sessionId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"conversation_context"}, new Callable<ConversationContextEntity>() {
      @Override
      @Nullable
      public ConversationContextEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfCurrentTopics = CursorUtil.getColumnIndexOrThrow(_cursor, "currentTopics");
          final int _cursorIndexOfEmotionalTone = CursorUtil.getColumnIndexOrThrow(_cursor, "emotionalTone");
          final int _cursorIndexOfPendingQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "pendingQuestions");
          final int _cursorIndexOfUserPreferences = CursorUtil.getColumnIndexOrThrow(_cursor, "userPreferences");
          final int _cursorIndexOfDiscussionDepth = CursorUtil.getColumnIndexOrThrow(_cursor, "discussionDepth");
          final int _cursorIndexOfLastSummaryAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSummaryAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ConversationContextEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSessionId;
            _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            final String _tmpCurrentTopics;
            if (_cursor.isNull(_cursorIndexOfCurrentTopics)) {
              _tmpCurrentTopics = null;
            } else {
              _tmpCurrentTopics = _cursor.getString(_cursorIndexOfCurrentTopics);
            }
            final String _tmpEmotionalTone;
            if (_cursor.isNull(_cursorIndexOfEmotionalTone)) {
              _tmpEmotionalTone = null;
            } else {
              _tmpEmotionalTone = _cursor.getString(_cursorIndexOfEmotionalTone);
            }
            final String _tmpPendingQuestions;
            if (_cursor.isNull(_cursorIndexOfPendingQuestions)) {
              _tmpPendingQuestions = null;
            } else {
              _tmpPendingQuestions = _cursor.getString(_cursorIndexOfPendingQuestions);
            }
            final String _tmpUserPreferences;
            if (_cursor.isNull(_cursorIndexOfUserPreferences)) {
              _tmpUserPreferences = null;
            } else {
              _tmpUserPreferences = _cursor.getString(_cursorIndexOfUserPreferences);
            }
            final int _tmpDiscussionDepth;
            _tmpDiscussionDepth = _cursor.getInt(_cursorIndexOfDiscussionDepth);
            final LocalDateTime _tmpLastSummaryAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfLastSummaryAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfLastSummaryAt);
            }
            _tmpLastSummaryAt = __converters.toLocalDateTime(_tmp);
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_2 = __converters.toLocalDateTime(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_2;
            }
            _result = new ConversationContextEntity(_tmpId,_tmpSessionId,_tmpCurrentTopics,_tmpEmotionalTone,_tmpPendingQuestions,_tmpUserPreferences,_tmpDiscussionDepth,_tmpLastSummaryAt,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
