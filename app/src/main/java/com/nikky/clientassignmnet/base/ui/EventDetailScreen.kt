package com.nikky.clientassignmnet.base.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.nikky.clientassignmnet.base.ui.common.TopBarView
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.worker.EventSyncWorker
import java.util.UUID

@Composable
fun EventDetailScreen(
    navController: NavController,
    vm: EventDetailViewModel = hiltViewModel()
) {
    val json = navController.previousBackStackEntry?.savedStateHandle?.get<String>("event_json")

    val context = LocalContext.current
    val event = vm.eventDetail.collectAsState()

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted -> }

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
                title = event.value?.title ?: "OOPS",
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
                Text("Trigger Worker")
            }
        }
    ) {
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
                            .height(300.dp),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            event.value?.title ?: "Oops",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "📍${event.value?.location}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = "Event Details",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Latitude: ${event.value?.lat}")
                                Text("Longitude: ${event.value?.lng}")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                val permissionStatus = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )

                                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                                    val uri = Uri.parse("geo:${event.value?.lat},${event.value?.lng}")
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    context.startActivity(intent)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                }
                            },
                        ) {
                            Text("Open in Maps")
                        }
                    }

                }
            } else {
                Text("Something Went Wrong",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }

        }
    }
}