package com.example.poznanhelper.model.ticket


import com.example.poznanhelper.model.Geometry
import com.example.poznanhelper.model.parking.ParkingMeterEntity
import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("id")
    val id: Int,
    @SerializedName("properties")
    val properties: TicketProperties,
    @SerializedName("type")
    val type: String,
    var distanceTo: Double
){
    fun toTicketEntity(): TicketEntity {
        return TicketEntity(
            id = this.id,
            latitude = this.geometry.coordinates[1].toString(),
            longitude = this.geometry.coordinates[0].toString(),
            street = this.properties.nazwa,
            opis = this.properties.opis,
            karta = this.properties.kartyPlatnicze == "1",
            peka = this.properties.systemPeka == "1"
        )
    }
}