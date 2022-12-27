package com.example.poznanhelper.model.ztm


import com.example.poznanhelper.model.Geometry
import com.example.poznanhelper.model.ticket.TicketEntity
import com.google.gson.annotations.SerializedName

data class ZtmStation(
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("id")
    val id: String,
    @SerializedName("properties")
    val properties: ZtmProperties,
    @SerializedName("type")
    val type: String,
    var distanceTo: Double
){
    fun toZtmEntity(): ZtmEntity {
        return ZtmEntity(
            id = this.id,
            latitude = this.geometry.coordinates[1].toString(),
            longitude = this.geometry.coordinates[0].toString(),
            street = this.properties.street,
            zone = this.properties.zone,
            headsigns = this.properties.headsigns,
            distanceTo = this.distanceTo
        )
    }
}