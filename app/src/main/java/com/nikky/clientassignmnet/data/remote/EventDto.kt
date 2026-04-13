package com.nikky.clientassignmnet.data.remote

import com.nikky.clientassignmnet.data.local.EventEntity

data class EventDto(
    val id: String,
    val title: String,
    val location: String,
    val time: Long,
    val imageUrl: String,
    val lat: Double,
    val lng: Double
)

fun EventDto.toEntity() = EventEntity(
    id = id,
    title = title,
    location = location,
    time = time,
    imageUrl = imageUrl,
    lat = lat,
    lng = lng,
    isBookmarked = false
)