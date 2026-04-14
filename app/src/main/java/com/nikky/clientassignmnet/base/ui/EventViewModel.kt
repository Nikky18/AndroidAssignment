package com.nikky.clientassignmnet.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {

    val events = repo.events.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val isLoading = MutableStateFlow(false)


    init {
        viewModelScope.launch {
            repo.refresh()
        }
    }

    fun bookmark(event: EventEntity) {
        viewModelScope.launch {
            repo.toggleBookmark(event)
        }
    }
}