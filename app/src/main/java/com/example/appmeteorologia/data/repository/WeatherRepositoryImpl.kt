package com.example.appmeteorologia.data.repository

import android.util.Log
import com.example.appmeteorologia.data.remote.RemoteDataSource
import com.example.appmeteorologia.model.WeatherInfo
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, lng: Double): WeatherInfo {
        return try {
            val response = remoteDataSource.getWeatherDataResponse(lat, lng)

            // Verifica se a resposta é válida
            if (response.weather.isEmpty()) {
                throw Exception("Nenhuma condição climática encontrada na resposta.")
            }

            val weather = response.weather.first() // Pega o primeiro item com segurança
            val main = response.main
            val wind = response.wind

            WeatherInfo(
                locationName = response.name.ifBlank { "Local desconhecido" },
                conditionIcon = weather.icon,
                condition = weather.main,
                temperature = main.temp.roundToInt(),
                feelsLikeTemperature = main.feelsLike.roundToInt(),
                humidity = main.humidity,
                windSpeed = wind.speed,
                dayOfWeek = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                isDay = weather.icon.lastOrNull() == 'd',
                tempMin = main.tempMin.roundToInt(),
                tempMax = main.tempMax.roundToInt()
            )
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Erro ao obter dados meteorológicos: ${e.localizedMessage}")

            // Retorna um WeatherInfo com valores padrão para evitar falhas no app
            WeatherInfo(
                locationName = "Desconhecido",
                conditionIcon = "unknown",
                condition = "Indisponível",
                temperature = 0,
                feelsLikeTemperature = 0,
                humidity = 0,
                windSpeed = 0.0,
                dayOfWeek = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                isDay = true,
                tempMin = 0,
                tempMax = 0
            )
        }
    }

    override suspend fun getWeatherDataByCity(cityName: String): WeatherInfo {
        val response = remoteDataSource.getWeatherDataByCityName(cityName)
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
