package com.nikky.clientassignmnet.domain.repository

import com.nikky.clientassignmnet.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    fun getEvents(): Flow<List<Event>>

    fun getBookmarkedEvents(): Flow<List<Event>>

    suspend fun refresh()

    suspend fun toggleBookmark(event: Event)
}