package com.kocogames.carrentalbooking.utils

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kocogames.carrentalbooking.R
import com.kocogames.carrentalbooking.databinding.ActivityMainBinding
import com.kocogames.carrentalbooking.location.LocationFragment
import com.kocogames.carrentalbooking.viewmodel.HomeViewModel

class NavigationHandler(
    private val activity: AppCompatActivity,
    private val binding: ActivityMainBinding,
    private val viewModel: HomeViewModel
) {
    fun handleSearch() {
        if (viewModel.pickupLocation?.isNotEmpty() == true &&
            binding.datePick.text.isNotEmpty() &&
            binding.dateDrop.text.isNotEmpty()
        ) {
            val url = "https://www.kayak.com/in?a=awesomecars&url=/cars/${viewModel.pickupLocation?.replace(" ","")?.trim()}/${viewModel.pickUpDate?.trim()}/${viewModel.dropDate?.trim()}"
                Log.d("NavigationHandler", "url: "+ url)
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } else {
            val message = if (viewModel.pickupLocation.isNullOrEmpty()) {
                "Please select a pickup location"
            } else {
                "Please select a date"
            }
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun openLocationFragment() {
        binding.bookingCard.isVisible = false
        val fragment = LocationFragment()
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.main, fragment)
            .addToBackStack(null)
            .commit()
    }
}
