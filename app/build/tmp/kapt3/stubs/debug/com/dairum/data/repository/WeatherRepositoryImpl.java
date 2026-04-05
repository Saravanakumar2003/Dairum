package com.dairum.data.repository;

import com.dairum.BuildConfig;
import com.dairum.data.remote.api.WeatherApi;
import com.dairum.domain.model.Weather;
import com.dairum.domain.repository.ApiKeyRepository;
import com.dairum.domain.repository.WeatherRepository;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\bH\u0082@\u00a2\u0006\u0002\u0010\tJ,\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0010\u0010\u0011J$\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0013\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0016"}, d2 = {"Lcom/dairum/data/repository/WeatherRepositoryImpl;", "Lcom/dairum/domain/repository/WeatherRepository;", "weatherApi", "Lcom/dairum/data/remote/api/WeatherApi;", "apiKeyRepository", "Lcom/dairum/domain/repository/ApiKeyRepository;", "(Lcom/dairum/data/remote/api/WeatherApi;Lcom/dairum/domain/repository/ApiKeyRepository;)V", "getApiKey", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCurrentWeather", "Lkotlin/Result;", "Lcom/dairum/domain/model/Weather;", "latitude", "", "longitude", "getCurrentWeather-0E7RQCE", "(DDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCurrentWeatherByCity", "city", "getCurrentWeatherByCity-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class WeatherRepositoryImpl implements com.dairum.domain.repository.WeatherRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.remote.api.WeatherApi weatherApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.ApiKeyRepository apiKeyRepository = null;
    
    @javax.inject.Inject()
    public WeatherRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.remote.api.WeatherApi weatherApi, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.ApiKeyRepository apiKeyRepository) {
        super();
    }
    
    private final java.lang.Object getApiKey(kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
}