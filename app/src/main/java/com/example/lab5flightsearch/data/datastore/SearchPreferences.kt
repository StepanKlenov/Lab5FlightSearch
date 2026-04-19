package com.example.lab5flightsearch.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "search_prefs")

class SearchPreferences(private val context: Context) {

    private val SEARCH_KEY = stringPreferencesKey("search_query")

    val searchQuery: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[SEARCH_KEY]
    }

    suspend fun saveQuery(query: String) {
        context.dataStore.edit { prefs ->
            prefs[SEARCH_KEY] = query
        }
    }
}