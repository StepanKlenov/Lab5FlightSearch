package com.example.lab5flightsearch.presentation.ui.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab5flightsearch.data.local.entity.AirportEntity

@Composable
fun AirportSuggestionItem(
    airport: AirportEntity,
    onSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Text(
            text = "${airport.name} (${airport.iata_code})",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Пассажиров: ${airport.passengers}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}