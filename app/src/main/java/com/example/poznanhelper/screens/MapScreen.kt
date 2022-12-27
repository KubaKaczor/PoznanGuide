package com.example.poznanhelper.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.poznanhelper.R
import com.example.poznanhelper.utils.UserLocation
import com.example.poznanhelper.utils.bitmapDescriptorFromVector
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(navController: NavController, lat: Double, lon: Double, category: String){
    val location = LatLng(lat, lon)
    val locationState = MarkerState(position = location)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(location, 16f)
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "arrow back icon",
            modifier = Modifier
                .clickable {
                    navController.popBackStack()
                }
                .padding(top = 16.dp, start = 16.dp)
        )

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ){
            Marker(
                state = locationState,
                title = category,
            )
            Marker(
                state = rememberMarkerState(position = LatLng(UserLocation.latitude.value, UserLocation.longitude.value)),
                title = "Twoja pozycja",
                icon = bitmapDescriptorFromVector(LocalContext.current, R.drawable.current_position)
            )
        }
    }
}