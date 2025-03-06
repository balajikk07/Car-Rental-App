package com.kocogames.carrentalbooking.datepicker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import com.kocogames.carrentalbooking.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTimePicker(private val context: Context, private val viewModel: HomeViewModel) {

    private var pickupDateTime: Calendar? = null
    private var dropDateTime: Calendar? = null
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd \n hh:mm a", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun selectPickupDateTime(onDateTimeSelected: (String) -> Unit, onError: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0) // Reset time for proper validation
                }
                // If selected date is today, restrict past times
                showTimePicker(
                    selectedDate,
                    restrictPastTime = selectedDate.isToday(),
                    onTimeSelected = { finalDateTime ->
                        pickupDateTime = finalDateTime
                        dropDateTime = null // Reset drop time if pickup changes
                        Log.d(
                            "DateTimePicker",
                            "selectPickupDateTime: " + dateFormat.format(finalDateTime.time)
                        )
                        viewModel.pickUpDate = dateFormat.format(finalDateTime.time)
                        onDateTimeSelected(dateTimeFormat.format(finalDateTime.time))
                    },
                    onError = onError
                )

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.datePicker.minDate = calendar.timeInMillis // **Disable past dates**
        datePicker.show()
    }


    fun selectDropDateTime(onDateTimeSelected: (String) -> Unit, onError: (String) -> Unit) {
        if (pickupDateTime == null) {
            onError("Select Pickup Date and Time First")
            return
        }

        val minDropDateTime = Calendar.getInstance().apply {
            time = pickupDateTime!!.time
            add(Calendar.DAY_OF_MONTH, 1)
        }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(
                        year,
                        month,
                        dayOfMonth,
                        0,
                        0,
                        0
                    )
                }
                showTimePicker(
                    selectedDate,
                    restrictPastTime = selectedDate.isToday(),
                    onTimeSelected = { finalDateTime ->
                        if (selectedDate.before(minDropDateTime)) {
                            onError("Drop Date Must Be At Least One Day After Pickup")
                            return@showTimePicker
                        }
                        Log.d("DateTimePicker", "selectDropOffDateTime: " + selectedDate)
                        dropDateTime = finalDateTime
                        viewModel.dropDate = dateFormat.format(finalDateTime.time)
                        onDateTimeSelected(dateTimeFormat.format(finalDateTime.time))
                    },
                    onError = onError
                )

            },
            minDropDateTime.get(Calendar.YEAR),
            minDropDateTime.get(Calendar.MONTH),
            minDropDateTime.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = minDropDateTime.timeInMillis
        }.show()
    }


    private fun showTimePicker(
        dateCalendar: Calendar,
        restrictPastTime: Boolean,
        onTimeSelected: (Calendar) -> Unit,
        onError: (String) -> Unit
    ) {
        val currentCalendar = Calendar.getInstance()

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                dateCalendar.apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }

                // **Check if the selected time is in the past when pickup is today**
                if (restrictPastTime && dateCalendar.before(currentCalendar)) {
                    onError("Pickup time cannot be in the past.")
                    return@TimePickerDialog
                }

                onTimeSelected(dateCalendar)
            },
            currentCalendar.get(Calendar.HOUR_OF_DAY),
            currentCalendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    // **Helper function to check if a calendar instance represents today**
    private fun Calendar.isToday(): Boolean {
        val today = Calendar.getInstance()
        return this.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                this.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
    }

    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        viewModel.pickUpDate = dateFormat.format(calendar.time)
        return dateTimeFormat.format(calendar.time)
    }

    fun getNextDayDateTime(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        viewModel.dropDate = dateFormat.format(calendar.time)
        return dateTimeFormat.format(calendar.time)
    }

}
