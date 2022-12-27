package com.example.poznanhelper.model.bike


import com.google.gson.annotations.SerializedName

data class BikesProperties(
    @SerializedName("bike_racks")
    val bikeRacks: String,
    @SerializedName("bikes")
    val bikes: String,
    @SerializedName("free_racks")
    val freeRacks: String,
    @SerializedName("label")
    val label: String,
//    @SerializedName("updated")
//    val updated: String
)