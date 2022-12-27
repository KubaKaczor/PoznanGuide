package com.example.poznanhelper.model.parking


import com.example.poznanhelper.model.Crs
import com.google.gson.annotations.SerializedName

data class ResultParking(
    @SerializedName("crs")
    val crs: Crs,
    @SerializedName("features")
    val features: List<ParkingMeter>,
    @SerializedName("type")
    val type: String
)