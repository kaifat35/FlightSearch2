package com.example.flightsearch.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flightsearch2.data.Airport
import com.example.flightsearch2.data.Favorite
import com.example.flightsearch2.data.FlightDatabase
import com.example.flightsearch2.data.UserPreferencesRepository
import com.example.flightsearch.data.repository.FlightRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.flightsearch2.data.dataStore

class FlightViewModel(
    application: Application,
    internal val repository: FlightRepository
) : AndroidViewModel(application) {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _suggestions = mutableStateOf<List<Airport>>(emptyList())
    val suggestions: State<List<Airport>> = _suggestions

    private val _destinations = mutableStateOf<List<Airport>>(emptyList())
    val destinations: State<List<Airport>> = _destinations

    private val _favorites = mutableStateOf<List<Favorite>>(emptyList())
    val favorites: State<List<Favorite>> = _favorites

    private val _selectedAirport = mutableStateOf<Airport?>(null)
    val selectedAirport: State<Airport?> = _selectedAirport

    init {
        viewModelScope.launch {
            repository.searchQuery.collect { query ->
                _searchQuery.value = query
                if (query.isNotEmpty()) {
                    _suggestions.value = repository.searchAirports(query)
                } else {
                    _suggestions.value = emptyList()
                }
            }
            loadFavorites()
        }
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            repository.updateSearchQuery(query)
        }
    }

    fun onAirportSelected(airport: Airport) {
        _selectedAirport.value = airport
        viewModelScope.launch {
            _destinations.value = repository.getDestinations(airport.iataCode)
        }
    }

    fun toggleFavorite(destination: Airport) {
        viewModelScope.launch {
            val departure = selectedAirport.value ?: return@launch
            repository.toggleFavorite(departure.iataCode, destination.iataCode)
            loadFavorites()
        }
    }

    private suspend fun loadFavorites() {
        _favorites.value = repository.getFavorites()
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = FlightDatabase.getInstance(application)
                    val preferencesRepository = UserPreferencesRepository(
                        (application.applicationContext).dataStore
                    )
                    val repository = FlightRepository(
                        database.flightDao(),
                        preferencesRepository
                    )
                    return FlightViewModel(application, repository) as T
                }
            }
        }
    }
}