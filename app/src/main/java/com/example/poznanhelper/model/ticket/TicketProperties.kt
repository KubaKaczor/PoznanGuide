package com.example.poznanhelper.model.ticket


import com.google.gson.annotations.SerializedName

data class TicketProperties(
//    @SerializedName("adres")
//    val adres: String,
//    @SerializedName("email")
//    val email: String,
//    @SerializedName("grafika")
//    val grafika: String,
//    @SerializedName("id_klasy")
//    val idKlasy: String,
//    @SerializedName("kod")
//    val kod: String,
//    @SerializedName("kolejnosc")
//    val kolejnosc: String,
//    @SerializedName("lang")
//    val lang: String,
//    @SerializedName("miasto")
//    val miasto: String,
    @SerializedName("nazwa")
    val nazwa: String,
    @SerializedName("opis")
    val opis: String,
//    @SerializedName("opis_klasy")
//    val opisKlasy: String,
//    @SerializedName("telefon")
//    val telefon: String,
//    @SerializedName("url")
//    val url: String,
    @SerializedName("y_4345_obs_uga_syste")
    val systemPeka: String,
    @SerializedName("y_4346_karty_p_atnic")
    val kartyPlatnicze: String
)