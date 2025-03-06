package com.kocogames.carrentalbooking.location

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

class PlacesAutocompleteManager(
    private val context: Context,
    private val placesClient: PlacesClient
) {
    private val sessionToken = AutocompleteSessionToken.newInstance()
    private val autocompleteAdapter: ArrayAdapter<String> =
        ArrayAdapter(context, android.R.layout.simple_list_item_1)
    private val placeIds = mutableMapOf<String, String>()

    fun setupAutoComplete(
        autoCompleteTextView: AutoCompleteTextView,
        onPlaceSelected: (LatLng, String) -> Unit
    ) {
        autoCompleteTextView.setAdapter(autocompleteAdapter)

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) return

                val request = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(sessionToken)
                    .setQuery(s.toString())
                    .build()

                placesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener { response ->
                        autocompleteAdapter.clear()
                        placeIds.clear()
                        response.autocompletePredictions.forEach { prediction ->
                            val primaryText = prediction.getPrimaryText(null).toString()
                            placeIds[primaryText] = prediction.placeId
                            autocompleteAdapter.add(primaryText)
                        }
                    }
            }
        })

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedPlaceName = autocompleteAdapter.getItem(position)
            val selectedPlaceId = placeIds[selectedPlaceName]

            selectedPlaceId?.let { placeId ->
                fetchPlaceDetails(placeId, onPlaceSelected)
            }
        }
    }

    private fun fetchPlaceDetails(placeId: String, onPlaceSelected: (LatLng, String) -> Unit) {
        val request = FetchPlaceRequest.newInstance(placeId, listOf(Place.Field.LAT_LNG, Place.Field.NAME))
        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            place.latLng?.let { latLng ->
                onPlaceSelected(latLng, place.name ?: "")
            }
        }
    }
}
