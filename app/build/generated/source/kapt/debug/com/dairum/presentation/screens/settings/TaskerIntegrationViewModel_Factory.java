package com.dairum.presentation.screens.settings;

import com.dairum.domain.repository.SettingsRepository;
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
public final class TaskerIntegrationViewModel_Factory implements Factory<TaskerIntegrationViewModel> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public TaskerIntegrationViewModel_Factory(
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public TaskerIntegrationViewModel get() {
    return newInstance(settingsRepositoryProvider.get());
  }

  public static TaskerIntegrationViewModel_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new TaskerIntegrationViewModel_Factory(settingsRepositoryProvider);
  }

  public static TaskerIntegrationViewModel newInstance(SettingsRepository settingsRepository) {
    return new TaskerIntegrationViewModel(settingsRepository);
  }
}
