package com.nikky.clientassignmnet.domain.usecase

import com.nikky.clientassignmnet.domain.model.Event
import com.nikky.clientassignmnet.domain.repository.EventRepository
import javax.inject.Inject

class ToggleBookmarkUseCase@Inject constructor(
    private val repository: EventRepository
)  {
    suspend fun toggleBookmark(event: Event) {
        repository.toggleBookmark(event)
    }

}