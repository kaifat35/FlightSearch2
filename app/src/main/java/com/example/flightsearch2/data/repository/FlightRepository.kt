package com.example.flightsearch.data.repository

import com.example.flightsearch2.data.Favorite
import com.example.flightsearch2.data.FlightDao
import com.example.flightsearch2.data.UserPreferencesRepository

class FlightRepository(
    private val flightDao: FlightDao,
    private val preferencesRepository: UserPreferencesRepository
) {
    suspend fun searchAirports(query: String) = flightDao.searchAirports(query)
    suspend fun getDestinations(departureCode: String) = flightDao.getDestinations(departureCode)
    suspend fun getFavorites() = flightDao.getAllFavorites()
    suspend fun toggleFavorite(departureCode: String, destinationCode: String) {
        if (flightDao.isFavorite(departureCode, destinationCode)) {
            flightDao.deleteFavorite(Favorite(0, departureCode, destinationCode))
        } else {
            flightDao.insertFavorite(Favorite(0, departureCode, destinationCode))
        }
    }
    suspend fun isFavorite(departure: String, destination: String) =
        flightDao.isFavorite(departure, destination)
    suspend fun getAirportByCode(code: String) = flightDao.getAirportByCode(code)
    suspend fun updateSearchQuery(query: String) = preferencesRepository.updateSearchQuery(query)
    val searchQuery = preferencesRepository.searchQuery
}