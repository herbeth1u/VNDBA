package com.booboot.vndbandroid.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.booboot.vndbandroid.app.ui.AppState
import com.booboot.vndbandroid.feature.explore.navigation.exploreScreen
import com.booboot.vndbandroid.feature.search.navigation.searchScreen
import com.booboot.vndbandroid.feature.vnlist.navigation.vnListRoute
import com.booboot.vndbandroid.feature.vnlist.navigation.vnListScreen

/**
 * Top-level navigation graph. Orchestrates the navigation between all the features.
 *
 * @param navController The [AppState]'s [NavHostController]
 * @param modifier
 * @param startDestination The first screen opened when the app starts.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = vnListRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        vnListScreen()
        searchScreen()
        exploreScreen()
    }
}
