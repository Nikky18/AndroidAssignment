package com.nikky.clientassignmnet.data.repository

import android.util.Log
import com.nikky.clientassignmnet.data.local.EventDao
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.remote.EventApi
import javax.inject.Inject
import kotlin.math.log

class EventRepository @Inject constructor(
    private val api: EventApi,
    private val dao: EventDao
) {

    val events = dao.getEvents()

    val bookmarkedEvents = dao.getBookmarkedEvents()

    suspend fun refresh() {

        try {
            val response = api.getEvents()

            // Get existing data (cache)
            val existingEvents = dao.getEventsOnce()

            val entities = response.map { dto ->
                // preserve bookmark state
                val old = existingEvents.find { it.id == dto.id }
                EventEntity(
                    id = dto.id,
                    title = dto.title,
                    location = dto.location,
                    time = dto.time,
                    imageUrl = dto.imageUrl,
                    lat = dto.lat,
                    lng = dto.lng,
                    isBookmarked = old?.isBookmarked ?: false
                )
            }
            dao.insertEvents(entities)
        } catch (e: Exception) {
            Log.d("REPOSITORY_EXCEPTION", "${e.message}")
        }
    }

    suspend fun toggleBookmark(event: EventEntity) {
        dao.updateBookmark(event.id, !event.isBookmarked)
    }
}