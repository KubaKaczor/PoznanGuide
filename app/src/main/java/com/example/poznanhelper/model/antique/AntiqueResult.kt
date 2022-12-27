package com.example.poznanhelper.model.antique


import com.example.poznanhelper.model.Crs
import com.google.gson.annotations.SerializedName

data class AntiqueResult(
    @SerializedName("crs")
    val crs: Crs,
    @SerializedName("features")
    val features: List<Antique>
)