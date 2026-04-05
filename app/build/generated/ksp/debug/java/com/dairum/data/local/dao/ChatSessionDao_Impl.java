package com.dairum.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
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
import com.dairum.data.local.entity.ChatMessageEntity;
import com.dairum.data.local.entity.ChatSessionEntity;
import com.dairum.data.local.entity.ChatSessionWithMessagesRelation;
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
import java.util.Set;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ChatSessionDao_Impl implements ChatSessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChatSessionEntity> __insertionAdapterOfChatSessionEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ChatSessionEntity> __deletionAdapterOfChatSessionEntity;

  private final EntityDeletionOrUpdateAdapter<ChatSessionEntity> __updateAdapterOfChatSessionEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSessionTitle;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSavedStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateArchivedStatus;

  private final SharedSQLiteStatement __preparedStmtOfIncrementMessageCount;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSessionSummary;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSessionById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldArchivedSessions;

  public ChatSessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChatSessionEntity = new EntityInsertionAdapter<ChatSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chat_sessions` (`id`,`title`,`summary`,`isSaved`,`isArchived`,`messageCount`,`lastMessageAt`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChatSessionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getSummary() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSummary());
        }
        final int _tmp = entity.isSaved() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isArchived() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getMessageCount());
        final Long _tmp_2 = __converters.fromLocalDateTime(entity.getLastMessageAt());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp_2);
        }
        final Long _tmp_3 = __converters.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp_3);
        }
        final Long _tmp_4 = __converters.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_4 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_4);
        }
      }
    };
    this.__deletionAdapterOfChatSessionEntity = new EntityDeletionOrUpdateAdapter<ChatSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `chat_sessions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChatSessionEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfChatSessionEntity = new EntityDeletionOrUpdateAdapter<ChatSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `chat_sessions` SET `id` = ?,`title` = ?,`summary` = ?,`isSaved` = ?,`isArchived` = ?,`messageCount` = ?,`lastMessageAt` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChatSessionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getSummary() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSummary());
        }
        final int _tmp = entity.isSaved() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isArchived() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getMessageCount());
        final Long _tmp_2 = __converters.fromLocalDateTime(entity.getLastMessageAt());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp_2);
        }
        final Long _tmp_3 = __converters.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp_3);
        }
        final Long _tmp_4 = __converters.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_4 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_4);
        }
        statement.bindString(10, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateSessionTitle = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chat_sessions SET title = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSavedStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chat_sessions SET isSaved = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateArchivedStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chat_sessions SET isArchived = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementMessageCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chat_sessions SET messageCount = messageCount + 1, lastMessageAt = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSessionSummary = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chat_sessions SET summary = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSessionById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM chat_sessions WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldArchivedSessions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM chat_sessions WHERE isArchived = 1 AND updatedAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSession(final ChatSessionEntity session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChatSessionEntity.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSession(final ChatSessionEntity session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfChatSessionEntity.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSession(final ChatSessionEntity session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChatSessionEntity.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSessionTitle(final String sessionId, final String title,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSessionTitle.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, title);
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
          __preparedStmtOfUpdateSessionTitle.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSavedStatus(final String sessionId, final boolean isSaved,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSavedStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isSaved ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
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
          __preparedStmtOfUpdateSavedStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateArchivedStatus(final String sessionId, final boolean isArchived,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateArchivedStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isArchived ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
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
          __preparedStmtOfUpdateArchivedStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementMessageCount(final String sessionId, final LocalDateTime lastMessageAt,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementMessageCount.acquire();
        int _argIndex = 1;
        final Long _tmp = __converters.fromLocalDateTime(lastMessageAt);
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
          __preparedStmtOfIncrementMessageCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSessionSummary(final String sessionId, final String summary,
      final LocalDateTime updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSessionSummary.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, summary);
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
          __preparedStmtOfUpdateSessionSummary.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSessionById(final String sessionId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSessionById.acquire();
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
          __preparedStmtOfDeleteSessionById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldArchivedSessions(final LocalDateTime beforeDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldArchivedSessions.acquire();
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
          __preparedStmtOfDeleteOldArchivedSessions.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ChatSessionEntity>> getAllSessions() {
    final String _sql = "SELECT * FROM chat_sessions ORDER BY lastMessageAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chat_sessions"}, new Callable<List<ChatSessionEntity>>() {
      @Override
      @NonNull
      public List<ChatSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfIsSaved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSaved");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfMessageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messageCount");
          final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ChatSessionEntity> _result = new ArrayList<ChatSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChatSessionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            if (_cursor.isNull(_cursorIndexOfSummary)) {
              _tmpSummary = null;
            } else {
              _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            }
            final boolean _tmpIsSaved;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSaved);
            _tmpIsSaved = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final int _tmpMessageCount;
            _tmpMessageCount = _cursor.getInt(_cursorIndexOfMessageCount);
            final LocalDateTime _tmpLastMessageAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastMessageAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastMessageAt);
            }
            _tmpLastMessageAt = __converters.toLocalDateTime(_tmp_2);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __converters.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            _item = new ChatSessionEntity(_tmpId,_tmpTitle,_tmpSummary,_tmpIsSaved,_tmpIsArchived,_tmpMessageCount,_tmpLastMessageAt,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<ChatSessionEntity>> getSavedSessions() {
    final String _sql = "SELECT * FROM chat_sessions WHERE isSaved = 1 ORDER BY lastMessageAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chat_sessions"}, new Callable<List<ChatSessionEntity>>() {
      @Override
      @NonNull
      public List<ChatSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfIsSaved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSaved");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfMessageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messageCount");
          final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ChatSessionEntity> _result = new ArrayList<ChatSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChatSessionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            if (_cursor.isNull(_cursorIndexOfSummary)) {
              _tmpSummary = null;
            } else {
              _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            }
            final boolean _tmpIsSaved;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSaved);
            _tmpIsSaved = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final int _tmpMessageCount;
            _tmpMessageCount = _cursor.getInt(_cursorIndexOfMessageCount);
            final LocalDateTime _tmpLastMessageAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastMessageAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastMessageAt);
            }
            _tmpLastMessageAt = __converters.toLocalDateTime(_tmp_2);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __converters.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            _item = new ChatSessionEntity(_tmpId,_tmpTitle,_tmpSummary,_tmpIsSaved,_tmpIsArchived,_tmpMessageCount,_tmpLastMessageAt,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<ChatSessionEntity>> getActiveSessions() {
    final String _sql = "SELECT * FROM chat_sessions WHERE isArchived = 0 ORDER BY lastMessageAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chat_sessions"}, new Callable<List<ChatSessionEntity>>() {
      @Override
      @NonNull
      public List<ChatSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfIsSaved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSaved");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfMessageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messageCount");
          final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ChatSessionEntity> _result = new ArrayList<ChatSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChatSessionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            if (_cursor.isNull(_cursorIndexOfSummary)) {
              _tmpSummary = null;
            } else {
              _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            }
            final boolean _tmpIsSaved;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSaved);
            _tmpIsSaved = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final int _tmpMessageCount;
            _tmpMessageCount = _cursor.getInt(_cursorIndexOfMessageCount);
            final LocalDateTime _tmpLastMessageAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastMessageAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastMessageAt);
            }
            _tmpLastMessageAt = __converters.toLocalDateTime(_tmp_2);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __converters.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            _item = new ChatSessionEntity(_tmpId,_tmpTitle,_tmpSummary,_tmpIsSaved,_tmpIsArchived,_tmpMessageCount,_tmpLastMessageAt,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getSessionById(final String sessionId,
      final Continuation<? super ChatSessionEntity> $completion) {
    final String _sql = "SELECT * FROM chat_sessions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ChatSessionEntity>() {
      @Override
      @Nullable
      public ChatSessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfIsSaved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSaved");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfMessageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messageCount");
          final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ChatSessionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            if (_cursor.isNull(_cursorIndexOfSummary)) {
              _tmpSummary = null;
            } else {
              _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            }
            final boolean _tmpIsSaved;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSaved);
            _tmpIsSaved = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final int _tmpMessageCount;
            _tmpMessageCount = _cursor.getInt(_cursorIndexOfMessageCount);
            final LocalDateTime _tmpLastMessageAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastMessageAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastMessageAt);
            }
            _tmpLastMessageAt = __converters.toLocalDateTime(_tmp_2);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __converters.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            _result = new ChatSessionEntity(_tmpId,_tmpTitle,_tmpSummary,_tmpIsSaved,_tmpIsArchived,_tmpMessageCount,_tmpLastMessageAt,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<ChatSessionEntity> observeSession(final String sessionId) {
    final String _sql = "SELECT * FROM chat_sessions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chat_sessions"}, new Callable<ChatSessionEntity>() {
      @Override
      @Nullable
      public ChatSessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final int _cursorIndexOfIsSaved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSaved");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfMessageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messageCount");
          final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ChatSessionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSummary;
            if (_cursor.isNull(_cursorIndexOfSummary)) {
              _tmpSummary = null;
            } else {
              _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            }
            final boolean _tmpIsSaved;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSaved);
            _tmpIsSaved = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final int _tmpMessageCount;
            _tmpMessageCount = _cursor.getInt(_cursorIndexOfMessageCount);
            final LocalDateTime _tmpLastMessageAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastMessageAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastMessageAt);
            }
            _tmpLastMessageAt = __converters.toLocalDateTime(_tmp_2);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __converters.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            _result = new ChatSessionEntity(_tmpId,_tmpTitle,_tmpSummary,_tmpIsSaved,_tmpIsArchived,_tmpMessageCount,_tmpLastMessageAt,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getSessionWithMessages(final String sessionId,
      final Continuation<? super ChatSessionWithMessagesRelation> $completion) {
    final String _sql = "SELECT * FROM chat_sessions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<ChatSessionWithMessagesRelation>() {
      @Override
      @Nullable
      public ChatSessionWithMessagesRelation call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
            final int _cursorIndexOfIsSaved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSaved");
            final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
            final int _cursorIndexOfMessageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messageCount");
            final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final ArrayMap<String, ArrayList<ChatMessageEntity>> _collectionMessages = new ArrayMap<String, ArrayList<ChatMessageEntity>>();
            while (_cursor.moveToNext()) {
              final String _tmpKey;
              _tmpKey = _cursor.getString(_cursorIndexOfId);
              if (!_collectionMessages.containsKey(_tmpKey)) {
                _collectionMessages.put(_tmpKey, new ArrayList<ChatMessageEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchatMessagesAscomDairumDataLocalEntityChatMessageEntity(_collectionMessages);
            final ChatSessionWithMessagesRelation _result;
            if (_cursor.moveToFirst()) {
              final ChatSessionEntity _tmpSession;
              final String _tmpId;
              _tmpId = _cursor.getString(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpSummary;
              if (_cursor.isNull(_cursorIndexOfSummary)) {
                _tmpSummary = null;
              } else {
                _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
              }
              final boolean _tmpIsSaved;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsSaved);
              _tmpIsSaved = _tmp != 0;
              final boolean _tmpIsArchived;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
              _tmpIsArchived = _tmp_1 != 0;
              final int _tmpMessageCount;
              _tmpMessageCount = _cursor.getInt(_cursorIndexOfMessageCount);
              final LocalDateTime _tmpLastMessageAt;
              final Long _tmp_2;
              if (_cursor.isNull(_cursorIndexOfLastMessageAt)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getLong(_cursorIndexOfLastMessageAt);
              }
              _tmpLastMessageAt = __converters.toLocalDateTime(_tmp_2);
              final LocalDateTime _tmpCreatedAt;
              final Long _tmp_3;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp_3 = null;
              } else {
                _tmp_3 = _cursor.getLong(_cursorIndexOfCreatedAt);
              }
              final LocalDateTime _tmp_4 = __converters.toLocalDateTime(_tmp_3);
              if (_tmp_4 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
              } else {
                _tmpCreatedAt = _tmp_4;
              }
              final LocalDateTime _tmpUpdatedAt;
              final Long _tmp_5;
              if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
                _tmp_5 = null;
              } else {
                _tmp_5 = _cursor.getLong(_cursorIndexOfUpdatedAt);
              }
              final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
              if (_tmp_6 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
              } else {
                _tmpUpdatedAt = _tmp_6;
              }
              _tmpSession = new ChatSessionEntity(_tmpId,_tmpTitle,_tmpSummary,_tmpIsSaved,_tmpIsArchived,_tmpMessageCount,_tmpLastMessageAt,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<ChatMessageEntity> _tmpMessagesCollection;
              final String _tmpKey_1;
              _tmpKey_1 = _cursor.getString(_cursorIndexOfId);
              _tmpMessagesCollection = _collectionMessages.get(_tmpKey_1);
              _result = new ChatSessionWithMessagesRelation(_tmpSession,_tmpMessagesCollection);
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
  public Flow<ChatSessionWithMessagesRelation> observeSessionWithMessages(final String sessionId) {
    final String _sql = "SELECT * FROM chat_sessions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"chat_messages",
        "chat_sessions"}, new Callable<ChatSessionWithMessagesRelation>() {
      @Override
      @Nullable
      public ChatSessionWithMessagesRelation call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
            final int _cursorIndexOfIsSaved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSaved");
            final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
            final int _cursorIndexOfMessageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messageCount");
            final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final ArrayMap<String, ArrayList<ChatMessageEntity>> _collectionMessages = new ArrayMap<String, ArrayList<ChatMessageEntity>>();
            while (_cursor.moveToNext()) {
              final String _tmpKey;
              _tmpKey = _cursor.getString(_cursorIndexOfId);
              if (!_collectionMessages.containsKey(_tmpKey)) {
                _collectionMessages.put(_tmpKey, new ArrayList<ChatMessageEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchatMessagesAscomDairumDataLocalEntityChatMessageEntity(_collectionMessages);
            final ChatSessionWithMessagesRelation _result;
            if (_cursor.moveToFirst()) {
              final ChatSessionEntity _tmpSession;
              final String _tmpId;
              _tmpId = _cursor.getString(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpSummary;
              if (_cursor.isNull(_cursorIndexOfSummary)) {
                _tmpSummary = null;
              } else {
                _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
              }
              final boolean _tmpIsSaved;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsSaved);
              _tmpIsSaved = _tmp != 0;
              final boolean _tmpIsArchived;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
              _tmpIsArchived = _tmp_1 != 0;
              final int _tmpMessageCount;
              _tmpMessageCount = _cursor.getInt(_cursorIndexOfMessageCount);
              final LocalDateTime _tmpLastMessageAt;
              final Long _tmp_2;
              if (_cursor.isNull(_cursorIndexOfLastMessageAt)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getLong(_cursorIndexOfLastMessageAt);
              }
              _tmpLastMessageAt = __converters.toLocalDateTime(_tmp_2);
              final LocalDateTime _tmpCreatedAt;
              final Long _tmp_3;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp_3 = null;
              } else {
                _tmp_3 = _cursor.getLong(_cursorIndexOfCreatedAt);
              }
              final LocalDateTime _tmp_4 = __converters.toLocalDateTime(_tmp_3);
              if (_tmp_4 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
              } else {
                _tmpCreatedAt = _tmp_4;
              }
              final LocalDateTime _tmpUpdatedAt;
              final Long _tmp_5;
              if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
                _tmp_5 = null;
              } else {
                _tmp_5 = _cursor.getLong(_cursorIndexOfUpdatedAt);
              }
              final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
              if (_tmp_6 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
              } else {
                _tmpUpdatedAt = _tmp_6;
              }
              _tmpSession = new ChatSessionEntity(_tmpId,_tmpTitle,_tmpSummary,_tmpIsSaved,_tmpIsArchived,_tmpMessageCount,_tmpLastMessageAt,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<ChatMessageEntity> _tmpMessagesCollection;
              final String _tmpKey_1;
              _tmpKey_1 = _cursor.getString(_cursorIndexOfId);
              _tmpMessagesCollection = _collectionMessages.get(_tmpKey_1);
              _result = new ChatSessionWithMessagesRelation(_tmpSession,_tmpMessagesCollection);
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
  public Object getSessionCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM chat_sessions";
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
  public Object getSavedSessionCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM chat_sessions WHERE isSaved = 1";
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipchatMessagesAscomDairumDataLocalEntityChatMessageEntity(
      @NonNull final ArrayMap<String, ArrayList<ChatMessageEntity>> _map) {
    final Set<String> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchArrayMap(_map, true, (map) -> {
        __fetchRelationshipchatMessagesAscomDairumDataLocalEntityChatMessageEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`sessionId`,`role`,`content`,`tokensUsed`,`generationTimeMs`,`isEdited`,`metadata`,`createdAt` FROM `chat_messages` WHERE `sessionId` IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : __mapKeySet) {
      _stmt.bindString(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "sessionId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfSessionId = 1;
      final int _cursorIndexOfRole = 2;
      final int _cursorIndexOfContent = 3;
      final int _cursorIndexOfTokensUsed = 4;
      final int _cursorIndexOfGenerationTimeMs = 5;
      final int _cursorIndexOfIsEdited = 6;
      final int _cursorIndexOfMetadata = 7;
      final int _cursorIndexOfCreatedAt = 8;
      while (_cursor.moveToNext()) {
        final String _tmpKey;
        _tmpKey = _cursor.getString(_itemKeyIndex);
        final ArrayList<ChatMessageEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final ChatMessageEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpSessionId;
          _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
          final String _tmpRole;
          _tmpRole = _cursor.getString(_cursorIndexOfRole);
          final String _tmpContent;
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
          final int _tmpTokensUsed;
          _tmpTokensUsed = _cursor.getInt(_cursorIndexOfTokensUsed);
          final long _tmpGenerationTimeMs;
          _tmpGenerationTimeMs = _cursor.getLong(_cursorIndexOfGenerationTimeMs);
          final boolean _tmpIsEdited;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsEdited);
          _tmpIsEdited = _tmp != 0;
          final String _tmpMetadata;
          if (_cursor.isNull(_cursorIndexOfMetadata)) {
            _tmpMetadata = null;
          } else {
            _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
          }
          final LocalDateTime _tmpCreatedAt;
          final Long _tmp_1;
          if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = _cursor.getLong(_cursorIndexOfCreatedAt);
          }
          final LocalDateTime _tmp_2 = __converters.toLocalDateTime(_tmp_1);
          if (_tmp_2 == null) {
            throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
          } else {
            _tmpCreatedAt = _tmp_2;
          }
          _item_1 = new ChatMessageEntity(_tmpId,_tmpSessionId,_tmpRole,_tmpContent,_tmpTokensUsed,_tmpGenerationTimeMs,_tmpIsEdited,_tmpMetadata,_tmpCreatedAt);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
