package com.example.poznanhelper.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.poznanhelper.R
import com.example.poznanhelper.model.MarkerMap
import com.example.poznanhelper.utils.UserLocation
import com.example.poznanhelper.utils.bitmapDescriptorFromVector
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun GuideAppBar(
    navController: NavController,
    isHome: Boolean
){
    val showDialog = remember{ mutableStateOf(false) }
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(!isHome){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow back",
                        tint = Blue,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                }

                Image(
                    painterResource(id = R.drawable.logo_poznan),
                    contentDescription = "appBar logo",
                    colorFilter = ColorFilter.tint(Blue)

                )
            }
        },
        actions = {
                  if(isHome){
                      IconButton(onClick = {
                          showDialog.value = true
                      }) {
                          Icon(
                              imageVector = Icons.Default.Info,
                              contentDescription = "info icon",
                              tint = Blue
                          )

                      }
                  }
        },
        elevation = 32.dp,
        backgroundColor = White
    )

    if(showDialog.value){
        ShowInfoDialog(showDialogState = showDialog)
    }
}

@Composable
fun ShowInfoDialog(showDialogState: MutableState<Boolean>){
    Dialog(
        onDismissRequest = { showDialogState.value = false}
    ) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 8.dp)
                // .width(300.dp)
                // .height(164.dp)
                .background(
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(25.dp)
                )
                .verticalScroll(rememberScrollState())

        ) {
            Column(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.Start,

            ) {

                Text(
                    text = "O Aplikacji",
                    fontSize = 24.sp,
                    color = Gray,
                    fontWeight = FontWeight.Bold
                )
                
                Divider(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    color = Gray,
                    thickness = 1.dp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(text = "Aplikacja powstała przy użyciu danych z serwisu:")

                val context = LocalContext.current
                val site = "http://www.poznan.pl/api"
                val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(site)) }
                Text(
                    text = site,
                    textDecoration = TextDecoration.Underline,
                    color = Blue,
                    modifier = Modifier.clickable {
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Autor:")
                Text(
                    text = "Jakub Kaczorowski",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Black
                )
                TextButton(onClick = {
                    showDialogState.value = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                ) {
                    Text("OK", style = MaterialTheme.typography.subtitle1) }
                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    }

}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    placeholder: String,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    stopSearching: () -> Unit
){
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 15.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        placeholder = { Text(text = placeholder) },
        shape = CircleShape,
        trailingIcon = {
            if(valueState.value.isEmpty())
                Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
            else
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "search icon",
                    tint = Red,
                    modifier = Modifier.clickable {
                        valueState.value = ""
                        stopSearching.invoke()
                    }
                )
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    searchQueryState: MutableState<String>,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    placeholder: String = "Podaj nazwę ulicy lub regionu",
    onSearch: (String) -> Unit = {}
){
    Column(
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        InputField(valueState = searchQueryState,
            placeholder = placeholder,
            labelId = "Wyszukaj",
            enabled = true,
            onAction = KeyboardActions{
                keyboardController?.hide()
            }
        ){
            keyboardController?.hide()
        }
    }
}

@Composable
fun TabsContent(
    listTab: @Composable () -> Unit,
    mapTab: @Composable () -> Unit,
    favouriteTab: @Composable () -> Unit,
){
    val activeTab = remember { mutableStateOf("Lista") }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabsRow(activeTab = activeTab){
            activeTab.value = it
        }

        when(activeTab.value){
            "Lista" ->{
                listTab()
            }
            "Mapa" ->{
                mapTab()
            }
            "Ulubione" -> {
                favouriteTab()
            }
        }
    }
}

@Composable
fun MapComponent(markers :List<MarkerMap>) {
    val userLocation = LatLng(UserLocation.latitude.value, UserLocation.longitude.value)
    val userLocationState = MarkerState(position = userLocation)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(userLocation, 17f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){
        Marker(
            state = userLocationState,
            title = "Twoja pozycja",
            icon = bitmapDescriptorFromVector(LocalContext.current, R.drawable.current_position)
        )

        for(marker in markers){
            Marker(
                state = rememberMarkerState(position = marker.position),
                title = marker.title
            )
        }
    }
}

@Composable
fun TabButton(
    text: String,
    activeTab: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
){
    val lightBlue = Color(0xFF4169E1)
    Button(
        onClick = { onClick(text) },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxHeight(),
        elevation = null,
        colors = if(text == activeTab) ButtonDefaults.buttonColors(
            backgroundColor = lightBlue,
            contentColor = androidx.compose.ui.graphics.Color.White
        )else ButtonDefaults.buttonColors(
            backgroundColor = androidx.compose.ui.graphics.Color.LightGray,
            contentColor = androidx.compose.ui.graphics.Color.White
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TabsRow(
    activeTab: State<String>,
    onClickTab: (String) -> Unit
){

    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(androidx.compose.ui.graphics.Color.LightGray)
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabButton(text = "Lista", activeTab = activeTab.value, modifier = Modifier.weight(1f)){
            onClickTab(it)
        }
        TabButton(text = "Mapa", activeTab = activeTab.value, modifier = Modifier.weight(1f))
        {
            onClickTab(it)
        }
        TabButton(text = "Ulubione", activeTab = activeTab.value, modifier = Modifier.weight(1f))
        {
            onClickTab(it)
        }
    }
}

@Composable
fun PayMethodRow(payMethod: String, available: String){
    val green = Color(0xFF228B22)
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
            text = payMethod,
            modifier = Modifier.padding(horizontal = 4.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
        Icon(
            imageVector = if(available == "TAK" || available == "1") Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if(available == "TAK" || available == "1") green else Red,
            modifier = Modifier.size(20.dp)
        )
    }
}