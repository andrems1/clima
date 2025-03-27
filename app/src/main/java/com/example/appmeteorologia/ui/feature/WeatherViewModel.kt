package com.example.appmeteorologia.ui.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmeteorologia.data.location.LocationService
import com.example.appmeteorologia.data.repository.WeatherRepository
import com.example.appmeteorologia.model.WeatherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    init {
        fetchWeatherData()
    }

    fun fetchWeatherData(lat: Double? = null, lng: Double? = null) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                val (latitude, longitude) = getCoordinates(lat, lng)

                Log.d("WeatherViewModel", "Buscando dados para: lat=$latitude, lng=$longitude")

                val weatherInfo = weatherRepository.getWeatherData(latitude, longitude)
                _weatherState.value = WeatherState.Success(weatherInfo)

            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Erro ao buscar dados meteorológicos", e)
                _weatherState.value = WeatherState.Error(
                    "Erro ao buscar dados meteorológicos: ${e.localizedMessage ?: "Erro desconhecido"}"
                )
            }
        }
    }

    private suspend fun getCoordinates(lat: Double?, lng: Double?): Pair<Double, Double> {
        return if (lat != null && lng != null) {
            Pair(lat, lng)
        } else {
            val location = locationService.getCurrentLocation()
            if (location != null) {
                Pair(location.latitude, location.longitude)
            } else {
                Log.w("WeatherViewModel", "Localização não disponível, usando fallback")
                Pair(-23.5505, -46.6333) // Coordenadas de São Paulo como padrão
            }
        }
    }

    fun fetchWeatherByCity(cityName: String) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                val weatherInfo = weatherRepository.getWeatherDataByCity(cityName)
                _weatherState.value = WeatherState.Success(weatherInfo)
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("Erro ao buscar clima para $cityName: ${e.localizedMessage}")
            }
        }
    }




}
