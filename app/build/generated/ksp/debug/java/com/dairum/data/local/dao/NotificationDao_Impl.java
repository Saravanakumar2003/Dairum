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
import com.dairum.data.local.entity.MoodCheckInConfigEntity;
import com.dairum.data.local.entity.NotificationHistoryEntity;
import com.dairum.data.local.entity.ReminderConfigEntity;
import com.dairum.data.local.entity.SmartReminderEntity;
import com.dairum.domain.model.NotificationPriority;
import com.dairum.domain.model.ReminderType;
import com.dairum.domain.model.SmartReminderType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
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
public final class NotificationDao_Impl implements NotificationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReminderConfigEntity> __insertionAdapterOfReminderConfigEntity;

  private final EntityInsertionAdapter<SmartReminderEntity> __insertionAdapterOfSmartReminderEntity;

  private final EntityInsertionAdapter<NotificationHistoryEntity> __insertionAdapterOfNotificationHistoryEntity;

  private final EntityInsertionAdapter<MoodCheckInConfigEntity> __insertionAdapterOfMoodCheckInConfigEntity;

  private final EntityDeletionOrUpdateAdapter<ReminderConfigEntity> __updateAdapterOfReminderConfigEntity;

  private final EntityDeletionOrUpdateAdapter<SmartReminderEntity> __updateAdapterOfSmartReminderEntity;

  private final EntityDeletionOrUpdateAdapter<MoodCheckInConfigEntity> __updateAdapterOfMoodCheckInConfigEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteReminderConfig;

  private final SharedSQLiteStatement __preparedStmtOfSetReminderEnabled;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSmartReminder;

  private final SharedSQLiteStatement __preparedStmtOfSetSmartReminderEnabled;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastNotified;

  private final SharedSQLiteStatement __preparedStmtOfMarkNotificationTapped;

  private final SharedSQLiteStatement __preparedStmtOfClearOldNotificationHistory;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMoodCheckInLastPrompt;

  public NotificationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReminderConfigEntity = new EntityInsertionAdapter<ReminderConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reminder_configs` (`id`,`type`,`name`,`description`,`isEnabled`,`scheduleHour`,`scheduleMinute`,`scheduleDays`,`notificationTitle`,`notificationMessage`,`priority`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReminderConfigEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, __ReminderType_enumToString(entity.getType()));
        statement.bindString(3, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getScheduleHour());
        statement.bindLong(7, entity.getScheduleMinute());
        statement.bindString(8, entity.getScheduleDays());
        statement.bindString(9, entity.getNotificationTitle());
        statement.bindString(10, entity.getNotificationMessage());
        statement.bindString(11, __NotificationPriority_enumToString(entity.getPriority()));
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfSmartReminderEntity = new EntityInsertionAdapter<SmartReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `smart_reminders` (`id`,`reminderType`,`title`,`description`,`reminderDate`,`repeatYearly`,`relatedPersonId`,`relatedEntryId`,`isEnabled`,`notifyDaysBefore`,`lastNotifiedAt`,`metadata`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SmartReminderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, __SmartReminderType_enumToString(entity.getReminderType()));
        statement.bindString(3, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getReminderDate());
        final int _tmp = entity.getRepeatYearly() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getRelatedPersonId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getRelatedPersonId());
        }
        if (entity.getRelatedEntryId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getRelatedEntryId());
        }
        final int _tmp_1 = entity.isEnabled() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getNotifyDaysBefore());
        if (entity.getLastNotifiedAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getLastNotifiedAt());
        }
        if (entity.getMetadata() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getMetadata());
        }
        statement.bindLong(13, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfNotificationHistoryEntity = new EntityInsertionAdapter<NotificationHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `notification_history` (`id`,`type`,`sourceId`,`title`,`message`,`wasTapped`,`sentAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NotificationHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindLong(3, entity.getSourceId());
        statement.bindString(4, entity.getTitle());
        statement.bindString(5, entity.getMessage());
        final int _tmp = entity.getWasTapped() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindLong(7, entity.getSentAt());
      }
    };
    this.__insertionAdapterOfMoodCheckInConfigEntity = new EntityInsertionAdapter<MoodCheckInConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `mood_checkin_config` (`id`,`isEnabled`,`intervalHours`,`startHour`,`endHour`,`lastPromptAt`,`enabledDays`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MoodCheckInConfigEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getIntervalHours());
        statement.bindLong(4, entity.getStartHour());
        statement.bindLong(5, entity.getEndHour());
        if (entity.getLastPromptAt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getLastPromptAt());
        }
        statement.bindString(7, entity.getEnabledDays());
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfReminderConfigEntity = new EntityDeletionOrUpdateAdapter<ReminderConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reminder_configs` SET `id` = ?,`type` = ?,`name` = ?,`description` = ?,`isEnabled` = ?,`scheduleHour` = ?,`scheduleMinute` = ?,`scheduleDays` = ?,`notificationTitle` = ?,`notificationMessage` = ?,`priority` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReminderConfigEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, __ReminderType_enumToString(entity.getType()));
        statement.bindString(3, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getScheduleHour());
        statement.bindLong(7, entity.getScheduleMinute());
        statement.bindString(8, entity.getScheduleDays());
        statement.bindString(9, entity.getNotificationTitle());
        statement.bindString(10, entity.getNotificationMessage());
        statement.bindString(11, __NotificationPriority_enumToString(entity.getPriority()));
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
        statement.bindLong(14, entity.getId());
      }
    };
    this.__updateAdapterOfSmartReminderEntity = new EntityDeletionOrUpdateAdapter<SmartReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `smart_reminders` SET `id` = ?,`reminderType` = ?,`title` = ?,`description` = ?,`reminderDate` = ?,`repeatYearly` = ?,`relatedPersonId` = ?,`relatedEntryId` = ?,`isEnabled` = ?,`notifyDaysBefore` = ?,`lastNotifiedAt` = ?,`metadata` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SmartReminderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, __SmartReminderType_enumToString(entity.getReminderType()));
        statement.bindString(3, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getReminderDate());
        final int _tmp = entity.getRepeatYearly() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getRelatedPersonId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getRelatedPersonId());
        }
        if (entity.getRelatedEntryId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getRelatedEntryId());
        }
        final int _tmp_1 = entity.isEnabled() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getNotifyDaysBefore());
        if (entity.getLastNotifiedAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getLastNotifiedAt());
        }
        if (entity.getMetadata() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getMetadata());
        }
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getId());
      }
    };
    this.__updateAdapterOfMoodCheckInConfigEntity = new EntityDeletionOrUpdateAdapter<MoodCheckInConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `mood_checkin_config` SET `id` = ?,`isEnabled` = ?,`intervalHours` = ?,`startHour` = ?,`endHour` = ?,`lastPromptAt` = ?,`enabledDays` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MoodCheckInConfigEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getIntervalHours());
        statement.bindLong(4, entity.getStartHour());
        statement.bindLong(5, entity.getEndHour());
        if (entity.getLastPromptAt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getLastPromptAt());
        }
        statement.bindString(7, entity.getEnabledDays());
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteReminderConfig = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reminder_configs WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetReminderEnabled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reminder_configs SET isEnabled = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSmartReminder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM smart_reminders WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetSmartReminderEnabled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE smart_reminders SET isEnabled = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastNotified = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE smart_reminders SET lastNotifiedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkNotificationTapped = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notification_history SET wasTapped = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearOldNotificationHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notification_history WHERE sentAt < ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMoodCheckInLastPrompt = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE mood_checkin_config SET lastPromptAt = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertReminderConfig(final ReminderConfigEntity config,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfReminderConfigEntity.insertAndReturnId(config);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSmartReminder(final SmartReminderEntity reminder,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSmartReminderEntity.insertAndReturnId(reminder);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertNotificationHistory(final NotificationHistoryEntity history,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfNotificationHistoryEntity.insertAndReturnId(history);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMoodCheckInConfig(final MoodCheckInConfigEntity config,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMoodCheckInConfigEntity.insertAndReturnId(config);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReminderConfig(final ReminderConfigEntity config,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfReminderConfigEntity.handle(config);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSmartReminder(final SmartReminderEntity reminder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSmartReminderEntity.handle(reminder);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMoodCheckInConfig(final MoodCheckInConfigEntity config,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMoodCheckInConfigEntity.handle(config);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteReminderConfig(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteReminderConfig.acquire();
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
          __preparedStmtOfDeleteReminderConfig.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setReminderEnabled(final long id, final boolean isEnabled, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetReminderEnabled.acquire();
        int _argIndex = 1;
        final int _tmp = isEnabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
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
          __preparedStmtOfSetReminderEnabled.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSmartReminder(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSmartReminder.acquire();
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
          __preparedStmtOfDeleteSmartReminder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setSmartReminderEnabled(final long id, final boolean isEnabled,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetSmartReminderEnabled.acquire();
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
          __preparedStmtOfSetSmartReminderEnabled.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastNotified(final long id, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastNotified.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
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
          __preparedStmtOfUpdateLastNotified.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markNotificationTapped(final long id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkNotificationTapped.acquire();
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
          __preparedStmtOfMarkNotificationTapped.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearOldNotificationHistory(final long beforeTimestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearOldNotificationHistory.acquire();
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
          __preparedStmtOfClearOldNotificationHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMoodCheckInLastPrompt(final long id, final long timestamp,
      final long updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMoodCheckInLastPrompt.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
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
          __preparedStmtOfUpdateMoodCheckInLastPrompt.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getReminderConfigById(final long id,
      final Continuation<? super ReminderConfigEntity> $completion) {
    final String _sql = "SELECT * FROM reminder_configs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReminderConfigEntity>() {
      @Override
      @Nullable
      public ReminderConfigEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfScheduleHour = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleHour");
          final int _cursorIndexOfScheduleMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleMinute");
          final int _cursorIndexOfScheduleDays = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleDays");
          final int _cursorIndexOfNotificationTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationTitle");
          final int _cursorIndexOfNotificationMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationMessage");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ReminderConfigEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final ReminderType _tmpType;
            _tmpType = __ReminderType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final int _tmpScheduleHour;
            _tmpScheduleHour = _cursor.getInt(_cursorIndexOfScheduleHour);
            final int _tmpScheduleMinute;
            _tmpScheduleMinute = _cursor.getInt(_cursorIndexOfScheduleMinute);
            final String _tmpScheduleDays;
            _tmpScheduleDays = _cursor.getString(_cursorIndexOfScheduleDays);
            final String _tmpNotificationTitle;
            _tmpNotificationTitle = _cursor.getString(_cursorIndexOfNotificationTitle);
            final String _tmpNotificationMessage;
            _tmpNotificationMessage = _cursor.getString(_cursorIndexOfNotificationMessage);
            final NotificationPriority _tmpPriority;
            _tmpPriority = __NotificationPriority_stringToEnum(_cursor.getString(_cursorIndexOfPriority));
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ReminderConfigEntity(_tmpId,_tmpType,_tmpName,_tmpDescription,_tmpIsEnabled,_tmpScheduleHour,_tmpScheduleMinute,_tmpScheduleDays,_tmpNotificationTitle,_tmpNotificationMessage,_tmpPriority,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<ReminderConfigEntity>> getAllReminderConfigsFlow() {
    final String _sql = "SELECT * FROM reminder_configs ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reminder_configs"}, new Callable<List<ReminderConfigEntity>>() {
      @Override
      @NonNull
      public List<ReminderConfigEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfScheduleHour = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleHour");
          final int _cursorIndexOfScheduleMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleMinute");
          final int _cursorIndexOfScheduleDays = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleDays");
          final int _cursorIndexOfNotificationTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationTitle");
          final int _cursorIndexOfNotificationMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationMessage");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReminderConfigEntity> _result = new ArrayList<ReminderConfigEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReminderConfigEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final ReminderType _tmpType;
            _tmpType = __ReminderType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final int _tmpScheduleHour;
            _tmpScheduleHour = _cursor.getInt(_cursorIndexOfScheduleHour);
            final int _tmpScheduleMinute;
            _tmpScheduleMinute = _cursor.getInt(_cursorIndexOfScheduleMinute);
            final String _tmpScheduleDays;
            _tmpScheduleDays = _cursor.getString(_cursorIndexOfScheduleDays);
            final String _tmpNotificationTitle;
            _tmpNotificationTitle = _cursor.getString(_cursorIndexOfNotificationTitle);
            final String _tmpNotificationMessage;
            _tmpNotificationMessage = _cursor.getString(_cursorIndexOfNotificationMessage);
            final NotificationPriority _tmpPriority;
            _tmpPriority = __NotificationPriority_stringToEnum(_cursor.getString(_cursorIndexOfPriority));
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ReminderConfigEntity(_tmpId,_tmpType,_tmpName,_tmpDescription,_tmpIsEnabled,_tmpScheduleHour,_tmpScheduleMinute,_tmpScheduleDays,_tmpNotificationTitle,_tmpNotificationMessage,_tmpPriority,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getEnabledReminderConfigs(
      final Continuation<? super List<ReminderConfigEntity>> $completion) {
    final String _sql = "SELECT * FROM reminder_configs WHERE isEnabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReminderConfigEntity>>() {
      @Override
      @NonNull
      public List<ReminderConfigEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfScheduleHour = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleHour");
          final int _cursorIndexOfScheduleMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleMinute");
          final int _cursorIndexOfScheduleDays = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleDays");
          final int _cursorIndexOfNotificationTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationTitle");
          final int _cursorIndexOfNotificationMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationMessage");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReminderConfigEntity> _result = new ArrayList<ReminderConfigEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReminderConfigEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final ReminderType _tmpType;
            _tmpType = __ReminderType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final int _tmpScheduleHour;
            _tmpScheduleHour = _cursor.getInt(_cursorIndexOfScheduleHour);
            final int _tmpScheduleMinute;
            _tmpScheduleMinute = _cursor.getInt(_cursorIndexOfScheduleMinute);
            final String _tmpScheduleDays;
            _tmpScheduleDays = _cursor.getString(_cursorIndexOfScheduleDays);
            final String _tmpNotificationTitle;
            _tmpNotificationTitle = _cursor.getString(_cursorIndexOfNotificationTitle);
            final String _tmpNotificationMessage;
            _tmpNotificationMessage = _cursor.getString(_cursorIndexOfNotificationMessage);
            final NotificationPriority _tmpPriority;
            _tmpPriority = __NotificationPriority_stringToEnum(_cursor.getString(_cursorIndexOfPriority));
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ReminderConfigEntity(_tmpId,_tmpType,_tmpName,_tmpDescription,_tmpIsEnabled,_tmpScheduleHour,_tmpScheduleMinute,_tmpScheduleDays,_tmpNotificationTitle,_tmpNotificationMessage,_tmpPriority,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getReminderConfigsByType(final ReminderType type,
      final Continuation<? super List<ReminderConfigEntity>> $completion) {
    final String _sql = "SELECT * FROM reminder_configs WHERE type = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, __ReminderType_enumToString(type));
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReminderConfigEntity>>() {
      @Override
      @NonNull
      public List<ReminderConfigEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfScheduleHour = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleHour");
          final int _cursorIndexOfScheduleMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleMinute");
          final int _cursorIndexOfScheduleDays = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleDays");
          final int _cursorIndexOfNotificationTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationTitle");
          final int _cursorIndexOfNotificationMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationMessage");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ReminderConfigEntity> _result = new ArrayList<ReminderConfigEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReminderConfigEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final ReminderType _tmpType;
            _tmpType = __ReminderType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final int _tmpScheduleHour;
            _tmpScheduleHour = _cursor.getInt(_cursorIndexOfScheduleHour);
            final int _tmpScheduleMinute;
            _tmpScheduleMinute = _cursor.getInt(_cursorIndexOfScheduleMinute);
            final String _tmpScheduleDays;
            _tmpScheduleDays = _cursor.getString(_cursorIndexOfScheduleDays);
            final String _tmpNotificationTitle;
            _tmpNotificationTitle = _cursor.getString(_cursorIndexOfNotificationTitle);
            final String _tmpNotificationMessage;
            _tmpNotificationMessage = _cursor.getString(_cursorIndexOfNotificationMessage);
            final NotificationPriority _tmpPriority;
            _tmpPriority = __NotificationPriority_stringToEnum(_cursor.getString(_cursorIndexOfPriority));
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ReminderConfigEntity(_tmpId,_tmpType,_tmpName,_tmpDescription,_tmpIsEnabled,_tmpScheduleHour,_tmpScheduleMinute,_tmpScheduleDays,_tmpNotificationTitle,_tmpNotificationMessage,_tmpPriority,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getSmartReminderById(final long id,
      final Continuation<? super SmartReminderEntity> $completion) {
    final String _sql = "SELECT * FROM smart_reminders WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SmartReminderEntity>() {
      @Override
      @Nullable
      public SmartReminderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderType = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfReminderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDate");
          final int _cursorIndexOfRepeatYearly = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatYearly");
          final int _cursorIndexOfRelatedPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedPersonId");
          final int _cursorIndexOfRelatedEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedEntryId");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfNotifyDaysBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "notifyDaysBefore");
          final int _cursorIndexOfLastNotifiedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastNotifiedAt");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final SmartReminderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SmartReminderType _tmpReminderType;
            _tmpReminderType = __SmartReminderType_stringToEnum(_cursor.getString(_cursorIndexOfReminderType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpReminderDate;
            _tmpReminderDate = _cursor.getLong(_cursorIndexOfReminderDate);
            final boolean _tmpRepeatYearly;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRepeatYearly);
            _tmpRepeatYearly = _tmp != 0;
            final Long _tmpRelatedPersonId;
            if (_cursor.isNull(_cursorIndexOfRelatedPersonId)) {
              _tmpRelatedPersonId = null;
            } else {
              _tmpRelatedPersonId = _cursor.getLong(_cursorIndexOfRelatedPersonId);
            }
            final Long _tmpRelatedEntryId;
            if (_cursor.isNull(_cursorIndexOfRelatedEntryId)) {
              _tmpRelatedEntryId = null;
            } else {
              _tmpRelatedEntryId = _cursor.getLong(_cursorIndexOfRelatedEntryId);
            }
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final int _tmpNotifyDaysBefore;
            _tmpNotifyDaysBefore = _cursor.getInt(_cursorIndexOfNotifyDaysBefore);
            final Long _tmpLastNotifiedAt;
            if (_cursor.isNull(_cursorIndexOfLastNotifiedAt)) {
              _tmpLastNotifiedAt = null;
            } else {
              _tmpLastNotifiedAt = _cursor.getLong(_cursorIndexOfLastNotifiedAt);
            }
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new SmartReminderEntity(_tmpId,_tmpReminderType,_tmpTitle,_tmpDescription,_tmpReminderDate,_tmpRepeatYearly,_tmpRelatedPersonId,_tmpRelatedEntryId,_tmpIsEnabled,_tmpNotifyDaysBefore,_tmpLastNotifiedAt,_tmpMetadata,_tmpCreatedAt);
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
  public Flow<List<SmartReminderEntity>> getAllSmartRemindersFlow() {
    final String _sql = "SELECT * FROM smart_reminders ORDER BY reminderDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"smart_reminders"}, new Callable<List<SmartReminderEntity>>() {
      @Override
      @NonNull
      public List<SmartReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderType = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfReminderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDate");
          final int _cursorIndexOfRepeatYearly = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatYearly");
          final int _cursorIndexOfRelatedPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedPersonId");
          final int _cursorIndexOfRelatedEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedEntryId");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfNotifyDaysBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "notifyDaysBefore");
          final int _cursorIndexOfLastNotifiedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastNotifiedAt");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SmartReminderEntity> _result = new ArrayList<SmartReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmartReminderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SmartReminderType _tmpReminderType;
            _tmpReminderType = __SmartReminderType_stringToEnum(_cursor.getString(_cursorIndexOfReminderType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpReminderDate;
            _tmpReminderDate = _cursor.getLong(_cursorIndexOfReminderDate);
            final boolean _tmpRepeatYearly;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRepeatYearly);
            _tmpRepeatYearly = _tmp != 0;
            final Long _tmpRelatedPersonId;
            if (_cursor.isNull(_cursorIndexOfRelatedPersonId)) {
              _tmpRelatedPersonId = null;
            } else {
              _tmpRelatedPersonId = _cursor.getLong(_cursorIndexOfRelatedPersonId);
            }
            final Long _tmpRelatedEntryId;
            if (_cursor.isNull(_cursorIndexOfRelatedEntryId)) {
              _tmpRelatedEntryId = null;
            } else {
              _tmpRelatedEntryId = _cursor.getLong(_cursorIndexOfRelatedEntryId);
            }
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final int _tmpNotifyDaysBefore;
            _tmpNotifyDaysBefore = _cursor.getInt(_cursorIndexOfNotifyDaysBefore);
            final Long _tmpLastNotifiedAt;
            if (_cursor.isNull(_cursorIndexOfLastNotifiedAt)) {
              _tmpLastNotifiedAt = null;
            } else {
              _tmpLastNotifiedAt = _cursor.getLong(_cursorIndexOfLastNotifiedAt);
            }
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SmartReminderEntity(_tmpId,_tmpReminderType,_tmpTitle,_tmpDescription,_tmpReminderDate,_tmpRepeatYearly,_tmpRelatedPersonId,_tmpRelatedEntryId,_tmpIsEnabled,_tmpNotifyDaysBefore,_tmpLastNotifiedAt,_tmpMetadata,_tmpCreatedAt);
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
  public Flow<List<SmartReminderEntity>> getEnabledSmartRemindersFlow() {
    final String _sql = "SELECT * FROM smart_reminders WHERE isEnabled = 1 ORDER BY reminderDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"smart_reminders"}, new Callable<List<SmartReminderEntity>>() {
      @Override
      @NonNull
      public List<SmartReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderType = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfReminderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDate");
          final int _cursorIndexOfRepeatYearly = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatYearly");
          final int _cursorIndexOfRelatedPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedPersonId");
          final int _cursorIndexOfRelatedEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedEntryId");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfNotifyDaysBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "notifyDaysBefore");
          final int _cursorIndexOfLastNotifiedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastNotifiedAt");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SmartReminderEntity> _result = new ArrayList<SmartReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmartReminderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SmartReminderType _tmpReminderType;
            _tmpReminderType = __SmartReminderType_stringToEnum(_cursor.getString(_cursorIndexOfReminderType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpReminderDate;
            _tmpReminderDate = _cursor.getLong(_cursorIndexOfReminderDate);
            final boolean _tmpRepeatYearly;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRepeatYearly);
            _tmpRepeatYearly = _tmp != 0;
            final Long _tmpRelatedPersonId;
            if (_cursor.isNull(_cursorIndexOfRelatedPersonId)) {
              _tmpRelatedPersonId = null;
            } else {
              _tmpRelatedPersonId = _cursor.getLong(_cursorIndexOfRelatedPersonId);
            }
            final Long _tmpRelatedEntryId;
            if (_cursor.isNull(_cursorIndexOfRelatedEntryId)) {
              _tmpRelatedEntryId = null;
            } else {
              _tmpRelatedEntryId = _cursor.getLong(_cursorIndexOfRelatedEntryId);
            }
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final int _tmpNotifyDaysBefore;
            _tmpNotifyDaysBefore = _cursor.getInt(_cursorIndexOfNotifyDaysBefore);
            final Long _tmpLastNotifiedAt;
            if (_cursor.isNull(_cursorIndexOfLastNotifiedAt)) {
              _tmpLastNotifiedAt = null;
            } else {
              _tmpLastNotifiedAt = _cursor.getLong(_cursorIndexOfLastNotifiedAt);
            }
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SmartReminderEntity(_tmpId,_tmpReminderType,_tmpTitle,_tmpDescription,_tmpReminderDate,_tmpRepeatYearly,_tmpRelatedPersonId,_tmpRelatedEntryId,_tmpIsEnabled,_tmpNotifyDaysBefore,_tmpLastNotifiedAt,_tmpMetadata,_tmpCreatedAt);
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
  public Flow<List<SmartReminderEntity>> getSmartRemindersByTypeFlow(final SmartReminderType type) {
    final String _sql = "SELECT * FROM smart_reminders WHERE reminderType = ? ORDER BY reminderDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, __SmartReminderType_enumToString(type));
    return CoroutinesRoom.createFlow(__db, false, new String[] {"smart_reminders"}, new Callable<List<SmartReminderEntity>>() {
      @Override
      @NonNull
      public List<SmartReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderType = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfReminderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDate");
          final int _cursorIndexOfRepeatYearly = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatYearly");
          final int _cursorIndexOfRelatedPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedPersonId");
          final int _cursorIndexOfRelatedEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedEntryId");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfNotifyDaysBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "notifyDaysBefore");
          final int _cursorIndexOfLastNotifiedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastNotifiedAt");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SmartReminderEntity> _result = new ArrayList<SmartReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmartReminderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SmartReminderType _tmpReminderType;
            _tmpReminderType = __SmartReminderType_stringToEnum(_cursor.getString(_cursorIndexOfReminderType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpReminderDate;
            _tmpReminderDate = _cursor.getLong(_cursorIndexOfReminderDate);
            final boolean _tmpRepeatYearly;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRepeatYearly);
            _tmpRepeatYearly = _tmp != 0;
            final Long _tmpRelatedPersonId;
            if (_cursor.isNull(_cursorIndexOfRelatedPersonId)) {
              _tmpRelatedPersonId = null;
            } else {
              _tmpRelatedPersonId = _cursor.getLong(_cursorIndexOfRelatedPersonId);
            }
            final Long _tmpRelatedEntryId;
            if (_cursor.isNull(_cursorIndexOfRelatedEntryId)) {
              _tmpRelatedEntryId = null;
            } else {
              _tmpRelatedEntryId = _cursor.getLong(_cursorIndexOfRelatedEntryId);
            }
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final int _tmpNotifyDaysBefore;
            _tmpNotifyDaysBefore = _cursor.getInt(_cursorIndexOfNotifyDaysBefore);
            final Long _tmpLastNotifiedAt;
            if (_cursor.isNull(_cursorIndexOfLastNotifiedAt)) {
              _tmpLastNotifiedAt = null;
            } else {
              _tmpLastNotifiedAt = _cursor.getLong(_cursorIndexOfLastNotifiedAt);
            }
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SmartReminderEntity(_tmpId,_tmpReminderType,_tmpTitle,_tmpDescription,_tmpReminderDate,_tmpRepeatYearly,_tmpRelatedPersonId,_tmpRelatedEntryId,_tmpIsEnabled,_tmpNotifyDaysBefore,_tmpLastNotifiedAt,_tmpMetadata,_tmpCreatedAt);
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
  public Object getSmartRemindersForPerson(final long personId,
      final Continuation<? super List<SmartReminderEntity>> $completion) {
    final String _sql = "SELECT * FROM smart_reminders WHERE relatedPersonId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SmartReminderEntity>>() {
      @Override
      @NonNull
      public List<SmartReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderType = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfReminderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDate");
          final int _cursorIndexOfRepeatYearly = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatYearly");
          final int _cursorIndexOfRelatedPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedPersonId");
          final int _cursorIndexOfRelatedEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedEntryId");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfNotifyDaysBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "notifyDaysBefore");
          final int _cursorIndexOfLastNotifiedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastNotifiedAt");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SmartReminderEntity> _result = new ArrayList<SmartReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmartReminderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SmartReminderType _tmpReminderType;
            _tmpReminderType = __SmartReminderType_stringToEnum(_cursor.getString(_cursorIndexOfReminderType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpReminderDate;
            _tmpReminderDate = _cursor.getLong(_cursorIndexOfReminderDate);
            final boolean _tmpRepeatYearly;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRepeatYearly);
            _tmpRepeatYearly = _tmp != 0;
            final Long _tmpRelatedPersonId;
            if (_cursor.isNull(_cursorIndexOfRelatedPersonId)) {
              _tmpRelatedPersonId = null;
            } else {
              _tmpRelatedPersonId = _cursor.getLong(_cursorIndexOfRelatedPersonId);
            }
            final Long _tmpRelatedEntryId;
            if (_cursor.isNull(_cursorIndexOfRelatedEntryId)) {
              _tmpRelatedEntryId = null;
            } else {
              _tmpRelatedEntryId = _cursor.getLong(_cursorIndexOfRelatedEntryId);
            }
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final int _tmpNotifyDaysBefore;
            _tmpNotifyDaysBefore = _cursor.getInt(_cursorIndexOfNotifyDaysBefore);
            final Long _tmpLastNotifiedAt;
            if (_cursor.isNull(_cursorIndexOfLastNotifiedAt)) {
              _tmpLastNotifiedAt = null;
            } else {
              _tmpLastNotifiedAt = _cursor.getLong(_cursorIndexOfLastNotifiedAt);
            }
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SmartReminderEntity(_tmpId,_tmpReminderType,_tmpTitle,_tmpDescription,_tmpReminderDate,_tmpRepeatYearly,_tmpRelatedPersonId,_tmpRelatedEntryId,_tmpIsEnabled,_tmpNotifyDaysBefore,_tmpLastNotifiedAt,_tmpMetadata,_tmpCreatedAt);
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
  public Object getUpcomingSmartReminders(final long startTime, final long endTime,
      final Continuation<? super List<SmartReminderEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM smart_reminders \n"
            + "        WHERE isEnabled = 1 \n"
            + "        AND reminderDate BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SmartReminderEntity>>() {
      @Override
      @NonNull
      public List<SmartReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderType = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfReminderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDate");
          final int _cursorIndexOfRepeatYearly = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatYearly");
          final int _cursorIndexOfRelatedPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedPersonId");
          final int _cursorIndexOfRelatedEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedEntryId");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfNotifyDaysBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "notifyDaysBefore");
          final int _cursorIndexOfLastNotifiedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastNotifiedAt");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SmartReminderEntity> _result = new ArrayList<SmartReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmartReminderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SmartReminderType _tmpReminderType;
            _tmpReminderType = __SmartReminderType_stringToEnum(_cursor.getString(_cursorIndexOfReminderType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpReminderDate;
            _tmpReminderDate = _cursor.getLong(_cursorIndexOfReminderDate);
            final boolean _tmpRepeatYearly;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRepeatYearly);
            _tmpRepeatYearly = _tmp != 0;
            final Long _tmpRelatedPersonId;
            if (_cursor.isNull(_cursorIndexOfRelatedPersonId)) {
              _tmpRelatedPersonId = null;
            } else {
              _tmpRelatedPersonId = _cursor.getLong(_cursorIndexOfRelatedPersonId);
            }
            final Long _tmpRelatedEntryId;
            if (_cursor.isNull(_cursorIndexOfRelatedEntryId)) {
              _tmpRelatedEntryId = null;
            } else {
              _tmpRelatedEntryId = _cursor.getLong(_cursorIndexOfRelatedEntryId);
            }
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final int _tmpNotifyDaysBefore;
            _tmpNotifyDaysBefore = _cursor.getInt(_cursorIndexOfNotifyDaysBefore);
            final Long _tmpLastNotifiedAt;
            if (_cursor.isNull(_cursorIndexOfLastNotifiedAt)) {
              _tmpLastNotifiedAt = null;
            } else {
              _tmpLastNotifiedAt = _cursor.getLong(_cursorIndexOfLastNotifiedAt);
            }
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SmartReminderEntity(_tmpId,_tmpReminderType,_tmpTitle,_tmpDescription,_tmpReminderDate,_tmpRepeatYearly,_tmpRelatedPersonId,_tmpRelatedEntryId,_tmpIsEnabled,_tmpNotifyDaysBefore,_tmpLastNotifiedAt,_tmpMetadata,_tmpCreatedAt);
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
  public Flow<List<SmartReminderEntity>> getAnniversaryRemindersFlow() {
    final String _sql = "\n"
            + "        SELECT * FROM smart_reminders \n"
            + "        WHERE reminderType IN ('BIRTHDAY', 'ANNIVERSARY') \n"
            + "        AND isEnabled = 1 \n"
            + "        AND repeatYearly = 1\n"
            + "        ORDER BY reminderDate ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"smart_reminders"}, new Callable<List<SmartReminderEntity>>() {
      @Override
      @NonNull
      public List<SmartReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderType = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfReminderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDate");
          final int _cursorIndexOfRepeatYearly = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatYearly");
          final int _cursorIndexOfRelatedPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedPersonId");
          final int _cursorIndexOfRelatedEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedEntryId");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfNotifyDaysBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "notifyDaysBefore");
          final int _cursorIndexOfLastNotifiedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastNotifiedAt");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SmartReminderEntity> _result = new ArrayList<SmartReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmartReminderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SmartReminderType _tmpReminderType;
            _tmpReminderType = __SmartReminderType_stringToEnum(_cursor.getString(_cursorIndexOfReminderType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpReminderDate;
            _tmpReminderDate = _cursor.getLong(_cursorIndexOfReminderDate);
            final boolean _tmpRepeatYearly;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRepeatYearly);
            _tmpRepeatYearly = _tmp != 0;
            final Long _tmpRelatedPersonId;
            if (_cursor.isNull(_cursorIndexOfRelatedPersonId)) {
              _tmpRelatedPersonId = null;
            } else {
              _tmpRelatedPersonId = _cursor.getLong(_cursorIndexOfRelatedPersonId);
            }
            final Long _tmpRelatedEntryId;
            if (_cursor.isNull(_cursorIndexOfRelatedEntryId)) {
              _tmpRelatedEntryId = null;
            } else {
              _tmpRelatedEntryId = _cursor.getLong(_cursorIndexOfRelatedEntryId);
            }
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final int _tmpNotifyDaysBefore;
            _tmpNotifyDaysBefore = _cursor.getInt(_cursorIndexOfNotifyDaysBefore);
            final Long _tmpLastNotifiedAt;
            if (_cursor.isNull(_cursorIndexOfLastNotifiedAt)) {
              _tmpLastNotifiedAt = null;
            } else {
              _tmpLastNotifiedAt = _cursor.getLong(_cursorIndexOfLastNotifiedAt);
            }
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SmartReminderEntity(_tmpId,_tmpReminderType,_tmpTitle,_tmpDescription,_tmpReminderDate,_tmpRepeatYearly,_tmpRelatedPersonId,_tmpRelatedEntryId,_tmpIsEnabled,_tmpNotifyDaysBefore,_tmpLastNotifiedAt,_tmpMetadata,_tmpCreatedAt);
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
  public Object getRecentNotificationHistory(final int limit,
      final Continuation<? super List<NotificationHistoryEntity>> $completion) {
    final String _sql = "SELECT * FROM notification_history ORDER BY sentAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<NotificationHistoryEntity>>() {
      @Override
      @NonNull
      public List<NotificationHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfSourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfWasTapped = CursorUtil.getColumnIndexOrThrow(_cursor, "wasTapped");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final List<NotificationHistoryEntity> _result = new ArrayList<NotificationHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NotificationHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final long _tmpSourceId;
            _tmpSourceId = _cursor.getLong(_cursorIndexOfSourceId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpWasTapped;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWasTapped);
            _tmpWasTapped = _tmp != 0;
            final long _tmpSentAt;
            _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            _item = new NotificationHistoryEntity(_tmpId,_tmpType,_tmpSourceId,_tmpTitle,_tmpMessage,_tmpWasTapped,_tmpSentAt);
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
  public Flow<List<NotificationHistoryEntity>> getNotificationHistoryFlow() {
    final String _sql = "SELECT * FROM notification_history ORDER BY sentAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notification_history"}, new Callable<List<NotificationHistoryEntity>>() {
      @Override
      @NonNull
      public List<NotificationHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfSourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfWasTapped = CursorUtil.getColumnIndexOrThrow(_cursor, "wasTapped");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final List<NotificationHistoryEntity> _result = new ArrayList<NotificationHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NotificationHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final long _tmpSourceId;
            _tmpSourceId = _cursor.getLong(_cursorIndexOfSourceId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpWasTapped;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWasTapped);
            _tmpWasTapped = _tmp != 0;
            final long _tmpSentAt;
            _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            _item = new NotificationHistoryEntity(_tmpId,_tmpType,_tmpSourceId,_tmpTitle,_tmpMessage,_tmpWasTapped,_tmpSentAt);
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
  public Object getNotificationCountSince(final long sinceTimestamp,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM notification_history WHERE sentAt >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sinceTimestamp);
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
  public Object getMoodCheckInConfig(
      final Continuation<? super MoodCheckInConfigEntity> $completion) {
    final String _sql = "SELECT * FROM mood_checkin_config LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MoodCheckInConfigEntity>() {
      @Override
      @Nullable
      public MoodCheckInConfigEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfIntervalHours = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalHours");
          final int _cursorIndexOfStartHour = CursorUtil.getColumnIndexOrThrow(_cursor, "startHour");
          final int _cursorIndexOfEndHour = CursorUtil.getColumnIndexOrThrow(_cursor, "endHour");
          final int _cursorIndexOfLastPromptAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPromptAt");
          final int _cursorIndexOfEnabledDays = CursorUtil.getColumnIndexOrThrow(_cursor, "enabledDays");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final MoodCheckInConfigEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final int _tmpIntervalHours;
            _tmpIntervalHours = _cursor.getInt(_cursorIndexOfIntervalHours);
            final int _tmpStartHour;
            _tmpStartHour = _cursor.getInt(_cursorIndexOfStartHour);
            final int _tmpEndHour;
            _tmpEndHour = _cursor.getInt(_cursorIndexOfEndHour);
            final Long _tmpLastPromptAt;
            if (_cursor.isNull(_cursorIndexOfLastPromptAt)) {
              _tmpLastPromptAt = null;
            } else {
              _tmpLastPromptAt = _cursor.getLong(_cursorIndexOfLastPromptAt);
            }
            final String _tmpEnabledDays;
            _tmpEnabledDays = _cursor.getString(_cursorIndexOfEnabledDays);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MoodCheckInConfigEntity(_tmpId,_tmpIsEnabled,_tmpIntervalHours,_tmpStartHour,_tmpEndHour,_tmpLastPromptAt,_tmpEnabledDays,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<MoodCheckInConfigEntity> getMoodCheckInConfigFlow() {
    final String _sql = "SELECT * FROM mood_checkin_config LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"mood_checkin_config"}, new Callable<MoodCheckInConfigEntity>() {
      @Override
      @Nullable
      public MoodCheckInConfigEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfIntervalHours = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalHours");
          final int _cursorIndexOfStartHour = CursorUtil.getColumnIndexOrThrow(_cursor, "startHour");
          final int _cursorIndexOfEndHour = CursorUtil.getColumnIndexOrThrow(_cursor, "endHour");
          final int _cursorIndexOfLastPromptAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPromptAt");
          final int _cursorIndexOfEnabledDays = CursorUtil.getColumnIndexOrThrow(_cursor, "enabledDays");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final MoodCheckInConfigEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final int _tmpIntervalHours;
            _tmpIntervalHours = _cursor.getInt(_cursorIndexOfIntervalHours);
            final int _tmpStartHour;
            _tmpStartHour = _cursor.getInt(_cursorIndexOfStartHour);
            final int _tmpEndHour;
            _tmpEndHour = _cursor.getInt(_cursorIndexOfEndHour);
            final Long _tmpLastPromptAt;
            if (_cursor.isNull(_cursorIndexOfLastPromptAt)) {
              _tmpLastPromptAt = null;
            } else {
              _tmpLastPromptAt = _cursor.getLong(_cursorIndexOfLastPromptAt);
            }
            final String _tmpEnabledDays;
            _tmpEnabledDays = _cursor.getString(_cursorIndexOfEnabledDays);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MoodCheckInConfigEntity(_tmpId,_tmpIsEnabled,_tmpIntervalHours,_tmpStartHour,_tmpEndHour,_tmpLastPromptAt,_tmpEnabledDays,_tmpCreatedAt,_tmpUpdatedAt);
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

  private String __ReminderType_enumToString(@NonNull final ReminderType _value) {
    switch (_value) {
      case DAILY_JOURNAL: return "DAILY_JOURNAL";
      case WEEKLY_REFLECTION: return "WEEKLY_REFLECTION";
      case CUSTOM: return "CUSTOM";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __NotificationPriority_enumToString(@NonNull final NotificationPriority _value) {
    switch (_value) {
      case LOW: return "LOW";
      case DEFAULT: return "DEFAULT";
      case HIGH: return "HIGH";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __SmartReminderType_enumToString(@NonNull final SmartReminderType _value) {
    switch (_value) {
      case BIRTHDAY: return "BIRTHDAY";
      case ANNIVERSARY: return "ANNIVERSARY";
      case MEMORY_ANNIVERSARY: return "MEMORY_ANNIVERSARY";
      case SPECIAL_DATE: return "SPECIAL_DATE";
      case PERSON_MENTION: return "PERSON_MENTION";
      case STREAK_RISK: return "STREAK_RISK";
      case MOOD_CHECK_IN: return "MOOD_CHECK_IN";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private ReminderType __ReminderType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "DAILY_JOURNAL": return ReminderType.DAILY_JOURNAL;
      case "WEEKLY_REFLECTION": return ReminderType.WEEKLY_REFLECTION;
      case "CUSTOM": return ReminderType.CUSTOM;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private NotificationPriority __NotificationPriority_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "LOW": return NotificationPriority.LOW;
      case "DEFAULT": return NotificationPriority.DEFAULT;
      case "HIGH": return NotificationPriority.HIGH;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private SmartReminderType __SmartReminderType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "BIRTHDAY": return SmartReminderType.BIRTHDAY;
      case "ANNIVERSARY": return SmartReminderType.ANNIVERSARY;
      case "MEMORY_ANNIVERSARY": return SmartReminderType.MEMORY_ANNIVERSARY;
      case "SPECIAL_DATE": return SmartReminderType.SPECIAL_DATE;
      case "PERSON_MENTION": return SmartReminderType.PERSON_MENTION;
      case "STREAK_RISK": return SmartReminderType.STREAK_RISK;
      case "MOOD_CHECK_IN": return SmartReminderType.MOOD_CHECK_IN;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
