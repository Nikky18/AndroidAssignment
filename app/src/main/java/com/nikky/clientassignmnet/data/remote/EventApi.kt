package com.nikky.clientassignmnet.data.remote

import android.content.Context
import com.google.gson.Gson

class EventApi(private val context: Context) {

    private val gson = Gson()

    suspend fun getEvents(): List<EventDto> {
        try {
            val json = context.assets.open("events.json")
                .bufferedReader().use { it.readText() }

            return gson.fromJson(json, Array<EventDto>::class.java).toList()
        } catch (e: Exception) {
            return  emptyList()
        }
    }
}