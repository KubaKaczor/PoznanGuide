package com.example.poznanhelper.model.parking


import com.google.gson.annotations.SerializedName

data class PropertiesX(
    @SerializedName("bilon")
    val bilon: String,
    @SerializedName("blik")
    val blik: String,
    @SerializedName("karta")
    val karta: String,
    @SerializedName("peka")
    val peka: String,
    @SerializedName("street")
    val street: String,
    @SerializedName("zone")
    val zone: String
)