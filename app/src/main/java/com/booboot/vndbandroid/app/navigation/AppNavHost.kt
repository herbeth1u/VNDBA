package com.booboot.vndbandroid.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.booboot.vndbandroid.app.ui.AppState

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
    startDestination: String = "hello_world", // TODO start destination
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        // TODO add destinations
        composable(route = "hello_world") {
            Text(text = "Hello world")
        }
    }
}
