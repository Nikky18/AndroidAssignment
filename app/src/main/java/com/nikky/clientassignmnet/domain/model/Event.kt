package com.nikky.clientassignmnet.domain.model

data class Event(
    val id: String,
    val title: String,
    val location: String,
    val time: Long,
    val imageUrl: String,
    val isBookmarked: Boolean = false,
    val lat: Double,
    val lng: Double
)