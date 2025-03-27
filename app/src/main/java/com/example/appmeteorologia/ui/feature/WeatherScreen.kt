package com.example.appmeteorologia.ui.feature

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmeteorologia.model.WeatherInfo

@Composable
fun WeatherRoute(
    viewModel: WeatherViewModel = viewModel()
) {
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()

    when (val state = weatherState) {
        is WeatherState.Loading -> LoadingScreen()
        is WeatherState.Success -> WeatherScreen(weatherInfo = state.weatherInfo)
        is WeatherState.Error -> ErrorScreen(message = state.message)
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            color = Color.Red,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun WeatherScreen(weatherInfo: WeatherInfo) {
    // Implementação similar à anterior, adicionando novos detalhes
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Temperatura: ${weatherInfo.locationName}°C")
        Text("Temperatura: ${weatherInfo.temperature}°C")
        Text("Sensação térmica: ${weatherInfo.feelsLikeTemperature}°C")
        Text("Umidade: ${weatherInfo.humidity}%")
        Text("Velocidade do vento: ${weatherInfo.windSpeed} m/s")
        Text("Temp. Mínima: ${weatherInfo.tempMin}°C")
        Text("Temp. Máxima: ${weatherInfo.tempMax}°C")
    }
}

