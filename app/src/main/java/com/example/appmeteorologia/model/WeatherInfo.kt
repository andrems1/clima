package com.example.appmeteorologia.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfo(
    val locationName: String,
    val conditionIcon: String,
    val condition: String,
    val temperature: Int,
    val feelsLikeTemperature: Int,
    val humidity: Int,
    val windSpeed: Double,
    val dayOfWeek: String,
    val isDay: Boolean,
    val tempMin: Int,
    val tempMax: Int,
    val uvIndex: Int? = null,
    val airQuality: AirQualityInfo? = null,
    val precipitation: Double? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null
)

@Serializable
data class HourlyForecast(
    val time: Long,
    val temperature: Double,
    val feelsLike: Double,
    val condition: String,
    val conditionIcon: String,
    val precipitation: Double,
    val windSpeed: Double,
    val uvIndex: Int
)

@Serializable
data class DailyForecast(
    val date: Long,
    val tempMin: Double,
    val tempMax: Double,
    val condition: String,
    val conditionIcon: String,
    val precipitation: Double,
    val windSpeed: Double,
    val uvIndex: Int
)

@Serializable
data class AirQualityInfo(
    val aqi: Int, // Air Quality Index
    val co: Double, // Carbon Monoxide
    val no2: Double, // Nitrogen Dioxide
    val o3: Double, // Ozone
    val pm2_5: Double, // Particulate Matter 2.5
    val pm10: Double, // Particulate Matter 10
    val so2: Double // Sulfur Dioxide
)