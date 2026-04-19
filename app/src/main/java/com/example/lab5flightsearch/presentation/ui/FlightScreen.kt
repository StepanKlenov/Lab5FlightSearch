package com.example.lab5flightsearch.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab5flightsearch.data.local.entity.AirportEntity
import com.example.lab5flightsearch.data.local.entity.FavoriteEntity
import com.example.lab5flightsearch.presentation.ui.screen.components.AirportSuggestionItem
import com.example.lab5flightsearch.presentation.ui.screen.components.FavoriteItem
import com.example.lab5flightsearch.presentation.ui.screen.components.FlightItem
import com.example.lab5flightsearch.presentation.viewmodel.FlightViewModel

@Composable
fun flightScreen(viewModel: FlightViewModel) {

    val query by viewModel.searchQuery.collectAsState()
    val suggestedAirports by viewModel.suggestedAirports.collectAsState()
    val flights by viewModel.flights.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = query,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text("Код IATA или название аэропорта") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            query.isBlank() -> {
                Text("Избранные рейсы", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                if (favorites.isEmpty()) {
                    Text("Список избранного пока пуст", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    LazyColumn {
                        items(favorites) { favorite ->
                            FavoriteItem(
                                favorite = favorite,
                                onRemove = { viewModel.toggleFavorite(favorite.departure_code, favorite.destination_code) }
                            )
                        }
                    }
                }
            }
            else -> {
                if (suggestedAirports.isNotEmpty()) {
                    Text("Найденные аэропорты", style = MaterialTheme.typography.titleMedium)
                    LazyColumn {
                        items(suggestedAirports) { airport ->
                            AirportSuggestionItem(
                                airport = airport,
                                onSelect = { viewModel.selectAirport(airport) }
                            )
                        }
                    }
                }

                if (flights.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Рейсы из $query", style = MaterialTheme.typography.titleMedium)
                    LazyColumn {
                        items(flights) { destination ->
                            FlightItem(
                                departureCode = query,
                                destination = destination,
                                isFavorite = favorites.any {
                                    it.departure_code == query && it.destination_code == destination.iata_code
                                },
                                onToggleFavorite = {
                                    viewModel.toggleFavorite(query, destination.iata_code)
                                }
                            )
                        }
                    }
                } else if (query.length >= 3 && suggestedAirports.isEmpty()) {
                    Text("Рейсы не найдены", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}