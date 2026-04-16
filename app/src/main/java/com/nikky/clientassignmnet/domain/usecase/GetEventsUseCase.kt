package com.nikky.clientassignmnet.domain.usecase

import com.nikky.clientassignmnet.domain.model.Event
import com.nikky.clientassignmnet.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {

    fun getEvents(): Flow<List<Event>> {
        return  repository.getEvents()
    }

}