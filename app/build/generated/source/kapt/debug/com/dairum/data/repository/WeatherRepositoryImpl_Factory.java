package com.dairum.data.repository;

import com.dairum.data.remote.api.WeatherApi;
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
public final class WeatherRepositoryImpl_Factory implements Factory<WeatherRepositoryImpl> {
  private final Provider<WeatherApi> weatherApiProvider;

  private final Provider<ApiKeyRepository> apiKeyRepositoryProvider;

  public WeatherRepositoryImpl_Factory(Provider<WeatherApi> weatherApiProvider,
      Provider<ApiKeyRepository> apiKeyRepositoryProvider) {
    this.weatherApiProvider = weatherApiProvider;
    this.apiKeyRepositoryProvider = apiKeyRepositoryProvider;
  }

  @Override
  public WeatherRepositoryImpl get() {
    return newInstance(weatherApiProvider.get(), apiKeyRepositoryProvider.get());
  }

  public static WeatherRepositoryImpl_Factory create(Provider<WeatherApi> weatherApiProvider,
      Provider<ApiKeyRepository> apiKeyRepositoryProvider) {
    return new WeatherRepositoryImpl_Factory(weatherApiProvider, apiKeyRepositoryProvider);
  }

  public static WeatherRepositoryImpl newInstance(WeatherApi weatherApi,
      ApiKeyRepository apiKeyRepository) {
    return new WeatherRepositoryImpl(weatherApi, apiKeyRepository);
  }
}
