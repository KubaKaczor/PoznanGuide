package com.example.poznanhelper.data

import androidx.room.*
import com.example.poznanhelper.model.ticket.TicketEntity
import com.example.poznanhelper.model.ztm.ZtmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ZtmDao {
    @Query("Select * from ztm_stations")
    fun getZtmStations(): Flow<List<ZtmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ztmEntity: ZtmEntity)

    @Delete
    suspend fun delete(ztmEntity: ZtmEntity)
}