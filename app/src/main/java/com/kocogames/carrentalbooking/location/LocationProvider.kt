package com.kocogames.carrentalbooking.location

import com.google.android.gms.maps.model.LatLng

interface LocationProvider {
    fun getCurrentLocation(callback: (LatLng?) -> Unit)
}


