package com.dairum.data.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.dairum.domain.model.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    
    private val geocoder: Geocoder by lazy {
        Geocoder(context, Locale.getDefault())
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    suspend fun getCurrentLocation(): Result<Location> = suspendCancellableCoroutine { continuation ->
        android.util.Log.d("LocationService", "getCurrentLocation called")
        if (!hasLocationPermission()) {
            android.util.Log.e("LocationService", "Location permission not granted")
            continuation.resume(Result.failure(SecurityException("Location permission not granted")))
            return@suspendCancellableCoroutine
        }

        val cancellationToken = CancellationTokenSource()
        
        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationToken.token
            ).addOnSuccessListener { androidLocation ->
                if (androidLocation != null) {
                    android.util.Log.d("LocationService", "Got raw location: lat=${androidLocation.latitude}, lng=${androidLocation.longitude}")
                    val location = Location(
                        latitude = androidLocation.latitude,
                        longitude = androidLocation.longitude
                    )
                    
                    // Try to get address from coordinates
                    try {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(
                            androidLocation.latitude,
                            androidLocation.longitude,
                            1
                        )
                        
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val resultLocation = location.copy(
                                address = address.getAddressLine(0),
                                placeName = address.featureName ?: address.locality
                            )
                            android.util.Log.d("LocationService", "Location with address: place=${resultLocation.placeName}, address=${resultLocation.address}")
                            continuation.resume(Result.success(resultLocation))
                        } else {
                            android.util.Log.d("LocationService", "No addresses found, returning raw location")
                            continuation.resume(Result.success(location))
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("LocationService", "Geocoding failed: ${e.message}")
                        // Geocoding failed, return location without address
                        continuation.resume(Result.success(location))
                    }
                } else {
                    android.util.Log.e("LocationService", "Android location is null")
                    continuation.resume(Result.failure(Exception("Unable to get location")))
                }
            }.addOnFailureListener { exception ->
                android.util.Log.e("LocationService", "Failed to get location: ${exception.message}")
                continuation.resume(Result.failure(exception))
            }
        } catch (e: SecurityException) {
            android.util.Log.e("LocationService", "Security exception: ${e.message}")
            continuation.resume(Result.failure(e))
        }

        continuation.invokeOnCancellation {
            cancellationToken.cancel()
        }
    }

    suspend fun getAddressFromCoordinates(latitude: Double, longitude: Double): String? {
        return try {
            android.util.Log.d("LocationService", "Reverse geocoding: lat=$latitude, lng=$longitude")
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addresses?.firstOrNull()?.getAddressLine(0)
            android.util.Log.d("LocationService", "Reverse geocode result: $address")
            address
        } catch (e: Exception) {
            android.util.Log.e("LocationService", "Reverse geocode error: ${e.message}")
            null
        }
    }

    suspend fun searchLocation(query: String): List<Location> {
        return try {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(query, 5)
            addresses?.map { address ->
                Location(
                    latitude = address.latitude,
                    longitude = address.longitude,
                    address = address.getAddressLine(0),
                    placeName = address.featureName ?: address.locality
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
