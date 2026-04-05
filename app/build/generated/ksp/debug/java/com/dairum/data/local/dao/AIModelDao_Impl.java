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
import com.dairum.data.local.entity.AIModelEntity;
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
public final class AIModelDao_Impl implements AIModelDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AIModelEntity> __insertionAdapterOfAIModelEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<AIModelEntity> __deletionAdapterOfAIModelEntity;

  private final EntityDeletionOrUpdateAdapter<AIModelEntity> __updateAdapterOfAIModelEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearDefaultForType;

  private final SharedSQLiteStatement __preparedStmtOfSetAsDefault;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDownloadStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastUsed;

  private final SharedSQLiteStatement __preparedStmtOfDeleteModelById;

  public AIModelDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAIModelEntity = new EntityInsertionAdapter<AIModelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `ai_models` (`id`,`name`,`description`,`filePath`,`sizeBytes`,`modelType`,`quantization`,`contextLength`,`isDefault`,`isDownloaded`,`downloadUrl`,`createdAt`,`lastUsedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AIModelEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getFilePath());
        statement.bindLong(5, entity.getSizeBytes());
        statement.bindString(6, entity.getModelType());
        if (entity.getQuantization() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getQuantization());
        }
        statement.bindLong(8, entity.getContextLength());
        final int _tmp = entity.isDefault() ? 1 : 0;
        statement.bindLong(9, _tmp);
        final int _tmp_1 = entity.isDownloaded() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        if (entity.getDownloadUrl() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDownloadUrl());
        }
        final Long _tmp_2 = __converters.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, _tmp_2);
        }
        final Long _tmp_3 = __converters.fromLocalDateTime(entity.getLastUsedAt());
        if (_tmp_3 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_3);
        }
      }
    };
    this.__deletionAdapterOfAIModelEntity = new EntityDeletionOrUpdateAdapter<AIModelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `ai_models` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AIModelEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfAIModelEntity = new EntityDeletionOrUpdateAdapter<AIModelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `ai_models` SET `id` = ?,`name` = ?,`description` = ?,`filePath` = ?,`sizeBytes` = ?,`modelType` = ?,`quantization` = ?,`contextLength` = ?,`isDefault` = ?,`isDownloaded` = ?,`downloadUrl` = ?,`createdAt` = ?,`lastUsedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AIModelEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getFilePath());
        statement.bindLong(5, entity.getSizeBytes());
        statement.bindString(6, entity.getModelType());
        if (entity.getQuantization() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getQuantization());
        }
        statement.bindLong(8, entity.getContextLength());
        final int _tmp = entity.isDefault() ? 1 : 0;
        statement.bindLong(9, _tmp);
        final int _tmp_1 = entity.isDownloaded() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        if (entity.getDownloadUrl() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDownloadUrl());
        }
        final Long _tmp_2 = __converters.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, _tmp_2);
        }
        final Long _tmp_3 = __converters.fromLocalDateTime(entity.getLastUsedAt());
        if (_tmp_3 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_3);
        }
        statement.bindString(14, entity.getId());
      }
    };
    this.__preparedStmtOfClearDefaultForType = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE ai_models SET isDefault = 0 WHERE modelType = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetAsDefault = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE ai_models SET isDefault = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateDownloadStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE ai_models SET isDownloaded = ?, filePath = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastUsed = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE ai_models SET lastUsedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteModelById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM ai_models WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertModel(final AIModelEntity model,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAIModelEntity.insert(model);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertModels(final List<AIModelEntity> models,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAIModelEntity.insert(models);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteModel(final AIModelEntity model,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAIModelEntity.handle(model);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateModel(final AIModelEntity model,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAIModelEntity.handle(model);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearDefaultForType(final String modelType,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearDefaultForType.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, modelType);
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
          __preparedStmtOfClearDefaultForType.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setAsDefault(final String modelId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetAsDefault.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, modelId);
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
          __preparedStmtOfSetAsDefault.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDownloadStatus(final String modelId, final boolean isDownloaded,
      final String filePath, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDownloadStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isDownloaded ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, filePath);
        _argIndex = 3;
        _stmt.bindString(_argIndex, modelId);
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
          __preparedStmtOfUpdateDownloadStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastUsed(final String modelId, final LocalDateTime lastUsedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastUsed.acquire();
        int _argIndex = 1;
        final Long _tmp = __converters.fromLocalDateTime(lastUsedAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 2;
        _stmt.bindString(_argIndex, modelId);
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
          __preparedStmtOfUpdateLastUsed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteModelById(final String modelId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteModelById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, modelId);
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
          __preparedStmtOfDeleteModelById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AIModelEntity>> getAllModels() {
    final String _sql = "SELECT * FROM ai_models ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ai_models"}, new Callable<List<AIModelEntity>>() {
      @Override
      @NonNull
      public List<AIModelEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfModelType = CursorUtil.getColumnIndexOrThrow(_cursor, "modelType");
          final int _cursorIndexOfQuantization = CursorUtil.getColumnIndexOrThrow(_cursor, "quantization");
          final int _cursorIndexOfContextLength = CursorUtil.getColumnIndexOrThrow(_cursor, "contextLength");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isDownloaded");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastUsedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsedAt");
          final List<AIModelEntity> _result = new ArrayList<AIModelEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AIModelEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpModelType;
            _tmpModelType = _cursor.getString(_cursorIndexOfModelType);
            final String _tmpQuantization;
            if (_cursor.isNull(_cursorIndexOfQuantization)) {
              _tmpQuantization = null;
            } else {
              _tmpQuantization = _cursor.getString(_cursorIndexOfQuantization);
            }
            final int _tmpContextLength;
            _tmpContextLength = _cursor.getInt(_cursorIndexOfContextLength);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadUrl;
            if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
              _tmpDownloadUrl = null;
            } else {
              _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
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
            final LocalDateTime _tmpLastUsedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfLastUsedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfLastUsedAt);
            }
            _tmpLastUsedAt = __converters.toLocalDateTime(_tmp_4);
            _item = new AIModelEntity(_tmpId,_tmpName,_tmpDescription,_tmpFilePath,_tmpSizeBytes,_tmpModelType,_tmpQuantization,_tmpContextLength,_tmpIsDefault,_tmpIsDownloaded,_tmpDownloadUrl,_tmpCreatedAt,_tmpLastUsedAt);
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
  public Object getAllModelsList(final Continuation<? super List<AIModelEntity>> $completion) {
    final String _sql = "SELECT * FROM ai_models ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AIModelEntity>>() {
      @Override
      @NonNull
      public List<AIModelEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfModelType = CursorUtil.getColumnIndexOrThrow(_cursor, "modelType");
          final int _cursorIndexOfQuantization = CursorUtil.getColumnIndexOrThrow(_cursor, "quantization");
          final int _cursorIndexOfContextLength = CursorUtil.getColumnIndexOrThrow(_cursor, "contextLength");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isDownloaded");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastUsedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsedAt");
          final List<AIModelEntity> _result = new ArrayList<AIModelEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AIModelEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpModelType;
            _tmpModelType = _cursor.getString(_cursorIndexOfModelType);
            final String _tmpQuantization;
            if (_cursor.isNull(_cursorIndexOfQuantization)) {
              _tmpQuantization = null;
            } else {
              _tmpQuantization = _cursor.getString(_cursorIndexOfQuantization);
            }
            final int _tmpContextLength;
            _tmpContextLength = _cursor.getInt(_cursorIndexOfContextLength);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadUrl;
            if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
              _tmpDownloadUrl = null;
            } else {
              _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
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
            final LocalDateTime _tmpLastUsedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfLastUsedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfLastUsedAt);
            }
            _tmpLastUsedAt = __converters.toLocalDateTime(_tmp_4);
            _item = new AIModelEntity(_tmpId,_tmpName,_tmpDescription,_tmpFilePath,_tmpSizeBytes,_tmpModelType,_tmpQuantization,_tmpContextLength,_tmpIsDefault,_tmpIsDownloaded,_tmpDownloadUrl,_tmpCreatedAt,_tmpLastUsedAt);
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
  public Flow<List<AIModelEntity>> getModelsByType(final String modelType) {
    final String _sql = "SELECT * FROM ai_models WHERE modelType = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, modelType);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ai_models"}, new Callable<List<AIModelEntity>>() {
      @Override
      @NonNull
      public List<AIModelEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfModelType = CursorUtil.getColumnIndexOrThrow(_cursor, "modelType");
          final int _cursorIndexOfQuantization = CursorUtil.getColumnIndexOrThrow(_cursor, "quantization");
          final int _cursorIndexOfContextLength = CursorUtil.getColumnIndexOrThrow(_cursor, "contextLength");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isDownloaded");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastUsedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsedAt");
          final List<AIModelEntity> _result = new ArrayList<AIModelEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AIModelEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpModelType;
            _tmpModelType = _cursor.getString(_cursorIndexOfModelType);
            final String _tmpQuantization;
            if (_cursor.isNull(_cursorIndexOfQuantization)) {
              _tmpQuantization = null;
            } else {
              _tmpQuantization = _cursor.getString(_cursorIndexOfQuantization);
            }
            final int _tmpContextLength;
            _tmpContextLength = _cursor.getInt(_cursorIndexOfContextLength);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadUrl;
            if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
              _tmpDownloadUrl = null;
            } else {
              _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
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
            final LocalDateTime _tmpLastUsedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfLastUsedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfLastUsedAt);
            }
            _tmpLastUsedAt = __converters.toLocalDateTime(_tmp_4);
            _item = new AIModelEntity(_tmpId,_tmpName,_tmpDescription,_tmpFilePath,_tmpSizeBytes,_tmpModelType,_tmpQuantization,_tmpContextLength,_tmpIsDefault,_tmpIsDownloaded,_tmpDownloadUrl,_tmpCreatedAt,_tmpLastUsedAt);
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
  public Flow<List<AIModelEntity>> getDownloadedModels() {
    final String _sql = "SELECT * FROM ai_models WHERE isDownloaded = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ai_models"}, new Callable<List<AIModelEntity>>() {
      @Override
      @NonNull
      public List<AIModelEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfModelType = CursorUtil.getColumnIndexOrThrow(_cursor, "modelType");
          final int _cursorIndexOfQuantization = CursorUtil.getColumnIndexOrThrow(_cursor, "quantization");
          final int _cursorIndexOfContextLength = CursorUtil.getColumnIndexOrThrow(_cursor, "contextLength");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isDownloaded");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastUsedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsedAt");
          final List<AIModelEntity> _result = new ArrayList<AIModelEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AIModelEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpModelType;
            _tmpModelType = _cursor.getString(_cursorIndexOfModelType);
            final String _tmpQuantization;
            if (_cursor.isNull(_cursorIndexOfQuantization)) {
              _tmpQuantization = null;
            } else {
              _tmpQuantization = _cursor.getString(_cursorIndexOfQuantization);
            }
            final int _tmpContextLength;
            _tmpContextLength = _cursor.getInt(_cursorIndexOfContextLength);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadUrl;
            if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
              _tmpDownloadUrl = null;
            } else {
              _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
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
            final LocalDateTime _tmpLastUsedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfLastUsedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfLastUsedAt);
            }
            _tmpLastUsedAt = __converters.toLocalDateTime(_tmp_4);
            _item = new AIModelEntity(_tmpId,_tmpName,_tmpDescription,_tmpFilePath,_tmpSizeBytes,_tmpModelType,_tmpQuantization,_tmpContextLength,_tmpIsDefault,_tmpIsDownloaded,_tmpDownloadUrl,_tmpCreatedAt,_tmpLastUsedAt);
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
  public Object getModelById(final String id,
      final Continuation<? super AIModelEntity> $completion) {
    final String _sql = "SELECT * FROM ai_models WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AIModelEntity>() {
      @Override
      @Nullable
      public AIModelEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfModelType = CursorUtil.getColumnIndexOrThrow(_cursor, "modelType");
          final int _cursorIndexOfQuantization = CursorUtil.getColumnIndexOrThrow(_cursor, "quantization");
          final int _cursorIndexOfContextLength = CursorUtil.getColumnIndexOrThrow(_cursor, "contextLength");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isDownloaded");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastUsedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsedAt");
          final AIModelEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpModelType;
            _tmpModelType = _cursor.getString(_cursorIndexOfModelType);
            final String _tmpQuantization;
            if (_cursor.isNull(_cursorIndexOfQuantization)) {
              _tmpQuantization = null;
            } else {
              _tmpQuantization = _cursor.getString(_cursorIndexOfQuantization);
            }
            final int _tmpContextLength;
            _tmpContextLength = _cursor.getInt(_cursorIndexOfContextLength);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadUrl;
            if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
              _tmpDownloadUrl = null;
            } else {
              _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
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
            final LocalDateTime _tmpLastUsedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfLastUsedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfLastUsedAt);
            }
            _tmpLastUsedAt = __converters.toLocalDateTime(_tmp_4);
            _result = new AIModelEntity(_tmpId,_tmpName,_tmpDescription,_tmpFilePath,_tmpSizeBytes,_tmpModelType,_tmpQuantization,_tmpContextLength,_tmpIsDefault,_tmpIsDownloaded,_tmpDownloadUrl,_tmpCreatedAt,_tmpLastUsedAt);
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
  public Object getDefaultModel(final String modelType,
      final Continuation<? super AIModelEntity> $completion) {
    final String _sql = "SELECT * FROM ai_models WHERE isDefault = 1 AND modelType = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, modelType);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AIModelEntity>() {
      @Override
      @Nullable
      public AIModelEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfModelType = CursorUtil.getColumnIndexOrThrow(_cursor, "modelType");
          final int _cursorIndexOfQuantization = CursorUtil.getColumnIndexOrThrow(_cursor, "quantization");
          final int _cursorIndexOfContextLength = CursorUtil.getColumnIndexOrThrow(_cursor, "contextLength");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefault");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isDownloaded");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastUsedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsedAt");
          final AIModelEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpModelType;
            _tmpModelType = _cursor.getString(_cursorIndexOfModelType);
            final String _tmpQuantization;
            if (_cursor.isNull(_cursorIndexOfQuantization)) {
              _tmpQuantization = null;
            } else {
              _tmpQuantization = _cursor.getString(_cursorIndexOfQuantization);
            }
            final int _tmpContextLength;
            _tmpContextLength = _cursor.getInt(_cursorIndexOfContextLength);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadUrl;
            if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
              _tmpDownloadUrl = null;
            } else {
              _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
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
            final LocalDateTime _tmpLastUsedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfLastUsedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfLastUsedAt);
            }
            _tmpLastUsedAt = __converters.toLocalDateTime(_tmp_4);
            _result = new AIModelEntity(_tmpId,_tmpName,_tmpDescription,_tmpFilePath,_tmpSizeBytes,_tmpModelType,_tmpQuantization,_tmpContextLength,_tmpIsDefault,_tmpIsDownloaded,_tmpDownloadUrl,_tmpCreatedAt,_tmpLastUsedAt);
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
