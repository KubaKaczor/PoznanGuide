package com.example.poznanhelper.model.antique


import com.example.poznanhelper.model.Geometry
import com.example.poznanhelper.model.ztm.ZtmEntity
import com.google.gson.annotations.SerializedName

data class Antique(
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("id")
    val id: Int,
    @SerializedName("properties")
    val properties: AntiqueProperties,
    @SerializedName("type")
    val type: String,
    var distanceTo: Double
){
    fun toAntiqueEntity(): AntiqueEntity{
        return AntiqueEntity(
            id = this.id,
            latitude = this.geometry.coordinates[1].toString(),
            longitude = this.geometry.coordinates[0].toString(),
            adres = this.properties.adres,
            nazwa = this.properties.nazwa,
            opis = this.properties.opis,
            distanceTo = this.distanceTo
        )
    }
}