package com.example.poznanhelper.screens.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.poznanhelper.R
import com.example.poznanhelper.components.GuideAppBar
import com.example.poznanhelper.navigation.Screens
import com.example.poznanhelper.utils.Constants
import com.example.poznanhelper.utils.RequestPermission
import com.example.poznanhelper.utils.UserLocation
import com.example.poznanhelper.utils.getIconByCategoryName
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.security.Permissions

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavController){
    var fusedLocationClient: FusedLocationProviderClient

    val latitude = remember{ mutableStateOf(0.0) }
    val longitude = remember{ mutableStateOf(0.0) }


    val context = LocalContext.current

    Scaffold(
        topBar = {
            GuideAppBar(
                navController,
                isHome = true
            )
        }
    ) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                UserLocation.getUserLocation(context)
            }
            else -> {
                RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        HomeContent(navController)
    }
}

@Composable
fun HomeContent(
    navController: NavController,
){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(4.dp),
        verticalArrangement = Arrangement.Center
    ) {

        //kafelki
        Row(
            modifier = Modifier.weight(1f)
        ){
            val green = Color(0xFF303F9F)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CategoryCard(categoryName = Constants.CATEGORIES[0], green){
                    navController.navigate(Screens.ParkingScreen.name)
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CategoryCard(categoryName = Constants.CATEGORIES[1], green){
                    navController.navigate(Screens.BikesScreen.name)
                }
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ){
            val darkPurple = Color(0xFF673AB7)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CategoryCard(categoryName = Constants.CATEGORIES[2], darkPurple){
                    navController.navigate(Screens.ShopScreen.name)
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CategoryCard(categoryName = Constants.CATEGORIES[3], darkPurple){
                    navController.navigate(Screens.ZtmScreen.name)
                }
            }
        }
        Row(
            modifier = Modifier.weight(1f)
        ){
            val purple = Color(0xFF7B1FA2)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CategoryCard(categoryName = Constants.CATEGORIES[4], purple){
                    navController.navigate(Screens.TicketScreen.name)
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CategoryCard(categoryName = Constants.CATEGORIES[5], purple){
                    navController.navigate(Screens.AntiqueScreen.name)
                }
            }
        }

    }
}

@Composable
fun CategoryCard(
    categoryName: String,
    color: Color,
    onClick: () -> Unit
)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(4.dp)
            .clickable {
                onClick()
            },
        elevation = 10.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = color
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val imageId = getIconByCategoryName(categoryName)
            Image(
                painterResource(imageId),
                contentDescription = "category icon",
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .padding(start = 8.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                text = categoryName,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }

}

@Composable
fun CategoryRow(
    navController: NavController,
    categoryName: String,
    color: Color
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .height(85.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(8.dp)
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
        ){
            Image(
                painterResource(id = R.drawable.background_app),
                contentDescription = "card background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(color)
        ){
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val imageId = getIconByCategoryName(categoryName)
                Image(
                    painterResource(imageId),
                    contentDescription = "category icon",
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .padding(start = 8.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }


            Column(
                modifier = Modifier.weight(3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = categoryName,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowRight,
                    contentDescription = "arrow right icon",
                    tint = Color.White,
                    modifier = Modifier
                        .height(40.dp)
                        .height(40.dp)
                )
            }
        }
    }
}