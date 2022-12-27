package com.example.poznanhelper.screens

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.poznanhelper.R
import com.example.poznanhelper.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController){

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = R.drawable.background_app),
            contentDescription = "background app",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(id = R.drawable.logo_poznan),
                contentDescription = "logo",
                modifier = Modifier.fillMaxWidth(0.7f)
            )
            Text(
                text = "Przewodnik po mieście",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            LaunchedEffect(Unit){
                delay(1000)
                navController.navigate(Screens.HomeScreen.name)
            }
        }

    }
}