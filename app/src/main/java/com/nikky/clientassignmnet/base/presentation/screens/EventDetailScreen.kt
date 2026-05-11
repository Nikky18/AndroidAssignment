package com.nikky.clientassignmnet.base.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.nikky.clientassignmnet.R
import com.nikky.clientassignmnet.base.presentation.common.LoadingView
import com.nikky.clientassignmnet.base.presentation.common.TopBarView
import com.nikky.clientassignmnet.base.presentation.viewModel.EventDetailViewModel
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.worker.EventSyncWorker
import com.nikky.clientassignmnet.ui.theme.Dimention

@Composable
fun EventDetailScreen(
    navController: NavController,
    vm: EventDetailViewModel = hiltViewModel()
) {
    val json = navController.previousBackStackEntry?.savedStateHandle?.get<String>("event_json")

    val context = LocalContext.current
    val event = vm.eventDetail.collectAsState()
    val isLoading = vm.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        val event = json?.let {
            Gson().fromJson(it, EventEntity::class.java)
        }
        vm.setEvent(event)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarView(
                title = event.value?.title ?: stringResource(R.string.event_detail),
                showLeftIcon = true,
                leftIcon = Icons.AutoMirrored.Filled.ArrowBack
            ) {
                navController.popBackStack()
            }
        },
        snackbarHost = {},
        bottomBar = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val request = OneTimeWorkRequestBuilder<EventSyncWorker>().build()
                    WorkManager.getInstance(context).enqueue(request)
                }
            ) {
                Text(stringResource(R.string.test_worker))
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
                if (event.value != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = event.value?.imageUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimention.Height_300),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(Dimention.Medium)
                        ) {
                            Text(
                                event.value?.title ?: stringResource(R.string.something_went_wrong),
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(Dimention.Small))
                            Text(
                                text = "📍${event.value?.location}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(Dimention.Medium)
                            ) {
                                Column(
                                    modifier = Modifier.padding(Dimention.Medium)
                                ) {

                                    Text(
                                        text = stringResource(R.string.event_detail),
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(Dimention.Height_8))

                                    Text("Latitude: ${event.value?.lat}")
                                    Text("Longitude: ${event.value?.lng}")
                                }
                            }

                            Spacer(modifier = Modifier.height(Dimention.Height_8))

                            Button(
                                onClick = {
                                    vm.showLocationOnMap(context)
                                },
                            ) {
                                Text(stringResource(R.string.open_in_maps))
                            }
                        }

                    }
                } else {
                    Text(
                        stringResource(R.string.something_went_wrong),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}