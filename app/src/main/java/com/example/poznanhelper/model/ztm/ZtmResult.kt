package com.example.poznanhelper.model.ztm


import com.example.poznanhelper.model.Crs
import com.google.gson.annotations.SerializedName

data class ZtmResult(
    @SerializedName("crs")
    val crs: Crs,
    @SerializedName("features")
    val stations: List<ZtmStation>,
    @SerializedName("type")
    val type: String
)