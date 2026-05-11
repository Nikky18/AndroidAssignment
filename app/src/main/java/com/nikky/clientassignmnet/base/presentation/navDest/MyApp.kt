package com.nikky.clientassignmnet.base.presentation.navDest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nikky.clientassignmnet.base.presentation.screens.BookmarkScreen
import com.nikky.clientassignmnet.base.presentation.screens.EventDetailScreen
import com.nikky.clientassignmnet.base.presentation.screens.EventListScreen

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