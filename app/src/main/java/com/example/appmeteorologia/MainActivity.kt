package com.example.appmeteorologia

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmeteorologia.model.WeatherInfo
import com.example.appmeteorologia.ui.feature.WeatherState
import com.example.appmeteorologia.ui.feature.WeatherViewModel
import com.example.appmeteorologia.ui.theme.AppMeteorologiaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppMeteorologiaTheme {
                WeatherScreen(weatherViewModel)
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    var cityName by remember { mutableStateOf("") }
    val weatherState by viewModel.weatherState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Digite o nome da cidade") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.fetchWeatherByCity(cityName) }, modifier = Modifier.fillMaxWidth()) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (weatherState) {
            is WeatherState.Loading -> CircularProgressIndicator()
            is WeatherState.Success -> {
                val weather = (weatherState as WeatherState.Success).weatherInfo
                WeatherInfoDisplay(weather)
            }
            is WeatherState.Error -> {
                Text((weatherState as WeatherState.Error).message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun WeatherInfoDisplay(weather: WeatherInfo) {
    Column {
        Text("Cidade: ${weather.locationName}")
        Text("Condição: ${weather.condition}")
        Text("Temperatura: ${weather.temperature}°C")
        Text("Sensação térmica: ${weather.feelsLikeTemperature}°C")
        Text("Umidade: ${weather.humidity}%")
        Text("Velocidade do vento: ${String.format("%.2f", weather.windSpeed * 3.6)} km/h")
        Text("Temp. Mínima: ${weather.tempMin}°C")
        Text("Temp. Máxima: ${weather.tempMax}°C")
        Text("Dia da Semana: ${weather.dayOfWeek}")
        Text("Momento do dia: ${if (weather.isDay) "Dia" else "Noite"}")

    }
}
