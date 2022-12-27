package com.example.poznanhelper.screens.parking

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.poznanhelper.components.*
import com.example.poznanhelper.data.Resource
import com.example.poznanhelper.model.MarkerMap
import com.example.poznanhelper.model.parking.ParkingMeter
import com.example.poznanhelper.navigation.Screens
import com.example.poznanhelper.utils.*
import com.google.android.gms.maps.model.LatLng

@Composable
fun ParkingScreen(navController: NavController, viewModel: ParkingViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
            GuideAppBar(
                navController = navController,
                isHome = false
            )
        }
    ) {
        ParkingContent(navController = navController, viewModel = viewModel)
    }
}

@Composable
fun ParkingContent(navController: NavController, viewModel: ParkingViewModel){
    var parkingMeters = remember { listOf<ParkingMeter>() }
    val searchQueryState = rememberSaveable { mutableStateOf("") }

    val parkingState = produceState<Resource<List<ParkingMeter>>>(initialValue = Resource.Loading()){
        value = viewModel.getParkingMeters()
    }.value

    if(parkingState.data != null) {
        parkingMeters = parkingState.data!!

        if(searchQueryState.value.isNotEmpty()){
            parkingMeters = parkingMeters.filter {
                it.properties.street.lowercase().contains(searchQueryState.value.lowercase())
                        || it.properties.zone.lowercase().contains(searchQueryState.value.lowercase())
            }
        }
    }

    TabsContent(
        listTab = {
            ParkingListTab(
                searchQueryState = searchQueryState,
                parkingState = parkingState,
                parkingMeters = parkingMeters,
                navController = navController,
                viewModel = viewModel)
        },
        mapTab = {
            MapTab(parkingMeters = parkingMeters)
        },
        favouriteTab = {
            FavouriteListTab(viewModel = viewModel, navController = navController)
        })
}

@Composable
fun MapTab(parkingMeters: List<ParkingMeter>){
    val markersList = mutableListOf<MarkerMap>()
    for (parkingMeter in parkingMeters){
        val marker = MarkerMap(
            position = LatLng(
                parkingMeter.geometry.coordinates[1],
                parkingMeter.geometry.coordinates[0]
            ),
            title = "Parkometr, ${parkingMeter.properties.street}"
        )
        markersList.add(marker)
    }
    MapComponent(markers = markersList)
}

