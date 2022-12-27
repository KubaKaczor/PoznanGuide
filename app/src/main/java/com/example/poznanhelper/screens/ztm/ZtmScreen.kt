package com.example.poznanhelper.screens.ztm

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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.poznanhelper.components.*
import com.example.poznanhelper.data.Resource
import com.example.poznanhelper.model.MarkerMap
import com.example.poznanhelper.model.ticket.Ticket
import com.example.poznanhelper.model.ztm.ZtmStation
import com.example.poznanhelper.navigation.Screens
import com.example.poznanhelper.screens.ticket.*
import com.example.poznanhelper.screens.ticket.FavouriteListTab
import com.example.poznanhelper.utils.Constants
import com.example.poznanhelper.utils.calculateTimeToTarget
import com.example.poznanhelper.utils.getDistanceInKm
import com.example.poznanhelper.utils.getIconByCategoryName
import com.google.android.gms.maps.model.LatLng

@Composable
fun ZtmScreen(navController: NavController, viewModel: ZtmViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
            GuideAppBar(
                navController = navController,
                isHome = false
            )
        }
    ) {
        ZtmContent(navController = navController, viewModel = viewModel)
    }
}

@Composable
fun ZtmContent(navController: NavController, viewModel: ZtmViewModel) {
    var ztmStations = remember { listOf<ZtmStation>() }
    val searchQueryState = rememberSaveable { mutableStateOf("") }

    val ztmState = produceState<Resource<List<ZtmStation>>>(initialValue = Resource.Loading()){
        value = viewModel.getZtmStations()
    }.value

    if(ztmState.data != null) {
        ztmStations = ztmState.data!!

        if(searchQueryState.value.isNotEmpty()){
            ztmStations = ztmStations.filter {
                it.properties.street.lowercase().contains(searchQueryState.value.lowercase())
                        || it.id.lowercase().contains(searchQueryState.value.lowercase())
            }
        }
    }

    TabsContent(
        listTab = {
            ZtmListTab(
                searchQueryState = searchQueryState,
                ztmState = ztmState,
                ztmStations = ztmStations,
                navController = navController,
                viewModel = viewModel)
        },
        mapTab = {
            MapTab(ztmStations = ztmStations)
        },
        favouriteTab = {
            FavouriteListTab(viewModel = viewModel, navController = navController)
        }
    )
}

@Composable
fun FavouriteListTab(viewModel: ZtmViewModel, navController: NavController) {
    val favouriteList = viewModel.favouriteList.collectAsState().value

    if(favouriteList.isEmpty()){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Brak ulubionych biletomatów")
        }
    }else{
        ShowList(ztmStations = favouriteList, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun MapTab(ztmStations: List<ZtmStation>){
    val markersList = mutableListOf<MarkerMap>()
    for (ztm in ztmStations){
        val marker = MarkerMap(
            position = LatLng(
                ztm.geometry.coordinates[1],
                ztm.geometry.coordinates[0]
            ),
            title = "Przystanek ZTM, ${ztm.properties.street}"
        )
        markersList.add(marker)
    }
    MapComponent(markers = markersList)
}

@Composable
fun ShowList(ztmStations: List<ZtmStation>, navController: NavController, viewModel: ZtmViewModel){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = ztmStations) { ztm ->
            ZtmStationRow(ztm = ztm, navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun ZtmListTab(
    searchQueryState: MutableState<String>,
    ztmState: Resource<List<ZtmStation>>,
    ztmStations: List<ZtmStation>,
    navController: NavController,
    viewModel: ZtmViewModel
){
    SearchForm(
        searchQueryState = searchQueryState,
        placeholder = "Podaj nazwę ulicy lub numer przystanku"
    ){ searchQuery ->
        searchQueryState.value = searchQuery
    }
    if (ztmState.data == null){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(modifier = Modifier.then(Modifier.size(32.dp)))
        }
    }

    if(searchQueryState.value.isEmpty()){
        ShowList(ztmStations = ztmStations, navController = navController, viewModel = viewModel)
    }
    else{
        ShowList(ztmStations = ztmStations, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun ZtmStationRow(ztm: ZtmStation, navController: NavController, viewModel: ZtmViewModel) {

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
                            val imageId = getIconByCategoryName(Constants.CATEGORIES[3])
                            Image(
                                painterResource(imageId),
                                contentDescription = "category icon",
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(50.dp),
                                colorFilter = ColorFilter.tint(Color.Black)
                            )
                            Text(
                                text = "Stacja ZTM",
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
                                text = "Ul. " + ztm.properties.street,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Strefa: ${ztm.properties.zone}" ,
                                style = MaterialTheme.typography.caption
                            )

                            Text(
                                text = "Numer przystanku: ${ztm.id}",
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
                            val distance = getDistanceInKm(ztm.geometry.coordinates[1], ztm.geometry.coordinates[0])
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
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )

                                Text(
                                    text = timeToCome,
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = expanded.value){
                ExpandedContent(ztm = ztm, navController = navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ExpandedContent(ztm: ZtmStation, navController: NavController, viewModel: ZtmViewModel) {
    var isFavourite by remember { mutableStateOf(false) }
    isFavourite = viewModel.favouriteList.collectAsState().value.any { b -> b.id == ztm.id }

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
                Text(text = "Linie:")

                for(trace in ztm.properties.headsigns.split(',')){
                    TraceRow(traceNumber = trace)
                }
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
                        val coordinates = ztm.geometry.coordinates
                        val lonFloat = coordinates[0].toFloat()
                        val latFloat = coordinates[1].toFloat()

                        navController.navigate(Screens.MapScreen.name + "/${latFloat}/${lonFloat}/Przystanek ZTM")
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
                            viewModel.removeZtmStation(ztm.toZtmEntity())
                            Toast.makeText(context, "Usunięto z ulubionych", Toast.LENGTH_SHORT).show()
                        }else{
                            viewModel.addZtmStation(ztm.toZtmEntity())
                            Toast.makeText(context, "Dodano do ulubionych", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if(isFavourite) Color.LightGray else green,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(180.dp)
                        .height(45.dp)
                        .padding(end = 8.dp, top = 8.dp)
                ) {
                    Text(text = "Ulubione")
                    Icon(
                        imageVector = if(isFavourite) Icons.Default.Close else Icons.Default.Star,
                        contentDescription = "star icon"
                    )
                }
            }
        }
    }
}

@Composable
fun TraceRow(traceNumber: String){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 8.dp)
    ){
        Icon(
            imageVector = Icons.Default.Circle,
            contentDescription = "list indicator",
            modifier = Modifier.size(8.dp)
        )
        Text(
            text = traceNumber.trim(),
            modifier = Modifier.padding(horizontal = 4.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
    }
}
