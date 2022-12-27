package com.example.poznanhelper.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.poznanhelper.screens.MapScreen
import com.example.poznanhelper.screens.home.HomeScreen
import com.example.poznanhelper.screens.SplashScreen
import com.example.poznanhelper.screens.bikes.BikesScreen
import com.example.poznanhelper.screens.bikes.BikesViewModel
import com.example.poznanhelper.screens.antique.AntiqueScreen
import com.example.poznanhelper.screens.antique.AntiqueViewModel
import com.example.poznanhelper.screens.shops.ShopScreen
import com.example.poznanhelper.screens.shops.ShopViewModel
import com.example.poznanhelper.screens.parking.ParkingViewModel
import com.example.poznanhelper.screens.parking.ParkingScreen
import com.example.poznanhelper.screens.ticket.TicketScreen
import com.example.poznanhelper.screens.ticket.TicketViewModel
import com.example.poznanhelper.screens.ztm.ZtmScreen
import com.example.poznanhelper.screens.ztm.ZtmViewModel

@Composable
fun NavigationComponent(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.SplashScreen.name)
    {
        composable(Screens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        composable(Screens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }

        composable(Screens.ParkingScreen.name){
            val viewModel: ParkingViewModel = hiltViewModel()
            ParkingScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screens.BikesScreen.name){
            val viewModel: BikesViewModel = hiltViewModel()
            BikesScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screens.TicketScreen.name){
            val viewModel: TicketViewModel = hiltViewModel()
            TicketScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screens.ZtmScreen.name){
            val viewModel: ZtmViewModel = hiltViewModel()
            ZtmScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screens.AntiqueScreen.name){
            val viewModel: AntiqueViewModel = hiltViewModel()
            AntiqueScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screens.ShopScreen.name){
            val viewModel: ShopViewModel = hiltViewModel()
            ShopScreen(navController = navController, viewModel = viewModel)
        }

        val mapName = Screens.MapScreen.name
        composable("$mapName/{lat}/{lon}/{category}", arguments = listOf(navArgument("lat"){
            type = NavType.FloatType
        }, navArgument("lon"){
            type = NavType.FloatType
        }, navArgument("category"){
            type = NavType.StringType
        })) { backStackEntry ->
            val lat = backStackEntry.arguments?.getFloat("lat")
            val lon = backStackEntry.arguments?.getFloat("lon")
            val category = backStackEntry.arguments?.getString("category")

            MapScreen(
                navController = navController,
                lat = lat!!.toDouble(),
                lon = lon!!.toDouble(),
                category = category!!
            )

        }
    }
}