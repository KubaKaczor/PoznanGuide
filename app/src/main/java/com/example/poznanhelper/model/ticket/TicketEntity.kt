package com.example.poznanhelper.model.ticket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poznanhelper.model.Geometry
import com.example.poznanhelper.model.parking.ParkingMeter
import com.example.poznanhelper.model.parking.PropertiesX

@Entity(tableName = "ticket")
data class TicketEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "street")
    val street: String,
    @ColumnInfo(name = "opis")
    val opis: String,
    @ColumnInfo(name = "peka")
    val peka: Boolean,
    @ColumnInfo(name = "karta")
    val karta: Boolean,
    @ColumnInfo(name = "distanceTo")
    val distanceTo: Double = 0.0
){
    fun toTicket(): Ticket {
        val properties = TicketProperties(
            nazwa = this.street,
            opis = this.opis,
            systemPeka = if(this.peka) "1" else "",
            kartyPlatnicze = if(this.karta) "1" else "",
        )

        val geometry = Geometry(
            coordinates = listOf(
                this.longitude.toDouble(),
                this.latitude.toDouble()
            ),
            type = "type"
        )

        return Ticket(
            id = this.id,
            geometry = geometry,
            properties = properties,
            type = "type",
            distanceTo = this.distanceTo
        )
    }
}
