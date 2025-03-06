package com.kocogames.carrentalbooking.location

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale

class GeocoderHelper(private val context: Context) {
    fun getAddressFromLatLng(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses?.firstOrNull()?.let { address ->
                Log.d("GeocoderHelper", "addresssubAdminArea: " + address.subAdminArea)
                Log.d("GeocoderHelper", "addresslocality: " + address.locality)
                Log.d("GeocoderHelper", "addressadminArea: " + address.adminArea)
                Log.d("GeocoderHelper", "addresssubLocality: " + address.subLocality)
                Log.d("GeocoderHelper", "addressurl: " + address.url)
                Log.d("GeocoderHelper", "addressfeatureName: " + address.featureName)
                Log.d("GeocoderHelper", "addresspostalCode: " + address.postalCode)
                Log.d("GeocoderHelper", "addresspremises: " + address.premises)
                Log.d("GeocoderHelper", "addresssubThoroughfare: " + address.subThoroughfare)
                Log.d("GeocoderHelper", "addressthoroughfare: " + address.thoroughfare)

                val landmark = address.thoroughfare
                    ?: address.subThoroughfare
                    ?: address.premises
                    ?: address.subLocality
                    ?: address.locality

                val state = address.adminArea
                if (landmark != null) "$landmark,$state" else state
            } ?: "${latLng.latitude}, ${latLng.longitude}"

        } catch (e: IOException) {
            "${latLng.latitude}, ${latLng.longitude}"
        }
    }
}
