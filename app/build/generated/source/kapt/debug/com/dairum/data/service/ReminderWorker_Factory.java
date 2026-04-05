package com.dairum.data.service;

import android.content.Context;
import androidx.work.WorkerParameters;
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
public final class ReminderWorker_Factory {
  private final Provider<NotificationDao> notificationDaoProvider;

  public ReminderWorker_Factory(Provider<NotificationDao> notificationDaoProvider) {
    this.notificationDaoProvider = notificationDaoProvider;
  }

  public ReminderWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, notificationDaoProvider.get());
  }

  public static ReminderWorker_Factory create(Provider<NotificationDao> notificationDaoProvider) {
    return new ReminderWorker_Factory(notificationDaoProvider);
  }

  public static ReminderWorker newInstance(Context context, WorkerParameters workerParams,
      NotificationDao notificationDao) {
    return new ReminderWorker(context, workerParams, notificationDao);
  }
}
