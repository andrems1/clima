package com.example.appmeteorologia.ui.feature

import com.example.appmeteorologia.model.WeatherInfo

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weatherInfo: WeatherInfo) : WeatherState()
    data class Error(val message: String) : WeatherState()
}