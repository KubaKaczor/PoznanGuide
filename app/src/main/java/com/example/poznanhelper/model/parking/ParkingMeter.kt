package com.example.poznanhelper.model.parking


import com.example.poznanhelper.model.Geometry
import com.google.gson.annotations.SerializedName

data class ParkingMeter(
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("id")
    val id: String,
    @SerializedName("properties")
    val properties: PropertiesX,
    @SerializedName("type")
    val type: String,
    var distanceTo: Double
){
    fun toParkingMeterEntity(): ParkingMeterEntity {
        return ParkingMeterEntity(
            id = this.id,
            latitude = this.geometry.coordinates[1].toString(),
            longitude = this.geometry.coordinates[0].toString(),
            street = this.properties.street,
            zone = this.properties.zone,
            bilon = this.properties.bilon,
            karta = this.properties.karta,
            blik = this.properties.blik,
            peka = this.properties.blik
        )
    }
}