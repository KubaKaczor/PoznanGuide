package com.example.poznanhelper.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.poznanhelper.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.Duration

fun getIconByCategoryName(name: String): Int{
    var imageId = -1
    with(name) {
        when {
            contains("Parkometry") -> {
                imageId = R.drawable.parkometr
            }
            contains("Rowery miejskie") -> {
                imageId = R.drawable.rower
            }
            contains("Sprzedaż biletów") -> {
                imageId = R.drawable.bilet
            }
            contains("Przystanki ZTM") -> {
                imageId = R.drawable.bus
            }
            contains("Biletomaty") -> {
                imageId = R.drawable.biletomat
            }
            contains("Zabytki") -> {
                imageId = R.drawable.antique
            }
        }
    }
    return imageId
}


fun getDistanceInKm(targetLat: Double, targetLon: Double): Double{
    val locationA = Location("userLocation")

    locationA.setLatitude(UserLocation.latitude.value)
    locationA.setLongitude(UserLocation.longitude.value)

    val locationB = Location("targetLocation")

    locationB.setLatitude(targetLat)
    locationB.setLongitude(targetLon)

    val distance: Float = locationA.distanceTo(locationB)

    val distanceInKm = (distance / 1000).toDouble()

    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.DOWN
    val result = df.format(distanceInKm).replace(',','.')

    return result.toDouble()
}

fun calculateTimeToTarget(distance: Double): String{
    val totalMinutes: Long = (distance * 10).toLong()
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    val result = if(hours > 0) "${hours}h ${minutes}m" else "${minutes}min"
    return result
}

fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        setBounds(0, 0, 150, 150)
        val bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

