package com.example.poznanhelper.data

import androidx.room.*
import com.example.poznanhelper.model.parking.ParkingMeterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParkingDao {

    @Query("Select * from parking_meters")
    fun getParkingMeters(): Flow<List<ParkingMeterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(parkingMeterEntity: ParkingMeterEntity)

    @Delete
    suspend fun delete(parkingMeterEntity: ParkingMeterEntity)
}