@Composable
fun ParkingListTab(
    searchQueryState: MutableState<String>,
    parkingState: Resource<List<ParkingMeter>>,
    parkingMeters: List<ParkingMeter>,
    navController: NavController,
    viewModel: ParkingViewModel
){
    SearchForm(searchQueryState){ searchQuery ->
        searchQueryState.value = searchQuery
    }
    if (parkingState.data == null){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(modifier = Modifier.then(Modifier.size(32.dp)))
        }
    }

    if(searchQueryState.value.isEmpty()){
        ShowList(parkingMeters = parkingMeters, navController = navController, viewModel = viewModel)
    }
    else{
        ShowList(parkingMeters = parkingMeters, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun FavouriteListTab(viewModel: ParkingViewModel, navController: NavController) {
    val favouriteList = viewModel.favouriteList.collectAsState().value

    if(favouriteList.isEmpty()){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Brak ulubionych parkometrów")
        }
    }else{
        ShowList(parkingMeters = favouriteList, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun ShowList(parkingMeters: List<ParkingMeter>, navController: NavController, viewModel: ParkingViewModel){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = parkingMeters, key = {
            it.id
        }) { parkingMeter ->
            ParkingRow(parkingMeter = parkingMeter, navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun ParkingRow(parkingMeter: ParkingMeter, navController: NavController, viewModel: ParkingViewModel) {

    val expanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                expanded.value = !expanded.value
            },
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Column(
                    modifier = Modifier.weight(4f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val imageId = getIconByCategoryName(Constants.CATEGORIES[0])
                            Image(
                                painterResource(imageId),
                                contentDescription = "category icon",
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(50.dp),
                                colorFilter = ColorFilter.tint(Color.Black)
                            )
                            Text(
                                text = "Parkometr",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Divider(
                            color = Color.Red,
                            modifier = Modifier
                                .height(60.dp)
                                .width(1.dp)
                        )

                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = "Ul. " + parkingMeter.properties.street,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = parkingMeter.properties.zone,
                                style = MaterialTheme.typography.caption
                            )
                        }

                    }
                }
                Column(
                    modifier = Modifier.weight(2f),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = if(expanded.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = "expand details arrow",
                        )

                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .width(100.dp)
                                .padding(start = 4.dp, end = 8.dp, top = 4.dp)
                        ) {
                            val distance = getDistanceInKm(parkingMeter.geometry.coordinates[1], parkingMeter.geometry.coordinates[0])
                            val timeToCome = calculateTimeToTarget(distance)

                                Column(
                                    modifier = Modifier
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .background(color = Color(0xFF4169E1))
                                        .fillMaxWidth()
                                        .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                                        text = distance.toString() + "km",
                                        style = TextStyle(color = Color.White, fontSize = 18.sp),
                                        fontWeight = FontWeight.Bold,
                                    )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.Center
                            ){
                                Icon(
                                    imageVector = Icons.Filled.DirectionsWalk,
                                    contentDescription = "walking icon",
                                    tint = Gray,
                                    modifier = Modifier.size(18.dp)
                                )

                                Text(
                                    text = timeToCome,
                                    color = Gray,
                                    fontSize = 14.sp
                                )
                            }

                        }
                    }
                }
            }

            AnimatedVisibility(visible = expanded.value){
                ExpandedContent(parkingMeter = parkingMeter, navController = navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ExpandedContent(parkingMeter: ParkingMeter, navController: NavController, viewModel: ParkingViewModel) {
    var isFavourite by remember { mutableStateOf(false) }
    isFavourite = viewModel.favouriteList.collectAsState().value.any { p -> p.id == parkingMeter.id }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        backgroundColor = Color(0xFFF0F0F0)

    ){
        Row(
            modifier = Modifier.padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(text = "Metody płatności:")

                PayMethodRow(payMethod = "Blik", available = parkingMeter.properties.blik)
                PayMethodRow(payMethod = "Gotówka", available = parkingMeter.properties.bilon)
                PayMethodRow(payMethod = "Karta", available = parkingMeter.properties.karta)
                PayMethodRow(payMethod = "PEKA", available = parkingMeter.properties.peka)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val green = Color(0xFF228B22)
                Button(
                    onClick = {
                        val coordinates = parkingMeter.geometry.coordinates
                        val lonFloat = coordinates[0].toFloat()
                        val latFloat = coordinates[1].toFloat()

                        navController.navigate(Screens.MapScreen.name + "/${latFloat}/${lonFloat}/Parkomat")
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = green,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(180.dp)
                        .height(45.dp)
                        .padding(end = 8.dp, top = 8.dp),
                ) {
                    Text(text = "Pokaż na mapie")
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "map icon"
                    )
                }

                val context = LocalContext.current
                Button(
                    onClick = {
                        if(isFavourite){
                            viewModel.removeParking(parkingMeter.toParkingMeterEntity())
                            Toast.makeText(context, "Usunięto z ulubionych", Toast.LENGTH_SHORT).show()
                        }else{
                            viewModel.addParking(parkingMeter.toParkingMeterEntity())
                            Toast.makeText(context, "Dodano do ulubionych", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if(isFavourite)LightGray else green,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(180.dp)
                        .height(45.dp)
                        .padding(end = 8.dp, top = 8.dp)
                ) {
                    Text(text = "Ulubione")
                    Icon(
                        imageVector = if(isFavourite)Icons.Default.Close else Icons.Default.Star,
                        contentDescription = "star icon"
                    )
                }
            }
        }
    }
}
