package com.booboot.vndbandroid.app.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import com.booboot.vndbandroid.app.navigation.TopLevelDestination

@Composable
fun rememberTestNavController(): TestNavHostController {
    val context = LocalContext.current
    val navController = remember {
        TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            graph = createGraph(startDestination = TopLevelDestination.VN_LIST.route) {
                composable(TopLevelDestination.VN_LIST.route) { }
                composable(TopLevelDestination.SEARCH.route) { }
                composable(TopLevelDestination.EXPLORE.route) { }
                composable(DESTINATION_FOO) { }
                composable(DESTINATION_BAR) { }
            }
        }
    }
    return navController
}

internal const val DESTINATION_FOO = "foo"
internal const val DESTINATION_BAR = "bar"