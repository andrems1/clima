package com.example.appmeteorologia.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationService @Inject constructor(
    private val context: Context
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission()) {
            Log.e("LocationService", "Permissões de localização não concedidas.")
            return null
        }

        return try {
            // Solicita a localização mais recente possível
            val locationRequest = LocationRequest.PRIORITY_HIGH_ACCURACY
            val currentLocation = fusedLocationClient.getCurrentLocation(locationRequest, null).await()

            if (currentLocation != null) {
                return currentLocation
            }

            // Se falhar, tenta pegar a última localização conhecida
            Log.w("LocationService", "Localização atual indisponível, tentando última localização conhecida.")
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            Log.e("LocationService", "Erro ao obter localização: ${e.localizedMessage}")
            null
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}
