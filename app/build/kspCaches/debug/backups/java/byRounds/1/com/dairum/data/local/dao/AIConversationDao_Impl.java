package com.dairum.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dairum.data.local.converter.Converters;
import com.dairum.data.local.entity.AIConversationEntity;
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
public final class AIConversationDao_Impl implements AIConversationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AIConversationEntity> __insertionAdapterOfAIConversationEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteConversation;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldConversations;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllConversations;

  public AIConversationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAIConversationEntity = new EntityInsertionAdapter<AIConversationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `ai_conversations` (`id`,`sessionId`,`role`,`content`,`sourceEntryIdsJson`,`tokensUsed`,`generationTimeMs`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AIConversationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSessionId());
        statement.bindString(3, entity.getRole());
        statement.bindString(4, entity.getContent());
        if (entity.getSourceEntryIdsJson() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSourceEntryIdsJson());
        }
        statement.bindLong(6, entity.getTokensUsed());
        statement.bindLong(7, entity.getGenerationTimeMs());
        final Long _tmp = __converters.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp);
        }
      }
    };
    this.__preparedStmtOfDeleteConversation = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM ai_conversations WHERE sessionId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldConversations = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM ai_conversations WHERE createdAt < ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllConversations = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM ai_conversations";
        return _query;
      }
    };
  }

  @Override
  public Object insertMessage(final AIConversationEntity message,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAIConversationEntity.insertAndReturnId(message);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteConversation(final String sessionId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteConversation.acquire();
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
          __preparedStmtOfDeleteConversation.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldConversations(final LocalDateTime beforeDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldConversations.acquire();
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
          __preparedStmtOfDeleteOldConversations.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllConversations(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllConversations.acquire();
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
          __preparedStmtOfDeleteAllConversations.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AIConversationEntity>> getConversation(final String sessionId) {
    final String _sql = "SELECT * FROM ai_conversations WHERE sessionId = ? ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ai_conversations"}, new Callable<List<AIConversationEntity>>() {
      @Override
      @NonNull
      public List<AIConversationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfSourceEntryIdsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEntryIdsJson");
          final int _cursorIndexOfTokensUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "tokensUsed");
          final int _cursorIndexOfGenerationTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "generationTimeMs");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AIConversationEntity> _result = new ArrayList<AIConversationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AIConversationEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSessionId;
            _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpSourceEntryIdsJson;
            if (_cursor.isNull(_cursorIndexOfSourceEntryIdsJson)) {
              _tmpSourceEntryIdsJson = null;
            } else {
              _tmpSourceEntryIdsJson = _cursor.getString(_cursorIndexOfSourceEntryIdsJson);
            }
            final int _tmpTokensUsed;
            _tmpTokensUsed = _cursor.getInt(_cursorIndexOfTokensUsed);
            final long _tmpGenerationTimeMs;
            _tmpGenerationTimeMs = _cursor.getLong(_cursorIndexOfGenerationTimeMs);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_1;
            }
            _item = new AIConversationEntity(_tmpId,_tmpSessionId,_tmpRole,_tmpContent,_tmpSourceEntryIdsJson,_tmpTokensUsed,_tmpGenerationTimeMs,_tmpCreatedAt);
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
  public Flow<List<String>> getAllSessionIds() {
    final String _sql = "SELECT DISTINCT sessionId FROM ai_conversations ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ai_conversations"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Object getLastMessageInSession(final String sessionId,
      final Continuation<? super AIConversationEntity> $completion) {
    final String _sql = "SELECT * FROM ai_conversations WHERE sessionId = ? ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AIConversationEntity>() {
      @Override
      @Nullable
      public AIConversationEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfSourceEntryIdsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEntryIdsJson");
          final int _cursorIndexOfTokensUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "tokensUsed");
          final int _cursorIndexOfGenerationTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "generationTimeMs");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final AIConversationEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSessionId;
            _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpSourceEntryIdsJson;
            if (_cursor.isNull(_cursorIndexOfSourceEntryIdsJson)) {
              _tmpSourceEntryIdsJson = null;
            } else {
              _tmpSourceEntryIdsJson = _cursor.getString(_cursorIndexOfSourceEntryIdsJson);
            }
            final int _tmpTokensUsed;
            _tmpTokensUsed = _cursor.getInt(_cursorIndexOfTokensUsed);
            final long _tmpGenerationTimeMs;
            _tmpGenerationTimeMs = _cursor.getLong(_cursorIndexOfGenerationTimeMs);
            final LocalDateTime _tmpCreatedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_1 = __converters.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_1;
            }
            _result = new AIConversationEntity(_tmpId,_tmpSessionId,_tmpRole,_tmpContent,_tmpSourceEntryIdsJson,_tmpTokensUsed,_tmpGenerationTimeMs,_tmpCreatedAt);
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
