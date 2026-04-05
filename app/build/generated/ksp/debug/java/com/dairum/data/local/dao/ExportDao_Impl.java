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
import com.dairum.data.local.entity.ExportConfigEntity;
import com.dairum.data.local.entity.ExportFormat;
import com.dairum.data.local.entity.ExportHistoryEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class ExportDao_Impl implements ExportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExportConfigEntity> __insertionAdapterOfExportConfigEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<ExportHistoryEntity> __insertionAdapterOfExportHistoryEntity;

  private final EntityDeletionOrUpdateAdapter<ExportConfigEntity> __deletionAdapterOfExportConfigEntity;

  private final EntityDeletionOrUpdateAdapter<ExportHistoryEntity> __deletionAdapterOfExportHistoryEntity;

  private final EntityDeletionOrUpdateAdapter<ExportConfigEntity> __updateAdapterOfExportConfigEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateConfigEnabled;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastExport;

  private final SharedSQLiteStatement __preparedStmtOfDeleteHistoryById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteHistoryBefore;

  public ExportDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExportConfigEntity = new EntityInsertionAdapter<ExportConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `export_configs` (`id`,`name`,`format`,`scheduleHour`,`scheduleMinute`,`scheduleDays`,`isEnabled`,`exportPath`,`includeMedia`,`lastExportAt`,`lastExportStatus`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExportConfigEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        final String _tmp = __converters.fromExportFormat(entity.getFormat());
        statement.bindString(3, _tmp);
        statement.bindLong(4, entity.getScheduleHour());
        statement.bindLong(5, entity.getScheduleMinute());
        statement.bindString(6, entity.getScheduleDays());
        final int _tmp_1 = entity.isEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getExportPath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getExportPath());
        }
        final int _tmp_2 = entity.getIncludeMedia() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        if (entity.getLastExportAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getLastExportAt());
        }
        if (entity.getLastExportStatus() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getLastExportStatus());
        }
        statement.bindLong(12, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfExportHistoryEntity = new EntityInsertionAdapter<ExportHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `export_history` (`id`,`configId`,`format`,`filePath`,`fileName`,`entriesCount`,`fileSize`,`isSuccess`,`errorMessage`,`exportedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExportHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getConfigId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getConfigId());
        }
        final String _tmp = __converters.fromExportFormat(entity.getFormat());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getFilePath());
        statement.bindString(5, entity.getFileName());
        statement.bindLong(6, entity.getEntriesCount());
        statement.bindLong(7, entity.getFileSize());
        final int _tmp_1 = entity.isSuccess() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getErrorMessage() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getErrorMessage());
        }
        statement.bindLong(10, entity.getExportedAt());
      }
    };
    this.__deletionAdapterOfExportConfigEntity = new EntityDeletionOrUpdateAdapter<ExportConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `export_configs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExportConfigEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfExportHistoryEntity = new EntityDeletionOrUpdateAdapter<ExportHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `export_history` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExportHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExportConfigEntity = new EntityDeletionOrUpdateAdapter<ExportConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `export_configs` SET `id` = ?,`name` = ?,`format` = ?,`scheduleHour` = ?,`scheduleMinute` = ?,`scheduleDays` = ?,`isEnabled` = ?,`exportPath` = ?,`includeMedia` = ?,`lastExportAt` = ?,`lastExportStatus` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExportConfigEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        final String _tmp = __converters.fromExportFormat(entity.getFormat());
        statement.bindString(3, _tmp);
        statement.bindLong(4, entity.getScheduleHour());
        statement.bindLong(5, entity.getScheduleMinute());
        statement.bindString(6, entity.getScheduleDays());
        final int _tmp_1 = entity.isEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getExportPath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getExportPath());
        }
        final int _tmp_2 = entity.getIncludeMedia() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        if (entity.getLastExportAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getLastExportAt());
        }
        if (entity.getLastExportStatus() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getLastExportStatus());
        }
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateConfigEnabled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE export_configs SET isEnabled = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastExport = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE export_configs SET lastExportAt = ?, lastExportStatus = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteHistoryById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM export_history WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteHistoryBefore = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM export_history WHERE exportedAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertConfig(final ExportConfigEntity config,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExportConfigEntity.insertAndReturnId(config);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertHistory(final ExportHistoryEntity history,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExportHistoryEntity.insertAndReturnId(history);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteConfig(final ExportConfigEntity config,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExportConfigEntity.handle(config);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHistory(final ExportHistoryEntity history,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExportHistoryEntity.handle(history);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateConfig(final ExportConfigEntity config,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExportConfigEntity.handle(config);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateConfigEnabled(final long id, final boolean isEnabled,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateConfigEnabled.acquire();
        int _argIndex = 1;
        final int _tmp = isEnabled ? 1 : 0;
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
          __preparedStmtOfUpdateConfigEnabled.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastExport(final long id, final long timestamp, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastExport.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, status);
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
          __preparedStmtOfUpdateLastExport.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHistoryById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteHistoryById.acquire();
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
          __preparedStmtOfDeleteHistoryById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHistoryBefore(final long beforeTimestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteHistoryBefore.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, beforeTimestamp);
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
          __preparedStmtOfDeleteHistoryBefore.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getConfigById(final long id,
      final Continuation<? super ExportConfigEntity> $completion) {
    final String _sql = "SELECT * FROM export_configs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExportConfigEntity>() {
      @Override
      @Nullable
      public ExportConfigEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfScheduleHour = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleHour");
          final int _cursorIndexOfScheduleMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleMinute");
          final int _cursorIndexOfScheduleDays = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleDays");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfExportPath = CursorUtil.getColumnIndexOrThrow(_cursor, "exportPath");
          final int _cursorIndexOfIncludeMedia = CursorUtil.getColumnIndexOrThrow(_cursor, "includeMedia");
          final int _cursorIndexOfLastExportAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExportAt");
          final int _cursorIndexOfLastExportStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExportStatus");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final ExportConfigEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final ExportFormat _tmpFormat;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFormat);
            _tmpFormat = __converters.toExportFormat(_tmp);
            final int _tmpScheduleHour;
            _tmpScheduleHour = _cursor.getInt(_cursorIndexOfScheduleHour);
            final int _tmpScheduleMinute;
            _tmpScheduleMinute = _cursor.getInt(_cursorIndexOfScheduleMinute);
            final String _tmpScheduleDays;
            _tmpScheduleDays = _cursor.getString(_cursorIndexOfScheduleDays);
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final String _tmpExportPath;
            if (_cursor.isNull(_cursorIndexOfExportPath)) {
              _tmpExportPath = null;
            } else {
              _tmpExportPath = _cursor.getString(_cursorIndexOfExportPath);
            }
            final boolean _tmpIncludeMedia;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIncludeMedia);
            _tmpIncludeMedia = _tmp_2 != 0;
            final Long _tmpLastExportAt;
            if (_cursor.isNull(_cursorIndexOfLastExportAt)) {
              _tmpLastExportAt = null;
            } else {
              _tmpLastExportAt = _cursor.getLong(_cursorIndexOfLastExportAt);
            }
            final String _tmpLastExportStatus;
            if (_cursor.isNull(_cursorIndexOfLastExportStatus)) {
              _tmpLastExportStatus = null;
            } else {
              _tmpLastExportStatus = _cursor.getString(_cursorIndexOfLastExportStatus);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new ExportConfigEntity(_tmpId,_tmpName,_tmpFormat,_tmpScheduleHour,_tmpScheduleMinute,_tmpScheduleDays,_tmpIsEnabled,_tmpExportPath,_tmpIncludeMedia,_tmpLastExportAt,_tmpLastExportStatus,_tmpCreatedAt);
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
  public Flow<List<ExportConfigEntity>> getAllConfigsFlow() {
    final String _sql = "SELECT * FROM export_configs ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"export_configs"}, new Callable<List<ExportConfigEntity>>() {
      @Override
      @NonNull
      public List<ExportConfigEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfScheduleHour = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleHour");
          final int _cursorIndexOfScheduleMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleMinute");
          final int _cursorIndexOfScheduleDays = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleDays");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfExportPath = CursorUtil.getColumnIndexOrThrow(_cursor, "exportPath");
          final int _cursorIndexOfIncludeMedia = CursorUtil.getColumnIndexOrThrow(_cursor, "includeMedia");
          final int _cursorIndexOfLastExportAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExportAt");
          final int _cursorIndexOfLastExportStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExportStatus");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ExportConfigEntity> _result = new ArrayList<ExportConfigEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExportConfigEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final ExportFormat _tmpFormat;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFormat);
            _tmpFormat = __converters.toExportFormat(_tmp);
            final int _tmpScheduleHour;
            _tmpScheduleHour = _cursor.getInt(_cursorIndexOfScheduleHour);
            final int _tmpScheduleMinute;
            _tmpScheduleMinute = _cursor.getInt(_cursorIndexOfScheduleMinute);
            final String _tmpScheduleDays;
            _tmpScheduleDays = _cursor.getString(_cursorIndexOfScheduleDays);
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final String _tmpExportPath;
            if (_cursor.isNull(_cursorIndexOfExportPath)) {
              _tmpExportPath = null;
            } else {
              _tmpExportPath = _cursor.getString(_cursorIndexOfExportPath);
            }
            final boolean _tmpIncludeMedia;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIncludeMedia);
            _tmpIncludeMedia = _tmp_2 != 0;
            final Long _tmpLastExportAt;
            if (_cursor.isNull(_cursorIndexOfLastExportAt)) {
              _tmpLastExportAt = null;
            } else {
              _tmpLastExportAt = _cursor.getLong(_cursorIndexOfLastExportAt);
            }
            final String _tmpLastExportStatus;
            if (_cursor.isNull(_cursorIndexOfLastExportStatus)) {
              _tmpLastExportStatus = null;
            } else {
              _tmpLastExportStatus = _cursor.getString(_cursorIndexOfLastExportStatus);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ExportConfigEntity(_tmpId,_tmpName,_tmpFormat,_tmpScheduleHour,_tmpScheduleMinute,_tmpScheduleDays,_tmpIsEnabled,_tmpExportPath,_tmpIncludeMedia,_tmpLastExportAt,_tmpLastExportStatus,_tmpCreatedAt);
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
  public Object getAllConfigs(final Continuation<? super List<ExportConfigEntity>> $completion) {
    final String _sql = "SELECT * FROM export_configs ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExportConfigEntity>>() {
      @Override
      @NonNull
      public List<ExportConfigEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfScheduleHour = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleHour");
          final int _cursorIndexOfScheduleMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleMinute");
          final int _cursorIndexOfScheduleDays = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleDays");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfExportPath = CursorUtil.getColumnIndexOrThrow(_cursor, "exportPath");
          final int _cursorIndexOfIncludeMedia = CursorUtil.getColumnIndexOrThrow(_cursor, "includeMedia");
          final int _cursorIndexOfLastExportAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExportAt");
          final int _cursorIndexOfLastExportStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExportStatus");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ExportConfigEntity> _result = new ArrayList<ExportConfigEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExportConfigEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final ExportFormat _tmpFormat;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFormat);
            _tmpFormat = __converters.toExportFormat(_tmp);
            final int _tmpScheduleHour;
            _tmpScheduleHour = _cursor.getInt(_cursorIndexOfScheduleHour);
            final int _tmpScheduleMinute;
            _tmpScheduleMinute = _cursor.getInt(_cursorIndexOfScheduleMinute);
            final String _tmpScheduleDays;
            _tmpScheduleDays = _cursor.getString(_cursorIndexOfScheduleDays);
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final String _tmpExportPath;
            if (_cursor.isNull(_cursorIndexOfExportPath)) {
              _tmpExportPath = null;
            } else {
              _tmpExportPath = _cursor.getString(_cursorIndexOfExportPath);
            }
            final boolean _tmpIncludeMedia;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIncludeMedia);
            _tmpIncludeMedia = _tmp_2 != 0;
            final Long _tmpLastExportAt;
            if (_cursor.isNull(_cursorIndexOfLastExportAt)) {
              _tmpLastExportAt = null;
            } else {
              _tmpLastExportAt = _cursor.getLong(_cursorIndexOfLastExportAt);
            }
            final String _tmpLastExportStatus;
            if (_cursor.isNull(_cursorIndexOfLastExportStatus)) {
              _tmpLastExportStatus = null;
            } else {
              _tmpLastExportStatus = _cursor.getString(_cursorIndexOfLastExportStatus);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ExportConfigEntity(_tmpId,_tmpName,_tmpFormat,_tmpScheduleHour,_tmpScheduleMinute,_tmpScheduleDays,_tmpIsEnabled,_tmpExportPath,_tmpIncludeMedia,_tmpLastExportAt,_tmpLastExportStatus,_tmpCreatedAt);
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
  public Object getEnabledConfigs(
      final Continuation<? super List<ExportConfigEntity>> $completion) {
    final String _sql = "SELECT * FROM export_configs WHERE isEnabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExportConfigEntity>>() {
      @Override
      @NonNull
      public List<ExportConfigEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfScheduleHour = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleHour");
          final int _cursorIndexOfScheduleMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleMinute");
          final int _cursorIndexOfScheduleDays = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleDays");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfExportPath = CursorUtil.getColumnIndexOrThrow(_cursor, "exportPath");
          final int _cursorIndexOfIncludeMedia = CursorUtil.getColumnIndexOrThrow(_cursor, "includeMedia");
          final int _cursorIndexOfLastExportAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExportAt");
          final int _cursorIndexOfLastExportStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExportStatus");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ExportConfigEntity> _result = new ArrayList<ExportConfigEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExportConfigEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final ExportFormat _tmpFormat;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFormat);
            _tmpFormat = __converters.toExportFormat(_tmp);
            final int _tmpScheduleHour;
            _tmpScheduleHour = _cursor.getInt(_cursorIndexOfScheduleHour);
            final int _tmpScheduleMinute;
            _tmpScheduleMinute = _cursor.getInt(_cursorIndexOfScheduleMinute);
            final String _tmpScheduleDays;
            _tmpScheduleDays = _cursor.getString(_cursorIndexOfScheduleDays);
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final String _tmpExportPath;
            if (_cursor.isNull(_cursorIndexOfExportPath)) {
              _tmpExportPath = null;
            } else {
              _tmpExportPath = _cursor.getString(_cursorIndexOfExportPath);
            }
            final boolean _tmpIncludeMedia;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIncludeMedia);
            _tmpIncludeMedia = _tmp_2 != 0;
            final Long _tmpLastExportAt;
            if (_cursor.isNull(_cursorIndexOfLastExportAt)) {
              _tmpLastExportAt = null;
            } else {
              _tmpLastExportAt = _cursor.getLong(_cursorIndexOfLastExportAt);
            }
            final String _tmpLastExportStatus;
            if (_cursor.isNull(_cursorIndexOfLastExportStatus)) {
              _tmpLastExportStatus = null;
            } else {
              _tmpLastExportStatus = _cursor.getString(_cursorIndexOfLastExportStatus);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ExportConfigEntity(_tmpId,_tmpName,_tmpFormat,_tmpScheduleHour,_tmpScheduleMinute,_tmpScheduleDays,_tmpIsEnabled,_tmpExportPath,_tmpIncludeMedia,_tmpLastExportAt,_tmpLastExportStatus,_tmpCreatedAt);
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
  public Flow<List<ExportHistoryEntity>> getAllHistoryFlow() {
    final String _sql = "SELECT * FROM export_history ORDER BY exportedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"export_history"}, new Callable<List<ExportHistoryEntity>>() {
      @Override
      @NonNull
      public List<ExportHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConfigId = CursorUtil.getColumnIndexOrThrow(_cursor, "configId");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfEntriesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entriesCount");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfIsSuccess = CursorUtil.getColumnIndexOrThrow(_cursor, "isSuccess");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final int _cursorIndexOfExportedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "exportedAt");
          final List<ExportHistoryEntity> _result = new ArrayList<ExportHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExportHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpConfigId;
            if (_cursor.isNull(_cursorIndexOfConfigId)) {
              _tmpConfigId = null;
            } else {
              _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
            }
            final ExportFormat _tmpFormat;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFormat);
            _tmpFormat = __converters.toExportFormat(_tmp);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final int _tmpEntriesCount;
            _tmpEntriesCount = _cursor.getInt(_cursorIndexOfEntriesCount);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpIsSuccess;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSuccess);
            _tmpIsSuccess = _tmp_1 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            final long _tmpExportedAt;
            _tmpExportedAt = _cursor.getLong(_cursorIndexOfExportedAt);
            _item = new ExportHistoryEntity(_tmpId,_tmpConfigId,_tmpFormat,_tmpFilePath,_tmpFileName,_tmpEntriesCount,_tmpFileSize,_tmpIsSuccess,_tmpErrorMessage,_tmpExportedAt);
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
  public Object getRecentHistory(final int limit,
      final Continuation<? super List<ExportHistoryEntity>> $completion) {
    final String _sql = "SELECT * FROM export_history ORDER BY exportedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExportHistoryEntity>>() {
      @Override
      @NonNull
      public List<ExportHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConfigId = CursorUtil.getColumnIndexOrThrow(_cursor, "configId");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfEntriesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entriesCount");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfIsSuccess = CursorUtil.getColumnIndexOrThrow(_cursor, "isSuccess");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final int _cursorIndexOfExportedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "exportedAt");
          final List<ExportHistoryEntity> _result = new ArrayList<ExportHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExportHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpConfigId;
            if (_cursor.isNull(_cursorIndexOfConfigId)) {
              _tmpConfigId = null;
            } else {
              _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
            }
            final ExportFormat _tmpFormat;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFormat);
            _tmpFormat = __converters.toExportFormat(_tmp);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final int _tmpEntriesCount;
            _tmpEntriesCount = _cursor.getInt(_cursorIndexOfEntriesCount);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpIsSuccess;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSuccess);
            _tmpIsSuccess = _tmp_1 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            final long _tmpExportedAt;
            _tmpExportedAt = _cursor.getLong(_cursorIndexOfExportedAt);
            _item = new ExportHistoryEntity(_tmpId,_tmpConfigId,_tmpFormat,_tmpFilePath,_tmpFileName,_tmpEntriesCount,_tmpFileSize,_tmpIsSuccess,_tmpErrorMessage,_tmpExportedAt);
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
  public Flow<List<ExportHistoryEntity>> getHistoryForConfigFlow(final long configId) {
    final String _sql = "SELECT * FROM export_history WHERE configId = ? ORDER BY exportedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, configId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"export_history"}, new Callable<List<ExportHistoryEntity>>() {
      @Override
      @NonNull
      public List<ExportHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConfigId = CursorUtil.getColumnIndexOrThrow(_cursor, "configId");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfEntriesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entriesCount");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfIsSuccess = CursorUtil.getColumnIndexOrThrow(_cursor, "isSuccess");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final int _cursorIndexOfExportedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "exportedAt");
          final List<ExportHistoryEntity> _result = new ArrayList<ExportHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExportHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpConfigId;
            if (_cursor.isNull(_cursorIndexOfConfigId)) {
              _tmpConfigId = null;
            } else {
              _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
            }
            final ExportFormat _tmpFormat;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFormat);
            _tmpFormat = __converters.toExportFormat(_tmp);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final int _tmpEntriesCount;
            _tmpEntriesCount = _cursor.getInt(_cursorIndexOfEntriesCount);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpIsSuccess;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSuccess);
            _tmpIsSuccess = _tmp_1 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            final long _tmpExportedAt;
            _tmpExportedAt = _cursor.getLong(_cursorIndexOfExportedAt);
            _item = new ExportHistoryEntity(_tmpId,_tmpConfigId,_tmpFormat,_tmpFilePath,_tmpFileName,_tmpEntriesCount,_tmpFileSize,_tmpIsSuccess,_tmpErrorMessage,_tmpExportedAt);
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
  public Object getLastSuccessfulExport(
      final Continuation<? super ExportHistoryEntity> $completion) {
    final String _sql = "SELECT * FROM export_history WHERE isSuccess = 1 ORDER BY exportedAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExportHistoryEntity>() {
      @Override
      @Nullable
      public ExportHistoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConfigId = CursorUtil.getColumnIndexOrThrow(_cursor, "configId");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfEntriesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entriesCount");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfIsSuccess = CursorUtil.getColumnIndexOrThrow(_cursor, "isSuccess");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final int _cursorIndexOfExportedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "exportedAt");
          final ExportHistoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpConfigId;
            if (_cursor.isNull(_cursorIndexOfConfigId)) {
              _tmpConfigId = null;
            } else {
              _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
            }
            final ExportFormat _tmpFormat;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfFormat);
            _tmpFormat = __converters.toExportFormat(_tmp);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final int _tmpEntriesCount;
            _tmpEntriesCount = _cursor.getInt(_cursorIndexOfEntriesCount);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpIsSuccess;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSuccess);
            _tmpIsSuccess = _tmp_1 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            final long _tmpExportedAt;
            _tmpExportedAt = _cursor.getLong(_cursorIndexOfExportedAt);
            _result = new ExportHistoryEntity(_tmpId,_tmpConfigId,_tmpFormat,_tmpFilePath,_tmpFileName,_tmpEntriesCount,_tmpFileSize,_tmpIsSuccess,_tmpErrorMessage,_tmpExportedAt);
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
  public Object getHistoryCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM export_history";
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
  public Object getTotalExportSize(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(fileSize) FROM export_history WHERE isSuccess = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
