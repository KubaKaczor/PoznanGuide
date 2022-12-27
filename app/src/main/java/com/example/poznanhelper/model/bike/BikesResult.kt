package com.example.poznanhelper.model.bike


import com.example.poznanhelper.model.Crs
import com.google.gson.annotations.SerializedName

data class BikesResult(
    @SerializedName("crs")
    val crs: Crs,
    @SerializedName("features")
    val bikes: List<BikeStation>,
    @SerializedName("type")
    val type: String
)