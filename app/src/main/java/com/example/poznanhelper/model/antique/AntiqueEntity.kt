package com.example.poznanhelper.model.antique

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poznanhelper.model.Geometry

@Entity(tableName = "antiques")
data class AntiqueEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "nazwa")
    val nazwa: String,
    @ColumnInfo(name = "adres")
    val adres: String,
    @ColumnInfo(name = "opis")
    val opis: String,
    @ColumnInfo(name = "distanceTo")
    val distanceTo: Double = 0.0
){
    fun toAntique(): Antique {
        val properties = AntiqueProperties(
            adres = this.adres,
            nazwa = this.nazwa,
            opis = this.opis
        )

        val geometry = Geometry(
            coordinates =
                    listOf(
                        this.longitude.toDouble(),
                        this.latitude.toDouble()
                    ),
            type = "type"
        )

        return Antique(
            id = this.id,
            geometry = geometry,
            properties = properties,
            type = "type",
            distanceTo = this.distanceTo
        )
    }
}
