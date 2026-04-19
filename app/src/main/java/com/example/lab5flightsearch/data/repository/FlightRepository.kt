package com.example.lab5flightsearch.data.repository

import com.example.lab5flightsearch.data.datastore.SearchPreferences
import com.example.lab5flightsearch.data.local.dao.AirportDao
import com.example.lab5flightsearch.data.local.dao.FavoriteDao
import com.example.lab5flightsearch.data.local.entity.AirportEntity
import com.example.lab5flightsearch.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FlightRepository(
    private val airportDao: AirportDao,
    private val favoriteDao: FavoriteDao,
    private val prefs: SearchPreferences
) {

    fun getSavedQuery(): Flow<String?> = prefs.searchQuery

    suspend fun saveQuery(query: String) = prefs.saveQuery(query)

    fun searchAirports(query: String): Flow<List<AirportEntity>> =
        airportDao.searchAirports(query)

    fun getFlightsFromDeparture(departureCode: String): Flow<List<AirportEntity>> =
        airportDao.getFlightsFromDeparture(departureCode)

    fun getFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()

    suspend fun addToFavorite(departureCode: String, destinationCode: String) {
        favoriteDao.insert(
            FavoriteEntity(
                departure_code = departureCode,
                destination_code = destinationCode
            )
        )
    }

    suspend fun removeFromFavorite(favorite: FavoriteEntity) {
        favoriteDao.delete(favorite)
    }

    suspend fun isFavorite(departureCode: String, destinationCode: String): Int {
        return favoriteDao.isFavorite(departureCode, destinationCode)
    }
}