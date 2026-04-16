package com.nikky.clientassignmnet.base.presentation

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
fun BookmarkScreen(
    navController: NavController,
    vm: BookMarkViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val bookmarkedEvents = vm.bookmarkedEvents.collectAsState()
    val isLoading = vm.isLoading.collectAsState()

    BackHandler {
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        vm.getBookMarkedEvent()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarView(
                title = "My BookMark",
                showLeftIcon = true,
                leftIcon = Icons.AutoMirrored.Filled.ArrowBack
            ) {
                navController.popBackStack()
            }
        },
        snackbarHost = {},
    ) {
        LoadingView(isLoading = isLoading.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bookmarkedEvents.value.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Bookmarked Events", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    LazyColumn {
                        items(bookmarkedEvents.value) { event ->
                            BookMarkedEventItem(context, event, onClick = {
                                val json = Gson().toJson(event)
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "event_json",
                                    json
                                )
                                navController.navigate(NavDest.EventDetail.route)
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookMarkedEventItem(
    context: Context,
    event: Event,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }) {

        Row {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data( event.imageUrl)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Column(Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)) {
                Text(event.title)
                Text(event.location)
                Text(DateFormatter.formatTime(event.time))
            }

            Icon(
                Icons.Default.Favorite,
                modifier = Modifier.padding(4.dp),
                contentDescription = null
            )
        }
    }
}