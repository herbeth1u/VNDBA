package com.booboot.vndbandroid.feature.explore.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.booboot.vndbandroid.feature.explore.ui.ExploreRoute

const val exploreRoute = "explore_route"

fun NavController.navigateToExplore(navOptions: NavOptions? = null) {
    navigate(exploreRoute, navOptions)
}

fun NavGraphBuilder.exploreScreen() {
    composable(route = exploreRoute) {
        ExploreRoute()
    }
}
