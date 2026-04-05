package com.dairum.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "location") val location: WeatherLocation,
    @Json(name = "current") val current: CurrentWeather
)

@JsonClass(generateAdapter = true)
data class WeatherLocation(
    @Json(name = "name") val name: String,
    @Json(name = "region") val region: String,
    @Json(name = "country") val country: String,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
    @Json(name = "tz_id") val tzId: String,
    @Json(name = "localtime") val localTime: String
)

@JsonClass(generateAdapter = true)
data class CurrentWeather(
    @Json(name = "temp_c") val tempCelsius: Double,
    @Json(name = "temp_f") val tempFahrenheit: Double,
    @Json(name = "is_day") val isDay: Int,
    @Json(name = "condition") val condition: WeatherCondition,
    @Json(name = "wind_kph") val windKph: Double,
    @Json(name = "wind_mph") val windMph: Double,
    @Json(name = "wind_dir") val windDirection: String,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "cloud") val cloudCover: Int,
    @Json(name = "feelslike_c") val feelsLikeCelsius: Double,
    @Json(name = "feelslike_f") val feelsLikeFahrenheit: Double,
    @Json(name = "uv") val uvIndex: Double
)

@JsonClass(generateAdapter = true)
data class WeatherCondition(
    @Json(name = "text") val text: String,
    @Json(name = "icon") val icon: String, // URL like "//cdn.weatherapi.com/weather/64x64/day/113.png"
    @Json(name = "code") val code: Int
)
