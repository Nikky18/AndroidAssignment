package com.nikky.clientassignmnet.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {
    val bookmarkedEvents = MutableStateFlow<List<EventEntity>>(emptyList())
    val isLoading = MutableStateFlow(false)

    fun getBookMarkedEvent() {
        viewModelScope.launch {
            isLoading.value = true
            repo.bookmarkedEvents.collect {
                bookmarkedEvents.value = it
                isLoading.value = false
            }
        }
    }
}