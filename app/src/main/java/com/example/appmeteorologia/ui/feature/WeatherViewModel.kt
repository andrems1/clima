package com.example.appmeteorologia.ui.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmeteorologia.data.location.LocationService
import com.example.appmeteorologia.data.remote.response.Coord
import com.example.appmeteorologia.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    fun fetchWeatherData() {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading

            try {
                val location = locationService.getCurrentLocation()

                if (location != null) {
                    val weatherInfo = weatherRepository.getWeatherData(
                        location.latitude,
                        location.longitude
                    )
                    _weatherState.value = WeatherState.Success(weatherInfo)
                } else {
                    // Fallback to a default location if no location found
                    val weatherInfo = weatherRepository.getWeatherData(37.7749, -122.4194)
                    _weatherState.value = WeatherState.Success(weatherInfo)
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(
                    "Erro ao buscar dados meteorológicos: ${e.localizedMessage}"
                )
            }
        }
    }
}