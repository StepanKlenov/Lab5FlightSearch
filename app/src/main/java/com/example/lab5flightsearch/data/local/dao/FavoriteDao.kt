package com.example.lab5flightsearch.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab5flightsearch.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)


    @Delete
    suspend fun delete(favorite: FavoriteEntity)


    @Query("""
        SELECT COUNT(*) FROM favorite 
        WHERE departure_code = :departureCode 
          AND destination_code = :destinationCode
    """)
    suspend fun isFavorite(departureCode: String, destinationCode: String): Int


    @Query("""
        DELETE FROM favorite 
        WHERE departure_code = :departureCode 
          AND destination_code = :destinationCode
    """)
    suspend fun deleteByCodes(departureCode: String, destinationCode: String)
}