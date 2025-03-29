package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlightDao {
    @Query("SELECT * FROM airport WHERE name LIKE '%' || :query || '%' OR iata_code LIKE '%' || :query || '%' ORDER BY passengers DESC LIMIT 10")
    suspend fun searchAirports(query: String): List<Airport>

    @Query("SELECT * FROM airport WHERE iata_code != :departureCode ORDER BY passengers DESC")
    suspend fun getDestinations(departureCode: String): List<Airport>

    @Insert
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    suspend fun getAllFavorites(): List<Favorite>

    @Query("SELECT * FROM airport WHERE iata_code = :code LIMIT 1")
    suspend fun getAirportByCode(code: String): Airport?

    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE departure_code = :departure AND destination_code = :destination)")
    suspend fun isFavorite(departure: String, destination: String): Boolean
}
