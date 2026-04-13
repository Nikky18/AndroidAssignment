package com.nikky.clientassignmnet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val location: String,
    val time: Long,
    val imageUrl: String,
    val isBookmarked: Boolean = false,
    val lat: Double,
    val lng: Double
)