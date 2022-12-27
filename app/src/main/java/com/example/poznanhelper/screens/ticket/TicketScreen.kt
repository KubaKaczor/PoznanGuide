package com.example.poznanhelper.screens.ticket

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
import androidx.navigation.NavController
import com.example.poznanhelper.components.*
import com.example.poznanhelper.data.Resource
import com.example.poznanhelper.model.MarkerMap
import com.example.poznanhelper.model.bike.BikeStation
import com.example.poznanhelper.model.ticket.Ticket
import com.example.poznanhelper.navigation.Screens
import com.example.poznanhelper.screens.bikes.*
import com.example.poznanhelper.screens.bikes.BikesListTab
import com.example.poznanhelper.utils.Constants
import com.example.poznanhelper.utils.calculateTimeToTarget
import com.example.poznanhelper.utils.getDistanceInKm
import com.example.poznanhelper.utils.getIconByCategoryName
import com.google.android.gms.maps.model.LatLng

@Composable
fun TicketScreen(navController: NavController, viewModel: TicketViewModel){
    Scaffold(
        topBar = {
            GuideAppBar(
                navController = navController,
                isHome = false
            )
        }
    ) {
        TicketContent(navController = navController, viewModel = viewModel)
    }
}

@Composable
fun TicketContent(navController: NavController, viewModel: TicketViewModel) {
    var tickets = remember { listOf<Ticket>() }
    val searchQueryState = rememberSaveable { mutableStateOf("") }

    val ticketState = produceState<Resource<List<Ticket>>>(initialValue = Resource.Loading()){
        value = viewModel.getTickets()
    }.value

    if(ticketState.data != null) {
        tickets = ticketState.data!!

        if(searchQueryState.value.isNotEmpty()){
            tickets = tickets.filter {
                it.properties.nazwa.lowercase().contains(searchQueryState.value.lowercase())
            }
        }
    }

    TabsContent(
        listTab = {
            TicketsListTab(
                searchQueryState = searchQueryState,
                ticketState = ticketState,
                tickets = tickets,
                navController = navController,
                viewModel = viewModel)
        },
        mapTab = {
            MapTab(tickets = tickets)
        },
        favouriteTab = {
            FavouriteListTab(viewModel = viewModel, navController = navController)
        }
    )
}

@Composable
fun FavouriteListTab(viewModel: TicketViewModel, navController: NavController) {
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
        ShowList(tickets = favouriteList, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun TicketsListTab(
    searchQueryState: MutableState<String>,
    ticketState: Resource<List<Ticket>>,
    tickets: List<Ticket>,
    navController: NavController,
    viewModel: TicketViewModel
){
    SearchForm(searchQueryState){ searchQuery ->
        searchQueryState.value = searchQuery
    }
    if (ticketState.data == null){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(modifier = Modifier.then(Modifier.size(32.dp)))
        }
    }

    if(searchQueryState.value.isEmpty()){
        ShowList(tickets = tickets, navController = navController, viewModel = viewModel)
    }
    else{
        ShowList(tickets = tickets, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun MapTab(tickets: List<Ticket>){
    val markersList = mutableListOf<MarkerMap>()
    for (ticket in tickets){
        val marker = MarkerMap(
            position = LatLng(
                ticket.geometry.coordinates[1],
                ticket.geometry.coordinates[0]
            ),
            title = "Biletomat, ${ticket.properties.nazwa}"
        )
        markersList.add(marker)
    }
    MapComponent(markers = markersList)
}

@Composable
fun ShowList(tickets: List<Ticket>, navController: NavController, viewModel: TicketViewModel){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = tickets, key = {
            it.id
        }) { ticket ->
            TicketRow(ticket = ticket, navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun TicketRow(ticket: Ticket, navController: NavController, viewModel: TicketViewModel) {

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
                            val imageId = getIconByCategoryName(Constants.CATEGORIES[4])
                            Image(
                                painterResource(imageId),
                                contentDescription = "category icon",
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(50.dp),
                                colorFilter = ColorFilter.tint(Color.Black)
                            )
                            Text(
                                text = "Biletomat",
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
                                text = "Ul. " + ticket.properties.nazwa,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            val desc = HtmlCompat.fromHtml(ticket.properties.opis,HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                            Text(
                                text = desc.replace("\n", "").replace("...",""),
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
                            val distance = getDistanceInKm(ticket.geometry.coordinates[1], ticket.geometry.coordinates[0])
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
                ExpandedContent(ticket = ticket, navController = navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ExpandedContent(ticket: Ticket, navController: NavController, viewModel: TicketViewModel) {
    var isFavourite by remember { mutableStateOf(false) }
    isFavourite = viewModel.favouriteList.collectAsState().value.any { b -> b.id == ticket.id }

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

                PayMethodRow("Karta: ", if(ticket.properties.kartyPlatnicze != null) ticket.properties.kartyPlatnicze else "")
                PayMethodRow("PEKA: ", if(ticket.properties.systemPeka != null) ticket.properties.systemPeka else "")
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
                        val coordinates = ticket.geometry.coordinates
                        val lonFloat = coordinates[0].toFloat()
                        val latFloat = coordinates[1].toFloat()

                        navController.navigate(Screens.MapScreen.name + "/${latFloat}/${lonFloat}/Biletomat")
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
                            viewModel.removeTicket(ticket.toTicketEntity())
                            Toast.makeText(context, "Usunięto z ulubionych", Toast.LENGTH_SHORT).show()
                        }else{
                            viewModel.addTicket(ticket.toTicketEntity())
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
                        imageVector = if(isFavourite)Icons.Default.Close else Icons.Default.Star,
                        contentDescription = "star icon"
                    )
                }
            }
        }
    }
}
