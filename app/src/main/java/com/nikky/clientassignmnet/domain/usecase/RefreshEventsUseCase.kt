package com.nikky.clientassignmnet.domain.usecase

import com.nikky.clientassignmnet.domain.repository.EventRepository
import javax.inject.Inject

class RefreshEventsUseCase @Inject constructor(
    private val repository: EventRepository
)  {
    suspend fun refresh() {
          repository.refresh()
    }

}