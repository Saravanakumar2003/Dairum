package com.dairum.data.service;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.NotificationDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class SmartReminderWorker_Factory {
  private final Provider<NotificationDao> notificationDaoProvider;

  private final Provider<JournalEntryDao> journalEntryDaoProvider;

  public SmartReminderWorker_Factory(Provider<NotificationDao> notificationDaoProvider,
      Provider<JournalEntryDao> journalEntryDaoProvider) {
    this.notificationDaoProvider = notificationDaoProvider;
    this.journalEntryDaoProvider = journalEntryDaoProvider;
  }

  public SmartReminderWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, notificationDaoProvider.get(), journalEntryDaoProvider.get());
  }

  public static SmartReminderWorker_Factory create(
      Provider<NotificationDao> notificationDaoProvider,
      Provider<JournalEntryDao> journalEntryDaoProvider) {
    return new SmartReminderWorker_Factory(notificationDaoProvider, journalEntryDaoProvider);
  }

  public static SmartReminderWorker newInstance(Context context, WorkerParameters workerParams,
      NotificationDao notificationDao, JournalEntryDao journalEntryDao) {
    return new SmartReminderWorker(context, workerParams, notificationDao, journalEntryDao);
  }
}
