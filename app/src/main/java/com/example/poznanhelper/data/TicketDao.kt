package com.example.poznanhelper.data

import androidx.room.*
import com.example.poznanhelper.model.bike.BikeStationEntity
import com.example.poznanhelper.model.ticket.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Query("Select * from ticket")
    fun getTickets(): Flow<List<TicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticketEntity: TicketEntity)

    @Delete
    suspend fun delete(ticketEntity: TicketEntity)
}