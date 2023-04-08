package com.booboot.vndbandroid.feature.vnlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.booboot.vndbandroid.feature.vnlist.ui.VnListRoute

const val vnListRoute = "vn_list_route"

fun NavController.navigateToVnList(navOptions: NavOptions? = null) {
    navigate(vnListRoute, navOptions)
}

fun NavGraphBuilder.vnListScreen() {
    composable(route = vnListRoute) {
        VnListRoute()
    }
}
