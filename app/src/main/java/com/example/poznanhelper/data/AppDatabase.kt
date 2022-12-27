package com.example.poznanhelper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.poznanhelper.model.bike.BikeStationEntity
import com.example.poznanhelper.model.antique.AntiqueEntity
import com.example.poznanhelper.model.parking.ParkingMeterEntity
import com.example.poznanhelper.model.shop.ShopEntity
import com.example.poznanhelper.model.ticket.TicketEntity
import com.example.poznanhelper.model.ztm.ZtmEntity

@Database(entities = [
    ParkingMeterEntity::class,
    BikeStationEntity::class,
    TicketEntity::class,
    ZtmEntity::class,
    AntiqueEntity::class,
    ShopEntity::class
                     ], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun parkingDao(): ParkingDao
    abstract fun bikeDao(): BikeDao
    abstract fun ticketDao(): TicketDao
    abstract fun ztmDao(): ZtmDao
    abstract fun antiqueDao(): AntiqueDao
    abstract fun shopDao(): ShopDao
}