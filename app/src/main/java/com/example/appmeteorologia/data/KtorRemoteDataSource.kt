package com.example.appmeteorologia.data

import com.example.appmeteorologia.data.remote.RemoteDataSource
import com.example.appmeteorologia.data.remote.response.WeatherDataResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class KtorRemoteDataSource @Inject constructor(
    private val httpClient: HttpClient
) : RemoteDataSource {

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5"
        private const val API_KEY = "0c2fc6938e4ed0e7bc37764904ec6895"
    }

    override suspend fun getWeatherDataResponse(lat: Double, lng: Double): WeatherDataResponse {
        return httpClient
            .get("$BASE_URL/weather?lat=$lat&lon=$lng&appid=$API_KEY&units=metric")
            .body()
    }

    override suspend fun getWeatherDataByCityName(cityName: String): WeatherDataResponse {
        return httpClient
            .get("$BASE_URL/weather?q=$cityName&appid=$API_KEY&units=metric")
            .body()
    }
}
