package com.nikky.clientassignmnet.base.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.gson.Gson
import com.nikky.clientassignmnet.NavDest
import com.nikky.clientassignmnet.R
import com.nikky.clientassignmnet.base.presentation.common.LoadingView
import com.nikky.clientassignmnet.base.presentation.common.TopBarView
import com.nikky.clientassignmnet.domain.model.Event
import com.nikky.clientassignmnet.utils.DateFormatter

@Composable
fun EventListScreen(
    navController: NavController,
    vm: EventListViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val events = vm.events.collectAsState()
    val isLoading = vm.isLoading.collectAsState()

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
                Row(Modifier.padding(2.dp)) {
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
                        EventItem(event, context, vm, onClick = {
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
    event: Event,
    context: Context,
    vm: EventListViewModel,
    onClick: () -> Unit,
    onBookmark: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }) {

        Row {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(event.imageUrl)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .placeholder(R.drawable.offline_24)
                    .error(R.drawable.outline_error_24)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(event.title)
                Text(event.location)
                Text(DateFormatter.formatTime(event.time))
                Text("${vm.getEventDistanceInKm(event)}")
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