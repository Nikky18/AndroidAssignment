package com.nikky.clientassignmnet


sealed class NavDest(val route: String) {
    object EventList : NavDest("eventList")
    object EventDetail : NavDest("eventDetail")
    object Bookmark : NavDest("bookmark")
}