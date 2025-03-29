package com.example.flightsearch2.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    val searchQuery: Flow<String> = dataStore.data
        .map { preferences -> preferences[PreferencesKeys.SEARCH_QUERY] ?: "" }

    suspend fun updateSearchQuery(query: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SEARCH_QUERY] = query
        }
    }

    private object PreferencesKeys {
        val SEARCH_QUERY = stringPreferencesKey("search_query")
    }
}