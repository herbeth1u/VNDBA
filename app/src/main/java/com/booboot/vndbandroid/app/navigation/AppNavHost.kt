package com.booboot.vndbandroid.app.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.booboot.vndbandroid.app.ui.AppState
import com.booboot.vndbandroid.core.designsystem.component.BottomSafeSpacer
import com.booboot.vndbandroid.core.designsystem.component.TopSafeSpacer

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
        // TODO remove hello world and add real destinations
        composable(route = "hello_world") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                TopSafeSpacer()
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                Text(text = "Hello world")
                BottomSafeSpacer()
            }
        }
    }
}
