package com.kocogames.carrentalbooking.datepicker

import android.content.Context
import android.widget.Toast
import com.kocogames.carrentalbooking.databinding.ActivityMainBinding
import com.kocogames.carrentalbooking.viewmodel.HomeViewModel

class DateTimeHandler(
    private val context: Context,
    private val binding: ActivityMainBinding,
    private val viewModel: HomeViewModel

) {
    private val datePicker = DateTimePicker(context,viewModel)

    fun initializeDates() {
        binding.datePick.text = datePicker.getCurrentDateTime()
        binding.dateDrop.text = datePicker.getNextDayDateTime()
    }

    fun selectPickupDate() {
        datePicker.selectPickupDateTime(onDateTimeSelected = { selectedDateTime ->
            binding.datePick.text = selectedDateTime
        }, onError = { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        })
    }

    fun selectDropOffDate() {
        datePicker.selectDropDateTime(onDateTimeSelected = { selectedDateTime ->
            binding.dateDrop.text = selectedDateTime
        }, onError = { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        })
    }
}
