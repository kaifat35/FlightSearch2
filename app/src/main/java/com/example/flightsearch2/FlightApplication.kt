package com.example.flightsearch2

import android.app.Application
import com.example.flightsearch2.data.FlightDatabase
import com.example.flightsearch2.data.UserPreferencesRepository
import com.example.flightsearch2.data.dataStore

class FlightApplication : Application() {
    // Экземпляр базы данных (ленивая инициализация)
    val database: FlightDatabase by lazy { FlightDatabase.getInstance(this) }

    // Репозиторий предпочтений (ленивая инициализация)
    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(dataStore)
    }
}