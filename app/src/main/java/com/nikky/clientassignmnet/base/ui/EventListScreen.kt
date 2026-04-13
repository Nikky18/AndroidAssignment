package com.nikky.clientassignmnet.base.ui

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nikky.clientassignmnet.NavDest
import com.nikky.clientassignmnet.base.ui.common.TopBarView
import com.nikky.clientassignmnet.data.local.EventEntity

@Composable
fun EventListScreen(
    navController: NavController,
    vm: EventViewModel = hiltViewModel()
) {

    val events = vm.events.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarView(
                title = "Events",
                showLeftIcon = false,
                leftIcon = Icons.Default.Menu
            ) {

            }
        },
        snackbarHost = {},
    ) {
        Column (
            modifier = Modifier.fillMaxSize().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                items( events.value) { event ->
                    EventItem(event, onClick = {
                        navController.navigate(NavDest.EventDetail.route)
                    }, onBookmark = {
                        vm.bookmark(event)
                    })
                }
            }

        }
    }

}

@Composable
fun EventItem(
    event: EventEntity,
    onClick: () -> Unit,
    onBookmark: () -> Unit
) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable { onClick() }) {

        Row {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Column(Modifier.weight(1f)) {
                Text(event.title)
                Text(event.location)
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