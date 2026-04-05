package com.dairum.data.remote.api

import com.dairum.data.remote.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String, // Can be lat,lng or city name
        @Query("aqi") includeAqi: String = "no"
    ): WeatherResponse
}
