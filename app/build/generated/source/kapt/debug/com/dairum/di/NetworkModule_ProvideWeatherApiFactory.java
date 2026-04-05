package com.dairum.di;

import com.dairum.data.remote.api.WeatherApi;
import com.squareup.moshi.Moshi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideWeatherApiFactory implements Factory<WeatherApi> {
  private final Provider<Moshi> moshiProvider;

  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_ProvideWeatherApiFactory(Provider<Moshi> moshiProvider,
      Provider<OkHttpClient> okHttpClientProvider) {
    this.moshiProvider = moshiProvider;
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public WeatherApi get() {
    return provideWeatherApi(moshiProvider.get(), okHttpClientProvider.get());
  }

  public static NetworkModule_ProvideWeatherApiFactory create(Provider<Moshi> moshiProvider,
      Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvideWeatherApiFactory(moshiProvider, okHttpClientProvider);
  }

  public static WeatherApi provideWeatherApi(Moshi moshi, OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideWeatherApi(moshi, okHttpClient));
  }
}
