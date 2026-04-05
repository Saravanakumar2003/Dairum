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
import com.dairum.data.local.entity.JournalEntryEntity;
import com.dairum.data.local.entity.MemoryEntity;
import com.dairum.data.local.entity.MemoryEntryCrossRef;
import com.dairum.data.local.entity.MemoryMilestoneEntity;
import com.dairum.data.local.entity.MemoryTrackerEntity;
import com.dairum.data.local.entity.MemoryTrackerType;
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
public final class MemoryDao_Impl implements MemoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MemoryEntity> __insertionAdapterOfMemoryEntity;

  private final EntityInsertionAdapter<MemoryEntryCrossRef> __insertionAdapterOfMemoryEntryCrossRef;

  private final EntityInsertionAdapter<MemoryMilestoneEntity> __insertionAdapterOfMemoryMilestoneEntity;

  private final EntityInsertionAdapter<MemoryTrackerEntity> __insertionAdapterOfMemoryTrackerEntity;

  private final EntityDeletionOrUpdateAdapter<MemoryEntryCrossRef> __deletionAdapterOfMemoryEntryCrossRef;

  private final EntityDeletionOrUpdateAdapter<MemoryEntity> __updateAdapterOfMemoryEntity;

  private final EntityDeletionOrUpdateAdapter<MemoryMilestoneEntity> __updateAdapterOfMemoryMilestoneEntity;

  private final EntityDeletionOrUpdateAdapter<MemoryTrackerEntity> __updateAdapterOfMemoryTrackerEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMemory;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFavorite;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllEntriesFromMemory;

  private final SharedSQLiteStatement __preparedStmtOfRemoveEntryFromMemory;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMilestone;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMemoryTracker;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMemoryStats;

  public MemoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMemoryEntity = new EntityInsertionAdapter<MemoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `memories` (`id`,`title`,`description`,`coverImageUri`,`startDate`,`endDate`,`location`,`locationData`,`isFavorite`,`color`,`icon`,`tags`,`moodSummary`,`totalTimeSpent`,`entryCount`,`wordCount`,`photoCount`,`videoCount`,`peopleInvolved`,`highlights`,`trackerSummary`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        if (entity.getCoverImageUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCoverImageUri());
        }
        statement.bindLong(5, entity.getStartDate());
        statement.bindLong(6, entity.getEndDate());
        if (entity.getLocation() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLocation());
        }
        if (entity.getLocationData() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLocationData());
        }
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getColor() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getColor());
        }
        if (entity.getIcon() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getIcon());
        }
        if (entity.getTags() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getTags());
        }
        if (entity.getMoodSummary() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getMoodSummary());
        }
        statement.bindLong(14, entity.getTotalTimeSpent());
        statement.bindLong(15, entity.getEntryCount());
        statement.bindLong(16, entity.getWordCount());
        statement.bindLong(17, entity.getPhotoCount());
        statement.bindLong(18, entity.getVideoCount());
        if (entity.getPeopleInvolved() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getPeopleInvolved());
        }
        if (entity.getHighlights() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getHighlights());
        }
        if (entity.getTrackerSummary() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getTrackerSummary());
        }
        statement.bindLong(22, entity.getCreatedAt());
        statement.bindLong(23, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfMemoryEntryCrossRef = new EntityInsertionAdapter<MemoryEntryCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `memory_entries` (`memoryId`,`entryId`,`sortOrder`,`addedAt`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemoryEntryCrossRef entity) {
        statement.bindLong(1, entity.getMemoryId());
        statement.bindLong(2, entity.getEntryId());
        statement.bindLong(3, entity.getSortOrder());
        statement.bindLong(4, entity.getAddedAt());
      }
    };
    this.__insertionAdapterOfMemoryMilestoneEntity = new EntityInsertionAdapter<MemoryMilestoneEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `memory_milestones` (`id`,`memoryId`,`title`,`description`,`milestoneDate`,`relatedEntryId`,`icon`,`sortOrder`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemoryMilestoneEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMemoryId());
        statement.bindString(3, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getMilestoneDate());
        if (entity.getRelatedEntryId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getRelatedEntryId());
        }
        if (entity.getIcon() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getIcon());
        }
        statement.bindLong(8, entity.getSortOrder());
      }
    };
    this.__insertionAdapterOfMemoryTrackerEntity = new EntityInsertionAdapter<MemoryTrackerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `memory_trackers` (`id`,`memoryId`,`name`,`type`,`icon`,`unit`,`goalValue`,`currentValue`,`progress`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemoryTrackerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMemoryId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, __MemoryTrackerType_enumToString(entity.getType()));
        if (entity.getIcon() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getIcon());
        }
        if (entity.getUnit() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUnit());
        }
        if (entity.getGoalValue() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getGoalValue());
        }
        if (entity.getCurrentValue() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCurrentValue());
        }
        statement.bindDouble(9, entity.getProgress());
      }
    };
    this.__deletionAdapterOfMemoryEntryCrossRef = new EntityDeletionOrUpdateAdapter<MemoryEntryCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `memory_entries` WHERE `memoryId` = ? AND `entryId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemoryEntryCrossRef entity) {
        statement.bindLong(1, entity.getMemoryId());
        statement.bindLong(2, entity.getEntryId());
      }
    };
    this.__updateAdapterOfMemoryEntity = new EntityDeletionOrUpdateAdapter<MemoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `memories` SET `id` = ?,`title` = ?,`description` = ?,`coverImageUri` = ?,`startDate` = ?,`endDate` = ?,`location` = ?,`locationData` = ?,`isFavorite` = ?,`color` = ?,`icon` = ?,`tags` = ?,`moodSummary` = ?,`totalTimeSpent` = ?,`entryCount` = ?,`wordCount` = ?,`photoCount` = ?,`videoCount` = ?,`peopleInvolved` = ?,`highlights` = ?,`trackerSummary` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        if (entity.getCoverImageUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCoverImageUri());
        }
        statement.bindLong(5, entity.getStartDate());
        statement.bindLong(6, entity.getEndDate());
        if (entity.getLocation() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLocation());
        }
        if (entity.getLocationData() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLocationData());
        }
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getColor() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getColor());
        }
        if (entity.getIcon() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getIcon());
        }
        if (entity.getTags() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getTags());
        }
        if (entity.getMoodSummary() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getMoodSummary());
        }
        statement.bindLong(14, entity.getTotalTimeSpent());
        statement.bindLong(15, entity.getEntryCount());
        statement.bindLong(16, entity.getWordCount());
        statement.bindLong(17, entity.getPhotoCount());
        statement.bindLong(18, entity.getVideoCount());
        if (entity.getPeopleInvolved() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getPeopleInvolved());
        }
        if (entity.getHighlights() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getHighlights());
        }
        if (entity.getTrackerSummary() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getTrackerSummary());
        }
        statement.bindLong(22, entity.getCreatedAt());
        statement.bindLong(23, entity.getUpdatedAt());
        statement.bindLong(24, entity.getId());
      }
    };
    this.__updateAdapterOfMemoryMilestoneEntity = new EntityDeletionOrUpdateAdapter<MemoryMilestoneEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `memory_milestones` SET `id` = ?,`memoryId` = ?,`title` = ?,`description` = ?,`milestoneDate` = ?,`relatedEntryId` = ?,`icon` = ?,`sortOrder` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemoryMilestoneEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMemoryId());
        statement.bindString(3, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getMilestoneDate());
        if (entity.getRelatedEntryId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getRelatedEntryId());
        }
        if (entity.getIcon() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getIcon());
        }
        statement.bindLong(8, entity.getSortOrder());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__updateAdapterOfMemoryTrackerEntity = new EntityDeletionOrUpdateAdapter<MemoryTrackerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `memory_trackers` SET `id` = ?,`memoryId` = ?,`name` = ?,`type` = ?,`icon` = ?,`unit` = ?,`goalValue` = ?,`currentValue` = ?,`progress` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemoryTrackerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMemoryId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, __MemoryTrackerType_enumToString(entity.getType()));
        if (entity.getIcon() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getIcon());
        }
        if (entity.getUnit() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUnit());
        }
        if (entity.getGoalValue() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getGoalValue());
        }
        if (entity.getCurrentValue() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCurrentValue());
        }
        statement.bindDouble(9, entity.getProgress());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteMemory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM memories WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateFavorite = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE memories SET isFavorite = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllEntriesFromMemory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM memory_entries WHERE memoryId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveEntryFromMemory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM memory_entries WHERE memoryId = ? AND entryId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMilestone = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM memory_milestones WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMemoryTracker = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM memory_trackers WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMemoryStats = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE memories SET \n"
                + "            entryCount = ?,\n"
                + "            wordCount = ?,\n"
                + "            photoCount = ?,\n"
                + "            videoCount = ?,\n"
                + "            updatedAt = ?\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object insertMemory(final MemoryEntity memory,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMemoryEntity.insertAndReturnId(memory);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMemoryEntry(final MemoryEntryCrossRef crossRef,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMemoryEntryCrossRef.insert(crossRef);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMemoryEntries(final List<MemoryEntryCrossRef> crossRefs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMemoryEntryCrossRef.insert(crossRefs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMilestone(final MemoryMilestoneEntity milestone,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMemoryMilestoneEntity.insertAndReturnId(milestone);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMemoryTracker(final MemoryTrackerEntity tracker,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMemoryTrackerEntity.insertAndReturnId(tracker);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMemoryEntry(final MemoryEntryCrossRef crossRef,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMemoryEntryCrossRef.handle(crossRef);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMemory(final MemoryEntity memory,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMemoryEntity.handle(memory);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMilestone(final MemoryMilestoneEntity milestone,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMemoryMilestoneEntity.handle(milestone);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMemoryTracker(final MemoryTrackerEntity tracker,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMemoryTrackerEntity.handle(tracker);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMemory(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMemory.acquire();
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
          __preparedStmtOfDeleteMemory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFavorite(final long id, final boolean isFavorite, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavorite.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
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
          __preparedStmtOfUpdateFavorite.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllEntriesFromMemory(final long memoryId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllEntriesFromMemory.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, memoryId);
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
          __preparedStmtOfDeleteAllEntriesFromMemory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object removeEntryFromMemory(final long memoryId, final long entryId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveEntryFromMemory.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, memoryId);
        _argIndex = 2;
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
          __preparedStmtOfRemoveEntryFromMemory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMilestone(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMilestone.acquire();
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
          __preparedStmtOfDeleteMilestone.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMemoryTracker(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMemoryTracker.acquire();
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
          __preparedStmtOfDeleteMemoryTracker.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMemoryStats(final long memoryId, final int entryCount, final int wordCount,
      final int photoCount, final int videoCount, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMemoryStats.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, entryCount);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, wordCount);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, photoCount);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, videoCount);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 6;
        _stmt.bindLong(_argIndex, memoryId);
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
          __preparedStmtOfUpdateMemoryStats.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getMemoryById(final long id, final Continuation<? super MemoryEntity> $completion) {
    final String _sql = "SELECT * FROM memories WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MemoryEntity>() {
      @Override
      @Nullable
      public MemoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUri");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfMoodSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "moodSummary");
          final int _cursorIndexOfTotalTimeSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeSpent");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfPhotoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "photoCount");
          final int _cursorIndexOfVideoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "videoCount");
          final int _cursorIndexOfPeopleInvolved = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleInvolved");
          final int _cursorIndexOfHighlights = CursorUtil.getColumnIndexOrThrow(_cursor, "highlights");
          final int _cursorIndexOfTrackerSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerSummary");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final MemoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverImageUri;
            if (_cursor.isNull(_cursorIndexOfCoverImageUri)) {
              _tmpCoverImageUri = null;
            } else {
              _tmpCoverImageUri = _cursor.getString(_cursorIndexOfCoverImageUri);
            }
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final String _tmpMoodSummary;
            if (_cursor.isNull(_cursorIndexOfMoodSummary)) {
              _tmpMoodSummary = null;
            } else {
              _tmpMoodSummary = _cursor.getString(_cursorIndexOfMoodSummary);
            }
            final long _tmpTotalTimeSpent;
            _tmpTotalTimeSpent = _cursor.getLong(_cursorIndexOfTotalTimeSpent);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpPhotoCount;
            _tmpPhotoCount = _cursor.getInt(_cursorIndexOfPhotoCount);
            final int _tmpVideoCount;
            _tmpVideoCount = _cursor.getInt(_cursorIndexOfVideoCount);
            final String _tmpPeopleInvolved;
            if (_cursor.isNull(_cursorIndexOfPeopleInvolved)) {
              _tmpPeopleInvolved = null;
            } else {
              _tmpPeopleInvolved = _cursor.getString(_cursorIndexOfPeopleInvolved);
            }
            final String _tmpHighlights;
            if (_cursor.isNull(_cursorIndexOfHighlights)) {
              _tmpHighlights = null;
            } else {
              _tmpHighlights = _cursor.getString(_cursorIndexOfHighlights);
            }
            final String _tmpTrackerSummary;
            if (_cursor.isNull(_cursorIndexOfTrackerSummary)) {
              _tmpTrackerSummary = null;
            } else {
              _tmpTrackerSummary = _cursor.getString(_cursorIndexOfTrackerSummary);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MemoryEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCoverImageUri,_tmpStartDate,_tmpEndDate,_tmpLocation,_tmpLocationData,_tmpIsFavorite,_tmpColor,_tmpIcon,_tmpTags,_tmpMoodSummary,_tmpTotalTimeSpent,_tmpEntryCount,_tmpWordCount,_tmpPhotoCount,_tmpVideoCount,_tmpPeopleInvolved,_tmpHighlights,_tmpTrackerSummary,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<MemoryEntity> getMemoryByIdFlow(final long id) {
    final String _sql = "SELECT * FROM memories WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"memories"}, new Callable<MemoryEntity>() {
      @Override
      @Nullable
      public MemoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUri");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfMoodSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "moodSummary");
          final int _cursorIndexOfTotalTimeSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeSpent");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfPhotoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "photoCount");
          final int _cursorIndexOfVideoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "videoCount");
          final int _cursorIndexOfPeopleInvolved = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleInvolved");
          final int _cursorIndexOfHighlights = CursorUtil.getColumnIndexOrThrow(_cursor, "highlights");
          final int _cursorIndexOfTrackerSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerSummary");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final MemoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverImageUri;
            if (_cursor.isNull(_cursorIndexOfCoverImageUri)) {
              _tmpCoverImageUri = null;
            } else {
              _tmpCoverImageUri = _cursor.getString(_cursorIndexOfCoverImageUri);
            }
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final String _tmpMoodSummary;
            if (_cursor.isNull(_cursorIndexOfMoodSummary)) {
              _tmpMoodSummary = null;
            } else {
              _tmpMoodSummary = _cursor.getString(_cursorIndexOfMoodSummary);
            }
            final long _tmpTotalTimeSpent;
            _tmpTotalTimeSpent = _cursor.getLong(_cursorIndexOfTotalTimeSpent);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpPhotoCount;
            _tmpPhotoCount = _cursor.getInt(_cursorIndexOfPhotoCount);
            final int _tmpVideoCount;
            _tmpVideoCount = _cursor.getInt(_cursorIndexOfVideoCount);
            final String _tmpPeopleInvolved;
            if (_cursor.isNull(_cursorIndexOfPeopleInvolved)) {
              _tmpPeopleInvolved = null;
            } else {
              _tmpPeopleInvolved = _cursor.getString(_cursorIndexOfPeopleInvolved);
            }
            final String _tmpHighlights;
            if (_cursor.isNull(_cursorIndexOfHighlights)) {
              _tmpHighlights = null;
            } else {
              _tmpHighlights = _cursor.getString(_cursorIndexOfHighlights);
            }
            final String _tmpTrackerSummary;
            if (_cursor.isNull(_cursorIndexOfTrackerSummary)) {
              _tmpTrackerSummary = null;
            } else {
              _tmpTrackerSummary = _cursor.getString(_cursorIndexOfTrackerSummary);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MemoryEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCoverImageUri,_tmpStartDate,_tmpEndDate,_tmpLocation,_tmpLocationData,_tmpIsFavorite,_tmpColor,_tmpIcon,_tmpTags,_tmpMoodSummary,_tmpTotalTimeSpent,_tmpEntryCount,_tmpWordCount,_tmpPhotoCount,_tmpVideoCount,_tmpPeopleInvolved,_tmpHighlights,_tmpTrackerSummary,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<MemoryEntity>> getAllMemoriesFlow() {
    final String _sql = "SELECT * FROM memories ORDER BY startDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"memories"}, new Callable<List<MemoryEntity>>() {
      @Override
      @NonNull
      public List<MemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUri");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfMoodSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "moodSummary");
          final int _cursorIndexOfTotalTimeSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeSpent");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfPhotoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "photoCount");
          final int _cursorIndexOfVideoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "videoCount");
          final int _cursorIndexOfPeopleInvolved = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleInvolved");
          final int _cursorIndexOfHighlights = CursorUtil.getColumnIndexOrThrow(_cursor, "highlights");
          final int _cursorIndexOfTrackerSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerSummary");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<MemoryEntity> _result = new ArrayList<MemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverImageUri;
            if (_cursor.isNull(_cursorIndexOfCoverImageUri)) {
              _tmpCoverImageUri = null;
            } else {
              _tmpCoverImageUri = _cursor.getString(_cursorIndexOfCoverImageUri);
            }
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final String _tmpMoodSummary;
            if (_cursor.isNull(_cursorIndexOfMoodSummary)) {
              _tmpMoodSummary = null;
            } else {
              _tmpMoodSummary = _cursor.getString(_cursorIndexOfMoodSummary);
            }
            final long _tmpTotalTimeSpent;
            _tmpTotalTimeSpent = _cursor.getLong(_cursorIndexOfTotalTimeSpent);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpPhotoCount;
            _tmpPhotoCount = _cursor.getInt(_cursorIndexOfPhotoCount);
            final int _tmpVideoCount;
            _tmpVideoCount = _cursor.getInt(_cursorIndexOfVideoCount);
            final String _tmpPeopleInvolved;
            if (_cursor.isNull(_cursorIndexOfPeopleInvolved)) {
              _tmpPeopleInvolved = null;
            } else {
              _tmpPeopleInvolved = _cursor.getString(_cursorIndexOfPeopleInvolved);
            }
            final String _tmpHighlights;
            if (_cursor.isNull(_cursorIndexOfHighlights)) {
              _tmpHighlights = null;
            } else {
              _tmpHighlights = _cursor.getString(_cursorIndexOfHighlights);
            }
            final String _tmpTrackerSummary;
            if (_cursor.isNull(_cursorIndexOfTrackerSummary)) {
              _tmpTrackerSummary = null;
            } else {
              _tmpTrackerSummary = _cursor.getString(_cursorIndexOfTrackerSummary);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MemoryEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCoverImageUri,_tmpStartDate,_tmpEndDate,_tmpLocation,_tmpLocationData,_tmpIsFavorite,_tmpColor,_tmpIcon,_tmpTags,_tmpMoodSummary,_tmpTotalTimeSpent,_tmpEntryCount,_tmpWordCount,_tmpPhotoCount,_tmpVideoCount,_tmpPeopleInvolved,_tmpHighlights,_tmpTrackerSummary,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<MemoryEntity>> getFavoriteMemoriesFlow() {
    final String _sql = "SELECT * FROM memories WHERE isFavorite = 1 ORDER BY startDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"memories"}, new Callable<List<MemoryEntity>>() {
      @Override
      @NonNull
      public List<MemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUri");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfMoodSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "moodSummary");
          final int _cursorIndexOfTotalTimeSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeSpent");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfPhotoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "photoCount");
          final int _cursorIndexOfVideoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "videoCount");
          final int _cursorIndexOfPeopleInvolved = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleInvolved");
          final int _cursorIndexOfHighlights = CursorUtil.getColumnIndexOrThrow(_cursor, "highlights");
          final int _cursorIndexOfTrackerSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerSummary");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<MemoryEntity> _result = new ArrayList<MemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverImageUri;
            if (_cursor.isNull(_cursorIndexOfCoverImageUri)) {
              _tmpCoverImageUri = null;
            } else {
              _tmpCoverImageUri = _cursor.getString(_cursorIndexOfCoverImageUri);
            }
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final String _tmpMoodSummary;
            if (_cursor.isNull(_cursorIndexOfMoodSummary)) {
              _tmpMoodSummary = null;
            } else {
              _tmpMoodSummary = _cursor.getString(_cursorIndexOfMoodSummary);
            }
            final long _tmpTotalTimeSpent;
            _tmpTotalTimeSpent = _cursor.getLong(_cursorIndexOfTotalTimeSpent);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpPhotoCount;
            _tmpPhotoCount = _cursor.getInt(_cursorIndexOfPhotoCount);
            final int _tmpVideoCount;
            _tmpVideoCount = _cursor.getInt(_cursorIndexOfVideoCount);
            final String _tmpPeopleInvolved;
            if (_cursor.isNull(_cursorIndexOfPeopleInvolved)) {
              _tmpPeopleInvolved = null;
            } else {
              _tmpPeopleInvolved = _cursor.getString(_cursorIndexOfPeopleInvolved);
            }
            final String _tmpHighlights;
            if (_cursor.isNull(_cursorIndexOfHighlights)) {
              _tmpHighlights = null;
            } else {
              _tmpHighlights = _cursor.getString(_cursorIndexOfHighlights);
            }
            final String _tmpTrackerSummary;
            if (_cursor.isNull(_cursorIndexOfTrackerSummary)) {
              _tmpTrackerSummary = null;
            } else {
              _tmpTrackerSummary = _cursor.getString(_cursorIndexOfTrackerSummary);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MemoryEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCoverImageUri,_tmpStartDate,_tmpEndDate,_tmpLocation,_tmpLocationData,_tmpIsFavorite,_tmpColor,_tmpIcon,_tmpTags,_tmpMoodSummary,_tmpTotalTimeSpent,_tmpEntryCount,_tmpWordCount,_tmpPhotoCount,_tmpVideoCount,_tmpPeopleInvolved,_tmpHighlights,_tmpTrackerSummary,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<MemoryEntity>> getMemoriesInDateRange(final long startTime, final long endTime) {
    final String _sql = "\n"
            + "        SELECT * FROM memories \n"
            + "        WHERE startDate >= ? AND endDate <= ?\n"
            + "        ORDER BY startDate DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"memories"}, new Callable<List<MemoryEntity>>() {
      @Override
      @NonNull
      public List<MemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUri");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfMoodSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "moodSummary");
          final int _cursorIndexOfTotalTimeSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeSpent");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfPhotoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "photoCount");
          final int _cursorIndexOfVideoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "videoCount");
          final int _cursorIndexOfPeopleInvolved = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleInvolved");
          final int _cursorIndexOfHighlights = CursorUtil.getColumnIndexOrThrow(_cursor, "highlights");
          final int _cursorIndexOfTrackerSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerSummary");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<MemoryEntity> _result = new ArrayList<MemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverImageUri;
            if (_cursor.isNull(_cursorIndexOfCoverImageUri)) {
              _tmpCoverImageUri = null;
            } else {
              _tmpCoverImageUri = _cursor.getString(_cursorIndexOfCoverImageUri);
            }
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final String _tmpMoodSummary;
            if (_cursor.isNull(_cursorIndexOfMoodSummary)) {
              _tmpMoodSummary = null;
            } else {
              _tmpMoodSummary = _cursor.getString(_cursorIndexOfMoodSummary);
            }
            final long _tmpTotalTimeSpent;
            _tmpTotalTimeSpent = _cursor.getLong(_cursorIndexOfTotalTimeSpent);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpPhotoCount;
            _tmpPhotoCount = _cursor.getInt(_cursorIndexOfPhotoCount);
            final int _tmpVideoCount;
            _tmpVideoCount = _cursor.getInt(_cursorIndexOfVideoCount);
            final String _tmpPeopleInvolved;
            if (_cursor.isNull(_cursorIndexOfPeopleInvolved)) {
              _tmpPeopleInvolved = null;
            } else {
              _tmpPeopleInvolved = _cursor.getString(_cursorIndexOfPeopleInvolved);
            }
            final String _tmpHighlights;
            if (_cursor.isNull(_cursorIndexOfHighlights)) {
              _tmpHighlights = null;
            } else {
              _tmpHighlights = _cursor.getString(_cursorIndexOfHighlights);
            }
            final String _tmpTrackerSummary;
            if (_cursor.isNull(_cursorIndexOfTrackerSummary)) {
              _tmpTrackerSummary = null;
            } else {
              _tmpTrackerSummary = _cursor.getString(_cursorIndexOfTrackerSummary);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MemoryEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCoverImageUri,_tmpStartDate,_tmpEndDate,_tmpLocation,_tmpLocationData,_tmpIsFavorite,_tmpColor,_tmpIcon,_tmpTags,_tmpMoodSummary,_tmpTotalTimeSpent,_tmpEntryCount,_tmpWordCount,_tmpPhotoCount,_tmpVideoCount,_tmpPeopleInvolved,_tmpHighlights,_tmpTrackerSummary,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<JournalEntryEntity>> getEntriesForMemory(final long memoryId) {
    final String _sql = "\n"
            + "        SELECT e.* FROM journal_entries e\n"
            + "        INNER JOIN memory_entries me ON e.id = me.entryId\n"
            + "        WHERE me.memoryId = ?\n"
            + "        ORDER BY me.sortOrder ASC, e.createdAt ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, memoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"journal_entries",
        "memory_entries"}, new Callable<List<JournalEntryEntity>>() {
      @Override
      @NonNull
      public List<JournalEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfPlainContent = CursorUtil.getColumnIndexOrThrow(_cursor, "plainContent");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMoodLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "moodLevel");
          final int _cursorIndexOfMoodIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "moodIcon");
          final int _cursorIndexOfWeatherData = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherData");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "isDraft");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JournalEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpPlainContent;
            _tmpPlainContent = _cursor.getString(_cursorIndexOfPlainContent);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpMoodLevel;
            if (_cursor.isNull(_cursorIndexOfMoodLevel)) {
              _tmpMoodLevel = null;
            } else {
              _tmpMoodLevel = _cursor.getInt(_cursorIndexOfMoodLevel);
            }
            final String _tmpMoodIcon;
            if (_cursor.isNull(_cursorIndexOfMoodIcon)) {
              _tmpMoodIcon = null;
            } else {
              _tmpMoodIcon = _cursor.getString(_cursorIndexOfMoodIcon);
            }
            final String _tmpWeatherData;
            if (_cursor.isNull(_cursorIndexOfWeatherData)) {
              _tmpWeatherData = null;
            } else {
              _tmpWeatherData = _cursor.getString(_cursorIndexOfWeatherData);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsDraft;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDraft);
            _tmpIsDraft = _tmp_2 != 0;
            final boolean _tmpIsPinned;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_3 != 0;
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            _item = new JournalEntryEntity(_tmpId,_tmpTitle,_tmpContent,_tmpPlainContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpMoodLevel,_tmpMoodIcon,_tmpWeatherData,_tmpLocationData,_tmpIsFavorite,_tmpIsArchived,_tmpIsDraft,_tmpIsPinned,_tmpWordCount);
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
  public Flow<List<MemoryEntity>> getMemoriesForEntry(final long entryId) {
    final String _sql = "\n"
            + "        SELECT m.* FROM memories m\n"
            + "        INNER JOIN memory_entries me ON m.id = me.memoryId\n"
            + "        WHERE me.entryId = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, entryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"memories",
        "memory_entries"}, new Callable<List<MemoryEntity>>() {
      @Override
      @NonNull
      public List<MemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUri");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfMoodSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "moodSummary");
          final int _cursorIndexOfTotalTimeSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeSpent");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfPhotoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "photoCount");
          final int _cursorIndexOfVideoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "videoCount");
          final int _cursorIndexOfPeopleInvolved = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleInvolved");
          final int _cursorIndexOfHighlights = CursorUtil.getColumnIndexOrThrow(_cursor, "highlights");
          final int _cursorIndexOfTrackerSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerSummary");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<MemoryEntity> _result = new ArrayList<MemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverImageUri;
            if (_cursor.isNull(_cursorIndexOfCoverImageUri)) {
              _tmpCoverImageUri = null;
            } else {
              _tmpCoverImageUri = _cursor.getString(_cursorIndexOfCoverImageUri);
            }
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final String _tmpMoodSummary;
            if (_cursor.isNull(_cursorIndexOfMoodSummary)) {
              _tmpMoodSummary = null;
            } else {
              _tmpMoodSummary = _cursor.getString(_cursorIndexOfMoodSummary);
            }
            final long _tmpTotalTimeSpent;
            _tmpTotalTimeSpent = _cursor.getLong(_cursorIndexOfTotalTimeSpent);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpPhotoCount;
            _tmpPhotoCount = _cursor.getInt(_cursorIndexOfPhotoCount);
            final int _tmpVideoCount;
            _tmpVideoCount = _cursor.getInt(_cursorIndexOfVideoCount);
            final String _tmpPeopleInvolved;
            if (_cursor.isNull(_cursorIndexOfPeopleInvolved)) {
              _tmpPeopleInvolved = null;
            } else {
              _tmpPeopleInvolved = _cursor.getString(_cursorIndexOfPeopleInvolved);
            }
            final String _tmpHighlights;
            if (_cursor.isNull(_cursorIndexOfHighlights)) {
              _tmpHighlights = null;
            } else {
              _tmpHighlights = _cursor.getString(_cursorIndexOfHighlights);
            }
            final String _tmpTrackerSummary;
            if (_cursor.isNull(_cursorIndexOfTrackerSummary)) {
              _tmpTrackerSummary = null;
            } else {
              _tmpTrackerSummary = _cursor.getString(_cursorIndexOfTrackerSummary);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MemoryEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCoverImageUri,_tmpStartDate,_tmpEndDate,_tmpLocation,_tmpLocationData,_tmpIsFavorite,_tmpColor,_tmpIcon,_tmpTags,_tmpMoodSummary,_tmpTotalTimeSpent,_tmpEntryCount,_tmpWordCount,_tmpPhotoCount,_tmpVideoCount,_tmpPeopleInvolved,_tmpHighlights,_tmpTrackerSummary,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getEntryCountForMemory(final long memoryId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM memory_entries WHERE memoryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, memoryId);
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
  public Flow<List<MemoryMilestoneEntity>> getMilestonesForMemory(final long memoryId) {
    final String _sql = "SELECT * FROM memory_milestones WHERE memoryId = ? ORDER BY milestoneDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, memoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"memory_milestones"}, new Callable<List<MemoryMilestoneEntity>>() {
      @Override
      @NonNull
      public List<MemoryMilestoneEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMemoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "memoryId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMilestoneDate = CursorUtil.getColumnIndexOrThrow(_cursor, "milestoneDate");
          final int _cursorIndexOfRelatedEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedEntryId");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<MemoryMilestoneEntity> _result = new ArrayList<MemoryMilestoneEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemoryMilestoneEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMemoryId;
            _tmpMemoryId = _cursor.getLong(_cursorIndexOfMemoryId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpMilestoneDate;
            _tmpMilestoneDate = _cursor.getLong(_cursorIndexOfMilestoneDate);
            final Long _tmpRelatedEntryId;
            if (_cursor.isNull(_cursorIndexOfRelatedEntryId)) {
              _tmpRelatedEntryId = null;
            } else {
              _tmpRelatedEntryId = _cursor.getLong(_cursorIndexOfRelatedEntryId);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new MemoryMilestoneEntity(_tmpId,_tmpMemoryId,_tmpTitle,_tmpDescription,_tmpMilestoneDate,_tmpRelatedEntryId,_tmpIcon,_tmpSortOrder);
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
  public Flow<List<MemoryTrackerEntity>> getTrackersForMemory(final long memoryId) {
    final String _sql = "SELECT * FROM memory_trackers WHERE memoryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, memoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"memory_trackers"}, new Callable<List<MemoryTrackerEntity>>() {
      @Override
      @NonNull
      public List<MemoryTrackerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMemoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "memoryId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfGoalValue = CursorUtil.getColumnIndexOrThrow(_cursor, "goalValue");
          final int _cursorIndexOfCurrentValue = CursorUtil.getColumnIndexOrThrow(_cursor, "currentValue");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final List<MemoryTrackerEntity> _result = new ArrayList<MemoryTrackerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemoryTrackerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMemoryId;
            _tmpMemoryId = _cursor.getLong(_cursorIndexOfMemoryId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final MemoryTrackerType _tmpType;
            _tmpType = __MemoryTrackerType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final String _tmpGoalValue;
            if (_cursor.isNull(_cursorIndexOfGoalValue)) {
              _tmpGoalValue = null;
            } else {
              _tmpGoalValue = _cursor.getString(_cursorIndexOfGoalValue);
            }
            final String _tmpCurrentValue;
            if (_cursor.isNull(_cursorIndexOfCurrentValue)) {
              _tmpCurrentValue = null;
            } else {
              _tmpCurrentValue = _cursor.getString(_cursorIndexOfCurrentValue);
            }
            final float _tmpProgress;
            _tmpProgress = _cursor.getFloat(_cursorIndexOfProgress);
            _item = new MemoryTrackerEntity(_tmpId,_tmpMemoryId,_tmpName,_tmpType,_tmpIcon,_tmpUnit,_tmpGoalValue,_tmpCurrentValue,_tmpProgress);
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
  public Object getTotalMemoryCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM memories";
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
  public Object getTotalEntriesInMemories(final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT SUM(entryCount) FROM memories\n"
            + "    ";
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
  public Object getTotalTimeSpentInMemories(final Continuation<? super Long> $completion) {
    final String _sql = "\n"
            + "        SELECT SUM(totalTimeSpent) FROM memories\n"
            + "    ";
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

  @Override
  public Flow<List<MemoryEntity>> searchMemories(final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM memories \n"
            + "        WHERE title LIKE '%' || ? || '%' OR description LIKE '%' || ? || '%'\n"
            + "        ORDER BY startDate DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"memories"}, new Callable<List<MemoryEntity>>() {
      @Override
      @NonNull
      public List<MemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUri");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLocationData = CursorUtil.getColumnIndexOrThrow(_cursor, "locationData");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfMoodSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "moodSummary");
          final int _cursorIndexOfTotalTimeSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeSpent");
          final int _cursorIndexOfEntryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "entryCount");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wordCount");
          final int _cursorIndexOfPhotoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "photoCount");
          final int _cursorIndexOfVideoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "videoCount");
          final int _cursorIndexOfPeopleInvolved = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleInvolved");
          final int _cursorIndexOfHighlights = CursorUtil.getColumnIndexOrThrow(_cursor, "highlights");
          final int _cursorIndexOfTrackerSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "trackerSummary");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<MemoryEntity> _result = new ArrayList<MemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverImageUri;
            if (_cursor.isNull(_cursorIndexOfCoverImageUri)) {
              _tmpCoverImageUri = null;
            } else {
              _tmpCoverImageUri = _cursor.getString(_cursorIndexOfCoverImageUri);
            }
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final String _tmpLocationData;
            if (_cursor.isNull(_cursorIndexOfLocationData)) {
              _tmpLocationData = null;
            } else {
              _tmpLocationData = _cursor.getString(_cursorIndexOfLocationData);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final String _tmpMoodSummary;
            if (_cursor.isNull(_cursorIndexOfMoodSummary)) {
              _tmpMoodSummary = null;
            } else {
              _tmpMoodSummary = _cursor.getString(_cursorIndexOfMoodSummary);
            }
            final long _tmpTotalTimeSpent;
            _tmpTotalTimeSpent = _cursor.getLong(_cursorIndexOfTotalTimeSpent);
            final int _tmpEntryCount;
            _tmpEntryCount = _cursor.getInt(_cursorIndexOfEntryCount);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final int _tmpPhotoCount;
            _tmpPhotoCount = _cursor.getInt(_cursorIndexOfPhotoCount);
            final int _tmpVideoCount;
            _tmpVideoCount = _cursor.getInt(_cursorIndexOfVideoCount);
            final String _tmpPeopleInvolved;
            if (_cursor.isNull(_cursorIndexOfPeopleInvolved)) {
              _tmpPeopleInvolved = null;
            } else {
              _tmpPeopleInvolved = _cursor.getString(_cursorIndexOfPeopleInvolved);
            }
            final String _tmpHighlights;
            if (_cursor.isNull(_cursorIndexOfHighlights)) {
              _tmpHighlights = null;
            } else {
              _tmpHighlights = _cursor.getString(_cursorIndexOfHighlights);
            }
            final String _tmpTrackerSummary;
            if (_cursor.isNull(_cursorIndexOfTrackerSummary)) {
              _tmpTrackerSummary = null;
            } else {
              _tmpTrackerSummary = _cursor.getString(_cursorIndexOfTrackerSummary);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MemoryEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCoverImageUri,_tmpStartDate,_tmpEndDate,_tmpLocation,_tmpLocationData,_tmpIsFavorite,_tmpColor,_tmpIcon,_tmpTags,_tmpMoodSummary,_tmpTotalTimeSpent,_tmpEntryCount,_tmpWordCount,_tmpPhotoCount,_tmpVideoCount,_tmpPeopleInvolved,_tmpHighlights,_tmpTrackerSummary,_tmpCreatedAt,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __MemoryTrackerType_enumToString(@NonNull final MemoryTrackerType _value) {
    switch (_value) {
      case COUNT: return "COUNT";
      case DURATION: return "DURATION";
      case DISTANCE: return "DISTANCE";
      case EXPENSE: return "EXPENSE";
      case RATING: return "RATING";
      case CUSTOM: return "CUSTOM";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private MemoryTrackerType __MemoryTrackerType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "COUNT": return MemoryTrackerType.COUNT;
      case "DURATION": return MemoryTrackerType.DURATION;
      case "DISTANCE": return MemoryTrackerType.DISTANCE;
      case "EXPENSE": return MemoryTrackerType.EXPENSE;
      case "RATING": return MemoryTrackerType.RATING;
      case "CUSTOM": return MemoryTrackerType.CUSTOM;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
