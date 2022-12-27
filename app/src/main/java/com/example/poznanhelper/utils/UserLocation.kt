package com.example.poznanhelper.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object UserLocation{

    val latitude =  mutableStateOf(0.0)
    val longitude =  mutableStateOf(0.0)


    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context){
        var fusedLocationClient: FusedLocationProviderClient

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                latitude.value = location!!.latitude
                longitude.value = location.longitude
            }
    }
}