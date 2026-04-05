package com.dairum.data.repository;

import android.content.Context;
import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.NotificationDao;
import com.dairum.data.local.dao.PersonDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class NotificationRepositoryImpl_Factory implements Factory<NotificationRepositoryImpl> {
  private final Provider<NotificationDao> notificationDaoProvider;

  private final Provider<JournalEntryDao> journalEntryDaoProvider;

  private final Provider<PersonDao> personDaoProvider;

  private final Provider<Context> contextProvider;

  public NotificationRepositoryImpl_Factory(Provider<NotificationDao> notificationDaoProvider,
      Provider<JournalEntryDao> journalEntryDaoProvider, Provider<PersonDao> personDaoProvider,
      Provider<Context> contextProvider) {
    this.notificationDaoProvider = notificationDaoProvider;
    this.journalEntryDaoProvider = journalEntryDaoProvider;
    this.personDaoProvider = personDaoProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public NotificationRepositoryImpl get() {
    return newInstance(notificationDaoProvider.get(), journalEntryDaoProvider.get(), personDaoProvider.get(), contextProvider.get());
  }

  public static NotificationRepositoryImpl_Factory create(
      Provider<NotificationDao> notificationDaoProvider,
      Provider<JournalEntryDao> journalEntryDaoProvider, Provider<PersonDao> personDaoProvider,
      Provider<Context> contextProvider) {
    return new NotificationRepositoryImpl_Factory(notificationDaoProvider, journalEntryDaoProvider, personDaoProvider, contextProvider);
  }

  public static NotificationRepositoryImpl newInstance(NotificationDao notificationDao,
      JournalEntryDao journalEntryDao, PersonDao personDao, Context context) {
    return new NotificationRepositoryImpl(notificationDao, journalEntryDao, personDao, context);
  }
}
