package com.example.poznanhelper.model.ticket


import com.example.poznanhelper.model.Crs
import com.google.gson.annotations.SerializedName

data class TicketResult(
    @SerializedName("crs")
    val crs: Crs,
    @SerializedName("features")
    val tickets: List<Ticket>,
//    @SerializedName("klasa")
//    val klasa: Klasa,
    @SerializedName("type")
    val type: String
)