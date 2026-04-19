package com.example.lab5flightsearch.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.lab5flightsearch.data.local.entity.AirportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {

    @Query("""
        SELECT * FROM airport 
        WHERE iata_code LIKE '%' || :query || '%' 
           OR name LIKE '%' || :query || '%'
        ORDER BY passengers DESC
        LIMIT 10
    """)
    fun searchAirports(query: String): Flow<List<AirportEntity>>

    @Query("""
        SELECT * FROM airport 
        WHERE iata_code != :departureCode 
        ORDER BY passengers DESC
    """)
    fun getFlightsFromDeparture(departureCode: String): Flow<List<AirportEntity>>

    @Query("SELECT * FROM airport WHERE iata_code = :code LIMIT 1")
    suspend fun getAirportByCode(code: String): AirportEntity?
}