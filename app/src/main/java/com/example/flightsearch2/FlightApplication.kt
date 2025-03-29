package com.example.flightsearch2

import android.app.Application
import com.example.flightsearch.data.FlightDatabase
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.data.dataStore

class FlightApplication : Application() {
    // Экземпляр базы данных (ленивая инициализация)
    val database: FlightDatabase by lazy { FlightDatabase.getInstance(this) }

    // Репозиторий предпочтений (ленивая инициализация)
    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(dataStore)
    }
}