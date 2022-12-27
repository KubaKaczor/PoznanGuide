package com.example.poznanhelper.model.shop


import com.google.gson.annotations.SerializedName

data class ShopProperties(
    @SerializedName("adres")
    val adres: String,
    @SerializedName("nazwa")
    val nazwa: String,
    @SerializedName("opis")
    val opis: String,
    @SerializedName("y_4308_godziny_otwar")
    val godziny: String,
    @SerializedName("y_4311_forma_sprzeda")
    val sposobyPlat: String,
    @SerializedName("y_4326_bilety_jednor")
    val biletJednorazowy: String,
)