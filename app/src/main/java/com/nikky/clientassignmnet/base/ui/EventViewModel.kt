package com.nikky.clientassignmnet.base.ui

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikky.clientassignmnet.base.ui.common.LocationHelper
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repo: EventRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {

    val events = repo.events.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val isLoading = MutableStateFlow(false)

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation = _userLocation.asStateFlow()

    init {
        refresh()
    }

    fun isLocationEnabled(context: Context): Boolean {
        return locationHelper.isLocationEnabled(context)
    }

    fun fetchLocation() {
        viewModelScope.launch {
            _userLocation.value = locationHelper.getCurrentLocation()
        }
    }

    private fun refresh() {
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