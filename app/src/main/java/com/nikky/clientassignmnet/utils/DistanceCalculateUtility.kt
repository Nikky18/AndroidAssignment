package com.nikky.clientassignmnet.utils

import android.location.Location

object DistanceCalculateUtility {
    fun calculateDistance(
        userLat: Double,
        userLng: Double,
        eventLat: Double,
        eventLng: Double
    ): Float {
        val result = FloatArray(1)
        Location.distanceBetween(
            userLat,
            userLng,
            eventLat,
            eventLng,
            result
        )
        return result[0] / 1000 // convert meters → KM
    }
}