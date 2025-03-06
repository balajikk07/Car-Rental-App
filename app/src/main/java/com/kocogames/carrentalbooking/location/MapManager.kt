package com.kocogames.carrentalbooking.location

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapManager(private val googleMap: GoogleMap) {
    private var pickupMarker: Marker? = null

    fun initializeMap(onMapClick: (LatLng) -> Unit, onMarkerDragEnd: (LatLng) -> Unit) {
        googleMap.uiSettings.isZoomControlsEnabled = true

        googleMap.setOnMapClickListener { latLng -> onMapClick(latLng) }

        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                onMarkerDragEnd(marker.position)
            }
        })
    }

    fun placeMarker(latLng: LatLng, title: String) {
        pickupMarker?.remove()
        pickupMarker = googleMap.addMarker(
            MarkerOptions().position(latLng).title(title).draggable(true)
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
    }
}
