package com.example.flightsearch2.ui.theme.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch2.data.Airport
import com.example.flightsearch2.data.Favorite
import com.example.flightsearch.viewmodel.FlightViewModel

@Composable
fun FlightSearchScreen(
    viewModel: FlightViewModel = viewModel(
        factory = FlightViewModel.provideFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val searchQuery by viewModel.searchQuery
    val suggestions by viewModel.suggestions
    val destinations by viewModel.destinations
    val favorites by viewModel.favorites
    val selectedAirport by viewModel.selectedAirport

    Column(modifier = Modifier.padding(16.dp)) {
        SearchField(
            value = searchQuery.toString(),
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier.fillMaxWidth()
        )

        if (suggestions.isNotEmpty()) {
            AirportSuggestions(
                suggestions = suggestions,
                onSelect = { viewModel.onAirportSelected(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        when {
            selectedAirport != null && destinations.isNotEmpty() -> {
                DestinationsList(
                    departure = selectedAirport as Airport,
                    destinations = destinations,
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    viewModel = viewModel
                )
            }
            searchQuery.isEmpty() && favorites.isNotEmpty() -> {
                FavoritesList(favorites = favorites)
            }
        }
    }
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text("Search airports...") }
    )
}

@Composable
fun AirportSuggestions(
    suggestions: List<Airport>,
    onSelect: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(suggestions) { airport ->
            Text(
                text = "${airport.name} (${airport.iataCode})",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(airport) }
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun DestinationsList(
    departure: Airport,
    destinations: List<Airport>,
    onToggleFavorite: (Airport) -> Unit,
    viewModel: FlightViewModel
) {
    LazyColumn {
        items(destinations) { destination ->
            var isFavorite by remember { mutableStateOf(false) }

            LaunchedEffect(destination) {
                isFavorite = viewModel.repository.isFavorite(departure.iataCode, destination.iataCode)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("${departure.iataCode} → ${destination.iataCode}")
                    Text(destination.name)
                }
                IconButton(onClick = { onToggleFavorite(destination) }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesList(favorites: List<Favorite>) {
    LazyColumn {
        items(favorites) { favorite ->
            Text(
                text = "${favorite.departureCode} → ${favorite.destinationCode}",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}