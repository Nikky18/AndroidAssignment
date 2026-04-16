package com.nikky.clientassignmnet.data.mapper

import com.nikky.clientassignmnet.data.local.EventEntity
import com.nikky.clientassignmnet.data.remote.EventDto
import com.nikky.clientassignmnet.domain.model.Event

// DTO → Domain
fun EventDto.toDomain() = Event(
    id = id,
    title = title,
    location = location,
    time = time,
    imageUrl = imageUrl,
    lat = lat,
    lng = lng,
    isBookmarked = false
)

// Entity → Domain
fun EventEntity.toDomain() = Event(
    id = id,
    title = title,
    location = location,
    time = time,
    imageUrl = imageUrl,
    lat = lat,
    lng = lng,
    isBookmarked = isBookmarked
)

// Domain → Entity
fun Event.toEntity() = EventEntity(
    id = id,
    title = title,
    location = location,
    time = time,
    imageUrl = imageUrl,
    lat = lat,
    lng = lng,
    isBookmarked = isBookmarked
)