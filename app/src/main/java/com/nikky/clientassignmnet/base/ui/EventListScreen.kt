package com.nikky.clientassignmnet.base.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.nikky.clientassignmnet.NavDest
import com.nikky.clientassignmnet.base.ui.common.LoadingView
import com.nikky.clientassignmnet.base.ui.common.TopBarView
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.utils.DateUtility
import com.nikky.clientassignmnet.utils.DistanceCalculateUtility.calculateDistance

@Composable
fun EventListScreen(
    navController: NavController,
    vm: EventViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val events = vm.events.collectAsState()
    val isLoading = vm.isLoading.collectAsState()
    val location = vm.userLocation.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (vm.isLocationEnabled(context)) {
                vm.fetchLocation()
            } else {
                Toast.makeText(context, "Enable location", Toast.LENGTH_SHORT).show()
            }
        }
    }


    LaunchedEffect(Unit) {
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (isPermissionGranted) {
            //Check GPS
            if (vm.isLocationEnabled(context)) {
                vm.fetchLocation()
            } else {
                Toast.makeText(context, "Please enable location", Toast.LENGTH_SHORT).show()
            }

        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    BackHandler {}

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarView(
                title = "All Events",
                showLeftIcon = false,
                leftIcon = Icons.Default.Menu
            ) {}
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavDest.Bookmark.route)
                }
            ) {
                Row (Modifier.padding(2.dp)){
                    Text("Show BookMarks", modifier = Modifier.padding(2.dp))
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Bookmarks"
                    )
                }

            }
        }
    ) {
        LoadingView(isLoading = isLoading.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn {
                    items(events.value) { event ->
                        EventItem(event, location.value, onClick = {
                            val json = Gson().toJson(event)
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "event_json",
                                json
                            )
                            navController.navigate(NavDest.EventDetail.route)
                        }, onBookmark = {
                            vm.bookmark(event)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(
    event: EventEntity,
    userLocation: Location?,
    onClick: () -> Unit,
    onBookmark: () -> Unit
) {
    val distance = userLocation?.let {
        calculateDistance(
            it.latitude,
            it.longitude,
            event.lat,
            event.lng
        )
    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }) {

        Row {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Column(Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)) {
                Text(event.title)
                Text(event.location)
                Text(DateUtility.formatTime(event.time))

                //TODO: Calculate distance took some time need to check  it
                if (userLocation == null) {
                    Column {
                        Text("Location unavailable")
                        /*Button(onClick = {
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        }) {
                            Text("Enable Location")
                        }*/
                    }
                }
                distance?.let {
                    Text(
                        text = String.format("%.1f km away", it),
                    )
                }
            }

            IconButton(onClick = onBookmark) {
                Icon(
                    if (event.isBookmarked)
                        Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}