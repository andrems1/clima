package com.example.appmeteorologia.model

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
    val tempMax: Int
)