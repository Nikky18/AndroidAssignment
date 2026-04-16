package com.nikky.clientassignmnet

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nikky.clientassignmnet.base.presentation.BookmarkScreen
import com.nikky.clientassignmnet.base.presentation.EventDetailScreen
import com.nikky.clientassignmnet.base.presentation.EventListScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()
    MainNavGraph(navController)
}


@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDest.EventList.route,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavDest.EventList.route) {
            EventListScreen(navController)
        }

        composable(NavDest.Bookmark.route) {
            BookmarkScreen(navController)
        }

        composable(NavDest.EventDetail.route) { //backStack ->
            EventDetailScreen(navController)
        }

    }

}