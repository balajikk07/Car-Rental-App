package com.kocogames.carrentalbooking

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.kocogames.carrentalbooking.databinding.ActivityMainBinding
import com.kocogames.carrentalbooking.datepicker.DateTimeHandler
import com.kocogames.carrentalbooking.utils.NavigationHandler
import com.kocogames.carrentalbooking.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var dateTimeHandler: DateTimeHandler
    private lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        viewModel = ViewModelProvider(this, HomeViewModel.HomeViewModelFactory(application))
            .get(HomeViewModel::class.java)

        dateTimeHandler = DateTimeHandler(this, binding, viewModel)
        navigationHandler = NavigationHandler(this, binding, viewModel)

        binding.pickUpLocationCard.setOnClickListener(this)
        binding.pickUpDateCard.setOnClickListener(this)
        binding.dropOffDateCard.setOnClickListener(this)
        binding.searchCard.setOnClickListener(this)

        dateTimeHandler.initializeDates()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.bookingCard.isVisible = true
        binding.pickUpLocationText.text = viewModel.pickupLocation
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.searchCard.id -> navigationHandler.handleSearch()
            binding.pickUpDateCard.id -> dateTimeHandler.selectPickupDate()
            binding.dropOffDateCard.id -> dateTimeHandler.selectDropOffDate()
            binding.pickUpLocationCard.id -> navigationHandler.openLocationFragment()
        }
    }
}
