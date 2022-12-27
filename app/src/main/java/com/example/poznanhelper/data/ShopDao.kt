package com.example.poznanhelper.data

import androidx.room.*
import com.example.poznanhelper.model.parking.ParkingMeterEntity
import com.example.poznanhelper.model.shop.ShopEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("Select * from shops")
    fun getShops(): Flow<List<ShopEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shopEntity: ShopEntity)

    @Delete
    suspend fun delete(shopEntity: ShopEntity)
}