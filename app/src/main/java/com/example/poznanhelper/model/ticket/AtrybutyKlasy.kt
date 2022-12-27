package com.example.poznanhelper.model.ticket


import com.google.gson.annotations.SerializedName

data class AtrybutyKlasy(
    @SerializedName("nazwa")
    val nazwa: String,
    @SerializedName("opis")
    val opis: String,
    @SerializedName("typ")
    val typ: String
)