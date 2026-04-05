package com.dairum.presentation.screens.settings;

import com.dairum.domain.repository.ApiKeyRepository;
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
public final class ApiKeySettingsViewModel_Factory implements Factory<ApiKeySettingsViewModel> {
  private final Provider<ApiKeyRepository> apiKeyRepositoryProvider;

  public ApiKeySettingsViewModel_Factory(Provider<ApiKeyRepository> apiKeyRepositoryProvider) {
    this.apiKeyRepositoryProvider = apiKeyRepositoryProvider;
  }

  @Override
  public ApiKeySettingsViewModel get() {
    return newInstance(apiKeyRepositoryProvider.get());
  }

  public static ApiKeySettingsViewModel_Factory create(
      Provider<ApiKeyRepository> apiKeyRepositoryProvider) {
    return new ApiKeySettingsViewModel_Factory(apiKeyRepositoryProvider);
  }

  public static ApiKeySettingsViewModel newInstance(ApiKeyRepository apiKeyRepository) {
    return new ApiKeySettingsViewModel(apiKeyRepository);
  }
}
