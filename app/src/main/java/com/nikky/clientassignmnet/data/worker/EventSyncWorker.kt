package com.nikky.clientassignmnet.data.worker

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nikky.clientassignmnet.data.local.EventDatabase
import com.nikky.clientassignmnet.data.remote.EventApi
import com.nikky.clientassignmnet.data.repository.EventRepository

class EventSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("WORKER_TEST", "Sync started")
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(applicationContext, "Sync Running...", Toast.LENGTH_SHORT).show()
            }

            // Get repository manually
            val db = EventDatabase.getInstance(applicationContext)
            val dao = db.eventDao()
            val api = EventApi(applicationContext)
            val repo = EventRepository(api, dao)

            repo.refresh()
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(applicationContext, "Sync Success ✅", Toast.LENGTH_SHORT).show()
            }

            Log.d("WORKER_TEST", "Sync completed")

            Result.success()

        } catch (e: Exception) {
            Log.d("WORKER_TEST", "Sync failed: ${e.message}")
            Result.retry()
        }
    }
}