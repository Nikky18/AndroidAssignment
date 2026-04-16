package com.nikky.clientassignmnet.data.repository

import android.util.Log
import com.nikky.clientassignmnet.data.local.EventDao
import com.nikky.clientassignmnet.data.remote.EventApi
import com.nikky.clientassignmnet.data.mapper.*
import com.nikky.clientassignmnet.domain.model.Event
import com.nikky.clientassignmnet.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventApi,
    private val dao: EventDao
) : EventRepository {

    override fun getEvents(): Flow<List<Event>> {
        return dao.getEvents().map { events ->
            events.map { it.toDomain() }
        }
    }

    override fun getBookmarkedEvents(): Flow<List<Event>> {
        return dao.getBookmarkedEvents().map { events ->
            events.map { it.toDomain() }
        }
    }

    override suspend fun refresh() {
        try {
            val response = api.getEvents()
            val existing = dao.getEventsOnce()

            val entities = response.map { dto ->
                val old = existing.find { it.id == dto.id }

                dto.toDomain().copy(
                    isBookmarked = old?.isBookmarked ?: false
                ).toEntity()
            }

            dao.insertEvents(entities)

        } catch (e: Exception) {
            Log.d("REPO", e.message.toString())
        }
    }

    override suspend fun toggleBookmark(event: Event) {
        dao.updateBookmark(event.id, !event.isBookmarked)
    }

}