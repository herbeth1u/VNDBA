/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.booboot.vndbandroid.app.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.booboot.vndbandroid.app.navigation.TopLevelDestination
import com.booboot.vndbandroid.app.navigation.TopLevelDestination.EXPLORE
import com.booboot.vndbandroid.app.navigation.TopLevelDestination.SEARCH
import com.booboot.vndbandroid.app.navigation.TopLevelDestination.VN_LIST
import com.booboot.vndbandroid.feature.explore.navigation.navigateToExplore
import com.booboot.vndbandroid.feature.search.navigation.navigateToSearch
import com.booboot.vndbandroid.feature.vnlist.navigation.navigateToVnList
import com.booboot.vndbandroid.feature.vnlist.navigation.vnListRoute
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(navController, coroutineScope, windowSizeClass) {
        AppState(navController, windowSizeClass)
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    val startDestination: String = vnListRoute,
) {
    val currentDestinationState: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    /**
     * Ordered list of top level destinations to be used in the BottomBar and NavRail.
     */
    val topLevelDestinations = TopLevelDestination.values().asList().toImmutableList()

    /**
     * Map of top level destinations indexed on [TopLevelDestination.route].
     * Compared to [topLevelDestinations], increases performance of [currentTopLevelDestinationState]
     * when looping through the top level destinations to find an element.
     */
    private val topLevelDestinationsMap =
        topLevelDestinations.associateBy { it.route }.toImmutableMap()

    /**
     * Current top level stack as selected in the BottomBar.
     * This is never null (a top level destination must be selected at all time).
     * Loops through the back stack from the most recent to the least recent screen and select the
     * first route that matches a top level destination.
     * N.B.: If you want to navigate to a top level destination screen without switching the current
     * stack (i.e. without changing the selected BottomBar item), please navigate using an alternative
     * route to the screen.
     */
    val currentTopLevelDestination: TopLevelDestination
        get() = navController.backQueue.asReversed().find { navBackStackEntry ->
            topLevelDestinationsMap[navBackStackEntry.destination.route] != null
        }?.let {
            topLevelDestinationsMap[it.destination.route]
        } ?: topLevelDestinationsMap[startDestination] ?: topLevelDestinations[0]

    /**
     * Same as [currentTopLevelDestination], except it's a [Composable] state so it updates
     * automatically and recomposes anytime the back stack changes.
     */
    val currentTopLevelDestinationState: TopLevelDestination
        @Composable get() {
            currentDestinationState // Recomposes the state when the back stack changes
            return currentTopLevelDestination
        }

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                // Save state when going back to a previously selected item
                // When it's already the currently selected item: DON'T save state to
                // quickly go back to the top level destination
                saveState = topLevelDestination != currentTopLevelDestination
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state (only works when saveState=true)
            restoreState = true
        }

        when (topLevelDestination) {
            VN_LIST -> navController.navigateToVnList(topLevelNavOptions)
            SEARCH -> navController.navigateToSearch(topLevelNavOptions)
            EXPLORE -> navController.navigateToExplore(topLevelNavOptions)
        }
    }
}
