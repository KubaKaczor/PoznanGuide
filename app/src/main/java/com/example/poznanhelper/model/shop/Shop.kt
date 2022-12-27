package com.example.poznanhelper.model.shop


import com.example.poznanhelper.model.Geometry
import com.example.poznanhelper.model.ticket.TicketEntity
import com.google.gson.annotations.SerializedName

data class Shop(
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("id")
    val id: Int,
    @SerializedName("properties")
    val properties: ShopProperties,
    @SerializedName("type")
    val type: String,
    var distanceTo: Double
) {
    fun toShopEntity(): ShopEntity {
        return ShopEntity(
            id = this.id,
            latitude = this.geometry.coordinates[1].toString(),
            longitude = this.geometry.coordinates[0].toString(),
            adres = this.properties.nazwa,
            opis = this.properties.opis,
            nazwa = this.properties.nazwa,
            godziny = this.properties.godziny,
            sposobyPlat = this.properties.sposobyPlat,
            biletJednorazowy = this.properties.biletJednorazowy,
            distanceTo = this.distanceTo
        )
    }
}