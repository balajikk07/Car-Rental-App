package com.kocogames.carrentalbooking.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.kocogames.carrentalbooking.R
import com.kocogames.carrentalbooking.databinding.FragmentLocationBinding
import com.kocogames.carrentalbooking.viewmodel.HomeViewModel

class LocationFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentLocationBinding
    private lateinit var locationProvider: LocationProvider
    private lateinit var geocoderHelper: GeocoderHelper
    private lateinit var placesAutocompleteManager: PlacesAutocompleteManager
    private lateinit var mapManager: MapManager
    private lateinit var googleMap: GoogleMap
    private val sharedVm: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationProvider = FusedLocationProvider(requireContext())
        geocoderHelper = GeocoderHelper(requireContext())
        placesAutocompleteManager = PlacesAutocompleteManager(requireContext(), Places.createClient(requireContext()))

        placesAutocompleteManager.setupAutoComplete(binding.searchEditText) { latLng, name ->
            binding.searchEditText.setText(name)
            mapManager.placeMarker(latLng, getString(R.string.pickup_location))
        }

        binding.setLocationButton.setOnClickListener {
            sharedVm.pickupLocation = binding.searchEditText.text.toString()
            requireActivity().onBackPressed()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        mapManager = MapManager(map)
        mapManager.initializeMap({ latLng ->
            val address = geocoderHelper.getAddressFromLatLng(latLng)
            binding.searchEditText.setText(address)
            mapManager.placeMarker(latLng, address)
        }, { latLng ->
            val address = geocoderHelper.getAddressFromLatLng(latLng)
            binding.searchEditText.setText(address)
            mapManager.placeMarker(latLng, address)
        })

        locationProvider.getCurrentLocation { latLng ->
            latLng?.let {
                val address = geocoderHelper.getAddressFromLatLng(it)
                binding.searchEditText.setText(address)
                sharedVm.pickupLocation = binding.searchEditText.text.toString()
                mapManager.placeMarker(it, getString(R.string.current_location))
            }
        }
    }
}
