package com.example.poznanhelper.model.shop


import com.example.poznanhelper.model.Crs
import com.google.gson.annotations.SerializedName

data class ShopResult(
    @SerializedName("crs")
    val crs: Crs,
    @SerializedName("features")
    val features: List<Shop>,
)