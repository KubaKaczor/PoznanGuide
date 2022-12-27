package com.example.poznanhelper.model.ticket


import com.example.poznanhelper.model.ticket.AtrybutyKlasy
import com.google.gson.annotations.SerializedName

data class Klasa(
    @SerializedName("atrybuty_klasy")
    val atrybutyKlasy: List<AtrybutyKlasy>,
    @SerializedName("id_klasy")
    val idKlasy: String,
    @SerializedName("opis_klasy")
    val opisKlasy: String
)