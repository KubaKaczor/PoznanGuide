package com.example.poznanhelper.model.bike

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poznanhelper.model.Geometry

@Entity(tableName = "bike_stations")
data class BikeStationEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "bike_racks")
    val bikeRacks: String,
    @ColumnInfo(name = "bikes")
    val bikes: String,
    @ColumnInfo(name = "free_racks")
    val freeRacks: String,
    @ColumnInfo(name = "street")
    val street: String,
    @ColumnInfo(name = "distanceTo")
    val distanceTo: Double = 0.0
){
    fun toBikeStation(): BikeStation {
        val properties = BikesProperties(
            bikeRacks = this.bikeRacks,
            bikes = this.bikes,
            freeRacks = this.freeRacks,
            label = this.street
        )

        val geometry = Geometry(
            coordinates = listOf(
                this.longitude.toDouble(),
                this.latitude.toDouble()
            ),
            type = "type"
        )

        return BikeStation(
            id = this.id,
            geometry = geometry,
            properties = properties,
            type = "type",
            distanceTo = this.distanceTo
        )
    }
}