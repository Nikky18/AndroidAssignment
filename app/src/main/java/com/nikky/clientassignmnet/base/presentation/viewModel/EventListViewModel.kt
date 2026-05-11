package com.nikky.clientassignmnet.base.presentation.viewModel

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikky.clientassignmnet.services.LocationHelper
import com.nikky.clientassignmnet.domain.model.Event
import com.nikky.clientassignmnet.domain.usecase.GetEventsUseCase
import com.nikky.clientassignmnet.domain.usecase.RefreshEventsUseCase
import com.nikky.clientassignmnet.domain.usecase.ToggleBookmarkUseCase
import com.nikky.clientassignmnet.utils.DistanceCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val refreshEventsUseCase: RefreshEventsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val locationHelper: LocationHelper
) : ViewModel() {

    val events = getEventsUseCase.getEvents().stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())
    val isLoading = MutableStateFlow(false)

    val userLocation = MutableStateFlow<Location?>(null)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isLoading.value = true
            refreshEventsUseCase.refresh()
            isLoading.value = false
        }
    }

    fun isLocationEnabled(): Boolean {
        return locationHelper.isLocationEnabled()
    }

    fun fetchLocation() {
        viewModelScope.launch {
            userLocation.value = locationHelper.getCurrentLocation()
        }
    }

    fun bookmark(event: Event) {
        viewModelScope.launch {
            toggleBookmarkUseCase.toggleBookmark(event)
        }
    }

    fun getEventDistanceInKm(event: Event): String {
        val loc = userLocation.value ?: return "Location unavailable"

        val distance = DistanceCalculator.calculateDistance(
            loc.latitude,
            loc.longitude,
            event.lat,
            event.lng
        )

        return String.format("%.1f km away", distance)
    }

}