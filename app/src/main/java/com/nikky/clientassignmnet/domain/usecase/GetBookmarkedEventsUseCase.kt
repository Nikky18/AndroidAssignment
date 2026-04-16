package com.nikky.clientassignmnet.domain.usecase

import com.nikky.clientassignmnet.domain.model.Event
import com.nikky.clientassignmnet.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkedEventsUseCase @Inject constructor(
    private val repository: EventRepository
)  {
    operator fun invoke(): Flow<List<Event>> {
        return  repository.getBookmarkedEvents()
    }

}