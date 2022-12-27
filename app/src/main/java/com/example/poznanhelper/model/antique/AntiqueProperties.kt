package com.example.poznanhelper.model.antique


import com.google.gson.annotations.SerializedName

data class AntiqueProperties(
    @SerializedName("adres")
    val adres: String,
    @SerializedName("nazwa")
    val nazwa: String,
    @SerializedName("opis")
    val opis: String,
)