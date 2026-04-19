package com.example.lab5flightsearch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab5flightsearch.data.repository.FlightRepository

class FlightViewModelFactory(
    private val repository: FlightRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightViewModel::class.java)) {
            return FlightViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}