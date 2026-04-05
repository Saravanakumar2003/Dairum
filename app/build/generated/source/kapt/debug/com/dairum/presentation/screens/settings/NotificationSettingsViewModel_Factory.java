package com.dairum.presentation.screens.settings;

import com.dairum.domain.repository.NotificationRepository;
import com.dairum.domain.repository.PersonRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class NotificationSettingsViewModel_Factory implements Factory<NotificationSettingsViewModel> {
  private final Provider<NotificationRepository> notificationRepositoryProvider;

  private final Provider<PersonRepository> personRepositoryProvider;

  public NotificationSettingsViewModel_Factory(
      Provider<NotificationRepository> notificationRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider) {
    this.notificationRepositoryProvider = notificationRepositoryProvider;
    this.personRepositoryProvider = personRepositoryProvider;
  }

  @Override
  public NotificationSettingsViewModel get() {
    return newInstance(notificationRepositoryProvider.get(), personRepositoryProvider.get());
  }

  public static NotificationSettingsViewModel_Factory create(
      Provider<NotificationRepository> notificationRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider) {
    return new NotificationSettingsViewModel_Factory(notificationRepositoryProvider, personRepositoryProvider);
  }

  public static NotificationSettingsViewModel newInstance(
      NotificationRepository notificationRepository, PersonRepository personRepository) {
    return new NotificationSettingsViewModel(notificationRepository, personRepository);
  }
}
