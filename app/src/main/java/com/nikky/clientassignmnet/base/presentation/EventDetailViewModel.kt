package com.nikky.clientassignmnet.base.presentation

import androidx.lifecycle.ViewModel
import com.nikky.clientassignmnet.data.local.EventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
) : ViewModel() {
    val isLoading = MutableStateFlow(false)
    val eventDetail = MutableStateFlow<EventEntity?>(null)

    fun setEvent(event: EventEntity?) {
        isLoading.value = true
        eventDetail.value = event
        isLoading.value = false
    }
}