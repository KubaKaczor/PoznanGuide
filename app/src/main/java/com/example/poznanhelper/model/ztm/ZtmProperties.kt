package com.example.poznanhelper.model.ztm


import com.google.gson.annotations.SerializedName

data class ZtmProperties(
    @SerializedName("headsigns")
    var headsigns: String,
    @SerializedName("route_type")
    val routeType: String,
    @SerializedName("stop_name")
    val street: String,
    @SerializedName("zone")
    val zone: String
)