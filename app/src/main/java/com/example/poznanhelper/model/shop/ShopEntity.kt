package com.example.poznanhelper.model.shop

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poznanhelper.model.Geometry


@Entity(tableName = "shops")
data class ShopEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "adres")
    val adres: String,
    @ColumnInfo(name = "nazwa")
    val nazwa: String,
    @ColumnInfo(name = "opis")
    val opis: String,
    @ColumnInfo(name = "godziny")
    val godziny: String,
    @ColumnInfo(name = "sposobyPlat")
    val sposobyPlat: String,
    @ColumnInfo(name = "biletJednorazowy")
    val biletJednorazowy: String,
    @ColumnInfo(name = "distanceTo")
    val distanceTo: Double = 0.0
) {
    fun toShop(): Shop {
        val properties = ShopProperties(
            adres = this.adres,
            opis = this.opis,
            nazwa = this.nazwa,
            godziny = this.godziny,
            sposobyPlat = this.sposobyPlat,
            biletJednorazowy = this.biletJednorazowy
        )

        val geometry = Geometry(
            coordinates = listOf(
                this.longitude.toDouble(),
                this.latitude.toDouble()
            ),
            type = "type"
        )

        return Shop(
            id = this.id,
            geometry = geometry,
            properties = properties,
            type = "type",
            distanceTo = this.distanceTo
        )
    }
}