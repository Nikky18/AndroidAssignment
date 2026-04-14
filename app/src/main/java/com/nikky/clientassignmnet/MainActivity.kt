package com.nikky.clientassignmnet

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.nikky.clientassignmnet.data.worker.EventSyncWorker
import com.nikky.clientassignmnet.ui.theme.ClientAssignmnetTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleWork(this)
        enableEdgeToEdge()
        setContent {
            ClientAssignmnetTheme {
                MainApp()
            }
        }
    }
}

/* ******************************************** Schedule Worker ********************************* */
fun scheduleWork(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<EventSyncWorker>(
        6, TimeUnit.HOURS // low frequency
    )
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "event_sync",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}