package com.example.poznanhelper.model.ztm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poznanhelper.model.Geometry
import com.example.poznanhelper.model.ticket.Ticket
import com.example.poznanhelper.model.ticket.TicketProperties

@Entity(tableName = "ztm_stations")
data class ZtmEntity(
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
    @ColumnInfo(name = "headsigns")
    val headsigns: String,
    @ColumnInfo(name = "distanceTo")
    val distanceTo: Double = 0.0
){
    fun toZtmStation(): ZtmStation {
        val properties = ZtmProperties(
            headsigns = this.headsigns,
            routeType = "0",
            zone = this.zone,
            street = this.street,
        )

        val geometry = Geometry(
            coordinates = listOf(
                this.longitude.toDouble(),
                this.latitude.toDouble()
            ),
            type = "type"
        )

        return ZtmStation(
            id = this.id,
            geometry = geometry,
            properties = properties,
            type = "type",
            distanceTo = this.distanceTo
        )
    }
}
