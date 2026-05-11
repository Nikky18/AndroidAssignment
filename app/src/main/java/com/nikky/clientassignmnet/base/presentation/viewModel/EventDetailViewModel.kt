package com.nikky.clientassignmnet.base.presentation.viewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.nikky.clientassignmnet.data.local.EventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor() : ViewModel() {
    val isLoading = MutableStateFlow(false)
    val eventDetail = MutableStateFlow<EventEntity?>(null)

    fun setEvent(event: EventEntity?) {
        isLoading.value = true
        eventDetail.value = event
        isLoading.value = false
    }

    fun showLocationOnMap(context: Context){
        val uri = Uri.parse("geo:${eventDetail.value?.lat},${eventDetail.value?.lng}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}