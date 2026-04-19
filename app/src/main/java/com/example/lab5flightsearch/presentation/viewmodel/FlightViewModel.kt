package com.example.lab5flightsearch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab5flightsearch.data.local.entity.AirportEntity
import com.example.lab5flightsearch.data.local.entity.FavoriteEntity
import com.example.lab5flightsearch.data.repository.FlightRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class FlightViewModel(
    private val repository: FlightRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val suggestedAirports: StateFlow<List<AirportEntity>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.length < 2) flowOf(emptyList()) else repository.searchAirports(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flights: StateFlow<List<AirportEntity>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.length == 3) {
                repository.getFlightsFromDeparture(query)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteEntity>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery.uppercase()
    }

    fun selectAirport(airport: AirportEntity) {
        val code = airport.iata_code
        _searchQuery.value = code
        viewModelScope.launch {
            repository.saveQuery(code)
        }
    }

    fun toggleFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            val isCurrentlyFavorite = repository.isFavorite(departureCode, destinationCode) > 0
            if (isCurrentlyFavorite) {
                repository.removeFromFavorite(
                    FavoriteEntity(departure_code = departureCode, destination_code = destinationCode)
                )
            } else {
                repository.addToFavorite(departureCode, destinationCode)
            }
        }
    }

    init {
        viewModelScope.launch {
            repository.getSavedQuery().firstOrNull()?.let { savedQuery ->
                if (savedQuery.isNotBlank()) {
                    _searchQuery.value = savedQuery
                }
            }
        }
    }
}