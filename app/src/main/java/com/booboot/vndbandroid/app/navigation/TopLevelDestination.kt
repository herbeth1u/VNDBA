package com.booboot.vndbandroid.app.navigation

import com.booboot.vndbandroid.core.designsystem.icon.AppIcons
import com.booboot.vndbandroid.core.designsystem.icon.Icon
import com.booboot.vndbandroid.feature.explore.navigation.exploreRoute
import com.booboot.vndbandroid.feature.search.navigation.searchRoute
import com.booboot.vndbandroid.feature.vnlist.navigation.vnListRoute
import com.booboot.vndbandroid.feature.explore.R as exploreR
import com.booboot.vndbandroid.feature.search.R as searchR
import com.booboot.vndbandroid.feature.vnlist.R as vnListR

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val route: String,
) {
    VN_LIST(
        selectedIcon = AppIcons.ListFilled,
        unselectedIcon = AppIcons.List,
        iconTextId = vnListR.string.nav_bar_vn_list,
        route = vnListRoute,
    ),
    SEARCH(
        selectedIcon = AppIcons.SearchFilled,
        unselectedIcon = AppIcons.Search,
        iconTextId = searchR.string.nav_bar_search,
        route = searchRoute,
    ),
    EXPLORE(
        selectedIcon = AppIcons.ExploreFilled,
        unselectedIcon = AppIcons.Explore,
        iconTextId = exploreR.string.nav_bar_explore,
        route = exploreRoute,
    ),
}
