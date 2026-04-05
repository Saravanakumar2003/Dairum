package com.dairum.presentation.screens.settings;

import com.dairum.data.security.AppLockManager;
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
public final class SecuritySettingsViewModel_Factory implements Factory<SecuritySettingsViewModel> {
  private final Provider<AppLockManager> appLockManagerProvider;

  public SecuritySettingsViewModel_Factory(Provider<AppLockManager> appLockManagerProvider) {
    this.appLockManagerProvider = appLockManagerProvider;
  }

  @Override
  public SecuritySettingsViewModel get() {
    return newInstance(appLockManagerProvider.get());
  }

  public static SecuritySettingsViewModel_Factory create(
      Provider<AppLockManager> appLockManagerProvider) {
    return new SecuritySettingsViewModel_Factory(appLockManagerProvider);
  }

  public static SecuritySettingsViewModel newInstance(AppLockManager appLockManager) {
    return new SecuritySettingsViewModel(appLockManager);
  }
}
