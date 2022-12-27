package com.example.poznanhelper.model.parking

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poznanhelper.model.Geometry

@Entity(tableName = "parking_meters")
data class ParkingMeterEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "street")
    val street: String,
    @ColumnInfo(name = "zone")
    val zone: String,
    @ColumnInfo(name = "bilon")
    val bilon: String,
    @ColumnInfo(name = "blik")
    val blik: String,
    @ColumnInfo(name = "karta")
    val karta: String,
    @ColumnInfo(name = "peka")
    val peka: String,
    @ColumnInfo(name = "distanceTo")
    val distanceTo: Double = 0.0
){
    fun toParkingMeter(): ParkingMeter {
        val properties = PropertiesX(
            bilon = this.bilon,
            blik = this.blik,
            karta = this.karta,
            peka = this.peka,
            street = this.street,
            zone = this.zone
        )

        val geometry = Geometry(
            coordinates = listOf(
                this.longitude.toDouble(),
                this.latitude.toDouble()
            ),
            type = "type"
        )

        return ParkingMeter(
            id = this.id,
            geometry = geometry,
            properties = properties,
            type = "type",
            distanceTo = this.distanceTo
        )
    }
}

