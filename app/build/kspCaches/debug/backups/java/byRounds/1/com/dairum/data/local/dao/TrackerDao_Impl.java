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
import com.dairum.data.local.entity.CustomTrackerEntity;
import com.dairum.data.local.entity.TrackerType;
import com.dairum.data.local.entity.TrackerValueEntity;
import java.lang.Class;
import java.lang.Double;
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
public final class TrackerDao_Impl implements TrackerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CustomTrackerEntity> __insertionAdapterOfCustomTrackerEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<TrackerValueEntity> __insertionAdapterOfTrackerValueEntity;

  private final EntityDeletionOrUpdateAdapter<CustomTrackerEntity> __deletionAdapterOfCustomTrackerEntity;

  private final EntityDeletionOrUpdateAdapter<TrackerValueEntity> __deletionAdapterOfTrackerValueEntity;

  private final EntityDeletionOrUpdateAdapter<CustomTrackerEntity> __updateAdapterOfCustomTrackerEntity;

  private final EntityDeletionOrUpdateAdapter<TrackerValueEntity> __updateAdapterOfTrackerValueEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTrackerById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTrackerActive;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTrackerSortOrder;

  private final SharedSQLiteStatement __preparedStmtOfDeleteValueById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllValuesForEntry;

  private final SharedSQLiteStatement __preparedStmtOfDeleteValueForEntryAndTracker;

  public TrackerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCustomTrackerEntity = new EntityInsertionAdapter<CustomTrackerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `custom_trackers` (`id`,`name`,`type`,`icon`,`unit`,`minValue`,`maxValue`,`options`,`isBuiltIn`,`isActive`,`sortOrder`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CustomTrackerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        final String _tmp = __converters.fromTrackerType(entity.getType());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getIcon());
        if (entity.getUnit() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUnit());
        }
        if (entity.getMinValue() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getMinValue());
        }
        if (entity.getMaxValue() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getMaxValue());
        }
        if (entity.getOptions() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getOptions());
        }
        final int _tmp_1 = entity.isBuiltIn() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final int _tmp_2 = entity.isActive() ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        statement.bindLong(11, entity.getSortOrder());
        statement.bindLong(12, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfTrackerValueEntity = new EntityInsertionAdapter<TrackerValueEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tracker_values` (`id`,`entryId`,`trackerId`,`value`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrackerValueEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getEntryId());
        statement.bindLong(3, entity.getTrackerId());
        statement.bindString(4, entity.getValue());
        statement.bindLong(5, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfCustomTrackerEntity = new EntityDeletionOrUpdateAdapter<CustomTrackerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `custom_trackers` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CustomTrackerEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfTrackerValueEntity = new EntityDeletionOrUpdateAdapter<TrackerValueEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tracker_values` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrackerValueEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCustomTrackerEntity = new EntityDeletionOrUpdateAdapter<CustomTrackerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `custom_trackers` SET `id` = ?,`name` = ?,`type` = ?,`icon` = ?,`unit` = ?,`minValue` = ?,`maxValue` = ?,`options` = ?,`isBuiltIn` = ?,`isActive` = ?,`sortOrder` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CustomTrackerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        final String _tmp = __converters.fromTrackerType(entity.getType());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getIcon());
        if (entity.getUnit() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUnit());
        }
        if (entity.getMinValue() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getMinValue());
        }
        if (entity.getMaxValue() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getMaxValue());
        }
        if (entity.getOptions() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getOptions());
        }
        final int _tmp_1 = entity.isBuiltIn() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final int _tmp_2 = entity.isActive() ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        statement.bindLong(11, entity.getSortOrder());
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getId());
      }
    };
    this.__updateAdapterOfTrackerValueEntity = new EntityDeletionOrUpdateAdapter<TrackerValueEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tracker_values` SET `id` = ?,`entryId` = ?,`trackerId` = ?,`value` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrackerValueEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getEntryId());
        statement.bindLong(3, entity.getTrackerId());
        statement.bindString(4, entity.getValue());
        statement.bindLong(5, entity.getCreatedAt());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteTrackerById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM custom_trackers WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTrackerActive = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE custom_trackers SET isActive = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTrackerSortOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE custom_trackers SET sortOrder = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteValueById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tracker_values WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllValuesForEntry = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tracker_values WHERE entryId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteValueForEntryAndTracker = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tracker_values WHERE entryId = ? AND trackerId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTracker(final CustomTrackerEntity tracker,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCustomTrackerEntity.insertAndReturnId(tracker);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertValue(final TrackerValueEntity value,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTrackerValueEntity.insertAndReturnId(value);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertValues(final List<TrackerValueEntity> values,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfTrackerValueEntity.insertAndReturnIdsList(values);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTracker(final CustomTrackerEntity tracker,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCustomTrackerEntity.handle(tracker);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteValue(final TrackerValueEntity value,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTrackerValueEntity.handle(value);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTracker(final CustomTrackerEntity tracker,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCustomTrackerEntity.handle(tracker);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateValue(final TrackerValueEntity value,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTrackerValueEntity.handle(value);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTrackerById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTrackerById.acquire();
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
          __preparedStmtOfDeleteTrackerById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTrackerActive(final long id, final boolean isActive,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTrackerActive.acquire();
        int _argIndex = 1;
        final int _tmp = isActive ? 1 : 0;
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
          __preparedStmtOfUpdateTrackerActive.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTrackerSortOrder(final long id, final int sortOrder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTrackerSortOrder.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, sortOrder);
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
          __preparedStmtOfUpdateTrackerSortOrder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteValueById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteValueById.acquire();
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
          __preparedStmtOfDeleteValueById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllValuesForEntry(final long entryId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllValuesForEntry.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, entryId);
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
          __preparedStmtOfDeleteAllValuesForEntry.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteValueForEntryAndTracker(final long entryId, final long trackerId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteValueForEntryAndTracker.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, entryId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, trackerId);
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
          __preparedStmtOfDeleteValueForEntryAndTracker.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getTrackerById(final long id,
      final Continuation<? super CustomTrackerEntity> $completion) {
    final String _sql = "SELECT * FROM custom_trackers WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CustomTrackerEntity>() {
      @Override
      @Nullable
      public CustomTrackerEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinValue = CursorUtil.getColumnIndexOrThrow(_cursor, "minValue");
          final int _cursorIndexOfMaxValue = CursorUtil.getColumnIndexOrThrow(_cursor, "maxValue");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfIsBuiltIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isBuiltIn");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final CustomTrackerEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final TrackerType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toTrackerType(_tmp);
            final String _tmpIcon;
            _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final Double _tmpMinValue;
            if (_cursor.isNull(_cursorIndexOfMinValue)) {
              _tmpMinValue = null;
            } else {
              _tmpMinValue = _cursor.getDouble(_cursorIndexOfMinValue);
            }
            final Double _tmpMaxValue;
            if (_cursor.isNull(_cursorIndexOfMaxValue)) {
              _tmpMaxValue = null;
            } else {
              _tmpMaxValue = _cursor.getDouble(_cursorIndexOfMaxValue);
            }
            final String _tmpOptions;
            if (_cursor.isNull(_cursorIndexOfOptions)) {
              _tmpOptions = null;
            } else {
              _tmpOptions = _cursor.getString(_cursorIndexOfOptions);
            }
            final boolean _tmpIsBuiltIn;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBuiltIn);
            _tmpIsBuiltIn = _tmp_1 != 0;
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new CustomTrackerEntity(_tmpId,_tmpName,_tmpType,_tmpIcon,_tmpUnit,_tmpMinValue,_tmpMaxValue,_tmpOptions,_tmpIsBuiltIn,_tmpIsActive,_tmpSortOrder,_tmpCreatedAt);
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
  public Flow<List<CustomTrackerEntity>> getActiveTrackersFlow() {
    final String _sql = "SELECT * FROM custom_trackers WHERE isActive = 1 ORDER BY sortOrder ASC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"custom_trackers"}, new Callable<List<CustomTrackerEntity>>() {
      @Override
      @NonNull
      public List<CustomTrackerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinValue = CursorUtil.getColumnIndexOrThrow(_cursor, "minValue");
          final int _cursorIndexOfMaxValue = CursorUtil.getColumnIndexOrThrow(_cursor, "maxValue");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfIsBuiltIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isBuiltIn");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CustomTrackerEntity> _result = new ArrayList<CustomTrackerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CustomTrackerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final TrackerType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toTrackerType(_tmp);
            final String _tmpIcon;
            _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final Double _tmpMinValue;
            if (_cursor.isNull(_cursorIndexOfMinValue)) {
              _tmpMinValue = null;
            } else {
              _tmpMinValue = _cursor.getDouble(_cursorIndexOfMinValue);
            }
            final Double _tmpMaxValue;
            if (_cursor.isNull(_cursorIndexOfMaxValue)) {
              _tmpMaxValue = null;
            } else {
              _tmpMaxValue = _cursor.getDouble(_cursorIndexOfMaxValue);
            }
            final String _tmpOptions;
            if (_cursor.isNull(_cursorIndexOfOptions)) {
              _tmpOptions = null;
            } else {
              _tmpOptions = _cursor.getString(_cursorIndexOfOptions);
            }
            final boolean _tmpIsBuiltIn;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBuiltIn);
            _tmpIsBuiltIn = _tmp_1 != 0;
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CustomTrackerEntity(_tmpId,_tmpName,_tmpType,_tmpIcon,_tmpUnit,_tmpMinValue,_tmpMaxValue,_tmpOptions,_tmpIsBuiltIn,_tmpIsActive,_tmpSortOrder,_tmpCreatedAt);
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
  public Flow<List<CustomTrackerEntity>> getAllTrackersFlow() {
    final String _sql = "SELECT * FROM custom_trackers ORDER BY sortOrder ASC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"custom_trackers"}, new Callable<List<CustomTrackerEntity>>() {
      @Override
      @NonNull
      public List<CustomTrackerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinValue = CursorUtil.getColumnIndexOrThrow(_cursor, "minValue");
          final int _cursorIndexOfMaxValue = CursorUtil.getColumnIndexOrThrow(_cursor, "maxValue");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfIsBuiltIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isBuiltIn");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CustomTrackerEntity> _result = new ArrayList<CustomTrackerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CustomTrackerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final TrackerType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toTrackerType(_tmp);
            final String _tmpIcon;
            _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final Double _tmpMinValue;
            if (_cursor.isNull(_cursorIndexOfMinValue)) {
              _tmpMinValue = null;
            } else {
              _tmpMinValue = _cursor.getDouble(_cursorIndexOfMinValue);
            }
            final Double _tmpMaxValue;
            if (_cursor.isNull(_cursorIndexOfMaxValue)) {
              _tmpMaxValue = null;
            } else {
              _tmpMaxValue = _cursor.getDouble(_cursorIndexOfMaxValue);
            }
            final String _tmpOptions;
            if (_cursor.isNull(_cursorIndexOfOptions)) {
              _tmpOptions = null;
            } else {
              _tmpOptions = _cursor.getString(_cursorIndexOfOptions);
            }
            final boolean _tmpIsBuiltIn;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBuiltIn);
            _tmpIsBuiltIn = _tmp_1 != 0;
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CustomTrackerEntity(_tmpId,_tmpName,_tmpType,_tmpIcon,_tmpUnit,_tmpMinValue,_tmpMaxValue,_tmpOptions,_tmpIsBuiltIn,_tmpIsActive,_tmpSortOrder,_tmpCreatedAt);
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
  public Object getAllTrackers(final Continuation<? super List<CustomTrackerEntity>> $completion) {
    final String _sql = "SELECT * FROM custom_trackers ORDER BY sortOrder ASC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CustomTrackerEntity>>() {
      @Override
      @NonNull
      public List<CustomTrackerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinValue = CursorUtil.getColumnIndexOrThrow(_cursor, "minValue");
          final int _cursorIndexOfMaxValue = CursorUtil.getColumnIndexOrThrow(_cursor, "maxValue");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfIsBuiltIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isBuiltIn");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CustomTrackerEntity> _result = new ArrayList<CustomTrackerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CustomTrackerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final TrackerType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toTrackerType(_tmp);
            final String _tmpIcon;
            _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final Double _tmpMinValue;
            if (_cursor.isNull(_cursorIndexOfMinValue)) {
              _tmpMinValue = null;
            } else {
              _tmpMinValue = _cursor.getDouble(_cursorIndexOfMinValue);
            }
            final Double _tmpMaxValue;
            if (_cursor.isNull(_cursorIndexOfMaxValue)) {
              _tmpMaxValue = null;
            } else {
              _tmpMaxValue = _cursor.getDouble(_cursorIndexOfMaxValue);
            }
            final String _tmpOptions;
            if (_cursor.isNull(_cursorIndexOfOptions)) {
              _tmpOptions = null;
            } else {
              _tmpOptions = _cursor.getString(_cursorIndexOfOptions);
            }
            final boolean _tmpIsBuiltIn;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBuiltIn);
            _tmpIsBuiltIn = _tmp_1 != 0;
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CustomTrackerEntity(_tmpId,_tmpName,_tmpType,_tmpIcon,_tmpUnit,_tmpMinValue,_tmpMaxValue,_tmpOptions,_tmpIsBuiltIn,_tmpIsActive,_tmpSortOrder,_tmpCreatedAt);
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
  public Object getActiveTrackers(
      final Continuation<? super List<CustomTrackerEntity>> $completion) {
    final String _sql = "SELECT * FROM custom_trackers WHERE isActive = 1 ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CustomTrackerEntity>>() {
      @Override
      @NonNull
      public List<CustomTrackerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinValue = CursorUtil.getColumnIndexOrThrow(_cursor, "minValue");
          final int _cursorIndexOfMaxValue = CursorUtil.getColumnIndexOrThrow(_cursor, "maxValue");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfIsBuiltIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isBuiltIn");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CustomTrackerEntity> _result = new ArrayList<CustomTrackerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CustomTrackerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final TrackerType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toTrackerType(_tmp);
            final String _tmpIcon;
            _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final Double _tmpMinValue;
            if (_cursor.isNull(_cursorIndexOfMinValue)) {
              _tmpMinValue = null;
            } else {
              _tmpMinValue = _cursor.getDouble(_cursorIndexOfMinValue);
            }
            final Double _tmpMaxValue;
            if (_cursor.isNull(_cursorIndexOfMaxValue)) {
              _tmpMaxValue = null;
            } else {
              _tmpMaxValue = _cursor.getDouble(_cursorIndexOfMaxValue);
            }
            final String _tmpOptions;
            if (_cursor.isNull(_cursorIndexOfOptions)) {
              _tmpOptions = null;
            } else {
              _tmpOptions = _cursor.getString(_cursorIndexOfOptions);
            }
            final boolean _tmpIsBuiltIn;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBuiltIn);
            _tmpIsBuiltIn = _tmp_1 != 0;
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CustomTrackerEntity(_tmpId,_tmpName,_tmpType,_tmpIcon,_tmpUnit,_tmpMinValue,_tmpMaxValue,_tmpOptions,_tmpIsBuiltIn,_tmpIsActive,_tmpSortOrder,_tmpCreatedAt);
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
  public Object getTrackersByType(final TrackerType type,
      final Continuation<? super List<CustomTrackerEntity>> $completion) {
    final String _sql = "SELECT * FROM custom_trackers WHERE type = ? AND isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromTrackerType(type);
    _statement.bindString(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CustomTrackerEntity>>() {
      @Override
      @NonNull
      public List<CustomTrackerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinValue = CursorUtil.getColumnIndexOrThrow(_cursor, "minValue");
          final int _cursorIndexOfMaxValue = CursorUtil.getColumnIndexOrThrow(_cursor, "maxValue");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfIsBuiltIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isBuiltIn");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CustomTrackerEntity> _result = new ArrayList<CustomTrackerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CustomTrackerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final TrackerType _tmpType;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toTrackerType(_tmp_1);
            final String _tmpIcon;
            _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final Double _tmpMinValue;
            if (_cursor.isNull(_cursorIndexOfMinValue)) {
              _tmpMinValue = null;
            } else {
              _tmpMinValue = _cursor.getDouble(_cursorIndexOfMinValue);
            }
            final Double _tmpMaxValue;
            if (_cursor.isNull(_cursorIndexOfMaxValue)) {
              _tmpMaxValue = null;
            } else {
              _tmpMaxValue = _cursor.getDouble(_cursorIndexOfMaxValue);
            }
            final String _tmpOptions;
            if (_cursor.isNull(_cursorIndexOfOptions)) {
              _tmpOptions = null;
            } else {
              _tmpOptions = _cursor.getString(_cursorIndexOfOptions);
            }
            final boolean _tmpIsBuiltIn;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsBuiltIn);
            _tmpIsBuiltIn = _tmp_2 != 0;
            final boolean _tmpIsActive;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_3 != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CustomTrackerEntity(_tmpId,_tmpName,_tmpType,_tmpIcon,_tmpUnit,_tmpMinValue,_tmpMaxValue,_tmpOptions,_tmpIsBuiltIn,_tmpIsActive,_tmpSortOrder,_tmpCreatedAt);
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
  public Object getMaxTrackerSortOrder(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT MAX(sortOrder) FROM custom_trackers";
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
  public Object getValuesForEntry(final long entryId,
      final Continuation<? super List<TrackerValueEntity>> $completion) {
    final String _sql = "SELECT * FROM tracker_values WHERE entryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, entryId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TrackerValueEntity>>() {
      @Override
      @NonNull
      public List<TrackerValueEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "entryId");
          final int _cursorIndexOfTrackerId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TrackerValueEntity> _result = new ArrayList<TrackerValueEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrackerValueEntity _item;
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
            _item = new TrackerValueEntity(_tmpId,_tmpEntryId,_tmpTrackerId,_tmpValue,_tmpCreatedAt);
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
  public Flow<List<TrackerValueEntity>> getValuesForEntryFlow(final long entryId) {
    final String _sql = "SELECT * FROM tracker_values WHERE entryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, entryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tracker_values"}, new Callable<List<TrackerValueEntity>>() {
      @Override
      @NonNull
      public List<TrackerValueEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "entryId");
          final int _cursorIndexOfTrackerId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TrackerValueEntity> _result = new ArrayList<TrackerValueEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrackerValueEntity _item;
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
            _item = new TrackerValueEntity(_tmpId,_tmpEntryId,_tmpTrackerId,_tmpValue,_tmpCreatedAt);
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
  public Object getValuesForTracker(final long trackerId,
      final Continuation<? super List<TrackerValueEntity>> $completion) {
    final String _sql = "SELECT * FROM tracker_values WHERE trackerId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, trackerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TrackerValueEntity>>() {
      @Override
      @NonNull
      public List<TrackerValueEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "entryId");
          final int _cursorIndexOfTrackerId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TrackerValueEntity> _result = new ArrayList<TrackerValueEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrackerValueEntity _item;
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
            _item = new TrackerValueEntity(_tmpId,_tmpEntryId,_tmpTrackerId,_tmpValue,_tmpCreatedAt);
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
  public Flow<List<TrackerValueEntity>> getValuesForTrackerFlow(final long trackerId) {
    final String _sql = "SELECT * FROM tracker_values WHERE trackerId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, trackerId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tracker_values"}, new Callable<List<TrackerValueEntity>>() {
      @Override
      @NonNull
      public List<TrackerValueEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "entryId");
          final int _cursorIndexOfTrackerId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TrackerValueEntity> _result = new ArrayList<TrackerValueEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrackerValueEntity _item;
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
            _item = new TrackerValueEntity(_tmpId,_tmpEntryId,_tmpTrackerId,_tmpValue,_tmpCreatedAt);
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
  public Object getTrackerValuesWithEntryDate(final long trackerId,
      final Continuation<? super List<TrackerValueEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT tv.* FROM tracker_values tv\n"
            + "        INNER JOIN journal_entries e ON tv.entryId = e.id\n"
            + "        WHERE tv.trackerId = ? AND e.isArchived = 0 AND e.isDraft = 0\n"
            + "        ORDER BY e.createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, trackerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TrackerValueEntity>>() {
      @Override
      @NonNull
      public List<TrackerValueEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "entryId");
          final int _cursorIndexOfTrackerId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TrackerValueEntity> _result = new ArrayList<TrackerValueEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrackerValueEntity _item;
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
            _item = new TrackerValueEntity(_tmpId,_tmpEntryId,_tmpTrackerId,_tmpValue,_tmpCreatedAt);
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
  public Object getValueForEntryAndTracker(final long entryId, final long trackerId,
      final Continuation<? super TrackerValueEntity> $completion) {
    final String _sql = "SELECT * FROM tracker_values WHERE entryId = ? AND trackerId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, entryId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, trackerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TrackerValueEntity>() {
      @Override
      @Nullable
      public TrackerValueEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "entryId");
          final int _cursorIndexOfTrackerId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final TrackerValueEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new TrackerValueEntity(_tmpId,_tmpEntryId,_tmpTrackerId,_tmpValue,_tmpCreatedAt);
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
  public Object getAverageValue(final long trackerId,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT AVG(CAST(value AS REAL)) FROM tracker_values \n"
            + "        WHERE trackerId = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, trackerId);
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
  public Object getMinValue(final long trackerId, final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT MIN(CAST(value AS REAL)) FROM tracker_values \n"
            + "        WHERE trackerId = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, trackerId);
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
  public Object getMaxValue(final long trackerId, final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT MAX(CAST(value AS REAL)) FROM tracker_values \n"
            + "        WHERE trackerId = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, trackerId);
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
  public Object getValueCount(final long trackerId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM tracker_values WHERE trackerId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, trackerId);
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
}
