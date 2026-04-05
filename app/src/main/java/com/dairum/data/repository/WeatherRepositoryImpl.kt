package com.dairum.data.repository

import com.dairum.BuildConfig
import com.dairum.data.remote.api.WeatherApi
import com.dairum.domain.model.Weather
import com.dairum.domain.repository.ApiKeyRepository
import com.dairum.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val apiKeyRepository: ApiKeyRepository
) : WeatherRepository {

    private suspend fun getApiKey(): String {
        // First try to get from user settings, fallback to BuildConfig
        return apiKeyRepository.getWeatherApiKey().first()
            ?: BuildConfig.WEATHER_API_KEY
    }

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Result<Weather> {
        return try {
            val apiKey = getApiKey()
            if (apiKey.isBlank() || apiKey == "YOUR_WEATHER_API_KEY") {
                return Result.failure(Exception("Weather API key not configured. Please set it in Settings > API Keys."))
            }
            
            val location = "$latitude,$longitude"
            val response = weatherApi.getCurrentWeather(
                apiKey = apiKey,
                location = location
            )
            Result.success(
                Weather(
                    temperature = response.current.tempCelsius,
                    temperatureFahrenheit = response.current.tempFahrenheit,
                    condition = response.current.condition.text,
                    icon = "https:${response.current.condition.icon}",
                    humidity = response.current.humidity,
                    feelsLike = response.current.feelsLikeCelsius,
                    locationName = response.location.name
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentWeatherByCity(city: String): Result<Weather> {
        return try {
            val apiKey = getApiKey()
            if (apiKey.isBlank() || apiKey == "YOUR_WEATHER_API_KEY") {
                return Result.failure(Exception("Weather API key not configured. Please set it in Settings > API Keys."))
            }
            
            val response = weatherApi.getCurrentWeather(
                apiKey = apiKey,
                location = city
            )
            Result.success(
                Weather(
                    temperature = response.current.tempCelsius,
                    temperatureFahrenheit = response.current.tempFahrenheit,
                    condition = response.current.condition.text,
                    icon = "https:${response.current.condition.icon}",
                    humidity = response.current.humidity,
                    feelsLike = response.current.feelsLikeCelsius,
                    locationName = response.location.name
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
