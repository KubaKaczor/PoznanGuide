package com.example.poznanhelper.screens.shops

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
import com.example.poznanhelper.model.antique.Antique
import com.example.poznanhelper.model.shop.Shop
import com.example.poznanhelper.navigation.Screens
import com.example.poznanhelper.screens.antique.*
import com.example.poznanhelper.screens.antique.AntiqueContent
import com.example.poznanhelper.ui.theme.green
import com.example.poznanhelper.utils.Constants
import com.example.poznanhelper.utils.calculateTimeToTarget
import com.example.poznanhelper.utils.getDistanceInKm
import com.example.poznanhelper.utils.getIconByCategoryName
import com.google.android.gms.maps.model.LatLng

@Composable
fun ShopScreen(navController: NavController, viewModel: ShopViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
            GuideAppBar(
                navController = navController,
                isHome = false
            )
        }
    ) {
        ShopContent(navController = navController, viewModel = viewModel)
    }
}

@Composable
fun ShopContent(navController: NavController, viewModel: ShopViewModel) {
    var shops = remember { listOf<Shop>() }
    val searchQueryState = rememberSaveable { mutableStateOf("") }

    val shopState = produceState<Resource<List<Shop>>>(initialValue = Resource.Loading()){
        value = viewModel.getShops()
    }.value

    if(shopState.data != null) {
        shops = shopState.data!!

        if(searchQueryState.value.isNotEmpty()){
            shops = shops.filter {
                it.properties.adres.lowercase().contains(searchQueryState.value.lowercase())
            }
        }
    }

    TabsContent(
        listTab = {
            ShopsListTab(
                searchQueryState = searchQueryState,
                shopState = shopState,
                shops = shops,
                navController = navController,
                viewModel = viewModel)
        },
        mapTab = {
            MapTab(shops = shops)
        },
        favouriteTab = {
            FavouriteListTab(viewModel = viewModel, navController = navController)
        }
    )
}

@Composable
fun FavouriteListTab(viewModel: ShopViewModel, navController: NavController) {
    val favouriteList = viewModel.favouriteList.collectAsState().value

    if(favouriteList.isEmpty()){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Brak ulubionych punktów sprzedaży biletów")
        }
    }else{
        ShowList(shops = favouriteList, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun ShopsListTab(
    searchQueryState: MutableState<String>,
    shopState: Resource<List<Shop>>,
    shops: List<Shop>,
    navController: NavController,
    viewModel: ShopViewModel
){
    SearchForm(searchQueryState = searchQueryState, placeholder = "Podaj ulicę"){ searchQuery ->
        searchQueryState.value = searchQuery
    }
    if (shopState.data == null){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(modifier = Modifier.then(Modifier.size(32.dp)))
        }
    }

    if(searchQueryState.value.isEmpty()){
        ShowList(shops = shops, navController = navController, viewModel = viewModel)
    }
    else{
        ShowList(shops = shops, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun MapTab(shops: List<Shop>){
    val markersList = mutableListOf<MarkerMap>()
    for (shop in shops){
        val marker = MarkerMap(
            position = LatLng(
                shop.geometry.coordinates[1].toString().toDouble(),
                shop.geometry.coordinates[0].toString().toDouble()
            ),
            title = "Punkt sprzedaży biletów, ${shop.properties.adres}"
        )
        markersList.add(marker)
    }
    MapComponent(markers = markersList)
}

@Composable
fun ShowList(shops: List<Shop>, navController: NavController, viewModel: ShopViewModel){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = shops, key = {
            it.id
        }) { shop ->
            ShopRow(shop = shop, navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun ShopRow(shop: Shop, navController: NavController, viewModel: ShopViewModel) {

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
                            val imageId = getIconByCategoryName(Constants.CATEGORIES[2])
                            Image(
                                painterResource(imageId),
                                contentDescription = "category icon",
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(50.dp),
                                colorFilter = ColorFilter.tint(Color.Black)
                            )
                            Text(
                                text = "Bilety",
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
                                text = shop.properties.nazwa,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = shop.properties.adres,
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
                            val distance = getDistanceInKm(
                                shop.geometry.coordinates[1].toString().toDouble(),
                                shop.geometry.coordinates[0].toString().toDouble()
                            )
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
                ExpandedContent(shop = shop, navController = navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ExpandedContent(shop: Shop, navController: NavController, viewModel: ShopViewModel) {
    var isFavourite by remember { mutableStateOf(false) }
    isFavourite = viewModel.favouriteList.collectAsState().value.any { b -> b.id == shop.id }

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
                if(shop.properties.opis.length > 4){
                    val desc = HtmlCompat.fromHtml(shop.properties.opis, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                    Text(text = desc.replace("\n", "").replace("...",""))
                }


                Text(text = "Godziny otwarcia: \n${shop.properties.godziny}")
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Możliwe płatności: \n${shop.properties.sposobyPlat}")
                Spacer(modifier = Modifier.height(8.dp))

                Row(){
                    Text(
                        text = "Bilet jednorazowy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    val green = Color(0xFF228B22)
                    Icon(
                        imageVector = if(shop.properties.biletJednorazowy == "1") Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if(shop.properties.biletJednorazowy == "1") green else Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
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
                        val coordinates = shop.geometry.coordinates
                        val lonFloat = coordinates[0].toString().toFloat()
                        val latFloat = coordinates[1].toString().toFloat()

                        navController.navigate(Screens.MapScreen.name + "/${latFloat}/${lonFloat}/Punkt sprzedaży biletów")
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
                            viewModel.removeShop(shop.toShopEntity())
                            Toast.makeText(context, "Usunięto z ulubionych", Toast.LENGTH_SHORT).show()
                        }else{
                            viewModel.addShop(shop.toShopEntity())
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