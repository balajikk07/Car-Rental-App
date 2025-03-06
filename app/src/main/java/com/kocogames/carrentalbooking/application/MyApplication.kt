package com.kocogames.carrentalbooking.application

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.kocogames.carrentalbooking.R

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
    }
}
