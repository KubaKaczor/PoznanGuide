package com.example.poznanhelper.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.poznanhelper.model.bike.BikeStationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BikeDao {
    @Query("Select * from bike_stations")
    fun getBikeStations(): Flow<List<BikeStationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bikeStationEntity: BikeStationEntity)

    @Delete
    suspend fun delete(bikeStationEntity: BikeStationEntity)
}