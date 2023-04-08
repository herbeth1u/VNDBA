package com.booboot.vndbandroid.app.navigation

import com.booboot.vndbandroid.app.R
import com.booboot.vndbandroid.core.designsystem.icon.AppIcons
import com.booboot.vndbandroid.core.designsystem.icon.Icon

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
) {
    VN_LIST(
        selectedIcon = AppIcons.ListFilled,
        unselectedIcon = AppIcons.List,
        iconTextId = R.string.nav_bar_vn_list, // TODO move to :feature:vnlist
    ),
    SEARCH(
        selectedIcon = AppIcons.SearchFilled,
        unselectedIcon = AppIcons.Search,
        iconTextId = R.string.nav_bar_search, // TODO move to :feature:search
    ),
    EXPLORE(
        selectedIcon = AppIcons.ExploreFilled,
        unselectedIcon = AppIcons.Explore,
        iconTextId = R.string.nav_bar_explore,// TODO move to :feature:explore
    ),
}
