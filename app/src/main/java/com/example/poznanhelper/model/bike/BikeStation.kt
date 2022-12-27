package com.example.poznanhelper.model.bike


import com.example.poznanhelper.model.Geometry
import com.google.gson.annotations.SerializedName

data class BikeStation(
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("id")
    val id: String,
    @SerializedName("properties")
    val properties: BikesProperties,
    @SerializedName("type")
    val type: String,
    var distanceTo: Double
){
    fun toBikeStationEntity(): BikeStationEntity {
        return BikeStationEntity(
            id = this.id,
            latitude = this.geometry.coordinates[1].toString(),
            longitude = this.geometry.coordinates[0].toString(),
            street = this.properties.label,
            bikeRacks = this.properties.bikeRacks,
            bikes = this.properties.bikes,
            freeRacks = this.properties.freeRacks,
        )
    }
}