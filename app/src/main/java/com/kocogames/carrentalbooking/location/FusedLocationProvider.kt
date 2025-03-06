package com.kocogames.carrentalbooking.location

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class FusedLocationProvider(private val context: Context) : LocationProvider {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun getCurrentLocation(callback: (LatLng?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                callback(location?.let { LatLng(it.latitude, it.longitude) })
            }
        } else {
            callback(null)
        }
    }
}