package com.example.appmeteorologia.data.repository

import com.example.appmeteorologia.data.remote.RemoteDataSource
import com.example.appmeteorologia.model.WeatherInfo
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, lng: Double): WeatherInfo {
        val response = remoteDataSource.getWeatherDataResponse(lat, lng)
        val weather = response.weather[0]

        return WeatherInfo(
            locationName = response.name,
            conditionIcon = weather.icon,
            condition = weather.main,
            temperature = response.main.temp.roundToInt(),
            feelsLikeTemperature = response.main.feelsLike.roundToInt(),
            humidity = response.main.humidity,
            windSpeed = response.wind.speed,
            dayOfWeek = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            isDay = weather.icon.last() == 'd',
            tempMin = response.main.tempMin.roundToInt(),
            tempMax = response.main.tempMax.roundToInt()
        )
    }
}