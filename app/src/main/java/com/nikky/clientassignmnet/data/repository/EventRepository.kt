package com.nikky.clientassignmnet.data.repository

import com.nikky.clientassignmnet.data.local.EventDao
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.remote.EventApi
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val api: EventApi,
    private val dao: EventDao
) {

    val events = dao.getEvents()

    val bookmarkedEvents = dao.getBookmarkedEvents()

    suspend fun refresh() {
        val response = api.getEvents()

        val entities = response.map {
            EventEntity(
                id = it.id,
                title = it.title,
                location = it.location,
                time = it.time,
                imageUrl = it.imageUrl,
                lat = it.lat,
                lng = it.lng
            )
        }

        dao.insertEvents(entities)
    }

    suspend fun toggleBookmark(event: EventEntity) {
        dao.updateBookmark(event.id, !event.isBookmarked)
    }
}