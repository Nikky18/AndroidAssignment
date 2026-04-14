package com.nikky.clientassignmnet.base.ui

import androidx.lifecycle.ViewModel
import com.nikky.clientassignmnet.data.local.EventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
) : ViewModel() {
    val eventDetail = MutableStateFlow<EventEntity?>(null)


    fun setEvent(event: EventEntity?) {
        eventDetail.value = event
    }
}