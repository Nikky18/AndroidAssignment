package com.nikky.clientassignmnet.base.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikky.clientassignmnet.domain.model.Event
import com.nikky.clientassignmnet.domain.usecase.GetBookmarkedEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val getBookmarkedEventsUseCase: GetBookmarkedEventsUseCase
) : ViewModel() {
    val bookmarkedEvents = MutableStateFlow<List<Event>>(emptyList())
    val isLoading = MutableStateFlow(false)

    fun getBookMarkedEvent() {
        viewModelScope.launch {
            isLoading.value = true
            getBookmarkedEventsUseCase().collect { list ->
                bookmarkedEvents.value = list
                isLoading.value = false
            }
        }
    }
}